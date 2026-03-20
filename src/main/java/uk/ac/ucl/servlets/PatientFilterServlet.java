package uk.ac.ucl.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uk.ac.ucl.model.AppContext;
import uk.ac.ucl.model.PatientSearch;
import uk.ac.ucl.model.PatientSnapshot;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/filter")
public class PatientFilterServlet extends HttpServlet
{
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    try
    {
      Map<String, String> filters = new LinkedHashMap<>();
      filters.put("CITY",   request.getParameter("city"));
      filters.put("GENDER", request.getParameter("gender"));
      filters.put("STATE",  request.getParameter("state"));

      boolean anyFilled = filters.values().stream().anyMatch(v -> v != null && !v.trim().isEmpty());
      if (!anyFilled)
      {
        request.setAttribute("errorMessage", "Please fill in at least one filter field.");
        getServletContext().getRequestDispatcher("/filterResult.jsp").forward(request, response);
        return;
      }

      PatientSearch search= new PatientSearch(AppContext.instance().getFrame());
      List<PatientSnapshot> matches= search.findByFields(filters);

      request.setAttribute("results", matches);
      request.setAttribute("filters", filters);

      getServletContext().getRequestDispatcher("/filterResult.jsp").forward(request, response);
    }
    catch (IOException e)
    {
      request.setAttribute("errorMessage", "Error loading data: " + e.getMessage());
      getServletContext().getRequestDispatcher("/filterResult.jsp").forward(request, response);
    }
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    doGet(request, response);
  }
}
