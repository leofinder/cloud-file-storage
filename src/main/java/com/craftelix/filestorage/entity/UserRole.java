package com.craftelix.filestorage.entity;

public enum UserRole {
    USER,
    ADMIN;

    public String getRoleName() {
        return "ROLE_" + this.name();
    }
}
