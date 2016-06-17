package loz.mapmaker;

public class Monster extends GameCharacter {
  /**
   * Name of monster class.
   *
   * For in game listing/battles.
   */
  private String strName;
  /**
   * Flavour text for monster, cool description(backstory) of monster.
   */
  private String strDescription;

  /**
   * Default constructor. Set empty values to variables.
   */
  public Monster() {
    super();
    this.strName = "";
    this.strDescription = "";
  }

  /**
   * Get the description of the monster
   *
   * @see Monster#strDescription
   * @return the Description
   */
  public String getDescription() {
    return strDescription;
  }

  /**
   * Set the description for the monster
   *
   * @see Monster#strDescription
   * @param strDescription
   *          the description to set
   */
  public void setDescription(String strDescription) {
    this.strDescription = strDescription;
  }

  /**
   * Get the name of the monster
   *
   * @see Monster#strName
   * @return the name
   */
  public String getName() {
    return strName;
  }

  /**
   * Set the name for the monster
   *
   * @see Monster#strName
   * @param strName
   *          the name to set
   */
  public void setName(String strName) {
    this.strName = strName;
  }
}
