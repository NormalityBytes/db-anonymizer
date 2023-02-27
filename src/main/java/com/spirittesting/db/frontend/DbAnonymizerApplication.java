package com.spirittesting.db.frontend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;

public class DbAnonymizerApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = getFxmlLoader("fxml/MainView.fxml");
        Stage stage  = loader.load();
        stage.show();
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
