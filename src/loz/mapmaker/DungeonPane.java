package loz.mapmaker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

/**
 * Main pane for the map edittor
 * 
 * @author az
 *
 */
public class DungeonPane extends TabPane {
  /**
   * The dungeon that this Pane edits(backend for this pane).
   */
  Dungeon     dungeon;
  /**
   * List of labels that are used to represent the IDs of items.
   *
   * Used when deleting an item(the IDs need to be changed).
   */
  List<Label> itemIdLabels;
  /**
   * List of labels that are used to represent the IDs of monsters.
   *
   * Used when deleting a monster(the IDs need to be changed).
   */
  List<Label> monsterIdLabels;
  /**
   * List of labels that are used to represent the IDs of rooms.
   *
   * Used when deleting an room(the IDs need to be changed).
   */
  List<Label> roomIdLabels;
  /**
   * Tab containing more tabs for editting items
   */
  Tab         itemsTab;
  /**
   * Tab containing more tabs for editting monsters
   */
  Tab         monstersTab;
  /**
   * Tab containing more tabs for editting rooms
   */
  Tab         roomsTab;

  /**
   * Set up the dungeon pane. Create basic things that are needed.
   */
  public DungeonPane() {
    super();
    this.setSide(Side.LEFT);

    dungeon = new Dungeon();
    itemIdLabels = new ArrayList<Label>();
    monsterIdLabels = new ArrayList<Label>();
    roomIdLabels = new ArrayList<Label>();

    Tab tabDungeon = new Tab();
    tabDungeon.setText("Dungeon");
    tabDungeon.setContent(createDungeonPane());
    this.getTabs().add(tabDungeon);

    itemsTab = new Tab();
    itemsTab.setText("Items");
    itemsTab.setContent(createItemPane(null));
    this.getTabs().add(itemsTab);

    monstersTab = new Tab();
    monstersTab.setText("Monsters");
    monstersTab.setContent(createMonsterPane(null));
    this.getTabs().add(monstersTab);

    roomsTab = new Tab();
    roomsTab.setText("Rooms");
    roomsTab.setContent(createRoomPane(null));
    this.getTabs().add(roomsTab);

  }

