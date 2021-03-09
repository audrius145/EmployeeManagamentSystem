package model;

/**this class is responsible for storing and managing a department's respective data*/
public class DepartmentStruct
{
  /**department's id*/
  private int ID;
  /**department's name*/
  private String name;

  /**initializes all instance variables*/
  DepartmentStruct(int ID, String name)
  {
    this.ID = ID;
    this.name = name;
  }

  /**updates all instance variables (except ID)*/
  void Update(String name)
  {
    this.name = name;
  }

  /**returns the department's id*/
  public int getID()
  {
    return ID;
  }

  /**returns the department's name*/
  public String getName()
  {
    return name;
  }

  /**returns the department's number of attending employees*/
  public int getEmployeeNumber()
  {
    int cnt = 0;
    for (EmployeeStruct employee : ModelManager.get().getDataHandler().getEmployees())
      if (ModelManager.get().getDataHandler().getDepartment(employee.getDepartment()).getID() == ID)
        cnt++;
    return cnt;
  }

  /**two departments are considered identical if their id matches*/
  public boolean equals(Object object)
  {
    if (!(object instanceof DepartmentStruct))
      return false;
    return ((DepartmentStruct)object).getID() == ID;
  }

  /**returns the department's id + name combo*/
  public String toString()
  {
    return ID + " " + name;
  }
}
