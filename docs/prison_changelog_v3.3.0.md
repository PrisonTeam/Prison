[Prison Documents - Table of Contents](prison_docs_000_toc.md)

## Prison Build Logs for v3.3.0

## Change logs
 - **[v3.3.0-alpha - v3.3.0](prison_changelog_v3.3.0.md)**
 - [v3.2.0 through v3.3.0-alpha.17](prison_changelogs.md)
 
* [Known Issues - Open](knownissues_v3.2.x.md)
* [Known Issues - Resolved](knownissues_v3.2.x_resolved.md)


These change logs represents the final changes to v3.3.0.


There were numerous changes that were made, here are some of the highlights:

* Support Spigot 26 - Preliminary, but functional
* Support Spigot 1.21.11
* Updated libraries that Prison relies on:
  * XSeries
  * NBT-api
  
* Preparing for the v3.4.0 v4.0.0 releases
  * Cleaning up a lot of old code
  * Removal of a lot of commented out code
    * Started with about 188,580 lines of code
    * Ended with about 170,148 lines of code
    * Removed about 18,432 lines
    
* Added support for Spigot 1.21.10 and 1.21.11, including handling for new unknown blocks.  

* Initiated an architecture split into legacy (Java 1.8) and modern (Java 21) builds to maintain broad compatibility.  

* Upgraded core libraries, including item-nbt-api to v2.15.5 and XSeries up to v13.8.0, to support modern Spigot releases.  

* Massively improved performance for large servers (tested with 46,000+ players) by disabling pre-adding players on startup and removing bulk Bukkit offline player lookups.  

* Fixed multiple placeholder bugs, including NullPointerExceptions for offline players and incorrect player objects preventing mineplayer placeholders from working.  

* Added Premium Vanish support to reject block break events from vanished players and admins.  

* Fixed bugs related to selling inventory items held in the off-hand.  

* Fixed mine bomb item stack duplication issues that were causing lore and NBT data loss.  

* Introduced the ability to throw mine bombs with configurable throw velocities.  

* Added full Bukkit Entity support for mine bombs to enable complex armor stand animations like starburst, orbital, and bounce.  

* Added an option to bypass block validation, allowing the mining of falling sand and player-placed objects within mines.  

* Introduced a new BackpackEvent API to allow external plugins to hook into Prison's backpack auto-pickup and auto-sell processes.  

* Added an InventoryFullEvent to signal when a player's inventory becomes full.  

* Added a {range: <low> <high>} placeholder to generate random integer values within commands.  

* Added new global command placeholders {ifPerm:<perm>} and {ifNotPerm:<perm>} to conditionally control command execution.  

* Implemented support for highly customizable complex placeholders using the prison__ prefix.  

* Significantly enhanced sellall compatibility for handling custom blocks and blocks with custom names.  

* Added the /mines debugBlockBreak tool to test and troubleshoot block breakage events using external tools.  

* Completely rewrote the startup air block counting sequence to process one mine at a time, eliminating massive TPS lag.  

* Enabled the use of enchantments to trigger auto features like auto pickup, auto smelt, and auto block.  

* Created a /mines block preventDrops feature to stop specific blocks from dropping items.  

* Updated player file name formats to include player names and safely support Bedrock player UUIDs.  

* Introduced a /ranks reload players command to safely reload player files without requiring a server restart.  

* Added support for hex color codes in mine bombs and text formatting.  

* Integrated new economy support for EdPrison's economy and The New Economy.  

* Added zEssentials and zMenu as soft dependencies to ensure proper plugin loading order.  

* Fixed block selection rounding errors by using floor integer comparisons to prevent selecting the wrong adjacent block.



---------------------------


These change logs represent the work that has been going on within prison. 

# 3.3.1 2026-06-15

** v3.3.1  2026-06-15**

  Prison-3.3.1.jar <-- Support for spigot 1.8 through spigot 1.20.x
  Prison26-3.3.1.jar <-- Support for spigot 1.21.x and spigot 26 (manual build)
  
  NOTE: the latest release of paper will be fully supported in v3.4.0.

* **Updated gradle config files && bumped to version 3.3.1**


# 3.3.0-alpha.19j 2026-06-13


* **Update the version of XSeries x11.3.0 to support 1.8 through 1.21.x and the newer version v13.8.0 supports spigot 2026.**
Update the goals for future development projects.


* **Prison Cleanup: prison-spigot: Removed obsolete and unneeded comments and fix indentation where needed.** Fifth and final part.  All basic clean ups has been completed with this set.


* **Prison Cleanup: prison-spigot: Removed obsolete and unneeded comments and fix indentation where needed.** Fourth part.



* **Prison Cleanup: prison-spigot: Removed obsolete and unneeded comments and fix indentation where needed.** Third part.


* **Placeholders: Bug fix.  The wrong player object was being used with the placeholder processing**, so it was not able to identify the player's location, which was preventing mineplayer placeholders from working.


* **Prison Cleanup: prison-spigot: Removed obsolete and unneeded comments and fix indentation where needed.** Second part.


* **Prison Cleanup: prison-spigot: Removed obsolete and unneeded comments and fix indentation where needed.** First part.


* **Bug fix in the ChatTest Junit test. When I was cleaning up that source I accidentally introduced a stray '.'.**


* **Prison Cleanup: prison-core: Removed obsolete and unneeded comments and fix indentation where needed.** Fifth part.


* **Prison Cleanup: prison-core: Removed obsolete and unneeded comments and fix indentation where needed.** Fourth part.


* **Prison Cleanup: prison-core: Removed obsolete and unneeded comments and fix indentation where needed.** Third part.


* **Prison Cleanup: prison-core: Removed obsolete and unneeded comments and fix indentation where needed.** Second part.


* **config.yml: change the default setting on adding new players on startup.  This is a major problem on large servers.**


* **Design documents: Starting to create some of the design and planning documents that will be needed in the next few phrases of Prison.**


* **Prison Cleanup: prison-core: Removed obsolete and unneeded comments and fix indentation where needed.** First part.


* **Removed some uses of the old block model. Breaking change: This removal should not actually break anything, but if upgrading from a version using the old block models, upgrade to an earlier release first to take advantage of the automatic conversion.**

* **Removed a few other unused source related to the old non-functional troubleshooting.**


* **Renamed an enum to BlockEventCustomPlaceholders since it was not clear it was related to block events and that this has nothing to do with the standard placeholders.**


* **Prison Cleanup: prison-ranks: Removed obsolete and unneeded comments and fix indentation where needed.**

* **Prison Cleanup: prison-mines: Removed obsolete and unneeded comments and fix indentation where needed.**


* **General cleanup: these are works in progress (wip) that have not been finalized.** If it's source, its been moved to a package with .wip.in the name.  These may be deleted in the future or they could be used.



** v3.3.0-alpha.19j 2025-12-10**




* **Bug fixes: There were some issues with a mine existing in the world that has not been reset, and as such, it had block types that XSeries did not recognize when running spigot 1.21.11.**  These fixes prevents prison from crashing and is able to perform a mine reset so they can be replaced with known blocks.


* **Better logging messages when something goes wrong with the air-counts.** Also, prevent an error in one mine from being displayed for all remaining mines.


* **2026-02-24 Major cross roads: Spigot 1.21.11 requires XSeries v13.6.0, but prison is stuck at v11.3.0 due to support for spigot 1.8.**
  - To move forward, I'm going to split prison in to a legacy build and a modern build. So the build will create two artifacts.  The source code will remain the same for both, but it will be different java versions it will be compiled with, and different versions of libraries.  Java 1.8's builds will be locked in to some plugin versions that cannot change, such as XSeries v11.3.0.  Others will be able to be upgraded and use the latest.
  - XSeries (block control within prison) will have to be spit... Spigot 1.8 cannot use any version newer than v11.3.0.
  - Java: The legacy build will continue to be built with java 1.8.  But the modern build may use java 21.  I will have to figure out what is ideal, and what other libraries are using.
  - Since the source will be identical, legacy builds should still get bug fixes and new enhancements.
  - Along with this change, I'm wanting to retire the v3.x.x and release v4.0.0.  There are a few core things that will change with the v4.0.0 release.
  
   

* **2026-02-23 Upgrade item-nbt-api to v2.15.5 so it can support spigot 1.21.11.**



**Spigot 1.21.10 support added.  The addition of this new version has resulted in new blocks that are not recognized by the version of XSeries that prison is using.**
Could not upgrade Xseries about 6+ months ago due to breaking changes that were causing failures with spigot 1.8 and a few other old releases.  These changes just ignores the unknown blocks, since prison would not be able to use them anyway.
Not sure how to deal with XSeries, but may have to split the builds.
There were also issues with building the sub-project prison-misc so that sub project was disabled since it's not directly being used.  The issue is that there is a resource in that project that is depending upon a specific library which is part of the bukkit 1.13.2 build, but it's using the repo from spigotmc.  Will address this later, when needed.



** 2025-04-13  ** **Placeholders: Bug fix.  When trying to access information through placeholders for an offline player**, it was throwing an NPE because prison was not able to get that player object from bukkit.
This bug was from a major change to try to get away from using so many calls to bukkit for a specific offline player since it's a very expensive operation because bukkit reads every player setting file until it finds the correct one.  So on servers with over a few thousand players, it can contribute to a lot of lag with a few player requests.



** 2025-07-26  ** **Mine bombs: Identify the name of the mine bomb in chat.**
I don't recall the significance of this addition, but there was an issue with not knowing what mine bomb was active?


** 2025-09-14 ** **Premium Vanish: If a player is using Premium Vanish and they are vanished**, then prison will reject all activity from that player and will cancel the block break event.
This is to prevent a vanished player (admin) from accidentally breaking blocks in mines while vanished.

