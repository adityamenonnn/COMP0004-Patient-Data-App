package uk.ac.ucl.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class DataFrame
{
  private LinkedHashMap<String, Column> columns;

  /**
   * Constructor for DataFrame
   */
  public DataFrame()
  {
    this.columns = new LinkedHashMap<>();
  }

  /**
   * Adds a column to the DataFrame
   * @param column the Column object to add
   */
  public void addColumn(Column column)
  {
    if (column != null)
    {
      columns.put(column.getName(), column); /** this will be like a dict where the column name is the key and the Column objects are the values */
    }
  }

  /**
   * Gets a list of column names in the order they were added
   */
  public ArrayList<String> getColumnNames()
  {
    return new ArrayList<>(columns.keySet());
  }

  /**
   * Gets the number of rows in the DataFrame
   * All columns should have the same number of rows
   */
  public int getRowCount()
  {
    if (columns.isEmpty()) /** buuilt in func**/
    {
      return 0;
    }
    // Get the first column's row count (assuming all columns have same number of rows)
    Column firstColumn = columns.values().iterator().next();
    return firstColumn.getSize();
  }

  /**
   * Gets a value from a specific column at a specific row
   */
  public String getValue(String columnName, int row)
  {
    Column column = columns.get(columnName);  /** searches in the dict for the column name and then the whole row*/
    if (column != null)
    {
      return column.getRowValue(row); /** row is basicaly index as modeled in column.java*/
    }
    return null;
  }

  /**
   * Sets a value in a specific column at a specific row
   */
  public boolean putValue(String columnName, int row, String value)
  {
    Column column = columns.get(columnName);
    if (column != null)
    {
      column.setRowValue(row, value);
      return true;
    }
    return false;
  }

  /**
   * Adds a value to the end of a column
   */
  public boolean addValue(String columnName, String value)
  {
    Column column = columns.get(columnName);
    if (column != null)
    {
      column.addRowValue(value);
      return true;
    }
    return false;
  }
}

