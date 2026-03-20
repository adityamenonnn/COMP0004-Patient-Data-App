package uk.ac.ucl.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uk.ac.ucl.model.AppContext;
import uk.ac.ucl.model.Model;
import uk.ac.ucl.model.PatientSnapshot;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet("/editPatient")
public class AmendPatientServlet extends HttpServlet
{
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    try
    {
      int rowIndex = Integer.parseInt(request.getParameter("row"));
      PatientSnapshot patient = AppContext.instance().fetchPatient(rowIndex);

      if (patient == null)
        request.setAttribute("errorMessage", "Patient not found.");
      else
        request.setAttribute("patient", patient);

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

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    try
    {
      int rowIndex = Integer.parseInt(request.getParameter("row"));
      Model model  = AppContext.instance();

      Map<String, String> incoming = new LinkedHashMap<>();
      for (String col : model.getFrame().getColumnNames())
      {
        String val = request.getParameter(col);
        incoming.put(col, val != null ? val : "");
      }

      model.amendPatient(rowIndex, incoming);
      response.sendRedirect("/viewPatient?row=" + rowIndex);
    }
    catch (NumberFormatException e)
    {
      request.setAttribute("errorMessage", "Invalid  reference.");
      forward(request, response, "/editPatient.jsp");
    }
    catch (IOException e)
    {
      request.setAttribute("errorMessage", "Error saving : " + e.getMessage());
      forward(request, response, "/editPatient.jsp");
    }
  }

  private void forward(HttpServletRequest request, HttpServletResponse response, String path) throws IOException, ServletException
  {
    getServletContext().getRequestDispatcher(path).forward(request, response);
  }
}