** 2025-07-26 ** **Bug fix: Prevent an NPE when a sellallItem is null.**  Generally this should not happen, but there was another bug that caused that object to be null, so this fix is just an extra insurance.


** 2025-07-28 ** **Fix a bug with inventory items being held in the off-hand.** If I recall, this was a potential exploit under some circumstances.  Added support for selling through the off-hand, and provided fallbacks for older versions of spigot, such as 1.9.


** 2025-07-26 **

* **Players: Bug fix. If unable to get a player's default rank, then return a null from the function.**
 It was recording that an error happened, but it was returning a null, but continuing on with the processing as if it has a valid rank.  This was not a commonly occurring bug, but was happening with edge case testing.



* **Mine bombs: Fixed an issue with the item stack not correctly duplicating itself when transitioning from an item in hand to being placed or thrown.**
As such, lore and NBTs were being lost or removed, which resulted in the mine bombs not being recognized when going through the explosion processing.


* **Mine bombs: Added a mine bombStatus so it's better documented if a mine bomb was successful or not.**
Toned down the status message after a mine bomb is submitted since it sounded like it may always be a possible failure when it really isn't.  The bombStatus will now be a clearer indication on success or not.


* **Mines: Startup air block counts: Changed slightly to start this task later, and to put a slightly longer delay between mine counts so it puts less load on the server.**



* **Mine Bombs: Fixed some issues with mine bombs, such as prison is now check all "throw" events, even if another plugin cancels it.**
Eliminated a few NPEs since some of them are now being triggered since validation can be turned off.



* **Mines: Support falling sand and player placed objects.**
Prison normally does not support mining blocks that it did not place in the mine. The reason is to prevent players from getting credit and money for mining invalid blocks, of which, could lead to other problems.  But this also prevents mining sand blocks when they fall because their supporting blocks were removed.
This change now turns off prisons validation of the blocks that are being mine, which will allow mining of fallen sand.  As a side effect, it will allow mining of any block found in the mine, for better or for worse.  
To enable this, start prison so prison can modify the autoFeaturesConfig.yml file.  Then edit it and set the following setting to false:
'validateBlocksWerePlacedByPrison: false'


* **3.3.0-alpha.19i 2025-06-26**


* **Mine Bombs: Bug fixes. There are a few bug fixes in these changes which were resulting in mine bombs not working at all, or causing some odd behaviors.**
One internal change was to pass the mine in to more functions so some of the processes can be more mine aware, and to prevent trying to find the mine a second time. Benefit is reduced processing and slightly faster speeds.
One issue was that the initial block being processed for the explosion was not actually in the mine when it was on the surface, so prison would ignore that event. Fix was to shift the block down in to the mine so it would be usable. This was primarily when the Y-offset was set to ZERO.  If it was any negative value, then the bombs would work well, but zero was the default value.
Some messages sent to the console have been enhanced to provide better clues with what's happening when in debug mode.  So if it is a config setting that is preventing the bomb from being tied to the mine, it will be more obvious.


* **Locations: Fixed a potential problem with getting the wrong block, or processing the wrong block.**
Found the older code was rounding doubles within the block, so instead of being able to confirm that the given block was the same as another, if the position was greater than or equal to x.5 then it would round up to a different block next to it.
Fixed by using floor and comparing integer values so the selected block will always be the same block.
I am not aware of any specific reported bug related to this problem, but it would have occurred around selecting blocks (like when laying out a mine) or be an issue one the edge of mines where if a block is hit near the edge the rounding could have checked the block outside of the mine instead.  I suspect occurrences of this bug were not too frequent, and appeared intermittent.
Note that in the prior commit, BoundsTest class was also benefiting from this fix and should have been included in this commit.


* **Bounds: Fixed an obscure bug where changing the World was not updating existing instances of a world.**  This would mostly be impacting unit tests, so this would never have been an issue with actual running of prison.
Cleaned up the equals since something was not looking correct with some of the logic.  Nothing really changed, but slightly altered to simplify the logic to ensure there are not problems.



* **Mines: Last block break bug fix.  Fixes an issue where the task to break the last few blocks is getting canceled on some servers because the mine reset is running before the blocks can be removed.**
The mine reset was canceling all tasks, when it should not be. The idea of canceling the tasks was if there was another reset being submitted, but that cannot happen due to the mutex.


* **Blocks: added an isPassable() function to the compatibility functions since this function does not exist in bukkit versions less than 1.14.**
This is used when throwing bombs.



* **Change logs: Restructured the change logs so the primary file is not quite as large.  It was starting to run in to some lag issues when editing.**



* **Prison was hard coding the teleport message.**  Not sure why it was not hooked in to the language files, or if it was, why it was removed.
Rehooked it up.


* **Added zEssentials and zMenu to Prison's soft depends so prison will load after those plugins.**
There was an issue with prison trying to access vault's economy before zEssentials could properly hook in to it.  This should allow zEss to fully enabled the economy now.


* **3.3.0-alpha.19h 2025-04-07**
The build for alpha.19g did not reflect the correct version.  So incrementing the version so it is clear if someone is using the correct version or not.



* **3.3.0-alpha.19g 2025-03-31*

* **These was a problem with hex color codes being used in lore (or elsewhere probably) where they are not being translated unless there is another color**, such as &7 anywhere else in the String.  
The problem was that the hex conversion was working perfectly well.  But since the "dirty" variable was not getting modified, since the hex colors were applied before that point in processing, it would always revert back to the original unchanged String value because it thought there was nothing that changed.
To fix the problem, I eliminated the dirty variable and am always converting the byte array back to a String value.  So no need to check if dirty anymore, since it always converts.


* **MineBombs: Fix the way players are used to resolve a few issues with the player objects.** 


* **Ranks: Change the way ranks player get the player objects, especially when the player is offline.
This fixes a few issues.**


* **Prison Utils: Changed the way the platform players are used to resolve some issues when the players are offline.**


* **PlayerCache Timer Task: Redo the submission of the task to be self contained and will not initiate the task if the Ranks are disabled.**


* **Player: Change the way to get a platform player.  Tie it to the Platform, and to use a new way to construct the SpigotPlayer and the SpigotOfflinePlayer** by creating a static function that can take a RankPlayer object.
This fixes some of the issues with getting the wrong bukkit player object, or none, even when the player is online.
By putting the bukkit code directly in the SpigotPlayer and SpigotOfflinePlayer objects, it's able to bypass some of the abstraction that was found in the platform class.



* **Placeholders: added a comment to better explain the hex colors.**


* **Ranks: Fixed a few issues with '/ranks player' be issued by the player.**
Added last seen date to the player objects.


* **MineBombs: Added the new cooldown message that is in the MineBombCooldownException to the core messages.**


* **Sellall: Ran in to a situation where an ItemStack that was being processed did not have a bukkit ItemStack internally, so it was causing an NPE. **
This fixes the issue by checking to ensure the bukkit ItemStack exists first.



* **BackpackEvent: New api using a backpack event to allow other plugins to hook in to the prison backpack processing which will behave as a new Backpack Integration.** 
This will allow anyone to easily hook in to Prison's generic backpack behavior for auto pickup and autosell.
This new event is tied to the new IntegrationBackpackAPI.  Basically, to perform a backpack operation on an unknown backpack source, the BackpackEvent collects a list of Inventory objects from the backpack, which allows prison to operate on the generic Inventory, and then it offers the use of a CallBack that will allow the backpack plugin to process the results of the actions against the inventories.



* **MineBombs: Players: Ranks disabled: Made some changes to how players are being loaded in the 'bomb give' command to allow it to work when ranks have been disabled.**


* **MineBombs: Eliminated cooldowns from the 'utils bomb give' command and from prison checking to see if an items is a mine bomb to determine if the event should be handled.**
This should get rid of the bug where using bomb give too frequently would result in error messages and lost bombs (if a player is paying for a bomb).


* **MineBombs: Added a cooldown exception so cooldowns can now be handled and reported correctly.  Before the "bomb not found" message would be used, which was confusing.**


* **MineBombs:  Improvements to the command '/prison utils bomb list' command to better format the information and include some missing details.**


* **Bug fix: CommandSender: Was causing a type cast exception when trying to TP a player from the console.**
The CommandSender was assuming the sender was always a player object, when it shouldn't have been doing that.


* **Bug fix: CommandSender: Was causing a type cast exception when trying to TP a player from the console.**
The CommandSender was assuming the sender was always a player object, when it shouldn't have been doing that.



* **Players:  CommandSender:  Added a new transient field miscText that can now be used to return a message to the caller.**
For the command '/mines tp' if a player tries to tp to their current rank's mines, and if that rank does not have any mines connected to it, it will now step back through all prior ranks until it finds a mine to TP the player to.
This returns a message that can be used, indicating the current rank, but indicates the next highest rank that had a mine.
This message is not yet externalized.  Including a TP message indicating where they player has been TP'd to.



* **CommandHandler: Expand the tracking of command stats to track the usage of aliases.**
The command '/prison support cmdStats' has been updated to include alias usage counts.
Usage of aliases would not be tracked at all, even the primary command.



* **AutoManager: PEExplosionEvent - remove a debug statement that was showing up in the console when not in debug mode.**


* **InventoryFullEvent: Start to setup a prison based event that will fire when prison detects that the inventory object is full.**
This is useful for when other plugins, or customization to prison needs to perform an action when the inventory is full, such as handling a custom sell all action.
This new event offers cancel, but there is nothing to cancel within prison since canceling the event will not alter the inventory.  It can be used to signal to other plugins that the event was handled by another plugin.


* **Mines block preventDrops: Improve the help documentation so it's clearer on what it actually does and how to use the command.**


* **PrisonRanks: A couple of other changes to help ensure there are no issues if Ranks are disabled.**


