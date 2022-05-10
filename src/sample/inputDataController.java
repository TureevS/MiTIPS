package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class inputDataController {
    DatabaseHandler dbHandler = new DatabaseHandler();
    @FXML
    private TableView<inputData> valuesTable;
    @FXML
    private TableColumn<inputData, String> signColumn;
    @FXML
    private TableColumn<inputData, String> valuesColumn;
    @FXML
    private TableColumn<inputData, Integer> timeColumn;


    @FXML
    private Button MenuButton;
    @FXML
    private Button addButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button diagnosticButton;

    @FXML
    private TextArea checkResult;
    @FXML
    private TextField timeTextField;


    @FXML
    private TableColumn<title, String> notColumn;
    @FXML
    private TableColumn<title, String> possibleColumn;
    @FXML
    private TableColumn<title, String> accurateColumn;

    @FXML
    private TableView<title> unsuitableTable;
    @FXML
    private TableView<title> possibleTable;
    @FXML
    private TableView<title> accurateTable;


    @FXML
    private ComboBox<String> valueChoiceBox;
    @FXML
    private ComboBox<String> signsChoiceBox;

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        updateSignsChoiceBox();
        updateValuesTable();
        signsChoiceBox.setOnAction(event ->{
            try {
                if(signsChoiceBox.getValue()!= null){
                    updateValueChoiceBox(signsChoiceBox.getValue());
                }else{
                    valueChoiceBox.getItems().clear();
                }
            } catch (SQLException | ClassNotFoundException exception) {
                exception.printStackTrace();
            }
        });
        addButton.setOnAction(event ->{
            try {
                add();
            } catch (SQLException | ClassNotFoundException exception) {
                exception.printStackTrace();
            }
        });
        deleteButton.setOnAction(event ->{
            try {
                delete();
            } catch (SQLException | ClassNotFoundException exception) {
                exception.printStackTrace();
            }
        });
        diagnosticButton.setOnAction(event ->{
            try {
                diagnostic();
            } catch (SQLException | ClassNotFoundException exception) {
                exception.printStackTrace();
            }
        });
    }
    private void updateSignsChoiceBox() throws SQLException, ClassNotFoundException {
        signsChoiceBox.getItems().clear();
        ResultSet rs = dbHandler.selectData("SELECT title FROM signs");
        ObservableList<String> signs = FXCollections.observableArrayList();
        while(rs.next()){
            signs.add(rs.getString(1));
        }
        signsChoiceBox.setItems(signs);
    }
    private void updateValueChoiceBox(String sign) throws SQLException, ClassNotFoundException {
        int id_sign = dbHandler.getId("idsigns", "signs", sign);
        ResultSet rs = dbHandler.selectData("SELECT value_string FROM possible_values_str WHERE id_sign = " + id_sign);
        ObservableList<String> values = FXCollections.observableArrayList();
        while(rs.next()){
            values.add(rs.getString(1));
        }
        valueChoiceBox.setItems(values);
    }

    private void add() throws SQLException, ClassNotFoundException {
        if(signsChoiceBox.getValue()!=null){
            if(valueChoiceBox.getValue() != null){
                if(!timeTextField.getText().equals("")){
                    ArrayList<Object> data = new ArrayList<>();
                    data.add(signsChoiceBox.getValue());
                    data.add(Integer.parseInt(timeTextField.getText().trim()));
                    data.add(valueChoiceBox.getValue());
                    dbHandler.insertRow(data, "input_value");
                    updateValuesTable();
                    timeTextField.clear();
                    valueChoiceBox.valueProperty().setValue("Выберите тип признака");
                }
                else{
                    ModalWindow.errorWindow("Введите момент времени");
                }
            }else{
                ModalWindow.errorWindow("Выберите значение признака");
            }
        }else{
            ModalWindow.errorWindow("Выберите признак");
        }
    }

    private void updateValuesTable() throws SQLException, ClassNotFoundException {
        ResultSet rs = dbHandler.selectData("SELECT * FROM input_value");
        signColumn.setCellValueFactory(new PropertyValueFactory<>("sign"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        valuesColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        ObservableList<inputData> inputData = FXCollections.observableArrayList();
        while(rs.next()){
            inputData.add(new inputData(rs.getString(1),rs.getInt(2),rs.getString(3)));
        }
        valuesTable.setItems(inputData);
    }

    private void delete() throws SQLException, ClassNotFoundException {
        String sign = valuesTable.getSelectionModel().getSelectedItem().getSign();
        int time = valuesTable.getSelectionModel().getSelectedItem().getTime();
        String value = valuesTable.getSelectionModel().getSelectedItem().getValue();
        String condition = "sign = '" + sign + "' AND time = " + time + " AND value = '" + value + "'";
        dbHandler.deleteData("input_value", condition);
        updateValuesTable();
    }
    private void diagnostic() throws SQLException, ClassNotFoundException {
        checkResult.clear();
        ThreadLocalRandom random = ThreadLocalRandom.current();
        boolean pictureFlag;
        int errors;
        ResultSet diseases = dbHandler.selectData("SELECT * FROM diseases");
        ArrayList<String> unsuitable_diseases = new ArrayList<>();
        ArrayList<String> possible_diseases = new ArrayList<>();
        ArrayList<String> accurate_diseases = new ArrayList<>();
        ArrayList<Integer> id_diseases = new ArrayList<>();
        ArrayList<Integer> id_signs = new ArrayList<>();
        ResultSet signs = dbHandler.selectData("SELECT DISTINCT sign FROM input_value");
        while (signs.next()){
            int id_sign = dbHandler.getId("idsigns", "signs", signs.getString(1));
            id_signs.add(id_sign);
        }
        while(diseases.next()){
            pictureFlag = true;
            for (Integer id_sign : id_signs) {
                if (!dbHandler.exist("pictures", diseases.getInt(1), id_sign)) {
                    pictureFlag = false;
                }
            }
            if(!pictureFlag){
                checkResult.appendText(diseases.getString(2) + ":\n");
                checkResult.appendText("\tКлиническая картина не совпадает\n");
                unsuitable_diseases.add(diseases.getString(2));
            }else{
                id_diseases.add(diseases.getInt(1));
            }
        }
        for (Integer id_disease : id_diseases){
            errors=0;
            String disease = dbHandler.getName("diseases", "idDiseases", id_disease);
            checkResult.appendText(disease + ":\n");
            for (Integer id_sign : id_signs) {
                ArrayList<Integer> borders = new ArrayList<>();
                String sign = dbHandler.getName("signs", "idsigns", id_sign);
                ResultSet rs = dbHandler.selectData("SELECT * FROM periodvalues WHERE id_disease = "
                        + id_disease + " AND id_sign = " + id_sign  + " ORDER BY number");
                while (rs.next()){
                    if(borders.isEmpty()){
                        borders.add(random.nextInt(rs.getInt(8),rs.getInt(9)));
                    }
                    else{
                        int temp = borders.get(borders.size() - 1);
                        borders.add(temp + random.nextInt(rs.getInt(8),rs.getInt(9)));
                    }
                }
                checkResult.appendText("\t" + sign + ":\n");
                checkResult.appendText("\t\tГраницы периодов: " + borders.toString() + "\n");
                ResultSet inputValue = dbHandler.selectData("SELECT * FROM input_value WHERE sign = '" + sign + "'");
                while(inputValue.next()){
                    int numberPeriod = 1;
                    for(Integer border : borders){
                        if(inputValue.getInt(2) > border){
                            numberPeriod++;
                            if(border.equals(borders.get(borders.size() - 1))){
                                numberPeriod--;
                            }
                        }
                    }
                    checkResult.appendText("\t\t\tИсходной признак '" + sign + "' МВ=" + inputValue.getInt(2) +
                            " попал в период динамики №" + numberPeriod + "\n");
                    ResultSet value = dbHandler.selectData("SELECT value FROM periodvalues WHERE id_disease = "
                            + id_disease + " AND id_sign = " + id_sign + " AND number = " + numberPeriod);
                    value.next();
                    String valueString = value.getString(1);
                    String[] subStr;
                    String separator = ", ";
                    subStr = valueString.split(separator);
                    boolean flag = false;
                    checkResult.appendText("\t\t\t\tЗначения " + numberPeriod + " периода: ");
                    for (String s : subStr) {
                        checkResult.appendText(s + " ");
                        if (inputValue.getString(3).equals(s)){
                            flag = true;
                        }
                    }
                    checkResult.appendText("\n");
                    checkResult.appendText("\t\t\t\tИсходное значение: " + inputValue.getString(3) + "\n");
                    if(!flag){
                        checkResult.appendText("\t\t\t\t\tЗначения не совпадают\n");
                        errors++;
                    }else{
                        checkResult.appendText("\t\t\t\t\tЗначения совпадают\n");
                    }
                }
            }
            if(errors==0){
                accurate_diseases.add(disease);
            }else{
                possible_diseases.add(disease);
            }
        }
        unsuitableTable.getItems().clear();
        possibleTable.getItems().clear();
        accurateTable.getItems().clear();
        notColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        for(String disease:unsuitable_diseases){
            title temp = new title(disease);
            unsuitableTable.getItems().add(temp);
        }
        possibleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        for(String disease:possible_diseases){
            title temp = new title(disease);
            possibleTable.getItems().add(temp);
        }
        accurateColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        for(String disease:accurate_diseases){
            title temp = new title(disease);
            accurateTable.getItems().add(temp);
        }
    }
}


