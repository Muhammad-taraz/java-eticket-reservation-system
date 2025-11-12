package gui;

import java.awt.*;
import javax.swing.*;

/**
 * HomePanel.java
 */
public class HomePanel extends JPanel {

    private final MainFrame host;

    public HomePanel(MainFrame host) {
        this.host = host;
        init();
    }

    private void init() {
        setLayout(new BorderLayout(12,12));
        setBorder(BorderFactory.createEmptyBorder(16,16,16,16));

        JLabel title = new JLabel("<html><h1>Welcome to E-Ticket</h1><p style='color:#555'>Choose an action below</p></html>");
        add(title, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12,12,12,12);

        JButton bookBtn = buildBigButton("Book Tickets", "Search routes and schedules", e -> host.showPanel(MainFrame.PANEL_ROUTE_SELECTION));
        JButton myBookingsBtn = buildBigButton("My Bookings", "View or cancel bookings", e -> host.showPanel(MainFrame.PANEL_MY_BOOKINGS));
        JButton logoutBtn = buildBigButton("Logout", "Return to login screen", e -> {
            controller.AppContext.setLoggedInUser(null);
            host.showPanel(MainFrame.PANEL_LOGIN);
        });

        gbc.gridx = 0; gbc.gridy = 0; grid.add(bookBtn, gbc);
        gbc.gridx = 1; grid.add(myBookingsBtn, gbc);
        gbc.gridx = 2; grid.add(logoutBtn, gbc);

        add(grid, BorderLayout.CENTER);
    }

    private JButton buildBigButton(String text, String tooltip, java.util.function.Consumer<java.awt.event.ActionEvent> action) {
        JButton btn = new JButton("<html><div style='text-align:center;'>" + text + "</div></html>");
        btn.setPreferredSize(new Dimension(260,140));
        btn.setToolTipText(tooltip);
        btn.addActionListener(action::accept);
        btn.setFocusPainted(false);
        btn.setBackground(new Color(245,245,255));
        btn.setBorder(BorderFactory.createLineBorder(new Color(200,200,220)));
        return btn;
    }
}
