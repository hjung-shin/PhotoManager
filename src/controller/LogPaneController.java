package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import model.PhotoManager;

public class LogPaneController extends Controller implements Initializable {

  @FXML
  private BorderPane rootPane;
  @FXML
  private TreeView<String> log;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // set treeView
    TreeItem<String> root = new TreeItem<>("Log of all changes");
    root.setExpanded(true);
    ArrayList<ArrayList<String>> logList = PhotoManager.getAllThePastNames();
    if (logList != null) {
      for (ArrayList<String> data : logList) {
        TreeItem<String> time = new TreeItem<>(data.get(0));
        time.setExpanded(true);
        TreeItem<String> name = new TreeItem<>(data.get(1));
        time.getChildren().add(name);
        root.getChildren().add(time);
      }
      log.setRoot(root);

      // Design the treeView
      log.setCellFactory(
          new Callback<TreeView<String>, TreeCell<String>>() {
            @Override
            public TreeCell<String> call(TreeView<String> param) {
              return new TreeCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                  super.updateItem(item, empty);
                  if (item != null) {
                    if (item.equals("Log of all changes")) {
                      setText(item);
                      setDisclosureNode(null);
                      setDisable(true);
                    } else if (!item.contains(".")) {
                      ImageView timeIcon =
                          new ImageView(new Image(getClass().getResourceAsStream("/views/time.png")));
                      timeIcon.setFitWidth(16);
                      timeIcon.setFitHeight(16);
                      setGraphic(timeIcon);
                      setText(item);
                      setDisclosureNode(null);
                      setDisable(true);
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
