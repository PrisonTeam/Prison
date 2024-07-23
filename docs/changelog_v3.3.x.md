[Prison Documents - Table of Co9ntents](prison_docs_000_toc.md)

## Prison Build Logs for v3.3.x

## Change logs
 - **[v3.3.0-alpha - Current](changelog_v3.3.x.md)**
 - [v3.2.0 through v3.3.0-alpha.17](prison_changelogs.md)
 
* [Known Issues - Open](knownissues_v3.2.x.md)
* [Known Issues - Resolved](knownissues_v3.2.x_resolved.md)



These change logs represent the work that has been going on within prison. 


# 3.3.0-beta.18c 2024-07-23


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



# 3.3.0-alpha.17 2024-04-20

**v3.3.0-alpha.17** 2024-04-20



* **Mines messages: Secondary placeholders.  Added support for mines' messages to be able to support secondary placeholders within the language files. NOTE: Not usable at this time.**
But... this is basically useless.  Within the mines language files, the vast majority of all messages are related to admin messages and are not viewable by the players.
Therefore the admin-only messages will not support the secondary placeholders since the players will never see them.
At this time, there are no messages that supports the use of these secondary placeholders, although the feature has been enabled for mines.
If a need is required, then please reach out and request such features should be enabled.  At this time, effort and work will not be performed blindly upon items that will never be used, so if you see a need for this, I'd be happy to add them since you would have a need.



* **Placeholder bug: The placeholder 'prison_rankup_cost_percent' uses the calculated value of a percentage, but when used with the placeholder attribute, it was found to use the price instead.**  As such, the actual value returned for the placeholder was incorrect.


* **Player Manager: Secondary Placeholders:  Setup the secondary placeholder support on the PlayerManager, but it has not been enabled yet** since secondary placeholders on placeholders do not make a lot of sense because each placeholder is only one value and they cannot contain alternative text.  At least not yet.


* **Localizable: Secondary placeholders:  Rewrote the whole support of secondary placeholders related to players.***
Expanded the support by making them generic so other data sources can also have their own custom set of placeholders too.  Such as mines.
This now supports a new interface that will provide the generic support.
Player's commands have been modified to pass a RankPlayer object, which supports the new interface.  Non-player commands have not been converted since players will never see those messages (such as admin commands).


* **Added a comment in the ranks message files indicating that there is now some support for player based placeholders to farther customize messages.**
This also fixes an issue with the broadcast messages to use the intended player instead of the target player who is being sent the message.



* **Localization: If admin adds extra parameters, or other parsing failures, happens on a message, the error will now be trapped and logged to the console without formatting.**



* **Economy: For economies that prison supports that has a method to check if the player has an account, prison now tries to check if there is an account for the player before trying to use the economy.**
This could potentially prevent issues or run time failures.


* **Prison API: Added a few new functions to work with ItemStacks.**



* **Players: Shift the function of getting a player object to the Player classes, such as CommandSender.**
This is to simplify the code and to put the functionality in one location.



* **Sellall: New command:  '/sellall items inspect'**
This new command will inspect what the player is holding, and dump the details so the admin can see exactly how an item/block is created, including lore and enchantments.
Eventually this information can be used to enhance the ability to sell and buy non-standard items by allowing the admins to filter on lore, enchantments, and/or NBTs.



* **SpigotPlayer: Fixed a potential issue if trying to use getRankPlayer() if the ranks module is not enabled.**
Added a check to ensure it's active.
We have not seen any reports of issues related to this.



* **Prison Player: Added a new sendMessage function using Lists of Strings. ** 
Added a new function getPlatformPlayer() which gets a bukkit player object if the player is online.  This will consolidate a lot of other duplicate code.


* **Mine Bombs: wrapped up the changes to enable the placement of a mine bomb when using the BlockPlaceEvent which is used when using a block for the bomb's item.**


* **Prison ItemStack: remove enchantments from the core ItemStack since prison cannot properly represent it in versions lower than 1.13.x**, plus it was wrong for all spigot versions greater than 1.12.x.
Added the proper enchantment functions to the SpigotItemStack object.


* **Initial setup of sellall lore filtering**
A little clean up.


**3.3.0-alpha.16c 2024-03-11** 


* **PlaceholderAPI: Upgrade from v2.11.2 to v2.11.5**


* **XSeries: Upgrade from v9.8.0 to v9.9.0**


* **Mine bombs: add support for BlockPlacementEvent so if someone is using a Block they can use it as the mine bomb's item.**


* **Prison's NBT: Add support for using NBTs with bukkit's Block.**


* **Add support for getting the "hand" from the BlockPlaceEvent.**


* **Prison support listeners: added support for listening to and providing dumps for PlayerDropItemEvent, PlayerPickupItemEvent, and BlockPlaceEvent.**


* **Promote & Demote: Improved upon reporting issues with the command.**
There were few situations where the command would exit without reporting why, which was leading to difficulties with using the command effectively. 


* **New feature: TopN customization now possible.  The messages and placeholders that you can use are located in the core multi-language files.**
See the bottom of the files for instructions on usage.
TopN data is set to delay load so it does not lengthen the startup process.  As such, it now reports that the data is being loaded so it is now clear why there are no entries in the list initially.


* **Mines: eliminate a field no longer used: includeInLayerCalculations.**
This was obsoleted with better use of logic.


* **Mine resets: Reworked how prison is selecting random blocks per layer, to properly include constraints.**
The addition of various new features in the past made a mess of the logic, so it's been cleaned up greatly so it now makes sense and should work properly now.
There is a slight risk, that as blocks are removed from a layer due to reaching it's max constraint value, that future random selections were missing blocks selections and was then inserting AIR. This fixed code now will insert a filler block which has been selected with no constraints, and the largest chance value.


* **mines block layerStats: rewrote to improve and get rid of the collection manipulations.**
Found potential problem with air being inserted in to mines.
Renamed a lot of uses of Location objects to include the name "location" in their variable names instead of "block".


* **Mines block layer: Added colors for same IDs so it's easier to read.**  Added a check that sees what block actually exists.  If counts of what should have spawned match whats in the mine for that layer, then it shows only one number.  It shows two numbers if a block's intended spawn does not match what's in the mine.


* **Mine reset: added a force to the reset so it will ignore an existing mine reset and allow a new one to begin.***
When a mine is being reset, there is no way to actually cancel it.  So this allows a large mine to undergo multiple concurrent resets.  Use at your own risk.



* **Bug fix: the check for the time the reset has been going on was incorrect and was fixed.**
Also all the code for submitting the reset task was moved in to the mutext.  Now, if the reset got hung up, this will properly terminate it and resubmit it.


* **File format: Eliminate the check for file types since there is only one.**
Currently there isn't a setting to specify what it should be.


* **Mines block layerStats: Added a new command that shows which blocks are in each layer in the mine.**
There are a lot of future enhancements that can be added to this command, such as checking the actual blocks to see if they are still there, or if there was a problem with the spawning of the blocks.


* **Block lists: Added the total chance percentage to be displayed with the blocks.**


* **File output technique: Changes to how the "replace" existing files works.**
If the file does not exist, then it opens it to create it, otherwise it truncates it.


* **Mine bombs: Ability to prevent a bomb's blocks from count towards the player's block totals.**


* **Sellall and autosell: Refinements were made to the handling of the sellall settings to better stabilize the use of the commands.**  Setting status for the players were moved to the player objects and is now used in all of the related calculations so there is better stability and consistency.


* **Mines import: prevent the processing of an importing of a mine if there are problems with the mine's name, or locations.**


* **AutoSell: Bug fix: When autosell and sell on inventory is full was all turned off, it would still sell.**
This fixes some of the logic to simplify the code, and to fix those issues.


* **Mine skip reset messaging: Did not have it hooked up in the correct location, so the skip messages were not happening.**


* **Mine reset: Under heavy load when performing a mine import, there were seen occasionally errors with concurrent modification errors.**
Code was changed to minimize that possibility.


* **Sellall and GUI message failures: a number of messages that would indicate the player does not have access to that command were changed to remove the permission from the message.**
This was requested by a couple of admins because they did not want the players to see the internal workings that would otherwise control how the software would behave.



* **Mine imports: Fix some minor issues to get this to work even better.**


* **Mine skip reset: If a mine is reset, send a message to players, but only if the message is defined and not empty: 'skip_reset_message='**



* **Player sellall Multipliers: Fixed the ranks multiplier to include all ranks that are defined within the sellall multipliers.**
Added a new function that will gather and list all multipliers that go in to the calculation of the player's total multipliers, which includes the rank multipliers (the sellall multipliers) and also permission based multipliers.
This detailed list of the individual multipliers, is viewable for each player that is online with the command `/ranks player <player>` and reveals the actual details of how it's calculated.



**3.3.0-alpha.16b 2024-02-24** 


* **Import Mines: Added the ability to import mines from JetPrisonMines config files.**
`/mines import jetprisonmines help`


* **Prison Command Handler: using config.yml you can now change all of prison's root commands with 'prisonCommandHandler.command-roots'.**
Can now map 'prison', 'mines', 'ranks', 'gui', 'sellall' to all new command that you want.


* **File saving: alternative technique for saving files. Do not use!**
This is a more dangerous technique that could possibly result in lost configurations.  This is being provided as a degraded service if the fail-safe technique is not working ideally on a degraded server.
This should never be used, unless directed by a prison support admin.



* **Prison startup bug: There was an issue with the prison startup when there was an error and prison tried to log the error**, only to find that resources that were needed for logging were not yet loaded nor were their dependencies.  This fixes some of the entanglements to allow the error messages to be properly logged.


* **Bug fix: GUI configuration: Found a problem that when configuring the gui initial settings, that there were problems when trying to access mines and ranks when they don't yet exist.**


* **Add prison debug option to filter on blockConstraints when regenerating the blocks within the mines.**


* **Bug fix: prison support submit: if the bukkit system cannot extract a file from the jar**, such as plugin.yml, this will prevent the failure of the command.  This will allow the command to continue being processed, but may just skip the extraction.



* **Placeholders: Top player rank was using the wrong ladder, which was incorrectly the prestiges rank and not the default rank.**
Correcting the rank fixed the problem.  This only was an issue if the player did not have a prestige rank.



* **Mines set resetTime: fix typo in the description where it shows '*all' instead of '*all*'.**


* **ranks ladder: applyRanksCostMultiplier command was changed to allow the value of 'true' to be used along with the value of 'apply'.**  This helps to eliminate some confusion on how the command works.


* **Bug fix: Prison command handler.  When players are de-op'd, and they do the commands such as `/ranks help` or `/mines help` it was incorrectly showing other sub commands they did not have access to.**
This now shows the correct sub commands that they have access to.


* **Placeholders: topn players - bug fix.  If a player did not have a prestige rank, then it would cause a NPE when using the `prison_top_player_rank_prestiges_nnn_tp` placeholder.**
Just check to ensure its not null... if it is, then return an empty string.


* **Bug fix: Player manager startup: fixed a problem where all players were being updated** even though they did not have a name change.  Only when name changes are detected are the files updated or when a new player is found.


* **Add debug statements to identify how each block was calculated during a mine reset.**


* **Bug fix: block constraints: fix an issue with the selection of lower limits.**


* **updated the help on the `/mines block constraint` to indicate that the layer count is originating from the top, not the bottom.**


* **alpha.16a - 2023-12-28**
  NOTE: I just noticed that alpha.16a was never committed.  So this is not the correct location of when it was set with the local builds.
  

* **Mines: Fixes an issue for when mines are disabled and they are being checked in other processes to see if they are active.**
If the instance of PrisonMines is null, then it will create a temp instance just to prevent an NPE.


* **Mines unit tests: Setup a new constructor for mines that is only to be used with unit tests which allows the mines to be created,** but it does not initialize them since such tasks and processes are not needed in the unit tests. 
As a side effect, these two unit test run much faster since it's not trying to setup tasks.


* **Prison Block change: Add support for display name, which is optional.**
Setup sellall so it can use the display name now, so renamed items will not be mistaken for vanilla minecraft items.
More work needs to be done to hook up displayName to other features, such as sellall and add prison block to mines.


* **Bug fix: Fixed the command `/mines set accessPermission` where it was apply the given perm to all mines.**
 Likewise, all mines parameter was failing to do anything.


* **NOTE: This alpha version "should" support spigot 20.0.4.**
After a few days, if no other issues surface pertaining to 20.0.4, or other related plugins, this will be released as a public release.


* **Fix issue with BlockEvent's SellAll when isAutoSellIfInventoryIsFullForBLOCKEVENTSPriority feature is enabled.**
This was not using the correct new functions that checks to see if a player can use autsell, or if they have it temporarily toggled off.  This also checks to see if the player has the correct perms, if perms are enabled for the sellall event.


