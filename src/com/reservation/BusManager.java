import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.sql.*;

public class BusManager {

    private Connection connection; // Database connection

    public BusManager(Connection connection) {
        this.connection = connection; // Initialize the connection
    }

    // Method to add a bus to the database
    public void addBusToDatabase(Bus bus) {
        String query = "INSERT INTO buses (totalSeats, start, end, time, fare) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, bus.getTotalSeats());
            stmt.setString(2, bus.getStart());
            stmt.setString(3, bus.getEnd());
            stmt.setString(4, bus.getTime());
            stmt.setDouble(5, bus.getFare());

            stmt.executeUpdate();
            System.out.println("Bus added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to retrieve a bus by ID from the database
    public Bus getBusByNumber(int id) {
        String query = "SELECT * FROM buses WHERE busNumber = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id); // Set the busID parameter

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Retrieve bus details from the result set
                    int busNumber = rs.getInt("busNumber");
                    int seats = rs.getInt("totalSeats");
                    String start = rs.getString("start");
                    String end = rs.getString("end");
                    String time = rs.getString("time");
                    Double fare = rs.getDouble("fare");

                    return new Bus(busNumber,seats, start, end, time, fare);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Return null if no bus was found
    }
}
