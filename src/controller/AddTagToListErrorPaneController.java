package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class AddTagToListErrorPaneController implements Initializable {

  public AnchorPane rootPane;
  public Label errorMessage;

  /**
   * Show error message if the user wants to add a tag that contains "@" symbol and "@" is not in
   * the first index.
   *
   * @param location location
   * @param resources resources
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    errorMessage.setText(
        "Invalid input. Please try again.\n\n 1. Do not include '@', '\\', '%'  or '/' symbol in the tag name. \n\n 2. Tag already exists in the tag list.");
  }
}
