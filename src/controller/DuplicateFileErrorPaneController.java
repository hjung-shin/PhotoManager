package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class DuplicateFileErrorPaneController implements Initializable {

  public AnchorPane rootPane;
  public Label errorMessage;

  /**
   * Show error message if there's a file with the same name already exists.
   * @param location location
   * @param resources resources
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    errorMessage.setText("An item with the same name already exists in this location.");
  }
}
