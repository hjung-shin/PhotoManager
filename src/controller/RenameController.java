package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import model.DirectoryManager;
import model.Photo;
import model.PhotoManager;
import model.User;

public class RenameController extends Controller implements Initializable {

  @FXML private BorderPane rootPane;

  @FXML private Label photoName;

  @FXML private TextField newNameTextField;

  @FXML public ImageView focusedImage;

  private Image imageOrigin = new Image("File:" + model.User.getPhoto().getPath());

  /**
   * Initialize the fxml Pane.
   *
   * @param location location
   * @param resources resources
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // set focused image
    focusedImage.setImage(imageOrigin);
    double imageHeight = imageOrigin.getHeight();
    double imageWidth = imageOrigin.getWidth();
    focusedImage.setImage(imageOrigin);
    focusedImage.setPreserveRatio(true);
    if (imageHeight <= imageWidth) {
      focusedImage.setFitWidth(900);
    } else if (imageHeight > imageWidth) {
      focusedImage.setFitHeight(535);
    }

    // set photo name
    photoName.setText(model.User.getPhoto().getFileName());
  }

  /**
   * Handle onClickAction for rename button. Rename the photo. Show error message if input is empty,
   * or user gives invalid inputs. e.g. containing "@" symbol in newName
   *
   * @throws IOException throw exception if file doesn't exist.
   */
  @FXML
  private void handleRenameButton() throws IOException {
    if (newNameTextField != null) {
      String newName = newNameTextField.getText();
      // check whether input is empty or it only contains whitespace
      if (newName.isEmpty() || newName.trim().isEmpty()) {
        loadError("/views/EmptyInputErrorPane.fxml");
      }
      // check if the input contains "@" symbol
      else if (newName.contains("@")
          || newNameTextField.getText().contains(File.separator)
          || newNameTextField.getText().contains("\\")
          || newNameTextField.getText().contains("%")) {
        loadError("/views/InvalidInputErrorPane.fxml");
      }
      // check if the file with the same name already exists.
      else if (DirectoryManager.checkSamePicture(User.getPhoto(), newName)) {
        loadError("/views/DuplicateFileErrorPane.fxml");
      } else {
        Photo photo = PhotoManager.getPhoto(User.getPhoto().getPath());
        if (photo != null) {
          photo.rename(newName);
          User.setPhoto(model.PhotoManager.getPhoto(photo.getPath()));
          model.User.setPhoto(photo);
          loadNextScene(rootPane, "/views/RenamePane.fxml");
        }
      }
    }
  }

  /**
   * Handle onClickAction for back button. Reload mainPane.
   *
   * @throws IOException throw exception if file doesn't exist
   */
  @FXML
  private void handleBackButton() throws IOException {
    loadNextScene(rootPane, "/views/focusPhotoPane.fxml");
  }
}
