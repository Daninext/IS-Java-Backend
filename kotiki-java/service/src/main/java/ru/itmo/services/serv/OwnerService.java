package ru.itmo.services.serv;

import ru.itmo.data.entity.Owner;

import java.util.List;

public interface OwnerService {
    void add(Owner owner);

    boolean update(int id, Owner owner);

    Owner getById(int id);

    List<Owner> getAll();

    boolean remove(int id);
}
