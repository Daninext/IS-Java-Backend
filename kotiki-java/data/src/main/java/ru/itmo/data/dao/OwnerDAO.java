package ru.itmo.data.dao;

import ru.itmo.data.entity.Owner;

import java.util.List;

public interface OwnerDAO {
    void add(Owner owner);

    Owner getById(int id);

    List<Owner> getAll();

    void remove(Owner owner);
}
