package app.model;

import java.sql.DatabaseMetaData;
import java.sql.Statement;
import java.util.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import app.ConservationStatus;
import app.ControllerUtils;
import javafx.util.Pair;

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
    tables.add("Dietarypattern");
    tables.add("Feedingstrategy");
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

  public void insertRow(String table, List<Pair<Object, Integer>> attributes) throws SQLException{
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
        throw new SQLException("No columns found for table: " + table);
      }

      if (attributes.size() != columnString.toString().split(",").length) {
        throw new SQLException("Number of values inputted is incorrect" + Arrays.toString(attributes.toArray()) +
                columnString);
      }

      StringBuilder placeholders = new StringBuilder();
      for (int i = 0; i < attributes.size(); i++) {
        if (i > 0) {
          placeholders.append(", ");
        }
        placeholders.append("?");
      }

      String insertQuery = "INSERT INTO `" + table + "` (" + columnString + ") VALUES (" + placeholders + ")";

      idStmt = connection.prepareStatement(insertQuery);

      for (int i = 0; i < attributes.size(); i++) {
        Object value = attributes.get(i).getKey();
        int sqlType = attributes.get(i).getValue();

        idStmt.setObject(i+1,value,sqlType);
      }
      System.out.println(insertQuery);
      System.out.println(Arrays.toString(attributes.toArray()));
      idStmt.executeUpdate();

    }
    catch (SQLException e){
      if(e.getErrorCode() == 1062){
        throw new SQLException("the "+table+" already exists in the database");
      }
      else{
        e.printStackTrace();
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
  public List<String> queryTable(String table) throws SQLException{
    table = formatTableName(table);
    if (!tables.contains(table)) {
      throw new SQLException("ERROR: Invalid table name");
    }
    System.out.println(table);
    String tableQuery = "Select * FROM "+table +";";
    // Use a Statement instead of PreparedStatement since there are no parameters
    Statement stmt = null;
    ResultSet rs = null;
    List<String> list = new ArrayList<>();

    try {
      // Create a statement and execute the query
      stmt = connection.createStatement();
      rs = stmt.executeQuery(tableQuery);

      // Process the result set
      while (rs.next()) {
        list.add(rs.getString(1)); // Assuming you want to fetch the first column
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw e;
    } finally {
      // Close resources properly in a finally block
      if (rs != null) {
        try {
          rs.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
      if (stmt != null) {
        try {
          stmt.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }

    return list;
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

    PreparedStatement stmt = null;

    try{
      if(parentType.equals("Database")){
        query = "SELECT * FROM Domain;";
        stmt = this.connection.prepareStatement(query);
      }
      else if(parentType.equals("Genus")) {
        query = "SELECT * FROM Species WHERE genus_name = ?";
        stmt = this.connection.prepareStatement(query);
        stmt.setString(1, parentName);
      }
      else{
        String firstVar =  parentType.toLowerCase()+"_name";
        System.out.println("parentType " + parentType);
        System.out.println("test " + ControllerUtils.descendHierarchy.get(parentType).toLowerCase());
        String secondVar = ControllerUtils.descendHierarchy.get(parentType).toLowerCase()+"_name";

        query = "SELECT " + secondVar + " FROM `" + ControllerUtils.descendHierarchy.get(parentType) + "` WHERE " + firstVar + " = ?";

        stmt = this.connection.prepareStatement(query);
        stmt.setString(1, parentName);
      }
      ResultSet rs = stmt.executeQuery();
      int var = 1;
      if(parentType.equals("Genus")){
        var = 2;
      }
      while (rs.next()) {
        subTaxa.add(rs.getString(var));
      }

    }
    catch (Exception e){
      e.printStackTrace();
    }

    return subTaxa;
  }

  public Species querySpeciesDetails(String genusName, String specificName) {
    String query = "SELECT * FROM Species WHERE genus_name = ? AND specific_name = ?";

    try (PreparedStatement stmt = connection.prepareStatement(query)) {

      // Debugging: Print the query parameters
      System.out.println("Executing query with genus_name = " + genusName + " and specific_name = " + specificName);

      stmt.setString(1, genusName.trim()); // Trim spaces before setting
      stmt.setString(2, specificName.trim()); // Trim spaces before setting

      System.out.println(query);

      try (ResultSet rs = stmt.executeQuery()) {
        System.out.println("Query executed successfully");

        // Check if ResultSet is not empty
        if (rs.next()) {
          String genus = rs.getString("genus_name");
          String species = rs.getString("specific_name");
          String commonName = rs.getString("common_name");
          String conservationStatus = rs.getString("conservation_status");
          int yearDescribed = rs.getInt("year_described");
          String dietName = rs.getString("diet_name");
          String strategyName = rs.getString("strategy_name");

          // Handle the 'gregarious' field which is stored as an integer (0 or 1)
          int gregariousInt = rs.getInt("gregarious");
          boolean gregarious = (gregariousInt == 1); // Convert 1 to true, 0 to false

          // Print the retrieved data for debugging
          System.out.println("Found species: " + genus + " " + species + " - " +
                  commonName + ", " + conservationStatus + ", " + yearDescribed +
                  ", " + dietName + ", " + strategyName + ", gregarious: " + gregarious);

          return new Species(genus, species, commonName, conservationStatus, yearDescribed,
                  dietName, strategyName, gregarious);
        } else {
          System.out.println("No species found for genus: " + genusName + " and specificName: " + specificName);
        }
      } catch (SQLException e) {
        ControllerUtils.showErrorMessage("Error executing the query: " + e.getMessage());
        e.printStackTrace();
      }

    } catch (SQLException e) {
      // Outer SQLException (connection/statement issue)
      ControllerUtils.showErrorMessage("preparing the statement or connection issue:" + e.getMessage());
      e.printStackTrace();
    }

    return null; // Return null if no result found
  }



}

