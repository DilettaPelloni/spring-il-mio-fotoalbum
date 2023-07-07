package org.lessons.springilmiofotoalbum.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MainController {

    @GetMapping
    public String home() {
        return "/generals/home";
    }

    @GetMapping("/login")
    public String login() {
        return "/generals/login";
    }

    @GetMapping("/logout")
    public String logout() {
        return "/generals/logout";
    }

}
