package ru.itmo.services.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import ru.itmo.data.entity.Owner;
import ru.itmo.services.serv.OwnerService;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@RequestMapping("owners")
public class OwnerController {
    private OwnerService ownerService;

    @Autowired
    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @PostMapping("/create/login/{login}/password/{password}/role/{role}")
    public void addOwner(@RequestBody Owner owner
            , @PathVariable(name = "login") String login
            , @PathVariable(name = "password") String pass
            , @PathVariable(name = "role") String role) {
        ownerService.add(owner, login, pass, role);
    }

    @PutMapping(value = "/{id}")
    public void updateOwner(@PathVariable(name = "id") int id, @RequestBody Owner owner) {
        ownerService.update(id, owner);
    }

    @GetMapping(value = "/{id}")
    public Owner getOwnerById(@PathVariable(name = "id") int id) {
        return ownerService.getById(id);
    }

    @GetMapping
    public List<Owner> getAllOwners() {
        return ownerService.getAll();
    }

    @DeleteMapping(value = "delete/{id}")
    public void removeOwner(@PathVariable(name = "id") int id) {
        ownerService.remove(id);
    }
}
