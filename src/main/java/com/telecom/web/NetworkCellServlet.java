
package com.telecom.web;

import com.telecom.util.DatabaseConnection;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/network-cells")
public class NetworkCellServlet extends HttpServlet {

    private String esc(String value) {
        if (value == null) return "";

        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    private int parseNumber(String value, String field)
            throws Exception {
        try {
            int number = Integer.parseInt(value);

            if (number < 0) {
                throw new Exception(field + " cannot be negative.");
            }

            return number;
        } catch (NumberFormatException e) {
            throw new Exception(
                    field + " must be a valid whole number.");
        }
    }

    private String requiredParameter(
            HttpServletRequest request, String name, String label)
            throws Exception {

        String value = request.getParameter(name);

        if (value == null || value.trim().isEmpty()) {
            throw new Exception(label + " is required.");
        }

        return value.trim();
    }

    private void redirectWithMessage(
            HttpServletRequest request,
            HttpServletResponse response,
            String message) throws IOException {

        String encoded = URLEncoder.encode(
                message, StandardCharsets.UTF_8);

        response.sendRedirect(
                request.getContextPath()
                        + "/network-cells?message=" + encoded);
    }

    private String normalizeStatus(String status) throws Exception {
        if (status == null) return "Active";

        switch (status.trim().toLowerCase()) {
            case "active":
                return "Active";
            case "inactive":
                return "Inactive";
            case "maintenance":
                return "Maintenance";
            default:
                throw new Exception("Select a valid cell status.");
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        String message;

        try (Connection connection =
                     DatabaseConnection.getConnection()) {

            if ("add".equals(action)) {
                String name = requiredParameter(
                        request, "cell_name", "Cell name");

                if (name.length() > 100) {
                    throw new Exception(
                            "Cell name cannot exceed 100 characters.");
                }

                String location = request.getParameter("location");
                if (location != null) location = location.trim();

                if (location != null && location.length() > 100) {
                    throw new Exception(
                            "Location cannot exceed 100 characters.");
                }

                int capacity = parseNumber(
                        request.getParameter("total_capacity"),
                        "Total capacity");

                int users = parseNumber(
                        request.getParameter("current_users"),
                        "Current users");

                if (users > capacity) {
                    throw new Exception(
                            "Current users cannot exceed total capacity.");
                }

                String status = normalizeStatus(
                        request.getParameter("status"));

                String sql = "INSERT INTO network_cells "
                        + "(cell_name, location, total_capacity, "
                        + "current_users, status) VALUES (?, ?, ?, ?, ?)";

                try (PreparedStatement ps =
                             connection.prepareStatement(sql)) {
                    ps.setString(1, name);
                    ps.setString(2,
                            location == null || location.isBlank()
                                    ? null : location);
                    ps.setInt(3, capacity);
                    ps.setInt(4, users);
                    ps.setString(5, status);
                    ps.executeUpdate();
                }

                message = "Network cell added successfully.";

            } else if ("update".equals(action)) {
                int id = parseNumber(
                        request.getParameter("cell_id"), "Cell ID");

                if (id == 0) {
                    throw new Exception("Invalid cell ID.");
                }

                String name = requiredParameter(
                        request, "cell_name", "Cell name");

                if (name.length() > 100) {
                    throw new Exception(
                            "Cell name cannot exceed 100 characters.");
                }

                String location = request.getParameter("location");
                if (location != null) location = location.trim();

                if (location != null && location.length() > 100) {
                    throw new Exception(
                            "Location cannot exceed 100 characters.");
                }

                int capacity = parseNumber(
                        request.getParameter("total_capacity"),
                        "Total capacity");

                int users = parseNumber(
                        request.getParameter("current_users"),
                        "Current users");

                if (users > capacity) {
                    throw new Exception(
                            "Current users cannot exceed total capacity.");
                }

                String status = normalizeStatus(
                        request.getParameter("status"));

                String sql = "UPDATE network_cells SET "
                        + "cell_name=?, location=?, total_capacity=?, "
                        + "current_users=?, status=? WHERE cell_id=?";

                try (PreparedStatement ps =
                             connection.prepareStatement(sql)) {
                    ps.setString(1, name);
                    ps.setString(2,
                            location == null || location.isBlank()
                                    ? null : location);
                    ps.setInt(3, capacity);
                    ps.setInt(4, users);
                    ps.setString(5, status);
                    ps.setInt(6, id);

                    int rows = ps.executeUpdate();
                    message = rows > 0
                            ? "Network cell updated successfully."
                            : "Network cell not found.";
                }

            } else if ("delete".equals(action)) {
                int id = parseNumber(
                        request.getParameter("cell_id"), "Cell ID");

                if (id == 0) {
                    throw new Exception("Invalid cell ID.");
                }

                try (PreparedStatement ps =
                             connection.prepareStatement(
                                     "DELETE FROM network_cells "
                                             + "WHERE cell_id=?")) {
                    ps.setInt(1, id);

                    int rows = ps.executeUpdate();
                    message = rows > 0
                            ? "Network cell deleted successfully."
                            : "Network cell not found.";
                }

            } else {
                throw new Exception("Invalid operation.");
            }

        } catch (Exception e) {
            message = "Operation failed: " + e.getMessage();
        }

        redirectWithMessage(request, response, message);
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String editId = request.getParameter("edit");
        String message = request.getParameter("message");

        String cellName = "";
        String location = "";
        String capacity = "";
        String users = "";
        String status = "Active";
        boolean editing = false;

        if (editId != null && !editId.isBlank()) {
            try {
                int id = Integer.parseInt(editId);

                if (id <= 0) {
                    throw new NumberFormatException();
                }

                try (Connection connection =
                             DatabaseConnection.getConnection();
                     PreparedStatement ps =
                             connection.prepareStatement(
                                     "SELECT * FROM network_cells "
                                             + "WHERE cell_id=?")) {

                    ps.setInt(1, id);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            editing = true;
                            cellName = rs.getString("cell_name");
                            location = rs.getString("location");

                            Object cap = rs.getObject("total_capacity");
                            Object current = rs.getObject("current_users");

                            capacity = cap == null ? "" : cap.toString();
                            users = current == null
                                    ? "" : current.toString();

                            String dbStatus = rs.getString("status");
                            status = dbStatus == null
                                    ? "Active" : dbStatus;
                        } else {
                            message = "Network cell not found.";
                        }
                    }
                }

            } catch (NumberFormatException e) {
                message = "Invalid network cell ID.";
            } catch (SQLException e) {
                message = "Unable to load cell for editing.";
            }
        }

        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html><html lang='en'><head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1'>");
        out.println("<title>Network Cell Management</title>");

        out.println("<style>");
        out.println("*{box-sizing:border-box}");
        out.println("body{margin:0;font-family:Arial,sans-serif;background:#f1f5f9;color:#1e293b;line-height:1.5}");
        out.println(".header{background:#172554;color:white;padding:25px 35px}");
        out.println(".header h1{margin:0;font-size:28px}");
        out.println(".header p{margin:8px 0 0;color:#dbeafe}");
        out.println(".container{max-width:1200px;margin:auto;padding:25px}");
        out.println(".card{background:white;padding:24px;margin:20px 0;border-radius:12px;box-shadow:0 3px 12px #0001}");
        out.println(".card h2{margin:0 0 22px;font-size:21px}");
        out.println(".back{display:inline-block;margin:5px 0 15px;color:#1d4ed8;text-decoration:none;font-weight:bold}");
        out.println(".back:hover{text-decoration:underline}");

        // Improved form layout
        out.println(".form{display:grid;grid-template-columns:repeat(auto-fit,minmax(220px,1fr));gap:20px}");
        out.println(".form label{display:flex;flex-direction:column;gap:8px;min-width:0;font-weight:600;font-size:14px;color:#334155}");
        out.println(".form input,.form select{display:block;width:100%;min-width:0;padding:12px;margin:0;border:1px solid #cbd5e1;border-radius:6px;font-size:14px;background:white;color:#1e293b}");
        out.println(".form input:focus,.form select:focus{outline:2px solid #93c5fd;border-color:#2563eb}");
        out.println(".form-actions{display:flex;gap:10px;flex-wrap:wrap;margin-top:22px}");

        out.println("button,.btn{display:inline-block;padding:10px 15px;border:0;border-radius:6px;background:#2563eb;color:white;text-decoration:none;cursor:pointer;font-size:14px;font-weight:600}");
        out.println("button:hover,.btn:hover{filter:brightness(.93)}");
        out.println(".delete{background:#dc2626}.cancel{background:#64748b}");
        out.println(".tablewrap{overflow-x:auto}");
        out.println("table{width:100%;border-collapse:collapse;background:white}");
        out.println("th{background:#172554;color:white}");
        out.println("th,td{padding:12px;text-align:left;border-bottom:1px solid #e2e8f0;white-space:nowrap}");
        out.println("tr:hover{background:#f8fafc}");
        out.println(".message{padding:13px 16px;margin:12px 0 20px;border-radius:7px;background:#dcfce7;color:#166534;overflow-wrap:anywhere}");
        out.println(".error{background:#fee2e2;color:#991b1b}");
        out.println(".actions{display:flex;align-items:center;gap:8px}");
        out.println(".actions form{margin:0}");
        out.println(".total{margin-top:18px;color:#334155}");
        out.println("@media(max-width:600px){.header{padding:22px}.container{padding:14px}.card{padding:17px}.form{grid-template-columns:1fr}}");
        out.println("</style></head><body>");

        out.println("<div class='header'>");
        out.println("<h1>Network Cell Management</h1>");
        out.println("<p>Add, view, edit and delete telecom network cells</p>");
        out.println("</div>");

        out.println("<div class='container'>");
        out.println("<a class='back' href='"
                + request.getContextPath()
                + "/dashboard'>← Back to Dashboard</a>");

        if (message != null && !message.isBlank()) {
            boolean error = message.startsWith("Operation failed")
                    || message.startsWith("Unable")
                    || message.startsWith("Invalid")
                    || message.startsWith("Network cell not found");

            out.println("<div class='message "
                    + (error ? "error" : "") + "'>"
                    + esc(message) + "</div>");
        }

        out.println("<div class='card'><h2>"
                + (editing ? "Edit Network Cell" : "Add Network Cell")
                + "</h2>");

        out.println("<form method='post' action='"
                + request.getContextPath() + "/network-cells'>");

        out.println("<input type='hidden' name='action' value='"
                + (editing ? "update" : "add") + "'>");

        if (editing) {
            out.println("<input type='hidden' name='cell_id' value='"
                    + esc(editId) + "'>");
        }

        out.println("<div class='form'>");

        out.println("<label>Cell Name"
                + "<input type='text' name='cell_name' maxlength='100' required value='"
                + esc(cellName) + "'></label>");

        out.println("<label>Location"
                + "<input type='text' name='location' maxlength='100' value='"
                + esc(location) + "'></label>");

        out.println("<label>Total Capacity"
                + "<input type='number' name='total_capacity' min='0' required value='"
                + esc(capacity) + "'></label>");

        out.println("<label>Current Users"
                + "<input type='number' name='current_users' min='0' required value='"
                + esc(users) + "'></label>");

        out.println("<label>Status<select name='status'>");

        for (String option :
                new String[]{"Active", "Inactive", "Maintenance"}) {
            out.println("<option value='" + option + "' "
                    + (option.equalsIgnoreCase(status)
                    ? "selected" : "") + ">"
                    + option + "</option>");
        }

        out.println("</select></label>");
        out.println("</div>");

        out.println("<div class='form-actions'>");
        out.println("<button type='submit'>"
                + (editing ? "Update Cell" : "Add Cell")
                + "</button>");

        if (editing) {
            out.println("<a class='btn cancel' href='"
                    + request.getContextPath()
                    + "/network-cells'>Cancel</a>");
        }

        out.println("</div></form></div>");

        out.println("<div class='card'><h2>Registered Network Cells</h2>");
        out.println("<div class='tablewrap'><table>");
        out.println("<thead><tr><th>ID</th><th>Cell Name</th>"
                + "<th>Location</th><th>Total Capacity</th>"
                + "<th>Current Users</th><th>Status</th>"
                + "<th>Actions</th></tr></thead><tbody>");

        int count = 0;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "SELECT * FROM network_cells ORDER BY cell_id");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("cell_id");

