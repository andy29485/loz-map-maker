# Legend of Zelda Map Maker
Create/Edit dungeons form the Legend of Zelda Project

## TODO
- Starting items(in inventory)
- Player stats(health, mana, skills, descriptions, ...)


## Save format

### xml layout
TODO

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