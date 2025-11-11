package gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * SchedulePanel.java
 *
 * Displays schedules for the selected route and allows selection of a schedule.
 *
 * DSA:
 * - Uses an ArrayList to store schedule descriptions and simple linear search to filter by transport/date.
 *
 * Integration:
 * - TODO: Get schedules from RouteController / ScheduleDAO and populate the list.
 */
public class SchedulePanel extends JPanel {

    private final MainFrame host;
    private final DefaultListModel<String> model = new DefaultListModel<>();
    private final JList<String> scheduleList = new JList<>(model);
    private final JTextField dateFilterField = new JTextField(10);
    private final JComboBox<String> transportCombo = new JComboBox<>(new String[]{"ALL","BUS","TRAIN"});

    // ArrayList holds schedule strings (DSA container)
    private final List<String> schedules = new ArrayList<>();

    public SchedulePanel(MainFrame host) {
        this.host = host;
        init();
        loadDemoSchedules();
        refreshModel();
    }

    private void init() {
        setLayout(new BorderLayout(12,12));
        setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        JLabel header = new JLabel("<html><h2>Available Schedules</h2></html>");
        add(header, BorderLayout.NORTH);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Date (YYYY-MM-DD):"));
        top.add(dateFilterField);
        top.add(new JLabel("Transport:"));
        top.add(transportCombo);
        JButton filterBtn = new JButton("Filter");
        top.add(filterBtn);
        add(top, BorderLayout.PAGE_START);

        scheduleList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(scheduleList), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton back = new JButton("← Back");
        JButton proceed = new JButton("Select Seats →");
        bottom.add(back);
        bottom.add(proceed);
        add(bottom, BorderLayout.SOUTH);

        filterBtn.addActionListener(e -> applyFilters());
        back.addActionListener(e -> host.showPanel(MainFrame.PANEL_ROUTE_SELECTION));
        proceed.addActionListener(e -> {
            if (scheduleList.getSelectedIndex() >= 0) {
                // TODO: register selected schedule to shared booking controller/state
                host.showPanel(MainFrame.PANEL_SEAT_MAP);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a schedule first.", "Validation", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    private void loadDemoSchedules() {
        schedules.clear();
        schedules.add("2025-01-20 09:00 (BUS) — Karachi → Lahore — Seats left: 40");
        schedules.add("2025-01-20 16:30 (BUS) — Karachi → Lahore — Seats left: 40");
        schedules.add("2025-01-20 06:00 (TRAIN) — Karachi → Lahore — Seats left: 200");
        schedules.add("2025-01-21 10:45 (BUS) — Karachi → Islamabad — Seats left: 40");
        schedules.add("2025-01-21 19:15 (TRAIN) — Lahore → Islamabad — Seats left: 150");
        schedules.add("2025-01-22 14:00 (BUS) — Lahore → Islamabad — Seats left: 40");
    }

    private void refreshModel() {
        model.clear();
        for (String s : schedules) model.addElement(s);
    }

    private void applyFilters() {
        String dateQ = dateFilterField.getText().trim();
        String transport = (String) transportCombo.getSelectedItem();
        model.clear();
        for (String s : schedules) {
            boolean match = true;
            if (!dateQ.isEmpty() && !s.contains(dateQ)) match = false;
            if (!"ALL".equals(transport) && !s.contains("(" + transport + ")")) match = false;
            if (match) model.addElement(s);
        }
    }
}
