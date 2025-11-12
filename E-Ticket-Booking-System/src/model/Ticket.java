package model;

import java.sql.Timestamp;

public class Ticket {
    private int id;
    private int userId;
    private int scheduleId;
    private String seats;
    private Timestamp bookingTime;
    private String paymentMode;
    private String status;

    public Ticket() {}

    public Ticket(int userId, int scheduleId, String seats, String paymentMode, String status) {
        this.userId = userId;
        this.scheduleId = scheduleId;
        this.seats = seats;
        this.paymentMode = paymentMode;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getScheduleId() { return scheduleId; }
    public void setScheduleId(int scheduleId) { this.scheduleId = scheduleId; }

    public String getSeats() { return seats; }
    public void setSeats(String seats) { this.seats = seats; }

    public Timestamp getBookingTime() { return bookingTime; }
    public void setBookingTime(Timestamp bookingTime) { this.bookingTime = bookingTime; }

    public String getPaymentMode() { return paymentMode; }
    public void setPaymentMode(String paymentMode) { this.paymentMode = paymentMode; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Ticket{id=" + id + ", userId=" + userId +
                ", scheduleId=" + scheduleId + ", seats='" + seats + '\'' +
                ", status='" + status + '\'' + '}';
    }
}