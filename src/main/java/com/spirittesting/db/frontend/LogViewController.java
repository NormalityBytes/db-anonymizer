package com.spirittesting.db.frontend;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class LogViewController implements Initializable {

    @FXML
    TextArea logview;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Logger.getLogger("").addHandler(new LoggingHandler(logview));
    }

}
