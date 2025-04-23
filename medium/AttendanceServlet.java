import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class AttendanceServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String date = request.getParameter("date");
        String status = request.getParameter("status");

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/yourdatabase", "root", "yourpassword")) {
            String sql = "INSERT INTO attendance (id, name, date, status) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setString(3, date);
            ps.setString(4, status);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new ServletException("DB error", e);
        }

        RequestDispatcher rd = request.getRequestDispatcher("attendance-success.jsp");
        rd.forward(request, response);
    }
}
