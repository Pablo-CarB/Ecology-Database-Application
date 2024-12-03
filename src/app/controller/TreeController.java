package app.controller;

import java.io.IOException;
import java.util.HashMap;

import app.model.ModelImpl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class TreeController {

  @FXML
  private TreeView<String> treeOfLife;


  private final HashMap<String, String> descendHeirarchy = new HashMap<>() {{
    put("Database", "Domain");
    put("Domain", "Kingdom");
    put("Kingdom", "Phylum");
    put("Phylum", "Class");
    put("Class", "Order");
    put("Order", "Family");
    put("Family", "Genus");
    put("Genus", "Species");
  }};

  private ModelImpl model = ModelImpl.getInstance();

  private boolean deleteWarningShown = false;

  private TreeItem<String> rootItem;

  public void initialize() {
    this.rootItem = new TreeItem<>("Database");
    treeOfLife.setRoot(rootItem);
    treeOfLife.setShowRoot(false);

    ContextMenu contextMenu = new ContextMenu();

    // handles right click
    rightClickHandler(contextMenu);

    // TreeView Styling
    treeStyling();
  }

  // creates correct menu option given TreeItem
  private void menuSetter(ContextMenu contextMenu, TreeItem<String> selected) {
    String[] words = selected.getValue().split("\\s+");
    String taxa = words[words.length - 1].replaceAll("[()]", "");
    String subTaxa = descendHeirarchy.get(taxa);

    MenuItem addSubTaxa = new MenuItem("Add " + subTaxa);
    addSubTaxa.setOnAction(e -> addSingleTaxaWindow(selected, "(" + subTaxa + ")", true));
    contextMenu.getItems().add(addSubTaxa);


  }

  // creates window with functionality
  private void addSingleTaxaWindow(TreeItem<String> selectedItem, String taxaType, boolean subTaxa) {
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
    addItemStage.setTitle("Add New " + taxaType.replaceAll("[()]", ""));
    addItemStage.setScene(addItemScene);


    // Action for the Add button
    addButton.setOnAction(e -> {
      addItem(nameField, selectedItem, taxaType, subTaxa, addItemStage);
    });

    // Action for the Enter key
    nameField.setOnAction(e -> {
      addItem(nameField, selectedItem, taxaType, subTaxa, addItemStage);
    });

    // Close the window when the TextField loses focus (but only if the button doesn't have focus)
    nameField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
      if (!isNowFocused) {
        // If the button has focus, don't close the window
        if (!addButton.isFocused()) {
          addItemStage.close(); // Close the stage only when focus is lost (but not on button click)
        }
      }
    });

    // Show the stage
    addItemStage.show();

  }

  private void addItem(TextField nameField, TreeItem<String> selectedItem, String taxaType,
                       boolean subTaxa, Stage addItemStage) {
    String newItemName = nameField.getText();
    TreeItem<String> newItem = new TreeItem<>(newItemName + " " + taxaType);
    if (subTaxa) {
      selectedItem.getChildren().add(0, newItem);  // Add new item under selected node
      selectedItem.setExpanded(true);
    } else {
      selectedItem.getParent().getChildren().add(0, newItem);
    }
    addItemStage.close();
  }

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
            openSpeciesPane(selectedItem);
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

        contextMenu.getItems().addAll(addItem, deleteItem);

      }

      // Show the context menu at the mouse position
      contextMenu.show(treeOfLife, event.getScreenX(), event.getScreenY());
    });
  }

  private void handleDeleteAction(TreeItem<String> selectedItem) {
    if (deleteWarningShown) {
      // Delete the item normally
      if (selectedItem != rootItem) {
        TreeItem<String> parent = selectedItem.getParent();
        if (parent != null) {
          parent.getChildren().remove(selectedItem);
        }
      }
    } else {
      String[] words = selectedItem.getValue().split("\\s+");
      String taxa = words[words.length - 1].replaceAll("[()]", "");
      Alert alert = new Alert(Alert.AlertType.WARNING, "Are you sure you want to delete this "
              + taxa + "?" + System.lineSeparator() + "This cannot be undone, and deletions also delete all sub taxa",
              ButtonType.YES, ButtonType.NO);
      alert.setTitle("Warning");
      alert.setHeaderText("Delete Confirmation");

      ButtonType result = alert.showAndWait().orElse(ButtonType.NO);
      if (result == ButtonType.YES) {
        if (selectedItem != rootItem) {
          TreeItem<String> parent = selectedItem.getParent();
          if (parent != null) {
            parent.getChildren().remove(selectedItem);
          }
        }

        // Mark that the warning has been shown and subsequent deletes don't trigger the warning
        deleteWarningShown = true;
      }
    }
  }

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

  private void openSpeciesPane(TreeItem<String> selectedItem) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("AddSpeciesWindow.fxml"));
      Pane speciesPane = loader.load();

      // Create a scene with the species pane
      Stage speciesStage = new Stage();
      Scene speciesScene = new Scene(speciesPane);
      speciesStage.setScene(speciesScene);
      speciesStage.setTitle("Edit Species Information");
      speciesStage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
