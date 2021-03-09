package model.business;

/**responsible for keeping the Interface Segregation principle principle*/
public interface EmployeeEdit extends EmployeeWrite
{
  /**returns true if the Caller ClientHandler Thread is first in the queue meaning it has the lock and write privilege*/
  boolean isAcquired();
}
