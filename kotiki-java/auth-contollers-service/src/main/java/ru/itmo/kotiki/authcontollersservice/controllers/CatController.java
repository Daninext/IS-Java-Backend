package ru.itmo.kotiki.authcontollersservice.controllers;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.itmo.kotiki.authcontollersservice.service.UserService;
import ru.itmo.kotiki.servicedata.entity.Cat;
import ru.itmo.kotiki.servicedata.entity.User;
import ru.itmo.kotiki.servicedata.transfer.CatBuffer;
import ru.itmo.kotiki.servicedata.transfer.CatTransfer;
import ru.itmo.kotiki.servicedata.transfer.OwnerBuffer;
import ru.itmo.kotiki.servicedata.transfer.OwnerTransfer;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("cats")
public class CatController {

    private final ReplyingKafkaTemplate<String, CatBuffer, CatBuffer> kafkaTemplate;
    private UserService userService;

    @Autowired
    public CatController(ReplyingKafkaTemplate<String, CatBuffer, CatBuffer> kafkaTemplate, UserService userService) {
        this.kafkaTemplate = kafkaTemplate;
        this.userService = userService;
    }

    @GetMapping(value = "/{id}")
    public CatBuffer getCatById(@PathVariable(name = "id") int id) throws InterruptedException, ExecutionException {
        User user = getUser();
        ProducerRecord<String, CatBuffer> record = new ProducerRecord<>("getCatById", new CatBuffer(user.getAuthorities().toString(), user.getOwnerId(), new CatTransfer(id)));
        record.headers().add(new RecordHeader(KafkaHeaders.REPLY_TOPIC, ("resultCat").getBytes()));
        RequestReplyFuture<String, CatBuffer, CatBuffer> future = kafkaTemplate.sendAndReceive(record);

        SendResult<String, CatBuffer> result = future.getSendFuture().get();
        result.getProducerRecord().headers().forEach(header -> System.out.println(header.key() + ":" + header.value().toString()));

        ConsumerRecord<String, CatBuffer> response = future.get();
        return response.value();
    }

    private User getUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        return userService.getByUsername(username);
    }
/*
    @PostMapping()
    public void addCat(@RequestBody Cat cat) {
        catService.add(cat);
    }

    @PutMapping(value = "/{id}")
    public void updateCat(@PathVariable(name = "id") int id, @RequestBody Cat cat) {
        catService.update(id, cat);
    }

    @PutMapping(value = "/{firstFriendId}/addFriendship/{secondFriendId}")
    public void addCatFriend(@PathVariable(name = "firstFriendId") int id, @PathVariable(name = "secondFriendId") int friendId) {
        if (id != friendId)
            catService.addFriend(id, friendId);
    }



    @GetMapping(value = "breed/{breed}")
    public List<Cat> getBreedCats(@PathVariable(name = "breed") String breed) {
        return catService.getByBreed(breed);
    }

    @GetMapping(value = "color/{color}")
    public List<Cat> getColorCats(@PathVariable(name = "color") String color) {
        return catService.getByColor(color);
    }

    @GetMapping
    public List<Cat> getAllCats() {
        return catService.getAll();
    }

    @DeleteMapping(value = "/{id}")
    public void removeCat(@PathVariable(name = "id") int id) {
        catService.remove(id);
    }*/
}
