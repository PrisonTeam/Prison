
### Prison Documentation 
[Prison Documents - Table of Contents](prison_docs_000_toc.md)

## Prison - Examples of Prison's Log Entries

This document provides information on what log entries are generated when prison starts up for the first time, and while running the command `/ranks autoConfigure`.

NOTE:  Prison has been designed to behave the same under any version of bukkit/Spigot from 1.8 through 1.18.  Therefore the spigot version in these logs are not important, and apply to all versions.  The same applies for Java versions 1.8.x through Java 17.

*Documented updated: 2021-12-11*

<hr style="height:5px; border:none; color:#aaf; background-color:#aaf;">

# Prison logs when starting for the first time

When the prison jar file has been added for the first time to a server, you will see something similar to these startup logs.


```
[13:14:48 INFO]: [Prison] Enabling Prison v3.2.11-alpha.11
[13:14:49 INFO]: | Prison |  -------------------- <  > ----------------------- (3.2.11-alpha.11)
[13:14:49 INFO]: | Prison |
[13:14:49 INFO]: | Prison |   _____      _
[13:14:49 INFO]: | Prison |  |  __ \    (_)
[13:14:49 INFO]: | Prison |  | |__) | __ _ ___  ___  _ __
[13:14:49 INFO]: | Prison |  |  ___/ '__| / __|/ _ \| '_ \
[13:14:49 INFO]: | Prison |  | |   | |  | \__ \ (_) | | | |
[13:14:49 INFO]: | Prison |  |_|   |_|  |_|___/\___/|_| |_|
[13:14:49 INFO]: | Prison |
[13:14:49 INFO]: | Prison |  Loading Prison version: 3.2.11-alpha.11
[13:14:49 INFO]: | Prison |  Running on platform: SpigotPlatform
[13:14:49 INFO]: | Prison |  Minecraft version: git-Spigot-21fe707-e1ebe52 (MC: 1.8.8)
[13:14:49 INFO]: | Prison |
[13:14:49 INFO]: | Prison |  Server runtime: 0s
[13:14:49 INFO]: | Prison |  Java Version: 1.8.0_291  Processor cores: 8
[13:14:49 INFO]: | Prison |  Memory Max: 3.556 GB  Total: 1.054 GB  Free: 674.204 MB  Used: 405.296 MB
[13:14:49 INFO]: | Prison |  Total Server Disk Space: 943.719 GB  Usable: 689.799 GB  Free: 689.799 GB  Used: 253.919 GB
[13:14:49 INFO]: | Prison |  Prison's File Count: 10  Folder Count: 5  Disk Space: 60.154 KB  Other Objects: 0
[13:14:49 INFO]: | Prison |  Prison TPS Average: 0.00  Min: 20.00  Max: 20.00   Interval: 10 ticks  Samples: 0
[13:14:49 INFO]: | Prison |
[13:14:49 INFO]: | Prison |  Enabling and starting...
[13:14:49 INFO]: | Prison |  Enabled Prison v3.2.11-alpha.11 in 376 milliseconds.
[13:14:49 INFO]: | Prison |  Using version adapter tech.mcprison.prison.spigot.compat.Spigot18
[13:14:49 INFO]: | Prison |  There were 21 new values added to the GuiConfig.yml file located at C:\mc_servers\spigot-1.8.8-basic_server\plugins\Prison\GuiConfig.yml
[13:14:49 INFO]: | Prison |  SpigotListener: Trying to register events
[13:14:49 INFO]: | Prison |  There were 41 new values added for the language files used by the SellAllConfig.yml file located at C:\mc_servers\spigot-1.8.8-basic_server\plugins\Prison\SellAllConfig.yml
[13:14:49 INFO]: | Prison |  There were 41 new values added for the language files used by the SellAllConfig.yml file located at C:\mc_servers\spigot-1.8.8-basic_server\plugins\Prison\SellAllConfig.yml
[13:14:49 INFO]: | Prison |  EssentialsEconomy is not directly enabled - Available as backup.
[13:14:49 INFO]: [PlaceholderAPI] Successfully registered expansion: prison
[13:14:49 INFO]: [PlaceholderAPI] Successfully registered expansion: PRISON
[13:14:49 INFO]: | Prison |  Mines Module enablement starting...
[13:14:49 INFO]: | Prison |  Mines Module enabled successfully in 271 milliseconds.
[13:14:49 INFO]: | Prison |  Ranks Module enablement starting...
[13:14:50 INFO]: | Prison |  Loaded 0 ranks.
[13:14:50 INFO]: | Prison |  Loaded 2 ladders.
[13:14:50 INFO]: | Prison |  Loaded 0 players.
[13:14:50 INFO]: | Prison |  Ranks by ladders:
[13:14:50 INFO]: | Prison |    default:
[13:14:50 INFO]: | Prison |    prestiges:
[13:14:50 INFO]: | Prison |    none:
[13:14:50 INFO]: | Prison |  Ranks Module enabled successfully in 241 milliseconds.
[13:14:50 INFO]: | Prison |  Utils Module enablement starting...
[13:14:50 INFO]: | Prison |  Utils Module enabled successfully in 15 milliseconds.
[13:14:50 INFO]: | Prison |  Loaded 0 mines and submitted with a 5000 millisecond offset timing for auto resets.
[13:14:50 INFO]: | Prison |  Total placeholders generated: 197
[13:14:50 INFO]: | Prison |    PLAYER: 102
[13:14:50 INFO]: | Prison |    LADDERS: 64
[13:14:50 INFO]: | Prison |    MINEPLAYERS: 31
[13:14:50 INFO]: | Prison |    ALIAS: 99
[13:14:50 INFO]: | Prison |  Total placeholders available to be Registered: 197
[13:14:50 INFO]: | Prison |  A total of 0 Mines and Ranks have been linked together.
[13:14:50 INFO]: | Prison |  Notice: AutoManager config file was just updated with 120 new entries. May need to be configured. File: autoFeaturesConfig.yml
[13:14:50 INFO]: | Prison |  Notice: AutoManager config file was just created. You must configure it to use it. File: autoFeaturesConfig.yml
[13:14:50 INFO]: | Prison |  AutoManager: AutoFeatures and the Mine module are enabled. Prison will register the selected block break listeners.
[13:14:50 INFO]: | Prison |  AutoManager: Trying to register BlockBreakEvent
[13:14:50 INFO]: | Prison |  ------------- < /prison version > --------------- (3.2.11-alpha.11)
[13:14:50 INFO]: | Prison |  Prison Version: 3.2.11-alpha.11
[13:14:50 INFO]: | Prison |  Running on Platform: tech.mcprison.prison.spigot.SpigotPlatform
[13:14:50 INFO]: | Prison |  Minecraft Version: git-Spigot-21fe707-e1ebe52 (MC: 1.8.8)
[13:14:50 INFO]: | Prison |
[13:14:50 INFO]: | Prison |  Server runtime: 1s
[13:14:50 INFO]: | Prison |  Java Version: 1.8.0_291  Processor cores: 8
[13:14:50 INFO]: | Prison |  Memory Max: 3.556 GB  Total: 1.054 GB  Free: 644.558 MB  Used: 434.942 MB
[13:14:50 INFO]: | Prison |  Total Server Disk Space: 943.719 GB  Usable: 689.799 GB  Free: 689.799 GB  Used: 253.919 GB
[13:14:50 INFO]: | Prison |  Prison's File Count: 28  Folder Count: 23  Disk Space: 174.540 KB  Other Objects: 0
[13:14:50 INFO]: | Prison |  Prison TPS Average: 0.00  Min: 20.00  Max: 20.00   Interval: 10 ticks  Samples: 0
[13:14:50 INFO]: | Prison |
[13:14:50 INFO]: | Prison |  Prison's root Command: /prison
[13:14:50 INFO]: | Prison |  Module: Mines : Enabled
[13:14:50 INFO]: | Prison |  Module: Ranks : Enabled
[13:14:50 INFO]: | Prison |  Module: Utils : Enabled
[13:14:50 INFO]: | Prison |
[13:14:50 INFO]: | Prison |  AutoManager Enabled: true
[13:14:50 INFO]: | Prison |  .   Apply Block Breaks through Sync Tasks: true
[13:14:50 INFO]: | Prison |  .   Cancel all Block Break Events: true
[13:14:50 INFO]: | Prison |  .   Cancel All Block Break Events Block Drops: false
[13:14:50 INFO]: | Prison |  .   'org.bukkit.BlockBreakEvent' Priority: LOW
[13:14:50 INFO]: | Prison |  .   Auto Pickup: true
[13:14:50 INFO]: | Prison |  .   Auto Smelt: true
[13:14:50 INFO]: | Prison |  .   Auto Block: true
[13:14:50 INFO]: | Prison |
[13:14:50 INFO]: | Prison |  .   Calculate Fortune: true
[13:14:50 INFO]: | Prison |  .  .  Extended Bukkit Fortune Enabled: true
[13:14:50 INFO]: | Prison |
[13:14:50 INFO]: | Prison |  .   Calculate XP: true
[13:14:50 INFO]: | Prison |  .   Drop XP as Orbs: false
[13:14:50 INFO]: | Prison |  .   Calculate Food Exhustion: true
[13:14:50 INFO]: | Prison |  .   Calculate Additional Items in Drop: true   (like flint in gravel)
[13:14:50 INFO]: | Prison |  Prestiges Enabled: true
[13:14:50 INFO]: | Prison |  .   Reset Money: true
[13:14:50 INFO]: | Prison |  .   Reset Default Ladder: true
[13:14:50 INFO]: | Prison |
[13:14:50 INFO]: | Prison |  GUI Enabled: true
[13:14:50 INFO]: | Prison |  Sellall Enabled: true
[13:14:50 INFO]: | Prison |  Backpacks Enabled: false
[13:14:50 INFO]: | Prison |
[13:14:50 INFO]: | Prison |  Integrations:
[13:14:50 INFO]: | Prison |  . . Permissions:  LuckPerms (Vault)
[13:14:50 INFO]: | Prison |  . . Economy:  Essentials Economy (Vault)
[13:14:50 INFO]: | Prison |  Integration Type: ECONOMY
[13:14:50 INFO]: | Prison |  . . Essentials Economy (Vault) <Active> [URL]
[13:14:50 INFO]: | Prison |  Integration Type: PERMISSION
[13:14:50 INFO]: | Prison |  . . LuckPerms (Vault) <Active>
[13:14:50 INFO]: | Prison |  . . LuckPerms (LuckPermsV5) <Active> [URL]
[13:14:50 INFO]: | Prison |  Integration Type: PLACEHOLDER
[13:14:50 INFO]: | Prison |  . . To list all or search for placeholders see: /prison placeholders
[13:14:50 INFO]: | Prison |  . . PlaceholderAPI <Active> [URL]
[13:14:50 INFO]: | Prison |
[13:14:50 INFO]: | Prison |  Registered Plugins:
[13:14:50 INFO]: | Prison |         CS-CoreLib (1.5.4 JavaSE_6)               Essentials (2.18.2.0 JavaSE_8)
[13:14:50 INFO]: | Prison |         EssentialsChat (2.18.2.0 JavaSE_8)        HolographicDisplays (2.4.1 JavaSE_8)
[13:14:50 INFO]: | Prison |         HolographicExtension (1.10.8 JavaSE_8)    LuckPerms (5.1.26 JavaSE_8)
[13:14:50 INFO]: | Prison |         PlaceholderAPI (2.10.10 JavaSE_8)         Prison (3.2.11-alpha.11 JavaSE_8)
[13:14:50 INFO]: | Prison |         ProtocolLib (4.5.0 JavaSE_8)              Vault (1.5.6-b49 JavaSE_6)
[13:14:50 INFO]: | Prison |         WorldEdit (6.1;no_git_id JavaSE_6)        WorldGuard (6.1 JavaSE_6)
[13:14:50 INFO]: | Prison |  Prison - Finished loading.
```


