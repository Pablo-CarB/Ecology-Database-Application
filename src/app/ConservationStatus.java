package app;

import java.util.HashMap;

public enum ConservationStatus {
  EX("extinct"),
  EW("extinct in the wild"),
  CE("critically endangered"),
  EN("endangered"),
  V("vulnerable"),
  NT("near threatened"),
  LC("least concern"),
  NE("not evaluated");

  private final String description;
  ConservationStatus(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }
}