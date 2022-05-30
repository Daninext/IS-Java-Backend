package ru.itmo.kotiki.servicedata.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itmo.kotiki.servicedata.entity.Owner;

@Repository
public interface OwnerDAO extends JpaRepository<Owner, Integer> {

}
