import java.util.LinkedList;
import java.util.Properties;
import java.util.Queue;
import java.util.Stack;
import javax.mail.*;
import javax.mail.internet.*;

public class ReservationManager {

    private Queue<Reservation> reservationQueue; // Queue to handle reservations waiting for a seat
    private Stack<Reservation> reservationStack; // Stack to handle reservations for undo or rollback

    private int reservationIdCounter; // Counter to track the next available reservation ID

    public ReservationManager() {
        this.reservationQueue = new LinkedList<>(); // Initialize the queue
        this.reservationStack = new Stack<>(); // Initialize the stack
        this.reservationIdCounter = 1; // Start the counter at 1
    }

    // Method to reserve a seat for a customer on a bus
    public void reserveSeat(Customer customer, Bus bus) {
        // Check if there are available seats on the bus
        if (bus.getReservedSeats() < bus.getTotalSeats()) {
            // If there are available seats, reserve a seat immediately
            Reservation reservation = new Reservation(customer, bus, "Reserved", System.currentTimeMillis(),
                    reservationIdCounter);

            // Add to the stack for potential rollback/undo
            reservationStack.push(reservation); // Push the reservation onto the stack
            bus.incrementReservedSeats(); // Increment the count of reserved seats

            // Increment the reservation ID for the next reservation
            reservationIdCounter++;

            // Send an email to the customer

            sendEmailToCustomer(customer.getEmail(),
                    "Reservation Confirmation",
                    "Dear Customer,\n\n" +
                            "We are pleased to inform you that your reservation has been confirmed.\n\n" +
                            "Reservation Details:\n" +
                            "----------------------\n" +
                            "Bus Number: " + bus.getBusNumber() + "\n" +
                            "Route: " + bus.getStart() + " to " + bus.getEnd() + "\n" +
                            "Departure Time: " + bus.getTime() + "\n\n" +
                            "Thank you for booking with us!\n\n" +
                            "We wish you a pleasant journey!\n\n" +
                            "Best regards,\n" +
                            "The Reservation Team");

            System.out.println(
                    "Reservation successful for " + customer.getName() + " on Bus " + bus.getBusNumber() + "\n");
        } else {
            // If no seats are available, add the reservation to the queue
            Reservation reservation = new Reservation(customer, bus, "Waiting", System.currentTimeMillis(),
                    reservationIdCounter);
            reservationQueue.offer(reservation); // Add to queue
            reservationIdCounter++; // Increment reservation ID

            System.out.println("Bus is fully booked. Customer added to the waitlist.\n");
        }
    }

    // Method to send email to the customer
    private void sendEmailToCustomer(String toEmail, String subject, String body) {
        String fromEmail = "bus-reservations@transport.com";
        String host = "localhost";

        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "1025");
        properties.put("mail.smtp.auth", "false");
        properties.put("mail.smtp.starttls.enable", "false");