* **Ranks: Setup the PrisonRanks so getInstance() will not return a null and to ensure that isEnabled() returns false if Ranks is not enabled through module configs.**


* **RankupMax: Fix  a bug where after successfully ranking up a few ranks, it always shows an error message related to the last attempt because the player cannot afford the next rank.**


* **Rankupmax broadcast bug: Sending the console log message to all players and the player too.**
Now sends the broadcast message that is associated with the normal rankup command.



* **Prison debug: Add a 'commandHandler' to the debugger activeTargets so that way you can selectively enable a commandHandler log message outside of if debug is enabled or not.** 
This is to troubleshoot if bukkit is actually passing commands to prison correctly.



## 3.3.0-alpha.19f 2025-02-15

* Support for spigot/paper v1.21.4
* Bug fix: NBT-lib.  If NBT-lib fails, prison no longer allows the stacktrace to fill the console/logs. This library needs to be updated with each release of paper/spigot and when it is out of date it can throw a lot of errors.
* New prison command placeholder: `{range: <low> <high>}`.  Can be used in rank commands, mine commands, ladder commands, and even Block event commands. This can help insert a random number in your commands, such as a random number of items that you may give a player.
* Bug fix: Players when reloading players, or on server startup, player's ranks could have been reset. This fixes that issue.
* Bug fix: Rankups. Changes to how internal references to players caused some problems on some commands by not returning the correct player objects.  See next item.
* Bug fix and enhancement: There was an issue with how prison would get player objects from bukkit. It generally wasn't a problem, unless you had over a few thousand players. The problem was that prison would get OfflinePLayers from bukkit, and that would cause bukkit to lag the server badly since it would consume a lot of resources and time trying to find player files within the bukkit world folders.  Prison no longer asks for OfflinePlayers.  Prison was tested with 35,000 + players and the issue is now resolved.
* Sellall and Blocks: Updated some of the internals in sellall to be able to better support custom blocks that have been added to sellall, but are not within prison.  Prison cannot spawn these blocks, but it can sell them now.
* 



* **Placeholders: Updated the docs to include the new command placeholder: '{range: }'**



* **Players: On reloading players, and also sometimes on server restarts, player's ranks were being reset back to the default ranks on each ladder.**
This was caused by a bug where the rankId was trying to be used instead of the rank name.  RankId is obsolete, but is still in the code so older player files can be loaded and converted to the new format.
The code that handles this conversion, and the basic loading, was rewritten to properly handle the rank name and rankId if it is still required.


* **Sellall: Removed use of ConfigurationSection since it works fine until a new value is being added.**
It was easier to roll back to how it was than to add a bunch of other code to add it if it does not exist.


* **Prison Commands: Added a new prison command placeholder '{range: <low> <high>}` that can be inserted in commands used in ranks, mines, or block events.**
This inserts a random integer value in the specified range.
This is good to provide some variation when issuing commands like give or other type of events.


* **Prison versions: Updated the internal references to java versions up to java 29.**
25 is the highest official value, but extended it by four extra versions just in case this is not revised between now and then.


* **Rankup: Found that a parameter that should not be null was being passed null in a few places.**
This was a problem in a few places for 'rankupmax'. 
The RankPlayer object that was expected was available so it was a simple fix.


* **NBT & MineBombs: addresses an issue with NBT-Lib caused stack traces when used on a newer platform that the NBT lib has not been updated for.**
This also fixes the issue (my fault) where the exception that was being thrown was NoClassDefFoundError extends from Error, not Exception, which is what the try-catch was trying to intercept.
This now will prevent the exception and it will fail silently.


* **Rankup: Fixed a problem with rankup commands not using the correct player objects.**
Simplified how some of the player objects are used to eliminate this issue.


* **Startup block checks: Had to make changes to the handling of XMaterial since the latest versions are more brittle since they tend to be conflictive with itself.**
There are now a few block types that actually cause problems if used or accessed.  
It appears as if 1.21.4 made major changes in how some blocks are identified, and XMaterial has not yet fully caught up with those changes?
This change captures exceptions caused byXMaterial, when in the past, it would only return a null value instead of throwing an exception.


* **Sellall: Major changes to better support custom blocks.  Not perfect overall, since some aspects are not fully handled, such as enchantments or filters on specific nbt values.**
These changes fixes most of the issues with identifying custom blocks and allows them to be sold.  This also fixes a lot of the internals on sellall, which is forcing a lot of movement away from XMaterial and relying more on PrisonBlocks.
Added better error messages to many functions, instead of just a generic stack trace.  Need to do many more.


* **NBT-Lib: Upgrade from v2.14.0 to v2.14.1 so as to better support v1.21.4.**


* **Ranks: Some rank commands were failing to work properly because they were not getting the correct player.**
Instead of getting the play by name, it was using the sender, which would have been the player issuing the command.
This was an issue with '/ranks promote' and '/ranks demote' when issued in game.
This was fixed by only allowing the named player to be resolved, without giving the sender an option to be used.  This was changed in a number of areas to help protect form possible errors.


* **PrisonBlock: Add isSellallOnly to prevent blocks added through sellall from being used in mines.**



* **Sellall: Much better support for blocks with custom names.**  Both for rejecting the wrong blocks, but also for now being able to sell them.
These changes are not perfect  and does not support some conditions.
Prison currently does not support multiple custom blocks with the same material types and the same display name.
These blocks that are added through sellall should never be used within mines because prison cannot create those blocks to place in the mines.
New command:  '/sellall addHand'.  Improved a few other commands too such as '/sellall list' and '/sellall items inspect'.


* **PrisonBlock:  Significant modifications to support non-prison blocks for sellall.**
The alters many behaviors to allow for almost any dynamic custom block name, outside of the supported custom items.
Created constants for the selection wand, both for the PrisonBlock and the item stack.
These changes will support sellall's use of more dynamic block names that are not within prison.  If it is a custom block name, then it will be added to prison's list of blocks.  These should not be used in mines since prison cannot generate them faithfully.



### Version 3.3.0-alpha.19e  2024-12-14


* **Updated nbtApi from v2.13.2 to v2.14.0.  This should provide better support for spigot 1.21.4.**

Tried to update XSeries to v12.0.0 but it failed.  It does not appear to work with spigot v1.13.2, which is the version which prison uses for building it's jars.  Will have to figure this out later, maybe their release v12.1.0 will address these issues.



* **Doc updates: Provide more information on '/mines set resetThreshold' and the new command '/mines debugBlockBreak'.**


* **Mines: Debug block breakage.  Created a new command '/mines debugBlockBreak` that will now be able to use tools from other plugins.**
Realized there was a problem with using the mine wand when testing block breakage in that you could not test with any other tool, which would prevent other plugins from handling the events like they should.
So now, by holding any tool in your hand, and issuing the command '/mines debugBlockBreak' it will test the block breakage with whatever they are holding.


* **Players: Fixed a problem with getting  the vector for where the player is looking.  The actual vector was getting lost so the result was that nothing was able to be properly calculated.**
This was fixed by creating an internal prison vector which could be used instead. 


2024-12-14

* **Prison was gifted over 46,000 players!!!** 
Someone who was running in to performance issues with prison on a large server, gifted me over 46,000 players in both prison and in bukkit.  Now I can perform various testing with larger player bases to help fix and prevent performance issues.


* **Major Prison performance cleanup:** When there are a few thousand players on the server and in prison, prison was running in to major performance issues that was basically killing the performance of the server.
One major cause was with the startup routine of trying to add players that are on the server, but not in prison.  This could run for literally days and consume tons of processing resources.  So prison no longer tries to pre-add any player.  They have to join the server to be added.
The other major problem, was that if prison was trying to lookup a player that was not online, it would hit bukkit's offline players, and it would get a full list of all offline players.  This was horrible!  Bukkit would have to read all player files to generate the offline players listing, which could take a long time.  For example, with 46,000 players, it could take a few minutes for bukkit to fully load all players, and during that time, paper starts generating tons of warnings stating the server is not responding correctly.  It does not kill the server, but it generates mega bytes of errors in the log files.
So to fix this problem, prison no longer uses the bukkit offline player's functions unless trying to access ONE offline player using strictly their UUID.  A name search would require almost all files to be searched too.
As a result, prison is far more stable for very large player bases on servers!!  This is a major improvement.  In the past there has been complaints about prison when there were a lot of players, but I was never able to zero in on the problem since no one was really willing to work with me on figuring this out.  What helped a lot was being gifted over 46,000 players. :)  So now I can do some serious testing with many players.


* **Rankup:  would not work for ladders other than default and prestiges.  Fixed.**


* **Update xseries to v11.3.0 from v11.2.1.**


* **Mines: checking access.  This change breaks down the access checks to see if a player has access to a given mine so it can be properly logged when in debug mode.**
It's been a problem trying to figure out why some events would work or wouldn't, now this will help identify access related conditions.  Example is the use of mine bombs.


* **Mines: Hooking up a test dump to the mines commands so I can more easily test what JSON is generated with the current settings and configurations.**
 
 
* **Mines: Hook up the reconnectObjects() function so it will be able to hook up the dependencies.**
This is not hooked up yet, but will be used with the new ORM json manager.


* **Mines: adding transient to Mine data so it will not try to save the temporary and transient data when using ORM is generating json data from it.**


* **Ranks: a user reported a null pointer issue so made some changes to prevent it.***
Was not able to reproduce it.


* **Mines: relocate a few objects to pull them out of MineData since they are constants.**


* **Mines: reconnectObjects() is a way to re-hooked up objects that could not be saved/stored when loading these objects from the database/files.**
These currently are not enabled or tested yet, but they are the foundation of getting the new process working to support a new way of storing and loading data.


