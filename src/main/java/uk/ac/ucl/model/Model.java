package uk.ac.ucl.model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Model
{
  private static Model instance = null;
  private DataFrame dataFrame;
  private String sourceFile;

  private Model()
  {
    this.dataFrame = new DataFrame();
  }

  public static synchronized Model getInstance()
  {
    if (instance == null)
    {
      instance = new Model();
    }
    return instance;
  }

  public void initialiseFrom(String filePath) throws IOException
  {
    this.dataFrame= new DataLoader().parseCSV(filePath);
    this.sourceFile =filePath;
  }

  public DataFrame getFrame()
  {
    return dataFrame;
  }

  public List<String> listPatientNames()
  {
    List<String> names= new ArrayList<>();
    for (int i = 0; i < dataFrame.getRowCount(); i++)
    {
      String first = dataFrame.getValue("FIRST", i);
      String last  = dataFrame.getValue("LAST", i);
      if (first != null && last != null)
      {
        names.add(first + " " + last);
      }
    }
    return names;
  }

  public PatientSnapshot fetchPatient(int rowIndex)
  {
    if (rowIndex < 0 || rowIndex >= dataFrame.getRowCount())
    {
      return null;
    }
    Map<String, String> fields = new LinkedHashMap<>();
    for (String col : dataFrame.getColumnNames())
    {
      fields.put(col, dataFrame.getValue(col, rowIndex));
    }
    return new PatientSnapshot(rowIndex, fields);
  }

  public void registerPatient(Map<String, String> incoming) throws IOException
  {
    for (String col : dataFrame.getColumnNames())
    {
      dataFrame.addValue(col, incoming.getOrDefault(col, ""));
    }
    flushToDisk();
  }

  public void amendPatient(int rowIndex, Map<String, String> incoming) throws IOException
  {
    for (String col : dataFrame.getColumnNames())
    {
      dataFrame.putValue(col, rowIndex, incoming.getOrDefault(col, ""));
    }
    flushToDisk();
  }

  public void removePatient(int rowIndex) throws IOException
  {
    if (rowIndex < 0 || rowIndex >= dataFrame.getRowCount())
    {
      throw new IOException("Row index out of bounds: " + rowIndex);
    }
    dataFrame.removeRow(rowIndex);
    flushToDisk();
  }

  // Writes current in-memory state back to the original CSV after any mutation.
  private void flushToDisk() throws IOException
  {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(sourceFile)))
    {
      new RecordWriter().export(dataFrame, writer);
    }
  }
}
