package ru.itmo.services.serv;

import ru.itmo.data.entity.User;

public interface UserService {
    void add(User user);

    User getById(int id);

    boolean remove(int id);
}