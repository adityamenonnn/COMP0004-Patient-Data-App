package uk.ac.ucl.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uk.ac.ucl.model.AppContext;
import uk.ac.ucl.model.PatientSnapshot;

import java.io.IOException;

@WebServlet("/viewPatient")
public class PatientDetailServlet extends HttpServlet
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

      getServletContext().getRequestDispatcher("/patientDetail.jsp").forward(request, response);
    }
    catch (NumberFormatException e)
    {
      request.setAttribute("errorMessage", "Invalid  reference.");
      getServletContext().getRequestDispatcher("/patientDetail.jsp").forward(request, response);
    }
    catch (IOException e)
    {
      request.setAttribute("errorMessage", "Error loading data: " + e.getMessage());
      getServletContext().getRequestDispatcher("/patientDetail.jsp").forward(request, response);
    }
  }
}