package viewmodel;

import model.ModelManager;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class EmployeeSpinnyBarViewModel
{
  private StringProperty error;
  private Thread errorTimeout;

  EmployeeSpinnyBarViewModel()
  {
    error = new SimpleStringProperty("");

    ModelManager.get().addListener("employeeAcquired", evt -> Platform.runLater(() -> {
      if ((int)evt.getNewValue() == DontDestroyOnLoad.get().currentEmployee.getID())
      {
        error.set("acquired");
        new Thread(() -> {
          try
          {
            errorTimeout = Thread.currentThread();
            Thread.sleep(2000);
            if (errorTimeout.equals(Thread.currentThread()))
            {
              Platform.runLater(() -> error.set(""));
            }
          }
          catch (InterruptedException ignore) {}
        }).start();
      }
    }));
  }

  public void cancel()
  {
    Platform.runLater(() -> error.set(""));
    ModelManager.get().releaseAll();
  }

  public StringProperty errorProperty()
  {
    return error;
  }

  public String getLockingID()
  {
    return "Employee " + DontDestroyOnLoad.get().currentEmployee.getID() + " (ID)";
  }
}