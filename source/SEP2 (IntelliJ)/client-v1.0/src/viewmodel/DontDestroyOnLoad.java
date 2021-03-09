package viewmodel;

import model.DepartmentStruct;
import model.EmployeeStruct;
import model.GroupChatStruct;

public class DontDestroyOnLoad
{
  private static DontDestroyOnLoad instance;
  EmployeeStruct currentEmployee;
  DepartmentStruct currentDepartment;
  GroupChatStruct currentChat;

  private DontDestroyOnLoad()
  {

  }

  public static DontDestroyOnLoad get()
  {
    if (instance == null)
      instance = new DontDestroyOnLoad();
    return instance;
  }
}
