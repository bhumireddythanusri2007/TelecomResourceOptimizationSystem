
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
import java.sql.Statement;
import java.util.Locale;

@WebServlet("/optimization")
public class OptimizationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        try (Connection con = DatabaseConnection.getConnection();
             PrintWriter out = response.getWriter()) {

            out.println("""
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Network Optimization</title>
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            margin: 0;
                            padding: 25px;
                            background: #f4f7fb;
                            color: #263238;
                        }
                        .container {
                            max-width: 1200px;
                            margin: auto;
                        }
                        h1, h2 { color: #17365d; }
                        .card {
                            background: white;
                            padding: 20px;
                            margin: 18px 0;
                            border-radius: 10px;
                            box-shadow: 0 2px 8px #00000012;
                            overflow-x: auto;
                        }
                        button {
                            background: #1769aa;
                            color: white;
                            border: none;
                            padding: 11px 18px;
                            border-radius: 5px;
                            cursor: pointer;
                        }
                        button:hover { background: #0d4778; }
                        table {
                            width: 100%;
                            border-collapse: collapse;
                            margin-top: 12px;
                            background: white;
                        }
                        th, td {
                            padding: 10px;
                            text-align: left;
                            border-bottom: 1px solid #ddd;
                            white-space: nowrap;
                        }
                        th { background: #e8eff7; }
                        .notice {
                            padding: 12px;
                            background: #fff4d6;
                            border-radius: 5px;
                            line-height: 1.5;
                        }
                        .success { color: #167447; }
                        .error { color: #b42318; }
                        a { color: #1769aa; }
                    </style>
                </head>
                <body>
                <div class="container">
                    <h1>Network Resource Optimization</h1>
                    <p>Rule-based simulation for telecom network resource management.</p>

                    <div class="card">
                        <h2>Run Optimization</h2>
                        <p>
                            Estimates a 10% relative reduction in load for eligible
                            active network cells.
                        </p>
                        <form method="post" action="optimization">
                            <input type="hidden" name="action" value="run">
                            <button type="submit">Run Optimization</button>
                        </form>
                        <p class="notice">
                            Research prototype: results are illustrative estimates,
                            not a quantum optimization algorithm or a real-world
                            network control system. The performance score is a
                            simple illustrative indicator, not a validated telecom KPI.
                        </p>
            """);

            String message = request.getParameter("message");

            if ("success".equals(message)) {
                out.println("<p class='success'>Optimization completed successfully.</p>");
            } else if ("failed".equals(message)) {
                out.println("<p class='error'>Optimization failed. Check the server console for details.</p>");
            }

            out.println("""
                    </div>
                    <div class="card">
                        <h2>Optimization Results</h2>
                        <table>
                            <tr>
                                <th>Result ID</th>
                                <th>Cell ID</th>
                                <th>Previous Load (%)</th>
                                <th>Simulated Load (%)</th>
                                <th>Allocated Bandwidth</th>
                                <th>Performance Score</th>
                                <th>Optimization Time</th>
                            </tr>
            """);

            String resultsSql =
                    "SELECT result_id, cell_id, previous_load, optimized_load, " +
                    "allocated_bandwidth, performance_score, optimization_time " +
                    "FROM optimization_results ORDER BY result_id ASC";

            try (PreparedStatement ps = con.prepareStatement(resultsSql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    out.println("<tr>");
                    out.println("<td>" + rs.getInt("result_id") + "</td>");
                    out.println("<td>" + rs.getObject("cell_id") + "</td>");
                    out.println("<td>" + formatNumber(rs.getObject("previous_load")) + "</td>");
                    out.println("<td>" + formatNumber(rs.getObject("optimized_load")) + "</td>");
                    out.println("<td>" + rs.getObject("allocated_bandwidth") + "</td>");
                    out.println("<td>" + formatNumber(rs.getObject("performance_score")) + "</td>");
                    out.println("<td>" + escapeHtml(rs.getString("optimization_time")) + "</td>");
                    out.println("</tr>");
                }
            }

            out.println("""
                        </table>
                    </div>
                    <div class="card">
                        <h2>Optimization Jobs</h2>
                        <table>
                            <tr>
                                <th>Job ID</th>
                                <th>Job Name</th>
                                <th>Optimization Type</th>
                                <th>Target Cell ID</th>
                                <th>Input Load</th>
                                <th>Status</th>
                                <th>Created Time</th>
                            </tr>
            """);

            // Select the latest 20 jobs, then display them in ascending order.
            String jobsSql =
                    "SELECT job_id, job_name, optimization_type, target_cell_id, " +
                    "input_load, status, created_time FROM (" +
                    "SELECT job_id, job_name, optimization_type, target_cell_id, " +
                    "input_load, status, created_time FROM optimization_jobs " +
                    "ORDER BY job_id DESC LIMIT 20) AS recent_jobs " +
                    "ORDER BY job_id ASC";

            try (PreparedStatement ps = con.prepareStatement(jobsSql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    out.println("<tr>");
                    out.println("<td>" + rs.getInt("job_id") + "</td>");
                    out.println("<td>" + escapeHtml(rs.getString("job_name")) + "</td>");
                    out.println("<td>" + escapeHtml(rs.getString("optimization_type")) + "</td>");
                    out.println("<td>" + rs.getObject("target_cell_id") + "</td>");
                    out.println("<td>" + formatNumber(rs.getObject("input_load")) + "</td>");
                    out.println("<td>" + escapeHtml(rs.getString("status")) + "</td>");
                    out.println("<td>" + escapeHtml(rs.getString("created_time")) + "</td>");
                    out.println("</tr>");
                }
            }

            out.println("""
                        </table>
                    </div>
                    <p><a href="dashboard">Back to Dashboard</a></p>
                </div>
                </body>
                </html>
            """);

        } catch (SQLException e) {
            throw new ServletException("Unable to load optimization data.", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        if (!"run".equals(request.getParameter("action"))) {
            response.sendRedirect("optimization");
            return;
        }

        try (Connection con = DatabaseConnection.getConnection()) {
            con.setAutoCommit(false);

            try {
                int jobId;

                String insertJob =
                        "INSERT INTO optimization_jobs " +
                        "(job_name, objective, optimization_type, input_load, status) " +
                        "VALUES (?, ?, ?, ?, ?)";

                try (PreparedStatement ps = con.prepareStatement(
                        insertJob, Statement.RETURN_GENERATED_KEYS)) {

                    ps.setString(1, "Rule-Based Network Load Optimization");
                    ps.setString(2, "Simulate a reduction in estimated cell load");
                    ps.setString(3, "RULE_BASED_SIMULATION");
                    ps.setDouble(4, 0.0);
                    ps.setString(5, "Running");
                    ps.executeUpdate();

                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (!keys.next()) {
                            throw new SQLException("Could not create optimization job.");
                        }
                        jobId = keys.getInt(1);
                    }
                }

                String cellSql =
                        "SELECT cell_id, current_users, total_capacity " +
                        "FROM network_cells WHERE status = 'Active' ORDER BY cell_id ASC";

                String allocationSql =
                        "SELECT COALESCE(SUM(bandwidth), 0) " +
                        "FROM resource_allocation WHERE cell_id = ?";

                String resultSql =
                        "INSERT INTO optimization_results " +
                        "(cell_id, previous_load, optimized_load, " +
                        "allocated_bandwidth, performance_score) " +
                        "VALUES (?, ?, ?, ?, ?)";

                double totalPreviousLoad = 0.0;
                int processedCells = 0;

                try (PreparedStatement cells = con.prepareStatement(cellSql);
                     ResultSet rs = cells.executeQuery();
                     PreparedStatement allocation = con.prepareStatement(allocationSql);
                     PreparedStatement result = con.prepareStatement(resultSql)) {

                    while (rs.next()) {
                        int cellId = rs.getInt("cell_id");
                        Object usersValue = rs.getObject("current_users");
                        Object capacityValue = rs.getObject("total_capacity");

                        if (!(usersValue instanceof Number)
                                || !(capacityValue instanceof Number)) {
                            continue;
                        }

                        int users = ((Number) usersValue).intValue();
                        int capacity = ((Number) capacityValue).intValue();

                        if (capacity <= 0) {
                            continue;
                        }

                        // Calculate the original cell load as a percentage.
                        double previousLoad = users * 100.0 / capacity;

                        // Simulate a 10% relative reduction in the original load.
                        double optimizedLoad = previousLoad * 0.90;

                        // Illustrative score: lower simulated load gives a higher score.
                        double performanceScore =
                                Math.max(0.0, Math.min(100.0, 100.0 - optimizedLoad));

                        allocation.setInt(1, cellId);
                        int bandwidth;

                        try (ResultSet ar = allocation.executeQuery()) {
                            ar.next();
                            bandwidth = ar.getInt(1);
                        }

                        result.setInt(1, cellId);
                        result.setDouble(2, previousLoad);
                        result.setDouble(3, optimizedLoad);
                        result.setInt(4, bandwidth);
                        result.setDouble(5, performanceScore);
                        result.executeUpdate();

                        totalPreviousLoad += previousLoad;
                        processedCells++;
                    }
                }

                if (processedCells == 0) {
                    throw new SQLException("No eligible active network cells were found.");
                }

                String updateJob =
                        "UPDATE optimization_jobs SET input_load = ?, status = ? " +
                        "WHERE job_id = ?";

                try (PreparedStatement ps = con.prepareStatement(updateJob)) {
                    ps.setDouble(1, totalPreviousLoad / processedCells);
                    ps.setString(2, "Completed");
                    ps.setInt(3, jobId);
                    ps.executeUpdate();
                }

                con.commit();

            } catch (Exception e) {
                try {
                    con.rollback();
                } catch (SQLException rollbackError) {
                    e.addSuppressed(rollbackError);
                }
                throw e;
            }

        } catch (Exception e) {
            getServletContext().log("Optimization failed.", e);
            response.sendRedirect("optimization?message=failed");
            return;
        }

        response.sendRedirect("optimization?message=success");
    }

    private String formatNumber(Object value) {
        if (value == null) {
            return "-";
        }

        if (value instanceof Number number) {
            return String.format(Locale.US, "%.2f", number.doubleValue());
        }

        return escapeHtml(value.toString());
    }

    private String escapeHtml(String value) {
        if (value == null) {
            return "-";
        }

        return value.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#39;");
    }
}
