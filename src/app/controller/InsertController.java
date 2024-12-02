package app.controller;

import java.io.IOException;

import app.model.ModelImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PopupControl;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

public class InsertController {

  @FXML
  private ChoiceBox<String> insertTypeChoice;  // ChoiceBox to select options
  @FXML
  private StackPane stackPane1;  // StackPane to hold different panes
  @FXML
  private Button homeButton;
  @FXML
  private Pane speciesPane;
  @FXML
  private Pane binomialTaxaPane;
  @FXML
  private Pane relationshipPane;

  // inserting binomialtaxa
  @FXML
  private ChoiceBox<String> binomialCategories;


  // inserting species
  @FXML
  private Button insertSpeciesButton;
  @FXML
  private TextField genusName;
  @FXML
  private TextField specificName;
  @FXML
  private ChoiceBox<String> conservationStatus;
  @FXML
  private ChoiceBox<String> feedingStrategy;
  @FXML
  private ChoiceBox<String> dietaryPattern;
  @FXML
  private CheckBox gregarious;
  @FXML
  private TextField commonName;
  @FXML
  private TextField yearDescribed;

  private ModelImpl model = ModelImpl.getInstance();


  @FXML
  public void initialize() {

    // text formatter only allows the year described to be integer
    TextFormatter<Integer> formatter = new TextFormatter<>(new IntegerStringConverter(), 0,
            c -> {
              return c.getControlNewText().matches("\\d*") ? c : null;
            });
    yearDescribed.setTextFormatter(formatter);

    // initialize insert choice types
    insertTypeChoice.getItems().addAll("Species", "Genus", "Relationship");
    insertTypeChoice.setValue("Species");

    // Listen for changes in the ChoiceBox selection
    insertTypeChoice.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
      // Change the visible pane based on the selected option
      changePane(newValue);
    });

    // Set the initial pane based on the default selection
    changePane(insertTypeChoice.getValue());

    Image image = new Image(getClass().getResourceAsStream("/app/images/homeIcon.png"));
    ImageView imageView = new ImageView(image);

    imageView.setFitWidth(20);
    imageView.setFitHeight(20);

    homeButton.setGraphic(imageView);

    // setting the conservation status choicebox
    conservationStatus.getItems().addAll("laaa","lalal");

    // setting the feeding strategy choicebox
    feedingStrategy.getItems().addAll("1","2");

    // setting the dietary pattern choicebox
    dietaryPattern.getItems().addAll("0","14");


    binomialCategories.getItems().addAll("Genus","Family","Order","Class","Phylum","Kingdom");
  }
  @FXML
  public void handleHomeButton() {
    try{
      Stage stage = (Stage) homeButton.getScene().getWindow();
      Utils.switchScene(stage,"Home.fxml","Main Menu");
    }
    catch (Exception e){
      Utils.showErrorMessage("ERROR",e.getMessage());
    }
  }

  @FXML
  public void handleInsertButton(ActionEvent event){
    Button clickedButton = (Button) event.getSource();
    switch (clickedButton.getId()){
      case "insertButton":
        break;
      case "fetchButton":
        break;
      case "deleteButton":
        break;
      case "updateButton":
        break;
      default:
        break;
    }

  }

  // Method to switch between panes based on the selection
  private void changePane(String selection) {
    // Hide all panes first
    speciesPane.setVisible(false);
    binomialTaxaPane.setVisible(false);
    relationshipPane.setVisible(false);

    switch (selection) {
      case "Species":
        speciesPane.setVisible(true);
        break;
      case "BinomialTaxa":
        binomialTaxaPane.setVisible(true);
        break;
      case "Relationship":
        relationshipPane.setVisible(true);
        break;
    }
  }
}


