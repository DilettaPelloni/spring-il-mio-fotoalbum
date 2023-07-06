package org.lessons.springilmiofotoalbum.service;

import org.lessons.springilmiofotoalbum.dto.PhotoDto;
import org.lessons.springilmiofotoalbum.exceptions.PhotoNotFoundException;
import org.lessons.springilmiofotoalbum.model.Photo;
import org.lessons.springilmiofotoalbum.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import javax.management.InvalidAttributeValueException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class PhotoService {

    @Autowired
    PhotoRepository photoRepository;

    //READ --------------------------------------------------------------------------------
    public List<Photo> getAll(Optional<String> keyword) {
        if(keyword.isEmpty()){
            return photoRepository.findAll();
        } else {
            return photoRepository.findByTitleContainingIgnoreCase(keyword.get());
        }
    }

    public Photo getById(Integer id) throws PhotoNotFoundException {
        Optional<Photo> photo = photoRepository.findById(id);
        if(photo.isPresent()) {
            return photo.get();
        } else {
            throw new PhotoNotFoundException("Photo with id "+id+" not found");
        }
    }

    //CREATE --------------------------------------------------------------------------------
    public Photo create(PhotoDto photoDto, BindingResult bindingResult) throws InvalidAttributeValueException {
        Photo photo = fromDtoToPhoto(photoDto);
        return create(photo, bindingResult);
    }

    public Photo create(Photo photo, BindingResult bindingResult) throws InvalidAttributeValueException {
        if(photo.getImg() == null) { //se non c'è l'immagine sparo l'errore
            bindingResult.addError(new FieldError(
                    "photo",
                    "img",
                    photo.getImg(),
                    false,
                    null,
                    null,
                    "Image must not be null"
            ));
        }
        if(bindingResult.hasErrors()) { //se ci sono errori lancio un'eccezione
            throw new InvalidAttributeValueException();
        } else { //altrimenti salvo il nuovo elemento
            Photo photoToSave = new Photo();
            photoToSave.setTitle(photo.getTitle());
            photoToSave.setDescription(photo.getDescription());
            photoToSave.setImg(photo.getImg());
            photoToSave.setVisible(photo.isVisible());
            photoToSave.setCategories(photo.getCategories());
            return photoRepository.save(photoToSave);
        }
    }

    //UTILITY --------------------------------------------------------------------------------
    private Photo fromDtoToPhoto(PhotoDto photoDto) {
        Photo result = new Photo();
        result.setTitle(photoDto.getTitle());
        result.setDescription(photoDto.getDescription());
        result.setImg(fromMpfToBytes(photoDto.getImg()));
        result.setVisible(photoDto.isVisible());
        result.setCategories(photoDto.getCategories());
        return result;
    }

    private byte[] fromMpfToBytes(MultipartFile mpf) {
        byte[] bytes = null;
        if(mpf != null && !mpf.isEmpty()) {
            try {
                bytes = mpf.getBytes();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bytes;
    }
}
