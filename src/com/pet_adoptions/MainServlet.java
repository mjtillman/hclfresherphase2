package com.pet_adoptions;

import com.pet_adoptions.model.DBConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

@WebServlet("/")
public class MainServlet extends HttpServlet {
  public MainServlet() { super(); }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    try {
      PrintWriter out = response.getWriter();
      out.println(getMainHTML());

      InputStream in = getServletContext().getResourceAsStream("/WEB-INF/config.properties");
      Properties props = new Properties();
      props.load(in);

      DBConnection conn = new DBConnection(props.getProperty("url"), props.getProperty("userid"), props.getProperty("password"));
      Statement stmt = conn.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
      ResultSet rst = stmt.executeQuery("SELECT * FROM pets;");

      out.println("<form action=\"pet_details\" method=\"post\">");
      out.println("<table><tr height=\"80\">");

      while (rst.next()) {
        int id = rst.getInt("id");
        out.println(getFormHTML(id));
        if (id % 4 == 0) {
          out.println("</tr><tr>");
        }
      }

      out.println("</tr></table>");

      stmt.close();
      out.println("<br /><br /><h3><a href=\"list\">...or view all pets</a></h3>");
      out.println("</body></html>");
      conn.closeConnection();

    } catch (ClassNotFoundException | SQLException e) {
      e.printStackTrace();
    }
  }

  private String getMainHTML() {
    return new StringBuilder()
        .append("<html>")
        .append("<head>")
        .append("<meta charset=\"UTF-8\">")
        .append("<title>Adopt A Pet!</title>")
        .append(getCSS())
        .append("</head>")
        .append("<body>")
        .append("<h2>Adopt A Pet!</h2>")
        .append("<br />")
        .append("<h3>Choose a pet...</h3>")
        .toString();
  }

  static String getCSS() {
    return new StringBuilder()
        .append("<style>")
        .append("body {")
        .append("max-width: 700px;")
        .append("margin: auto;")
        .append("align-content: center; }")
        .append("h1, h2, h3 { text-align: center; margin: auto; }")
        .append("table { margin: auto; }")
        .append("</style>")
        .toString();
  }

  private String getFormHTML(int id) {
    return new StringBuilder()
        .append("<td width=\"80\" align=\"center\">")
        .append("<button type=\"submit\" name=\"pet_id\" value=\"")
        .append(id)
        .append("\" class=\"link-button\">")
        .append(id)
        .append("</button></td>")
        .toString();
  }
}
