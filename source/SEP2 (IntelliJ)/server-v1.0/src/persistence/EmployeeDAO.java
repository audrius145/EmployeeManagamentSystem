package persistence;

import model.business.Employee;

import java.sql.*;
import java.util.ArrayList;

/**responsible for data manipulation in employee table*/
class EmployeeDAO
{
    /**postgre server url*/
    private String url;
    /**postgre server user*/
    private String user;
    /**postgre server password*/
    private String password;

    /**takes postgre server credentials and stores them in the instance variables*/
    EmployeeDAO(String url, String user, String password)
    {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    /**opens a new connection to the db, inserts a new record in the employee table, then the db generates some fields of the new employee e.g. ID which is used to return a new Employee object, and closes the connection to the db*/
    Employee create()
    {
        try (Connection connection = DriverManager.getConnection(url,user,password))
        {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO employee(e_name, e_department, e_admin, e_password, e_archived) VALUES (?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, "New Employee");
            statement.setNull(2, 0);
            statement.setBoolean(3, false);
            statement.setString(4, "1234");
            statement.setBoolean(5, false);
            statement.executeUpdate();
            ResultSet result = statement.getGeneratedKeys();
            if (result.next())
            {
                return new Employee(
                    result.getInt("e_id"),
                    result.getString("e_name"),
                    result.getInt("e_department"),
                    result.getBoolean("e_admin"),
                    result.getString("e_password"),
                    result.getBoolean("e_archived"));
            }
            else
            {
                throw new SQLException();
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("error creating employee", e);
        }
    }

    /**opens a new connection to the db, queries the employee table and returns all records in an ArrayList of Employee objects, and closes the connection to the db*/
    ArrayList<Employee> readAll()
    {
        try (Connection connection = DriverManager.getConnection(url,user,password))
        {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM employee");
            ResultSet result = statement.executeQuery();
            ArrayList<Employee> temp = new ArrayList<>();
            while(result.next())
            {
                temp.add(new Employee(
                    result.getInt("e_id"),
                    result.getString("e_name"),
                    result.getInt("e_department"),
                    result.getBoolean("e_admin"),
                    result.getString("e_password"),
                    result.getBoolean("e_archived")));
            }
            return temp;
        }
        catch (SQLException e)
        {
            throw new RuntimeException("error reading employees", e);
        }
    }

    /**opens a new connection to the db, queries the employee table using the ID in the argument as a criteria and returns the issued employee as an Employee object, and closes the connection to the db*/
    private Employee readByID(int ID)
    {
        try (Connection connection = DriverManager.getConnection(url,user,password))
        {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM employee WHERE e_id = ?");
            statement.setInt(1, ID);
            ResultSet result = statement.executeQuery();
            if(result.next())
            {
                return new Employee(
                    result.getInt("e_id"),
                    result.getString("e_name"),
                    result.getInt("e_department"),
                    result.getBoolean("e_admin"),
                    result.getString("e_password"),
                    result.getBoolean("e_archived"));
            }
            else
            {
                throw new RuntimeException("employee " + ID + " is non existent");
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("error reading employee " + ID, e);
        }
    }

    /**opens a new connection to the db, updates a record in the employee table according to the inputted Employee in the argument, then reads the issued record and returns it as a new Employee object, and closes the connection to the db*/
    Employee update(Employee employee)
    {
        try (Connection connection = DriverManager.getConnection(url,user,password))
        {
            PreparedStatement statement = connection.prepareStatement("UPDATE employee SET e_name = ?, e_department = ?, e_admin = ?, e_password = ?, e_archived = ? WHERE e_id = ?");
            statement.setString(1, employee.getName());
            if (employee.getDepartment() == 0)
                statement.setNull(2, 0);
            else
                statement.setInt(2, employee.getDepartment());
            statement.setBoolean(3, employee.isAdmin());
            statement.setString(4, employee.getPassword());
            statement.setBoolean(5, employee.isArchived());
            statement.setInt(6, employee.getID());
            statement.executeUpdate();
            return readByID(employee.getID());
        }
        catch (SQLException | RuntimeException e)
        {
            throw new RuntimeException("error reading employee " + employee.getID(), e);
        }
    }
}