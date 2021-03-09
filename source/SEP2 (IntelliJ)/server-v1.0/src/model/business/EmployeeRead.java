package model.business;

/**responsible to differentiate between the read and write methods of the Employee*/
public interface EmployeeRead
{
  /**returns the ID*/
  int getID();
  /**returns the archived*/
  boolean isArchived();
  /**returns the admin*/
  boolean isAdmin();
  /**returns the name*/
  String getName();
  /**returns the departmentID*/
  int getDepartment();
  /**returns true if the password in the argument matches the password instance variable*/
  boolean isPassword(String password);
}
