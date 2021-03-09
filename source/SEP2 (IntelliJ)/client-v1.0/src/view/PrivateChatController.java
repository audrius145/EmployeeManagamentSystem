package view;

import model.ModelManager;
import model.PrivateChatStruct;
import viewmodel.PrivateChatViewModel;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;

public class PrivateChatController
{
    private ViewHandler viewHandler;
    private PrivateChatViewModel viewModel;

    @FXML private Label chatName;
    @FXML private TableView<PrivateChatStruct> chatTV;
    @FXML private TableColumn<PrivateChatStruct, String> idCol;
    @FXML private TableColumn<PrivateChatStruct, String> nameCol;
    @FXML private ListView<String> chatLV;
    @FXML private TextField chatTF;

    public void init(ViewHandler viewHandler, PrivateChatViewModel viewModel)
    {
        this.viewHandler = viewHandler;
        this.viewModel = viewModel;
        viewModel.reset();

        idCol.setCellValueFactory(new PropertyValueFactory<>("PartnerID"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("PartnerName"));
        chatTV.setItems(viewModel.getChats());
        chatTV.getSortOrder().addAll(idCol);
        chatTV.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            viewModel.setCurrent(newValue);
            if (newValue != null)
                chatLV.setItems(viewModel.getMessages());
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
}