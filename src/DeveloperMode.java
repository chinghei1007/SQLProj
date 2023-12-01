import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.sql.SQLException;

public class DeveloperMode {
    private Scanner scanner = new Scanner(System.in);

    public void display(Connection conn) {
        System.out.println("1. Add product");
        System.out.println("2. Update product");
        System.out.println("3. Delete product");
        System.out.println("4. View orders");
        System.out.println("5. Logout");

        System.out.print("Enter choice: ");
        while (true) {
            try {
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
                        System.out.println("Invalid choice please input number 1 to 5");
                        display(conn);
                }
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input format. Please enter an integer for the choice.");
                scanner.nextLine();
            }
        }
    }

    private void addProduct(Connection conn) {
        try {
            System.out.print("Enter the product ID: ");
            int productID;
            while (true) {
                try {
                    productID = scanner.nextInt();
                    scanner.nextLine();
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input format. Please enter an integer for the product ID.");
                    scanner.nextLine();
                }
            }

            if (isProductIdExists(conn, productID)) {
                System.out.println("Product ID already exists. Please change the ID or update the existing product.");
                addProduct(conn);
            }

            System.out.print("Enter the Category: ");
            String category = scanner.nextLine();

            System.out.print("Enter product price: ");
            double productPrice;
            while (true) {
                try {
                    productPrice = scanner.nextDouble();
                    scanner.nextLine();
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input format. Please enter a valid number for the product price.");
                    scanner.nextLine();
                }
            }

            System.out.print("Enter the Brand: ");
            String brand = scanner.nextLine();

            System.out.print("Enter product description: ");
            String productDescription = scanner.nextLine();

            System.out.print("Enter product Quantity: ");
            int productQuantity;
            while (true) {
                try {
                    productQuantity = scanner.nextInt();
                    scanner.nextLine();
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input format. Please enter an integer for the product Quantity.");
                    scanner.nextLine();
                }
            }

            System.out.print("Enter product name: ");
            String productName = scanner.nextLine();

            String sql = "INSERT INTO Products (id, Category, Price, Brand, Description, Quantity, Name) VALUES  (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, productID);
            stmt.setString(2, category);
            stmt.setDouble(3, productPrice);
            stmt.setString(4, brand);
            stmt.setString(5, productDescription);
            stmt.setInt(6, productQuantity);
            stmt.setString(7, productName);
            stmt.executeUpdate();

            System.out.println("Product added successfully!");
            System.out.println("Added Product Information:");
            System.out.println("Product ID: " + productID);
            System.out.println("Category: " + category);
            System.out.println("Price: " + productPrice);
            System.out.println("Brand: " + brand);
            System.out.println("Description: " + productDescription);
            System.out.println("Stock: " + productQuantity);
            System.out.println("Name: " + productName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        display(conn);
    }

    private boolean isProductIdExists(Connection conn, int productID) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Products WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, productID);
        ResultSet rs = stmt.executeQuery();
        rs.next();
        int count = rs.getInt(1);
        return count > 0;
    }


    private void updateProduct(Connection conn) {
        try {
            System.out.print("Enter product ID to update: ");
            int productID = scanner.nextInt();
            scanner.nextLine();

            // Check if the product exists
            if (!isProductIdExists(conn, productID)) {
                System.out.println("Product ID does not exist. Please enter a valid product ID.");
                updateProduct(conn);
            }

            System.out.print("Enter new product category: ");
            String category = scanner.nextLine();

            System.out.print("Enter new product price: ");
            double productPrice;
            while (true) {
                try {
                    productPrice = scanner.nextDouble();
                    scanner.nextLine();
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input format. Please enter a valid number for the product price.");
                    scanner.nextLine();
                }
            }

            System.out.print("Enter new product brand: ");
            String brand = scanner.nextLine();

            System.out.print("Enter new product description: ");
            String productDescription = scanner.nextLine();

            System.out.print("Enter new product Quantity: ");
            int productQuantity;
            while (true) {
                try {
                    productQuantity = scanner.nextInt();
                    scanner.nextLine();
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input format. Please enter an integer for the product Quantity.");
                    scanner.nextLine();
                }
            }

            System.out.print("Enter new product name: ");
            String productName = scanner.nextLine();

            // Prepare the SQL update statement
            String sql = "UPDATE Products SET Category = ?, Price = ?, Brand = ?, Description = ?, Quantity = ?, Name = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, category);
            stmt.setDouble(2, productPrice);
            stmt.setString(3, brand);
            stmt.setString(4, productDescription);
            stmt.setInt(5, productQuantity);
            stmt.setString(6, productName);
            stmt.setInt(7, productID);

            // Execute the update statement
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Product updated successfully!");
            } else {
                System.out.println("Failed to update the product.");
            }

            // Print the updated product information
            System.out.println("Updated Product Information:");
            System.out.println("Product ID: " + productID);
            System.out.println("Category: " + category);
            System.out.println("Price: " + productPrice);
            System.out.println("Brand: " + brand);
            System.out.println("Description: " + productDescription);
            System.out.println("Stock: " + productQuantity);
            System.out.println("Name: " + productName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        display(conn);
    }

    private void deleteProduct(Connection conn) {
        try {
            System.out.print("Enter product ID to delete: ");
            int productID;
            while (true) {
                try {
                    productID = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline character
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input format. Please enter a valid integer for the product ID.");
                    scanner.nextLine(); // Consume the invalid input
                }
            }

            // Check if the product exists
            if (!isProductIdExists(conn, productID)) {
                System.out.println("Product ID does not exist. Please enter a valid product ID.");
                return;
            }

            String retrieveSql = "SELECT * FROM Products WHERE id = ?";
            PreparedStatement retrieveStmt = conn.prepareStatement(retrieveSql);
            retrieveStmt.setInt(1, productID);
            ResultSet resultSet = retrieveStmt.executeQuery();

            // Store the deleted product information
            int deletedProductId = 0;
            String deletedCategory = "";
            double deletedPrice = 0.0;
            String deletedBrand = "";
            String deletedDescription = "";
            int deletedQuantity = 0;
            String deletedName = "";

            if (resultSet.next()) {
                deletedProductId = resultSet.getInt("id");
                deletedCategory = resultSet.getString("Category");
                deletedPrice = resultSet.getDouble("Price");
                deletedBrand = resultSet.getString("Brand");
                deletedDescription = resultSet.getString("Description");
                deletedQuantity = resultSet.getInt("Quantity");
                deletedName = resultSet.getString("Name");
            }

            String deleteSql = "DELETE FROM Products WHERE id = ?";
            PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);
            deleteStmt.setInt(1, productID);
            deleteStmt.executeUpdate();

            System.out.println("Product deleted successfully!");

            // Print the deleted product information
            System.out.println("Deleted Product Information:");
            System.out.println("Product ID: " + deletedProductId);
            System.out.println("Category: " + deletedCategory);
            System.out.println("Price: " + deletedPrice);
            System.out.println("Brand: " + deletedBrand);
            System.out.println("Description: " + deletedDescription);
            System.out.println("Stock: " + deletedQuantity);
            System.out.println("Name: " + deletedName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        display(conn);
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
