package model;

import java.util.ArrayList;

/**this singleton class is for logging to console, as well as to optionally save the log to file*/
public class Log
{
  /**the single instance of this class*/
  private static Log instance;
  /**the log history*/
  private ArrayList<String> history;

  /**initializes the history*/
  private Log()
  {
    history = new ArrayList<>();
  }

  /**returns the only instance of this class, constructs one if needed*/
  public static Log get()
  {
    if (instance == null)
      instance = new Log();
    return instance;
  }

  /**adds an entry to the history as well as prints out the entry to the console*/
  public void add(String log)
  {
    history.add(log);
    System.out.println(log);
  }

  /**adds an error entry to the history as well as prints out the stack trace of the exception</br>
   * IMPORTANT: this method is used for debugging and logging non fatal exceptions that happen, since these exceptions are not thrown*/
  public void error(RuntimeException e)
  {
    add("[ERROR]" + e.toString() + " (by " + Thread.currentThread().toString() + ")");
    e.printStackTrace();
  }
}