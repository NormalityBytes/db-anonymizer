package com.spirittesting.db.frontend.databaseview;

import com.spirittesting.db.frontend.DbAnonymizerApplication;
import com.spirittesting.db.model.Reader;
import com.spirittesting.db.model.TableDefinition;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.util.Set;

public class DatabaseViewScene extends Scene {

    private final String jdbc;
    private final String username;
    private final String password;

    private BorderPane root;

    public DatabaseViewScene(String jdbc, String username, String password) {
        super(new BorderPane(), 1440, 900);
        this.root = (BorderPane) getRoot();
        this.jdbc = jdbc;
        this.username = username;
        this.password = password;

        DbAnonymizerApplication.primaryStage.setTitle("Database " + jdbc);

        Reader reader = new Reader(jdbc, username, password);
        Set<TableDefinition> tableDefinitions = reader.getTableDefinitions();

        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setWrapText(true);
        tableDefinitions.stream().filter(def -> "TABLE".equals(def.type())).forEach(tableDefinition -> textArea.appendText(tableDefinition.toString() + "\n"));
        root.setCenter(textArea);
    }
}
