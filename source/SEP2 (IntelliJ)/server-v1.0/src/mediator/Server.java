package mediator;

import model.Receiver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**responsible for creating new ClientHandler Threads for every incoming connection*/
public class Server implements Runnable
{
  /**predefined port for communication*/
  private final static int port = 25565;
  /**the created ClientHandler Threads' proprietary ThreadGroup*/
  private final static ThreadGroup group = new ThreadGroup("ClientHandlerThreads");
  /**value used to break the while loop in the Thread's run() method*/
  private boolean running;
  /**counter initialized as 0 and incremented for every opened ClientHandler, defines the ClientHandlers' sessionID*/
  private int clientCounter;
  /**object implementing Receiver interface where the received requests from the client will be forwarded*/
  private Receiver receiver;

  private ServerSocket socket;

  /**takes an object implementing Receiver interface where the received requests from the client will be forwarded*/
  public Server(Receiver receiver)
  {
    this.receiver = receiver;
    clientCounter = 0;
    new Thread(this).start();
  }

  /**Thread's run() method, which is responsible for creating new ClientHandlers for incoming connections*/
  public void run()
  {
    try
    {
      socket = new ServerSocket(port);
      running = true;
      while (running)
      {
        Socket clientSocket = socket.accept();
        ClientHandler clientHandler = new ClientHandler(clientSocket, ++clientCounter, group, receiver);
        clientHandler.start();
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  /**this method closes the ServerSocket and as well as stops all the ClientHandler threads*/
  public void stop()
  {
    running = false;
    ClientHandler[] threads = new ClientHandler[group.activeCount()];
    group.enumerate(threads);
    for (ClientHandler t : threads)
    {
      try {
        t.close();
      } catch (Exception ignored) {}
    }
    try
    {
      socket.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
