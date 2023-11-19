[Prison Documents - Table of Contents](prison_docs_000_toc.md)

## Prison Build Logs for v3.3.x

## Build logs
 - **[v3.3.0-alpha - Current](changelog_v3.3.x.md)**
 - [v3.2.0 through v3.3.0-alpha.16](prison_changelogs.md)
 

These build logs represent the work that has been going on within prison. 


# 3.3.0-alpha.15 2023-07-07



The following is a highlight of changes for the alpha.15 release since the alpha.14 release.

- Full support for 1.20.x including the heights and blocks.


- Added support for Java 20.


- MineBombs - Added a new field specifically for use with item names which supports color codes.


- AutoFeatures - Fine tune block break priorities for custom events. New feature to process MONITOR and BLOCKEVENTS only if the current block is AIR. 


- New feature: If invalid player access, then TP player to their current mine.  Optional Setting.


- Expand Prison's debug block inspector: added more information.


- Updates to many internal libraries.


- Added support for silk touch

- Added a fortune multiplier - this is used to fine tune highly OP tools so the drops can be better controlled.

- Fix: Rev Enchants JackHammerEvent: found out that this event returns the corner blocks of a cuboid of blocks.  Now calculates all included blocks to process this event correctly.

- Sellall now partially works when ranks are disabled.

- Fixed problems introduced when config files were manually edited incorrectly and they were injecting null values in to some properties.

- Fixed how NBT-api is being shadowed and used to get it to work properly with Spigot 1.20.x.

- Minor fixes and enhancements: blockEvents, autoFeatures and monitor priorities, GUI Player Mine config options, prevent GUI configs from loading twice, when mining now can control non-prison placed blocks to pass through to be handled by bukkit, fix autosell when disabled, hooked up support for minecraft stats to track block mining, prevent zero drops if calcs are less than 1, fix sellall trying to sell an invalid stack, fix prison utils potion effects when no player is provided, support for ExaltedEconomy, able to bypass adding new players on startup, updated Prison's jar reporter to identify Java versions 19, 20, and 21, clarified  help on `/rankup` and `/presetige`, update of how topN is being processed, added topN stats which reports some of the core info.



- = - = - = - = - = - = -


**2023-07-07 v3.3.0-alpha.15 Released**


* **Added comments to the prison-spigot/build.gradle configs to add more descriptive comments.**
Checked the libraries and all are up to date.


* **Setup the topN stats to be included in the /prison version information.**


* **Added the language files for the three new topN messages.**


* **Update the topN command to eliminate the use of the save file.**
All players are dynamically loaded by the refresh task.  Stats have been added and are not a new option for the command.
To act as a form of debugging feature, the save of the topN data has been disconnected from the current process.


* **Updated ItemsAdder from v3.2.5 to v3.5.0b**
because github's build was failing to find v3.2.5 online and was killing the prison build.


* **Update topN Stats.**  This is not the same as topNPlayers and this is not yet being used.
This tracks topN on blocks mined, tokens, and the player's ranks and balances. 
The comparator was not correct and was fixed.


* **MC 1.18 new world height support.**
Prison now supports the newer world heights as found in mc 1.18.x and newer. 
The new range for Y is from -64 to 320.


* **Bug fix: found that under a rare situation that it was trying to use an empty String with decimal formatting.**
This was found while testing some placeholders while players were offline.


* **I made a mistake when adding the new feature to skip player scans on startup.**
I added a new root perm in config.yml that started with `prison-ranks` when `ranks` already existed.  Therefore, I changed the defaults in config.sys to fix this and changed the code to allow the old version of the config (the bad version) and the newer version.


* **Changes to the help for `/rankup` and `/prestige` commands to make it a little more clearer** as to what is expected with perms and to provide more details.
Added a new config setting to disable the need to use the prestige perm `ranks.rankup.presetiges` which may make it easier to get presetiges working on most servers.
Prestiges still requires the use of the `ranks.user` perm which is the same perm used for `/rankup`.


* **Updated the PrisonJarReporter tool to include java versions 19, 20, and 21.**
Also fixed a bug in which if the version signature is not known, then it was returning a null.  Now it returns an enum of type "JavaSE_UnknownVersion" which will prevent future errors.



* **2023-06-20: Version 3.3.0-alpha.14c released**


