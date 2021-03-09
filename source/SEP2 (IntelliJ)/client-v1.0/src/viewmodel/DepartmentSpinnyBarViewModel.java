package viewmodel;

import model.ModelManager;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DepartmentSpinnyBarViewModel
{
  private StringProperty error;
  private Thread errorTimeout;

  DepartmentSpinnyBarViewModel()
  {
    error = new SimpleStringProperty("");

    ModelManager.get().addListener("departmentAcquired", evt -> Platform.runLater(() -> {
      if ((int)evt.getNewValue() == DontDestroyOnLoad.get().currentDepartment.getID())
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
    return "Department " + DontDestroyOnLoad.get().currentDepartment.getID() + " (ID)";
  }
}