package com.spirittesting.db.frontend;

import com.spirittesting.db.database.ConnectionProperty;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class ConnectDialog extends Dialog<DatabaseConnectionParameters> {

    public ConnectDialog() {
        setTitle("Connect to Database");
        setHeaderText("Enter the connection parameters for the database you want to anonymize");

        VBox content = new VBox();
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setPrefSize(600, 400);


        getDialogPane().setContent(scrollPane);

        content.getChildren().add(new Label("Connection URL:"));
        TextField jdbcTextField = new TextField("jdbc:postgresql://localhost:5432/testoffice");
        content.getChildren().add(jdbcTextField);

        Button addPropertyButton = new Button("Add Property");
        content.getChildren().add(addPropertyButton);
        addPropertyButton.setOnAction(event -> {
            HBox propertyBox = new HBox();
            propertyBox.setSpacing(10);
            propertyBox.getChildren().add(new Label("Key:"));
            TextField keyTextField = new TextField();
            propertyBox.getChildren().add(keyTextField);
            propertyBox.getChildren().add(new Label("Value:"));
            TextField valueTextField = new TextField();
            valueTextField.setPrefWidth(300);
            propertyBox.getChildren().add(valueTextField);
            content.getChildren().add(propertyBox);
        });


        setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                String jdbcUrl = jdbcTextField.getText();
                // convert all properties to ConnectionProperty
                List<ConnectionProperty> connectionProperties = content.getChildren().stream()
                                                                        .filter(node -> node instanceof HBox)
                                                                        .map(node -> (HBox) node)
                                                                        .map(hbox -> new ConnectionProperty(((TextField) hbox.getChildren().get(1)).getText(), ((TextField) hbox.getChildren().get(3)).getText()))
                                                                        .toList();
                //return new DatabaseConnectionParameters("jdbc:postgresql://localhost:5432/testoffice", new ConnectionProperty("user", "testoffice"), new ConnectionProperty("password", "testoffice"));
                return new DatabaseConnectionParameters(jdbcUrl, connectionProperties);
            }
            return null;
        });

        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
    }


}
