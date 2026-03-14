package uk.ac.ucl.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uk.ac.ucl.model.Model;
import uk.ac.ucl.model.ModelFactory;

import java.io.IOException;

@WebServlet("/stats")
public class StatsServlet extends HttpServlet
{
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    try
    {
      String view = request.getParameter("view");
      Model model = ModelFactory.getModel();

      if ("oldest".equals(view))
        request.setAttribute("oldest", model.getOldestLivingPatient());
      else if ("youngest".equals(view))
        request.setAttribute("youngest", model.getYoungestLivingPatient());
      else if ("agedist".equals(view))
        request.setAttribute("ageDistribution", model.getAgeDistribution());

      getServletContext().getRequestDispatcher("/stats.jsp").forward(request, response);
    }
    catch (IOException e)
    {
      request.setAttribute("errorMessage", "Error loading data: " + e.getMessage());
      getServletContext().getRequestDispatcher("/stats.jsp").forward(request, response);
    }
  }
}