* **Auto Features: clear bukkit clearDrops.**
An issue using an older version of prison resulted in the targetBlock being null. This has not been reproduceable with the current version of prison, so it's probably been fixed at it's root levels.  But this change was made just to ensure it does not become an issue in the future.


* **PrisonBlock: Fix an issue with PrisonBlock setting a class variable that is also set within it's parent class.**
Overall, at a rough level, this does not cause a problem with the current prison environment.  It's wrong.  But it works. 
Where it is failing is when prison is trying to setup ORM on the Mine object to simplify and expand the capability of future enhancements..


* **Mine Bombs: Improve the debugging information on when mine bombs are selected, or rejected based upon the player having access to the mine or if a cooldown has rejected the mine bomb.**
SpigotPlayer: Using bukkit's block.isPassable() on getLineOfSight(), but may need to be extended to getLineOfsightExactLocation().
It was not clear why mine bombs were not working.


### 3.3.0-alpha.19d 2024-09-26**


* **Prison blocks: It was realized that due to a recent expansion of the items that are included in the default for sellall, that the first 27 or so entries, were the blocks that were used in the auto generated mines when running the command '/ranks autoConfigure'.**
These first few blocks, were returned to their proper place so the mines are properly populated again.
Also, since blocks from multiple versions of spigot have been added, new code has been added to prevent adding any block that is not support by the version of spigot that the server is running.


* **Sellall startup messages:  Fixed some formatting issues that were causing a misrepresentation of the sellall configurations within the startup logs.**


* **Prison Backpacks: Found that the startup messages for backpacks was using references to sellall configs, which was incorrect.**
The messages are now corrected.  This error appeared as if sellall was being setup and configured twice, which it was not.


* **Sellall: The sellall initialization function was being called twice by error.**
As such, this prevents a duplicate message from appearing in the startup log.  No problems were caused by calling initialize twice, but it was unneeded.


* **Mines: Startup air block counts.  Bypass processing if no mines exist.***
This prevents an error when starting prison for the first time before any mines can be defined.


* **Mine Bombs: When loading the default bombs, this change now prevents the display of "warnings" which are actually normal because the settings for the wrong spigot platform must be removed.**
This now uses the check to see if the mine module has been enabled, and if it has mines, if not, then it will not purge any of the mine constraints since the server is probably under construction and they should not be purged until the mines have been setup.


* ***Prison modules: Added an element count which reflects how many elements are loaded for each module.**
  Examples would be ranks and mines, or number of prison utilities that were enabled.


* **File storage: Prison was changed a few weeks ago to precheck some of the critical folder structures to ensure they are there.**
That change was causing problems when creating a new instance of prison since the old code would fail if the directories were already there, which made no sense.


* **WorldGuard Regions: Enable the commands to be able to be ran from the console and through online players.**
Disabled the Mine-Area commands related to the WorldGuard regions because mine areas are not yet implemented.


* **Player: the code that gets a player object based upon a name has been altered to address a few issues.**


* **WorldGuard Regions: Updated the configs to include placeholders for the world, and updated the code to support it too.**


* **Custom placeholders: Fix an issue with the custom placeholders changes being able to correctly count the aliases in the command `/prison commands list`.**


* **Custom Placeholders: Added support for a more complex custom placeholder where the default is an abbreviated placeholder (simple) and a more complex expanded placeholder where you can set various options such as enable PAPI expansions and adding descriptions.**
Added custom placeholders to be included in the `/prison placeholders list`.
To view all custom placeholders, as translated, search for the custom placeholder prefix:
`/prison placeholder search prison__`



* **Added a new function to the platform to check to see if there is a specific configuration section.**


* **Custom Placeholders: A custom placeholder is identified with the prefix of 'prison__' but one of the functions that cleans the placeholder fragments sent to prison, was removing one of those underscore characters.**
This preserves the needed underscore and allows for proper matching and identification.


** 3.3.0-alpha.19c 2024-09-20 **


* **Build automation: github has deprecated and disabled version 1 and 2 of the actions, and v3 is slated to be disabled in Nov/Dec of this year too.**
So moving on to v4 to allow for better future use. 
This prison build script is very simple and therefore the newer issues related to v4 when upgrading from older versions, should not impact prison.  Basically the largest change is that these artifacts are now immutable.


* **Mine bombs: Not a significant change. Allowed the mine bomb data to be part of the event that is being passed along so it can be used in later updates and future features.**


* **Placeholders: Realized that the "uppercase" integration hook for placeholder API is not used.**  
Realized that placeholder registration is done without sensitivity to the case used.


* **Custom Placeholders: Created support for custom placeholders that are defined in the config.yml file, and one simple short custom placeholder will be replaced by any combination of text and other placeholders.**
This way you can define complex placeholders within prison, and then use the short custom placeholders in other config files, which will help a lot if you are limited by characters.


* **Auto features: PrisonEnchant's listener: minor changes to only display the registration notice once.**


* **Removed various comments that were auto generated.**


**v3.3.0-alpha.19b 2024-09-09**



* **Block Break Listeners: If in debug mode, and there is a fast fail, or an ignore on the blocks being broken, log the reasons why.**


* **Minor: variable not being used, so commented out to eliminate a compile warning.**


* **Mines: Air Block Counts: A few more fixes. Found that 'position' was not being reset on each mine, which was causing partial, or no resets for most mines.**


* **Mines: Air block counts: Minor change to the completed message to be more consistent with the other similar messages.**
Changed "-" to "of".


* **Ranks and Ladders: Minor changes to prevent new ranks and ladders from getting assigned a non -1 id.**
All new ranks and ladders will have an id of -1.  All existing ranks and ladders will keep their ids.


* **Ranks and Ladders: Changed the way the ladder and rank file names are saved. Instead of using their IDs, it is now using their names.**
The file names are converted upon loading.  Once loaded, and converted, the prison config setup cannot be reverted, but the old settings and files will be saved in the saved directories and can be restored if needed.
Rank IDs and Ladder IDs are no longer used internally.  They are being kept for now, but will be removed in the future.


* **Mines: Air Block Counts: Improvement.  Rewrote the way prison is handling the air block counts because they were causing the servers to fall behind on TPS by a significant amount.**
The server, depending upon how many mines, and how large they were, the system (spigot, paper, etc) would warn that it was running behind by 50 ticks to a few hundred ticks.  The primary reason was that all the mines would be submitted to run almost at the same time, in different threads, so it would starve all available TPS.
These changes were a complete rewrite on how the jobs are submitted and the mines are processed.  Now it's just one initial job submission, instead of each mine submitting it's own task.  And that one new job steps through each mine, one at time, counting the blocks, and then moving on to the next mine.  As such, we are not ensuring only one task is trying to run at the same time, thus allowing other services to get sufficient access to the processing that they need.  Where a mine-heavy test server was reporting falling behind by 600+ ticks, after these changes there were no warnings.  During, and right after the air counts, the server is reporting a solid 20 TPS.


**Prison v3.3.0-alpha.19 2024-09-07**


* **Mines: Air block counting on startup: Change some of the setting to be less demanding on the server.**


* **Change the prison TPS monitor to a singleton.**
Will be using this in the near future to monitor system loads.


* **Mines: Minor change: Simplify how the command is ran so there is one exit point instead of two.**


