package com.telecom.app;

import com.telecom.web.DashboardServlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Proxy;

public class DashboardVerificationTest {

    public static void main(String[] args) throws Exception {
        System.out.println("=== Testing DashboardServlet with Database ===");

        DashboardServlet servlet = new DashboardServlet();

        // Mock ServletConfig & ServletContext for logging
        ServletContext mockContext = (ServletContext) Proxy.newProxyInstance(
                ServletContext.class.getClassLoader(),
                new Class<?>[]{ServletContext.class},
                (proxy, method, mArgs) -> {
                    if ("log".equals(method.getName())) {
                        System.out.println("[Servlet Log] " + (mArgs != null && mArgs.length > 0 ? mArgs[0] : ""));
                    }
                    return null;
                }
        );

        ServletConfig mockConfig = (ServletConfig) Proxy.newProxyInstance(
                ServletConfig.class.getClassLoader(),
                new Class<?>[]{ServletConfig.class},
                (proxy, method, mArgs) -> {
                    if ("getServletContext".equals(method.getName())) {
                        return mockContext;
                    }
                    return null;
                }
        );

        servlet.init(mockConfig);

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        HttpServletRequest mockRequest = (HttpServletRequest) Proxy.newProxyInstance(
                HttpServletRequest.class.getClassLoader(),
                new Class<?>[]{HttpServletRequest.class},
                (proxy, method, mArgs) -> {
                    if ("getContextPath".equals(method.getName())) return "";
                    if ("getMethod".equals(method.getName())) return "GET";
                    return null;
                }
        );

        HttpServletResponse mockResponse = (HttpServletResponse) Proxy.newProxyInstance(
                HttpServletResponse.class.getClassLoader(),
                new Class<?>[]{HttpServletResponse.class},
                (proxy, method, mArgs) -> {
                    if ("getWriter".equals(method.getName())) return printWriter;
                    return null;
                }
        );

        // Call doGet
        servlet.service(mockRequest, mockResponse);
        printWriter.flush();

        String html = stringWriter.toString();

        if (html.contains("<div class='alert-banner'>")) {
            System.out.println("[RESULT] Database is down -> Error banner rendered cleanly on dashboard.");
            if (html.contains("Database Connection Alert")) {
                System.out.println("  Alert title present: 'Database Connection Alert'");
            }
        } else {
            System.out.println("[RESULT] Database is connected -> Real statistics rendered on dashboard!");
            System.out.println("  Network Cells retrieved: " + html.contains("<div class='card-value'>16</div>"));
        }

        // Print snippet of rendered HTML cards
        int start = html.indexOf("<div class='dashboard'>");
        int end = html.indexOf("</main>");
        if (start != -1 && end != -1) {
            System.out.println("\nDashboard output cards snippet:\n" + html.substring(start, end));
        }
    }
}
