package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class InvalidnputErrorPaneController implements Initializable {

  public AnchorPane rootPane;
  public Label errorMessage;

  /**
   * Show error message when user inputs nothing or whitespace only.
   * @param location location
   * @param resources resources
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    errorMessage.setText(
        "Photo's name cannot contain '@', '\\', '%' or '/' symbol.");
  }
}
