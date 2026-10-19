
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

@WebServlet("/subscribers")
public class SubscriberServlet extends HttpServlet {

    private String esc(Object value) {
        if (value == null) return "";
        return value.toString()
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    private String param(HttpServletRequest req, String name) {
        String value = req.getParameter(name);
        return value == null ? "" : value.trim();
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = param(request, "action");

        try (Connection con = DatabaseConnection.getConnection()) {

            if ("delete".equals(action)) {
                int id = Integer.parseInt(param(request, "subscriber_id"));

                try (PreparedStatement ps = con.prepareStatement(
                        "DELETE FROM subscribers WHERE subscriber_id = ?")) {
                    ps.setInt(1, id);
                    ps.executeUpdate();
                }

            } else if ("add".equals(action) || "update".equals(action)) {

                String name = param(request, "subscriber_name");
                String phone = param(request, "phone_number");
                String email = param(request, "email");
                String type = param(request, "subscription_type");
                String plan = param(request, "data_plan");
                String limitText = param(request, "monthly_data_limit");
                String usageText = param(request, "current_data_usage");
                String status = param(request, "status");

                if (name.isEmpty() || phone.isEmpty() || email.isEmpty()
                        || type.isEmpty() || plan.isEmpty()
                        || limitText.isEmpty() || usageText.isEmpty()
                        || status.isEmpty()) {
                    response.sendError(400, "Please fill in all subscriber fields.");
                    return;
                }

                double limit;
                double usage;

                try {
                    limit = Double.parseDouble(limitText);
                    usage = Double.parseDouble(usageText);

                    if (!Double.isFinite(limit) || !Double.isFinite(usage)
                            || limit < 0 || usage < 0) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException ex) {
                    response.sendError(400,
                            "Monthly data limit and current usage must be valid non-negative numbers.");
                    return;
                }

                if (!status.equals("Active") && !status.equals("Inactive")
                        && !status.equals("Suspended")) {
                    response.sendError(400, "Invalid subscriber status.");
                    return;
                }

                String sql;

                if ("add".equals(action)) {
                    sql = "INSERT INTO subscribers " +
                            "(subscriber_name, phone_number, email, " +
                            "subscription_type, data_plan, monthly_data_limit, " +
                            "current_data_usage, status) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                } else {
                    sql = "UPDATE subscribers SET subscriber_name=?, phone_number=?, " +
                            "email=?, subscription_type=?, data_plan=?, " +
                            "monthly_data_limit=?, current_data_usage=?, status=? " +
                            "WHERE subscriber_id=?";
                }

                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setString(1, name);
                    ps.setString(2, phone);
                    ps.setString(3, email);
                    ps.setString(4, type);
                    ps.setString(5, plan);
                    ps.setDouble(6, limit);
                    ps.setDouble(7, usage);
                    ps.setString(8, status);

                    if ("update".equals(action)) {
                        int id = Integer.parseInt(param(request, "subscriber_id"));
                        ps.setInt(9, id);
                    }

                    ps.executeUpdate();
                }

            } else {
                response.sendError(400, "Invalid action.");
                return;
            }

            response.sendRedirect(request.getContextPath() + "/subscribers");

        } catch (NumberFormatException ex) {
            response.sendError(400, "Invalid subscriber ID.");
        } catch (SQLException ex) {
            log("Subscriber database operation failed", ex);
            response.sendError(500,
                    "Database operation failed. Check the server console for details.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        String editId = param(request, "edit");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html><html><head><meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width,initial-scale=1'>");
        out.println("<title>Subscriber Management</title>");

        out.println("<style>");
        out.println("*{box-sizing:border-box}body{margin:0;font-family:Arial,sans-serif;background:#f5f7fb;color:#1f2937}");
        out.println(".header{background:#172554;color:white;padding:24px 35px}");
        out.println(".header h1{margin:0;font-size:28px}.header p{margin:8px 0 0;color:#cbd5e1}");
        out.println(".container{padding:28px 35px;max-width:1500px;margin:auto}");
        out.println(".back{display:inline-block;margin-bottom:20px;color:#1d4ed8;text-decoration:none;font-weight:bold}");
        out.println(".panel{background:white;padding:22px;border-radius:10px;margin-bottom:24px;box-shadow:0 3px 12px #0000000d}");
        out.println(".form-grid{display:grid;grid-template-columns:repeat(4,minmax(150px,1fr));gap:15px}");
        out.println("label{display:block;font-size:13px;font-weight:bold;margin-bottom:7px}");
        out.println("input,select{width:100%;padding:11px;border:1px solid #cbd5e1;border-radius:6px;font-size:14px}");
        out.println(".btn{display:inline-block;padding:10px 14px;border:0;border-radius:6px;background:#2563eb;color:white;text-decoration:none;cursor:pointer;font-weight:bold}");
        out.println(".green{background:#15803d}.red{background:#dc2626}.gray{background:#64748b}");
        out.println(".actions{display:flex;gap:7px;align-items:center;flex-wrap:wrap}.actions form{margin:0}");
        out.println(".table-wrap{overflow-x:auto}table{width:100%;border-collapse:collapse;background:white;font-size:13px}");
        out.println("th{background:#172554;color:white;text-align:left;padding:12px;white-space:nowrap}");
        out.println("td{padding:11px;border-bottom:1px solid #e5e7eb;white-space:nowrap}tr:hover{background:#f8fafc}");
        out.println(".total{font-weight:bold;margin-top:16px}.cancel{margin-left:8px}");
        out.println("@media(max-width:800px){.container{padding:18px}.form-grid{grid-template-columns:repeat(2,minmax(0,1fr))}.header{padding:22px}}");
        out.println("@media(max-width:480px){.form-grid{grid-template-columns:1fr}}");
        out.println("</style></head><body>");

        out.println("<div class='header'><h1>Subscriber Management</h1>");
        out.println("<p>Add, view, edit and delete telecom subscribers</p></div>");
        out.println("<div class='container'>");
        out.println("<a class='back' href='" + request.getContextPath() +
                "/dashboard'>&larr; Back to Dashboard</a>");

        String name = "", phone = "", email = "", type = "Prepaid";
        String plan = "5G Basic", limit = "50", usage = "0", status = "Active";
        boolean editing = false;

        if (!editId.isEmpty()) {
            try {
                int id = Integer.parseInt(editId);

                try (Connection con = DatabaseConnection.getConnection();
                     PreparedStatement ps = con.prepareStatement(
                             "SELECT * FROM subscribers WHERE subscriber_id=?")) {
                    ps.setInt(1, id);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            editing = true;
                            name = rs.getString("subscriber_name");
                            phone = rs.getString("phone_number");
                            email = rs.getString("email");
                            type = rs.getString("subscription_type");
                            plan = rs.getString("data_plan");
                            limit = rs.getString("monthly_data_limit");
                            usage = rs.getString("current_data_usage");
                            status = rs.getString("status");
                        }
                    }
                }
            } catch (NumberFormatException ex) {
                out.println("<p>Invalid subscriber ID.</p>");
            } catch (SQLException ex) {
                log("Could not load subscriber for editing", ex);
                out.println("<p>Could not load subscriber. Check the server log.</p>");
            }
        }

