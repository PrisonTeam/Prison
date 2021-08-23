[Prison Documents - Table of Contents](prison_docs_000_toc.md)

## Prison Build Logs for v3.3.x

## Build logs
 - **[v3.3.0-alpha - Current](changelog_v3.3.x.md)**
 - [v3.2.0 through v3.2.10](prison_changelogs.md)
 

These build logs represent the work that has been going on within prison. 


*Will continue as v3.3.0-alpha.7 2021-06-?? in the near future.*



# v3.2.10 2021-08-22

* **Prison Release v3.2.10**


* **Updated to the support of the Prison's ExplosiveBlockBreakEvent. There were a few adjustments that were needed.**


* **Some blocks within the liners are not compatible with all versions of bukkit/spigot/minecraft.**
Added minimal versions to the patterns so as to help prevent the use of the wrong patterns for the server version.


* **Found an error with two Mine Liners that had pillar_quartz_block instead of quartz_pillar.**


* **Added support for Prison's own ExplosiveBlockBreakEvent.**
This will be used for Prison's up coming Prison Bombs.  Also this can be used by other plugins too.


* **Update the PrisonEnchants code for handing their PEExplosionEvent object.**


* **Fix a problem with NPE when getting the player's Locale when it wasn't set.**


* **More adjustments to the /ranks list to add information about the player's current multipliers on all the ladders.**
This makes it easier to understand why the rank multiplier is a specific value.


* **Some adjustments to `/ranks list` to remove "admin stuff" from the list for non-admins.**


* **v3.2.10-alpha.14b 2021-08-22**


* **Removed the option to delete a rank from the ranks info command since it should not be that easy to remove a rank, which could easily be clicked on in game.**
Also commented out dead code.


* **Hook up the process to have all unjoined players added to prison by giving them the default rank.**
This is also ran now at the end of the /ranks autoConfigure to ensure all players are hooked up as soon as the default ladder and it's ranks exists.


* **Player join bug: Fixed a bug... if a player is not on the default ladder, then this was preventing them from being added.**


* **Added virtual checks to prevent placeholders from trying to access mine features that do not exist.**


* **v3.2.10-alpha.14 2021-08-21**


* **reduce what is shown on /rank if for non-op players.**


* **Decrease the autoConfigure's prestige costs from 2 B to just 1 B.**


* **Reload auto features after running ranks autoconfigure to enable the auto pickup.**
It wasn't working right after generating the mines.


* **Fixed an issue with virtual mines not being included in /mines list all command.**


* **Now `/ranks autoConfigure` creates 10 prestiges ranks.  **


* **Have placeholders reload after mines or ranks are created, removed, or renamed.**
Create mine and create rank now has an option to suppress updating placeholders since commands like autoConfigure will cause tons of messages to be generated.  The placeholders are regenerated after all mines and ranks are created.


* **Command '/mines set area' fixed confirmation to use yes or confirm.**
Added the ability to set size when using feet.  The size is what is used with the '/mines set size' command.  So 20 width is really 41 since it's adding 40 blocks in all directions.



* **Redesign the /mines info command to reduce and compact the listing.**
`/mines info <mineName>` is the reduced listing where disabled features are not shown. `/mines info <mineName> all` is the expanded listing that includes more details.
Reworked the block list to use the String.format for spacing instead of manual adjustments.


* **v3.2.10-alpha.13 2021-08-20**


* **Fixed an issue with null for the playerUuid..**
it's rare, but it could be null.  This prevents the failures.


* **Fixed a few issues with ranks, a NPEs.**


* **The text for ladder info was changed to remove player's name, which does not make sense to include, especially if ran from console.**


* **Yet another situation where CMI needed prison to start with a delay; confirmed setting is correct.**
It was confirmed for the second time that 'Economy_CMI' works so I moved that to the 'vault-economy-name:' setting.


* **Fixes a bug when checking offline players which was returning a null with rPlayer.getRank().  Was fixed.**


* **Ranks autoConfigure: Add to the prestiges ladder the base rank cost multiplier of 10 percent.**
Provide a few messages to document it when the /ranks autoConfigure command is ran.


* **For the /ranks autoConfigure command, setup an alias to /prison autoConfigure.**


* **Setup a task that is to be ran whenever there is a rank change within a ladder, or if the ladder has the base rank cost multiplier changed.**
This helps to ensure that the rank costs are correct for all players.


* **Renamed the command `/ranks remove rank` to `/ranks removeRank` since the prior command was a single command within it's own sub-group.**
This change will now list the command with the others when using `/ranks`.  It was "lost" and "hidden" and admins were not able to find it that easily.


