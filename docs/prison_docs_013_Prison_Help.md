
### Prison Documentation 
[Prison Documents - Table of Contents](prison_docs_000_toc.md)

## Prison - Getting Help

This document provides some important information on how to find help in setting up your prison server, and ultimately, how and where to ask for help.

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">

# Overview

If you are having problem, please take a quick look at the following documents as found in the Table of Contents:

* Setting up prison and various plugins - If special conditions for their configurations become apparent in order for prison to work, notes will be added there.  If you notice there is a special configuration consideration that we did not document, please share with us so we can update the documents.
* Review topics that may address your issue
* At the end of the table of contents are some FAQs.  Special situations may be added to them.
* Known Issues & A TO DO List - This is also a TO DO list, but at the bottom are some known issues with prison.  

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


# General Information on Prison Commands

All of the commands for Prison can be ran in-game, and most can be ran from the console. When running commands from the console, you do not have to prefix the commands with a **/**.  But within these documents, all commands will be referenced with a prefixed **/** so it is clear that it's a command.  Just don't include it when running within the console.

Personal preference is to run the commands from the console since there are less restrictions on width or number of lines shown, and they are easier to see without the busy background of the game.

Within most of these documents, the console will be used to screen print from for those reasons.

The only commands that cannot be ran from the console are the commands that expect you to be an in-game player.  Examples are **/mines whereami** and **/mines tp <mineName>**.  But note, you can now run the TP command from the console if you supply the name of an online player:  **/mines tp <mineName> <playerName>**.

If you need to do some maintenance, or configurating of your Prison, the console could be an easier environment to use.

But in-game, some commands have clickable actions.  Such as page next or page prior.  Or even command completion, such as with **/mines block search** and clicking on a search result provides you with a filled in copy of **/mines block add** where all you need to do is just fill in the percentage since it uses the last mine name that has been used.


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


# Prison Commands and Using "help"

It may be helpful to know what commands are available, and what options exist for those commands.



**Note: the details below still apply, but are slightly out of date.**  The best way to start exploring all commands within prison is to start with `/prison` which will list all subcommands related to `/prison`, but also will show all other root commands and aliases.


Upon startup, prison lists many details about the environment, and one set of those details, are the base commands that are available within Prison.  See the area pertaining to the modules, since the commands generally are tied to modules.


<img src="images/prison_docs_013_Prison_Help_startup_5.png" alt="Prison Commands" title="Prison Commands" width="600" />


Many commands within prison are compound commands, such that they start with a base command, followed by one or more other commands.  When Prison lists the available commands, if there are sub commands, that information is included in the command listing, including the sub command count.  For example:

* **/mines**
* **/mines set**
* **/mines set spawn**

If for a given set of commands, such as **/mines set**, has sub commands, then Prison will present a listing of all sub commands and their parameters.  

<img src="images/prison_docs_013_Prison_Help_startup_3.png" alt="Prison Mines Commands" title="Prison Mines Commands" width="600" />

In this screen print, you can see the hierarchy of **/mines** and **/mines set**.  The parameter listing is helpful, but it does not provide all the information that is available.

Let's take a closer look at **/mines set notification** and how the **help** keyword can provide a lot more meaningful details.  If you just enter that command, without any parameters, Prison reports an error message, depending upon which parameter failed. See the next screen print and the related *Error* message.  It's not helpful in the least, especially if you are not certain what each parameter is supposed to be.


<img src="images/prison_docs_013_Prison_Help_startup_4.png" alt="Prison Mines Commands" title="Prison Mines Commands" width="600" />

But notice within the above screen print, the same command has been entered, but this time with the keyword **help** added as if it were the first parameter.  For example, **/mines set notification help**.  Prison recognizes that you are requesting help for that command, and then it displays all of the information it has for each parameter.


Prison automatically inserts the **help** keyword if you enter the base commands.  So **/mines** is really injecting the **help** keyword for you so it will generate the list.  It's like you've entered **/mines help**.



<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# Where to ask for help: The Prison Discord Server

The best place to ask for help, and to get answers quickly (within a few hours) would be without a doubt the Prison Discord Server.

