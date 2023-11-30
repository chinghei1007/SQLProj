import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class Menu {
    public static void main(String[] args) throws IOException, InterruptedException, SQLException {
        System.out.println("Welcome to the Online Shopping System");
        Login login = new Login();
        login.authenticate();
    }
}
