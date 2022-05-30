package ru.itmo.kotiki.catsservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.itmo.kotiki.servicedata.dao.*;
import ru.itmo.kotiki.servicedata.entity.*;
import ru.itmo.kotiki.servicedata.transfer.CatBuffer;
import ru.itmo.kotiki.servicedata.transfer.CatTransfer;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@Component
public class CatServiceImpl implements CatService {

    private final CatDAO catRepository;

    @Autowired
    public CatServiceImpl(CatDAO catRepository) {
        this.catRepository = catRepository;
    }

    @RabbitListener(queues = "addCat")
    public void add(String record) throws JsonProcessingException {
        catRepository.save(new Cat(jsonToCatBuffer(record).getCats().get(0)));
    }

    @RabbitListener(queues = "addCatFriend")
    public void addFriend(String record) throws JsonProcessingException {
        CatBuffer buffer = jsonToCatBuffer(record);
        int id = buffer.getCats().get(0).getId();
        int friendId = buffer.getCats().get(1).getId();
        Cat cat = catRepository.getById(id);
        Cat friend = catRepository.getById(friendId);

        cat.addFriend(friend);
        friend.addFriend(cat);
        catRepository.save(cat);
        catRepository.save(friend);
    }

    @RabbitListener(queues = "removeCatFriend")
    public void removeFriend(String record) throws JsonProcessingException {
        CatBuffer buffer = jsonToCatBuffer(record);
        int id = buffer.getCats().get(0).getId();
        int friendId = buffer.getCats().get(1).getId();
        Cat cat = catRepository.getById(id);
        Cat friend = catRepository.getById(friendId);

        cat.removeFriend(friend);
        friend.removeFriend(cat);
        catRepository.save(cat);
        catRepository.save(friend);
    }

    @RabbitListener(queues = "getCatById")
    public String getById(String record) throws JsonProcessingException {
        CatBuffer buffer = jsonToCatBuffer(record);
        Cat cat;

        if (buffer.getUser().getRole() == RoleType.ADMIN)
            cat = catRepository.getById(buffer.getCats().get(0).getId());
        else
            cat = catRepository.getCatByIdAndOwner(buffer.getCats().get(0).getId(), new Owner(buffer.getUser().getOwner()));

        if (cat == null)
            return catBufferToJson(new CatBuffer(null));

        return catBufferToJson(new CatBuffer(null, new CatTransfer(cat)));
    }

    @RabbitListener(queues = "getAllCats")
    public String getAll(String record) throws JsonProcessingException {
        CatBuffer buffer = jsonToCatBuffer(record);
        List<CatTransfer> ct = new ArrayList<>();

        if (buffer.getUser().getRole() == RoleType.ADMIN)
            catRepository.findAll().forEach(cat -> ct.add(new CatTransfer(cat)));
        else
            catRepository.findCatsByOwner(new Owner(buffer.getUser().getOwner())).forEach(cat -> ct.add(new CatTransfer(cat)));

        return catBufferToJson(new CatBuffer(null, ct));
    }

    @RabbitListener(queues = "getCatsByBreed")
    public String getByBreed(String record) throws JsonProcessingException {
        CatBuffer buffer = jsonToCatBuffer(record);
        List<CatTransfer> ct = new ArrayList<>();

        if (buffer.getUser().getRole() == RoleType.ADMIN)
            catRepository.findCatsByBreed(buffer.getCats().get(0).getBreed()).forEach(cat -> ct.add(new CatTransfer(cat)));
        else
            catRepository.findCatsByOwnerAndBreed(new Owner(buffer.getUser().getOwner()), buffer.getCats().get(0).getBreed()).forEach(cat -> ct.add(new CatTransfer(cat)));

        return catBufferToJson(new CatBuffer(null, ct));
    }

    @RabbitListener(queues = "getCatsByColor")
    public String getByColor(String record) throws JsonProcessingException {
        CatBuffer buffer = jsonToCatBuffer(record);
        List<CatTransfer> ct = new ArrayList<>();

        if (buffer.getUser().getRole() == RoleType.ADMIN)
            catRepository.findCatsByColor(buffer.getCats().get(0).getColor()).forEach(cat -> ct.add(new CatTransfer(cat)));
        else
            catRepository.findCatsByOwnerAndColor(new Owner(buffer.getUser().getOwner()), buffer.getCats().get(0).getColor()).forEach(cat -> ct.add(new CatTransfer(cat)));

        return catBufferToJson(new CatBuffer(null, ct));
    }

    @RabbitListener(queues = "updateCat")
    public boolean update(String record) throws JsonProcessingException {
        CatBuffer buffer = jsonToCatBuffer(record);
        if (buffer.getUser().getRole() != RoleType.ADMIN
            || buffer.getCats().get(0).getOwnerId() != buffer.getUser().getOwner().getId())
            return false;

        int id = buffer.getCats().get(0).getId();
        if (catRepository.existsById(id)) {
            Cat oldCat = catRepository.getById(id);
            oldCat.copy(new Cat(buffer.getCats().get(0)));
            catRepository.save(oldCat);
            return true;
        }

        return false;
    }

    @Transactional
    @RabbitListener(queues = "removeCat")
    public boolean remove(String record) throws JsonProcessingException {
        int id = jsonToCatBuffer(record).getCats().get(0).getId();
        if (catRepository.existsById(id)) {
            Cat cat = catRepository.getById(id);
            for (Cat friend: cat.getFriends())
                friend.removeFriend(cat);

            catRepository.saveAll(cat.getFriends());
            cat.clearFriends();
            catRepository.deleteById(id);
            return true;
        }

        return false;
    }

    private String catBufferToJson(CatBuffer buffer) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(buffer);
    }

    private CatBuffer jsonToCatBuffer(String json) throws JsonProcessingException {
        return new ObjectMapper().readValue(json, CatBuffer.class);
    }
}
