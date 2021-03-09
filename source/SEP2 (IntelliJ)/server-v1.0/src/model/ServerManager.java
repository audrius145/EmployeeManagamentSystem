package model;

import mediator.ClientHandler;
import mediator.ClientInterface;
import persistence.PersistenceInterface;
import model.business.*;

import java.util.ArrayList;

/**this singleton class is responsible for processing requests from clients and respond to them accordingly*/
public class ServerManager implements Receiver
{
  /**the single instance of this class*/
  private static ServerManager instance;

  /**the persistence interface of the system (currently PostgreManager but can be changed anytime)*/
  private PersistenceInterface persistence;

  /**list of all clients*/
  private ArrayList<ClientInterface> clients;
  /**list of active clients*/
  private ArrayList<ClientInterface> activeClients;

  /**EmployeeManager instance*/
  private EmployeeManager employeeManager;
  /**DepartmentManager instance*/
  private DepartmentManager departmentManager;
  /**ChatManager instance*/
  private ChatManager chatManager;

  /**takes a PersistenceInterface and initializes all instance variables*/
  private ServerManager(PersistenceInterface persistence)
  {
    this.persistence = persistence;
    clients = new ArrayList<>();
    activeClients = new ArrayList<>();
    employeeManager = new EmployeeManager();
    departmentManager = new DepartmentManager();
    chatManager = new ChatManager();
  }

  /**initializes the singleton instance*/
  public static void init(PersistenceInterface persistence)
  {
    if (instance == null)
      instance = new ServerManager(persistence);
    else
      throw new IllegalStateException("ServerManager is already initialized");
  }

  /**gets the singleton instance*/
  public static ServerManager get()
  {
    if (instance != null)
      return instance;
    else
      throw new IllegalStateException("ServerManager is not initialized");
  }

  /**uses persistence to load all the stored data into the managers*/
  public void loadAllData()
  {
    ArrayList<Department> departments = persistence.getAllDepartments();
    for (Department department : departments)
      departmentManager.loadDepartment(department);
    ArrayList<Employee> employees = persistence.getAllEmployees();
    for (Employee employee : employees)
      employeeManager.loadEmployee(employee);
    ArrayList<Chat> chats = persistence.getAllChats();
    for (Chat chat : chats)
      chatManager.loadChat(chat);
    ArrayList<Message> messages = persistence.getAllMessages();
    for (Message message : messages)
      chatManager.loadMessage(message);
    chatManager.orderMessages();
    Log.get().add("[DATA] loaded successfully");
  }

  //------------------------------------------------------------------------------------------------------------------------------------------------
  //-----------------------------------------------------------  server ==> client  ----------------------------------------------------------------
  //-----------------------------------------------------------  (used by mediator)  ---------------------------------------------------------------
  //------------------------------------------------------------------------------------------------------------------------------------------------

  /**adds a new active client (in this case active client is defined as a session which has an employeeID, thus it is logged it)*/
  public void addActiveClient(ClientInterface client)
  {
    activeClients.add(client);
  }

  /**removes an active client*/
  public void removeActiveClient(ClientInterface client)
  {
    activeClients.remove(client);
  }

  /**adds a new client (used whenever the Server accepted an incoming connection and started a ClientHandler Thread)*/
  public void addClient(ClientInterface client)
  {
    clients.add(client);
    Log.get().add("[CONNECTION] new session " + getCurrentSessionID());
    sendToSession(getCurrentSessionID(), "<command>sessionID=" + getCurrentSessionID() + "</command>");
  }

  /**removes a client (used whenever a connection is closed towards a client)*/
  public void removeClient(ClientInterface client)
  {
    Log.get().add("[CONNECTION] session " + client.getSessionID() + " has been disconnected");
    departmentManager.releaseAll();
    employeeManager.releaseAll();
    clients.remove(client);
  }