* **Breaking change in XSeries: GRASS has been changed to SHORT_GRASS for v20.0.4!** It's disappointing to say the least that after all of these damn years, XSeries screwed up and pushed a breaking change to their repo.  They should have kept GRASS so they would have remained compatible will all past code and configs that had to refer to GRASS directly, but nope... they opted for causing problems.  Very disappointing.
Setup a converter to automatically convert all GRASS to SHORT_GRASS as the mines are loaded.



* **Upgrade XSeries from v9.7.0 to v9.8.0.**
* **Upgrade nbt-api from v2.12.0 to v2.12.2.**


* **config.yml - changed the default values for remapping aliases and restricting players from using commands.**
The default, which used `/mines tp` was actually causing conflict with normal usage.


* **AutoFeatures auto permissions: enable the ability to 'disable' the perms.**  Any op'd player, if perms are enabled, will have these auto features enabled.  There is no other way around this, since this is the correct behavior of OP'd players.


* **Mine resets: If a mine reset takes longer than 4 minutes, then that is probably a failure and the mine reset did not complete.**  Therefore, reset the mine reset mutex and try again.  This allows a "crashed" mine reset to auto fix itself if it can.  The 4 minute wait time is LONG, but it will prevent a normal reset from being canceled and restarted in the middle of a restart.


* **Performance: Changed the defaults for the mine reset settings to help improve the performance on larger servers.**
The older settings would allow other commands to backup and it would appear as if there was lag happening, TPS would rarely drop below 20.  This helps to keep performance a little more responsive. 
The side effect is that there will need to be more "chunks" submitted which could possibly result in longer wall-time for mine resets.


* **Mine resets: If a suggested block is null, then set it to air.  This was causing an NPE under some conditions.**


* **BlockEvents: Added the ability to update block events**
instead of deleting them and re-adding them.  Follow directions when using '/mines blockevent update help', or whenever a block event listing is shown in game, you can now click on the commands to auto populate the block event update command... then just edit the needed changes and submit.


* **Upgrade XSeries to v9.7.0 from v9.4.0.**


* **Bug Fix: Mine resets and block constraints.**
This fixes a few issues with block constrains using min and max, along with exclude from top and bottom too.


* **Bug Fix: GUI ranks, mines, and prestiges were not using the default item name correctly.**  It was using the template correctly, but was not translating the use of placeholders.  Only name and tag are supported.
Mines: `{mineName}` and `{mineTag}`
Ranks and prestiges: `{rankName}` and `{rankTag}`


* **Mines: Added support for '*all*' for mine names for the following mine commands: resetDelay, resetThreshhold, notificationPerm, and accessPermission**


* **Localizable: Bug fix. Blanks were being removed by the use of trim() so the spaces were being ignored.**


**v3.3.0-alpha.16 2023-11-18**

* Update change logs for v3.3.0-alpha.16


* **Fixed an issue with ranks being disabled.  It now skips over this processing when ranks are disabled.**



* **Modules: Changed the way some of the module management is used to help prevent errors when a module is disabled.**
Suppress disabled modules from the placeholder list... only Ranks and Mines, which covers all of the placeholders.



* **Sellall: Standardize how sellall is being checked to see if it's enabled.**
There are still a few ways it can be improved, but this is a step in the right direction.
There was a problem with the older way things were being handled that was causing an NPE with the SpigotPlayer, which was brought to my attention by DinoFengz, but I noticed there were other problems that needed to also be addressed.


* **Economy support for CoinsEngine: support has been initially added**, but it is unsure if it is working correctly.  This request originated from pingu and they said it's not working, but has not provided any more information.  Unsure how it's not working, or if they cannot use it the way they originally envisioned because sellall cannot support different currencies for different items within sellall.
Because of the lack of an API jar to be used, a new sub-project 'prison-misc' was created to be able to generate a pseudo-shell api for the CoinsEngine plugin.  This pseduo api jar is used strictly to allow the successful compiling of the prison's economy hooks for CoinsEngine.
NOTE: I have not heard back from pingu to know if this is working.  If you try to use this plugin and you have issues, please contact me on our discord support server.


* **Block Converters: Change the usage to Player instead of RankPlayer since if ranks are disabled then RankPlayer could not exist.**


** 3.3.0-alpha.15h  2023-11-05**


* **Player Cache: Put some of the player cache numbers in to the config.yml file so they can be fine tuned if desired.**
See the changes to config.yml for information to what the new setting controls.


* **GUI: Add support for changing the gui item names for Ranks and Mines.  Ranks can now set their material type too.**


* **Updated nbt-api and fixed a new issue that was introduced with mc 1.20.2.**


* **Placeholder attribute time: add the time attribute to 4 more placeholders.**


* **Auto Sell: Bug fix for full inventory when auto sell is toggled off, which was incorrectly selling the player's inventory.**


* **Prison Debug: Added support to target some debug logging to a specific player.**
When enabled, it will ignore all other players. Not all debug messages have been hooked up. More can be added upon request.
When debug mode is disabled, it will remove the debug player name.


* **Placeholders: Added support for a new placeholder attribute to better format time based placeholders.**


* **Placeholders: Added the ability to provide a shorted output of the command `/prison placeholders test` so it only shows the command header and the results.**
Use the '-s' flag as in: '/prison placeholders test -s'


* **Placeholders: bug fix: If using the placeholder attribute for an off line player, it would cause an error when checking if the player was in a disabled world.**


* **SellAll messages: Cleaned up some of the US EN messages related to the sellall command.**


* **Bug: sellall auto sell enabled messages reversed.**
The command to enable and disable the auto sell feature was reversed, so when turning off, it would report that it was just turned on.  And on when it was turned off.


**v3.3.0-alpha.15g 2023-10-03**


* **Player Economy Cache Delay: Add the ability to change the player economy cache delay.  Default value is 3 seconds, or 60 ticks.**


* **Prison version: Improve the content of the auto features details.**


* **Placeholders:  Added the ability to specify a player name in all placeholder attributes.**
This can allow the use of placeholders that are player centric in plugins that cannot support player based placeholder requests.


* **Autosell: Setup the SpigotPlayer object to support functions to identify if the player has autosell enabled.  This is used in a couple of places to eliminate redundancy.**
Fixes a problem with the block break event not also checking to see if the player has toggled their autosell status for the forced sell after the event is processed, and also the delayed sell.


* **Cleanup the '/ranks list' to add mines and better format the name, tag, and cost.**
Also removed rankId which is not important anymore.


* **Auto features: change a few of the new line breaks so there are fewer.**


* **Changed the default color code from `&9` (dark blue) to `&b` (light blue) for debug logging since the dark blue could be difficult to see on some consoles. 


**v3.3.0-alpha.15f 2023-09-24**


* **TopNPlayer: Task could not startup if ranks are enabled but there are no default ranks.**
Log a message in the console that the task cannot start because ranks are enabled and there are no ranks. 
Request that Ranks module is disabled, or add default ranks and then restart the server.


* **Prison Support: Added a more secure method and server (privatebin) for submitting server information under prison support submit commands.**
Can now control some of the settings that are used, including password, in the config.yml file.
May need to refresh config.ymml to see now settings.


* **MineBombs: validate all mine bombs upon server startup to validate the sound effects, visual effects, and shape based upon the version of spigot that they are running.**
This mostly is to clean up the default mine bombs where they have sound and visual effects for versions of spigot so something will happen.  This removes the invalid ones for the version so there are less errors at run time.


* **MineBombs: Add support for customModelData for the item used for the bomb.**
This will only work on spigot version 1.14.x and higher.


* **Prison compatibility: Added support for block metadata customModelData.**
This is only compatible with spigot 1.14.x and higher.


* **Ranks Ladder resetRankCost: Added a new parameter to provide an exponent which is used as a Math.pow() function over the base rank cost calculations.**
This can help increase the rank costs for higher ranks.
Default value is 1.0 so it does not apply unless it is specifically changed.
 

* **Update the Double vs BigDecimal example.  Increased from 25 to 35 iterations, and expanded all columns to adjust for the wider output.**


* **Block Converters: event triggers:  More work. Got it working to the point that it's ready for production.**
The way it is right now, any block that is in an event trigger, will be excluded from all explosions.  They will remain unbroken in the mine.  
The players can then break them directly to trigger the events.  Eventually I may allow processing within an explosion event, but right now it's not making sense to process 100+ triggers all at one time for huge explosions... the other plugin that's being "fired" may cause lag trying to process that many at one time.



* **BlockConverters eventTriggers: Fixed the handling of event trigger blocks so they can be ignored within an explosion event.  Now works.**
Still have to process the event trigger blocks in explosions for when they need to be triggered.


* **Sellall command: '/sellall set delay' - fixed the description which had a typo in the description.**


* **SpigotPlayer: fixed a problem where the object was expected to be comparable.**


* **BlockConverters eventTriggers: Add the support for explosion events to all of the explosion event handlers.**


* **BlockConverters EventTriggers: Setup the next phase of handling where blocks in explosions can be ignored.**


* **BlockConverters EventTriggers - setup the PrisonMinesBlockBreakEvent to allow an event to identify if the primary block must be forcefully removed**, which is only used right now with event triggers, and would remove the block when handling a MONITOR event, which normally does not remove any blocks.


* **BlockConverters EventTriggers: Setup more controls within the settings of a blockEvent.**
Setup the ability to control processing of drops: if disabled, it will treat the block event as a MONITOR.  This allows the block to be counted correctly.
Setup the ability to ignore the block type within all explosions, so each block would have to be broken individually.
Setup the ability to remove the block without dropping anything since another plugin would have already processed the block, so nothing would remain to be done with it.


* **Mines block edit - Found a problem where if you are trying to edit a block and the name does not match, it was causing an error.**  Now reports that the block name is invalid.


* **Block Converters - Event Triggers - Had issues with block names not matching, so using all lower case. When using an event trigger it now logs the debug info to the console.**
This will need more work, such as block removal and logging as if it were a MONITOR priority.
At this point, we are testing to confirm that the event is actually being triggered.  So far it looks like it is working as intended.


* **Block Converters: Start to hook up block converters to auto features.**
Changed how block converters were structured to get them to work with the prison environment.
Hooked up the Block Converter Event Trigger to the bukkit BlockBreakEvent.  Explosions are not yet covered, will add support for them if this appears to work.


* **Update docs and some command descriptions to make them a little more clearer as to what they do.**


* **sellall multipliers list:  Added 2 options to control the number of columns displayed with 'cols=7' and also only show multipliers for a single ladder if that ladder name is provided in the options.**


* **sellall multiplier list: Now applies a sort order to the ranks, grouping by ladders.**  It groups by ladders, and then lists the ranks in rank order, within each ladder.


* **sellall multiplier addLadder: added more comments to the command's help, and added defaults to the parameters.**


* **SellAll multipliers:  Increased the number of columns for the listing to 8 columns instead of 5.**  May need to expand it even more so if there are thousands of ranks, it can be better managed.
New command: `/sellall multiplier deleteLadder` deletes all multipliers for that ladder.
New command: `/sellall multiplier addLadder` adds multipliers for all ranks on the ladder.


* **Ranks Auto Configure: Fixed message format when options are not valid so it's better understood what's wrong with the command.**
The parameter names are case sensitive, but added a fallback mapping to lowercase so there is a higher chance of matching the correct commands.
Had to move the location of the 'prestigeMulti=' parameter to be evaluated before 'multi=' parameter since it was taking over the `prestigeMulti=` parameter.


* **Update the rank's getPosition() java docs to better clarify what it is.**


* **Ladders: Added a new command to reset all rank costs for a given ladder: '/ranks ladder resetRankCosts help'**
This will allow a simple and easy change to all rank costs within a given ladder even if there are many ranks, such as the presetiges ladder which could have thousands of ranks.
These calculations are similar to how the `/ranks autoConfigure` will set them up.


* **Prison logging: When line breaks are applied in log messages, it will no long include the prison template prefix with the message to reduce the clutter and make it easier to read multi-lined content.**
The line break placeholder is '{br}', similar to the html element BR.



**v3.3.0-alpha.15e  2023-09-03**

* **Prison messages: Expanded the use of prison message line breaks, `{br}` in both console messages and sending messages to player.**
Auto Features: Added line breaks to the existing block break debug info since it's very long and difficult to read.


* **Mine wand debug info: Slightly alter the printing of the details to make it easier to read.**



* **AutoFeatures and prison version:  I have no idea why I added an auto features reload when doing prison version.  Removed.**
Best guess at this moment is that it was to test something.


