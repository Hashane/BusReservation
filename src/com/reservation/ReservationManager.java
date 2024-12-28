import java.sql.*;
import java.util.ArrayList;

public class ReservationManager {

    private Connection connection; // Database connection

    // Constructor to initialize the connection
    public ReservationManager(Connection connection) {
        this.connection = connection;
    }

    // Method to reserve a seat for a customer on a bus
    public void reserveSeat(Customer customer, Bus bus) {
        
        String query = "INSERT INTO reservations (customerID, busNumber,status, timestamp) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, customer.getCustomerID());
            stmt.setInt(2, bus.getBusNumber());
            stmt.setString(3, "Reserved");
            stmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));

            stmt.executeUpdate();
            System.out.println("Reservation successful!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to retrieve all reservations from the database
    public void displayReservations() {
        String query = "SELECT * FROM reservations";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (!rs.next()) {
                System.out.println("No reservations made yet.");
                return;
            }

            // Loop through all the reservations and display them
            do {
                int customerID = rs.getInt("customerID");
                String busNumber = rs.getString("busNumber");

                // Retrieve the customer details by ID
                CustomerManager customerManager = new CustomerManager(connection);
                Customer customer = customerManager.getCustomerById(customerID);
                if (customer != null) {
                    System.out.println("Customer: " + customer.getName() + " reserved seat on Bus: " + busNumber);
                }
            } while (rs.next());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
