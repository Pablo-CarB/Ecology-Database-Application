package app.controller;

import java.nio.file.Paths;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import app.ControllerUtils;
import app.TreeRank;
import javafx.scene.Node;
import javafx.stage.Stage;
import app.ConservationStatus;
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


/**
 * {@code InsertSpeciesController} is the controller class
 * that handles the window responsible for species insertion
 */
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

  private TreeItem<Pair<String,TreeRank>> genusNode;
  private final ModelImpl model = ModelImpl.getInstance();

  /**
   * passes to the {@code InsertSpeciesController} the parent node of the species
   * that is going to be inserted
   * @param genusNode the parent/genus node
   */
  public void setGenus(TreeItem<Pair<String, TreeRank>> genusNode){
    this.genusNode = genusNode;
  }

  /**
   * method that initializes the insert species window and is run as soon as the fxml is loaded
   */
  @FXML
  public void initialize() {

    // text formatter only allows the year described to be integer
    TextFormatter<Integer> formatter = new TextFormatter<>(new IntegerStringConverter(), 0,
            c -> {
              return c.getControlNewText().matches("\\d*") ? c : null;
            });
    yearDescribed.setTextFormatter(formatter);

    Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(
            Paths.get("app", "images", "homeIcon.png").toString()
    )));
    ImageView imageView = new ImageView(image);

    imageView.setFitWidth(20);
    imageView.setFitHeight(20);

    conservationStatus.getItems().addAll(ConservationStatus.getConservationStatusDescriptions());

    try {
      feedingStrategy.getItems().addAll(model.queryTable("FeedingStrategy"));

      dietaryPattern.getItems().addAll(model.queryTable("DietaryPattern"));
    }
    catch (NullPointerException n){
      ControllerUtils.showErrorMessage("internal file paths are incorrectly configured, attempt re-download app");
    }
    catch (Exception e){
      ControllerUtils.showErrorMessage(e.getMessage());
    }

  }

  /**
   * handles the logic for pressing the insert button
   * @param event the
   */
  @FXML
  public void handleInsertButton(ActionEvent event){
    String genusName = this.genusNode.getValue().getKey();
    String yearText = this.yearDescribed.getText();

    try {
      int yearNum = Integer.parseInt(yearText);

      List<Pair<Object, Integer>> pairs = new ArrayList<>();

          pairs.add(new Pair<>(genusName, Types.VARCHAR));
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
      ControllerUtils.showErrorMessage("They year field must only contain numbers");
    }
    catch (SQLException e){
      ControllerUtils.showErrorMessage(e.getMessage());
    }

  }
}