  /**a String processor which breaks down a request coming from the client, into its bare components which are: tag, attributes and value*/
  public void receiveContent(String text)
  {
    Log.get().add("[RECEIVING from " + getCurrentSessionID() + "] " + text);
    try
    {
      String[] temp;
      temp = text.split("</");
      String endTag = "</" + temp[temp.length - 1];
      temp = text.split(">");
      String startTag = temp[0] + ">";
      String value = text.substring(startTag.length(), text.length() - endTag.length());
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

  /**a String processor with multiple freedom degrees to process an incoming request according to its: tag, attributes and value</br>
   * <i>controls the responses and actions of the system, please refer to Implementation part of Project Report for further detail</i>*/
  private void processContent(String tag, String value, String[] attributes)
  {
    RuntimeException exception = null;
    boolean recognized = false;
    switch (tag)
    {
      case "login":
        try {
          //_________________________________________________________________________________________________________________________
          String[] temp = value.split("//");
          if (employeeManager.readEmployee(Integer.parseInt(temp[0])).isPassword(temp[1]))
          {
            Log.get().add("[LOGIN] user " + Integer.parseInt(temp[0]) + " logged in from session " + getCurrentSessionID());
            sendToSession(getCurrentSessionID(),"<login>user=" + Integer.parseInt(temp[0]) + "</login>");
            recognized = true;

            getCurrentClient().setUserID(Integer.parseInt(temp[0]));
            addActiveClient(getCurrentClient());
          }
          else
          {
            Log.get().add("[LOGIN] user " + Integer.parseInt(temp[0]) + "'s login denied from session " + getCurrentSessionID());
            sendToSession(getCurrentSessionID(),"<login>denied</login>");
            recognized = true;
          }
          //-------------------------------------------------------------------------------------------------------------------------
        } catch (StringIndexOutOfBoundsException | ArrayIndexOutOfBoundsException e) {
          sendToSession(getCurrentSessionID(),"<login>denied</login>");
          exception = e;
        }
        break;
      case "sync":
        try {
          //_________________________________________________________________________________________________________________________
          if (value.equals("getAll"))
          {
            int[] temp = employeeManager.getActiveEmployees();
            for (int employee : temp)
              sendToSession(getCurrentSessionID(),sync(employeeManager.readEmployee(employee)));
            temp = departmentManager.getDepartments();
            for (int department : temp)
              sendToSession(getCurrentSessionID(),sync(departmentManager.readDepartment(department)));
            temp = chatManager.getChats(getCurrentEmployeeID());
            for (int chat : temp)
            {
              sendToSession(getCurrentSessionID(),sync(chatManager.getChat(chat)));
              for (Message message : chatManager.getChat(chat).getHistory())
                sendToSession(getCurrentSessionID(),sync(message));
            }
            recognized = true;
          }
          //-------------------------------------------------------------------------------------------------------------------------
        } catch (StringIndexOutOfBoundsException | ArrayIndexOutOfBoundsException e) {exception = e;}
        break;
      case "command":
        try {
          //_________________________________________________________________________________________________________________________
          if (value.equals("newEmployee"))
          {
            if (employeeManager.readEmployee(getCurrentEmployeeID()).isAdmin())
            {
              Employee employee = persistence.newEmployee();
              employeeManager.loadEmployee(employee);
              sendToAllActiveClients(sync(employee));

              int[] temp = employeeManager.getActiveEmployees();
              for (int i : temp)
                if (employeeManager.readEmployee(i).getID() != employee.getID())
                {
                  Chat privatechat = persistence.newChat(i + " - " + employee.getID(),false, new int[]{employee.getID(), i});
                  chatManager.loadChat(privatechat);
                  sendToEmployeesClients(new int[]{employee.getID(), i}, sync(privatechat));
                }
            }
            else
              Log.get().error(new IllegalStateException("session " + getCurrentSessionID() + " has no admin rights"));
            recognized = true;
          }
          //-------------------------------------------------------------------------------------------------------------------------
        } catch (StringIndexOutOfBoundsException | ArrayIndexOutOfBoundsException e) {exception = e;}
        try {
          //_________________________________________________________________________________________________________________________
          if (value.substring(0,"archiveEmployee=".length()).equals("archiveEmployee="))
          {
            if (employeeManager.readEmployee(getCurrentEmployeeID()).isAdmin())
            {
              int ID = Integer.parseInt(value.substring("archiveEmployee=".length()));
              Employee employee = employeeManager.cloneEmployee(ID);
              employee.archive();
              employee.setDepartment(0);
              employee.setPassword("ARCHIVED");
              employee.setAdmin(false);
              employee = persistence.updateEmployee(employee);
              employeeManager.loadEmployee(employee);
              sendToAllActiveClients(sync(employee));

              int[] temp = chatManager.getChats(ID);
              for (int i : temp)
                if (chatManager.getChat(i).isGroup())
                {
                  int[] members = chatManager.getChat(i).getMembers();
                  if (members.length == 1)
                  {
                    persistence.removeChat(i);
                    chatManager.remove(i);
                    sendToEmployeeClients(ID, "<command>removeChat=" + i + "</command>");
                  }
                  else
                  {
                    Chat groupchat = chatManager.cloneChat(i);
                    groupchat.removeMember(ID);
                    groupchat = persistence.updateChat(groupchat);
                    chatManager.loadChat(groupchat);
                    sendToEmployeesClients(members, sync(groupchat));
                    sendToEmployeeClients(ID, "<command>removeChat=" + i + "</command>");
                  }
                }
                else
                {
                  persistence.removeChat(i);
                  chatManager.remove(i);
                  sendToAllActiveClients("<command>removeChat=" + i + "</command>");
                }
            }
            else
              Log.get().error(new IllegalStateException("session " + getCurrentSessionID() + " has no admin rights"));
            recognized = true;
          }
          //-------------------------------------------------------------------------------------------------------------------------
        } catch (StringIndexOutOfBoundsException | ArrayIndexOutOfBoundsException e) {exception = e;}
        try {
          //_________________________________________________________________________________________________________________________
          if (value.equals("newDepartment"))
          {
            if (employeeManager.readEmployee(getCurrentEmployeeID()).isAdmin())
            {
              Department department = persistence.newDepartment();
              departmentManager.loadDepartment(department);
              sendToAllActiveClients(sync(department));
            }
            else
              Log.get().error(new IllegalStateException("session " + getCurrentSessionID() + " has no admin rights"));
            recognized = true;
          }
          //-------------------------------------------------------------------------------------------------------------------------
        } catch (StringIndexOutOfBoundsException | ArrayIndexOutOfBoundsException e) {exception = e;}
        try {
          //_________________________________________________________________________________________________________________________
          if (value.substring(0,"removeDepartment=".length()).equals("removeDepartment="))
          {
            if (employeeManager.readEmployee(getCurrentEmployeeID()).isAdmin())
            {
              int ID = Integer.parseInt(value.substring("removeDepartment=".length()));

              int[] temp = employeeManager.getActiveEmployees();
              for (int i : temp)
                if (employeeManager.readEmployee(i).getDepartment() == ID)
                {
                  Employee employee = employeeManager.cloneEmployee(i);
                  employee.setDepartment(0);
                  employee = persistence.updateEmployee(employee);
                  employeeManager.loadEmployee(employee);
                  sendToAllActiveClients(sync(employee));
                }

              persistence.removeDepartment(ID);
              departmentManager.remove(ID);
              sendToAllActiveClients("<command>removeDepartment=" + ID + "</command>");
            }
            else
              Log.get().error(new IllegalStateException("session " + getCurrentSessionID() + " has no admin rights"));
            recognized = true;
          }
          //-------------------------------------------------------------------------------------------------------------------------
        } catch (StringIndexOutOfBoundsException | ArrayIndexOutOfBoundsException e) {exception = e;}
        try {
          //_________________________________________________________________________________________________________________________
          if (value.equals("newGroupChat"))
          {
            Chat groupchat = persistence.newChat("new group chat", true, new int[]{getCurrentEmployeeID()});
            chatManager.loadChat(groupchat);
            sendToEmployeeClients(getCurrentEmployeeID(), sync(groupchat));
            recognized = true;
          }
          //-------------------------------------------------------------------------------------------------------------------------
        } catch (StringIndexOutOfBoundsException | ArrayIndexOutOfBoundsException e) {exception = e;}
        break;
      case "lock":
        try {
          //_________________________________________________________________________________________________________________________
          if (value.substring(0,"employee=".length()).equals("employee="))
          {
            if (employeeManager.readEmployee(getCurrentEmployeeID()).isAdmin())
            {
              employeeManager.acquireEmployee(Integer.parseInt(value.substring("employee=".length())));
            }
            else
              Log.get().error(new IllegalStateException("session " + getCurrentSessionID() + " has no admin rights"));
            recognized = true;
          }
          //-------------------------------------------------------------------------------------------------------------------------
        } catch (StringIndexOutOfBoundsException | ArrayIndexOutOfBoundsException e) {exception = e;}
        try {
          //_________________________________________________________________________________________________________________________
          if (value.substring(0,"department=".length()).equals("department="))
          {
            if (employeeManager.readEmployee(getCurrentEmployeeID()).isAdmin())
            {
              departmentManager.acquireDepartment(Integer.parseInt(value.substring("department=".length())));
            }
            else
              Log.get().error(new IllegalStateException("session " + getCurrentSessionID() + " has no admin rights"));
            recognized = true;
          }
          //-------------------------------------------------------------------------------------------------------------------------
        } catch (StringIndexOutOfBoundsException | ArrayIndexOutOfBoundsException e) {exception = e;}
        try {
          //_________________________________________________________________________________________________________________________
          if (value.equals("releaseAll"))
          {
            employeeManager.releaseAll();
            departmentManager.releaseAll();
            recognized = true;
          }
          //-------------------------------------------------------------------------------------------------------------------------
        } catch (StringIndexOutOfBoundsException | ArrayIndexOutOfBoundsException e) {exception = e;}
        break;
      case "department":
        try {
          //_________________________________________________________________________________________________________________________
          if (employeeManager.readEmployee(getCurrentEmployeeID()).isAdmin())
          {
            String[] temp = value.split("//");
            int ID = Integer.parseInt(temp[0]);
            String name = temp[1];
            DepartmentEdit department = departmentManager.acquireDepartment(ID);
            department.update(name);
            Department department1 = departmentManager.cloneDepartment(ID);
            department1 = persistence.updateDepartment(department1);
            departmentManager.loadDepartment(department1);
            sendToAllActiveClients(sync(department1));
          }
          else
            Log.get().error(new IllegalStateException("session " + getCurrentSessionID() + " has no admin rights"));
          recognized = true;
          //-------------------------------------------------------------------------------------------------------------------------
        } catch (StringIndexOutOfBoundsException | ArrayIndexOutOfBoundsException e) {exception = e;}
        break;
      case "employee":
        try {
          //_________________________________________________________________________________________________________________________
          if (employeeManager.readEmployee(getCurrentEmployeeID()).isAdmin())
          {
            String[] temp = value.split("//");
            int ID = Integer.parseInt(temp[0]);
            String name = temp[1];
            int department = Integer.parseInt(temp[2]);
            boolean admin = Boolean.parseBoolean(temp[3]);
            EmployeeEdit employee = employeeManager.acquireEmployee(ID);
            employee.update(name, department, admin);
            Employee employee1 = employeeManager.cloneEmployee(ID);
            employee1 = persistence.updateEmployee(employee1);
            employeeManager.loadEmployee(employee1);
            sendToAllActiveClients(sync(employee1));
          }
          else
            Log.get().error(new IllegalStateException("session " + getCurrentSessionID() + " has no admin rights"));
          recognized = true;
          //-------------------------------------------------------------------------------------------------------------------------
        } catch (StringIndexOutOfBoundsException | ArrayIndexOutOfBoundsException e) {exception = e;}
        break;
      case "password":
        try {
          //_________________________________________________________________________________________________________________________
          if (employeeManager.readEmployee(getCurrentEmployeeID()).isAdmin())
          {
            String[] temp = value.split("//");
            int ID = Integer.parseInt(temp[0]);
            String password = temp[1];
            EmployeeEdit employee = employeeManager.acquireEmployee(ID);
            employee.setPassword(password);
            Employee employee1 = employeeManager.cloneEmployee(ID);
            employee1 = persistence.updateEmployee(employee1);
            employeeManager.loadEmployee(employee1);
          }
          else
            Log.get().error(new IllegalStateException("session " + getCurrentSessionID() + " has no admin rights"));
          recognized = true;
          //-------------------------------------------------------------------------------------------------------------------------
        } catch (StringIndexOutOfBoundsException | ArrayIndexOutOfBoundsException e) {exception = e;}
        break;
      case "message":
        try {
          //_________________________________________________________________________________________________________________________
          int ID = Integer.parseInt(attributes[0].substring("id=".length()));
          if (chatManager.getChat(ID).isInvolved(getCurrentEmployeeID()))
          {
            Message message = persistence.newMessage(value, getCurrentEmployeeID(), ID);
            chatManager.loadMessage(message);
            int[] temp = chatManager.getChat(ID).getMembers();
            for (int i : temp)
              sendToEmployeeClients(i, syncAs(message, i));
          }
          else
            Log.get().error(new IllegalStateException("session " + getCurrentSessionID() + " has no rights to access chat " + ID));
          recognized = true;
          //-------------------------------------------------------------------------------------------------------------------------
        } catch (StringIndexOutOfBoundsException | ArrayIndexOutOfBoundsException e) {exception = e;}
        break;
      case "groupchat":
        try {
          //_________________________________________________________________________________________________________________________
          if (value.substring(0,"name=".length()).equals("name="))
          {
            int ID = Integer.parseInt(attributes[0].substring("id=".length()));
            if (chatManager.getChat(ID).isInvolved(getCurrentEmployeeID()))
            {
              String name = value.substring("name=".length());
              Chat groupchat = chatManager.cloneChat(ID);
              groupchat.setName(name);
              groupchat = persistence.updateChat(groupchat);
              chatManager.loadChat(groupchat);
              sendToEmployeesClients(groupchat.getMembers(), sync(groupchat));
            }
            else
              Log.get().error(new IllegalStateException("session " + getCurrentSessionID() + " has no rights to access chat " + ID));
            recognized = true;
          }
          //-------------------------------------------------------------------------------------------------------------------------
        } catch (StringIndexOutOfBoundsException | ArrayIndexOutOfBoundsException e) {exception = e;}
        try {
          //_________________________________________________________________________________________________________________________
          if (value.substring(0,"removeMember=".length()).equals("removeMember="))
          {
            int ID = Integer.parseInt(attributes[0].substring("id=".length()));
            if (chatManager.getChat(ID).isInvolved(getCurrentEmployeeID()))
            {
              int[] members = chatManager.getChat(ID).getMembers();
              int member = Integer.parseInt(value.substring("removeMember=".length()));
              if (members.length == 1)
              {
                persistence.removeChat(ID);
                chatManager.remove(ID);
                sendToEmployeeClients(member, "<command>removeChat=" + ID + "</command>");
              }
              else
              {
                Chat groupchat = chatManager.cloneChat(ID);
                groupchat.removeMember(member);
                groupchat = persistence.updateChat(groupchat);
                chatManager.loadChat(groupchat);
                sendToEmployeesClients(members, sync(groupchat));
                sendToEmployeeClients(member, "<command>removeChat=" + ID + "</command>");
              }
            }
            else
              Log.get().error(new IllegalStateException("session " + getCurrentSessionID() + " has no rights to access chat " + ID));
            recognized = true;
          }
          //-------------------------------------------------------------------------------------------------------------------------
        } catch (StringIndexOutOfBoundsException | ArrayIndexOutOfBoundsException e) {exception = e;}
        try {
          //_________________________________________________________________________________________________________________________
          if (value.substring(0,"addMember=".length()).equals("addMember="))
          {
            int ID = Integer.parseInt(attributes[0].substring("id=".length()));
            if (chatManager.getChat(ID).isInvolved(getCurrentEmployeeID()))
            {
              int member = Integer.parseInt(value.substring("addMember=".length()));
              Chat groupchat = chatManager.cloneChat(ID);
              groupchat.addMember(member);
              groupchat = persistence.updateChat(groupchat);
              chatManager.loadChat(groupchat);
              int[] members = groupchat.getMembers();
              sendToEmployeesClients(members, sync(groupchat));
              for (Message message : chatManager.getChat(ID).getHistory())
                sendToEmployeeClients(member,syncAs(message, member));
            }
            else
              Log.get().error(new IllegalStateException("session " + getCurrentSessionID() + " has no rights to access chat " + ID));
            recognized = true;
          }
          //-------------------------------------------------------------------------------------------------------------------------
        } catch (StringIndexOutOfBoundsException | ArrayIndexOutOfBoundsException e) {exception = e;}
    }
    if (!recognized)
      if (exception == null)
        Log.get().error(new UnsupportedOperationException("instruction not recognized"));
      else
        Log.get().error(new UnsupportedOperationException("instruction not recognized", exception));
  }

  //------------------------------------------------------------------------------------------------------------------------------------------------
  //------------------------------------------------------------------------------------------------------------------------------------------------
  //------------------------------------------------------------------------------------------------------------------------------------------------
  //------------------------------------------------------------------------------------------------------------------------------------------------

  /**method used to appropriately format an Employee into a single line of instruction which then can be sent to client and can be processed by them*/
  private String sync(EmployeeRead employee)
  {
    String temp;
    if (employee.isArchived())
    {
      temp = "<command>archiveEmployee=" + employee.getID() + "</command>";
    }
    else
    {
      temp = "<employee>";
      temp += employee.getID() + "//";
      temp += employee.getName() + "//";
      temp += employee.getDepartment() + "//";
      temp += employee.isAdmin() + "</employee>";
    }
    return temp;
  }

  /**method used to appropriately format a Department into a single line of instruction which then can be sent to client and can be processed by them*/
  private String sync(DepartmentRead department)
  {
    String temp;
    temp = "<department>";
    temp += department.getID() + "//";
    temp += department.getName() + "</department>";
    return temp;
  }

  /**method used to appropriately format a Chat into a single line of instruction which then can be sent to client and can be processed by them*/
  private String sync(Chat chat)
  {
    String temp;
    int[] temp1 = chat.getMembers();
    if (chat.isGroup())
    {
      temp = "<groupchat>";
      temp += chat.getID() + "//";
      temp += chat.getName() + "//";
      for (int i = 0; i < temp1.length - 1; i++)
        temp += temp1[i] + ",";
      temp += temp1[temp1.length-1] + "</groupchat>";
    }
    else
    {
      temp = "<chat>";
      temp += chat.getID() + "//";
      temp += temp1[0] + ",";
      temp += temp1[1] + "</chat>";
    }
    return temp;
  }

  /**method used to appropriately format a Message into a single line of instruction from the current session's associated employee's point of view which then can be sent to client and can be processed by them*/
  private String sync(Message message)
  {
    String temp;
    temp = "<message//id=";
    temp += message.getChatID() + ">";
    if (message.getEmployeeID() == getCurrentEmployeeID())
      temp += "You: ";
    else
      temp += employeeManager.readEmployee(message.getEmployeeID()).getName() + ": ";
    temp += message.getMessage() + "</message>";
    return temp;
  }

  /**method used to appropriately format an Message into a single line of instruction from a given employee's point of view which then can be sent to client and can be processed by them*/
  private String syncAs(Message message, int employeePOV)
  {
    String temp;
    temp = "<message//id=";
    temp += message.getChatID() + ">";
    if (message.getEmployeeID() == employeePOV)
      temp += "You: ";
    else
      temp += employeeManager.readEmployee(message.getEmployeeID()).getName() + ": ";
    temp += message.getMessage() + "</message>";
    return temp;
  }

  /**returns the current ClientInterface (based on the caller Thread)*/
  private ClientInterface getCurrentClient()
  {
    if (!(Thread.currentThread() instanceof ClientHandler))
      return new ClientInterface() {
        @Override public void setUserID(int employeeID) {throw new RuntimeException("for debugging only: setting user id " + employeeID);}
        @Override public int getUserID() {return -1;}
        @Override public void sendToClient(String text) {throw new RuntimeException("for debugging only: sending " + text);}
        @Override public int getSessionID() {return -1;}};
    for (ClientInterface client : clients)
      if (client.getSessionID() == Integer.parseInt(Thread.currentThread().getName()))
        return client;
    throw new IllegalThreadStateException("can't find caller Client Thread");
  }

  /**sends formatted content to a sessions*/
  private void sendToClient(ClientInterface client, String text)
  {
    Log.get().add("[SENDING to " + client.getSessionID() + "] " + text);
    client.sendToClient(text);
  }

  /**uses sendToClient() to send the content to all active clients*/
  private void sendToAllActiveClients(String text)
  {
    for (ClientInterface activeClient : activeClients)
      sendToClient(activeClient, text);
  }

  /**uses sendToClient() to send the content to all sessions associated with a given employee*/
  private void sendToEmployeeClients(int employeeID, String text)
  {
    for (ClientInterface activeClient : activeClients)
      if (activeClient.getUserID() == employeeID)
        sendToClient(activeClient, text);
  }

  /**uses sendToClient() to send the content to all sessions associated with multiple given employee*/
  private void sendToEmployeesClients(int[] employeeIDs, String text)
  {
    for (ClientInterface activeClient : activeClients)
      for (int employeeID : employeeIDs)
        if (activeClient.getUserID() == employeeID)
          sendToClient(activeClient, text);
  }

  /**uses sendToClient() to send the content to an explicit sessions*/
  private void sendToSession(int sessionID, String text)
  {
    for (ClientInterface client : clients)
      if (client.getSessionID() == sessionID)
      {
        sendToClient(client, text);
        break;
      }
  }

  /**uses getCurrentClient() and returns the employeeID of the session*/
  private int getCurrentEmployeeID()
  {
    return getCurrentClient().getUserID();
  }

  /**uses getCurrentClient() and returns the sessionID of the session*/
  public int getCurrentSessionID()
  {
    return getCurrentClient().getSessionID();
  }

  /**sends an appropriately formatted message to a session, that it has got the lock on an Employee (used by EmployeeProxy)*/
  public void employeeAcquired(int sessionID, int employeeID)
  {
    sendToSession(sessionID, "<lock>employeeAcquired=" + employeeID + "</lock>");
  }

  /**sends an appropriately formatted message to a session, that it has got the lock on a Department (used by DepartmentProxy)*/
  public void departmentAcquired(int sessionID, int departmentID)
  {
    sendToSession(sessionID, "<lock>departmentAcquired=" + departmentID + "</lock>");
  }
}