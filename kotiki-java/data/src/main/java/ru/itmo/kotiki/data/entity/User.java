package ru.itmo.kotiki.data.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collection;

@Entity
@Table(name = "users")
public class User implements UserDetails {
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

    @Column(name = "enabled")
    private boolean enabled = true;

    public User() { }

    public User(String username, String password, RoleType role, Owner owner) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.owner = owner;
    }

    public int getId() {
        return id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(role);
    }

    @Override
    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public Owner getOwner() {
        return owner;
    }

    @JsonGetter("owner")
    public int getOwnerId() {
        return owner.getId();
    }

    @JsonSetter("role")
    private void setRole(String role) {
        this.role = RoleType.valueOf(role);
    }

    @JsonSetter("password")
    private void setPassword(String password) {
        this.password = new BCryptPasswordEncoder().encode(password);
    }
}
