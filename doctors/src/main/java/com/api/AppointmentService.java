package com.api;

import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.*;

public class AppointmentService {

    public void createAppointment(HttpServletResponse resp, int userId, String appointmentDate, String appointmentReason, String appointmentTime) {
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "INSERT INTO appointment (user_id, appointment_date, appointment_reason, appointment_time) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, userId);
                stmt.setDate(2, Date.valueOf(appointmentDate));
                stmt.setString(3, appointmentReason);
                stmt.setString(4, appointmentTime);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    outputResponse(resp, "{\"message\":\"Appointment created successfully\"}", 201);
                } else {
                    outputResponse(resp, "{\"error\":\"Failed to create appointment\"}", 400);
                }
            }
        } catch (SQLException e) {
            outputResponse(resp, "{\"error\":\"" + e.getMessage() + "\"}", 500);
        }
    }

    public void getAppointments(HttpServletResponse resp) {
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "SELECT * FROM appointment";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                try (ResultSet rs = stmt.executeQuery()) {
                    JSONObject result = new JSONObject();
                    result.put("appointments", new org.json.JSONArray());

                    while (rs.next()) {
                        JSONObject appointment = new JSONObject();
                        appointment.put("id", rs.getInt("id"));
                        appointment.put("user_id", rs.getInt("user_id"));
                        appointment.put("appointment_date", rs.getDate("appointment_date"));
                        appointment.put("appointment_reason", rs.getString("appointment_reason"));
                        appointment.put("appointment_time", rs.getString("appointment_time"));
                        result.getJSONArray("appointments").put(appointment);
                    }
                    outputResponse(resp, result.toString(), 200);
                }
            }
        } catch (SQLException e) {
            outputResponse(resp, "{\"error\":\"" + e.getMessage() + "\"}", 500);
        }
    }

    public void getAppointmentsByUser(HttpServletResponse resp, int userId) {
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "SELECT * FROM appointment WHERE user_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, userId);
                try (ResultSet rs = stmt.executeQuery()) {
                    JSONObject result = new JSONObject();
                    result.put("appointments", new org.json.JSONArray());

                    while (rs.next()) {
                        JSONObject appointment = new JSONObject();
                        appointment.put("id", rs.getInt("id"));
                        appointment.put("appointment_date", rs.getDate("appointment_date"));
                        appointment.put("appointment_reason", rs.getString("appointment_reason"));
                        appointment.put("appointment_time", rs.getString("appointment_time"));
                        result.getJSONArray("appointments").put(appointment);
                    }
                    outputResponse(resp, result.toString(), 200);
                }
            }
        } catch (SQLException e) {
            outputResponse(resp, "{\"error\":\"" + e.getMessage() + "\"}", 500);
        }
    }

    public void getAppointmentsById(HttpServletResponse resp, int userId) {
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "SELECT * FROM appointment WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, userId);
                try (ResultSet rs = stmt.executeQuery()) {
                    JSONObject result = new JSONObject();
                    result.put("appointments", new org.json.JSONArray());

                    while (rs.next()) {
                        JSONObject appointment = new JSONObject();
                        appointment.put("id", rs.getInt("id"));
                        appointment.put("appointment_date", rs.getDate("appointment_date"));
                        appointment.put("appointment_reason", rs.getString("appointment_reason"));
                        appointment.put("appointment_time", rs.getString("appointment_time"));
                        result.getJSONArray("appointments").put(appointment);
                    }
                    outputResponse(resp, result.toString(), 200);
                }
            }
        } catch (SQLException e) {
            outputResponse(resp, "{\"error\":\"" + e.getMessage() + "\"}", 500);
        }
    }

    public void updateAppointment(HttpServletResponse resp, int id, String appointmentDate, String appointmentReason, String appointmentTime) {
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "UPDATE appointment SET appointment_date = ?, appointment_reason = ?, appointment_time = ? WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, appointmentDate);
                stmt.setString(2, appointmentReason);
                stmt.setString(3, appointmentTime);
                stmt.setInt(4, id);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    outputResponse(resp, "{\"message\":\"Appointment updated successfully\"}", 200);
                } else {
                    outputResponse(resp, "{\"error\":\"Appointment not found\"}", 404);
                }
            }
        } catch (SQLException e) {
            outputResponse(resp, "{\"error\":\"" + e.getMessage() + "\"}", 500);
        }
    }

    public void deleteAppointment(HttpServletResponse resp, int id) {
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "DELETE FROM appointment WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    outputResponse(resp, "{\"message\":\"Appointment deleted successfully\"}", 200);
                } else {
                    outputResponse(resp, "{\"error\":\"Appointment not found\"}", 404);
                }
            }
        } catch (SQLException e) {
            outputResponse(resp, "{\"error\":\"" + e.getMessage() + "\"}", 500);
        }
    }

    public void getUpcomingAppointments(HttpServletResponse resp) {
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "SELECT * FROM appointment WHERE appointment_date > CURRENT_DATE " +
                    "OR (appointment_date = CURRENT_DATE AND appointment_time > CURRENT_TIME)";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                try (ResultSet rs = stmt.executeQuery()) {
                    JSONObject result = new JSONObject();
                    result.put("appointments", new org.json.JSONArray());

                    while (rs.next()) {
                        JSONObject appointment = new JSONObject();
                        appointment.put("id", rs.getInt("id"));
                        appointment.put("user_id", rs.getInt("user_id"));
                        appointment.put("appointment_date", rs.getDate("appointment_date"));
                        appointment.put("appointment_reason", rs.getString("appointment_reason"));
                        appointment.put("appointment_time", rs.getString("appointment_time"));
                        result.getJSONArray("appointments").put(appointment);
                    }
                    outputResponse(resp, result.toString(), 200);
                }
            }
        } catch (SQLException e) {
            outputResponse(resp, "{\"error\":\"" + e.getMessage() + "\"}", 500);
        }
    }

    public void getUpcomingAppointmentsByUser(HttpServletResponse resp, int userId) {
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "SELECT * FROM appointment WHERE user_id = ? AND " +
                    "(appointment_date > CURRENT_DATE " +
                    "OR (appointment_date = CURRENT_DATE AND appointment_time > CURRENT_TIME))";

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, userId);
                try (ResultSet rs = stmt.executeQuery()) {
                    JSONObject result = new JSONObject();
                    result.put("appointments", new org.json.JSONArray());

                    while (rs.next()) {
                        JSONObject appointment = new JSONObject();
                        appointment.put("id", rs.getInt("id"));
                        appointment.put("appointment_date", rs.getDate("appointment_date"));
                        appointment.put("appointment_reason", rs.getString("appointment_reason"));
                        appointment.put("appointment_time", rs.getString("appointment_time"));
                        result.getJSONArray("appointments").put(appointment);
                    }
                    outputResponse(resp, result.toString(), 200);
                }
            }
        } catch (SQLException e) {
            outputResponse(resp, "{\"error\":\"" + e.getMessage() + "\"}", 500);
        }
    }



    // Utility method to send response back to client
    private void outputResponse(HttpServletResponse response, String payload, int status) {
        response.setHeader("Content-Type", "application/json");
        try {
            response.setStatus(status);
            if (payload != null) {
                byte[] bytes = payload.getBytes("UTF-8");
                response.setContentLength(bytes.length); // Set content length
                response.getOutputStream().write(bytes);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
