import java.sql.Time;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Managers for handling in-memory operations
        CustomerManager customerManager = new CustomerManager();
        BusManager busManager = new BusManager();
        ReservationManager reservationManager = new ReservationManager();

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            // Display menu to the user
            System.out.println("\nWelcome to the Bus Reservation System");
            System.out.println("1. Add Customer");
            System.out.println("2. Bus Management");
            System.out.println("3. Reservations");
            System.out.println("4. Display Customers Sorted by Age (Bubble Sort)");
            System.out.println("5. Display Customers Sorted by Age (Quick Sort)");
            System.out.println("6. Display All Customers and Buses Information");
            System.out.println("7. Exit");
            System.out.print("\n");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();
            System.out.print("\n");

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

                    Customer customer = new Customer(name, mobile, email, city, age);
                    customerManager.addCustomer(customer);
                    break;

                case 2:
                    System.out.println("\n--- Bus Options ---");
                    System.out.println("1. Add Bus");
                    System.out.println("2. Search Bus by Number");
                    System.out.println("3. Display All Buses");
                    System.out.println("4. Search Bus");
                    System.out.println("5. Exit");
                    System.out.print("Choose an option for reservation: ");
                    int busChoice = scanner.nextInt();
                    scanner.nextLine(); // consume newline

                    switch (busChoice) {
                        case 1:
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

                            try {
                                // Parse the input string into a LocalTime object
                                LocalTime localTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));

                                // Convert LocalTime to java.sql.Time
                                Time sqlTime = Time.valueOf(localTime);

                                Bus bus = new Bus(totalSeats, startLocation, endLocation, sqlTime, fare);
                                busManager.addBus(bus);
                                break;
                                // Use the sqlTime object in your program (e.g., save to the database)
                            } catch (DateTimeParseException e) {
                                System.out.println("Invalid time format. Please use HH:mm (e.g., 14:30).");
                            }
                        case 2:
                            busManager.displayAllBuses();
                            break;

                        case 3:
                            busManager.displayAllBuses();
                            break;

                        case 4:
                            System.out.print("Enter Start Location: ");
                            startLocation = scanner.nextLine();
                            System.out.print("Enter End Location: ");
                            endLocation = scanner.nextLine();
                            System.out.print("Enter Time Range (e.g., 01:00-02:00): ");
                            String timeRange = scanner.nextLine();

                            List<Bus> busesByCriteria = busManager.searchByAllCriteria(startLocation, endLocation,
                                    timeRange);
                            if (busesByCriteria.isEmpty()) {
                                System.out.println("No buses found for the given criteria.");
                            } else {
                                busesByCriteria.forEach(System.out::println);
                            }
                            break;
                        case 5:
                            break;

                        default:
                            System.out.println("Invalid option. Try again.");
                    }
                    break;

                case 3:
                    System.out.println("\n1. Reserve Seat");
                    System.out.println("2. Cancel Last Reservation");
                    System.out.println("3. Cancel a Reservation");
                    System.out.println("4. Display Reservations");
                    System.out.println("5. Display Reservations in Queue");
                    System.out.println("6. Request a New Seat");
                    System.out.println("7. Exit");
                    System.out.print("\n");
                    System.out.print("Choose an option for reservation: ");
                    int subChoice = scanner.nextInt();
                    System.out.print("\n");

                    switch (subChoice) {
                        case 1:
                            System.out.print("Enter Customer ID: ");
                            int customerId = scanner.nextInt();
                            System.out.print("Enter Bus Number: ");
                            int busNumber = scanner.nextInt();

                            Customer customerToReserve = customerManager.getCustomerById(customerId);
                            Bus busToReserve = busManager.getBusByNumber(busNumber);

                            if (customerToReserve != null && busToReserve != null) {
                                reservationManager.reserveSeat(customerToReserve, busToReserve);
                            } else {
                                System.out.println("Invalid Customer ID or Bus Number.\n");
                            }
                            break;

                        case 2:
                            reservationManager.cancelLastReservation();
                            break;

                        case 3:
                            System.out.print("Enter Reservation ID: ");
                            int reservationId = scanner.nextInt();
                            reservationManager.cancelReservationById(reservationId);
                            break;

                        case 4:
                            reservationManager.displayReservationsInStack();
                            break;

                        case 5:
                            reservationManager.displayReservationsInQueue();
                            break;

                        case 6:
                            System.out.print("Enter Reservation ID: ");
                            int currentReservationId = scanner.nextInt();
                            System.out.print("Enter Seat Number: ");
                            int seatNumber = scanner.nextInt();
                            reservationManager.requestNewSeat(currentReservationId, seatNumber);
                            break;

                        case 7:
                            break;

                        default:
                            System.out.println("Invalid option. Try again.");
                    }
                    break;

                case 4:
                    // Bubble Sort
                    sortAndDisplayCustomers(customerManager, "bubble");
                    break;

                case 5:
                    // Quick Sort
                    sortAndDisplayCustomers(customerManager, "quick");
                    break;

                case 6:
                    System.out.println("All Buses Info:");
                    List<String> buses = busManager.getAllBusesWithIds();
                    for (String bus1 : buses) {
                        System.out.println(bus1);
                    }

                    System.out.println("\nAll Customers Info:");
                    List<String> customers = customerManager.getAllCustomersWithIds();
                    for (String customer1 : customers) {
                        System.out.println(customer1);
                    }
                    break;

                case 7:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
        scanner.close();
    }

    private static void sortAndDisplayCustomers(CustomerManager customerManager, String sortType) {
        System.out.println(
                "Sorting customers by age using " + (sortType.equals("bubble") ? "Bubble Sort" : "Quick Sort") + "...");
        long startTime = System.nanoTime();
        List<Customer> sortedCustomers = customerManager.getCustomersSortedByAge(sortType);
        long endTime = System.nanoTime();

        System.out.println("Customers sorted by age:");
        for (Customer c : sortedCustomers) {
            System.out.println(c.getName() + " - " + c.getAge());
        }

        long duration = (endTime - startTime) / 1_000_000; // Convert nanoseconds to milliseconds
        System.out.println("Time taken for " + (sortType.equals("bubble") ? "Bubble Sort" : "Quick Sort") + ": "
                + duration + " ms");
    }
}
