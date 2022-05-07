package ru.itmo.services.serv;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.data.dao.CatDAO;
import ru.itmo.data.dao.UserDAO;
import ru.itmo.data.entity.*;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CatServiceImpl implements CatService {

    private final CatDAO catRepository;
    private final UserDAO userRepository;

    @Autowired
    public CatServiceImpl(CatDAO catRepository, UserDAO userRepository) {
        this.catRepository = catRepository;
        this.userRepository = userRepository;
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

    public Cat getById(int id) {
        return getOwnersCat(catRepository.getById(id));
    }

    public List<Cat> getByBreed(String breed) {
        return getOwnersCats(catRepository.findCatsByBreed(BreedType.valueOf(breed)));
    }

    public List<Cat> getByColor(String color) {
        return getOwnersCats(catRepository.findCatsByColor(ColorType.valueOf(color)));
    }

    public List<Cat> getAll() {
        return getOwnersCats(catRepository.findAll());
    }

    public boolean update(int id, Cat cat) {
        if (getOwnersCat(cat) == null || getUser().getRole() != RoleType.ADMIN)
            return false;

        if (catRepository.existsById(id)) {
            Cat oldCat = getById(id);
            oldCat.copy(cat);
            catRepository.save(oldCat);
            return true;
        }

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

    private User getUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        return userRepository.findByUsername(username);
    }

    private List<Cat> getOwnersCats(List<Cat> allCats) {
        User user = getUser();

        List<Cat> cats = new ArrayList<Cat>();
        for (Cat cat : allCats) {
            if (cat.getOwner().getId() == user.getOwner().getId())
                cats.add(cat);
        }

        return Collections.unmodifiableList(cats);
    }

    private Cat getOwnersCat(Cat cat) {
        User user = getUser();

        if (cat.getOwner().getId() == user.getOwner().getId())
            return cat;

        return null;
    }
}
