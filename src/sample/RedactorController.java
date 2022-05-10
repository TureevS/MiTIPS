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
    DatabaseHandler dbHandler = new DatabaseHandler();
    //TABLE
    @FXML
    private TableView<title> diseasesTable;
    @FXML
    private TableColumn<title, String> diseaseColumn;

    @FXML
    private TableView<title> signsTable;
    @FXML
    private TableColumn<title, String> signColumn;

    @FXML
    private TableView<title> picturesTable;
    @FXML
    private TableColumn<title, String> pictureColumn;

    @FXML
    private TableView<title> possibleValuesTable;
    @FXML
    private TableColumn<title, String> possiblevalueColumn;

    @FXML
    private TableView<title> normalValuesTable;
    @FXML
    private TableColumn<title, String> normalvalueColumn;

    @FXML
    private TableView<title> valueTable;
    @FXML
    private TableColumn<title, String> valueColumn;



    //TEXTFIELD
    @FXML
    private TextField diseasesTextField;
    @FXML
    private TextField signsTextField;

    @FXML
    private TextField possibleValuesTextField;
    @FXML
    private TextField possibleValuesTextFieldDo;
    @FXML
    private TextField possibleValuesTextFieldOt;

    @FXML
    private TextField quantityTextField;
    @FXML
    private TextField upperTextField;
    @FXML
    private TextField bottomTextField;
    @FXML
    private TextArea checkResult;


    //BUTTONS
    @FXML
    private Button MenuButton;
    @FXML
    private Button checkButton;



    @FXML
    private Button diseasesAddButton;
    @FXML
    private Button diseasesDeleteButton;


    @FXML
    private Button signsAddButton;
    @FXML
    private Button signsDeleteButton;


    @FXML
    private Button pictureAddButton;
    @FXML
    private Button pictureDeleteButton;


    @FXML
    private Button possibleValuesAddButton;
    @FXML
    private Button pvalueDeleteButton;


    @FXML
    private Button normalValuesAddButton;
    @FXML
    private Button nvalueDeleteButton;

    @FXML
    private Button quantityAddButton;
    @FXML
    private Button quantityDeleteButton;

    @FXML
    private Button periodValuesAddButton;
    @FXML
    private Button periodValuesDeleteButton;

    @FXML
    private Button addValueButton;
    @FXML
    private Button deleteValueButton;


    //ChoiceBox
    @FXML
    private ComboBox<title> diseasesChoiceBox;
    @FXML
    private ComboBox<title> signsChoiceBox;
    @FXML
    private ComboBox<title> pictureChoiceBox;
    @FXML
    private ComboBox<title> normalValueChoiceBox;
    @FXML
    private ComboBox<String> periodValueChoiceBox;
    @FXML
    private ComboBox<title> signsForValueChoiceBox;
    @FXML
    private ComboBox<Integer> periodsChoiceBox;
    @FXML
    private ComboBox<String> typeChoiceBox;



    private final String[] type = {"Качественный", "Интервальный"};



    @FXML
    private Label otLabel;
    @FXML
    private Label doLabel;
    @FXML
    private Label checkResultLabel;



    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        possibleValuesAddButton.setVisible(false);
        pvalueDeleteButton.setVisible(false);
        possibleValuesTextField.setVisible(false);

        doLabel.setVisible(false);
        otLabel.setVisible(false);
        possibleValuesTextFieldOt.setVisible(false);
        possibleValuesTextFieldDo.setVisible(false);
        periodValuesDeleteButton.setVisible(false);

        addValueButton.setVisible(false);
        deleteValueButton.setVisible(false);
        periodValueChoiceBox.setVisible(false);

        //ОБНОВЛЕНИЯ
        update("diseases", diseasesTable, diseaseColumn);
        update("signs", signsTable, signColumn);

        updateChoiceBox(diseasesChoiceBox, diseasesTable);
        updateChoiceBox(pictureChoiceBox, signsTable);
        updateChoiceBox(signsForValueChoiceBox, signsTable);

        typeChoiceBox.getItems().addAll(type);


        //ДОБАВЛЕНИЕ
        diseasesAddButton.setOnAction(event ->{
            try {
                addDisease();
                update("diseases", diseasesTable, diseaseColumn);
                updateChoiceBox(diseasesChoiceBox, diseasesTable);
            } catch (SQLException exception) {
                ModalWindow.errorWindow("Данное заболевание уже существует");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        signsAddButton.setOnAction(event ->{
            try {
                addSigns();
                update("signs", signsTable, signColumn);
                updateChoiceBox(pictureChoiceBox, signsTable);
                updateChoiceBox(signsForValueChoiceBox, signsTable);
            } catch (SQLException exception) {
                ModalWindow.errorWindow("Данный признак уже существует");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        pictureAddButton.setOnAction(event ->{
            try {
                addPicture();
                updateChoiceBox(signsChoiceBox, picturesTable);
            } catch (SQLException | ClassNotFoundException exception) {
                exception.printStackTrace();
            }
        });

        possibleValuesAddButton.setOnAction(event ->{
            try {
                addPossibleValue(true);
                updateChoiceBox(normalValueChoiceBox, possibleValuesTable);
            } catch (SQLException | ClassNotFoundException exception) {
                exception.printStackTrace();
            }
        });

        normalValuesAddButton.setOnAction(event ->{
            try {
                addPossibleValue(false);
            } catch (SQLException | ClassNotFoundException exception) {
                exception.printStackTrace();
            }
        });

        quantityAddButton.setOnAction(event ->{
            try {
                addQuantity();
                updateChoiceBox();
            } catch (SQLException | ClassNotFoundException exception) {
                exception.printStackTrace();
            }
        });

        periodValuesAddButton.setOnAction(event ->{
            try {
                addPeriods();
            } catch (SQLException | ClassNotFoundException exception) {
                exception.printStackTrace();
            }
        });

        addValueButton.setOnAction(event -> addValue());
        deleteValueButton.setOnAction(event -> deleteValue());


        //УДАЛЕНИЕ
        diseasesDeleteButton.setOnAction(event ->{
            try {
                delete("diseases", diseasesTable);
                update("diseases", diseasesTable, diseaseColumn);
                updateChoiceBox(diseasesChoiceBox, diseasesTable);
            } catch (SQLException | ClassNotFoundException exception) {
                exception.printStackTrace();
            }
        });

        signsDeleteButton.setOnAction(event ->{
            try {
                delete("signs", signsTable);
                update("signs", signsTable, signColumn);
                updateChoiceBox(pictureChoiceBox, signsTable);
                updateChoiceBox(signsForValueChoiceBox, signsTable);
            } catch (SQLException | ClassNotFoundException exception) {
                exception.printStackTrace();
            }
        });

        pictureDeleteButton.setOnAction(event ->{
            try {
                deletePicture();
                updateChoiceBox(signsChoiceBox, picturesTable);
            } catch (SQLException | ClassNotFoundException exception) {
                exception.printStackTrace();
            }
        });

        pvalueDeleteButton.setOnAction(event ->{
            try {
                deletePossibleValue(true);
                updateChoiceBox(normalValueChoiceBox, possibleValuesTable);
            } catch (SQLException | ClassNotFoundException exception) {
                exception.printStackTrace();
            }
        });

        nvalueDeleteButton.setOnAction(event ->{
            try {
                deletePossibleValue(false);
            } catch (SQLException | ClassNotFoundException exception) {
                exception.printStackTrace();
            }
        });
        quantityDeleteButton.setOnAction(event ->{
            try {
                deleteQuantity();
                updateQuantity();
            } catch (SQLException | ClassNotFoundException exception) {
                exception.printStackTrace();
            }
        });
        periodValuesDeleteButton.setOnAction(event ->{
            try {
                deletePeriods();
                updatePeriods();
            } catch (SQLException | ClassNotFoundException exception) {
                exception.printStackTrace();
            }
        });



        //ДЕЙСТВИЯ ПРИ ИЗМЕНИНИИ CHOICE BOX
        diseasesChoiceBox.setOnAction(event ->{
            try {
                if(diseasesChoiceBox.getValue()!= null){
                    updatePicture();
                    updateChoiceBox(signsChoiceBox, picturesTable);
                }else{
                    picturesTable.getItems().clear();
                    signsChoiceBox.getItems().clear();
                }
            } catch (SQLException | ClassNotFoundException exception) {
                exception.printStackTrace();
            }
        });

        signsForValueChoiceBox.setOnAction(event ->{
            try {
                if(signsForValueChoiceBox.getValue()!= null){
                    updatePossibleValue(true);
                    updatePossibleValue(false);
                    updateChoiceBox(normalValueChoiceBox, possibleValuesTable);
                    String type = dbHandler.getType(signsForValueChoiceBox.getValue().getTitle());
                    possibleValuesAddButton.setVisible(true);
                    pvalueDeleteButton.setVisible(true);
                    if(type.equals("Качественный")){
                        possibleValuesTextField.setVisible(true);
                        doLabel.setVisible(false);
                        otLabel.setVisible(false);
                        possibleValuesTextFieldOt.setVisible(false);
                        possibleValuesTextFieldDo.setVisible(false);
                    }
                    else{
                        doLabel.setVisible(true);
                        otLabel.setVisible(true);
                        possibleValuesTextFieldOt.setVisible(true);
                        possibleValuesTextFieldDo.setVisible(true);
                        possibleValuesTextField.setVisible(false);
                    }
                }else{
                    possibleValuesTable.getItems().clear();
                    normalValuesTable.getItems().clear();
                    possibleValuesAddButton.setVisible(false);
                    pvalueDeleteButton.setVisible(false);
                    doLabel.setVisible(false);
                    otLabel.setVisible(false);
                    possibleValuesTextFieldOt.setVisible(false);
                    possibleValuesTextFieldDo.setVisible(false);
                    possibleValuesTextField.setVisible(false);
                }
            } catch (SQLException | ClassNotFoundException exception) {
                exception.printStackTrace();
            }
        });

        signsChoiceBox.setOnAction(event ->{
            try {
                if(signsChoiceBox.getValue()!= null){
                    updateQuantity();
                    updateValueChoiceBox(signsChoiceBox.getValue().getTitle());
                }else{
                    quantityAddButton.setText("+");
                    quantityTextField.clear();
                    periodsChoiceBox.getItems().clear();
                    valueTable.getItems().clear();
                    periodValuesDeleteButton.setVisible(false);
                }
            } catch (SQLException | ClassNotFoundException exception) {
                exception.printStackTrace();
            }
        });


        periodsChoiceBox.setOnAction(event ->{
            try {
                if(periodsChoiceBox.getValue()!= null){
                    updatePeriods();
                    addValueButton.setVisible(true);
                    deleteValueButton.setVisible(true);
                    periodValueChoiceBox.setVisible(true);
                }else{
                    periodValuesAddButton.setText("Добавить");
                    upperTextField.clear();
                    upperTextField.setEditable(true);
                    bottomTextField.clear();
                    bottomTextField.setEditable(true);
                    periodValuesDeleteButton.setVisible(false);

                    addValueButton.setVisible(false);
                    deleteValueButton.setVisible(false);
                    periodValueChoiceBox.setVisible(false);
                }
            } catch (SQLException | ClassNotFoundException exception) {
                exception.printStackTrace();
            }
        });
        checkButton.setOnAction(event ->{
            try {
                check();
            } catch (SQLException | ClassNotFoundException exception) {
                exception.printStackTrace();
            }
        });
    }

    //ДОБАВЛЕНИЕ
    public void addDisease() throws SQLException, ClassNotFoundException {
        String title = diseasesTextField.getText().trim();
        ArrayList<Object> data = new ArrayList<>();
        data.add(0);
        data.add(title);
        if(!title.equals("") ){
            dbHandler.insertRow(data, "diseases");
            diseasesTextField.clear();
        }
        else{
            ModalWindow.errorWindow("Нельзя ввести пустую строку");
        }
    }

    private void addSigns() throws SQLException, ClassNotFoundException {
        if(typeChoiceBox.getValue() != null){
            if(!signsTextField.getText().equals("")){
                String title = signsTextField.getText().trim();
                ArrayList<Object> data = new ArrayList<>();
                data.add(0);
                data.add(title);
                data.add(typeChoiceBox.getValue());
                dbHandler.insertRow(data, "signs");
                signsTextField.clear();
                typeChoiceBox.valueProperty().setValue("Выберите тип признака");
            }
            else{
                ModalWindow.errorWindow("Нельзя ввести пустую строку");
            }
        }else{
            ModalWindow.errorWindow("Выберите тип признака");
        }
    }

    private void addPicture() throws SQLException, ClassNotFoundException {
        if (pictureChoiceBox.getValue() != null && diseasesChoiceBox.getValue() != null){
            String title_diseases = diseasesChoiceBox.getValue().getTitle();
            String title_sign = pictureChoiceBox.getValue().getTitle();
            int id_sign = dbHandler.getId("idsigns", "signs", title_sign);
            int id_disease = dbHandler.getId("idDiseases", "diseases", title_diseases);
            if (dbHandler.exist("pictures",id_disease, id_sign)){
                ModalWindow.errorWindow("Данный признак уже существует в клинической картине данного заболевания");
            }
            else{
                ArrayList<Object> data = new ArrayList<>();
                data.add(0);
                data.add(title_sign);
                data.add(id_disease);
                data.add(id_sign);
                dbHandler.insertRow(data, "pictures");
                updatePicture();
                pictureChoiceBox.valueProperty().set(null);
            }
        }else{
            ModalWindow.errorWindow("Не выбрано заболевание или признак");
        }
    }

    private void addPossibleValue(boolean flag)
            throws SQLException, ClassNotFoundException {
        String title_sign = signsForValueChoiceBox.getValue().getTitle();
        int id_sign = dbHandler.getId("idsigns", "signs", title_sign);
        String type = dbHandler.getType(signsForValueChoiceBox.getValue().getTitle());
        if (type.equals("Качественный")){
            if (flag) {
                if(!possibleValuesTextField.getText().equals("")){
                    String pValue = possibleValuesTextField.getText().trim();
                    ArrayList<Object> data = new ArrayList<>();
                    data.add(0);
                    data.add(pValue);
                    data.add(id_sign);
                    dbHandler.insertRow(data, "possible_values_str");
                    possibleValuesTextField.clear();
                }else{
                    ModalWindow.errorWindow("Нельзя ввести пустую строку");
                }
            }
            else{
                if(normalValueChoiceBox.getValue()!=null){
                    String nValue = normalValueChoiceBox.getValue().getTitle();
                    ArrayList<Object> data = new ArrayList<>();
                    data.add(0);
                    data.add(nValue);
                    data.add(id_sign);
                    dbHandler.insertRow(data, "normal_values_str");
                    normalValueChoiceBox.valueProperty().set(null);
                }else{
                    ModalWindow.errorWindow("Нельзя ввести пустую строку");
                }
            }
        }
        else{
            if (flag) {
                if(!(possibleValuesTextFieldOt.getText().equals("") || possibleValuesTextFieldDo.getText().equals(""))){
                    float bottom = Float.parseFloat(possibleValuesTextFieldOt.getText().trim());
                    float upper = Float.parseFloat(possibleValuesTextFieldDo.getText().trim());
                    String value = "От " + bottom + " до " + upper;
                    ArrayList<Object> data = new ArrayList<>();
                    data.add(0);
                    data.add(value);
                    data.add(id_sign);
                    dbHandler.insertRow(data, "possible_values_str");
                    possibleValuesTextFieldOt.clear();
                    possibleValuesTextFieldDo.clear();
                }else{
                    ModalWindow.errorWindow("Нельзя ввести пустую строку");
                }
            }
            else{
                if(normalValueChoiceBox.getValue() != null){
                    String value = normalValueChoiceBox.getValue().getTitle();
                    ArrayList<Object> data = new ArrayList<>();
                    data.add(0);
                    data.add(value);
                    data.add(id_sign);
                    dbHandler.insertRow(data, "normal_values_str");
                    possibleValuesTextFieldOt.clear();
                    possibleValuesTextFieldDo.clear();
                }else{
                    ModalWindow.errorWindow("Нельзя ввести пустую строку");
                }
            }
        }
        updatePossibleValue(flag);
    }

    private void addQuantity() throws SQLException, ClassNotFoundException {
        String title_diseases = diseasesChoiceBox.getValue().getTitle();
        String title_sign = signsChoiceBox.getValue().getTitle();
        int id_disease = dbHandler.getId("idDiseases", "diseases", title_diseases);
        int id_sign = dbHandler.getId("idsigns", "signs", title_sign);
        if(quantityAddButton.getText().equals("+")){
            if(!quantityTextField.getText().equals("")){
                int quantity = Integer.parseInt(quantityTextField.getText().trim());
                ArrayList<Object> data = new ArrayList<>();
                data.add(0);
                data.add(id_disease);
                data.add(id_sign);
                data.add(quantity);
                dbHandler.insertRow(data, "quantityofperiods");
                quantityAddButton.setText("И");
            }
            else{
                ModalWindow.errorWindow("Нельзя ввести пустую строку");
            }
        }else{
            if(!quantityTextField.getText().equals("И")){
                int quantity = Integer.parseInt(quantityTextField.getText().trim());
                String conditional = "id_disease = " + id_disease + " AND id_sign = " + id_sign;
                dbHandler.updateData("quantityofperiods", "quantity", quantity, conditional);
            }else{
                ModalWindow.errorWindow("Нельзя ввести пустую строку");
            }
        }
    }
    private void addPeriods() throws SQLException, ClassNotFoundException {
        String title_diseases = diseasesChoiceBox.getValue().getTitle();
        String title_sign = signsChoiceBox.getValue().getTitle();
        int quantity = Integer.parseInt(quantityTextField.getText());
        int id_disease = dbHandler.getId("idDiseases", "diseases", title_diseases);
        int id_sign = dbHandler.getId("idsigns", "signs", title_sign);
        int id_quantity = dbHandler.getQuantityId(quantity, id_disease, id_sign);
        int id_picture = dbHandler.getIdPicture(id_disease, id_sign);
        int number = periodsChoiceBox.getValue();
        if(periodValuesAddButton.getText().equals("Добавить")){
            if(periodValueChoiceBox.getValue() != null && !upperTextField.getText().equals("")
                    && !bottomTextField.getText().equals("")){
                int upperBound = Integer.parseInt(upperTextField.getText().trim());
                int bottomLine = Integer.parseInt(bottomTextField.getText().trim());
                ObservableList<title> arr = valueTable.getItems();
                String value = "";
                for (title temp : arr){
                    String tempString = temp.toString();
                    value = value + tempString + ", ";
                }
                value = removeLastChar(value);
                if(value.equals("")){
                    ModalWindow.errorWindow("Нельзя ввести пустую строку");
                }else{
                    ArrayList<Object> data = new ArrayList<>();
                    data.add(0);
                    data.add(id_disease);
                    data.add(id_sign);
                    data.add(id_quantity);
                    data.add(id_picture);
                    data.add(number);
                    data.add(value);
                    data.add(bottomLine);
                    data.add(upperBound);
                    dbHandler.insertRow(data, "periodvalues");
                    periodValuesAddButton.setText("Изменить");
                    periodValuesDeleteButton.setVisible(true);
                    periodValueChoiceBox.setVisible(false);
                    upperTextField.setEditable(false);
                    bottomTextField.setEditable(false);
                }
            }
            else{
                ModalWindow.errorWindow("Нельзя ввести пустую строку");
            }
        }else{
            if(periodValuesAddButton.getText().equals("Изменить")){
                periodValuesAddButton.setText("Подтвердить");
                upperTextField.setEditable(true);
                bottomTextField.setEditable(true);

                periodValuesDeleteButton.setVisible(false);
                periodValueChoiceBox.valueProperty().setValue(null);
                periodValueChoiceBox.setVisible(true);
            }else{
                if(periodValueChoiceBox.getItems()!=null && !upperTextField.getText().equals("")
                        && !bottomTextField.getText().equals("")){
                    int upperBound = Integer.parseInt(upperTextField.getText().trim());
                    int bottomLine = Integer.parseInt(bottomTextField.getText().trim());
                    String value = periodValueChoiceBox.getValue();
                    String conditional = "id_disease = " + id_disease + " AND id_sign = " + id_sign
                            + " AND number = " + number;
                    dbHandler.updateData("periodvalues", "upper_bound", upperBound, conditional);
                    dbHandler.updateData("periodvalues", "bottom_line", bottomLine, conditional);
                    dbHandler.updateData("periodvalues", "value", value, conditional);
                    periodValuesAddButton.setText("Изменить");
                    upperTextField.setEditable(false);
                    bottomTextField.setEditable(false);


                    periodValuesDeleteButton.setVisible(true);
                    periodValueChoiceBox.setVisible(false);
                }else{
                    ModalWindow.errorWindow("Нельзя ввести пустую строку");
                }
            }
        }
    }
    public static String removeLastChar(String s) {
        return (s == null || s.length() == 0) ? null : (s.substring(0, s.length() - 2));
    }

    public void addValue() {
        if(periodValueChoiceBox.getValue() != null){
            boolean flag = true;
            valueColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
            String value = periodValueChoiceBox.getValue();
            title periodValue = new title(value);
            ObservableList<title> arr = valueTable.getItems();
            for (title temp : arr){
                String tempString = temp.toString();
                if(tempString.equals(value)){
                    ModalWindow.errorWindow("Значение уже добавлено");
                    flag = false;
                }
            }
            if(flag){
                valueTable.getItems().add(periodValue);
            }
        }
        else{
            ModalWindow.errorWindow("Нельзя ввести пустую строку");
        }
    }
    public void deleteValue()  {
        valueTable.getItems().removeAll(valueTable.getSelectionModel().getSelectedItem());
    }


    //УДАЛЕНИЕ
    private void delete(String TableName, TableView<title> Table) throws SQLException, ClassNotFoundException {
        String name = Table.getSelectionModel().getSelectedItem().getTitle();
        String condition = "title = '" + name + "'";
        dbHandler.deleteData(TableName, condition);
    }

    private void deletePicture() throws SQLException, ClassNotFoundException {
        String title_diseases = diseasesChoiceBox.getValue().getTitle();
        int id_disease = dbHandler.getId("idDiseases", "diseases", title_diseases);
        String title = picturesTable.getSelectionModel().getSelectedItem().getTitle();
        String condition = "title_sign = '" + title + "' AND id_disease = " + id_disease;
        dbHandler.deleteData("pictures", condition);
        updatePicture();
    }

    private void deletePossibleValue(boolean flag) throws SQLException, ClassNotFoundException {
        String title_sign = signsForValueChoiceBox.getValue().getTitle();
        int id_sign = dbHandler.getId("idsigns", "signs", title_sign);
        if (flag){
            String title = possibleValuesTable.getSelectionModel().getSelectedItem().getTitle();
            String condition = "value_string = '" + title + "' AND id_sign = " + id_sign;
            dbHandler.deleteData("possible_values_str", condition);
        }
        else{
            String title = normalValuesTable.getSelectionModel().getSelectedItem().getTitle();
            String condition = "value_string = '" + title + "' AND id_sign = " + id_sign;
            dbHandler.deleteData("normal_values_str", condition);
        }
        updatePossibleValue(flag);
    }

    private void deleteQuantity() throws SQLException, ClassNotFoundException {
        if(!quantityTextField.getText().equals("")){
            String title_diseases = diseasesChoiceBox.getValue().getTitle();
            String title_sign = signsChoiceBox.getValue().getTitle();
            int id_disease = dbHandler.getId("idDiseases", "diseases", title_diseases);
            int id_sign = dbHandler.getId("idsigns", "signs", title_sign);
            int quantity = Integer.parseInt(quantityTextField.getText());
            String condition = "quantity = " + quantity + " AND id_disease = " + id_disease + " AND id_sign = " + id_sign;
            dbHandler.deleteData("quantityofperiods", condition);
        }
        else{
            ModalWindow.errorWindow("Нельзя удалить пустую строку");
        }
    }

    private void deletePeriods() throws SQLException, ClassNotFoundException {
        String title_diseases = diseasesChoiceBox.getValue().getTitle();
        String title_sign = signsChoiceBox.getValue().getTitle();
        int id_disease = dbHandler.getId("idDiseases", "diseases", title_diseases);
        int id_sign = dbHandler.getId("idsigns", "signs", title_sign);
        int number = periodsChoiceBox.getValue();
        String condition = "number = " + number + " AND id_disease = " + id_disease + " AND id_sign = " + id_sign;
        dbHandler.deleteData("periodvalues", condition);
    }

    //ОБНОВЛЕНИЯ ТАБЛИЦ
    private void update(String name, TableView<title> Table, TableColumn<title, String> Column)
            throws SQLException, ClassNotFoundException {
        ResultSet rs_disease = dbHandler.selectData("SELECT * FROM "+ name);
        ArrayList<ArrayList<String>> result = new ArrayList<>();
        while(rs_disease.next()){
            ArrayList<String> tmp = new ArrayList<>();
            tmp.add(rs_disease.getString(2));
            result.add(tmp);
        }
        Column.setCellValueFactory(new PropertyValueFactory<>("title"));
        ObservableList<title> diseasesData = FXCollections.observableArrayList();
        int n = result.size();
        for (int i = 0; i <= n-1; i++){
            diseasesData.add(new title(result.get(i).get(0)));
        }
        Table.setItems(diseasesData);
    }

    private void updatePicture() throws SQLException, ClassNotFoundException {
        String title_diseases = diseasesChoiceBox.getValue().getTitle();
        int id_disease = dbHandler.getId("idDiseases", "diseases", title_diseases);
        ResultSet rs_disease = dbHandler.selectData("SELECT * FROM pictures WHERE id_disease = " + id_disease);
        ArrayList<ArrayList<String>> result = new ArrayList<>();
        while(rs_disease.next()){
            ArrayList<String> tmp = new ArrayList<>();
            tmp.add(rs_disease.getString(2));
            result.add(tmp);
        }
        pictureColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        ObservableList<title> diseasesData = FXCollections.observableArrayList();
        int n = result.size();
        for (int i = 0; i <= n-1; i++){
            diseasesData.add(new title(result.get(i).get(0)));
        }
        picturesTable.setItems(diseasesData);
    }

    private void updatePossibleValue(boolean flag)
            throws SQLException, ClassNotFoundException {
        String title_sign = signsForValueChoiceBox.getValue().getTitle();
        int id_sign = dbHandler.getId("idsigns", "signs", title_sign);
        if(flag){
            ResultSet rs = dbHandler.selectData("SELECT value_string FROM possible_values_str WHERE id_sign = " + id_sign);
            possiblevalueColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
            ObservableList<title> diseasesData = FXCollections.observableArrayList();
            while(rs.next()){
                diseasesData.add(new title(rs.getString(1)));
            }
            possibleValuesTable.setItems(diseasesData);
        }else{
            ResultSet rs = dbHandler.selectData("SELECT value_string FROM normal_values_str WHERE id_sign = " + id_sign);
            normalvalueColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
            ObservableList<title> diseasesData = FXCollections.observableArrayList();
            while(rs.next()){
                diseasesData.add(new title(rs.getString(1)));
            }
            normalValuesTable.setItems(diseasesData);
        }
    }

    private void updateQuantity() throws SQLException, ClassNotFoundException {
        String title_diseases = diseasesChoiceBox.getValue().getTitle();
        String title_sign = signsChoiceBox.getValue().getTitle();
        int id_disease = dbHandler.getId("idDiseases", "diseases", title_diseases);
        int id_sign = dbHandler.getId("idsigns", "signs", title_sign);
        String quantity = dbHandler.getQuantity(id_disease, id_sign);
        if (quantity.equals("")){
            quantityAddButton.setText("+");
            periodsChoiceBox.getItems().clear();
        }else{
            periodsChoiceBox.getItems().clear();
            quantityAddButton.setText("И");
            int nums = Integer.parseInt(quantity);
            Integer[] periods = new Integer[nums];
            for(int i = 0 ; i < nums ; i++) {
                periods[i] = i+1;
            }
            periodsChoiceBox.getItems().addAll(periods);
        }
        quantityTextField.setText(quantity);
    }

    private void updatePeriods() throws SQLException, ClassNotFoundException {
        valueTable.getItems().clear();
        String title_diseases = diseasesChoiceBox.getValue().getTitle();
        String title_sign = signsChoiceBox.getValue().getTitle();
        int id_disease = dbHandler.getId("idDiseases", "diseases", title_diseases);
        int id_sign = dbHandler.getId("idsigns", "signs", title_sign);
        int number = periodsChoiceBox.getValue();
        ResultSet rs = dbHandler.selectData("SELECT * FROM periodvalues WHERE id_sign = "
                + id_sign + " AND id_disease = " + id_disease + " AND number = " + number);
        if(rs.next()){
            String bottom_line = rs.getString(8);
            String upper_bound = rs.getString(9);
            String value = rs.getString(7);
            String[] subStr;
            String separator = ", ";
            subStr = value.split(separator);
            for (String s : subStr) {
                valueColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
                title periodValue = new title(s);
                valueTable.getItems().add(periodValue);
            }
            periodValuesDeleteButton.setVisible(true);
            periodValueChoiceBox.setVisible(false);
            upperTextField.setText(upper_bound);
            upperTextField.setEditable(false);
            bottomTextField.setText(bottom_line);
            bottomTextField.setEditable(false);
            periodValuesAddButton.setText("Изменить");
        }else{
            periodValuesAddButton.setText("Добавить");
            upperTextField.clear();
            upperTextField.setEditable(true);
            bottomTextField.clear();
            bottomTextField.setEditable(true);

            periodValuesDeleteButton.setVisible(false);
            periodValueChoiceBox.setVisible(true);
        }
    }

    private void updateChoiceBox(ComboBox<title> ChoiceBox, TableView<title> Table) throws SQLException, ClassNotFoundException {
        ObservableList<title> result = Table.getItems();
        ChoiceBox.setItems(result);
    }
    private void updateChoiceBox() throws SQLException, ClassNotFoundException {
        periodsChoiceBox.getItems().clear();
        String title_diseases = diseasesChoiceBox.getValue().getTitle();
        String title_sign = signsChoiceBox.getValue().getTitle();
        int id_disease = dbHandler.getId("idDiseases", "diseases", title_diseases);
        int id_sign = dbHandler.getId("idsigns", "signs", title_sign);
        String quantity = dbHandler.getQuantity(id_disease, id_sign);
        int nums = Integer.parseInt(quantity);
        var periods = new Integer[nums];
        for(int i = 0 ; i < nums ; i++) {
            periods[i] = i+1;
        }
        periodsChoiceBox.getItems().addAll(periods);
    }
    private void updateValueChoiceBox(String sign) throws SQLException, ClassNotFoundException {
        int id_sign = dbHandler.getId("idsigns", "signs", sign);
        ResultSet rs = dbHandler.selectData("SELECT value_string FROM possible_values_str WHERE id_sign = " + id_sign);
        ObservableList<String> values = FXCollections.observableArrayList();
        while(rs.next()){
            values.add(rs.getString(1));
        }
        periodValueChoiceBox.setItems(values);
    }

    private void check() throws SQLException, ClassNotFoundException {
        boolean flag = true;
        checkResult.clear();
        checkResult.appendText("\t\t\t\tЗаболевания\n");
        ResultSet diseases = dbHandler.selectData("SELECT * FROM diseases");
        ArrayList<Integer> id_diseases = new ArrayList<>();
        while(diseases.next()){
            if(!dbHandler.exist("pictures", diseases.getInt(1), "id_disease")){
                checkResult.appendText(diseases.getString(2) + "\n\tОтсутствует клиническая картина\n");
            }else{
                id_diseases.add(diseases.getInt(1));
            }
        }
        for (Integer id_disease : id_diseases) {
            String diseasesTitle = dbHandler.getName("diseases", "idDiseases", id_disease);
            checkResult.appendText(diseasesTitle + "\n");
            ResultSet rs = dbHandler.selectData("SELECT id_sign FROM pictures WHERE id_disease = " + id_disease);
            while (rs.next()) {
                String signTitle = dbHandler.getName("signs", "idsigns", rs.getInt(1));
                checkResult.appendText("\t" + signTitle + ":\n");
                if (!dbHandler.exist("quantityofperiods", id_disease, rs.getInt(1))) {
                    checkResult.appendText("\t\tНе заполнено ЧПД\n");
                } else {
                    int quantityId = dbHandler.getQuantityId(id_disease, rs.getInt(1));
                    int quantity = Integer.parseInt(dbHandler.getQuantity(id_disease, rs.getInt(1)));
                    for (int j = 1; j <= quantity; j++) {
                        if (!dbHandler.existValue(id_disease, rs.getInt(1), quantityId, j)) {
                            checkResult.appendText("\t\tУ периода динамики " + j + " не заполнено ЗДП\n");
                        }
                    }
                }
            }
        }
        checkResult.appendText("\n\t\t\t\tПризнаки\n");
        ResultSet signs = dbHandler.selectData("SELECT * FROM signs");
        boolean signFlag1, signFlag2, signFlag3;
        while(signs.next()){
            signFlag1 = true;
            signFlag2 = true;
            signFlag3 = true;
            if (!dbHandler.exist("possible_values_str", signs.getInt(1), "id_sign")){
                signFlag1 = false;
            }
            if (!dbHandler.exist("normal_values_str", signs.getInt(1), "id_sign")){
                signFlag2 = false;
            }
            if(!dbHandler.exist("pictures", signs.getInt(1), "id_sign")){
                signFlag3 = false;
            }
            if(!signFlag1 || !signFlag2 || !signFlag3){
                checkResult.appendText(signs.getString(2) + ":\n");
                if(!signFlag1){
                    checkResult.appendText("\tНе заполнены возможные значения\n");
                }
                if(!signFlag2){
                    checkResult.appendText("\tНе заполнены нормальные значения\n");
                }
                if(!signFlag3){
                    checkResult.appendText("\tПризнака нет ни в одной клинической картине\n");
                }
            }
        }
        if (flag){
            checkResultLabel.setText("Диагностика возможна");
        }else{
            checkResultLabel.setText("Диагностика невозможна");

        }
    }
}

/*Pattern pat=Pattern.compile("[-]?[0-9]+(.[0-9]+)?");
                    Matcher matcher=pat.matcher(value);
                    ArrayList<Float> values = new ArrayList<>();
                    while (matcher.find()) {
                        values.add(Float.parseFloat(matcher.group()));
                    }
                    bottom = values.get(0);
                    upper = values.get(1);*/
