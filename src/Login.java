import java.io.IOException;
import java.sql.SQLException;
import java.sql.*;
import java.util.Scanner;

public class Login {
    public static void login() throws IOException, InterruptedException, SQLException {
        Scanner scanner = new Scanner(System.in);
        Connection connection;
        String username;
        String password;
        while(true) {
            try{
                System.out.println("Please input your username: ");
                username = scanner.nextLine().trim();
                System.out.println("Please input your password: ");
                password = scanner.nextLine();
                DriverManager.registerDriver(new org.h2.Driver());
                connection = DriverManager.getConnection("jdbc:h2:./h2/src",username,password);
                break;
            }catch (SQLException e){
                System.out.println("Either your username or password is incorrect, please try again");
            }
        }
        if (!username.trim().equalsIgnoreCase("admin")){
            UserMode.userMode(connection);

        }else{
            DeveloperMode.developerMode(connection);
        }
    }
}