        out.println("<div class='panel'><h2>" +
                (editing ? "Edit Subscriber" : "Add Subscriber") + "</h2>");

        out.println("<form method='post' action='" +
                request.getContextPath() + "/subscribers'>");

        out.println("<input type='hidden' name='action' value='" +
                (editing ? "update" : "add") + "'>");

        if (editing) {
            out.println("<input type='hidden' name='subscriber_id' value='" +
                    esc(editId) + "'>");
        }

        out.println("<div class='form-grid'>");

        field(out, "Subscriber Name", "subscriber_name", name, "text");
        field(out, "Phone Number", "phone_number", phone, "tel");
        field(out, "Email", "email", email, "email");

        out.println("<div><label>Subscription Type</label><select name='subscription_type'>");
        option(out, "Prepaid", type);
        option(out, "Postpaid", type);
        out.println("</select></div>");

        out.println("<div><label>Data Plan</label><select name='data_plan'>");
        option(out, "4G Basic", plan);
        option(out, "4G Standard", plan);
        option(out, "5G Basic", plan);
        option(out, "5G Standard", plan);
        option(out, "5G Premium", plan);
        out.println("</select></div>");

        field(out, "Monthly Data Limit (GB)", "monthly_data_limit", limit, "number");
        field(out, "Current Data Usage (GB)", "current_data_usage", usage, "number");

