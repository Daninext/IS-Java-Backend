package ru.itmo.kotiki.ownersservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface OwnerService {
    void add(String record) throws JsonProcessingException;

    boolean update(String record) throws JsonProcessingException;

    String getById(String record) throws JsonProcessingException;

    String getAll(String record) throws JsonProcessingException;

    boolean remove(String record) throws JsonProcessingException;
}