  /**
   * Create a Pane to store general information associated with the dungeon.
   *
   * Will also include buttons to save and load the dungeon.
   *
   * @return A Pane that will modify general dungeon information
   */
  private Node createDungeonPane() {

    // create main Nodes
    BorderPane pane = new BorderPane();
    ScrollPane scroll = new ScrollPane();
    VBox vbox = new VBox(2);
    HBox hbox1 = new HBox(9);
    HBox hbox2 = new HBox(15);

    // create sub nodes for hbox
    Label nameLabel = new Label("Name:");
    TextField nameField = new TextField();
    Label versionLabel = new Label("Version:");
    TextField versionField = new TextField();
    Button saveButton = new Button("Save");
    Button loadButton = new Button("Load");

    // create sub nodes for scroll
    VBox scrollpane = new VBox();
    Label desLabel = new Label("Description:");
    TextArea desArea = new TextArea();
    Label creditLabel = new Label("Credits:");
    TextArea creditArea = new TextArea();

    // set load/save button press actions
    saveButton.setOnAction(e -> dungeon.save());
    loadButton.setOnAction(e -> {
      dungeon.load(dungeon.getName());
      nameField.setText(dungeon.getName());
      versionField.setText(dungeon.getVersion());
      desArea.setText(dungeon.getDescription());
      creditArea.setText(dungeon.getCredits());

      itemsTab.setContent(createItemPane(dungeon.getItems()));

      monstersTab.setContent(createMonsterPane(dungeon.getMonsters()));

      roomsTab.setContent(createRoomPane(dungeon.getRooms()));
    });

    // change name
    nameField.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable,
          String oldValue, String newValue) {

        dungeon.setName(newValue);
      }
    });

    // change version
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

    // change description
    desArea.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable,
          String oldValue, String newValue) {

        dungeon.setDescription(newValue);
      }
    });

    // change credits
    creditArea.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable,
          String oldValue, String newValue) {

        dungeon.setCredits(newValue);
      }
    });

    // populate scroll
    scroll.setFitToWidth(true);
    scrollpane.getChildren().addAll(desLabel, desArea, creditLabel, creditArea);
    scroll.setContent(scrollpane);

    // populate main node
    pane.setTop(vbox);
    pane.setCenter(scroll);

    return pane;
  }

  /**
   * Create a Pane to store tabs representing items in the dungeon
   *
   * @param items
   *          Map of room IDs to Item objects that will be used for initial
   *          data.
   *
   *          If null, a single blank Item will be created and added.
   * @return A TabPane that will store tabs for each item in the dungeon
   */
  private Node createItemPane(Map<Integer, Item> items) {
    TabPane pane = new TabPane();

    if (items == null || items.size() == 0)
      addNewTab(pane, "", true, 0, null);
    else
      for (int i = 0; i < items.size(); i++)
        addNewTab(pane, "", true, 0, items.get(i));

    addNewTab(pane, "+", false, 0, null);
    setBehaviourForPlusTabClick(pane, 0);

    return pane;
  }

  /**
   * Create a Pane to store tabs representing monsters in the dungeon
   *
   * @param monsters
   *          Map of monster IDs to Monster objects that will be used for
   *          initial data.
   *
   *          If null, a single blank Monster will be created and added.
   * @return A TabPane that will store tabs for each monster in the dungeon
   */
  private Node createMonsterPane(Map<Integer, Monster> monsters) {
    TabPane pane = new TabPane();

    if (monsters == null || monsters.size() == 0)
      addNewTab(pane, "", true, 1, null);
    else
      for (int i = 0; i < monsters.size(); i++)
        addNewTab(pane, "", true, 1, monsters.get(i));

    addNewTab(pane, "+", false, 1, null);
    setBehaviourForPlusTabClick(pane, 1);

    return pane;
  }

  /**
   * Create a Pane to store tabs representing rooms in the dungeon
   *
   * @param rooms
   *          Map of room IDs to Room objects that will be used for initial
   *          data.
   *
   *          If null, a single blank Room will be created and added.
   * @return A TabPane that will store tabs for each room in the dungeon
   */
  private Node createRoomPane(Map<Integer, Room> rooms) {
    TabPane pane = new TabPane();

    if (rooms == null || rooms.size() == 0)
      addNewTab(pane, "", true, 2, null);
    else
      for (int i = 0; i < rooms.size(); i++)
        addNewTab(pane, "", true, 2, rooms.get(i));

    addNewTab(pane, "+", false, 2, null);
    setBehaviourForPlusTabClick(pane, 2);

    return pane;
  }

  /**
   * Create a new tab for a game object.
   *
   * Values for type:<br>
   * <ol start=0>
   * <li>item</li>
   * <li>monster</li>
   * <li>room</li>
   * </ol>
   *
   * @param tabPane
   *          TabPane to which to add new tab
   * @param newTabName
   *          tab title, should be null or "+"
   * @param isCloseable
   *          should be true unless this is the "+" tab
   * @param type
   *          what type of game object this tab represents
   * @param obj
   *          game object from which to load initial data.
   *
   *          if null, then a new game object of specified type will be created
   *          and added to the dungeon
   * @return a new Tab with specified properties
   */
  private Tab addNewTab(final TabPane tabPane, String newTabName,
      boolean isCloseable, int type, Object obj) {
    final Tab newTab;
    int id;
    if (newTabName.isEmpty()) {
      switch (type) {
        case 0:// Item
          id = dungeon.getItems().size();
          for (Map.Entry<Integer, Item> entry : dungeon.getItems().entrySet()) {
            if (Objects.equals(obj, entry.getValue())) {
              id = entry.getKey();
              break;
            }
          }
          newTabName = String.format("Item %d", id);
          newTab = new Tab(newTabName);
          newTab.setContent(getEmptyItemTabContent((Item) obj));
          newTab.setOnCloseRequest(e -> {
            int i = tabPane.getTabs().indexOf(newTab);
            Item tmp;
            itemIdLabels.remove(i);
            dungeon.getItems().remove(i);
            while (i < itemIdLabels.size()
                && (tmp = dungeon.getItems().get(i + 1)) != null) {
              tabPane.getTabs().get(i + 1).setText(String.format("Item %d", i));
              itemIdLabels.get(i).setText(String.format("ID: %d", i));
              dungeon.getItems().put(i++, tmp);
              dungeon.getItems().remove(i);

            }
          });
          break;
        case 1:// Monster
          id = dungeon.getMonsters().size();
          for (Map.Entry<Integer, Monster> entry : dungeon.getMonsters()
              .entrySet()) {
            if (Objects.equals(obj, entry.getValue())) {
              id = entry.getKey();
              break;
            }
          }
          newTabName = String.format("Monster %d", id);
          newTab = new Tab(newTabName);
          newTab.setContent(getEmptyMonsterTabContent((Monster) obj));
          newTab.setOnCloseRequest(e -> {
            int i = tabPane.getTabs().indexOf(newTab);
            Monster tmp;
            monsterIdLabels.remove(i);
            dungeon.getMonsters().remove(i);
            while (i < monsterIdLabels.size()
                && (tmp = dungeon.getMonsters().get(i + 1)) != null) {
              tabPane.getTabs().get(i + 1)
                  .setText(String.format("Monster %d", i));
              monsterIdLabels.get(i).setText(String.format("ID: %d", i));
              dungeon.getMonsters().put(i++, tmp);
              dungeon.getMonsters().remove(i);
            }
          });
          break;
        case 2:// Room
          id = dungeon.getRooms().size();
          for (Map.Entry<Integer, Room> entry : dungeon.getRooms().entrySet()) {
            if (Objects.equals(obj, entry.getValue())) {
              id = entry.getKey();
              break;
            }
          }
          newTabName = String.format("Room %d", id);
          newTab = new Tab(newTabName);
          newTab.setContent(getEmptyRoomTabContent((Room) obj));
          newTab.setOnCloseRequest(e -> {
            int i = tabPane.getTabs().indexOf(newTab);
            Room tmp;
            roomIdLabels.remove(i);
            dungeon.getRooms().remove(i);
            while (i < roomIdLabels.size()
                && (tmp = dungeon.getRooms().get(i + 1)) != null) {
              tabPane.getTabs().get(i + 1).setText(String.format("Room %d", i));
              roomIdLabels.get(i).setText(String.format("ID: %d", i));
              dungeon.getRooms().put(i++, tmp);
              dungeon.getRooms().remove(i);
            }
          });
          break;
        default:
          newTab = new Tab(newTabName);
          break;
      }
    }
    else {
      newTab = new Tab(newTabName);
    }
    newTab.setClosable(isCloseable);
    newTab.setOnClosed(new EventHandler<Event>() {
      @Override
      public void handle(Event event) {
        if (tabPane.getTabs().size() == 2) {
          event.consume();
        }
      }
    });

    tabPane.getTabs().add(newTab);
    return newTab;

  }

  /**
   * Set action for '+' tab to create a new tab on click, but not to focus
   *
   * Values for type:<br>
   * <ol start=0>
   * <li>item</li>
   * <li>monster</li>
   * <li>room</li>
   * </ol>
   *
   * @param tabPane
   *          the TabPane in which the '+' tab to change resides
   * @param type
   *          the type of tabs to create(item, monster, room)
   */
  private void setBehaviourForPlusTabClick(final TabPane tabPane, int type) {
    tabPane.getSelectionModel().selectedItemProperty()
        .addListener(new ChangeListener<Tab>() {
          @Override
          public void changed(ObservableValue<? extends Tab> observable,
              Tab oldTab, Tab newTab) {
            if (newTab.getText().equals("+")) {
              Tab addedTab = addNewTab(tabPane, "", true, type, null);
              tabPane.getTabs().remove(addedTab);
              tabPane.getTabs().set(tabPane.getTabs().size() - 1, addedTab);
              tabPane.getTabs().add(newTab);
              tabPane.getSelectionModel().select(addedTab);

            }
          }
        });
  }

  /**
   * Creates a tab for the GUI used to edit the item.
   *
   * Send null as tmp_item to create a new item.
   *
   * @param tmp_item
   *          item to use for tab creation(or null)
   * @return a javafx Node to be used as the content of the tab
   */
  private Node getEmptyItemTabContent(Item tmp_item) {
    final Item item;
    int id = dungeon.getItems().size();
    if (tmp_item == null) {
      item = new Item();
      dungeon.getItems().put(id, item);
    }
    else {
      item = tmp_item;
      for (Map.Entry<Integer, Item> entry : dungeon.getItems().entrySet()) {
        if (Objects.equals(item, entry.getValue())) {
          id = entry.getKey();
          break;
        }
      }
    }

    // create main Nodes
    BorderPane pane = new BorderPane();
    ScrollPane scroll = new ScrollPane();
    HBox hbox = new HBox(8);

    // TODO - use item values as defaults

    // create sub nodes for hbox
    Label idLabel = new Label(String.format("ID: %d", id));
    itemIdLabels.add(idLabel);
    Label nameLabel = new Label("Name:");
    TextField nameField = new TextField(item.getName());

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
    TextArea desArea = new TextArea(item.getDescription());

    // change description
    desArea.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable,
          String oldValue, String newValue) {

        item.setDescription(newValue);
      }
    });

    // populate scroll
    scroll.setFitToWidth(true);
    scrollpane.getChildren().addAll(desLabel, desArea);
    scroll.setContent(scrollpane);

    // populate main node
    pane.setTop(hbox);
    pane.setCenter(scroll);

    return pane;
  }

  /**
   * Creates a tab for the GUI used to edit the monster.
   *
   * Send null as tmp_monster to create a new monster.
   *
   * @param tmp_monster
   *          monster to use for tab creation(or null)
   * @return a javafx Node to be used as the content of the tab
   */
  private Node getEmptyMonsterTabContent(Monster tmp_monster) {
    final Monster monster;
    int id = dungeon.getMonsters().size();
    if (tmp_monster == null) {
      monster = new Monster();
      dungeon.getMonsters().put(id, monster);
    }
    else {
      monster = tmp_monster;
      for (Map.Entry<Integer, Monster> entry : dungeon.getMonsters()
          .entrySet()) {
        if (Objects.equals(monster, entry.getValue())) {
          id = entry.getKey();
          break;
        }
      }
    }

    // TODO - use item values as defaults

    // create main Nodes
    BorderPane pane = new BorderPane();
    ScrollPane scroll = new ScrollPane();
    HBox hbox = new HBox(8);

    // create sub nodes for hbox
    Label idLabel = new Label(String.format("ID: %d", id));
    monsterIdLabels.add(idLabel);
    Label nameLabel = new Label("Name:");
    TextField nameField = new TextField(monster.getName());

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
    TextArea desArea = new TextArea(monster.getDescription());

    // change description
    desArea.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable,
          String oldValue, String newValue) {

        monster.setDescription(newValue);
      }
    });

    // populate scroll
    scroll.setFitToWidth(true);
    scrollpane.getChildren().addAll(desLabel, desArea);
    scroll.setContent(scrollpane);

    // populate main node
    pane.setTop(hbox);
    pane.setCenter(scroll);

    return pane;
  }

  /**
   * Creates a tab for the GUI used to edit the room.
   *
   * Send null as tmp_room to create a new room.
   *
   * @param tmp_room
   *          room to use for tab creation(or null)
   * @return a javafx Node to be used as the content of the tab
   */
  private Node getEmptyRoomTabContent(Room tmp_room) {
    final Room room;
    int id = dungeon.getRooms().size();
    if (tmp_room == null) {
      room = new Room();
      dungeon.getRooms().put(id, room);
    }
    else {
      room = tmp_room;
      for (Map.Entry<Integer, Room> entry : dungeon.getRooms().entrySet()) {
        if (Objects.equals(room, entry.getValue())) {
          id = entry.getKey();
          break;
        }
      }
    }

    // create main Nodes
    BorderPane pane = new BorderPane();
    ScrollPane scroll = new ScrollPane();
    HBox hbox = new HBox(8);

    // TODO - use item values as defaults

    // create sub nodes for hbox
    Label idLabel = new Label(String.format("ID: %d", id));
    roomIdLabels.add(idLabel);

    // populate hbox
    hbox.getChildren().addAll(idLabel);

    // create sub nodes for scroll
    VBox scrollpane = new VBox();
    Label desLabel = new Label("Description:");
    TextArea desArea = new TextArea(room.getDescription());

    // change description
    desArea.textProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable,
          String oldValue, String newValue) {

        room.setDescription(newValue);
      }
    });

    HBox hconbox = new HBox();
    Button addConButton = new Button("Add Connection");
    Label conLabel = new Label("Connections:");
    TableView<Connection> conTable = new TableView<>();
    TableColumn<Connection, String> dirCol = new TableColumn<>("Direction");
    TableColumn<Connection, String> roomCol = new TableColumn<>("Room ID");

    ObservableList<Connection> conData = FXCollections.observableArrayList();

    for (String dir : room.getConnections().keySet()) {
      conData.add(new Connection(dir, room.getConnections().get(dir)));
    }

    dirCol.setMinWidth(100);
    dirCol.setCellValueFactory(
        new PropertyValueFactory<Connection, String>("dir"));
    dirCol.setCellFactory(TextFieldTableCell.forTableColumn());

    dirCol.setOnEditCommit((CellEditEvent<Connection, String> t) -> {
      String old = t.getOldValue();
      Connection con =
          t.getTableView().getItems().get(t.getTablePosition().getRow());
      con.setDir(t.getNewValue());
      room.getConnections().put(t.getNewValue(),
          room.getConnections().remove(old));
      if (t.getNewValue().isEmpty()) {
        conData.remove(con);
      }
    });

    roomCol.setMinWidth(100);
    roomCol.setCellValueFactory(
        new PropertyValueFactory<Connection, String>("id"));
    roomCol.setCellFactory(TextFieldTableCell.forTableColumn());
    roomCol.setOnEditCommit((CellEditEvent<Connection, String> t) -> {
      Connection con =
          t.getTableView().getItems().get(t.getTablePosition().getRow());
      if (t.getNewValue().matches("\\d+")) {
        con.setId(t.getNewValue());
        room.getConnections().replace(con.getDir(),
            Integer.valueOf(con.getId()));
      }
      else if (t.getNewValue().isEmpty()) {
        conData.remove(con);
        room.getConnections().remove(con.getDir());
      }
    });

    addConButton.setOnAction(e -> {
      if (room.getConnections().get("dir") == null) {
        conData.add(new Connection("dir", 0));
        room.getConnections().put("dir", 0);
      }
    });

    conTable.setItems(conData);
    conTable.setEditable(true);
    conTable.getColumns().add(dirCol);
    conTable.getColumns().add(roomCol);

    HBox idBox1 = new HBox(50);
    HBox idBox2 = new HBox(50);

    ObservableList<String> itemData = FXCollections.observableArrayList();
    Button addItemButton = new Button("Add Item");
    TableView<String> itemTable = new TableView<>();
    TableColumn<String, String> itemCol = new TableColumn<>("Items");
    // TODO - Maybe add name column for items and monsters

    ObservableList<String> monstData = FXCollections.observableArrayList();
    Button addMonstButton = new Button("Add Monster");
    TableView<String> monstTable = new TableView<>();
    TableColumn<String, String> monstCol = new TableColumn<>("Monsters");

    for (Integer item : room.getItems()) {
      itemData.add(String.valueOf(item));
    }
    for (Integer monster : room.getMonsters()) {
      monstData.add(String.valueOf(monster));
    }

    addItemButton.setOnAction(e -> {
      itemData.add("0");
      room.getItems().add(0);
    });

    addMonstButton.setOnAction(e -> {
      monstData.add("0");
      room.getMonsters().add(0);
    });

    itemCol.setMinWidth(30);
    itemCol.setCellValueFactory(
        new Callback<TableColumn.CellDataFeatures<String, String>, ObservableValue<String>>() {
          @Override
          public ObservableValue<String>
              call(TableColumn.CellDataFeatures<String, String> t) {
            return new SimpleStringProperty(t.getValue());
          }
        });
    itemCol.setCellFactory(TextFieldTableCell.forTableColumn());
    itemCol.setOnEditCommit((CellEditEvent<String, String> t) -> {
      int index = t.getTablePosition().getRow();
      if (t.getNewValue().matches("\\d+")) {
        room.getItems().set(index, Integer.valueOf(t.getNewValue()));
        itemData.set(index, t.getNewValue());
      }
      else if (t.getNewValue().isEmpty()) {
        itemData.remove(index);
        room.getItems().remove(index);
      }
    });

    monstCol.setMinWidth(30);
    monstCol.setCellValueFactory(
        new Callback<TableColumn.CellDataFeatures<String, String>, ObservableValue<String>>() {
          @Override
          public ObservableValue<String>
              call(TableColumn.CellDataFeatures<String, String> t) {
            return new SimpleStringProperty(t.getValue());
          }
        });
    monstCol.setCellFactory(TextFieldTableCell.forTableColumn());
    monstCol.setOnEditCommit((CellEditEvent<String, String> t) -> {
      int index = t.getTablePosition().getRow();
      if (t.getNewValue().matches("\\d+")) {
        room.getMonsters().remove(index);
        room.getMonsters().add(index, Integer.valueOf(t.getNewValue()));
        monstData.remove(index);
        monstData.add(index, t.getNewValue());
      }
      else if (t.getNewValue().isEmpty()) {
        monstData.remove(index);
        room.getMonsters().remove(index);
      }
    });

    idBox1.setAlignment(Pos.CENTER);
    idBox2.setAlignment(Pos.CENTER);
    idBox1.getChildren().addAll(addItemButton, addMonstButton);
    idBox2.getChildren().addAll(itemTable, monstTable);
    itemTable.setEditable(true);
    monstTable.setEditable(true);
    itemTable.setItems(itemData);
    monstTable.setItems(monstData);
    itemTable.getColumns().add(itemCol);
    monstTable.getColumns().add(monstCol);

    // populate scroll
    scroll.setFitToWidth(true);
    hconbox.getChildren().addAll(conLabel, addConButton);
    scrollpane.getChildren().addAll(desLabel, desArea, hconbox, conTable,
        idBox1, idBox2);
    scroll.setContent(scrollpane);

    // populate main node
    pane.setTop(hbox);
    pane.setCenter(scroll);

    return pane;
  }

  /**
   * Class used to simplify storage of room connections in the table view,
   * really just a struct.
   *
   * @author az
   *
   */
  public static class Connection {
    private String dir;
    private String id;

    /**
     * Create a connection from a direction and a room id
     *
     * @param dir
     *          direction of connection
     * @param id
     *          room id for connection
     */
    public Connection(String dir, int id) {
      this.dir = dir;
      this.id = String.valueOf(id);
    }

    /**
     * Get the direction for this connection
     *
     * @return the direction
     */
    public String getDir() {
      return dir;
    }

    /**
     * Set the direction for this connection
     *
     * @param dir
     *          the direction to set
     */
    public void setDir(String dir) {
      this.dir = dir;
    }

    /**
     * Get the id for this connection
     *
     * @return the id
     */
    public String getId() {
      return id;
    }

    /**
     * Set the id for this connection
     *
     * @param id
     *          the id to set
     */
    public void setId(String id) {
      this.id = id;
    }
  }
}
