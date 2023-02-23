package com.spirittesting.db.model;

import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.Driver;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;

@Slf4j
public class Reader {

    private static final ThreadLocal<Connection> _connection = new ThreadLocal<>();
    private final String jdbc;
    private final String username;
    private final String password;

    public Reader(String jdbc, String username, String password) {
        this.jdbc = jdbc;
        this.username = username;
        this.password = password;
    }

    private Connection connect(String jdbc, String username, String password) {
        log.info("Connecting to {} with user {}", jdbc, username);
        Driver driver = new Driver();
        Properties props = new Properties();
        props.setProperty("user", username);
        props.setProperty("password", password);
        try {
            Connection connection = driver.connect(jdbc, props);
            assert connection != null;
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Synchronized
    private Connection getConnection() {
        if (_connection.get() == null) {
            log.debug("Creating new connection");
            _connection.set(connect(jdbc, username, password));
        }

        try {
            if (!_connection.get().isValid(1)) {
                log.debug("Recreating closed connection");
                _connection.set(connect(jdbc, username, password));
            } else {
                log.debug("Connection is still valid");
            }
        } catch (SQLException e) {
            log.error("Error while checking connection", e);
            throw new RuntimeException(e);
        }

        log.debug("Returning connection");
        return _connection.get();
    }

    public Set<ColumnDefinition> getColumnDefinitions(TableDescriptor tableDescriptor) {
        try {
            ResultSet resultSet = getConnection().getMetaData().getColumns(tableDescriptor.catalog(), tableDescriptor.schema(), tableDescriptor.table(), null);
            return parseColumnDefinitions(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Set<ColumnDefinition> getColumnDefinitions() {
        try {
            ResultSet resultSet = getConnection().getMetaData().getColumns(null, null, null, null);
            return parseColumnDefinitions(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Set<ColumnDefinition> parseColumnDefinitions(ResultSet resultSet) {
        Set<ColumnDefinition> columnDefinitions = new HashSet<>();
        try {
            while (resultSet.next()) {
                columnDefinitions.add(new ColumnDefinition(
                        new ColumnDescriptor(
                                new TableDescriptor(
                                        resultSet.getString("TABLE_CAT"),
                                        resultSet.getString("TABLE_SCHEM"),
                                        resultSet.getString("TABLE_NAME")
                                ),
                                resultSet.getString("COLUMN_NAME")
                        ),
                        resultSet.getInt("DATA_TYPE"),
                        resultSet.getString("TYPE_NAME"),
                        resultSet.getInt("COLUMN_SIZE"),
                        resultSet.getInt("DECIMAL_DIGITS"),
                        resultSet.getInt("NUM_PREC_RADIX"),
                        switch (resultSet.getInt("NULLABLE")) {
                            case DatabaseMetaData.columnNoNulls -> Optional.of(false);
                            case DatabaseMetaData.columnNullable -> Optional.of(true);
                            default -> Optional.empty();
                        },
                        resultSet.getString("REMARKS"),
                        resultSet.getString("COLUMN_DEF"),
                        resultSet.getInt("CHAR_OCTET_LENGTH"),
                        resultSet.getInt("ORDINAL_POSITION"),
                        resultSet.getString("SCOPE_CATALOG"),
                        resultSet.getString("SCOPE_SCHEMA"),
                        resultSet.getString("SCOPE_TABLE"),
                        resultSet.getInt("SOURCE_DATA_TYPE"),
                        switch (resultSet.getString("IS_AUTOINCREMENT")) {
                            case "YES" -> Optional.of(true);
                            case "NO" -> Optional.of(false);
                            default -> Optional.empty();
                        },
                        switch (resultSet.getString("IS_GENERATEDCOLUMN")) {
                            case "YES" -> Optional.of(true);
                            case "NO" -> Optional.of(false);
                            default -> Optional.empty();
                        }
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return columnDefinitions;
    }

    public Set<TableDefinition> getTableDefinitions() {
        Set<TableDefinition> tableDefinitions = new HashSet<>();
        try {
            DatabaseMetaData metaData = getConnection().getMetaData();
            ResultSet resultSet = metaData.getTables(null, null, null, null);
            while (resultSet.next()) {
                tableDefinitions.add(new TableDefinition(
                        new TableDescriptor(
                                resultSet.getString("TABLE_CAT"),
                                resultSet.getString("TABLE_SCHEM"),
                                resultSet.getString("TABLE_NAME")),
                        resultSet.getString("TABLE_TYPE"),
                        resultSet.getString("REMARKS"),
                        resultSet.getString("TYPE_CAT"),
                        resultSet.getString("TYPE_SCHEM"),
                        resultSet.getString("TYPE_NAME"),
                        resultSet.getString("SELF_REFERENCING_COL_NAME"),
                        resultSet.getString("REF_GENERATION")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tableDefinitions;
    }

    public Set<ForeignKeyDefinition> getForeignKeyDefinitions(TableDescriptor tableDescriptor) {
        Set<ForeignKeyDefinition> foreignKeyDefinitions = new HashSet<>();
        try {
            DatabaseMetaData metaData = getConnection().getMetaData();
            ResultSet resultSet = metaData.getImportedKeys(tableDescriptor.catalog(), tableDescriptor.schema(), tableDescriptor.table());
            while (resultSet.next()) {
                foreignKeyDefinitions.add(new ForeignKeyDefinition(
                        new ColumnDescriptor(
                                new TableDescriptor(
                                        resultSet.getString("PKTABLE_CAT"),
                                        resultSet.getString("PKTABLE_SCHEM"),
                                        resultSet.getString("PKTABLE_NAME")
                                ),
                                resultSet.getString("PKCOLUMN_NAME")
                        ),
                        new ColumnDescriptor(
                                new TableDescriptor(
                                        resultSet.getString("FKTABLE_CAT"),
                                        resultSet.getString("FKTABLE_SCHEM"),
                                        resultSet.getString("FKTABLE_NAME")
                                ),
                                resultSet.getString("FKCOLUMN_NAME")
                        ),
                        resultSet.getShort("KEY_SEQ"),
                        ForeignKeyDefinition.UpdateRule.fromValue(resultSet.getShort("UPDATE_RULE")),
                        ForeignKeyDefinition.UpdateRule.fromValue(resultSet.getShort("DELETE_RULE")),
                        resultSet.getString("FK_NAME"),
                        resultSet.getString("PK_NAME"),
                        ForeignKeyDefinition.Deferrability.fromValue(resultSet.getShort("DEFERRABILITY"))
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return foreignKeyDefinitions;
    }

    public Set<IndexDefinition> getIndexDefinitions(TableDescriptor tableDescriptor) {
        Set<IndexDefinition> indexDefinitions = new HashSet<>();
        try {
            DatabaseMetaData metaData = getConnection().getMetaData();
            ResultSet resultSet = metaData.getIndexInfo(tableDescriptor.catalog(), tableDescriptor.schema(), tableDescriptor.table(), false, false);
            while (resultSet.next()) {
                indexDefinitions.add(new IndexDefinition(
                        new TableDescriptor(
                                resultSet.getString("TABLE_CAT"),
                                resultSet.getString("TABLE_SCHEM"),
                                resultSet.getString("TABLE_NAME")),
                        resultSet.getBoolean("NON_UNIQUE"),
                        resultSet.getString("INDEX_QUALIFIER"),
                        resultSet.getString("INDEX_NAME"),
                        IndexDefinition.IndexType.fromValue(resultSet.getShort("TYPE")),
                        resultSet.getShort("ORDINAL_POSITION"),
                        resultSet.getString("COLUMN_NAME"),
                        IndexDefinition.SortOrder.fromValue(resultSet.getString("ASC_OR_DESC")),
                        resultSet.getInt("CARDINALITY"),
                        resultSet.getInt("PAGES"),
                        resultSet.getString("FILTER_CONDITION")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return indexDefinitions;
    }

}
