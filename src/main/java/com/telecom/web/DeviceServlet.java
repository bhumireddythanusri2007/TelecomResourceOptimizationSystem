package com.telecom.web;

import com.telecom.util.DatabaseConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;

@WebServlet("/devices")
public class DeviceServlet extends HttpServlet {

    private String escape(String value) {
        if (value == null) return "";
        return value.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#39;");
    }

    private String value(HttpServletRequest request, String name) {
        String v = request.getParameter(name);
        return v == null ? "" : v.trim();
    }

    private int parseId(String text) {
        try {
            int id = Integer.parseInt(text);
            return id > 0 ? id : -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String action = value(request, "action");
        String idText = value(request, "device_id");

        try (Connection con = DatabaseConnection.getConnection()) {

            if ("delete".equals(action)) {
                int id = parseId(idText);

                if (id < 1) {
                    response.sendError(400, "Invalid device ID.");
                    return;
                }

                try (PreparedStatement ps = con.prepareStatement(
                        "DELETE FROM devices WHERE device_id = ?")) {
                    ps.setInt(1, id);
                    ps.executeUpdate();
                }

                response.sendRedirect(request.getContextPath() + "/devices");
                return;
            }

            if (!"add".equals(action) && !"update".equals(action)) {
                response.sendError(400, "Unknown action.");
                return;
            }

            String name = value(request, "device_name");
            String type = value(request, "device_type");
            String ip = value(request, "ip_address");
            String connectionType = value(request, "connection_type");
            String status = value(request, "status");

            // Read the raw optional value separately.
            String rawUserId = request.getParameter("user_id");
            Integer userId = null;

            if (rawUserId != null && !rawUserId.trim().isEmpty()) {
                try {
                    userId = Integer.valueOf(rawUserId.trim());

                    if (userId <= 0) {
                        response.sendError(400, "User ID must be positive.");
                        return;
                    }
                } catch (NumberFormatException e) {
                    response.sendError(
                            400, "Enter a whole number for User ID, or leave it blank.");
                    return;
                }
            }

            if (name.isEmpty()) {
                response.sendError(400, "Device name is required.");
                return;
            }

            if (!status.equals("Active")
                    && !status.equals("Inactive")
                    && !status.equals("Maintenance")) {
                response.sendError(400, "Invalid device status.");
                return;
            }

            if ("add".equals(action)) {
                String sql = "INSERT INTO devices "
                        + "(device_name, device_type, ip_address, "
                        + "connection_type, status, user_id) "
                        + "VALUES (?, ?, ?, ?, ?, ?)";

                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setString(1, name);
                    ps.setString(2, type.isEmpty() ? null : type);
                    ps.setString(3, ip.isEmpty() ? null : ip);
                    ps.setString(4, connectionType.isEmpty()
                            ? null : connectionType);
                    ps.setString(5, status);

                    if (userId == null) {
                        ps.setNull(6, Types.INTEGER);
                    } else {
                        ps.setInt(6, userId);
                    }

                    ps.executeUpdate();
                }

            } else {
                int id = parseId(idText);

                if (id < 1) {
                    response.sendError(400, "Invalid device ID.");
                    return;
                }

                String sql = "UPDATE devices SET device_name = ?, "
                        + "device_type = ?, ip_address = ?, "
                        + "connection_type = ?, status = ?, user_id = ? "
                        + "WHERE device_id = ?";

                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setString(1, name);
                    ps.setString(2, type.isEmpty() ? null : type);
                    ps.setString(3, ip.isEmpty() ? null : ip);
                    ps.setString(4, connectionType.isEmpty()
                            ? null : connectionType);
                    ps.setString(5, status);

                    if (userId == null) {
                        ps.setNull(6, Types.INTEGER);
                    } else {
                        ps.setInt(6, userId);
                    }

                    ps.setInt(7, id);
                    ps.executeUpdate();
                }
            }

        } catch (java.sql.SQLException e) {
            throw new ServletException("Unable to save device.", e);
        }