* **Changed the /ranks ladder delete command to prevent a ladder from being deleted if it has any ranks associated with it.**
To delete a ladder with ranks would corrupt the ranks.  The ranks would have to be removed first to ensure no players are on the ranks.


* **After a change in a player's rank, have the player's Rank Cost Multipliers recalculated.**


* **The command /ranks player <playerName> perms was generating an error with offline players so this fixes that issue.**


* **Changed config.yml to enable and disable prestige confirmations.**
Updated the GUI to support this.  Settings where changed and config.yml may need to be reset when updating prison to use these new features.


* **Fixed bug with `/ranks ladder moveRank`.**
It was not removing the rank from the source ladder, but was also adding it to the destination ladder.  This was fixed and is now working correctly.


* **Removed the remainder of the ladder/rank permissions that were never used.**


* **Start to setup MineBombs basics.**


* **Add to the World object, a function to set the block.**
The SpigotWorld class uses prison's compatibility functions to perform this action.


* **v3.2.10-alpha.12 2021-08-16**
Released alpha.12


* **Updates to fix the prestige issues.**
Moved more of the processing in to the PlayerRank object to reduce issues. Shutdown one of the constructors since it could lead to the wrong amounts.
Prestige was fixed by rewriting the way the rankup handled the ranks and playerRanks.


* **Fixed an issue with logging when using a debug target mode.**
It was not logging when the global debug was off.


* **Added some rankup debug logging details.**
Enable with `/prison debug rankup`.


* **v3.2.10-alpha.11 2021-08-15**
Bump the version


* **Changed the /ranks info to show the ranks multiplier information.**


* **Work on /ranks list to improve the content to better present the rank cost multiplier.**


* **General changes to code that uses getRank() that returns a PlayerData object... **
Since that is a player's instance of a rank, this may be null for a lot of players.  A lot of the old code would not take in to consideration that there may be no value, so hence the NPEs that we've been seeing.  This should address a lot of the potential issues.


* **Found situations where players are not on a ladder and its causing issues.**
Need to expand to other code...


* **Bump to v3.2.10-alpha.10f.**
Will be trying to release a more formal alpha later today.


* **Fixed a typo in the name of a function.  Prevent a possible NPE.  Revised how one of the PlayerRank constructors works so it will actually calculate the correct multiplier.**


* **v3.2.10-alpha.10e 2021-08-13**


* **More adjustments to rankups and PlayerRanks.**  I think I've addressed all issues with ranks.


* **Remove three redundant ladder commands dealing with ranks: listranks, addrank, and delrank.**


* **There was a report that the mine would continue to reset after it was deleted.**
Cleaned up a few things and added a boolean field to indicate it's delete.  If its set to deleted, then it will prevent the resets from happening.  Just as a safety check.  Not 100% sure that this was actually an issue.


* **Setup the AsyncPlayerChatEvent listener within Prison to be able to dynamically set the priority based upon a new setting in the config.yml file.**


* **Bug fixes: Fixed some issues with the use of the new PlayerRank object.**
They were not always not-null, and there were some instances of the wrong variable being used.


* **Fixed an issue with the regular expression block quote... didn't need the second backslash.**


* **Bug fix: Fixed a few issues with PlayerRank object not being set correctly everywhere.**
Was also checking the wrong object for null.  These fix about 3 different NPEs.


* **Removal of the obsolete ranks related permissions and permission groups.**
This was a failed attempt to automate the user of permissions with ranks.  This did not work, since vault does not support the creation of permission groups, plus a few other issues and limitations with vault.


* **Initial setup of ladder rank cost multipliers.**
All ladders a player has, will contribute to the total rank cost multiplier value, and all ranks for that player will have their cost adjusted.  
Overall this will allow an increasing rank cost as players rankup on the prestige ladder, and/or it can help reduce rank costs if donor ranks provide a negative adjustment.
Currently there is no way to set the ladder's multiplier... that's next.  But everything else is hooked up and should be functional.


* **v3.2.10-alpha.10 2021-08-11**


* **Added a missing ranks message: when removing a player from a rank and it fails.**


* **LocalManager.getLocale: Fixed an issue with player being null when the player object originated from a task processing offline players.**


* **Added the event level MONITOR and changed the event listeners to only register the MONITOR event when the config is set to MONITOR.**


* **Updates to the JumboTextFont by adding most of the lowercase characters.**


* **Alter the use of accessing the player to help eliminate the risk of concurrent modification events.**


* **Added a spigot module en_US.properties language file.**


