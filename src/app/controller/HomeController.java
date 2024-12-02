package app.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class HomeController {
  @FXML
  private Button insertButton;
  @FXML
  private Button fetchButton;
  @FXML
  private Button deleteButton;
  @FXML
  private Button updateButton;

  @FXML
  public void handleButtons(ActionEvent event) {
    Button clickedButton = (Button) event.getSource();
    String destination = null;
    switch (clickedButton.getId()){
      case "insertButton":
        destination = "Insert2.fxml";
        break;
      case "fetchButton":
        destination = "Read.fxml";
        break;
      case "deleteButton":
        destination = "Delete.fxml";
        break;
      case "updateButton":
        destination = "Update.fxml";
        break;
      default:
        break;
    }
    Stage currentStage = (Stage) insertButton.getScene().getWindow();
    try {
      Utils.switchScene(currentStage,destination,destination.substring(0,destination.length()-5)+ " Menu");
    }
    catch (Exception e){
      Utils.showErrorMessage("ERROR",e.getMessage());
    }
  }
}
