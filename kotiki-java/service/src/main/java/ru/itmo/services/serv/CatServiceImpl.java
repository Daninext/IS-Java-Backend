package ru.itmo.services.serv;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.data.dao.CatDAO;
import ru.itmo.data.entity.*;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

@Service
@ComponentScan("ru.itmo.data")
public class CatServiceImpl implements CatService {

    private final CatDAO repository;

    @Autowired
    public CatServiceImpl(CatDAO repository) {
        this.repository = repository;
    }

    public void add(Cat cat) {
        repository.save(cat);
    }

    public void addFriend(int id, int friendId) {
        Cat cat = repository.getById(id);
        Cat friend = repository.getById(friendId);

        cat.addFriend(friend);
        friend.addFriend(cat);
        repository.save(cat);
        repository.save(friend);
    }

    public void removeFriend(int id, int friendId) {
        Cat cat = repository.getById(id);
        Cat friend = repository.getById(friendId);

        cat.removeFriend(friend);
        friend.removeFriend(cat);
        repository.save(cat);
        repository.save(friend);
    }

    public Cat getById(int id) {
        return repository.getById(id);
    }

    public List<Cat> getByBreed(String breed) {
        return Collections.unmodifiableList(repository.findCatsByBreed(BreedType.valueOf(breed)));
    }

    public List<Cat> getByColor(String color) {
        return Collections.unmodifiableList(repository.findCatsByColor(ColorType.valueOf(color)));
    }

    public List<Cat> getAll() {
        return Collections.unmodifiableList(repository.findAll());
    }

    public boolean update(int id, Cat cat) {
        if (repository.existsById(id)) {
            Cat oldCat = getById(id);
            oldCat.copy(cat);
            repository.save(oldCat);
            return true;
        }

        return false;
    }

    @Transactional
    public boolean remove(int id) {
        if (repository.existsById(id)) {
            Cat cat = repository.getById(id);
            for (Cat friend: cat.getFriends())
                friend.removeFriend(cat);

            repository.saveAll(cat.getFriends());
            cat.clearFriends();
            repository.deleteById(id);
            return true;
        }

        return false;
    }
}
