package gui;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import model.Ticket;

/**
 * ConfirmationPanel.java
 * Reads selected seats from MainFrame.pendingSelectedSeats and creates Ticket via BookingController.
 */
public class ConfirmationPanel extends JPanel implements Refreshable {

    private final MainFrame host;
    private final JTextArea summaryArea = new JTextArea(8, 40);
    private final JButton confirmBtn = new JButton("Confirm Booking");
    private final JButton backBtn = new JButton("← Back");

    public ConfirmationPanel(MainFrame host) {
        this.host = host;
        init();
    }

    private void init() {
        setLayout(new BorderLayout(12,12));
        setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        JLabel header = new JLabel("<html><h2>Booking confirmation</h2></html>");
        add(header, BorderLayout.NORTH);

        summaryArea.setEditable(false);
        add(new JScrollPane(summaryArea), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(backBtn);
        bottom.add(confirmBtn);
        add(bottom, BorderLayout.SOUTH);

        backBtn.addActionListener(e -> host.showPanel(MainFrame.PANEL_SEAT_MAP));
        confirmBtn.addActionListener(e -> handleConfirm());
    }

    @Override
    public void refresh() {
        // Build summary from AppContext and host.pendingSelectedSeats
        var user = controller.AppContext.getLoggedInUser();
        int scheduleId = controller.AppContext.getSelectedScheduleId();
        List<Integer> seats = host.getPendingSelectedSeats();

        StringBuilder sb = new StringBuilder();
        sb.append("User: ").append(user == null ? "(not logged in)" : user.getName()).append("\n");
        sb.append("Schedule ID: ").append(scheduleId).append("\n");
        sb.append("Seats: ").append(seats == null ? "None" : seats.toString()).append("\n");
        sb.append("\nPayment: Cash (demo)\n");
        summaryArea.setText(sb.toString());
    }

    private void handleConfirm() {
        var user = controller.AppContext.getLoggedInUser();
        if (user == null) {
            JOptionPane.showMessageDialog(this, "You must be logged in to book.", "Auth", JOptionPane.WARNING_MESSAGE);
            host.showPanel(MainFrame.PANEL_LOGIN);
            return;
        }

        int scheduleId = controller.AppContext.getSelectedScheduleId();
        List<Integer> seats = host.getPendingSelectedSeats();
        if (seats == null || seats.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No seats selected.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // create Ticket and enqueue booking via BookingController
        String seatsCsv = seats.stream().map(Object::toString).reduce((a,b)->a+","+b).orElse("");
        Ticket t = new Ticket(user.getId(), scheduleId, seatsCsv, "Cash", "BOOKED");

        controller.AppContext.booking().bookTicket(t);

        JOptionPane.showMessageDialog(this, "Booking confirmed and queued.", "Success", JOptionPane.INFORMATION_MESSAGE);

        // Clear pending seats
        host.setPendingSelectedSeats(null);

        // Move to My Bookings (refresh will load from booking controller's loaded list)
        controller.AppContext.booking().loadUserBookings(user.getId());
        host.showPanel(MainFrame.PANEL_MY_BOOKINGS);
    }
}
