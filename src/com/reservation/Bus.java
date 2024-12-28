public class Bus {
    private int busNumber;
    private int totalSeats;
    private String start;
    private String end;
    private String time;
    private double fare;

    public Bus(int totalSeats, String start, String end, String time, double fare) {
        this.totalSeats = totalSeats;
        this.start = start;
        this.end = end;
        this.time = time;
        this.fare = fare;
    }

    public Bus(int busNumber, int totalSeats, String start, String end, String time, double fare) {
        this.busNumber = busNumber;
        this.totalSeats = totalSeats;
        this.start = start;
        this.end = end;
        this.time = time;
        this.fare = fare;
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

    public String getTime() {
        return time;
    }

    public double getFare() {
        return fare;
    }

    @Override
    public String toString() {
        return "Bus{" +
                "busNumber='" + busNumber + '\'' +
                ", totalSeats=" + totalSeats +
                ", start='" + start + '\'' +
                ", end='" + end + '\'' +
                ", time='" + time + '\'' +
                ", fare=" + fare +
                '}';
    }

    public void getDetails() {
        System.out.println("Bus Details: " + busNumber + " from " + start + " to " + end + " at " + time);
    }

    public void updateDetails(int totalSeats, String start, String end, String time, double fare) {
        this.totalSeats = totalSeats;
        this.start = start;
        this.end = end;
        this.time = time;
        this.fare = fare;
    }

}
