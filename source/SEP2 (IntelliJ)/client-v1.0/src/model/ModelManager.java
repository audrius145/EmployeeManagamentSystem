package model;

import mediator.Client;
import mediator.ClientInterface;

import utility.observer.subject.NamedPropertyChangeSubject;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javafx.application.Platform;

/**this singleton class is responsible for processing instructions from the server and send appropriately formatted requests to the server based on the user's interactions*/
public class ModelManager implements NamedPropertyChangeSubject, Receiver
{
  /**the single instance of this class*/
  private static ModelManager instance;
  /**the mediator main class*/
  private ClientInterface client;
  /**stores the relevant data, fetched from the server (no business logic done here, that is strictly server sided)*/
  private DataHandler data;
  /**the ID of the employee who the user is logged in as*/
  private int employeeID;
  /**the ID of the current session*/
  private int sessionID;
  /**the host of the current session*/
  private String host;

  private PropertyChangeSupport property;

  /**initializes the Client and the DataHandler*/
  private ModelManager()
  {
    this.property = new PropertyChangeSupport(this);
    client = new Client(this);
    data = new DataHandler();
  }

  /**returns the only instance of this class*/
  public static synchronized ModelManager get()
  {
    if (instance == null)
      instance = new ModelManager();
    return instance;
  }

  /**adds listener*/
  @Override public void addListener(String propertyName, PropertyChangeListener listener)
  {
    property.addPropertyChangeListener(propertyName, listener);
  }

  /**removes listener"*/
  @Override public void removeListener(String propertyName, PropertyChangeListener listener)
  {
    property.removePropertyChangeListener(propertyName, listener);
  }

  //------------------------------------------------------------------------------------------------------------------------------------------------
  //-----------------------------------------------------------  server ==> client  ----------------------------------------------------------------
  //-----------------------------------------------------------  (used by mediator)  ---------------------------------------------------------------
  //------------------------------------------------------------------------------------------------------------------------------------------------

  /**a string processor which breaks down an instruction coming from the server into its bare components which are: tag, attributes and value*/
  public void receiveContent(String content)
  {
    Log.get().add("[RECEIVING from server] " + content);
    try
    {
      String[] temp;
      temp = content.split("</");
      String endTag = "</" + temp[temp.length - 1];
      temp = content.split(">");
      String startTag = temp[0] + ">";
      String value = content.substring(startTag.length(), content.length() - endTag.length());
      String[] attributes = new String[0];
      temp = startTag.split("//");
      endTag = endTag.substring(2, endTag.length() - 1);
      if (temp.length == 1)
        startTag = temp[0].substring(1, startTag.length() - 1);
      else if (temp.length > 1)
      {
        startTag = temp[0].substring(1);
        attributes = new String[temp.length - 1];
        System.arraycopy(temp, 1, attributes, 0, temp.length - 1);
        attributes[attributes.length - 1] = attributes[attributes.length - 1].substring(0, attributes[attributes.length - 1].length() - 1);
      }
      else
      {
        attributes = new String[0];
      }
      if (startTag.equals(endTag))
        processContent(startTag, value, attributes);
      else
        Log.get().error(new UnsupportedOperationException("start tag does not match with end tag"));
    }
    catch (StringIndexOutOfBoundsException | ArrayIndexOutOfBoundsException e)
    {
      Log.get().error(new UnsupportedOperationException("unknown content exception", e));
    }
  }

