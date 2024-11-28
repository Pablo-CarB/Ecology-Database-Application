package app.controller;

import app.model.Model;
import app.view.View;

/**
 * {@code ControllerImpl} represents a command line interface implementation of the ecology
 * database application
 */
public class ControllerImpl implements Controller{

  Model model;
  View view;

  public ControllerImpl(Model model, View view){
    this.model = model;
    this.view = view;
  }
  @Override
  public void execute() {
    boolean quit = false;

    while(!quit) {

    }
  }
}
