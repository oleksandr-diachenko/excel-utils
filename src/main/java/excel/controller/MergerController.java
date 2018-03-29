package excel.controller;


import excel.Util.TimeUtil;
import excel.model.Excel;
import excel.model.ExcelImpl;
import excel.service.MergerService;
import javafx.beans.binding.BooleanBinding;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;


/**
 * @author Alexander Diachenko.
 */
public class MergerController implements Initializable {

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Label fromFilePath;
    @FXML
    private Spinner<Integer> fromId;
    @FXML
    private Spinner<Integer> fromField;
    @FXML
    private Spinner<Integer> toId;
    @FXML
    private Spinner<Integer> toField;
    @FXML
    private Label toFilePath;
    @FXML
    private Label saveDirectoryPath;
    @FXML
    private ProgressIndicator progressIndicator;
    @FXML
    private Label complete;
    @FXML
    private Button mergeButton;
    @FXML
    private Button openButton;
    private File fileFrom;
    private File fileTo;
    private String savedFilePath;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        init();
    }

    public void selectFromFile() {
        FileChooser fileFromChooser = new FileChooser();
        fileFrom = fileFromChooser.showOpenDialog(getStage());
        if (fileFrom != null) {
            fromFilePath.setText(fileFrom.getAbsolutePath());
        } else {
            fromFilePath.setText("");
        }
    }

    public void selectToFile() {
        FileChooser fileFromChooser = new FileChooser();
        fileTo = fileFromChooser.showOpenDialog(getStage());
        if (fileTo != null) {
            toFilePath.setText(fileTo.getAbsolutePath());
        } else {
            toFilePath.setText("");
        }
    }

    public void selectSaveDirectory() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File saveDirectory = directoryChooser.showDialog(getStage());
        if (saveDirectory != null) {
            saveDirectoryPath.setText(saveDirectory.getAbsolutePath());
        } else {
            saveDirectoryPath.setText("");
        }
    }

    public void merge() {
        anchorPane.setDisable(true);
        Excel excel = new ExcelImpl();
        List<Integer> fromColumns = Arrays.asList(fromId.getValue() - 1, fromField.getValue() - 1);
        List<Integer> toColumns = Arrays.asList(toId.getValue() - 1, toField.getValue() - 1);
        savedFilePath = saveDirectoryPath.getText() + "\\" + "merged_" + TimeUtil.getCurrentTime() + ".xlsx";
        MergerService mergerService = new MergerService(excel, fileFrom, fileTo, fromColumns, toColumns, savedFilePath);
        Task<Void> task = mergerService.createTask();
        progressIndicator.visibleProperty().bind(task.runningProperty());
        new Thread(task).start();
        task.setOnSucceeded(event -> setComplete());
        task.setOnFailed(event -> setFailed(task.getException()));
    }

    public void open() {
        Desktop desktop = Desktop.getDesktop();
        try {
            openButton.setDisable(true);
            desktop.open(new File(savedFilePath));
        } catch (IOException e) {
            setFailed(e);
            e.printStackTrace();
        }
    }

    private void init() {
        mergeButton.disableProperty().bind(getBooleanBinding());
    }

    private Stage getStage() {
        return (Stage) anchorPane.getScene().getWindow();
    }

    private void setComplete() {
        anchorPane.setDisable(false);
        openButton.setDisable(false);
        //TODO придумать вывод завершения
        complete.setText("DONE");
    }

    private void setFailed(Throwable exception) {
        anchorPane.setDisable(false);
        //TODO придумать вывод ошибок
        complete.setText(exception.getMessage());
    }

    private BooleanBinding getBooleanBinding() {
        return fromFilePath.textProperty().isEmpty()
                .or(toFilePath.textProperty().isEmpty())
                .or(saveDirectoryPath.textProperty().isEmpty());
    }
}
