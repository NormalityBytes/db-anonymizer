package com.spirittesting.db.database;

public enum Deferrability {

    INITIALLY_DEFERRED(5),
    INITIALLY_IMMEDIATE(6),
    NOT_DEFERRABLE(7);

    private final int value;

    Deferrability(int value) {
        this.value = value;
    }

    public static Deferrability fromValue(int value) {
        for (Deferrability deferrability : values()) {
            if (deferrability.value == value) {
                return deferrability;
            }
        }
        throw new IllegalArgumentException("Unbekannter Deferrability-Wert: " + value);
    }

}
