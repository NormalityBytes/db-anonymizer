package com.spirittesting.db.model;

public record IndexDefinition(
        String catalog,
        String schema,
        String table,
        boolean nonUnique,
        String indexQualifier,
        String indexName,
        IndexType type,
        int ordinalPosition,
        String columnName,
        SortOrder ascOrDesc,
        int cardinality,
        int pages,
        String filterCondition) {


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

}
