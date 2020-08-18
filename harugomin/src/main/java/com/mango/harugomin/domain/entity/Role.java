package com.mango.harugomin.domain.entity;

public enum Role {
    MEMBER("MEMBER"),
    ADMIN("ADMIN");

    String value;
    Role(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}