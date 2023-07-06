package org.lessons.springilmiofotoalbum.controller;

import org.lessons.springilmiofotoalbum.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/photos")
public class PhotoController {

    @Autowired
    PhotoService photoService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("photos", photoService.getAll());
        return "/photos/index";
    }

}
