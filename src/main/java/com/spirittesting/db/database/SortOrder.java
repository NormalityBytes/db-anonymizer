package com.spirittesting.db.database;

public enum SortOrder {
    ASC("A"),
    DESC("D"),
    UNSPECIFIED("U");

    private final String value;

    SortOrder(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static SortOrder fromValue(String value) {
        for (SortOrder order : values()) {
            if (order.getValue().equals(value)) {
                return order;
            }
        }
        return SortOrder.UNSPECIFIED;
    }
}
