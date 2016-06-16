import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class Dungeon {// TODO make rooms,items,and monsters maps? This would
                      // allow dropping rooms/items/monsters at points, using up
                      // less memory.
  /**
   * List of rooms composing this dungeon, list indices are the ids
   */
  private List<Room>    rooms;
  /**
   * List of items found in this dungeon, list indices are the ids
   */
  private List<Item>    items;
  /**
   * List of monsters found in this dungeon, list indices are the ids
   */
  private List<Monster> monsters;
  /**
   * Name of this dungeon
   *
   * @see Dungeon#strFilename
   */
  private String        strName;
  /**
   * Filename (no extension)
   *
   * same as the name of the dungeon, but stripped of some characters
   *
   * @see Dungeon#strName
   */
  private String        strFilename;
  /**
   * Version of this Dungeon
   *
   * Probably for version control
   */
  private String        strVersion;
  /**
   * Description of the dungeon
   *
   * To appear at the beginning of the game
   */
  private String        strDescription;
  /**
   * End credits, thanks, and other things like that.
   */
  private String        strCredits;

  /**
   * Default constructor
   */
  public Dungeon() {
    this.rooms = new ArrayList<Room>();
    this.items = new ArrayList<Item>();
    this.monsters = new ArrayList<Monster>();
  }

  /**
   * loads map from file(.xml or .map)
   *
   * @param strFilename
   *          the file to load the Dungeon map from
   * @see Dungeon#loadBin
   * @see Dungeon#loadXml
   */
  public void load(String strFilename) {
    if (strFilename.endsWith(".xml"))
      this.loadXml(strFilename);
    else if (strFilename.endsWith(".map"))
      this.loadBin(strFilename);
  }

  /**
   * loads map from file(.xml)
   *
   * @param strFilename
   *          the file to load the Dungeon map from
   * @see Dungeon#load
   * @see Dungeon#loadBin
   */
  private void loadXml(String strFilename) {
    // TODO load xml
  }

  /**
   * loads map from file(.map)
   *
   * @param strFilename
   *          the file to load the Dungeon map from
   * @see Dungeon#load
   * @see Dungeon#loadXml
   */
  private void loadBin(String strFilename) {
    // TODO load map
  }

  /**
   * Save map as both a .map and temporary .xml file
   */
  public void save() {
    // TODO save as .xml

    try {
      RandomAccessFile raf =
          new RandomAccessFile(this.strFilename + ".map", "rw");
      // TODO save as .map
      raf.close();
    }
    catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * Get list of rooms found in this dungeon
   *
   * @return list of rooms
   * @see Dungeon#rooms
   */
  public List<Room> getRooms() {
    return this.rooms;
  }

  /**
   * Get list of items composing this dungeon
   *
   * @return list of items
   * @see Dungeon#items
   */
  public List<Item> getItems() {
    return this.items;
  }

  /**
   * Get list of monsters found in this dungeon
   *
   * @return list of monsters
   * @see Dungeon#monsters
   */
  public List<Monster> getMonsters() {
    return this.monsters;
  }

  /**
   * Get the filename
   *
   * @return the filename without the extension
   */
  public String getFilename() {
    return strFilename;
  }

  /**
   * Get the map name
   *
   * @return the name
   */
  public String getName() {
    return strName;
  }

  /**
   * Change the map name(and the file name as well)
   *
   * @param strName
   *          the name to set
   */
  public void setName(String strName) {
    this.strName = strName;
    this.strFilename =
        strName.replaceAll("[^\\w\\s_-]+", "").replaceAll("\\s+", "_");
  }

  /**
   * Get the Version for this dungeon
   *
   * @return the version
   * @see Dungeon#strVersion
   */
  public String getVersion() {
    return strVersion;
  }

  /**
   * Set the Version for this dungeon
   *
   * @param version
   *          the version to set
   * @see Dungeon#strVersion
   */
  public void setVersion(String strVersion) {
    this.strVersion = strVersion;
  }

  /**
   * Get the Description for this dungeon
   *
   * @return the description
   * @see Dungeon#strDescription
   */
  public String getDescription() {
    return strDescription;
  }

  /**
   * Set the Description for this dungeon
   *
   * @param description
   *          the description to set
   * @see Dungeon#strDescription
   */
  public void setDescription(String strDescription) {
    this.strDescription = strDescription;
  }

  /**
   * Get the Credits for this dungeon
   *
   * @return the credits
   * @see Dungeon#strCredits
   */
  public String getCredits() {
    return strCredits;
  }

  /**
   * Set the Credits for this dungeon
   *
   * @param credits
   *          the credits to set
   * @see Dungeon#strCredits
   */
  public void setCredits(String strCredits) {
    this.strCredits = strCredits;
  }
}
