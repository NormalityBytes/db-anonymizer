package com.spirittesting.db.frontend;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class LoggingHandler extends Handler {

    private final TextArea out;

    public LoggingHandler(TextArea out) {
        this.out = out;
    }

    @Override
    public void publish(LogRecord record) {
        Platform.runLater(() -> {
                    out.appendText(record.getLevel() + ": " + record.getMessage() + "\n");
                }
        );
    }

    @Override
    public void flush() {

    }

    @Override
    public void close() throws SecurityException {

    }
}
