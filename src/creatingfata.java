import org.h2.Driver;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class creatingfata {
    //public static void dataInsert() throws SQLException {
    public static void main(String[] args) throws SQLException, IOException {

        Connection connection;
        String username = "test";
        String password = "123";
        String url = "jdbc:h2:./h2/src";
        DriverManager.registerDriver(new org.h2.Driver());
        connection = DriverManager.getConnection(url,username,password);
        Statement statement = connection.createStatement();

        //製造table
        //statement.executeUpdate("create table products (id INT, name VARCHAR(256), category VARCHAR(20), price Decimal(12),brand VARCHAR(50) )");
        //statement.executeUpdate("insert into products1 select id,name,category,price,brand from products");
        //statement.executeUpdate("DROP TABLE products;");


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
        String sql = "select name, brand, price from products order by random() limit 40;";
        ShowTable.showtable(statement,sql);
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
