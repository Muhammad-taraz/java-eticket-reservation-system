package gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import model.Route;

/**
 * RouteSelectionPanel.java
 * Loads routes via AppContext.route().getAllRoutes()
 */
public class RouteSelectionPanel extends JPanel implements Refreshable {

    private final MainFrame host;
    private final DefaultListModel<Route> routeListModel = new DefaultListModel<>();
    private final JList<Route> routeJList = new JList<>(routeListModel);
    private final JTextField filterField = new JTextField(20);

    // Backing list of Route objects
    private final List<Route> routes = new ArrayList<>();

    public RouteSelectionPanel(MainFrame host) {
        this.host = host;
        init();
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
        routeJList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Route) {
                    setText(((Route) value).toString());
                }
                return this;
            }
        });
        center.add(new JScrollPane(routeJList), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton next = new JButton("Next →");
        JButton back = new JButton("← Back");
        bottom.add(back);
        bottom.add(next);

        add(center, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        searchBtn.addActionListener(e -> applyFilter());
        filterField.addActionListener(e -> applyFilter());
        next.addActionListener(e -> {
            Route sel = routeJList.getSelectedValue();
            if (sel != null) {
                controller.AppContext.setSelectedRouteId(sel.getId());
                host.showPanel(MainFrame.PANEL_SCHEDULE);
            } else {
                JOptionPane.showMessageDialog(this, "Choose a route first.", "Validation", JOptionPane.WARNING_MESSAGE);
            }
        });
        back.addActionListener(e -> host.showPanel(MainFrame.PANEL_HOME));
    }

    private void applyFilter() {
        String q = filterField.getText().trim().toLowerCase();
        routeListModel.clear();
        for (Route r : routes) {
            if (q.isEmpty() || r.getSource().toLowerCase().contains(q) || r.getDestination().toLowerCase().contains(q)) {
                routeListModel.addElement(r);
            }
        }
    }

    @Override
    public void refresh() {
        // load routes from controller
        routes.clear();
        routes.addAll(controller.AppContext.route().getAllRoutes());
        routeListModel.clear();
        for (Route r : routes) routeListModel.addElement(r);
    }
}