* **Added a new function to the PrisonEventManager to return the list of event listeners as a string so they can be logged to the console, or through helpch.at which is not yet hooked up.**                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     


* **Added JumboTextFonts to prison support submit ranks.**
Fixed a few issues and added more characters to the fonts.  Added /ranks info <rankName> all to the ranks output for more detailed information.


* **Found and fixed an obscure bug with regular expression quotes.**
It was duplicating text and making the results much larger than what they should have been.


* **Created a JumboTextFont to be included in the prison support submit documents.**
Hooked up to mines.  This will make it easier to identify the various mines in the listings.


* **Add the capture and display of all support submit URLs in each additional submission.**


* **Expand '/prison support submit mines' details by including the '/mines info <mineName> all' command.** 
Had to redesign some of the mine commands to be able to capture all of those details.  Added a table of contents to the submission so it's clear all these details are included, since it could be a HUGE file.


* **Added 6 new placeholders to provide the whole line for the prison_top_mine_block_ plus the heading and total line.**


* **Placeholders with _nnn_ numeric ranges: Fixes a potential problem of where it was unable to match a placeholder if placeholder escape characters were not used.**
Now it will properly identify and match the series.


* **Fixed a problem with block constrains, the min number of blocks, when no blocks were initially added to the mine.**
When revisiting to ensure the min amount is set, it did not have the correct values set for the ranges. 
This has been fixed by adding a low and high limit field that not does record the actual block positions of this block type, but of the limits as defined by the constraints.  This allows adding more blocks to reach the min amount, when no block has been added before.


* **Prevent the use of % in the ranks and ladder commands.**
Could not use them before, but they would cause errors when messaging details about the commands.  Now the command will be rejected and it will have to be submitted correctly.


* **ButtonLore Utility got a couple of new methods.**


* **Some GUIs were still using the old deprecated buttons. This got fixed.**


* **All GUIs got a new common lore Format.**


* **All GUIs now use ButtonLore utility and lore format.**


* **Fixed an issue with spigot 1.17.1 where they appear to be extending the BlockBreakEvent for FurnaceExtractEvent but not isolating the listeners.**
Forced to check to ensure the event is actually a BlockBreakEvent to ensure the correct event is really being fired correctly.


* **Added prison's version to the chat display's title.**
is will help support since it wil help show us what version someone is using.


* **Added the recording of earnings per mine.**
The add earnings must be called within the time limit as defined by the session timeout for mining, which is currently 15 seconds.
If something is sold outside of that session timeout, then it will not be attributed to the mine.


* **Minor design changes to the main GUI /gui**: to apply them you should reset (delete it) your en_US.yml at this path ->
  Prison/module_conf/lang/en_US.yml. More changes will come in the future.


* **ButtonLore is quite ready and already in use for the /gui main GUI, only the first page uses it.**


* **Started working on a new ButtonLore Utility that will be included in the SpigotGUI Utility.**


* **v3.2.10-alpha.9 2021-08-04**
Released...
Upgraded gradle to use v6.1.0 of Jengleman's ShadowJar... was v5.2.0.


* **Modify the block break events to merge most of the preliminary processing from all of the different kind of block events.**
This enables a single function to be able to handle most of the initial setup processes for all of the different block break events.  This will reduce the amount of code, eliminate possible errors, and make it a lot easier to enable more events to be processed.


* **Add the option for using '*all*' for mine name for adding block events, which will add the event to all mines.**


* **All GUIs now use PrisonGUI Utility, finally it should be done now.**


* **2 more autofeatures GUIs now use PrisonGUI Utility.**


* **All SellAll GUIs now use PrisonGUI Utility.**


* **All Mines GUIs now use PrisonGUI Utility.**


* **Disabled some old Block model code in use for GUIs.**


* **Mines Blocks List GUI now uses PrisonGUI utility.**


* **Fixed Mines Blocks List GUI (the one that shows the blocks currently in the mine).** The GUI wasn't translating
correctly blocks, and showing a "barrier" instead of the block, the GUI was working anyway, just
  not good looking, now it's fixed for newer versions.


* **Mines Reset Time GUI now uses PrisonGUI utility.**


* **Mines Set Notification Mode GUI now uses PrisonGUI utility.**


* **Mines Notification Radius GUI now uses PrisonGUI utility.**


* **Mines Blocks Set Percentage GUI now uses PrisonGUI utility.**


* **Updated Buttons PrisonGUI Utility to support enchantments.**


* **For the fortune calculations, converted the use of the old block model over to use XMaterial instead.**
Updated to include the newest 1.17 blocks too.


