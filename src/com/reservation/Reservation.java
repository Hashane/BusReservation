public class Reservation {
    private static int reservationCounter = 1;
    private int reservationID;
    private int customerID;
    private String busNumber;
    private String status;
    private String timestamp;

    public Reservation(int customerID, String busNumber) {
        this.reservationID = reservationCounter++;
        this.customerID = customerID;
        this.busNumber = busNumber;
        this.status = "Reserved";
        this.timestamp = java.time.LocalDateTime.now().toString(); // Timestamp of reservation
    }

    public int getReservationID() {
        return reservationID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public String getStatus() {
        return status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "reservationID=" + reservationID +
                ", customerID=" + customerID +
                ", busNumber='" + busNumber + '\'' +
                ", status='" + status + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }

    public void createReservation() {
        System.out.println("Reservation Created: " + reservationID + ", Bus " + busNumber + ", Customer " + customerID);
    }

    public void cancelSeat() {
        this.status = "Cancelled";
        System.out.println("Reservation " + reservationID + " has been cancelled.");
    }

    public void display() {
        System.out.println("Reservation Details: ID " + reservationID + ", Bus " + busNumber + ", Status " + status);
    }
}
