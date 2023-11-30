import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class DeveloperMode {
    private Scanner scanner = new Scanner(System.in);

    public void display(Connection conn) {
        System.out.println("1. Add product");
        System.out.println("2. Update product");
        System.out.println("3. Delete product");
        System.out.println("4. View orders");
        System.out.println("5. Logout");

        System.out.print("Enter choice: ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                // Add product
                addProduct(conn);
                break;
            case 2:
                // Update product
                updateProduct(conn);
                break;
            case 3:
                // Delete product
                deleteProduct(conn);
                break;
            case 4:
                // View orders
                viewOrders(conn);
                break;
            case 5:
                // Logout
                System.out.println("Logged out");
                break;
            default:
                System.out.println("Invalid choice");
                break;
        }
    }

    private void addProduct(Connection conn) {
        try {
            System.out.print("Enter product name: ");
            String productName = scanner.nextLine();
            System.out.print("Enter product description: ");
            String productDescription = scanner.nextLine();
            System.out.print("Enter product price: ");
            double productPrice = scanner.nextDouble();
            System.out.print("Enter product stock: ");
            int productStock = scanner.nextInt();
            System.out.print("Enter product category ID: ");
            int productCategoryID = scanner.nextInt();

            String sql = "INSERT INTO Products (ProductName, Description, Price, Stock, CategoryID) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, productName);
            stmt.setString(2, productDescription);
            stmt.setDouble(3, productPrice);
            stmt.setInt(4, productStock);
            stmt.setInt(5, productCategoryID);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateProduct(Connection conn) {
        try {
            System.out.print("Enter product ID to update: ");
            int productID = scanner.nextInt();
            System.out.print("Enter new product name: ");
            String productName = scanner.nextLine();
            System.out.print("Enter new product description: ");
            String productDescription = scanner.nextLine();
            System.out.print("Enter new product price: ");
            double productPrice = scanner.nextDouble();
            System.out.print("Enter new product stock: ");
            int productStock = scanner.nextInt();
            System.out.print("Enter new product category ID: ");
            int productCategoryID = scanner.nextInt();

            String sql = "UPDATE Products SET ProductName = ?, Description = ?, Price = ?, Stock = ?, CategoryID = ? WHERE ProductID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, productName);
            stmt.setString(2, productDescription);
            stmt.setDouble(3, productPrice);
            stmt.setInt(4, productStock);
            stmt.setInt(5, productCategoryID);
            stmt.setInt(6, productID);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteProduct(Connection conn) {
        try {
            System.out.print("Enter product ID to delete: ");
            int productID = scanner.nextInt();

            String sql = "DELETE FROM Products WHERE ProductID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, productID);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void viewOrders(Connection conn) {
        try {
            String sql = "SELECT * FROM Orders";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                System.out.println("Order ID: " + rs.getInt("OrderID"));
                System.out.println("User ID: " + rs.getInt("UserID"));
                System.out.println("Order Date: " + rs.getDate("OrderDate"));
                System.out.println("Shipping Address ID: " + rs.getInt("ShippingAddressID"));
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
