package viewmodel;

import model.GroupChatStruct;
import model.ModelManager;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public class GroupChatViewModel
{
    private StringProperty chatName;

    GroupChatViewModel()
    {
        chatName = new SimpleStringProperty("");
        ModelManager.get().addListener("alert", evt -> Platform.runLater(() -> {
            if (evt.getNewValue().equals("refresh"))
                if (DontDestroyOnLoad.get().currentChat != null)
                    chatName.set(DontDestroyOnLoad.get().currentChat.getName());
        }));
    }

    public void reset()
    {
        chatName.set("");
        DontDestroyOnLoad.get().currentChat = null;
    }

    public ObservableList<GroupChatStruct> getChats()
    {
        return ModelManager.get().getDataHandler().getGroupChats();
    }

    public void send(String message)
    {
        if(DontDestroyOnLoad.get().currentChat != null)
            ModelManager.get().sendMessage(DontDestroyOnLoad.get().currentChat.getID(), message);
    }

    public void setCurrent(GroupChatStruct currentChat)
    {
        DontDestroyOnLoad.get().currentChat = currentChat;
        if (currentChat != null)
            chatName.set(currentChat.getName());
    }

    public void newGroupChat()
    {
        ModelManager.get().newGroupChat();
    }

    public void leave()
    {
       if(DontDestroyOnLoad.get().currentChat != null)
           ModelManager.get().removeFromChat(DontDestroyOnLoad.get().currentChat.getID(), ModelManager.get().getEmployeeID());
    }

    public StringProperty chatNameProperty()
    {
        return chatName;
    }

    public ObservableList getMessages()
    {
        return DontDestroyOnLoad.get().currentChat.getHistory();
    }
   
}