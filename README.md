# Legend of Zelda Map Maker
Create/Edit dungeons form the Legend of Zelda Project

## Directions
### To save
1. Go to the dungeon tab
2. Make sure that the dungeon has a name specified
  - With at least *some*(not necessarily all) alphanumeric characters
3. Click the Save button
### To load
1. Put file(.xml or .map) into the same directory as the jar
2. Go to the the dungeon tab
3. Enter file name in the "Name:" text field, with the extension
  - e.g. "Test.xml" (without quotes)
4. Click the Load button

## TODO
- Starting items(in inventory)
- Player stats(health, mana, skills, descriptions, ...)


## Save format

### xml layout
```xml
<dungeon name="..." version="...">
  <description>...</description>
  <credits>...</credits>
  <items>
    <item id="..." name="...">
      <description>...</description>
      TODO
    </item>
  </items>
  <monsters>
    <monster id="..." name="...">
      <description>...</description>
      TODO
    </monster>
  </monsters>
  <rooms>
    <room id="...">
      <description>...</description>
      <connections>
        <connection direction="..." id="..." />
      </connections>
      <items>
        <item id="..." />
      </items>
      <monsters>
        <monster id="..." />
      </monsters>
    </room>
  </rooms>
</dungeon>
```

### map layout
- num items(int)
- num monsters(int)
- num rooms(int)
- list of longs, containing pointers to object locations(within the file)
  - pointers: number of bytes from beginning of file(use seek)
  - this is done by ids
    - item with id 0 will be the first long
  - 1 long per object
    - 3 items, 2 monsters, 8 rooms: 13 longs(104 bytes + 12 bytes for count)
- name of map(UTF str)
- version of map(UTF str)
- map description(UTF str)
- credits(UTF str)
- item
  - name of item(UTF str)
  - flavour text/description(UTF str)
  - TODO
- monsters
  - name of monster(UTF str)
  - description of monster(UTF str)
  - TODO
- rooms
  - description of room(UTF str)
  - connections
    - num connections(int)
    - list of connections
      - direction(UTF str)
      - room id(int)
  - items found
    - num items(int)
    - list of items
      - item id(int)
  - monsters found
    - num monsters(int)
    - list of monster
      - monster id(int)
