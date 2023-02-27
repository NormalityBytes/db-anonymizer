open module com.spirittesting.dbanonymizer {

    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires java.sql;
    requires org.postgresql.jdbc;
    requires org.checkerframework.checker.qual;
    requires java.prefs;

    exports com.spirittesting.db.frontend to javafx.fxml, javafx.graphics;
    exports com.spirittesting.db.frontend.dialogs to javafx.fxml, javafx.graphics;
    exports com.spirittesting.db.database to javafx.fxml, javafx.graphics;
    exports com.spirittesting.db.frontend.cellfactories to javafx.fxml, javafx.graphics;
}
