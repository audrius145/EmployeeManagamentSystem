package mediator;

/**responsible for keeping the Dependency Inversion principle*/
public interface ClientInterface
{
  /**sets employeeID*/
  void setUserID(int employeeID);
  /**returns the employeeID*/
  int getUserID();
  /**sends an instruction to the client*/
  void sendToClient(String text);
  /**returns the sessionID*/
  int getSessionID();
}
