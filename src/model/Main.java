package model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("/views/Welcome.fxml"));
    primaryStage.setTitle("Photos");
    primaryStage.setScene(new Scene(root, 1200, 720));
    primaryStage.setResizable(false);
    primaryStage.show();
  }

  /**
   * Currently holds mock data Must implement the back-end functions in order to have the controller
   * working dynamically
   *
   * @param args
   */
  public static void main(String[] args) {
    launch(args);
  }
}