* **AutoFeatures: PrisonEnchants (Pulsi's): Made changes to handle explosion events that happen outside of the mine, but there are some blocks that are within the mine.**
This prevents PrisonEnchants, or bukkit, from breaking the blocks within the mine.


* **Auto Features: Added support for 9 new Blocking combinations.**
These can be individually controlled to disable.


* **Auto Manager: Use RevEnchant's fortune.**
This may have been a bug, but not sure.  Was successfully loading Rev's fortune level, but it appears like the default minecraft fortune level may have overwritten it. If so, its now fixed.


* **Auto Features: blocking: Fixed a bug where 'blockAllBlocks' was incorrectly using 'smeltAllBlocks'.**
Fixed.  'blockAllBlocks' now works properly.


* **Auto features: PrisonEnchants plugin support.**  Adjustments to get it to work better for all versions.
Needed to have it perform the checks in two different areas.


* **Auto Manager: Bug Fix!  I realized by chance that the whole normal drop config was setup incorrectly!  This was fixed.**
What was wrong, was that the normal drops for smelting and blocking were not tied to the perms, lore, and enchantment activators!
Also I found that if auto features (auto pickup, auto smelt, and auto blocking) is disabled, then it was bypassing normal drop processing too.
So these fixes have normal drops correctly get used if auto pickup is not used.  For example, if auto smelt is enabled, but auto pickup is off, and auto pickup does not get triggered by lore, perms, or enchantments, then it will fall back to normal drops and will enable the smelting for that.


* **Mine reset: Not sure if this was a problem but found that the wrong value was being used as a key to access a set of blocks.**


* **WorldGuard Region support:  Starting to add WorldGuard region support to mines.**
These initially are intended to help create regions, update regions, and view the regions based upon the mine sizes.
There are some default command listings in config.yml so they can be customized and expanded to meet your server's needs.
The commands are: `/mines worldGuard region`


* **Startup directory check: Changed where and how the startup directory check is ran so it is actually included on startup, which it was skipping it.**



* **Mine block searches:  Changed them so if the command is ran from the console, it will list all items without any paging.**


* **Sellall Items: Expanded the number of default sellall items.**
Can use `/sellall items setDefaults` to add the missing items.


* **Auto Features: Can now trigger auto pickup, auto smelt, and auto block with the use of enchantments.***  
This adds to the ability to trigger them with Lore and permissions for greater flexibility in working with other plugins.
The enchantment names must be the full qualified name as found in the blockbreak debug messages that shows the details for that tool, or you can use `/sellall items inspect` while holding a tool with the enchantment you're interested in using.
For example, enable auto features, but keep the globals turned off, then enabled the custom enchantments and add 'minecraft:smelter' to both the auto pickup and auto smelt enchantments.  This enchantment is from another enchantment plugin and is not standard, but this is how it's listed. Note that you can also use the same enchantment in more than one option too.


* **Moved the Prison File System Check to the PrisonStatsUtil class and got it out of the SpigotPlatform.**



* **Player Cache: Fixed a problem with creating missing directories.... forgot to use the parent of the file, which is the current directory for the player cache files.**


* **Mine bomb: Added more features for control of the animations: **
radius, radiusDelta, alterateDirections, animation speed, and spin speed, and armorstand item location.
This is working pretty good overall, but still needs to do some work on the related spin.



* **Added a new feature to the prison versions information: Directory path checks.**
If a specified directory is missing, it will be created to prevent possible errors in other parts of prison.  See last commit.
This reports the path, number of directories and files, with the file's total size in that directory.


* **Bug fix: If someone deletes the playerCache folder, it was causing failures in trying to create new player cache files.**
Fixed by using '.mkDirs()' to ensure the path fully exists before creating the temp files.
Also eliminated the stack trace and replaced it with a one line entry in the console.


* **Blocks: Update the list of blocks that can be effected by gravity (fall when disturbed).**
Added falling_sand, falling_block, suspicious_sand, suspecicious_gravel


* **AutoFeatures: EntityExplodeEvent: Fixed a few issues with how it was setup. **
Apparently the event can be fired with zero blocks!  This now handles that situation and will bypass handling of that event, but it will log a warning in the console.


* **AutoFeatures: Changed the debug logging on block break events and explosions, to better encode them with color so important items stand out better.**
Plus added logging on features that were not covered before: If a mine reset was triggered, how many block events were submitted.
If the blockbreak task is submitted with how many blocks it's changing.
If minesweeper was submitted.


* **New feature: Mines:  Prevent a block from dropping drops!**
This can be used to setup something like lucky blocks, of which the player does not get paid for this block, nor do they get the block or whatever bukkit would normally drop.
The new command is:  `/mines block preventDrops help`.
This saves the new feature with the mine data, and works with normal pickaxes, explosions, and also with silktouch to prevent the drops.
The debug information for block breaks has been updated to properly report on how this is working within handling the events.


* **Sellall: Changed '/sellall items inspect' to print everything to the console, and to decode the lore so it shows all of the color codes.**
This is critical for understanding how lore is setup so you can use the auto features lore settings.


* **3.3.0-alpha.18d  2024-08-27**



* **ExcellentEnchants: Added support for org.bukkit.event.entity.EntityExplodeEvent.**
This fixes some issues where the initial changes were not fully finished.



* **Mine bombs: Minor adjustments.**
There still are changes that are needed, but I'm needing to post a new alpha release before I proceed to wrap up the mine bombs.  
They are getting very close to working as intended, and I'm wanting to add more features to the bombs, along with changing a few other things too.
But for now, a new alpha needs to be pushed out because other issues have been addressed and fixed.



* **Material validations: The validation process was generating a ton of warnings for all items and blocks that exist in minecraft, but yet cannot exist in an item stack.**  Such as wall hangings, water, etc... since they are fixed in the world, and when in itemstacks, they are represented by something else, such as an ItemFrame or a Bucket of Water.
I eliminated most errors, but reduced it down to a list of items.  Also starting to suppress items that are known not to be ItemStack-able so the list is greatly reduced.  Still need to work on suppressing others too.



* **Bug fixes: With spigot/paper 1.21.1 and the NBT lib that we are using, there were issue that were suddenly occurring that worked fine before.**
Basically, eliminated a lot of generation of stack traces, especially when dealing with NBTs.
Upgraded NBT-lib to v2.13.2 from v2.13.1.  
When formatting the rank list, the defaults were set to a value of zero.  But if there are no tags on any ranks within a ladder, then the width was being used with a value of zero.  This was causing a failure since it cannot left-position anything with a zero width. It was trying to format '%-0s'.  With a default of 1, it would  always use 1 as the minimum value, which works perfectly fine.


* **Fixed a potential problem with null ranks being loaded within the ladder loaders.**
I cannot find a reason why this was happening, or what data was triggering it, but this fixed the issue by skipping null ranks.


* **New Feature:  Support for Bukkit's EntityExplodeEvent which is what the ExcellentEnchantments plugin uses.**
owo

* **Mine bombs:  A lot of various changes and fixes to get them to work better.**
This new animation and mine bombs code needs more refinement and adjustments, but it's working far better than what it was.


* **Placeholders: Sellall multiplier:  Fixed a few issues related to being offline and even OP'd.**
When offline, it was not able to get an active player object so it was bypassing the sellall multiplier calculations.**
Now it can work with bukkit's offline players.  And its also able to fallback to using snapshot perms so the sellall calculations at least reflects what it was the last time the player was online.


* **Upgrade XSeries to v11.2.1 from v11.2.0.1. **
This may help with a few issues with spigot v1.21.1 blocks.



* **Sellall multipliers: Fixes some of the problems with sellall multipliers so they can work better when the player is offline.**
This will now use the snapshot of the player's perms as they were when they were last online.  This helps to ensure better accuracy when bukkit will not provide perms when the player is not online.


* **Ranks player: Added the capture and storage of the player's rank multiplier and changed the command '/ranks player' display these stored values for the multipliers when the player is offline.**



* **PrisonEnchants update: Updated support for Pulsi's PrisonEnchants plugin.**
The structure of the API changed with v2.0.  Another change happened with v2.2.1.
Prison now supports all versions from v1.0.0, v2.x, and v2.2.1 and newer.
Created a pseudo API class in the prison-misc project to be able to compile for all three versions of support.


* **Gradle: Modified the gradle configs to "force" it to compile with java 1.8, which is what it needs to be for it to work with spigot 1.8 through 1.15 or so.**


* **AutoFeatures: Add support for using RevEnchant's fortune enchantment level using NBT data.**
Not 100% this works because I cannot test it since I do not own the RevEnchants plugin.



* **BlockBreak: RevEnchants was firing one of their multi-block break events with NO blocks include. **
Had to handle a null condition and cancel the event.


* **Prison backpacks:  On the last commit for setting up the integrations for backpacks, I overlooked an issue when prison backpacks were disabled.**
This issue was fixed.  It was causing a failure on startup which prevented prison from loading due to causing the integration manager to fail.


* **Sellall: Enchantments: Prevent items with enchantments from being sold.**
Unlike lore, there is no way to bypass this.  Sellall cannot sell anything that is enchanted.
There was an issue that would "sell" enchanted items, but would not remove them from the inventory unless the player was holding it in their hands, which would allow the item to be sold multiple times.


* **Integrations: Backpacks and prison versions reporting of integrations.**
Fixed an issue with backpacks not being included in the sellall activity when they are enabled in the sellall settings.
Added BACKPACKs to the IntegrationType and made changes to include the listings of all backpacks and their status in the startup information and prison versions.



* **Economy: CoinsEngine: fixed a problem with using the wrong plugin signature.**
This now works as expected.  This was tested with gemsEconommy such that some currencies from both were actively being used.


* **Placeholders: Fixed a bug when the player is at the last possible rank on both the default and prestiges ladder.**
The fact that there isn't a next rank was causing problem when trying to get the next ranks ladder base multiplier, which does not exist.


* **Debugging info: Expanded the debugging info for the smelting and blocking to show the progressions.**
This is important for tracking and understanding how and if they are working.
Changed some of the coloring so it's easier to read and for various headers to standout.


* **Bug Fix: BlockBreakEvent ACCESS Priority.  This event was not being handled in the correct place, so other fast-fail checks were intercepting this ACCESS check.**
This now has three different results.... If outside of a mine, it will fail and cancel the event.  If in a mine and the player does not have access, then it will cancel the event.
If its in a mine and the player has access, then it will let the other events handle the event without it being canceled.


* **TheNewEconomy support:  I had a wrong method signature for one of the mock functions.  This should correct it.**
The jar was regenerated for java 1.8.


* **Mine Bombs: Updated text on the mine bomb list help details.**



* **PrisonJarReporter: Update support for java 22.**



* **Update the TNE API jar: TheNewEconomy_prisonBuild_v0.1.3.0.jar  It was accidentally built with Java 21 instead of Java 1.8.**


* **Bug fix: When using a file filter, handle the condition of the result being null.**


* **Add support for new economy: The New Economy:  Created dummy stubs to allow prison to build the new classes for the integration.**
Cannot use maven because those jars are built with Java 17 when prison can only be built with java 1.8.


* **Mind bombs: Minor updates to the BombAnimations class to realign a few of the settings. **


* **Mine bombs: Various updates to the general mine bomb defaults to include more of the newer features.**


* **XSeries: Upgrade XSeries to v11.2.0.1 from v11.2.0.**


* **Mine Bomb: Setup a simplified constructor for animations which will not use an item.**
The purpose would be for showing only the holographic name, which is needed for animations that are actually moving the armor stand.


* **Mine bombs: Hooked up the animationSpeed mine bomb setting to the bounce, infinity, and orbital animations.**
Since bounce and infinity are radians based, the speed is divided by 16 to help slow it down, otherwise it would be way too fast.
Normally, for these two animations, it was using an internal speed of 0.35, so now a comparable speed can be achieved with a value of 5.6.  The other animations appear to be more ideal with an animation speed around 5.0, so this is a good common ratio to use.


* **Mine bombs: Update the '/prison utils bomb list' command to include more of the mine bomb's features and options. **
Added support to show the available Mine Bomb's animation patterns.


* **Mine Bombs: More adjustments to the animation objects.**
Mostly cleaning them up.


* **Mine Bombs: removed nbt info that was being passed into the creation of an ArmorStand.**
This never did work correctly since it was set on the bukkit object, which was not visible when wrapped with a prison object.


* **Mine Bombs: update the orbital animation to fix a few issues.**



* **Mine Bombs: Added a first none animation to the animation pattern so the holograph remains in the same spot.**
Otherwise it would move with the first moving armorstand.  
Orbital, orbital8, and starburst move armorstands so this allows the name to remain stationary.


* **Mine Bombs: Added the setup for starburst animations.**



* **Mine Bombs: added two new AnimationPatterns: orbital8 and starburst.**


* **Mine Bombs: Add new settings for animationOffset and animationSpeed.**
Animation speed controls how much the animation angles change by adding this value to the prior value.
Smaller is slower, and larger is faster.
Animation offset is used on animations that move the actual armor stands, such as orbital, orbital8, and starburst.  
If offset is zero, then it will result in a round orbit.  If it non-zero, then it will rotate around a point outside of the original location.  Multiple ArmorStand animations, like orbital8 and starburst, will offset each placed armorstand around the main Location, therefore creating interesting geometric patterns.


* **Mine bombs: Added support for "small" armorstands.**


* **Location and Vector: Enhance the toString() functions so they can be used in debugging and unit tests.**



* **Mine bombs: Geometric: update descriptions and create unit tests to confirm if the function getPointsOnCircleXZ are producing the intended results.**



* **Autofeatures: Use TokenEnchant fortune levels: Added support for the newer TE API "ITokenEnchant". "TokenEnchantAPI" is still supported.**



* **Mine bombs: Hex color codes:  Setup one mine bomb using hex color codes.**
Hex color support was added to minecraft with spigot v1.16.  Older versions of spigot will not work.
No changes were needed to allow the hex support since prison was already handling support for hex colors.
This is triggered with a prefix of '&#' followed by the 6 hex digits, such as '&#a1b2c3'.
As a result, this simple hex color code will be translated by prison in to the code structure that spigot and paper will recognize:
'&#a1b2c3' is converted to: '&x&a&1&b&2&c&3'



* **Oops... accidentally committed code that has been on the back burner for a long time.  This resulted in a build failure.**
The code is going to take the `/prison support saveToFile help` output, which is HTML based, and auto generate a table of contents with a lot of cross referenced hyper links.
The intention is that if you load this document, you can quickly jump to any section of interest to help solve support issues.
This will be updated and completed in the near future... nearer now that I accidentally committed it. ;) LOL



