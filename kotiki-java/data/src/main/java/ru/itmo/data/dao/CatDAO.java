package ru.itmo.data.dao;

import ru.itmo.data.entity.BreedType;
import ru.itmo.data.entity.Cat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itmo.data.entity.ColorType;

import java.util.List;

@Repository
public interface CatDAO extends JpaRepository<Cat, Integer> {
    List<Cat> findCatsByBreed(BreedType breed);

    List<Cat> findCatsByColor(ColorType color);
}
