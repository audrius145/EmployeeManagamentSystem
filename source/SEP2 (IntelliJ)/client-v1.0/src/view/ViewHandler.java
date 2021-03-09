package view;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import model.ModelManager;
import viewmodel.ViewModelFactory;

public class ViewHandler
{
  private Scene currentScene;
  private Stage primaryStage;
  private ViewModelFactory viewModelFactory;

  public ViewHandler(ViewModelFactory viewModelFactory)
  {
    this.viewModelFactory = viewModelFactory;
    currentScene = new Scene(new Region());
  }

  public void start(Stage primaryStage)
  {
    this.primaryStage = primaryStage;
    switchView("Login");

    //BREAKS MVVM PATTERN
    ModelManager.get().addListener("alert", evt -> Platform.runLater(() -> {
      if (evt.getNewValue().equals("disconnected"))
        switchView("Login");
    }));
  }

  public void switchView(String view)
  {
    switchView(view, true);
  }

  public void switchView(String view, boolean keepSize)
  {
    try
    {
      double Width = primaryStage.getWidth();
      double Height = primaryStage.getHeight();
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getResource(view + "View.fxml"));
      Region root = loader.load();
      switch (view)
      {
        case "Login":
          LoginController controller1 = loader.getController();
          controller1.init(this, viewModelFactory.getLoginViewModel());
          break;
        case "EditEmployeeSelect":
          EditEmployeeSelectController controller2 = loader.getController();
          controller2.init(this, viewModelFactory.getEditEmployeeSelectViewModel());
          break;
        case "EditEmployee":
          EditEmployeeController controller3 = loader.getController();
          controller3.init(this, viewModelFactory.getEditEmployeeViewModel());
          break;
        case "EmployeeSpinnyBar":
          EmployeeSpinnyBarController controller4 = loader.getController();
          controller4.init(this, viewModelFactory.getEmployeeSpinnyBarViewModel());
          break;
        case "MainMenu":
          MainMenuController controller5 = loader.getController();
          controller5.init(this, viewModelFactory.getMainMenuViewModel());
          break;
        case "EditDepartmentSelect":
          EditDepartmentSelectController controller6 = loader.getController();
          controller6.init(this, viewModelFactory.getEditDepartmentSelectViewModel());
          break;
        case "EditDepartment":
          EditDepartmentController controller7 = loader.getController();
          controller7.init(this, viewModelFactory.getEditDepartmentViewModel());
          break;
        case "DepartmentSpinnyBar":
          DepartmentSpinnyBarController controller8 = loader.getController();
          controller8.init(this, viewModelFactory.getDepartmentSpinnyBarViewModel());
          break;
        case "PrivateChat" :
          PrivateChatController controller9 = loader.getController();
          controller9.init(this, viewModelFactory.getPrivateChatViewModel());
          break;
        case "GroupChat" : 
          GroupChatController controller10 = loader.getController();
          controller10.init(this, viewModelFactory.getGroupChatViewModel());
          break;
        case "EditGroupChat":
          EditGroupChatController controller11 = loader.getController();
          controller11.init(this, viewModelFactory.getEditGroupChatViewModel());
          break;
      }
      currentScene.setRoot(root);
      String title = "";
      if (root.getUserData() != null)
      {
        title += root.getUserData();
      }
      primaryStage.setTitle(title);
      primaryStage.setScene(currentScene);
      primaryStage.setWidth(root.getPrefWidth());
      primaryStage.setHeight(root.getPrefHeight());
      primaryStage.setWidth(root.getPrefWidth());
      primaryStage.setHeight(root.getPrefHeight());
      primaryStage.show();
      primaryStage.setMinWidth(root.minWidth(-1));
      primaryStage.setMinHeight(root.minHeight(-1));
      if (keepSize)
      {
        if (primaryStage.getMinWidth() <= Width)
          primaryStage.setWidth(Width);
        if (primaryStage.getMinHeight() <= Height)
          primaryStage.setHeight(Height);
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  void close()
  {
    primaryStage.close();
  }

  boolean validityChecker(String content)
  {
    return (!(
        content.contains("/") ||
        content.contains("<") ||
        content.contains(">") ||
        content.contains("=") ||
        content.contains(",") ));
  }
}