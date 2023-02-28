package com.spirittesting.db.frontend.components;

import com.spirittesting.db.FxmlView;
import com.spirittesting.db.database.Column;
import com.spirittesting.db.database.ConnectionFactory;
import com.spirittesting.db.database.ForeignKey;
import com.spirittesting.db.database.Index;
import com.spirittesting.db.database.Table;
import com.spirittesting.db.database.TableId;
import javafx.scene.Parent;
import javafx.scene.control.TitledPane;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import org.controlsfx.control.decoration.Decoration;
import org.controlsfx.control.decoration.Decorator;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class TableComponent extends FxmlView {

    private final Logger log = Logger.getLogger(getClass().getName());

    //https://stackoverflow.com/questions/17312734/how-to-make-a-draggable-node-in-javafx-2-0

    public void startDrag(MouseEvent mouseEvent) {
        log.info("startDrag");
        Dragboard db = ((Parent) mouseEvent.getSource()).startDragAndDrop(TransferMode.ANY);

        mouseEvent.consume();

    }

    public void stopDrag(DragEvent dragEvent) {
        log.info("stopDrag");
    }
}