  /**a string processor with multiple freedom degrees to process an incoming instruction according to its: tag, attributes and value*/
  private void processContent(String tag, String value, String[] attributes)
  {
    RuntimeException exception = null;
    boolean recognized = false;
    switch (tag)
    {
      case "command":
        try {
          //_________________________________________________________________________________________________________________________
          if (value.substring(0,"sessionID=".length()).equals("sessionID="))
          {
            int ID = Integer.parseInt(value.substring("sessionID=".length()));
            sessionID = ID;
            Log.get().add("[COMMAND] sessionID has been set to " + ID);
            recognized = true;
          }
          //-------------------------------------------------------------------------------------------------------------------------
        } catch (StringIndexOutOfBoundsException | ArrayIndexOutOfBoundsException e) {exception = e;}
        try {
          //_________________________________________________________________________________________________________________________
          if (value.substring(0,"archiveEmployee=".length()).equals("archiveEmployee="))
          {
            int ID = Integer.parseInt(value.substring("archiveEmployee=".length()));
            if (ID == employeeID)
            {
              property.firePropertyChange("alert", "", "disconnected");
            }
            else
            {
              data.removeEmployee(ID);
              Log.get().add("[COMMAND] employee " + ID + " has been removed");
            }
            recognized = true;
          }
          //-------------------------------------------------------------------------------------------------------------------------
        } catch (StringIndexOutOfBoundsException | ArrayIndexOutOfBoundsException e) {exception = e;}
        try {
          //_________________________________________________________________________________________________________________________
          if (value.substring(0,"removeDepartment=".length()).equals("removeDepartment="))
          {
            int ID = Integer.parseInt(value.substring("removeDepartment=".length()));
            data.removeDepartment(ID);
            Log.get().add("[COMMAND] department " + ID + " has been removed");
            recognized = true;
          }
          //-------------------------------------------------------------------------------------------------------------------------
        } catch (StringIndexOutOfBoundsException | ArrayIndexOutOfBoundsException e) {exception = e;}
        try {
          //_________________________________________________________________________________________________________________________
          if (value.substring(0,"removeChat=".length()).equals("removeChat="))
          {
            int ID = Integer.parseInt(value.substring("removeChat=".length()));
            data.removeChat(ID);
            Log.get().add("[COMMAND] chat " + ID + " has been removed");
            recognized = true;
          }
          //-------------------------------------------------------------------------------------------------------------------------
        } catch (StringIndexOutOfBoundsException | ArrayIndexOutOfBoundsException e) {exception = e;}
        break;
      case "login":
        try {
          //_________________________________________________________________________________________________________________________
          if (value.equals("denied"))
          {
            property.firePropertyChange("login", "", "denied");
            Log.get().add("[LOGIN] login denied");
            recognized = true;
          }
          //-------------------------------------------------------------------------------------------------------------------------
        } catch (StringIndexOutOfBoundsException | ArrayIndexOutOfBoundsException e) {exception = e;}
        try {
          //_________________________________________________________________________________________________________________________
          if (value.substring(0,"user=".length()).equals("user="))
          {
            employeeID = Integer.parseInt(value.substring("user=".length()));
            property.firePropertyChange("login", "", "approved");
            sendToServer("<sync>getAll</sync>");
            Log.get().add("[LOGIN] logged in as " + employeeID);
            recognized = true;
          }
          //-------------------------------------------------------------------------------------------------------------------------
        } catch (StringIndexOutOfBoundsException | ArrayIndexOutOfBoundsException e) {exception = e;}
        break;
      case "employee":
        try {
          //_________________________________________________________________________________________________________________________
          String[] temp = value.split("//");
          boolean tempAdmin = false;
          try {
            tempAdmin = data.getEmployee(employeeID).isAdmin();
          } catch (Exception ignored) {}
          if (Integer.parseInt(temp[0]) == employeeID && !Boolean.parseBoolean(temp[3]) && tempAdmin)
          {
            property.firePropertyChange("alert", "", "disconnected");
          }
          else
          {
            data.employee(Integer.parseInt(temp[0]), temp[1], Integer.parseInt(temp[2]), Boolean.parseBoolean(temp[3]));
            Log.get().add("[SYNC] employee " + Integer.parseInt(temp[0]) + " received");
            property.firePropertyChange("alert", "", "refresh");
          }
          recognized = true;
          //-------------------------------------------------------------------------------------------------------------------------
        } catch (StringIndexOutOfBoundsException | ArrayIndexOutOfBoundsException e) {exception = e;}
        break;
      case "department":
        try {
          //_________________________________________________________________________________________________________________________
          String[] temp = value.split("//");
          data.department(Integer.parseInt(temp[0]),temp[1]);
          Log.get().add("[SYNC] department " + Integer.parseInt(temp[0]) + " received");
          property.firePropertyChange("alert", "", "refresh");
          recognized = true;
          //-------------------------------------------------------------------------------------------------------------------------
        } catch (StringIndexOutOfBoundsException | ArrayIndexOutOfBoundsException e) {exception = e;}
        break;
      case "lock":
        try {
          //_________________________________________________________________________________________________________________________
          if (value.substring(0,"employeeAcquired=".length()).equals("employeeAcquired="))
          {
            int ID = Integer.parseInt(value.substring("employeeAcquired=".length()));
            property.firePropertyChange("employeeAcquired","",ID);
            Log.get().add("[LOCK] employee " + ID + " acquired");
            recognized = true;
          }
          //-------------------------------------------------------------------------------------------------------------------------
        } catch (StringIndexOutOfBoundsException | ArrayIndexOutOfBoundsException e) {exception = e;}
        try {
          //_________________________________________________________________________________________________________________________
          if (value.substring(0,"departmentAcquired=".length()).equals("departmentAcquired="))
          {
            int ID = Integer.parseInt(value.substring("departmentAcquired=".length()));
            property.firePropertyChange("departmentAcquired","",ID);
            Log.get().add("[LOCK] department" + ID + " acquired");
            recognized = true;
          }
          //-------------------------------------------------------------------------------------------------------------------------
        } catch (StringIndexOutOfBoundsException | ArrayIndexOutOfBoundsException e) {exception = e;}
        break;
      case "chat":
        try {
          //_________________________________________________________________________________________________________________________
          String[] temp = value.split("//");
          int[] employees = new int[2];
          employees[0] = Integer.parseInt(temp[1].split(",")[0]);
          employees[1] = Integer.parseInt(temp[1].split(",")[1]);
          data.chat(Integer.parseInt(temp[0]),temp[1],employees,false);
          Log.get().add("[SYNC] chat " + Integer.parseInt(temp[0]) + " received");
          property.firePropertyChange("alert", "", "refresh");
          recognized = true;
          //-------------------------------------------------------------------------------------------------------------------------
        } catch (StringIndexOutOfBoundsException | ArrayIndexOutOfBoundsException e) {exception = e;}
        break;
      case "groupchat":
        try {
          //_________________________________________________________________________________________________________________________
          String[] temp = value.split("//");
          int[] employees = new int[temp[2].split(",").length];
          for (int i = 0; i < employees.length; i++)
            employees[i] = Integer.parseInt(temp[2].split(",")[i]);
          data.chat(Integer.parseInt(temp[0]),temp[1],employees,true);
          Log.get().add("[SYNC] chat " + Integer.parseInt(temp[0]) + " received");
          property.firePropertyChange("alert", "", "refresh");
          recognized = true;
          //-------------------------------------------------------------------------------------------------------------------------
        } catch (StringIndexOutOfBoundsException | ArrayIndexOutOfBoundsException e) {exception = e;}
        break;
      case "message":
        try {
          //_________________________________________________________________________________________________________________________
          int ID = Integer.parseInt(attributes[0].substring("id=".length()));
          Platform.runLater(() -> data.getChat(ID).addMessage(value));
          Log.get().add("[SYNC] message received, in chat " + ID);
          property.firePropertyChange("alert", "", "refresh");
          recognized = true;
          //-------------------------------------------------------------------------------------------------------------------------
        } catch (StringIndexOutOfBoundsException | ArrayIndexOutOfBoundsException e) {exception = e;}
        break;
    }
    if (!recognized)
      if (exception == null)
        Log.get().error(new UnsupportedOperationException("instruction not recognized"));
      else
        Log.get().error(new UnsupportedOperationException("instruction not recognized", exception));
  }