        out.println("<div><label>Status</label><select name='status'>");
        option(out, "Active", status);
        option(out, "Inactive", status);
        option(out, "Suspended", status);
        out.println("</select></div>");

        out.println("</div><p style='margin-bottom:0;margin-top:20px'>");
        out.println("<button class='btn green' type='submit'>" +
                (editing ? "Update Subscriber" : "Add Subscriber") + "</button>");

        if (editing) {
            out.println("<a class='btn gray cancel' href='" +
                    request.getContextPath() + "/subscribers'>Cancel</a>");
        }

        out.println("</p></form></div>");

        out.println("<div class='panel'><h2>Registered Subscribers</h2>");
        out.println("<div class='table-wrap'><table><thead><tr>");
        out.println("<th>ID</th><th>Name</th><th>Phone</th><th>Email</th>");
        out.println("<th>Subscription</th><th>Data Plan</th><th>Monthly Limit (GB)</th>");
        out.println("<th>Usage (GB)</th><th>Status</th><th>Actions</th>");
        out.println("</tr></thead><tbody>");

        int count = 0;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT * FROM subscribers ORDER BY subscriber_id");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("subscriber_id");
                count++;

                out.println("<tr>");
                cell(out, id);
                cell(out, rs.getString("subscriber_name"));
                cell(out, rs.getString("phone_number"));
                cell(out, rs.getString("email"));
                cell(out, rs.getString("subscription_type"));
                cell(out, rs.getString("data_plan"));
                cell(out, rs.getString("monthly_data_limit"));
                cell(out, rs.getString("current_data_usage"));
                cell(out, rs.getString("status"));

                out.println("<td><div class='actions'>");
                out.println("<a class='btn' href='" + request.getContextPath() +
                        "/subscribers?edit=" + id + "'>Edit</a>");

                out.println("<form method='post' action='" + request.getContextPath() +
                        "/subscribers' onsubmit=\"return confirm('Delete this subscriber?');\">");
                out.println("<input type='hidden' name='action' value='delete'>");
                out.println("<input type='hidden' name='subscriber_id' value='" + id + "'>");
                out.println("<button class='btn red' type='submit'>Delete</button>");
                out.println("</form></div></td></tr>");
            }

        } catch (SQLException ex) {
            log("Could not load subscribers", ex);
            out.println("<tr><td colspan='10'>Unable to load subscribers. Check the server log.</td></tr>");
        }

        out.println("</tbody></table></div>");
        out.println("<p class='total'>Total Subscribers: " + count + "</p>");
        out.println("</div></div></body></html>");
    }

    private void field(PrintWriter out, String label, String name,
                       String value, String type) {
        out.println("<div><label>" + esc(label) + "</label>");
        out.println("<input type='" + type + "' name='" + name +
                "' value='" + esc(value) + "' required" +
                ("number".equals(type) ? " min='0' step='0.1'" : "") +
                "></div>");
    }

    private void option(PrintWriter out, String value, String selected) {
        out.println("<option value='" + esc(value) + "'" +
                (value.equals(selected) ? " selected" : "") + ">" +
                esc(value) + "</option>");
    }

    private void cell(PrintWriter out, Object value) {
        out.println("<td>" + esc(value) + "</td>");
    }
}
