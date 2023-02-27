package com.spirittesting.db.frontend;

import com.spirittesting.db.database.Column;
import com.spirittesting.db.database.ConnectionFactory;
import com.spirittesting.db.database.Table;
import com.spirittesting.db.frontend.cellfactories.TableCellFactory;
import com.spirittesting.db.frontend.dialogs.ConnectDialog;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class MainViewController implements Initializable {

    @FXML
    ListView<Table> tablesList;
    @FXML
    TableView<String> columnsTable;
    @FXML
    ListView<String> actionsList;
    @FXML
    TextArea logView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // forward all log messages to the logView
        Logger.getLogger("").addHandler(new LoggingHandler(logView));
        tablesList.setCellFactory(new TableCellFactory());
        /* tablesList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                columnsTable.getItems().clear();
                columnsTable.getItems().addAll(Column.getColumns(ConnectionFactory.getInstance().getConnection(), newValue.descriptor()));
            }
        }*/
    }

    public void doConnect(ActionEvent actionEvent) throws IOException {
        ConnectDialog dialog = new ConnectDialog();
        dialog.showAndWait().ifPresent(params -> {
            Logger.getLogger("Main").info("Connecting to " + params.jdbc() + " with parameters " + params.properties());
            ConnectionFactory.getInstance().setJdbc(params.jdbc());
            ConnectionFactory.getInstance().clearConnectionProperties();
            params.properties().forEach(property -> ConnectionFactory.getInstance().addConnectionProperty(property));
            Connection connection = ConnectionFactory.getInstance().getConnection();
            tablesList.getItems().addAll(Table.getTables(connection, "TABLE"));
        });
    }
}
