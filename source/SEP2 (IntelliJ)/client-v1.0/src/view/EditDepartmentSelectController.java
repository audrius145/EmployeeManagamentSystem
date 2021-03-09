package view;

import model.DepartmentStruct;
import model.ModelManager;
import viewmodel.EditDepartmentSelectViewModel;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class EditDepartmentSelectController
{
  private ViewHandler viewHandler;
  private EditDepartmentSelectViewModel viewModel;

  @FXML private TableView<DepartmentStruct> departmentTV;
  @FXML private TableColumn<DepartmentStruct,String> idCol;
  @FXML private TableColumn<DepartmentStruct,String> nameCol;
  @FXML private TableColumn<DepartmentStruct,String> eNumCol;
  @FXML private Label user;
  @FXML private Button edit;
  @FXML private Button remove;

  public void init(ViewHandler viewHandler, EditDepartmentSelectViewModel viewModel)
  {
    this.viewHandler = viewHandler;
    this.viewModel = viewModel;
    viewModel.reset();

    idCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
    nameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
    eNumCol.setCellValueFactory(new PropertyValueFactory<>("EmployeeNumber"));
    departmentTV.setItems(viewModel.getDepartments());
    departmentTV.getSortOrder().addAll(idCol);
    departmentTV.sort();
    edit.setDisable(true);
    remove.setDisable(true);
    departmentTV.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
      viewModel.setCurrent(newValue);
      if (newValue.getID() == 0)
      {
        edit.setDisable(true);
        remove.setDisable(true);
      }
      else
      {
        edit.setDisable(false);
        remove.setDisable(false);
      }
    });
    user.setText(viewModel.getLoggedInName());

    ModelManager.get().addListener("alert", evt -> {
      if (evt.getNewValue().equals("refresh"))
        departmentTV.refresh();
    });
  }

  public void addNew()
  {
    viewModel.addNew();
  }

  public void edit()
  {
    if (viewModel.edit())
      viewHandler.switchView("DepartmentSpinnyBar");
  }

  public void archive()
  {
    viewModel.archive();
  }

  public void back()
  {
    viewHandler.switchView("MainMenu");
  }
}
