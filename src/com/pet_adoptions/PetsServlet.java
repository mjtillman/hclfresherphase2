package com.pet_adoptions;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.pet_adoptions.model.DBConnection;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

@WebServlet("/list")
public class PetsServlet extends HttpServlet {

  public PetsServlet() {
    super();
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    try {

      String style = MainServlet.getCSS();

      PrintWriter out = response.getWriter();
      out.println("<html>" + style + "<body>");

      InputStream in = getServletContext().getResourceAsStream("/WEB-INF/config.properties");
      Properties props = new Properties();
      props.load(in);

      DBConnection conn = new DBConnection(props.getProperty("url"), props.getProperty("userid"), props.getProperty("password"));
      Statement stmt = conn.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
      ResultSet rst = stmt.executeQuery("SELECT * FROM pets;");

      out.println("" +
          "<table><tr>" +
          "<th>Name</th>" +
          "<th>Animal</th>" +
          "<th>Color</th>" +
          "<th>Adoption Fee</th>" +
          "</tr>"
      );

      while (rst.next()) {
        out.println("" +
            "<tr><td>" +
            rst.getString("name") +
            "</td><td>" +
            rst.getString("animal") +
            "</td><td>" +
            rst.getString("color") +
            "</td><td>" +
            rst.getBigDecimal("adoption_fee") +
            "</td></tr>"
        );
      }

      out.println("</table>");
      stmt.close();
      out.println("</body></html>");
      conn.closeConnection();

    } catch (ClassNotFoundException | SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    doGet(request, response);
  }
}
