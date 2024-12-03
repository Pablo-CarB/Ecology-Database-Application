package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
  public static void main(String...args){
    launch();
  }

  @Override
  public void start(Stage stage) throws Exception {
    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("controller/Login.fxml"));
    Scene scene = new Scene(fxmlLoader.load());
    stage.setTitle("Login Window");
    stage.setScene(scene);
    stage.show();
  }
}
