package com.spirittesting.db.frontend.connect;

import com.spirittesting.db.database.ConnectionFactory;
import com.spirittesting.db.database.ConnectionProperty;
import com.spirittesting.db.frontend.DbAnonymizerApplication;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

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

        keyColumn.setCellValueFactory(data -> data.getValue().getKey());
        valueColumn.setCellValueFactory(data -> data.getValue().getValue());

        connectButton.addEventHandler(ActionEvent.ACTION, event -> {
            ConnectionFactory.getInstance().setJdbc(jdbcUrlText.getText());
            ConnectionFactory.getInstance().clearConnectionProperties();
            propertiesTable.getItems().forEach(property -> ConnectionFactory.getInstance().addConnectionProperty(property.toConnectionProperty()));

            Stage stage = (Stage) jdbcUrlText.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DatabaseViewScene.fxml"));
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
        @Getter
        @Setter
        private StringProperty key;
        @Getter
        @Setter
        private StringProperty value;

        public EditableConnectionProperty(String key, String value) {
            this.key = new SimpleStringProperty(key);
            this.value = new SimpleStringProperty(value);
        }

        public ConnectionProperty toConnectionProperty() {
            return new ConnectionProperty(key.get(), value.get());
        }
    }
}
