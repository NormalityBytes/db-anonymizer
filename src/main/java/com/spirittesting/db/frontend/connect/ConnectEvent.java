package com.spirittesting.db.frontend.connect;

import com.spirittesting.db.database.ConnectionFactory;
import com.spirittesting.db.database.ConnectionProperty;
import com.spirittesting.db.frontend.DbAnonymizerApplication;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
        try {
            ConnectionFactory.getInstance().setJdbc(jdbc.getText());
            ConnectionFactory.getInstance().clearConnectionProperties();
            ConnectionFactory.getInstance().addConnectionProperty(new ConnectionProperty("user", username.getText()));
            ConnectionFactory.getInstance().addConnectionProperty(new ConnectionProperty("password", password.getText()));

            DbAnonymizerApplication.getInstance().setScene("Database " + jdbc.getText(), "/DatabaseViewScene.fxml");
        } catch (Exception e) {
            log.error("Error loading Database Scene", e);
        }
    }

}
