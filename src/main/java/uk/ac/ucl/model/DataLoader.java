package uk.ac.ucl.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class DataLoader
{
  /**
   * Loads data from a CSV file into a DataFrame
   * The first row of the CSV file is treated as column headers
   * Subsequent rows are treated as data
   */
  public DataFrame load(String filePath) throws IOException
  {
    DataFrame dataFrame = new DataFrame();

    BufferedReader reader = new BufferedReader(new FileReader(filePath));

    // First line contains column headers
    String headerLine = reader.readLine();
    if (headerLine == null)
    {
      reader.close();
      System.out.println("CSV file is empty: " + filePath);
      return dataFrame;
    }

    String[] columnNames = headerLine.split(",");

    // Create an empty Column for each name and add to DataFrame
    for (String name : columnNames)
    {
      dataFrame.addColumn(new Column(name.trim()));
    }

    // Read the rest of the lines (actual data)
    String line;
    while ((line = reader.readLine()) != null)
    {
      String[] values = line.split(",", -1); // -1 keeps empty trailing values

      for (int i = 0; i < columnNames.length; i++)
      {
        if (i < values.length)
        {
          dataFrame.addValue(columnNames[i].trim(), values[i].trim());
        }
        else
        {
          dataFrame.addValue(columnNames[i].trim(), ""); // blank if missing
        }
      }
    }

    reader.close();
    return dataFrame;
  }
}



