import java.sql.Time;
import java.util.*;

class BusManager {
    private List<Bus> buses;
    private Map<String, List<Bus>> startLocationMap;
    private Map<String, List<Bus>> endLocationMap;
    private Map<Time, List<Bus>> startTimeMap;
    private int busNumberCounter;

    public BusManager() {
        buses = new ArrayList<>();
        startLocationMap = new HashMap<>();
        endLocationMap = new HashMap<>();
        startTimeMap = new HashMap<>();
        busNumberCounter = 1; // Start bus numbers from 1
    }

    // Method to add bus and update the search maps
    public void addBus(Bus bus) {
        bus.setBusNumber(busNumberCounter); // Set the bus number
        buses.add(bus);

        // Update maps for efficient searching
        startLocationMap.computeIfAbsent(bus.getStart(), k -> new ArrayList<>()).add(bus);
        endLocationMap.computeIfAbsent(bus.getEnd(), k -> new ArrayList<>()).add(bus);
        startTimeMap.computeIfAbsent(bus.getTime(), k -> new ArrayList<>()).add(bus);

        busNumberCounter++; // Increment bus number counter for next bus
        System.out.println("Bus added successfully!\n");
    }


    // Search buses by all criteria: start location, end location, and start time
    public List<Bus> searchByAllCriteria(String startLocation, String endLocation, String timeRange) {
        List<Bus> busesResult = new ArrayList<>();

        // Split the input time range (e.g., "1:00 PM-2:00 PM")
        String[] times = timeRange.split("-");

        if (times.length == 2) {
            try {
                // Parse start and end time from the time range string
                Time startTime = Time.valueOf(times[0].trim() + ":00"); // Add ":00" for seconds
                Time endTime = Time.valueOf(times[1].trim() + ":00"); // Add ":00" for seconds

                // Search for buses based on the criteria
                for (Bus bus : buses) {
                    // Convert bus time to Time object if necessary, or use bus.getTime() directly
                    Time busTime = bus.getTime();

                    // Check if the bus start time is between the given range
                    if (bus.getStart().equals(startLocation) && bus.getEnd().equals(endLocation) &&
                            busTime.after(startTime) && busTime.before(endTime)) {
                        busesResult.add(bus);
                    }
                }
            } catch (IllegalArgumentException e) {
                System.out.println(
                        "Invalid time format. Please enter the time range in the format: HH:mm AM/PM-HH:mm AM/PM.");
            }
        } else {
            System.out.println("Invalid time range format. Please use the format: HH:mm-HH:mm.");
        }

        return busesResult;
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
        System.out.println("No bus found with number: " + busNumber + "\n");
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
