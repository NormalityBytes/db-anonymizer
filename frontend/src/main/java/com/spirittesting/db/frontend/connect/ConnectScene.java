package com.spirittesting.db.frontend.connect;

import com.spirittesting.db.frontend.DbAnonymizerApplication;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class ConnectScene extends Scene {
    public ConnectScene() {
        super(new FlowPane(Orientation.VERTICAL, 10, 10), 320, 300);

        DbAnonymizerApplication.primaryStage.setResizable(true);
        DbAnonymizerApplication.primaryStage.setTitle("Connect to Database");

        TextField jdbc = new TextField("jdbc:postgresql://localhost:5432/profiler");
        jdbc.setPrefWidth(300);
        TextField user = new TextField("profiler");
        user.setPrefWidth(300);
        PasswordField password = new PasswordField();
        password.setPrefWidth(300);
        password.setText("profiler");
        Button connect = new Button("Connect");
        connect.setDefaultButton(true);
        connect.setPrefWidth(300);
        connect.setOnAction(new ConnectEvent(jdbc, user, password));

        ((Pane) getRoot()).getChildren().addAll(new Text("JDBC URL"), jdbc, new Text("User"), user, new Text("Password"), password, connect);
        ((FlowPane) getRoot()).setPadding(new javafx.geometry.Insets(10));
    }

}
