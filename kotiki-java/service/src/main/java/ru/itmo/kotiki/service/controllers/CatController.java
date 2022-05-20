package ru.itmo.kotiki.service.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import ru.itmo.kotiki.data.entity.Cat;
import ru.itmo.kotiki.service.serv.CatService;

import java.util.List;

@RestController
@RequestMapping("cats")
public class CatController {
    private CatService catService;

    @Autowired
    public CatController(CatService catService) {
        this.catService = catService;
    }

    @PostMapping()
    public void addCat(@RequestBody Cat cat) {
        catService.add(cat);
    }

    @PutMapping(value = "/{id}")
    public void updateCat(@PathVariable(name = "id") int id, @RequestBody Cat cat) {
        catService.update(id, cat);
    }

    @PutMapping(value = "/{firstFriendId}/addFriendship/{secondFriendId}")
    public void addCatFriend(@PathVariable(name = "firstFriendId") int id, @PathVariable(name = "secondFriendId") int friendId) {
        if (id != friendId)
            catService.addFriend(id, friendId);
    }

    @GetMapping(value = "/{id}")
    public Cat getCatById(@PathVariable(name = "id") int id) {
        return catService.getById(id);
    }

    @GetMapping(value = "breed/{breed}")
    public List<Cat> getBreedCats(@PathVariable(name = "breed") String breed) {
        return catService.getByBreed(breed);
    }

    @GetMapping(value = "color/{color}")
    public List<Cat> getColorCats(@PathVariable(name = "color") String color) {
        return catService.getByColor(color);
    }

    @GetMapping
    public List<Cat> getAllCats() {
        return catService.getAll();
    }

    @DeleteMapping(value = "delete/{id}")
    public void removeCat(@PathVariable(name = "id") int id) {
        catService.remove(id);
    }
}
