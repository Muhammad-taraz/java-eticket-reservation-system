package gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import model.Schedule;

/**
 * SeatMapPanel.java
 * Renders seats and writes selected seat numbers into MainFrame.pendingSelectedSeats.
 */
public class SeatMapPanel extends JPanel implements Refreshable {

    private final MainFrame host;
    private final JPanel seatsGrid = new JPanel();
    private final JButton confirmBtn = new JButton("Confirm Seats");
    private final JButton backBtn = new JButton("← Back");

    private int rows = 8;
    private int cols = 5;
    private JToggleButton[][] seatButtons;
    private boolean[][] seatSelected;

    private Schedule schedule; // currently loaded schedule

    public SeatMapPanel(MainFrame host) {
        this.host = host;
        init();
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

    @Override
    public void refresh() {
        // Reset state and load schedule information from AppContext
        seatButtons = null;
        seatSelected = null;
        seatsGrid.removeAll();

        int scheduleId = controller.AppContext.getSelectedScheduleId();
        schedule = null;
        if (scheduleId <= 0) {
            seatsGrid.revalidate();
            seatsGrid.repaint();
            return;
        }

        // Find schedule from controller lists (ScheduleController doesn't expose getById)
        // We'll fetch schedules for the selected route and search for matching id
        int routeId = controller.AppContext.getSelectedRouteId();
        List<Schedule> candidates = new ArrayList<>();
        try { candidates.addAll(controller.AppContext.schedule().getSchedules(routeId, model.TransportType.BUS)); } catch (Exception ignored) {}
        try { candidates.addAll(controller.AppContext.schedule().getSchedules(routeId, model.TransportType.TRAIN)); } catch (Exception ignored) {}
        for (Schedule s : candidates) if (s.getId() == scheduleId) schedule = s;

        // If schedule found, set rows/cols based on totalSeats (try to keep grid rectangular)
        int totalSeats = (schedule != null && schedule.getTotalSeats() > 0) ? schedule.getTotalSeats() : (rows * cols);
        // derive rows x cols: prefer ~5 columns
        cols = Math.min(6, Math.max(4, totalSeats / 8 == 0 ? 5 : totalSeats / 8));
        rows = (int) Math.ceil((double) totalSeats / cols);

        seatButtons = new JToggleButton[rows][cols];
        seatSelected = new boolean[rows][cols];

        // Build reserved map from seatMap string (CSV)
        boolean[][] reserved = new boolean[rows][cols];
        if (schedule != null && schedule.getSeatMap() != null && !schedule.getSeatMap().isBlank()) {
            String[] tokens = schedule.getSeatMap().split(",");
            for (String t : tokens) {
                if (t.isBlank()) continue;
                try {
                    int sn = Integer.parseInt(t.trim());
                    int r = (sn - 1) / cols;
                    int c = (sn - 1) % cols;
                    if (r >= 0 && r < rows && c >= 0 && c < cols) reserved[r][c] = true;
                } catch (NumberFormatException ignored) {}
            }
        }

        seatsGrid.setLayout(new GridLayout(rows, cols, 8, 8));
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int seatNum = r * cols + c + 1;
                String label = String.valueOf(seatNum);
                JToggleButton btn = new JToggleButton(label);
                btn.setPreferredSize(new Dimension(60, 40));
                btn.setFocusPainted(false);

                if (reserved[r][c]) {
                    btn.setEnabled(false);
                    btn.setText(label + " (X)");
                } else {
                    final int rr = r, cc = c;
                    btn.addActionListener(a -> seatSelected[rr][cc] = btn.isSelected());
                }
                seatButtons[r][c] = btn;
                seatsGrid.add(btn);
            }
        }

        seatsGrid.revalidate();
        seatsGrid.repaint();
    }

    private void handleConfirm() {
        List<Integer> selectedSeatNumbers = new ArrayList<>();
        if (seatSelected != null) {
            for (int r = 0; r < seatSelected.length; r++) {
                for (int c = 0; c < seatSelected[r].length; c++) {
                    if (seatSelected[r][c]) selectedSeatNumbers.add(r * cols + c + 1);
                }
            }
        }

        if (selectedSeatNumbers.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select at least one seat.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        host.setPendingSelectedSeats(selectedSeatNumbers);
        JOptionPane.showMessageDialog(this, "Seats selected: " + selectedSeatNumbers, "Seats", JOptionPane.INFORMATION_MESSAGE);
        host.showPanel(MainFrame.PANEL_CONFIRMATION);
    }
}
