package ru.itmo.services.serv;

import ru.itmo.data.dao.OwnerDAO;
import ru.itmo.data.entity.Owner;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

@Service
public class OwnerServiceImpl implements OwnerService {

    private final OwnerDAO repository;

    @Autowired
    public OwnerServiceImpl(OwnerDAO repository) {
        this.repository = repository;
    }

    public void add(Owner owner) {
        repository.save(owner);
    }

    public boolean update(int id, Owner owner) {
        if (repository.existsById(owner.getId())) {
            Owner oldOwner = getById(id);
            oldOwner.copy(owner);
            repository.save(owner);
            return true;
        }

        return false;
    }

    public Owner getById(int id) {
        return repository.getById(id);
    }

    public List<Owner> getAll() {
        return Collections.unmodifiableList(repository.findAll());
    }

    public boolean remove(int id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }

        return false;
    }
}
