package ru.itmo.services.serv;

import ru.itmo.data.dao.OwnerDAO;
import ru.itmo.data.entity.Owner;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

@Service
public class OwnerServiceImpl implements OwnerService {

    private final OwnerDAO ownerRepository;

    @Autowired
    public OwnerServiceImpl(OwnerDAO ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    public void add(Owner owner) {
        ownerRepository.save(owner);
    }

    public boolean update(int id, Owner owner) {
        if (ownerRepository.existsById(owner.getId())) {
            Owner oldOwner = getById(id);
            oldOwner.copy(owner);
            ownerRepository.save(owner);
            return true;
        }

        return false;
    }

    public Owner getById(int id) {
        return ownerRepository.getById(id);
    }

    public List<Owner> getAll() {
        return Collections.unmodifiableList(ownerRepository.findAll());
    }

    public boolean remove(int id) {
        if (ownerRepository.existsById(id)) {
            ownerRepository.deleteById(id);
            return true;
        }

        return false;
    }
}