  /**triggers a connection refused event*/
  public void connectionRefused()
  {
    host = null;
    Log.get().add("[CONNECTION] connection refused");
    property.firePropertyChange("login", "", "connectionRefused");
  }

  /**triggers a disconnected from server event*/
  public void disconnectedFromServer()
  {

    data = new DataHandler();
    employeeID = 0;
    sessionID = 0;
    host = null;
    Log.get().add("[CONNECTION] disconnected from server");
    property.firePropertyChange("alert", "", "disconnected");
  }

  //------------------------------------------------------------------------------------------------------------------------------------------------
  //-----------------------------------------------------------  client ==> server  ----------------------------------------------------------------
  //----------------------------------------------------------  (used by viewmodels)  --------------------------------------------------------------
  //------------------------------------------------------------------------------------------------------------------------------------------------
  
  /**sends a request to the server*/
  private void sendToServer(String content)
  {
    Log.get().add("[SENDING to server] " + content);
    client.sendToServer(content);
  }
  
  /**returns the DataHandler*/
  public DataHandler getDataHandler()
  {
    return data;
  }

  /**disconnects from the server*/
  public void disconnectFromServer()
  {
    client.disconnect();
  }

  /**connects to the server and tries log in*/
  public void login(String employeeID, String password, String host)
  {
    client.disconnect();
    client = new Client(this);
    Log.get().add("[CONNECTION] connecting to server");
    client.connect(host);
    this.host = host;
    sendToServer("<login>" + employeeID + "//" + password + "</login>");
  }

