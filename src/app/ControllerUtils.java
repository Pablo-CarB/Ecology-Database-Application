package app;

import java.io.IOException;
import java.util.HashMap;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * {@code ControllerUtils} is a utility class for controller classes
 */
public final class ControllerUtils {

  // private constructor to avoid installation
  private ControllerUtils(){

  }

  /**
   * a HashMap<String,String> that allows the application to descend down the taxonomic hierarchy
   */
  public static final HashMap<String, String> descendHierarchy = new HashMap<>() {{
    put("Database", "Domain");
    put("Domain", "Kingdom");
    put("Kingdom", "Phylum");
    put("Phylum", "Class");
    put("Class", "Order");
    put("Order", "Family");
    put("Family", "Genus");
    put("Genus", "Species");
  }};

  /**
   * a helper method that helps in switching JavaFX fxml scenes given the path to the new
   * {@code Stage}, String path to the FXML file, and title of the window
   *
   * @param newStage the new Stage object
   * @param FXMLFileName the path to the new fxml file
   * @param windowTitle the title of the new window
   * @throws IOException if the path doesn't exist/is invalid
   */
  public static void switchScene(Stage newStage, String FXMLFileName, String windowTitle) throws IOException {
    FXMLLoader loader = new FXMLLoader(ControllerUtils.class.getResource("controller/"+FXMLFileName));
    Parent newRoot = loader.load();
    newStage.setTitle(windowTitle);
    Scene newScene = new Scene(newRoot);
    newStage.setScene(newScene);
    newStage.show();
  }

  /**
   * opens an error window to show the user
   *
   * @param message the error message
   */
  public static void showErrorMessage(String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Error");
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }
}