* **Totally stupid change: github's compiler forgot how to use overloaded functions so it was trying to use the wrong one.**
Renaming the function should help it's anti-AI skill sets.


* **Fixed the way prison was using the nbt-api.**
Now using the correct repo and the newer API functions.


* **Trying to get nbt-api setup properly with shadowing.**  It looks like it's correctly shadowed, but it's being improperly reported as not being shadowed.  I have a conversation with the developer open to figure out what's going on with this issue.


* **Update the item-nbt-api library to v2.11.3 from v2.11.2.**
Turned out the maven repo they actually use was not mavencentral.com, of which, has not yet pulled in the updated version.  Using the correct repo now.


* **Update libraries:**
Update bstats from v3.0.0 to v3.0.2.
Update XSeries from v9.2.0 to v9.4.0.
Update vaultAPI from v1.7.0 to v1.7.1.


* **Changed config.yml to be able to bypass the add new player at prison startup.**  This would be more related to servers that already have a large player base when they switch to this plugin.
NOTE: It's not possible to fully test all conditions where a player object may be null. Use at your own risk if you do not allow prison to scan for new players at startup.  If an error is found, please contact support ASAP in our discord server so we can get it fixed for you.


2023-06-13 : 
* **Update how the player objects are written when dirty.** There were some situations where the RankPlayer object would be written to the file system 2 or 3 times for one change.
The logic of how things are nested remains the same (to minimize breakage of the code), but the RankPlayer is not utilizing a dirty flag internally so if it's saved once, it skip the other attempts to save without changes.


* **Simple example illustrating the weakness of doubles with large values.**


* **Update nbt api to v2.11.2 from v2.11.1**


* **Added ExaltedEconomy to the soft depends so prison will wait until it is loaded before trying to startup.** 2023-05-24


* **Fixed an issue that if the prison config files are manually modified and as a result, the block events cannot be parsed, this fix prevents a null value being inserted in to the loaded block events.**
For example, a trailing comma would produce a null block event because the parser that prison uses will read the comma, then with nothing else following it, it injects a null in to the collection of raw data for the block events.  Then when that raw data is parsed, it passed along that null as a valid block event.  The fix, prevents any of the nulls from being added to the active block events.


* **A sellall gui message that was supposed to say that the gui was not enabled only said sellall was not enabled.  Added a  new message to clearly state it's the gui that is not enabled.**


* **Get part of sellall to work if ranks are disabled.  The command /sellall sell works, but the other sellall commands need to be tested and fixed.**


* **Bug fix: bstats and topn was using the wrong function to check to see if ranks were enabled.**


* **Fixed an issue when cannot get a player from bukkit**


* **AutoFeatures bug fix: If normal drops is enabled (no auto pickup), and sellall was disabled, then normal drops were being disabled.**
The location of checking for if sellall was active was in the wrong location, which was preventing prison from actually dropping the blocks for the player.


* **Fixed an issue with prison utils potions where if the player was null, then it was throwing an NPE.  **


* **AutoFeatures: Rev Enchants JackHammerEvent: Bug fix: The jackhammer event was not returning a list of all of the blocks involved in the event, which could be excluding hundreds if not more than 1000 blocks.**
The fix, uses the two points to calculate which blocks to include, and then include them through that cuboid instead of getting a list of blocks from the event.


* **BugFix: Fixes an issue with sellall where it is trying to sell an invalid ItemStack.**
As a result, the sellall pays the player for the itemstack but the itemstack is not removed.  This fixes it by not trying to sell the questionable itemstacks.


* **AutoFeatures: Add a bukkit drops multiplier which is applied to the bukkit drops before the fortune calculations are performed.**
This can be used to reduce the total number of drops if a value less than 1.0 is used.  A value of 1.0 does nothing.  A value greater than one will increase the bukkit drops.  All values are floored and are integers.


**Update to v3.3.0-alpha.14b** 2023-02-26


* **AutoFeatures bug fix: If global fortune multiplier is set to a value lower than 1, then there is a risk of zero drops; this prevents zero drops and returns a drop of one.**


* **Fixed an issue with the initial event check for events that will break multiple blocks.**
The issue is that the initial check will ignore the event if the primary block is air.  The issue is that since the events are fired based upon the BlockBreakEvent then the odds of the primary block is AIR is very high.  So for those events, the primary block should not be checked for AIR to be bypassed.  This fix allows things like explosions to work.


