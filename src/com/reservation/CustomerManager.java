import java.sql.*;

public class CustomerManager {

    private Connection connection; // Database connection

    public CustomerManager(Connection connection) {
        this.connection = connection; // Initialize the connection
    }

    // Method to add a customer to the database
    public void addCustomerToDatabase(Customer customer) {
        String query = "INSERT INTO customers (name, mobile, email, city, age) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getMobile());
            stmt.setString(3, customer.getEmail());
            stmt.setString(4, customer.getCity());
            stmt.setInt(5, customer.getAge());

            stmt.executeUpdate();
            System.out.println("Customer added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to retrieve a customer by ID from the database
    public Customer getCustomerById(int id) {
        String query = "SELECT * FROM customers WHERE customerID = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id); // Set the customerID parameter

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Retrieve customer details from the result set
                    int customerID = rs.getInt("customerID");
                    String name = rs.getString("name");
                    String mobile = rs.getString("mobile");
                    String email = rs.getString("email");
                    String city = rs.getString("city");
                    int age = rs.getInt("age");

                    return new Customer(customerID,name, mobile, email, city, age);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Return null if no customer was found
    }
}
