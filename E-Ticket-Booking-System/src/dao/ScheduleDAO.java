package dao;

import model.Schedule;
import model.TransportType;
import java.sql.*;
import java.util.*;

public class ScheduleDAO {

    public List<Schedule> getSchedulesByRouteAndTransport(int routeId, String transportType) {
        List<Schedule> schedules = new ArrayList<>();
        String sql = "SELECT * FROM schedules WHERE route_id = ? AND transport_type = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, routeId);
            stmt.setString(2, transportType);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                schedules.add(mapToSchedule(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return schedules;
    }

    private Schedule mapToSchedule(ResultSet rs) throws SQLException {
        Schedule s = new Schedule();
        s.setId(rs.getInt("id"));
        s.setRouteId(rs.getInt("route_id"));
        s.setTransportType(TransportType.valueOf(rs.getString("transport_type")));
        s.setTravelDate(rs.getDate("travel_date"));
        s.setTravelTime(rs.getTime("travel_time"));
        s.setTotalSeats(rs.getInt("total_seats"));
        s.setSeatsLeft(rs.getInt("seats_left"));
        s.setSeatMap(rs.getString("seat_map"));
        return s;
    }
}