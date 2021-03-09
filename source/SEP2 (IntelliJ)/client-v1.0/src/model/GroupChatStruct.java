package model;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**this class is responsible for storing and managing a group chat's respective data*/
public class GroupChatStruct
{
  /**group chat's id*/
  private int ID;
  /**group chat's name*/
  private String name;
  /**group chat's members*/
  private ObservableList<EmployeeStruct> members;
  /**group chat's history*/
  private ObservableList<String> history;

  /**initializes all instance variables*/
  GroupChatStruct(int ID, String name, int[] members)
  {
    this.ID = ID;
    this.name = name;
    this.members = FXCollections.observableArrayList();
    this.history = FXCollections.observableArrayList();
    for (int employee : members)
      this.members.add(ModelManager.get().getDataHandler().getEmployee(employee));
  }

  /**updates all instance variables (except ID)*/
  void Update(String name, int[] members)
  {
    Platform.runLater(() -> this.members.clear());
    for (int employee : members)
      Platform.runLater(() -> this.members.add(ModelManager.get().getDataHandler().getEmployee(employee)));
    this.name = name;
    /*for (int j = 0; j < this.members.size(); j++)
    {
      boolean contains = false;
      for (int member : members)
      {
        if (this.members.get(j).getID() == member)
        {
          contains = true;
          break;
        }
      }
      if (!contains)
      {
        int finalJ = j;
        Platform.runLater(() -> this.members.remove(finalJ));
        j--;
      }
    }
    for (int i : members)
    {
      boolean contains = false;
      for (EmployeeStruct member : this.members)
      {
        if (i == member.getID())
        {
          contains = true;
          break;
        }
      }
      if (!contains)
      {
        Platform.runLater(() -> this.members.add(ModelManager.get().getDataHandler().getEmployee(i)));
      }
    }*/
  }

  /**adds a new message to the group chat's history*/
  void addMessage(String content)
  {
    Platform.runLater(() -> history.add(content));
  }

  /**this method returns the group chat's id*/
  public int getID()
  {
    return ID;
  }

  /**this method returns the group chat's name*/
  public String getName()
  {
    return name;
  }

  /**returns the group chat's members as an observable list</br>
   * IMPORTANT: this method is for one directional bindings only, can possibly be abused with incorrect use, possible consequence is breaking data integrity on client side resulting the client not to work properly, since this list just a fetched data it can't cause server sided harm respectively*/
  public ObservableList<EmployeeStruct> getMembers()
  {
    return members;
  }

  /**returns the group chat's history as an observable list</br>
   * IMPORTANT: this method is for one directional bindings only, can possibly be abused with incorrect use, possible consequence is breaking data integrity on client side resulting the client not to work properly, since this list just a fetched data it can't cause server sided harm respectively*/
  public ObservableList<String> getHistory()
  {
    return history;
  }
}
