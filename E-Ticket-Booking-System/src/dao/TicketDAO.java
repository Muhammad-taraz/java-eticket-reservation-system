package dao;

import model.Ticket;
import java.sql.*;
import java.util.*;

public class TicketDAO {

    // Create new booking
    public boolean createTicket(Ticket t) {
        String sql = "INSERT INTO tickets (user_id, schedule_id, seats, payment_mode, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, t.getUserId());
            stmt.setInt(2, t.getScheduleId());
            stmt.setString(3, t.getSeats());
            stmt.setString(4, t.getPaymentMode());
            stmt.setString(5, t.getStatus());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Cancel ticket
    public boolean cancelTicket(int ticketId) {
        String sql = "UPDATE tickets SET status = 'CANCELLED' WHERE id = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ticketId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get all tickets of a user
    public List<Ticket> getTicketsByUser(int userId) {
        List<Ticket> tickets = new ArrayList<>();
        String sql = "SELECT * FROM tickets WHERE user_id = ?";
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) tickets.add(mapToTicket(rs));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }

    private Ticket mapToTicket(ResultSet rs) throws SQLException {
        Ticket t = new Ticket();
        t.setId(rs.getInt("id"));
        t.setUserId(rs.getInt("user_id"));
        t.setScheduleId(rs.getInt("schedule_id"));
        t.setSeats(rs.getString("seats"));
        t.setBookingTime(rs.getTimestamp("booking_time"));
        t.setPaymentMode(rs.getString("payment_mode"));
        t.setStatus(rs.getString("status"));
        return t;
    }
}