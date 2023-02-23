package com.spirittesting.db.frontend.databaseview;

import com.spirittesting.db.frontend.DbAnonymizerApplication;
import com.spirittesting.db.frontend.FxmlLoader;
import com.spirittesting.db.model.Reader;
import javafx.scene.Scene;

public class DatabaseViewScene extends Scene {


    public DatabaseViewScene(String jdbc, String username, String password) {
        super(FxmlLoader.loadFxml("/DatabaseViewScene.fxml"), 1440, 900);

        DbAnonymizerApplication.primaryStage.setTitle("Database " + jdbc);

        DatabaseViewController controller = FxmlLoader.getController("/DatabaseViewScene.fxml");
        controller.setReader(new Reader(jdbc, username, password));
        controller.createTreeNodes();
    }




}
