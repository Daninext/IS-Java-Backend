package ru.itmo.data.entity;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private RoleType role;

    @OneToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private Owner owner;

    public User() { }

    public User(String username, String password, RoleType role, Owner owner) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.owner = owner;
    }

    public String getUsername() {
        return username;
    }

    public RoleType getRole() {
        return role;
    }

    public Owner getOwner() {
        return owner;
    }
}