        Session session = Session.getInstance(properties);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            System.out.println("Email sent successfully to " + toEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Error sending email: " + e.getMessage());
        }
    }

    // Method to cancel the last reservation (using Stack)
    public void cancelLastReservation() {
        if (!reservationStack.isEmpty()) {
            Reservation lastReservation = reservationStack.pop(); // Pop the reservation from the stack
            lastReservation.setStatus("Cancelled");

            Bus bus = lastReservation.getBus();
            bus.decrementReservedSeats(); // Decrement reserved seat count

            sendEmailToCustomer(lastReservation.getCustomer().getEmail(),
                    "Reservation Cancellation",
                    "Dear " + lastReservation.getCustomer().getName() + ",\n\n" +
                            "We regret to inform you that your reservation for Bus " + bus.getBusNumber() +
                            " has been cancelled.\n\n" +
                            "Reservation Details:\n" +
                            "----------------------\n" +
                            "Bus Number: " + bus.getBusNumber() + "\n" +
                            "Route: " + bus.getStart() + " to " + bus.getEnd() + "\n" +
                            "Departure Time: " + bus.getTime() + "\n\n" +
                            "We apologize for any inconvenience this may cause.\n\n" +
                            "Best regards,\n" +
                            "The Reservation Team\n");

            System.out.println("Cancelled reservation for " + lastReservation.getCustomer().getName() + "\n");

            // After a cancellation, process the next reservation in the queue if available
            if (!reservationQueue.isEmpty()) {
                // Take the first customer from the queue and process the reservation
                Reservation nextInQueue = reservationQueue.poll();
                nextInQueue.setStatus("Reserved");
                reservationStack.push(nextInQueue); // Store in stack for potential undo
                bus.incrementReservedSeats(); // Increment reserved seat count

                sendEmailToCustomer(nextInQueue.getCustomer().getEmail(),
                        "Reservation Confirmation",
                        "Dear " + nextInQueue.getCustomer().getName() + ",\n\n" +
                                "We are pleased to inform you that your reservation for Bus " + bus.getBusNumber() +
                                " has been confirmed.\n\n" +
                                "Reservation Details:\n" +
                                "----------------------\n" +
                                "Bus Number: " + bus.getBusNumber() + "\n" +
                                "Route: " + bus.getStart() + " to " + bus.getEnd() + "\n" +
                                "Departure Time: " + bus.getTime() + "\n\n" +
                                "Thank you for booking with us!\n\n" +
                                "We wish you a pleasant journey!\n\n" +
                                "Best regards,\n" +
                                "The Reservation Team\n");

                System.out.println("Processed reservation for " + nextInQueue.getCustomer().getName() +
                        " from the waitlist.\n");

            }
        } else {
            System.out.println("No reservations to cancel.\n");
        }
    }

    // Method to cancel a reservation by ID
    public void cancelReservationById(int reservationId) {
        // Temporary stack to hold reservations while searching for the target
        Stack<Reservation> tempStack = new Stack<>();
        Reservation reservationToCancel = null;

        // Search for the reservation in the stack
        while (!reservationStack.isEmpty()) {
            Reservation currentReservation = reservationStack.pop();
            if (currentReservation.getReservationId() == reservationId) {
                reservationToCancel = currentReservation;
                break;
            } else {
                tempStack.push(currentReservation); // Temporarily store other reservations
            }
        }

        // Restore the stack
        while (!tempStack.isEmpty()) {
            reservationStack.push(tempStack.pop());
        }

        if (reservationToCancel == null) {
            System.out.println("No reservation found with ID: " + reservationId);
            return;
        }

        // Cancel the reservation
        reservationToCancel.setStatus("Cancelled");

        // Update the associated bus
        Bus bus = reservationToCancel.getBus();
        bus.decrementReservedSeats(); // Decrement the reserved seat count

        // Notify the customer about the cancellation
        sendEmailToCustomer(reservationToCancel.getCustomer().getEmail(),
                "Reservation Cancellation",
                "Dear " + reservationToCancel.getCustomer().getName() + ",\n\n" +
                        "We regret to inform you that your reservation for Bus " + bus.getBusNumber() +
                        " has been cancelled.\n\n" +
                        "Reservation Details:\n" +
                        "----------------------\n" +
                        "Bus Number: " + bus.getBusNumber() + "\n" +
                        "Route: " + bus.getStart() + " to " + bus.getEnd() + "\n" +
                        "Departure Time: " + bus.getTime() + "\n\n" +
                        "We apologize for any inconvenience this may cause.\n\n" +
                        "Best regards,\n" +
                        "The Reservation Team\n");

        System.out.println("Cancelled reservation for " + reservationToCancel.getCustomer().getName());

        // Handle the next reservation in the queue, if any
        if (!reservationQueue.isEmpty()) {
            // Process the next customer in the queue
            Reservation nextInQueue = reservationQueue.poll();
            nextInQueue.setStatus("Reserved");
            reservationStack.push(nextInQueue); // Add to stack for undo functionality
            bus.incrementReservedSeats(); // Increment reserved seat count

            // Notify the customer about the new reservation
            sendEmailToCustomer(nextInQueue.getCustomer().getEmail(),
                    "Reservation Confirmation",
                    "Dear " + nextInQueue.getCustomer().getName() + ",\n\n" +
                            "We are pleased to inform you that your reservation for Bus " + bus.getBusNumber() +
                            " has been confirmed.\n\n" +
                            "Reservation Details:\n" +
                            "----------------------\n" +
                            "Bus Number: " + bus.getBusNumber() + "\n" +
                            "Route: " + bus.getStart() + " to " + bus.getEnd() + "\n" +
                            "Departure Time: " + bus.getTime() + "\n\n" +
                            "Thank you for booking with us!\n\n" +
                            "We wish you a pleasant journey!\n\n" +
                            "Best regards,\n" +
                            "The Reservation Team\n");

            System.out.println("Processed reservation for " + nextInQueue.getCustomer().getName() +
                    " from the waitlist.\n");
        }
    }

    // Method to display all reservations in the queue (FIFO order)
    public void displayReservationsInQueue() {
        if (reservationQueue.isEmpty()) {
            System.out.println("No reservations in the queue.\n");
            return;
        }

        System.out.println("Reservations in Queue (FIFO):\n");
        for (Reservation reservation : reservationQueue) {
            System.out.println(
                    "ID: " + reservation.getReservationId() + " - Customer: " + reservation.getCustomer().getName() +
                            " waiting for a seat on Bus: " + reservation.getBus().getBusNumber() + " - Status: "
                            + reservation.getStatus());
        }
    }

    private Queue<Reservation> seatRequestQueue = new LinkedList<>();

    // Method to request a new seat
    public void requestNewSeat(int reservationId, int requestedSeat) {
        Reservation reservation = null;

        // Search for the reservation by ID
        for (Reservation res : reservationStack) {
            if (res.getReservationId() == reservationId) {
                reservation = res;
                break;
            }
        }

        if (reservation == null) {
            System.out.println("No reservation found with ID: " + reservationId);
            return;
        }

        // Update reservation status and add to the queue
        reservation.setStatus("Waiting");
        reservation.setRequestedSeat(requestedSeat);
        seatRequestQueue.add(reservation);

        // Decrement reserved seats on the bus
        reservation.getBus().decrementReservedSeats();

        System.out.println("Seat change request added for " + reservation.getCustomer().getName() +
                ". They are now in the waitlist.");
    }

    // Method to display all reservations in the stack (LIFO order)
    public void displayReservationsInStack() {
        if (reservationStack.isEmpty()) {
            System.out.println("No reservations in the stack.\n");
            return;
        }

        System.out.println("Reservations in Stack (LIFO):\n");
        for (Reservation reservation : reservationStack) {
            System.out.println(
                    "ID: " + reservation.getReservationId() + " - Customer: " + reservation.getCustomer().getName() +
                            " reserved seat on Bus: " + reservation.getBus().getBusNumber() + " - Status: "
                            + reservation.getStatus());
        }
    }
}
