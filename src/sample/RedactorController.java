package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RedactorController {
    @FXML
    private TableView<diseases> diseasesTable;
    @FXML
    private TableColumn<diseases, String> diseaseColumn;

    @FXML
    private TableView<?> possibleValuesTable;
    @FXML
    private TableColumn<?, ?> possiblevalueColumn;

    @FXML
    private TableView<?> signsTable;
    @FXML
    private TableColumn<?, ?> signColumn;

    @FXML
    private TableView<?> normalValuesTable;
    @FXML
    private TableColumn<?, ?> normalvalueColumn;

    @FXML
    private TableView<?> picturesTable;
    @FXML
    private TableColumn<?, ?> pictureColumn;

    @FXML
    private Button MenuButton;

    @FXML
    private Button diseasesAddButton;

    @FXML
    private Button normalValuesAddButton;

    @FXML
    private Button possibleValuesAddButton;

    @FXML
    private Button signsAddButton;

    @FXML
    private TextField diseasesTextField;

    @FXML
    private TextField normalValuesTextField;

    @FXML
    private TextField possibleValuesTextField;

    @FXML
    private TextField signsTextField;

    @FXML
    private ChoiceBox<?> picturesSelect;

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        updateDiseases();

        diseasesAddButton.setOnAction(event ->{
            try {
                addDiseases();
            } catch (SQLException exception) {
                exception.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

    }


    private void addDiseases() throws SQLException, ClassNotFoundException {
        DatabaseHandler dbHandler = new DatabaseHandler();
        String title = diseasesTextField.getText().trim();

        ArrayList<Object> data = new ArrayList<Object>();
        data.add(0);
        data.add(title);
        updateDiseases();

        if(!title.equals("") ){
            dbHandler.insertRow(data, "diseases");
        }
    }

    private void updateDiseases() throws SQLException, ClassNotFoundException {
        DatabaseHandler dbHandler = new DatabaseHandler();
        ResultSet rs = dbHandler.selectData("SELECT * FROM diseases");
        ArrayList<ArrayList<String>> result = new ArrayList<>();
        while(rs.next()){
            ArrayList<String> tmp = new ArrayList<>();
            for (int i = 2; i <= 2; i++){
                tmp.add(rs.getString(i));
            }
            result.add(tmp);
        }

        diseaseColumn.setCellValueFactory(new PropertyValueFactory<diseases, String>("title"));


        ObservableList<diseases> diseasesData = FXCollections.observableArrayList();
        int n = result.size();
        for (int i = 0; i <= n-1; i++){
            diseasesData.add(new diseases(result.get(i).get(0)));
        }
        diseasesTable.setItems(diseasesData);
    }
}
