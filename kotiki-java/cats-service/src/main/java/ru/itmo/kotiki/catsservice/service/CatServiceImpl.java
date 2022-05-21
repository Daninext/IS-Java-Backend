package ru.itmo.kotiki.catsservice.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.kafka.annotation.EnableKafka;

import ru.itmo.kotiki.servicedata.dao.*;
import ru.itmo.kotiki.servicedata.entity.*;
import ru.itmo.kotiki.servicedata.transfer.CatBuffer;
import ru.itmo.kotiki.servicedata.transfer.CatTransfer;
import ru.itmo.kotiki.servicedata.transfer.OwnerBuffer;

import java.util.Collections;
import java.util.List;

@EnableKafka
@Service
public class CatServiceImpl implements CatService {

    private final CatDAO catRepository;

    @Autowired
    public CatServiceImpl(CatDAO catRepository) {
        this.catRepository = catRepository;
    }

    public void add(Cat cat) {
        catRepository.save(cat);
    }

    public void addFriend(int id, int friendId) {
        Cat cat = catRepository.getById(id);
        Cat friend = catRepository.getById(friendId);

        cat.addFriend(friend);
        friend.addFriend(cat);
        catRepository.save(cat);
        catRepository.save(friend);
    }

    public void removeFriend(int id, int friendId) {
        Cat cat = catRepository.getById(id);
        Cat friend = catRepository.getById(friendId);

        cat.removeFriend(friend);
        friend.removeFriend(cat);
        catRepository.save(cat);
        catRepository.save(friend);
    }

    @KafkaListener(topics="getCatById")
    @SendTo("resultCat")
    public CatBuffer getById(ConsumerRecord<String, CatBuffer> record, @Header(KafkaHeaders.CORRELATION_ID) byte[] correlation) {
        record.headers().add(KafkaHeaders.CORRELATION_ID, correlation);

        if (RoleType.valueOf(record.value().getReqRole()) == RoleType.ADMIN)
            return new CatBuffer(null, -1, new CatTransfer(catRepository.getById(record.value().getBuffer().get(0).getId())));

        return new CatBuffer(null, -1, new CatTransfer(catRepository.getCatByIdAndOwnerId(record.value().getBuffer().get(0).getId(), record.value().getReqId())));
    }

    public List<Cat> getByBreed(String breed) {
        /*User user = getUser();
        if (user.getAuthorities().contains(RoleType.ADMIN))
            return Collections.unmodifiableList(catRepository.findCatsByBreed(BreedType.valueOf(breed)));

        return Collections.unmodifiableList(catRepository.findCatsByOwnerAndBreed(user.getOwner(), BreedType.valueOf(breed)));*/
        return null;
    }

    public List<Cat> getByColor(String color) {
        /*User user = getUser();
        if (user.getAuthorities().contains(RoleType.ADMIN))
            return Collections.unmodifiableList(catRepository.findCatsByColor(ColorType.valueOf(color)));

        return Collections.unmodifiableList(catRepository.findCatsByOwnerAndColor(user.getOwner(), ColorType.valueOf(color)));*/
        return null;
    }

    public List<Cat> getAll() {
        /*User user = getUser();
        if (user.getAuthorities().contains(RoleType.ADMIN))
            return Collections.unmodifiableList(catRepository.findAll());

        return Collections.unmodifiableList(catRepository.findCatsByOwner(user.getOwner()));*/
        return null;
    }

    public boolean update(int id, Cat cat) {
        /*User user = getUser();
        if (!cat.isOwner(user.getOwner()) || !user.getAuthorities().contains(RoleType.ADMIN))
            return false;

        if (catRepository.existsById(id)) {
            Cat oldCat = getById(id);
            oldCat.copy(cat);
            catRepository.save(oldCat);
            return true;
        }*/

        return false;
    }

    @Transactional
    public boolean remove(int id) {
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
