package uk.ac.ucl.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uk.ac.ucl.model.AppContext;

import java.io.IOException;
import java.util.Set;

@WebServlet("/selectDataset")
public class SelectDatasetServlet extends HttpServlet
{
  // for user to choose what doc to load
  private static final Set<String> ALLOWED = Set.of(
    "patients100.csv",
    "patients10000.csv",
    "patients100000.csv"
  );

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
  {
    String file = request.getParameter("dataset");
    if (file == null || !ALLOWED.contains(file))
    {
      response.sendRedirect(request.getContextPath() + "/");
      return;
    }
    AppContext.reload("data/" + file);
    request.getSession().setAttribute("currentDataset", file);
    response.sendRedirect(request.getContextPath() + "/");
  }
}