* **Updated smelting to use the newer 1.17 blocks, such as all of the deepslate variations, and the raw iron, raw copper, and raw gold.**
Updated all code that had these variations.


* **Updated the XP calculations to include the newer blocks from 1.17**


* **Upgrade XSeries from v8.2.0 to v8.3.0**


* **Remove the dump of block break events from the command /prison version all since it was appearing first.**
It will need to be rewritten to capture in a list of strings.


* **Added mining time by Mine.**
This will also track in which mine, players are mining.


* **Found a bug in the TokenEnchant event listener class.**
It was casting to the wrong class for the Monitor event.


* **Fixed an issue with PlayerCache and mining duration.**
Also added logging if unable to rename a temp file.


* **Added support for jumbo and full ladder types with the mine liners.**


* **Update the custom ladder type feature in mine liners.**
Now you can select none, normal, and wide.


* **Provide break the list of placeholders down by placeholder types, along with sub total counts.**
Also added total count at the top of the list.  Breaking down the large list in to sub-types will make it easier to read the list.


* **Provided a way to specify a last next rank message for the placeholder:**
prison_rankup_rank_tag_default
This will only apply on the default ladder and when the next rank tag is empty.


* **Started to look in to providing the ability to import player ranks** based upon perms, but ran in to an issue that if the player is not online, then it can be very difficult to get their actual perms.  Would not be able to get a listing of all perms, as if they were online, so it would make this less flexible.
This has been canned because the person who was needing it, is no longer needing it.  May revisit in the future.


* **About 56 new placeholders!** SpigotPlayerUtil has been updated to provide support for all of the functionality that it needs.  It's been hooked up to the placeholders so they should be functional. 


* **Setup the Platform to be able to provide a SpigotPlayerUtil object.**
This is needed for placeholder support and a few other things.


* **There was a concurrent modification exception on the line with** `playerData.setDirty( false );`.
These changes will not prevent that issue, but it will trap it so it does not register in the log files.  If this happens, then the player's cache will be saved on the next attempt.  This should be a rare event.
The changes needed are more complex that this simple "fix" and will be made in the near future.


* **Prison Mines Blocks Set Percentage GUI now uses PrisonGUI Utility.**


* **Prison Mine Blocks List GUI now uses PrisonGUI Utility.**


* **Prison Blocks List GUI now uses PrisonGUI Utility.**


* **Fixed some errors of the Prison Mines Blocks Set Percentage GUI:** There were some ArrayOutOfBonds exceptions.


* **Auto Features: Added food exhaustion calculations.**
Now on each block break,  Configurable to be disabled.  For explosions events, only applies to the one block they actually mined.


* **For various add commands, the messages for the 'placeholders' was being sent to the console instead of the player issuing the command.  This was an issue in game, but not the console.**


* **Placeholders durability: add support for a percentage and bars.**


* **3 new Backpacks GUIs for admins now use PrisonGUI Utility.**


* **Bug fixes with the setup of token enchant event listeners.**


* **Added many more features to SpigotPlayerUtil and getting ready for the new placeholders.**


* **Started to add a PlayerUtil and SpigotPlayerUtil to prison.**
This will allow an easier access to specific player values in other parts of prison with just a single call to a util function.  This will be used with a number of new placeholders.


* **Removal of a function from the prison Player object that is no longer being used.**


* **A couple of updates for CMI economy.**
Added a note on what value to try first for cmi with prison.


* **Changed some logging information from debug to info so it will be registered when requested by the admin.**


* **PrisonEnchants changed the class name of their event.**
This updates prison's code and changes the api jar file.


* **Created a new group within auto features for durability related items and created a new group autoManager just for the isAutoManagerEnabled to get it out of the options.general grouping since that is the main "switch" and it is getting lost in the configs.**
There was too many for the general group, and they were not being kept together, especially since we have a comment now.


* **Backpacks admin GUI now uses PrisonGUI Utility.**


* **v3.2.10-alpha.8 2021-07-25**


* **Auto features: Removed unused classes and cleaned up a few things.  Fixed a reference to one of the deleted classes.**


* **Auto features events: Added an abstract class to manage the unregisterListeners in one place, to eliminate duplication of code.**


* **For the command /prison reload autoFeatures hooked it up to include unregistering all of the event listeners that auto features setup, then it uses the configurations to reregister them all.**


* **Fixes a major problem with how Zenchantments works because the plugin extends the BlockBreakEvent with their BlockShredEvent.**
It has issues.. but... this ensures that the correct event is being trapped.


