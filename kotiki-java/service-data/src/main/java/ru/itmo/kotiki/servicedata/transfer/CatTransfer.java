package ru.itmo.kotiki.servicedata.transfer;

import ru.itmo.kotiki.servicedata.entity.Cat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CatTransfer {
    private Integer id = -1;
    private String name = null;
    private Date birthday = new Date();
    private String breed = null;
    private String color = null;
    private List<Integer> friendsIds = new ArrayList<>();
    private Integer ownerId = -1;

    public CatTransfer() {
    }

    public CatTransfer(Cat cat) {
        id = cat.getId();
        name = cat.getName();
        birthday = cat.getBirthday();
        breed = cat.getBreed().toString();
        color = cat.getColor().toString();
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

    public String getBreed() {
        return breed;
    }

    public String getColor() {
        return color;
    }

    public int getOwnerId() {
        return  ownerId;
    }

    public List<Integer> getFriendsIds() {
        return friendsIds;
    }
}
