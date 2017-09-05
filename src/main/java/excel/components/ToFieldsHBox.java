package excel.components;

import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * @author Alexander Diachenko.
 */
public class ToFieldsHBox extends HBox {

    private final TextField toId;
    private final TextField toField;

    public ToFieldsHBox(final String numericRegex) {
        toId = new TextField();
        toId.setPromptText("Enter 'to' id");
        toId.setFocusTraversable(true);
        toId.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(numericRegex)) {
                toId.setText(oldValue);
            }
        });
        toField = new TextField();
        toField.setPromptText("Enter 'to' field");
        toField.setFocusTraversable(true);
        toField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches(numericRegex)) {
                toField.setText(oldValue);
            }
        });
        getChildren().addAll(toId, toField);
    }

    public TextField getToId() {
        return toId;
    }

    public TextField getToField() {
        return toField;
    }
}
