
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

@WebServlet("/resource-allocation")
public class ResourceAllocationServlet extends HttpServlet {

    private String escape(Object value) {
        if (value == null) return "";
        return value.toString()
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        String message = "Operation failed.";

        try (Connection con = DatabaseConnection.getConnection()) {

            if ("add".equals(action)) {
                String sql = "INSERT INTO resource_allocation "
                        + "(cell_id, bandwidth, frequency, allocated_users) "
                        + "VALUES (?, ?, ?, ?)";

                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setInt(1, Integer.parseInt(request.getParameter("cell_id")));
                    ps.setInt(2, Integer.parseInt(request.getParameter("bandwidth")));
                    ps.setInt(3, Integer.parseInt(request.getParameter("frequency")));
                    ps.setInt(4, Integer.parseInt(request.getParameter("allocated_users")));
                    ps.executeUpdate();
                }
                message = "Resource allocation added successfully.";

            } else if ("update".equals(action)) {
                String sql = "UPDATE resource_allocation SET cell_id=?, "
                        + "bandwidth=?, frequency=?, allocated_users=? "
                        + "WHERE allocation_id=?";

                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setInt(1, Integer.parseInt(request.getParameter("cell_id")));
                    ps.setInt(2, Integer.parseInt(request.getParameter("bandwidth")));
                    ps.setInt(3, Integer.parseInt(request.getParameter("frequency")));
                    ps.setInt(4, Integer.parseInt(request.getParameter("allocated_users")));
                    ps.setInt(5, Integer.parseInt(request.getParameter("allocation_id")));

                    if (ps.executeUpdate() > 0) {
                        message = "Resource allocation updated successfully.";
                    } else {
                        message = "Allocation not found.";
                    }
                }

            } else if ("delete".equals(action)) {
                String sql = "DELETE FROM resource_allocation WHERE allocation_id=?";

                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setInt(1, Integer.parseInt(request.getParameter("allocation_id")));

                    if (ps.executeUpdate() > 0) {
                        message = "Resource allocation deleted successfully.";
                    } else {
                        message = "Allocation not found.";
                    }
                }
            }

        } catch (Exception e) {
            getServletContext().log("Resource allocation operation failed", e);
            message = "Operation failed. Check the entered values and database connection.";
        }

        response.sendRedirect(request.getContextPath()
                + "/resource-allocation?message="
                + java.net.URLEncoder.encode(message,
                java.nio.charset.StandardCharsets.UTF_8));
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            String context = request.getContextPath();

            out.println("""
                <!DOCTYPE html>
                <html>
                <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1">
                <title>Resource Allocation Management</title>
                <style>
                body{margin:0;font-family:Arial,sans-serif;background:#f5f7fb;color:#1f2937}
                .header{background:#172554;color:white;padding:25px 40px}
                .header h1{margin:0;font-size:28px}
                .header p{color:#cbd5e1}
                .container{padding:30px 40px}
                .back{display:inline-block;margin-bottom:20px;color:#1d4ed8;font-weight:bold;text-decoration:none}
                .card{background:white;padding:22px;margin-bottom:25px;border-radius:10px;box-shadow:0 3px 12px #00000012}
                form.grid{display:grid;grid-template-columns:repeat(auto-fit,minmax(180px,1fr));gap:14px;align-items:end}
                label{display:block;font-weight:bold;margin-bottom:6px}
                input{box-sizing:border-box;width:100%;padding:10px;border:1px solid #cbd5e1;border-radius:5px}
                button{padding:10px 14px;border:0;border-radius:5px;background:#1d4ed8;color:white;cursor:pointer}
                button.delete{background:#dc2626}
                button.edit{background:#047857}
                table{width:100%;border-collapse:collapse;background:white;box-shadow:0 3px 12px #00000012}
                th{background:#172554;color:white;text-align:left}
                th,td{padding:12px;border-bottom:1px solid #e5e7eb}
                tr:hover{background:#f8fafc}
                .actions{display:flex;gap:8px;flex-wrap:wrap}
                .message{background:#dcfce7;color:#166534;padding:12px;margin-bottom:18px;border-radius:6px}
                .table-wrap{overflow-x:auto}
                </style>
                </head><body>
                <div class="header">
                <h1>Resource Allocation Management</h1>
                <p>Add, view, update and delete allocated network resources</p>
                </div><div class="container">
                """);

            out.println("<a class='back' href='" + context
                    + "/dashboard'>← Back to Dashboard</a>");

            String message = request.getParameter("message");
            if (message != null) {
                out.println("<div class='message'>" + escape(message) + "</div>");
            }

            out.println("""
                <div class="card">
                <h2>Add Resource Allocation</h2>
                <form class="grid" method="post">
                <input type="hidden" name="action" value="add">
                <div><label>Cell ID</label><input type="number" name="cell_id" min="1" required></div>
                <div><label>Bandwidth</label><input type="number" name="bandwidth" min="0" required></div>
                <div><label>Frequency</label><input type="number" name="frequency" min="0" required></div>
                <div><label>Allocated Users</label><input type="number" name="allocated_users" min="0" required></div>
                <div><button type="submit">Add Allocation</button></div>
                </form></div>
                <h2>Existing Resource Allocations</h2>
                <div class="table-wrap"><table>
                <tr><th>Allocation ID</th><th>Cell ID</th><th>Bandwidth</th>
                <th>Frequency</th><th>Allocated Users</th><th>Actions</th></tr>
                """);

            int count = 0;

            String sql = "SELECT allocation_id, cell_id, bandwidth, frequency, "
                    + "allocated_users FROM resource_allocation ORDER BY allocation_id";

            try (Connection con = DatabaseConnection.getConnection();
                 Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery(sql)) {

                while (rs.next()) {
                    count++;

                    int id = rs.getInt("allocation_id");
                    int cellId = rs.getInt("cell_id");
                    int bandwidth = rs.getInt("bandwidth");
                    int frequency = rs.getInt("frequency");
                    int users = rs.getInt("allocated_users");

                    out.println("<tr><td>" + id + "</td><td>" + cellId
                            + "</td><td>" + bandwidth + "</td><td>" + frequency
                            + "</td><td>" + users + "</td><td><div class='actions'>");

                    out.println("<form method='post' class='actions'>"
                            + "<input type='hidden' name='action' value='update'>"
                            + "<input type='hidden' name='allocation_id' value='" + id + "'>"
                            + "<input type='number' name='cell_id' min='1' required value='" + cellId + "'>"
                            + "<input type='number' name='bandwidth' min='0' required value='" + bandwidth + "'>"
                            + "<input type='number' name='frequency' min='0' required value='" + frequency + "'>"
                            + "<input type='number' name='allocated_users' min='0' required value='" + users + "'>"
                            + "<button class='edit' type='submit'>Save Edit</button></form>");

                    out.println("<form method='post' onsubmit=\"return confirm('Delete allocation "
                            + id + "?')\">"
                            + "<input type='hidden' name='action' value='delete'>"
                            + "<input type='hidden' name='allocation_id' value='" + id + "'>"
                            + "<button class='delete' type='submit'>Delete</button></form>");

                    out.println("</div></td></tr>");
                }
            } catch (Exception e) {
                getServletContext().log("Unable to load resource allocations", e);
                out.println("<tr><td colspan='6'>Unable to load allocations. Check the server log.</td></tr>");
            }

            out.println("</table></div>");
            out.println("<p><strong>Total Resource Allocations: "
                    + count + "</strong></p></div></body></html>");
        }
    }
}
