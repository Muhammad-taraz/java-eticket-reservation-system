package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

/**
 * RegisterPanel.java
 * Registers using controller.AppContext.auth().registerUser(...)
 */
public class RegisterPanel extends JPanel {

    private final MainFrame host;
    private final JTextField nameField = new JTextField(26);
    private final JTextField emailField = new JTextField(26);
    private final JTextField phoneField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(26);
    private final JButton registerBtn = new JButton("Create Account");
    private final JButton backBtn = new JButton("Back to Login");

    public RegisterPanel(MainFrame host) {
        this.host = host;
        initLayout();
        initListeners();
    }

    private void initLayout() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(24,24,24,24));

        JLabel header = new JLabel("<html><h2>Create an account</h2><p>Fill details to register — fields are validated client-side</p></html>");
        add(header, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridBagLayout());
        center.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,8,8,8);
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.WEST;

        center.add(new JLabel("Full name"), gbc);
        gbc.gridx = 1; center.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy++; center.add(new JLabel("Email"), gbc);
        gbc.gridx = 1; center.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy++; center.add(new JLabel("Phone"), gbc);
        gbc.gridx = 1; center.add(phoneField, gbc);

        gbc.gridx = 0; gbc.gridy++; center.add(new JLabel("Password"), gbc);
        gbc.gridx = 1; center.add(passwordField, gbc);

        gbc.gridx = 1; gbc.gridy++;
        JPanel row = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        registerBtn.setPreferredSize(new Dimension(160,36));
        backBtn.setPreferredSize(new Dimension(120,36));
        row.add(backBtn);
        row.add(registerBtn);
        center.add(row, gbc);

        add(center, BorderLayout.CENTER);
    }

    private void initListeners() {
        backBtn.addActionListener(e -> host.showPanel(MainFrame.PANEL_LOGIN));
        registerBtn.addActionListener(this::handleRegister);
    }

    private void handleRegister(ActionEvent evt) {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String pass = new String(passwordField.getPassword());

        String[] required = {"Name", "Email", "Phone", "Password"};
        String[] values = {name, email, phone, pass};
        for (int i = 0; i < required.length; i++) {
            if (values[i].isEmpty()) {
                JOptionPane.showMessageDialog(this, required[i] + " is required.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        boolean ok = controller.AppContext.auth().registerUser(name, email, phone, pass);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Registered successfully. Please login.", "Success", JOptionPane.INFORMATION_MESSAGE);
            host.showPanel(MainFrame.PANEL_LOGIN);
        } else {
            JOptionPane.showMessageDialog(this, "Email already exists or registration failed.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
