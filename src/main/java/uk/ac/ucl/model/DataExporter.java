package uk.ac.ucl.model;

import java.io.IOException;
import java.io.Writer;

// Common contract for classes that export a DataFrame to a character stream.
public abstract class DataExporter
{
  public abstract void export(DataFrame dataFrame, Writer writer) throws IOException;
}
