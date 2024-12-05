package app;

import java.io.IOException;
import java.util.HashMap;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class Utils {

  public static final HashMap<String, String> descendHeirarchy = new HashMap<>() {{
    put("Database", "Domain");
    put("Domain", "Kingdom");
    put("Kingdom", "Phylum");
    put("Phylum", "Class");
    put("Class", "Order");
    put("Order", "Family");
    put("Family", "Genus");
    put("Genus", "Species");
  }};

  public static void switchScene(Stage currentStage, String fxmlFile, String title) throws IOException {
    FXMLLoader loader = new FXMLLoader(Utils.class.getResource("controller/"+fxmlFile));
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
