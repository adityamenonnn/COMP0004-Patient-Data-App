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

@WebServlet("/editPatient")
public class EditPatientServlet extends HttpServlet
{
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    try
    {
      int row = Integer.parseInt(request.getParameter("row"));
      Map<String, String> patientData = ModelFactory.getModel().getPatientData(row);

      if (patientData == null)
      {
        request.setAttribute("errorMessage", "Patient not found.");
      }
      else
      {
        request.setAttribute("patientData", patientData);
        request.setAttribute("row", row);
      }
      forward(request, response, "/editPatient.jsp");
    }
    catch (NumberFormatException e)
    {
      request.setAttribute("errorMessage", "Invalid patient reference.");
      forward(request, response, "/editPatient.jsp");
    }
    catch (IOException e)
    {
      request.setAttribute("errorMessage", "Error loading patient: " + e.getMessage());
      forward(request, response, "/editPatient.jsp");
    }
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    try
    {
      int row = Integer.parseInt(request.getParameter("row"));
      Model model = ModelFactory.getModel();

      Map<String, String> data = new LinkedHashMap<>();
      for (String col : model.getDataFrame().getColumnNames())
      {
        String val = request.getParameter(col);
        data.put(col, val != null ? val : "");
      }

      model.updatePatient(row, data);
      response.sendRedirect("/viewPatient?row=" + row);
    }
    catch (NumberFormatException e)
    {
      request.setAttribute("errorMessage", "Invalid patient reference.");
      forward(request, response, "/editPatient.jsp");
    }
    catch (IOException e)
    {
      request.setAttribute("errorMessage", "Error saving patient: " + e.getMessage());
      forward(request, response, "/editPatient.jsp");
    }
  }

  private void forward(HttpServletRequest request, HttpServletResponse response, String path) throws IOException, ServletException
  {
    getServletContext().getRequestDispatcher(path).forward(request, response);
  }
}