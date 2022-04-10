package ru.itmo.data.dao;

import ru.itmo.data.entity.Cat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatDAO extends JpaRepository<Cat, Integer> {

}
