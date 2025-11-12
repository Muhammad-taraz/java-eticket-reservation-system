package controller;

import dao.TicketDAO;
import model.Ticket;
import java.util.*;

public class BookingController {
    private TicketDAO ticketDAO = new TicketDAO();

    // LinkedList for user's bookings (dynamic)
    private LinkedList<Ticket> userBookings = new LinkedList<>();

    // Queue for booking/cancellation requests
    private Queue<Ticket> bookingQueue = new LinkedList<>();

    // Load user's bookings initially
    public void loadUserBookings(int userId) {
        userBookings.clear();
        userBookings.addAll(ticketDAO.getTicketsByUser(userId));
    }

    // === Insert / Book Ticket ===
    public void bookTicket(Ticket ticket) {
        bookingQueue.add(ticket); // enqueue request
        processBookingQueue();
    }

    // Process booking requests in FIFO order
    private void processBookingQueue() {
        while (!bookingQueue.isEmpty()) {
            Ticket t = bookingQueue.poll();
            boolean success = ticketDAO.createTicket(t);
            if (success) {
                userBookings.add(t); // insertion (LinkedList addLast)
                System.out.println("✅ Ticket booked: " + t);
            } else {
                System.out.println("❌ Booking failed: " + t);
            }
        }
    }

    // === Cancel Ticket ===
    public boolean cancelTicket(int ticketId) {
        // find & remove from linked list
        Iterator<Ticket> itr = userBookings.iterator();
        while (itr.hasNext()) {
            Ticket t = itr.next();
            if (t.getId() == ticketId) {
                itr.remove(); // deletion
                ticketDAO.cancelTicket(ticketId);
                System.out.println("🚫 Ticket cancelled: " + t);
                return true;
            }
        }
        return false;
    }

    // === Traverse bookings (for display) ===
    public void displayBookings() {
        System.out.println("=== Your Current Bookings ===");
        for (Ticket t : userBookings) {
            System.out.println(t);
        }
    }

    public LinkedList<Ticket> getUserBookings() {
        return userBookings;
    }
}