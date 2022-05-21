package ru.itmo.kotiki.servicedata.transfer;

import com.fasterxml.jackson.annotation.JsonGetter;
import ru.itmo.kotiki.servicedata.entity.Cat;
import ru.itmo.kotiki.servicedata.entity.Owner;
import ru.itmo.kotiki.servicedata.entity.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OwnerTransfer {
    private Integer id = 0;
    private String name = null;
    private Date birthday = new Date();
    private List<CatTransfer> cats = new ArrayList<>();
    private Integer account = -1;

    public OwnerTransfer() {
    }

    public OwnerTransfer(Owner owner) {
        id = owner.getId();
        name = owner.getName();
        birthday = owner.getBirthday();

        for (Cat cat : owner.getCats()) {
            cats.add(new CatTransfer(cat));
        }

        account = owner.getAccountId();
    }

    public OwnerTransfer(Integer id) {
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

    public List<CatTransfer> getCats() {
        return cats;
    }

    public User getAccount() {
        return null;
    }

    @JsonGetter("account")
    public int getAccountId() {
        if (account == null)
            return -1;

        return account;
    }
}
