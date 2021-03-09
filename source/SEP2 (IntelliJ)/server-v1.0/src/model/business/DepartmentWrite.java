package model.business;

/**responsible to differentiate between the read and write methods of the Department*/
public interface DepartmentWrite extends DepartmentRead
{
  /**updates the name according to the arguments*/
  void update(String name);
}