* **Mine Bombs config settings: updated some of the defaults and added a few new bombs to use the newer animations.**


* **Mine Bomb Animations: Added two new animations: bounce and orbital.**


* **Mine Bombs: Update to a lot of the core support for ItemStacks, World, Location, and ArmorStand.**
These enhancements are to help support moving mine bomb animations back to core.


* **Mine Bombs: Geometric shapes and functions.**


* **Prison command placeholders: Updated the new placeholders for "ifPerm:" and "ifNotPerm" are now working properly.**


* **Entity ArmorStands: ArmorStands are now able to spawn as invisible.**
Before this change you could see an armor stand appear for a fraction of a second before the invisibility kicked in.
Now armor stands are being spawned elsewhere, set to invisible, then teleported to the intended location.  At the point of teleportation, they would be invisible.


* **Prison commands: Added support for new global placeholders: '{ifPerm:<perm>}' and '{ifNotPer:<perm>}'.** More adjustments.


* **Prison backups: updated some of the help text.**


* **Mine Bombs: cooldowns.**
Changed the cooldown handling by moving it to the mine bomb's core and created its own task to run to monitor it.
Fixed issue with the older version of cooldowns not working properly with throwing the mine bombs.


* **3.3.0-beta.18c Beta release - Please read!!**
I usually do not release beta versions because I do not want to expose anything risky
to any server. 

The reason why this is a beta release: There are changes to two different player file names that older releases of prison cannot correctly read or access.


The reason why this is a beta release: There are changes to two different player file names that older releases of prison cannot correctly read or access.

**Since this is a beta release, use at your own risk.**
**Please backup your files**, although prison automatically updates all of prison's files when a new version is detected, and
before starting the new prison version.  Those backups can be found in the `plugins/Prison/backups/`
folder.

**Please wait and do not use this BETA release if you do not want to risk trying this beta, please wait until the next alpha is released, which will be in about a week or two.****Please wait and do not use this BETA release If you do not want to risk trying this beta, please wait until the next alpha is released, which will be in about a week or two.**

The reason why this is a beta version, is because the file name format for RankPlayer files and the player cache files have changed. Everything is working great, but the catch is that
this is the first time with prison that you cannot easily rollback to an older version.  It's possible, but you have to manually copy or rename files.

The file names have been changed so the player's name is now part of the file name so its easier to identify which file belongs to each player. This also makes Prison more compatible with bedrock platforms.

Prison is using an intelligent method to identify what the file name is, and it's able to use either the new format, or the old.  Prison also automatically updates the file name to the newer format when the file is saved again.  

This release has been tested for about a week plus on my test servers, and it's proving to be very stable.  But I would like to see a few more users to confirm there isn't an odd combination of settings that are causing issues.  If you encounter any issues, please ping me on discord. You can even DM me directly.



* **Prison commands: Added two new command placeholders that are globally available to allow control of commands based upon player perms.**
The new placeholders are '{ifPerm:<perm>}' and '{ifNotPerm:<perm>}' and this will control the execution of all commands that follows those placeholders.


* **Disabled the system settings component.**  It posed too much risk for something to go wrong, especially if the config file that it was using was eliminated. Now the server is able to more intelligently deal with file names, and how to auto upgrade them.  This makes it easier on the server, and it helps transition over to the new format for the file names.


* **RankPlayer files: minor change to how the permission snap shots are handled to fix a problem where it was not working correctly.**
When a player was online, it would properly populate the permsSnapShot array with the player's current perm list.  But when they logged off, then it would be cleared.
This fix ensures that the perms updated and saved, so they can be properly loaded when they are not online.


* **Rankup: removed unused code to eliminate a compile warning.**
This is a trivial change that has no impact.



* **Rankup messages: externalized another message related to an economy failure.***


* **Mine bombs: Update and fix some issues with the new mine bomb processing and animations.**


* **Prison NBT: removed debug code which was generating a lot of entries in the logs.**


* **Mine bombs: Setup throwing of mine bombs.**
The mine bombs can now be thrown.  If they don't land within the mine, then they are not removed from the player's inventory.  
The check for the mine is performed where the block lands.  The throwing velocity can be controlled in the settings of each mine bomb; the higher the value, the farther the player can throw the bomb, which can easily be outside of the mine.


* **Mine bomb defaults:  Added a couple of new sample mine bombs to illustrate the new animation sequences.**
Changed the text on the bombs' tags and lore to include a `%r` at the end; a reset character.  This helps to prevent bleed over when dumping the raw values to the console and logs.


* **Ranks: Fixed some issues with the newer formatting of the player files.**
Had an issue with using a rank name for a ladder name, which resulted in player's ranks from getting reset since the player had zero ranks due to incorrect mappings to ladders.


* **Mine bombs: throwing.  Setup the first pass with throwing mine bombs.**
This is not fully functioning yet, but it's getting closer.



* **Mine bomb findArmorStands: More work to provide more features and to work around issues with NBT data not working correctly with entities.**



* **Mine bombs: Fix issue with the printing of the error message with the included json data since it contains non-encoded % signs.  Setup regex comments on them by using: `\Q` and `\E`.**


* **Mine bombs: More work on getting the new mine bombs functional and working better.
Still not perfect and fully working yet, but getting there.**



* **Rankup: removed dead code.**


* **NBT library: Some adjustments to make the code more "correct".**
This does not work with entities, since you can write a value to the entity, but you cannot read it. 
ItemStacks do work fine.   


* **Mine bomb animations:  Throw velocity.**
Added support for throw velocity by providing a low and high range, which will randomly select a value between those points.  This provides for a variation in throw speed to keep it challenging.


* **Mine Bomb Animations: More changes to fix more issues with the mine bombs in general, and the animations.**
These major changes has broke many things, so they are being hooked up little by little as each part is tested.


* **RankPlayer: Updated how some classes uses the RankPlayer object**, and how it gets it, to help improve the code by simplifying it, and helping to ensure it's accurate.
Also updated some of the comments in the PlayerManager.


* **Player Cache: Adjustments to improve access to the RankPlayer object.**


* **Mine Bombs:  Refactoring how mine bombs are handled when placed.**  Had to shift a lot of the variable types over to more of the prison flavors to allow for better support of the newer functions.
This is setting up support for throwing mine bombs.


* **More work on the findArmorStands function.**
Fixed an issue with getting the entities within an area too.  Found that was not working correctly.


* **Spigot compatibility: Expanded some of the compatibility functions so that some other mine bomb code can be simplified.**


* **Mines: Converted a mine cache lookup from using longs which were a fragment of the player's uuid, to using their whole uuid. **
This change was made to ensure that bedrock players won't have problems.


* **Mine Bombs: Added NBT ids to all mine bomb armor stands so they can be identified as being part of the mine bombs processes.**
Added this support to the `/prison utils bomb findArmorStand` command so it applies to only mine bomb used ones.


* **AutoFeatures: Added new blocking capabilities: raw to raw blocks.** 
raw_gold => raw_gold_block, raw_iron => raw_iron_block, and raw_copper => raw_copper_block.


* **Entity clean up:  Small changes that missed the last commit related to the addition of the Entity and mine bomb animations.**


* **Prison API: I noticed that an API class had an PrisonItemStack qualifying the class?**]
I have no idea what happened here, or why it even was added.  All I can think of is that it was a stray paste or something. 
This was removed.


