package org.lessons.springilmiofotoalbum.repository;

import org.lessons.springilmiofotoalbum.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Integer> {
    //ricerca lista con filtro
    List<Photo> findByTitleContainingIgnoreCase(String keyword);

    //ricerca visibili
    List<Photo> findByVisibleTrue();

    //ricerca visibili con filtro
    List<Photo> findByVisibleTrueAndTitleContainingIgnoreCase(String keyword);

    //ricerca con id utente
    List<Photo> findByUserId(Integer id);

    //ricerca con id utente e filtro
    List<Photo> findByUserIdAndTitleContainingIgnoreCase(Integer id, String keyword);
}
