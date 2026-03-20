package uk.ac.ucl.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uk.ac.ucl.model.AppContext;
import uk.ac.ucl.model.DataVisualiser;
import uk.ac.ucl.model.PatientSnapshot;
import uk.ac.ucl.model.PatientStatistics;

import java.io.IOException;

@WebServlet("/stats")
public class StatisticsServlet extends HttpServlet
{
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    try
    {
      String view = request.getParameter("view");
      PatientStatistics stats = new PatientStatistics(AppContext.instance().getFrame());
      DataVisualiser visualiser = new DataVisualiser();

      if ("oldest".equals(view))
      {
        PatientSnapshot oldest = stats.findOldestLiving();
        request.setAttribute("oldest", oldest);
      }
      else if ("youngest".equals(view))
      {
        PatientSnapshot youngest = stats.findYoungestLiving();
        request.setAttribute("youngest", youngest);
      }
      else if ("agedist".equals(view))
        request.setAttribute("ageDistribution", stats.computeAgeSpread());
      else if ("agechart".equals(view))
        request.setAttribute("chart", visualiser.renderBarChart(
          stats.computeAgeSpread(), "Age Distribution of Living Patients"
        ));
      else if ("genderchart".equals(view))
        request.setAttribute("chart", visualiser.renderPieChart(
          stats.computeGenderBreakdown(), "Gender Distribution"
        ));

      getServletContext().getRequestDispatcher("/stats.jsp").forward(request, response);
    }
    catch (IOException e)
    {
      request.setAttribute("errorMessage", "Error loading data: " + e.getMessage());
      getServletContext().getRequestDispatcher("/stats.jsp").forward(request, response);
    }
  }
}
