package app.model;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;


public class ModelImplTest {
  ModelImpl model;

  String username = System.getenv("USERNAME");
  String password = System.getenv("PASSWORD");

  @Before
  public void setup(){
    this.model = new ModelImpl();
    try {
      this.model.establishConnection(username, password);
    }
    catch (Exception e){
      System.out.println(e.getMessage());
    }
  }

}
