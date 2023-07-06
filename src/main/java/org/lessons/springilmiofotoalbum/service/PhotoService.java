package org.lessons.springilmiofotoalbum.service;

import org.lessons.springilmiofotoalbum.model.Photo;
import org.lessons.springilmiofotoalbum.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhotoService {

    @Autowired
    PhotoRepository photoRepository;

    public List<Photo> getAll() {
        return photoRepository.findAll();
    }

}
