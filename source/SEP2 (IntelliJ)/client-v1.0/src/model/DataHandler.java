package model;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**this class is responsible for storing and managing data*/
public class DataHandler
{
  /**stores non archived employees*/
  private ObservableList<EmployeeStruct> employees;
  /**stores departments*/
  private ObservableList<DepartmentStruct> departments;
  /**stores private chats*/
  private ObservableList<PrivateChatStruct> privateChats;
  /**stores group chats*/
  private ObservableList<GroupChatStruct> groupChats;

  /**initializes all lists*/
  DataHandler()
  {
    employees = FXCollections.observableArrayList();
    departments = FXCollections.observableArrayList();
    Platform.runLater(() -> departments.add(new DepartmentStruct(0, "No Department")));
    privateChats = FXCollections.observableArrayList();
    groupChats = FXCollections.observableArrayList();
  }

  /**takes an employee's attributes and stores it in employees, if it contains an employee with the same id it updates the already stored one*/
  void employee(int ID, String name, int department, boolean admin)
  {
    boolean exists = false;
    for (EmployeeStruct employee : employees)
    {
      if (employee.getID() == ID)
      {
        exists = true;
        employee.Update(name, department, admin);
        break;
      }
    }
    if (!exists)
      Platform.runLater(() -> employees.add(new EmployeeStruct(ID, name, department, admin)));
  }
  
  /**takes an employee's id and returns the issued EmployeeStruct*/
  public EmployeeStruct getEmployee(int ID)
  {
    for (EmployeeStruct employee : employees)
    {
      if (employee.getID() == ID)
      {
        return employee;
      }
    }
    throw new RuntimeException("employee " + ID + " not found");
  }

  /**takes a employee's id and removes it*/
  void removeEmployee(int ID)
  {
    for (int i = 0; i < employees.size(); i++)
    {
      if (employees.get(i).getID() == ID)
      {
        int finalI = i;
        Platform.runLater(() -> employees.remove(finalI));
        break;
      }
    }
  }

  /**takes a department's attributes and stores it in departments, if it contains a department with the same id it updates the already stored one*/
  void department(int ID, String name)
  {
    boolean exists = false;
    for (DepartmentStruct department : departments)
    {
      if (department.getID() == ID)
      {
        exists = true;
        department.Update(name);
        break;
      }
    }
    if (!exists)
    {
      Platform.runLater(() -> departments.add(new DepartmentStruct(ID, name)));
    }
  }

  /**takes a department's id and returns the issued DepartmentStruct*/
  public DepartmentStruct getDepartment(int ID)
  {
    for (DepartmentStruct department : departments)
    {
      if (department.getID() == ID)
      {
        return department;
      }
    }
    throw new RuntimeException("department " + ID + " not found");
  }

  /**takes a department's id and removes it*/
  void removeDepartment(int ID)
  {
    for (int i = 0; i < departments.size(); i++)
    {
      if (departments.get(i).getID() == ID)
      {
        int finalI = i;
        Platform.runLater(() -> departments.remove(finalI));
        break;
      }
    }
  }

  /**takes a chat's attributes and stores it in privateChats or groupChats (depending on isGroup argument), if it contains a chat with the same id it updates the already stored one*/
  void chat(int ID, String name, int[] employees, boolean isGroup)
  {
    if (isGroup)
    {
      for (int i = 0; i < privateChats.size(); i++)
      {
        if (privateChats.get(i).getID() == ID)
        {
          int finalI = i;
          Platform.runLater(() -> privateChats.remove(finalI));
          break;
        }
      }
      boolean exists = false;
      for (GroupChatStruct groupChat : groupChats)
      {
        if (groupChat.getID() == ID)
        {
          exists = true;
          groupChat.Update(name, employees);
          break;
        }
      }
      if (!exists)
      {
        Platform.runLater(() -> groupChats.add(new GroupChatStruct(ID, name, employees)));
      }
    }
    else
    {
      for (int i = 0; i < groupChats.size(); i++)
      {
        if (groupChats.get(i).getID() == ID)
        {
          int finalI = i;
          Platform.runLater(() -> groupChats.remove(finalI));
          break;
        }
      }
      boolean exists = false;
      for (GroupChatStruct privateChat : privateChats)
      {
        if (privateChat.getID() == ID)
        {
          exists = true;
          privateChat.Update(name, employees);
          break;
        }
      }
      if (!exists)
      {
        Platform.runLater(() -> privateChats.add(new PrivateChatStruct(ID, name, employees)));
      }
    }
  }

  /**takes a chat's id and returns the issued ChatStruct*/
  public GroupChatStruct getChat(int ID)
  {
    for (GroupChatStruct chat : privateChats)
    {
      if (chat.getID() == ID)
      {
        return chat;
      }
    }
    for (GroupChatStruct chat : groupChats)
    {
      if (chat.getID() == ID)
      {
        return chat;
      }
    }
    throw new RuntimeException("chat " + ID + " not found");
  }

  /**takes a chat's id and removes it*/
  void removeChat(int ID)
  {
    for (int i = 0; i < groupChats.size(); i++)
    {
      if (groupChats.get(i).getID() == ID)
      {
        int finalI = i;
        Platform.runLater(() -> groupChats.remove(finalI));
        break;
      }
    }
    for (int i = 0; i < privateChats.size(); i++)
    {
      if (privateChats.get(i).getID() == ID)
      {
        int finalI = i;
        Platform.runLater(() -> privateChats.remove(finalI));
        break;
      }
    }
  }

  /**returns the employees as an observable list</br>
   * IMPORTANT: this method is for one directional bindings only, can possibly be abused with incorrect use, possible consequence is breaking data integrity on client side resulting the client not to work properly, since this list just a fetched data it can't cause server sided harm respectively*/
  public ObservableList<EmployeeStruct> getEmployees()
  {
    return employees;
  }

  /**returns the departments as an observable list</br>
   * IMPORTANT: this method is for one directional bindings only, can possibly be abused with incorrect use, possible consequence is breaking data integrity on client side resulting the client not to work properly, since this list just a fetched data it can't cause server sided harm respectively*/
  public ObservableList<DepartmentStruct> getDepartments()
  {
    return departments;
  }

  /**returns the private chats as an observable list</br>
   * IMPORTANT: this method is for one directional bindings only, can possibly be abused with incorrect use, possible consequence is breaking data integrity on client side resulting the client not to work properly, since this list just a fetched data it can't cause server sided harm respectively*/
  public ObservableList<PrivateChatStruct> getPrivateChats()
  {
    return privateChats;
  }

  /**returns the group chats as an observable list</br>
   * IMPORTANT: this method is for one directional bindings only, can possibly be abused with incorrect use, possible consequence is breaking data integrity on client side resulting the client not to work properly, since this list just a fetched data it can't cause server sided harm respectively*/
  public ObservableList<GroupChatStruct> getGroupChats()
  {
    return groupChats;
  }
}
