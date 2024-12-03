package app.controller;

import app.model.ModelImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.converter.IntegerStringConverter;

public class InsertController {

  @FXML
  private Button insertSpeciesButton;
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


    Image image = new Image(getClass().getResourceAsStream("/app/images/homeIcon.png"));
    ImageView imageView = new ImageView(image);

    imageView.setFitWidth(20);
    imageView.setFitHeight(20);

    // setting the conservation status choicebox
    conservationStatus.getItems().addAll("laaa","lalal");

    // setting the feeding strategy choicebox
    feedingStrategy.getItems().addAll("1","2");

    // setting the dietary pattern choicebox
    dietaryPattern.getItems().addAll("0","14");
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
}


