
### Prison Documentation 
[Prison Documents - Table of Contents](prison_docs_000_toc.md)

## Prison - Examples of Prison's Log Entries

This document provides information on what log entries are generated when prison starts up for the first time, and while running the command `/ranks autoConfigure`.

NOTE:  Prison has been designed to behave the same under any version of bukkit/Spigot from 1.8 through 1.18.  Therefore the spigot version in these logs are not important, and apply to all versions.  The same applies for Java versions 1.8.x through Java 17.

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



