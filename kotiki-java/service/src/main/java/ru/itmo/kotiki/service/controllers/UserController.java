package ru.itmo.kotiki.service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ru.itmo.kotiki.data.entity.User;
import ru.itmo.kotiki.service.serv.OwnerService;
import ru.itmo.kotiki.service.serv.UserService;

@RestController
@RequestMapping("users")
public class UserController {
    private UserService userService;
    private OwnerService ownerService;

    @Autowired
    public UserController(UserService userService, OwnerService ownerService) {
        this.userService = userService;
        this.ownerService = ownerService;
    }

    @PostMapping
    public void addUser(@RequestBody User user) {
        if (ownerService.getById(user.getOwnerId()).getAccountId() == -1)
            userService.add(user);
    }

    @GetMapping(value = "/{id}")
    public User getUserById(@PathVariable(name = "id") int id) {
        return userService.getById(id);
    }

    @DeleteMapping(value = "/delete/{id}")
    public void deleteUser(@PathVariable(name = "id") int id) {
        userService.remove(id);
    }
}
