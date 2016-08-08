package com.example;

import javax.management.relation.Role;

public class UserProfile {
    private String username;

    private String password;

    private Role[] roles;

    private String description;

    public UserProfile(String username, String password, Role[] roles, String description) {
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.description = description;
    }

    public String getUserName() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Role[] getRoles() {
        return roles;
    }

    public String getDescription() {
        return description;
    }
}