package org.lessons.springilmiofotoalbum.service;


import org.lessons.springilmiofotoalbum.dto.PhotoDto;
import org.lessons.springilmiofotoalbum.exceptions.PhotoNotFoundException;
import org.lessons.springilmiofotoalbum.exceptions.UserNotAllowedException;
import org.lessons.springilmiofotoalbum.model.Photo;
import org.lessons.springilmiofotoalbum.model.Role;
import org.lessons.springilmiofotoalbum.model.User;
import org.lessons.springilmiofotoalbum.repository.PhotoRepository;
import org.lessons.springilmiofotoalbum.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;
import javax.management.InvalidAttributeValueException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PhotoService {

    @Autowired
    PhotoRepository photoRepository;
    @Autowired
    UserRepository userRepository;

    //VERIFICA UTENTE --------------------------------------------------------------------------------
    //cerca una foto e verifica che appartenga all'utente loggato, poi restituisce la foto (per non fare doppie query nei metodi che verranno usati dopo di lui)
    public Photo userIsAllowed(Integer id, String email) throws UsernameNotFoundException, UserNotAllowedException, PhotoNotFoundException {
        Photo photo = getById(id);
        User user = getUserByEmail(email);
        if(isSuperAdmin(user) || photo.getUser().getId().equals(user.getId())) {
            return photo;
        } else {
            throw new UserNotAllowedException("You are not allowed to be here.");
        }
    }

    //CONVERTITORI DTO/PHOTO --------------------------------------------------------------------------------
    //restituisce un PhotoDto data una foto
    public PhotoDto fromPhotoToDto(Photo photo) {
        PhotoDto result = new PhotoDto();
        result.setId(photo.getId());
        result.setTitle(photo.getTitle());
        result.setDescription(photo.getDescription());
        result.setVisible(photo.isVisible());
        result.setCategories(photo.getCategories());
        return result;
    }
    //restituisce una foto dato un PhotoDto
    public Photo fromDtoToPhoto(PhotoDto photoDto) {
        Photo result = new Photo();
        result.setTitle(photoDto.getTitle());
        result.setDescription(photoDto.getDescription());
        result.setImg(fromMpfToBytes(photoDto.getImg()));
        result.setVisible(photoDto.isVisible());
        result.setCategories(photoDto.getCategories());
        return result;
    }

    //READ --------------------------------------------------------------------------------
    //restituisce tutte le foto, eventualmente filtrate
    public List<Photo> getAll(Optional<String> keyword) {
        if(keyword.isEmpty()){
            return photoRepository.findAll();
        } else {
            return photoRepository.findByTitleContainingIgnoreCase(keyword.get());
        }
    }

    //restituisce tutte le foto visibili, eventualmente filtrate (serve solo all'API)
    public List<Photo> getAllVisible(Optional<String> keyword) {
        if(keyword.isEmpty()){
            return photoRepository.findByVisibleTrue();
        } else {
            return photoRepository.findByVisibleTrueAndTitleContainingIgnoreCase(keyword.get());
        }
    }

    //restituisce tutte le foto dell'utente loggato (tutte se è il superadmin), eventualmente filtrate
    public List<Photo> getAllOfActiveUser(Optional<String> keyword, String email) throws UsernameNotFoundException{
        User user = getUserByEmail(email);
        if(isSuperAdmin(user)) {
            return getAll(keyword);
        } else {
            if(keyword.isEmpty()){
                return photoRepository.findByUserId(user.getId());
            } else {
                return photoRepository.findByUserIdAndTitleContainingIgnoreCase(user.getId(), keyword.get());
            }
        }
    }

    //restituisce una foto dato l'id
    public Photo getById(Integer id) throws PhotoNotFoundException {
        Optional<Photo> photo = photoRepository.findById(id);
        if(photo.isPresent()) {
            return photo.get();
        } else {
            throw new PhotoNotFoundException("Photo with id "+id+" not found");
        }
    }

    //restituisce un PhotoDto dato l'id
    public PhotoDto getDtoById(Integer id) throws PhotoNotFoundException {
        Photo photo = getById(id);
        return fromPhotoToDto(photo);
    }

    //CREATE --------------------------------------------------------------------------------
    public Photo create(PhotoDto photoDto, BindingResult bindingResult, String email) throws InvalidAttributeValueException, UsernameNotFoundException {
        Photo photo = fromDtoToPhoto(photoDto);
        return create(photo, bindingResult, email);
    }

    public Photo create(Photo photo, BindingResult bindingResult, String email) throws InvalidAttributeValueException, UsernameNotFoundException {
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
            photoToSave.setUser(getUserByEmail(email));
            return photoRepository.save(photoToSave);
        }
    }

    //UPDATE --------------------------------------------------------------------------------
    //aggiorna una foto partendo da un PhotoDto
    public Photo update(Integer id, PhotoDto photoDto, BindingResult bindingResult) throws PhotoNotFoundException, InvalidAttributeValueException {
        Photo photo = fromDtoToPhoto(photoDto);
        return update(id, photo, bindingResult);
    }
    //aggiorna una foto
    public Photo update(Integer id, Photo photo, BindingResult bindingResult) throws PhotoNotFoundException, InvalidAttributeValueException {
        Photo photoToEdit = getById(id);
        if(bindingResult.hasErrors()) {
            throw new InvalidAttributeValueException();
        } else {
            photoToEdit.setTitle(photo.getTitle());
            photoToEdit.setDescription(photo.getDescription());
            if(photo.getImg() != null) { photoToEdit.setImg(photo.getImg()); }
            photoToEdit.setVisible(photo.isVisible());
            photoToEdit.setCategories(photo.getCategories());
            return photoRepository.save(photoToEdit);
        }
    }

    //DELETE --------------------------------------------------------------------------------
    //elimina una foto dato l'id
    public void delete(Photo photo) throws PhotoNotFoundException{
        photoRepository.delete(photo);
    }

    //UTILITY --------------------------------------------------------------------------------
    private User getUserByEmail(String email) throws UsernameNotFoundException{
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()){
            throw new UsernameNotFoundException("user with email "+email+" not found");
        }
        return user.get();
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

    //restituisce vero se l'utente dato è il super admin
    private boolean isSuperAdmin(User user) {
        boolean flag = false;
        for (Role r:user.getRoles()) {
            if(r.getName().equals("SUPERADMIN")) {
                flag = true;
            }
        }
        return flag;
    }

}