* **Prison GUI: When disabled through the config.yml 'prison-gui-enabled: false' there were still some commands that were being registered with the '/gui' root.**
As a result, prison was taking over the use of the command '/gui' that was trying to be used by other plugins.
This fix tries to isolate the GUI commands from backpacks, prestiges, and sellall, to make sure they cannot be registered if GUI is disabled.
Had to create new classes to allow the isolation when registering the commands.


* **AutoManager: percent gradient fortune: Changed the calculations to use doubles instead of integers.**


* **Slime-fun: Moved a lot of the settings for it to the config.yml file instead of hard coding them.**
Now the messages can be turned off, and the boosters can now be added to, and changed.


* **Sellall: Fixed a bug with spigot 1.8.8 where bricks were not able to be sold correctly.**
The issue is with XBlock not correctly mapping brick and bricks to the correct bukkit 1.8 materials.  It may be close, or accurate, but when converting to a bukkit item stack, it fails to map back to the same objects.  
Sellall was not using the prison compatibility classes, and those classes for 1.8 had to be updated too.


* **AutoFeatures: New option to use TokenEnchant to get the enchantment level through their API instead of using the bukkit functions to get the fortune.**


* **AutoFeatures: Added a debug statement when player autosell has been toggled off by the player, since it may look as if autosell is not working correctly.**
Wrapped the notice in a WARNING color code so it stands out in the console with it being red.


* **AutoFeatures: Updated the gradient fortune to fix a problem with not setting the bonus block counts correctly.**


* **AutoManager: Added a new fortune type: percentGradient.**
This fortune calculation is an alternative to the extendedBukkit and altFortune calculations.
This fortune calculation applies a linear distribution based upon the player's tool's fortune level versus the maxfortuneLevel and the maxBonusBlocks.


* **Added a `/mines top` command, alias `/mtop`, which will tp a player to the spawn location of the current mine they are in. **
If they are not in a mine, then it will tp them to a mine tied to their current default rank.


**v3.3.0-alpha.15d 2023-08-16**


* **Mine reset time: Found a conflict with the setting '*disable*' being ignored.**
It's been fixed.


* **BlockBreak sync task:  Found a possible cause of jitters, or visual appearance of lag.**
Basically, need to check the block to ensure it's not already AIR before setting it to AIR.  This could happen if there is a heavy load on the server from other plugins, or from bukkit itself, and bukkit naturally breaks the block before prison's sync task can get to it.
Prison submits the sync task to run "next" in the future, but if there are other tasks trying to run, and if they cause a longer delay, then it can appear to be laggy.


* **AutoFeatures: Expand the number of features being reported to bstats.**
Removed a duplicate comment in the autoFeatures config file.


* **AutoFeatures: Add comment on autosell by perms so it's clear what setting are needed.**
Also added a setting of 'false', in addition to 'disable', which disables the permission based autosell.


* **AutoFeatures XPrison event listener: Fixed a bug that was ignoring the first block in the exploded block list.**


* **AutoFeatures: Added the ability to force a delayed inventory sellall at the end of handling a bukkit BlockBreakEvent.  This is in addition to the other instant sellall at the end of the bukkit BlockBreakEvent.**
This has the ability to set a delay in ticks before it is fired.
If a task was submitted for a player, then future tasks cannot be submitted for that player until the submitted sellall task was finished running.
This was added to help cover situations where third party plugins are trying to add additional bonus drops to the players, but after prison is done handling the events.


* **AutoFeatures: Added the ability to force an inventory sellall at the end of handling a bukkit BlockBreakEvent.**
This was added to help cover situations where third party plugins are trying to add additional bonus drops to the players.


* **auto features: setup a sellall function on PrisonMinesBlockBreakEvent so it can be easier to utilize from other functions.**


* **AutoFeatures SellAll: Added the ability to disable the "nothing to sell" message without effecting the other settings.**


* **auto features: Add the calculated autosell to the dropExtra function to force autosell if it should happen to have extra drops left over (it never should).**


* **Auto Features: If autosell is enabled and there are any leftover blocks that was not sold, it will now generate an error message and if prison debug mode is turned off, then it will force the logging of the transaction.**
This forcing the logging can be turned off in the auto features configs.
Expanded the logging to change the color on some of the more important warnings and failures so they stand out.
Also reworked some of the log details to eliminate redundancy and clarify what's being logged.


* **AutoFeatures: Added support for XPrison's enchantments... forgot to add the API jar which is used to just compile prison (not used on servers).**


* **Prison Placeholders:  Added support to disable placeholders in disabled worlds.**
This feature is not enabled by default.  
Any disabled world in the prisonCommandHandler configs within config.yml, could also shutdown the prison placeholders in that world if enabled.  
The placeholder text will be replaced with just an empty string.


* **Prestiges: Bug fix. If no prestige rank, then prevent a NPE on a simple check.**
Totally thought this was fixed a while ago?


* **AutoFeatures BlockInspector: Fixed a bug with not negating a condition... was causing some problems since it was misreporting the results.**


* **AutoFeatures: Add support for the XPrison enchantments.**
Please be aware that event priorities must be adjusted. You can change prison's event priorities, but XPrison is hard coded to NORMAL  So to get this work, you may have to adjust prison's priorities so it is after XPrison's.
We cannot support XPrison especially if their event priorities become a problem, or causes a problem.


**v3.3.0-alpha.15c 2023-07-30**


* **RevEnchants: added additional logging and details if there is a failure trying to hook into the RevEnchant's events.**
Trying to see if there is additional causedBy information.


* **ranks autoConfigure: Major enhancements to add more prestige ranks.**
Added a lot more informatio to the command's help: `/ranks autoConfigure help`.
More options have been added: prestiges prestiges=x prestigesCost=x prestigesMult=x.
Now able to add more prestige ranks without impacting ranks or mines.  
Example to add up to 50 new prestige ranks: `/ranks autoConfigure force presetiges prestiges=50`


* **Sellall: Rearrange the sellall commands so they are better organized and updated the help text so its also meaningful.**


* **sellall & autosell: auto sell was not working correctly within auto manager.**
Also fixed the user toggle on auto sell so players can turn off autosell when they need to.


* **Sellall: clean up some of the help for a few sellall features and expand on the details.  **


**v3.3.0-alpha.15b 2023-07-28**


* **Prevent a NPE if the target block is not found within the mine's settings.**


* **Mine Bombs: Found an issue with the bomb settings for allowedMines and preventMines, and fixed it.**
There is a global setting in config.yml under the settings: `prison-mines.mine-bombs.prevent-usage-in-mines` to disable all mine bombs from working in those mines.  The bombs can then be individually added by setting adding mine names to the bomb configs settings for `allowedMines` and `preventedMines`.  If a mine is included on a bomb's allowedMines setting, it will override any global setting.


* **Fixed an issue with BRICKS being mismatched to BRICK.  This is an XSeries bug.**


* **TopN: TopN was not being disabled correctly for when ranks were disabled.**
This now properly checks the PrisonRanks to see if the ranks module is active or not.  The prior code was not being as detailed.


* **Prison support: Added more color related test.  Changed the color schema name from 'madog' to 'prison'.**


* **Mines set tracer: Update the command to add options for 'clear' the whole mine, and 'corners' where it clears the whole mine but puts the tracer only in the corners.**
The default option of 'outline' is the default value, and if 'clear' or 'corners' is not set, then it will default to the standard outline, or tracer.


* **Enable all Ranks to be used with the sellall rank multiplier.**
It used to be limited to just prestige ranks, but there has been requests to expand to all ranks.


* **Fixed a color code conflict in the ranks list when displaying the default rank.**
It wasn't wrong, but it was showing incorrectly.  Added a reset `&r` and that fixed it.  Almost like too much nesting got in the way.


* **Prison Support: Support HTML file: Added a color test to prison, color matched on the console's colors to provide an accurate reproduction and match with the console.**
Added the ability to support themes: console is the primary, with Madog being an alternative.  Can have others themes too.
Fixed a few layout issues.  Added the ladder listing, which did not exist before.  Setup the placeholders for the hyperlinks... they will be added next along with the auto generation of a table of contents.


* **Prison Support: More enhancements to the html save file.**
Instead of calling the four `/prison support submit` commands, they are all now generated from within the same function.  This will allow the collection of all hyperlinks to generate a tabl of contents.
Improvements to the layout of some of the items in report.


* **Prison Support:  Enabling the initial save file to an HTML file.**
Color codes are working great, but needs some tweaking.
The framework for hyperlinks are inserted in most locations... they are just double pipes surrounding 2 or 3 words.  I will generate a series of classes that will auto generate hyperlinks and table of contents based upon these encodings.


* **Prison Support: More setup of the new SupportHyperLinkComponent, but mostly the java docs which explains it pretty well.**


* **Prison Support: Setup the Platform with the function to get the related Rank name or Ladder name, based upon the save file's name.**
This is used to reverse engineer which rank or ladder is tied to a give file, without having to read the file.


* **Prison Support:  Start to setup an alternative support file target, of an html file.**
This file will also convert minecraft color codes to html colors.


* **PrisonPasteChat: change the exception to just Exception so it can capture all errors.**
The server has been down for the last two days and so other errors need to be caught.


* **If at last rank, show a message to tell the player that.**


* **Added a few more items to the default list of items in sellall.**


* **Added new feature to prevent mine bombs from being used in mines.**
A specific mine bomb can have a list of included mines, which overrides any exclusions. The mine bombs can be excluded from specific mines too.
There is also a global disallowed mine list that will apply to all mine bombs, its in the config.yml file with the setting name of:
  prison-mines.mine-bombs.prevent-usage-in-mines
There is a global setting in config.yml under the settings: `prison-mines.mine-bombs.prevent-usage-in-mines` to disable all mine bombs from working in those mines.  The bombs can then be individually added by setting adding mine names to the bomb configs settings for `allowedMines` and `preventedMines`.  If a mine is included on a bomb's allowedMines setting, it will override any global setting.
  

* **The Platform function getConfigStringArray should be a List of Strings for the return value, so updated the result type to reflect the correct setting.**


* **Bug fix: If a sellall transaction is null, then it now returns a zero since nothing was sold.**


* **More adjustments to the PrisonDebugBlockInspector for readability.**


* **Auto features not being fully disabled when turned off.**
There was an issue with `/prison reload autoFeatures` enabling itself when it should have been off.



** v3.3.0-alpha.15a 2023-07-16**




* **Enhance Prison's debug block inspector to fix an issue with running it multiple times for one test.**
Reformatted the layout so each plugin is now using only one line instead of two, and added the duration of runtime in ms.


* **SellAllData: The transaction log: Enhanced the itemsSoldReport by combining (compressing) entries for the same PrisonBlock type.**
This will make it easier to review since there will be only one entry per PrisonBlockType.


* **Auto Features AutoSell fix: There were situations where mine bombs that are set with the setting autosell was not being sold.**
Found a conflict with the logic of enabling autosell within the auto pickup code. There are four ways autsell could be enabled, and a couple were incorrectly mixed with their logic.
Debug mode is now showing drop counts before and after adjustments from the fortune calculations.


* **Prison tokens: expanded the error messages for playing not being found to the set and remove functions for the admin token commands.**


* **Prison Tokens: bug fix: Ran in to NPE when an invalid player name is used.**
The message text needs to be stored in the lang files.


* **Fixed a bug with using the wrong player object within auto feature's autosell.**


* **Update the prison API to add direct support for payPlayer function (various options).**


* **Prison Multi-Language Locale Manager: Updated all language files to include information about the new `*none*` keyword.**
This keyword is case insensitive and will return an empty string for that message component if it's part of a compound message.  If the message is supposed to be sent to a player, it will be bypassed and nothing will be sent.


* **Prison Multi-Language Locale Manager: Possibly fixed a few issues with setting messages to "blanks".  If the text of a message is removed, and set to an empty string, it should not be used.**
There was a situation where a zn_TW language file was set to an empty string and it was falling back to the en_US version.
I found that there was a bug with a sendMessage() function to a player that was not bypassing the message like the other functions were doing. 
Also in the code where it was calculating the Locale variations, it was not accepting a blank as the final input.  This was fixed.
Also, to be clear, or more specific, I added a new keyword `*none*` to serve the same purpose.  So either an empty string can be used, or that new `*none*` key word.


* **More work on getting the new world guard sub-projects hooked up and functional in the build with gradle.**


* **Update the PrisonSpigotAPI to include a lot of new api endpoints for accessing sellall related functions.**


* **Sellall: Expanded the functionality of the SellAllData obejects to indicate if the items were sold.**


