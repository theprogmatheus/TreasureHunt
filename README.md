# Treasure Hunt

![Untitled](https://file.notion.so/f/f/de0234d5-d071-422f-a297-f46a3323c726/63b2d06b-1368-439b-9787-753507cf2569/Untitled.png?table=block&id=22ebed77-a717-81b8-9c92-d3d821fc571e&spaceId=de0234d5-d071-422f-a297-f46a3323c726&expirationTimestamp=1754712000000&signature=kufEKjmYn-gWK5F-Dhvsleu-2g_qdL1hjpPrIOAxrjM&downloadName=Untitled.png)

**Plugin Name:** Treasure Hunt

**Description:** A plugin to manage custom treasure across the world. Administrators can place blocks that when clicked run a command for the player. Each treasure can only be clicked once for each player

**Allowed libraries**: Only one for connection pooling, nothing else is allowed.

**Commands:**

- /treasure create <id> <command> : Asks the player to click on a block to select as the treasure and saves it. When that treasure will be found the command will be executed from the console. %player% placeholder is allowed. **Example**: `/treasure create example say %player% found a treasure`
- /treasure delete <id> : Delete a treasure
- /treasure completed <id>: Returns the list of players who found that treasure
- /treasure list: Returns a list of treasures

**Additional - More points if you do it:**

- Make a gui to view and delete treasures

> All the data should be stored in a mysql database, the plugin should work in a multi-server environment. This means that if I claim the treasure on a server, it should still be claimed on other servers connected to the same database. (This only applies for claiming, for creation you can still require a server restart to sync)
>

**Deadline:** 7 days