package controller;

import dao.ScheduleDAO;
import model.Schedule;
import model.TransportType;
import java.util.*;

public class ScheduleController {
    private ScheduleDAO scheduleDAO = new ScheduleDAO();

    public List<Schedule> getSchedules(int routeId, TransportType type) {
        return scheduleDAO.getSchedulesByRouteAndTransport(routeId, type.name());
    }

    // === Example seat map logic using array traversal ===
    public void displaySeatMap(Schedule schedule) {
        int rows = schedule.getTotalSeats() / 4; // assuming 4 seats per row
        boolean[][] seatMatrix = new boolean[rows][4];
        String[] bookedSeats = schedule.getSeatMap().split(",");

        for (String s : bookedSeats) {
            if (!s.isBlank()) {
                int seatNo = Integer.parseInt(s.trim());
                int r = (seatNo - 1) / 4;
                int c = (seatNo - 1) % 4;
                seatMatrix[r][c] = true; // mark booked
            }
        }

        System.out.println("\nSeat Map (O = available, X = booked):");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.print(seatMatrix[i][j] ? "X " : "O ");
            }
            System.out.println();
        }
    }
}