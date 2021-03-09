package view;

import viewmodel.MainMenuViewModel;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class MainMenuController
{
  private ViewHandler viewHandler;
  private MainMenuViewModel viewModel;

  @FXML private Label name;
  @FXML private Button employees;
  @FXML private Button departments;

  public void init(ViewHandler viewHandler, MainMenuViewModel viewModel)
  {
    this.viewHandler = viewHandler;
    this.viewModel = viewModel;

    name.setText(viewModel.getLoggedInName());
    employees.setDisable(!viewModel.isAdmin());
    departments.setDisable(!viewModel.isAdmin());
  }

  public void manageEmployees()
  {
    viewHandler.switchView("EditEmployeeSelect");
  }

  public void manageDepartments()
  {
    viewHandler.switchView("EditDepartmentSelect");
  }

  public void logOut()
  {
    viewModel.disconnectFromServer();
  }

  public void privateChat()
  {
    viewHandler.switchView("PrivateChat");
  }

  public void groupChat()
  {
    viewHandler.switchView("GroupChat");
  }
}
