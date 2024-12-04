package app.model;

import java.sql.DatabaseMetaData;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import app.ConservationStatus;

public class ModelImpl{

  private static ModelImpl instance;
  Connection connection;

  int portNumber = 3306;
  String serverName = "localhost";

  String dbName = "ecosystem_db";

  HashMap<String, ConservationStatus> map = new HashMap<String, ConservationStatus>();

  HashSet<String> tables = new HashSet<>();

  ModelImpl() {

    map.put("extinct".toUpperCase(), ConservationStatus.EX);
    map.put("ex".toUpperCase(), ConservationStatus.EX);
    map.put("extinct in the wild".toUpperCase(), ConservationStatus.EW);
    map.put("ew".toUpperCase(), ConservationStatus.EW);
    map.put("critically endangered".toUpperCase(), ConservationStatus.CE);
    map.put("ce".toUpperCase(), ConservationStatus.CE);
    map.put("endangered".toUpperCase(), ConservationStatus.EN);
    map.put("e".toUpperCase(), ConservationStatus.EN);
    map.put("vulnerable".toUpperCase(), ConservationStatus.V);
    map.put("v".toUpperCase(), ConservationStatus.V);
    map.put("near threatened".toUpperCase(), ConservationStatus.NT);
    map.put("nt".toUpperCase(), ConservationStatus.NT);
    map.put("least concern".toUpperCase(), ConservationStatus.LC);
    map.put("lc".toUpperCase(), ConservationStatus.LC);
    map.put("not evaluated".toUpperCase(), ConservationStatus.NE);
    map.put("ne".toUpperCase(), ConservationStatus.NE);

    tables.add("Domain");
    tables.add("Kingdom");
    tables.add("Phylum");
    tables.add("Class");
    tables.add("Order");
    tables.add("Family");
    tables.add("Genus");
    tables.add("Species");
    tables.add("DietaryPattern");
    tables.add("FeedingStrategy");
    tables.add("Predation");
    tables.add("Mutualism");
    tables.add("Parasitism");

  }

  public static synchronized ModelImpl getInstance() {
    if (instance == null) {
      instance = new ModelImpl();
    }
    return instance;
  }

  public void establishConnection(String username, String password) throws Exception {
    Properties connectionProps = new Properties();
    connectionProps.put("user", username);
    connectionProps.put("password", password);
    try{
      connection = DriverManager.getConnection("jdbc:mysql://"
              + this.serverName + ":" + this.portNumber
              + "/" + this.dbName
              + "?characterEncoding=UTF-8&useSSL=false", connectionProps);
    }
    catch (SQLException e){
      throw new Exception("Could not connect"
              + " to the database, please retry credentials and check connection");
    }

  }

  private String formatTableName(String name){
    if(name.length() == 0){
      return "";
    }
    else if(name.length() == 1){
      return name.toUpperCase();
    }
    else{
      name = name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
      return name;
    }
  }

  public void insertRow(String table, String... attributes) throws SQLException{
    // check if the table actually exists in the db
    table = formatTableName(table);
    if (!tables.contains(table)) {
      throw new SQLException("ERROR: Invalid table name");
    }

    // initialize variables
    DatabaseMetaData metaData = connection.getMetaData();
    ResultSet columns = null;
    PreparedStatement idStmt = null;

    try{

      // retrieve columns from table
      columns = metaData.getColumns(this.dbName, null, table, null);
      StringBuilder columnString = new StringBuilder();
      while (columns.next()) {
        if (columnString.length() > 0) {
          columnString.append(", ");
        }
        columnString.append(columns.getString("COLUMN_NAME"));
      }

      if (columnString.length() == 0) {
        throw new SQLException("ERROR: No columns found for table: " + table);
      }

      if (attributes.length != columnString.toString().split(",").length) {
        throw new SQLException("ERROR: number of values inputted is incorrect" + Arrays.toString(attributes) +
                columnString);
      }

      StringBuilder placeholders = new StringBuilder();
      for (int i = 0; i < attributes.length; i++) {
        if (i > 0) {
          placeholders.append(", ");
        }
        placeholders.append("?");
      }

      String insertQuery = "INSERT INTO `" + table + "` (" + columnString + ") VALUES (" + placeholders + ")";

      idStmt = connection.prepareStatement(insertQuery);

      for (int i = 0; i < attributes.length; i++) {
        idStmt.setString(i + 1, attributes[i]);
      }

      idStmt.executeUpdate();

    }
    catch (SQLException e){
      if(e.getErrorCode() == 1062){
        throw new SQLException("the "+table+" already exists in the database");
      }
      else{
        throw e;
      }
    }
    // close variables
    finally{
      if (columns != null) {
        columns.close();
      }
      if (idStmt != null) {
        idStmt.close();
      }
    }
  }
  public void queryTable(String table) throws SQLException{
    table = formatTableName(table);
    if (tables.contains(table)) {
      throw new SQLException("ERROR: Invalid table name");
    }

    String tableQuery = "Select * FROM `table`;";
    PreparedStatement idStmt = connection.prepareStatement(tableQuery);
    idStmt.executeQuery();
  }

