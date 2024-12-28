import java.util.ArrayList;
import java.util.List;

public class BusManager {

    private List<Bus> buses; // Local list to store buses
    private int busNumberCounter; // Counter to track the next available bus number

    public BusManager() {
        this.buses = new ArrayList<>();  // Initialize the local list
        this.busNumberCounter = 1;       // Start the counter at 1
    }

    // Method to add a bus to the local list with an incrementing bus number
    public void addBus(Bus bus) {
        bus.setBusNumber(busNumberCounter); // Set the bus number
        buses.add(bus);
        busNumberCounter++; // Increment the counter for the next bus
        System.out.println("Bus added successfully!\n");
    }

    // Method to retrieve a list of all buses with their IDs
    public List<String> getAllBusesWithIds() {
        List<String> busDetails = new ArrayList<>();
        for (Bus bus : buses) {
            busDetails.add("Bus Number: " + bus.getBusNumber() + ", Route: " + bus.getStart() + " -> " + bus.getEnd() +
                           ", Departure Time: " + bus.getTime() + ", Total Seats: " + bus.getTotalSeats());
        }
        return busDetails;
    }
    
    // Method to retrieve a bus by bus number
    public Bus getBusByNumber(int busNumber) {
        for (Bus bus : buses) {
            if (bus.getBusNumber() == busNumber) {
                return bus;
            }
        }
        System.out.println("No bus found with number: " + busNumber+"\n");
        return null; // Return null if no bus was found
    }

    // Method to display all buses
    public void displayAllBuses() {
        if (buses.isEmpty()) {
            System.out.println("No buses available.\n");
        } else {
            for (Bus bus : buses) {
                System.out.println(bus);
            }
        }
    }
}
