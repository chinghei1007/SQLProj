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
        //statement.executeUpdate("alter table users add column address");
        //statement.executeUpdate("create table payment_history (id int primary key auto_increment, Prod_name varchar(100), price decimal(10,2), payment_method varchar(100), review int)");
        //statement.executeUpdate("drop table users");
        //statement.executeUpdate("insert into users (username, password) values ('mike', 'pw123')");
        /*statement.executeUpdate("create table Users (id INT PRIMARY KEY AUTO_INCREMENT," +
                "  username VARCHAR(50) UNIQUE NOT NULL," +
                "  password VARCHAR(255) NOT NULL,  " +
                "  is_admin BOOLEAN DEFAULT FALSE," +
                "  shipping_address TEXT" +
                ");");
         */
        //statement.executeUpdate("update users set shipping_address = 'nope' where username = 'john'");
        //statement.executeUpdate("delete from shoppingcart");
        //statement.executeUpdate("ALTER TABLE products " + "ADD COLUMN quantity int;");
        //statement.executeUpdate("UPDATE products" + " SET quantity = FLOOR(RAND() * 101);");

        //statement.executeUpdate("ALTER TABLE shoppingcart ADD COLUMN amount int;");
        //statement.executeUpdate("ALTER TABLE products aLTER COLUMN id SET PRIMARY KEY AUTO_INCREMENT");
        //statement.executeUpdate("create table products (id int, name varchar(100), category varchar(100), price decimal(10,2), brand varchar(100))");
        //statement.executeUpdate("create table shoppingcart(id INT PRIMARY KEY AUTO_INCREMENT, username varchar(100), prodID int)");

        //john, pw123
        //statement.executeUpdate("INSERT INTO Users (username, password, is_admin, shipping_address) VALUES ('john', 'pw123', FALSE, '123 Main St');");
        //statement.executeUpdate("insert into products1 select id,name,category,price,brand from products");
        //statement.executeUpdate("DROP TABLE products;");

        //statement.executeUpdate("create table Users, ");
        //ShowTable.showtable(statement,"select * from payment_history");
        //ShowTable.showtable(statement, "select count(*), p.price from products p, shoppingcart s where s.username = 'john' and p.id = s.prodid and s.prodid =1 group by s.prodid");
        //ShowTable.showtable(statement,"select p.name,s.amount, p.price from products p, shoppingcart s where s.username = '" + username + "' and p.id = s.prodid");
        ShowTable.showtable(statement,"Select * from users");




        //添加record1
        /*
        BufferedReader reader = new BufferedReader(new FileReader("records1.txt"));
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
        /*String sql = "select id, name, brand, price from products order by rand() limit 40;";
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
