package ru.itmo.data.dao;

import ru.itmo.data.entity.BreedType;
import ru.itmo.data.entity.Cat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itmo.data.entity.ColorType;
import ru.itmo.data.entity.Owner;

import java.util.List;

@Repository
public interface CatDAO extends JpaRepository<Cat, Integer> {
    List<Cat> findCatsByBreed(BreedType breed);

    List<Cat> findCatsByColor(ColorType color);

    List<Cat> findCatsByOwner(Owner owner);

    List<Cat> findCatsByOwnerAndColor(Owner owner, ColorType color);

    List<Cat> findCatsByOwnerAndBreed(Owner owner, BreedType breed);

    Cat getCatByIdAndOwner(Integer id, Owner owner);
}
