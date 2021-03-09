package model;

import mediator.ClientInterface;

/**responsible for keeping the Dependency Inversion principle*/
public interface Receiver
{
  /**adds a new active client (in this case active client is defined as a session which has an employeeID, thus it is logged it)*/
  void addActiveClient(ClientInterface client);
  /**removes an active client*/
  void removeActiveClient(ClientInterface client);
  /**adds a new client (used whenever the Server accepted an incoming connection and started a ClientHandler Thread)*/
  void addClient(ClientInterface client);
  /**removes a client (used whenever a connection is closed towards a client)*/
  void removeClient(ClientInterface client);
  /**forwards the received content (from the client) to the object implementing this*/
  void receiveContent(String text);
}