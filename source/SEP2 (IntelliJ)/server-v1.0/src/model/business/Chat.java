package model.business;

import model.Log;

import java.util.ArrayList;

/**this class is responsible for keeping a cached version of a chat*/
public class Chat
{
    /**ID of the chat*/
    private int ID;
    /**name of the chat*/
    private String name;
    /**decides if its a private/group chat (false/true)*/
    private boolean isGroup;
    /**history of the chat*/
    private ArrayList<Message> history;
    /**members of the chat (their employeeIDs)*/
    private ArrayList<Integer> members;

    /**initializes all instance variables according to the arguments*/
    public Chat(int ID, String name, boolean isGroup, int[] members)
    {
        this.ID = ID;
        this.name = name;
        this.isGroup = isGroup;
        this.history = new ArrayList<>();
        this.members = new ArrayList<>();
        for (int member : members)
            this.members.add(member);
    }

    /**updates the name, isGroup, members fields according to the arguments*/
    public void update(String name, boolean isGroup, int[] members)
    {
        this.name = name;
        this.isGroup = isGroup;
        this.members = new ArrayList<>();
        for (int member : members)
            this.members.add(member);
    }

    /**sets the name*/
    public void setName(String name)
    {
        this.name = name;
    }

    /**used to decide if an employee is a member of this chat or not*/
    public boolean isInvolved(int employeeID)
    {
        return members.contains(employeeID);
    }

    /**adds a member*/
    public void addMember(int employeeID)
    {
        members.add(employeeID);
    }

    /**removes a member*/
    public void removeMember(int employeeID)
    {
        boolean present = false;
        for (int i = 0; i < members.size(); i++)
            if (members.get(i) == employeeID)
            {
                members.remove(i);
                present = true;
                break;
            }
        if (!present)
            Log.get().error(new NullPointerException("employee " + employeeID + " is not part of chat " + ID));
    }

    /**returns the ID*/
    public int getID()
    {
        return ID;
    }

    /**returns the name*/
    public String getName()
    {
        return name;
    }

    /**returns the isGroup*/
    public boolean isGroup()
    {
        return isGroup;
    }

    /**returns the history*/
    public ArrayList<Message> getHistory()
    {
        return history;
    }

    /**returns the members (as an array, not an ArrayList)*/
    public int[] getMembers()
    {
        int[] temp = new int[members.size()];
        for (int i = 0; i < members.size(); i++)
            temp[i] = members.get(i);
        return temp;
    }

    /**adds a message to the history*/
    void loadMessage(Message message)
    {
        history.add(message);
    }

    /**orders the messages according to their timestamps (uses bubble order)*/
    void orderMessages()
    {
        boolean swapped = true;
        while (swapped)
        {
            swapped = false;
            for(int i = 0; i < history.size() - 1; i++)
            {
                if ((history.get(i).getTime().isAfter(history.get(i + 1).getTime())))
                {
                    history.add(i, history.get(i + 1));
                    history.remove(i + 2);
                    swapped = true;
                }
            }
        }
    }

    /**overwrites name, isGroup, members attributes with the other chat's attributes in the argument*/
    void overWrite(Chat chat)
    {
        this.name = chat.getName();
        this.isGroup = chat.isGroup;
        this.members = new ArrayList<>();
        for (int member : chat.getMembers())
            this.members.add(member);
    }

    /**two Chats counts as identical if their ID is identical or they are both private chats and contains the same members*/
    public boolean equals(Object obj)
    {
        if (!(obj instanceof Chat))
            return false;
        Chat temp = (Chat) obj;
        if (temp.getID() == ID)
            return true;
        if (!temp.isGroup() && temp.getMembers().length == 2 && !isGroup && members.size() == 2)
            if ((temp.getMembers()[0] == members.get(0) && temp.getMembers()[1] == members.get(1)) ||
                (temp.getMembers()[0] == members.get(1) && temp.getMembers()[1] == members.get(0)))
                return true;
        return false;
    }
}