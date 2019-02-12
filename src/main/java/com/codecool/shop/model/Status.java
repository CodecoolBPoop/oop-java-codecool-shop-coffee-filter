package com.codecool.shop.model;

import java.util.Arrays;
import java.util.Optional;
import java.util.OptionalInt;

public enum Status {
    NEW ("new", 1),
    CHECKED ("checked", 2),
    PAID ("paid", 3),
    CONFIRMED ("confirmed", 4),
    SHIPPED ("shipped", 5);

    private final String name;
    private final int intValue;

    Status(String name, int intValue) {
        this.name = name;
        this.intValue = intValue;
    }

    public String getName() {
        return name;
    }

    public static Status getStatusByIntValue(int intValue) {
        return Arrays.stream(Status.values()).filter(s -> s.intValue == intValue).findFirst().orElse(null);
    }
}
