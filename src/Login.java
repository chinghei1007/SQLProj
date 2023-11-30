import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.security.MessageDigest;
import java.util.Scanner;

public class Login {
    private static final String URL = "jdbc:h2:./h2/src";
    private static final String USERNAME = "test";
    private static final String PASSWORD = "123";

    public void authenticate() {
        Scanner scanner = new Scanner(System.in);
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
                String storedPasswordHash = rs.getString("password");

                if (password.equals(storedPasswordHash)) {
                    if (isAdmin) {
                        DeveloperMode developMode = new DeveloperMode();
                        developMode.display(conn);
                    } else {
                        UserMode userMode = new UserMode();
                        System.out.println("User mode");// userMode.display(conn);
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