* **New sellall features: 'sellall valueof' calculates the value of everything in the player's inventory that can be sold.  '/sellall valueofHand' calculates what is held in the player's hand.**


* **Major rewrites to sellall to utilize more of Prison's internal classes and to get away from XMaterial since it cannot handle custom blocks.**
A lot of sellall code has been eliminated, but no loss in functionality.  Actually new functions and enhancements have been added.
Eliminated the two step process of selling... where it first calculated the values, then after the fact, would try to remove the items with no "validation" that the items removed were the items that calculated the sales amount.
Sellall commands: moved the '/sellall trigger add' and '/sellall trigger remove' to under the '/sell set' section since they were hidden because '/sellall trigger' was a command and many did not realize they have to use the 'help' keyword to show the others.


* **Updated Prison's PlayerInventory to include 'contents' and 'extraContents' to match the bukkit PlayerInventory object.**
Within the SpigotPlayerInventory class, overrode the behavior of removeItem to ensure that obscure inventory locations are being removed, since there was a bug where you can get all inventory items, then remove them, and they would not remove all occurrences of the items stacks that were initially returned, such as when someone is wearing an item as a hat, or holding something in one of their hands.


* **Allow additional parameters to be passed on the gradlew.bat command; needed for additional debugging and etc...**


* **Add new salePrice and purchasePrice to the prison Block.**


* **Sellall : remove the disabled worlds setting in the configs since it is obsolete and never used.**
The correct way to disable prison in specific worlds is by using the config.yml settings for prisonCommandHandler.exclude-worlds.


* **Bug fix... this is a continuation of a prior issue of prison commands not being mapped to their assigned command name by bukkit when prison's command handler registers them.**
This was an issue with another plugin that registered `/gui` before prison was able to, so then all of prison's gui commands were mapped to `/prison:gui`.  So where this was an issue was with `/prestige` trying to run the gui presetige confirmation which was trying to kick off a GuiPlus command as a result of this improper mis-match.
Tested to confirm it is now functional.  Changed all occurrences that I could find that also needed to be mapped.


* **Start to setup support for WorldEdit and WorldGuard.**


* **Setup a way to pull a config's hash keys.**
These would be used to dynamically get all settings within a hash.


* **Fixed an issue with prison commands being remapped, but other commands within prison were not using them.**
This tries to find the remapped command for all commands by updating the SpigotCommandSender.dispatchCommand().


* **Fixed an issue where the setting isAutoFeaturesEnabled was not being applied to the permissions which resulted in the perms always being enabled when OPd.**




* **2023-07-07 v3.3.0-alpha.15 Released**



See [Prison Change log v3.2.3-alpha.15](prison_changelog_v3.3.0-alpha.15.md)



**Prison v3.3.0-alpha.14 2023-01-23**


See [Prison Change log v3.2.3-alpha.14](prison_changelog_v3.3.0-alpha.14.md)



---------------------------



**3.3.0-alpha.13 2022-08-25**

Highlights of some of the changes included in this alpha.13 release. Please see the change logs for all details.


* Added a new tool: `mines tp list` which will show a player all of the mines they have access to.  They can also click on a listed mine to generate the TP command.  This command can also be ran from the console to inspect what players have access to.
* Fixed a recently introduced bug where if the server starts up, but someone has no ranks, it was not able to properly assign them their first default rank. It was leading to circular references.
* Fixed an issue with color codes not being translated correctly with placeholderAPI.   
* Prison has a rank cost multiplier where ranks on different ladders can increase, or decrease, the cost of all ranks the player buys.  So when they prestige, it makes ranks A-Z cost more each time.  What's new is that now you can control which ladders these rank cost multipliers are applied to, such as not on prestiges, but only on default.
* Fixed calculations of the placeholder `prison_rank__player_cost_rankname`.  It was not fully working with every possible rank on every possible ladder.  Now it works correctly if trying to get the player's cost for even many prestige ranks out (it includes cals for all A-Z mines at multiple passes).
* Mine bombs: Changed to only allow mine bombs to be setoff withn mines the player has access to. Fixed an issue with color codes within the mine bomb's tags.
* Fixes issues with NBT, color codes with prison broadcast commands.
* Rewrote topN for better performance: `/topn`. Older players are archived within topN and can be queried: `/topn archive`. 
* Update ladder details on a few commands.
* Update XSeries from v8.8.0 to v9.0.0 so prison now supports 1.19.x blocks.
* Bug fixes with first join events.  Bug fix with a few guis.
* CMI update: If CMI is detected at startup, and delayed startup is not enabled, prison will go in a simple delayed startup mode to allow CMI a chance to enable it's economy through vault.  This reduces the learning curve with CMI users.
* New feature: Prison will now make an auto backup of all files in it's directory when it detects a change in version.  Can manually backup too. The backup stores temp files then removes them from the server, this helps keep the server clean.
* Update bstats: Gained control of the account and started to add useful custom reports to help zero in on what we need to help support.
* More work on block converts. Will be added in the next alpha releases.
* Bug fixes: mines gui fixes for virtual mines.  Sellall bug fixes. Placeholders fixes.




* **Minor addition to bstats.**


* **Player Mine GUI had the wrong calculation for volume which also threw off blocks remaining and percent remaining.**
The calculation for volume was using the surface area and not the total number of blocks.


**v3.3.0-alpha.12L 2022-08-25**


* **Updates to the bstats....**


* **New placeholders: `prison_rank__linked_mine_tag_rankname` and alias `prison_r_lmt_rankname`.**
Similar to `prison_rank__linked_mine_rankname` but uses the mine's tag instead of the mine's name.


* **Mine TP list: use mine tags and clickable mines to teleport to them.**


* **Mines TP list.  Added a new options to mines tp command to list all mines that the player actually has access to.**
Not finished with it... will add clickable links to them when in game.


* **There was an unused updated tool in prison.  It's against my policy to auto update this plugin, which would need to be consented to anyway, but I feel that admins need to be in full control of updates and know what is included in the updates. There was identified a potential exploit called zip-slip-vulnerability that could hijack a server if malicious zip is extracted.  Prison never used this tool, so it's been fully disabled with no intention of reenabling.  It may be deleted in the near future.**


* **TopN bug fix: If a player was in an archived state, they were not being moved to active when they would login.**


* **If the player is holding the mine bomb in their off hand, then remove the inventory from their off hand.**


* **v3.3.0-alpha.12k**


* **Fixed an issue when starting the server an no ranks exist.  Also fixes an issue when starting the server an a player has no rank.**
Was using a mix of really old code, and the latest code, which caused a conflict since neither was doing what it was really supposed to.


* **Added the custom bstats report for Prison Vault Plugins.**
This reports all plugins that have been integrated through Vault.  This report does not impact any other plugins report.  This is segmented by integration type.


* **Fixed bug when server starts up when no player ranks exist.**
It will now bypass the player validation until ranks have been configured.


* **v3.3.0-alpha.12j 2022-08-21**


* **Update bstats to remove old custom reports that are not wanted/needed anymore.**
Added 6 new placeholder reports that classifies placeholders ini various categories related to how they are used within prison.  Any placeholder that appears in these lists, will not be included in the generic 4-category placeholder lists.
Added a few more simple pie charts to cover a lot of the details on ranks, ladders, and players.  Simple is better so you can just glance at all of them, without having to drill down on each one.
 

* **v3.3.0-alpha.12i 2022-09-19**


* **TopN players - fixed an issue where topN was being processed before the offline players were validated and fixed.**
There was an issue with processing an invalid player that did not have a default rank.


* **v3.3.0-alpha.12h 2022-08-19**


* **Rankup costs: Minor clean up of existing code. Using the calculateTargetPlayerRank function within the RankPlayer object.**


* **PAPI Placeholders: Force color code translations on all resulting placeholders.**
There were a few issues where placeholder color codes were not being properly translated.  This was not consistent with everyone.  Not sure why it was working for most.
These changes are more in line with how chat handlers and MVdW placeholders works.


* **Ladder: apply rank cost multiplier to a ladder or not.**
This new feature enables you to disable all rank cost multipliers for a specific ladder.  Normally that rank cost multiplier applies to all ladders, but now you can suppress it.  It's for the whole ladder, and not on a per rank basis.


* **Fixed an issue with calculating the player's rank cost when they already on the presetiges ladder and calculating the higher prestige ranks.**
Appears as if this becomes an issue when at the last rank on the default ladder.


* **v3.3.0-alpha12g 2022-08-14**


* **Fxing of the calculations of the placeholder prison_rank__player_cost_rankname and related placeholders.**
The original implementation did not take in to consideration the prestige ranks in relation to the default rank.  
The improvements in this calculation now generates a list of all ranks between the current rank and the target rank.  So if a few prestige ranks out from the player's current prestige rank will result in calculating every rank in between including multiple passes through all default ranks.  So if there are 26 default ranks, and the player is at rank A with no prestiges, then to calculate rank P4 would include the following ranks:
b --> z + p1 + a --> z + p2 + a --> z + p3 + a --> z + p4.  
This results in a total of 107 ranks that must be collected, then the player's cost for each rank will have to be calculated.  Then all of these must be added together to get the player's cost on rank P4.
This calculation has to be performed for each rank in it's entirety
Warning: this calculation on high prestige ranks will be a performance issue. If this becomes a problem on any particular server, then the only recommendation that can be provided is not to use any of the prison_rank__player_cost placeholders.


* **TopN : a few more adjustments to fix a few issues with duplicates and also with using values from within the topN to include in the report to help minimize the need to recalculate everything especially with archived entries.**


* **Mine bombs: Fixed an issue with the mine bomb names not always working with color codes.**
Honestly the wrong function was being used so how it even worked I don't know. lol


* **New topN functionality: far better performance, with regular updates.**
TopN now is a singleton and is self contained.  When the singleton is instantiated, it then loads and setup the prisonTopN.json file on the first run.  30 seconds after the initial load, it then hits all players to load their balances in an async thread.  
The command /ranks topn, or just /topn has new parameter: "archived".  Any player who has not been online for more than 90 days will be marked as archived.  The archived option will show just the archived players.
Setup new parameters within config.yml to control the topn behavior with the async task.


* **v3.3.0-alpha.12f 2022-08-08 ** (forgot to commit when made this version)


* **Mine Bombs: Only allow bombs to be placed when within a mine that the player has access to.**
This will help prevent wasted bombs.


* **Fixed an issue with nbt items not having a value for toString().**


* **Encode color codes for the prison utils broadcast command.**


* **Added an "invalid player name" message to the rankup commands.**
Also added missing messages to the zh_TW.properties file. 


* **BlockEvents were changed to auto display the existing rows so it's easier for the end user to know which row to select.**
All they need to do is to enter the mine's name, then press enter to submit the command, and then the existing rows details will be shown.  Then the user can select the row and complete the command.
Updated docs on block events.


* **BlockEvents were changed to auto display the existing rows so it's easier for the end user to know which row to select.**
All they need to do is to enter the mine's name, then press enter to submit the command, and then the existing rows details will be shown.  Then the user can select the row and complete the command.


* **minor updates for disabled mine reset times. No functional changes were  made.**


* **Fixed a potential NPE with giving the players overflow blocks, but not sure what the exact cause was, but looked like there was an issue with mapping to a spigot item stack.**


 **CMI delayed startup: Added new feature to try to auto enable Prison's delayed startup if CMI is detected as an active plugin, and if the delayed startup is disabled within the config.yml.**
This is to help get more CMI users up and running without more effort, but yet still provide the ability to customize how it is triggered.
If CMI is active, there is NO WAY to disable a delayed startup check.*

* **Added the the option for playerName to the `/rankup` command so the command can be scripted and ran from the console.**


* **There was another issue with using `/gui` related to no ladders being loaded.**
This fixes that problem, and it appears like the issue was caused by plugman messing things up.  This does not "solve" the problem with ladders not being loaded, but prevents the NPE from happening.


* **There was an issue with `/prison reload gui` causing a NPE.**


* **Fixed the `/ranks topn` command (`/topn`) to sort the list of players before printing the list.**
The list was being set a server startup time, and if someone would rankup or prestige, it was not reflecting their new position.  The list is also now sorted after each rankup.  Sorting should be a low cost operation since the list used never is regenerated so the changes made during sorting is minimal at best.


* **Added the ability to control the prefix spaces on the unit names.**
NOTE: may need to enable the use of the `core_text__time_units_short` since the long units are not being used.  May need to create another placeholder for short/long.  It used to be short, so may need to use long with the new placeholder and convert the calcs to the short as the default.
This was requested by PassBL.


* **v3.3.0-alpha.12e**


