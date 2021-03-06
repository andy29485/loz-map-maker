package loz.mapmaker;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
  @Override
  public void start(Stage stage) {
    Scene scene = new Scene(new DungeonPane(), 700, 400);

    stage.setTitle("LoZ Map Maker v0.91");
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
