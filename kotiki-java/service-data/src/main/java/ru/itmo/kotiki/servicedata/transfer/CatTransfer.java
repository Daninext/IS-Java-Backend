package ru.itmo.kotiki.servicedata.transfer;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import ru.itmo.kotiki.servicedata.entity.BreedType;
import ru.itmo.kotiki.servicedata.entity.Cat;
import ru.itmo.kotiki.servicedata.entity.ColorType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CatTransfer {
    private Integer id = -1;
    private String name = null;
    private Date birthday = new Date();
    private BreedType breed = BreedType.BENGAL;
    private ColorType color = ColorType.BLACK;
    private List<Integer> friendsIds = new ArrayList<>();
    private Integer ownerId = -1;

    public CatTransfer() {
    }

    public CatTransfer(Cat cat) {
        id = cat.getId();
        name = cat.getName();
        birthday = cat.getBirthday();
        breed = cat.getBreed();
        color = cat.getColor();
        friendsIds = new ArrayList<>(cat.getFriendsId());
        ownerId = cat.getOwner().getId();
    }

    public CatTransfer(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getBirthday() {
        return birthday;
    }

    public BreedType getBreed() {
        return breed;
    }

    @JsonGetter("breed")
    public String getBreedString() {
        return breed.toString();
    }

    @JsonSetter("breed")
    public void setBreedString(String breed) {
        this.breed = BreedType.valueOf(breed);
    }

    public ColorType getColor() {
        return color;
    }

    @JsonGetter("color")
    public String getColorString() {
        return color.toString();
    }

    @JsonSetter("color")
    public void setColorString(String color) {
        this.color = ColorType.valueOf(color);
    }

    public int getOwnerId() {
        return  ownerId;
    }

    public List<Integer> getFriendsIds() {
        return friendsIds;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
