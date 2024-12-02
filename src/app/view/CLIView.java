package app.view;

import java.io.IOException;

public class CLIView implements View{
  Appendable out;

  public CLIView(Appendable out) {
    this.out = out;
  }

  @Override
  public void writeMessage(String message) {
    try {
      this.out.append(message).append(System.lineSeparator());
    } catch (IOException e) {
      throw new IllegalStateException(e.getMessage());
    }
  }
}
