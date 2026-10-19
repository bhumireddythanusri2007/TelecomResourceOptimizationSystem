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
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

@WebServlet("/traffic-flows")
public class TrafficFlowServlet extends HttpServlet {

    private String escape(String value) {
        if (value == null) return "";
        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    private String param(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        return value == null ? "" : value.trim();
    }

    private int parseInt(String value, String field)
            throws IllegalArgumentException {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    field + " must be a whole number.");
        }
    }

    private double parseDouble(String value, String field)
            throws IllegalArgumentException {
        try {
            double number = Double.parseDouble(value);
            if (!Double.isFinite(number)) {
                throw new NumberFormatException();
            }
            return number;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    field + " must be a valid number.");
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String action = param(request, "action");
        String message;

        try (Connection connection = DatabaseConnection.getConnection()) {

            if ("add".equals(action)) {

                String sql =
                        "INSERT INTO traffic_flows " +
                        "(cell_id, source_ip, destination_ip, protocol, " +
                        "data_volume, flow_duration, packet_count, flow_status) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

                try (PreparedStatement ps =
                             connection.prepareStatement(sql)) {

                    ps.setInt(1, parseInt(param(request, "cell_id"), "Cell ID"));
                    ps.setString(2, param(request, "source_ip"));
                    ps.setString(3, param(request, "destination_ip"));
                    ps.setString(4, param(request, "protocol"));
                    ps.setDouble(5, parseDouble(
                            param(request, "data_volume"), "Data volume"));
                    ps.setDouble(6, parseDouble(
                            param(request, "flow_duration"), "Flow duration"));
                    ps.setInt(7, parseInt(
                            param(request, "packet_count"), "Packet count"));
                    ps.setString(8, param(request, "flow_status"));

                    if (ps.executeUpdate() > 0) {
                        message = "Traffic flow added successfully.";
                    } else {
                        message = "Traffic flow was not added.";
                    }
                }

            } else if ("update".equals(action)) {

                String sql =
                        "UPDATE traffic_flows SET cell_id=?, source_ip=?, " +
                        "destination_ip=?, protocol=?, data_volume=?, " +
                        "flow_duration=?, packet_count=?, flow_status=? " +
                        "WHERE flow_id=?";

                try (PreparedStatement ps =
                             connection.prepareStatement(sql)) {

                    ps.setInt(1, parseInt(param(request, "cell_id"), "Cell ID"));
                    ps.setString(2, param(request, "source_ip"));
                    ps.setString(3, param(request, "destination_ip"));
                    ps.setString(4, param(request, "protocol"));
                    ps.setDouble(5, parseDouble(
                            param(request, "data_volume"), "Data volume"));
                    ps.setDouble(6, parseDouble(
                            param(request, "flow_duration"), "Flow duration"));
                    ps.setInt(7, parseInt(
                            param(request, "packet_count"), "Packet count"));
                    ps.setString(8, param(request, "flow_status"));
                    ps.setInt(9, parseInt(param(request, "flow_id"), "Flow ID"));

                    int updated = ps.executeUpdate();
                    message = updated > 0
                            ? "Traffic flow updated successfully."
                            : "Traffic flow ID was not found.";
                }

            } else if ("delete".equals(action)) {

                String sql =
                        "DELETE FROM traffic_flows WHERE flow_id=?";

                try (PreparedStatement ps =
                             connection.prepareStatement(sql)) {

                    ps.setInt(1, parseInt(
                            param(request, "flow_id"), "Flow ID"));

                    int deleted = ps.executeUpdate();
                    message = deleted > 0
                            ? "Traffic flow deleted successfully."
                            : "Traffic flow ID was not found.";
                }

            } else {
                message = "Invalid action.";
            }

        } catch (IllegalArgumentException e) {
            message = e.getMessage();
        } catch (SQLException e) {
            getServletContext().log("Traffic flow database operation failed.", e);
            message = "Database operation failed. Check the entered values and try again.";
        } catch (Exception e) {
            getServletContext().log("Traffic flow operation failed.", e);
            message = "An unexpected error occurred.";
        }

        response.sendRedirect(
                request.getContextPath() + "/traffic-flows?message=" +
                java.net.URLEncoder.encode(
                        message, java.nio.charset.StandardCharsets.UTF_8));
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html><html><head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<title>Traffic Flow Management</title>");

        out.println("<style>");
        out.println("*{box-sizing:border-box}");
        out.println("body{margin:0;font-family:Arial,sans-serif;background:#f5f7fb;color:#1f2937}");
        out.println(".header{background:#172554;color:white;padding:25px 40px}");
        out.println(".header h1{margin:0;font-size:28px}");
        out.println(".header p{margin:8px 0 0;color:#cbd5e1}");
        out.println(".container{padding:30px 40px;max-width:1600px;margin:auto}");
        out.println(".back{display:inline-block;margin-bottom:20px;text-decoration:none;color:#1d4ed8;font-weight:bold}");
        out.println(".panel{background:white;padding:22px;margin-bottom:25px;border-radius:10px;box-shadow:0 3px 12px rgba(0,0,0,.07)}");
        out.println(".form-grid{display:grid;grid-template-columns:repeat(4,minmax(150px,1fr));gap:15px}");
        out.println("label{display:block;font-weight:bold;font-size:13px;margin-bottom:7px}");
        out.println("input,select{width:100%;padding:10px;border:1px solid #cbd5e1;border-radius:6px;font-size:14px}");
        out.println("button{border:0;border-radius:6px;padding:10px 15px;cursor:pointer;font-weight:bold}");
        out.println(".add{background:#2563eb;color:white;margin-top:18px}");
        out.println(".save{background:#15803d;color:white}");
        out.println(".cancel{background:#64748b;color:white}");
        out.println(".edit{background:#dbeafe;color:#1d4ed8}");
        out.println(".delete{background:#fee2e2;color:#b91c1c}");
        out.println(".table-wrap{overflow-x:auto;background:white;border-radius:10px;box-shadow:0 3px 12px rgba(0,0,0,.07)}");
        out.println("table{width:100%;border-collapse:collapse;white-space:nowrap}");
        out.println("th{background:#172554;color:white;padding:13px;text-align:left}");
        out.println("td{padding:12px;border-bottom:1px solid #e5e7eb}");
        out.println("tr:hover{background:#f8fafc}");
        out.println(".actions{display:flex;gap:7px}");
        out.println(".message{padding:12px 15px;background:#dcfce7;color:#166534;border-radius:7px;margin-bottom:18px}");
        out.println(".error{padding:12px 15px;background:#fee2e2;color:#991b1b;border-radius:7px;margin-bottom:18px}");
        out.println(".total{font-weight:bold;margin:18px 0}");
        out.println("@media(max-width:900px){.form-grid{grid-template-columns:repeat(2,minmax(120px,1fr))}.container{padding:20px}.header{padding:22px}}");
        out.println("@media(max-width:500px){.form-grid{grid-template-columns:1fr}}");
        out.println("</style></head><body>");

        out.println("<div class='header'><h1>Traffic Flow Management</h1>");
        out.println("<p>Add, view, update and delete telecom network traffic flows</p></div>");
        out.println("<div class='container'>");
        out.println("<a class='back' href='" + request.getContextPath() +
                "/dashboard'>← Back to Dashboard</a>");

        String message = request.getParameter("message");
        if (message != null && !message.isBlank()) {
            out.println("<div class='message'>" + escape(message) + "</div>");
        }

        String editId = param(request, "edit");
        String cellId = "";
        String sourceIp = "";
        String destinationIp = "";
        String protocol = "TCP";
        String dataVolume = "";
        String flowDuration = "";
        String packetCount = "";
        String flowStatus = "Active";
        boolean editing = false;

        if (!editId.isBlank()) {
            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement ps = connection.prepareStatement(
                         "SELECT * FROM traffic_flows WHERE flow_id=?")) {
                ps.setInt(1, parseInt(editId, "Flow ID"));
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        editing = true;
                        cellId = rs.getString("cell_id");
                        sourceIp = rs.getString("source_ip");
                        destinationIp = rs.getString("destination_ip");
                        protocol = rs.getString("protocol");
                        dataVolume = rs.getString("data_volume");
                        flowDuration = rs.getString("flow_duration");
                        packetCount = rs.getString("packet_count");
                        flowStatus = rs.getString("flow_status");
                    }
                }
            } catch (Exception e) {
                out.println("<div class='error'>Unable to load the selected flow.</div>");
            }
        }

        out.println("<div class='panel'><h2>" +
                (editing ? "Edit Traffic Flow" : "Add New Traffic Flow") +
                "</h2>");

        out.println("<form method='post' action='" +
                request.getContextPath() + "/traffic-flows'>");
        out.println("<input type='hidden' name='action' value='" +
                (editing ? "update" : "add") + "'>");

        if (editing) {
            out.println("<input type='hidden' name='flow_id' value='" +
                    escape(editId) + "'>");
        }

        out.println("<div class='form-grid'>");
        field(out, "Cell ID", "cell_id", cellId, "number", true);
        field(out, "Source IP", "source_ip", sourceIp, "text", true);
        field(out, "Destination IP", "destination_ip", destinationIp, "text", true);

        out.println("<div><label for='protocol'>Protocol</label>");
        out.println("<select id='protocol' name='protocol' required>");
        option(out, "TCP", protocol);
        option(out, "UDP", protocol);
        option(out, "HTTP", protocol);
        option(out, "MQTT", protocol);
        option(out, "HTTPS", protocol);
        option(out, "Other", protocol);
        out.println("</select></div>");

        field(out, "Data Volume", "data_volume", dataVolume, "number", true);
        field(out, "Flow Duration", "flow_duration", flowDuration, "number", true);
        field(out, "Packet Count", "packet_count", packetCount, "number", true);

        out.println("<div><label for='flow_status'>Flow Status</label>");
        out.println("<select id='flow_status' name='flow_status' required>");
        option(out, "Active", flowStatus);
        option(out, "Completed", flowStatus);
        option(out, "Failed", flowStatus);
        out.println("</select></div></div>");

        out.println("<button class='add' type='submit'>" +
                (editing ? "Save Changes" : "Add Traffic Flow") + "</button> ");

        if (editing) {
            out.println("<a href='" + request.getContextPath() +
                    "/traffic-flows'><button class='cancel' type='button'>Cancel</button></a>");
        }
        out.println("</form></div>");

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "SELECT * FROM traffic_flows ORDER BY flow_id");
             ResultSet rs = ps.executeQuery()) {

            ResultSetMetaData md = rs.getMetaData();
            int columns = md.getColumnCount();

            out.println("<div class='table-wrap'><table><thead><tr>");
            for (int i = 1; i <= columns; i++) {
                out.println("<th>" + escape(md.getColumnName(i)) + "</th>");
            }
            out.println("<th>Actions</th></tr></thead><tbody>");

            int count = 0;
            while (rs.next()) {
                out.println("<tr>");
                for (int i = 1; i <= columns; i++) {
                    Object value = rs.getObject(i);
                    out.println("<td>" + escape(value == null ? "" : value.toString()) + "</td>");
                }

                String id = rs.getString("flow_id");
                out.println("<td><div class='actions'>");
                out.println("<form method='get' action='" +
                        request.getContextPath() + "/traffic-flows'>");
                out.println("<input type='hidden' name='edit' value='" +
                        escape(id) + "'>");
                out.println("<button class='edit' type='submit'>Edit</button></form>");

                out.println("<form method='post' action='" +
                        request.getContextPath() + "/traffic-flows' " +
                        "onsubmit=\"return confirm('Delete traffic flow " +
                        escape(id) + "?');\">");
                out.println("<input type='hidden' name='action' value='delete'>");
                out.println("<input type='hidden' name='flow_id' value='" +
                        escape(id) + "'>");
                out.println("<button class='delete' type='submit'>Delete</button>");
                out.println("</form></div></td></tr>");
                count++;
            }

            out.println("</tbody></table></div>");
            out.println("<p class='total'>Total Traffic Flows: " + count + "</p>");

        } catch (Exception e) {
            getServletContext().log("Unable to load traffic flows.", e);
            out.println("<div class='error'>Unable to load traffic flow records. Check the server log.</div>");
        }

        out.println("</div></body></html>");
    }

    private void field(
            PrintWriter out,
            String label,
            String name,
            String value,
            String type,
            boolean required) {

        out.println("<div><label for='" + name + "'>" + label + "</label>");
        out.println("<input id='" + name + "' name='" + name +
                "' type='" + type + "' value='" + escape(value) +
                "' " + (required ? "required" : "") +
                (("data_volume".equals(name) || "flow_duration".equals(name))
                        ? " min='0' step='any'"
                        : ("cell_id".equals(name) || "packet_count".equals(name)
                                ? " min='0' step='1'" : "")) +
                "></div>");
    }

    private void option(PrintWriter out, String value, String selected) {
        out.println("<option value='" + escape(value) + "' " +
                (value.equalsIgnoreCase(selected == null ? "" : selected)
                        ? "selected" : "") +
                ">" + escape(value) + "</option>");
    }
}
