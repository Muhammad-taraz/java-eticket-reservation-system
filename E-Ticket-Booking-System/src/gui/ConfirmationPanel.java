package gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 * ConfirmationPanel.java
 *
 * Shows booking summary and confirms booking.
 *
 * DSA:
 * - Uses a Queue (ArrayDeque) to simulate booking request queue (enqueue on confirm).
 *
 * Integration:
 * - TODO: Replace localQueue with BookingController.enqueue(...)
 */
public class ConfirmationPanel extends JPanel {

    private final MainFrame host;
    private final JTextArea summaryArea = new JTextArea(8, 40);
    private final JButton confirmBtn = new JButton("Confirm Booking");
    private final JButton backBtn = new JButton("← Back");

    // Demo booking request queue (DSA queue)
    private static final Queue<String> localQueue = new ArrayDeque<>();

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
        summaryArea.setText("Booking details will appear here.\n(Seats, schedule, price, user info)");
        add(new JScrollPane(summaryArea), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(backBtn);
        bottom.add(confirmBtn);
        add(bottom, BorderLayout.SOUTH);

        backBtn.addActionListener(e -> host.showPanel(MainFrame.PANEL_SEAT_MAP));
        confirmBtn.addActionListener(e -> handleConfirm());
    }

    private void handleConfirm() {
        // In a real app, construct a BookingRequest object and enqueue it in BookingController.
        // For demo: push a textual record into our local queue to demonstrate queue behavior (FIFO).
        String bookingSummary = "DemoBooking@" + System.currentTimeMillis();
        localQueue.add(bookingSummary); // enqueue

        // TODO: replace with: BookingController.getInstance().enqueue(bookingRequest);

        JOptionPane.showMessageDialog(this, "Booking confirmed and queued (demo).", "Success", JOptionPane.INFORMATION_MESSAGE);
        host.showPanel(MainFrame.PANEL_MY_BOOKINGS);
    }

    /**
     * Demo access to queue contents (DSA traversal)
     */
    public static String dequeueDemo() {
        return localQueue.poll();
    }
}
