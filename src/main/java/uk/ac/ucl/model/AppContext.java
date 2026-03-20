package uk.ac.ucl.model;

import java.io.IOException;

// Access to the application's Model instance.
// Data is loaded ONLY once from disk  and then reused.
public class AppContext
{
  private static Model model;

  private AppContext() {}

  public static Model instance() throws IOException
  {
    if (model == null)
    {
      model = Model.getInstance();
      model.initialiseFrom("data/patients100.csv");
    }
    return model;
  }

  public static void reload(String filePath) throws IOException
  {
    if (model == null)
    {
      model = Model.getInstance();
    }
    model.initialiseFrom(filePath);
  }
}