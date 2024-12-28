import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        try (Connection connection = DatabaseConnection.getConnection()) {

            // Check if the connection is successful
            if (connection != null) {
                System.out.println("Database connection established successfully.");

                // Create an instance of CustomerManager to handle customer operations
                CustomerManager customerManager = new CustomerManager(connection);
                BusManager busManager = new BusManager(connection);

                // Initialize the ReservationManager to manage customers and buses
                ReservationManager manager = new ReservationManager(connection);

                Scanner scanner = new Scanner(System.in);
                boolean running = true;

                while (running) {
                    // Display menu to the user
                    System.out.println("Welcome to the Bus Reservation System");
                    System.out.println("1. Add Customer");
                    System.out.println("2. Add Bus");
                    System.out.println("3. Reserve Seat");
                    System.out.println("4. Display Reservations");
                    System.out.println("5. Display Customers Sorted by Age (Bubble Sort)");
                    System.out.println("6. Display Customers Sorted by Age (Quick Sort)");
                    System.out.println("7. Exit");
                    System.out.print("Choose an option: ");

                    int choice = scanner.nextInt();
                    scanner.nextLine();

                    switch (choice) {
                        case 1:
                            // Add a customer
                            System.out.println("Enter Customer Details:");
                            System.out.print("Name: ");
                            String name = scanner.nextLine();
                            System.out.print("Mobile: ");
                            String mobile = scanner.nextLine();
                            System.out.print("Email: ");
                            String email = scanner.nextLine();
                            System.out.print("City: ");
                            String city = scanner.nextLine();
                            System.out.print("Age: ");
                            int age = scanner.nextInt();
                            scanner.nextLine();

                            // Create a new customer and add to the manage
                            Customer customer = new Customer(name, mobile, email, city, age);
                            customerManager.addCustomerToDatabase(customer);

                            break;

                        case 2:
                            // Add a bus
                            System.out.println("Enter Bus Details:");
                            System.out.print("Total Seats: ");
                            int totalSeats = scanner.nextInt();
                            scanner.nextLine();
                            System.out.print("Start Location: ");
                            String startLocation = scanner.nextLine();
                            System.out.print("End Location: ");
                            String endLocation = scanner.nextLine();
                            System.out.print("Time: ");
                            String time = scanner.nextLine();
                            System.out.print("Fare: ");
                            double fare = scanner.nextDouble();
                            scanner.nextLine();

                            // Create a new bus and add to the manager
                            Bus bus = new Bus(totalSeats, startLocation, endLocation, time, fare);
                            busManager.addBusToDatabase(bus);

                            break;

                        case 3:
                            // Reserve a seat for a customer
                            System.out.print("Enter Customer ID to Reserve Seat: ");
                            int custID = scanner.nextInt();
                            scanner.nextLine();
                            System.out.print("Enter Bus Number to Reserve Seat: ");
                            int busNum = scanner.nextInt();

                            // Find the customer and bus from the manager
                            Customer customerToReserve = customerManager.getCustomerById(custID);
                            Bus busToReserve = busManager.getBusByNumber(busNum);

                            if (customerToReserve != null && busToReserve != null) {
                                manager.reserveSeat(customerToReserve, busToReserve);
                            } else {
                                System.out.println("Invalid Customer ID or Bus Number.");
                            }
                            break;

                        case 4:
                            // Display all reservations
                            manager.displayReservations();
                            break;

                        case 5:
                            // Display customers sorted by age using Bubble Sort
                            System.out.println("Sorting customers by age using Bubble Sort...");
                            long startTimeBubble = System.nanoTime();
                            List<Customer> bubbleSortedCustomers = customerManager.getCustomersSortedByAge("bubble");
                            long endTimeBubble = System.nanoTime();
                            System.out.println("Customers sorted by age (Bubble Sort):");
                            for (Customer c : bubbleSortedCustomers) {
                                System.out.println(c.getName() + " - " + c.getAge());
                            }
                            long durationBubble = (endTimeBubble - startTimeBubble) / 1000000; // Convert nanoseconds to
                                                                                               // milliseconds
                            System.out.println("Time taken for Bubble Sort: " + durationBubble + " ms");
                            break;

                        case 6:
                            // Display customers sorted by age using Quick Sort
                            System.out.println("Sorting customers by age using Quick Sort...");
                            long startTimeQuick = System.nanoTime();
                            List<Customer> quickSortedCustomers = customerManager.getCustomersSortedByAge("quick");
                            long endTimeQuick = System.nanoTime();
                            System.out.println("Customers sorted by age (Quick Sort):");
                            for (Customer c : quickSortedCustomers) {
                                System.out.println(c.getName() + " - " + c.getAge());
                            }
                            long durationQuick = (endTimeQuick - startTimeQuick) / 1000000; // Convert nanoseconds to
                                                                                            // milliseconds
                            System.out.println("Time taken for Quick Sort: " + durationQuick + " ms");
                            break;

                        case 7:
                            // Exit the program
                            running = false;
                            break;
                        default:
                            System.out.println("Invalid option. Please try again.");
                            break;
                    }
                }

                scanner.close();

            } else {
                System.out.println("Failed to establish database connection.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
