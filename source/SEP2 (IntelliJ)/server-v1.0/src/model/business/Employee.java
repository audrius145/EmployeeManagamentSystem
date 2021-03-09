package model.business;

/**this class is responsible for keeping a cached version of an employee*/
public class Employee implements EmployeeWrite
{
  /**proxy of the employee (part of locking pattern)*/
  private EmployeeProxy proxy;

  /**ID of the employee*/
  private int ID;
  /**name of the employee*/
  private String name;
  /**departmentID of the employee*/
  private int department;
  /**admin privileges of the employee*/
  private boolean admin;
  /**password of the employee*/
  private String password;
  /**archived attribute of the employee*/
  private boolean archived;

  /**initializes all instance variables according to the arguments*/
  public Employee(int ID, String name, int department, boolean admin, String password, boolean archived)
  {
    this.ID = ID;
    this.name = name;
    this.department = department;
    this.admin = admin;
    this.password = password;
    this.archived = archived;
  }

  /**initializes the proxy of the employee*/
  void initProxy()
  {
    proxy = new EmployeeProxy(this);
  }

  /**returns the proxy of the employee*/
  EmployeeProxy getProxy()
  {
    return proxy;
  }

  /**updates the name, department and admin according to the arguments*/
  @Override public void update(String name, int department, boolean admin)
  {
    this.name = name;
    this.department = department;
    this.admin = admin;
  }

  /**sets the password*/
  @Override public void setPassword(String password)
  {
    this.password = password;
  }

  /**returns the ID*/
  @Override public int getID()
  {
    return ID;
  }

  /**returns the archived*/
  @Override public boolean isArchived()
  {
    return archived;
  }

  /**returns the admin*/
  @Override public boolean isAdmin()
  {
    return admin;
  }

  /**returns the name*/
  @Override public String getName()
  {
    return name;
  }

  /**returns the departmentID*/
  @Override public int getDepartment()
  {
    return department;
  }

  /**returns true if the password in the argument matches the password instance variable*/
  @Override public boolean isPassword(String password)
  {
    return this.password.equals(password);
  }

  /**sets the name*/
  public void setName(String name)
  {
    this.name = name;
  }

  /**sets the department*/
  public void setDepartment(int department)
  {
    this.department = department;
  }

  /**sets the sets the admin*/
  public void setAdmin(boolean admin)
  {
    this.admin = admin;
  }

  /**gets the password*/
  public String getPassword()
  {
    return password;
  }

  /**sets archived to true and the proxy to null*/
  public void archive()
  {
    archived = true;
    proxy = null;
  }

  /**overwrites name, department, admin, password, archived with the other Employees's attributes in the argument*/
  void overWrite(Employee employee)
  {
    this.name = employee.getName();
    this.department = employee.getDepartment();
    this.admin = employee.isAdmin();
    this.password = employee.getPassword();
    this.archived = employee.isArchived();
    if (archived)
      proxy = null;
  }

  /**two Employees counts as identical if their ID is identical*/
  public boolean equals(Object object)
  {
    if (!(object instanceof Employee))
      return false;
    return ((Employee)object).getID() == ID;
  }
}
