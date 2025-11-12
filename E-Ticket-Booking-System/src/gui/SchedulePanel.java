package gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import model.Schedule;
import model.TransportType;

/**
 * SchedulePanel.java
 * Loads schedules for selected route using ScheduleController via AppContext.
 */
public class SchedulePanel extends JPanel implements Refreshable {

    private final MainFrame host;
    private final DefaultListModel<Schedule> model = new DefaultListModel<>();
    private final JList<Schedule> scheduleList = new JList<>(model);
    private final JTextField dateFilterField = new JTextField(10);
    private final JComboBox<String> transportCombo = new JComboBox<>(new String[]{"ALL","BUS","TRAIN"});

    private final List<Schedule> schedules = new ArrayList<>();

    public SchedulePanel(MainFrame host) {
        this.host = host;
        init();
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
        scheduleList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Schedule) {
                    setText(value.toString());
                }
                return this;
            }
        });
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
            Schedule sel = scheduleList.getSelectedValue();
            if (sel != null) {
                controller.AppContext.setSelectedScheduleId(sel.getId());
                host.showPanel(MainFrame.PANEL_SEAT_MAP);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a schedule first.", "Validation", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    @Override
    public void refresh() {
        // Load schedules for currently selected route
        schedules.clear();
        model.clear();
        int routeId = controller.AppContext.getSelectedRouteId();
        if (routeId <= 0) {
            model.addElement(new Schedule()); // empty placeholder (to avoid null)
            return;
        }

        // Try fetching BUS and TRAIN schedules separately and add all
        try {
            schedules.addAll(controller.AppContext.schedule().getSchedules(routeId, TransportType.BUS));
        } catch (Exception ignored) {}
        try {
            schedules.addAll(controller.AppContext.schedule().getSchedules(routeId, TransportType.TRAIN));
        } catch (Exception ignored) {}

        for (Schedule s : schedules) model.addElement(s);
    }

    private void applyFilters() {
        String dateQ = dateFilterField.getText().trim();
        String transport = (String) transportCombo.getSelectedItem();
        model.clear();
        for (Schedule s : schedules) {
            boolean match = true;
            if (!dateQ.isEmpty() && (s.getTravelDate() == null || !s.getTravelDate().toString().contains(dateQ))) match = false;
            if (!"ALL".equals(transport) && !s.getTransportType().name().equalsIgnoreCase(transport)) match = false;
            if (match) model.addElement(s);
        }
    }
}
