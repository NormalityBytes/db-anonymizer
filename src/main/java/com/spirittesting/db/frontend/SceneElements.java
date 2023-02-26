package com.spirittesting.db.frontend;

import javafx.fxml.Initializable;
import javafx.scene.Parent;

public record SceneElements(Parent parent, Initializable controller) {

    @SuppressWarnings({"unchecked", "unused"})
    public <T extends Parent> T getParent() {
        return (T) parent;
    }

    @SuppressWarnings({"unchecked", "unused"})
    public <T extends Initializable> T getController() {
        return (T) controller;
    }

}
