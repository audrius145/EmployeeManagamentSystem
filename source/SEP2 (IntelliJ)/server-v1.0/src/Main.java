import mediator.Server;
import model.ServerManager;
import persistence.PostgreManager;

/**main entry point of the software*/
public class Main
{
  /**creates a PostgreManager (input credentials here) then initializes the ServerManager, loads all data from db, and starts a new Server*/
  public static void main(String[] args)
  {
    PostgreManager database = new PostgreManager("jdbc:postgresql://localhost:5432/postgres?currentSchema=employee_management_sys", "postgres", "admin");
    ServerManager.init(database);
    ServerManager.get().loadAllData();
    Server server = new Server(ServerManager.get());
  }
}