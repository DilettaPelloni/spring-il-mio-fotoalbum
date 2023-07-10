package org.lessons.springilmiofotoalbum.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MainController {

    @GetMapping
    public String home(
        Authentication authentication,
        Model model
    ) {
        if(authentication != null){
            model.addAttribute("user", authentication.getName());
        }
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