**Update to v3.3.0-alpha.14a** 2023-02-25


* **Enhanced the debug reporting for fortune calculations and fixed a few uses of the newer fortune settings, some of which were used in the wrong locations.**


* **Updated the formatting on the prison's mine wand for debug reporting of which blocks are clicked on.**
The information has been cleaned up to be easier to read and follow.  It's now being logged in the console too so the details can more easily be reported back for troubleshooting.


* **Updated the auto features config file to include the ACCESS priorities in the list of priorities so its better understood what the real options are.**


* **Enhanced some of the auto features logging related to fortune, silk touch, and event and drop canceling to eliminate ambiguity and provide more specific details.**


* **Fixed an issue with player counts being doubled.  Counts should no longer be done within the auto pickup or the normal drops... it's being handled at a higher level for consistency with other priorities.**


* **Added a fortune multiplier that is applied to all fortune calculations, which allows for increasing or decreasing the results of the fortune.**


* **French support added by Maxcension. Thanks Maxcension!**


* **Move the check for access to the OnBlockBreakMines.ignoreMinesBlockBreakEvent so it is logged with the other conditions.**


* **Setup minecraft statistics so prison can report block mining through a new setting within the auto features.**


* **Enable silk touch enchantment by dropping the actual blocks that are being broke.**
If alt fortune is being used, then fortune will apply to these silked drops.
If players place silked blocks back in the mines (fi that feature is enabled), prison will ignore those blocks and won't break them... it will let bukkit or another plugin deal with them, but it will not apply any fortune to them. 


* **Bug Fix autosell: was trying to access autosell when it was disabled.**


* **Update google gson (json IO tool) from v2.8.6 to v2.10.1**


* **Update google guava from v19.0 to v31.1-jre.  Guava is used for internal event listeners.**


* **Prison Debug Block Inspector: Expand and enhanced the prison tool to provide an inspection of the block break events.**
Added event block details and drops being canceled for each listener. Reformatted to make it easier to read.


* **Remove the optional from getModule functions since java 17.0.6 was failing.**
Not sure if it's an actual java issue, or a problem caused by another plugin, or etc... this works well with java 17.0.2.


* **Minor improvements to the EventResultsReasons to show a success and more detailed debug logging.**


* **Relocate the ACCESS failure which will trigger a TP to an accessible mine...**
this is relocated because it's not an event, but a behavior triggered by an event condition.


* **Setup a temp test to test ItemsAdder.**


* **AutoFeatures: new feature to process MONITOR and BLOCKEVENTS only if the block is AIR.**
The reason for this is that if we are monitoring a blockbreak event, then we can assume that the block should be AIR.  This setting is important for enchantment plugins handling the block break events, since a non-AIR value would indicate that the player was not successful in breaking the block.  
Added more detailed debugging logging if the event is fast-failed or under normal conditions.


* **Change the block break priority BLOCKEVENTS to MONITOR.  Updated the docs too.**


* **Add new autoFeature setting to allow non-prison placed blocks to be handled by bukkit: ifBlockIsAlreadyCountedThenCancelEvent: true. (default setting).**
Prison was canceling the event if it found a block placed in the mine that it did not place during a mine reset.  This would allow players to place blocks and then remove them if they have the worldguard perms to do block breaks.
These blocks are not tracked in prison and are not handled.  Prison just ignores them.


* **Fixed the gui config which it needed to load after loading ranks and mines.  So it's initialized a second time in the startup process.**


* **Gui Player Mine config settings:  Added `Options.Mines.MaterialType.NoMineAccess` which defaults to REDSTONE_BLOCK.**
If it does not exist in a player's GuiConfig.yml file, it will now be auto added.
Also if the `Options.Mines.MaterialType` block list of material types to use for each mine does not exist, it will auto add them, using the first block in the mine's block list.


* **Some adjustments to AutoFeatures and monitor priority... it was processing block events under some conditions.**


* **Mine BlockEvents: Enables the use of pipes in commands and messages now.**


* **MineBombs: Added a new field specifically for the item name for the bomb.**
The mine bombs now auto load upon startup and will auto update now if there is a change in mine data versions.


* Changed the example world names in the config.yml file where Prison is disabled. Too many people were running in to the problem where they just happened to have those worlds, and that's where they were trying to use prison.  So they were thinking Prison was not working instead of Prison being disable in those worlds because that's what was in the configs.




