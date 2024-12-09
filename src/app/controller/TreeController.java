package app.controller;

import java.io.IOException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.ControllerUtils;
import app.TreeRank;
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
  private TreeView<Pair<String, TreeRank>> treeOfLife;

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

  private TreeItem<Pair<String,TreeRank>> rootItem;


  /**
   * method that initializes the tree view window and is run as soon as the fxml is loaded
   */
  public void initialize() {
    this.rootItem = new TreeItem<>();
    treeOfLife.setRoot(rootItem);
    treeOfLife.setShowRoot(false);

    ContextMenu contextMenu = new ContextMenu();

    populateTree(TreeRank.Root, "",rootItem);

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
      TreeRank rank = newValue.getValue().getValue();
      String name = newValue.getValue().getKey();
      if (rank == TreeRank.Species) {
        displaySpeciesDetails(newValue,name);
      }
    });

  }

  /**
   * populates the tree with taxa from the database
   * @param parentRank the rank of the parent node (ie clade,phylum etc)
   * @param parentName the name of the parent taxa (ie homo,canis etc)
   * @param node the current node in the tree
   */
  private void populateTree(TreeRank parentRank,String parentName,TreeItem<Pair<String,TreeRank>> node) {
    List<String> subTaxa = model.querySubTaxa(parentRank, parentName);

    for (String subTaxon : subTaxa) {
      Pair<String,TreeRank> nodeInfo = new Pair<>(subTaxon,parentRank.descendHeirarchy());
      TreeItem<Pair<String,TreeRank>> subTaxonItem = new TreeItem<>(nodeInfo);
      node.getChildren().add(subTaxonItem);

      populateTree(parentRank.descendHeirarchy(), subTaxon, subTaxonItem);
    }
  }

  /**
   * displays the species that is currently selected in the table
   * @param selectedItem the currently selected node in the tree
   * @param specificName the specific name of the currently selected node
   */
  private void displaySpeciesDetails(TreeItem<Pair<String,TreeRank>> selectedItem, String specificName) {
    // Parse the genus from the selected TreeItem
    TreeItem<Pair<String,TreeRank>> parentNode = selectedItem.getParent();
    String genus = parentNode.getValue().getKey();

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
  private void menuSetter(ContextMenu contextMenu, TreeItem<Pair<String,TreeRank>> selected) {
    TreeRank rank = selected.getValue().getValue();
    TreeRank subRank = rank.descendHeirarchy();

    MenuItem addSubTaxa = new MenuItem("Add " + subRank.getTitle());
    addSubTaxa.setOnAction(e -> addSingleTaxaWindow(selected, subRank,
            true));
    contextMenu.getItems().add(addSubTaxa);


  }

  /**
   * creates the window for
   * @param selectedNode the currently
   * @param rank the taxonomic rank of the newly inserted taxa
   * @param subTaxa a boolean that indicates whether the new node to be inserted
   *                will be inserted horizontally or as a sub-node
   */
  private void addSingleTaxaWindow(TreeItem<Pair<String,TreeRank>> selectedNode, TreeRank rank, boolean subTaxa) {
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
    addItemStage.setTitle("Add New " + rank.getTitle());
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


  private void addItem(TextField nameField, TreeItem<Pair<String,TreeRank>> selectedItem, TreeRank taxaType,
                       boolean subTaxa, Stage addItemStage) {
    String newItemName = nameField.getText();
    TreeItem<Pair<String,TreeRank>> newItem = new TreeItem<>(new Pair<String,TreeRank>(newItemName,taxaType));
    try{
      if (subTaxa) {
        String table = taxaType.getTitle();
        String name = selectedItem.getValue().getKey();
        List<Pair<Object, Integer>> list = new ArrayList<>();
        list.add(new Pair<>(newItemName, Types.VARCHAR));

        if(selectedItem != rootItem){
          list.add(new Pair<>(name, Types.VARCHAR));
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
          List<Pair<Object, Integer>> list = new ArrayList<>();
          list.add(new Pair<>(newItemName, Types.VARCHAR));
          list.add(new Pair<>(selectedItem.getValue().getKey(), Types.VARCHAR));
          model.insertRow(taxaType.getTitle(),list);
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
      TreeItem<Pair<String,TreeRank>> selectedItem = treeOfLife.getSelectionModel().getSelectedItem();

      contextMenu.getItems().clear();  // Clear old items

      // add option for sub taxa addition
      if (rootItem.getChildren().isEmpty()) {
        menuSetter(contextMenu, rootItem);
      } else {
        TreeRank rank = selectedItem.getValue().getValue();

        if (rank == TreeRank.Genus) {
          MenuItem addSpecies = new MenuItem("Add Species");
          addSpecies.setOnAction(event1 -> {
            openSpeciesPane(selectedItem);
          });
          contextMenu.getItems().add(addSpecies);
        }
        else if (rank != TreeRank.Species) {
          menuSetter(contextMenu, selectedItem);
        }

        // add option for horizontal/same rank taxa addition
        MenuItem addItem = new MenuItem("Add " + rank.getTitle());
        if (rank == TreeRank.Species){
          addItem.setOnAction(event1 -> {
            openSpeciesPane(selectedItem.getParent());
          });
        }
        else{
          addItem.setOnAction(event1 -> {
            addSingleTaxaWindow(selectedItem, rank, false);
          });
        }

        // add option for taxa deletion
        MenuItem deleteItem = new MenuItem("Delete " + rank.getTitle());
        deleteItem.setOnAction(event1 -> {
          handleDeleteAction(selectedItem);
        });

        // add option for editing taxa
        MenuItem editItem = new MenuItem("Edit " + rank.getTitle());
        if (rank == TreeRank.Species){
          addItem.setOnAction(event1 -> {
            openSpeciesPane(selectedItem);
          });
        }
        else{
          addItem.setOnAction(event1 -> {
            addSingleTaxaWindow(selectedItem, rank, false);
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
  private void handleDeleteAction(TreeItem<Pair<String,TreeRank>> selectedNode) {
    if (deleteWarningShown) {
      if (selectedNode != rootItem) {
        TreeItem<Pair<String,TreeRank>> parent = selectedNode.getParent();
        if (parent != null) {
          parent.getChildren().remove(selectedNode);
        }
      }
    } else {
      TreeRank rank = selectedNode.getValue().getValue();
      Alert alert = new Alert(Alert.AlertType.WARNING, "Are you sure you want to delete this "
              + rank.getTitle() + "?" + System.lineSeparator() + "This cannot be undone, and deletions also delete all sub taxa",
              ButtonType.YES, ButtonType.NO);
      alert.setTitle("Warning");
      alert.setHeaderText("Delete Confirmation");

      ButtonType result = alert.showAndWait().orElse(ButtonType.NO);
      if (result == ButtonType.YES) {
        if (selectedNode != rootItem) {
          TreeItem<Pair<String,TreeRank>> parent = selectedNode.getParent();
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
    Map<TreeRank, Color> rankColors = new HashMap<>();
    rankColors.put(TreeRank.Domain, Color.web("#006d77"));
    rankColors.put(TreeRank.Kingdom, Color.web("#0077b6"));
    rankColors.put(TreeRank.Phylum, Color.GREEN);
    rankColors.put(TreeRank.Class, Color.ORANGE);
    rankColors.put(TreeRank.Order, Color.PURPLE);
    rankColors.put(TreeRank.Family, Color.DARKRED);
    rankColors.put(TreeRank.Genus, Color.PINK);
    rankColors.put(TreeRank.Species, Color.GRAY);

    treeOfLife.setCellFactory(param -> new TreeCell<Pair<String,TreeRank>>() {
      @Override
      protected void updateItem(Pair<String,TreeRank> item, boolean empty) {
        super.updateItem(item, empty);
        if(empty || item == null){
          setText(null);
          setStyle("");
        } else {
          setText(item.getKey());
          TreeRank rank = item.getValue();
          if (rank != null && rankColors.containsKey(rank)) {
            setTextFill(rankColors.get(rank));
          }
        }
      }
    });
  }

  private void openSpeciesPane(TreeItem<Pair<String,TreeRank>> GenusNode) {

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
