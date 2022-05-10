package sample;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseHandler extends Configs {
    private static Connection dbConnection;

    // Конструктор
    DatabaseHandler(){
        dbConnection =  null;
    }

    // Соединение с БД
    private Connection getDbConnection()
            throws ClassNotFoundException, SQLException {
        String connectionString = "jdbc:mysql://" + dbHost + ":"
                + dbPort + "/" + dbName + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

        Class.forName("com.mysql.cj.jdbc.Driver");

        dbConnection = DriverManager.getConnection(connectionString,
                dbUser,dbPass);

        return dbConnection;
    }



    public int getId(String id, String table, String title) throws SQLException, ClassNotFoundException {
        ResultSet rs = selectData("SELECT " + id + " FROM " + table + " WHERE title = '" + title + "'");
        rs.next();
        int result = rs.getInt(1);
        return result;
    }
    public int getQuantityId(int quantity, int id_disease, int id_sign) throws SQLException, ClassNotFoundException {
        ResultSet rs = selectData("SELECT idquantityofperiods FROM quantityofperiods WHERE quantity = "
                        + quantity + " AND id_disease = " + id_disease + " AND id_sign = " + id_sign);
        rs.next();
        int result = rs.getInt(1);
        return result;
    }
    public int getQuantityId(int id_disease, int id_sign) throws SQLException, ClassNotFoundException {
        ResultSet rs = selectData("SELECT idquantityofperiods FROM quantityofperiods WHERE " +
                "id_disease = " + id_disease + " AND id_sign = " + id_sign);
        rs.next();
        return rs.getInt(1);
    }

    public int getIdPicture(int id_disease, int id_sign) throws SQLException, ClassNotFoundException {
        ResultSet rs = selectData("SELECT idpictures FROM pictures WHERE id_disease = " + id_disease + " AND id_sign = " + id_sign);
        rs.next();
        return rs.getInt(1);
    }

    public String getType(String title) throws SQLException, ClassNotFoundException {
        ResultSet rs = selectData("SELECT type FROM signs WHERE title = '" + title + "'");
        rs.next();
        String result = rs.getString(1);
        return result;
    }
    public String getName(String table, String idField, int id) throws SQLException, ClassNotFoundException {
        ResultSet rs = selectData("SELECT title FROM " + table + " WHERE " + idField + " = " + id);
        rs.next();
        return rs.getString(1);
    }

    public boolean exist(String table, int id_disease, int id_sign) throws SQLException, ClassNotFoundException {
        ResultSet rs = selectData("SELECT * FROM " + table +  " WHERE id_disease = " + id_disease + " AND id_sign = " + id_sign);
        if (rs.next()){
            return true;
        }else{
            return false;
        }
    }
    public boolean exist(String table, int id, String name) throws SQLException, ClassNotFoundException {
        ResultSet rs = selectData("SELECT * FROM " + table +  " WHERE " + name + " = " + id);
        if (rs.next()){
            return true;
        }else{
            return false;
        }
    }
    public String getQuantity(int id_disease, int id_sign) throws SQLException, ClassNotFoundException {
        ResultSet rs = selectData("SELECT quantity FROM quantityofperiods WHERE id_disease = " + id_disease + " AND id_sign = " + id_sign);
        String result = "";
        if(rs.next()){
            result = rs.getString(1);
        }
        return result;
    }

    public boolean existValue(int id_disease, int id_sign, int id_quantity, int number) throws SQLException, ClassNotFoundException {
        ResultSet rs = selectData("SELECT * FROM periodvalues WHERE id_disease = " + id_disease + " AND id_sign = "
                + id_sign + " AND id_quantity = " + id_quantity + " AND number = " + number);
        if (rs.next()){
            return true;
        }else{
            return false;
        }
    }


    // Метод извлечения данных из БД
    public ResultSet selectData(String sqlQuery)
            throws SQLException, ClassNotFoundException {

        PreparedStatement prSt = getDbConnection().prepareStatement(sqlQuery);
        ResultSet resSet = prSt.executeQuery();

        return resSet;
    }
    // Метод вставки строки
    public void insertRow(ArrayList<Object> values, String table)
            throws SQLException, ClassNotFoundException {

        ResultSet rs = selectData("SELECT * FROM " + table);
        assert rs != null;
        ResultSetMetaData rsmd = rs.getMetaData();

        // Количество колонок в результирующем запросе
        int columns = rs.getMetaData().getColumnCount();

        StringBuilder header = new StringBuilder("("); //Строка наименований полей
        for (int i = 1; i <= columns; i++) {
            header.append(rsmd.getColumnName(i)).append(", ");
        }

        header.deleteCharAt(header.length() - 1).deleteCharAt(header.length() - 1).append(")");

        StringBuilder d = new StringBuilder("('");

        // The column count starts from 1
        for (int i = 0; i < columns; i++) {
            d.append(values.get(i)).append("', '");
        }
        d.deleteCharAt(d.length() - 1).deleteCharAt(d.length() - 1).deleteCharAt(d.length() - 1).append(")");

        PreparedStatement prSt = getDbConnection().prepareStatement("INSERT INTO " + table + " " + header + " VALUES" + d + ";");
        prSt.executeUpdate();
    }
    public void updateData(String table, String column, int value, String condition)
            throws SQLException, ClassNotFoundException {
        PreparedStatement prSt = getDbConnection().prepareStatement("UPDATE " + table + " SET " + column + " = " + value + " WHERE " + condition);
        prSt.executeUpdate();
    }
    public void updateData(String table, String column, String value, String condition)
            throws SQLException, ClassNotFoundException {
        PreparedStatement prSt = getDbConnection().prepareStatement("UPDATE " + table + " SET " + column + " = '" + value + "' WHERE " + condition);
        prSt.executeUpdate();
    }
    public void deleteData(String table, String condition)
            throws SQLException, ClassNotFoundException {
        PreparedStatement prSt = getDbConnection().prepareStatement("DELETE from " + table + " WHERE " + condition + ";");
        prSt.executeUpdate();
    }

}
