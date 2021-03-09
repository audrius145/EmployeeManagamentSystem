package viewmodel;

import model.ModelManager;

public class EditDepartmentViewModel
{
    EditDepartmentViewModel() {}

    public void SaveDepartment(String name)
    {
        ModelManager.get().saveDepartment(DontDestroyOnLoad.get().currentDepartment.getID(), name);
    }

    public void cancel()
    {
        ModelManager.get().releaseAll();
    }

    public String getName()
    {
        return DontDestroyOnLoad.get().currentDepartment.getName();
    }

    public int getID()
    {
        return DontDestroyOnLoad.get().currentDepartment.getID();
    }
}
