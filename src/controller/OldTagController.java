package controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.util.Callback;
import model.Photo;

public class OldTagController extends Controller implements Initializable {

  @FXML private ImageView focusedImage;

  @FXML private TreeView<String> treeView;

  @FXML private BorderPane rootPane;

  @FXML private Label photoName;

  @FXML private String selectedTagsTime;

  private Image imageOrigin = new Image("File:" + model.User.getPhoto().getPath());

  /**
   * Initialize the fxml Pane.
   *
   * @param location location
   * @param resources resources.
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // set focused photo
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

    // set treeView
    TreeItem<String> root = new TreeItem<>("Select the time");
    root.setExpanded(true);
    model.Photo photo = model.PhotoManager.getPhoto(model.User.getPhoto().getPath());
    if (photo != null) {
      for (int i = 0; i < photo.getTimes().size(); i++) {
        TreeItem<String> time = new TreeItem<>(photo.getTimes().get(i));
        time.setExpanded(true);
        TreeItem<String> name = new TreeItem<>(photo.getPrevName(photo.getTimes().get(i)));
        time.getChildren().add(name);
        root.getChildren().add(time);
      }
      treeView.setRoot(root);

      // Design the treeView
      // Add clock icon if the string represents the time
      // Add tag icon if the string represents the name
      treeView.setCellFactory(
          new Callback<TreeView<String>, TreeCell<String>>() {
            @Override
            public TreeCell<String> call(TreeView<String> param) {
              return new TreeCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                  super.updateItem(item, empty);
                  if (item != null) {
                    if (item.equals("Select the time")) {
                      setText(item);
                      setDisclosureNode(null);
                      setDisable(true);
                      // only enable selection for time
                    } else if (photo.getTimes().contains(item)) {
                      ImageView timeIcon =
                          new ImageView(new Image(getClass().getResourceAsStream("/views/time.png")));
                      timeIcon.setFitWidth(16);
                      timeIcon.setFitHeight(16);
                      setGraphic(timeIcon);
                      setText(item);
                      setDisclosureNode(null);
                    } else {
                      setText(item);
                      setDisclosureNode(null);
                      setDisable(true);
                      ImageView nameIcon =
                          new ImageView(new Image(getClass().getResourceAsStream("/views/name.png")));
                      setGraphic(nameIcon);
                    }
                  }
                }
              };
            }
          });

      // handle onClickAction for treeItem which shows time
      treeView
          .getSelectionModel()
          .selectedItemProperty()
          .addListener(
              (observable, oldValue, newValue) -> {
                if (!treeView.getSelectionModel().getSelectedItem().getValue().startsWith("@")) {
                  selectedTagsTime = treeView.getSelectionModel().getSelectedItem().getValue();
                }
              });
    }
  }

  /**
   * Handle onClickAction for set button.
   *
   * @throws IOException throw exception if file doesn't exist.
   */
  @FXML
  private void handleSetButton() throws IOException {
    if (selectedTagsTime != null) {
      model.Photo photo = model.PhotoManager.getPhoto(model.User.getPhoto().getPath());
      if (photo != null) {
        String newName = photo.getPrevName(selectedTagsTime);
        if (!photo.getFileName().equals(newName)) {
          photo.changeToOldTag(selectedTagsTime);
          Photo newPhoto = model.PhotoManager.getPhoto(photo.getPath());
          selectedTagsTime = null;
          model.User.setPhoto(newPhoto);
          loadNextScene(rootPane, "/views/OldTags.fxml");
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
