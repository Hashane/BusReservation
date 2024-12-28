import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

      public List<Customer> getCustomersSortedByAge(String sortMethod) {
        List<Customer> customers = new ArrayList<>();
        String query = "SELECT * FROM customers";

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int id = rs.getInt("customerID");
                String name = rs.getString("name");
                String mobile = rs.getString("mobile");
                String email = rs.getString("email");
                String city = rs.getString("city");
                int age = rs.getInt("age");

                customers.add(new Customer(id, name, mobile, email, city, age));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Sorting customers based on the chosen method
        if (sortMethod.equals("bubble")) {
            bubbleSort(customers);
        } else if (sortMethod.equals("quick")) {
            quickSort(customers, 0, customers.size() - 1);
        }

        return customers;
    }

    // Bubble Sort
    private void bubbleSort(List<Customer> customers) {
        for (int i = 0; i < customers.size() - 1; i++) {
            for (int j = 0; j < customers.size() - 1 - i; j++) {
                if (customers.get(j).getAge() > customers.get(j + 1).getAge()) {
                    // Swap elements
                    Customer temp = customers.get(j);
                    customers.set(j, customers.get(j + 1));
                    customers.set(j + 1, temp);
                }
            }
        }
    }

    // Quick Sort
    private void quickSort(List<Customer> customers, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(customers, low, high);
            quickSort(customers, low, pivotIndex - 1);
            quickSort(customers, pivotIndex + 1, high);
        }
    }

    private int partition(List<Customer> customers, int low, int high) {
        int pivot = customers.get(high).getAge();
        int i = (low - 1);

        for (int j = low; j < high; j++) {
            if (customers.get(j).getAge() <= pivot) {
                i++;
                // Swap elements
                Customer temp = customers.get(i);
                customers.set(i, customers.get(j));
                customers.set(j, temp);
            }
        }

        // Swap the pivot element
        Customer temp = customers.get(i + 1);
        customers.set(i + 1, customers.get(high));
        customers.set(high, temp);

        return i + 1;
    }
}
