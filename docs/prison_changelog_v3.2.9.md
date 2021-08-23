[Prison Documents - Table of Contents](prison_docs_000_toc.md)

## Prison Build Logs for v3.2.9 - 2021-07-03

## Build logs
 - **[v3.3.0-alpha - Current](changelog_v3.3.x.md)**
 - [v3.2.0 through v3.2.10](prison_changelogs.md)
 

 

Greetings!  I'm delighted that you are interested in the build logs for the
Prison plugin.  I'm wanting to provide a more formal documentation as to what 
is going on in each build so you have a better idea if it may be something 
that you need.


# Prison now fully supports Spigot 1.17 and Java 16 


# v3.2.9 2021-07-03


### v3.2.9 Release Highlights
v3.2.9 - Prison Update - Enhancements to blockEvents, placeholders, support, and events


* Prison now has new commands to help provide better support and can be found under:  /prison support  These commands will be expanded to help make it a lot easier to get help on many issues that could be configuration based.  Prison is now able to help sent some information to https://paste.helpch.at.  These tools also cleans up a lot junk in the log messages so they are easier to read.


* Added a new utility feature to prison: block decays.  This allows blocks to change types when they are mined.  Two defaults are an obby decay and rainbow decay.  Plus there is a blocks tool that will allow a custom list of blocks, as many, or as few, as you like. [https://youtu.be/y4vmeC0aPIc](https://youtu.be/y4vmeC0aPIc)


* Added 18 new placeholders that are focused on all ranks that are on the server.  Now if you use a rank name with these placeholders, you can access almost all rank information that is available, including how many players are at a specific rank, or a list of mines that are linked to a rank.


* Rewrote how event listeners are setup in Prison to optimize them. Created tools to inspect them to help with troubleshooting. Made significant improvements.
  `/prison debug blockBreakListeners`


* Enhanced Prison's delayed start to better integrate with CMI's Economy without sacrificing prison's advanced startup validation of existing ranks.


* Added many new placeholders for the use within the prison commands ladders, ranks, mines, and blockEvents.  Each type of these commands now has custom listings of all placeholders that will work in those areas.  This large expansive set of placeholders should be able to provide the greatest amount of flexibility in customizing your servers.  Use the following commands to review the latest listings:

```
/ranks ladder command add placeholders
{player} {player_uid} {msg} {broadcast} {inline} {inlinePlayer} {sync} {syncPlayer} {firstJoin} {balanceInitial} {balanceFinal} {currency} {rankupCost} {ladder} {rank} {rankTag} {targetRank} {targetRankTag}

/ranks command add placeholders
{player} {player_uid} {msg} {broadcast} {inline} {inlinePlayer} {sync} {syncPlayer} {firstJoin} {balanceInitial} {balanceFinal} {currency} {rankupCost} {ladder} {rank} {rankTag} {targetRank} {targetRankTag}

/mines command add placeholders
{player} {player_uid} {msg} {broadcast} {inline} {inlinePlayer} {sync} {syncPlayer}

/mines blockEvent add placeholders
{player} {player_uid} {msg} {broadcast} {inline} {inlinePlayer} {sync} {syncPlayer} {blockName} {mineName} {locationWorld} {locationX} {locationY} {locationZ} {coordinates} {worldCoordinates} {blockCoordinates} {blockChance} {blockIsAir} {blocksPlaced} {blockRemaining} {blocksMinedTotal} {mineBlocksRemaining} {mineBlocksRemainingPercent} {mineBlocksTotalMined} {mineBlocksSize} {blockMinedName} {blockMinedNameFormal} {blockMinedBlockType} {eventType} {eventTriggered} {utilsDecay}

```

* Upgraded the behavior of all command tools within prison to use row numbers when workig with commands, instead of having to reenter all of the commands.


* Added block filtering to BlockEvents so events can now be filtered by blocks now.


* /ranks autoConfigure has been enhanced to provide more functionality when forcing auto configure to run.  This will now try to fully incorporate preexisting ranks and mines as much as possible.  Preexisting mine liners will not be replaced, but block listings for the mines will be replaced with the defaults.


* When starting up prison when no mines and no ranks exist, prison will now print a few messages to the console to help direct new admins on how to configure prison.


* GUIs are being rewritten to improve performance and improve the behavior. 


* SELLALL has had some significant performance improvements.






- - - - - - - - - - - - - 


* **Hooked up the new RANKS placeholders.**


* **Preliminary work on 18 new placeholders that focus on ranks.**  These are non-functional at this time.


* **Found the wrong message signature was being used... it was for rank ids.**


* **Utils decay made some adjustments and changes.**
decayBlocks list: The use of pipe was conflicting with the saving and loading of the mines data so was chanted to commas.


* **Fixed a divide by zero error... utils decay**


* **Enhanced the PrisonBlock.getBlockTypesByName to search the custom blocks if not found initially.**
It will then try prefixing the name with the custom block type ids. Provided a hook for air.


* **v3.2.9-alpha.6 2021-07-03**


* **Added custom decay blocks list so custom decays are possible.**
Reworked the rainbow decay to use the custom block decay since they are so similar.


* **Added support for a rainbow decay.  It cycles through 10 block colors.**


* **More changes to get decays working.**
When used and hooked up, the decay obby feature will cause mined block will turn to turn to obby for a given amount of time before it is changed to a specified block of choice.


* **Fixed an issue with the {firstjoin} placeholder for rank related commands; it was not removing the placeholder correctly, but was injecting itself back in to the command.**


* **Change the way block events are handled.**
Instead of just passing the block name, the block event now gets the block that was actually broke, plus the block that was originally set.  
New placeholders were added for block events and even for all placeholders.  This greatly expands the functionality of the placeholders.


* **SpigotAutoFeaturesGUI was rewritten using the new GUI utility.**


* **v3.2.9-alpha.4 2021-07-02**


* **Check for null on the location when cloning a prison block.**


* **Start to hook up the prison decay utility.  Its disabled.**


* **setup a few things to extend the reporting of the block's name along with it's coordinates if they are set.**
Added Location to the PrisonBlock so it can be used to identify a specific block.


* **Created a new BlockUtils class that can be used with utilities and the like.**
 Currently its managing the unbreakableBlockList and has been hooked up to the auto manager to prevent the breaking of those blocks.


* **Fixes some issues with cleaning up the text thats being sent.**
It removes color codes and strips out all utf-8 color codes that fail to translate properly so the resulting text is clean.
Also injects the submission size in to the payload without altering the content size.
Added the ability to send the current server log.  But cuts off at a max size of 400,000 characters.


* **Added a requirement to provide a support name that will be used to tag all pastes.**
This will help identify who the paste belongs to.


* **Set defaults for Access by Rank to be off.**
With it being enabled, it was causing issues when no rank was hooked up to the mine.


* **Hook prison up to paste.helpch.at** 
with the current support providing logging of `/prison version all`.


* **Improving SellAll config usage performance:** 
SellAll config is now cached and loaded/updated only when necessary, this was causing a ton of unnecessary load for each PlayerInteractAction, especially Prison Listeners class.


* **v3.2.9-alpha.4 2021-06-30**


* **Add support for sending /prison version to a discord channel to make it easier to get detail information from users**
This is a work in progress. For now it has a testMessage and version option.


* **SellAll performance improvements:** Improved sellall performance with Inventories and Backpacks.


* **SpigotAutoBlockGUI was rewritten using the new GUI utility.**


* **GUI Utility updated:** it now supports buttons without a position (null value), they'll be 
added to the first free slot in the inventory.


* **v3.2.9-alpha.3 2021-06-28**28


* **SpigotSetupGUI was rewritten using the new GUI utility.**


* **Added world to the MineTargetBlockKey since it will be needed with future util tools.**


* **Hook up the blockEvents block filters.  This is now 100% functional.**


* **Added support for smelting cobblestone to stone (defaults to off) and copper_ore to copper_ingots.  Added support for blocking copper_ingots to copper_blocks.**


* **Adjustments to the blockEvent block remove.**


* **More work on adding support for block filters on mines blockEvents.**
Got the commands working for adding and removing blocks to and from a blockEvent.


* **Enhancements and fixes to /ranks autoConfigure.**
Fixes an issue when trying to use force where it was not working.  It now works.
Provided new behavior of when forcing it will try to fully use what you already have, both with ranks and mines, but all mine that have blocks will be replaced.  If mine liners already exist, they will not be replaced.
Added a new option to `forceKeepBlocks` that when mines already exist, it will keep their blocks.


* **Setup blockEvents to be able to search for blocks to be added.**
Unfortunately, this turned out to be the wrong approach and is too complicated.  I just thought of a better way to do this.
Committing this code since some of it does improve a few things not directly related to blockEvents.


* **Changed around how the startup of prison works.**
This helps to eliminate a couple of error messages when enabling the compatibility classes.
Moved the display of the system stats to the Prison class so it can be now included with the prison splash screen.  This is important to get displayed here so if there are other failures, at least we have that covered to help with troubleshooting.


* **Update docs and provided a new startup task that runs when there are no mines or ranks on a server.**
The new task will print out a few messages in the console and provide links for additional information.  This is to help new admins of Prison figure out how to get it configured quickly and where to find help.


* **Added a few more rank messages for the ladder commands and fine tune how they work.**
Changes to the list commands for ranks and ladders to suppress the option to remove the commands.  The /ranks info command will now suppress the removals.


* **Hooked up ladder rankup commands to the rankup processes.**


* **Setup the ladder messages for the ladder commands.**
It was using the rank command messages.


* **Hook up the ladder rankup commands to the commands to list, add, and remove commands from the ladder.**
These work, but currently use the rank messages and not ladder specific messages.


* **Added rankup commands to the ladders objects.** 
This sets up the loading and saving of them.


* **In the LadderManager is a function saveLadder that should not be used outside of the ladder manager so it's visibility was set to private.**
As such, many external functions had to be changed to the save() function.


* **Converted mine commands to use row number for removal.**


* **Fixed an issue with rank commands removal with a line number... should have subtracted one from the row number.**


* **Found an issue with lore value of Block hitting upon any lore that begins with Block, such as Blocks Mined:**
To fix this issue, the default lore has been changed to include prefixes of "Auto ".


* **Found bug with lapis_lazuli having a 1 to 1 auto block to lapis_block.**
Should be 1 to 9.


* **GUI utility, started transition:** GUIs are slowly being rewritten to the new GUI
utility, this will improve performance and make GUIs more stable in the future.



* **v3.2.9-alpha.2 2021-06-24**
Bump to alpha.2.


* **Bug fix: Found a rank message that was missing the second parameter.**
This was with the command /ranks command add when the command was a duplicate of a preexisting command.


* **Updated some of the comments on how to manually specify the java location to control what version of java is used (this is like hard coding).**
It is not advisable directly change the actual build scripts since they will cause failures on other platforms.


* **Changed rank commands to use row number for removal**, instead of having to enter the exact command.
Entering the command has problems when formatting is used.


* **Later versions of Java required that the getters and setters for the handlers exist.**


* **Fixed an invalid use of an Intger object.  It was deprecated and removed.**


* **Found a statement that had a parameter on a String.format when it shouldn't have.**
This did not produce any errors; it was just incorrect.


* **Getting ready to add a new debug feature to test the BlockBreakEvent processing by going through all registered event listeners one by one and see if they handle the event.**
The new feature, when done, will list what happens with each listener, and report the state of the block between each one.  
The mine will be a parameter so you can test how it works within a specific mine without having to have an online player look at a block, like how wg does it with their command /wg debug testbreak.


* **Added a new {firstJoin} placeholder for the rank commands.**
This will only be activated upon a first join event, therefore it can only be added to the default rank on the default ladder. If added to any other command on any other ladder or any other rank, that command will always be ignored.
The use of this new placeholder allow special processing for new players only and prevents th need for other plugins just to provide On Join processing.


* **Fixed the placeholder prison_rankup_cost_remaining_bar_laddername to remove a numeric value that was being prepended to the bar graph.**
It was left over from the percent function and should have been removed before.


* **The RankPlayer's addRank() function was changed to eliminate the ladder parameter because the ladder "should" be part of the Rank object anyway.**
Having ladder was redundant.


* **A little more advanced way to delay the startup of prison.**
This change requires that vault be present and active, then also that the VaultEconomy is also active.  It then is able to validate the economy plugin that is actually active to confirm that CMI Economy is loaded and hopefully active. 
It is hoped that this will allow a better control starting up prison.


* **Set the next version to v3.2.9-alpha.1  2021-06-19**


* **Fix a potential NPE. Suspect the new value of Y is too low and is result in no block.**
Cannot check the y value for a lower limit since future versions of minecraft will allow for negative values of y, and values greater than 255.  So having to check when a block cannot be resolved, which is not too specific.


* **Reworked some of the event listeners to eliminate duplicates and to pull some out in to their own classes so it is clear what they are listening for.**
Zenchantments has been fixed for when auto features is off.  It did not have any listeners setup for the normal drop processing.
Moved around some of the config settings for the autoFeaturesConfig.yml to group them better and add a processing control setting for zenchantment.  
Set the default value for enabling auto manager to true so it will be on by default now.


* **Bug fix: Issue with non-plugin jars being scanned, which was resulting in a null value for the plugin names.**
Changed the code to bypass all jars that are not registered (has the signature of) as a bukkit plugin.
This bug prevented prison from starting up, so this is a very serious issue.


* **Rework how some of the BlockBreakEvents are setup to reduce unused and duplicate listeners.**
Changed the defaults for the three enchantment plugins to be DISABLED by default.  This will help reduce listeners when these plugins are not being used.


* **Suppress the auto manager details in `/prison version` if auto manager is disabled.**
Displaying this information would only cause confusion since the setting are ignored, but could imply they are active since they are shown in this command.




# v3.2.8.1 2021-06-18


Prison V3.2.8 Release!
Prison now fully support Spigot 1.17 and Java 16!


**NOTE:** Since the start of the development on v3.3.0, Prison has had a few other releases under v3.2.7 and v3.2.8.  The reason for these releases is that the major structures (and code) that would make prison v3.4.x, are not complete.  Therefore, to get out new updates sooner than later, v3.2.7 and v3.2.8 have been release.


* **Released v3.2.8!**


* **v3.2.8-alpha.3 2021-06-16**


* **v3.2.8-alpha.2 2021-06-12**

* **Spigot 1.17 release - v3.2.8-alpha.1 - 2021-06-11**
Only known issues: 
   * Unable to use nms to get the player's preferred language

* **v3.2.8-alpha.1 2021-06-07**
Internally set the version, but will not release it until a few other things are finished.
The prison version is set to 3.2.8-alpha.1 to prepare for the release of prison that is compatible with Java 16 and Spigot 1.17.


NOTE: v3.2.8-alpha.1 is identical to v3.3.0-alpha.6.  V3.3.0 is far from being ready to be released.  So v3.2.8 will enable Java 16 and also Minecraft 1.17.


# v3.3.0-alpha.6 2021-06-07


* **v3.3.0-alpha.6 2021-06-07**
Setting the version.  The v3.3.0 release will be put on hold since focus will be to get v3.2.8 out which will support Java 16.  It is unknown how many of the spigot 1.17 blocks will be initially supported.

* **v3.3.0-alpha.5c - 2021-06-06**

* **v3.3.0-alpha.5 2021-06-01**

* **v3.3.0-alpha.4 2021-05-15**


* Next release will be v3.3.0-alpha.3
Please note that the correct order of releases have been: 
v3.2.6, v3.3.0-alpha.1, v3.3.0-alpha.2, v3.2.7, v3.3.0-alpha.3


# v3.2.7 2021-05-02


* **Set version to v3.2.7**
  - Note that all changes that were made under v3.3.0-alpha.1 and v3.3.0-alpha.2 have been publicly released under v3.2.7


* **3.3.0-alpha.2 2021-04-23**


* **v3.3.0-alpha.1 2021-04-16**


* **v3.3.0-alpha.0 2021-04-11**

  Start on the alpha.1 release.
  

  