* **Change to player cache field that was not so clear with it's name.**
It's supposed to be the config field name, but the description did not reflect that.


* **Fixed GuiUtility button add lore method:** The method to add a line to an already existing button got now fixed.


* **Adjustments to get the new event listeners working fully.  Still need to hook them up to the reloading.**


* **Next step in rewriting how the event listeners are setup...**


* **Move the event listener managers in to a common package to prepare to extend them to the non-auto features event listeners.**


* **Rewriting how the event listeners are being registered.**
Added ability to unregister.  This is working, but is only for the auto pickups.  WIll need more work to hook up to the non-auto manager.


* **Setup the ability to log more than just the BlockBreakEvent.**
It it mow able to log details to Lists so they can be included in other output, such as support submits.


* **Rewrote how the event listener is registered so the priority is dynamic.**
This appears to be working well.


* **clean up some of the /prison version all commands to better reflect the actual details with dependencies with the settings for auto features.**
This should help better convey what the settings are doing, especially when a setting disables another setting.  This should help reduce support issues.


* **Prestige confirming fix:** Fixing Prestige confirm by chat or GUI when possible.


* **v3.2.10-alpha.7 2021-07-23**



* **Fixed the way some of the autofeature block settings were named.**
They had "autoBlock" when auto has nothing to do with it anymore, since those setting will be applied for normal drops too.
Fixed an issue with auto smelt and auto block where it was not using the config settings, so now it's working correctly.
Added more debugInfo messages to better understand what's going on when something is not working correctly.


* **Added a note on how fortune works... it's been confusing...**


* **Updates to the liner to allow the use of '*all*' for the mine name to apply the same pattern to all mines.**
For auto configure, ensure that the random liner applied to the mines is not remove, removeAll, or repair.


* **Prevent slime fun from registering if it is disabled.**


* **Fixed a problem with the registration of the events failed due to the parameter of ExplosiveEvent.**
Had to set it to Object to allow it to be registered when the plugin and class does not exist on the server.


* **Added more internal debug details.**


* **Mine liners: Added glowstone and also added the ability to apply a liner change, or removal, from all mines with one command by using `*all*` for the mine name.**


* **Add the debugInfo to more functions and a few more messages.**


* **Changed the debugInfo variable to a StringBuilder instead of just a String.**
This will provide a little bit of a performance improvement, plus it can then be passed in to the functions to gather more information.


* **A few minor adjustments to better identify the function that is running when logging the debug information on BlockBreaks.**
Tweaks to the PrisonEnchant's plugin code.  Missed a few changes.


* **Changed some of the DebugTarget names to better reflect where in the project they are occurring.**


* **Bug Fix: If a message is removed, such that there is nothing left of the = character in the language properties files, it will now not throw an index out of bounds exception.**
Removal of the message was not expected and there was unable to handle that situation.


* **Added support for 4 new types of placeholders that uses the PlayerCache:**
Player's total block count.  And player's count per mine.


* **Issue with using offline players with the playerCache.**
The playerCache should typically only contain active players, but in testing, like using /prison placeholders search, it may add an offline player to get placeholder data from them.
This prevents null issues with the locations, and eliminates the excessive error message when trying to use getLocation() on an offline player.


* **PlayerCache changes: Start to take in to consideration that a player within the cache may be offline.**
If that's the case, then prevent some calculations, and remove them from the cache.  Offline players would be added to look up some stats, but then they would be safe to purge.
When checking the timerTasks, add an earnings of zero to purge past earnings.  This will prevent the average earnings from staying there forever when the player stops mining.


* **Added a debug logging entry for XP Calculations.**
This will be able to provide detail infomation on if it's working and what the results are.


* **Add support for PrisonEnchants' ExplosiveEvent.**
While adding support for the ExplosiveEvent, found a few issues and fixed them.  Some changes were just in names of functions, others were functional, such as improving the support of unbreakable and blocks that fall outside of a mine.


* **Simplified how the title, subtitle, and actionBar are setup with the /prison utils titles title command. **


* **New Feature: Prison now tracks average earnings per minute.**
If you have auto sell enabled (sell upon each block break, see autoFeaturesConfig.yml) then prison will be able to report your average earnings over the last 5 minutes of mining activity.
`prison_player_balance_earnings_per_minute` prison_pb_epm
`prison_player_balance_earnings_per_minute_formatted` prison_pb_epmf


* **Rewrite how the player uses title, subtitle, and actionBar for the /prison utils titles command.**
Spigot 1.8 does not support any of this.
Spigot 1.8.8 is when this was introduced. But 1.8.8 does not have actionBar, so instead it shows the message in the subtitle.
Spigot 1.9+ are able to display title, subtitle, and actionBar at the same time.


