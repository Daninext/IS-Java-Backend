package ru.itmo.kotiki.authcontollersservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ru.itmo.kotiki.servicedata.entity.Owner;
import ru.itmo.kotiki.servicedata.transfer.OwnerBuffer;
import ru.itmo.kotiki.servicedata.transfer.OwnerTransfer;

import java.util.List;

@RestController
@RequestMapping("owners")
public class OwnerController {

    private final RabbitTemplate template;

    @Autowired
    public OwnerController(RabbitTemplate template) {
        this.template = template;
    }

    @PostMapping
    public void addOwner(@RequestBody Owner owner) throws JsonProcessingException {
        template.convertAndSend("addOwner", ownerBufferToJson(new OwnerBuffer(new OwnerTransfer(owner))));
    }

    @PutMapping(value = "/{id}")
    public void updateOwner(@PathVariable(name = "id") int id, @RequestBody Owner owner) throws JsonProcessingException {
        OwnerTransfer ot = new OwnerTransfer(owner);
        ot.setId(id);
        template.convertAndSend("updateOwner", ownerBufferToJson(new OwnerBuffer(ot)));
    }

    @GetMapping(value = "/{id}")
    public List<OwnerTransfer> getOwnerById(@PathVariable(name = "id") int id) throws JsonProcessingException {
        String response = (String) template.convertSendAndReceive("getOwnerById", ownerBufferToJson(new OwnerBuffer(new OwnerTransfer(id))));

        if (response == null)
            return null;

        return jsonToOwnerBuffer(response).getOwners();
    }

    @GetMapping
    public List<OwnerTransfer> getAllOwners() throws JsonProcessingException {
        String response = (String) template.convertSendAndReceive("getAllOwners", ownerBufferToJson(new OwnerBuffer(new OwnerTransfer(0))));

        if (response == null)
            return null;

        return jsonToOwnerBuffer(response).getOwners();
    }

    @DeleteMapping(value = "/{id}")
    public void removeOwner(@PathVariable(name = "id") int id) throws JsonProcessingException {
        template.convertAndSend("removeOwner", ownerBufferToJson(new OwnerBuffer(new OwnerTransfer(id))));
    }

    private String ownerBufferToJson(OwnerBuffer buffer) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(buffer);
    }

    private OwnerBuffer jsonToOwnerBuffer(String json) throws JsonProcessingException {
        return new ObjectMapper().readValue(json, OwnerBuffer.class);
    }
}
