package ru.itmo.kotiki.servicedata.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itmo.kotiki.servicedata.entity.User;

@Repository
public interface UserDAO extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}
