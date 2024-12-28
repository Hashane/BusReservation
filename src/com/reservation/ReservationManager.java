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
        this.reservationStack = new Stack<>();     // Initialize the stack
        this.reservationIdCounter = 1;              // Start the counter at 1
    }

    // Method to reserve a seat for a customer on a bus
    public void reserveSeat(Customer customer, Bus bus) {
        // Check if there are available seats on the bus
        if (bus.getReservedSeats() < bus.getTotalSeats()) {
            // If there are available seats, reserve a seat immediately
            Reservation reservation = new Reservation(customer, bus, "Reserved", System.currentTimeMillis(), reservationIdCounter);

            // Add to the stack for potential rollback/undo
            reservationStack.push(reservation); // Push the reservation onto the stack
            bus.incrementReservedSeats();  // Increment the count of reserved seats

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

            System.out.println("Reservation successful for " + customer.getName() + " on Bus " + bus.getBusNumber() + "\n");
        } else {
            // If no seats are available, add the reservation to the queue
            Reservation reservation = new Reservation(customer, bus, "Waiting", System.currentTimeMillis(), reservationIdCounter);
            reservationQueue.offer(reservation);  // Add to queue
            reservationIdCounter++;  // Increment reservation ID

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
            bus.decrementReservedSeats();  // Decrement reserved seat count

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

            System.out.println("Cancelled reservation for " + lastReservation.getCustomer().getName()+"\n");

            // After a cancellation, process the next reservation in the queue if available
            if (!reservationQueue.isEmpty()) {
                // Take the first customer from the queue and process the reservation
                Reservation nextInQueue = reservationQueue.poll();
                nextInQueue.setStatus("Reserved");
                reservationStack.push(nextInQueue);  // Store in stack for potential undo
                bus.incrementReservedSeats();  // Increment reserved seat count

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

    // Method to display all reservations in the queue (FIFO order)
    public void displayReservationsInQueue() {
        if (reservationQueue.isEmpty()) {
            System.out.println("No reservations in the queue.\n");
            return;
        }

        System.out.println("Reservations in Queue (FIFO):\n");
        for (Reservation reservation : reservationQueue) {
            System.out.println("ID: " + reservation.getReservationId() + " - Customer: " + reservation.getCustomer().getName() +
                    " waiting for a seat on Bus: " + reservation.getBus().getBusNumber() + " - Status: " + reservation.getStatus());
        }
    }

    // Method to display all reservations in the stack (LIFO order)
    public void displayReservationsInStack() {
        if (reservationStack.isEmpty()) {
            System.out.println("No reservations in the stack.\n");
            return;
        }

        System.out.println("Reservations in Stack (LIFO):\n");
        for (Reservation reservation : reservationStack) {
            System.out.println("ID: " + reservation.getReservationId() + " - Customer: " + reservation.getCustomer().getName() +
                    " reserved seat on Bus: " + reservation.getBus().getBusNumber() + " - Status: " + reservation.getStatus());
        }
    }
}
