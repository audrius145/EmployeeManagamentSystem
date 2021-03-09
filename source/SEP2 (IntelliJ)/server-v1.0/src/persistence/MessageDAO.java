package persistence;

import model.business.Message;

import java.sql.*;
import java.util.ArrayList;

/**responsible for data manipulation in message table*/
class MessageDAO
{
  /**postgre server url*/
  private String url;
  /**postgre server user*/
  private String user;
  /**postgre server password*/
  private String password;

  /**takes postgre server credentials and stores them in the instance variables*/
  MessageDAO(String url, String user, String password)
  {
    this.url = url;
    this.user = user;
    this.password = password;
  }

  /**opens a new connection to the db, inserts a new record in the message table, then the db generates some fields of the new message e.g. timestamp which is used to return a new Message object, and closes the connection to the db*/
  Message create(String message, int sender, int chatID)
  {
    try (Connection connection = DriverManager.getConnection(url,user,password))
    {
      PreparedStatement statement = connection.prepareStatement("INSERT INTO message(m_sender, m_group, m_content) VALUES (?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
      statement.setInt(1, sender);
      statement.setInt(2, chatID);
      statement.setString(3, message);
      statement.executeUpdate();
      ResultSet result = statement.getGeneratedKeys();
      if (result.next())
      {
        return new Message(
            result.getString("m_content"),
            result.getTimestamp("m_time").toLocalDateTime(),
            result.getInt("m_sender"),
            result.getInt("m_group"));
      }
      else
      {
        throw new SQLException();
      }
    }
    catch (SQLException e)
    {
      throw new RuntimeException("error creating message", e);
    }
  }

  /**opens a new connection to the db, queries the message table and returns all records in an ArrayList of Message objects, and closes the connection to the db*/
  ArrayList<Message> readAll()
  {
    try (Connection connection = DriverManager.getConnection(url,user,password))
    {
      PreparedStatement statement = connection.prepareStatement("SELECT * FROM message ORDER BY m_time;");
      ResultSet result = statement.executeQuery();
      ArrayList<Message> temp = new ArrayList<>();
      while (result.next())
      {
        temp.add(new Message(
            result.getString("m_content"),
            result.getTimestamp("m_time").toLocalDateTime(),
            result.getInt("m_sender"),
            result.getInt("m_group")));
      }
      return temp;
    }
    catch (SQLException e)
    {
      throw new RuntimeException("error reading messages", e);
    }
  }
}
