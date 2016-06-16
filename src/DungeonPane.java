import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class DungeonPane extends TabPane {
  Dungeon dungeon;

  public DungeonPane() {
    super();
    this.setSide(Side.LEFT);

    dungeon = new Dungeon();

    Tab tabDungeon = new Tab();
    tabDungeon.setText("Dungeon");
    tabDungeon.setContent(createDungeonPane());
    this.getTabs().add(tabDungeon);

    Tab tabItems = new Tab();
    tabItems.setText("Items");
    tabItems.setContent(createItemPane());
    this.getTabs().add(tabItems);

    Tab tabMonsters = new Tab();
    tabMonsters.setText("Monsters");
    tabMonsters.setContent(createMonsterPane());
    this.getTabs().add(tabMonsters);

    Tab tabRooms = new Tab();
    tabRooms.setText("Rooms");
    tabRooms.setContent(createRoomPane());
    this.getTabs().add(tabRooms);

  }

  private Node createDungeonPane() {

    // create main Nodes
    BorderPane pane = new BorderPane();
    ScrollPane scroll = new ScrollPane();
    VBox vbox = new VBox();
    HBox hbox1 = new HBox();
    HBox hbox2 = new HBox();

    // create sub nodes for hbox
    Label nameLabel = new Label("Name:");
    TextField nameField = new TextField();
    Label versionLabel = new Label("Version:");
    TextField versionField = new TextField();
    Button saveButton = new Button("Save");
    Button loadButton = new Button("Load");

    saveButton.setOnAction(e -> dungeon.save());
    loadButton.setOnAction(e -> dungeon.load(dungeon.getFilename()));

    // change name
    nameField.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable,
          String oldValue, String newValue) {

        dungeon.setName(newValue);
      }
    });

    // change version
    versionField.setPrefColumnCount(5);
    versionField.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable,
          String oldValue, String newValue) {

        dungeon.setVersion(newValue);
      }
    });

    // populate hbox
    hbox1.getChildren().addAll(nameLabel, nameField, versionLabel,
        versionField);
    hbox2.getChildren().addAll(saveButton, loadButton);
    vbox.getChildren().addAll(hbox1, hbox2);
    HBox.setHgrow(nameField, Priority.ALWAYS);
    HBox.setHgrow(versionField, Priority.ALWAYS);

    // create sub nodes for scroll
    VBox scrollpane = new VBox();
    Label desLabel = new Label("Description:");
    TextField desField = new TextField();
    Label creditLabel = new Label("Credits:");
    TextField creditField = new TextField();

    // change description
    desField.setPrefColumnCount(5);
    desField.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable,
          String oldValue, String newValue) {

        dungeon.setDescription(newValue);
      }
    });

    // change credits
    creditField.setPrefColumnCount(5);
    creditField.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable,
          String oldValue, String newValue) {

        dungeon.setCredits(newValue);
      }
    });

    // populate scroll
    scroll.setFitToWidth(true);
    scrollpane.getChildren().addAll(desLabel, desField, creditLabel,
        creditField);
    scroll.setContent(scrollpane);

    // populate main node
    pane.setTop(vbox);
    pane.setCenter(scroll);

    return pane;
  }

  private Node createItemPane() {

    TabPane pane = new TabPane();

    addNewTab(pane, "", null, true, 0);
    addNewTab(pane, "+", new Label(), false, 0);
    setBehaviourForPlusTabClick(pane, 0);

    return pane;
  }

  private Node createMonsterPane() {

    TabPane pane = new TabPane();

    addNewTab(pane, "", null, true, 1);
    addNewTab(pane, "+", new Label(), false, 1);
    setBehaviourForPlusTabClick(pane, 1);

    return pane;
  }

  private Node createRoomPane() {

    TabPane pane = new TabPane();

    addNewTab(pane, "", null, true, 2);
    addNewTab(pane, "+", new Label(), false, 2);
    setBehaviourForPlusTabClick(pane, 2);

    return pane;
  }

  private Tab addNewTab(final TabPane tabPane, String newTabName,
      Node newTabContent, boolean isCloseable, int type) {
    if (newTabName.isEmpty() || newTabContent == null) {
      switch (type) {
        case 0:// Item
          newTabName = String.format("Item %d", dungeon.getItems().size());
          newTabContent = getEmptyItemTabContent();
          break;
        case 1:// Monster
          newTabName =
              String.format("Monster %d", dungeon.getMonsters().size());
          newTabContent = getEmptyMonsterTabContent();
          break;
        case 2:// Room
          newTabName = String.format("Room %d", dungeon.getRooms().size());
          newTabContent = getEmptyRoomTabContent();
          break;
      }
    }
    Tab newTab = new Tab(newTabName);
    newTab.setContent(newTabContent);
    newTab.setClosable(isCloseable);
    newTab.setOnClosed(new EventHandler<Event>() {
      @Override
      public void handle(Event event) {
        if (tabPane.getTabs().size() == 2) {
          event.consume();
        }
        else {
          // TODO - remove item from list and change title of other tabs

        }

      }
    });

    tabPane.getTabs().add(newTab);
    return newTab;

  }

  private void setBehaviourForPlusTabClick(final TabPane tabPane, int type) {
    tabPane.getSelectionModel().selectedItemProperty()
        .addListener(new ChangeListener<Tab>() {
          @Override
          public void changed(ObservableValue<? extends Tab> observable,
              Tab oldTab, Tab newTab) {
            if (newTab.getText().equals("+")) {
              Tab addedTab = addNewTab(tabPane, "", null, true, type);
              tabPane.getTabs().remove(addedTab);
              tabPane.getTabs().set(tabPane.getTabs().size() - 1, addedTab);
              tabPane.getTabs().add(newTab);
              tabPane.getSelectionModel().select(addedTab);

            }
          }
        });
  }

  private Node getEmptyItemTabContent() {
    Item item = new Item();
    int id = dungeon.getItems().size();
    dungeon.getItems().add(item);

    // create main Nodes
    BorderPane pane = new BorderPane();
    ScrollPane scroll = new ScrollPane();
    HBox hbox = new HBox();

    // create sub nodes for hbox
    Label idLabel = new Label(String.format("ID: %d", id));
    Label nameLabel = new Label("Name:");
    TextField nameField = new TextField();

    // change name
    nameField.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable,
          String oldValue, String newValue) {

        item.setName(newValue);
      }
    });

    // populate hbox
    hbox.getChildren().addAll(idLabel, nameLabel, nameField);
    HBox.setHgrow(nameField, Priority.ALWAYS);

    // create sub nodes for scroll
    VBox scrollpane = new VBox();
    Label desLabel = new Label("Description:");
    TextField desField = new TextField();

    // change description
    desField.setPrefColumnCount(5);
    desField.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable,
          String oldValue, String newValue) {

        item.setDescription(newValue);
      }
    });

    // populate scroll
    scroll.setFitToWidth(true);
    scrollpane.getChildren().addAll(desLabel, desField);
    scroll.setContent(scrollpane);

    // populate main node
    pane.setTop(hbox);
    pane.setCenter(scroll);

    return pane;
  }

  private Node getEmptyMonsterTabContent() {
    Monster monster = new Monster();
    int id = dungeon.getMonsters().size();
    dungeon.getMonsters().add(monster);

    // create main Nodes
    BorderPane pane = new BorderPane();
    ScrollPane scroll = new ScrollPane();
    HBox hbox = new HBox();

    // create sub nodes for hbox
    Label idLabel = new Label(String.format("ID: %d", id));
    Label nameLabel = new Label("Name:");
    TextField nameField = new TextField();

    // change name
    nameField.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable,
          String oldValue, String newValue) {

        monster.setName(newValue);
      }
    });

    // populate hbox
    hbox.getChildren().addAll(idLabel, nameLabel, nameField);
    HBox.setHgrow(nameField, Priority.ALWAYS);

    // create sub nodes for scroll
    VBox scrollpane = new VBox();
    Label desLabel = new Label("Description:");
    TextField desField = new TextField();

    // change description
    desField.setPrefColumnCount(5);
    desField.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable,
          String oldValue, String newValue) {

        monster.setDescription(newValue);
      }
    });

    // populate scroll
    scroll.setFitToWidth(true);
    scrollpane.getChildren().addAll(desLabel, desField);
    scroll.setContent(scrollpane);

    // populate main node
    pane.setTop(hbox);
    pane.setCenter(scroll);

    return pane;
  }

  private Node getEmptyRoomTabContent() {
    Room room = new Room();
    // int id = dungeon.getRooms().size();
    dungeon.getRooms().add(room);

    // TODO

    return new Label("TODO");
  }
}
