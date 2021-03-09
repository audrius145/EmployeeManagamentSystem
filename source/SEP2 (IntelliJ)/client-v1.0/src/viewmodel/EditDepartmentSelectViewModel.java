package viewmodel;

import model.DepartmentStruct;
import model.ModelManager;

import javafx.collections.ObservableList;

public class EditDepartmentSelectViewModel
{
  EditDepartmentSelectViewModel() {}

  public void reset()
  {
    DontDestroyOnLoad.get().currentDepartment = null;
  }

  public void setCurrent(DepartmentStruct currentDepartment)
  {
    DontDestroyOnLoad.get().currentDepartment = currentDepartment;
  }

  public ObservableList<DepartmentStruct> getDepartments()
  {
    return ModelManager.get().getDataHandler().getDepartments();
  }

  public void addNew()
  {
    ModelManager.get().newDepartment();
  }

  public boolean edit()
  {
    if (DontDestroyOnLoad.get().currentDepartment != null)
    {
      ModelManager.get().editDepartment(DontDestroyOnLoad.get().currentDepartment.getID());
      return true;
    }
    else
    {
      return false;
    }
  }

  public void archive()
  {
    if (DontDestroyOnLoad.get().currentDepartment != null)
      ModelManager.get().deleteDepartment(DontDestroyOnLoad.get().currentDepartment.getID());
  }

  public String getLoggedInName()
  {
    return ModelManager.get().getDataHandler().getEmployee(ModelManager.get().getEmployeeID()).getName();
  }
}
