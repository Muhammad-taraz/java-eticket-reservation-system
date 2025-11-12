package controller;

import model.User;

/**
 * AppContext.java
 * 
 * Provides global singletons and shared state for GUI integration.
 * Acts as a bridge between Swing panels and controllers.
 */
public class AppContext {
    private static final AuthController authController = new AuthController();
    private static final RouteController routeController = new RouteController();
    private static final ScheduleController scheduleController = new ScheduleController();
    private static final BookingController bookingController = new BookingController();

    private static User loggedInUser;
    private static int selectedRouteId = -1;
    private static int selectedScheduleId = -1;

    public static AuthController auth() { return authController; }
    public static RouteController route() { return routeController; }
    public static ScheduleController schedule() { return scheduleController; }
    public static BookingController booking() { return bookingController; }

    public static User getLoggedInUser() { return loggedInUser; }
    public static void setLoggedInUser(User u) { loggedInUser = u; }

    public static int getSelectedRouteId() { return selectedRouteId; }
    public static void setSelectedRouteId(int id) { selectedRouteId = id; }

    public static int getSelectedScheduleId() { return selectedScheduleId; }
    public static void setSelectedScheduleId(int id) { selectedScheduleId = id; }
}