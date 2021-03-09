package model.business;

import model.Log;

import java.util.ArrayList;

/**this class is responsible for managing Departments*/
public class DepartmentManager
{
  /**all Departments*/
  private ArrayList<Department> departments;

  /**initializes all instance variables*/
  public DepartmentManager()
  {
    departments = new ArrayList<>();
  }

  /**loads a new Department instance to the departments ArrayList, if it contains an identical Department, it overwrites some of its attributes using the .overwrite() method on the stored Department instance*/
  public void loadDepartment(Department newDepartment)
  {
    boolean exists = false;
    for (Department department : departments)
      if (department.getID() == newDepartment.getID())
      {
        department.overWrite(newDepartment);
        exists = true;
        break;
      }
    if (!exists)
    {
      newDepartment.initProxy();
      departments.add(newDepartment);
    }
  }

  /**adds the session to the proxy's queue and returns the proxy of the Department as a DepartmentEdit interface, based on a matching ID criteria taken from the argument*/
  public DepartmentEdit acquireDepartment(int ID)
  {
    for (Department department : departments)
    {
      if (department.getID() == ID)
      {
        department.getProxy().addToQueue();
        return department.getProxy();
      }
    }
    throw new NullPointerException("department " + ID + " is non existent");
  }

  /**removes the session from the Department's proxy's queue, based on a matching ID criteria taken from the argument*/
  public void releaseDepartment(int ID)
  {
    boolean exists = false;
    for (Department department : departments)
      if (department.getID() == ID)
      {
        department.getProxy().removeFromQueue();
        exists = true;
        break;
      }
    if (!exists)
      Log.get().error(new NullPointerException("department " + ID + " is non existent"));
  }

  /**removes the session from all of the Departments' proxies' queues*/
  public void releaseAll()
  {
    for (Department department : departments)
      department.getProxy().removeFromQueue();
  }

  /**returns a Department instance as a DepartmentRead interface based on a matching ID criteria taken from the argument*/
  public DepartmentRead readDepartment(int ID)
  {
    for (Department department : departments)
      if (department.getID() == ID)
        return department;
    throw new NullPointerException("department " + ID + " is non existent");
  }

  /**returns all DepartmentIDs*/
  public int[] getDepartments()
  {
    int[] temp = new int[departments.size()];
    for (int i = 0; i < departments.size(); i++)
      temp[i] = departments.get(i).getID();
    return temp;
  }

  /**removes a Department instance based on a matching ID criteria taken from the argument*/
  public void remove(int ID)
  {
    boolean exists = false;
    for (int i = 0; i < departments.size(); i++)
      if (departments.get(i).getID() == ID)
      {
        departments.remove(i);
        exists = true;
        break;
      }
    if (!exists)
      Log.get().error(new NullPointerException("department " + ID + " is non existent"));
  }

  /**returns a new Department instance with a matching ID and name attributes, based on a matching ID criteria taken from the argument*/
  public Department cloneDepartment(int ID)
  {
    for (Department department : departments)
      if (department.getID() == ID)
        return new Department(department.getID(), department.getName());
    throw new NullPointerException("department " + ID + " is non existent");
  }
}
