package com.min204.coseproject.constant;

public enum UserRoles {
    USER("USER"),
    ADMIN("ADMIN");
    // Add more roles as needed

    private final String role;

    UserRoles(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;

    }
}

