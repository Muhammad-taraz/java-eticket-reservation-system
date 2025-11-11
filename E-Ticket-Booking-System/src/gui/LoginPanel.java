package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * LoginPanel.java
 *
 * UI for user login. Demonstrates basic validation, nice layout, and navigation.
 *
 * DSA notes:
 * - Uses arrays/lists nowhere heavy here but demonstrates constant-time checks for input presence.
 */
public class LoginPanel extends JPanel {

    private final MainFrame host;
    private final JTextField emailField = new JTextField(28);
    private final JPasswordField passwordField = new JPasswordField(28);
    private final JButton loginBtn = new JButton("Login");
    private final JButton registerBtn = new JButton("Register");

    public LoginPanel(MainFrame host) {
        this.host = host;
        initLayout();
        initListeners();
    }

    private void initLayout() {
        setLayout(new BorderLayout(20, 20));
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        // Left - branding panel
        JPanel branding = new JPanel(new BorderLayout());
        JLabel title = new JLabel("<html><span style='font-size:24pt;font-weight:600;'>E-Ticket</span><br><span style='font-size:11pt;color:#666;'>Smart Booking</span></html>");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        branding.add(title, BorderLayout.NORTH);

        JLabel hero = new JLabel("<html><center>Fast bookings • Secure payments • Smart seat map</center></html>");
        hero.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        hero.setHorizontalAlignment(SwingConstants.CENTER);
        branding.add(hero, BorderLayout.CENTER);

        // Right - form
        JPanel form = new JPanel();
        form.setLayout(new GridBagLayout());
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200,200,200)),
                BorderFactory.createEmptyBorder(16,16,16,16)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,8,8,8);
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;

        JLabel emailLabel = new JLabel("Email");
        form.add(emailLabel, gbc);

        gbc.gridx = 1;
        form.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        JLabel passLabel = new JLabel("Password");
        form.add(passLabel, gbc);

        gbc.gridx = 1;
        form.add(passwordField, gbc);

        gbc.gridx = 1; gbc.gridy++;
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        loginBtn.setPreferredSize(new Dimension(110, 34));
        registerBtn.setPreferredSize(new Dimension(110, 34));
        btnRow.add(registerBtn);
        btnRow.add(loginBtn);
        form.add(btnRow, gbc);

        // Tooltips
        emailField.setToolTipText("Enter your registered email");
        passwordField.setToolTipText("Enter your password");

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, branding, form);
        split.setResizeWeight(0.5);
        split.setDividerSize(2);
        add(split, BorderLayout.CENTER);

        // Footer small note
        JLabel footer = new JLabel("Tip: Use your registered email and password. (demo only UI)");
        footer.setHorizontalAlignment(SwingConstants.CENTER);
        footer.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
        add(footer, BorderLayout.SOUTH);
    }

    private void initListeners() {
        loginBtn.addActionListener(this::handleLogin);
        registerBtn.addActionListener(e -> host.showPanel(MainFrame.PANEL_REGISTER));
    }

    private void handleLogin(ActionEvent e) {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter email and password.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // TODO: Replace this placeholder with real controller call:
        // AuthController auth = AuthController.getInstance();
        // User user = auth.login(email, password);
        //
        // if (user != null) { host.showPanel(MainFrame.PANEL_HOME); } else { show error }
        //
        // For now: demo flow that accepts any non-empty input (so you can test the GUI immediately).
        boolean demoSuccess = true;

        if (demoSuccess) {
            // On success navigate to home
            host.showPanel(MainFrame.PANEL_HOME);
        } else {
            JOptionPane.showMessageDialog(this, "Login failed. Check credentials.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}
