package org.lessons.springilmiofotoalbum.repository;

import org.lessons.springilmiofotoalbum.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findByName(String name);
}
