package controller;

import java.awt.Desktop;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.DirectoryManager;
import java.io.File;
import java.io.IOException;
import model.Tag;
import model.User;

public class FocusedPaneController extends Controller implements Initializable {

  @FXML private Label photoPath;

  @FXML private ImageView focusedImage;

  @FXML private Rectangle favouriteBacking;

  @FXML private BorderPane rootPane;

  @FXML private Label photoName;

  @FXML public ListView<Tag> curTags;

  /** Photo extensions. */
  private static final String[] photoExtension = {
    ".JPEG", ".JFIF", ".TIFF", ".GIF", ".BMP", ".PNG", ".JPG", "jpg", "jpeg", "png", "bmp", "gif",
  };

  /**
   * Initialize the fxml pane.
   *
   * @param location location
   * @param resources resource
   */
  @FXML
  public void initialize(URL location, ResourceBundle resources) {
    // set photo
    Image imageOrigin = new Image("File:" + model.User.getPhoto().getPath());
    double imageHeight = imageOrigin.getHeight();
    double imageWidth = imageOrigin.getWidth();
    focusedImage.setImage(imageOrigin);
    focusedImage.setPreserveRatio(true);
    if (imageHeight <= imageWidth) {
      focusedImage.setFitWidth(900);
    } else if (imageHeight > imageWidth) {
      focusedImage.setFitHeight(535);
    }

    // set text
    photoName.setText(model.User.getPhoto().getFileName());
    photoPath.setText(model.User.getPhoto().getFile().getAbsolutePath());

    // set list view
    setCurTags();

    //Set favourited photo button color
    if (!User.getPhoto().getFavourite()) {
      favouriteBacking.setStyle("-fx-fill: white;");
    }
    else if (User.getPhoto().getFavourite()) {
      favouriteBacking.setStyle("-fx-fill: red;");
    }
  }

  /**
   * Handle onClickAction for back button.
   *
   * @throws IOException throw exception if file doesn't exist.
   */
  @FXML
  private void backIconClick() throws IOException {
    model.User.setPhoto(null);
    loadNextScene(rootPane, "/views/mainPane.fxml");
  }


  /**
   * Handle onClickAction for favourite button.
   *
   * @throws IOException throw exception if file doesn't exist.
   */
  @FXML
  private void favouriteClick() throws IOException {
    if (!User.getPhoto().getFavourite()) {
      User.getPhoto().like();
      favouriteBacking.setStyle("-fx-fill: red;");
    }
    else if (User.getPhoto().getFavourite()) {
      User.getPhoto().unlike();
      favouriteBacking.setStyle("-fx-fill: white;");
    }
  }

  /**
   * Handle onClickAction for addTag button.
   *
   * @throws IOException throw exception if file doesn't exist.
   */
  @FXML
  private void addTagClick() throws IOException {
    loadNextScene(rootPane, "/views/AddTagPane.fxml");
  }

  /**
   * Handle onClickAction for movePhoto button. Open the DirectoryChooser and enable user to choose
   * new directory.
   *
   * @throws IOException throw exception if file doesn't exist.
   */
  @FXML
  private void movePhotoClick() throws IOException {
    DirectoryChooser chooser = new DirectoryChooser();
    chooser.setTitle("Choose Directory to move");
    Stage curStage = (Stage) rootPane.getScene().getWindow();
    File selectedDirectory = chooser.showDialog(curStage);
    model.Photo photo = model.PhotoManager.getPhoto(model.User.getPhoto().getPath());
    // check if the file with the same name already exist in the destination directory
    boolean checkFileName = false;
    if (photo != null && selectedDirectory != null) {
      // we use listFile instead of DirectoryManager because we want to check when we move
      // photo to a directory that is not under the root directory the user selected.
      File[] files = selectedDirectory.listFiles();
      if (files != null) {
        for (File file : files) {
          if (file != null && file.getName().contains(".")) {
            // filter out the image files only
            if (imageFilter(file)) {
              if (file.getName().contains(" @")) {
                if (photo.getName().trim().equals(file.getName().substring(0, file.getName().indexOf(" @")))) {
                  checkFileName = true;
                }
              } else {
                if (photo.getName().trim().equals(file.getName().substring(0, file.getName().indexOf(".")))) {
                  checkFileName = true;
                }
              }
            }
          }
        }
      }
      if (checkFileName) {
        loadError("/views/DuplicateFileErrorPane.fxml");
      } else {
        model.PhotoManager.getPhotos().remove(photo);
        photo.move(selectedDirectory.getPath());
        model.PhotoManager.addPhoto(photo);
        model.DirectoryManager.set(model.User.getRootDir());
        model.User.setGalleryPhotos(model.DirectoryManager.getTree().getDir().getPhotos());
        loadNextScene(rootPane, "/views/mainPane.fxml");
      }
    }
  }

  /**
   * Handle onClickAction for removeTag button.
   *
   * @throws IOException throw exception if file doesn't exist.
   */
  @FXML
  private void renamePhotoClick() throws IOException {
    loadNextScene(rootPane, "/views/RenamePane.fxml");
  }

  /**
   * Handle onClickAction for previousTags button.
   *
   * @throws IOException throw exception if file doesn't exist.
   */
  @FXML
  private void oldTagsClick() throws IOException {
    loadNextScene(rootPane, "/views/OldTags.fxml");
  }

  /** Show the current tags on the listView. */
  private void setCurTags() {
    ObservableList<Tag> observableList = FXCollections.observableArrayList();
    for (model.Tag tag : model.User.getPhoto().getCurTags()) {
      if (!observableList.contains(tag)) {
        observableList.add(tag);
      }
    }
    curTags.setItems(observableList);

    // show tag name in the UI although the listView stores Tag object
    curTags.setCellFactory(
        new Callback<ListView<Tag>, ListCell<Tag>>() {
          @Override
          public ListCell<Tag> call(ListView<Tag> param) {
            return new ListCell<Tag>() {
              @Override
              protected void updateItem(Tag item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                  setText(item.getName());
                }
              }
            };
          }
        });
  }

  /**
   * Filter out all images files only.
   *
   * @param file the files under the directory user selected
   * @return true if the file is an image file
   */
  private static boolean imageFilter(File file) {
    for (String extension : photoExtension) {
      if (file.getName().endsWith("." + extension)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Open directory that contains the selected photo in OS's file viewer.
   *
   * @throws IOException throw exceptions if file doesn't exist
   */
  @FXML
  private void openDirectoryButton() throws IOException {
    if (System.getProperty("os.name").toLowerCase().contains("nux") || System.getProperty("os.name").toLowerCase().contains("nix")) {
      String command = "nautilus " + DirectoryManager.getCurrentDirectory(User.getPhoto()).getFile().getPath();
      Runtime.getRuntime().exec(command);
    } else {
      Desktop desktop = Desktop.getDesktop();
      File dirToOpen = new File(DirectoryManager.getCurrentDirectory(User.getPhoto()).getFile().getPath());
      desktop.open(dirToOpen);
    }
  }
}
