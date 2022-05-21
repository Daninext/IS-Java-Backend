package ru.itmo.kotiki.servicedata.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.itmo.kotiki.servicedata.entity.BreedType;
import ru.itmo.kotiki.servicedata.entity.Cat;
import ru.itmo.kotiki.servicedata.entity.ColorType;
import ru.itmo.kotiki.servicedata.entity.Owner;

import java.util.List;

@Repository
public interface CatDAO extends JpaRepository<Cat, Integer> {
    List<Cat> findCatsByBreed(BreedType breed);

    List<Cat> findCatsByColor(ColorType color);

    List<Cat> findCatsByOwner(Owner owner);

    List<Cat> findCatsByOwnerAndColor(Owner owner, ColorType color);

    List<Cat> findCatsByOwnerAndBreed(Owner owner, BreedType breed);

    List<Cat> findCatsByOwnerId(Integer id);

    List<Cat> findCatsByOwnerIdAndColor(Integer id, ColorType color);

    List<Cat> findCatsByOwnerIdAndBreed(Integer id, BreedType breed);

    Cat getCatByIdAndOwner(Integer id, Owner owner);

    Cat getCatByIdAndOwnerId(Integer first, Integer second);
}
