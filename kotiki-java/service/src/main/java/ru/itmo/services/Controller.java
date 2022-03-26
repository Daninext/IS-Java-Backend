package ru.itmo.services;

import ru.itmo.data.entity.Cat;
import ru.itmo.data.entity.Owner;
import ru.itmo.services.serv.CatService;
import ru.itmo.services.serv.OwnerService;

import java.util.List;

public class Controller {
    private CatService catService = new CatService();
    private OwnerService ownerService = new OwnerService();

    public void addCat(Cat cat) {
        catService.add(cat);
    }

    public void addCatFriend(Cat cat, Cat friend) {
        catService.addFriend(cat, friend);
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

    public void removeCat(Cat cat) {
        catService.remove(cat);
    }

    public void removeOwner(Owner owner) {
        ownerService.remove(owner);
    }
}
