package ru.itmo.tests;

import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Mock;
import ru.itmo.data.dao.CatDAO;
import ru.itmo.data.entity.BreedType;
import ru.itmo.data.entity.Cat;
import ru.itmo.data.entity.ColorType;
import ru.itmo.data.entity.Owner;
import ru.itmo.services.serv.CatService;
import ru.itmo.services.serv.CatServiceImpl;

import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

class CatServiceImplTest {

    @Mock
    private CatDAO dao;

    private CatService catService;

    public CatServiceImplTest() {
        MockitoAnnotations.openMocks(this);
        catService = new CatServiceImpl(dao);
    }

    @Test
    void getById() {
        given(dao.getById(1)).willReturn(new Cat(1, "-", new Date(), BreedType.valueOf("BENGAL"), ColorType.valueOf("BLACK"),
                new Owner(1, "-", new Date())));
        assertEquals(1, catService.getById(1).getId());
    }

    @Test
    void getByBreed() {
        given(dao.findCatsByBreed(BreedType.valueOf("BENGAL"))).willReturn(Arrays.asList(new Cat(1, "-", new Date(), BreedType.valueOf("BENGAL"), ColorType.valueOf("BLACK"),
                new Owner(1, "-", new Date()))));
        assertEquals(1, catService.getByBreed("BENGAL").size());
        assertEquals(BreedType.valueOf("BENGAL"), catService.getByBreed("BENGAL").get(0).getBreed());
    }

    @Test
    void getByColor() {
        given(dao.findCatsByColor(ColorType.valueOf("BLACK"))).willReturn(Arrays.asList(new Cat(1, "-", new Date(), BreedType.valueOf("BENGAL"), ColorType.valueOf("BLACK"),
                new Owner(1, "-", new Date()))));
        assertEquals(1, catService.getByColor("BLACK").size());
        assertEquals(ColorType.valueOf("BLACK"), catService.getByColor("BLACK").get(0).getColor());
    }
}