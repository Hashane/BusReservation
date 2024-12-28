public class Reservation {
    private int reservationId;   // Unique reservation ID
    private Customer customer;   // Customer for the reservation
    private Bus bus;             // Bus for the reservation
    private String status;       // Status of the reservation (e.g., Reserved, Cancelled)
    private long timestamp;      // Timestamp of when the reservation was made

    public Reservation(Customer customer, Bus bus, String status, long timestamp, int reservationId) {
        this.customer = customer;
        this.bus = bus;
        this.status = status;
        this.timestamp = timestamp;
        this.reservationId = reservationId; // Set the reservation ID
    }

    // Getter and setter methods
    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId + ", Customer: " + customer.getName() + ", Bus: " + bus.getBusNumber() + ", Status: " + status;
    }
}
