package mediator;

/**responsible for keeping the Dependency Inversion principle*/
public interface ClientInterface
{
  /**tries to connect to a host*/
  void connect(String host);
  /**disconnects from current server*/
  void disconnect();
  /**sends a request to the server*/
  void sendToServer(String content);
}
