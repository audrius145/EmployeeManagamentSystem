package model.business;

import java.time.LocalDateTime;

/**this class is responsible for keeping a cached version of a message*/
public class Message
{
    /**actual content of the message*/
    private String message;
    /**timestamp of the message*/
    private LocalDateTime time;
    /**id of the sender employee*/
    private int employeeID;
    /**id of the chat which it is sent to*/
    private int chatID;

    /**initializes all instance variables according to the arguments*/
    public Message(String message, LocalDateTime time, int employeeID, int chatID)
    {
        this.message = message;
        this.time = time;
        this.employeeID = employeeID;
        this.chatID = chatID;
    }

    /**returns the message (actual content of the Message)*/
    public String getMessage()
    {
        return message;
    }

    /**returns the timestamp*/
    LocalDateTime getTime()
    {
        return time;
    }

    /**returns the sender Employee's ID*/
    public int getEmployeeID()
    {
        return employeeID;
    }

    /**returns the Chat's ID*/
    public int getChatID()
    {
        return chatID;
    }
}