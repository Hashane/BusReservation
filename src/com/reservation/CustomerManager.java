import java.util.ArrayList;
import java.util.List;

public class CustomerManager {

    private List<Customer> customers; // Local list to store customers
    private int customerIdCounter;    // Counter to track the next available customer ID

    public CustomerManager() {
        this.customers = new ArrayList<>(); // Initialize the local list
        this.customerIdCounter = 1;         // Start the counter at 1
    }

    // Method to add a customer to the local list with an incrementing ID
    public void addCustomer(Customer customer) {
        customer.setCustomerID(customerIdCounter); // Set the ID for the new customer
        customers.add(customer);
        customerIdCounter++; // Increment the counter for the next customer
        System.out.println("Customer added successfully!\n");
    }

    // Method to retrieve a list of all customers with their IDs
    public List<String> getAllCustomersWithIds() {
        List<String> customerDetails = new ArrayList<>();
        for (Customer customer : customers) {
            customerDetails.add("ID: " + customer.getCustomerID() + ", Name: " + customer.getName());
        }
        return customerDetails;
    }

    // Method to retrieve a customer by ID
    public Customer getCustomerById(int id) {
        for (Customer customer : customers) {
            if (customer.getCustomerID() == id) {
                return customer;
            }
        }
        System.out.println("No customer found with ID: " + id + "\n");
        return null; // Return null if no customer was found
    }

    // Method to display all customers
    public void displayAllCustomers() {
        if (customers.isEmpty()) {
            System.out.println("No customers available.\n");
        } else {
            for (Customer customer : customers) {
                System.out.println(customer);
            }
        }
    }

    public List<Customer> getCustomersSortedByAge(String sortMethod) {
        // Sorting customers based on the chosen method
        if (sortMethod.equals("bubble")) {
            bubbleSort(customers); // Call Bubble Sort if chosen
        } else if (sortMethod.equals("quick")) {
            quickSort(customers, 0, customers.size() - 1); // Call Quick Sort if chosen
        } else {
            System.out.println("Invalid sort method. Returning unsorted list.\n");
        }

        return customers; // Return the sorted list of customers
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
