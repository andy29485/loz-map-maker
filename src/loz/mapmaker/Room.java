package loz.mapmaker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class string information of rooms in the dungeon and what they contain.
 *
 * @author az
 *
 */
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
   * List of IDs of monsters that reside in this room
   */
  private List<Integer>        monsters;
  /**
   * Additional info for the player, apart from things located in room
   *
   * Printed when entering room and looking around
   */
  private String               strDescription;

  /**
   * Default constructor, create empty list/map for the variables belonging to
   * this object
   */
  public Room() {
    this.connections = new HashMap<String, Integer>();
    this.items = new ArrayList<Integer>();
    this.monsters = new ArrayList<Integer>();
    this.strDescription = "";
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
   * Get the item IDs of items in this room
   *
   * @return list of item IDs
   * @see Room#items
   */
  public List<Integer> getItems() {
    return this.items;
  }

  /**
   * Get the monster IDs of monsters in this room
   *
   * @return list of monster IDs
   * @see Room#monsters
   */
  public List<Integer> getMonsters() {
    return this.monsters;
  }

  /**
   * Get the Description for this room
   *
   * @return the description
   * @see Room#strDescription
   */
  public String getDescription() {
    return strDescription;
  }

  /**
   * Set the Description for this room
   *
   * @param description
   *          the description to set
   * @see Room#strDescription
   */
  public void setDescription(String strDescription) {
    this.strDescription = strDescription;
  }
}
