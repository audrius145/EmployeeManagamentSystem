package model.business;

import model.Log;

import java.util.ArrayList;

/**this class is responsible for managing Chats and partially their Messages*/
public class ChatManager
{
    /**all Chats*/
    private ArrayList<Chat> chats;

    /**initializes all instance variables*/
    public ChatManager()
    {
        chats = new ArrayList<>();
    }

    /**loads a new Chat instance to the chats ArrayList, if it contains an identical Chat, it overwrites some of its attributes using the .overwrite() method on the stored Chat instance*/
    public void loadChat(Chat newChat)
    {
        boolean exists = false;
        for (Chat chat : chats)
            if (chat.getID() == newChat.getID())
            {
                chat.overWrite(newChat);
                exists = true;
                break;
            }
        if (!exists)
            chats.add(newChat);
    }

    /**loads a new Message into a Chat which has matching ID with the Message's chatID*/
    public void loadMessage(Message newMessage)
    {
        boolean contains = false;
        for (Chat chat : chats)
        {
            if (chat.getID() == newMessage.getChatID())
            {
                chat.loadMessage(newMessage);
                contains = true;
                break;
            }
        }
        if (!contains)
            throw (new ArrayStoreException("chat is non existent with ID " + newMessage.getChatID()));
    }

    /**returns all ChatIDs which are not group Chats*/
    public int[] getPrivateChats(int employeeID)
    {
        int c = 0;
        for(Chat chat : chats)
            if(!chat.isGroup() && chat.isInvolved(employeeID))
                c++;
        int i = 0;
        int[] history = new int[c];
        for(Chat chat : chats)
            if(!chat.isGroup() && chat.isInvolved(employeeID))
                history[i++] = chat.getID();
        return history;  
    }

    /**returns all ChatIDs which are group Chats*/
    public int[] getGroupChats(int employeeID)
    {
        int c = 0;
        for(Chat chat : chats)
            if(chat.isGroup() && chat.isInvolved(employeeID))
                c++;
        int i = 0;
        int[] history = new int[c];
        for(Chat chat : chats)
            if(chat.isGroup() && chat.isInvolved(employeeID))
                history[i++] = chat.getID();
        return history;
    }

    /**returns all ChatIDs*/
    public int[] getChats(int employeeID)
    {
        int c = 0;
        for(Chat chat : chats)
            if(chat.isInvolved(employeeID))
                c++;
        int i = 0;
        int[] history = new int[c];
        for(Chat chat : chats)
            if(chat.isInvolved(employeeID))
                history[i++] = chat.getID();
        return history;
    }

    /**returns a Chat instance based on a matching ID criteria taken from the argument*/
    public Chat getChat(int ID)
    {
        for(Chat chat : chats)
            if(chat.getID() == ID)
                return chat;
        throw new NullPointerException("chat " + ID + " is non existent");
    }

    /**removes a Chat instance based on a matching ID criteria taken from the argument*/
    public void remove(int ID)
    {
        boolean exists = false;
        for (int i = 0; i < chats.size(); i++)
            if (chats.get(i).getID() == ID)
            {
                chats.remove(i);
                exists = true;
                break;
            }
        if (!exists)
            Log.get().error(new NullPointerException("chat " + ID + " is non existent"));
    }

    /**calls orderMessages() in all Chat instances*/
    public void orderMessages()
    {
        for(Chat chat : chats)
            chat.orderMessages();
    }

    /**returns a new Chat instance with a matching ID, name, isGroup, and members attributes, based on a matching ID criteria taken from the argument*/
    public Chat cloneChat(int ID)
    {
        for (Chat chat : chats)
            if (chat.getID() == ID)
                return new Chat(chat.getID(), chat.getName(), chat.isGroup(), chat.getMembers());
        throw new NullPointerException("chat " + ID + " is non existent");
    }
}