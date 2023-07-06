package org.lessons.springilmiofotoalbum.service;

import org.lessons.springilmiofotoalbum.exceptions.PhotoNotFoundException;
import org.lessons.springilmiofotoalbum.model.Photo;
import org.lessons.springilmiofotoalbum.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PhotoService {

    @Autowired
    PhotoRepository photoRepository;

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

}
