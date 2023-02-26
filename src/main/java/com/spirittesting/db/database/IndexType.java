package com.spirittesting.db.database;

public enum IndexType {
    TABLE_INDEX_CLUSTERED(1),
    TABLE_INDEX_HASHED(2),
    TABLE_INDEX_OTHER(3);

    private final int value;

    IndexType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static IndexType fromValue(int value) {
        for (IndexType type : values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unbekannter Wert: " + value);
    }
}
