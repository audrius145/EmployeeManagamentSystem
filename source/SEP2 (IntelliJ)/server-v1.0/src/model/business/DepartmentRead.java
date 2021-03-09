package model.business;

/**responsible to differentiate between the read and write methods of the Department*/
public interface DepartmentRead
{
  /**returns the ID*/
  int getID();
  /**returns the name*/
  String getName();
}
