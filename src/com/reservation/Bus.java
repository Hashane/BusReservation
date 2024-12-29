import java.sql.Time;

public class Bus {
    private int busNumber;
    private int totalSeats;
    private int reservedSeats;  // Track the number of reserved seats
    private String start;
    private String end;
    private Time time;
    private double fare;

    // Constructor for a bus without a bus number (if needed)
    public Bus(int totalSeats, String start, String end, Time time, double fare) {
        this.totalSeats = totalSeats;
        this.start = start;
        this.end = end;
        this.time = time;
        this.fare = fare;
        this.reservedSeats = 0;  // Initially, no seats are reserved
    }

    // Constructor with bus number
    public Bus(int busNumber, int totalSeats, String start, String end, Time time, double fare) {
        this.busNumber = busNumber;
        this.totalSeats = totalSeats;
        this.start = start;
        this.end = end;
        this.time = time;
        this.fare = fare;
        this.reservedSeats = 0;  // Initially, no seats are reserved
    }

    // Getter and setter methods
    public void setBusNumber(int busNumber) {
        this.busNumber = busNumber;
    }

    public int getBusNumber() {
        return busNumber;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public Time getTime() {
        return time;
    }

    public double getFare() {
        return fare;
    }

    public int getReservedSeats() {
        return reservedSeats;  // Return the number of reserved seats
    }

    // Method to increment reserved seats when a reservation is made
    public void incrementReservedSeats() {
        if (reservedSeats < totalSeats) {
            reservedSeats++;
        } else {
            System.out.println("No more available seats on this bus.");
        }
    }

    // Method to decrement reserved seats when a reservation is canceled
    public void decrementReservedSeats() {
        if (reservedSeats > 0) {
            reservedSeats--;
        } else {
            System.out.println("No reserved seats to cancel.");
        }
    }

    @Override
    public String toString() {
        return "Bus{" +
                "busNumber=" + busNumber +
                ", totalSeats=" + totalSeats +
                ", reservedSeats=" + reservedSeats +
                ", start='" + start + '\'' +
                ", end='" + end + '\'' +
                ", time='" + time + '\'' +
                ", fare=" + fare +
                '}';
    }

    // Method to display bus details
    public void getDetails() {
        System.out.println("Bus Details: " + busNumber + " from " + start + " to " + end + " at " + time);
    }

    // Method to update bus details
    public void updateDetails(int totalSeats, String start, String end, Time time, double fare) {
        this.totalSeats = totalSeats;
        this.start = start;
        this.end = end;
        this.time = time;
        this.fare = fare;
    }
}
