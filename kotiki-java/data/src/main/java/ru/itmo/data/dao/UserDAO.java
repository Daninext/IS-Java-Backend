package ru.itmo.data.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itmo.data.entity.User;

@Repository
public interface UserDAO extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}
