package gui;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;

/**
 * Main application window that holds all panels in a CardLayout.
 * Also stores small session data (pending selected seats).
 */
public class MainFrame extends JFrame {

    public static final String PANEL_LOGIN = "Login";
    public static final String PANEL_REGISTER = "Register";
    public static final String PANEL_HOME = "Home";
    public static final String PANEL_ROUTE_SELECTION = "RouteSelection";
    public static final String PANEL_SCHEDULE = "Schedule";
    public static final String PANEL_SEAT_MAP = "SeatMap";
    public static final String PANEL_CONFIRMATION = "Confirmation";
    public static final String PANEL_MY_BOOKINGS = "MyBookings";

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardContainer = new JPanel(cardLayout);
    private final Map<String, JPanel> panelsMap = new HashMap<>();

    // Small session storage exposed to GUI panels
    private List<Integer> pendingSelectedSeats = null;

    public MainFrame() {
        super("E-Ticket Booking System");
        initLookAndFeel();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1000, 700));
        setLocationRelativeTo(null);
        buildUI();
    }

    private void buildUI() {
        // Register panels
        addPanel(PANEL_LOGIN, new LoginPanel(this));
        addPanel(PANEL_REGISTER, new RegisterPanel(this));
        addPanel(PANEL_HOME, new HomePanel(this));
        addPanel(PANEL_ROUTE_SELECTION, new RouteSelectionPanel(this));
        addPanel(PANEL_SCHEDULE, new SchedulePanel(this));
        addPanel(PANEL_SEAT_MAP, new SeatMapPanel(this));
        addPanel(PANEL_CONFIRMATION, new ConfirmationPanel(this));
        addPanel(PANEL_MY_BOOKINGS, new MyBookingsPanel(this));

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(cardContainer, BorderLayout.CENTER);

        showPanel(PANEL_LOGIN);
    }

    public void addPanel(String name, JPanel panel) {
        panelsMap.put(name, panel);
        cardContainer.add(panel, name);
    }

    public void replacePanel(String name, JPanel panel) {
        JPanel old = panelsMap.remove(name);
        if (old != null) cardContainer.remove(old);
        addPanel(name, panel);
        cardContainer.revalidate();
        cardContainer.repaint();
    }

    /**
     * Show registered panel by name and call refresh() if panel implements Refreshable.
     */
    public void showPanel(String name) {
        JPanel p = panelsMap.get(name);
        if (p == null) {
            System.err.println("Panel not found: " + name);
            return;
        }
        if (p instanceof Refreshable) {
            ((Refreshable) p).refresh();
        }
        cardLayout.show(cardContainer, name);
    }

    private void initLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            Font base = UIManager.getFont("Label.font");
            if (base != null) {
                Font uiFont = base.deriveFont(14f);
                UIManager.put("Label.font", uiFont);
                UIManager.put("Button.font", uiFont);
                UIManager.put("TextField.font", uiFont);
                UIManager.put("TextArea.font", uiFont);
                UIManager.put("List.font", uiFont);
            }
        } catch (Exception e) {
            // ignore and use defaults
        }
    }

    // Session accessor for seats selected in SeatMapPanel (used by ConfirmationPanel)
    public void setPendingSelectedSeats(java.util.List<Integer> seats) { this.pendingSelectedSeats = seats; }
    public java.util.List<Integer> getPendingSelectedSeats() { return pendingSelectedSeats; }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame mf = new MainFrame();
            mf.setVisible(true);
        });
    }
}
