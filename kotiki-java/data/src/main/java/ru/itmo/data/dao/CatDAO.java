package ru.itmo.data.dao;

import ru.itmo.data.entity.Cat;

import java.util.List;

public interface CatDAO {
    void add(Cat cat);

    void addFriend(Cat cat, Cat friend);

    void removeFriend(Cat cat, Cat friend);

    Cat getById(int id);

    List<Cat> getAll();

    void remove(Cat cat);
}
