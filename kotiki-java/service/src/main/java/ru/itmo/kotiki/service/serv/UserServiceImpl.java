package ru.itmo.kotiki.service.serv;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.itmo.kotiki.data.dao.UserDAO;
import ru.itmo.kotiki.data.entity.User;

@Service
public class UserServiceImpl implements UserService {

    private final UserDAO userRepository;

    @Autowired
    public UserServiceImpl(UserDAO userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void add(User user) {
        userRepository.save(user);
    }

    @Override
    public User getById(int id) {
        return userRepository.getById(id);
    }

    @Override
    public boolean remove(int id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }

        return false;
    }
}
