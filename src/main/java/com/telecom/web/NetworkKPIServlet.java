
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
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

@WebServlet("/kpi-monitoring")
public class NetworkKPIServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {

            out.println("""
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport"
                          content="width=device-width, initial-scale=1.0">
                    <title>KPI Monitoring | Telecom Resource Optimization</title>
                    <style>
                        * {
                            box-sizing: border-box;
                        }

                        body {
                            margin: 0;
                            padding: 30px;
                            font-family: Arial, sans-serif;
                            background: #f3f6fb;
                            color: #1f2937;
                        }

                        .container {
                            max-width: 1200px;
                            margin: auto;
                        }

                        .header {
                            background: #173b67;
                            color: white;
                            padding: 25px;
                            border-radius: 12px;
                            margin-bottom: 20px;
                        }

                        .header h1 {
                            margin: 0 0 10px;
                        }

                        .header p {
                            margin: 0;
                            color: #e0eaf8;
                        }

                        .actions {
                            display: flex;
                            gap: 12px;
                            flex-wrap: wrap;
                            margin: 20px 0;
                        }

                        .button {
                            display: inline-block;
                            padding: 11px 18px;
                            border-radius: 6px;
                            background: #1769aa;
                            color: white;
                            text-decoration: none;
                            border: none;
                            cursor: pointer;
                            font-size: 14px;
                        }

                        .button:hover {
                            background: #104e83;
                        }

                        .card {
                            background: white;
                            padding: 22px;
                            border-radius: 10px;
                            box-shadow: 0 2px 8px
                                        rgba(0, 0, 0, 0.06);
                            margin-bottom: 20px;
                        }

                        .table-wrapper {
                            overflow-x: auto;
                        }

                        table {
                            width: 100%;
                            border-collapse: collapse;
                            background: white;
                        }

                        th, td {
                            padding: 13px;
                            border-bottom: 1px solid #e5e7eb;
                            text-align: left;
                            white-space: nowrap;
                        }

                        th {
                            background: #e8eff8;
                            color: #173b67;
                        }

                        tr:hover td {
                            background: #f8fafc;
                        }

                        .message {
                            padding: 15px;
                            border-radius: 6px;
                            background: #fff1f0;
                            color: #9b1c1c;
                            margin-bottom: 15px;
                        }

                        .empty {
                            color: #64748b;
                            padding: 20px 0;
                        }

                        footer {
                            margin-top: 25px;
                            color: #64748b;
                            text-align: center;
                            font-size: 13px;
                        }

                        @media (max-width: 600px) {
                            body {
                                padding: 15px;
                            }
                        }
                    </style>
                </head>
                <body>
                <div class="container">

                    <div class="header">
                        <h1>Network KPI Monitoring</h1>
                        <p>
                            Monitor network performance indicators
                            using recorded database information.
                        </p>
                    </div>

                    <div class="actions">
                        <a class="button" href="/dashboard">
                            Back to Dashboard
                        </a>
                        <a class="button" href="/kpi-monitoring">
                            Refresh KPIs
                        </a>
                    </div>

                    <div class="card">
                        <h2>KPI Records</h2>
                        <p>
                            This page displays the existing records
                            from the network_kpis database table.
                        </p>
            """);

            String sql = "SELECT * FROM network_kpis ORDER BY 1 ASC";

            try (Connection connection =
                         DatabaseConnection.getConnection();
                 Statement statement =
                         connection.createStatement();
                 ResultSet resultSet =
                         statement.executeQuery(sql)) {

                ResultSetMetaData metadata =
                        resultSet.getMetaData();

                int columnCount = metadata.getColumnCount();
                int rowCount = 0;

                out.println("<div class='table-wrapper'>");
                out.println("<table>");
                out.println("<thead><tr>");

                for (int i = 1; i <= columnCount; i++) {
                    out.println("<th>"
                            + escapeHtml(metadata.getColumnLabel(i))
                            + "</th>");
                }

                out.println("</tr></thead>");
                out.println("<tbody>");

                while (resultSet.next()) {
                    rowCount++;
                    out.println("<tr>");

                    for (int i = 1; i <= columnCount; i++) {
                        Object value = resultSet.getObject(i);

                        String displayValue =
                                value == null ? "—" : value.toString();

                        out.println("<td>"
                                + escapeHtml(displayValue)
                                + "</td>");
                    }

                    out.println("</tr>");
                }

                out.println("</tbody></table></div>");

                if (rowCount == 0) {
                    out.println("""
                        <p class="empty">
                            No KPI records are available yet.
                        </p>
                    """);
                } else {
                    out.println("<p><strong>Total KPI records: "
                            + rowCount + "</strong></p>");
                }

            } catch (SQLException e) {
                getServletContext().log(
                        "Error loading network KPI records", e);

                out.println("""
                    <div class="message">
                        Unable to load KPI records from the database.
                        Check the database connection and network_kpis
                        table, then try refreshing this page.
                    </div>
                """);
            }

            out.println("""
                    </div>

                    <footer>
                        Java Enterprise Technologies |
                        Telecom Network Resource Optimization System
                    </footer>

                </div>
                </body>
                </html>
            """);
        }
    }

    private String escapeHtml(String value) {
        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