* **Release of v3.2.10-alpha.6 - 2021-07-19**



* **Try to better handle situations where integrations fail when integrating.**
Now has the ability to disable the integration and remove it when it fails.


* **New feature: prison utils titles.** 
This enables sending messages to the player's console as either a title, a subtitle, and/or actionBar.  Can also clear, reset, or set the timings on the titles.
All three can be sent through the title by using a double colon to indicate the various parts.
For example: 'Hello Title!::Subtitle is Here!::I'm on an actionBar!'


* **Add feature to Auto Features to prevent tools from breaking or being used when durability is equal to or greater than maxDurability.**


* **Added some documentation to the ExplosiveBlockBreakEvent so users can better understand how it should be used.**


* **This is an example of an explosive block break event that should be used so prison can monitor for it, and consume it.**


* **More adjustments to the Taiwanese language files.**


* **More tweaks to player cache file saving... **


* **PlayerCache adjustments for timing tracking.  A work in progress.**


* **Updates to he Taiwanese language files.**


* **Improvements to adding new players to prison upon startup.**
This process scans bukkit's offline players and finds everyone who is not already setup in prison and adds them.
It now uses the /ranks set rank command now.  


* **Added support for zh_TW Taiwanese.**
This is just an initial setup for now.  To enable it, set the language in the config.yml file to zh_TW.


* **Player Cache: Hooked up block tracking for players.**
Appears to be working as far as tracking blocks that are being broke.  It loads and unloads when a player joins and leaves the server so only active players have their data online. Performs periodical updates on saving.
This tracks total blocks, block counts by mine, and block counts by block type.
Starting to setup time tracking.


* **More changes to setting up a player cache.**
The basics should be represented, with startup and shutdown of the cache, running a periodical task to refresh the data, running a periodical task to save the data.
No data is processed as of yet, but the cache should be reactive to players logging on and off the server.


* **Initial work on setting up the Player Cache.**


* **Minor SellAll utility changes.**


* **SellAll Player Blocks List GUI now supports pages.**


* **Minor improvements for GUIs to improve performance.**


* **PrisonGUI Buttons utility minor changes.**


* **Add the ability to play sounds through the /prison utils sound command.**


* **Update docs... scale back what we support with WorldGuard regions since access by ranks solves so many problems.**


* **SellAll Admin Blocks List GUI now supports pages.**  Note: This was **not** part of the v3.2.10-alpha.5 release.


* **SpigotGUIComponents minor changes.**  Note: This was **not** part of the v3.2.10-alpha.5 release.


* **SellAll Blocks GUI now uses PrisonGUI Utility.** Note: This was **not** part of the v3.2.10-alpha.5 release.



* **v3.2.10-alpha.5 2021-07-14**



* **Made the RankLadder comparable and in the RankPlayer changed the map to a TreeMap.**
This should fix the inconsistencies that I was seeing earlier.


* **If a player is detected to be lacking a rank on the default ladder, it will adde the default rank.**
Ran in a problem where getting the current rank for a ladder was failing when it was ran, but worked when stepped through a debugger.


* **When checking for players not hooked up to prison, prison now ensures there is a default rank configured or it won't try to check the players.**
This change also checks players within prison to ensure they have a rank on the default ladder.


* **MineInfo GUI now uses PrisonGUI Utility.**


* **Removed unused imports from some GUI classes.**


* **MineInfo GUI TP Button now has an Arrow instead of a red_bed:** red_bed can't be translated to a legacy block in 1.8.8.


* **Fixed performance issues for MinesInfo GUI.**


* **Added information to the language files on how to use utf-8 text in these properties files.**


* **Setup prison to ensure all compliers within gradle is using utf-8.**


* **Enable loading utf-8 encoding from the properties files. Java 1.8 is unable load them as utf-8, so this provides a work around.**
At this point, this ensures that if a properties file contains utf-8 encoded text, that it can be packaged in the prison jar, extracted to the file system, then loaded and used and maintain full utf-8 encoding all the way until it hits bukkit.  bukkit corrupts it and confirmed it was corrupted in spigot 1.8.8, 1.3.4, and 1.16.5 so its not an isolated issue for just one spigot version.


* **Updated cryptomorin's XSeries to v8.2.0.**


* **Convert the CommandHandler.getHelpMessage() to return a ChatDisplay object to allow the use of creating clickable URLs in the help messages.**
The help messages now has headers which shows the command that's being listed.
Updated the /mines blockEvent add command to include two docURLs to use as an example; improved the help message.