[Prison Discord Server](https://discord.gg/DCJ3j6r)


You can also submit a help ticket on the Prison github Issues tab, but the response may be slower and less detailed


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# Asking for Help

Before you actually ask for help, take a look at some of the documents presented here.  You may find your answer, or at least become a little more familiar with Prison.  There is a good chance that if your question is a common one, then you will just be referred to this documentation anyway.

When you do ask for help, please realize if you can provide a clear description of the problems you are experiencing, plus the versions of Prison, Spigot, etc, then we can help you faster and more accurately.  To help provide you with answers to these questions, see the next section of this document for information on what you can copy and paste to provide all those much needed details.



<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


# Prison Debugger


Prison has been evolving to provide many new features, but as a result, it's also becoming very complex in some areas.  To help address these greater complexities, Prison has some debugging details that can be dynamically enabled.  These can help identify what's happening and provide hints as to what may be the trouble.


New targets will be added to Prison overtime.  So check with the command for the latests updates for the version that you are using.


To enable the debugger, you can toggle them all on with `/prison debug` once to turn it on, and again to turn them all off.

To review the options available, use the command `/prison debug help`.  There are also debug targets that only enable specific debug statements and the list of the available targets can be displayed with `/prison debug targets`.

```
>prison debug help
[21:07:00 INFO]: For internal use only. Do not use unless instructed.
[21:07:00 INFO]: /prison debug [targets]
[21:07:00 INFO]: [targets] Enable or disable a debugging target. Use 'targets' to list all available targets.  Use 'on' or 'off' to toggle on and off individual targets, or all targets if no target is specified.
[21:07:00 INFO]: Permissions:
[21:07:00 INFO]:    prison.debug
>prison debug targets
[21:08:21 INFO]: Global Debug Logging is enabled
[21:08:21 INFO]: . Valid Targets: all, on, off, blockBreak, blockBreakFortune, durability
```

It should be noted that every time you use the command, other than with the help keyword, it will always show the current status of the debugging information.  It will show if the global logging is enabled or not, and if any targets are enabled, it will list all of the active ones.  Plus it will show all of the available targets too.


The following shows toggling the global settings:

```
>prison debug
[21:12:10 INFO]: Global Debug Logging is enabled
[21:12:10 INFO]: . Valid Targets: all, on, off, blockBreak, blockBreakFortune, durability
>prison debug
[21:12:15 INFO]: Global Debug Logging is disabled
[21:12:15 INFO]: . Valid Targets: all, on, off, blockBreak, blockBreakFortune, durability
```

The following will show how you can toggle on a few features.  Without using the actions 'on' or 'off' it will toggle the specified targets.  The use of 'on' and 'off' can occur anywhere in the list of targets, and if both 'on' and 'off' are specified, then 'on' will take precedence over 'off'.


First notice two targets are activated with two different uses of the `/prison debug` command.

```
>prison debug blockBreak
[21:17:23 INFO]: Global Debug Logging is disabled
[21:17:23 INFO]: . Active Debug Targets:
[21:17:23 INFO]: . . Target: blockBreak
[21:17:23 INFO]: . Valid Targets: all, on, off, blockBreak, blockBreakFortune, durability
>prison debug blockBreakFortune
[21:17:30 INFO]: Global Debug Logging is disabled
[21:17:30 INFO]: . Active Debug Targets:
[21:17:30 INFO]: . . Target: blockBreak
[21:17:30 INFO]: . . Target: blockBreakFortune
[21:17:30 INFO]: . Valid Targets: all, on, off, blockBreak, blockBreakFortune, durability
```

Then using both of the same targets, they are toggled off.

```
>prison debug blockBreakFortune blockBreak
[21:17:42 INFO]: Global Debug Logging is disabled
[21:17:42 INFO]: . Valid Targets: all, on, off, blockBreak, blockBreakFortune, durability
```

Next, this is showing multiple targets being used, with the 'on' keyword mixed in.  Notice it lists all of these active targets.  Then `/prison debug` is used to globally turn on all features, which removes any individual targets. Then the global is used again to turn them all off.

```
>prison debug blockBreakFortune blockBreak on durability
[21:18:31 INFO]: Global Debug Logging is disabled
[21:18:31 INFO]: . Active Debug Targets:
[21:18:31 INFO]: . . Target: durability
[21:18:31 INFO]: . . Target: blockBreak
[21:18:31 INFO]: . . Target: blockBreakFortune
[21:18:31 INFO]: . Valid Targets: all, on, off, blockBreak, blockBreakFortune, durability
>prison debug
[21:18:38 INFO]: Global Debug Logging is enabled
[21:18:38 INFO]: . Valid Targets: all, on, off, blockBreak, blockBreakFortune, durability
>prison debug
[21:18:43 INFO]: Global Debug Logging is disabled
[21:18:43 INFO]: . Valid Targets: all, on, off, blockBreak, blockBreakFortune, durability
```



<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


# Prison v3.2.1 and it's Pre-Release Versions


The Prison startup screen contains a lot of information that can be used help identify issues that are detected by Prison on start up, and can provide useful information about the general environment.  See the screen prints below.


As prison evolves, the information provided within these commands will better reflect the current state of the plugin, and provide the information that is most helpful when contacting support.  For example, prison now includes information on the version of prison and spigot, but also memory information for your server since a lot of performance issues have been tied to the lack of having enough memory; now it's easy to access that information.


When the server starts, Prison's startup information will contain a lot of information, including any errors that may prevent prison from running correctly.  Also you can get information on demand with `/prison version`, or even more in greater detail with `/prison version all`.


If you are having problems and need to ask for help, please provide all of this information when requested. Screen prints are preferred.  When asked what versions of prison and platform that you are running, copying the following three lines is what is needed to answer that question:

```
>prison version all
[21:29:55 INFO]: ------------- < /prison version > ---------------
[21:29:55 INFO]: Prison Version: 3.3.0-alpha.4h
[21:29:55 INFO]: Running on Platform: tech.mcprison.prison.spigot.SpigotPlatform
[21:29:55 INFO]: Minecraft Version: git-Spigot-21fe707-e1ebe52 (MC: 1.8.8)
[21:29:55 INFO]:
[21:29:55 INFO]: Server runtime: 4h 26m 21s
[21:29:55 INFO]: Java Version: 1.8.0_291  Processor cores: 8
[21:29:55 INFO]: Memory Max: 3.556 GB  Total: 1.922 GB  Free: 1.459 GB  Used: 473.920 MB
[21:29:55 INFO]: Total Server Disk Space: 943.719 GB  Usable: 750.948 GB  Free: 750.948 GB  Used: 192.770 GB
[21:29:55 INFO]: Prison's File Count: 116  Folder Count: 24  Disk Space: 158.258 KB  Other Objects: 0
[21:29:55 INFO]:
[21:29:55 INFO]: Prison's root Command: /prison
[21:29:55 INFO]: Module: Mines : Enabled
[21:29:55 INFO]: Module: Ranks : Enabled
[21:29:55 INFO]: Module: Utils : Enabled
[21:29:55 INFO]:
[21:29:55 INFO]: AutoManager Enabled: true
[21:29:55 INFO]: .   Block Break Event Priority: LOW
[21:29:55 INFO]: .   Token Enchant BlockExplodeEvent Priority: LOW
[21:29:55 INFO]: .   Crazy Enchant BlastUseEvent Priority: LOW
[21:29:55 INFO]: .   Zenchantments BlockShredEvent Priority: LOW
[21:29:55 INFO]:
[21:29:55 INFO]: .   Auto Pickup: true
[21:29:55 INFO]: .   Auto Smelt: true
[21:29:55 INFO]: .   Auto Block: true
[21:29:55 INFO]: .   Handle Normal Drops: disabled by AutoPickup
[21:29:55 INFO]: .   Normal Drop Smelt: disabled
[21:29:55 INFO]: .   Normal Drop Block: disabled
[21:29:55 INFO]:
[21:29:55 INFO]: .   Calculate Durability: true
[21:29:55 INFO]: .   Calculate Fortune: true
[21:29:55 INFO]: .  .  Max Fortune Multiplier: 0
[21:29:55 INFO]: .  .  Extended Bukkit Fortune Enabled: true
[21:29:55 INFO]: .  .  Extended Bukkit Fortune Factor Percent Range Low: 40
[21:29:55 INFO]: .  .  Extended Bukkit Fortune Factor Percent Range High: 110
[21:29:55 INFO]: .  .  Calculate Alt Fortune Enabled: disabled
[21:29:55 INFO]: .  .  Calculate Alt Fortune on all Blocks: false
[21:29:55 INFO]:
[21:29:55 INFO]: .   Calculate XP: true
[21:29:55 INFO]: .   Drop XP as Orbs: true
[21:29:55 INFO]: .   Process TokensEnchant Explosive Events: true
[21:29:55 INFO]: .   Process Crazy Enchants Block Explode Events: true
[21:29:55 INFO]: .   Process McMMO BlockBreakEvents: true
[21:29:55 INFO]: Prestiges Enabled: true
[21:29:55 INFO]: .   Reset Money: true
[21:29:55 INFO]: .   Reset Default Ladder: true
[21:29:55 INFO]: GUI Enabled: true
[21:29:55 INFO]: Sellall Enabled: true
[21:29:55 INFO]: Backpacks Enabled: true
[21:29:55 INFO]:
[21:29:55 INFO]: Integrations:
[21:29:55 INFO]: . . Permissions:  LuckPerms (Vault)
[21:29:55 INFO]: . . Economy:  Essentials Economy (Vault)
[21:29:55 INFO]: Integration Type: ECONOMY
[21:29:55 INFO]: . . Essentials Economy (Vault) <Active> [URL]
[21:29:55 INFO]: . . Essentials (EssentialsX) (disabled) <Inactive> [URL]
[21:29:55 INFO]: . . SaneEconomy (API v0.15.0) <Inactive> [URL]
[21:29:55 INFO]: . . GemsEconomy <Inactive> [URL]
[21:29:55 INFO]: Integration Type: PERMISSION
[21:29:55 INFO]: . . LuckPerms (Vault) <Active>
[21:29:55 INFO]: . . LuckPerms (LuckPermsV5) <Active> [URL]
[21:29:55 INFO]: . . LuckPerms (LuckPerms-Legacy) <Inactive> [URL]
[21:29:55 INFO]: Integration Type: PLACEHOLDER
[21:29:55 INFO]: . . To list all or search for placeholders see: /prison placeholders
[21:29:55 INFO]: . . MVdWPlaceholderAPI <Inactive> [URL]
[21:29:55 INFO]: . . PlaceholderAPI <Active> [URL]
[21:29:55 INFO]:
[21:29:55 INFO]: Registered Plugins:
[21:29:55 INFO]: .  LuckPerms (5.1.26),   WorldEdit (6.1;no_git_id)
[21:29:55 INFO]: .  Vault (1.5.6-b49),   PlaceholderAPI (2.10.9)
[21:29:55 INFO]: .  ProtocolLib (4.5.0),   WorldGuard (6.1)
[21:29:55 INFO]: .  Essentials (2.18.1.0),   Scoreboard-revision (R3 1.4.2 RELEASE)
[21:29:55 INFO]: .  EssentialsChat (2.18.1.0),   Prison (3.3.0-alpha.4h)
```

The following are screen prints of an older version of what is shown at the server startup.


<img src="images/prison_docs_013_Prison_Help_startup_1.png" alt="Prison Startup Screen" title="Prison Startup Screen" width="600" />
<img src="images/prison_docs_013_Prison_Help_startup_2.png" alt="Prison Startup Screen" title="Prison Startup Screen" width="600" />


These screen prints may not contain the most recent enhancements to prison, since they are based upon an alpha release. Please see your server's console for details pertaining to your environment.


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# Prison v3.1.0 and Earlier - General Information

There is zero support available for these earlier versions of Prison.  It is therefore highly recommended that you upgrade to the latest release of Prison.  Once a version of prison is released, we cannot go back and apply any fixes; fixes will be applied to the next release.


My suggestions are based upon what I have seen within the code for Prison, and best guesses on how the older versions **may** have worked.  These are guesses.  Proceed carefully and make backups at each upgrade to ensure you can retry a step if something should go wrong.  - Blue


To upgrade prison, the process should be rather simple.  But there are general steps that you must follow to help ensure a smoother transition to the latest release.  


It is highly suggested that you should first backup your whole server, especially the data within your plugins folder.  Please be aware that upgrading Prison may require you to upgrade other pugins that you have, and those plugins may require other plugins to be upgraded.  


DO NOT just install the latest plugin(s) you find on the internet!  Most plugins have specific versions of Spigot/Bukkit/Minecraft that they will work with!  Get them only from trusted servers such as spigotmc.org, polymart.org, bukkit.org, or etc...  Those sites should have multiple versions and should identify what server engines they are compatible with.  


If you want to also upgrade to a newer version of the server software that you are running, such as spigot, bukkit, paper, etc..., first upgrade the plugins for your current version of the server.  So if you are running Spigot 1.8.8 and a plugin has a newer version available that works with 1.8.8, install that first and then start the server and make sure everything is working well.  Usually if there is a major change in a plugin from one version to the next, the "last" version may have code to "convert" your data to prepare for the next higher version. If there is an important intermediate step that will convert your data, or require you to make major changes, there should be some notes in documents somewhere.  Spending a few minutes reading the release notes on these websites could save you hours of work trying to recover from a messed up upgrade.  Remember to make backups!
   

<h3>Upgrading from Prison v3.2.0 to Prison v3.2.1</h3>

If you are running Prison v3.2.0, then upgrading to v3.2.1, or it's pre-release editions, you don't have to take any special precautions other than backing up your server and the plugin's data folders.  

The internal files remain the same between these two versions.  Version 3.2.1 has a lot of new additions to the internal file formats, but nothing will break if using v3.2.0 data with v3.2.1.  Matter of fact, if for some reason, you want to down grade from v3.2.1 to v3.2.0 you can.  Any new data elements from v3.2.1 will be lost if any of the data items are rewriting to the file system.  If a mine is not modified, as an example, then it will not write the mine data back to the file system.  


<h3>Upgrading from Prison v3.1.1 to Prison v3.2.1 or Prison v3.2.0</h3>

Upgrading from Prison v3.1.1 to either Prison v3.2.0 or Prison v3.2.1 requires no special procedures or processes.  The internal file formats are pretty close to being the same that it should work with no problems.  Just make sure you perform a backup of the server and the plugins data folders to provide that extra level of insurance and protection.


It is inadvisable to try to down grade to Prison v3.1.1 if running Prison v3.2.0 or later.  It may work, but there could possibly be potential internal failures due to the addition of the new fields.  If you try to do this, you assume all risks and no help will be provided.


<h3>Upgrading from v3.0.0 to Newer Versions of Prison</h3>

Honestly I cannot advise you on actual steps to take, since I do not know what file formats were used with v3.0.0.  I suspect they are compatible 100% with v3.1.1.  If this is true, you can just jump to Prison v3.2.1 with no problems.  

If there is an incompatibility, then its strongly suggested that you upgrade to v3.1.1 first and let it perform its own conversions, then shut down the server carefully.  If it is not shutdown cleanly, then the data files will not be saved to the file system. Once you update to v3.1.1 then you can update to v3.2.1 without any issues.




<h3>Upgrading from Earlier Versions Prior to v3.0.0</p> 
   
For prison releases prior to Prison v3.0.0, the data structures they uses to store all the data on the file system was different.  I do not know how it was different, I just know it was.  I saw there were remains of a conversion utility in Prison v3.0.0 that I **think** was able to convert Prison v2.x data to Prison v3.x formats.  If you are upgrading from Prison v2.x it is VERY IMPORTANT that you first upgrade to Prison v3.0.0!!  Once up start up that server, use the command **/prison** and confirm there is a command **/prison convert**, and if there is, run that command to convert your old data to the new Prison v3.0.0 format.  Make sure you take backups BEFORE and AFTER upgrading your data!  Also do a "clean" shutdown on prison v3.0.0 to finalize the changes.


**Please NOTE:** It needs to be understood that changes made to mines, ranks, ladders, or player data, in versions of Prison prior to v3.2.0 did NOT save those changes until the server shutdown.  So if the server should happen to crash, the changes could have been lost.  So this is important to understand, because if you are upgrading from an older version of prison to Prison v3.0.0, a version prior to Prison v3.2.0, then you MUST ensure the server shuts down cleanly or the conversions and/or changes to the prison data may not have been written to the server file system.  After you shut down the server, if performing an upgrade to v3.0.0, please review all the files under the plugins/Prison directory and make sure the last modify date reflects when you shut down the server.  If the files have not been updated, try restarting the server, and maybe make a change to mine or rank to force a save when the server shuts down.  I cannot help with this process other than these suggestions.  Sorry.


Once you are running Prison v3.x.x then you can safely upgrade to Prison v3.2.0, or better yet, to Prison v3.2.1 (or the pre-release edition).




<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


