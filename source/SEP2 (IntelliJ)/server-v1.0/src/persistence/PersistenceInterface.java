package persistence;

import model.business.Chat;
import model.business.Department;
import model.business.Employee;
import model.business.Message;

import java.util.ArrayList;

/**responsible for keeping the Dependency Inversion principle*/
public interface PersistenceInterface
{
  /**returns all stored employees*/
  ArrayList<Employee> getAllEmployees();
  /**returns a newly created employee with default values*/
  Employee newEmployee();
  /**updates an employee's attributes and returns an employee which matches the updated data, read from the persistence</br>
   * (this is used to ensure that the business layer has the same data which is in the persistence)*/
  Employee updateEmployee(Employee employee);

  /**returns all stored departments*/
  ArrayList<Department> getAllDepartments();
  /**returns a newly created department with default values*/
  Department newDepartment();
  /**updates a department's attributes and returns a department which matches the updated data, read from the persistence</br>
   * (this is used to ensure that the business layer has the same data which is in the persistence)*/
  Department updateDepartment(Department department);
  /**deletes a department*/
  void removeDepartment(int departmentID);

  /**returns all stored chats*/
  ArrayList<Chat> getAllChats();
  /**returns a newly created chat read from persistence with the values inputted in the constructor*/
  Chat newChat(String name, boolean isGroup, int[] members);
  /**updates a chat's attributes and returns a chat which matches the updated data, read from the persistence</br>
   * (this is used to ensure that the business layer has the same data which is in the persistence)*/
  Chat updateChat(Chat chat);
  /**deletes a chat*/
  void removeChat(int chatID);

  /**returns all stored messages*/
  ArrayList<Message> getAllMessages();
  /**returns a newly created message read from persistence with the values inputted in the constructor*/
  Message newMessage(String message, int sender, int chatID);
}
