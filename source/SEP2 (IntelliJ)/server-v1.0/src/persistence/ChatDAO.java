package persistence;

import model.business.Chat;

import java.sql.*;
import java.util.ArrayList;

/**responsible for data manipulation in messaginggroup and empconnection tables*/
class ChatDAO
{
  /**postgre server url*/
  private String url;
  /**postgre server user*/
  private String user;
  /**postgre server password*/
  private String password;

  /**takes postgre server credentials and stores them in the instance variables*/
  ChatDAO(String url, String user, String password)
  {
    this.url = url;
    this.user = user;
    this.password = password;
  }

  /**opens a new connection to the db, inserts a new record in the messaginggroup table and a some records in the empconnection table depending on the number of members of the chat, then the db generates some fields of the new chat e.g. ID which is used to return a new Chat object, and closes the connection to the db*/
  Chat create(String name, boolean isGroup, int[] members)
  {
    try (Connection connection = DriverManager.getConnection(url,user,password))
    {
      PreparedStatement statement = connection.prepareStatement("INSERT INTO messaginggroup(mg_name,mg_isgroup) VALUES (?,?)",PreparedStatement.RETURN_GENERATED_KEYS);
      statement.setString(1, name);
      statement.setBoolean(2, isGroup);
      statement.executeUpdate();
      ResultSet result = statement.getGeneratedKeys();
      if (result.next())
      {
        PreparedStatement statement1 = connection.prepareStatement("INSERT INTO empconnection(c_eid,c_gid) VALUES (?,?)");
        for (int member : members)
        {
          if (member != 0)
          {
            statement1.setInt(1, member);
            statement1.setInt(2, result.getInt("mg_id"));
            statement1.execute();
          }
        }
      }
      return new Chat(result.getInt("mg_id"),name,isGroup,members);
    }
    catch (SQLException e)
    {
      throw new RuntimeException("error creating chat", e);
    }
  }

  /**opens a new connection to the db, queries the messaginggroup and empconnection tables (joins them) and returns all records (combines some of them in a logical manner) in an ArrayList of Chat objects, and closes the connection to the db*/
  ArrayList<Chat> readAll()
  {
    try (Connection connection = DriverManager.getConnection(url,user,password))
    {
      PreparedStatement statement = connection.prepareStatement(
          "SELECT messaginggroup.mg_id, messaginggroup.mg_name,messaginggroup.mg_isgroup, empconnection.c_eid FROM messaginggroup JOIN empconnection ON Messaginggroup.mg_id = empconnection.c_gid;");
      ResultSet result = statement.executeQuery();
      ArrayList<Chat> temp = new ArrayList<>();
      while (result.next())
      {
        boolean contains = false;
        for (Chat chat : temp)
        {
          if (chat.getID() == result.getInt("mg_id"))
        {
          chat.addMember(result.getInt("c_eid"));
          contains = true;
          break;
        }
      }
      if (!contains)
        {
          temp.add(new Chat(result.getInt("mg_id"), result.getString("mg_name"), result.getBoolean("mg_isgroup"), new int[] {result.getInt("c_eid")}));
        }
      }
      return temp;
    }
    catch (SQLException e)
    {
      throw new RuntimeException("error reading chats", e);
    }
  }

  /**opens a new connection to the db, updates a record in the messaginggroup table and removes/adds certain records from/to empconnection table, according to the inputted Chat in the argument, then reads the issued records and returns it as a new Chat object, and closes the connection to the db*/
  Chat update(Chat chat)
  {
    try (Connection connection = DriverManager.getConnection(url,user,password))
    {
      PreparedStatement statement = connection.prepareStatement("UPDATE messaginggroup SET mg_name = ?, mg_isgroup = ? WHERE mg_id = ?");
      statement.setString(1, chat.getName());
      statement.setBoolean(2, chat.isGroup());
      statement.setInt(3,chat.getID());
      statement.executeUpdate();

      PreparedStatement statement1 = connection.prepareStatement("Select c_eid FROM empconnection where c_gid = ?");
      statement1.setInt(1,chat.getID());
      ResultSet result = statement1.executeQuery();

      int[] members = chat.getMembers();
      while(result.next())
      {
        boolean contains = false;
        for (int value : members)
        {
          if (result.getInt("c_eid") == value)
          {
            contains = true;
            break;
          }
        }
        if (!contains)
        {
          PreparedStatement statement2 = connection.prepareStatement("DELETE FROM empconnection WHERE c_eid=? AND c_gid = ?");
          statement2.setInt(1, result.getInt("c_eid"));
          statement2.setInt(2, chat.getID());
          statement2.executeUpdate();
        }
      }

      for (int value : members)
      {
        boolean contains = false;
        result = statement1.executeQuery();
        while (result.next())
        {
          if (result.getInt("c_eid") == value)
          {
            contains = true;
            break;
          }
        }
        if (!contains)
        {
          PreparedStatement statement3 = connection.prepareStatement("INSERT INTO empconnection(c_eid, c_gid) VALUES(?,?)");
          statement3.setInt(1, value);
          statement3.setInt(2, chat.getID());
          statement3.executeUpdate();
        }
      }

      return readByID(chat.getID());
    }
    catch (SQLException | RuntimeException e)
    {
      throw new RuntimeException("error updating chat " + chat.getID(), e);
    }
  }

  /**opens a new connection to the db, queries the messaginggroup and empconnection tables (joins them) according to the ID in the argument and returns the issued Chat object, and closes the connection to the db*/
  private Chat readByID(int ID)
  {
    try (Connection connection = DriverManager.getConnection(url,user,password))
    {
      Chat chat;
      PreparedStatement statement = connection.prepareStatement("SELECT * FROM messaginggroup WHERE mg_id = ?");
      statement.setInt(1, ID);
      ResultSet result = statement.executeQuery();
      if (result.next())
      {
        chat = new Chat(result.getInt("mg_id"), result.getString("mg_name"), result.getBoolean("mg_isgroup"), new int[0]);
      }
      else
      {
        throw new RuntimeException("chat " + ID + " is non existent");
      }
      PreparedStatement statement1 = connection.prepareStatement("SELECT c_eid FROM empconnection WHERE c_gid = ?");
      statement1.setInt(1, ID);
      result = statement1.executeQuery();
      while (result.next())
      {
        chat.addMember(result.getInt("c_eid"));
      }
      return chat;
    }
    catch (SQLException e)
    {
      throw new RuntimeException("error reading chat " + ID, e);
    }
  }

  /**opens a new connection to the db, deletes a chat from the messaginggroup table using the ID in the argument as a criteria, and closes the connection to the db*/
  void deleteByID(int ID)
  {
    try(Connection connection = DriverManager.getConnection(url,user,password))
    {
      PreparedStatement statement = connection.prepareStatement("DELETE FROM messaginggroup WHERE mg_id=?");
      statement.setInt(1, ID);
      statement.executeUpdate();
    }
    catch (SQLException e)
    {
      throw new RuntimeException("error deleting chat " + ID, e);
    }
  }
}

