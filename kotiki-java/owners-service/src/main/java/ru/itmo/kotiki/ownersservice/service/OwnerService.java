package ru.itmo.kotiki.ownersservice.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import ru.itmo.kotiki.servicedata.transfer.OwnerBuffer;

public interface OwnerService {
    void add(ConsumerRecord<Integer, OwnerBuffer> record);

    boolean update(ConsumerRecord<Integer, OwnerBuffer> record);

    OwnerBuffer getById(ConsumerRecord<String, OwnerBuffer> record, @Header(KafkaHeaders.CORRELATION_ID) byte[] correlation);

    OwnerBuffer getAll(ConsumerRecord<String, OwnerBuffer> record, @Header(KafkaHeaders.CORRELATION_ID) byte[] correlation);

    boolean remove(ConsumerRecord<String, OwnerBuffer> record);
}
