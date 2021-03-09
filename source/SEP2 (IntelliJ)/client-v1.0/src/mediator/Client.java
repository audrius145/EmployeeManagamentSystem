package mediator;

import model.Receiver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**responsible for sending/receiving content to/from the server*/
public class Client extends Thread implements ClientInterface
{
  /**predefined port for communication*/
  private static final int port = 25565;
  /**value used to break the while loop in the Thread's run() method*/
  private boolean running;
  /**object implementing Receiver interface where the received instructions from the server will be forwarded*/
  private Receiver receiver;

  private Socket socket;
  private BufferedReader in;
  private PrintWriter out;

  /**takes an object implementing Receiver interface where the received instructions from the server will be forwarded*/
  public Client(Receiver receiver)
  {
    running = false;
    this.receiver = receiver;
  }

  /**tries to open a new socket towards the inputted host*/
  public void connect(String host)
  {
    try
    {
      socket = new Socket(host, port);
      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      out = new PrintWriter(socket.getOutputStream(), true);
      this.start();
    }
    catch (IOException e)
    {
      receiver.connectionRefused();
    }
  }

  /**breaks the while loop in the Thread's run() method and closes the current socket*/
  public void disconnect()
  {
    running = false;
    try
    {
      if (socket != null)
        if (!socket.isClosed())
        {
          socket.close();
          receiver.disconnectedFromServer();
          in.close();
          out.close();
        }
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  /**sends a request to server (needs to be properly formatted in order for the server side to recognize)*/
  public void sendToServer(String content)
  {
    if (socket != null)
    if (!socket.isClosed())
    {
      out.println(content);
    }
    else
    {
      new Thread(() -> {
        try {
          for (int i = 0; i < 3; i++)
          {
            Thread.sleep(50);
            if (!socket.isClosed())
            {
              out.println(content);
              break;
            }
          }
        } catch (InterruptedException ignore) {}
      }).start();
    }
  }

  /**Thread's run() method, which is responsible for receiving incoming instructions from the server*/
  @Override
  public void run()
  {
    running = true;
    while (running)
    {
      try
      {
        receiver.receiveContent(in.readLine());
      }
      catch (IOException e)
      {
        disconnect();
      }
    }
    disconnect();
  }
}