* **Prison Bomb Animations: Initial setup and switch over to use the new prison bomb animations. Non-functional.**
This state of development has the animations working, but the bomb's actual explosions are not yet enabled, so this commit is non-functional.  It's being committed due to the number of classes changed and for how much is working well too.
The animations were pushed back to core so that way it's not more code that is being crammed within the spigot module, which was originally intended to be a light weight layer sitting between the actual prison code, and the platform.
The animations are designed so that there can be a number of variations created, and then they would be available to use with the mine bomb configs.


* **Updated XSeries to v11.2.0 from v11.1.0.**
* **Update Papi to v2.11.6 from v2.11.5.**


* **Prison semantic version: Moved the code of prison's semantic version tools to the prison-core module.**
This was needed to be used within the core, so it's movement and the elimination of dependencies on spiget was beneficial.


* **Sellall: when sellall message delay is enabled for autosell, changed it to when the player does `/sellall sell` it reports the sale for that transaction instead of waiting until 15 to 20 seconds later.**



* **Refactor JsonFileIO to push more of the non-json content to FileIO where it really should be located.**


* **RankPlayer: Added new fields to expand what is being stored in the RankPlayer object to store data that is not available when the player is offline.**
This can provide additional support for players when they are offline.


* **Mine Bombs: New feature to remove stray amour stands.**
`/prison utils bomb findAmourStands help`
This lists all amour stands in a given radius and you have the option to remove them.
Early on, mine bombs use of amour stands could glitch and not be removed.  
This command can identify them, and remove them.  
This command should be used with caution since you could also remove holographic displays too, and if you do, you may not be able to regenerate them too easily, so use with caution.


* **Mine bombs: potential bug fix.  If a mine bomb is null, this was causing problems.  This now properly handles null mine bombs.**


* **Player info fix: When creating the new entity support, there were times when the names were being lost and as such this code was failing because the name was null.**
Normally it never will be null, so I cannot really call this a bug.  But this fixes the issue if it happens again in the future. If a name is null, it now will properly support it.


* **Prison Entity support added!**
This is a major change to prison.  Support for bukkit Entities has been added, which had to be worked in between the SpigotCommandSender and the players. 
This is a significant change since it does allow more complex support for other features, such as newer and enhanced support for mine bomb animations, which uses armour stands, which are entities.
This will allow the animations to be moved to the core and be associated with the mine bomb package.


* **File saves for players with the ranks and player cache: reworked how the files saves will work and how they will process the files.**
Before I had them setup to be "enabled" by a command that they admin would run.  But there were some problems with that.
One thing that happened on my test server was that the save file that kept the config status disappeared and as such, there was some conflicts with which file was actually being used.
As a result, some of the settings got screwed up for a couple of players.
The solution was to change how files are read... basically it checks to see if either the new format is being used, and if not, then checks for the old format, and if not, then it uses the new file.  If the old file is used, then it's renamed.
When checking for these files preexisting, it does as such with filtering all files to find files with the given prefix.  Therefore, it finds the correct file even if the player's name has been changed.
This appears to be working very well at this point. 
NOTE: There are other commits that need to happen, and will follow this one, that will allow everything to compile successfully.  This commit may not e able to be compiled.


* **Mine Bombs: Finalizing the validation and hooking in the new AnimationPatterns.**
Next is to work on adding new animation patterns.


* **Prison platform: Added a setPlatform function so this can be used for unit testing.**  The test fixture is the TestPlatform object that is not connect to any spigot runtime.


* **Json File IO: Changed how errors are reported.**  Since the exception message may be null, provided a check and an alternative message instead of just passing a null value to the string formatting parameter.
The JSON data can be huge, so only printing the first 500 characters.


* **Mine Bombs: Setup reporting on when validation of bomb effects fail. ** It's not an error as much as that version of the effect is not supported by the active spigot/paper version the server is running.  It's a runtime check.
**Added unit tests for the conversion of mine bombs to JSON and back to the mine bomb config objects.**  **Added units for cloning** that are similar in how it validates what has been cloned and restored from json.


* **Mine Bombs: Added support for an AnimationPattern for the mine bombs.**
This Animation pattern will control the animation sequence as the bomb is ticking down.


* **Mine bombs: Setup a better system to validate mine bombs when creating the defaults and when loading from the save file.**
Now if a mine bomb effect is not valid for that version of bukkit/spigot, it will not be included in the mine bomb so this will eliminate the startup error messages.



* **sellall multiplier add to many ranks: new feature to add (replace) a rank multiplier and have it apply that value to all higher ranks until the end of the ladder or until it hits another rank with a multiplier.**
So if you have p10 and p20 setup and want to fill in p11 through p19, you can use this command by reading p10 with this new option and it will apply the multiplier through p19.


* **ranks ladder moveRank: Added the ability to remove a rank from a ladder.**
This was not possible before, but yet prison would temporarily disconnect a rank from a ladder before assigning it to another ladder.
It should be noted that ranks should always be tied to a ladder, so this is mostly used for testing purposes.


** v3.3.0-alpha.18b  2024-06-23**


* **Prison nbtApi: Found an issue when a newer version of spigot broke the functionality of the nbtApi library, it was causing failures in prison.**
Made changes to prevent an nbt failure from causing prison to fail.  Provided new controls to log a simple error message, but still enable the rest of the prison's functions to work.
The under laying problem was with a 3rd party library and not prison.  But this fix allows prison to better handle when future of versions of spigot may break things again.


* **Upgraded nbtApi to v2.13.1 from v2.12.4.**
This better supports spigot 1.21.x.


* **Auto and manual smelting: List all possible smelting conditions.**
Should provide a similar list for blocking.  This list shows what manual settings will effect the actual blocks, since one setting can control multiple blocks.


* **Rankup and Presetige: Fixed a bug where when prestiging for the first time that it was taking the cost of the default ladder's first rank (generally zero value).**
This now uses the correct ladder based upon which ladder is being targeted for the rankup or prestige.


* **Block break of unknown block type: Handle a condition of when a target block is not a known.***  Cancels the event.


* **XMaterial bug fix.  If a newer version of spigot is being use that XSeries does not support**, there is a chance an error could happen if a newer block type is added to the sellall shop list, or some other need tries to access all block types on the server.
This prevents a parse error within XMaterial from shutting down prison's ability to process all of the blocks.  This should have been a very rare condition, but now it's guarded against cause problems.


* **Upgrade XSeries to v11.1.0 to support spigot v1.21.0.**
XSeries v11.x no longer directly supports the matching to spigot 1.8 resources by the use of id and data byte.  So added the support directly to the prison compatibility class for spigot 1.8 so prison can continue to use XSeries with spigot 1.8 without any issues.


* **New feature: AutoFeatures: Smelt and Blocking: New option to include the player's current inventory when performing a smelting and/or blocking.**
The way this works, is that before "trying" to smelt or block an item in the drops list for the player, that it removes that same item from the player's inventory, and adds them to the player's drops.
This ensures that the inventory gets smelted or blocked, and any leftovers are then returned to the inventory.
These new features default to false, which off, so as the older behavior is honored and this will not break any existing servers.


* **Prison support troubleshoot sellallmines:  This new commmand will review all blocks used in all mines, and show a report based upon each block to identify if it is setup in the sellall shop.**
This is a good way to identify if all blocks that are used have an entry within sellall.
Note that this only reviews the blocks, but not their possible drops, which may differ.


* **Prison support troubleshoot autosell: Added a new feature to help identify why autosell may not be working.**
This new command also provides a lot more information about what the individual features do, and how they work.
It also provides additional information on features related to sellall that the users may not understand or are aware of,
these features may help provide understanding on how to fix issues they may have with other plugins.


* **Rankups: Change how rankups are working by default.  They no longer require perms, but within the 'plugis/Prison/config.yml' they can be enabled.**
The reason for this change is due to the fact that a lot of people have been saying that perms are suddenly forcing a perm check, which has been failing.
So although this is a change that may break a few servers which do not want players to be able to control their own rankup or prestige, this is a fix that will make it easier for many others.

```yaml
ranks:
  rankup-bypass-perm-check: true
prestige:
  prestige-bypass-perm-check: true
```



* **Spigot utils: saw a rare situation where a NPE happened when prison had custom blocks setup, but the plugin was removed.**
This prevents a NPE if the backing 3rd plugin goes away.



* **Mine reset notifications: Forgot to add support for 'server' notification mode.**


* **Mine notifications: Added support for world and server modes.**



* **Friendly player files: Slight adjustments.  Added two report features so list both old and new file names for all players, for either player or cache files.**


* **New feature: Reloadable ranks and ladders.**


* **Block utils: Unbreakable blocks breaking.**  There was an issue with the unbreakable blocks not identifying a collection with a Location for a key.  Changed the key to a String using the world-coordinates and it fixed the issue.


* **Friendly player files: changes to get everything working.**
The command is able to convert player files, and to update the active player cache.
The players do not need to be reloaded, since the system will automatically use the newer file format once the conversion is ran.
The checks for which file format should be used is based upon the config.yml setting and if the conversion was ran.
Update some notes on the `/ranks reload players`.


* **New feature: Reloadable players.**
Players can now be reloaded with the command: `/ranks reload players` or the alias '/prison reload players'
If a player's RankPlayer file, as found in `plugins/Prison/data_storage/ranksDB/players/players_*.json', is modified manually, or replaced with a backup copy, then this command will reload all players for the server.
When this command is issued, prison will attempt to save all changed player files and remove them from the rank's PlayerManager.  Once all players have been removed, the process of reloading them will begin.
There is a chance there could be a currently running process that is using the older copy of the player's RankPlayer obect/file.  This will not terminate any process, or swap usage of an object in mid-stream.
All risks of using this new feature are placed on the individual using this command since it is unknown how some operations and tasks may respond.  That said, this really should have minimal to no impact.
This feature should not be used frequently; it should only be used on the rare occasion.  If this is used frequently, then perhaps there is a larger over arching issue that needs to be addressed.


