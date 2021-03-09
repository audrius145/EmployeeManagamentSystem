package view;

import viewmodel.DepartmentSpinnyBarViewModel;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DepartmentSpinnyBarController
{
  private ViewHandler viewHandler;
  private DepartmentSpinnyBarViewModel viewModel;

  @FXML private Label spinnyError;
  @FXML private Label lockingID;

  public void init(ViewHandler viewHandler, DepartmentSpinnyBarViewModel viewModel)
  {
    this.viewHandler = viewHandler;
    this.viewModel = viewModel;

    spinnyError.textProperty().bind(viewModel.errorProperty());
    viewModel.errorProperty().addListener(Observable -> Platform.runLater(() -> {
      if (viewModel.errorProperty().get().equals("acquired"))
        viewHandler.switchView("EditDepartment");
      if (viewModel.errorProperty().get().equals("removed"))
        viewHandler.switchView("EditDepartmentSelect");
    }));
    lockingID.setText(viewModel.getLockingID());
  }

  public void cancel()
  {
    viewModel.cancel();
    viewHandler.switchView("EditDepartmentSelect");
  }
}
