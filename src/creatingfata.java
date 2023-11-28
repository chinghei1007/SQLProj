import org.h2.Driver;

import java.sql.*;

public class creatingfata {
    //public static void dataInsert() throws SQLException {
    public static void main(String[] args) throws SQLException {

        Connection connection;
        String username = "test";
        String password = "123";
        String url = "jdbc:h2:./h2/src";
        DriverManager.registerDriver(new org.h2.Driver());
        connection = DriverManager.getConnection(url,username,password);
        Statement statement = connection.createStatement();
        statement.executeQuery("select * from products");
        //statement.executeUpdate("create table products (id INT, category VARCHAR(20), price Decimal(12),brand VARCHAR(50), name VARCHAR(70))");


    }
}