* **Fixed issue rank null issues when showing ladder details.**


* **Prison backups: Fixed an issue with folders not existing when running the backups the first time.**


* **v3.3.0-alpha.12d 2022-07-25**


* **Added more information on ladder listing to show name, number of ranks, and rank cost multiplier.**


* **bStats update: Added a new bstats custom chart for auto features.**


* **Update some docs. Added docs for Prison Backups**
[Prison Backup Document](prison_docs_050_Prison_backups.md)


* **Upgrade XSeries from v8.8.0 to v9.0.0**


* **Fixed issue with prison version check triggering a backup upon startup.**
It was always bypassing the previous version check, so it was always creating another backup.


* **Update bstats by moving to its own class in its own package.**
Added 4 new custom charts to split the plugins in to 4 parts.


* **Fixed a few issues with the ranks gui where they were using the wrong message key (placeholder).**


* **Prison v3.3.0-alpha.12c**



* **Prison bstats: setup 4 new bstats charts for prison.  May change a few charts or add new ones in the near future.**
Got control over the prison bstats so can now add custom stats.


* **Prison backups: Created a Prison/backups/versions.log file which gets logs when a new prison version is detected on startup, which also performs a backup.**                                                                                                                                                                                      
All backups are also logged in the versions.log file too.


* **v3.3.0-alpha.12b**
- Added the fix for the placeholders. See next note.


* **Fixed an issue with placeholders not be properly evaluated; there were 3 sections and they were combined in to one so it would not bypass any.**


* **Possible bug fix with first join: it appears like it was inconsistant with running the rank commands.  Fixed by rewriting how the first join event is handled.**



* **Prison backups: Added new features where it is generating a stats file in the root of the zip file which contains all of the "prison support submit" items.**
This is just about ready, but lacking support for auto backups when prison versions change, or job submission to run auto backups at regular intervals.


* **Setup a prison backup command that will backup all files within the prison plugin folder.**
When finished, it will delete all temp files since they have been included in the backup.
The new command is `/prison support backup help`.


* **v3.3.0-alpha.12a**


* **Added a new set of intelligent placeholders: these show the tags for the default ladder and prestige ladder, for the "next" rank but are linked together.**
They only apply to the default ladder and the prestige ladders.  The tags are only shown if the player has that rank, or if that will become their next rank.  
These ONLY show the tags that will be appropriate when the next rank up.  So if the can still rankup on the default ladder, then only the default rank shows the next ranks tag.  If they are at the end of the default rank, then it will show the next rank on the prestiges ladder; if they do not have a rank there currently, then it will show the next prestige rank with the default rank showing the first rank on that ladder.


* **When the command handler starts up, it now logs the pluigin's root command and the command prefix which is used if there are duplicate commands found during bukkit command registration.**


* **Bug fix: Placeholders search was missing the assignment of the placeholderKey, which is what would like the search results on the raw placeholders, with the actual data that is tied back to the player.**
In otherwords, without the PlaceholderKey it was not possible to extract the player's data to be displayed within the command: /prison placeholders search.


* **Added constants for the default and prestiges ladder name so it does not have to be duplicated all over the place, which can lead to bugs with typos.**


* **Sellall bug fix: There wasn't a common point of refernce to check if sellall is enabled.  Many locations were directly checking config.yml, but the new setting has been moved to the modules.yml file. ** 
If config.yml has sellall enabled in there, it will be used as a secondary setting if the sellall setting in modules.yml is not defined or set to false.  Eventually the config.yml setting will be removed.


* **Found that bStats was erroring out with the servers hitting the rate limit so this makes a few adjustments to try to get prison to work with bstats.**
Basically plugins that load last will be unable to report their stats since every single plugin that is using bstats submits on it's own, and therefore it quickly reaches the limits.


* **BlockConverters: More changes to block converters.**
Added defaults for auto blocking, and for auto features with support for *all* blocks.


* **Bug fix: mines gui was not able to handle virtual mines with the internal placeholders.
This bug fix was included with the deployment of alpha.12 to spigotmc.org.



* **Pull Request from release.branch.v3.3.0-alpha.12 to Master - 2022-06-25**


This represents about six months of a lot of work with many bug fixes, performance improvements, and new features that have been introduced. The last two alphas were not pulled back to main, but they were released, This PR will preserve the released alpha as it has been published.

Also, this helps to ensure that this work will not be lost in the event the bleeding branch is lost/removed. Hopefully it won't be, but a lot of work has gone in to it and it will be impossible to recreate the current state of the alpha release.

This version, v3.3.0-alpha.12, has 300 commits and 323 changed files. The list of actual changes since v3.2.11 is substantial and the change log should be referenced.

Highlights of some of the changes include (a sparse list):

