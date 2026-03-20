package uk.ac.ucl.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
public class DataLoader
{
  public DataFrame parseCSV(String filePath) throws IOException
  {
    DataFrame table = new DataFrame();

    try (BufferedReader reader = new BufferedReader(new FileReader(filePath)))
    {
      String headerLine = reader.readLine();
      if (headerLine == null)
      {
        return table;
      }

      String[] headers = headerLine.split(",");
      for (String header : headers)
      {
        table.addColumn(new Column(header.trim()));
      }

      String line;
      while ((line = reader.readLine()) != null)
      {
        String[] cells = line.split(",", -1);
        for (int i = 0; i < headers.length; i++)
        {
          table.addValue(headers[i].trim(), i < cells.length ? cells[i].trim() : "");
        }
      }
    }
    return table;
  }
}