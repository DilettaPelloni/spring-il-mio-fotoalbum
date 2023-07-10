package org.lessons.springilmiofotoalbum.controller;

import jakarta.validation.Valid;
import org.lessons.springilmiofotoalbum.messages.AlertMessage;
import org.lessons.springilmiofotoalbum.messages.AlertMessageType;
import org.lessons.springilmiofotoalbum.model.Role;
import org.lessons.springilmiofotoalbum.model.User;
import org.lessons.springilmiofotoalbum.repository.RoleRepository;
import org.lessons.springilmiofotoalbum.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/")
public class MainController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;

    //HOME ---------------------------------------------------------------------------------
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

    //LOGIN-LOGOUT ---------------------------------------------------------------------------------
    @GetMapping("/login")
    public String login() {
        return "/generals/login";
    }

    @GetMapping("/performLogout")
    public String logout() {
        return "/generals/logout";
    }

    //REGISTRAZIONE ---------------------------------------------------------------------------------
    @GetMapping("/signin")
    public String signin(
        Model model
    ) {
        model.addAttribute("user", new User());
        return "/generals/signin";
    }

    @PostMapping("/signin")
    public String register(
        @Valid @ModelAttribute("user") User user,
        BindingResult bindingResult,
        RedirectAttributes redirectAttributes
    ){
        if(!isEmailUnique(user.getEmail())) {
            bindingResult.addError(new FieldError(
                    "user",
                    "email",
                    user.getEmail(),
                    false,
                    null,
                    null,
                    "Email "+user.getEmail()+ " already registered"
            ));
        }
        if(bindingResult.hasErrors()) {
            return "/generals/signin";
        }
        user.setPassword("{noop}"+user.getPassword());
        Optional<Role> role = roleRepository.findByName("ADMIN");
        user.getRoles().add(role.get());
        userRepository.save(user);
        redirectAttributes.addFlashAttribute("message", new AlertMessage(AlertMessageType.SUCCESS, "You have been successfully registered!"));
        return "redirect:/login";
    }

    //UTILITY ---------------------------------------------------------------------------------
    private boolean isEmailUnique(String email) {
        Optional<User> result = userRepository.findByEmail(email);
        return result.isEmpty();
    }
}
