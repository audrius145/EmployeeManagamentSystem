package model;

/**responsible for keeping the Dependency Inversion principle*/
public interface Receiver
{
  /**forwards the received content (from the server) to the object implementing this*/
  void receiveContent(String content);
  /**possibly called after calling ClientInterface.connect(host), if the host refuses the connection*/
  void connectionRefused();
  /**called whenever the connection to the server is closed, whether it is intentional or not*/
  void disconnectedFromServer();
}