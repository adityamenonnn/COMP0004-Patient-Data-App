package uk.ac.ucl.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class DataFrame
{
  private LinkedHashMap<String, Column> columns;

  public DataFrame()
  {
    this.columns = new LinkedHashMap<>();
  }

  public void addColumn(Column column)
  {
    if (column != null)
    {
      columns.put(column.getName(), column);
    }
  }

  public ArrayList<String> getColumnNames()
  {
    return new ArrayList<>(columns.keySet());
  }

  public int getRowCount()
  {
    if (columns.isEmpty()) return 0;
    return columns.values().iterator().next().getSize();
  }

  public String getValue(String columnName, int row)
  {
    Column column = columns.get(columnName);
    return column != null ? column.getRowValue(row) : null;
  }

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

  public void removeRow(int index)
  {
    for (Column column : columns.values())
    {
      column.removeRow(index);
    }
  }
}