package app;

import java.nio.file.Paths;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.shape.Path;
import javafx.stage.Stage;

/**
 * Main class for the GUI application
 */
public class Main extends Application {
  public static void main(String...args){
    launch();
  }

  @Override
  public void start(Stage stage) throws Exception {
    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(
            Paths.get("controller", "Login.fxml").toString()));
    Scene scene = new Scene(fxmlLoader.load());
    stage.setTitle("Login Window");
    stage.setScene(scene);
    stage.show();
  }
}
