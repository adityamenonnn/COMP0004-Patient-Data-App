package uk.ac.ucl.model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVWriter
{
  public void save(DataFrame dataFrame, String filePath) throws IOException
  {
    List<String> columns = dataFrame.getColumnNames();

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath)))
    {
      writer.write(String.join(",", columns));
      writer.newLine();

      for (int row = 0; row < dataFrame.getRowCount(); row++)
      {
        StringBuilder line = new StringBuilder();
        for (int col = 0; col < columns.size(); col++)
        {
          String value = dataFrame.getValue(columns.get(col), row);
          line.append(value != null ? value : "");
          if (col < columns.size() - 1) line.append(",");
        }
        writer.write(line.toString());
        writer.newLine();
      }
    }
  }
}