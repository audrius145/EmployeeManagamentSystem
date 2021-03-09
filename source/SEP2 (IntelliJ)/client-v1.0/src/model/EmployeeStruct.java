package model;

/**this class is responsible for storing and managing an employee's respective data*/
public class EmployeeStruct
{
  /**employee's id*/
  private int ID;
  /**employee's name*/
  private String name;
  /**employee's department*/
  private int department;
  /**employee's admin privileges*/
  private boolean admin;

  /**initializes all instance variables*/
  EmployeeStruct(int ID, String name, int department, boolean admin)
  {
    this.ID = ID;
    this.name = name;
    this.department = department;
    this.admin = admin;
  }

  /**updates all instance variables (except ID)*/
  void Update(String name, int department, boolean admin)
  {
    this.name = name;
    this.department = department;
    this.admin = admin;
  }

  /**returns the employee's id*/
  public int getID()
  {
    return ID;
  }

  /**returns the employee's name*/
  public String getName()
  {
    return name;
  }

  /**returns the employee's department*/
  public int getDepartment()
  {
    return department;
  }

  /**returns the employee's department number + department name*/
  public String getDepartmentCombo()
  {
    return department + " " + ModelManager.get().getDataHandler().getDepartment(department).getName();
  }

  /**returns the employee's admin privileges*/
  public boolean isAdmin()
  {
    return admin;
  }

  /**two employees are considered identical if their id matches*/
  public boolean equals(Object object)
  {
    if (!(object instanceof EmployeeStruct))
      return false;
    return ((EmployeeStruct)object).getID() == ID;
  }

  /**returns the employee's id + name combo*/
  public String toString()
  {
    return ID + " " + name;
  }
}
