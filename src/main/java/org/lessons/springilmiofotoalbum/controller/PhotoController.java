package org.lessons.springilmiofotoalbum.controller;

import jakarta.validation.Valid;
import org.lessons.springilmiofotoalbum.dto.PhotoDto;
import org.lessons.springilmiofotoalbum.exceptions.PhotoNotFoundException;
import org.lessons.springilmiofotoalbum.exceptions.UserNotAllowedException;
import org.lessons.springilmiofotoalbum.messages.AlertMessage;
import org.lessons.springilmiofotoalbum.messages.AlertMessageType;
import org.lessons.springilmiofotoalbum.model.Photo;
import org.lessons.springilmiofotoalbum.repository.CategoryRepository;
import org.lessons.springilmiofotoalbum.security.DbUserDetails;
import org.lessons.springilmiofotoalbum.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.management.InvalidAttributeValueException;
import java.security.Principal;
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
        Authentication authentication,
        @RequestParam Optional<String> keyword
    ) {
        try {
            model.addAttribute("photos", photoService.getAllOfActiveUser(keyword, authentication.getName()));
            if(keyword.isPresent()) {
                model.addAttribute("keyword", keyword.get());
            }
            return "/photos/index";
        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public String show(
        @PathVariable Integer id,
        Authentication authentication,
        Model model
    ) {
        try {
            Photo photo = photoService.userIsAllowed(id, authentication.getName());
            model.addAttribute("photo", photo);
            return "/photos/show";
        } catch (PhotoNotFoundException | UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UserNotAllowedException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
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
        Authentication authentication,
        Model model,
        RedirectAttributes redirectAttributes
    ) {
        try {
            Photo photo = photoService.create(photoDto, bindingResult, authentication.getName());
            redirectAttributes.addFlashAttribute("message", new AlertMessage(AlertMessageType.SUCCESS, "Photo " + photo.getTitle() + " created successfully!"));
            return "redirect:/admin/photos/" + photo.getId();
        } catch (InvalidAttributeValueException e) {
            model.addAttribute("catList", categoryRepository.findAll());
            return "/photos/editor";
        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    //UPDATE ----------------------------------------------------------------------------------
    @GetMapping("/edit/{id}")
    public String edit(
        @PathVariable Integer id,
        Authentication authentication,
        Model model
    ) {
        try{
            Photo photo = photoService.userIsAllowed(id, authentication.getName());
            model.addAttribute("catList", categoryRepository.findAll());
            model.addAttribute("photo", photoService.fromPhotoToDto(photo));
            return "/photos/editor";
        } catch (PhotoNotFoundException | UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UserNotAllowedException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

    @PostMapping("/edit/{id}")
    public String update(
            @PathVariable Integer id,
            @Valid @ModelAttribute("photo") PhotoDto photoDto,
            BindingResult bindingResult,
            Authentication authentication,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        try {
            photoService.userIsAllowed(id, authentication.getName()); //per lanciare l'eccezione nel caso in cui l'utente stesse cercando di modificare una foto non sua
            Photo updatedPhoto = photoService.update(id, photoDto, bindingResult);
            redirectAttributes.addFlashAttribute("message", new AlertMessage(AlertMessageType.SUCCESS, "Photo " + updatedPhoto.getTitle() + " updated successfully!"));
            return "redirect:/admin/photos/" + updatedPhoto.getId();
        } catch (InvalidAttributeValueException e) {
            model.addAttribute("catList", categoryRepository.findAll());
            return "/photos/editor";
        } catch (PhotoNotFoundException | UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UserNotAllowedException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

    //DELETE ----------------------------------------------------------------------------------
    @PostMapping("/delete/{id}")
    public String delete(
        @PathVariable Integer id,
        Authentication authentication,
        RedirectAttributes redirectAttributes
    ){
        try {
            Photo photo = photoService.userIsAllowed(id, authentication.getName());
            photoService.delete(photo);
            redirectAttributes.addFlashAttribute("message", new AlertMessage(AlertMessageType.SUCCESS, "Photo with id "+id+" deleted successfully!"));
            return "redirect:/admin/photos";
        } catch (PhotoNotFoundException | UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UserNotAllowedException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }
}
