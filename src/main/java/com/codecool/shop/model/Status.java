package com.codecool.shop.model;

public enum Status {
    NEW ("new"),
    CHECKED ("checked"),
    PAID ("paid"),
    CONFIRMED ("confirmed"),
    SHIPPED ("shipped");

    private final String name;

    Status(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
