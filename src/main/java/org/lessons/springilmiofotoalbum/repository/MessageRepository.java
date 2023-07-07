package org.lessons.springilmiofotoalbum.repository;

import org.lessons.springilmiofotoalbum.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Integer> {
}
