package org.lessons.springilmiofotoalbum.controller;

import org.lessons.springilmiofotoalbum.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/messages")
public class MessageController {

    @Autowired
    MessageRepository messageRepository;

    @GetMapping
    public String index(Model model){
        model.addAttribute("messages", messageRepository.findAll());
        return "/messages/index";
    }

}
