package uk.ac.ucl.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uk.ac.ucl.model.Model;
import uk.ac.ucl.model.ModelFactory;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet("/addPatient")
public class AddPatientServlet extends HttpServlet
{
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    try
    {
      request.setAttribute("columnNames", ModelFactory.getModel().getDataFrame().getColumnNames());
      forward(request, response, "/addPatient.jsp");
    }
    catch (IOException e)
    {
      request.setAttribute("errorMessage", "Error loading form: " + e.getMessage());
      forward(request, response, "/addPatient.jsp");
    }
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    try
    {
      Model model = ModelFactory.getModel();
      Map<String, String> data = new LinkedHashMap<>();
      for (String col : model.getDataFrame().getColumnNames())
      {
        String val = request.getParameter(col);
        data.put(col, val != null ? val : "");
      }
      model.addPatient(data);
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