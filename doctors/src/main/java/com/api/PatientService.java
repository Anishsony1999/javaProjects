package com.api;

import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.*;

public class PatientService {

    public int authenticatePatient(String email, String pass) {
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "SELECT * FROM user WHERE email = ? AND password = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, email);
                stmt.setString(2, pass);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    } else {
                        return 0;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void createPatient(HttpServletResponse resp, String fullname, String email, String password, String bloodGroup, String phoneNumber) {
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "INSERT INTO user (fullname, email, password, blood_group, phone_number, registered_date) VALUES (?, ?, ?, ?, ?, CURRENT_DATE)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, fullname);
                stmt.setString(2, email);
                stmt.setString(3, password);
                stmt.setString(4, bloodGroup);
                stmt.setString(5, phoneNumber);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    outputResponse(resp, "{\"message\":\"Patient created successfully\"}", 201);
                } else {
                    outputResponse(resp, "{\"error\":\"Failed to create patient\"}", 400);
                }
            }
        } catch (SQLException e) {
            outputResponse(resp, "{\"error\":\"" + e.getMessage() + "\"}", 500);
        }
    }

    public void getAllPatients(HttpServletResponse resp) {
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "SELECT * FROM user";
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                JSONObject result = new JSONObject();
                result.put("patients", new org.json.JSONArray());

                while (rs.next()) {
                    JSONObject patient = new JSONObject();
                    patient.put("id", rs.getInt("id"));
                    patient.put("fullname", rs.getString("fullname"));
                    patient.put("email", rs.getString("email"));
                    patient.put("blood_group", rs.getString("blood_group"));
                    patient.put("phone_number", rs.getString("phone_number"));
                    patient.put("registered_date", rs.getDate("registered_date"));
                    result.getJSONArray("patients").put(patient);
                }
                outputResponse(resp, result.toString(), 200);
            }
        } catch (SQLException e) {
            outputResponse(resp, "{\"error\":\"" + e.getMessage() + "\"}", 500);
        }
    }

    public void getPatientById(HttpServletResponse resp, int id) {
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "SELECT * FROM user WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        JSONObject patient = new JSONObject();
                        patient.put("id", rs.getInt("id"));
                        patient.put("fullname", rs.getString("fullname"));
                        patient.put("email", rs.getString("email"));
                        patient.put("blood_group", rs.getString("blood_group"));
                        patient.put("phone_number", rs.getString("phone_number"));
                        patient.put("registered_date", rs.getDate("registered_date"));
                        outputResponse(resp, patient.toString(), 200);
                    } else {
                        outputResponse(resp, "{\"error\":\"Patient not found\"}", 404);
                    }
                }
            }
        } catch (SQLException e) {
            outputResponse(resp, "{\"error\":\"" + e.getMessage() + "\"}", 500);
        }
    }

    public void updatePatient(HttpServletResponse resp, int id, String fullname, String email, String bloodGroup, String phoneNumber) {
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "UPDATE user SET fullname = ?, email = ?, blood_group = ?, phone_number = ? WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, fullname);
                stmt.setString(2, email);
                stmt.setString(3, bloodGroup);
                stmt.setString(4, phoneNumber);
                stmt.setInt(5, id);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    outputResponse(resp, "{\"message\":\"Patient updated successfully\"}", 200);
                } else {
                    outputResponse(resp, "{\"error\":\"Patient not found or no changes made\"}", 404);
                }
            }
        } catch (SQLException e) {
            outputResponse(resp, "{\"error\":\"" + e.getMessage() + "\"}", 500);
        }
    }

    public void deletePatient(HttpServletResponse resp, int id) {
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "DELETE FROM user WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    outputResponse(resp, "{\"message\":\"Patient deleted successfully\"}", 200);
                } else {
                    outputResponse(resp, "{\"error\":\"Patient not found\"}", 404);
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
