package view;

import model.GroupChatStruct;
import model.ModelManager;
import viewmodel.GroupChatViewModel;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;

public class GroupChatController
{
    private ViewHandler viewHandler;
    private GroupChatViewModel viewModel;

    @FXML private Label chatName;
    @FXML private TableView<GroupChatStruct> chatTV;
    @FXML private TableColumn<GroupChatStruct, String> idCol;
    @FXML private TableColumn<GroupChatStruct, String> nameCol;
    @FXML private ListView<String> chatLV;
    @FXML private TextField chatTF;
    @FXML private Button edit;
    @FXML private Button leave;

    public void init(ViewHandler viewHandler, GroupChatViewModel viewModel)
    {
        this.viewHandler = viewHandler;
        this.viewModel = viewModel;
        viewModel.reset();

        idCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
        chatTV.setItems(viewModel.getChats());
        chatTV.getSortOrder().addAll(idCol);
        edit.setDisable(true);
        leave.setDisable(true);
        chatTV.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            viewModel.setCurrent(newValue);
            if (newValue != null)
            {
              edit.setDisable(false);
              leave.setDisable(false);
              chatLV.setItems(viewModel.getMessages());
            }
          });
        chatName.textProperty().bind(viewModel.chatNameProperty());

        chatTF.setOnKeyPressed((evt) -> {
            if(evt.getCode() == KeyCode.ENTER)
            send();
          });

        ModelManager.get().addListener("alert", evt -> {
            if (evt.getNewValue().equals("refresh"))
                chatTV.refresh();
        });
    }

    public void send()
    {
        viewModel.send(chatTF.getText());
        chatTF.setText("");
    }

    public void back()
    {
        viewHandler.switchView("MainMenu");
    }

    public void newChat()
    {
        viewModel.newGroupChat();
    }

    public void edit()
    {
        viewHandler.switchView("EditGroupChat");
    }

    public void leave()
    {
        viewModel.leave();
    }
}