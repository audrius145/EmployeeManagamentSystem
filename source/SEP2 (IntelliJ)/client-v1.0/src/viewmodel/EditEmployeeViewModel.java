package viewmodel;

import javafx.collections.ObservableList;
import model.DepartmentStruct;
import model.ModelManager;

public class EditEmployeeViewModel
{
    EditEmployeeViewModel() {}

    public void SaveEmployee(String name, int departmentID, boolean admin, String password)
    {
        if (password != null)
        {
            if (!password.equals(""))
                ModelManager.get().newEmployeePassword(DontDestroyOnLoad.get().currentEmployee.getID(), password);
        }
        ModelManager.get().saveEmployee(DontDestroyOnLoad.get().currentEmployee.getID(), name, departmentID, admin);
    }

    public void cancel()
    {
        ModelManager.get().releaseAll();
    }

    public String getName()
    {
        return DontDestroyOnLoad.get().currentEmployee.getName();
    }

    public int getID()
    {
        return DontDestroyOnLoad.get().currentEmployee.getID();
    }

    public boolean isAdmin()
    {
        return DontDestroyOnLoad.get().currentEmployee.isAdmin();
    }

    public ObservableList<DepartmentStruct> getDepartments()
    {
        return ModelManager.get().getDataHandler().getDepartments();
    }

    public DepartmentStruct getDepartment()
    {
        return ModelManager.get().getDataHandler().getDepartment(DontDestroyOnLoad.get().currentEmployee.getDepartment());
    }
}
