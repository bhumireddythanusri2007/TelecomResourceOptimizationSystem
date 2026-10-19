
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
import java.sql.SQLException;

@WebServlet("/edge-servers")
public class EdgeServerServlet extends HttpServlet {

    private String escape(String value) {
        if (value == null) return "";
        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    private Integer getInteger(String value) {
        if (value == null || value.trim().isEmpty()) return null;
        return Integer.valueOf(value.trim());
    }

    private void redirect(HttpServletRequest request,
                          HttpServletResponse response,
                          String message)
            throws IOException {
        response.sendRedirect(
                request.getContextPath() + "/edge-servers?message=" + message);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        try (Connection con = DatabaseConnection.getConnection()) {

            if ("add".equals(action)) {
                String name = request.getParameter("server_name");
                if (name == null || name.trim().isEmpty()) {
                    redirect(request, response, "name_required");
                    return;
                }

                String sql = "INSERT INTO edge_servers "
                        + "(server_name, location, cpu_capacity, memory_capacity, "
                        + "storage_capacity, connected_users, status) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?)";

                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setString(1, name.trim());
                    ps.setString(2, request.getParameter("location"));
                    setNullableInt(ps, 3, request.getParameter("cpu_capacity"));
                    setNullableInt(ps, 4, request.getParameter("memory_capacity"));
                    setNullableInt(ps, 5, request.getParameter("storage_capacity"));
                    setNullableInt(ps, 6, request.getParameter("connected_users"));
                    ps.setString(7, request.getParameter("status"));
                    ps.executeUpdate();
                }

                redirect(request, response, "added");
                return;
            }

            if ("update".equals(action)) {
                String name = request.getParameter("server_name");
                int id = Integer.parseInt(request.getParameter("edge_server_id"));

                if (name == null || name.trim().isEmpty()) {
                    redirect(request, response, "name_required");
                    return;
                }

                String sql = "UPDATE edge_servers SET server_name=?, location=?, "
                        + "cpu_capacity=?, memory_capacity=?, storage_capacity=?, "
                        + "connected_users=?, status=? WHERE edge_server_id=?";

                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setString(1, name.trim());
                    ps.setString(2, request.getParameter("location"));
                    setNullableInt(ps, 3, request.getParameter("cpu_capacity"));
                    setNullableInt(ps, 4, request.getParameter("memory_capacity"));
                    setNullableInt(ps, 5, request.getParameter("storage_capacity"));
                    setNullableInt(ps, 6, request.getParameter("connected_users"));
                    ps.setString(7, request.getParameter("status"));
                    ps.setInt(8, id);
                    ps.executeUpdate();
                }

                redirect(request, response, "updated");
                return;
            }

            if ("delete".equals(action)) {
                int id = Integer.parseInt(request.getParameter("edge_server_id"));

                try (PreparedStatement ps = con.prepareStatement(
                        "DELETE FROM edge_servers WHERE edge_server_id=?")) {
                    ps.setInt(1, id);
                    ps.executeUpdate();
                }

                redirect(request, response, "deleted");
                return;
            }

            redirect(request, response, "invalid_action");

        } catch (NumberFormatException e) {
            redirect(request, response, "invalid_number");
        } catch (SQLException e) {
            throw new ServletException("Could not modify edge server records.", e);
        }
    }

    private void setNullableInt(PreparedStatement ps, int index, String value)
            throws SQLException {
        Integer number = getInteger(value);
        if (number == null) {
            ps.setNull(index, java.sql.Types.INTEGER);
        } else {
            ps.setInt(index, number);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        String ctx = request.getContextPath();

        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html><html><head><meta charset='UTF-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1'>");
            out.println("<title>Edge Computing Management</title>");
            out.println("<style>");
            out.println("body{margin:0;font-family:Arial,sans-serif;background:#f5f7fb;color:#1f2937}");
            out.println(".header{background:#172554;color:white;padding:25px 35px}");
            out.println(".container{padding:25px 35px;overflow-x:auto}");
            out.println(".back{display:inline-block;margin-bottom:20px;color:#1d4ed8;text-decoration:none;font-weight:bold}");
            out.println(".panel{background:white;padding:20px;margin:18px 0;border-radius:10px;box-shadow:0 3px 12px #0000000d}");
            out.println(".formgrid{display:grid;grid-template-columns:repeat(4,minmax(150px,1fr));gap:12px}");
            out.println("input,select{box-sizing:border-box;width:100%;padding:9px;border:1px solid #cbd5e1;border-radius:5px}");
            out.println("button{padding:9px 13px;border:0;border-radius:5px;cursor:pointer;background:#1d4ed8;color:white}");
            out.println(".delete{background:#dc2626}.save{background:#15803d}");
            out.println("table{width:100%;border-collapse:collapse;background:white;min-width:1050px}");
            out.println("th{background:#172554;color:white;text-align:left;padding:12px}");
            out.println("td{padding:10px;border-bottom:1px solid #e5e7eb}");
            out.println(".rowinput{min-width:90px}.notice{padding:12px;background:#dcfce7;border-radius:6px}");
            out.println("@media(max-width:800px){.formgrid{grid-template-columns:repeat(2,minmax(130px,1fr))}.container{padding:18px}}");
            out.println("</style></head><body>");

            out.println("<div class='header'><h1>Edge Computing Management</h1>");
            out.println("<p>Add, view, update and delete edge computing servers</p></div>");
            out.println("<div class='container'><a class='back' href='" + ctx
                    + "/dashboard'>← Back to Dashboard</a>");

            String message = request.getParameter("message");
            if (message != null) {
                String text = switch (message) {
                    case "added" -> "Edge server added successfully.";
                    case "updated" -> "Edge server updated successfully.";
                    case "deleted" -> "Edge server deleted successfully.";
                    case "name_required" -> "Server name is required.";
                    case "invalid_number" -> "Please enter valid numeric values.";
                    default -> "Request processed.";
                };
                out.println("<p class='notice'>" + escape(text) + "</p>");
            }

            out.println("<div class='panel'><h2>Add Edge Server</h2>");
            out.println("<form method='post' action='" + ctx + "/edge-servers'>");
            out.println("<input type='hidden' name='action' value='add'>");
            out.println("<div class='formgrid'>");
            field(out, "Server Name *", "server_name", "", "text", true);
            field(out, "Location", "location", "", "text", false);
            field(out, "CPU Capacity", "cpu_capacity", "", "number", false);
            field(out, "Memory Capacity", "memory_capacity", "", "number", false);
            field(out, "Storage Capacity", "storage_capacity", "", "number", false);
            field(out, "Connected Users", "connected_users", "", "number", false);
            out.println("<div><label>Status</label><select name='status'>"
                    + "<option>Active</option><option>Inactive</option>"
                    + "<option>Maintenance</option></select></div>");
            out.println("</div><p><button type='submit'>Add Edge Server</button></p></form></div>");

            out.println("<div class='panel'><h2>Existing Edge Servers</h2>");
            out.println("<table><thead><tr><th>ID</th><th>Server Name</th><th>Location</th>"
                    + "<th>CPU</th><th>Memory</th><th>Storage</th><th>Connected Users</th>"
                    + "<th>Status</th><th>Actions</th></tr></thead><tbody>");

            int count = 0;
            String sql = "SELECT edge_server_id, server_name, location, cpu_capacity, "
                    + "memory_capacity, storage_capacity, connected_users, status "
                    + "FROM edge_servers ORDER BY edge_server_id";

            try (Connection con = DatabaseConnection.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    int id = rs.getInt("edge_server_id");
                    String formId = "update-" + id;

                    out.println("<tr>");
                    out.println("<td>" + id + "</td>");
                    out.println("<td><input class='rowinput' form='" + formId
                            + "' name='server_name' required value='"
                            + escape(rs.getString("server_name")) + "'></td>");
                    out.println("<td><input class='rowinput' form='" + formId
                            + "' name='location' value='"
                            + escape(rs.getString("location")) + "'></td>");

                    rowNumber(out, formId, "cpu_capacity", rs.getObject("cpu_capacity"));
                    rowNumber(out, formId, "memory_capacity", rs.getObject("memory_capacity"));
                    rowNumber(out, formId, "storage_capacity", rs.getObject("storage_capacity"));
                    rowNumber(out, formId, "connected_users", rs.getObject("connected_users"));

                    String status = rs.getString("status");
                    out.println("<td><select form='" + formId + "' name='status'>");
                    for (String option : new String[]{"Active", "Inactive", "Maintenance"}) {
                        out.println("<option value='" + option + "' "
                                + (option.equalsIgnoreCase(status) ? "selected" : "")
                                + ">" + option + "</option>");
                    }
                    out.println("</select></td>");

                    out.println("<td><form id='" + formId + "' method='post' action='"
                            + ctx + "/edge-servers'></form>");
                    out.println("<input form='" + formId
                            + "' type='hidden' name='action' value='update'>");
                    out.println("<input form='" + formId
                            + "' type='hidden' name='edge_server_id' value='" + id + "'>");
                    out.println("<button class='save' form='" + formId
                            + "' type='submit'>Save</button>");
                    out.println("<form method='post' action='" + ctx
                            + "/edge-servers' style='display:inline' "
                            + "onsubmit=\"return confirm('Delete this edge server?')\">");
                    out.println("<input type='hidden' name='action' value='delete'>");
                    out.println("<input type='hidden' name='edge_server_id' value='" + id + "'>");
                    out.println("<button class='delete' type='submit'>Delete</button></form></td>");
                    out.println("</tr>");
                    count++;
                }
            } catch (SQLException e) {
                throw new ServletException("Could not load edge server records.", e);
            }

            out.println("</tbody></table><p>Total Edge Servers: " + count + "</p></div>");
            out.println("</div></body></html>");
        }
    }

    private void field(PrintWriter out, String label, String name, String value,
                       String type, boolean required) {
        out.println("<div><label>" + escape(label) + "</label><input type='" + type
                + "' name='" + name + "' value='" + escape(value) + "' "
                + (required ? "required" : "") + "></div>");
    }

    private void rowNumber(PrintWriter out, String formId, String name, Object value) {
        out.println("<td><input class='rowinput' form='" + formId
                + "' type='number' name='" + name + "' value='"
                + (value == null ? "" : value) + "'></td>");
    }
}
