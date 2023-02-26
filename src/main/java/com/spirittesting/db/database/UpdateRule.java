package com.spirittesting.db.database;

public enum UpdateRule {
    NO_ACTION(0),
    CASCADE(1),
    SET_NULL(2),
    SET_DEFAULT(3),
    RESTRICT(4);

    private final int value;

    UpdateRule(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static UpdateRule fromValue(int value) {
        for (UpdateRule rule : values()) {
            if (rule.getValue() == value) {
                return rule;
            }
        }
        throw new IllegalArgumentException("Unbekannter Wert: " + value);
    }
}
