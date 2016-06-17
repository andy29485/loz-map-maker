package loz.mapmaker;

public class Item {
  /**
   * Name of item.
   *
   * For in game listing.
   */
  private String strName;
  /**
   * Flavour text for item, cool description/explanation of item.
   */
  private String strDescription;// TODO May need short description
  /**
   * Type of item, will affect how item is used. Hidden from player.
   * <p>
   * Values:
   * <ol start=0>
   * <li>Undefined</li>
   * <li>Consumable - short term stats effect/item disappears(health
   * potions/infusions</li>
   * <li>Equippable - long term status effect, when equipped(weapons/armour)
   * </li>
   * <li>Interactable - modifies room/stats, cannot be placed in
   * inventory(doors)</li>
   * <li>Tradable - do nothing but have value(coins/trophies)</li>
   * <li>Operational - can be used to do things(keys/monster feed)</li>
   * </ol>
   * </p>
   */
  private int    nType;

  /**
   * Default constructor. Set default values of undefined/empty for all
   * variables
   */
  public Item() {
    this.strName = "";
    this.setDescription("");
    this.setType(0);
  }

  /**
   * Use item as it should be used.
   *
   * Depends of the
   *
   * @param room
   * @param player
   * @return true if item is to disappear from room, consumable, and equippable
   *         do this
   */
  public boolean use(Room room, Player player) {
    switch (this.nType) {
      // TODO use item
    }
    return false;
  }

  /**
   * @return the strName
   */
  public String getName() {
    return strName;
  }

  /**
   * @param strName
   *          the strName to set
   */
  public void setName(String strName) {
    this.strName = strName;
  }

  /**
   * @return the strDescription
   */
  public String getDescription() {
    return strDescription;
  }

  /**
   * @param strDescription
   *          the strDescription to set
   */
  public void setDescription(String strDescription) {
    this.strDescription = strDescription;
  }

  /**
   * @return the nType
   */
  public int getType() {
    return nType;
  }

  /**
   * @param nType
   *          the nType to set
   */
  public void setType(int nType) {
    this.nType = nType;
  }

}
