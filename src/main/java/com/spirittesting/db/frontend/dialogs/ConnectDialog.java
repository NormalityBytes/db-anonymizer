package com.spirittesting.db.frontend.dialogs;

import com.spirittesting.db.database.ConnectionProperty;
import com.spirittesting.db.frontend.DatabaseConnectionParameters;
import com.spirittesting.db.frontend.DbAnonymizerApplication;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

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
        addPropertyButton.setOnAction(event -> content.getChildren().add(createPropertyBox()));

        try {
            // load last connection properties
            String lastJdbc = getLastJdbc();
            if (lastJdbc != null) {
                jdbcTextField.setText(lastJdbc);
            }

            List<ConnectionProperty> lastConnectionProperties = getLastConnectionProperties();
            if (lastConnectionProperties != null) {
                lastConnectionProperties.forEach(property -> content.getChildren().add(createPropertyBox(property)));
            }
        } catch (Exception e) {
            Logger.getLogger("ConnectDialog").warning("Could not load last connection properties: " + e.getMessage());
        }

        setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                String jdbcUrl = jdbcTextField.getText();
                // convert all properties to ConnectionProperty
                List<ConnectionProperty> connectionProperties = content.getChildren().stream()
                                                                        .filter(node -> node instanceof HBox)
                                                                        .map(node -> (HBox) node)
                                                                        .map(hbox -> new ConnectionProperty(((TextField) hbox.getChildren().get(1)).getText(), ((TextField) hbox.getChildren().get(3)).getText()))
                                                                        .toList();
                setLastJdbc(jdbcUrl);
                setLastConnectionProperties(connectionProperties);
                return new DatabaseConnectionParameters(jdbcUrl, connectionProperties);
            }
            return null;
        });

        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
    }

    private Parent createPropertyBox() {
        return createPropertyBox(new ConnectionProperty("", ""));
    }

    private Parent createPropertyBox(ConnectionProperty property) {
        HBox propertyBox = new HBox();
        propertyBox.setSpacing(10);
        propertyBox.getChildren().add(new Label("Key:"));
        TextField keyTextField = new TextField(property.key());
        propertyBox.getChildren().add(keyTextField);
        propertyBox.getChildren().add(new Label("Value:"));
        TextField valueTextField = new TextField(property.value());
        valueTextField.setPrefWidth(300);
        propertyBox.getChildren().add(valueTextField);
        return propertyBox;
    }

    public String getLastJdbc() {
        Preferences prefs = Preferences.userNodeForPackage(DbAnonymizerApplication.class);
        Logger.getLogger("ConnectDialog").info("Last JDBC: " + prefs.get("jdbc", ""));
        return prefs.get("jdbc", null);
    }

    public List<ConnectionProperty> getLastConnectionProperties() {
        Preferences prefs = Preferences.userNodeForPackage(DbAnonymizerApplication.class);
        Logger.getLogger("ConnectDialog").info("Last connection properties: " + prefs.get("connectionProperties", null));

        return prefs.get("connectionProperties", null) == null ? null :  Arrays.stream(prefs.get("connectionProperties", "").split(";"))
                       .map(property -> property.split("="))
                       .map(property -> new ConnectionProperty(property[0], property[1]))
                       .toList();
    }

    public void setLastJdbc(String jdbc) {
        Preferences prefs = Preferences.userNodeForPackage(DbAnonymizerApplication.class);
        if (jdbc != null) {
            prefs.put("jdbc", jdbc);
        } else {
            prefs.remove("jdbc");
        }
    }

    public void setLastConnectionProperties(List<ConnectionProperty> connectionProperties) {
        Preferences prefs = Preferences.userNodeForPackage(DbAnonymizerApplication.class);
        if (connectionProperties != null) {
            prefs.put("connectionProperties", connectionProperties.stream().map(property -> property.key() + "=" + property.value()).reduce((a, b) -> a + ";" + b).orElse(""));
        } else {
            prefs.remove("connectionProperties");
        }
    }

}
