package viewmodel;

import model.EmployeeStruct;
import model.ModelManager;

import javafx.collections.ObservableList;

public class EditEmployeeSelectViewModel
{
  EditEmployeeSelectViewModel() {}

  public void reset()
  {
    DontDestroyOnLoad.get().currentEmployee = null;
  }

  public void setCurrent(EmployeeStruct currentEmployee)
  {
    DontDestroyOnLoad.get().currentEmployee = currentEmployee;
  }

  public ObservableList<EmployeeStruct> getEmployees()
  {
    return ModelManager.get().getDataHandler().getEmployees();
  }

  public void addNew()
  {
    ModelManager.get().newEmployee();
  }

  public boolean edit()
  {
    if (DontDestroyOnLoad.get().currentEmployee != null)
    {
      ModelManager.get().editEmployee(DontDestroyOnLoad.get().currentEmployee.getID());
      return true;
    }
    else
    {
      return false;
    }
  }

  public void archive()
  {
    if (DontDestroyOnLoad.get().currentEmployee != null)
      ModelManager.get().archiveEmployee(DontDestroyOnLoad.get().currentEmployee.getID());
  }

  public String getLoggedInName()
  {
    return ModelManager.get().getDataHandler().getEmployee(ModelManager.get().getEmployeeID()).getName();
  }
}
