package view;

import model.DepartmentStruct;
import viewmodel.EditEmployeeViewModel;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class EditEmployeeController
{
  private ViewHandler viewHandler;
  private EditEmployeeViewModel viewModel;

  @FXML private ChoiceBox<DepartmentStruct> department;
  @FXML private CheckBox admin;
  @FXML private TextField name;
  @FXML private TextField pw;
  @FXML private Label id;

  public void init(ViewHandler viewHandler, EditEmployeeViewModel viewModel)
  {
    this.viewHandler = viewHandler;
    this.viewModel = viewModel;

    name.setText(viewModel.getName());
    admin.setSelected(viewModel.isAdmin());
    id.setText(String.valueOf(viewModel.getID()));
    department.setItems(viewModel.getDepartments());
    department.getSelectionModel().select(viewModel.getDepartment());
  }

  public void cancelEdit()
  {
    viewModel.cancel();
    viewHandler.switchView("EditEmployeeSelect");
  }

  public void saveEdit()
  {
    viewModel.SaveEmployee(name.getText(), department.getValue().getID(), admin.isSelected(), pw.getText());
    viewHandler.switchView("EditEmployeeSelect");
  }
}