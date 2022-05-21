package ru.itmo.kotiki.data.dao;

import ru.itmo.kotiki.data.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerDAO extends JpaRepository<Owner, Integer> {

}