                out.println("<tr>");
                out.println("<td>" + id + "</td>");
                out.println("<td>" + esc(rs.getString("cell_name")) + "</td>");
                out.println("<td>" + esc(rs.getString("location")) + "</td>");
                out.println("<td>" + esc(String.valueOf(
                        rs.getObject("total_capacity"))) + "</td>");
                out.println("<td>" + esc(String.valueOf(
                        rs.getObject("current_users"))) + "</td>");
                out.println("<td>" + esc(rs.getString("status")) + "</td>");
                out.println("<td><div class='actions'>");

                out.println("<a class='btn' href='"
                        + request.getContextPath()
                        + "/network-cells?edit=" + id
                        + "'>Edit</a>");

                out.println("<form method='post' action='"
                        + request.getContextPath()
                        + "/network-cells' "
                        + "onsubmit=\"return confirm('Delete this network cell?')\">");

                out.println("<input type='hidden' name='action' value='delete'>");
                out.println("<input type='hidden' name='cell_id' value='"
                        + id + "'>");
                out.println("<button class='delete' type='submit'>Delete</button>");
                out.println("</form></div></td>");
                out.println("</tr>");

                count++;
            }

        } catch (Exception e) {
            out.println("<tr><td colspan='7'>"
                    + "Unable to load records. Check the database connection."
                    + "</td></tr>");
        }

        out.println("</tbody></table></div>");
        out.println("<p class='total'><strong>Total Network Cells: "
                + count + "</strong></p></div>");

        out.println("</div></body></html>");
    }
}
