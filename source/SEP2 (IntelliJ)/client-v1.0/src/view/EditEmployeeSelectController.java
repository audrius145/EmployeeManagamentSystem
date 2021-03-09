package view;

import model.EmployeeStruct;
import model.ModelManager;
import viewmodel.EditEmployeeSelectViewModel;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class EditEmployeeSelectController
{
  private ViewHandler viewHandler;
  private EditEmployeeSelectViewModel viewModel;

  @FXML private TableView<EmployeeStruct> employeeTV;
  @FXML private TableColumn<EmployeeStruct,String> idCol;
  @FXML private TableColumn<EmployeeStruct,String> nameCol;
  @FXML private TableColumn<EmployeeStruct,String> departmentCol;
  @FXML private TableColumn<EmployeeStruct,String> adminCol;
  @FXML private Label user;
  @FXML private Button edit;
  @FXML private Button archive;
  

  public void init(ViewHandler viewHandler, EditEmployeeSelectViewModel viewModel)
  {
    this.viewHandler = viewHandler;
    this.viewModel = viewModel;
    viewModel.reset();

    idCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
    nameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
    departmentCol.setCellValueFactory(new PropertyValueFactory<>("DepartmentCombo"));
    adminCol.setCellValueFactory(new PropertyValueFactory<>("Admin"));
    employeeTV.setItems(viewModel.getEmployees());
    employeeTV.getSortOrder().addAll(idCol);
    employeeTV.sort();
    edit.setDisable(true);
    archive.setDisable(true);
    employeeTV.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
    viewModel.setCurrent(newValue);
    if (newValue != null)
    {
      edit.setDisable(false);
      archive.setDisable(false);
    }
  });
    user.setText(viewModel.getLoggedInName());

    ModelManager.get().addListener("alert", evt -> {
      if (evt.getNewValue().equals("refresh"))
        employeeTV.refresh();
    });
  }

  public void addNew()
  {
    viewModel.addNew();
  }

  public void edit()
  {
    if (viewModel.edit())
      viewHandler.switchView("EmployeeSpinnyBar");
  }

  public void archive()
  {
    viewModel.archive();
  }

  public void Back()
  {
    viewHandler.switchView("MainMenu");
  }
}
