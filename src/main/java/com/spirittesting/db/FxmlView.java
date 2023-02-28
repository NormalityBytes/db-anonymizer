package com.spirittesting.db;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

public abstract class FxmlView {

    private FXMLLoader loader;
    private Parent view;

    public FxmlView() {
        URL resource = getClass().getClassLoader().getResource("fxml/" + getClass().getSimpleName() + ".fxml");
        Logger.getLogger(getClass().getSimpleName()).info("Loading FXML for " + getClass().getSimpleName() + " from " + resource);
        loader = new FXMLLoader(resource);
        loader.setController(this);
    }

    public Parent getView() {
        if (view == null) {
            synchronized (this) {
                if (view == null) {
                    view = loadView();
                }
            }
        }
        return view;
    }

    private Parent loadView() {
        Logger.getLogger(getClass().getSimpleName()).info("Loading view for " + getClass().getSimpleName() + " from " + loader.getLocation());
        try {
            return loader.load();
        } catch (IOException e) {
            Logger.getLogger(getClass().getSimpleName()).severe("Could not load view for " + getClass().getSimpleName() + " from " + loader.getLocation());
            throw new RuntimeException(e);
        }
    }

    public FXMLLoader getLoader() {
        return loader;
    }

}
