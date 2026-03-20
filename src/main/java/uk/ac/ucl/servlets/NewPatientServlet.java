package uk.ac.ucl.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uk.ac.ucl.model.AppContext;
import uk.ac.ucl.model.Model;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet("/addPatient")
public class NewPatientServlet extends HttpServlet
{
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    try
    {
      request.setAttribute("columnNames", AppContext.instance().getFrame().getColumnNames());
      forward(request, response, "/addPatient.jsp");
    }
    catch (IOException e)
    {
      request.setAttribute("errorMessage", "Error loading : " + e.getMessage());
      forward(request, response, "/addPatient.jsp");
    }
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    try
    {
      Model model = AppContext.instance();
      Map<String, String> incoming = new LinkedHashMap<>();
      for (String col : model.getFrame().getColumnNames())
      {
        String val = request.getParameter(col);
        incoming.put(col, val != null ? val : "");
      }
      model.registerPatient(incoming);
      response.sendRedirect("/patientList");
    }
    catch (IOException e)
    {
      request.setAttribute("errorMessage", "Error adding patient: " + e.getMessage());
      forward(request, response, "/addPatient.jsp");
    }
  }

  private void forward(HttpServletRequest request, HttpServletResponse response, String path) throws IOException, ServletException
  {
    getServletContext().getRequestDispatcher(path).forward(request, response);
  }
}