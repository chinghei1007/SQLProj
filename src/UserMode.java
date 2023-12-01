import javax.swing.*;
import javax.swing.plaf.nimbus.State;
import java.math.BigDecimal;
import java.sql.*;
import java.text.NumberFormat;
import java.util.*;

public class UserMode {
    List<String> shoppingCart = new ArrayList<>();
    public static void userMode(Connection connection, String username) throws Exception {
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
                    lookproducts(connection,username);
                    continue;
                case 2:
                    shoppingcart(connection,username);
                    continue;
                case 3:
                    System.out.println("The system will now terminate");
                    return;
                default:
                    System.out.println("Please input numbers in range");
            }


        }
    }
    private static void lookproducts(Connection connection, String username) throws Exception {
        System.out.println("Here are some products for you: ");
        Statement statement = connection.createStatement();
        String sql = "select id, name, brand, price, quantity from products order by rand() limit 30;";
        SwingUtilities.invokeLater(() -> {
            try {
                ShowTable.showtable(statement, sql);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        while (true) {
            System.out.println("Would you like to add any of them to the shopping cart? (Y/N)");

        String input;
        int quantityinDb = 0;
        String name = "";
        String productID = "";
        Scanner scanner = new Scanner(System.in);
        input = scanner.nextLine();
        switch (input.toLowerCase()) {
            case "y":
                while (true) {
                    System.out.println("Please input the Product ID");
                    productID = scanner.nextLine().trim();
                    if (isPositiveInteger(productID)) {
                        int count = 0;
                        ResultSet resultSet = statement.executeQuery("select  name, count(*), quantity from products where id = " + productID + ";");
                        if (resultSet.next()) {
                            quantityinDb = resultSet.getInt("quantity");
                            count = resultSet.getInt("count(*)");
                            name = resultSet.getString("name");
                        }
                        if (count == 0) {
                            System.out.println("Product not found, Please try again");
                            continue;
                        } else {
                            addtoshoppingcart(username, statement, name, quantityinDb, Integer.parseInt(productID));
                            break;
                        }
                    }
                }
            case "n":
                System.out.println("What you wish to do next?");
                System.out.println("1. Search for more products");
                System.out.println("2. Look at items in shopping cart");
                System.out.println("3. Return to main menu");
                String choice = "";
                choice = scanner.nextLine();
                switch (choice){
                    case "1":
                        lookProductscategory(connection, username);
                        continue;
                    case "2":
                        shoppingcart(connection,username);
                        continue;
                    case "3":
                        return;
                }
                break;
            default:
                System.out.println("Invalid input, please try again.");
        }
        }
    }
    private static void lookProductscategory(Connection connection, String username) throws SQLException{
        Statement statement = connection.createStatement();
        Scanner scanner = new Scanner(System.in);
        String choice;
        ResultSet resultSet = statement.executeQuery("Select distinct category from products group by category");
        List<String> categories = new ArrayList<>();
        while(resultSet.next()){
            categories.add(resultSet.getString("category"));
        }
        while(true) {
            System.out.println("You can filter products by the following categories");
            int i = 0;
            for (i = 0; i < categories.size(); i++) {
                System.out.println(i + 1 + ". " + capitalize(categories.get(i)));
            }
            System.out.println(i + ". No filter");
            choice = scanner.next();

            if (!isDigit(choice)) {
                System.out.println("Please input only numbers");
            }
            if (choice.equals(String.valueOf(categories.size()))){ //no filter
                ShowTable.showtable(statement, "select id, name, category, brand, quantity from products");
                return;
            }else if (Integer.parseInt(choice) < categories.size()){
                String category = categories.get(Integer.parseInt(choice)-1);
                ShowTable.showtable(statement, "select id, name, brand, quantity from products where category = '" + category + "'");
                return;
            }

        }
    }

    private static void shoppingcart(Connection connection, String username) throws Exception {
        Scanner scanner = new Scanner(System.in);

        Statement statement = connection.createStatement();

        ShowTable.showtable(statement,
                "select p.id, p.name, sum(s.amount) as amount, p.price as price from shoppingcart s join products p on s.prodid = p.id where s.username = '"+username+"' group by p.id");

        while(true) {

            System.out.println("What would you like to do?");
            System.out.println("1. Purchase items");
            System.out.println("2. Manage cart");
            System.out.println("3. Back to main menu");

            String choice = scanner.nextLine();

            if(choice.equals("1")) {
                purchase(connection, username);
                break;
            }

            else if(choice.equals("2")) {
                manageCart(connection, username);
            }

            else if(choice.equals("3")) {
                return;
            }
        }
    }

    private static void purchase(Connection connection, String username) throws Exception {
        ResultSet resultSet;
        Scanner scanner = new Scanner(System.in);
        Statement statement = connection.createStatement();
        //ShowTable.showtable(statement,"select  p.name,sum(s.amount), p.price from products p, shoppingcart s where s.username = '" + username + "' and p.id = s.prodid group by s.prodid");
        String ID;
        while (true) {
            System.out.println("Which would you like to purchase?");
            ID = scanner.nextLine().trim();
            if (!isPositiveInteger(ID)){
                System.out.println("Please input correct ID");continue;}
            resultSet = statement.executeQuery("select p.id as id, sum(s.amount) as amount, p.quantity as quantity, p.name as name, count(*), p.price as price from products p, shoppingcart s where s.username = '" + username + "' and p.id = s.prodid and s.prodid ="+ID+" group by s.prodid");
            break;
        }
        int id =0;
        int exist=0;
        int quantity =0;
        int amount=0;
        String name = "";
        float price = 0;
        String payment = "";
        String review;
        if (resultSet.next()){
            id = resultSet.getInt("id");
            quantity = resultSet.getInt("quantity");
            amount = resultSet.getInt("amount");
            name = resultSet.getString("name");
            exist = resultSet.getInt("count(*)");
            price = resultSet.getFloat("price");
        }
        if (amount > quantity){
            amount = changeNumber(amount,quantity);
        }
        if (exist == 0) {
            System.out.println("Product does not exist in the shopping cart");
        }else{
            String shippingaddress = "";
            resultSet = statement.executeQuery("select shipping_address from users where username='" + username +"'");
            if (resultSet.next()){
                shippingaddress = resultSet.getString("shipping_address");
            }
            if(shippingaddress==null){
                System.out.println("Set your new shipping address, it will be used for next time");
                String newSAdress = scanner.nextLine();
                statement.executeUpdate("update users set shipping_address ='"+newSAdress+"'where username ='"+ username+"'");

            }else
            if (shippingaddress.isEmpty()){
                System.out.println("Set your new shipping address, it will be used for next time");
                String newSAdress = scanner.nextLine();
                statement.executeUpdate("update users set shipping_address ='"+newSAdress+"'where username ='"+ username+"'");

            }else{
                String choice;
                System.out.println("Your shipping address is as below");
            System.out.println(shippingaddress);
            System.out.println("Is it correct? Y/N");
            choice = scanner.next().toLowerCase();
            if (choice.equals("y")){
            }else{

                System.out.println("Set your new shipping address, it will be used for next time");
                String newSAdress = scanner.nextLine();
                statement.executeUpdate("update users set shipping_address ='"+newSAdress+"'where username ='"+ username+"'");

            }}
            System.out.println("Price: $" + price*amount);
            while(true) {
                System.out.println("Choose your payment method");
                int[] paymentMethods = {1,2,3,4};
                String[] methodss = {"Credit Card", "Debit Card", "PayPal", "RandomPay"};
                for (int i = 0; i < paymentMethods.length; i++){
                    System.out.println(paymentMethods[i] + ". " + methodss[i]);
                }
                System.out.println(paymentMethods.length +1 +". Cancel");
                String input = scanner.next();
                try {

                    int choice3 = Integer.parseInt(input);

                    // Validate in range
                    if (choice3 < 1 || choice3 > paymentMethods.length + 1) {
                        throw new Exception("Out of range");
                    }

                    // Cancel option
                    if (choice3 == paymentMethods.length + 1) {
                        System.out.println("Cancelling purchase...");
                        return;
                    }

                    // Valid input, break loop
                    System.out.println("Payment method: " + methodss[choice3 - 1]);
                    payment = methodss[choice3 -1];
                    break;
                } catch(NumberFormatException e) {
                System.out.println("Please input only numbers");
            } catch(Exception e) {
                System.out.println(e.getMessage());
            }
        }
            while (true) {
                Scanner scanner1 = new Scanner(System.in);
                System.out.println("Please input your review from 1 to 5");
                review = scanner1.nextLine();
                if (!isPositiveInteger(review)){
                    System.out.println("Not integer");
                }else
                if (Integer.parseInt(review) < 1 || Integer.parseInt(review) > 5){
                    System.out.println("Please input number in range");
                }else{
                    break;
                }

            }

            statement.executeUpdate("insert into payment_history(prod_name, price, payment_method, review) values ('" + name + "'," + price + ",'" + payment + "'," + review + ")");
            statement.executeUpdate("delete from shoppingcart where id = " + id);
            statement.executeUpdate("update products set quantity = quantity - "+amount +"where name = '" + name +"'");
            System.out.println( name + price + payment + review);
        }
    }

    public static int changeNumber(int amount, int quantity) {
        Scanner scanner = new Scanner(System.in);
        int newAmount = 0;
        boolean isValidInput = false;

        while (!isValidInput) {
            try {
                System.out.print("Enter a new number: ");
                newAmount = scanner.nextInt();
                if (newAmount < quantity) {
                    isValidInput = true;
                } else {
                    System.out.println("Number should be smaller than quantity.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.nextLine(); // Clear the input buffer
            }
        }

        scanner.close();
        return newAmount;
    }
    /*private static void purchaseAll(Connection conn, String username) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        String choice = "";
        if (!validateCartQuantities(conn,"john")){
            return;
        }
        // get total price
        String sql = "select sum(p.price) as total from shoppingcart as s, products as p where s.prodid = p.id and s.username = '"+username+"' group by p.id";
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        BigDecimal price = BigDecimal.valueOf(0);
        if (resultSet.next()){
            price = resultSet.getBigDecimal("total");
        }
        System.out.println("Total: $" + price);

        System.out.println("Your shipping address is as below");
        String shippingaddress = "";
        resultSet = statement.executeQuery("select shipping_address from users where username='" + username +"'");
        if (resultSet.next()){
            shippingaddress = resultSet.getString("shipping_address");
        }
        System.out.println(shippingaddress);
        System.out.println("Is it correct? Y/N");
        choice = scanner.next().toLowerCase();
        if (choice.equals("y")){


        }else{

                System.out.println("Set your new shipping address, it will be used for next time");
                String newSAdress = scanner.nextLine();
                statement.executeUpdate("update users set shipping_address ='"+newSAdress+"'where username ='"+ username+"'");

        }
        while(true) {
            System.out.println("Choose your payment method");
            int[] paymentMethods = {1,2,3,4};
            String[] methodss = {"Credit Card", "Debit Card", "PayPal", "RandomPay"};
            for (int i = 0; i < paymentMethods.length; i++){
                System.out.println(paymentMethods[i] + ". " + methodss[i]);
            }
            System.out.println(paymentMethods.length +1 +". Cancel");
            String input = scanner.next();
            try {
                String review;

                int choice3 = Integer.parseInt(input);

                // Validate in range
                if(choice3 < 1 || choice3 > paymentMethods.length+1) {
                    throw new Exception("Out of range");
                }

                // Cancel option
                if(choice3 == paymentMethods.length+1) {
                    System.out.println("Cancelling purchase...");
                    return;
                }

                // Valid input, break loop
                System.out.println("Payment method: " + methodss[choice3-1]);
                while(true) {
                    System.out.println("Please rate the product from 1-5");
                    review = "";
                    review = scanner.next();
                    if (isPositiveInteger(review) && Integer.parseInt(review) > 0 && Integer.parseInt(review) < 6){break;}
                    System.out.println("Please type numbers between 1 to 5");
                }

                PreparedStatement stmt = conn.prepareStatement("SELECT p.name, p.id, p.price FROM products as p, shoppingcart as p WHERE p.id = s.prodid and s.username = '" + username + "'");

                ResultSet rs = stmt.executeQuery();

                while(rs.next()) {
                    String prodID = rs.getString("p.id");
                    String name = rs.getString("p.name");
                    String Indprice = rs.getString("p.price");


                }
                statement.executeUpdate("delete from shoppingcart where username = 'john'");


                break;

            } catch(NumberFormatException e) {
                System.out.println("Please input only numbers");
            } catch(Exception e) {
                System.out.println(e.getMessage());
            }
        }

        // clear cart
        statement.executeUpdate("delete shoppingcart where username = '" + username + "'");
    }*/

    private static void manageCart(Connection conn, String user) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.println("Please select your action\n\t1. Delete item\n\t2. Update amount\n\t3.Exit");
            String action;
            action = scanner.nextLine();
            switch (action){
                case "1":
                    int id;
                    while (true) {
                        System.out.println("Please enter the ID of the item to delete: ");
                        id = scanner.nextInt();
                        if (!isPositiveInteger(String.valueOf(id))){
                            System.out.println("Please input only numbers");
                            continue;
                        }
                        break;
                    }
                    deleteCartItem(conn, id);
                    continue;
                case "2":
                    String id2;
                    String newAmount="";
                    while(true){
                        System.out.println("Enter the ID of the item to update: ");
                        id2 = scanner.nextLine();
                        if (!isPositiveInteger(id2)){
                            System.out.println("Please type in only numbers");
                            continue;
                        }
                        if(isItemExist(conn, Integer.parseInt(id2))){
                            while(true) {
                                newAmount = scanner.nextLine();
                                if (!isPositiveInteger(newAmount)){
                                    System.out.println("Please type in only numbers");
                                    continue;
                                }break;
                            }

                        }
                        break;
                    }
                    updateCartAmount(conn, Integer.parseInt(id2),Integer.parseInt(newAmount));
                case "3":
                    return;
                default:
                    System.out.println("Invalid input, please try again");
            }
            // show cart
            // get item action - add, remove, etc
            // update cart
            // show cart
        }

    }
    private static void deleteCartItem(Connection conn, int id) throws SQLException{
        Statement statement = conn.createStatement();
        int count = statement.executeUpdate("delete from shoppingcart where id= " + id);
        statement.close();
        if (count ==0){
            System.out.println("Item does not exist");
        }else{
            System.out.println("Item deleted");
        }
    }
    private static void updateCartAmount(Connection conn, int id, int newAmount) throws SQLException {
        if (!isAmountValid(conn,id,newAmount)){
            System.out.println("Invalid amount, the new amount is lagrer than the product quantity");
            return;
        }
        mergeCartByProductId(conn, id);
        Statement statement = conn.createStatement();
        statement.executeUpdate("update shoppingcart set amount = " + newAmount + "where prodid = " + id);
        statement.close();
        System.out.println("Item amount updated");
    }

    private static void addtoshoppingcart(String username, Statement statement, String name, int quantity, int ID) throws SQLException {
        int amount = 0;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please input amount");
        amount = scanner.nextInt();


        if (amount > quantity) {
            System.out.println("Not enough quantity available. Only " + quantity + " left in stock.");
            System.out.println("What would you like to do?");
            System.out.println("1. Update amount");
            System.out.println("2. Buy remaining (" + quantity + ")");
            System.out.println("3. Cancel operation");
            String choice;
            while (true) {
                choice = scanner.nextLine();
                switch (choice) {
                    case "1":
                        scanner.close();
                        addtoshoppingcart(username,statement, name, quantity, ID);
                        break;
                    case "2":
                        confirmAddToShoppingCart(statement,username, name, quantity, ID, quantity);
                        break;
                    case "3":
                        return;
                    default:
                        System.out.println("Invalid input, please try again");

                }
            }

        } else {
            confirmAddToShoppingCart(statement,username, name, amount, ID, quantity);
        }
    }
    private static void confirmAddToShoppingCart(Statement statement, String username, String name, int amount, int ID, int quantity) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        String choice;
        System.out.println("Product Information as below: ");
        System.out.println("Product name: "+ name);
        System.out.println("Quantity: "+amount);
        System.out.println("\nIs the information correct?");
        choice = scanner.nextLine();
        while(true) {
            switch (choice.toLowerCase()) {
                case "y":
                    statement.executeUpdate("insert into shoppingcart (username, prodid, amount) values ('"+ username + "'," + ID + "," + amount + ")");
                    System.out.println("executed addtoShoppingCart");
                    return;
                case "n":
                    System.out.println("What would you like to do?");
                    System.out.println("1. Update amount");
                    System.out.println("2. Cancel operation");
                    String choice2;
                    choice2 = scanner.nextLine().trim();
                    if (choice2.equals("1")){
                        addtoshoppingcart(username, statement, name, quantity, ID);
                        return;
                    } else if (choice2.equals("2")){
                        return;
                }
                default:
                    System.out.println("Invalid input, please try again");
            }
        }
    }
    public static boolean validateCartQuantities(Connection conn, String username) throws SQLException {

        String sql = "SELECT p.id, p.name, p.quantity, s.amount " +
                "FROM shoppingcart s JOIN products p " +
                "ON s.prodid = p.id " +
                "WHERE s.username = ?";

        PreparedStatement statement = conn.prepareStatement(sql);
        statement.setString(1, username);

        ResultSet resultSet = statement.executeQuery();

        while(resultSet.next()) {

            int prodId = resultSet.getInt("id");
            int stockQty = resultSet.getInt("quantity");
            int cartQty = resultSet.getInt("amount");

            if(cartQty > stockQty) {
                String itemName = resultSet.getString("name");
                System.out.println("Item " + itemName + " is out of stock, cannot perform purchase");
                return false;
            }
        }
        return true;
    }
    public static void mergeCartByProductId(Connection conn, int prodId) throws SQLException {

        Statement stmt = conn.createStatement();

        Map<String, Integer> mergedCart = new HashMap<>();

        ResultSet rs = stmt.executeQuery("SELECT username, amount FROM shoppingcart WHERE prodid=" + prodId);

        while(rs.next()) {

            String username = rs.getString("username");
            int amount = rs.getInt("amount");

            if(mergedCart.containsKey(username)) {
                int existingAmount = mergedCart.get(username);
                mergedCart.put(username, existingAmount + amount);
            }
            else {
                mergedCart.put(username, amount);
            }
        }

        rs.close();

        stmt.executeUpdate("DELETE FROM shoppingcart WHERE prodid="+prodId);

        for(Map.Entry<String, Integer> entry : mergedCart.entrySet()) {

            String username = entry.getKey();
            int amount = entry.getValue();

            stmt.executeUpdate("INSERT INTO shoppingcart VALUES('"+username+"',"+prodId+","+amount+")");
        }

        stmt.close();

    }
    public static boolean isPositiveInteger(String input){
        try{
            int number = Integer.parseInt(input);
            return number >=0;
        }catch (NumberFormatException e){
            return false;
        }
    }
    private static boolean isAmountValid(Connection connection, int id, int newAmound) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select quantity from products where id =" + id);
        if(resultSet.next()){
            int quantity = resultSet.getInt("quantity");
            return quantity >= newAmound;
        }
    return false;}
    private static boolean isItemExist(Connection conn, int itemId) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) AS count FROM shoppingcart WHERE id = ?");
        stmt.setInt(1, itemId);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            int count = rs.getInt("count");
            return count > 0;
        }

        rs.close();
        stmt.close();

        return false;
    }
    private static String capitalize(String str) {
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
    static boolean isDigit(String str) {
        return str.matches("\\d+");
    }

}