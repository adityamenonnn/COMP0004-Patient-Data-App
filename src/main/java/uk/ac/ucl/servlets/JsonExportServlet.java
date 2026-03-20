package uk.ac.ucl.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uk.ac.ucl.model.AppContext;
import uk.ac.ucl.model.JSONWriter;

import java.io.IOException;

@WebServlet("/exportJson")
public class JsonExportServlet extends HttpServlet
{
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    try
    {
      response.setContentType("application/json");
      response.setHeader("Content-Disposition", "attachment; filename=\":PATIENTS_LIST.json\""); // CONTENT DISPOSITION IS TO DOWNLOAD FILE IN HTML
      new JSONWriter().export(AppContext.instance().getFrame(), response.getWriter());
    }
    catch (IOException e)
    {
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Export failed: " + e.getMessage());
    }
  }
}