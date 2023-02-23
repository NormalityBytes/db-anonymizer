package com.spirittesting.db.model;

public record ForeignKeyDefinition(
        String pkCatalog,
        String pkSchema,
        String pkTable,
        String pkColumn,
        String fkCatalog,
        String fkSchema,
        String fkTable,
        String fkColumn,
        int keySeq,
        UpdateRule updateRule,
        UpdateRule deleteRule,
        String fkName,
        String pkName,
        Deferrability deferrability) {

    public enum Deferrability {
        INITIALLY_DEFERRED(5),
        INITIALLY_IMMEDIATE(6),
        NOT_DEFERRABLE(7);

        private final int value;

        Deferrability(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
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

}
