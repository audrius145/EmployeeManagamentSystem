package viewmodel;

import model.ModelManager;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LoginViewModel
{
  private StringProperty error;
  private Thread errorTimeout;

  LoginViewModel()
  {
    this.error = new SimpleStringProperty("");

    ModelManager.get().addListener("login", evt -> Platform.runLater(() -> {
      error.set(evt.getNewValue().toString());
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
    }));
  }

  public void reset()
  {
    error.set("");
  }

  public void login(String userName, String password, String host)
  {
    ModelManager.get().login(userName, password, host);
  }

  public StringProperty errorProperty()
  {
    return error;
  }
}