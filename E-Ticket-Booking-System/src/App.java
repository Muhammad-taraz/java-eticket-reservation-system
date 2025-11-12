import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

import dao.JDBCUtil;
import gui.MainFrame;

public class App {
    private static final Logger LOG = Logger.getLogger(App.class.getName());

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                LOG.log(Level.WARNING, "Failed to set system look & feel", e);
            }

            // Make connection mutable via final array
            final Connection[] connHolder = new Connection[1];

            try {
                connHolder[0] = JDBCUtil.getConnection();
                if (connHolder[0] == null) {
                    throw new IllegalStateException("JDBCUtil.getConnection() returned null");
                }
                LOG.info("Database connection established.");
            } catch (Throwable dbInitEx) {
                String msg = "Database initialization failed:\n" + dbInitEx.getMessage();
                LOG.log(Level.SEVERE, msg, dbInitEx);
                JOptionPane.showMessageDialog(null,
                        msg + "\nApplication will start in degraded mode.",
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE);
            }

            try {
                MainFrame mainFrame = new MainFrame();
                mainFrame.setLocationRelativeTo(null);
                mainFrame.setVisible(true);
            } catch (Throwable uiEx) {
                LOG.log(Level.SEVERE, "Failed to create or show MainFrame", uiEx);
                JOptionPane.showMessageDialog(null,
                        "Failed to launch application UI:\n" + uiEx.getMessage(),
                        "Startup Error",
                        JOptionPane.ERROR_MESSAGE);
                System.exit(2);
            }

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    if (connHolder[0] != null && !connHolder[0].isClosed()) {
                        connHolder[0].close();
                        LOG.info("Database connection closed.");
                    }
                } catch (Exception e) {
                    LOG.log(Level.WARNING, "Error while closing DB connection during shutdown", e);
                }
            }));
        });
    }
}
