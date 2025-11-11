package gui;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

/**
 * MyBookingsPanel.java
 *
 * Shows user's bookings in a list. Demonstrates LinkedList usage.
 *
 * DSA:
 * - Uses LinkedList<String> to store booking entries to allow efficient insertion and deletion.
 *
 * Integration:
 * - TODO: Populate from TicketDAO or BookingController for real data.
 */
public class MyBookingsPanel extends JPanel {

    private final MainFrame host;
    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JList<String> bookingsList = new JList<>(listModel);
    private final JButton cancelBtn = new JButton("Cancel Booking");
    private final JButton refreshBtn = new JButton("Refresh");
    private final JButton backBtn = new JButton("← Back");

    // Demo dynamic linked list storing booking ids/strings
    private final LinkedList<String> bookingsLinkedList = new LinkedList<>();

    public MyBookingsPanel(MainFrame host) {
        this.host = host;
        init();
        loadDemoBookings();
        refreshListFromLinkedList();
    }

    private void init() {
        setLayout(new BorderLayout(12,12));
        setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        JLabel header = new JLabel("<html><h2>My Bookings</h2></html>");
        add(header, BorderLayout.NORTH);

        bookingsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(bookingsList), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(backBtn);
        bottom.add(refreshBtn);
        bottom.add(cancelBtn);
        add(bottom, BorderLayout.SOUTH);

        backBtn.addActionListener(e -> host.showPanel(MainFrame.PANEL_HOME));
        refreshBtn.addActionListener(e -> {
            // TODO: reload bookings from controller and re-populate linked list
            refreshListFromLinkedList();
        });
        cancelBtn.addActionListener(e -> handleCancel());
    }

    private void loadDemoBookings() {
        bookingsLinkedList.clear();
        // Demo data; in real app, fetch via TicketDAO or BookingController for the logged in user
        bookingsLinkedList.add("Booking#1001 - Karachi→Lahore - 2 seats - BOOKED");
        bookingsLinkedList.add("Booking#1002 - Karachi→Islamabad - 1 seat - BOOKED");
    }

    private void refreshListFromLinkedList() {
        listModel.clear();
        // Traversal of LinkedList to populate UI
        for (String b : bookingsLinkedList) {
            listModel.addElement(b);
        }
    }

    private void handleCancel() {
        int idx = bookingsList.getSelectedIndex();
        if (idx < 0) {
            JOptionPane.showMessageDialog(this, "Select a booking to cancel.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String selected = bookingsList.getSelectedValue();

        int confirm = JOptionPane.showConfirmDialog(this, "Cancel booking?\n" + selected, "Confirm Cancel", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            // DSA deletion from LinkedList (efficient removal)
            bookingsLinkedList.remove(idx);

            // TODO: Call BookingController.cancel(bookingId) or TicketDAO.updateStatus(...)
            // For now reflect change immediately in UI:
            refreshListFromLinkedList();
            JOptionPane.showMessageDialog(this, "Booking cancelled (demo).", "Cancelled", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
