import javax.swing.plaf.nimbus.State;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class User {
    List<Integer> cartList = new ArrayList<>();
    public static void home(Connection connection){
        int choice = 0;
        System.out.println("Menu" +
                "\n1. Surf for products" +
                "\n2. Shopping Cart" +
                "\n3. Exit");
        Scanner scanner = new Scanner(System.in);
        while(true){
            try{
                choice = scanner.nextInt();
                switch (choice){
                    case 1:
                        surfing(connection);
                        break;
                    case 2:
                        shoppingCart(connection);
                        break;
                    case 3:
                        System.out.println("The system will now terminate");
                        return;
                    default:
                        System.out.println("Error: Please only input numbers in the menu");
                }
            }catch(Exception e){
                System.out.println("Error: Please only input numbers");
            }
        }
    }

    public static void surfing(Connection connection) throws SQLException {
        System.out.println("Here are all the products available");
        Statement statement = connection.createStatement();
    }
    public static void shoppingCart(Connection connection){

    }
}
