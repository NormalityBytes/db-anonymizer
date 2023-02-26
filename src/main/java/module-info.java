open module com.spirittesting.dbanonymizer {

    requires org.slf4j;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.postgresql.jdbc;
    requires lombok;
    requires org.checkerframework.checker.qual;

    exports com.spirittesting.db.frontend to javafx.graphics;
    exports com.spirittesting.db.frontend.databaseview to javafx.fxml;
    exports com.spirittesting.db.frontend.connect to javafx.fxml;

}
