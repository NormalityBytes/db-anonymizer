package com.spirittesting.db.frontend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class DbAnonymizerApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        LogManager.getLogManager().reset();
        FXMLLoader loader = getFxmlLoader("fxml/DatabaseConnectDialog.fxml");
        Parent parent = loader.load();
        stage.setTitle("Connect to Database");
        stage.setScene(new Scene(parent));
        stage.show();

        FXMLLoader logLoader = getFxmlLoader("fxml/LogView.fxml");
        Parent logParent = logLoader.load();
        Stage logStage = new Stage();
        logStage.setTitle("Log");
        logStage.setScene(new Scene(logParent));
        logStage.show();
        logStage.setX(stage.getX() + stage.getWidth());
    }

    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler(new Handler());
        launch();
    }

    public static FXMLLoader getFxmlLoader(String fxml) {
        return new FXMLLoader(DbAnonymizerApplication.class.getClassLoader().getResource(fxml));
    }

    static class Handler implements Thread.UncaughtExceptionHandler {

        public void uncaughtException(Thread t, Throwable e) {
            TextArea textArea = new TextArea();
            textArea.setText(e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
            textArea.setWrapText(true);
            textArea.setEditable(false);
            Stage stage = new Stage();
            stage.setTitle("Error");
            stage.setScene(new Scene(textArea));
            stage.show();
        }
    }
}
