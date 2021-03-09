import view.ViewHandler;
import viewmodel.ViewModelFactory;

import javafx.application.Application;
import javafx.stage.Stage;

public class ClientApplication extends Application
{
  public void start(Stage primaryStage)
  {
    ViewModelFactory viewModelFactory = new ViewModelFactory();
    ViewHandler view = new ViewHandler(viewModelFactory);
    view.start(primaryStage);
  }
}