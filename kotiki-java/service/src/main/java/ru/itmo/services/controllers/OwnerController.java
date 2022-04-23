package ru.itmo.services.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import ru.itmo.data.entity.Owner;
import ru.itmo.services.serv.OwnerServiceImpl;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@RequestMapping("owners")
public class OwnerController {
    private OwnerServiceImpl ownerService;

    @Autowired
    public OwnerController(OwnerServiceImpl ownerService) {
        this.ownerService = ownerService;
    }

    @PostMapping
    public void addOwner(@RequestBody Owner owner) {
        ownerService.add(owner);
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

    @DeleteMapping(value = "/{id}")
    public void removeOwner(@PathVariable(name = "id") int id) {
        ownerService.remove(id);
    }
}
