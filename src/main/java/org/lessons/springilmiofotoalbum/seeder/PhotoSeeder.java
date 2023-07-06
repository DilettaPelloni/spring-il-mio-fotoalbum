package org.lessons.springilmiofotoalbum.seeder;

import org.lessons.springilmiofotoalbum.model.Photo;
import org.lessons.springilmiofotoalbum.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Component
public class PhotoSeeder implements CommandLineRunner {

    @Autowired
    PhotoRepository photoRepository;

    @Override
    public void run(String... args) throws Exception {

        File directory = new ClassPathResource("photo_seeder").getFile();

        List<Photo> photos = new ArrayList<>();

        int counter = 0;
        for (File f : directory.listFiles()) {
            try {
                counter ++;
                Photo photo = new Photo();
                byte[] bytes = Files.readAllBytes(Path.of(f.getPath()));
                photo.setTitle("photo-"+counter);
                photo.setDescription("description-"+counter);
                photo.setImg(bytes);
                photos.add(photo);
            } catch (IOException e) {
                System.out.println("Non riesco a leggere il file");
            }
        }
        photoRepository.saveAll(photos);
    }
}