*    new block model - full support for CustomItems custom blocks - updated XSeries which mean prison supports Spigot 1.19.
*    major improvements to auto features - streamlined and new code - higher performance - many bugs eliminated - now supports drop canceling to make prison more compatible with other plugins
*    better multi-language support - supports UTF-8
*    Improved rankup - rankup commands are now ran in batch and will not lag the server if players spam it
*    rewrite of the async mine resets - next to impossible for mine resets to cause lag - Uses a new intelligence design that will throttle placing blocks as the server load increases, which makes it next to impossible for it to cause lag.
*    Enhanced debugging tools - if a server owner is having issues, prison has more useful tools and logging to better identify where the issues are - new areas are not able to log details when in debug mode - debug mode now has a "count down timer" where if debug mode is 8enabled like /prison debug 10 then it will only allow 10 debug messages to print, then it will turn off debug mode automatically. This is very useful on very heavy servers when a lot of players are active... it prevents massive flooding of the console.
*    Major rewrite of the placeholder code that identifies which placeholder raw text is tied to, so it can then retrieve and process the data. - Pre-cache that provides mapping to raw text, so once it is mapped, it can prevent the expensive costs of finding the correct placeholder - Added the beginning of tracking stats (through the pre-cache0 and will be adding an actual placeholder cache in the near future.
*    Mine Bombs - fixes and enhancements
*    Starting to create a new sellall module that will support multiple shops and custom blocks (not just XMaterial names)
*    Block Converters - Will allow full customization on all block specific things within auto features - will eliminate all hard coded block
*    Started to add NBT support. - Used in mine bombs - Starting to use in GUI's to simplify the complexity of hooking actions up with the menu items.
*    Added rank scores and top-n players - Rank score is a fair way to score players within a rank. It's currently the percentage of money they have to rank up (0 to 100 percent), but once they cross the 100% threshold, then 10% of the excess subtracts from their rank score. This prevents camping at levels.
*    There is more stuff, some major, a bunch of minor, and many bug fixes along the way.





* **v3.3.0-alpha.12 2022-06-25**



* **v3.3.0-alpha.11k 2022-06-20**
Plus luckperms doc update.


* **Mine resets: Fixed an issue when dealing with zero-block resets on a very small mine, such as a one block mine in that the 5 second delay was preventing from rapid resets.**
Bypass both 5 second cooldown on resets and blockmatching when 25 blocks or less for the mine size.
With running resets in async mode, with rapid resets for a one-block mine, the handling of the block breaks can occur out of order, which will trigger the block mismatch.


* **Fix issue: On the creation of a new mine, it would reset the mine a number of times.  This fixes the problem by only allowing one reset every 5 seconds at the soonest.**



* **Placeholder fix: The PAPI placeholder integrations should not be prefixing raw text with "prison_"; that is the task for PlaceholderIdentifier.**


* **minor items changed with the GUIs... no functional changes.**


* **Update a number of docs...**


* **Fixed an issue where if you try to use a % on a number it's causing String format errors.**
This now strips off % and $ if they are used.


* **Update Docs: LuckPerms groups and tracks... added images and fixes a few minor things too.**


* **Update some of the docs on setting up luckperms and tracks.**


* **v3.3.0-alpha.11j**


* **Since the chat event is handled within the spigot module, and since ranks and mines would just duplicate the processing since they both will hit the SpigotPlaceholder class, it made sense to handle the chat event directly within the spigot module.**


* **Updates to the prison placeholder handler.  This fixes a bug with chat messages return a null value.**
These changes also allows the pre-cache to track invalid placeholders now, so it can fast-fail them so it does not have to waste CPU time trying to look up which placeholder key they are tied to.


* **v3.3.0-alpha.11i**
Getting ready to release alpha.12.


* **Placeholder stats: A new feature that is tracking usage counts with placeholders.**
This is not a placeholder cache that caches the results, but it caches the placeholder that is associated with text placeholder.  The stats currently only tracks the total number of hits, and the average run time to calculate the placeholder. 
The pre-cache will reduce some overhead costs.  This also provides the framework to hooking up a formal placeholder cache.


* **Placeholders: changed the two top_player line placeholders that are the headings**
 since they originally had _nnn_ pattern that is getting messed up in some settings.  So removal of the nnn helped to getting it working.


* **GUI MInes: Update support for custom lore support within the gui configs.**


* **Update XSeries from v8.7.1 to v8.8.0 to better support the newest blocks.**


* **Updated item-nbt-api-plugin from v2.9.2 to v2.10.0.**


* **v3.3.0-alpha.11h 2022-06-14**


* **Prison Placeholders: General clean up of obsolete code.**
Since the new placeholder system is working well with the new class PlaceholderIdentifier, obsolete code that was commented out has been removed.
The obsolete class that used to be the key component to identifying placeholders was PlaceholderResults and is no longer used anywhere.  It's core components were moved to PlaceholderIdentifier and therefore all references to this obsolete class has been eliminated.
At this time, PlaceholderResults has not been deleted, but will be at some future time.


* **Prison Placeholders: Major rewrite the handling of placeholders.**
Prison's placeholder handling was completely rewritten to better handle the matching of a placeholder text with the actual placeholder objects.  Over the last few years, many new features were added to prison's placeholders, but the way they were implemented were through patching existing code. This rewrite starts from scratch on how placeholder are decoded. Placeholders are now only decoded once instead of being decoded when attempting to match each internal placeholder.  The results are significant performance improvements and eliminates a lot of redundant code. Some new features were add, such as supporting more than one placeholder attribute at a time.  Also it streamlines how parameters and data is passed from the outer most layers of prison to where the placeholders are calculated. 

Another major benefit of this rewrite, beside reduction of code complexity and performance improvements, is that it opens the door to being able to implement an internal placeholder cache.  Some plugins request placeholder data once per tick, or 20 times per second.  Multiply that by 50 online players, and you got prison performing the same calculation 1000 times per second. Caching could help reduce that to only one calculation per second (assuming a cache time to live value of 1 second. Caching will not always be so simple, or possible, or every placeholder.  Player-based placeholders can't be cached like static mine placeholders (mine names and mine tags as an example).


* **Add support for Portuguese.**


* **BlockConverters: fix issue when block converters are not active.**


**v3.3.0-alpha.11g - 2022-06-11**


* **Disable the gui for autofeatures configs.  They are so out of date, they were causing problems.**
Autofeatures should be manually edited.


* **Fix a problem when BlockConverters are disabled, and doing a reload on auto features, it's not able to find that config file so its throwing an exception.**


* **The build was failing intermittently on the continual integration (CI)**
pertaining to the item-nbt-api-plugin, so an entry to added to "lock it in" to the correct path within the mavenrepository.com repo.
This should prevent the resource from being paired with the wrong repo.


* **There is a situation when checking for new updates to the language files, that it needs to write the new file, but the old one has not been archived.**
This now checks to make sure the old one has been renamed, and if it hasn't, then it will rename it.


* **Added an entry for the sellall module in the modules.yml file.** 
Code has been setup to check, with a default fall-back on to the sellall settings within config.yml file.  The entry in config.yml has been commented out. 
Either will work, but the setting within modules.yml will take priority.


* **Update XSeries to v8.7.1 from v8.6.2.**
Note that this does not add any of the newer 1.19 blocks or items.


* **GUI: Fixed some issues with the gui and admin perms.  Added some admin perms to a few gui commands to lock them down.**
Found a serious issue with non-admins being able to edit rank costs and sellall item costs.  The GUIs were not locked down and if the players knew the commands, they could edit the costs.


* **v3.3.0-alpha.11f 2022-06-06**


* **BlockConverters: minor changes.**


* **Bug fix: Backpacks were not working properly with just ".save()" but had to add ".setChanged()" too, otherwise minepacks will not actually save the status of the backpacks.**


* **BlockConverters: rename targets to outputs.**


* **BlockConversions: hooked up the code to not only filter and return the blockConversions for the player and the block, but to also return the item stacks from the results.**
This is just about ready to be used in the code.


* **Romanian Locale language files were placed in the wrong location.**
Oreoezi provide two new language files for the Romanian Locale, but they were placed in the wrong location.
They were added to "prison-core/out/production/resources/lang/core/" and ".../mines/".  For them to actually
work correctly, without being deleted, need to be placed within the following path:
"prison-core/src/main/resources/lang/core/" and "prison-core/src/main/resources/lang/mines".
These should now be usable.  Also the LocaleManager now has alternatives setup to default to en_US; future
alternative languages can be added in the future.


* **BlockConverters: add some validators to the BlockConverters.**
Reports various issues, fixes non-lowercase source block names, and also disables invalid settings.


* **BlockConverters: Adjusting around how they are setup, and how they are generated.**
BlockConverters are now in their own config file: blockConvertersConfig.json.
They are no longer being stacked/placed in the autoFeaturesConfig.yml file, so all the conversion code is no longer required. With it being json, it now can reflect the java classes without any special considerations on the conversion process.


* **BlockConverters: More work on these settings.**
Setting up to work with AutoFeaturesConfig.yml, but having second thoughts about adding these configs to that file since it will complicate the config details.


* **Fixed a bug on the smelting of coal_ore which was yielding 10 times too much, but this was never seen since a silk touch pickaxe would have to been used.**


* **Placeholder fix for `prison_mines_blocks_mined_minename` since it was not being incremented after the fixing of the autopickup=false and handle normal drops = true.**
Also found that the calculated field for the mine's total blocks mined was not being recalculated after load the mines from their save files.  This now is working properly.


* **Major exploit fix: sellall was not indicating that the inventory was changed within the Minepacks backpacks,**
and therefore players were able to sellall of their inventory, logoff, and then when they log back on, it will be restored.
Now, all inventory changes are forcing a save for the backpacks.


* **Fixed an incorrect mapping to a message: auto features tool is worn out.**


* **v3.3.0-alpha.11e 2022-05-23**


* **Bug fix: Fixed an issue with sellall when the module sellall is not defined but sellall is enabled in the config.yml file.**


* **Bug fix: Minepacks has a new function in their API to force backpack changes to be saved.**
Before it could only be marked as changed, which was not enough to get it to save in all situations. Prison is now calling "save()" to ensure its behaving better now.
NOTE: releasing this fix with alpha.11d even though it has been added after being set to 11d.


* **Prison v3.3.0-alpha.11d 2022-05-22**


* **GUI messages: a few more updates and corrections**


* **GUI: More fixes to the gui messages... including moving all of the new gui specific messages out of prison-sellall module to the prison-core module so they will still be accessible if the prison-sellall module is disabled.**


* **GUI cleaned up by eliminating so many excessive uses of translating amp color codes to the native color codes.**
Found some locations where there were at least 7 layers of function calls, with each layer trying to translate the color codes, which of course was excessive.


* **Change the name of the SpigotSellallUtilMessages class to SpigotVariousGuiMessages due to the fact these messages are used in more than just sellall.**
It should be noted that eventually the non-sellall messages may have to be removed from the sellall module.


* **Spigot GUI Messages: Hook up more messages to prison's messaging system.**


* **Sellall messages: Start to setup the correct usage of the multi-language message handling through the new prison-sellall module.**
This fixes the messaging within the SellAllUtil class.


* **Move auto feature messages to the spigot message file so they can be customized.**
Removed the inventory full messages from the AutoFeaturesConifg.yml file.


* **The normalDrops processing was not hooked up to the newest way auto pickup is disabled, which was skipping normalDrops if auto pickup was disabled.**
The number of blocks in the normalDrops is now being passed back through the code so it can identify that it was successful and finalize the processing.


* **3.3.0-alpha.11c 2022-05-14**


* **GUI Menus enable NBT support.**
This is a major change.  The details for the menus options and commands are now stored in NBT data so they do not have to rely on the item name, lore, or other tricks.
This is a first phase, and more work needs to be done to remove hooks with the item names for other menu options.  Main set of changes has been done to the menu tools. 


* **Changed placeholder attributes to print the raw value and placeholder.**
Changes to the logging to allow & to be encoded to the unicode string of 
`U+0026` so it can bypass the color code conversions, then it is converted back
to an & before sending to bukkit.  This works far better than trying to 
use Java regEx quotes.


* **Fixed signs for sellall to enable them to work with any wood variant.**


* **3.3.0-alpha.11b 2022-05-02** 


* **Placeholder fix for formatted time segments to use the values setup in the language files within core.**
This allows the placeholders to use the proper notations for singular and plural units of times as configured for each language.


* **Placeholder fix for rankup_cost and rankup_cost_remaining on both the formating of the percents and the bar.**
The percents were being displayed as an integer, so with rounding, they were very misleading since they would show 100% when they were really hitting 99.5% and higher.  Also the bar is not working better, and if the percentage is less than 100%, then it will always show a RED segment at the end of the bar; it will ONLY show GREEN when it's 100% or higher.


* **Mine Bombs fix to allow color codes within the bomb's name.**
The color codes are removed for the sake of matching and selecting when giving to players so you don't have to use them in the commands.


* **Placeholder issues when not prefixed with "prison_" is being addressed by prefixing the identifier with "prison_" right away.**
This "is" addressed, but it's deep in the code and for some reason certain parts of the code is not making the connection to the correct placeholder without that prefix.  So this really is not the desired way to address this, but it eliminates the problem.  The reason why it's not the desired way, is because it's exposing buisness rules of how to handle the placeholders, outside of the placeholder core code.


* **Bug fix... with placeholder prison_rank__player_cost_remaining_rankname, and its variants,**
 eliminate the calculation of including the current rank since that has already been paid for.  Prior to this fix, it was only excluding prior ranks.


* **3.3.0-alpha.11a 2022-04-25**


* **Mine Bombs and NBT settings: this fixes mine bombs to work with NBT tags, which are being used to identify which items are actually mine bombs.**


* **Fixes the mine bomb usage of lore where the lore that is defined in the settings is no longer altered so it's now used verbatim.**
Also the check for mine bomb is removed from using the name, or first line of lore, and now tries to use NBT data.
But note, that the NBT data is not working correctly yet.


* **Fixed the usage of setting up the NBT library within the gradle config file.**
Fixed issue with unknown, or incompatible items were unable to be parsed by XMaterial which was resulting in failures. This fixes the problem by preventing the use of a partial created SpigotItemStack.


* **Hook up the NBT library to the SpigotItemStack class.**
This has not been tested yet to see how it works, especially between server resets.


* **Added NBT support to prison.  This loads a NBT library to be used only with the spigot sub-project.**
This has not been hooked up to anything yet.


* **Placeholder fix: Problem with the placeholder getting a prestige rank that was one too high.**
The following placeholders were fixed: prison_rrt, prison_rankup_rank_tag, prison_rrt_laddername, prison_rankup_rank_tag_laddername


* **Hooked up the BlockConvertersNode to the yaml file IO code so it will save and load changes to the auto features configs for anything with the BlockConverters data type.**
Removed unused functions.


* **Mine reset potential bug fix: Some rare conditions was causing problems, so using another collection to pass the blocks, and getting the size prior to calling the function to prevent the problems from happening.**
This appeared to be happening when a mine was being reset multiple times, at the same time.  The mine should never be resetting multiple times, at the same time.  May need to add more controls to prevent it from happening.


* **Bug Fix: The IGNORE block type was not marked as a block, therefore could not be used within a mine.**


* **New feature: Block Converters. Setup the initial core settings for block converters within the auto features.**
The core internal structure is in place and so is the ability to write the data to the file system. 
This has not been hooked up to anything yet.  


* **Setup placeholder formatted time values to use the language config file.**
This set of values will "NOT" reload when the command `/prison reload locales` is ran.  The server must be restarted to reload these values.


* **CustomItems getDrops() debug mode will list the results of the get drops.**
This will help track what's going on with the getDrops function since it's a complicated process.


* **Placeholders: prison_rankup_rank_tag (and the ladder variants) now shows the prestiges next rank when at the top rank in the default ladder.**
This only applies to the default ladder and only if the prestiges ladder is activated.


* **Pull out the setBlock and blockAt functions from the SpigotWorld class so that way it would properly track within Timings.**



** v3.3.0-alpha.10 2022-04-02**

** Release notes for the v3.3.0-alpha.10 release as posted to spigotmc.org and polymart.org:

v3.3.0-alpha.10

This alpha.10 release includes many significant performance improvements and bug fixes.  Although this is an alpha release, it is proving to be stable enough to use on a production server.  Please make backups and test prior to using.  This v3.3.0-alpha.10 release is "still" backwards compatible with v3.2.11 so you should be able to down-grade back to v3.2.11 without major issues. The breaking changes that will be in the final v3.3.0 release have not been applied yet to these alpha releases.

Please see our discord server for the full listing of all bug fixes and improvements, there have been more than 70 updates since the alpha.9 release.  The following is just a simple short list.

- Many bug fixes.  Some that even predates the v3.2.11 release.  

- Performance improvements: startup validations moved to an async thread. Slight delay between mine validations to allow other tasks to run (needed for less powerful servers). Improvements with sellall performance.

- Added more support for Custom Items (custom blocks)

- Added support for top-n players and added over 30 new placeholders.  Top-n support for blocks mined and tokens earned will be added shortly too.

- Upgraded internal libraries: bstats, XSeries, gradle, custom items, and a couple others.

- Many fixes: Mine bombs, sellall, autosell, auto features, block even listening and handling.


* **Ran in to an issue with spigot versions < 1.13 where a data value outside of the normal range prevents XMaterial from mapping to a bukkit block.**
This change provides a better fallback which ignores the data value, which is the varient.  The drawback of ignoring the varient type, which is outside the valid ranges anyway, is that it may not accurately reflect the intended block types.  But at least this will prevent errors and being unable to map to any blocks.


* **Change to prison startup details reporting to elminate duplication.**
Near the end of the prison startup process, prison would run the `/prison version` command to provide additional information in the logs.  This was duplicating some of the information that was already printed during the actual startup process. 
Changes were made to only add the information that was missing so the whole command does not need to re reran.  Overall this is a small impact, but a useful one.  It does shift where these functions live and ran from.


* **ChatDisplay: An internal change that controls if a chat display object (multi-lined content, such as command output) displays the title.**
This will be useful when integrating in to other commands and workflows, such as redesigning how the startup reporting is handled.


* **v3.3.0-alpha.9g 2022-03-29**


* **auto features: Enable player toggle on sellall for auto feature's autosell.**


* **sellall reload - fixed issue where the reload was not chaning any online valus or settings.**


* **Mine bombs cooldown - ran in to a null value in the cooldown timers. Handles this situation now. **


* **Sellall - added debug logging on the calculation of sell prices.**


* **Sellall bug fix on calculation boolean config values; it was not returning the correct value.**
This was found by a report that `/sellall hand` was not working.


* **Auto features bug fix: was paying the player, instead of just reporting the value when in debug mode.**


* **Update debug info in auto features to properly show it's within the BLOCKEVENTS priority processing.**


* **Topn calculations: handle a null being returned for the prestige ladder.**


* **Enabled a sellall feature to enable the old functionality where sellall ignores the Display Name or is not a valid prison block type.**


* **Fixed an NPE issue with checking to see if a block exists within a mine.**
This issue was impacting spigot versions less than 1.13.  The problem is with data values being abnormal and out of the standard range.


* **Fixed a NPE on the topn calculations.**


* **auto features autosell when inventory is full when using the priority BLOCKEVENTS.**


* **topn fix: If next rank is null, then try to use the next prestige rank for the cost.**


* **v3.3.0-alpha.9f 2022-03-25**


* **Placeholders top player: added new placeholders based upon the _nnn_ pattern to identify the player.**


* **Top-n players listing: added an alternative line.**


* **Placeholder Bar Attributes: Now supports a non-positional keyword "reverse" which will take any bar graph and reverse it.**


* **AutoFeatures debugging: Some color change in the logging details for failures so they are easier to see.**


* **Prepare for the handling of STATSPLAYERS placeholders, which will be the ones that provides the placeholders for the top-n players.**
This handles the workflow on handling the placeholders.


* **Slight update on how the top-n players are printed... simplifies and also cleans it up the formatting.**


* **Updated the rankup accuracy to be greater than or equal to 1.0.**
And conditionally only report the accuracy_out_of_range if >= 1.0.


* **When validating the success of a rankup transaction's abiliity for the rankup cost to be applied, the validation is now checking to see if it's within a plus/minus of 1.0 from the target final balance of the player.**
This covers the inability of floats and doubles not being able to accurately repesent base 10 numbers all of the time, which the accuracy may be off by a small value such as 0.000001, but that will prevent an equality check from passing.  
By checking that it's within a range of plus/minus one will help prevent false failures.


* **Fixed issue where ranking does not which rank is associated with each rank.**
Now the ranks will properly track the players at their ranks.


* **3.3.0-alpha.9e 2022-03-14**


* **Top-n: More work to enable.  Now supports /ranks topn, with alias /topn.**
The rank-score and penalty is not yet enabled.  Placeholders will be enabled after the command is fully functional.


* **Prison startup performance fix: On large servers with many players, the process of getting the player's balance from the economy plugin can cause significant delays if that plugin is not able to handle the load...**
so the validation of the players and the sorting of the top-n list is now ran in an async thread so it will not cause lag or delays on startup.


* **Prison version: including more information on ranks and add the ladder rank listing to the prison version command.**


* **Removed some old code from block event processing...**


* **Mine bombs getting a replacement blocks from the player's location.**


* **CustomItems drops: If custom items do not produce a drop, then default to dropping the block itself.**


* **Sellall: prevent selling with custom name items.**


* **PlayerCache earningsPerMinute: Sychronize to prevent an issue with concurrent mods.**


* **Mine bombs: Fix an issue with the generated mine bomb tool not being enchanted with the specified fortune, which also was effecting the durability and dig_speed too.**


* **Reworked how some of the registered event listeners are setup, which is needed for expanding to supporting other plugin's enchanments.**


* **Update the bstats configs for v3.0.0.**
Although it compiled without the bstats-base, it failed to run.  I suspect my local cache for gradle was incorrectly providing objects when it shouldn't have.


* **Upgrade bstats to v3.0.0, was at v2.2.1.**
Hoping this will better report the proper usage.
Added more custom details on the graphs: player count, defaultRankCount, prestigesRankCount, otherRankCounts.
Set api version to v3.3.


* **BugFix: Prevent a possible NPE when blocks are null when calculating gravity effected blocks, and ensuring there is a location when trying to place blocks.**
Both of these should never be an issue, but based upon different conditions, they can become an issue.


* **Added an autoFeatures to enable/disable the use of CustomItems' getDrops().**


* **CustomItems integration: Adding support for getDrops() from CUI.**
This integrates custom blocks in to getting the SpigotBlock (an internal prison block).
It's not yet functional due to issues within CUI, but this is the initial setup.


* **Report that bedrock users are not getting their tokens.**
When in debug mode, if their balance is not correctly updated it will report it in the console.


* **v3.3.0-alpha.9d 2022-03-10**


* **Within the SpigotBlock, now has hooks to load CustomItems blocks when trying to convert an org.bukkit.Block to a SpigotBlock.**


* **For unbreakable blocks, reinforce that the location, which is the key, will not be null.**
The block sometimes can be null, so by having the seperate location will not cause a failure if the block is null.


* **Fixed an issue when checking if a block is unbreakable... it should not have been null, so this is a temp fix to prevent an error.**


* **CustomItems custom blocks: Hook up the new drops for CustomItems plugin.**


* **Update some of the gradle settings and fix the new custom items api.**


* **Upgrade XSeries from v8.5.0.1 to v8.6.2.**


* **Update CustomItems API from v4.1.3 to v4.1.15.**
This update adds support for prison to get the drops from the CustomItem blocks.


* **Changed the development environment and updated the java 1.8 to the latest release.**


* **v3.3.0-alpha.9c 2022-03-06**


* **Enable the ability to split messages in to multiple lines by using the placeholder `{br}`.**


* **Small adjustments to the MineReset handing of the targetBlock collections.**
Prevent their instantiation in the constructor since they are being lazy loaded.  Also synchronizing on the adding of target block, since there was one report on an issue with that not being synchronized.


* **Added more validation checks and reporting on rankups and demotes.**
So if something goes wrong, it can hopefully identified and tracked.  
If rank change failed, or if a refund failed, it will now better report these conditions.


* **Setup a return of success, or failure, on custom currency functions.**
GemsEconomy does not indicate if it was successful, but added code to check to see if it was successfully manually/indirectly.


* **Sync set blocks fixes. Isolate the targetBlocks and add a null check to ensure thre are no problems.**


* **RankLadder: removed obsolete code that was never used.**


* **Some initial setup for a rankScore.**
This is not hooked up yet, but the the core basics are there and should work soon.


* **Bug fix:  Fixed an issue were a block would be added, or changed, and it would change all similar blocks in all mines to have the same percentage.**
This issue was intermittent and was caused by directly getting the block from the master list, without cloning it.  The correction to this issue was to use a search function that would clone the block, but it also would compensate for custom blocks if the block's namespace was not initially provided.


* **Bug fix: Risk of a null on the blockHit, so add checks to ensure it's not before trying to process.**


* **Bug fix: The clickable delete code is that is generated is off by 1 on the inserted row.**
The row number needed to be reduced by one since the row number was incremented right before this final injection.


* **v3.3.0-alpha.9b 2022-02-28**


* **Fixed the command '/ranks ladder command remove' when specifying a row value that was too large.**
The message was only providing one value when it should have had two, and the first parameter was '%d' instead of '%1'.


* **PlayerCache: Unloading Players... when a player is being unloaded, and they are not in the cache, the unloading process is now able to indicate that the player should not be loaded.**
Also when trying to load a player, it will not attempt the load if the file does not exist.


* **Sellall bug fix... was using the wrapper to map it to an XMaterial which was causing NPEs.**
Using the prison's compatibility functions to perform the mapping, which will now provide a safer mapping that will not cause NPEs.


* **Module prison-sellall cleaned up gradle config to remove a few configs that are not needed.**


* **Fixed a bug with the blockEvent block filter for adding blocks, it was using the blockEvents collection instead of the prison blocks collection.**


* **Fix placeholder for prison_player_tool_lore to provide the actual tool's lore.**
The placeholder was not hooked up.


* **Mine manager when enabling mines after the delayed loading from multiverse-core delayed loading...**
put a slight delay on each submission of the startup air counts for each mine... spacing them out by one tick so they are not all trying to run at the same time.


* **v3.3.0-alpha.9 2022-02-27**


* **Bug fix:  Sellall error:  Resolve an issue with the off-hand not being removed when selling.**
Turned out that you can read all inventory slots, which includes the off-and slot, but when removing ItemStacks, the remove(ItemStack) function then ignores the off-hand slot.  Has to directly remove from the off-hand slot.


* **Mine bombs: fixed issue with lore not being added.**
Was adding the wrong source; was adding the destination to the destination.


* **Mine Bombs:  Add some basic validations when loading the mine bombs from the config files**


* **Mine Bombs: add a reload function for mine bombs.**
/prison reload bombs  or  /prison utils bomb reload


* **Removed warnings from the Vault economy wrapper since NPCs can actually initiate commands and NPC will always return nulls for OfflinePlayers....** therefore just return a value of zero.


* **New command added to '/prison support runCmd' to allow an OP process, such as a NPC in Citizens, to run a command as a player.**
For example this is handy for having an NPC open the player's GUIs such as mines or ranks.


* **v3.3.0-alpha.8h 2022-02-26**


* **Bug fix: Synchronized some of the collections that are needing it within the PlayerCache.**


* **Bug fix: Fixed an inventory glitch that was preventing items from being added to the inventory.**
Basically the inventory had items, but it was not updating the contents of the inventory on the client side.  This was fixed by updating inventory when finished processing the adds.
If autosell on full inventory is enabled, and there are extra drops, then sell them all before they make it to the inventory.  This works most of the time, but sometimes the inventory still fills up.  This is now more of a characteristic than a bug.


* **3.3.0-alpha.8g 2022-02-25**


* **More adjustments to the block events so the config setting can be shown in the header of the /prison support listeners blockevent command.**


* **Setting up support for the BLOCKEVENTS on all block break event listeners.**
Changed around how the listeners are created to simplify and be more accurate in the event states.


* **Extracted the BlockBreakPriority enum to be an object on its own.**
Added BLOCKEVENT and added information on what the various priorities should do.  This is in preparation to refactoring how events are processed.


* **Prison tokens: externalize the messages related to the admin tokens commands.**


* **For the admin commands for tokens, added an option to be able to suppress the messages.**


* **v3.3.0-alpha.8f 2022-02-23**


* **The creation of a new sellall module which will eventually contain the code to manage multiple shops that will be based upon ranks.**


* **Adjustments to the configuration of the mutex to better ensure that only one job is submitted for the reset, and to ensure other tasks are not locked up, or locked out.**
There was a report that the prior way was causing the mines to lockup.


* **v3.3.0-alpha.8e 2022-02-20**


* **Mine reset mutex is conditionally enabled to ensure the locks remain balanced.**
To ensure the mutex is enabled ASAP, its engaged outside of the normal location... it may only be a few nano-seconds savings, but with OP pickaxes mining with many players within one mine, the mutex must be enabled rapidly.


* **Bug Fix: Mine reset changes: Eliminate paged resets, some code that is not being use anymore, disabled the RESET_ASYNC type to be similar to RESET_SYNC since they are now the same, locked out checkZeroBlockResets so mines cannot reset multiple times at the same time using the MineStateMutex.**
The major issue here was that mines were being reset in the middle of a reset action.  Used a preexisting MineStateMutex to secure the checkZeroBlockResets() function to prevent it from kicking off many resets.  These multiple resets were happening because many players were triggering the resets... as a side effect, there were many situations of collections failing due to concurrent modification exceptions.  


* **Getting the collection size was an issue by the time it was done processing the blocks, so getting them first may help prevent errors.**


* **Made many changes to the default configurations of the autoFeatures.**
This is to try to make it easier to use prison by using more of the settings that are most useful.
Added more comments to make it easier to understand these settings too.f


* **Release v3.3.0-alpha.8d 2022-02-20**


- **Fixed issues with vault economy and withdrawing from a player's balance.**
It now also reports any errors that may be returned.


* **To prevent NPEs, isBlockAMatch has been changed to use the MineTargetPrisonBlock as a parameter and then internally, checking for nulls, it will extract the status data block.**
This was causing errors when processes were trying to access target blocks before they had a chance to initialize.


* **Address a rare condition where the mineTargetPrisonBlocks is being "accessed" before the system is done initializing the mine.**
This creates an empty collection, but it will prevent errors in the long run.


* **Add equals and hashCode to the MineTargetBlockKey so it can be better used in structures like HashMaps.**


* **Mine bombs: Added a {countdown} placeholder for use with the MineBomb's tagName field.** 
A few other adjustments such as adding more "color" to the default bomb tagNames.


* **Added validation check to make sure the player's balance was decreased, or increased, successfully before actually applying the rank change.**
If the balance does not reflect the change, then the rank change will be prevented.


* **Slight adjustment to addBalance so as to help reduce out of synch possibilities.**
The access to economy hooks, has been moved in to the sychronized block.


* **v3.3.0-alpha.8c 2022-02-16**


* **Fixed a start up issue with multiverse-core in that it now runs the air-count processes so the mines can have their targetBlocks defined.**
Many issues were resulting from failure to get the target blocks.  Not sure how it was working before, other than targetBlocks were not being used as much as they are now.


* **Fixed a potential error if targetBlocks are not loaded yet, or loaded at all for a given mine.**
Was causing NPEs.... 


* **Added logging for when a delayed world comes online and list all mines that are activated.**


* **v3.3.0-alpha.8b**


* **Clean up the way the command tasks were being called.**
Added mine name to the blockEvent logging.


* **Fixed a reversal of some calculations when converting nano seconds to milliseconds.**


* **New feature: debug count down timer.**
Able to now set a debug count down timer where debugging is turned off after logging that number of entries.


* **Potential bug fix in better managing if sellall should be enabled by directly checking the configuration parameter that enables it.**
Better logging of sellall when inventory is full.


* **Commit some SellAllUtil comments that are useful for debugging timing issues.**
These are now disabled, but can be manually reenabled when needed.


* **Some changes to Sellall to provide more flexibility and to fix some potential bugs**
The isEnabled now uses the proper boolean settings to indicate if the sellall utility is enabled or not.  Before it was trying to treat strings as boolean.


* **Add prison command descriptions that goes along with the placeholders.**
They are not yet hooked up, but they will provide more information to the admins on what the placeholders will provide, and also how they can use them since some of these will include examples of the formats.


* **Bug fix: The cancellation of the event was not being returned in the correct locations**, 
so it was bypassing all of the before mine reset commands. The before mine commands will now run correctly.


* **Prison commands: reorganize some of the structures used for the prison commands.**
Hook up some of the logging to track run times for each command.


* **Prison commands: reorganize some of the structures used for the prison commands.**
Hook up some of the logging to track run times for each command.


* **Prevent the autosell happening just because someone is op.**
To make this work, and to prevent odd behaviors where OPs suddenly are not able to mine correctly, OP can no longer use the autosell based upon perms.


* **Setup the time durations on reporting of mine resets to use external settings.**
Enables the use of singular and plural unit names. 


* **Rework how rankup commands are ran: in progress.**
This new way of dealing with rankup commands is to collect all commands that need to be ran, from all rankups, then run them in one group when the player is done being ranked up.
For most changes in rank, this will have zero effect on anything (mostly), but it has a huge impact with the **rankupmax** command.  
When hooked up (which is is not), this will take all commands and run them in a sync task.  So "every" command will run in a sync task. But each command will be monitored for run time, and if the runtime for one command exceeds a threshold, then the sync task will resubmit itself to run again after on tick.  This will slow down the process of running all of the commands, but it will help prevent them from causing lag.
With tracking run times on each command, if prison is in debug-mode, then it will generate console logs identify how long it take to run each command.  So if any given command is causing lag, then it would be possible to identify what the offending command is.


* **Fixed a problem before releasing... was not using the correct variable so the generated File object was not getting used.**


* **Bug fix: If a player cache file does not exist, it now prevents it from loading.**


* **Fixed an issue with the GUI, such that if the player does not have a rank on the ladder**, 
that it will now force the creation of a PlayerRank object so it does not cause a NPE.


* **Mine bombs: Enable the use of color codes on the armor stands when setting off the bombs...**


* **Mine bombs: Added durability and digspeed enchantments to the mine bomb data.**
This will allow for greater flexibility in how the tool in hand behaves.


* **If using a mine bomb, then do not allow durability calculations to be used**, 
since if the pseudo tool breaks, then what ever the player is holding will be removed, which is usually an item stack of mine bombs.


* **Mine Bombs: The mine bomb give command now is case insensitive.**


* **Mine Bombs: Add ability to set the Y offset.**
It defaults to a value of -1.  This allows fine tuning of bombs to better position them to sink deeper in the mine to increase the number of blocks that are included.


* **Fix issue with mine bombs not dropping blocks.**
The underlying block changed and therefore so did the behavior of the equals() function.


* **Added various token functions to the prison spigot API class.**


***3.3.0-alpha.8 2022-02-12**


* **Enable debug mode from within the config.yml file.**
It was not hooked up before.  This is useful for initial logging of the mine air-counts.


* **Redesigned the initial mine air-counts which not only identifies which blocks are within a mine upon startup, but it also establishes the number of air blocks in a mine to help ensure it's able to properly reset when the mine is empty.**


* **Bug fix: cleaned up the way PlayerCache files are managed.**
Eliminated a lot of old code and simplifed the logic to ensure the liklihood of preventing corruption of the player caches.  There has been some reports that the files were not being properly tracked and stats were being replaced with new entries. This also fixes some performance issues by caching the files in the directories.  So once loaded, the loaders no longer need to read the file listings, which could take a while with a lot of files.


* **Provide information on locale settings within the `/prison version` command.**
Falls back to the en_US properties file if the selected language file does not exist.
If the non en_US properties files are found to be missing a property, then the english property is used as a fallback.  These fallbacks are not written back to the save files.


* **v3.3.0-alpha.7 2022-02-09**
Set this back on an alpha release schedule.  The betas appear to have been pretty stable.


* **Disable the player's nms attempts to get their locale... spigot 1.17 and higher no longer can get that value.**
Just use the server's default value.


* **For the /prison support commands, the output is now sent to the player instead of just the console.**


* **Some minor changes to /prison debug to give it an alias of /prison support debug.**
Format a few of the messages to make it easier to understand.


* **Removed the backpack's object from the player's cache.**
 Backpacks are too massive for the player's cache and needs thier own cache system.


* **On the command /ranks set tag, added the note that if a tag is removed from a rank, then the rank name will be used instead.**
Fixed the placeholder for rank tags so if it is null, it no longer show a null, but now it show the rank's name.


* **Fixed the generation of the player mined block count placeholders.**
Was missing one _ after generating the specific block related placeholder.


* **Upgrade gradle from v7.3 to v7.3.1 to v7.3.2 to v7.3.3**
This is at the latest release.


* **Upgrade gradle from v7.2 to v7.3**
 - Changes to provide better security when runnign gradle to prevent injection attacks.


* **Upgrade gradle from v7.1 to v7.1.1 to v7.2.**


* **Upgrade gradle from v7.0.2 to v7.1.**
NOTE: There are a number of updates to apply for gradle.  Will commit on the minor versions and final version.


* **Added a few new placeholders and a new placeholder type of PLAYERBLOCKS.**
Added raws to the player_block_total per mine.  Added player_blocks_total and its raw counts, which is a PLAYERBLOCKS.


* **Added a few new placeholders and a new placeholder type of PLAYERBLOCKS.**
Added raws to the player_block_total per mine.  Added player_blocks_total and its raw counts, which is a PLAYERBLOCKS.


* **Changed around the logging of messages related to the use of autofeatures autosell.**
Added permissions to enable autosell on a per block.


* **Update CustomItems api from v3.7.17 to v4.1.3.**
This newer version of the API still does not have a getDrops() function.


* **Add more support for CustomItems plugin.**
It appears like this is working really well with auto pickup.  It should be noted that the CustomItems' API does not have a getDrops() so it's impossible to get the correctly configured drops for the block, so for now, it will only return the block itself and not any configured drops.
Sellall may need to be fixed and there could be some other areas that needs some fine tuning, but so far all is working well.


* **For CustomBlockIntegrations added getDrops().**
This has to be used instead of bukkit's getDrops() since that will return only the base item drops, which are the wrong items.
For CustomItems plugin, there currently isn't a getDrops() function in the CustomItems API so instead, the integration's getDrops() returns the block.


* **If cancelAllBlockEventBlockDrops is enabled when it's not valid on the server version, then it will print the error to console, then turn off this features**


* **CustomItems: Hook up on server startup the ability to check for custom blocks when scanning the mines to set the air counts and block counts.**


* **Clean up the formatting on `/mines block list` so it's easier to read and looks better.**


* **If fail on /mines reset, then needed a missing return so the mine reset success message won't follow the error message.**


* **Bug Fix: When mine reset time is disabled, set to -1, and then all mines are reset with '/mines reset `*all*` details' it would terminate the reset chain on that mine.**
This change allows the next mine to be reset by not trying to set this mine's next action... which is none because reset time is -1.


* **v3.3.0-beta.2 2022-02-03**


* **Added an error message when failed to add a prestige multiplier.**


* **New feature: cached adding of earnings for the default currency.**
This was causing a significant amount of lag/slow down when performing autosell, or spamming of sellall.  The lag was in the economy plugin not being able to accept additions of money fast enough.  
Now this simple cache, will wait 3 seconds before adding the player's earnings to the economy plugin.  When it does, it will do so in an async thread so as to not impact any performance in bukkit's main thread.  Also prison's getBalance() functions, which includes the use of all prison placeholders, will include the cached amount, which means the player's balances appear as if they are not being cached.
Still need to cache the custom currencies.


* **Update /ranks autoConfigure to set notifications to a radius of 25 blocks, and enabled skip resets at a limit of 90% with 24 skips.**
Also moved DARK_PRISMARINE down a few levels since it's not as valuable as the other blocks.


* **Bug fix: Correct the comparison of a prison ItemStack by using compareTo.**
The old code was using enums, so the check for equality of enums resulted in comparing pointers, which will never work.
Updated a few other parts of the code to use the compareTo function instead of the equals function since that may not work correctly all the time.


* **For command /mines set notification added *all* for mine name so all mines can be changed at the same time.**


* **Change notification alerts from runnign every 5 minutes to every hour.**
Got a few complaints within the last fewa days that the notifications are too frequent.


* **Modified SpigotPlayer to add getRankPlayer() and modified RankPlayer to add getRankLadder, with short cuts for default and prestige so you don't have to always refer to their names (reduce errors).**
This is to remove the "mess" from other functions that need to get these player objects, of which sometimes they are not going about it the correct way.


* **sellall multiplier add - Now reports if a multiplier cannot be added.  Also now adds the multiplier based upon the actual rank name**, 
of which it was what the user entered with the command, which may not match the actual rank name.


* **RankLadders - Added a boolean function to check if the ladder is the default ladder or prestiges ladder.**


* **sellall multiplier - Now able to list all multipliers.**
It lists them in a 5 column listing.


* **Add debug logging when calling the external events.**
Will have to revisit this when hooked up to multi-block events, otherwise it could overwhelm the logging.


* **Ladder rank cost multiplier has 100 percent limits removed.**
Value can be any positive or negative number now.


* **Update some documentation related to CMI Economy.**


* **Broadcast the prison welcome message to all online players when prison is loaded with no mines or ranks defined.**
The messag is loggd to console 8 sconds after prison loads.  The broadcast messags are sent 16 seconds after logging the welcome message.
The intention is to help bring awareness to new mods/admins that there is an easy way to get started with prison.


* **Broadcast the failed ranks loading to all online players.**
Its important that they know ranks failed to load.


* **Release v3.3.0-beta.1 !! Hooray!!** 2022-01-29 2:11 PM EST


* **Added nano-second timing autosell to confirm if there is a performance issue.**
My initial testings are showing that sellall has significant chance of performance problems in that selling items takes way too long.  Will address in the future.


* **Disable all ranks related commands within the GUI menus.**
GUI was bypassing safeguards that were in place when the ranks module failed to load.


* **Update the placeholderAPI docs to correct the formatting of the docs to match what they should be.**
Had to indent by two spaces.


* **Created updated documents for the placeholderAPI wiki.**
These are local copies of the content since the prior content was removed/vandelized.



* **New Feature:  Added support for Quests so that block breakage within mines can now be tracked and be applied towards quests.**


* **Bug fix: Lapis_ore appently does not drop lapis_laluzi when using the bukkit's getDrops() function, it instead drops blue_dye, then when it gets to the player's inventory, it is then converted to lapis_lazuli.**
Therefore, auto sell cannot sell lapis_ore drops unles blue_dye is within the shop.  I added blue_dye with the same value of lapis_lazuli to the sellall shop.  This allows it to be sold now through auto pickup and auto sell.



* **Bug Fix: Damage was being applied all the time.**
Found a field being initialized with a value of 1 when it should have been 0.


* **Prevent sellall from loading if ranks does not load.  Sellall uses too many rank functions to stand alone.**


* **Bug Fix: The new Ranks error message handler which intercepts all ranks messags was failing to load properly when prison startup was not set for a delayed startup,**
 which was because the ranks gui command (/ranks) was always being set even when ranks module failed to load.  Now /ranks gui loads only if ranks was successful in being started.


* **Initially setup to use the actionBar for the messages, but that is not working correctly with such high volume of messages.**
So disabled them for now, but will switch them over shortly...


* **Format the earnings amount properly, so it will have a consistant format.**
Once in a while, instead of showing a value like 165.00 it shows 165.000000000000001.  This is caused by the fact that doubles are binary, not base-10 so it canot always show the correct values.


* **Deprecated the MessagesConfig class since it is not implemented correctly.**
The messages should have been handled through Prison's multi-language tool, of which this does not use.


* **Try to use a different way to identify the item stack, especially if the bukkit item stack does not exist.**
This was a random error when using gravel, sand, and dirt on spigot/paper 1.12.2.


* **Clean up some of the refrences to the new/old block models.**


* **Added the new command: '/sellall list' that will list all blocks and their prices.**


* **Added comments that usage of auto features cancel drops will not work from spigot v1.8 through 1.12.x.**
Should work with v1.13.x and newer.


* **Fix some block issues, mostly getting the correct block bukkit block and limit it to only one location and function that ultimately provides these hooks.**
This release appears to be more functional, but it still should not be used since it's not fully tested.


* **First pass at removing the old block model.  Do not use this release!!** 
This compiles and runs on the server.  Most commands appear to work, including mine resets, but no visual confirmation has been performed in game yet.  Since so much has been changed and it has not yet been tested in-game, this release should not be used until such rudementary testing can be performed.





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
  


-------
------- Upgrading Gradle:
-------

  Use the following for a list of version for upgrading to:
  https://gradle.org/releases/

  * <code>gradlew wrapper --gradle-version=7.4</code> :: Sets the new wrapper version  
  * <code>gradlew --version</code> :: Will actually install the new version  
  * <code>gradlew build</code> :: Will build project with the new version to ensure all is good.  If build is good, then you can try to upgrade to the next version.



  
