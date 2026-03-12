package uk.ac.ucl.model;

import java.util.ArrayList;

public class Column
{
  private String name;
  private ArrayList<String> rows;

  /**
   * Constructor
   */
  public Column(String name)
  {
    this.name = name;
    this.rows = new ArrayList<>();
  }

  /**
   * Gets the name of the column (HEADER)
   */
  public String getName()
  {
    return name;
  }

  /**
   * Gets the number of rows in the column
   */
  public int getSize()
  {
    return rows.size();
  }

  /**
   * Gets the value at a specific row index
   */
  public String getRowValue(int index)
  {
    if (index >= 0 && index < rows.size())
    {
      return rows.get(index);
    }
    return null;
  }

  /**
   * Sets the value at a specific row index
   */
  public void setRowValue(int index, String value)
  {
    if (index >= 0 && index < rows.size())
    {
      rows.set(index, value);
    }
  }

  /**
   * Adds a new row value to the column
   **/
  public void addRowValue(String value)
  {
    rows.add(value);
  }
}

