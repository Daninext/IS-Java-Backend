package ru.itmo.kotiki.catsservice.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.kafka.annotation.EnableKafka;

import ru.itmo.kotiki.servicedata.dao.*;
import ru.itmo.kotiki.servicedata.entity.*;
import ru.itmo.kotiki.servicedata.transfer.CatBuffer;
import ru.itmo.kotiki.servicedata.transfer.CatTransfer;

import java.util.ArrayList;
import java.util.List;

@EnableKafka
@Service
@Transactional
public class CatServiceImpl implements CatService {

    private final CatDAO catRepository;

    @Autowired
    public CatServiceImpl(CatDAO catRepository) {
        this.catRepository = catRepository;
    }

    @KafkaListener(topics="addCat", groupId = "cat", containerFactory = "catKafkaListenerContainerFactory")
    public void add(ConsumerRecord<String, CatBuffer> record) {
        catRepository.save(new Cat(record.value().getCats().get(0)));
    }

    @KafkaListener(topics="addCatFriend", groupId = "cat", containerFactory = "catKafkaListenerContainerFactory")
    public void addFriend(ConsumerRecord<String, CatBuffer> record) {
        int id = record.value().getCats().get(0).getId();
        int friendId = record.value().getCats().get(1).getId();
        Cat cat = catRepository.getById(id);
        Cat friend = catRepository.getById(friendId);

        cat.addFriend(friend);
        friend.addFriend(cat);
        catRepository.save(cat);
        catRepository.save(friend);
    }

    @KafkaListener(topics="removeCatFriend", groupId = "cat", containerFactory = "catKafkaListenerContainerFactory")
    public void removeFriend(ConsumerRecord<String, CatBuffer> record) {
        int id = record.value().getCats().get(0).getId();
        int friendId = record.value().getCats().get(1).getId();
        Cat cat = catRepository.getById(id);
        Cat friend = catRepository.getById(friendId);

        cat.removeFriend(friend);
        friend.removeFriend(cat);
        catRepository.save(cat);
        catRepository.save(friend);
    }

    @KafkaListener(topics="getCatById", groupId = "cat", containerFactory = "catKafkaListenerContainerFactory")
    @SendTo("resultCat")
    public CatBuffer getById(ConsumerRecord<String, CatBuffer> record, @Header(KafkaHeaders.CORRELATION_ID) byte[] correlation) {
        record.headers().add(KafkaHeaders.CORRELATION_ID, correlation);
        System.out.println(record.value().getCats().get(0).getId());
        if (record.value().getUser().getRole() == RoleType.ADMIN) {
            Cat cat = catRepository.getById(record.value().getCats().get(0).getId());
            if (cat == null)
                return new CatBuffer(null);
            else
                return new CatBuffer(null, new CatTransfer(cat));
        }

        Cat cat = catRepository.getCatByIdAndOwner(record.value().getCats().get(0).getId(), new Owner(record.value().getUser().getOwner()));
        if (cat == null)
            return new CatBuffer(null);
        else
            return new CatBuffer(null, new CatTransfer(cat));
    }

    @KafkaListener(topics="getAllCats", groupId = "cat", containerFactory = "catKafkaListenerContainerFactory")
    @SendTo("resultCat")
    public CatBuffer getAll(ConsumerRecord<String, CatBuffer> record, @Header(KafkaHeaders.CORRELATION_ID) byte[] correlation) {
        record.headers().add(KafkaHeaders.CORRELATION_ID, correlation);

        if (record.value().getUser().getRole() == RoleType.ADMIN) {
            List<CatTransfer> ct = new ArrayList<>();
            catRepository.findAll().forEach(cat -> ct.add(new CatTransfer(cat)));
            return new CatBuffer(null, ct);
        }

        List<CatTransfer> ct = new ArrayList<>();
        catRepository.findCatsByOwner(new Owner(record.value().getUser().getOwner())).forEach(cat -> ct.add(new CatTransfer(cat)));
        return new CatBuffer(null, ct);
    }

    @KafkaListener(topics="getCatsByBreed", groupId = "cat", containerFactory = "catKafkaListenerContainerFactory")
    @SendTo("resultCat")
    public CatBuffer getByBreed(ConsumerRecord<String, CatBuffer> record, @Header(KafkaHeaders.CORRELATION_ID) byte[] correlation) {
        record.headers().add(KafkaHeaders.CORRELATION_ID, correlation);

        if (record.value().getUser().getRole() == RoleType.ADMIN) {
            List<CatTransfer> ct = new ArrayList<>();
            catRepository.findCatsByBreed(record.value().getCats().get(0).getBreed()).forEach(cat -> ct.add(new CatTransfer(cat)));
            return new CatBuffer(null, ct);
        }

        List<CatTransfer> ct = new ArrayList<>();
        catRepository.findCatsByOwnerAndBreed(new Owner(record.value().getUser().getOwner()), record.value().getCats().get(0).getBreed()).forEach(cat -> ct.add(new CatTransfer(cat)));
        return new CatBuffer(null, ct);
    }

    @KafkaListener(topics="getCatsByColor", groupId = "cat", containerFactory = "catKafkaListenerContainerFactory")
    @SendTo("resultCat")
    public CatBuffer getByColor(ConsumerRecord<String, CatBuffer> record, @Header(KafkaHeaders.CORRELATION_ID) byte[] correlation) {
        record.headers().add(KafkaHeaders.CORRELATION_ID, correlation);

        if (record.value().getUser().getRole() == RoleType.ADMIN) {
            List<CatTransfer> ct = new ArrayList<>();
            catRepository.findCatsByColor(record.value().getCats().get(0).getColor()).forEach(cat -> ct.add(new CatTransfer(cat)));
            return new CatBuffer(null, ct);
        }

        List<CatTransfer> ct = new ArrayList<>();
        catRepository.findCatsByOwnerAndColor(new Owner(record.value().getUser().getOwner()), record.value().getCats().get(0).getColor()).forEach(cat -> ct.add(new CatTransfer(cat)));
        return new CatBuffer(null, ct);
    }

    @KafkaListener(topics="updateCat", groupId = "cat", containerFactory = "catKafkaListenerContainerFactory")
    public boolean update(ConsumerRecord<String, CatBuffer> record) {
        if (record.value().getUser().getRole() != RoleType.ADMIN
            || record.value().getCats().get(0).getOwnerId() != record.value().getUser().getOwner().getId())
            return false;

        int id = record.value().getCats().get(0).getId();
        if (catRepository.existsById(id)) {
            Cat oldCat = catRepository.getById(id);
            oldCat.copy(new Cat(record.value().getCats().get(0)));
            catRepository.save(oldCat);
            return true;
        }

        return false;
    }

    @Transactional
    @KafkaListener(topics="removeCat", groupId = "cat", containerFactory = "catKafkaListenerContainerFactory")
    public boolean remove(ConsumerRecord<String, CatBuffer> record) {
        int id = record.value().getCats().get(0).getId();
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
}
