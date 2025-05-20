package org.example.controllers;

import org.example.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.example.service.UserService;

import java.io.IOException;


@Controller
public class UserController {

    @Autowired

    @GetMapping("/")
    public String redirectToUsers() {
        return "redirect:/users";
    }

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users";
    }

    @GetMapping("/users/new")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User());
        return "create_users";
    }

    @PostMapping("/users")
    public String createUser(@ModelAttribute User user) {
        try {
            userService.createUser(user);
            return "redirect:/users";
        } catch (IllegalArgumentException | IOException e) {
            return "redirect:/users/new?error=" + e.getMessage();
        }
    }

    @GetMapping("/users/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new RuntimeException("Uživatel nenalezen"));
        model.addAttribute("user", user);
        return "editor_user";
    }

    @PostMapping("/users/update/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute User user) {
        user.setId(id);
        userService.updateUser(user);
        return "redirect:/users";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }
}






