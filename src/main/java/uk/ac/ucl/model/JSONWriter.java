package uk.ac.ucl.model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class JSONWriter
{
  // Writes the DataFrame as a JSON array of objects, one object per patient row.
  public void export(DataFrame dataFrame, String filePath) throws IOException
  {
    List<String> columns = dataFrame.getColumnNames();
    int rowCount = dataFrame.getRowCount();

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath)))
    {
      writer.write("[");
      writer.newLine();

      for (int row = 0; row < rowCount; row++)
      {
        writer.write("  {");
        writer.newLine();

        for (int col = 0; col < columns.size(); col++)
        {
          String key = columns.get(col);
          String value = dataFrame.getValue(key, row);
          String escaped = escape(value != null ? value : "");

          String entry = "    \"" + key + "\": \"" + escaped + "\"";
          if (col < columns.size() - 1) entry += ",";

          writer.write(entry);
          writer.newLine();
        }

        writer.write("  }");
        // No trailing comma on last object
        if (row < rowCount - 1) writer.write(",");
        writer.newLine();
      }

      writer.write("]");
      writer.newLine();
    }
  }

  // Escapes characters that would break JSON string literals.
  private String escape(String value)
  {
    return value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
  }
}