* **If a Ladder has a bad rank that is already in another ladder, that rank will not be added to the new ladder that is being loaded.**
An error message is already being logged, but this change will update the ladder to resave it without the duplicate rank.


* **Clean up Ranks a little more by removing more of the rank position and report failures if a rank is already loaded in another rank to prevent internal failures if a ladder save file is corrupted by manual editing.**


* **Removed the use of rank.getPosition() from all saved information within ranks and ladders.**
Position is no longer used to access any rank. It is only used to compare if one rank is higher or lower than another.  It's lazy loaded only when it is needed the first time. If a rank is removed from a ladder, or inserted in to a ladder other than at the end, then all ranks in the ladder are set to -1 for the position so they recalculate when they are used the next time.


* **This fixes a user error situation, which is not a bug, in the listing of ranks.**
The user copied the contents of one ladder to another ladder, without deleting the source ladder.  Thus there were two instances of a series of ranks, when there should ever be only one instance of a rank.  A rank can be in at most one ladder, or no ladder.  
The result of this issue is that when the second ladder was hookedup at prison's startup, it corrupted the ranks in the first ladder.
This works around such problems by changing /ranks list by using the list of ranks within the ladder, instead of using the rank's prior and next associations.  
These changes also add the rankId to both the '/ranks list' and '/ranks ladder rankList' commands, and also '-' and '+' notations to indicate a rank is linked to a prior and next rank.


* **Simplify the command /mines blockEvent add by removing the permissions and taskMode from the add.**
This was causing too muchh confusion for a lot of people. It should help to keep the add much more basic. There have been alternative commands to modify those settings so it really isn't important to set them on the add.  Also odds are they will not anything but the default values for these two fields.  The defaults are perms = none and taskMode = sync.  TaskMode is not defaulting to inline due to increased risk of lag and prison being falsely blamed for it.  If desired, the admin can always change the taskMode to what they need.


* **Bug fix: Fixed an issue where the "on click" events were using the wrong row number.**
The row number was getting incremented at the wrong time.


* **v3.2.10-alpha.4 2021-07-10 **


* **Few minor tweaks to the mines info command with formatting.**


* **Added player counts to the rank list command.**


* **Hooked up the /ranks list all command to the /prison support submit ranks command.**


* **Add option to /ranks list to include all ranks through the use of the "all" in place of the ladder name.**


* **Fixed a bug where getting a player was returning a null.**
When that null was encountered, it was not processing the placeholders, especially for the bars. 
Not really sure why it was failing, but replaced the use of getting the player balance with the most recent use of RankPlayer's getBalance() works perfectly.


* **More adjustments on the /ranks list to prepare for listing all ladders.**
Also added a couple of more messages.


* **Added an error message if there is a failure when using /prison support submit.**
I assumed that other error messages would exist, but having doubts if it always will have other errors displayed.


* **Improvements to the /ranks list command.**
Enhanced the header so it's clear which ladder is involved.  Also cleaned up the formatting with the tag colors so it does not impact the column alignments. Also doing a little more with injecting the "default" tag so it does not shift the columns.


* **Adds the listing of all mines to the /prison support submit mines command.**
This provides an overview of all mines and is included at the top of the listing.


* **Improve the layout of all of the mines with the command /mines list all.**


* **pasteChat update to keep the color codes.**  It will only keep the color codes that start with &, and all others will be removed.


* **New feature: /prison reload locales.  Its now possible to reload the language files that prison uses.**
Added a new debug item to test to see if utf-8 is working:  /prison debug testLocale.  No, it does not work with utf-8 text yet.
I did confirm that if the properties files are setup with UTF-8 and are a part of the prion jar, that the UTF-8 encoding will properly be saved to the local file system.  
The area that I think is causing failures is with the Properties object itself since that is not utf-8 compatible, so it appears.


* **v3.2.10-alpha.3 2021-07-09**


* **reformat the /mines list command to mak it easier to read**


* **Added support for prison support submit for ranks and ladders, and also for mines.**
These two new commands will submit the raw json files.


* **New Feature!! Major support feature!  Send all of prison's configs to paste.helpch.at so we can know exactly how servers are configured.**
This logs the following config files: config.yml plugin.yml autoFeaturesConfig.yml modules.yml module_conf/mines/config.json 
      SellAllConfig.yml GuiConfig.yml backpacks/backpacksconfig.yml


* **Added a success message when a player is added to prison's default rank.**


