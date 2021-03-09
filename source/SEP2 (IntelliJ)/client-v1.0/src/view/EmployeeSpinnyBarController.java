package view;

import viewmodel.EmployeeSpinnyBarViewModel;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class EmployeeSpinnyBarController
{
  private ViewHandler viewHandler;
  private EmployeeSpinnyBarViewModel viewModel;

  @FXML private Label spinnyError;
  @FXML private Label lockingID;

  public void init(ViewHandler viewHandler, EmployeeSpinnyBarViewModel viewModel)
  {
    this.viewHandler = viewHandler;
    this.viewModel = viewModel;

    spinnyError.textProperty().bind(viewModel.errorProperty());
    viewModel.errorProperty().addListener(Observable -> Platform.runLater(() -> {
      if (viewModel.errorProperty().get().equals("acquired"))
        viewHandler.switchView("EditEmployee");
      if (viewModel.errorProperty().get().equals("archived"))
        viewHandler.switchView("EditEmployeeSelect");
    }));
    lockingID.setText(viewModel.getLockingID());
  }

  public void cancel()
  {
    viewModel.cancel();
    viewHandler.switchView("EditEmployeeSelect");
  }
}