* **RankPlayer files and PlayerCache files:** Update to how prison is managing these files, and how it's tracking more of the data that is added to the RankPlayer so that way the TopN process will not have to access the player cache.
RankPlayer file changes will increase the frequency of updates on this file.  Used to be primarily when there would be a rank change but now it's tracking more information which will be updated when the player cache is saved or unloaded.


* **Player file name update: The file names used for the rank players and the player cache are being updated.**
These files have the player's name as part of them, but the big change is that the new format now is supporting bedrock players.
The bedrock player UUIDs are all zeros at the beginning of the UUID, so it was leading to possible issues when there were more than one bedrock player due to the cache trying to load the wrong file.
The transition to the new file format will not be forced yet.  The old format will continue to be used until a major release is made in the future.
The new format can be enabled through the config.yml file.  There is also a new task and command under '/prison support updates playerFilenameUpdate`.
The prison system is now able to track and store different events and settings in a new 'pluigins/Prison/backups/prison-system-settings.json' file.  This will track the usage of the player name update.


* **Docs: Fixed a typo.**


**v3.3.0-alpha.18a 2024-05-21**
Releasing this alpha.18a because the fix of the of the new player bug was crippling servers.


* **Bug Fix: When a new player was joining prison, and there were placeholders being used in the rank commands for either the default ladder, or the first rank, the resolution of the placeholders was triggering a new player on-join processing within prison.**
This happed because the new RankPlayer object was not being added to the PlayerManager before ranking the player... now the player's object will be there when the rank commands are processed.
Honestly have no idea why this has not been an issue in the past....


* **Docs... added curseForge.com to the list of locations where prison can be downloaded from.**



* **Fix to the docs... for some reason, eclispse, or one of it's plugins failed and corrupted the markdown.**
I did not realize it was corrupted since it was still showing the correct content, but when restarting the IDE and loading the files, they were missing the first characters.


**Prison v3.3.0-alpha.18 2024-05-20**

This version has been tested and confirmed to be working with Spigot v1.20.6 and Paper v1.20.6.



* **Player Ranks GUI:  Fixed an issue with the code not using the correct defaults for NoRankAccess when no value is provided in the configs.**


* **Obsolete blocks: Marked an enum as @Deprecated to suppress a compile warning.**  This has not real impact on anything.


* **Gradle updates:**
Upgraded XSeries from v9.10.0 to v10.0.0
Upgraded nbtApi from v2.12.2 to v2.12.4
Upgraded luckperms-v5 from v5.0 to v5.4


* **Economy: Added a feature to check if a player has an economy account.**
Currently this is not being used outside of the economy integrations, but it can be used to help suppress initial startup messages where players do not have an account, which will help prevent flooding a lot of messages to the console for some servers.


* **Player Cache: There was a report of a concurrent modification exception.**
This is very rare and generally should not happen.
The keySet is part of the original TreeMap collection, so the fix here is to take all keys and put them in a new collection so they are then disconnected from the original TreeSet.
This will prevent a concurrent modification exception if there is an action to add or remove users from the user cache, since the user cache remains active and cannot be locked with a synchronization for any amount of time, other than then smallest possible.
The standard solution with dealing with this TreeSet collection would be to synchronize the whole activity of saving the dirty elements of the player cache.  Unfortunately, that will cause blocking transactions when player events try to access the player cache.  Therefore it's a balance game of trying to protect the player cache with the minimal amount of synchronizations, but allow the least amount of I/O blocking for all other processes that are trying to use it.
Hopefully this is sufficient to allow it all to work without conflict, and to be able to provide enough protection.


* **Gradle: Removed a lot of the older commented out settings.**
See prior commits to better understand how things were setup before, or for references.


* **Gradle: A few more adjustments to add a few more items to the libs.versions.toml.**



* **Placeholders:  The placeholder api call from PlaceholderAPI is passing a null OfflinePlayer object.**
Not sure why this has never been an issue before, but added support for null OfflnePlayers.



* **Spiget: Updated the way prison handles spiget by now submitting a task with a 5 second delay.**
The messages are more helpful now.
This also moves it out of the SpigotPrison class.


* **Upgraded John Rengelman's shadow, a gradle plugin, from v6.1.0 to v8.1.1**

 

* **Upgrade gradle from v7.6.4 to v8.7**
  Upgraded from: v7.6.4 -> v8.0 -> v8.0.2 -> v8.1 -> v8.1.1
  		-> v8.2 -> v8.3 -> v8.4 -> v8.5 -> v8.6 -> v8.7
  v8.3 required a configuration change due to `org.gradle.api.plugins.BasePluginConvention` type has been deprecated and will be removed in gradle v9.x.  This is impacting the use of the `build.gradle`'s `archivesBaseNamme`.  This is being replaced by the new `base{}` configuration block.
  v8.3 also required other config changes.
  


* **Upgrade spiget from v1.4.2 to v1.4.6**
  Was using a jar with v1.4.2 due to their repo going down frequently.
  Switched back to pulling through maven and got rid of jar.
  

* **Upgrade gradle from v7.3.3 to v7.6.4**
  Upgraded from: v7.3.3 -> v7.4 -> v7.4.1 -> v7.4.2 
  		-> v7.5 -> v7.5.1 -> v7.6 -> v7.6.1 -> v7.6.2 -> v7.6.3 -> v7.6.4
  Preparing for Gradle v8.x
  Around v7.5.1 required a change to auto provisioning
  

**v3.3.0-alpha.17a 2024-04-29**


* **GUI settings: Update them to remove unused stuff.**


* **GUI Tools messages: refined the messages and hooked them up.**


* **Initial setup of the GUI tools messages that are at the bottom of a page.**
Setup the handling of the messages and added the messages to all of the language files.
Support for prior, current, and next page. Also c
* **Update the plugin.yml and removed the permissions configs since they were generating errors (lack of a schema) and the perms and handled through the prison command handler.**



* **CustomItems: Fixed an issue when CustomItems is a plugin on the server, but the plugin fails to load.**
Therefore the problem was fixed to allow a failed CustomItems loading to bypass being setup and loaded for prison.
`CustomItems.isEnabled()` must exist and return a value of true before the integration is enabled.


* **XSeries XMaterials: Update to XSeries v9.10.0 from v9.9.0.**
Had issues with case sensitivity when using `valueOf()`, which was changed to `matchXMaterial().orElse(null)` which resolves a few issues.
XMaterials v9.10.0 sets up support for spigot 1.20.5. There may be more changes as spigot stabilizes.
The issue with using `valueOf("green_wool")` would not find any matches since the enum case must match the string value exactly.  So `valueOf("GREEN_WOOL")` would have worked.  This was fixed to help eliminate possible issues with configuring the server.


* **Auto features: normal drop processing: Added a new feature to check inventory for being full, and if it is, then display the messages.**



* **Auto features: Inventory full chat notification: bug fix.  This fixes using the wrong player object.**
It now use prison's player object so the color codes are properly translated.


* **Placeholders: bug fix: When using a search from the console which included a player name, it was generating an invalid cast to a SpigotPlayer object when it wasn't related to that class due to the player being offline.**


* **GUI: ranks and mines: setup and enable a new default access block type that can be used if that rank or mine  has not been specifically specified.**


* **GUI: tool bar's prior page and next page: Suppress the page buttons if there is only one page worth of gui content. **


* **GUI: Player ranks: Fixed a bug where clicking on a rank in the player's gui was trying to run an empty command, which was generating an invalid command error.**
Ignores the command running if the command is either null or blank.



* **Update to plugin.yml since some soft dependencies were missing.**


* **Economies: fixed the display of too many economy related messages, including eliminating logging of messages for offline players.**
The vault economy check for offline players, will now only show one informational message if a player is not setup in the economy.


* **GUI Player ranks: The setting for Options.Ranks.MaterialType.NoRankAccess was not hooked up properly so it was not really working.**
The config creation was wrong.  Also fixed the code that was generating the gui.


* **RankPlayer and topn ranking: This may not have an impact overall, but for both the default and prestiges ladders, they are defaulting to a value of -1 when performing a comparison between players.**


* **Update privatebin-java-api to a newer release that now does a better job with a failure to use the correct protocol.**
It identifies what TLS version is being used, and if TLSv1.3 is missing, then it will indicate that the java version needs to be updated.
As a fallback, if the privatebin cannot be used, it is now using the older paste.helpch.at service.  But if it does, the resulting documents are not purged and not encrypted.


* **Economy: EdPrison's economy. Added support for use of EdPrison's economy and custom currencies.**
This will allow prison to use EdPrison's economy does not also use another established economy that is accessible through vault, or multi-currency.


 
<hr style="height:3px; border:none; color:#aaf; background-color:#aaf;">



## Current Change Log - Part 2:

**[v3.3.0-alpha - Current - Part 2](changelog_v3.3.xb.md)**

- **v3.3.0-alpha.17** 2024-04-20 and older



 
<hr style="height:3px; border:none; color:#aaf; background-color:#aaf;">



# Older change logs:

## 3.3.0-alpha.7 2022-01-22*

* **3.3.0-alpha.7 2022-01-22**

A return to the v3.3.0 release track.  The alpha.7 release represents a continuation of where we left off before.  Once we got to alpha.6, it became apparent that it was critical to release before v3.3.0 was ready, so we returned to the v3.2.x track, including everything up to and including the v3.3.0-alpha.6.










# 3.2.11 2022-01-22



# v3.2.10 2021-08-22



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
  