* **Fixed issue with a few placeholders that were not working.**
Was using the wrong translator so a number of mine related placeholders stopped working.  
The mineplayers were not working too.  The bug was introduced by hooking up STATSMINES.


* **Hooked up STATSRANKS placeholders and got rid of obsolete ones.  Still more testing needed.**


* **Added a player check on prison startup that will add all offline players that are not hooked up to prison yet.  Plus it will register name changes too.**


* **Added top rank balance placeholders: 6 for now and more will be added soon once these are fully working.**
These are based upon the player's balance within the rank.
Also added a Hesitancy Delay Penalty to help encourage players to rankup instead of trying to dominate a single rank.
These have not yet been tested.


* **Added RankPlayers to Ranks.**
This will allow quicker access to all players at each rank, which will help for top ranking reporting.


* **Clean up some rank and ladder function to eliminate the use of magic numbers: both rank ids and ladder ids.**
These were bad, since the objects were already linked together, so using these functions were just adding unneeded overhead.


* **v3.2.10-alpha.2 2021-07-06**


* **Added the ability to fail silently for the Localizable object to prevent the message ID from flooding certain messages.**
This will be employed in the Output.get() functions so if it cannot load the prefixes and level colors, then the message IDs will not be injected in to the messages.  
This new feature should never be used without careful consideration since it is important to identify when messages fail so they can be fixed properly.


* **Adjustments to LocaleManager for updating the internal checks for if the external files need to be updated.**
Also finalize how Prison object works with the LocaleManager.


* **Removed obsolete placeholders that are not used anymore.**


* **Adjustments to when the LocalManager is started so it can be activated as soon as possible.**
This is because of the need for Output.get() to use externalized messages for it's prefixes and color codes.  This does present some problems, but it actually works fine at runtime and also for junit tests too.


* **Setup a getConfigString in the Platform with a default value.** 


* **New placeholders for mine block stats: Renamed percent to chance. Added prison_top_mine_block_remaining_bar_nnn_minename.**


* **Used the wrong numeric formatter... needed to be an integer.**


* **Added 10 new placeholders to provide stats on mine blocks.**
These use a series value in the placeholder to specify the specific block.  The series number is not limited and can range from 1 through a large reasonable value.  The number can be one or more digits, with or without a left padding zeros.


* **Renamed the field PrisonBlockStatusData.getResetBlockCount() to getBlockPlacedCount() to be more descriptive of what it actually is.**


* **Add the ability of a PlaceholderFlag to self identify as having a sequence.**
This will allow for automatic processing of these kind of placeholders without having to hard code for them.


* **Placeholders test: Fixed issue with player name being incorrect and also added player information if it is provided in the output.**


* **Fixed a typo that was made a long time ago.  Placeholder has a capitol H in the name.**


* **Increased the number of rows displayed when using the placeholder search.  It was showing six placeholders and I increased it to 10.**


* **Renamed the PlaceHolderFlags from PLAYERMINES to MINEPLAYERS which is more consistent in how it is being used.**


* **v3.2.10-alpha.1 2021-07-05**


* **Bug fix... when selling on each block break event, it needs to suppress the per-sale messages.**


* **Fix a few other issues with the new placeholders.**


* **Enhance the /ranks autoConfigure to work much better with existing mines and ranks when doing a force.**


* **New 18 new placeholders added to prison.  Hooked up the following placeholders, but have not yet ran through testing yet.**
prison_rank_ladder_position (prison_rlp), prison_rank_ladder_position_laddername (prison_rlp_laddername), 
prison_rank__ladder_position_rankname (prison_r_lp_rankname), prison_rank__player_cost_rankname (prison_r_pcst_rankname),
prison_rank__player_cost_formatted_rankname (prison_r_pcf_rankname),   prison_rank__player_cost_remaining_rankname (prison_r_pcf_rankname),
prison_rank__player_cost_remaining_formatted_rankname (prison_r_pcf_rankname),  prison_rank__player_cost_percent_rankname (prison_r_pcp_rankname),
prison_rank__player_cost_bar_rankname (prison_r_pcb_rankname)


* **Refactored ranks and RankLadders to eliminate old and inefficient ways to get the ranks and next ranks.**
These change will help improve the performance of processing the placeholders.
This also allows the elimination of a few functions that are now obsolete.


# v3.2.9 2021-07-03

- release v3.2.9


# v3.2.8.1 2021-06-18


* **Note: Bug fixes for 3.2.8.**

* **Fixed a failure on startup for new installations of prison.**
Basically it was unable to deploy the language files due to try-with-resources closing the initial zip connection.


# v3.2.8 2021-06-17

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
  
