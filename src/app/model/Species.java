package app.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Species {
  private final StringProperty genus;
  private final StringProperty speciesName;
  private final StringProperty commonName;
  private final StringProperty conservationStatus;
  private final IntegerProperty yearDescribed;
  private final StringProperty dietName;
  private final StringProperty strategyName;
  private final BooleanProperty gregarious;

  public Species(String genus, String speciesName, String commonName, String conservationStatus,
                 int yearDescribed, String dietName, String strategyName, boolean gregarious) {
    this.genus = new SimpleStringProperty(genus);
    this.speciesName = new SimpleStringProperty(speciesName);
    this.commonName = new SimpleStringProperty(commonName);
    this.conservationStatus = new SimpleStringProperty(conservationStatus);
    this.yearDescribed = new SimpleIntegerProperty(yearDescribed);
    this.dietName = new SimpleStringProperty(dietName);
    this.strategyName = new SimpleStringProperty(strategyName);
    this.gregarious = new SimpleBooleanProperty(gregarious);
  }

  public StringProperty genusProperty() {
    return genus;
  }

  public StringProperty speciesNameProperty() {
    return speciesName;
  }

  public StringProperty commonNameProperty() {
    return commonName;
  }

  public StringProperty conservationStatusProperty() {
    return conservationStatus;
  }

  public IntegerProperty yearDescribedProperty() {
    return yearDescribed;
  }

  public StringProperty dietNameProperty() {
    return dietName;
  }

  public StringProperty strategyNameProperty() {
    return strategyName;
  }

  public BooleanProperty gregariousProperty() {
    return gregarious;
  }
}
