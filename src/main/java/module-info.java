open module com.spirittesting.dbanonymizer {

    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.postgresql.jdbc;
    requires org.checkerframework.checker.qual;

    exports com.spirittesting.db.frontend to javafx.fxml, javafx.graphics;

}
