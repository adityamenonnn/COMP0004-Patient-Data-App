package uk.ac.ucl.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uk.ac.ucl.model.Model;
import uk.ac.ucl.model.ModelFactory;

import java.io.IOException;
import java.util.List;

@WebServlet("/filter")
public class FilterServlet extends HttpServlet
{
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    try
    {
      String type = request.getParameter("type");
      String value = request.getParameter("value");

      if (type != null && value != null && !value.trim().isEmpty())
      {
        Model model = ModelFactory.getModel();
        List<String[]> results = switch (type)
        {
          case "city"   -> model.getPatientsByCity(value);
          case "gender" -> model.getPatientsByGender(value);
          case "state"  -> model.getPatientsByState(value);
          default       -> null;
        };

        request.setAttribute("results", results);
        request.setAttribute("filterType", type);
        request.setAttribute("filterValue", value);
      }

      getServletContext().getRequestDispatcher("/filterResult.jsp").forward(request, response);
    }
    catch (IOException e)
    {
      request.setAttribute("errorMessage", "Error loading data: " + e.getMessage());
      getServletContext().getRequestDispatcher("/filterResult.jsp").forward(request, response);
    }
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    doGet(request, response);
  }
}