
package com.telecom.web;

import com.telecom.dao.UserDAO;
import com.telecom.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/users")
public class UserServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        String action = request.getParameter("action");

        if ("edit".equals(action)) {
            showEditForm(request, response);
            return;
        }

        if ("add".equals(action)) {
            showForm(response, null, "");
            return;
        }

        showUsers(response, request.getParameter("message"));
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        try {
            if ("save".equals(action)) {
                saveUser(request, response);
            } else if ("delete".equals(action)) {
                deleteUser(request, response);
            } else {
                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid action.");
            }
        } catch (NumberFormatException e) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid user ID.");
        }
    }

    private void showUsers(
            HttpServletResponse response,
            String message) throws IOException {

        PrintWriter out = response.getWriter();
        List<User> users = userDAO.getAllUsers();

        pageStart(out, "User Management");

        out.println("<div class='container'>");
        out.println("<h2>User Management</h2>");
        out.println("<p>View and manage registered system users.</p>");

        if (message != null) {
            out.println("<p class='message'>" + escape(message) + "</p>");
        }

        out.println("<a class='button' href='/users?action=add'>+ Add User</a>");
        out.println("<div class='table-wrap'><table>");
        out.println("<thead><tr>");
        out.println("<th>ID</th><th>Username</th><th>Email</th>");
        out.println("<th>Role</th><th>Status</th><th>Created At</th>");
        out.println("<th>Actions</th></tr></thead><tbody>");

        for (User user : users) {
            out.println("<tr>");
            out.println("<td>" + user.getUserId() + "</td>");
            out.println("<td>" + escape(user.getUsername()) + "</td>");
            out.println("<td>" + escape(user.getEmail()) + "</td>");
            out.println("<td>" + escape(user.getRole()) + "</td>");
            out.println("<td>" + escape(user.getStatus()) + "</td>");
            out.println("<td>" +
                    (user.getCreatedAt() == null
                            ? "—"
                            : escape(user.getCreatedAt().toString())) +
                    "</td>");

            out.println("<td class='actions'>");
            out.println("<a class='button edit' href='/users?action=edit&id="
                    + user.getUserId() + "'>Edit</a>");

            out.println("<form method='post' action='/users' "
                    + "onsubmit=\"return confirm('Delete this user?');\">");
            out.println("<input type='hidden' name='action' value='delete'>");
            out.println("<input type='hidden' name='userId' value='"
                    + user.getUserId() + "'>");
            out.println("<button class='button delete' type='submit'>Delete</button>");
            out.println("</form>");

            out.println("</td></tr>");
        }

        if (users.isEmpty()) {
            out.println("<tr><td colspan='7'>No users found.</td></tr>");
        }

        out.println("</tbody></table></div>");
        out.println("<p>Total users: " + users.size() + "</p>");
        out.println("<a href='/dashboard'>Back to Dashboard</a>");
        out.println("</div>");

        pageEnd(out);
    }

    private void showEditForm(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        int id;

        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (Exception e) {
            response.sendError(400, "Invalid user ID.");
            return;
        }

        User found = null;

        for (User user : userDAO.getAllUsers()) {
            if (user.getUserId() == id) {
                found = user;
                break;
            }
        }

        if (found == null) {
            response.sendError(404, "User not found.");
            return;
        }

        showForm(response, found, "");
    }

    private void showForm(
            HttpServletResponse response,
            User user,
            String message) throws IOException {

        boolean editing = user != null;
        PrintWriter out = response.getWriter();

        pageStart(out, editing ? "Edit User" : "Add User");

        out.println("<div class='container form-container'>");
        out.println("<h2>" + (editing ? "Edit User" : "Add User") + "</h2>");

        if (!message.isEmpty()) {
            out.println("<p class='message'>" + escape(message) + "</p>");
        }

        out.println("<form method='post' action='/users'>");
        out.println("<input type='hidden' name='action' value='save'>");
        out.println("<input type='hidden' name='userId' value='"
                + (editing ? user.getUserId() : 0) + "'>");

        out.println("<label>Username</label>");
        out.println("<input name='username' required maxlength='100' value='"
                + (editing ? escape(user.getUsername()) : "") + "'>");

        out.println("<label>Email</label>");
        out.println("<input type='email' name='email' required maxlength='150' value='"
                + (editing ? escape(user.getEmail()) : "") + "'>");

        out.println("<label>" + (editing
                ? "New Password (leave blank to keep current password)"
                : "Password") + "</label>");
        out.println("<input type='password' name='password' "
                + (editing ? "" : "required ")
                + "minlength='8' maxlength='100' autocomplete='new-password'>");

        out.println("<label>Role</label>");
        out.println("<select name='role' required>");
        option(out, "Admin", editing ? user.getRole() : "Operator");
        option(out, "Operator", editing ? user.getRole() : "Operator");
        option(out, "Viewer", editing ? user.getRole() : "Operator");
        out.println("</select>");

        out.println("<label>Status</label>");
        out.println("<select name='status' required>");
        option(out, "Active", editing ? user.getStatus() : "Active");
        option(out, "Inactive", editing ? user.getStatus() : "Active");
        out.println("</select>");

        out.println("<button class='button' type='submit'>Save User</button>");
        out.println(" <a class='button cancel' href='/users'>Cancel</a>");
        out.println("</form></div>");

        pageEnd(out);
    }

    private void option(PrintWriter out, String value, String selected) {
        out.println("<option value='" + escape(value) + "'"
                + (value.equalsIgnoreCase(selected) ? " selected" : "")
                + ">" + escape(value) + "</option>");
    }

    private void saveUser(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        String username = trim(request.getParameter("username"));
        String email = trim(request.getParameter("email"));
        String password = request.getParameter("password");
        String role = request.getParameter("role");
        String status = request.getParameter("status");
        int id = Integer.parseInt(request.getParameter("userId"));

        if (username.isEmpty() || email.isEmpty()
                || role == null || status == null) {
            response.sendError(400, "Please complete all required fields.");
            return;
        }

        if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            response.sendError(400, "Enter a valid email address.");
            return;
        }

        if (!List.of("Admin", "Operator", "Viewer").contains(role)
                || !List.of("Active", "Inactive").contains(status)) {
            response.sendError(400, "Invalid role or status.");
            return;
        }

        User user = new User();
        user.setUserId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setRole(role);
        user.setStatus(status);

        boolean success;

        if (id == 0) {
            if (password == null || password.length() < 8) {
                response.sendError(400,
                        "Password must contain at least 8 characters.");
                return;
            }

            user.setPassword(password);
            success = userDAO.addUser(user);
        } else {
            User existing = null;

            for (User item : userDAO.getAllUsers()) {
                if (item.getUserId() == id) {
                    existing = item;
                    break;
                }
            }

            if (existing == null) {
                response.sendError(404, "User not found.");
                return;
            }

            if (password == null || password.isBlank()) {
                user.setPassword(existing.getPassword());
            } else {
                if (password.length() < 8) {
                    response.sendError(400,
                            "New password must contain at least 8 characters.");
                    return;
                }
                user.setPassword(password);
            }

            success = userDAO.updateUser(user);
        }

        response.sendRedirect("/users?message="
                + (success ? "saved" : "error"));
    }

    private void deleteUser(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        int id = Integer.parseInt(request.getParameter("userId"));

        boolean success = userDAO.deleteUser(id);

        response.sendRedirect("/users?message="
                + (success ? "deleted" : "error"));
    }

    private String trim(String value) {
        return value == null ? "" : value.trim();
    }

    private String escape(String value) {
        if (value == null) return "";

        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    private void pageStart(PrintWriter out, String title) {
        out.println("<!DOCTYPE html><html lang='en'><head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<meta name='viewport' content='width=device-width, initial-scale=1'>");
        out.println("<title>" + escape(title) + "</title>");
        out.println("<style>");
        out.println("body{margin:0;background:#f5f7fb;color:#1f2937;font-family:Arial,sans-serif}");
        out.println("header{background:#172554;color:white;padding:22px 35px}");
        out.println(".container{padding:30px 35px}");
        out.println(".form-container{max-width:650px}");
        out.println(".button{display:inline-block;background:#1d4ed8;color:white;padding:9px 13px;border:0;border-radius:5px;text-decoration:none;cursor:pointer;font-size:13px}");
        out.println(".edit{background:#0f766e}.delete{background:#b91c1c}.cancel{background:#64748b}");
        out.println(".table-wrap{overflow-x:auto;margin-top:20px}");
        out.println("table{width:100%;border-collapse:collapse;background:white}");
        out.println("th,td{padding:12px;text-align:left;border-bottom:1px solid #e5e7eb;font-size:14px}");
        out.println("th{background:#eaf0ff}");
        out.println(".actions{white-space:nowrap}.actions form{display:inline}");
        out.println("label{display:block;margin:16px 0 6px;font-weight:bold}");
        out.println("input,select{width:100%;padding:10px;border:1px solid #cbd5e1;border-radius:5px}");
        out.println("form .button{margin-top:20px}");
        out.println(".message{padding:10px;background:#e0f2fe;border-radius:5px}");
        out.println("</style></head><body>");
        out.println("<header><h1>Telecom Resource Optimization System</h1>");
        out.println("<p>User Management</p></header>");
    }

    private void pageEnd(PrintWriter out) {
        out.println("</body></html>");
    }
}
