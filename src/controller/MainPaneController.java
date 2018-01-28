package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import model.*;
import java.io.File;
import java.io.IOException;

public class MainPaneController extends Controller implements Initializable {

  @FXML private BorderPane rootPane;

  @FXML private TilePane galleryDisplay;

  @FXML private TreeView<File> dirFolders;

  /**
   * Initialize the fxml pane.
   *
   * @param location url location
   * @param resources resource
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // set treeView. Show all subdirectories and photos under every subdirectories.
    try {
      setTree();
    } catch (IOException e) {
      e.printStackTrace();
    }
    // set Gallery Display. Only show the photo directly under the selected directory.
    setGalleryDisplay();
  }

  /**
   * Build DirectoryManager.tree to visible TreeView.
   *
   * @param node tree node of DirectoryManager.tree
   * @param parent parent root of visible tree
   * @return visible tree
   * @throws IOException throw exception if file doesn't exist
   */
  private TreeItem<File> buildTree(model.Node node, TreeItem<File> parent) throws IOException {
    // set the root of the tree
    TreeItem<File> root = new TreeItem<>(node.getDir().getFile());
    // show all subdirectories and photos by default
    root.setExpanded(true);
    // set all photos under the directory as the child TreeItem of this directory
    for (model.Photo photo : node.getDir().getPhotos()) {
      root.getChildren().add(new TreeItem<>(photo.getFile()));
      if (!model.PhotoManager.getPhotos().contains(photo)) {
        model.PhotoManager.addPhoto(photo);
      }
    }
    // set all subdirectories of this directory as the child TreeItem
    for (Object child : node.getChildren()) {
      model.Node subDir = (model.Node) child;
      buildTree(subDir, root);
    }
    if (parent == null) {
      return root;
    } else {
      parent.getChildren().add(root);
    }
    return null;
  }

  /**
   * Set the visible tree. Set text, graphic, and handle onClickAction.
   *
   * @throws IOException throw exception if file doesn't exist.
   */
  private void setTree() throws IOException {
    TreeItem<File> root = new TreeItem<>();
    dirFolders.setRoot(buildTree(model.DirectoryManager.getTree(), root));
    dirFolders.setRoot(root.getChildren().get(0));

    // format cellFactory
    // we want to show file name in UI although treeView stores File object
    // Add directory logo if the file is a directory
    // Add photo thumbnail if the file is a photo
    dirFolders.setCellFactory(
        new Callback<TreeView<File>, TreeCell<File>>() {
          @Override
          public TreeCell<File> call(TreeView<File> param) {
            return new TreeCell<File>() {
              @Override
              protected void updateItem(File item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                  if (item.isDirectory()) {
                    setGraphic(
                        new ImageView(
                            new Image(getClass().getResourceAsStream("/views/folder.png"))));
                    setDisclosureNode(null);
                  } else {
                    ImageView image = new ImageView("File:" + item.getPath());
                    image.setFitHeight(32);
                    image.setFitWidth(36);
                    setGraphic(image);
                  }
                  setText(item.getName());
                }
              }
            };
          }
        });

    // handle onClickAction
    dirFolders
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              if (!dirFolders.getSelectionModel().getSelectedItem().getValue().isDirectory()) {
                model.User.setPhoto(
                    model.PhotoManager.getPhoto(
                        dirFolders.getSelectionModel().getSelectedItem().getValue().getPath()));
                try {
                  loadNextScene(rootPane, "/views/focusPhotoPane.fxml");
                } catch (IOException e) {
                  e.printStackTrace();
                }
              } else {
                Stage curStage = (Stage) rootPane.getScene().getWindow();
                File selectedDirectory =
                    dirFolders.getSelectionModel().getSelectedItem().getValue();
                if (selectedDirectory != null) {
                  Directory dir =
                      DirectoryManager.getCurrentDirectory(selectedDirectory.getPath());
                  if (dir != null) {
                    model.User.setGalleryPhotos(dir.getPhotos());
                    fadeOutEvent();
                  }
                } else {
                  curStage.show();
                }
              }
            });
  }

  /** Set up GalleryDisplay, set size of the image, and handle onClickAction. */
  private void setGalleryDisplay() {
    // ArrayList<model.Photo> photos = model.DirectoryManager.getTree().getDir().getPhotos();
    ArrayList<Photo> photos = User.getGalleryPhotos();
    if (photos != null) {
      for (model.Photo photo : photos) {
        Image imageOrigin = new Image("File:" + photo.getPath());
        ImageView displayImage = new ImageView(imageOrigin);
        displayImage.setId(photo.getPath());

        double imageHeight = imageOrigin.getHeight();
        double imageWidth = imageOrigin.getWidth();
        displayImage.setPreserveRatio(true);
        if (imageHeight <= imageWidth) {
          displayImage.setFitWidth(190);
        } else if (imageHeight > imageWidth) {
          displayImage.setFitHeight(120);
        }

        displayImage.setFitWidth(190);
        displayImage.getStyleClass().add("photoElement");
        displayImage.setOnMouseClicked(
            (MouseEvent e) -> {
              model.User.setPhoto(model.PhotoManager.getPhoto(displayImage.getId()));
              try {
                loadNextScene(rootPane, "/views/focusPhotoPane.fxml");
              } catch (IOException e1) {
                e1.printStackTrace();
              }
            });
        galleryDisplay.getChildren().add(displayImage);
      }
    }
  }

  /**
   * Handle button onClickAction enables the user to choose new top directory.
   *
   * @throws IOException throw exception if file doesn't exist.
   */
  @FXML
  private void chooseNewDirEvent() throws IOException {
    DirectoryChooser chooser = new DirectoryChooser();
    chooser.setTitle("Choose Home Directory");
    Stage curStage = (Stage) rootPane.getScene().getWindow();
    File selectedDirectory = chooser.showDialog(curStage);
    if (selectedDirectory != null) {
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
            loadNextScene(rootPane, "/views/mainPane.fxml");
          } catch (IOException e) {
            e.printStackTrace();
          }
        });
    fadeTransition.play();
  }

  /** Handle onMouseClick for favourite photo button. */
  @FXML
  private void chooseFavouritePhotoEvent() {
    if (!PhotoManager.getFavouriteList().isEmpty()) {
      User.setGalleryPhotos(PhotoManager.getFavouriteList());
      fadeOutEvent();
    }
  }

  /** Handle onMouseClick for all tags button. */
  @FXML
  private void seeAllTagsEvent() throws IOException {
    loadNextScene(rootPane, "/views/AllTagPane.fxml");
  }

  /** Handle onMouseClick for all tags button. */
  @FXML
  private void seeAllLogsEvent() throws IOException {
    loadNextScene(rootPane, "/views/LogPane.fxml");
  }
}
