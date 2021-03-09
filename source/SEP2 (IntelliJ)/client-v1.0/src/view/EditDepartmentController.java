package view;

import viewmodel.EditDepartmentViewModel;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class EditDepartmentController
{
  private ViewHandler viewHandler;
  private EditDepartmentViewModel viewModel;

  @FXML private Label id;
  @FXML private TextField name;

  public void init(ViewHandler viewHandler, EditDepartmentViewModel viewModel)
  {
    this.viewHandler = viewHandler;
    this.viewModel = viewModel;

    name.setText(viewModel.getName());
    id.setText(String.valueOf(viewModel.getID()));
  }

  public void cancelEdit()
  {
    viewModel.cancel();
    viewHandler.switchView("EditDepartmentSelect");
  }

  public void saveEdit()
  {
    viewModel.SaveDepartment(name.getText());
    viewHandler.switchView("EditDepartmentSelect");
  }
}