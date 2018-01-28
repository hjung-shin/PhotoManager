package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import model.Photo;
import model.PhotoManager;
import model.Tag;
import model.TagManager;

public class AddTagController extends Controller implements Initializable {

  @FXML private BorderPane rootPane;

  @FXML private Label photoName;

  @FXML private ListView<Tag> tagList;

  @FXML private TextField newTagTextField;

  @FXML private ImageView focusedImage;

  private Image imageOrigin = new Image("File:" + model.User.getPhoto().getPath());

  private ArrayList<Tag> selectedTag;

  /**
   * Initialize the fxml pane.
   *
   * @param location location
   * @param resources resources
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    ObservableList<Tag> observableList = FXCollections.observableArrayList();
    // Set focused photo
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

    // Set photo name
    photoName.setText(model.User.getPhoto().getFileName());

    // Set listView
    // Show all existing tags in the listView
    for (Tag tag : model.TagManager.getTags()) {
      if (!observableList.contains(tag)) {
        observableList.add(tag);
      }
    }
    tagList.setItems(observableList);

    // show tag name in the UI although the listView stores Tag object
    tagList.setCellFactory(
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

    // Handle listView onClickAction.
    // Enable multiple selections
    tagList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    tagList
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            ((observable, oldValue, newValue) -> {
              if (!selectedTag.contains(newValue)) {
                selectedTag.add(newValue);
              } else {
                selectedTag.remove(newValue);
              }
            }));

    // Initialize selectedTag
    selectedTag = new ArrayList<>();
  }

  /**
   * Handle onClickAction for add button. Add tags to photo. Show pop up warning window if there is
   * an error.
   *
   * @throws IOException throw exception if file doesn't exist
   */
  @FXML
  private void handleAddButton() throws IOException {
    Photo photo = PhotoManager.getPhoto(model.User.getPhoto().getPath());
    boolean tagExists = false;
    if (photo != null) {
      if (!selectedTag.isEmpty()) {
        // check if selected tag(s) already exists in photo's current tag
        for (Tag tag : selectedTag) {
          if (photo.getCurTags().contains((tag))) {
            tagExists = true;
          }
        }
        if (tagExists) {
          loadError("/views/AddTagErrorPane.fxml");
        } else {
          photo.addTag(selectedTag);
          model.User.setPhoto(photo);
        }
        // refresh
        selectedTag.clear();
        loadNextScene(rootPane, "/views/AddTagPane.fxml");
      }
    }
  }

  /**
   * Handle onClickAction for delete button. Delete selected photo's tags. Show pop up warning
   * window if there is an error.
   *
   * @throws IOException throw exception if file doesn't exist
   */
  @FXML
  private void handleDeleteButton() throws IOException {
    Photo photo = PhotoManager.getPhoto(model.User.getPhoto().getPath());
    boolean checkTag = false;
    if (photo != null) {
      if (!selectedTag.isEmpty()) {
        // check if selected tag(s) does not exist in photo's current tag
        for (Tag tag : selectedTag) {
          if (!photo.getCurTags().contains((tag))) {
            checkTag = true;
          }
        }
        if (checkTag) {
          loadError("/views/DeleteTagErrorPane.fxml");
        } else {
          photo.removeTag(selectedTag);
          model.User.setPhoto(photo);
        }
        // refresh scene
        selectedTag.clear();
        loadNextScene(rootPane, "/views/AddTagPane.fxml");
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

  /**
   * Handle onClickAction for addToTagList button. Show error if input is empty or
   *
   * @throws IOException throws exception if file doesn't exist.
   */
  @FXML
  private void handleAddToTagList() throws IOException {
    if (newTagTextField != null) {
      // if input is empty or contains whitespace only
      if (newTagTextField.getText().isEmpty() || newTagTextField.getText().trim().isEmpty()) {
        newTagTextField.clear();
        loadError("/views/EmptyInputErrorPane.fxml");
      } else {
        // if user types "@" at first, then we treat following string as tag
        if (newTagTextField.getText().startsWith("@")) {
          Tag tag = new Tag(newTagTextField.getText().substring(1));
          // give error if tag already exists
          if (TagManager.getTags().contains(tag)) {
            newTagTextField.clear();
            loadError("/views/AddTagToListErrorPane.fxml");
          } else {
            TagManager.addTagInList(new Tag(newTagTextField.getText().substring(1)));
            // refresh scene
            newTagTextField.clear();
            loadNextScene(rootPane, "/views/AddTagPane.fxml");
          }
        } else {
          // if input doesn't start with "@" but contains "@" somewhere, then raise error.
          // we don't allow the tag's name to include "@" symbol
          if (newTagTextField.getText().contains("@")
              || newTagTextField.getText().contains(File.separator)
              || newTagTextField.getText().contains("\\")
              || newTagTextField.getText().contains("%")) {
            newTagTextField.clear();
            loadError("/views/AddTagToListErrorPane.fxml");
          } else {
            Tag tag = new Tag(newTagTextField.getText());
            if (TagManager.getTags().contains(tag)) {
              newTagTextField.clear();
              loadError("/views/AddTagToListErrorPane.fxml");
            } else {
              TagManager.addTagInList(new Tag(newTagTextField.getText()));
              // refresh scene
              newTagTextField.clear();
              loadNextScene(rootPane, "/views/AddTagPane.fxml");
            }
          }
        }
      }
    }
  }
}
