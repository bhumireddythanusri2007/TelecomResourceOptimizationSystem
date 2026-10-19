
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

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        int networkCells = 0;
        int devices = 0;
        int subscribers = 0;
        int trafficRecords = 0;
        int alerts = 0;
        int optimizationResults = 0;
        int kpiRecords = 0;

        try (Connection connection =
                     DatabaseConnection.getConnection()) {

            networkCells = getCount(connection, "network_cells");
            devices = getCount(connection, "devices");
            subscribers = getCount(connection, "subscribers");
            trafficRecords = getCount(connection, "network_traffic");
            alerts = getCount(connection, "alerts");
            optimizationResults =
                    getCount(connection, "optimization_results");
            kpiRecords = getCount(connection, "network_kpis");

        } catch (Exception e) {
            getServletContext().log(
                    "Unable to load dashboard statistics.", e);
        }

        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
        out.println("<title>Telecom Resource Optimization System</title>");

        out.println("<style>");
        out.println("* { box-sizing: border-box; }");
        out.println("body { margin:0; font-family:Arial,sans-serif; background:#f5f7fb; color:#1f2937; }");
        out.println(".header { background:#172554; color:white; padding:25px 40px; }");
        out.println(".header h1 { margin:0; font-size:28px; }");
        out.println(".header p { margin:8px 0 0; color:#cbd5e1; }");
        out.println(".nav { background:white; padding:15px 40px; border-bottom:1px solid #e5e7eb; display:flex; gap:20px; flex-wrap:wrap; }");
        out.println(".nav a { text-decoration:none; color:#334155; font-size:14px; font-weight:bold; }");
        out.println(".nav a:hover { color:#1d4ed8; }");
        out.println(".container { padding:35px 40px; }");
        out.println(".section-title { font-size:22px; margin-bottom:20px; }");
        out.println(".dashboard { display:grid; grid-template-columns:repeat(3,minmax(0,1fr)); gap:22px; }");
        out.println(".card { background:white; padding:25px; border-radius:12px; box-shadow:0 3px 12px rgba(0,0,0,0.08); border:1px solid #e5e7eb; }");
        out.println(".card-title { font-size:15px; color:#64748b; margin-bottom:12px; }");
        out.println(".card-value { font-size:34px; font-weight:bold; color:#1d4ed8; }");
        out.println(".card-description { font-size:13px; color:#64748b; margin-top:8px; }");
        out.println(".footer { text-align:center; padding:25px; margin-top:40px; color:#64748b; font-size:13px; }");
        out.println("@media(max-width:900px) { .dashboard { grid-template-columns:repeat(2,minmax(0,1fr)); } }");
        out.println("@media(max-width:600px) { .dashboard { grid-template-columns:1fr; } .container { padding:25px; } .nav { padding:15px 25px; gap:15px; } .header { padding:25px; } }");
        out.println("</style>");

        out.println("</head>");
        out.println("<body>");

        out.println("<div class='header'>");
        out.println("<h1>Telecom Resource Optimization System</h1>");
        out.println("<p>Network Resource Management Dashboard</p>");
        out.println("</div>");

        // Navigation bar
        out.println("<nav class='nav'>");
        out.println("<a href='/dashboard'>Dashboard</a>");
        out.println("<a href='/network-cells'>Network Cells</a>");
        out.println("<a href='/devices'>Devices</a>");
        out.println("<a href='/subscribers'>Subscribers</a>");
        out.println("<a href='/traffic-flows'>Traffic</a>");
        out.println("<a href='/resource-allocation'>Resource Allocation</a>");
        out.println("<a href='/network-slices'>Network Slices</a>");
        out.println("<a href='/edge-servers'>Edge Servers</a>");
        out.println("<a href='/optimization'>Optimization</a>");
        out.println("<a href='/kpi-monitoring'>KPI Monitoring</a>");
        out.println("<a href='/users'>User Management</a>");
        out.println("<a href='/alerts'>Alerts</a>");
        out.println("<a href='/reports'>Reports</a>");
        out.println("</nav>");

        // Network overview cards
        out.println("<main class='container'>");
        out.println("<div class='section-title'>Network Overview</div>");
        out.println("<div class='dashboard'>");

        createCard(out, "Network Cells", networkCells,
                "Registered network cells");
        createCard(out, "Devices", devices,
                "Registered network devices");
        createCard(out, "Subscribers", subscribers,
                "Total subscribers");
        createCard(out, "Traffic Records", trafficRecords,
                "Recorded traffic data");
        createCard(out, "Alerts", alerts,
                "Network alerts");
        createCard(out, "Optimization Results", optimizationResults,
                "Generated optimization results");
        createCard(out, "KPI Records", kpiRecords,
                "Recorded network performance indicators");

        out.println("</div>");
        out.println("</main>");

        out.println("<div class='footer'>");
        out.println("Java Enterprise Technologies | Telecom Network Management");
        out.println("</div>");

        out.println("</body>");
        out.println("</html>");
    }

    private int getCount(Connection connection, String tableName)
            throws Exception {

        // Table names are fixed constants supplied by this servlet.
        String sql = "SELECT COUNT(*) AS total FROM " + tableName;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                return resultSet.getInt("total");
            }
        }

        return 0;
    }

    private void createCard(
            PrintWriter out,
            String title,
            int value,
            String description) {

        out.println("<div class='card'>");
        out.println("<div class='card-title'>" + title + "</div>");
        out.println("<div class='card-value'>" + value + "</div>");
        out.println("<div class='card-description'>" + description + "</div>");
        out.println("</div>");
    }
}
