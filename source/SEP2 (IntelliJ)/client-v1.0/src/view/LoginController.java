package view;

import viewmodel.LoginViewModel;

import javafx.scene.control.Button;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.control.TextField;

public class LoginController
{
  private ViewHandler viewHandler;
  private LoginViewModel viewModel;

  @FXML private TextField userName;
  @FXML private TextField host;
  @FXML private PasswordField password;
  @FXML private Label error;
  @FXML private Button login;

  public void init(ViewHandler viewHandler, LoginViewModel viewModel)
  {
    this.viewHandler = viewHandler;
    this.viewModel = viewModel;

    viewModel.reset();
    login.setDisable(true);
    error.textProperty().bind(viewModel.errorProperty());
    viewModel.errorProperty().addListener(Observable -> Platform.runLater(() -> {
      if (viewModel.errorProperty().get().equals("approved"))
      {
        new Thread(() -> {
          try {
            Thread.sleep(100);
            Platform.runLater(() -> viewHandler.switchView("MainMenu"));
          } catch (InterruptedException ignored) {}
        }).start();
      }
    }));

    userName.setOnKeyPressed((evt) -> {
      if(evt.getCode() == KeyCode.ENTER)
        login();
    });
    host.setOnKeyPressed((evt) -> {
      if(evt.getCode() == KeyCode.ENTER)
        login();
    });
    password.setOnKeyPressed((evt) -> {
      if(evt.getCode() == KeyCode.ENTER)
        login();
    });
  }

  public void login()
  {
    viewModel.login(userName.getText(), password.getText(), host.getText());
  }

  public void exit()
  {
    viewHandler.close();
  }

  private String userNamePrevious;
  public void onIDchange()
  {
    if (viewHandler.validityChecker(userName.getText()))
    {
      userNamePrevious = userName.getText();
    }
    else
    {
      int CursorPos = userName.getCaretPosition();
      userName.setText(userNamePrevious);
      userName.positionCaret(CursorPos - 1);
    }
    loginCheck();
  }

  private String passwordPrevious;
  public void onPWchange()
  {
    if (viewHandler.validityChecker(password.getText()))
    {
      passwordPrevious = password.getText();
    }
    else
    {
      int CursorPos = password.getCaretPosition();
      password.setText(passwordPrevious);
      password.positionCaret(CursorPos - 1);
    }
    loginCheck();
  }

  private void loginCheck()
  {
    if (userName.getText().equals("") || password.getText().equals(""))
      login.setDisable(true);
    else
      login.setDisable(false);
  }
}
