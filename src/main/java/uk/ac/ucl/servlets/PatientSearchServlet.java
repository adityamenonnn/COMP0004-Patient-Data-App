package uk.ac.ucl.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uk.ac.ucl.model.AppContext;
import uk.ac.ucl.model.PatientSearch;

import java.io.IOException;

@WebServlet("/runsearch")
public class PatientSearchServlet extends HttpServlet
{
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    doPost(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    String term = request.getParameter("searchstring");
    try
    {
      if (term == null || term.trim().isEmpty())
      {
        request.setAttribute("errorMessage", "Please enter a search term.");
      }
      else
      {
        PatientSearch search= new PatientSearch(AppContext.instance().getFrame());
        request.setAttribute("results", search.findMatching(term));
      }
      request.setAttribute("mode", request.getParameter("mode"));
      getServletContext().getRequestDispatcher("/searchResult.jsp").forward(request, response);
    }
    catch (IOException e)
    {
      request.setAttribute("errorMessage", "Error loading data: " + e.getMessage());
      getServletContext().getRequestDispatcher("/searchResult.jsp").forward(request, response);
    }
  }
}
