package ru.itmo.kotiki.authcontollersservice.controllers;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.web.bind.annotation.*;
import ru.itmo.kotiki.servicedata.entity.Owner;
import ru.itmo.kotiki.servicedata.transfer.OwnerBuffer;
import ru.itmo.kotiki.servicedata.transfer.OwnerTransfer;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("owners")
public class OwnerController {

    private final ReplyingKafkaTemplate<String, OwnerBuffer, OwnerBuffer> kafkaTemplate;

    @Autowired
    public OwnerController(ReplyingKafkaTemplate<String, OwnerBuffer, OwnerBuffer> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping
    public void addOwner(@RequestBody Owner owner) {
        kafkaTemplate.send("addOwner", "add", new OwnerBuffer(new OwnerTransfer(owner)));
    }

    @PutMapping(value = "/{id}")
    public void updateOwner(@PathVariable(name = "id") int id, @RequestBody Owner owner) {
        kafkaTemplate.send("updateOwner", "update", new OwnerBuffer(new OwnerTransfer(owner)));
    }

    @GetMapping(value = "/{id}")
    public OwnerBuffer getOwnerById(@PathVariable(name = "id") int id) throws InterruptedException, ExecutionException {
        ProducerRecord<String, OwnerBuffer> record = new ProducerRecord<>("getOwnerById", new OwnerBuffer(new OwnerTransfer(id)));
        record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, ("resultOwner").getBytes()));
        RequestReplyFuture<String, OwnerBuffer, OwnerBuffer> future = kafkaTemplate.sendAndReceive(record);

        SendResult<String, OwnerBuffer> result = future.getSendFuture().get();
        result.getProducerRecord().headers().forEach(header -> System.out.println(header.key() + ":" + header.value().toString()));

        ConsumerRecord<String, OwnerBuffer> response = future.get();
        return response.value();
    }

    @GetMapping
    public OwnerBuffer getAllOwners() throws InterruptedException, ExecutionException {
        ProducerRecord<String, OwnerBuffer> record = new ProducerRecord<>("getAllOwners", new OwnerBuffer(new OwnerTransfer(0)));
        record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, ("resultOwner").getBytes()));
        RequestReplyFuture<String, OwnerBuffer, OwnerBuffer> future = kafkaTemplate.sendAndReceive(record);

        SendResult<String, OwnerBuffer> result = future.getSendFuture().get();
        result.getProducerRecord().headers().forEach(header -> System.out.println(header.key() + ":" + header.value().toString()));

        ConsumerRecord<String, OwnerBuffer> response = future.get();
        return response.value();
    }

    @DeleteMapping(value = "/{id}")
    public void removeOwner(@PathVariable(name = "id") int id) {
        kafkaTemplate.send("removeOwner", "delete", new OwnerBuffer(new OwnerTransfer(id)));
    }
}
