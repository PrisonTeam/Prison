# Prison Documentation 

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">

*Documented updated: 2023-11-18*

## Project Related

* **[Prison README](prison_readme.md)** High level information, plus how to use with gradle builds.
* **[Prison License](prison_license.md)** GNU General Public License
* **[Prison Change logs](prison_changelogs.md)** Detailed changes to prison.


* **[Prison Discord Server](https://discord.gg/DCJ3j6r)** Get help here.


* **[Most Recent Version of Prison Documentation : Table of Contents](https://prisonteam.github.io/Prison/prison_docs_000_toc.html)** Docs change frequently, so the bleeding branch is tracked for Prison's online docs to keep them up to date.


## Build logs
 - **[v3.3.0-alpha - Current](changelog_v3.3.x.md)**
 
 - [v3.2.0 through v3.2.11](prison_changelogs.md)
 


 
<hr style="height:3px; border:none; color:#aaf; background-color:#aaf;">


# Prison Supports Spigot 1.8 through Spigot 1.20.x+
# Prison Supports Java 1.8 though Java 20+
# Prison is created for the Spigot Platform, and works on other platforms based upon Spigot


Prison supports Spigot 1.20.x, along with Java 1.8 through Java 20.  At this time there hasn't been any reports of incompatibilities.  Since prison is using a library to support the correct blocks for the version of the server that you are running, we are limited to when updates are released for that library.  Luckily they have had a couple of releases and we have applied them to the latest alpha releases.  So if you are wanting to maximize the new Spigot 1.20 experience, please upgrade to the latest alpha release as found on our discord server in the #alpha channel. 


### Newer features and updates in Prison:

* Prison's AutoFeatures was redesigned to support more Event Listener Priorities.  This means prison is more flexible and will work with more plugins, using them the way you want to.  Some of the new priorities are `ACCESS` so you can use Prison's Mine and Teleport Access by Rank without having to use Prison's block handlers.  This bypasses the need to configure WorldGuard regions for access to the mines that are linked to Prison Ranks.

* Now supports RevEnchants, both with Prison handling the block break events, and with RevEnchants handling the events.


* Prison Backups: Prison now has a new feature that will backup (zip) all of the files within it's plugin directory.  Prison now will perform an automated backup when it detects a change in Prison's version to help preserve settings from prior versions.  See the new [Prison Backup Document](prison_docs_050_Prison_backups.md).

* Auto Configure: Even if you really don't want to use auto configure when setting up your server, it may be worth trying it out just to see what it does.  If you're not happy with it, then deleting the `plugins/Prison/` directory will remove "everything" and on the next restart of your server, prison will load for the first time.  So if you are just getting started with prison, it's worth a try.


* Prison now has Access By Rank to reduce the number of Permissions needed! This simplifies a lot of settings and on a simple Prison server, can get you up and running much faster.

* Backpacks: It is advised not to use Prison's backpacks at this time.  They will be going through a rewrite and may result in content losses when upgrading when the newer version is available.  It is suggested you give MinePacks at try.

* Prison Mine Bombs!  Prison is starting to add mine bombs to the list of available features.  This is a work in progress and more enhancements and features will be added in the future.  The idea with these, is that you can configure almost every aspect of the mine bombs, and you can have as many different varieties as you want.

* Prison Tokens!  Prison is starting to implement the earning of tokens within the mines as the players mine.  This is a work in progress.  Currently the hooks are added to earn tokens, and for admins to manage them.  But more features need to be added to help enable using them.

* Prison Stats!  More stats are being tracked for each player.  This is a work in progress.  Prison is tracking blocks mined and even per mine.  Prison is tracking time spent mining in each mine, along with how much a player is earning per mine with both regular currency, and with tokens.  Top-n reports will be available shortly.  Rankup requirements will soon include the ability to specify blocks mined, time spent mining, and even tokens.  This will help you customize how you want your players to ranup.


### Features planned for the near future

These new features are in the planning stages...

* Block Converters:  Auto features can be controlled by enabling different blocks to perform different functions, if so desired. Currently the list of blocks is very limited and hard coded in both the configuration files, and also in the source code.  But Block Converters will remove all of that, and place the configs in a new json file.  The new format will provide a very powerful way to control all block conversions, such as smelting, blocking, and even drops.  It will support permissions on all blocks and items, so you could setup a smelting to provide multiple block types, or even provide different block and item types based upon perms.  Block Converters will take prison to the next level on mining customizations, but yet simplify auto features in both the auto features configs and code.

* New backpacks:  A rewrite of the backpacks that will give a little more flexibility.  You will be ble to use them as backpacks, or as vaults.  Could even sell, or trade backpacks/vaults with their contents.  ETA is unknown since a new storage management system needs to be created.

* Mine Effects:  The ability to set mine effects for a given mine, or to allow players to buy effects using their earned tokens.  Effects could be simple potion effects (haste, night vision), or even effects such as no fall damage, and even flight.  Other options could be no-pvp, enable pvp, no block break, no fire, etc... The options are numerous, but will be added a little at a time, and upon request.
 
 * Mine Regions:  Mine regions may be added to prison soon.  They would be "area" that will control the Mine Effects and a region would/should enclose a given mine.  But mine regions could be used on their own, were no mine is involved, such as at spawn to enble flight and no fall damage for your higher ranking players, or even allow players to "buy" regions to put around their bases so they can enable nigh vision and flight.

* Custom Menus: Simple custom menus could be added so admins can setup simple commands and features.  For example, custom token shop for enchantments or other in game items, or run any commands in general.  This will start off simple, but will expand upon requests.

* Unlimited Prestiges: This has been a long standing requested feature.  It's close to being added.  Although the levels may be unlimited, special configurations could be added for different levels, such as adding ladder commands at specific levels (ladder commands allows you to peform any action upon rankup, even from other plugins).

* Custom Shops: Custom shops will allow for an unlimited number of new shops to be created.  These shops can be tied to perms or player Ranks, or even specific mines.  Each shop could be either stand alone, or it could be based upon another shop with price modifies and new items to prevent the need to redefine all entries for each shop.  A mine shop would only be able to sell items that are retrived from mining in that mine.

* Cells: They have been requested many times in the past.  At this time, we cannot add them yet, but we would like to sometime in the distant future.

* Enchantments: In the past we have stated that Prison will never support enchantments for pickaxes, or other tools, or weapons and amour.  But as Prison is evolving and more features such as mine bombs and mine effects are added, along with natively supporting tokens within prison, the idea of adding enchantments is almost a no-brainer since most of the complicated details will already be supported through other features.  So in the distant future, some time after mine bombs and mine effects have matured, we may add our own Enchantments.



<hr style="height:7px; border:none; color:#aaf; background-color:#aaf;">

# New! Prison Fast Start

See the Auto Configure documentation for more information:
[Prison Auto Configure / Prison Quick Start!](prison_docs_100_setting_up_auto_configure.md)

Prison now has a new set of features that can help you get up and running faster than ever! With the latest version of Prison, you can even have a functional Prison server running with just two Prison commands.  See below for more information.


**It is strongly recommended that the '/ranks autoConfigure' should always be ran first.**  Prison's Auto Configure sets up so many features, that it can help resolve many initial issues.  It's worth trying the first time you run prison since it's easy to undo: just delete the `plugins/Prison' directory and the next time you restart your server Prison will startup as if it was just installed with no settings.


Before you try to setup Prison, you really need to install an Economy or the Ranks module will not be enabled.  It is strongly suggested you install the following plugins:  Vault, EssentialsX, EssentialsX-Chat, PlaceholderAPI, LuckPerms, WorldEdit (or Fast Async World Edit, FAWE, on newer versions of Spigot), WorldGuard.


`/ranks autoConfigure`. It can auto create your ranks and virtual mines, A through Z, it will link the mines to the ranks, setup the Mine Access By Rank and TP Access By Rank.  It will also setup the Mine as a Virtual Mine will and assign blocks of increasing values to all mines.  Each mine will also be assigned a random liner.  The Ranks autoConfigure will also enable sellall and load over 90 default blocks for your shop.  Auto features will be enabled (auto pickup, auto smelt, and auto blocking).  

Some of the newer features that are enabled with `/ranks autoConfigure` are: Ladder Base Rank Cost Multiplier (ranks cost more every time you prestige), auto configure 10 prestige levels, and improved placement of the mine. Based upon where you are standing, you can now define both the location of the mine, and the size.

Once it generates all the virtual mines, all you need to do is to use the command `/mines set area help` on all mines to make them physical mines and then prison will be ready to use.  Plus there are many new features to help provide the finishing touches in almost no time.
 
Auto configure can get you up and running with as little as two commands.  The first command is: `/ranks autoConfigure`.  Then the second command you run while you are in game, and it defines the mine for you in the world.  It's based upon where you are standing to make it "simple": `/mines set area A feet 10 6`. This last command tells prison to place the mine at your feet and to expand the walls outward by 10 blocks in all directions, and push the bottom of the mine down by 6 blocks.  The result will be a mine that is 21 x 21 x 7.


 - `/ranks autoConfigure`
 - `/mines set area a feet 10 6`
 
 At this point you have a 1 mine functional prison server.  Of course you will want to add more mines, but this is a quick overview of the basics on getting up and running.
 
 
 To see the blocks at this point, just reset the mine:
 
 - `/mines reset a`
 
 Then to protect the world so players cannot break your builds, you need to setup a global WorldGuard region for that world:
 
 - `/rg flag __global__ -w world passthrough deny`
 
 
 For more information, check out the following commands.
 
 - `/mines set area help`
 - `/mines set tracer help`
 - `/mines set size help`
 - `/mines set liner help`
 
 

 
 [Prison Auto Configure / Prison Quick Start Guide!](prison_docs_100_setting_up_auto_configure.md)


<hr style="height:5px; border:none; color:#aaf; background-color:#aaf;">

# A Quick Word About the Prison Command Handler

Prison has an advanced command handler that manages all of the commands. Programmatically, 
the commands are not setup the same as a normal bukkit command, but instead, there are a
lot of more powerful features available through the Prison Command Handler.

All commands will respond to the **help** keyword.  It will show a list of all of the parameters
for the command, any permissions tied to the command, and other details too.  If a document is
associated with the command, it can show a clickable link when used in-game (not many commands
have been linked to their docs yet).

The Prison Command Handler also manages aliases, auto complete (tab complete), organizing 
commands in a hierarchy so you can explore what commands are available by starting with the 
root commands.  For example, `/prison` is the core root command, and it will also show you 
a listing of all other root commands.  So `/prison` is a great place to begin exploring 
the commands that are available.

For the latest alpha releases, there is an exciting new command: `/prison support cmdStats`.
This new command will show you every prison command that was ran since the server was started,
along with how many times the command was used, and the average milliseconds it took Prison
to handle that command.  Some commands, such as `/mines reset` are submitted and ran
asynchronously and so their average run times will not be able to be reflected in that
command. The `cmdStats` does not track "command" usage if it bypasses the Prison
Command Handler, such as when a mine automatically resets since the internal calls bypass the 
Prison Command Handler.  

This is useful to give you an idea what commands your players and mods may be using.


<hr style="height:5px; border:none; color:#aaf; background-color:#aaf;">


# Table of Contents for this Document

* [Commands Shortcut Help](#commands-shortcut-help)

* [Guides - Setting up the Server Basics](#guides-setting-up-the-server-basics)
* [Guides - Configuring Prison](#guides-configuring-prison)

* [Guides - Working with other Plugins](#guides-working-with-other-plugins)
* [Guides - Configuring Other Plugins for Prison](#guides-configuring-other-plugins-for-prison)
* [Guides - FAQs](#guides-faqs)



<hr style="height:5px; border:none; color:#aaf; background-color:#aaf;">



### Commands Shortcut help

*Adding the `help` argument as the first parameter of any command will show additional help.*


**PRISON COMMANDS:** 

- [/prison](docs-commands/prison_docs_command_01_prison.md) `prison.admin` 
- [/prison alerts](docs-commands/prison_docs_command_02_prison_alerts.md) `prison.alerts`
- [/prison autofeatures](docs-commands/prison_docs_command_03_prison_autofeatures.md) `prison.admin` `prison.automanager` `prison.automanager.pickup` `prison.automanager.smelt` `prison.automanager.block` Plus custom permissions.
- [/prison gui](docs-commands/prison_docs_command_04_prison_gui.md) `prison.gui`
- [/prison modules](docs-commands/prison_docs_command_05_prison_modules.md) `prison.modules`
- [/prison placeholders](docs-commands/prison_docs_command_06_prison_placeholders.md) `prison.placeholder`
- [/prison reload](docs-commands/prison_docs_command_07_prison_reload.md) `prison.reload`
- [/prison version](docs-commands/prison_docs_command_09_prison_version.md) `prison.admin`


**RANKS COMMANDS**:

- [/ranks autoConfigure \[arg\] \[startPrice\] \[multiplier\] ](docs-commands/prison_docs_command_42_ranks_autoconfigure.md) `ranks.set`
- /ranks \[ladder\]  `ranks.admin`
- [/ranks command](docs-commands/prison_docs_command_12_ranks_command.md) `prison.alerts`
- [/ranks create \[rankName\] \[cost\] \[ladder\] \[tag\] ](docs-commands/prison_docs_command_13_ranks_create.md) `ranks.create`
- [/ranks delete \[rankName\] ](docs-commands/prison_docs_command_14_ranks_delete.md) `ranks.delete`
- [/ranks demote \[playerName\] \[ladder\] \[chargePlayers\] ](docs-commands/prison_docs_command_15_ranks_demote.md) `ranks.demote`
- [/ranks info \[rankName\] ](docs-commands/prison_docs_command_16_ranks_info.md) `ranks.info`
- [/ranks ladder](docs-commands/prison_docs_command_17_ranks_ladder_info.md) `ranks.admin`
- [/ranks list \[ladderName\] ](docs-commands/prison_docs_command_18_ranks_list.md) `ranks.list`
- [/ranks players \[ladderName\] \[action\] ](docs-commands/prison_docs_command_19_ranks_players.md) `ranks.admin`
- [/ranks player \[player\] ](docs-commands/prison_docs_command_20_ranks_player.md) `ranks.admin`
- [/ranks promote \[playerName\] \[ladder\] \[chargePlayers\] ](docs-commands/prison_docs_command_21_ranks_promote.md) `ranks.promote`
- [/ranks set](docs-commands/prison_docs_command_22_ranks_set.md) `ranks.admin`

**MINES COMMANDS: _Guidebook TO-DO_** (Work-In-Progress)

- /mines `mines.admin`
- /mines blockEvents
- /mines block `mines.admin`
- /mines command `mines.admin`
- /mines create \[mineName\]  `mines.create`
- /mines delete \[mineName\] \[confirm\]  `mines.delete`
- /mines info \[mineName\] \[page\]  `mines.info`
- /mines list \[page\]  `mines.list`
- /mines rename \[page\]  `mines.rename`
- /mines reset \[mineName\]  `mines.reset`
- /mines set  `mines.admin`
- /mines stats  `mines.stats`
- /mines tp \[mineName\]  `mines.tp` `mines.tp.[mineName]`
- /mines wand  `mines.wand`
- /mines whereami  `mines.whereami`

**MORE COMMANDS: _Guidebook TO-DO_** (Work-In-Progress)

- /sellall `prison.admin` `none for GUI`
- /prisonmanager `prison.admin for Admin GUI` `none for Players GUIs`
- /prestiges `none`
- /prestige `ranks.user` `ranks.rankup.prestiges`
- /rankupMax \[ladder\] `ranks.user` `ranks.rankupmax` `ranks.rankupmax.[ladderName]`
- /rankup \[ladder\] `ranks.user` `ranks.rankup.[ladderName]`
- /gui \[gui\]
- [/backpack](docs-commands/prison_docs_command_43_backpack.md)


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">






# Guides: Setting Up the Server Basics


* [Prison Backup Document](prison_docs_050_Prison_backups.md) Automatic and manual backups of `plugins/Prison/` directory.


* [Setting up a Spigot Server](prison_docs_010_setting_up_a_spigot_server.md)
    Setting up Java. Setting up and running BuildTool. Creating a runnable Spigot Server.


* [Setting up Prison - Basics](prison_docs_012_setting_up_prison_basics.md) **Required**
    Adding the plugin to your server.  **Other plugin dependency suggestions.**


* [Prison - Getting Help](prison_docs_013_Prison_Help.md) **Important**
    Commands. Getting help for issues. Upgrading Prison.


* [Prison - Prison Configuration Files](prison_docs_014_Prison_Configs.md) **Important**
    Customizing how Prison works by changing the Config files.




* [Setting up Vault](prison_docs_016_setting_up_Vault.md)
    Including Vault is Strongly Suggested.


* [Setting up EssentialsX](prison_docs_0xx_setting_up_EssentialsX.md) - 
    Including EssentialsX is Strongly Suggested - Configuring EssentialsX Chat placeholders - Recommend which modules to include.


* [Setting up LuckPerms](prison_docs_020_setting_up_luckperms.md)
    Setting up LuckPerms. 
* [Setting up LuckPerms Groups & Tracks](prison_docs_030_LuckPerms_Groups_Tracks.md)
    Using LuckPerms's groups and tracks with Prison.


* [Setting up PermissionsEX](prison_docs_022_setting_up_PermissionsEX.md)
    Setting up PermissionsEX. Warning about being obsolete.


* [Setting up Ultra Permissions](prison_docs_024_setting_up_Ultra_Permissions.md)
    Setting up Ultra Permissions. Compatible with Prison 3.2+


* [Setting up WorldGuard & WorldEdit](prison_docs_026_setting_up_worldguard_worldedit.md)
    Installing the plugins


* [Setting up CMI Economy](prison_docs_028_setting_up_CMI_economy.md)
    Enabling Prison's Delayed Startup so CMI Economy will work.  If CMI Economy fails to start before Prison starts, then the Prison Ranks will fail to load.


* [Prison's PlaceholderAPI wiki page content](prison_placeholderapi_wiki_docs.md)
    This document contains the Prison's content that is published on PlaceholderAPI's wiki.
    

* [Setting up PlaceholderAPI](prison_docs_0xx_setting_up_PlaceholderAPI.md) - Strongly Suggested if using placeholders. Troubleshooting.

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# Guides: Configuring Prison

These guides cover the core fundamentals of Prison.  They may reference other plugins, but these guides will not go in to depth on how to install or use the other plugins; those details will be a focused guide of their own. 


* [Prison Auto Configure / Prison Quick Start!](prison_docs_100_setting_up_auto_configure.md)
Get your prison setup quickly by running the command `/ranks autoCommand` which will configure most features within prison.


* [Setting up Mines](prison_docs_101_setting_up_mines.md)
	Basics of creating. Searching for Blocks. Customizing. One-block mine example with HolographicDisplays.



* [Mine Commands](prison_docs_111_mine_commands.md)
	What they are. Setting up. Working with Mine Commands.



* [BlockEvents](prison_docs_115_using_BlockEvents.md)
	**-+= New! Work In Progress! =+-** 
	Setting up BlockEvents and information about them.




* [Setting up Ranks & Ladders](prison_docs_102_setting_up_ranks.md)
	**-+= New! Work In Progress! =+-** Configuring and using Ranks and Ladders.



* [Setting up Prestiges](prison_docs_107_setting_up_prestiges.md)
	Configuring and using Prestiges.




* [Setting up SellAll](prison_docs_113_setting_up_sellall.md)	
    What's SellAll, how to use it and set up (Including sellall **multipliers**).



* [Setting up Mine Bombs](prison_docs_130_Prison_Mine_Bombs.md)	
    A work in progress: How to use and setup Prison Mine Bombs.



* [Setting up Backpacks](prison_docs_117_setting_up_backpacks.md)	
    How to enable backpacks.


* [Troubleshooting](prison_docs_900_troubleshooting.md) 
    Mining issues.
    

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">






# Guides: Working with other Plugins


* [Guide: Prison Placeholders](prison_docs_310_guide_placeholders.md) How to use. Includes HolographicDisplays.



* [Guide: Auto Manager - Setting Up and Enabling Other Plugins](prison_docs_311_guide_automanager.md) Enabling TokenEnchant and CrazyEnchant.


* [Guide: Prison APIs](prison_docs_318_prison_APIs.md) 
Prison's APIs and Events to help customize Prison.  Ideal for plugins using prison and to help customize how prison works on your server.




<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">




# Guides: Configuring Other Plugins for Prison

This section of guides will focus more on other plugins and how they can integrate with Prison.  They may use a few aspects of the prison API or command interface, but these guides will spend the majority of their time covering the other technologies that can help give Prison your own character.


* [Configuring and Using WorldGuard with LuckPerms to Protect Mines](prison_docs_626_configuring_worldguard_regions.md) 
    This guide uses LuckPerms in the examples, but any other permission plugin will work just as well.
    

* [Setting up EssentialsX Warps for Players](prison_docs_630_configuring_warps.md)


* [Ideas on Setting up Donor Mines and Private Mines](prison_docs_628_configuring_private_mines.md)








<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# Guides: FAQs

This section of guides will focus on FAQs; short helps for common problems.  


* [FAQ - Other Plugins](prison_docs_810_faq_other_plugins.md) 
	*No FAQs are available at this time*


* [FAQ - Miscellaneous Questions](prison_docs_880_faq_misc_01.md)
	Is Paper Supported? Setting the Currency symbol.

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">
