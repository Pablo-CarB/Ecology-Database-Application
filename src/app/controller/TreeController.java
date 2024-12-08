package app.controller;

import java.io.IOException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import app.ControllerUtils;
import app.model.ModelImpl;
import app.model.Species;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Pair;

/**
 * {@code TreeController} is the controller responsible for handling the tree view
 */
public class TreeController {

  @FXML
  private TreeView<String> treeOfLife;

  // Table variables
  @FXML
  private TableView<Species> speciesTable;
  @FXML
  private TableColumn<Species, String> genusColumn;
  @FXML
  private TableColumn<Species, String> specificNameColumn;
  @FXML
  private TableColumn<Species, String> commonNameColumn;
  @FXML
  private TableColumn<Species, String> feedingStrategyColumn;
  @FXML
  private TableColumn<Species, String> dietaryPatternColumn;
  @FXML
  private TableColumn<Species, Integer> yearDescribedColumn;
  @FXML
  private TableColumn<Species, Boolean> gregariousColumn;
  @FXML
  private TableColumn<Species, String> conservationStatusColumn;

  // other variables
  private final ModelImpl model = ModelImpl.getInstance();

  private boolean deleteWarningShown = false;

  private TreeItem<String> rootItem;


  /**
   * method that initializes the tree view window and is run as soon as the fxml is loaded
   */
  public void initialize() {
    this.rootItem = new TreeItem<>("Database");
    treeOfLife.setRoot(rootItem);
    treeOfLife.setShowRoot(false);

    ContextMenu contextMenu = new ContextMenu();

    populateTree("Database", "",rootItem);

    // handles right click
    rightClickHandler(contextMenu);

    // Check if speciesTable is added to the layout
    if (speciesTable.getParent() == null) {
      System.out.println("TableView is not added to the layout!");
    }


    // TreeView Styling
    treeStyling();

    genusColumn.setCellValueFactory(new PropertyValueFactory<>("genusName"));
    specificNameColumn.setCellValueFactory(new PropertyValueFactory<>("specificName"));
    commonNameColumn.setCellValueFactory(new PropertyValueFactory<>("commonName"));
    feedingStrategyColumn.setCellValueFactory(new PropertyValueFactory<>("strategyName"));
    dietaryPatternColumn.setCellValueFactory(new PropertyValueFactory<>("dietName"));
    yearDescribedColumn.setCellValueFactory(new PropertyValueFactory<>("yearDescribed"));
    gregariousColumn.setCellValueFactory(new PropertyValueFactory<>("gregarious"));
    conservationStatusColumn.setCellValueFactory(new PropertyValueFactory<>("conservationStatus"));

    treeOfLife.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
      String[] words = newValue.getValue().split("\\s+");
      String taxa = words[words.length - 1].replaceAll("[()]", "");
      if (taxa.equals("Species")) {
        displaySpeciesDetails(newValue,words[0]);
      }
    });

  }

  /**
   * populates the tree with taxa from the database
   * @param parentType the taxa of the parent node (ie clade,phylum etc)
   * @param parentName the name of the parent taxa (ie homo,canis etc)
   * @param node the current node in the tree
   */
  private void populateTree(String parentType,String parentName,TreeItem<String> node) {
    List<String> subTaxa = model.querySubTaxa(parentType, parentName);
    System.out.println(parentType);
    System.out.println(Arrays.toString(subTaxa.toArray()));
    for (String subTaxon : subTaxa) {
      TreeItem<String> subTaxonItem = new TreeItem<>(subTaxon
              + " (" + ControllerUtils.descendHierarchy.get(parentType) + ")");
      node.getChildren().add(subTaxonItem);

      populateTree(ControllerUtils.descendHierarchy.get(parentType), subTaxon, subTaxonItem);
    }
  }

  /**
   * displays the species that is currently selected in the table
   * @param selectedItem the currently selected node in the tree
   * @param specificName the specific name of the currently selected node
   */
  private void displaySpeciesDetails(TreeItem<String> selectedItem, String specificName) {
    // Parse the genus from the selected TreeItem
    String[] words = selectedItem.getParent().getValue().split("\\s+");
    String genus = words[0].replaceAll("[()]", "");

    // Query the species details from the model
    Species speciesDetails = model.querySpeciesDetails(genus, specificName);

    if (speciesDetails != null) {
      // Add the species data to the table
      speciesTable.getItems().clear();  // Clear previous items
      speciesTable.getItems().add(speciesDetails);  // Add new data


    } else {
      // Show alert if no data found
      Alert alert = new Alert(Alert.AlertType.WARNING, "Species details not found!");
      alert.showAndWait();
    }
  }


  /**
   * creates the correct right click options depending on the context of the click
   * @param contextMenu the contextMenu where the options are presented to the user
   * @param selected the selected tree node from which the options that are valid are decided
   */
  private void menuSetter(ContextMenu contextMenu, TreeItem<String> selected) {
    String[] words = selected.getValue().split("\\s+");
    String taxa = words[words.length - 1].replaceAll("[()]", "");
    String subTaxa = ControllerUtils.descendHierarchy.get(taxa);

    MenuItem addSubTaxa = new MenuItem("Add " + subTaxa);
    addSubTaxa.setOnAction(e -> addSingleTaxaWindow(selected, "(" + subTaxa + ")", true));
    contextMenu.getItems().add(addSubTaxa);


  }

  /**
   * creates the window for
   * @param selectedNode the currently
   * @param rank the taxonomic rank of the newly inserted taxa
   * @param subTaxa a boolean that indicates whether the new node to be inserted
   *                will be inserted horizontally or as a sub-node
   */
  private void addSingleTaxaWindow(TreeItem<String> selectedNode, String rank, boolean subTaxa) {
    VBox addItemPane = new VBox(10);
    addItemPane.setStyle("-fx-padding: 10;");

    Label nameLabel = new Label("Enter the Name:");
    TextField nameField = new TextField();

    nameField.setTextFormatter(new TextFormatter<String>(change -> {
      String newText = change.getControlNewText();
      if (newText.matches("^[\\p{L}]*$")) {
        return change;
      } else {
        return null;
      }
    }));

    Button addButton = new Button("Add Item");

    addItemPane.getChildren().addAll(nameLabel, nameField, addButton);

    Stage addItemStage = new Stage();
    Scene addItemScene = new Scene(addItemPane, 300, 120);
    addItemStage.setTitle("Add New " + rank.replaceAll("[()]", ""));
    addItemStage.setScene(addItemScene);

    addButton.setOnAction(e -> {
      addItem(nameField, selectedNode, rank, subTaxa, addItemStage);
    });

    nameField.setOnAction(e -> {
      addItem(nameField, selectedNode, rank, subTaxa, addItemStage);
    });

    // Close the window when the TextField loses focus only if the button doesn't have focus
    nameField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
      if (!isNowFocused) {
        if (!addButton.isFocused()) {
          addItemStage.close();
        }
      }
    });
    addItemStage.show();

  }

  /**
   * handles the logic for
   * @param nameField
   * @param selectedItem
   * @param taxaType
   * @param subTaxa
   * @param addItemStage
   */
  private void addItem(TextField nameField, TreeItem<String> selectedItem, String taxaType,
                       boolean subTaxa, Stage addItemStage) {
    String newItemName = nameField.getText();
    TreeItem<String> newItem = new TreeItem<>(newItemName + " " + taxaType);
    try{
      if (subTaxa) {
        String table = taxaType.replaceAll("[()]","");
        String[] words = selectedItem.getValue().split("\\s+");
        List<Pair<Object, Integer>> list = new ArrayList<>();
        list.add(new Pair<>(newItemName, Types.VARCHAR));

        if(selectedItem != rootItem){
          list.add(new Pair<>(words[0], Types.VARCHAR));
        }

        model.insertRow(table,list);
        selectedItem.getChildren().add(0, newItem);
        selectedItem.setExpanded(true);
      } else {
        if(selectedItem.getParent() == rootItem){
          List<Pair<Object, Integer>> list = new ArrayList<>();
          list.add(new Pair<>(newItemName, Types.VARCHAR));
          model.insertRow("Domain",list);
        }
        else{
          String[] words = selectedItem.getParent().getValue().split("\\s+");
          List<Pair<Object, Integer>> list = new ArrayList<>();
          list.add(new Pair<>(newItemName, Types.VARCHAR));
          list.add(new Pair<>(words[0], Types.VARCHAR));
          model.insertRow(taxaType.replaceAll("[()]", ""),list);
        }
        selectedItem.getParent().getChildren().add(0, newItem);
      }
    }
    catch (Exception e){
      ControllerUtils.showErrorMessage(e.getMessage());
    }
    addItemStage.close();
  }

  /**
   * handles the logic for performing a right click on the tree view
   * @param contextMenu the menu where the possible actions are presented to the user
   */
  private void rightClickHandler(ContextMenu contextMenu) {
    treeOfLife.setOnContextMenuRequested(event -> {
      TreeItem<String> selectedItem = treeOfLife.getSelectionModel().getSelectedItem();

      contextMenu.getItems().clear();  // Clear old items

      // add option for sub taxa addition
      if (rootItem.getChildren().isEmpty()) {
        menuSetter(contextMenu, rootItem);
      } else {
        String[] words = selectedItem.getValue().split("\\s+");
        String taxa = words[words.length - 1].replaceAll("[()]", "");
        if (taxa.equals("Genus")) {
          MenuItem addSpecies = new MenuItem("Add Species");
          addSpecies.setOnAction(event1 -> {
            openSpeciesPane(selectedItem);
          });
          contextMenu.getItems().add(addSpecies);
        }
        else if (!taxa.equals("Species")) {
          menuSetter(contextMenu, selectedItem);
        }

        // add option for horizontal/same rank taxa addition
        MenuItem addItem = new MenuItem("Add " + taxa);
        if (taxa.equals("Species")){
          addItem.setOnAction(event1 -> {
            openSpeciesPane(selectedItem.getParent());
          });
        }
        else{
          addItem.setOnAction(event1 -> {
            addSingleTaxaWindow(selectedItem, "(" + taxa + ")", false);
          });
        }

        // add option for taxa deletion
        MenuItem deleteItem = new MenuItem("Delete " + taxa);
        deleteItem.setOnAction(event1 -> {
          handleDeleteAction(selectedItem);
        });

        // add option for editing taxa
        MenuItem editItem = new MenuItem("Edit " + taxa);
        if (taxa.equals("Species")){
          addItem.setOnAction(event1 -> {
            openSpeciesPane(selectedItem);
          });
        }
        else{
          addItem.setOnAction(event1 -> {
            addSingleTaxaWindow(selectedItem, "(" + taxa + ")", false);
          });
        }

        contextMenu.getItems().addAll(addItem, editItem, deleteItem);

      }

      contextMenu.show(treeOfLife, event.getScreenX(), event.getScreenY());
    });
  }

  /**
   * handles the logic for deleting a node from the {@code treeOfLife}
   * @param selectedNode the selected node
   */
  private void handleDeleteAction(TreeItem<String> selectedNode) {
    if (deleteWarningShown) {
      if (selectedNode != rootItem) {
        TreeItem<String> parent = selectedNode.getParent();
        if (parent != null) {
          parent.getChildren().remove(selectedNode);
        }
      }
    } else {
      String[] words = selectedNode.getValue().split("\\s+");
      String taxa = words[words.length - 1].replaceAll("[()]", "");
      Alert alert = new Alert(Alert.AlertType.WARNING, "Are you sure you want to delete this "
              + taxa + "?" + System.lineSeparator() + "This cannot be undone, and deletions also delete all sub taxa",
              ButtonType.YES, ButtonType.NO);
      alert.setTitle("Warning");
      alert.setHeaderText("Delete Confirmation");

      ButtonType result = alert.showAndWait().orElse(ButtonType.NO);
      if (result == ButtonType.YES) {
        if (selectedNode != rootItem) {
          TreeItem<String> parent = selectedNode.getParent();
          if (parent != null) {
            parent.getChildren().remove(selectedNode);
          }
        }

        deleteWarningShown = true;
      }
    }
  }

  /**
   * handles the styling/color coding of the tree
   */
  private void treeStyling() {
    treeOfLife.setCellFactory(param -> new TreeCell<String>() {
      @Override
      protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
          setText(null);
          setStyle("");
        } else {
          setText(item);
          if (item.contains("(Domain)")) {
            setTextFill(Color.web("#006d77"));
          } else if (item.contains("(Kingdom)")) {
            setTextFill(Color.web("#0077b6"));
          } else if (item.contains("(Phylum)")) {
            setTextFill(Color.GREEN);
          } else if (item.contains("(Class)")) {
            setTextFill(Color.ORANGE);
          } else if (item.contains("(Order)")) {
            setTextFill(Color.PURPLE);
          } else if (item.contains("(Family)")) {
            setTextFill(Color.DARKRED);
          } else if (item.contains("(Genus)")) {
            setTextFill(Color.PINK);
          } else if (item.contains("(Species)")) {
            setTextFill(Color.GRAY);
          }
        }
      }
    });
  }

  private void openSpeciesPane(TreeItem<String> GenusNode) {

    try {
      System.out.println(TreeController.class.getResource("lala"));
      FXMLLoader loader = new FXMLLoader(TreeController.class.getResource("AddSpeciesWindow.fxml"));
      Pane speciesPane = loader.load();
      InsertSpeciesController controller = loader.getController();
      Stage speciesStage = new Stage();
      Scene speciesScene = new Scene(speciesPane);
      speciesStage.setScene(speciesScene);
      speciesStage.setTitle("Edit Species Information");
      speciesStage.show();
      System.out.println(GenusNode.getValue());
      controller.setGenus(GenusNode);
    } catch (IOException e) {
      e.printStackTrace();  // Will give more detailed info about the error
      ControllerUtils.showErrorMessage(e.getMessage());

    }
  }

}
