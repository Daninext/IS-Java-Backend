package ru.itmo.kotiki.catsservice.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import ru.itmo.kotiki.servicedata.transfer.CatBuffer;

public interface CatService {
    void add(ConsumerRecord<String, CatBuffer> record);

    void addFriend(ConsumerRecord<String, CatBuffer> record);

    void removeFriend(ConsumerRecord<String, CatBuffer> record);

    CatBuffer getById(ConsumerRecord<String, CatBuffer> record, @Header(KafkaHeaders.CORRELATION_ID) byte[] correlation);

    CatBuffer getAll(ConsumerRecord<String, CatBuffer> record, @Header(KafkaHeaders.CORRELATION_ID) byte[] correlation);

    CatBuffer getByBreed(ConsumerRecord<String, CatBuffer> record, @Header(KafkaHeaders.CORRELATION_ID) byte[] correlation);

    CatBuffer getByColor(ConsumerRecord<String, CatBuffer> record, @Header(KafkaHeaders.CORRELATION_ID) byte[] correlation);

    boolean update(ConsumerRecord<String, CatBuffer> record);

    boolean remove(ConsumerRecord<String, CatBuffer> record);
}
