package com.api;

import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.*;

public class DoctorService {

    public boolean authenticateDoctor(String name, String pass) {
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "SELECT * FROM doctor WHERE doctorname = ? AND password = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, name);
                stmt.setString(2, pass);
                try (ResultSet rs = stmt.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void createDoctor(HttpServletResponse resp, String doctorName, String department, String password) {
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "INSERT INTO doctor (doctorname, department, password, registered_date) VALUES (?, ?, ?, CURRENT_DATE)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, doctorName);
                stmt.setString(2, department);
                stmt.setString(3, password);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    outputResponse(resp, "{\"message\":\"Doctor created successfully\"}", 201);
                } else {
                    outputResponse(resp, "{\"error\":\"Failed to create doctor\"}", 400);
                }
            }
        } catch (SQLException e) {
            outputResponse(resp, "{\"error\":\"" + e.getMessage() + "\"}", 500);
        }
    }

    public void getAllDoctors(HttpServletResponse resp) {
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "SELECT * FROM doctor";
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                JSONObject result = new JSONObject();
                result.put("doctors", new org.json.JSONArray());

                while (rs.next()) {
                    JSONObject doctor = new JSONObject();
                    doctor.put("id", rs.getInt("id"));
                    doctor.put("doctorname", rs.getString("doctorname"));
                    doctor.put("department", rs.getString("department"));
                    doctor.put("password", rs.getString("password"));
                    doctor.put("registered_date", rs.getDate("registered_date"));
                    result.getJSONArray("doctors").put(doctor);
                }
                outputResponse(resp, result.toString(), 200);
            }
        } catch (SQLException e) {
            outputResponse(resp, "{\"error\":\"" + e.getMessage() + "\"}", 500);
        }
    }

    public void getDoctorById(HttpServletResponse resp, int id) {
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "SELECT * FROM doctor WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        JSONObject doctor = new JSONObject();
                        doctor.put("id", rs.getInt("id"));
                        doctor.put("doctorname", rs.getString("doctorname"));
                        doctor.put("department", rs.getString("department"));
                        doctor.put("password", rs.getString("password"));
                        doctor.put("registered_date", rs.getDate("registered_date"));
                        outputResponse(resp, doctor.toString(), 200);
                    } else {
                        outputResponse(resp, "{\"error\":\"Doctor not found\"}", 404);
                    }
                }
            }
        } catch (SQLException e) {
            outputResponse(resp, "{\"error\":\"" + e.getMessage() + "\"}", 500);
        }
    }

    public void updateDoctor(HttpServletResponse resp, int id, String doctorName, String department, String password) {
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "UPDATE doctor SET doctorname = ?, department = ?, password = ? WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, doctorName);
                stmt.setString(2, department);
                stmt.setString(3, password);
                stmt.setInt(4, id);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    outputResponse(resp, "{\"message\":\"Doctor updated successfully\"}", 200);
                } else {
                    outputResponse(resp, "{\"error\":\"Doctor not found or no changes made\"}", 404);
                }
            }
        } catch (SQLException e) {
            outputResponse(resp, "{\"error\":\"" + e.getMessage() + "\"}", 500);
        }
    }

    public void deleteDoctor(HttpServletResponse resp, int id) {
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "DELETE FROM doctor WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    outputResponse(resp, "{\"message\":\"Doctor deleted successfully\"}", 200);
                } else {
                    outputResponse(resp, "{\"error\":\"Doctor not found\"}", 404);
                }
            }
        } catch (SQLException e) {
            outputResponse(resp, "{\"error\":\"" + e.getMessage() + "\"}", 500);
        }
    }

    private void outputResponse(HttpServletResponse response, String payload, int status) {
        response.setHeader("Content-Type", "application/json");
        try {
            response.setStatus(status);
            if (payload != null) {
                byte[] bytes = payload.getBytes("UTF-8");
                response.setContentLength(bytes.length);
                response.getOutputStream().write(bytes);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
