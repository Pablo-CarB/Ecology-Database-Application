package app;

import app.controller.Controller;
import app.controller.ControllerImpl;
import app.model.Model;
import app.model.ModelImpl;
import app.view.CLIView;
import app.view.View;

public class Main {
  public static void main(String...args){

    Model model = new ModelImpl();
    View view = new CLIView();

    Controller controller = new ControllerImpl(model,view);
    controller.execute();
  }
}
