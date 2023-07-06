package org.lessons.springilmiofotoalbum.repository;

import org.lessons.springilmiofotoalbum.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, Integer> {

}
