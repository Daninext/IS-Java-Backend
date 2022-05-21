package ru.itmo.kotiki.authcontollersservice.service;

import ru.itmo.kotiki.servicedata.entity.User;

public interface UserService {
    void add(User user);

    User getById(int id);

    User getByUsername(String username);

    boolean remove(int id);
}