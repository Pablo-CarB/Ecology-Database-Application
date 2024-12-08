package app.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Species {

  // Fields from the database
  private final StringProperty genusName = new SimpleStringProperty();
  private final StringProperty specificName = new SimpleStringProperty();
  private final StringProperty commonName = new SimpleStringProperty();
  private final StringProperty conservationStatus = new SimpleStringProperty();
  private final IntegerProperty yearDescribed = new SimpleIntegerProperty();
  private final StringProperty dietName = new SimpleStringProperty();
  private final StringProperty strategyName = new SimpleStringProperty();
  private final BooleanProperty gregarious = new SimpleBooleanProperty();

  // Constructor to initialize the fields
  public Species(String genus, String species, String commonName, String conservationStatus,
                 int yearDescribed, String dietName, String strategyName, boolean gregarious) {
    this.genusName.set(genus);
    this.specificName.set(species);
    this.commonName.set(commonName);
    this.conservationStatus.set(conservationStatus);
    this.yearDescribed.set(yearDescribed);
    this.dietName.set(dietName);
    this.strategyName.set(strategyName);
    this.gregarious.set(gregarious);
  }

  // Getter methods for properties
  public StringProperty genusNameProperty() {
    return genusName;
  }

  public StringProperty specificNameProperty() {
    return specificName;
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

  // Getter methods for the values (if needed)
  public String getGenusName() {
    return genusName.get();
  }

  public String getSpecies() {
    return specificName.get();
  }

  public String getCommonName() {
    return commonName.get();
  }

  public String getConservationStatus() {
    return conservationStatus.get();
  }

  public int getYearDescribed() {
    return yearDescribed.get();
  }

  public String getDietName() {
    return dietName.get();
  }

  public String getStrategyName() {
    return strategyName.get();
  }

  public boolean isGregarious() {
    return gregarious.get();
  }

  // Setters (if needed)
  public void setGenusName(String genusName) {
    this.genusName.set(genusName);
  }

  public void setSpecies(String species) {
    this.specificName.set(species);
  }

  public void setCommonName(String commonName) {
    this.commonName.set(commonName);
  }

  public void setConservationStatus(String conservationStatus) {
    this.conservationStatus.set(conservationStatus);
  }

  public void setYearDescribed(int yearDescribed) {
    this.yearDescribed.set(yearDescribed);
  }

  public void setDietName(String dietName) {
    this.dietName.set(dietName);
  }

  public void setStrategyName(String strategyName) {
    this.strategyName.set(strategyName);
  }

  public void setGregarious(boolean gregarious) {
    this.gregarious.set(gregarious);
  }
}


