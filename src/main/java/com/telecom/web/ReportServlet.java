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

@WebServlet("/reports")
public class ReportServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        int networkCells = 0;
        int trafficRecords = 0;
        int resourceAllocations = 0;
        int optimizationResults = 0;
        int kpiRecords = 0;
        int alerts = 0;
        int openAlerts = 0;
        int resolvedAlerts = 0;

        double averageTrafficLoad = 0;
        double averageLatency = 0;
        double averageAvailability = 0;

        try (Connection connection =
                     DatabaseConnection.getConnection()) {

            networkCells =
                    getCount(connection, "network_cells");

            trafficRecords =
                    getCount(connection, "network_traffic");

            resourceAllocations =
                    getCount(connection, "resource_allocation");

            optimizationResults =
                    getCount(connection, "optimization_results");

            kpiRecords =
                    getCount(connection, "network_kpis");

            alerts =
                    getCount(connection, "alerts");

            openAlerts =
                    getStatusCount(
                            connection,
                            "OPEN"
                    );

            resolvedAlerts =
                    getStatusCount(
                            connection,
                            "RESOLVED"
                    );

            averageTrafficLoad =
                    getAverage(
                            connection,
                            "network_traffic",
                            "traffic_load"
                    );

            averageLatency =
                    getAverage(
                            connection,
                            "network_kpis",
                            "latency"
                    );

            averageAvailability =
                    getAverage(
                            connection,
                            "network_kpis",
                            "availability"
                    );

        } catch (Exception e) {

            e.printStackTrace();
        }

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");

        out.println("<meta charset='UTF-8'>");

        out.println(
                "<meta name='viewport' " +
                "content='width=device-width, initial-scale=1.0'>"
        );

        out.println("<title>Network Reports</title>");

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
        out.println("margin-bottom: 25px;");
        out.println("text-decoration: none;");
        out.println("color: #1d4ed8;");
        out.println("font-weight: bold;");
        out.println("}");

        out.println(".reports {");
        out.println("display: grid;");
        out.println("grid-template-columns: repeat(3, 1fr);");
        out.println("gap: 20px;");
        out.println("}");

        out.println(".card {");
        out.println("background: white;");
        out.println("padding: 22px;");
        out.println("border-radius: 10px;");
        out.println("box-shadow: 0 3px 12px rgba(0,0,0,0.08);");
        out.println("border: 1px solid #e5e7eb;");
        out.println("}");

        out.println(".title {");
        out.println("font-size: 14px;");
        out.println("color: #64748b;");
        out.println("margin-bottom: 10px;");
        out.println("}");

        out.println(".value {");
        out.println("font-size: 28px;");
        out.println("font-weight: bold;");
        out.println("color: #1d4ed8;");
        out.println("}");

        out.println(".section {");
        out.println("margin-top: 30px;");
        out.println("margin-bottom: 15px;");
        out.println("font-size: 21px;");
        out.println("font-weight: bold;");
        out.println("}");

        out.println(".footer {");
        out.println("text-align: center;");
        out.println("margin-top: 35px;");
        out.println("padding: 20px;");
        out.println("color: #64748b;");
        out.println("}");

        out.println("@media (max-width: 900px) {");

        out.println(".reports {");
        out.println("grid-template-columns: repeat(2, 1fr);");
        out.println("}");

        out.println("}");

        out.println("@media (max-width: 600px) {");

        out.println(".reports {");
        out.println("grid-template-columns: 1fr;");
        out.println("}");

        out.println(".container {");
        out.println("padding: 25px;");
        out.println("}");

        out.println("}");

        out.println("</style>");

        out.println("</head>");

        out.println("<body>");

        out.println("<div class='header'>");

        out.println("<h1>Network Reports</h1>");

        out.println(
                "<p>Telecom Network Performance Summary</p>"
        );

        out.println("</div>");

        out.println("<div class='container'>");

        out.println(
                "<a class='back' href='/dashboard'>" +
                "← Back to Dashboard" +
                "</a>"
        );

        out.println("<div class='section'>");

        out.println("Network Summary");

        out.println("</div>");

        out.println("<div class='reports'>");

        createCard(
                out,
                "Total Network Cells",
                String.valueOf(networkCells)
        );

        createCard(
                out,
                "Total Traffic Records",
                String.valueOf(trafficRecords)
        );

        createCard(
                out,
                "Resource Allocations",
                String.valueOf(resourceAllocations)
        );

        createCard(
                out,
                "Optimization Results",
                String.valueOf(optimizationResults)
        );

        createCard(
                out,
                "KPI Records",
                String.valueOf(kpiRecords)
        );

        createCard(
                out,
                "Total Alerts",
                String.valueOf(alerts)
        );

        out.println("</div>");

        out.println("<div class='section'>");

        out.println("Alert Summary");

        out.println("</div>");

        out.println("<div class='reports'>");

        createCard(
                out,
                "Open Alerts",
                String.valueOf(openAlerts)
        );

        createCard(
                out,
                "Resolved Alerts",
                String.valueOf(resolvedAlerts)
        );

        out.println("</div>");

        out.println("<div class='section'>");

        out.println("Performance Summary");

        out.println("</div>");

        out.println("<div class='reports'>");

        createCard(
                out,
                "Average Traffic Load",
                String.format(
                        "%.2f%%",
                        averageTrafficLoad
                )
        );

        createCard(
                out,
                "Average Latency",
                String.format(
                        "%.2f",
                        averageLatency
                )
        );

        createCard(
                out,
                "Average Availability",
                String.format(
                        "%.2f%%",
                        averageAvailability
                )
        );

        out.println("</div>");

        out.println("</div>");

        out.println("<div class='footer'>");

        out.println(
                "Java Enterprise Technologies | " +
                "Telecom Network Management"
        );

        out.println("</div>");

        out.println("</body>");

        out.println("</html>");
    }

    private int getCount(
            Connection connection,
            String tableName)
            throws Exception {

        String sql =
                "SELECT COUNT(*) AS total FROM "
                + tableName;

        try (
                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            if (resultSet.next()) {

                return resultSet.getInt("total");
            }
        }

        return 0;
    }

    private int getStatusCount(
            Connection connection,
            String status)
            throws Exception {

        String sql =
                "SELECT COUNT(*) AS total " +
                "FROM alerts WHERE status = ?";

        try (
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, status);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {

                    return resultSet.getInt("total");
                }
            }
        }

        return 0;
    }

    private double getAverage(
            Connection connection,
            String tableName,
            String columnName)
            throws Exception {

        String sql =
                "SELECT AVG(" +
                columnName +
                ") AS average_value FROM " +
                tableName;

        try (
                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            if (resultSet.next()) {

                return resultSet.getDouble(
                        "average_value"
                );
            }
        }

        return 0;
    }

    private void createCard(
            PrintWriter out,
            String title,
            String value) {

        out.println("<div class='card'>");

        out.println(
                "<div class='title'>" +
                title +
                "</div>"
        );

        out.println(
                "<div class='value'>" +
                value +
                "</div>"
        );

        out.println("</div>");
    }
}