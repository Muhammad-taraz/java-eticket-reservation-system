package gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * SeatMapPanel.java
 *
 * Visual seat map representation using a 2D array of buttons.
 *
 * DSA:
 * - Seats represented as a 2D boolean array: seatSelected[row][col]
 * - Traversal: iterate rows/cols to compute selected seats
 * - Uses ArrayList<Integer> to collect seat numbers for booking
 *
 * Integration:
 * - TODO: Load reserved seats from ScheduleDAO and mark them disabled.
 * - On confirm: send selected seats to BookingController.enqueueBooking(...)
 */
public class SeatMapPanel extends JPanel {

    private final MainFrame host;
    private final JPanel seatsGrid = new JPanel();
    private final JButton confirmBtn = new JButton("Confirm Seats");
    private final JButton backBtn = new JButton("← Back");

    // Seat map configuration (rows x cols)
    private final int rows = 8;
    private final int cols = 5;
    private final JToggleButton[][] seatButtons = new JToggleButton[rows][cols];

    // DSA: 2D boolean array representing selection/reservation state
    private final boolean[][] seatSelected = new boolean[rows][cols];

    public SeatMapPanel(MainFrame host) {
        this.host = host;
        init();
        renderSeats();
    }

    private void init() {
        setLayout(new BorderLayout(12,12));
        setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        JLabel header = new JLabel("<html><h2>Select seats</h2><p>Click to select or deselect seats</p></html>");
        add(header, BorderLayout.NORTH);

        seatsGrid.setLayout(new GridLayout(rows, cols, 8, 8));
        add(seatsGrid, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        confirmBtn.setPreferredSize(new Dimension(150,36));
        backBtn.setPreferredSize(new Dimension(110,36));
        bottom.add(backBtn);
        bottom.add(confirmBtn);
        add(bottom, BorderLayout.SOUTH);

        backBtn.addActionListener(e -> host.showPanel(MainFrame.PANEL_SCHEDULE));
        confirmBtn.addActionListener(e -> handleConfirm());
    }

    private void renderSeats() {
        seatsGrid.removeAll();

        // Example: mark some seats as reserved (demo). In real app, query schedule seat_map.
        boolean[][] reserved = new boolean[rows][cols];
        reserved[0][1] = true; // a reserved seat example
        reserved[3][2] = true;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int seatNum = r * cols + c + 1;
                JToggleButton btn = new JToggleButton(String.valueOf(seatNum));
                btn.setPreferredSize(new Dimension(60, 40));
                btn.setFocusPainted(false);

                if (reserved[r][c]) {
                    btn.setEnabled(false);
                    btn.setText(seatNum + " (X)");
                } else {
                    final int rr = r, cc = c;
                    btn.addActionListener(a -> {
                        seatSelected[rr][cc] = btn.isSelected();
                    });
                }

                seatButtons[r][c] = btn;
                seatsGrid.add(btn);
            }
        }

        seatsGrid.revalidate();
        seatsGrid.repaint();
    }

    private void handleConfirm() {
        // Traverse 2D boolean array to collect selected seats (DSA traversal)
        List<Integer> selectedSeatNumbers = new ArrayList<>();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (seatSelected[r][c]) {
                    int seatNum = r * cols + c + 1;
                    selectedSeatNumbers.add(seatNum);
                }
            }
        }

        if (selectedSeatNumbers.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select at least one seat.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // TODO: Pass selectedSeatNumbers to BookingController (e.g., enqueue booking)
        // BookingController.getInstance().selectSeats(scheduleId, selectedSeatNumbers);

        // Pass info to confirmation panel (optionally via shared controller/state)
        JOptionPane.showMessageDialog(this, "Seats selected: " + selectedSeatNumbers, "Seats", JOptionPane.INFORMATION_MESSAGE);
        host.showPanel(MainFrame.PANEL_CONFIRMATION);
    }
}
