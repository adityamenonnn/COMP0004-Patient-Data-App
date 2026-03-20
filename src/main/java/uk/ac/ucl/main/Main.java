package uk.ac.ucl.main;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.*;

import org.apache.catalina.Context;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

public class Main
{
  private static final int    DEFAULT_PORT            = 8080;
  private static final String DEFAULT_WEBAPP_DIR      = "src/main/webapp/";
  private static final String DEFAULT_TARGET_CLASSES  = "target/classes";
  // Tomcat needs compiled classes under /WEB-INF/classes to load servlets without a packaged WAR.
  private static final String WEB_INF_CLASSES         = "/WEB-INF/classes";

  private static int getPort()
  {
    String port = System.getProperty("SERVER_PORT", System.getenv("SERVER_PORT"));
    return (port != null) ? Integer.parseInt(port) : DEFAULT_PORT;
  }

  private static String getWebappDir()
  {
    String dir = System.getProperty("WEBAPP_DIR", System.getenv("WEBAPP_DIR"));
    return (dir != null) ? dir : DEFAULT_WEBAPP_DIR;
  }

  private static String getClassesDir()
  {
    String dir = System.getProperty("CLASSES_DIR", System.getenv("CLASSES_DIR"));
    return (dir != null) ? dir : DEFAULT_TARGET_CLASSES;
  }

  public static Thread addShutdown(final Tomcat tomcat, final Logger logger)
  {
    Thread shutdownHook = new Thread(() -> {
      try
      {
        if (tomcat != null)
        {
          tomcat.stop();
          tomcat.destroy();
          logger.info("Tomcat has shut down normally.");
        }
      }
      catch (Exception e)
      {
        logger.log(Level.SEVERE, "Error shutting down Tomcat", e);
      }
    });
    Runtime.getRuntime().addShutdownHook(shutdownHook);
    return shutdownHook;
  }

  private static Logger initialiseLogger()
  {
    Logger logger = Logger.getLogger(Main.class.getName());
    // Disable parent handlers to avoid duplicate log lines on the console.
    logger.setUseParentHandlers(false);

    ConsoleHandler consoleHandler = new ConsoleHandler();
    consoleHandler.setLevel(Level.INFO);
    logger.addHandler(consoleHandler);

    logger.setLevel(Level.INFO);
    return logger;
  }

  private static Context getContext(Path webappDirectory, Tomcat tomcat)
  {
    if (!Files.exists(webappDirectory) || !Files.isDirectory(webappDirectory))
    {
      throw new IllegalArgumentException("Webapp directory does not exist: " + webappDirectory);
    }
    return tomcat.addWebapp("/", webappDirectory.toAbsolutePath().toString());
  }

  private static void setResources(Context context, Path targetClassesDirectory)
  {
    WebResourceRoot resources = new StandardRoot(context);
    resources.addPreResources(new DirResourceSet(resources, WEB_INF_CLASSES,
      targetClassesDirectory.toAbsolutePath().toString(), "/"));
    context.setResources(resources);
  }

  public static void main(String[] args)
  {
    final Logger logger                  = initialiseLogger();
    final int    port                    = getPort();
    final Path   webappDirectory         = Paths.get(getWebappDir());
    final Path   targetClassesDirectory  = Paths.get(getClassesDir());
    final Tomcat tomcat                  = new Tomcat();

    try
    {
      tomcat.setPort(port);
      tomcat.getConnector();
      addShutdown(tomcat, logger);

      Context context = getContext(webappDirectory, tomcat);
      setResources(context, targetClassesDirectory);

      tomcat.start();
      logger.info("Server started successfully on port " + port);
      tomcat.getServer().await();
    }
    catch (IllegalArgumentException e)
    {
      logger.log(Level.SEVERE, "Configuration error", e);
    }
    catch (Exception e)
    {
      logger.log(Level.SEVERE, "Error occurred while starting the server", e);
    }
  }
}