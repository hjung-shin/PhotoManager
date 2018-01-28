package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class WelcomeController extends Controller implements Initializable {

  @FXML private AnchorPane rootPane;

  /**
   * Initialize the fxml Pane.
   *
   * @param location location
   * @param resources resources
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    try {
      // load configuration files
      model.PhotoManager.readFile();
      model.TagManager.readFile();
      model.PhotoManager.readFilePastName();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  /**
   * handle onMouseClick for Get Started button.
   *
   * @throws IOException throw exception if file doesn't exist.
   */
  @FXML
  private void handleButtonClick() throws IOException {
    // choose directory
    DirectoryChooser chooser = new DirectoryChooser();
    chooser.setTitle("Choose Home Directory");
    Stage curStage = (Stage) rootPane.getScene().getWindow();
    File selectedDirectory = chooser.showDialog(curStage);
    if (selectedDirectory != null) {
      // set directory for MainPane
      model.User.setRootDir(selectedDirectory);
      model.DirectoryManager.set(selectedDirectory);
      model.User.setGalleryPhotos(model.DirectoryManager.getTree().getDir().getPhotos());
      fadeOutEvent();
    } else {
      curStage.show();
    }
  }

  /** FadeOut animation. */
  private void fadeOutEvent() {
    FadeTransition fadeTransition = new FadeTransition();
    fadeTransition.setDuration(Duration.millis(1000));
    fadeTransition.setNode(rootPane);
    fadeTransition.setFromValue(1);
    fadeTransition.setToValue(0);
    fadeTransition.setOnFinished(
        event -> {
          try {
            anchorPaneLoadNextScene(rootPane, "/views/mainPane.fxml");
          } catch (IOException e) {
            e.printStackTrace();
          }
        });
    fadeTransition.play();
  }
}
