package org.lessons.springilmiofotoalbum.api;

import jakarta.validation.Valid;
import org.lessons.springilmiofotoalbum.model.Message;
import org.lessons.springilmiofotoalbum.repository.MessageRepository;
import org.lessons.springilmiofotoalbum.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/messages")
public class MessageRestController {

    @Autowired
    MessageRepository messageRepository;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> sendMessage(
        @Valid @RequestBody Message message,
        BindingResult bindingResult
    ) {
        if(bindingResult.hasErrors()) {
            return new ResponseEntity<>(new ApiResponse<>(null, bindingResult.getAllErrors()), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ApiResponse<>(messageRepository.save(message)), HttpStatus.CREATED);
    }

}