  /**sends an appropriately formatted employee creation request to the server*/
  public void newEmployee()
  {
    sendToServer("<command>newEmployee</command>");
  }

  /**sends an appropriately formatted lock request on a given employee to the server*/
  public void editEmployee(int ID)
  {
    sendToServer("<lock>employee=" + ID + "</lock>");
  }

  /**sends appropriately formatted save modifications on a given employee and then release all lock (including the issued employee) requests to the server*/
  public void saveEmployee(int ID, String name, int department, boolean admin)
  {
    sendToServer("<employee>" + ID + "//" + name + "//" + department + "//" + admin + "</employee>");
    releaseAll();
  }

  /**sends an appropriately formatted password change request on a given employee to the server*/
  public void newEmployeePassword(int ID, String password)
  {
    sendToServer("<password>" + ID + "//" + password + "</password>");
  }

  /**sends an appropriately formatted archive employee request to the server*/
  public void archiveEmployee(int ID)
  {
    sendToServer("<command>archiveEmployee=" + ID + "</command>");
  }

  /**sends an appropriately formatted release all lock request to the server*/
  public void releaseAll()
  {
    sendToServer("<lock>releaseAll</lock>");
  }

  /**sends an appropriately formatted department creation request to the server*/
  public void newDepartment()
  {
    sendToServer("<command>newDepartment</command>");
  }

  /**sends an appropriately formatted lock request on a given department to the server*/
  public void editDepartment(int ID)
  {
    sendToServer("<lock>department=" + ID + "</lock>");
  }

  /**sends appropriately formatted save modifications on a given department and then release all lock (including the issued department) requests to the server*/
  public void saveDepartment(int ID, String name)
  {
    sendToServer("<department>" + ID + "//" + name + "</department>");
    releaseAll();
  }

  /**sends an appropriately formatted remove department request to the server*/
  public void deleteDepartment(int ID)
  {
    sendToServer("<command>removeDepartment=" + ID + "</command>");
  }

  /**method uses the Client, sends an appropriately formatted group chat creation request to the server*/
  public void newGroupChat()
  {
    sendToServer("<command>newGroupChat</command>");
  }

  /**sends an appropriately formatted add member to group chat request to the server*/
  public void addToChat(int groupChatID, int employeeID)
  {
    sendToServer("<groupchat//id=" + groupChatID + ">addMember=" + employeeID + "</groupchat>");
  }

  /**sends an appropriately formatted remove member from group chat request to the server*/
  public void removeFromChat(int groupChatID, int employeeID)
  {
    sendToServer("<groupchat//id=" + groupChatID + ">removeMember=" + employeeID + "</groupchat>");
  }

  /**sends an appropriately formatted rename group chat request to the server*/
  public void renameChat(int groupChatID, String name)
  {
    sendToServer("<groupchat//id=" + groupChatID + ">name=" + name + "</groupchat>");
  }

  /**sends an appropriately formatted send message request to the server*/
  public void sendMessage(int chatID, String message)
  {
    sendToServer("<message//id=" + chatID + ">" + message + "</message>");
  }

  /**returns the ID of the employee who the user is logged in as*/
  public int getEmployeeID()
  {
    return employeeID;
  }

  /**returns the ID of the current session*/
  public int getSessionID()
  {
    return sessionID;
  }

  /**returns the host of the current session*/
  public String getHost()
  {
    return host;
  }
}
