package ru.itmo.kotiki.service.serv;

import ru.itmo.kotiki.data.entity.User;

public interface UserService {
    void add(User user);

    User getById(int id);

    boolean remove(int id);
}