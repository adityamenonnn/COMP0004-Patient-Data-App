package uk.ac.ucl.model;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class RecordWriter extends DataExporter
{
  @Override
  public void export(DataFrame dataFrame, Writer writer) throws IOException
  {
    List<String> columns = dataFrame.getColumnNames();

    writer.write(String.join(",", columns));
    writer.write(System.lineSeparator());

    for (int row = 0; row < dataFrame.getRowCount(); row++)
    {
      StringBuilder line = new StringBuilder();
      for (int col = 0; col < columns.size(); col++)
      {
        String cell = dataFrame.getValue(columns.get(col), row);
        line.append(cell != null ? cell : "");
        if (col < columns.size() - 1) line.append(",");
      }
      writer.write(line.toString());
      writer.write(System.lineSeparator());
    }
  }
}
