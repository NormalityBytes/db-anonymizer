module com.spirittesting.dbanonymizer {

    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.postgresql.jdbc;
    requires lombok;
    requires org.slf4j;
    requires logback.classic;
    requires logback.core;

    exports com.spirittesting.db.frontend;

    exports com.spirittesting.db.frontend.databaseview to javafx.fxml;
    opens com.spirittesting.db.frontend.databaseview to javafx.fxml;

}
