package ru.itmo.data.entity;

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

    @OneToMany(mappedBy = "owner")
    private List<Cat> cats;

    public Owner() { }

    public Owner(String name, Date birthday) {
        this.name = name;
        this.birthday = birthday;
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
}
