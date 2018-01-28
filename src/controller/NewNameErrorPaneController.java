package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class NewNameErrorPaneController implements Initializable {

  public AnchorPane rootPane;
  public Label errorMessage;

  /**
   * Show error message when user wants to add "@" symbol in photo name.
   * @param location location
   * @param resources resources
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    errorMessage.setText(
        "Invalid input. Please try again.\n\n 1. Do not include '@', '%', '\\' or '/' in the new name.");
  }
}
