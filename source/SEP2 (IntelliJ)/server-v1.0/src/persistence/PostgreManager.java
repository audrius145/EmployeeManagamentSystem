package persistence;

import model.business.Chat;
import model.business.Department;
import model.business.Employee;
import model.business.Message;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

/**responsible for managing DAO classes and keeping the Dependency Inversion principle*/
public class PostgreManager implements PersistenceInterface
{
  /**ChatDAO instance*/
  private ChatDAO chatDAO;
  /**DepartmentDAO instance*/
  private DepartmentDAO departmentDAO;
  /**EmployeeDAO instance*/
  private EmployeeDAO employeeDAO;
  /**MessageDAO instance*/
  private MessageDAO messageDAO;

  /**takes postgre server credentials and initializes all DAO instances*/
  public PostgreManager(String url, String user, String password)
  {
    try
    {
      DriverManager.registerDriver(new org.postgresql.Driver());
    }
    catch (SQLException e)
    {
      throw new RuntimeException("error registering driver", e);
    }
    chatDAO = new ChatDAO(url, user, password);
    departmentDAO = new DepartmentDAO(url, user, password);
    employeeDAO = new EmployeeDAO(url, user, password);
    messageDAO = new MessageDAO(url, user, password);
  }

  /**uses employeeDAO to return all stored employees*/
  public ArrayList<Employee> getAllEmployees()
  {
    return employeeDAO.readAll();
  }

  /**uses employeeDAO to return a newly created employee with default values*/
  public Employee newEmployee()
  {
    return employeeDAO.create();
  }

  /**uses employeeDAO to update an employee's attributes and returns an employee which matches the updated data, read from the database</br>
   * (this is used to ensure that the business layer has the same data which is in the database)*/
  public Employee updateEmployee(Employee employee)
  {
    return employeeDAO.update(employee);
  }

  /**uses departmentDAO to return all stored departments*/
  public ArrayList<Department> getAllDepartments()
  {
    return departmentDAO.readAll();
  }

  /**uses departmentDAO to return a newly created department with default values*/
  public Department newDepartment()
  {
    return departmentDAO.create();
  }

  /**uses departmentDAO to update a department's attributes and returns a department which matches the updated data, read from the database</br>
   * (this is used to ensure that the business layer has the same data which is in the database)*/
  public Department updateDepartment(Department department)
  {
    return departmentDAO.update(department);
  }

  /**uses departmentDAO to delete a department*/
  public void removeDepartment(int departmentID)
  {
    departmentDAO.deleteByID(departmentID);
  }

  /**uses chatDAO to return all stored chats*/
  public ArrayList<Chat> getAllChats()
  {
    return chatDAO.readAll();
  }
  /**uses chatDAO to return a newly created chat read from the database with the values inputted in the constructor*/
  public Chat newChat(String name, boolean isGroup, int[] members)
  {
    return chatDAO.create(name, isGroup, members);
  }

  /**uses chatDAO to update a chat's attributes and returns a chat which matches the updated data, read from the database</br>
   * (this is used to ensure that the business layer has the same data which is in the database)*/
  public Chat updateChat(Chat chat)
  {
    return chatDAO.update(chat);
  }

  /**uses chatDAO to delete a chat*/
  public void removeChat(int chatID)
  {
    chatDAO.deleteByID(chatID);
  }

  /**uses messageDAO to return all stored messages*/
  public ArrayList<Message> getAllMessages()
  {
    return messageDAO.readAll();
  }

  /**uses messageDAO to return a newly created message read from the database with the values inputted in the constructor*/
  public Message newMessage(String message, int sender, int chatID)
  {
    return messageDAO.create(message, sender, chatID);
  }
}
