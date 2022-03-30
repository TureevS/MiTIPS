package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller {

    @FXML
    private Button redactorButton;

    @FXML
    private Button dataButton;

    @FXML
    private Button tableButton;

    @FXML
    private Button exitButton;

    @FXML
    void initialize() {

        redactorButton.setOnAction(event ->{
            open("/sample/Redactor.fxml", redactorButton, "Редактор базы знаний");
        });
        dataButton.setOnAction(event ->{
            open("/sample/Deliveries.fxml", dataButton, "Ввод исходных данных");
        });
        tableButton.setOnAction(event ->{
            open("/sample/staff.fxml", tableButton, "Таблица");
        });
        exitButton.setOnAction(event ->{
            open("/sample/Bookkeeping.fxml", exitButton, "exit");
        });

    }

    private void open(String w, Button b, String name) {
        Stage stage = (Stage) b.getScene().getWindow();

        stage.close();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(w));
        Parent root1 = null;
        try {
            root1 = (Parent) fxmlLoader.load();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle(name);
        stage.setScene(new Scene(root1));
        stage.show();
    }
}
