package app.controller;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class Utils {

  public static void switchScene(Stage currentStage, String fxmlFile, String title) throws IOException {
    FXMLLoader loader = new FXMLLoader(Utils.class.getResource(fxmlFile));
    Parent newRoot = loader.load();
    currentStage.setTitle(title);
    Scene newScene = new Scene(newRoot);
    currentStage.setScene(newScene);
    currentStage.show();
  }

  public static void showErrorMessage(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }
}
