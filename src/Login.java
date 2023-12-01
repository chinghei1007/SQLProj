import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Login {
    private static final String URL = "jdbc:h2:./h2/src";
    private static final String USERNAME = "test";
    private static final String PASSWORD = "123";

    public void authenticate() {
        public void authenticate() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("1. Create Account");
        System.out.println("2. Log In");

        int choice = 0;
        boolean validChoice = false;
        while (!validChoice) {
            System.out.print("Enter your choice (1 or 2): ");
            String input = scanner.nextLine().trim();

            try {
                choice = Integer.parseInt(input);
                if (choice == 1 || choice == 2) {
                    validChoice = true;
                } else {
                    System.out.println("Invalid choice. Please enter 1 or 2.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number (1 or 2).");
            }
        }

        if (choice == 1) {
            // Create Account
            createAccount();
        } else if (choice == 2) {
            System.out.print("Enter username: ");
            String username = scanner.nextLine().trim();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            Connection conn = null;
            PreparedStatement stmt = null;
            ResultSet rs = null;

            try {
                conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

                String sql = "SELECT * FROM Users WHERE Username = ?";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, username);

                rs = stmt.executeQuery();

                if (rs.next()) {
                    boolean isAdmin = rs.getBoolean("Is_Admin");
                    String storedPasswordHash = rs.getString("Password");

                    if (verifyPassword(password, storedPasswordHash)) {
                        if (isAdmin) {
                            DeveloperMode developMode = new DeveloperMode();
                            developMode.display(conn);
                        } else {
                            UserMode userMode = new UserMode();
                            System.out.println("User mode");
                            UserMode.userMode(conn, username);
                        }
                    } else {
                        System.out.println("Login failed");
                    }
                } else {
                    System.out.println("Username not found");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (rs != null) rs.close();
                    if (stmt != null) stmt.close();
                    if (conn != null) conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void createAccount() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Creating a new account\n");
        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            // Check if the username already exists
            String checkUsernameSql = "SELECT * FROM Users WHERE Username = ?";
            stmt = conn.prepareStatement(checkUsernameSql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("Username already exists. Please choose a different username.");
                createAccount();
            } else {
                // Generate password hash
                String hashedPassword = hashPassword(password);

                // Insert the new account into the database
                String insertSql = "INSERT INTO Users (Username, Password, Is_Admin) VALUES (?, ?, ?)";
                stmt = conn.prepareStatement(insertSql);
                stmt.setString(1, username);
                stmt.setString(2, hashedPassword);
                stmt.setBoolean(3, false); // Assuming the new account is not an admin

                int affectedRows = stmt.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Account created successfully!");
                    authenticate();
                } else {
                    System.out.println("Failed to create account. Please try again.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean verifyPassword(String password, String storedPasswordHash) {
        String hashedPassword = hashPassword(password);
        return hashedPassword != null && hashedPassword.equals(storedPasswordHash);
    }
}
