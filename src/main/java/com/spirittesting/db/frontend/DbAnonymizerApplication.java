package com.spirittesting.db.frontend;

import com.spirittesting.db.frontend.connect.ConnectScene;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DbAnonymizerApplication extends Application {

    public static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        Scene connectScene = new ConnectScene();
        stage.setScene(connectScene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
