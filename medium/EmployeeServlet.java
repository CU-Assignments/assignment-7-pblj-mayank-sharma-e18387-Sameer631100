import java.io.*;
import java.sql.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class EmployeeServlet extends HttpServlet {
    private String dbURL, dbUser, dbPass;

    public void init() {
        try (InputStream input = getServletContext().getResourceAsStream("/WEB-INF/db-config.properties")) {
            Properties props = new Properties();
            props.load(input);
            dbURL = props.getProperty("db.url");
            dbUser = props.getProperty("db.user");
            dbPass = props.getProperty("db.password");
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("id");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try (Connection conn = DriverManager.getConnection(dbURL, dbUser, dbPass)) {
            String query;
            if (idParam != null && !idParam.isEmpty()) {
                query = "SELECT * FROM employees WHERE id=?";
                try (PreparedStatement ps = conn.prepareStatement(query)) {
                    ps.setInt(1, Integer.parseInt(idParam));
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        out.println("<h3>Employee Details:</h3>");
                        out.printf("ID: %d, Name: %s, Dept: %s, Email: %s<br>",
                                rs.getInt("id"), rs.getString("name"),
                                rs.getString("department"), rs.getString("email"));
                    } else {
                        out.println("<p>No employee found with ID " + idParam + "</p>");
                    }
                }
            } else {
                query = "SELECT * FROM employees";
                try (Statement stmt = conn.createStatement()) {
                    ResultSet rs = stmt.executeQuery(query);
                    out.println("<h3>All Employees:</h3>");
                    while (rs.next()) {
                        out.printf("ID: %d, Name: %s, Dept: %s, Email: %s<br>",
                                rs.getInt("id"), rs.getString("name"),
                                rs.getString("department"), rs.getString("email"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(out);
        }
    }
}
