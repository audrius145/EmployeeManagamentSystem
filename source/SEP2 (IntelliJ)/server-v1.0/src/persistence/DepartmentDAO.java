package persistence;

import model.business.Department;

import java.sql.*;
import java.util.ArrayList;

/**responsible for data manipulation in department table*/
class DepartmentDAO
{
    /**postgre server url*/
    private String url;
    /**postgre server user*/
    private String user;
    /**postgre server password*/
    private String password;

    /**takes postgre server credentials and stores them in the instance variables*/
    DepartmentDAO(String url, String user, String password)
    {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    /**opens a new connection to the db, inserts a new record in the department table, then the db generates some fields of the new department e.g. ID which is used to return a new Department object, and closes the connection to the db*/
    Department create()
    {
        try (Connection connection = DriverManager.getConnection(url,user,password))
        {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO department(d_name) VALUES (?)", PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, "New Department");
            statement.executeUpdate();
            ResultSet result = statement.getGeneratedKeys();
            if (result.next())
            {
                return new Department(
                    result.getInt("d_id"),
                    result.getString("d_name"));
            }
            else
            {
                throw new SQLException();
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("error creating department", e);
        }
    }

    /**opens a new connection to the db, queries the department table and returns all records in an ArrayList of Department objects, and closes the connection to the db*/
    ArrayList<Department> readAll()
    {
        try (Connection connection = DriverManager.getConnection(url,user,password))
        {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM department");
            ResultSet result = statement.executeQuery();
            ArrayList<Department> temp = new ArrayList<>();
            while(result.next())
            {
                temp.add(new Department(
                    result.getInt("d_id"),
                    result.getString("d_name")));
            }
            return temp;
        }
        catch (SQLException e)
        {
            throw new RuntimeException("error reading departments", e);
        }
    }

    /**opens a new connection to the db, queries the department table using the ID in the argument as a criteria and returns the issued department as a Department object, and closes the connection to the db*/
    private Department readByID(int ID)
    {
        try (Connection connection = DriverManager.getConnection(url,user,password))
        {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM department WHERE d_id = ?");
            statement.setInt(1, ID);
            ResultSet result = statement.executeQuery();
            if(result.next())
            {
                return new Department(
                    result.getInt("d_id"),
                    result.getString("d_name"));
            }
            else
            {
                throw new RuntimeException("department " + ID + " is non existent");
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException("error reading department " + ID, e);
        }
    }

    /**opens a new connection to the db, updates a record in the department table according to the inputted Department in the argument, then reads the issued record and returns it as a new Department object, and closes the connection to the db*/
    Department update(Department department)
    {
        try (Connection connection = DriverManager.getConnection(url,user,password))
        {
            PreparedStatement statement = connection.prepareStatement("UPDATE department SET d_name = ? WHERE d_id = ?");
            statement.setString(1, department.getName());
            statement.setInt(2, department.getID());
            statement.executeUpdate();
            return readByID(department.getID());
        }
        catch (SQLException | RuntimeException e)
        {
            throw new RuntimeException("error updating department " + department.getID(), e);
        }
    }

    /**opens a new connection to the db, deletes a department from the department table using the ID in the argument as a criteria, and closes the connection to the db*/
    void deleteByID(int ID)
    {
        try (Connection connection = DriverManager.getConnection(url,user,password))
        {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM department WHERE d_id = ?");
            statement.setInt(1, ID);
            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException("error deleting department " + ID, e);
        }
    }  
}