package model.business;

/**responsible to differentiate between the read and write methods of the Department*/
public interface EmployeeWrite extends EmployeeRead
{
  /**updates the name, department and admin according to the arguments*/
  void update(String name, int department, boolean admin);
  /**sets the password*/
  void setPassword(String password);
}