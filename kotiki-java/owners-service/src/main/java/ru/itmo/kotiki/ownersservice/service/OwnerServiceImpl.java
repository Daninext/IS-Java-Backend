package ru.itmo.kotiki.ownersservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.itmo.kotiki.servicedata.dao.OwnerDAO;
import ru.itmo.kotiki.servicedata.entity.Owner;
import ru.itmo.kotiki.servicedata.transfer.OwnerBuffer;
import ru.itmo.kotiki.servicedata.transfer.OwnerTransfer;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Component
public class OwnerServiceImpl implements OwnerService {

    private final OwnerDAO ownerRepository;

    @Autowired
    public OwnerServiceImpl(OwnerDAO ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    @RabbitListener(queues = "addOwner")
    public void add(String record) throws JsonProcessingException {
        ownerRepository.save(new Owner(jsonToOwnerBuffer(record).getOwners().get(0)));
    }

    @RabbitListener(queues = "updateOwner")
    public boolean update(String record) throws JsonProcessingException {
        OwnerBuffer buffer = jsonToOwnerBuffer(record);
        if (ownerRepository.existsById(buffer.getOwners().get(0).getId())) {
            Owner oldOwner = ownerRepository.getById(buffer.getOwners().get(0).getId());
            oldOwner.copy(new Owner(buffer.getOwners().get(0)));
            ownerRepository.save(oldOwner);
            return true;
        }
        return false;
    }

    @RabbitListener(queues = "getOwnerById")
    public String getById(String record) throws JsonProcessingException {
        return ownerBufferToJson(new OwnerBuffer(new OwnerTransfer(ownerRepository.getById(jsonToOwnerBuffer(record).getOwners().get(0).getId()))));
    }

    @RabbitListener(queues = "getAllOwners")
    public String getAll(String record) throws JsonProcessingException {
        List<OwnerTransfer> temp = new ArrayList<>();
        ownerRepository.findAll().forEach(owner -> temp.add(new OwnerTransfer(owner)));
        return ownerBufferToJson(new OwnerBuffer(temp));
    }

    @RabbitListener(queues = "removeOwner")
    public boolean remove(String record) throws JsonProcessingException {
        int id = jsonToOwnerBuffer(record).getOwners().get(0).getId();
        if (ownerRepository.existsById(id)) {
            ownerRepository.deleteById(id);
            return true;
        }

        return false;
    }

    private String ownerBufferToJson(OwnerBuffer buffer) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(buffer);
    }

    private OwnerBuffer jsonToOwnerBuffer(String json) throws JsonProcessingException {
        return new ObjectMapper().readValue(json, OwnerBuffer.class);
    }
}
