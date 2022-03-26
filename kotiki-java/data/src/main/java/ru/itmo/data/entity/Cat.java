package ru.itmo.data.entity;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "cats")
public class Cat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "birthday")
    private Date birthday;

    @Enumerated(EnumType.STRING)
    @Column(name = "breed")
    private BreedType breed;

    @ManyToMany(fetch = FetchType.EAGER
            , cascade = {CascadeType.ALL})
    @JoinTable (
            name = "cat_friends",
            joinColumns = @JoinColumn(name = "cat_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private List<Cat> friends = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "color")
    private ColorType color;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Owner owner;

    public Cat() { }

    public Cat(String name, Date birthday, BreedType breed, ColorType color, Owner owner) {
        this.name = name;
        this.birthday = birthday;
        this.breed = breed;
        this.color = color;
        this.owner = owner;
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

    public BreedType getBreed() {
        return breed;
    }

    public ColorType getColor() {
        return color;
    }

    public List<Cat> getFriends() {
        return Collections.unmodifiableList(friends);
    }

    public Owner getOwner() {
        return owner;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public void addFriend(Cat cat) {
        friends.add(cat);
    }

    public void removeFriend(Cat cat) { friends.remove(cat); }

    public void clearFriends() { friends.clear(); }
}