  public void updateRow(String table, String... attributes) throws SQLException{
    DatabaseMetaData metaData = connection.getMetaData();
    ResultSet columns = metaData.getColumns(null, null, table, null);

    StringBuilder columnString = new StringBuilder();
    while (columns.next()) {
      columnString.append(columns.getString("COLUMN_NAME")).append(",");
    }
    columnString = new StringBuilder(columnString.substring(0, columnString.length() - 1));

    StringBuilder attributesString = new StringBuilder();
    for(String s : attributes){
      attributesString.append(s).append(",");
    }
    attributesString = new StringBuilder(attributesString.substring(0, attributesString.length() - 1));

    String insertQuery = "INSERT INTO `"+table.toUpperCase()+"`(" + columnString + ") VALUES (" +
            attributesString +");";

    PreparedStatement idStmt = connection.prepareStatement(insertQuery);
    idStmt.executeUpdate();
  }
  public List<String> querySubTaxa(String parentType, String parentName) {
    List<String> subTaxa = new ArrayList<>();
    String query = "";

    switch (parentType) {
      case "Database":
        query = "SELECT * FROM Domain;";
        break;
      case "Domain":
        query = "SELECT kingdom_name FROM Kingdom WHERE domain_name = ?";
        break;
      case "Kingdom":
        query = "SELECT phylum_name FROM Phylum WHERE kingdom_name = ?";
        break;
      case "Phylum":
        query = "SELECT class_name FROM Class WHERE phylum_name = ?";
        break;
      case "Class":
        query = "SELECT order_name FROM `Order` WHERE class_name = ?";
        break;
      case "Order":
        query = "SELECT family_name FROM Family WHERE order_name = ?";
        break;
      case "Family":
        query = "SELECT genus_name FROM Genus WHERE family_name = ?";
        break;
      case "Genus":
        query = "SELECT CONCAT(genus_name, ' ', specific_name) AS species_name FROM Species WHERE genus_name = ?";
        break;
      default:
        return subTaxa;
    }

    try (PreparedStatement stmt = this.connection.prepareStatement(query)) {
      stmt.setString(1, parentName);

      try (ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
          subTaxa.add(rs.getString(1));
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return subTaxa;
  }

  public Species querySpeciesDetails(String genusName, String specificName) {
    String query = "SELECT s.genus_name, s.specific_name, s.common_name, s.conservation_status, " +
            "s.year_described, s.diet_name, s.strategy_name, s.gregarious " +
            "FROM Species s " +
            "WHERE s.genus_name = ? AND s.specific_name = ?";

    try (PreparedStatement stmt = connection.prepareStatement(query)) {
      stmt.setString(1, genusName);
      stmt.setString(2, specificName);

      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          String genus = rs.getString("genus_name");
          String species = rs.getString("specific_name");
          String commonName = rs.getString("common_name");
          String conservationStatus = rs.getString("conservation_status");
          int yearDescribed = rs.getInt("year_described");
          String dietName = rs.getString("diet_name");
          String strategyName = rs.getString("strategy_name");
          boolean gregarious = rs.getBoolean("gregarious");

          return new Species(genus, species, commonName, conservationStatus, yearDescribed,
                  dietName, strategyName, gregarious);
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return null;
  }
}