<hr style="height:5px; border:none; color:#aaf; background-color:#aaf;">


# Prison Information - Setting up a new Prison Server


When Prison detects that it has not ran on the server before, it will display the following message in the console, after waiting about 10 seconds for the other plugins to finish logging their startup information.


```
[13:14:55 INFO]: | Prison |  ----- < Setting up a new Prison Server > -------- (3.2.11-alpha.11)
[13:14:55 INFO]: | Prison |  Welcome to Prison!
[13:14:55 INFO]: | Prison |
[13:14:55 INFO]: | Prison |  To quickly get started, it is suggested to use the following command which will setup Ranks, Mines, link the Mines to the Ranks, setup automatic Access by Ranks, auto assign blocks to the generated Mines in increasing value,Enable the auto features (auto pickup, smelt, and block), setup the sellall's default shop pricing on about 95 items, etc...
[13:14:55 INFO]: | Prison |  . /ranks autoConfigure
[13:14:55 INFO]: | Prison |
[13:14:55 INFO]: | Prison |  For more information on what to do next, after running autoConfigure, check out this document:
[13:14:55 INFO]: | Prison |  .  https://prisonteam.github.io/Prison/prison_docs_100_setting_up_auto_configure.html
[13:14:55 INFO]: | Prison |
[13:14:55 INFO]: | Prison |
[13:14:55 INFO]: | Prison |  For more information on how to setup Prison, see our extensive documentation that is online:
[13:14:55 INFO]: | Prison |  .  https://prisonteam.github.io/Prison/prison_docs_000_toc.html
[13:14:55 INFO]: | Prison |
[13:14:55 INFO]: | Prison |  Information on suggested plugins can be found here:
[13:14:55 INFO]: | Prison |  .  https://prisonteam.github.io/Prison/prison_docs_012_setting_up_prison_basics.html
[13:14:55 INFO]: | Prison |
[13:14:55 INFO]: | Prison |  If you need help with setting up prison, please see our documentation.
[13:14:55 INFO]: | Prison |  If you find an issue with Prison, or need help for things not in the documentation, then please visit our discord server:
[13:14:55 INFO]: | Prison |
[13:14:55 INFO]: | Prison |
>

```

