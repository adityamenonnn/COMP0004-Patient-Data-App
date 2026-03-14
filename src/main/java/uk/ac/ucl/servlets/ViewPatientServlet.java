package uk.ac.ucl.servlets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uk.ac.ucl.model.Model;
import uk.ac.ucl.model.ModelFactory;

import java.io.IOException;
import java.util.Map;

@WebServlet("/viewPatient")
public class ViewPatientServlet extends HttpServlet
{
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    try
    {
      String rowParam = request.getParameter("row");
      int row = Integer.parseInt(rowParam);

      Model model = ModelFactory.getModel();
      Map<String, String> patientData = model.getPatientData(row);

      if (patientData == null)
      {
        request.setAttribute("errorMessage", "Patient not found.");
      }
      else
      {
        request.setAttribute("patientData", patientData);
      }

      ServletContext context = getServletContext();
      RequestDispatcher dispatch = context.getRequestDispatcher("/patientDetail.jsp");
      dispatch.forward(request, response);
    }
    catch (NumberFormatException e)
    {
      request.setAttribute("errorMessage", "Invalid patient reference.");
      ServletContext context = getServletContext();
      RequestDispatcher dispatch = context.getRequestDispatcher("/patientDetail.jsp");
      dispatch.forward(request, response);
    }
    catch (IOException e)
    {
      request.setAttribute("errorMessage", "Error loading data: " + e.getMessage());
      ServletContext context = getServletContext();
      RequestDispatcher dispatch = context.getRequestDispatcher("/patientDetail.jsp");
      dispatch.forward(request, response);
    }
  }
}
