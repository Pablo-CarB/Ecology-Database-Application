package app;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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

  public static List<String> getConservationStatusDescriptions() {
    return List.of(ConservationStatus.values())  // Get all enum values
            .stream()                        // Create a stream
            .map(ConservationStatus::getDescription)  // Map each enum to its description
            .collect(Collectors.toList());     // Collect into a List<String>
  }
}