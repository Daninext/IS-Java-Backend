package ru.itmo.kotiki.servicedata.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import ru.itmo.kotiki.servicedata.transfer.OwnerTransfer;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "owners")
public class Owner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "birthday")
    private Date birthday;

    @OneToMany(targetEntity = Cat.class, mappedBy = "owner")
    private List<Cat> cats;

    @OneToOne(mappedBy = "owner")
    private User account;

    public Owner() { }

    public Owner(String name, Date birthday) {
        this.name = name;
        this.birthday = birthday;
    }

    public Owner(int id, String name, Date birthday) {
        this.id = id;
        this.name = name;
        this.birthday = birthday;
    }

    public Owner(OwnerTransfer ownerTransfer) {
        this.name = ownerTransfer.getName();
        this.birthday = ownerTransfer.getBirthday();
        this.account = ownerTransfer.getAccount();
        //this.cats = new ArrayList<>(ownerTransfer.getCats());
    }

    public void copy(Owner owner) {
        this.name = owner.getName();
        this.birthday = owner.getBirthday();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getBirthday() {
        return birthday;
    }

    public List<Cat> getCats() {
        return Collections.unmodifiableList(cats);
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getAccount() {
        return account;
    }

    @JsonGetter("account")
    public int getAccountId() {
        if (account == null)
            return -1;

        return account.getId();
    }
}
