package ru.itmo.kotiki.service.serv;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.kotiki.data.dao.CatDAO;
import ru.itmo.kotiki.data.dao.UserDAO;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import ru.itmo.kotiki.data.entity.*;

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
        User user = getUser();
        if (user.getAuthorities().contains(RoleType.ADMIN))
            return catRepository.getById(id);

        return catRepository.getCatByIdAndOwner(id, user.getOwner());
    }

    public List<Cat> getByBreed(String breed) {
        User user = getUser();
        if (user.getAuthorities().contains(RoleType.ADMIN))
            return Collections.unmodifiableList(catRepository.findCatsByBreed(BreedType.valueOf(breed)));

        return Collections.unmodifiableList(catRepository.findCatsByOwnerAndBreed(user.getOwner(), BreedType.valueOf(breed)));
    }

    public List<Cat> getByColor(String color) {
        User user = getUser();
        if (user.getAuthorities().contains(RoleType.ADMIN))
            return Collections.unmodifiableList(catRepository.findCatsByColor(ColorType.valueOf(color)));

        return Collections.unmodifiableList(catRepository.findCatsByOwnerAndColor(user.getOwner(), ColorType.valueOf(color)));
    }

    public List<Cat> getAll() {
        User user = getUser();
        if (user.getAuthorities().contains(RoleType.ADMIN))
            return Collections.unmodifiableList(catRepository.findAll());

        return Collections.unmodifiableList(catRepository.findCatsByOwner(user.getOwner()));
    }

    public boolean update(int id, Cat cat) {
        User user = getUser();
        if (!cat.isOwner(user.getOwner()) || !user.getAuthorities().contains(RoleType.ADMIN))
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
}
