package model.business;

/**this class is responsible for keeping a cached version of a department*/
public class Department implements DepartmentWrite
{
  /**proxy of the department (part of locking pattern)*/
  private DepartmentProxy proxy;

  /**ID of the department*/
  private int ID;
  /**name of the department*/
  private String name;

  /**initializes all instance variables according to the arguments*/
  public Department(int ID, String name)
  {
    this.ID = ID;
    this.name = name;
  }

  /**initializes the proxy of the department*/
  void initProxy()
  {
    proxy = new DepartmentProxy(this);
  }

  /**returns the proxy of the department*/
  DepartmentProxy getProxy()
  {
    return proxy;
  }

  /**updates the name according to the arguments*/
  @Override public void update(String name)
  {
    this.name = name;
  }

  /**returns the ID*/
  @Override public int getID()
  {
    return ID;
  }

  /**returns the name*/
  @Override public String getName()
  {
    return name;
  }

  /**sets the name*/
  public void setName(String name)
  {
    this.name = name;
  }

  /**overwrites name with the other Departments's attributes in the argument*/
  void overWrite(Department department)
  {
    this.name = department.getName();
  }

  /**two Departments counts as identical if their ID is identical*/
  public boolean equals(Object object)
  {
    if (!(object instanceof Department))
      return false;
    return ((Department)object).getID() == ID;
  }
}