        response.sendRedirect(request.getContextPath() + "/devices");
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        String editText = value(request, "edit");
        int editId = editText.isEmpty() ? -1 : parseId(editText);
        boolean editing = editId > 0;

        String deviceName = "";
        String deviceType = "";
        String ipAddress = "";
        String connectionType = "";
        String status = "Active";
        String userId = "";

        try (Connection con = DatabaseConnection.getConnection()) {

            if (editing) {
                String sql = "SELECT * FROM devices WHERE device_id = ?";

                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setInt(1, editId);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            response.sendError(404, "Device not found.");
                            return;
                        }

                        deviceName = rs.getString("device_name");
                        deviceType = rs.getString("device_type");
                        ipAddress = rs.getString("ip_address");
                        connectionType = rs.getString("connection_type");
                        status = rs.getString("status");

                        Object uid = rs.getObject("user_id");
                        userId = uid == null ? "" : uid.toString();

                        if (status == null || status.isBlank()) {
                            status = "Active";
                        }
                    }
                }
            }

            PrintWriter out = response.getWriter();

            out.println("<!DOCTYPE html><html><head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1'>");
            out.println("<title>Device Management</title>");

            out.println("<style>");
            out.println("*{box-sizing:border-box}");
            out.println("body{margin:0;font-family:Arial,sans-serif;background:#f1f5f9;color:#1e293b}");
            out.println(".header{background:#192953;color:white;padding:30px}");
            out.println(".header h1{margin:0 0 10px;font-size:34px}");
            out.println(".header p{margin:0;color:#dbeafe;font-size:18px}");
            out.println(".container{padding:32px 16px;max-width:1400px;margin:auto}");
            out.println(".back{display:inline-block;margin:8px 0 28px;color:#2563eb;text-decoration:none;font-weight:bold;font-size:18px}");
            out.println(".card{background:white;padding:30px;border-radius:16px;margin-bottom:28px;box-shadow:0 4px 14px rgba(15,23,42,.07)}");
            out.println("h2{margin:0 0 24px;font-size:26px}");
            out.println(".form{display:grid;grid-template-columns:repeat(auto-fit,minmax(220px,1fr));gap:20px}");
            out.println(".form label{display:flex;flex-direction:column;gap:9px;min-width:0;font-weight:600;font-size:15px;color:#334155}");
            out.println(".form input,.form select{display:block;width:100%;min-width:0;padding:14px;border:1px solid #cbd5e1;border-radius:7px;font-size:15px;background:white;color:#1e293b}");
            out.println(".btn{display:inline-block;border:0;border-radius:7px;padding:13px 20px;background:#2563eb;color:white;font-weight:bold;font-size:15px;cursor:pointer;text-decoration:none}");
            out.println(".btn:hover{background:#1d4ed8}");
            out.println(".cancel{background:#64748b;margin-left:8px}");
            out.println(".table-wrap{overflow-x:auto}");
            out.println("table{width:100%;border-collapse:collapse;min-width:850px}");
            out.println("th{background:#192953;color:white;text-align:left;padding:15px}");
            out.println("td{padding:14px;border-bottom:1px solid #e2e8f0}");
            out.println("tr:hover td{background:#f8fafc}");
            out.println(".action{color:#2563eb;text-decoration:none;font-weight:bold;margin-right:12px}");
            out.println(".delete{display:inline;background:none;border:0;color:#dc2626;font-weight:bold;cursor:pointer;font-size:14px;padding:0}");
            out.println(".total{font-weight:bold;margin-top:20px}");
            out.println("@media(max-width:600px){.header{padding:24px 20px}.header h1{font-size:27px}.container{padding:22px 12px}.card{padding:20px}.form{grid-template-columns:1fr}}");
            out.println("</style></head><body>");

            out.println("<div class='header'><h1>Device Management</h1>");
            out.println("<p>Add, view, edit and delete telecom network devices</p></div>");

            out.println("<main class='container'>");
            out.println("<a class='back' href='" + request.getContextPath()
                    + "/dashboard'>&larr; Back to Dashboard</a>");

            out.println("<section class='card'><h2>"
                    + (editing ? "Edit Network Device" : "Add Network Device")
                    + "</h2>");

            out.println("<form method='post' action='"
                    + request.getContextPath() + "/devices'>");

            out.println("<input type='hidden' name='action' value='"
                    + (editing ? "update" : "add") + "'>");

            if (editing) {
                out.println("<input type='hidden' name='device_id' value='"
                        + editId + "'>");
            }

            out.println("<div class='form'>");

            field(out, "Device Name", "device_name", deviceName, "text", true);
            field(out, "Device Type", "device_type", deviceType, "text", false);
            field(out, "IP Address", "ip_address", ipAddress, "text", false);
            field(out, "Connection Type", "connection_type", connectionType, "text", false);

            // Numeric input, but deliberately not required.
            field(out, "User ID (optional)", "user_id", userId, "number", false);

            out.println("<label>Status<select name='status' required>");
            option(out, "Active", status);
            option(out, "Inactive", status);
            option(out, "Maintenance", status);
            out.println("</select></label>");

            out.println("</div><p style='margin-top:24px'>");
            out.println("<button class='btn' type='submit'>"
                    + (editing ? "Update Device" : "Add Device")
                    + "</button>");

            if (editing) {
                out.println("<a class='btn cancel' href='"
                        + request.getContextPath() + "/devices'>Cancel</a>");
            }

            out.println("</p></form></section>");

            out.println("<section class='card'><h2>Registered Devices</h2>");
            out.println("<div class='table-wrap'><table><thead><tr>");
            out.println("<th>ID</th><th>Device Name</th><th>Device Type</th>");
            out.println("<th>IP Address</th><th>Connection Type</th>");
            out.println("<th>Status</th><th>User ID</th><th>Actions</th>");
            out.println("</tr></thead><tbody>");

            int count = 0;

            String sql = "SELECT device_id, device_name, device_type, "
                    + "ip_address, connection_type, status, user_id "
                    + "FROM devices ORDER BY device_id";

            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery(sql)) {

                while (rs.next()) {
                    int id = rs.getInt("device_id");

                    out.println("<tr>");
                    cell(out, String.valueOf(id));
                    cell(out, rs.getString("device_name"));
                    cell(out, rs.getString("device_type"));
                    cell(out, rs.getString("ip_address"));
                    cell(out, rs.getString("connection_type"));
                    cell(out, rs.getString("status"));

                    Object uid = rs.getObject("user_id");
                    cell(out, uid == null ? "" : uid.toString());

                    out.println("<td><a class='action' href='"
                            + request.getContextPath() + "/devices?edit="
                            + id + "'>Edit</a>");

                    out.println("<form method='post' action='"
                            + request.getContextPath()
                            + "/devices' style='display:inline' "
                            + "onsubmit=\"return confirm('Delete this device?');\">");

                    out.println("<input type='hidden' name='action' value='delete'>");
                    out.println("<input type='hidden' name='device_id' value='"
                            + id + "'>");
                    out.println("<button class='delete' type='submit'>Delete</button>");
                    out.println("</form></td></tr>");

                    count++;
                }
            }

            out.println("</tbody></table></div>");
            out.println("<p class='total'>Total Devices: " + count + "</p>");
            out.println("</section></main></body></html>");

        } catch (java.sql.SQLException e) {
            throw new ServletException("Unable to load device records.", e);
        }
    }

    private void field(
            PrintWriter out,
            String label,
            String name,
            String fieldValue,
            String type,
            boolean required) {

        out.println("<label>" + label
                + "<input type='" + type
                + "' name='" + name
                + "' value='" + escape(fieldValue) + "'"
                + (type.equals("number") ? " min='1' step='1'" : "")
                + (required ? " required" : "")
                + "></label>");
    }

    private void option(PrintWriter out, String option, String selected) {
        out.println("<option value='" + option + "'"
                + (option.equals(selected) ? " selected" : "")
                + ">" + option + "</option>");
    }

    private void cell(PrintWriter out, String value) {
        out.println("<td>" + escape(value) + "</td>");
    }
}
