package uk.ac.ucl.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PatientSearch
{
  private final DataFrame dataFrame;

  public PatientSearch(DataFrame dataFrame)
  {
    this.dataFrame = dataFrame;
  }

  public List<PatientSnapshot> findMatching(String keyword)
  {
    List<PatientSnapshot> hits = new ArrayList<>();
    if (keyword == null || keyword.trim().isEmpty()) return hits;

    String term = keyword.toLowerCase();
    // Track rows already added so a patient matching multiple columns appears only once.
    Set<Integer> seen = new HashSet<>();

    for (int i = 0; i < dataFrame.getRowCount(); i++)
    {
      for (String col : dataFrame.getColumnNames())
      {
        String cell = dataFrame.getValue(col, i);
        if (cell != null && cell.toLowerCase().contains(term) && !seen.contains(i))
        {
          hits.add(buildSnapshot(i));
          seen.add(i);
          break;
        }
      }
    }
    return hits;
  }

  public List<PatientSnapshot> findByCity(String city)
  {
    return matchByField("CITY", city);
  }

  public List<PatientSnapshot> findByGender(String gender)
  {
    return matchByField("GENDER", gender);
  }

  public List<PatientSnapshot> findByState(String state)
  {
    return matchByField("STATE", state);
  }

  // Filters by multiple fields at once (AND logic). Only non-blank entries in the map are applied.
  public List<PatientSnapshot> findByFields(Map<String, String> filters)
  {
    Map<String, String> active = filters.entrySet().stream()
      .filter(e -> e.getValue() != null && !e.getValue().trim().isEmpty())
      .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    List<PatientSnapshot> matches = new ArrayList<>();
    for (int i = 0; i < dataFrame.getRowCount(); i++)
    {
      boolean allMatch = true;
      for (Map.Entry<String, String> entry : active.entrySet())
      {
        String cell = dataFrame.getValue(entry.getKey(), i);
        if (!entry.getValue().equalsIgnoreCase(cell))
        {
          allMatch = false;
          break;
        }
      }
      if (allMatch) matches.add(buildSnapshot(i));
    }
    return matches;
  }

  private List<PatientSnapshot> matchByField(String column, String value)
  {
    List<PatientSnapshot> matches = new ArrayList<>();
    for (int i = 0; i < dataFrame.getRowCount(); i++)
    {
      String cell = dataFrame.getValue(column, i);
      if (value.equalsIgnoreCase(cell))
      {
        matches.add(buildSnapshot(i));
      }
    }
    return matches;
  }

  private PatientSnapshot buildSnapshot(int row)
  {
    Map<String, String> fields = new LinkedHashMap<>();
    for (String col : dataFrame.getColumnNames())
    {
      fields.put(col, dataFrame.getValue(col, row));
    }
    return new PatientSnapshot(row, fields);
  }
}
