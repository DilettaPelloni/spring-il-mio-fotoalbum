package org.lessons.springilmiofotoalbum.controller;

import jakarta.validation.Valid;
import org.lessons.springilmiofotoalbum.dto.PhotoDto;
import org.lessons.springilmiofotoalbum.messages.AlertMessage;
import org.lessons.springilmiofotoalbum.messages.AlertMessageType;
import org.lessons.springilmiofotoalbum.model.Photo;
import org.lessons.springilmiofotoalbum.repository.CategoryRepository;
import org.lessons.springilmiofotoalbum.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.management.InvalidAttributeValueException;
import java.util.Optional;

@Controller
@RequestMapping("/admin/photos")
public class PhotoController {

    @Autowired
    PhotoService photoService;
    @Autowired
    CategoryRepository categoryRepository;

    //READ ----------------------------------------------------------------------------------
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

    //CREATE ----------------------------------------------------------------------------------
    @GetMapping("/create")
    public String create(
        Model model
    ) {
        model.addAttribute("catList", categoryRepository.findAll());
        model.addAttribute("photo", new PhotoDto());
        return "/photos/editor";
    }

    @PostMapping("create")
    public String store(
        @Valid @ModelAttribute("photo") PhotoDto photoDto,
        BindingResult bindingResult,
        Model model,
        RedirectAttributes redirectAttributes
    ) {
        model.addAttribute("catList", categoryRepository.findAll());
        try {
            Photo photo = photoService.create(photoDto, bindingResult);
            redirectAttributes.addFlashAttribute("message", new AlertMessage(AlertMessageType.SUCCESS, "Photo " + photo.getTitle() + " created successfully!"));
//            return "redirect:/admin/photos/" + photo.getId();
            return "redirect:/admin/photos";
        } catch (InvalidAttributeValueException e) {
            return "/photos/editor";
        }
    }

}
