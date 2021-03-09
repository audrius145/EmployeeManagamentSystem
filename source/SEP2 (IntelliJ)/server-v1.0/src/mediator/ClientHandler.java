package mediator;

import model.Receiver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**responsible for sending/receiving content to/from a client*/
public class ClientHandler extends Thread implements ClientInterface
{
  /**value used to break the while loop in the Thread's run() method*/
  private boolean running;
  /**value stores the employeeID of the employee who is logged in from the session (it is 0 if no one has logged in)*/
  private int employeeID;
  /**object implementing Receiver interface where the received requests from the client will be forwarded*/
  private Receiver receiver;

  private Socket socket;
  private BufferedReader in;
  private PrintWriter out;

  /**takes an object implementing Receiver interface where the received requests from the client will be forwarded, as well as its ThreadGroup, sessionID and Socket which are defined by the Server</br>
   * IMPORTANT: sessionID is stored in the Thread superclass' name instance variable*/
  ClientHandler(Socket socket, int sessionID, ThreadGroup group, Receiver receiver)
  {
    super(group, String.valueOf(sessionID));
    this.receiver = receiver;
    this.socket = socket;
    try
    {
      this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      this.out = new PrintWriter(socket.getOutputStream(), true);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  /**sets the employeeID*/
  public void setUserID(int employeeID)
  {
    this.employeeID = employeeID;
  }

  /**returns the employeeID*/
  public int getUserID()
  {
    return employeeID;
  }

  /**returns the sessionID</br>
   * IMPORTANT: sessionID is stored in the Thread superclass' name instance variable*/
  public int getSessionID()
  {
    return Integer.parseInt(super.getName());
  }

  /**Thread's run() method, which is responsible for receiving incoming requests from the client*/
  public void run()
  {
    receiver.addClient(this);
    running = true;
    while (running)
    {
      try
      {
        String temp = in.readLine();
        if (temp == null)
          close();
        else
          receiver.receiveContent(temp);
      }
      catch (IOException e)
      {
        close();
      }
    }
    close();
  }

  /**breaks the while loop in the Thread's run() method and closes the current socket*/
  void close()
  {
    running = false;
    try
    {
      if (!socket.isClosed())
      {
        socket.close();
        receiver.removeActiveClient(this);
        receiver.removeClient(this);
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  /**sends an instruction to client (needs to be properly formatted in order for the client side to recognize)*/
  public void sendToClient(String text)
  {
    out.println(text);
  }

  /**two ClientHandler counts as identical if their sessionID is identical*/
  public boolean equals(Object obj)
  {
    if (!(obj instanceof ClientHandler))
      return false;
    return ((ClientHandler)obj).getName().equals(super.getName());
  }

  /**returns the sessionID and the employeeID*/
  public String toString()
  {
    return "sessionID=" + super.getName() + " employeeID=" + employeeID;
  }
}
