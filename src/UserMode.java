import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class UserMode {
    public static void userMode(Connection connection) throws SQLException {
        System.out.println("Login successfully");
        //Home 1.Surfing 2.Shopping Cart 3.Exit
        //Surfing 1.Show all 2.Filtered
        //Filtered 1.Category 2.Price Range 3.Brand
        while(true){
            System.out.println("Menu: ");
            System.out.println("What would you like to do?");
            System.out.println("\t1. Look for products");
            System.out.println("\t2. Shopping cart");
            System.out.println("\t3. Terminate system");
            Scanner scanner = new Scanner(System.in);
            int choice = 0;
            try{
                choice = scanner.nextInt();
            }catch (Exception e){
                System.out.println("Please input only numbers");
            }
            switch (choice){
                case 1: //look for products
                    lookproducts(connection);
                    continue;
                case 2:
                    shoppingcart(connection);
                    continue;
                case 3:
                    System.out.println("The system will now terminate");
                    return;
                default:
                    System.out.println("Please input numbers in range");
            }


        }
    }
    private static void lookproducts(Connection connection) throws SQLException {
        System.out.println("Here are some products for you: ");
        Statement statement = connection.createStatement();
        String sql = "select name, brand, price from products order by random() limit 40;";
        ShowTable.showtable(statement,sql);
    }
    private static void shoppingcart(Connection connection) {
    }
}
