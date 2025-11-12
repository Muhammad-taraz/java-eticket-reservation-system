package gui;

import java.awt.*;
import java.util.LinkedList;
import javax.swing.*;
import model.Ticket;

/**
 * MyBookingsPanel.java
 * Loads from BookingController.userBookings.
 */
public class MyBookingsPanel extends JPanel implements Refreshable {

    private final MainFrame host;
    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JList<String> bookingsList = new JList<>(listModel);
    private final JButton cancelBtn = new JButton("Cancel Booking");
    private final JButton refreshBtn = new JButton("Refresh");
    private final JButton backBtn = new JButton("← Back");

    public MyBookingsPanel(MainFrame host) {
        this.host = host;
        init();
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
        refreshBtn.addActionListener(e -> refresh());
        cancelBtn.addActionListener(e -> handleCancel());
    }

    @Override
    public void refresh() {
        listModel.clear();
        var user = controller.AppContext.getLoggedInUser();
        if (user == null) {
            listModel.addElement("Not logged in.");
            return;
        }
        // Ensure booking controller has loaded current user's bookings
        controller.AppContext.booking().loadUserBookings(user.getId());
        LinkedList<Ticket> bookings = controller.AppContext.booking().getUserBookings();
        if (bookings.isEmpty()) {
            listModel.addElement("No bookings found.");
            return;
        }
        for (Ticket t : bookings) {
            listModel.addElement(t.toString());
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
            // Parse ticket id from toString; Ticket.toString prints "Ticket{id=..., ..."
            int ticketId = parseTicketId(selected);
            if (ticketId > 0) {
                boolean ok = controller.AppContext.booking().cancelTicket(ticketId);
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Booking cancelled.", "Cancelled", JOptionPane.INFORMATION_MESSAGE);
                    refresh();
                } else {
                    JOptionPane.showMessageDialog(this, "Cancellation failed.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Could not determine ticket id.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private int parseTicketId(String text) {
        if (text == null) return -1;
        try {
            int start = text.indexOf("id=");
            if (start < 0) return -1;
            start += 3;
            int end = text.indexOf(",", start);
            if (end < 0) end = text.indexOf("}", start);
            String idStr = text.substring(start, end).trim();
            return Integer.parseInt(idStr);
        } catch (Exception ex) {
            return -1;
        }
    }
}
