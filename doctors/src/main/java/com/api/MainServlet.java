package com.api;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet("/api/*")
public class MainServlet extends HttpServlet {

    private final DoctorService doctorService = new DoctorService();
    private final AppointmentService appointmentService = new AppointmentService();
    private final PatientService patientService = new PatientService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();

        // Adding CORS headers
        addCorsHeaders(resp);

        if (path == null || path.equals("/")) {
            outputResponse(resp, "{\"error\":\"Missing resource path\"}", 400);
            return;
        }

        try {
            if (path.startsWith("/login")) {
                handleLogin(req, resp);
            } else if (path.startsWith("/doctors")) {
                handleDoctorsRequests(path, resp);
            } else if (path.startsWith("/appointments")) {
                handleAppointmentsRequests(path, resp);
            } else if (path.startsWith("/patients")) {
                handlePatientsRequests(path, resp);
            } else if (path.startsWith("/deletePatient")) {
                handleDeletePatient(path,resp);
            } else if (path.startsWith("/deleteAppointment")){
                handleDeleteAppointment(path,resp);
            }
            else{
                outputResponse(resp, "{\"error\":\"Invalid resource\"}", 404);
            }
        } catch (Exception e) {
            e.printStackTrace();
            outputResponse(resp, "{\"error\":\"Error processing request\"}", 500);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();

        // Adding CORS headers
        addCorsHeaders(resp);

        try {
            if (path.startsWith("/doctors")) {
                handleCreateDoctor(req, resp);
            } else if (path.startsWith("/patients")) {
                handleCreatePatient(req, resp);
            } else if (path.startsWith("/appointments")) {
                handleCreateAppointment(req, resp);
            } else if (path.startsWith("/updateAppointment")){
                handleUpdateAppointment(req,resp);
            } else if (path.startsWith("/updatePatients")){
                handleUpdatePatient(req,resp);
            } else {
                outputResponse(resp, "{\"error\":\"Invalid resource\"}", 404);
            }
        } catch (Exception e) {
            e.printStackTrace();
            outputResponse(resp, "{\"error\":\"Error processing request\"}", 500);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();

        // Adding CORS headers
        addCorsHeaders(resp);

        try {
            if (path.startsWith("/doctors")) {
                handleUpdateDoctor(req, resp);
            } else if (path.startsWith("/patients")) {
                handleUpdatePatient(req, resp);
            } else if (path.startsWith("/appointments")) {
                handleUpdateAppointment(req, resp);
            } else {
                outputResponse(resp, "{\"error\":\"Invalid resource\"}", 404);
            }
        } catch (Exception e) {
            e.printStackTrace();
            outputResponse(resp, "{\"error\":\"Error processing request\"}", 500);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();

        // Adding CORS headers
        addCorsHeaders(resp);

        try {
            if (path.startsWith("/doctors")) {
                handleDeleteDoctor(path, resp);
            } else if (path.startsWith("/patients")) {
                handleDeletePatient(path, resp);
            } else if (path.startsWith("/appointments")) {
                handleDeleteAppointment(path, resp);
            } else {
                outputResponse(resp, "{\"error\":\"Invalid resource\"}", 404);
            }
        } catch (Exception e) {
            e.printStackTrace();
            outputResponse(resp, "{\"error\":\"Error processing request\"}", 500);
        }
    }

    // Handle Update Doctor Request
    private void handleUpdateDoctor(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = extractIdFromPath(req.getPathInfo());
        String fullname = req.getParameter("fullname");
        String department = req.getParameter("department");
        String password = req.getParameter("password");

        doctorService.updateDoctor(resp, id, fullname, department, password);
    }

    // Handle Update Patient Request
    private void handleUpdatePatient(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = extractIdFromPath(req.getPathInfo());
        String fullname = req.getParameter("fullname");
        String email = req.getParameter("email");
        String bloodGroup = req.getParameter("blood_group");
        String phoneNumber = req.getParameter("phone_number");

        patientService.updatePatient(resp, id, fullname, email, bloodGroup, phoneNumber);
    }

    // Handle Update Appointment Request
    private void handleUpdateAppointment(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = extractIdFromPath(req.getPathInfo());
        String date = req.getParameter("date");
        String reason = req.getParameter("reason");
        String time = req.getParameter("time");

        appointmentService.updateAppointment(resp, id, date, reason, time);
    }

    // Handle Delete Doctor Request
    private void handleDeleteDoctor(String path, HttpServletResponse resp) throws IOException {
        int id = extractIdFromPath(path);
        doctorService.deleteDoctor(resp, id);
    }

    // Handle Delete Patient Request
    private void handleDeletePatient(String path, HttpServletResponse resp) throws IOException {
            int id = extractIdFromPath(path);
            patientService.deletePatient(resp, id);
    }

    // Handle Delete Appointment Request
    private void handleDeleteAppointment(String path, HttpServletResponse resp) throws IOException {
        int id = extractIdFromPath(path);
        appointmentService.deleteAppointment(resp, id);
    }


    // Handle Login Request
    private void handleLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        if (email == null || password == null) {
            outputResponse(resp, "{\"error\":\"Missing username or password\"}", 400);
            return;
        }

        try {
            int id = patientService.authenticatePatient(email, password);

            if (doctorService.authenticateDoctor(email, password)) {
                outputResponse(resp, "{\"message\":\"Login successful\", \"role\":\"admin\"}", 200);
            } else if (id!=0) {
                String jsonResponse = String.format("{\"message\":\"Login successful\", \"role\":\"user\",\"id\":%d}", id);
                outputResponse(resp, jsonResponse, 200);
            } else {
                outputResponse(resp, "{\"error\":\"Invalid credentials\"}", 401);
            }
        } catch (Exception e) {
            e.printStackTrace();
            outputResponse(resp, "{\"error\":\"Error during authentication\"}", 500);
        }
    }

    // Handle Doctors Requests
    private void handleDoctorsRequests(String path, HttpServletResponse resp) throws IOException {
        if (path.equals("/doctors")) {
            doctorService.getAllDoctors(resp);
        } else if (path.matches("/doctors/\\d+")) {
            int id = extractIdFromPath(path);
            doctorService.getDoctorById(resp, id);
        }
    }

    // Handle Appointments Requests
    private void handleAppointmentsRequests(String path, HttpServletResponse resp) throws IOException {
        if (path.equals("/appointments")) {
            appointmentService.getAppointments(resp);
        } else if (path.matches("/appointments/\\d+")) {
            int id = extractIdFromPath(path);
            appointmentService.getAppointmentsByUser(resp, id);
        } else if (path.matches("/appointmentsById/\\d+")) {
            int id = extractIdFromPath(path);
            appointmentService.getAppointmentsById(resp, id);
        }else if (path.equals("/appointments/upcoming")) {
            appointmentService.getUpcomingAppointments(resp);
        } else if (path.matches("/appointments/upcoming/\\d+")) {
            int id = extractIdFromPath(path, 3); // User ID is in the 3rd segment
            appointmentService.getUpcomingAppointmentsByUser(resp, id);
        }
    }

    // Handle Patients Requests
    private void handlePatientsRequests(String path, HttpServletResponse resp) throws IOException {
        if (path.equals("/patients")) {
            patientService.getAllPatients(resp);
        } else if (path.matches("/patients/\\d+")) {
            int id = extractIdFromPath(path);
            patientService.getPatientById(resp, id);
        }
    }

    // Handle Create Doctor Request
    private void handleCreateDoctor(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String fullname = req.getParameter("fullname");
        String department = req.getParameter("department");
        String password = req.getParameter("password");

        doctorService.createDoctor(resp, fullname, department, password);
    }

    // Handle Create Patient Request
    private void handleCreatePatient(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String fullname = req.getParameter("fullname");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String bloodGroup = req.getParameter("blood_group");
        String phoneNumber = req.getParameter("phone_number");

        patientService.createPatient(resp, fullname, email, password, bloodGroup, phoneNumber);
    }

    // Handle Create Appointment Request
    private void handleCreateAppointment(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int userId = Integer.parseInt(req.getParameter("user_id"));
        String date = req.getParameter("date");
        String reason = req.getParameter("reason");
        String time = req.getParameter("time");

        appointmentService.createAppointment(resp, userId, date, reason, time);
    }

    // Utility Method to Extract ID from Path
    private int extractIdFromPath(String path) {
        return extractIdFromPath(path, 2); // Default to 2nd segment for basic paths
    }

    private int extractIdFromPath(String path, int segmentIndex) {
        String[] segments = path.split("/");
        try {
            return Integer.parseInt(segments[segmentIndex]);
        } catch (NumberFormatException e) {
            return -1; // Invalid ID
        }
    }

    // Add CORS Headers
    private void addCorsHeaders(HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Origin", "* ");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");
    }

    // Output Response as JSON
    private void outputResponse(HttpServletResponse response, String payload, int status) {
        response.setHeader("Content-Type", "application/json");
        try {
            response.setStatus(status);
            byte[] bytes = payload.getBytes(StandardCharsets.UTF_8);
            response.setContentLength(bytes.length);
            response.getOutputStream().write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error writing response", e);
        }
    }
}
