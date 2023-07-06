package org.lessons.springilmiofotoalbum.controller;

import org.lessons.springilmiofotoalbum.dto.PhotoDto;
import org.lessons.springilmiofotoalbum.repository.CategoryRepository;
import org.lessons.springilmiofotoalbum.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Optional;

@Controller
@RequestMapping("/admin/photos")
public class PhotoController {

    @Autowired
    PhotoService photoService;
    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping
    public String index(
        Model model,
        @RequestParam Optional<String> keyword
    ) {
        model.addAttribute("photos", photoService.getAll(keyword));
        if(keyword.isPresent()) {
            model.addAttribute("keyword", keyword.get());
        }
        return "/photos/index";
    }

    @GetMapping("/create")
    public String create(
        Model model
    ) {
        model.addAttribute("catList", categoryRepository.findAll());
        model.addAttribute("photo", new PhotoDto());
        return "/photos/editor";
    }

}
