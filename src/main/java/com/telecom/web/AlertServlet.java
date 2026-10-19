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

@WebServlet("/alerts")
public class AlertServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        String sql =
                "SELECT * FROM alerts " +
                "ORDER BY alert_id ASC";

        int alertCount = 0;

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");

        out.println("<meta charset='UTF-8'>");

        out.println(
                "<meta name='viewport' " +
                "content='width=device-width, initial-scale=1.0'>"
        );

        out.println("<title>Network Alerts</title>");

        out.println("<style>");

        out.println("* { box-sizing: border-box; }");

        out.println("body {");
        out.println("margin: 0;");
        out.println("font-family: Arial, sans-serif;");
        out.println("background: #f5f7fb;");
        out.println("color: #1f2937;");
        out.println("}");

        out.println(".header {");
        out.println("background: #172554;");
        out.println("color: white;");
        out.println("padding: 25px 40px;");
        out.println("}");

        out.println(".header h1 {");
        out.println("margin: 0;");
        out.println("font-size: 28px;");
        out.println("}");

        out.println(".header p {");
        out.println("margin: 8px 0 0;");
        out.println("color: #cbd5e1;");
        out.println("}");

        out.println(".container {");
        out.println("padding: 35px 40px;");
        out.println("}");

        out.println(".back {");
        out.println("display: inline-block;");
        out.println("margin-bottom: 20px;");
        out.println("text-decoration: none;");
        out.println("color: #1d4ed8;");
        out.println("font-weight: bold;");
        out.println("}");

        out.println(".summary {");
        out.println("background: white;");
        out.println("padding: 18px 20px;");
        out.println("margin-bottom: 20px;");
        out.println("border-radius: 8px;");
        out.println("box-shadow: 0 3px 12px rgba(0,0,0,0.08);");
        out.println("}");

        out.println(".summary strong {");
        out.println("color: #1d4ed8;");
        out.println("}");

        out.println(".table-container {");
        out.println("overflow-x: auto;");
        out.println("}");

        out.println("table {");
        out.println("width: 100%;");
        out.println("border-collapse: collapse;");
        out.println("background: white;");
        out.println("box-shadow: 0 3px 12px rgba(0,0,0,0.08);");
        out.println("}");

        out.println("th, td {");
        out.println("padding: 12px;");
        out.println("border: 1px solid #e5e7eb;");
        out.println("text-align: left;");
        out.println("}");

        out.println("th {");
        out.println("background: #1d4ed8;");
        out.println("color: white;");
        out.println("}");

        out.println("tr:nth-child(even) {");
        out.println("background: #f8fafc;");
        out.println("}");

        out.println(".empty {");
        out.println("text-align: center;");
        out.println("padding: 25px;");
        out.println("color: #64748b;");
        out.println("}");

        out.println(".footer {");
        out.println("margin-top: 30px;");
        out.println("padding: 20px;");
        out.println("text-align: center;");
        out.println("color: #64748b;");
        out.println("}");

        out.println("</style>");

        out.println("</head>");

        out.println("<body>");

        // Header
        out.println("<div class='header'>");

        out.println("<h1>Network Alerts</h1>");

        out.println(
                "<p>Telecom Network Alert Monitoring</p>"
        );

        out.println("</div>");

        // Main container
        out.println("<div class='container'>");

        out.println(
                "<a class='back' href='/dashboard'>" +
                "← Back to Dashboard" +
                "</a>"
        );

        // Database query
        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            // First get all rows and display count
            java.util.List<String[]> alerts =
                    new java.util.ArrayList<>();

            while (resultSet.next()) {

                alertCount++;

                String[] alert = {
                        String.valueOf(
                                resultSet.getInt("alert_id")
                        ),

                        String.valueOf(
                                resultSet.getInt("cell_id")
                        ),

                        resultSet.getString("alert_type"),

                        resultSet.getString("alert_message"),

                        resultSet.getString("severity"),

                        resultSet.getString("status"),

                        String.valueOf(
                                resultSet.getTimestamp("created_time")
                        )
                };

                alerts.add(alert);
            }

            out.println("<div class='summary'>");

            out.println(
                    "Total Network Alerts: " +
                    "<strong>" +
                    alertCount +
                    "</strong>"
            );

            out.println("</div>");

            out.println("<div class='table-container'>");

            out.println("<table>");

            out.println("<tr>");

            out.println("<th>Alert ID</th>");
            out.println("<th>Cell ID</th>");
            out.println("<th>Alert Type</th>");
            out.println("<th>Alert Message</th>");
            out.println("<th>Severity</th>");
            out.println("<th>Status</th>");
            out.println("<th>Created Time</th>");

            out.println("</tr>");

            for (String[] alert : alerts) {

                out.println("<tr>");

                for (String value : alert) {

                    out.println(
                            "<td>" +
                            value +
                            "</td>"
                    );
                }

                out.println("</tr>");
            }

            if (alertCount == 0) {

                out.println("<tr>");

                out.println(
                        "<td colspan='7' class='empty'>" +
                        "No network alerts found." +
                        "</td>"
                );

                out.println("</tr>");
            }

            out.println("</table>");

            out.println("</div>");

        } catch (Exception e) {

            out.println("<div class='summary'>");

            out.println(
                    "<strong>Error loading network alerts.</strong>"
            );

            out.println("</div>");

            e.printStackTrace();
        }

        out.println(
                "<div class='summary' style='margin-top:20px;'>" +
                "Network alerts are retrieved from the " +
                "telecom_optimization database." +
                "</div>"
        );

        out.println("</div>");

        // Footer
        out.println("<div class='footer'>");

        out.println(
                "Java Enterprise Technologies | " +
                "Telecom Network Management"
        );

        out.println("</div>");

        out.println("</body>");

        out.println("</html>");
    }
}