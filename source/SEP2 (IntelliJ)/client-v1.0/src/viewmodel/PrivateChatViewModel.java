package viewmodel;

import model.ModelManager;
import model.PrivateChatStruct;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public class PrivateChatViewModel
{
    private StringProperty chatName;

    PrivateChatViewModel()
    {
        chatName = new SimpleStringProperty("");
        ModelManager.get().addListener("alert", evt -> Platform.runLater(() -> {
            if (evt.getNewValue().equals("refresh"))
                if (DontDestroyOnLoad.get().currentChat != null)
                    if (DontDestroyOnLoad.get().currentChat instanceof PrivateChatStruct)
                        chatName.set(((PrivateChatStruct) DontDestroyOnLoad.get().currentChat).getPartnerName());
        }));
    }

    public void reset()
    {
        chatName.set("");
        DontDestroyOnLoad.get().currentChat = null;
    }

    public ObservableList<PrivateChatStruct> getChats()
    {
        return ModelManager.get().getDataHandler().getPrivateChats();
    }

    public void send(String message)
    {
        if (DontDestroyOnLoad.get().currentChat != null)
            if (DontDestroyOnLoad.get().currentChat instanceof PrivateChatStruct)
                ModelManager.get().sendMessage(DontDestroyOnLoad.get().currentChat.getID(), message);
    }

    public void setCurrent(PrivateChatStruct currentChat)
    {
        DontDestroyOnLoad.get().currentChat = currentChat;
        if (currentChat != null)
            chatName.set(currentChat.getPartnerName());
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