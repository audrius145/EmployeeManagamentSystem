package viewmodel;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import model.EmployeeStruct;
import model.ModelManager;

public class EditGroupChatViewModel
{
  private StringProperty nameProperty;

  EditGroupChatViewModel()
  {
    nameProperty = new SimpleStringProperty("");
    ModelManager.get().addListener("alert", evt -> {
      if (evt.getNewValue().equals("refresh") && DontDestroyOnLoad.get().currentChat != null)
        nameProperty.set(DontDestroyOnLoad.get().currentChat.getName());
    });
  }

  public int getID()
  {
    return DontDestroyOnLoad.get().currentChat.getID();
  }

  public ObservableList<EmployeeStruct> getEmployees()
  {
    return ModelManager.get().getDataHandler().getEmployees();
  }

  public ObservableList<EmployeeStruct> getMembers()
  {
    return DontDestroyOnLoad.get().currentChat.getMembers();
  }

  public StringProperty nameProperty()
  {
    nameProperty.set(DontDestroyOnLoad.get().currentChat.getName());
    return nameProperty;
  }

  public void changeName(String name)
  {
    ModelManager.get().renameChat(DontDestroyOnLoad.get().currentChat.getID(), name);
  }

  public void removeEmployee(int employeeID)
  {
    ModelManager.get().removeFromChat(DontDestroyOnLoad.get().currentChat.getID(), employeeID);
  }

  public void addEmployee(int employeeID)
  {
    ModelManager.get().addToChat(DontDestroyOnLoad.get().currentChat.getID(), employeeID);
  }
}