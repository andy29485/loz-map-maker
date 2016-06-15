import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Room {
  /**
   * Map of direction string to other room id
   */
  private Map<String, Integer> connections;
  /**
   * List of items that are located in this room
   */
  private List<Integer>        items;
  /**
   * List of ids of monsters that reside in this room
   */
  private List<Integer>        monsters;

  /**
   * Default constructor, create empty list/map for the variables belonging to
   * this object
   */
  public Room() {
    this.connections = new HashMap<String, Integer>();
    this.items = new ArrayList<Integer>();
    this.monsters = new ArrayList<Integer>();
  }

  /**
   * Get the connections to other rooms from this room
   * 
   * @return map of connections to other rooms from this room
   * @see Room#connections
   */
  public Map<String, Integer> getConnections() {
    return this.connections;
  }

  /**
   * Get the item ids of items in this room
   * 
   * @return list of item ids
   * @see Room#items
   */
  public List<Integer> getItems() {
    return this.items;
  }

  /**
   * Get the monster ids of monsters in this room
   * 
   * @return list of monster ids
   * @see Room#monsters
   */
  public List<Integer> getMonsters() {
    return this.monsters;
  }
}
