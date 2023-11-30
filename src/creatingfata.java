import org.h2.Driver;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class creatingfata {
    //public static void dataInsert() throws SQLException {
    public static void main(String[] args) throws SQLException, IOException, NoSuchAlgorithmException {

        Connection connection;
        String username = "test";
        String password = "123";
        String url = "jdbc:h2:./h2/src";
        DriverManager.registerDriver(new org.h2.Driver());
        connection = DriverManager.getConnection(url,username,password);
        Statement statement = connection.createStatement();

        //製造table
        statement.executeUpdate("drop table users");
        statement.executeUpdate("create table Users (id INT PRIMARY KEY AUTO_INCREMENT," +
                "  username VARCHAR(50) UNIQUE NOT NULL," +
                "  password VARCHAR(255) NOT NULL,  " +
                "  is_admin BOOLEAN DEFAULT FALSE," +
                "  shipping_address TEXT" +
                ");");
        //john, pw123
        statement.executeUpdate("INSERT INTO Users (username, password, is_admin, shipping_address) VALUES ('john', 'pw123', FALSE, '123 Main St');");
        //statement.executeUpdate("insert into products1 select id,name,category,price,brand from products");
        //statement.executeUpdate("DROP TABLE products;");

        //add column quantity
        //statement.executeUpdate("create table Users, ");
        ShowTable.showtable(statement,"select * from Users");


        //添加record1
        /*BufferedReader reader = new BufferedReader(new FileReader("records1.txt"));
        List<String> satement = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null){
            if (line.isEmpty()){continue;}
            satement.add(line);
        }
        for (String string : satement) {
            statement.executeUpdate(string);
        }
*/
        /*String sql = "select id, name, brand, price from products order by random() limit 40;";
        SwingUtilities.invokeLater(() -> {
            try {
                ShowTable.showtable(statement,sql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        System.out.println("this is the proof of the program continue running after the table appeared");
        /*
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        int colCount=resultSetMetaData.getColumnCount();
        System.out.println(colCount);

        DefaultTableModel tableModel = new DefaultTableModel();
        String[] colNames = new String[colCount];
        for (int i = 1; i < colCount+1; i++){
            tableModel.addColumn(resultSetMetaData.getColumnName(i));
            colNames[i-1] = resultSetMetaData.getColumnName(i);
        }
        JTable table = new JTable(tableModel);
        JScrollPane jScrollPane = new JScrollPane(table);

        JFrame jFrame = new JFrame();
        jFrame.setTitle("Test");
        jFrame.setSize(500,600);
        jFrame.add(jScrollPane);
        jFrame.setVisible(true);
*/


    }
}
