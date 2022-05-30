package ru.itmo.kotiki.servicedata.transfer;

import ru.itmo.kotiki.servicedata.entity.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CatBuffer {
    private List<CatTransfer> cats = new ArrayList<>();
    private UserTransfer user = null;

    public CatBuffer() {
    }

    public CatBuffer(User user, List<CatTransfer> cats) {
        this.user = new UserTransfer(user);
        this.cats = new ArrayList<>(cats);
    }

    public CatBuffer(User user, CatTransfer... cats) {
        this.user = new UserTransfer(user);
        this.cats = new ArrayList<>(Arrays.asList(cats));
    }

    public List<CatTransfer> getCats() {
        return cats;
    }

    public UserTransfer getUser() {
        return user;
    }
}
