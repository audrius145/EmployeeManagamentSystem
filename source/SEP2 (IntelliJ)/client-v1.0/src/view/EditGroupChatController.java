package view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.EmployeeStruct;
import model.ModelManager;
import viewmodel.EditGroupChatViewModel;

public class EditGroupChatController
{
  private ViewHandler viewHandler;
  private EditGroupChatViewModel viewModel;

  @FXML private Label id;
  @FXML private TextField nameTF;
  @FXML private TableView<EmployeeStruct> membersTV;
  @FXML private TableColumn<EmployeeStruct,String> idColumn;
  @FXML private TableColumn<EmployeeStruct,String> nameColumn;
  @FXML private Button removeEmployee;
  @FXML private ChoiceBox<EmployeeStruct> choseEmployee;
  @FXML private Button addEmployee;

  public void init(ViewHandler viewHandler, EditGroupChatViewModel viewModel)
  {
    this.viewHandler = viewHandler;
    this.viewModel = viewModel;

    id.setText(String.valueOf(viewModel.getID()));
    choseEmployee.setItems(viewModel.getEmployees());
    idColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
    nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
    membersTV.setItems(viewModel.getMembers());
    membersTV.getSortOrder().addAll(idColumn);
    membersTV.sort();
    addEmployee.setDisable(true);
    removeEmployee.setDisable(true);
    membersTV.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue != null)
      {
        removeEmployee.setDisable(false);
        if (newValue.getID() == ModelManager.get().getEmployeeID())
          removeEmployee.setDisable(true);
      }
    });
    choseEmployee.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
      if (newValue != null)
      {
        addEmployee.setDisable(false);
        if (newValue.getID() == ModelManager.get().getEmployeeID())
          addEmployee.setDisable(true);
      }
    });
    nameTF.textProperty().bindBidirectional(viewModel.nameProperty());

    ModelManager.get().addListener("alert", evt -> {
      if (evt.getNewValue().equals("refresh"))
        membersTV.refresh();
    });
  }

  public void changeName()
  {
    viewModel.changeName(nameTF.getText());
  }

  public void removeEmployee()
  {
    viewModel.removeEmployee(membersTV.getSelectionModel().getSelectedItem().getID());
  }

  public void addEmployee()
  {
    viewModel.addEmployee(choseEmployee.getSelectionModel().getSelectedItem().getID());
  }

  public void back()
  {
    viewHandler.switchView("GroupChat");
  }
}