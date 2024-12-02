package app.model;

import java.sql.SQLException;

public interface Model {

  void establishConnection(String username, String password) throws Exception;

  void insertRow(String table, String... attributes) throws SQLException;

  void queryTable(String table) throws SQLException;
}
