package com.spirittesting.db.frontend;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class FxmlLoader {

    public static Map<String, FXMLLoader> fxmlLoaders = new HashMap<>();

    public static Parent loadFxml(String path) {
        try {
            FXMLLoader loader = new FXMLLoader(FxmlLoader.class.getResource(path));
            fxmlLoaders.put(path, loader);
            return loader.load();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T getController(String path) {
        return fxmlLoaders.get(path).getController();
    }

}
