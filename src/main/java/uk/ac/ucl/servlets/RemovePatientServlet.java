package uk.ac.ucl.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uk.ac.ucl.model.AppContext;

import java.io.IOException;

@WebServlet("/deletePatient")
public class RemovePatientServlet extends HttpServlet
{
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    try
    {
      int rowIndex= Integer.parseInt(request.getParameter("row"));
      AppContext.instance().removePatient(rowIndex);
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