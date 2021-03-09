package model.business;

import model.Log;

import java.util.ArrayList;

/**this class is responsible for managing Employees*/
public class EmployeeManager
{
  /**all Employees*/
  private ArrayList<Employee> employees;

  /**initializes all instance variables*/
  public EmployeeManager()
  {
    employees = new ArrayList<>();
  }

  /**loads a new Employee instance to the employees ArrayList, if it contains an identical Employee, it overwrites some of its attributes using the .overwrite() method on the stored Employee instance*/
  public void loadEmployee(Employee newEmployee)
  {
    boolean exists = false;
    for (Employee employee : employees)
      if (employee.getID() == newEmployee.getID())
      {
        employee.overWrite(newEmployee);
        exists = true;
        break;
      }
    if (!exists)
    {
      newEmployee.initProxy();
      employees.add(newEmployee);
    }
  }

  /**adds the session to the proxy's queue and returns the proxy of the Employee as an EmployeeEdit interface, based on a matching ID criteria taken from the argument*/
  public EmployeeEdit acquireEmployee(int ID)
  {
    for (Employee employee : employees)
    {
      if (employee.getID() == ID)
      {
        if (!employee.isArchived())
        {
          employee.getProxy().addToQueue();
          return employee.getProxy();
        }
        else
        {
          Log.get().error(new IllegalStateException("employee " + ID + " is archived"));
        }
        break;
      }
    }
    throw new NullPointerException("employee " + ID + " is non existent");
  }

  /**removes the session from the Employee's proxy's queue, based on a matching ID criteria taken from the argument*/
  public void releaseEmployee(int ID)
  {
    for (Employee employee : employees)
      if (employee.getID() == ID)
        if (!employee.isArchived())
          employee.getProxy().removeFromQueue();
        else
          Log.get().error(new IllegalStateException("employee " + ID + " is archived"));
  }

  /**removes the session from all of the Employees' proxies' queues*/
  public void releaseAll()
  {
    for (Employee employee : employees)
      if (!employee.isArchived())
        employee.getProxy().removeFromQueue();
  }

  /**returns an Employee instance as an EmployeeRead interface based on a matching ID criteria taken from the argument*/
  public EmployeeRead readEmployee(int ID)
  {
    for (Employee employee : employees)
      if (employee.getID() == ID)
        return employee;
    throw new NullPointerException("employee " + ID + " is non existent");
  }

  /**returns all EmployeeIDs which are not archived*/
  public int[] getActiveEmployees()
  {
    int cnt = 0;
    for (Employee employee : employees)
      if (!employee.isArchived())
        cnt++;
    int[] temp = new int[cnt];
    cnt = 0;
    for (Employee employee : employees)
      if (!employee.isArchived())
        temp[cnt++] = employee.getID();
    return temp;
  }

  /**returns all EmployeeIDs*/
  public int[] getAllEmployees()
  {
    int[] temp = new int[employees.size()];
    for (int i = 0; i < employees.size(); i++)
      temp[i] = employees.get(i).getID();
    return temp;
  }

  /**returns a new Employee instance with a matching ID, name, department, admin, password and archived attributes, based on a matching ID criteria taken from the argument*/
  public Employee cloneEmployee(int ID)
  {
    for (Employee employee : employees)
      if (employee.getID() == ID)
        return new Employee(employee.getID(), employee.getName(), employee.getDepartment(), employee.isAdmin(), employee.getPassword(), employee.isArchived());
    throw new NullPointerException("employee " + ID + " is non existent");
  }
}