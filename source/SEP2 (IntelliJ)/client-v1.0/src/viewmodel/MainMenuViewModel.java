package viewmodel;

import model.ModelManager;

public class MainMenuViewModel
{
  MainMenuViewModel() {}

  public String getLoggedInName()
  {
    return ModelManager.get().getDataHandler().getEmployee(ModelManager.get().getEmployeeID()).getName();
  }

  public boolean isAdmin()
  {
    return ModelManager.get().getDataHandler().getEmployee(ModelManager.get().getEmployeeID()).isAdmin();
  }

  public void disconnectFromServer()
  {
    ModelManager.get().disconnectFromServer();
  }
}
