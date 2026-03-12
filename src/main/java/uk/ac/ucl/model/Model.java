package uk.ac.ucl.model;

import java.io.IOException;

public class Model
{
  private static Model instance = null;
  private DataFrame dataFrame;

  /**
   * Private constructor - prevents direct instantiation
   */
  private Model()
  {
    this.dataFrame = new DataFrame();
  }

  /**
   * Gets the singleton instance of the Model
   * Creates it if it doesn't exist
   * @return the single Model instance
   */
  public static synchronized Model getInstance()
  {
    if (instance == null)
    {
      instance = new Model();
    }
    return instance;
  }

  /**
   * Loads patient data from a CSV file into the DataFrame
   * @param filePath the path to the CSV file
   * @throws IOException if the file cannot be read
   */
  public void loadData(String filePath) throws IOException
  {
    DataLoader loader = new DataLoader();
    this.dataFrame = loader.load(filePath);
  }

  /**
   * Gets the DataFrame containing all data
   * @return the DataFrame
   */
  public DataFrame getDataFrame()
  {
    return dataFrame;
  }

  /**
   * Gets a list of patient names from the DataFrame
   * @return a list of names from the FIRST column
   */
  public java.util.List<String> getPatientNames()
  {
    java.util.List<String> names = new java.util.ArrayList<>();
    if (dataFrame.getRowCount() > 0)
    {
      for (int i = 0; i < dataFrame.getRowCount(); i++)
      {
        String firstName = dataFrame.getValue("FIRST", i);
        String lastName = dataFrame.getValue("LAST", i);
        if (firstName != null && lastName != null)
        {
          names.add(firstName + " " + lastName);
        }
      }
    }
    return names;
  }

  /**
   * Searches for a keyword in the DataFrame
   * @param keyword the search term
   * @return a list of matching results
   */
  public java.util.List<String> searchFor(String keyword)
  {
    java.util.List<String> results = new java.util.ArrayList<>();

    if (keyword == null || keyword.trim().isEmpty())
    {
      return results;
    }

    String searchTerm = keyword.toLowerCase();

    // Search through all rows and columns
    for (String columnName : dataFrame.getColumnNames())
    {
      for (int row = 0; row < dataFrame.getRowCount(); row++)
      {
        String value = dataFrame.getValue(columnName, row);
        if (value != null && value.toLowerCase().contains(searchTerm))
        {
          results.add("Found in " + columnName + ": " + value);
        }
      }
    }

    return results;
  }
}
