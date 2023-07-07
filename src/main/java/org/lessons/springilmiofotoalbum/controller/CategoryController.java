package org.lessons.springilmiofotoalbum.controller;

import jakarta.validation.Valid;
import org.lessons.springilmiofotoalbum.messages.AlertMessage;
import org.lessons.springilmiofotoalbum.messages.AlertMessageType;
import org.lessons.springilmiofotoalbum.model.Category;
import org.lessons.springilmiofotoalbum.model.Photo;
import org.lessons.springilmiofotoalbum.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/admin/categories")
public class CategoryController {

    @Autowired
    CategoryRepository categoryRepository;

    //INDEX --------------------------------------------------------------------------------
    @GetMapping
    public String index(
        @RequestParam(value = "edit") Optional<Integer> id,
        Model model
    ) {
        Category catObj;
        if(id.isPresent()) {
            Optional<Category> result = categoryRepository.findById(id.get());
            if(result.isPresent()) {
                catObj = result.get();
            } else {
                catObj = new Category();
            }
        } else {
            catObj = new Category();
        }
        model.addAttribute("catObj", catObj);
        model.addAttribute("categories", categoryRepository.findAll());
        return "categories/index";
    }

    //CREATE-UPDATE --------------------------------------------------------------------------------
    @PostMapping("/save")
    public String save(
        @Valid @ModelAttribute("catObj") Category category,
        BindingResult bindingResult,
        Model model,
        RedirectAttributes redirectAttributes
    ) {
        if(category.getId() != null) {
            if(!category.getName().equals(findNameById(category.getId())) && !isNameUnique(category.getName())) {
                bindingResult.addError(new FieldError(
                        "catObj",
                        "name",
                        category.getName(),
                        false,null,null,
                        "Name "+category.getName()+" is already taken"
                ));
            }
        } else {
            if(!isNameUnique(category.getName())) {
                bindingResult.addError(new FieldError(
                        "catObj",
                        "name",
                        category.getName(),
                        false,null,null,
                        "Name "+category.getName()+" is already taken"
                ));
            }
        }

        if(bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryRepository.findAll());
            return "categories/index";
        }

        String action = "created";
        if(category.getId() != null) { action = "updated"; }
        categoryRepository.save(category);
        redirectAttributes.addFlashAttribute("message", new AlertMessage(AlertMessageType.SUCCESS, "Category "+category.getName()+" "+action+" successfully!"));
        return "redirect:/admin/categories";
    }

    //DELETE --------------------------------------------------------------------------------
    @PostMapping("delete/{id}")
    public String delete(
        @PathVariable Integer id,
        RedirectAttributes redirectAttributes
    ) {
        Category category = getById(id);
        for (Photo p : category.getPhotos()) {
            p.getCategories().remove(category);
        }
        categoryRepository.delete(category);
        redirectAttributes.addFlashAttribute("message", new AlertMessage(AlertMessageType.SUCCESS, "Category "+category.getName()+" deleted successfully!"));
        return "redirect:/admin/categories";
    }

    //UTILITY --------------------------------------------------------------------------------
    private boolean isNameUnique(String name) {
        Optional<Category> result = categoryRepository.findByName(name);
        return result.isEmpty();
    }

    private Category getById(Integer id) {
        Optional<Category> result = categoryRepository.findById(id);
        if(result.isPresent()) {
            return result.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category with id "+id+" not found");
        }
    }

    private String findNameById(Integer id) {
        Category result = getById(id);
        return result.getName();
    }
}
