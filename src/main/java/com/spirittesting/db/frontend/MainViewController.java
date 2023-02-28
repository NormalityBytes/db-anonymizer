package com.spirittesting.db.frontend;

import com.spirittesting.db.database.Column;
import com.spirittesting.db.database.ConnectionFactory;
import com.spirittesting.db.database.Table;
import com.spirittesting.db.frontend.cellfactories.TableCellFactory;
import com.spirittesting.db.frontend.components.TableComponent;
import com.spirittesting.db.frontend.dialogs.ConnectDialog;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class MainViewController implements Initializable {

    @FXML
    ListView<Table> tablesList;
    @FXML
    Pane tablesPane;
    @FXML
    ListView<String> actionsList;
    @FXML
    TextArea logView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // forward all log messages to the logView
        Logger.getLogger("").addHandler(new LoggingHandler(logView));
        tablesList.setCellFactory(new TableCellFactory());
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

            tablesPane.getChildren().add(new TableComponent().getView());
            tablesPane.getChildren().add(new TableComponent().getView());

        });
    }
}
