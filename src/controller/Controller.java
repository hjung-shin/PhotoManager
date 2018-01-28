package controller;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

abstract class Controller {

  /**
   * Close current scene and load new scene.
   *
   * @param rootPane the rootPane of the current scene.
   * @param nextScene fxml file name that we want to load.
   * @throws IOException throws exception if file doesn't exist.
   */
  void loadNextScene(BorderPane rootPane, String nextScene) throws IOException {
    Parent secondView;
    secondView = (BorderPane) FXMLLoader.load(getClass().getResource(nextScene));
    Scene newScene = new Scene(secondView);

    Stage curStage = (Stage) rootPane.getScene().getWindow();

    curStage.setScene(newScene);
  }

  /**
   * Pop up warning window for showing error message.
   *
   * @param errorPane fxml file name that we want to load.
   * @throws IOException throw exception if file doesn't exist.
   */
  void loadError(String errorPane) throws IOException {
    Stage stage = new Stage();
    Parent renameParent = FXMLLoader.load(getClass().getResource(errorPane));
    Scene renameScene = new Scene(renameParent);
    stage.setScene(renameScene);
    stage.initModality(Modality.APPLICATION_MODAL);
    stage.showAndWait();
  }

  /**
   * Close current scene and load new scene.
   *
   * @param rootPane the rootPane of the current scene.
   * @param nextScene fxml file name that we want to load.
   * @throws IOException throws exception if file doesn't exist.
   */
  void anchorPaneLoadNextScene(AnchorPane rootPane, String nextScene) throws IOException {
    Parent secondView;
    secondView = (BorderPane) FXMLLoader.load(getClass().getResource(nextScene));
    Scene newScene = new Scene(secondView);
    Stage curStage = (Stage) rootPane.getScene().getWindow();
    curStage.setScene(newScene);
  }
}
