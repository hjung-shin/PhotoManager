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
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import model.Tag;
import model.TagManager;

public class AllTagPaneController extends Controller implements Initializable {

  @FXML private BorderPane rootPane;

  @FXML private ListView<Tag> tagList;

  @FXML private TextField newTagTextField;

  private ArrayList<Tag> selectedTag;

  /**
   * set listView which shows all of the existing tags.
   *
   * @param location location
   * @param resources resources
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // Set listView
    // Show all existing tags in the listView
    ObservableList<Tag> observableList = FXCollections.observableArrayList();
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
   * Handle onClickAction for add button. Add tags to photo. Show pop up warning window if input is
   * empty or input contains "@' symbol and "@" symbol is not in the first index.
   *
   * @throws IOException throw exception if file doesn't exist
   */
  @FXML
  private void handleAddButton() throws IOException {
    if (newTagTextField != null) {
      // if input is empty or contains whitespace only
      if (newTagTextField.getText().isEmpty() || newTagTextField.getText().trim().isEmpty()) {
        newTagTextField.clear();
        loadError("/views/EmptyInputErrorPane.fxml");
      } else {
        // if user types "@" at first, then we treat following string as tag
        if (newTagTextField.getText().startsWith("@")) {
          TagManager.addTagInList(new Tag(newTagTextField.getText().substring(1)));
          // refresh scene
          newTagTextField.clear();
          loadNextScene(rootPane, "/views/AllTagPane.fxml");
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
            TagManager.addTagInList(new Tag(newTagTextField.getText()));
            // refresh scene
            newTagTextField.clear();
            loadNextScene(rootPane, "/views/AllTagPane.fxml");
          }
        }
      }
    }
  }

  /**
   * Handle onClickAction for delete button. Delete selected tag(s) from the tag list.
   *
   * @throws IOException throw exception if file doesn't exist
   */
  @FXML
  private void handleDeleteButton() throws IOException {
    if (!selectedTag.isEmpty()) {
      for (Tag tag : selectedTag) {
        if (TagManager.getTags().contains(tag)) {
          TagManager.removeTagInList(tag);
        }
      }
      // refresh scene
      selectedTag.clear();
      loadNextScene(rootPane, "/views/AllTagPane.fxml");
    }
  }

  /**
   * Handle onClickAction for back button. Reload mainPane.
   *
   * @throws IOException throw exception if file doesn't exist
   */
  @FXML
  private void handleBackButton() throws IOException {
    loadNextScene(rootPane, "/views/mainPane.fxml");
  }
}
