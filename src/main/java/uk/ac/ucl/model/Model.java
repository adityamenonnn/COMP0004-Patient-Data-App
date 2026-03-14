package uk.ac.ucl.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Model
{
  private static Model instance = null;
  private DataFrame dataFrame;
  private String filePath;

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

  public void loadData(String filePath) throws IOException
  {
    this.dataFrame = new DataLoader().load(filePath);
    this.filePath = filePath;
  }

  public DataFrame getDataFrame()
  {
    return dataFrame;
  }

  public void addPatient(Map<String, String> data) throws IOException
  {
    for (String col : dataFrame.getColumnNames())
    {
      dataFrame.addValue(col, data.getOrDefault(col, ""));
    }
    persist();
  }

  public void updatePatient(int row, Map<String, String> data) throws IOException
  {
    for (String col : dataFrame.getColumnNames())
    {
      dataFrame.putValue(col, row, data.getOrDefault(col, ""));
    }
    persist();
  }

  public void deletePatient(int row) throws IOException
  {
    if (row < 0 || row >= dataFrame.getRowCount())
    {
      throw new IOException("Row index out of bounds: " + row);
    }
    dataFrame.removeRow(row);
    persist();
  }

  // Writes current state back to disk after any mutation.
  private void persist() throws IOException
  {
    new CSVWriter().save(dataFrame, filePath);
  }

  public List<String> getPatientNames()
  {
    List<String> names = new ArrayList<>();
    for (int i = 0; i < dataFrame.getRowCount(); i++)
    {
      String first = dataFrame.getValue("FIRST", i);
      String last = dataFrame.getValue("LAST", i);
      if (first != null && last != null)
      {
        names.add(first + " " + last);
      }
    }
    return names;
  }

  public Map<String, String> getPatientData(int row)
  {
    if (row < 0 || row >= dataFrame.getRowCount())
    {
      return null;
    }
    Map<String, String> data = new LinkedHashMap<>();
    for (String col : dataFrame.getColumnNames())
    {
      data.put(col, dataFrame.getValue(col, row));
    }
    return data;
  }

  public String getOldestLivingPatient()
  {
    String name = null;
    String earliest = null;

    for (int i = 0; i < dataFrame.getRowCount(); i++)
    {
      String birthDate = livingPatientBirthDate(i);
      if (birthDate == null) continue;

      if (earliest == null || birthDate.compareTo(earliest) < 0)
      {
        earliest = birthDate;
        name = fullName(i);
      }
    }
    return name != null ? name + " (born " + earliest + ")" : "N/A";
  }

  public String getYoungestLivingPatient()
  {
    String name = null;
    String latest = null;

    for (int i = 0; i < dataFrame.getRowCount(); i++)
    {
      String birthDate = livingPatientBirthDate(i);
      if (birthDate == null) continue;

      if (latest == null || birthDate.compareTo(latest) > 0)
      {
        latest = birthDate;
        name = fullName(i);
      }
    }
    return name != null ? name + " (born " + latest + ")" : "N/A";
  }

  // BIRTHDATE is YYYY-MM-DD so lexicographic ordering works for date comparison.
  // Returns null if the patient is deceased or has no birth date recorded.
  private String livingPatientBirthDate(int row)
  {
    String deathDate = dataFrame.getValue("DEATHDATE", row);
    if (deathDate != null && !deathDate.isEmpty()) return null;

    String birthDate = dataFrame.getValue("BIRTHDATE", row);
    if (birthDate == null || birthDate.isEmpty()) return null;

    return birthDate;
  }

  public Map<String, Integer> getAgeDistribution()
  {
    // Initialise in display order so the map iterates 0-10 through 90+.
    Map<String, Integer> buckets = new LinkedHashMap<>();
    String[] labels = {"0-10", "10-20", "20-30", "30-40", "40-50", "50-60", "60-70", "70-80", "80-90", "90+"};
    for (String label : labels) buckets.put(label, 0);

    for (int i = 0; i < dataFrame.getRowCount(); i++)
    {
      String birthDate = livingPatientBirthDate(i);
      if (birthDate == null) continue;

      int age = 2026 - Integer.parseInt(birthDate.substring(0, 4));
      String bucket = age >= 90 ? "90+" : ((age / 10) * 10) + "-" + ((age / 10) * 10 + 10);
      buckets.put(bucket, buckets.get(bucket) + 1);
    }
    return buckets;
  }

  public List<String[]> getPatientsByCity(String city)
  {
    return filterByColumn("CITY", city);
  }

  public List<String[]> getPatientsByGender(String gender)
  {
    return filterByColumn("GENDER", gender);
  }

  public List<String[]> getPatientsByState(String state)
  {
    return filterByColumn("STATE", state);
  }

  private List<String[]> filterByColumn(String column, String value)
  {
    List<String[]> matches = new ArrayList<>();
    for (int i = 0; i < dataFrame.getRowCount(); i++)
    {
      String cell = dataFrame.getValue(column, i);
      if (value.equalsIgnoreCase(cell))
      {
        matches.add(new String[]{fullName(i), String.valueOf(i)});
      }
    }
    return matches;
  }

  public List<String> searchFor(String keyword)
  {
    List<String> results = new ArrayList<>();
    if (keyword == null || keyword.trim().isEmpty()) return results;

    String term = keyword.toLowerCase();
    for (String col : dataFrame.getColumnNames())
    {
      for (int row = 0; row < dataFrame.getRowCount(); row++)
      {
        String cell = dataFrame.getValue(col, row);
        if (cell != null && cell.toLowerCase().contains(term))
        {
          results.add("Found in " + col + ": " + cell);
        }
      }
    }
    return results;
  }

  private String fullName(int row)
  {
    return dataFrame.getValue("FIRST", row) + " " + dataFrame.getValue("LAST", row);
  }
}