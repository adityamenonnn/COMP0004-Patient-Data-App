package uk.ac.ucl.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uk.ac.ucl.model.DataFrame;
import uk.ac.ucl.model.JSONWriter;
import uk.ac.ucl.model.ModelFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/exportJson")
public class ExportJsonServlet extends HttpServlet
{
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    try
    {
      DataFrame dataFrame = ModelFactory.getModel().getDataFrame();

      // Stream the JSON directly to the browser as a file download.
      response.setContentType("application/json");
      response.setHeader("Content-Disposition", "attachment; filename=\"patients.json\"");

      // Write to a temp file then stream, reusing JSONWriter which works with files.
      // Simpler alternative: build JSON into the response writer directly.
      List<String> columns = dataFrame.getColumnNames();
      int rowCount = dataFrame.getRowCount();
      PrintWriter out = response.getWriter();

      out.println("[");
      for (int row = 0; row < rowCount; row++)
      {
        out.println("  {");
        for (int col = 0; col < columns.size(); col++)
        {
          String key = columns.get(col);
          String value = dataFrame.getValue(key, row);
          String escaped = escape(value != null ? value : "");
          String entry = "    \"" + key + "\": \"" + escaped + "\"";
          if (col < columns.size() - 1) entry += ",";
          out.println(entry);
        }
        out.print("  }");
        if (row < rowCount - 1) out.print(",");
        out.println();
      }
      out.println("]");
    }
    catch (IOException e)
    {
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to export data: " + e.getMessage());
    }
  }

  private String escape(String value)
  {
    return value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
  }
}