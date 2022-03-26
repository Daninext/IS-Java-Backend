package ru.itmo.tests;

import org.junit.jupiter.api.*;
import ru.itmo.data.entity.Cat;
import ru.itmo.data.entity.Owner;
import ru.itmo.services.Controller;
import ru.itmo.services.serv.CatService;
import ru.itmo.services.serv.OwnerService;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DBTest {
    @Test
    public void addAndRemoveCatAndOwner() {
        Controller controller = new Controller();

        Owner owner = new Owner("Steven", new Date());
        Cat cat = new Cat("Mars", new Date(), Cat.BreedType.MAINE_COON, Cat.ColorType.BLACK, owner);

        controller.addOwner(owner);
        controller.addCat(cat);

        assertEquals(1, controller.getAllOwners().size());
        assertEquals(1, controller.getAllCats().size());

        controller.removeCat(cat);
        controller.removeOwner(owner);

        assertEquals(0, controller.getAllOwners().size());
        assertEquals(0, controller.getAllCats().size());
    }

    @Test
    public void isItMyCat() {
        Controller controller = new Controller();

        Owner owner = new Owner("Steven", new Date());
        Cat cat = new Cat("Mars", new Date(), Cat.BreedType.BENGAL, Cat.ColorType.BLACK, owner);

        controller.addOwner(owner);
        controller.addCat(cat);

        assertEquals(owner.getId(), cat.getOwner().getId());

        controller.removeCat(cat);
        controller.removeOwner(owner);
    }

    @Test
    public void areWeFriends() {
        Controller controller = new Controller();

        Owner owner = new Owner("Steven", new Date());
        Cat cat = new Cat("Mars", new Date(), Cat.BreedType.BENGAL, Cat.ColorType.BLACK, owner);

        controller.addOwner(owner);
        controller.addCat(cat);

        Cat newCat = new Cat("Kay", new Date(), Cat.BreedType.MAINE_COON, Cat.ColorType.WHITE, owner);

        controller.addCat(newCat);
        controller.addCatFriend(cat, newCat);

        assertEquals(1, controller.getCatById(cat.getId()).getFriends().size());
        assertEquals(1, controller.getCatById(newCat.getId()).getFriends().size());

        controller.removeCat(controller.getCatById(cat.getId()));

        Cat dbCat = controller.getCatById(newCat.getId());
        controller.removeCat(dbCat);
    }
}
