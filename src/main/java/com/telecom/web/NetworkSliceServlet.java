
package com.telecom.web;

import com.telecom.util.DatabaseConnection;
import jakarta.servlet.ServletException;
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
import java.sql.Types;

@WebServlet("/network-slices")
public class NetworkSliceServlet extends HttpServlet {

    private String escape(String value) {
        if (value == null) return "";
        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private Integer toInteger(String value) {
        if (value == null || value.isBlank()) return null;
        int number = Integer.parseInt(value.trim());
        if (number < 0) throw new IllegalArgumentException("Values cannot be negative.");
        return number;
    }

    private Double toDouble(String value) {
        if (value == null || value.isBlank()) return null;
        double number = Double.parseDouble(value.trim());
        if (!Double.isFinite(number) || number < 0) {
            throw new IllegalArgumentException("Latency must be a non-negative number.");
        }
        return number;
    }

    private String statusValue(String value) {
        if (value == null || value.isBlank()) return "Active";
        String status = value.trim();
        if (!status.equals("Active") &&
            !status.equals("Inactive") &&
            !status.equals("Maintenance")) {
            throw new IllegalArgumentException("Invalid status.");
        }
        return status;
    }

    private void setNullableInteger(PreparedStatement ps, int index,
                                    Integer value) throws java.sql.SQLException {
        if (value == null) ps.setNull(index, Types.INTEGER);
        else ps.setInt(index, value);
    }

    private void setNullableDouble(PreparedStatement ps, int index,
                                   Double value) throws java.sql.SQLException {
        if (value == null) ps.setNull(index, Types.DOUBLE);
        else ps.setDouble(index, value);
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("""
            <!DOCTYPE html>
            <html lang="en">
            <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1">
            <title>Network Slice Management</title>
            <style>
            body{margin:0;font-family:Arial,sans-serif;background:#f5f7fb;color:#1f2937}
            header{background:#172554;color:white;padding:24px 35px}
            header h1{margin:0}
            main{padding:30px 35px}
            .back{display:inline-block;margin-bottom:20px;color:#1d4ed8;font-weight:bold}
            .panel{background:white;padding:22px;margin-bottom:25px;border-radius:10px;
                   box-shadow:0 3px 12px #0000000d}
            .addform{display:grid;grid-template-columns:repeat(auto-fit,minmax(170px,1fr));
                     gap:14px;align-items:end}
            label{display:block;font-weight:bold;margin-bottom:7px}
            input,select{box-sizing:border-box;width:100%;padding:9px;border:1px solid #cbd5e1;
                         border-radius:5px;background:white}
            button{padding:9px 12px;border:0;border-radius:5px;cursor:pointer;font-weight:bold}
            .addbtn,.save{background:#2563eb;color:white}
            .delete{background:#fee2e2;color:#991b1b}
            .tablewrap{overflow-x:auto}
            table{width:100%;border-collapse:collapse;background:white}
            th{background:#172554;color:white;text-align:left}
            th,td{padding:11px;border-bottom:1px solid #e5e7eb;vertical-align:middle}
            td input,td select{min-width:95px}
            .actions{display:flex;gap:7px;align-items:center}
            .actions form{margin:0}
            .message{padding:12px;margin-bottom:15px;background:#dcfce7;border-radius:5px}
            .error{background:#fee2e2}
            .idform{display:none}
            </style>
            </head>
            <body>
            <header><h1>Network Slice Management</h1>
            <p>Add, view, update and delete network slices</p></header>
            <main><a class="back" href="/dashboard">← Back to Dashboard</a>
            """);

        String message = request.getParameter("message");
        if (message != null) {
            out.println("<div class='message'>" + escape(message) + "</div>");
        }

        out.println("""
            <section class="panel">
            <h2>Add Network Slice</h2>
            <form class="addform" method="post" action="/network-slices">
            <input type="hidden" name="action" value="add">
            <div><label>Slice Name *</label>
            <input name="slice_name" maxlength="100" required></div>
            <div><label>Slice Type</label>
            <input name="slice_type" maxlength="50" placeholder="eMBB, URLLC, mMTC"></div>
            <div><label>Bandwidth</label>
            <input name="bandwidth" type="number" min="0"></div>
            <div><label>Connected Users</label>
            <input name="connected_users" type="number" min="0"></div>
            <div><label>Latency Requirement</label>
            <input name="latency_requirement" type="number" min="0" step="any"></div>
            <div><label>Status</label><select name="status">
            <option value="Active">Active</option>
            <option value="Inactive">Inactive</option>
            <option value="Maintenance">Maintenance</option>
            </select></div>
            <button class="addbtn" type="submit">Add Slice</button>
            </form></section>

            <section class="panel">
            <h2>Existing Network Slices</h2>
            <div class="tablewrap">
            <table><thead><tr>
            <th>ID</th><th>Name</th><th>Type</th><th>Bandwidth</th>
            <th>Connected Users</th><th>Latency</th><th>Status</th><th>Actions</th>
            </tr></thead><tbody>
            """);

        int count = 0;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "SELECT slice_id, slice_name, slice_type, bandwidth, " +
                     "connected_users, latency_requirement, status " +
                     "FROM network_slices ORDER BY slice_id");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                count++;

                int id = rs.getInt("slice_id");
                String name = rs.getString("slice_name");
                String type = rs.getString("slice_type");
                Object bandwidth = rs.getObject("bandwidth");
                Object users = rs.getObject("connected_users");
                Object latency = rs.getObject("latency_requirement");
                String status = rs.getString("status");

                String rowKey = "slice-" + id;

                out.println("<tr>");

                out.println("<td>" + id + "</td>");

                out.println("<td><input form='" + rowKey +
                        "' name='slice_name' maxlength='100' required value='" +
                        escape(name) + "'></td>");

                out.println("<td><input form='" + rowKey +
                        "' name='slice_type' maxlength='50' value='" +
                        escape(type) + "'></td>");

                out.println("<td><input form='" + rowKey +
                        "' name='bandwidth' type='number' min='0' value='" +
                        escape(bandwidth == null ? "" : bandwidth.toString()) + "'></td>");

                out.println("<td><input form='" + rowKey +
                        "' name='connected_users' type='number' min='0' value='" +
                        escape(users == null ? "" : users.toString()) + "'></td>");

                out.println("<td><input form='" + rowKey +
                        "' name='latency_requirement' type='number' min='0' step='any' value='" +
                        escape(latency == null ? "" : latency.toString()) + "'></td>");

                out.println("<td><select form='" + rowKey + "' name='status'>");

                String currentStatus = status == null ? "Active" : status;

                for (String option : new String[]{"Active", "Inactive", "Maintenance"}) {
                    out.println("<option value='" + option + "'" +
                            (option.equalsIgnoreCase(currentStatus) ? " selected" : "") +
                            ">" + option + "</option>");
                }

                out.println("</select></td><td><div class='actions'>");

                // Each row has a separate update form.
                out.println("<form id='" + rowKey +
                        "' method='post' action='/network-slices'>");
                out.println("<input type='hidden' name='action' value='update'>");
                out.println("<input type='hidden' name='slice_id' value='" + id + "'>");
                out.println("</form>");

                out.println("<button class='save' type='submit' form='" +
                        rowKey + "'>Save</button>");

                // Delete uses a separate form, outside the update form.
                out.println("<form method='post' action='/network-slices' " +
                        "onsubmit=\"return confirm('Delete this network slice?')\">");
                out.println("<input type='hidden' name='action' value='delete'>");
                out.println("<input type='hidden' name='slice_id' value='" + id + "'>");
                out.println("<button class='delete' type='submit'>Delete</button>");
                out.println("</form>");

                out.println("</div></td></tr>");
            }

        } catch (Exception e) {
            getServletContext().log("Unable to load network slices", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.println("<tr><td colspan='8'>Unable to load network slices. " +
                    "Check the application log.</td></tr>");
        }

        out.println("</tbody></table></div>");
        out.println("<p><strong>Total Network Slices: " + count + "</strong></p>");
        out.println("</section></main></body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        try (Connection connection = DatabaseConnection.getConnection()) {

            if ("add".equals(action)) {
                String name = request.getParameter("slice_name");

                if (name == null || name.isBlank()) {
                    throw new IllegalArgumentException("Slice name is required.");
                }

                String sql = "INSERT INTO network_slices " +
                        "(slice_name, slice_type, bandwidth, connected_users, " +
                        "latency_requirement, status) VALUES (?, ?, ?, ?, ?, ?)";

                try (PreparedStatement ps = connection.prepareStatement(sql)) {
                    ps.setString(1, name.trim());
                    ps.setString(2, blankToNull(request.getParameter("slice_type")));
                    setNullableInteger(ps, 3, toInteger(request.getParameter("bandwidth")));
                    setNullableInteger(ps, 4, toInteger(request.getParameter("connected_users")));
                    setNullableDouble(ps, 5, toDouble(request.getParameter("latency_requirement")));
                    ps.setString(6, statusValue(request.getParameter("status")));
                    ps.executeUpdate();
                }

            } else if ("update".equals(action)) {
                int id = Integer.parseInt(request.getParameter("slice_id"));
                String name = request.getParameter("slice_name");

                if (name == null || name.isBlank()) {
                    throw new IllegalArgumentException("Slice name is required.");
                }

                String sql = "UPDATE network_slices SET slice_name=?, slice_type=?, " +
                        "bandwidth=?, connected_users=?, latency_requirement=?, status=? " +
                        "WHERE slice_id=?";

                try (PreparedStatement ps = connection.prepareStatement(sql)) {
                    ps.setString(1, name.trim());
                    ps.setString(2, blankToNull(request.getParameter("slice_type")));
                    setNullableInteger(ps, 3, toInteger(request.getParameter("bandwidth")));
                    setNullableInteger(ps, 4, toInteger(request.getParameter("connected_users")));
                    setNullableDouble(ps, 5, toDouble(request.getParameter("latency_requirement")));
                    ps.setString(6, statusValue(request.getParameter("status")));
                    ps.setInt(7, id);
                    ps.executeUpdate();
                }

            } else if ("delete".equals(action)) {
                int id = Integer.parseInt(request.getParameter("slice_id"));

                try (PreparedStatement ps = connection.prepareStatement(
                        "DELETE FROM network_slices WHERE slice_id=?")) {
                    ps.setInt(1, id);
                    ps.executeUpdate();
                }

            } else {
                throw new IllegalArgumentException("Invalid action.");
            }

            String successMessage = "add".equals(action) ? "Network slice added."
                    : "update".equals(action) ? "Network slice updated."
                    : "Network slice deleted.";

            response.sendRedirect(request.getContextPath() +
                    "/network-slices?message=" +
                    URLEncoder.encode(successMessage, StandardCharsets.UTF_8));

        } catch (Exception e) {
            getServletContext().log("Network slice operation failed", e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Unable to complete the operation. Check the entered values and application log.");
        }
    }
}
