package controller;

import dao.RouteDAO;
import model.Route;
import model.TransportType;
import java.util.*;

public class RouteController {
    private RouteDAO routeDAO = new RouteDAO();
    private List<Route> routesCache; // in-memory cache (ArrayList)

    public RouteController() {
        routesCache = routeDAO.getAllRoutes(); // load once
    }

    // === LINEAR SEARCH (by source and destination) ===
    public List<Route> searchRoutes(String source, String destination) {
        List<Route> matched = new ArrayList<>();
        for (Route r : routesCache) {
            if (r.getSource().equalsIgnoreCase(source)
                    && r.getDestination().equalsIgnoreCase(destination)) {
                matched.add(r);
            }
        }
        return matched;
    }

    // === OPTIONAL: Binary Search (if list sorted by source/destination) ===
    // public Route binarySearchRoute(String source, String destination) { ... }

    // === Get all available routes ===
    public List<Route> getAllRoutes() {
        return routesCache;
    }

    // === Get transport types for a given route ===
    public List<TransportType> getTransportTypes(int routeId) {
        return routeDAO.getTransportTypesByRoute(routeId);
    }
}