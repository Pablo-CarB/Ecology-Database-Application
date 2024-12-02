/*package app.controller;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;

import app.controller.command.ControllerCommand;
import app.controller.command.InsertDomainCommand;
import app.controller.command.InsertGroupCommand;
import app.model.Model;
import app.view.View;

/**
 * {@code ControllerImpl} represents a command line interface implementation of the ecology
 * database application
 */
/*
public class ControllerImpl implements Controller{

  Model model;
  View view;
  Readable in;

  Scanner sc;

  protected Map<String, Function<Scanner, ControllerCommand>> commandMap;

  private InsertGroupCommand createInsertCommand(Scanner sc, String label, String... questions) {
    ArrayList<String> inputs = new ArrayList<>();
    inputs.add(label);
    inputs.addAll(queryUserStrings(sc, questions));
    return new InsertGroupCommand(inputs);
  }
  public ControllerImpl(Model model, Readable in, View view){
    this.model = model;
    this.view = view;
    this.in = in;
    this.sc = new Scanner(this.in);

    this.commandMap = new HashMap<>();
    commandMap.put("insert-domain", s -> new InsertDomainCommand(queryUserStrings(s,
            "domain name?")));
    commandMap.put("insert-kingdom", s -> createInsertCommand(s,"kingdom", "kingdom name?", "domain name?"));
    commandMap.put("insert-phylum", s -> createInsertCommand(s,"phylum", "phylum name?", "kingdom name?"));
    commandMap.put("insert-class", s -> createInsertCommand(s,"class", "class name?", "phylum name?"));
    commandMap.put("insert-order", s -> createInsertCommand(s,"order", "order name?", "class name?"));
    commandMap.put("insert-family", s -> createInsertCommand(s,"family", "family name?", "order name?"));
    commandMap.put("insert-genus", s -> createInsertCommand(s,"genus", "genus name?", "family name?"));
    commandMap.put("insert-species", s -> createInsertCommand(s,"genus", "genus name?", "family name?"));

    commandMap.put("fetch-kingdom", s -> createInsertCommand(s,"kingdom", "kingdom name?", "domain name?"));
    commandMap.put("fetch-phylum", s -> createInsertCommand(s,"phylum", "phylum name?", "kingdom name?"));
    commandMap.put("fetch-class", s -> createInsertCommand(s,"class", "class name?", "phylum name?"));
    commandMap.put("fetch-order", s -> createInsertCommand(s,"order", "order name?", "class name?"));
    commandMap.put("fetch-family", s -> createInsertCommand(s,"family", "family name?", "order name?"));
    commandMap.put("fetch-genus", s -> createInsertCommand(s,"genus", "genus name?", "family name?"));
    commandMap.put("fetch-species", s -> createInsertCommand(s,"genus", "genus name?", "family name?"));

  }

  protected List<String> queryUserStrings(Scanner sc, String... vals) {
    ArrayList<String> outputs = new ArrayList<>();
    for(String s : vals) {
      view.writeMessage(s);
      outputs.add(sc.next());
    }
    return outputs;
  }

  @Override
  public void execute() {

    boolean quit = false;
    boolean connectionEstablished = false;

    while(!connectionEstablished){
      this.view.writeMessage("MySQL Username?");
      String username = sc.nextLine();
      this.view.writeMessage("MySQL Password?");
      String password = sc.nextLine();
      try{
        this.model.establishConnection(username,password);
        this.view.writeMessage("Success! Connection established");
        connectionEstablished = true;
      }
      catch (SQLException e){
        this.view.writeMessage("ERROR: Could not connect"
                + " to the database, please retry credentials and check connection");
      }
    }

    while(!quit) {
      String input = sc.next();

      if(input.equals("q") || input.equals("quit")){
        quit = true;
      }
      if(!this.commandMap.containsKey(input)){
        this.view.writeMessage("unrecognized command \"" + input + "\","
                + " enter menu for a list of commands, " +
                "or welcome for the initial welcome message");
        this.view.writeMessage("Try another command : ");
      }
      else{
        ControllerCommand command = this.commandMap.get(input).apply(sc);
        try{
          command.apply(this.model,this.view);
        }
        catch (Exception e){
          view.writeMessage(e.getMessage());
          view.writeMessage("try another command!");
        }
      }
    }
  }

  public String welcomeMessage(){
    String message = "";
    return message;
  }
}
*/