package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class DeleteTagErrorPaneController implements Initializable {

  public AnchorPane rootPane;
  public Label errorMessage;

  /**
   * Show error message when the user wants to delete tag that the photo doesn't have.
   *
   * @param location location
   * @param resources resources
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    errorMessage.setText(
        "Invalid input. This photo doesn't contain selected tag(s). Please try again.");
  }
}
