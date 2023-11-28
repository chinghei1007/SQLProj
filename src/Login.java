import java.io.IOException;
import java.sql.SQLException;
import java.sql.*;
import java.util.Scanner;

public class Login {
    public static void login(int choice) throws IOException, InterruptedException{
        Scanner scanner = new Scanner(System.in);
        Connection connection;
        while(true) {
            try{
                System.out.println("Please input your username: ");
                String username = scanner.nextLine();
                System.out.println("Please input your password: ");
                String password = scanner.nextLine();
                DriverManager.registerDriver(new org.h2.Driver());
                connection = DriverManager.getConnection("jdbc:h2:./h2/src",username,password);
                break;
            }catch (SQLException e){
                System.out.println("Either your username or password is incorrect, please try again");
            }
        }
        if (choice == 1){
            userMode(connection);

        }else if (choice == 2){
            developerMode(connection);
        }
    }

    private static void developerMode(Connection connection) {
    }

    private static void userMode(Connection connection) {
        System.out.println("Login successfully");
        //Home 1.Surfing 2.Shopping Cart 3.Exit
        //Surfing 1.Show all 2.Filtered
        //Filtered 1.Category 2.Price Range 3.Brand
    }


}