package ru.itmo.tests;

import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Mock;

import ru.itmo.data.dao.OwnerDAO;
import ru.itmo.data.dao.UserDAO;
import ru.itmo.data.entity.Owner;
import ru.itmo.services.serv.OwnerService;
import ru.itmo.services.serv.OwnerServiceImpl;

import java.util.Arrays;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

class OwnerServiceImplTest {

    @Mock
    private OwnerDAO dao;

    private OwnerService ownerService;

    public OwnerServiceImplTest() {
        MockitoAnnotations.openMocks(this);
        this.ownerService = new OwnerServiceImpl(dao);
    }

    @Test
    void getById() {
        given(dao.getById(1)).willReturn(new Owner(1, "-", new Date()));
        assertEquals(1, ownerService.getById(1).getId());
    }

    @Test
    void getAll() {
        given(dao.findAll()).willReturn(Arrays.asList(new Owner(1, "1", new Date()), new Owner(2, "2", new Date())));
        assertEquals(2, ownerService.getAll().size());
    }
}