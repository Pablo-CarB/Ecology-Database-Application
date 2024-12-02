package app.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class TreeController {

  @FXML
  private TreeView<String> treeOfLife;  // Use generic type String

  @FXML
  private SplitPane splitPane;

  // Initialization method will automatically be called
  public void initialize() {
    TreeItem<String> rootItem = new TreeItem<>("Database");

    // Create the root item (Domain)
    TreeItem<String> eukarya = new TreeItem<>("Eukarya (Domain)");
    TreeItem<String> bacteria = new TreeItem<>("Bacteria (Domain)");

    rootItem.getChildren().addAll(eukarya, bacteria);

    // Create nodes for different kingdoms (e.g., Animalia, Plantae)
    TreeItem<String> animalia = new TreeItem<>("Animalia (Kingdom)");
    TreeItem<String> plantae = new TreeItem<>("Plantae (Kingdom)");

    // Add the kingdoms to the root
    eukarya.getChildren().addAll(animalia, plantae);

    // Create a sample "Animalia" subtree
    TreeItem<String> chordata = new TreeItem<>("Chordata (Phylum)");
    TreeItem<String> arthropoda = new TreeItem<>("Arthropoda (Phylum)");

    animalia.getChildren().addAll(chordata, arthropoda);

    // Add more specific taxonomic layers for Chordata
    TreeItem<String> mammals = new TreeItem<>("Mammalia (Class)");
    TreeItem<String> birds = new TreeItem<>("Aves (Class)");
    chordata.getChildren().addAll(mammals, birds);

    TreeItem<String> carnivora = new TreeItem<>("Carnivora (Order)");
    mammals.getChildren().add(carnivora);


    TreeItem<String> felidae = new TreeItem<>("Felidae (Family)");
    carnivora.getChildren().addAll(felidae);

    TreeItem<String> panthera = new TreeItem<>("Panthera leo (Genus)");
    felidae.getChildren().addAll(panthera);

    TreeItem<String> lion = new TreeItem<>("P. leo (Species)");
    TreeItem<String> tiger = new TreeItem<>("P. tigris (Species)");
    panthera.getChildren().addAll(lion,tiger);

    // Set the TreeView to use the root item
    treeOfLife.setRoot(rootItem);
    treeOfLife.setShowRoot(true);  // Display the root node

    // Set up the ContextMenu for right-clicking
    ContextMenu contextMenu = new ContextMenu();

    // Menu item for adding a new item
    MenuItem addItem = new MenuItem("Add Taxa");
    addItem.setOnAction(event -> {
      TreeItem<String> selectedItem = treeOfLife.getSelectionModel().getSelectedItem();
      if (selectedItem != null) {
        openAddItemPane(selectedItem);
      }
    });

    // Menu item for deleting an item
    MenuItem deleteItem = new MenuItem("Delete Taxa");
    deleteItem.setOnAction(event -> {
      TreeItem<String> selectedItem = treeOfLife.getSelectionModel().getSelectedItem();
      if (selectedItem != null && selectedItem != rootItem) {
        TreeItem<String> parent = selectedItem.getParent();
        if (parent != null) {
          parent.getChildren().remove(selectedItem);
        }
      }
    });

    MenuItem editItem = new MenuItem("Edit Taxa");

    // Add the "Add New Item" and "Delete Item" menu items to the context menu
    contextMenu.getItems().addAll(addItem, deleteItem, editItem);

    // Show the context menu when right-clicking on a tree item
    treeOfLife.setOnContextMenuRequested(event -> {
      TreeItem<String> selectedItem = treeOfLife.getSelectionModel().getSelectedItem();
      if (selectedItem != null) {
        contextMenu.show(treeOfLife, event.getScreenX(), event.getScreenY());
      }
    });

    treeOfLife.setCellFactory(param -> new TreeCell<String>() {
      @Override
      protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
          setText(null);
          setStyle("");
        } else {
          setText(item);

          // Apply styles based on the taxonomic rank
          if (item.contains("(Domain)")) {
            setTextFill(Color.web("#006d77"));
            setFont(Font.font("Arial", 14));
          } else if (item.contains("(Kingdom)")) {
            setTextFill(Color.web("#0077b6"));
            setFont(Font.font("Arial", 13));
          } else if (item.contains("(Phylum)")) {
            setFont(Font.font("Arial", 12));
            setTextFill(Color.GREEN);
          } else if (item.contains("(Class)")) {
            setFont(Font.font("Arial", 12));
            setTextFill(Color.ORANGE);
          } else if (item.contains("(Order)")) {
            setFont(Font.font("Arial", 12));
            setTextFill(Color.PURPLE);
          } else if (item.contains("(Family)")) {
            setFont(Font.font("Arial", 12));
            setTextFill(Color.DARKRED);
          } else if (item.contains("(Genus)")) {
            setFont(Font.font("Arial", 12));
            setTextFill(Color.PINK);
          } else if (item.contains("(Species)")) {
            setFont(Font.font("Arial", 10));
            setTextFill(Color.GRAY);
          }
        }
      }
    });
  }

  private void openAddItemPane(TreeItem<String> selectedItem) {
    // Create a new pane for adding a new item based on taxonomic rank
    VBox addItemPane = new VBox(10);
    addItemPane.setStyle("-fx-padding: 10;");

    Label nameLabel = new Label("Enter the Name:");
    TextField nameField = new TextField();

    Label descriptionLabel = new Label("Enter Description:");
    TextArea descriptionArea = new TextArea();

    Button addButton = new Button("Add Item");

    // Create different panes based on the taxonomic rank
    String selectedText = selectedItem.getValue();

    if (selectedText.contains("(Domain)")) {
      addItemPane.getChildren().addAll(nameLabel, nameField, descriptionLabel, descriptionArea, addButton);
      addButton.setOnAction(e -> {
        // Handle adding a new Kingdom under Domain
        String newItemName = nameField.getText();
        TreeItem<String> newItem = new TreeItem<>(newItemName + " (Kingdom)");
        selectedItem.getChildren().add(newItem);
      });
    } else if (selectedText.contains("(Kingdom)")) {
      addItemPane.getChildren().addAll(nameLabel, nameField, descriptionLabel, descriptionArea, addButton);
      addButton.setOnAction(e -> {
        // Handle adding a new Phylum under Kingdom
        String newItemName = nameField.getText();
        TreeItem<String> newItem = new TreeItem<>(newItemName + " (Phylum)");
        selectedItem.getChildren().add(newItem);
      });
    } else if (selectedText.contains("(Phylum)")) {
      addItemPane.getChildren().addAll(nameLabel, nameField, descriptionLabel, descriptionArea, addButton);
      addButton.setOnAction(e -> {
        // Handle adding a new Class under Phylum
        String newItemName = nameField.getText();
        TreeItem<String> newItem = new TreeItem<>(newItemName + " (Class)");
        selectedItem.getChildren().add(newItem);
      });
    } else if (selectedText.contains("(Class)")) {
      addItemPane.getChildren().addAll(nameLabel, nameField, descriptionLabel, descriptionArea, addButton);
      addButton.setOnAction(e -> {
        // Handle adding a new Order under Class
        String newItemName = nameField.getText();
        TreeItem<String> newItem = new TreeItem<>(newItemName + " (Order)");
        selectedItem.getChildren().add(newItem);
      });
    } else if (selectedText.contains("(Order)")) {
      addItemPane.getChildren().addAll(nameLabel, nameField, descriptionLabel, descriptionArea, addButton);
      addButton.setOnAction(e -> {
        // Handle adding a new Family under Order
        String newItemName = nameField.getText();
        TreeItem<String> newItem = new TreeItem<>(newItemName + " (Family)");
        selectedItem.getChildren().add(newItem);
      });
    } else if (selectedText.contains("(Family)")) {
      addItemPane.getChildren().addAll(nameLabel, nameField, descriptionLabel, descriptionArea, addButton);
      addButton.setOnAction(e -> {
        // Handle adding a new Genus under Family
        String newItemName = nameField.getText();
        TreeItem<String> newItem = new TreeItem<>(newItemName + " (Genus)");
        selectedItem.getChildren().add(newItem);
      });
    } else if (selectedText.contains("(Genus)")) {
      addItemPane.getChildren().addAll(nameLabel, nameField, descriptionLabel, descriptionArea, addButton);
      addButton.setOnAction(e -> {
        // Handle adding a new Species under Genus
        String newItemName = nameField.getText();
        TreeItem<String> newItem = new TreeItem<>(newItemName + " (Species)");
        selectedItem.getChildren().add(newItem);
      });
    } else {
      // Default case: handle error or unsupported taxonomic rank
      Alert alert = new Alert(Alert.AlertType.WARNING, "Cannot add item here!");
      alert.show();
    }

    // Create a scene to show the add item pane
    Stage addItemStage = new Stage();
    Scene addItemScene = new Scene(addItemPane, 300, 250);
    addItemStage.setTitle("Add New ");
    addItemStage.setScene(addItemScene);
    addItemStage.show();
  }
}
