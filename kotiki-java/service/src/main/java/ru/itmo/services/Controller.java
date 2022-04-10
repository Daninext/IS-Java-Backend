package ru.itmo.services;

import ru.itmo.data.entity.Cat;
import ru.itmo.data.entity.Owner;
import ru.itmo.services.serv.CatService;
import ru.itmo.services.serv.OwnerService;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Deprecated
@RestController
public class Controller {
    private CatService catService;
    private OwnerService ownerService;

    @Autowired
    public Controller() {
    }

    public void addCat(Cat cat) {
        catService.add(cat);
    }

    public void addCatFriend(Cat cat, Cat friend) {
        catService.addFriend(cat.getId(), friend.getId());
    }

    public void addOwner(Owner owner) {
        ownerService.add(owner);
    }

    public Cat getCatById(int id) {
        return catService.getById(id);
    }

    public Owner getOwnerById(int id) {
        return ownerService.getById(id);
    }

    public List<Cat> getAllCats() {
        return catService.getAll();
    }

    public List<Owner> getAllOwners() {
        return ownerService.getAll();
    }

    public void removeCat(int id) {
        catService.remove(id);
    }

    public void removeOwner(int id) {
        ownerService.remove(id);
    }
}
