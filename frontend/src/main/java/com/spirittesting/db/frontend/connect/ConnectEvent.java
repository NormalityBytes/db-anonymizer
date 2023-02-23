package com.spirittesting.db.frontend.connect;

import com.spirittesting.db.frontend.DbAnonymizerApplication;
import com.spirittesting.db.frontend.databaseview.DatabaseViewScene;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class ConnectEvent implements EventHandler<ActionEvent> {

    private final TextField jdbc;
    private final TextField username;
    private final PasswordField password;

    public ConnectEvent(TextField jdbc, TextField username, PasswordField password) {
        this.jdbc = jdbc;
        this.username = username;
        this.password = password;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        DatabaseViewScene databaseViewScene = new DatabaseViewScene(jdbc.getText(), username.getText(), password.getText());
        DbAnonymizerApplication.primaryStage.setScene(databaseViewScene);
    }

}
