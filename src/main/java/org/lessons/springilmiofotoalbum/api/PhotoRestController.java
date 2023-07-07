package org.lessons.springilmiofotoalbum.api;

import org.lessons.springilmiofotoalbum.model.Photo;
import org.lessons.springilmiofotoalbum.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/photos")
public class PhotoRestController {

    @Autowired
    PhotoService photoService;

    @GetMapping
    public List<Photo> getAll(
        @RequestParam Optional<String> keyword
    ) {
        return photoService.getAll(keyword);
    }

}
