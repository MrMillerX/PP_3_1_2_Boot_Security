package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping()
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users/index";
    }

    @PostMapping()
    public String createUser(@ModelAttribute("user") User user,
                             @RequestParam(value = "rol") Long... longs) {
        Set<Role> bufRoles = new HashSet<>();
        for (long l : longs) {
            bufRoles.add(roleService.getRole(l));
        }
        user.setRoles(bufRoles);
        userService.addUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/new")
    public String newUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.getAllRoles());
        return "users/new";
    }

    @PatchMapping("/{id}")
    public String updateUser(@ModelAttribute("user") User user, @PathVariable("id") long id,
                             @RequestParam(value = "rol") Long... longs) {
        Set<Role> bufRoles = new HashSet<>();
        for (long l : longs) {
            bufRoles.add(roleService.getRole(l));
        }
        user.setRoles(bufRoles);
        userService.updateUser(user);
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") long id,
                             Model model) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    @GetMapping("/{id}/edit")
    public String editUser(Model model, @PathVariable("id") long id) {
        model.addAttribute("user", userService.getUser(id));
        model.addAttribute("setRoles", roleService.getAllRoles());
        return "users/edit";
    }
}
