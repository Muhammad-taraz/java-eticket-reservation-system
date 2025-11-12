package model;

import java.sql.Date;
import java.sql.Time;

public class Schedule {
    private int id;
    private int routeId;
    private TransportType transportType;
    private Date travelDate;
    private Time travelTime;
    private int totalSeats;
    private int seatsLeft;
    private String seatMap;

    public Schedule() {}

    public Schedule(int id, int routeId, TransportType transportType, Date travelDate, Time travelTime,
                    int totalSeats, int seatsLeft, String seatMap) {
        this.id = id;
        this.routeId = routeId;
        this.transportType = transportType;
        this.travelDate = travelDate;
        this.travelTime = travelTime;
        this.totalSeats = totalSeats;
        this.seatsLeft = seatsLeft;
        this.seatMap = seatMap;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getRouteId() { return routeId; }
    public void setRouteId(int routeId) { this.routeId = routeId; }

    public TransportType getTransportType() { return transportType; }
    public void setTransportType(TransportType transportType) { this.transportType = transportType; }

    public Date getTravelDate() { return travelDate; }
    public void setTravelDate(Date travelDate) { this.travelDate = travelDate; }

    public Time getTravelTime() { return travelTime; }
    public void setTravelTime(Time travelTime) { this.travelTime = travelTime; }

    public int getTotalSeats() { return totalSeats; }
    public void setTotalSeats(int totalSeats) { this.totalSeats = totalSeats; }

    public int getSeatsLeft() { return seatsLeft; }
    public void setSeatsLeft(int seatsLeft) { this.seatsLeft = seatsLeft; }

    public String getSeatMap() { return seatMap; }
    public void setSeatMap(String seatMap) { this.seatMap = seatMap; }

    @Override
    public String toString() {
        return "Schedule{" +
                "id=" + id +
                ", routeId=" + routeId +
                ", type=" + transportType +
                ", date=" + travelDate +
                ", time=" + travelTime +
                ", seatsLeft=" + seatsLeft +
                '}';
    }
}