package com.spirittesting.db.frontend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class DbAnonymizerApplication extends Application {

    private static final DbAnonymizerApplication INSTANCE = new DbAnonymizerApplication();

    public static DbAnonymizerApplication getInstance() {
        return INSTANCE;
    }

    private static final Map<String, Stage> stages = new HashMap<>();

    public Stage getStage(String stage) {
        return stages.get(stage);
    }

    @Override
    public void start(Stage stage) {
        stages.put("primary", stage);
        setScene("Connect to Database", "/DatabaseConnectDialog.fxml");
        stage.show();
    }

    public SceneElements setScene(String title, String fxml) {
        return setScene(title, fxml, "primary");
    }

    public SceneElements setScene(String title, String fxml, String stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent parent = loader.load();
            Initializable controller = loader.getController();
            log.debug("Currently known stages: {}", stages.keySet());
            stages.get(stage).setScene(new Scene(parent));
            stages.get(stage).setTitle(title);
            return new SceneElements(parent, controller);
        } catch (IOException e) {
            log.error("Error setting stage {}", fxml, e);
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
