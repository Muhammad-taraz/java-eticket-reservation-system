package dao;

import model.Route;
import model.TransportType;
import java.sql.*;
import java.util.*;

public class RouteDAO {

    // Fetch all routes
    public List<Route> getAllRoutes() {
        List<Route> routes = new ArrayList<>();
        String sql = "SELECT * FROM routes";
        try (Connection conn = JDBCUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                routes.add(new Route(rs.getInt("id"), rs.getString("source"), rs.getString("destination")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return routes;
    }

    // Fetch allowed transport types for a given route
    public List<TransportType> getTransportTypesByRoute(int routeId) {
        List<TransportType> transports = new ArrayList<>();
        String sql = "SELECT transport_type FROM route_transports WHERE route_id = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, routeId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                transports.add(TransportType.valueOf(rs.getString("transport_type")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transports;
    }
}