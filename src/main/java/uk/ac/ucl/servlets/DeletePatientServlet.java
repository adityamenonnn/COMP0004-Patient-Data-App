package uk.ac.ucl.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uk.ac.ucl.model.ModelFactory;

import java.io.IOException;

@WebServlet("/deletePatient")
public class DeletePatientServlet extends HttpServlet
{
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    try
    {
      int row = Integer.parseInt(request.getParameter("row"));
      ModelFactory.getModel().deletePatient(row);
      response.sendRedirect("/patientList");
    }
    catch (NumberFormatException e)
    {
      request.setAttribute("errorMessage", "Invalid patient reference.");
      getServletContext().getRequestDispatcher("/patientDetail.jsp").forward(request, response);
    }
    catch (IOException e)
    {
      request.setAttribute("errorMessage", "Error deleting patient: " + e.getMessage());
      getServletContext().getRequestDispatcher("/patientDetail.jsp").forward(request, response);
    }
  }
}