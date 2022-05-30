package ru.itmo.kotiki.servicedata.transfer;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import ru.itmo.kotiki.servicedata.entity.RoleType;
import ru.itmo.kotiki.servicedata.entity.User;

public class UserTransfer {
    private int id = -1;
    private String username = null;
    private RoleType role = RoleType.USER;
    private OwnerTransfer owner = null;

    public UserTransfer() {
    }

    public UserTransfer(User user) {
        if (user == null)
            return;

        id = user.getId();
        username = user.getUsername();
        role = RoleType.valueOf(user.getAuthorities().toArray()[0].toString());
        owner = new OwnerTransfer(user.getOwner());
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public RoleType getRole() {
        return role;
    }

    @JsonGetter("role")
    public String getRoleString() {
        return role.toString();
    }

    @JsonSetter("role")
    public void setRoleString(String role) {
        this.role = RoleType.valueOf(role);
    }

    public OwnerTransfer getOwner() {
        return owner;
    }
}
