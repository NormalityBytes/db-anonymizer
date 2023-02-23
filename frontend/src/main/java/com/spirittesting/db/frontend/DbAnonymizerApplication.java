package com.spirittesting.db.frontend;

import com.spirittesting.db.frontend.connect.ConnectScene;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class DbAnonymizerApplication extends Application {

    public static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        Scene connectScene = new ConnectScene();
        stage.setScene(connectScene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
