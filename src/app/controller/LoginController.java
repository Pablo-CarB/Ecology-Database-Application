package app.controller;

import app.ControllerUtils;
import app.model.ModelImpl;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;


/**
 * is {@code LoginController} the controller for the login window that the user sees on startup
 */
public class LoginController {

  @FXML
  private TextField usernameField;

  @FXML
  private PasswordField passwordField;

  @FXML
  private TextField passwordFieldVisible;

  @FXML
  private ToggleButton hidePassword;

  private final ModelImpl model = ModelImpl.getInstance();


  /**
   * handles the logic for pressing the login button
   */
  @FXML
  private void handleLoginAction() {
    String username = usernameField.getText();
    String password = passwordFieldVisible.getText().isEmpty() ? passwordField.getText(): passwordFieldVisible.getText();
    try{
      model.establishConnection(username,password);
      Stage currentStage = (Stage) usernameField.getScene().getWindow();
      ControllerUtils.switchScene(currentStage,"Tree.fxml","Tree View");
    }
    catch (Exception e){
      e.printStackTrace();
      ControllerUtils.showErrorMessage(e.getMessage());
    }

  }

  /**
   * handles the logic for pressing any keys on the login window
   * @param event the key that was pressed
   */
  @FXML
  private void handleKeyPress(KeyEvent event) {
    if (event.getCode().toString().equals("ENTER")) {
      handleLoginAction();
    }
  }

  /**
   * handles the logic for hiding/displaying the password text field
   */
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
