package gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * RouteSelectionPanel.java
 *
 * Allows the user to select source/destination and transport type.
 *
 * DSA focus:
 * - Uses ArrayList<String> to hold route descriptions (dynamic list).
 * - Demonstrates searching/filtering on the list via traversal.
 *
 * Integration:
 * - Replace the demo route population with RouteController.getRoutes() call.
 */
public class RouteSelectionPanel extends JPanel {

    private final MainFrame host;
    private final DefaultListModel<String> routeListModel = new DefaultListModel<>();
    private final JList<String> routeJList = new JList<>(routeListModel);
    private final JTextField filterField = new JTextField(20);

    // Underlying dynamic list (ArrayList) representing available routes (DSA)
    private final List<String> routes = new ArrayList<>();

    public RouteSelectionPanel(MainFrame host) {
        this.host = host;
        init();
        loadRoutesDemo();
        refreshListFromArrayList();
    }

    private void init() {
        setLayout(new BorderLayout(12,12));
        setBorder(BorderFactory.createEmptyBorder(12,12,12,12));

        JLabel header = new JLabel("<html><h2>Select a route</h2></html>");
        add(header, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(8,8));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Filter:"));
        top.add(filterField);
        JButton searchBtn = new JButton("Search");
        top.add(searchBtn);
        center.add(top, BorderLayout.NORTH);

        routeJList.setVisibleRowCount(10);
        routeJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        center.add(new JScrollPane(routeJList), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton next = new JButton("Next →");
        JButton back = new JButton("← Back");
        bottom.add(back);
        bottom.add(next);

        add(center, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        // Listeners
        searchBtn.addActionListener(e -> applyFilter());
        filterField.addActionListener(e -> applyFilter());
        next.addActionListener(e -> {
            if (routeJList.getSelectedIndex() >= 0) {
                // TODO: store selected route in a controller or shared state
                host.showPanel(MainFrame.PANEL_SCHEDULE);
            } else {
                JOptionPane.showMessageDialog(this, "Choose a route first.", "Validation", JOptionPane.WARNING_MESSAGE);
            }
        });
        back.addActionListener(e -> host.showPanel(MainFrame.PANEL_HOME));
    }

    private void applyFilter() {
        String q = filterField.getText().trim().toLowerCase();
        // Demonstrates traversal/search in the ArrayList routes
        routeListModel.clear();
        for (String r : routes) {
            if (q.isEmpty() || r.toLowerCase().contains(q)) {
                routeListModel.addElement(r);
            }
        }
    }

    /**
     * Demo loader: populate the ArrayList with seeded route strings.
     * Replace this with RouteController.fetchRoutes() when available.
     */
    private void loadRoutesDemo() {
        routes.clear();
        // Example route strings: "Karachi → Lahore (BUS, TRAIN)"
        routes.add("Karachi → Lahore (BUS, TRAIN)");
        routes.add("Karachi → Islamabad (BUS)");
        routes.add("Lahore → Islamabad (TRAIN, BUS)");
        routes.add("Islamabad → Peshawar (BUS)");
        routes.add("Multan → Karachi (BUS)");
    }

    private void refreshListFromArrayList() {
        routeListModel.clear();
        for (String r : routes) routeListModel.addElement(r);
    }
}
