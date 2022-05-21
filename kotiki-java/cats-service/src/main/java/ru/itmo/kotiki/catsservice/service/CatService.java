package ru.itmo.kotiki.catsservice.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import ru.itmo.kotiki.servicedata.entity.Cat;
import ru.itmo.kotiki.servicedata.transfer.CatBuffer;

import java.util.List;

public interface CatService {
    void add(Cat cat);

    void addFriend(int id, int friendId);

    void removeFriend(int id, int friendId);

    CatBuffer getById(ConsumerRecord<String, CatBuffer> record, @Header(KafkaHeaders.CORRELATION_ID) byte[] correlation);

    List<Cat> getByBreed(String breed);

    List<Cat> getByColor(String color);

    List<Cat> getAll();

    boolean update(int id, Cat cat);

    boolean remove(int id);
}
