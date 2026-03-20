package uk.ac.ucl.model;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class JSONWriter extends DataExporter
{
  @Override
  public void export(DataFrame dataFrame, Writer writer) throws IOException
  {
    List<String> columns = dataFrame.getColumnNames();
    int rowCount = dataFrame.getRowCount();

    writer.write("[");
    writer.write(System.lineSeparator());

    for (int row = 0; row < rowCount; row++)
    {
      writer.write("  {");
      writer.write(System.lineSeparator());

      for (int col = 0; col < columns.size(); col++)
      {
        String key = columns.get(col);
        String value= dataFrame.getValue(key, row);
        String escaped= escape(value != null ? value : "");
        String entry = "    \"" + key + "\": \"" + escaped + "\"";
        if (col < columns.size() - 1) entry += ",";
        writer.write(entry);
        writer.write(System.lineSeparator());
      }

      writer.write("  }");
      // No trailing comma on the last object — JSON does not allow it.
      if (row < rowCount - 1) writer.write(",");
      writer.write(System.lineSeparator());
    }

    writer.write("]");
    writer.write(System.lineSeparator());
  }

  // Escapes characters that would break a JSON string literal.
  private String escape(String value)
  {
    return value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
  }
}