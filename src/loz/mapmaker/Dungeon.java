package loz.mapmaker;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.lang3.StringEscapeUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class Dungeon {// TODO make rooms,items,and monsters maps?
                      // This would allow dropping rooms/items/monsters at
                      // points, using up
                      // less memory.
  /**
   * List of rooms composing this dungeon, list indices are the ids
   */
  private Map<Integer, Room>    rooms;
  /**
   * List of items found in this dungeon, list indices are the ids
   */
  private Map<Integer, Item>    items;
  /**
   * List of monsters found in this dungeon, list indices are the ids
   */
  private Map<Integer, Monster> monsters;
  /**
   * Name of this dungeon
   *
   * @see Dungeon#strFilename
   */
  private String                strName;
  /**
   * Filename (no extension)
   *
   * same as the name of the dungeon, but stripped of some characters
   *
   * @see Dungeon#strName
   */
  private String                strFilename;
  /**
   * Version of this Dungeon
   *
   * Probably for version control
   */
  private String                strVersion;
  /**
   * Description of the dungeon
   *
   * To appear at the beginning of the game
   */
  private String                strDescription;
  /**
   * End credits, thanks, and other things like that.
   */
  private String                strCredits;

  /**
   * Default constructor
   */
  public Dungeon() {
    this.rooms = new HashMap<Integer, Room>();
    this.items = new HashMap<Integer, Item>();
    this.monsters = new HashMap<Integer, Monster>();

    this.strName = "";
    this.strFilename = "";
    this.strVersion = "";
    this.strDescription = "";
    this.strCredits = "";
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
    try {

      SAXParserFactory factory = SAXParserFactory.newInstance();
      SAXParser saxParser = factory.newSAXParser();

      DefaultHandler handler = new DefaultHandler() {

        /**
         * Stores the xml element type that is currently being parsed.
         *
         * <p>
         * Uses bit manipulation to remember where it is:<br>
         * 0: Nothing<br>
         * 1: description<br>
         * 2: credits<br>
         * 4: item<br>
         * 8: monster<br>
         * 16: room<br>
         * </p>
         */
        int     nElementType = 0;
        int     id;
        Room    room;
        Monster monster;
        Item    item;

        @Override
        public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {

          if (attributes.getValue("id") != null)
            id = Integer.valueOf(attributes.getValue("id"));

          if (qName.equalsIgnoreCase("dungeon")) {
            Dungeon.this.setName(
                StringEscapeUtils.unescapeXml(attributes.getValue("name")));
            Dungeon.this.setVersion(
                StringEscapeUtils.unescapeXml(attributes.getValue("version")));
            Dungeon.this.getRooms().clear();
            Dungeon.this.getMonsters().clear();
            Dungeon.this.getItems().clear();
            nElementType = 0;
          }
          else if (qName.equalsIgnoreCase("description")) {
            nElementType |= 1;
          }
          else if (qName.equalsIgnoreCase("credits")) {
            nElementType |= 2;
          }
          else if (qName.equalsIgnoreCase("item")) {
            if ((nElementType & 16) == 0) {
              item = new Item();
              item.setName(
                  StringEscapeUtils.unescapeXml(attributes.getValue("name")));
              Dungeon.this.getItems().put(id, item);
            }
            nElementType |= 4;
          }
          else if (qName.equalsIgnoreCase("monster")) {
            if ((nElementType & 16) == 0) {
              monster = new Monster();
              monster.setName(
                  StringEscapeUtils.unescapeXml(attributes.getValue("name")));
              Dungeon.this.getMonsters().put(id, monster);
            }
            nElementType |= 8;
          }
          else if (qName.equalsIgnoreCase("room")) {
            room = new Room();
            Dungeon.this.getRooms().put(id, room);
            nElementType |= 16;
          }
          else if (qName.equalsIgnoreCase("connection")) {
            room.getConnections().put(
                StringEscapeUtils.unescapeXml(attributes.getValue("dir")), id);
          }

        }

        @Override
        public void endElement(String uri, String localName, String qName)
            throws SAXException {

          if (qName.equalsIgnoreCase("dungeon")) {
            nElementType = 0;
          }
          else if (qName.equalsIgnoreCase("description")) {
            nElementType &= ~1;
          }
          else if (qName.equalsIgnoreCase("credits")) {
            nElementType &= ~2;
          }
          else if (qName.equalsIgnoreCase("item")) {
            if ((nElementType & 16) > 0)
              room.getItems().add(id);
            nElementType &= ~4;
          }
          else if (qName.equalsIgnoreCase("monster")) {
            if ((nElementType & 16) > 0)
              room.getMonsters().add(id);
            nElementType &= ~8;
          }
          else if (qName.equalsIgnoreCase("room")) {
            nElementType &= ~16;
          }
          else if (qName.equalsIgnoreCase("connection")) {
            nElementType &= ~32;
          }

        }

        @Override
        public void characters(char ch[], int start, int length)
            throws SAXException {

          String str =
              StringEscapeUtils.unescapeXml(new String(ch, start, length));

          if ((nElementType & 1) > 0) {// Description
            switch (nElementType & 28) {
              case 0:
                Dungeon.this.setDescription(str);
                break;
              case 4:
                item.setDescription(str);
                break;
              case 8:
                monster.setDescription(str);
                break;
              case 16:
                room.setDescription(str);
                break;
            }
          }
          else if ((nElementType & 2) > 0) {// Credits
            Dungeon.this.setCredits(str);
          }

        }

      };

      saxParser.parse(strFilename, handler);

    }
    catch (Exception e) {
      e.printStackTrace();
    }
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
    try {
      RandomAccessFile raf = new RandomAccessFile(this.getName(), "r");

      int nItems = raf.readInt();
      int nMonsters = raf.readInt();
      int nRooms = raf.readInt();

      raf.seek(12 + 8 * (nItems + nMonsters + nRooms));

      this.setName(raf.readUTF());
      this.setVersion(raf.readUTF());
      this.setDescription(raf.readUTF());
      this.setCredits(raf.readUTF());

      for (int i = 0; i < nItems; i++) {
        raf.seek(12 + i * 8);
        raf.seek(raf.readLong());

        Item item = new Item();

        item.setName(raf.readUTF());
        item.setDescription(raf.readUTF());

        // TODO - other item properties load bin

        this.getItems().put(i, item);
      }

      for (int i = 0; i < nMonsters; i++) {
        raf.seek(12 + i * 8 + this.getItems().size() * 8);
        raf.seek(raf.readLong());

        Monster monster = new Monster();

        monster.setName(raf.readUTF());
        monster.setDescription(raf.readUTF());

        // TODO - other monster properties load bin

        this.getMonsters().put(i, monster);
      }

      for (int i = 0; i < nRooms; i++) {
        raf.seek(12 + i * 8 + nItems * 8 + nMonsters * 8);
        raf.seek(raf.readLong());

        Room room = new Room();

        room.setDescription(raf.readUTF());

        int nValue = raf.readInt();
        for (int j = 0; j < nValue; j++) {
          room.getConnections().put(raf.readUTF(), raf.readInt());
        }

        nValue = raf.readInt();
        for (int j = 0; j < nValue; j++) {
          room.getItems().add(raf.readInt());
        }

        nValue = raf.readInt();
        for (int j = 0; j < nValue; j++) {
          room.getMonsters().add(raf.readInt());
        }

        // TODO - other room properties load bin

        this.getRooms().put(i, room);
      }

      raf.close();
    }
    catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  // TODO - load single bin items

  /**
   * Save map as both a .map and temporary .xml file
   */
  public void save() {
    // save xml
    XMLOutputFactory factory = XMLOutputFactory.newInstance();
    try {
      XMLStreamWriter writer = factory
          .createXMLStreamWriter(new FileWriter(this.getFilename() + ".xml"));

      writer.writeStartDocument();
      writer.writeCharacters("\n");
      writer.writeStartElement("dungeon");
      writer.writeAttribute("name",
          StringEscapeUtils.escapeXml11(this.getName()));
      writer.writeAttribute("version",
          StringEscapeUtils.escapeXml11(this.getVersion()));
      writer.writeCharacters("\n  ");
      writer.writeStartElement("description");
      writer.writeCharacters(
          StringEscapeUtils.escapeXml11(this.getDescription()));
      writer.writeEndElement();
      writer.writeCharacters("\n  ");
      writer.writeStartElement("credits");
      writer.writeCharacters(StringEscapeUtils.escapeXml11(this.getCredits()));
      writer.writeEndElement();
      writer.writeCharacters("\n  ");
      writer.writeStartElement("items");
      for (int i : this.getItems().keySet()) {
        Item item = this.getItems().get(i);
        writer.writeCharacters("\n    ");
        writer.writeStartElement("item");
        writer.writeAttribute("id", String.valueOf(i));
        writer.writeAttribute("name",
            StringEscapeUtils.escapeXml11(item.getName()));
        writer.writeCharacters("\n      ");
        writer.writeStartElement("description");
        writer.writeCharacters(
            StringEscapeUtils.escapeXml11(item.getDescription()));
        writer.writeEndElement();
        // TODO - other item properties save xml
        writer.writeCharacters("\n    ");
        writer.writeEndElement();
      }
      writer.writeCharacters("\n  ");
      writer.writeEndElement();
      writer.writeCharacters("\n  ");
      writer.writeStartElement("monsters");
      for (int i : this.getMonsters().keySet()) {
        Monster monster = this.getMonsters().get(i);
        writer.writeCharacters("\n    ");
        writer.writeStartElement("monster");
        writer.writeAttribute("id", String.valueOf(i));
        writer.writeAttribute("name",
            StringEscapeUtils.escapeXml11(monster.getName()));
        writer.writeCharacters("\n      ");
        writer.writeStartElement("description");
        writer.writeCharacters(
            StringEscapeUtils.escapeXml11(monster.getDescription()));
        writer.writeEndElement();
        // TODO - other monster properties save xml
        writer.writeCharacters("\n    ");
        writer.writeEndElement();
      }
      writer.writeCharacters("\n  ");
      writer.writeEndElement();
      writer.writeCharacters("\n  ");
      writer.writeStartElement("rooms");
      for (int i : this.getRooms().keySet()) {
        Room room = this.getRooms().get(i);
        writer.writeCharacters("\n    ");
        writer.writeStartElement("room");
        writer.writeAttribute("id", String.valueOf(i));
        writer.writeCharacters("\n      ");
        writer.writeStartElement("description");
        writer.writeCharacters(
            StringEscapeUtils.escapeXml11(room.getDescription()));
        writer.writeEndElement();
        writer.writeCharacters("\n      ");
        writer.writeStartElement("connections");
        for (String dir : room.getConnections().keySet()) {
          writer.writeCharacters("\n        ");
          writer.writeStartElement("connection");
          writer.writeAttribute("dir", StringEscapeUtils.escapeXml11(dir));
          writer.writeAttribute("id",
              String.valueOf(room.getConnections().get(dir)));
          writer.writeEndElement();
        }
        writer.writeCharacters("\n      ");
        writer.writeEndElement();
        writer.writeCharacters("\n      ");
        writer.writeStartElement("items");
        for (int j : room.getItems()) {
          writer.writeCharacters("\n        ");
          writer.writeStartElement("item");
          writer.writeAttribute("id", String.valueOf(j));
          writer.writeEndElement();
        }
        writer.writeCharacters("\n      ");
        writer.writeEndElement();
        writer.writeCharacters("\n      ");
        writer.writeStartElement("monsters");
        for (int j : room.getMonsters()) {
          writer.writeCharacters("\n        ");
          writer.writeStartElement("monster");
          writer.writeAttribute("id", String.valueOf(j));
          writer.writeEndElement();
        }
        writer.writeCharacters("\n      ");
        writer.writeEndElement();
        // TODO - other room properties save xml
        writer.writeCharacters("\n    ");
        writer.writeEndElement();
      }
      writer.writeCharacters("\n  ");
      writer.writeEndElement();
      writer.writeCharacters("\n");
      writer.writeEndElement();
      writer.writeCharacters("\n");
      writer.writeEndDocument();

      writer.flush();
      writer.close();

    }
    catch (XMLStreamException e) {
      e.printStackTrace();
    }
    catch (IOException e) {
      e.printStackTrace();
    }

    // save bin
    try {
      RandomAccessFile raf =
          new RandomAccessFile(this.strFilename + ".map", "rw");

      raf.writeInt(this.getItems().size());
      raf.writeInt(this.getMonsters().size());
      raf.writeInt(this.getRooms().size());

      for (int i = 0; i < this.getRooms().size() + this.getMonsters().size()
          + this.getItems().size(); i++)
        raf.writeLong(0);

      raf.writeUTF(this.getName());
      raf.writeUTF(this.getVersion());
      raf.writeUTF(this.getDescription());
      raf.writeUTF(this.getCredits());

      for (int i = 0; i < this.getItems().size(); i++) {
        raf.seek(12 + i * 8);
        raf.writeLong(raf.length());
        raf.seek(raf.length());

        Item item = this.getItems().get(i);

        raf.writeUTF(item.getName());
        raf.writeUTF(item.getDescription());

        // TODO - other item properties save bin
      }

      for (int i = 0; i < this.getMonsters().size(); i++) {
        raf.seek(12 + i * 8 + this.getItems().size() * 8);
        raf.writeLong(raf.length());
        raf.seek(raf.length());

        Monster monster = this.getMonsters().get(i);

        raf.writeUTF(monster.getName());
        raf.writeUTF(monster.getDescription());

        // TODO - other monster properties save bin
      }

      for (int i = 0; i < this.getRooms().size(); i++) {
        raf.seek(12 + i * 8 + this.getItems().size() * 8
            + this.getMonsters().size() * 8);
        raf.writeLong(raf.length());
        raf.seek(raf.length());

        Room room = this.getRooms().get(i);

        raf.writeUTF(room.getDescription());

        raf.writeInt(room.getConnections().size());
        for (String dir : room.getConnections().keySet()) {
          raf.writeUTF(dir);
          raf.writeInt(room.getConnections().get(dir));
        }

        raf.writeInt(room.getItems().size());
        for (int id : room.getItems()) {
          raf.writeInt(id);
        }

        raf.writeInt(room.getMonsters().size());
        for (int id : room.getMonsters()) {
          raf.writeInt(id);
        }

        // TODO - other room properties save bin
      }

      raf.close();
    }
    catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Get list of rooms found in this dungeon
   *
   * @return list of rooms
   * @see Dungeon#rooms
   */
  public Map<Integer, Room> getRooms() {
    return this.rooms;
  }

  /**
   * Get list of items composing this dungeon
   *
   * @return list of items
   * @see Dungeon#items
   */
  public Map<Integer, Item> getItems() {
    return this.items;
  }

  /**
   * Get list of monsters found in this dungeon
   *
   * @return list of monsters
   * @see Dungeon#monsters
   */
  public Map<Integer, Monster> getMonsters() {
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
   * @param strVersion
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
   * @param strDescription
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
   * @param strCredits
   *          the credits to set
   * @see Dungeon#strCredits
   */
  public void setCredits(String strCredits) {
    this.strCredits = strCredits;
  }
}
