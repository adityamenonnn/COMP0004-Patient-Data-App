package uk.ac.ucl.model;

import java.util.LinkedHashMap;
import java.util.Map;


public class PatientStatistics
{
  private final DataFrame dataFrame;

  public PatientStatistics(DataFrame dataFrame)
  {
    this.dataFrame = dataFrame;
  }

  public PatientSnapshot findOldestLiving()
  {
    String earliest = null;
    int    foundRow = -1;

    for (int i = 0; i < dataFrame.getRowCount(); i++)
    {
      String dob = birthDateIfAlive(i);
      if (dob == null) continue;

      if (earliest == null || dob.compareTo(earliest) < 0)
      {
        earliest = dob;
        foundRow = i;
      }
    }
    return foundRow >= 0 ? buildSnapshot(foundRow) : null;
  }

  public PatientSnapshot findYoungestLiving()
  {
    String latest   = null;
    int    foundRow = -1;

    for (int i = 0; i < dataFrame.getRowCount(); i++)
    {
      String dob = birthDateIfAlive(i);
      if (dob == null) continue;

      if (latest == null || dob.compareTo(latest) > 0)
      {
        latest  = dob;
        foundRow = i;
      }
    }
    return foundRow >= 0 ? buildSnapshot(foundRow) : null;
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

  public Map<String, Integer> computeAgeSpread()
  {
    // Initialise in display order so the map iterates 0-10 through 90+.
    Map<String, Integer> buckets = new LinkedHashMap<>();
    String[] bands = {"0-10", "10-20", "20-30", "30-40", "40-50",
                      "50-60", "60-70", "70-80", "80-90", "90+"};
    for (String band : bands) buckets.put(band, 0);

    for (int i = 0; i < dataFrame.getRowCount(); i++)
    {
      String dob = birthDateIfAlive(i);
      if (dob == null) continue;

      int age    = 2026 - Integer.parseInt(dob.substring(0, 4));
      String band = age >= 90 ? "90+" : ((age / 10) * 10) + "-" + ((age / 10) * 10 + 10);
      buckets.put(band, buckets.get(band) + 1);
    }
    return buckets;
  }

  public Map<String, Integer> computeGenderBreakdown()
  {
    Map<String, Integer> tally = new LinkedHashMap<>();
    tally.put("Male", 0);
    tally.put("Female", 0);

    for (int i = 0; i < dataFrame.getRowCount(); i++)
    {
      String gender = dataFrame.getValue("GENDER", i);
      if ("M".equalsIgnoreCase(gender))
        tally.put("Male", tally.get("Male") + 1);
      else if ("F".equalsIgnoreCase(gender))
        tally.put("Female", tally.get("Female") + 1);
    }
    return tally;
  }

  // BIRTHDATE is YYYY-MM-DD so lexicographic ordering works for date comparison.
  // Returns null if the patient is deceased or has no birth date on record.
  private String birthDateIfAlive(int row)
  {
    String deathDate = dataFrame.getValue("DEATHDATE", row);
    if (deathDate != null && !deathDate.isEmpty()) return null;

    String birthDate = dataFrame.getValue("BIRTHDATE", row);
    if (birthDate == null || birthDate.isEmpty()) return null;

    return birthDate;
  }

}
