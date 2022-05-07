package ru.itmo.data.entity;

public enum RoleType {
    USER("USER"), ADMIN("ADMIN");

    String role;

    private RoleType(final String role){
        this.role = role;
    }

    public String getRole(){
        return role;
    }
}
