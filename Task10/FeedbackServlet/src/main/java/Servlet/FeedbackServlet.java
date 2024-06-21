package Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class FeedbackServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String name = request.getParameter("name");
		String bookName = request.getParameter("bookName");
		String feedback = request.getParameter("feedback");

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
        	connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/feedbackDB", "root", "Preethika@1223");

			// Prepare the SQL statement
			preparedStatement = connection.prepareStatement("INSERT INTO feedback (name, bookName, feedback) VALUES (?, ?, ?)");
			preparedStatement.setString(1, name);
			preparedStatement.setString(2, bookName);
			preparedStatement.setString(3, feedback);

			// Execute the SQL statement
			int rows = preparedStatement.executeUpdate();
			if (rows > 0) {
				out.println("Feedback saved!");
			} else {
				out.println("Failed to save feedback!");
			}
			connection.close();


		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			out.println("Error loading JDBC driver: " + e.getMessage());
		} catch (SQLException e) {
			e.printStackTrace();
			out.println("SQL Error: " + e.getMessage());
		} finally {
			// Close the resources
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
				out.println("Error closing resources: " + e.getMessage());
			}
			if (out != null) {
				out.close();
			}
		}
	}
}
