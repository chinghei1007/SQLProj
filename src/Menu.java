import java.io.IOException;
import java.util.Scanner;

public class Menu {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Welcome to the Online Shopping System");
        loginMenu();
    }
    public static void loginMenu() throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        int choice = 0;
        System.out.println("Please select an option" +
                "\n1. User" +
                "\n2. Developer");
        try{
            choice = scanner.nextInt();
        }catch (Exception e){
            System.out.println("Error, Please input only integers");
        }

        switch (choice){
            case 1:
                Login.login(1);
                break;
            case 2:
                Login.login(2);
                break;

        }
    }


}
