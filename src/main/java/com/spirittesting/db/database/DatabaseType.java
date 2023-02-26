package com.spirittesting.db.database;

import java.sql.Driver;
import java.util.regex.Pattern;

public enum DatabaseType {

    ORACLE("oracle.jdbc.OracleDriver", "jdbc:oracle:thin:@.*"),
    POSTGRES("org.postgresql.Driver", "jdbc:postgresql://.*"),
    MYSQL("com.mysql.cj.jdbc.Driver", "jdbc:mysql://.*"),
    SQLSERVER("com.microsoft.sqlserver.jdbc.SQLServerDriver", "jdbc:sqlserver://.*"),
    H2_MEMORY("org.h2.Driver", "jdbc:h2:mem:.*"),
    H2_FILE("org.h2.Driver", "jdbc:h2:file:.*"),
    H2_TCP("org.h2.Driver", "jdbc:h2:tcp://.*"),
    HSQLDB_MEMORY("org.hsqldb.jdbcDriver", "jdbc:hsqldb:mem:.*"),
    HSQLDB_FILE("org.hsqldb.jdbcDriver", "jdbc:hsqldb:file:.*"),
    HSQLDB_SERVER("org.hsqldb.jdbcDriver", "jdbc:hsqldb:hsql://.*"),
    DERBY_MEMORY("org.apache.derby.jdbc.EmbeddedDriver", "jdbc:derby:memory:.*"),
    DERBY_FILE("org.apache.derby.jdbc.EmbeddedDriver", "jdbc:derby:.*"),
    DERBY_SERVER("org.apache.derby.jdbc.ClientDriver", "jdbc:derby://.*"),
    DB2("com.ibm.db2.jcc.DB2Driver", "jdbc:db2://.*"),
    SQLITE("org.sqlite.JDBC", "jdbc:sqlite:.*"),
    FIREBIRD("org.firebirdsql.jdbc.FBDriver", "jdbc:firebirdsql:.*"),
    INFORMIX("com.informix.jdbc.IfxDriver", "jdbc:informix-sqli://.*"),
    SYBASE("com.sybase.jdbc3.jdbc.SybDriver", "jdbc:sybase:Tds:.*"),
    HANA("com.sap.db.jdbc.Driver", "jdbc:sap://.*");

    private final String driver;
    private final Pattern jdbcPattern;


    DatabaseType(String driver, String jdbcPattern) {
        this.driver = driver;
        this.jdbcPattern = Pattern.compile(jdbcPattern);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Driver> T getDriver(String jdbc) {
        try {
            for (DatabaseType type : values()) {
                if (type.jdbcPattern.matcher(jdbc).matches()) {
                    return (T) Class.forName(type.driver).getDeclaredConstructor().newInstance();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        throw new IllegalArgumentException("Unknown JDBC URL: " + jdbc);
    }

}
