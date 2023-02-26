package com.spirittesting.db.frontend;

import com.spirittesting.db.database.ConnectionFactory;
import com.spirittesting.db.database.ConnectionProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DatabaseConnectDialogController implements Initializable {

    @FXML
    TextField jdbcUrlText;

    @FXML
    TableView<EditableConnectionProperty> propertiesTable;
    @FXML
    TableColumn<EditableConnectionProperty, String> keyColumn;
    @FXML
    TableColumn<EditableConnectionProperty, String> valueColumn;

    @FXML
    Button connectButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        jdbcUrlText.setText("jdbc:postgresql://localhost:5432/testoffice");

        propertiesTable.getItems().add(new EditableConnectionProperty("user", "testoffice"));
        propertiesTable.getItems().add(new EditableConnectionProperty("password", "testoffice"));
        propertiesTable.getItems().add(new EditableConnectionProperty("foo", "bar"));

        keyColumn.setCellValueFactory(data -> data.getValue().keyProperty());
        valueColumn.setCellValueFactory(data -> data.getValue().valueProperty());

        connectButton.addEventHandler(ActionEvent.ACTION, event -> {
            ConnectionFactory.getInstance().setJdbc(jdbcUrlText.getText());
            ConnectionFactory.getInstance().clearConnectionProperties();
            propertiesTable.getItems().forEach(property -> ConnectionFactory.getInstance().addConnectionProperty(property.toConnectionProperty()));

            Stage stage = (Stage) jdbcUrlText.getScene().getWindow();
            FXMLLoader loader = DbAnonymizerApplication.getFxmlLoader("fxml/DatabaseView.fxml");
            try {
                Parent parent = loader.load();
                stage.setTitle("Database " + jdbcUrlText.getText());
                stage.setScene(new Scene(parent));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });
    }


    private class EditableConnectionProperty {
        private StringProperty key;
        private StringProperty value;

        public EditableConnectionProperty(String key, String value) {
            this.key = new SimpleStringProperty(key);
            this.value = new SimpleStringProperty(value);
        }

        public ConnectionProperty toConnectionProperty() {
            return new ConnectionProperty(key.get(), value.get());
        }

        public StringProperty keyProperty() {
            return key;
        }

        public void setKey(String key) {
            this.key.set(key);
        }

        public StringProperty valueProperty() {
            return value;
        }

        public void setValue(String value) {
            this.value.set(value);
        }
    }
}
