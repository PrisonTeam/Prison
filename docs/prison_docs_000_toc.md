# Prison Documentation 

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


## Project Related

* **[Prison README.md](README.md)**
* **[Prison LICENSE.md](LICENSE.md)**
* **[Prison Change logs](changelog_v3.2.x.md)** Detailed changes to prison.
* **[Prison Known Issues](knownissues_v3.2.x.md)** Known Issues and To Do's.
* **[Prison Discord Server](https://discord.gg/DCJ3j6r)** Get help here.


* **[Most Recent Version of Prison Documentation](https://github.com/PrisonTeam/Prison/blob/bleeding/docs/prison_docs_000_toc.md)** Docs change frequently, but GITHUG's master versions do not. These are the latest docs!


## Build logs
 - **[v3.2.5-alpha - Current](changelog_v3.2.x.md)**
 - **[v3.2.0 - 2019-12-03](prison_changelog_v3.2.0.md)**&nbsp;&nbsp;
**[v3.2.1 - 2020-09-27](prison_changelog_v3.2.1.md)**&nbsp;&nbsp;
**[v3.2.2 - 2020-11-21](prison_changelog_v3.2.2.md)**&nbsp;&nbsp;
**[v3.2.3 - 2020-12-25](prison_changelog_v3.2.3.md)**
 
 
<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">

# New! Prison Fast Start

Prison now has a new set of features that can help you get up and running faster than ever!  `/ranks autoConfigure`. It can auto create your ranks and virtual mines, A through Z, it will link the mines to the ranks, setup the basic rank commands to provide basic access permissions for your players, and assign blocks of increasing values to all mines.  All you need to do is to use the command `/mines set area` on all mines to make them physical mines.  Plus there are a new features to help provide the finishing touches in almost no time.   
 -  `/ranks autoConfigure`
 - `/mines set area help`
 - `/mines set tracer help`
 - `/mines set size help`
 - `/mines set liner help`
 
 Documentation pertaining to the use of the auto configuration will be coming soon.
 

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">

# Table of Contents for this Document

* [Commands Shortcut Help](#commands-shortcut-help)

* [Guides - Setting up the Server Basics](#guides-setting-up-the-server-basics)
* [Guides - Configuring Prison](#guides-configuring-prison)

* [Guides - Working with other Plugins](#guides-working-with-other-plugins)
* [Guides - Configuring Other Plugins for Prison](#guides-configuring-other-plugins-for-prison)
* [Guides - FAQs](#guides-faqs)

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



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
- [/ranks \[ladder\] ](docs-commands/prison_docs_command_11_ranks.md) `ranks.admin`
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

- [/mines](docs-commands/prison_docs_command_23_mines.md) `mines.admin`
- [/mines block](docs-commands/prison_docs_command_24_mines_block.md) `mines.admin`
- [/mines command](docs-commands/prison_docs_command_25_mines_command.md) `mines.admin`
- [/mines create \[mineName\] ](docs-commands/prison_docs_command_26_mines_create.md) `mines.create`
- [/mines delete \[mineName\] \[confirm\] ](docs-commands/prison_docs_command_27_mines_delete.md) `mines.delete`
- [/mines info \[mineName\] \[page\] ](docs-commands/prison_docs_command_28_mines_info.md) `mines.info`
- [/mines list \[page\] ](docs-commands/prison_docs_command_29_mines_list.md) `mines.list`
- [/mines rename \[page\] ](docs-commands/prison_docs_command_41_mines_rename.md) `mines.rename`
- [/mines reset \[mineName\] ](docs-commands/prison_docs_command_30_mines_reset.md) `mines.reset`
- [/mines set](docs-commands/prison_docs_command_31_mines_set.md) `mines.admin`
- [/mines stats](docs-commands/prison_docs_command_32_mines_stats.md) `mines.stats`
- [/mines tp \[mineName\] ](docs-commands/prison_docs_command_33_mines_tp.md) `mines.tp` `mines.tp.[mineName]`
- [/mines wand](docs-commands/prison_docs_command_34_mines_wand.md) `mines.wand`
- [/mines whereami](docs-commands/prison_docs_command_35_mines_whereami.md) `mines.whereami`

**MORE COMMANDS: _Guidebook TO-DO_** (Work-In-Progress)

- [/sellall](docs-commands/prison_docs_command_10_sellall.md) `prison.admin` `none for GUI`
- [/prisonmanager](docs-commands/prison_docs_command_36_prisonmanager.md) `prison.admin for Admin GUI` `none for Players GUIs`
- [/prestiges](docs-commands/prison_docs_command_37_prestiges.md) `none`
- [/prestige](docs-commands/prison_docs_command_38_prestige.md) `ranks.user` `ranks.rankup.prestiges`
- [/rankupMax \[ladder\]](docs-commands/prison_docs_command_39_rankupmax.md) `ranks.user` `ranks.rankupmax` `ranks.rankupmax.[ladderName]`
- [/rankup \[ladder\]](docs-commands/prison_docs_command_40_rankup.md) `ranks.user` `ranks.rankup.[ladderName]`
- /gui \[gui\]


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">






# Guides: Setting Up the Server Basics


* [Setting up a Spigot Server](prison_docs_010_setting_up_a_spigot_server.md)
    Setting up Java. Setting up and running BuildTool. Creating a runnable Spigot Server.


* [Setting up Prison - Basics](prison_docs_012_setting_up_prison_basics.md) **Required**
    Adding the plugin to your server.


* [Prison - Getting Help](prison_docs_013_Prison_Help.md) **Important**
    Commands. Getting help for issues. Upgrading Prison.


* [Prison - Prison Configuration Files](prison_docs_014_Prison_Configs.md) **Important**
    Customizing how Prison works by changing the Config files.




* [Setting up Vault](prison_docs_016_setting_up_Vault.md)
    Including Vault is Strongly Suggested.


* [Setting up EssentialsX](prison_docs_0xx_setting_up_EssentialsX.md) - 
    Including EssentialsX is Strongly Suggested - Configuring EssentialsX Chat placeholders - Recommend which modules to include.


* [Setting up LuckPerms](prison_docs_020_setting_up_luckperms.md)
    Setting up LuckPerms. Warning about LuckPerms Versions.


* [Setting up PermissionsEX](prison_docs_022_setting_up_PermissionsEX.md)
    Setting up PermissionsEX. Warning about being obsolete.


* [Setting up Ultra Permissions](prison_docs_024_setting_up_Ultra_Permissions.md)
    Setting up Ultra Permissions. Compatible with Prison 3.2+


* [Setting up WorldGuard & WorldEdit](prison_docs_026_setting_up_worldguard_worldedit.md)
    Installing the plugins


* [Setting up PlaceholderAPI](prison_docs_0xx_setting_up_PlaceholderAPI.md) - Strongly Suggested if using placeholders. Troubleshooting.

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# Guides: Configuring Prison

These guides cover the core fundamentals of Prison.  They may reference other plugins, but these guides will not go in to depth on how to install or use the other plugins; those details will be a focused guide of their own. 



* [Setting up Mines](prison_docs_101_setting_up_mines.md)
	Basics of creating. Searching for Blocks. Customizing. One-block mine example with HolographicDisplays.



* [Setting up Ranks & Ladders](prison_docs_102_setting_up_ranks.md)
	**-+= New! Work In Progress! =+-** Configuring and using Ranks and Ladders.



* [Setting up Prestiges](prison_docs_107_setting_up_prestiges.md)
	Configuring and using Prestiges.



* [Mine Commands](prison_docs_111_mine_commands.md)
	What they are. Setting up. Working with Mine Commands.



* [Setting up SellAll](prison_docs_113_setting_up_sellall.md)	
    What's SellAll, how to use it and set up (Including sellall **multipliers**).



<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">






# Guides: Working with other Plugins


* [Guide: Prison Placeholders](prison_docs_310_guide_placeholders.md) How to use. Includes HolographicDisplays.


**Work In Progress**



<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">




# Guides: Configuring Other Plugins for Prison

This section of guides will focus more on other plugins and how they can integrate with Prison.  They may use a few aspects of the prison API or command interface, but these guides will spend the majority of their time covering the other technologies that can help give Prison your own character.


* [Configuring and Using WorldGuard with LuckPerms to Protect Mines](prison_docs_626_configuring_worldguard_regions.md) 
    This guide uses LuckPerms in the examples, but any other permission plugin will work just as well.
    

* [Setting up EssentialsX Warps for Players](prison_docs_630_configuring_warps.md)


* [Ideas on Setting up Donor Mines and Private Mines](prison_docs_628_configuring_private_mines.md)



* **Work In Progress**






<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# Guides: FAQs

This section of guides will focus on FAQs; short helps for common problems.  


* [FAQ - Other Plugins](prison_docs_810_faq_other_plugins.md) 
	CMI Plugin. Prison fails to load. No modules.


* [FAQ - Miscellaneous Questions](prison_docs_880_faq_misc_01.md)
	Is Paper Supported? Setting the Currency symbol.

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">
