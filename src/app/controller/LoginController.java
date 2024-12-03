package app.controller;

import app.model.ModelImpl;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class LoginController {

  @FXML
  private TextField usernameField;

  @FXML
  private PasswordField passwordField;

  @FXML
  private TextField passwordFieldVisible;

  @FXML
  private ToggleButton hidePassword;

  private ModelImpl model = ModelImpl.getInstance();

  // Handle login button click (or Enter press)
  @FXML
  private void handleLoginAction() {
    String username = usernameField.getText();
    String password = passwordFieldVisible.getText().isEmpty() ? passwordField.getText(): passwordFieldVisible.getText();
    try{
      model.establishConnection(username,password);
      Stage currentStage = (Stage) usernameField.getScene().getWindow();
      Utils.switchScene(currentStage,"Tree.fxml","Tree View");
    }
    catch (Exception e){
      Utils.showErrorMessage("ERROR",e.getMessage());
    }

  }
  @FXML
  private void handleKeyPress(KeyEvent event) {
    if (event.getCode().toString().equals("ENTER")) {
      handleLoginAction();
    }
  }
  @FXML
  private void handleTogglePasswordVisibility() {
    if (hidePassword.isSelected()) {
      passwordField.setVisible(false);
      passwordFieldVisible.setVisible(true);
      passwordFieldVisible.setText(passwordField.getText());
      passwordField.setText("");
    } else {
      passwordField.setVisible(true);
      passwordFieldVisible.setVisible(false);
      passwordField.setText(passwordFieldVisible.getText());
      passwordFieldVisible.setText("");
    }
  }
}
