package org.lessons.springilmiofotoalbum.api;

import jakarta.validation.Valid;
import org.lessons.springilmiofotoalbum.model.Message;
import org.lessons.springilmiofotoalbum.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/messages")
public class MessageRestController {

    @Autowired
    MessageRepository messageRepository;

    @PostMapping("/create")
    public Message sendMessage(
        @Valid @RequestBody Message message
    ) {
        return messageRepository.save(message);
    }

}
