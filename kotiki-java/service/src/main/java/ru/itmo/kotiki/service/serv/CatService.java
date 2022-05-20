package ru.itmo.kotiki.service.serv;

import ru.itmo.kotiki.data.entity.Cat;

import java.util.List;

public interface CatService {
    void add(Cat cat);

    void addFriend(int id, int friendId);

    void removeFriend(int id, int friendId);

    Cat getById(int id);

    List<Cat> getByBreed(String breed);

    List<Cat> getByColor(String color);

    List<Cat> getAll();

    boolean update(int id, Cat cat);

    boolean remove(int id);
}
