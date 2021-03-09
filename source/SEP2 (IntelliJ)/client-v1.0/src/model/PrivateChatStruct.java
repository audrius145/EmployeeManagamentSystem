package model;

/**this class is responsible for extending the group chat (since a private chat can be perceived as a group chat with only 2 people in it) with private chat only methods*/
public class PrivateChatStruct extends GroupChatStruct
{
  /**same constructor as super class'*/
  PrivateChatStruct(int ID, String name, int[] employees)
  {
    super(ID, name, employees);
  }

  /**this method returns the id of the member of the chat which is not the employee who the client has logged in with*/
  public int getPartnerID()
  {
    return getPartner().getID();
  }

  /**this method returns the name of the member of the chat which is not the employee who the client has logged in with*/
  public String getPartnerName()
  {
    return getPartner().getName();
  }

  /**this method returns the member of the chat which is not the employee who the client has logged in with*/
  private EmployeeStruct getPartner()
  {
    if (getMembers().get(0).getID() == ModelManager.get().getEmployeeID())
      return getMembers().get(1);
    else if (getMembers().get(1).getID() == ModelManager.get().getEmployeeID())
      return getMembers().get(0);
    throw new RuntimeException("broken data integrity in chat " + getID());
  }
}