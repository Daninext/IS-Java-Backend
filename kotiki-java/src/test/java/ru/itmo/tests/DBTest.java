package ru.itmo.tests;

import org.junit.jupiter.api.*;
import ru.itmo.data.entity.Cat;
import ru.itmo.data.entity.Owner;
import ru.itmo.services.serv.CatService;
import ru.itmo.services.serv.OwnerService;

import java.util.Date;

import static org.junit.Assert.*;

public class DBTest {
    @Test
    public void addAndRemoveCatAndOwner() {
        CatService catService = new CatService();
        OwnerService ownerService = new OwnerService();

        Owner owner = new Owner("Steven", new Date());
        Cat cat = new Cat("Mars", new Date(), "breed", Cat.color_type.black, owner);

        ownerService.add(owner);
        catService.add(cat);

        assertEquals(1, ownerService.getAll().size());
        assertEquals(1, catService.getAll().size());

        catService.remove(cat);
        ownerService.remove(owner);

        assertEquals(0, ownerService.getAll().size());
        assertEquals(0, catService.getAll().size());

        catService.close();
        ownerService.close();
    }

    @Test
    public void isItMyCat() {
        CatService catService = new CatService();
        OwnerService ownerService = new OwnerService();

        Owner owner = new Owner("Steven", new Date());
        Cat cat = new Cat("Mars", new Date(), "breed", Cat.color_type.black, owner);

        ownerService.add(owner);
        catService.add(cat);

        assertEquals(owner.getId(), cat.getOwner().getId());

        catService.remove(cat);
        ownerService.remove(owner);

        catService.close();
        ownerService.close();
    }
}