<hr style="height:5px; border:none; color:#aaf; background-color:#aaf;">


# Running command: /ranks autoConfigure help


```
>ranks autoConfigure help
[13:31:26 INFO]: ------- < Cmd: /ranks autoConfigure > --------- (3.2.11-alpha.11)
[13:31:26 INFO]: Auto configures Ranks and Mines using single letters A through Z for both the rank and mine names. Both ranks and mines are generated, they will also be linked together automatically. To set the starting price use price=x. To set multiplier mult=x. AutoConfigure will try to merge any preexsiting ranks and mines, but you must use the 'force' keyword in 'options'. Force will replace all blocks in preexisting mines. To keep preexisting blocks, use 'forceKeepBlocks' with the 'force' option. Default values [full price=50000 mult=1.5]
[13:31:26 INFO]: /ranks autoConfigure [options]
[13:31:26 INFO]: [options | full] Options: [full ranks mines price=x mult=x force forceKeepBlocks dontForceLinerWalls dontForceLinerBottoms]
[13:31:26 INFO]: Permissions:
[13:31:26 INFO]:    ranks.set
[13:31:26 INFO]: Aliases:
[13:31:26 INFO]:    [prison autoConfigure]
>
```


<hr style="height:5px; border:none; color:#aaf; background-color:#aaf;">


