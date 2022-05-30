package ru.itmo.kotiki.catsservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface CatService {
    void add(String record) throws JsonProcessingException;

    void addFriend(String record) throws JsonProcessingException;

    void removeFriend(String record) throws JsonProcessingException;

    String getById(String record) throws JsonProcessingException;

    String getAll(String record) throws JsonProcessingException;

    String getByBreed(String record) throws JsonProcessingException;

    String getByColor(String record) throws JsonProcessingException;

    boolean update(String record) throws JsonProcessingException;

    boolean remove(String record) throws JsonProcessingException;
}
