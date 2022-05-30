package ru.itmo.kotiki.authcontollersservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ru.itmo.kotiki.servicedata.entity.User;
import ru.itmo.kotiki.authcontollersservice.service.UserService;
import ru.itmo.kotiki.servicedata.transfer.OwnerBuffer;
import ru.itmo.kotiki.servicedata.transfer.OwnerTransfer;

@RestController
@RequestMapping("users")
public class UserController {

    private final RabbitTemplate template;
    private final UserService userService;

    @Autowired
    public UserController(RabbitTemplate template, UserService userService) {
        this.template = template;
        this.userService = userService;
    }

    @PostMapping
    public void addUser(@RequestBody User user) throws JsonProcessingException {
        String response = (String) template.convertSendAndReceive("getOwnerById", ownerBufferToJson(new OwnerBuffer(new OwnerTransfer(user.getOwnerId()))));

        if (response == null)
            return;

        OwnerBuffer ownerBuffer= jsonToOwnerBuffer(response);

        if (ownerBuffer.getOwners().get(0).getAccountId() == -1)
            userService.add(user);
    }

    @GetMapping(value = "/{id}")
    public User getUserById(@PathVariable(name = "id") int id) {
        return userService.getById(id);
    }

    @DeleteMapping(value = "/delete/{id}")
    public void deleteUser(@PathVariable(name = "id") int id) {
        userService.remove(id);
    }

    private String ownerBufferToJson(OwnerBuffer buffer) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(buffer);
    }

    private OwnerBuffer jsonToOwnerBuffer(String json) throws JsonProcessingException {
        return new ObjectMapper().readValue(json, OwnerBuffer.class);
    }
}
