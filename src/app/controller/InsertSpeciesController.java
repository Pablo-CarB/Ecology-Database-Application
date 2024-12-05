package app.controller;

import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javafx.scene.Node;
import javafx.stage.Stage;
import app.ConservationStatus;
import app.Utils;
import app.model.ModelImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Pair;
import javafx.util.converter.IntegerStringConverter;

public class InsertSpeciesController {

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

  private TreeItem<String> genusNode;
  private ModelImpl model = ModelImpl.getInstance();

  public void setGenus(TreeItem<String> genusNode){
    this.genusNode = genusNode;
  }

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

    conservationStatus.getItems().addAll(ConservationStatus.getConservationStatusDescriptions());

    try {
      feedingStrategy.getItems().addAll(model.queryTable("FeedingStrategy"));

      dietaryPattern.getItems().addAll(model.queryTable("DietaryPattern"));
    }
    catch (Exception e){
      Utils.showErrorMessage("ERROR",e.getMessage());
    }

  }

  @FXML
  public void handleInsertButton(ActionEvent event){
    String[] words = this.genusNode.getValue().split("\\s+");

    String yearText = this.yearDescribed.getText();
    try {
      int yearNum = Integer.parseInt(yearText);

      List<Pair<Object, Integer>> pairs = new ArrayList<>();

          pairs.add(new Pair<>(words[0], Types.VARCHAR));
          pairs.add(new Pair<>(this.specificName.getText(), Types.VARCHAR));
          pairs.add(new Pair<>(this.conservationStatus.getValue(), Types.VARCHAR));
          pairs.add(new Pair<>(this.yearDescribed.getText(), Types.INTEGER));
          pairs.add(new Pair<>(this.commonName.getText(), Types.VARCHAR));
          pairs.add(new Pair<>(this.gregarious.isSelected(), Types.BOOLEAN));
          pairs.add(new Pair<>(this.dietaryPattern.getValue(), Types.VARCHAR));
          pairs.add(new Pair<>(this.feedingStrategy.getValue(), Types.VARCHAR));

      model.insertRow("Species", pairs);

      Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
      stage.close();

    } catch (NumberFormatException e) {
      Utils.showErrorMessage("ERROR","They year field must only contain numbers");
    }
    catch (SQLException e){
      Utils.showErrorMessage("ERROR",e.getMessage());
    }

  }
}