# Running command: /ranks autoConfigure


```
>ranks autoConfigure
[13:33:38 INFO]: Your new rank, 'A', was created in the ladder 'default', using the tag value of '[A]'
[13:33:38 INFO]: Virtual mine created: use command '/mines set area help' set an area within a world to enable as a normal mine.
[13:33:38 INFO]: Your new rank, 'B', was created in the ladder 'default', using the tag value of '[B]'
[13:33:38 INFO]: Virtual mine created: use command '/mines set area help' set an area within a world to enable as a normal mine.
[13:33:38 INFO]: Your new rank, 'C', was created in the ladder 'default', using the tag value of '[C]'
[13:33:38 INFO]: Virtual mine created: use command '/mines set area help' set an area within a world to enable as a normal mine.
[13:33:38 INFO]: Your new rank, 'D', was created in the ladder 'default', using the tag value of '[D]'
[13:33:38 INFO]: Virtual mine created: use command '/mines set area help' set an area within a world to enable as a normal mine.
[13:33:38 INFO]: Your new rank, 'E', was created in the ladder 'default', using the tag value of '[E]'
[13:33:38 INFO]: Virtual mine created: use command '/mines set area help' set an area within a world to enable as a normal mine.
[13:33:38 INFO]: Your new rank, 'F', was created in the ladder 'default', using the tag value of '[F]'
[13:33:38 INFO]: Virtual mine created: use command '/mines set area help' set an area within a world to enable as a normal mine.
[13:33:38 INFO]: Your new rank, 'G', was created in the ladder 'default', using the tag value of '[G]'
[13:33:38 INFO]: Virtual mine created: use command '/mines set area help' set an area within a world to enable as a normal mine.
[13:33:38 INFO]: Your new rank, 'H', was created in the ladder 'default', using the tag value of '[H]'
[13:33:38 INFO]: Virtual mine created: use command '/mines set area help' set an area within a world to enable as a normal mine.
[13:33:38 INFO]: Your new rank, 'I', was created in the ladder 'default', using the tag value of '[I]'
[13:33:38 INFO]: Virtual mine created: use command '/mines set area help' set an area within a world to enable as a normal mine.
[13:33:38 INFO]: Your new rank, 'J', was created in the ladder 'default', using the tag value of '[J]'
[13:33:38 INFO]: Virtual mine created: use command '/mines set area help' set an area within a world to enable as a normal mine.
[13:33:38 INFO]: Your new rank, 'K', was created in the ladder 'default', using the tag value of '[K]'
[13:33:38 INFO]: Virtual mine created: use command '/mines set area help' set an area within a world to enable as a normal mine.
[13:33:38 INFO]: Your new rank, 'L', was created in the ladder 'default', using the tag value of '[L]'
[13:33:38 INFO]: Virtual mine created: use command '/mines set area help' set an area within a world to enable as a normal mine.
[13:33:38 INFO]: Your new rank, 'M', was created in the ladder 'default', using the tag value of '[M]'
[13:33:39 INFO]: Virtual mine created: use command '/mines set area help' set an area within a world to enable as a normal mine.
[13:33:39 INFO]: Your new rank, 'N', was created in the ladder 'default', using the tag value of '[N]'
[13:33:39 INFO]: Virtual mine created: use command '/mines set area help' set an area within a world to enable as a normal mine.
[13:33:39 INFO]: Your new rank, 'O', was created in the ladder 'default', using the tag value of '[O]'
[13:33:39 INFO]: Virtual mine created: use command '/mines set area help' set an area within a world to enable as a normal mine.
[13:33:39 INFO]: Your new rank, 'P', was created in the ladder 'default', using the tag value of '[P]'
[13:33:39 INFO]: Virtual mine created: use command '/mines set area help' set an area within a world to enable as a normal mine.
[13:33:39 INFO]: Your new rank, 'Q', was created in the ladder 'default', using the tag value of '[Q]'
[13:33:39 INFO]: Virtual mine created: use command '/mines set area help' set an area within a world to enable as a normal mine.
[13:33:39 INFO]: Your new rank, 'R', was created in the ladder 'default', using the tag value of '[R]'
[13:33:39 INFO]: Virtual mine created: use command '/mines set area help' set an area within a world to enable as a normal mine.
[13:33:39 INFO]: Your new rank, 'S', was created in the ladder 'default', using the tag value of '[S]'
[13:33:39 INFO]: Virtual mine created: use command '/mines set area help' set an area within a world to enable as a normal mine.
[13:33:39 INFO]: Your new rank, 'T', was created in the ladder 'default', using the tag value of '[T]'
[13:33:39 INFO]: Virtual mine created: use command '/mines set area help' set an area within a world to enable as a normal mine.
[13:33:39 INFO]: Your new rank, 'U', was created in the ladder 'default', using the tag value of '[U]'
[13:33:39 INFO]: Virtual mine created: use command '/mines set area help' set an area within a world to enable as a normal mine.
[13:33:39 INFO]: Your new rank, 'V', was created in the ladder 'default', using the tag value of '[V]'
[13:33:39 INFO]: Virtual mine created: use command '/mines set area help' set an area within a world to enable as a normal mine.
[13:33:39 INFO]: Your new rank, 'W', was created in the ladder 'default', using the tag value of '[W]'
[13:33:39 INFO]: Virtual mine created: use command '/mines set area help' set an area within a world to enable as a normal mine.
[13:33:39 INFO]: Your new rank, 'X', was created in the ladder 'default', using the tag value of '[X]'
[13:33:39 INFO]: Virtual mine created: use command '/mines set area help' set an area within a world to enable as a normal mine.
[13:33:39 INFO]: Your new rank, 'Y', was created in the ladder 'default', using the tag value of '[Y]'
[13:33:39 INFO]: Virtual mine created: use command '/mines set area help' set an area within a world to enable as a normal mine.
[13:33:39 INFO]: Your new rank, 'Z', was created in the ladder 'default', using the tag value of '[Z]'
[13:33:39 INFO]: Virtual mine created: use command '/mines set area help' set an area within a world to enable as a normal mine.
[13:33:39 INFO]: Your new rank, 'P1', was created in the ladder 'prestiges', using the tag value of '[+]'
[13:33:39 INFO]: Your new rank, 'P2', was created in the ladder 'prestiges', using the tag value of '[+2]'
[13:33:39 INFO]: Your new rank, 'P3', was created in the ladder 'prestiges', using the tag value of '[+3]'
[13:33:39 INFO]: Your new rank, 'P4', was created in the ladder 'prestiges', using the tag value of '[+4]'
[13:33:39 INFO]: Your new rank, 'P5', was created in the ladder 'prestiges', using the tag value of '[+5]'
[13:33:39 INFO]: Your new rank, 'P6', was created in the ladder 'prestiges', using the tag value of '[+6]'
[13:33:39 INFO]: Your new rank, 'P7', was created in the ladder 'prestiges', using the tag value of '[+7]'
[13:33:39 INFO]: Your new rank, 'P8', was created in the ladder 'prestiges', using the tag value of '[+8]'
[13:33:39 INFO]: Your new rank, 'P9', was created in the ladder 'prestiges', using the tag value of '[+9]'
[13:33:39 INFO]: Your new rank, 'P10', was created in the ladder 'prestiges', using the tag value of '[+10]'
[13:33:40 INFO]: | Prison |  Mine A: [minecraft: andesite 5.0, minecraft: cobblestone 95.0]
[13:33:40 INFO]: | Prison |  Mine B: [minecraft: diorite 5.0, minecraft: andesite 10.0, minecraft: cobblestone 85.0]
[13:33:40 INFO]: | Prison |  Mine C: [minecraft: coal_ore 5.0, minecraft: diorite 10.0, minecraft: andesite 20.0, minecraft: cobblestone 65.0]
[13:33:40 INFO]: | Prison |  Mine D: [minecraft: granite 5.0, minecraft: coal_ore 10.0, minecraft: diorite 20.0, minecraft: andesite 20.0, minecraft: cobblestone 45.0]
[13:33:40 INFO]: | Prison |  Mine E: [minecraft: stone 5.0, minecraft: granite 10.0, minecraft: coal_ore 20.0, minecraft: diorite 20.0, minecraft: andesite 20.0, minecraft: cobblestone 25.0]
[13:33:40 INFO]: | Prison |  Mine F: [minecraft: iron_ore 5.0, minecraft: stone 10.0, minecraft: granite 20.0, minecraft: coal_ore 20.0, minecraft: diorite 20.0, minecraft: andesite 25.0]
[13:33:40 INFO]: | Prison |  Mine G: [minecraft: polished_andesite 5.0, minecraft: iron_ore 10.0, minecraft: stone 20.0, minecraft: granite 20.0, minecraft: coal_ore 20.0, minecraft: diorite 25.0]
[13:33:40 INFO]: | Prison |  Mine H: [minecraft: gold_ore 5.0, minecraft: polished_andesite 10.0, minecraft: iron_ore 20.0, minecraft: stone 20.0, minecraft: granite 20.0, minecraft: coal_ore 25.0]
[13:33:40 INFO]: | Prison |  Mine I: [minecraft: mossy_cobblestone 5.0, minecraft: gold_ore 10.0, minecraft: polished_andesite 20.0, minecraft: iron_ore 20.0, minecraft: stone 20.0, minecraft: granite 25.0]
[13:33:40 INFO]: | Prison |  Mine J: [minecraft: coal_block 5.0, minecraft: mossy_cobblestone 10.0, minecraft: gold_ore 20.0, minecraft: polished_andesite 20.0, minecraft: iron_ore 20.0, minecraft: stone 25.0]
[13:33:40 INFO]: | Prison |  Mine K: [minecraft: nether_quartz_ore 5.0, minecraft: coal_block 10.0, minecraft: mossy_cobblestone 20.0, minecraft: gold_ore 20.0, minecraft: polished_andesite 20.0, minecraft: iron_ore 25.0]
[13:33:40 INFO]: | Prison |  Mine L: [minecraft: lapis_ore 5.0, minecraft: nether_quartz_ore 10.0, minecraft: coal_block 20.0, minecraft: mossy_cobblestone 20.0, minecraft: gold_ore 20.0, minecraft: polished_andesite 25.0]
[13:33:40 INFO]: | Prison |  Mine M: [minecraft: end_stone 5.0, minecraft: lapis_ore 10.0, minecraft: nether_quartz_ore 20.0, minecraft: coal_block 20.0, minecraft: mossy_cobblestone 20.0, minecraft: gold_ore 25.0]
[13:33:40 INFO]: | Prison |  Mine N: [minecraft: iron_block 5.0, minecraft: end_stone 10.0, minecraft: lapis_ore 20.0, minecraft: nether_quartz_ore 20.0, minecraft: coal_block 20.0, minecraft: mossy_cobblestone 25.0]
[13:33:40 INFO]: | Prison |  Mine O: [minecraft: redstone_ore 5.0, minecraft: iron_block 10.0, minecraft: end_stone 20.0, minecraft: lapis_ore 20.0, minecraft: nether_quartz_ore 20.0, minecraft: coal_block 25.0]
[13:33:40 INFO]: | Prison |  Mine P: [minecraft: diamond_ore 5.0, minecraft: redstone_ore 10.0, minecraft: iron_block 20.0, minecraft: end_stone 20.0, minecraft: lapis_ore 20.0, minecraft: nether_quartz_ore 25.0]
[13:33:40 INFO]: | Prison |  Mine Q: [minecraft: quartz_block 5.0, minecraft: diamond_ore 10.0, minecraft: redstone_ore 20.0, minecraft: iron_block 20.0, minecraft: end_stone 20.0, minecraft: lapis_ore 25.0]
[13:33:40 INFO]: | Prison |  Mine R: [minecraft: emerald_ore 5.0, minecraft: quartz_block 10.0, minecraft: diamond_ore 20.0, minecraft: redstone_ore 20.0, minecraft: iron_block 20.0, minecraft: end_stone 25.0]
[13:33:40 INFO]: | Prison |  Mine S: [minecraft: gold_block 5.0, minecraft: emerald_ore 10.0, minecraft: quartz_block 20.0, minecraft: diamond_ore 20.0, minecraft: redstone_ore 20.0, minecraft: iron_block 25.0]
[13:33:40 INFO]: | Prison |  Mine T: [minecraft: prismarine 5.0, minecraft: gold_block 10.0, minecraft: emerald_ore 20.0, minecraft: quartz_block 20.0, minecraft: diamond_ore 20.0, minecraft: redstone_ore 25.0]
[13:33:40 INFO]: | Prison |  Mine U: [minecraft: lapis_block 5.0, minecraft: prismarine 10.0, minecraft: gold_block 20.0, minecraft: emerald_ore 20.0, minecraft: quartz_block 20.0, minecraft: diamond_ore 25.0]
[13:33:40 INFO]: | Prison |  Mine V: [minecraft: redstone_block 5.0, minecraft: lapis_block 10.0, minecraft: prismarine 20.0, minecraft: gold_block 20.0, minecraft: emerald_ore 20.0, minecraft: quartz_block 25.0]
[13:33:40 INFO]: | Prison |  Mine W: [minecraft: obsidian 5.0, minecraft: redstone_block 10.0, minecraft: lapis_block 20.0, minecraft: prismarine 20.0, minecraft: gold_block 20.0, minecraft: emerald_ore 25.0]
[13:33:40 INFO]: | Prison |  Mine X: [minecraft: diamond_block 5.0, minecraft: obsidian 10.0, minecraft: redstone_block 20.0, minecraft: lapis_block 20.0, minecraft: prismarine 20.0, minecraft: gold_block 25.0]
[13:33:40 INFO]: | Prison |  Mine Y: [minecraft: dark_prismarine 5.0, minecraft: diamond_block 10.0, minecraft: obsidian 20.0, minecraft: redstone_block 20.0, minecraft: lapis_block 20.0, minecraft: prismarine 25.0]
[13:33:40 INFO]: | Prison |  Mine Z: [minecraft: emerald_block 5.0, minecraft: dark_prismarine 10.0, minecraft: diamond_block 20.0, minecraft: obsidian 20.0, minecraft: redstone_block 20.0, minecraft: lapis_block 25.0]
[13:33:40 INFO]: | Prison |  Mine Liner status: A (Created) : walls:bricked:forced,bottom:bedrock:forced,ladderType:normal
[13:33:41 INFO]: | Prison |  Mine Liner status: B (Created) : walls:darkForest:forced,bottom:glowstone:forced,ladderType:normal
[13:33:41 INFO]: | Prison |  Mine Liner status: C (Created) : walls:blackAndWhite:forced,bottom:glowingPlanks:forced,ladderType:normal
[13:33:41 INFO]: | Prison |  Mine Liner status: D (Created) : walls:glowstone:forced,bottom:bedrock:forced,ladderType:normal
[13:33:41 INFO]: | Prison |  Mine Liner status: E (Created) : walls:obby:forced,bottom:glowstone:forced,ladderType:normal
[13:33:41 INFO]: | Prison |  Mine Liner status: F (Created) : walls:bricked:forced,bottom:bright:forced,ladderType:normal
[13:33:41 INFO]: | Prison |  Mine Liner status: G (Created) : walls:darkForest:forced,bottom:glowingPlanks:forced,ladderType:normal
[13:33:41 INFO]: | Prison |  Mine Liner status: H (Created) : walls:bedrock:forced,bottom:beacon:forced,ladderType:normal
[13:33:41 INFO]: | Prison |  Mine Liner status: I (Created) : walls:beacon:forced,bottom:glowstone:forced,ladderType:normal
[13:33:41 INFO]: | Prison |  Mine Liner status: J (Created) : walls:seaEchos:forced,bottom:bricked:forced,ladderType:normal
[13:33:41 INFO]: | Prison |  Mine Liner status: K (Created) : walls:blackAndWhite:forced,bottom:glowingPlanks:forced,ladderType:normal
[13:33:41 INFO]: | Prison |  Mine Liner status: L (Created) : walls:bricked:forced,bottom:bricked:forced,ladderType:normal
[13:33:41 INFO]: | Prison |  Mine Liner status: M (Created) : walls:glowingPlanks:forced,bottom:glowingPlanks:forced,ladderType:normal
[13:33:41 INFO]: | Prison |  Mine Liner status: N (Created) : walls:beacon:forced,bottom:blackAndWhite:forced,ladderType:normal
[13:33:41 INFO]: | Prison |  Mine Liner status: O (Created) : walls:darkOakPrismarine:forced,bottom:glowstone:forced,ladderType:normal
[13:33:41 INFO]: | Prison |  Mine Liner status: P (Created) : walls:bricked:forced,bottom:glowingPlanks:forced,ladderType:normal
[13:33:41 INFO]: | Prison |  Mine Liner status: Q (Created) : walls:bright:forced,bottom:beacon:forced,ladderType:normal
[13:33:41 INFO]: | Prison |  Mine Liner status: R (Created) : walls:glowstone:forced,bottom:obby:forced,ladderType:normal
[13:33:41 INFO]: | Prison |  Mine Liner status: S (Created) : walls:darkForest:forced,bottom:darkOakPrismarine:forced,ladderType:normal
[13:33:41 INFO]: | Prison |  Mine Liner status: T (Created) : walls:obby:forced,bottom:glowingPlanks:forced,ladderType:normal
[13:33:41 INFO]: | Prison |  Mine Liner status: U (Created) : walls:bricked:forced,bottom:obby:forced,ladderType:normal
[13:33:41 INFO]: | Prison |  Mine Liner status: V (Created) : walls:beacon:forced,bottom:darkForest:forced,ladderType:normal
[13:33:41 INFO]: | Prison |  Mine Liner status: W (Created) : walls:bricked:forced,bottom:obby:forced,ladderType:normal
[13:33:41 INFO]: | Prison |  Mine Liner status: X (Created) : walls:darkOakPrismarine:forced,bottom:beacon:forced,ladderType:normal
[13:33:41 INFO]: | Prison |  Mine Liner status: Y (Created) : walls:darkOakPrismarine:forced,bottom:seaEchos:forced,ladderType:normal
[13:33:41 INFO]: | Prison |  Mine Liner status: Z (Created) : walls:white:forced,bottom:darkForest:forced,ladderType:normal
[13:33:41 INFO]: The 'prestiges' ladder has been enabled to apply a Base Rank Cost Multiplier of 0.1000 that will be applied to 'all' rank costs.  This multiplier will be increased with each rank on the ladder.
[13:33:41 INFO]: The Base Rank Cost Multiplier can be adjusted, or disabled, with the command: '/ranks ladder rankCostMultiplier <ladderName> <rankCostMultiplier>
[13:33:41 INFO]: Ranks autoConfigure: 26 ranks were created.
[13:33:41 INFO]: Ranks autoConfigure: No rank commands were created.
[13:33:41 INFO]: | Prison |  Total placeholders generated: 1419
[13:33:41 INFO]: | Prison |    PLAYER: 102
[13:33:41 INFO]: | Prison |    LADDERS: 64
[13:33:41 INFO]: | Prison |    MINES: 832
[13:33:41 INFO]: | Prison |    MINEPLAYERS: 57
[13:33:41 INFO]: | Prison |    STATSMINES: 364
[13:33:41 INFO]: | Prison |    ALIAS: 697
[13:33:41 INFO]: | Prison |  Total placeholders available to be Registered: 1419
[13:33:41 INFO]: | Prison |  AutoManagerEventsManager: unregistered a total of 2 event listeners.
[13:33:41 INFO]: | Prison |  AutoManager: Trying to register BlockBreakEvent
[13:33:41 INFO]: | Prison |  Welcome! RoyalBlueRanger just joined the server and was assigned the default ranks.
[13:33:41 INFO]: | Prison |  Welcome! Myrqua just joined the server and was assigned the default ranks.
[13:33:41 INFO]: | Prison |  Welcome! RoyalCoffeeBeans just joined the server and was assigned the default ranks.
[13:33:41 INFO]: Ranks autoConfigure: 26 mines were created.
[13:33:41 INFO]: Ranks autoConfigure: 26 ranks and mines were linked.
[13:33:41 INFO]: Created 10 prestige ranks (temp message).
[13:33:41 INFO]: | Prison |
>


```

<hr style="height:5px; border:none; color:#aaf; background-color:#aaf;">





