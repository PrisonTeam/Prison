[Prison Documents - Table of Contents](prison_docs_000_toc.md)

# Prison Build Logs for v3.2.1 - 2020-09-27

## Build logs
 - **[v3.3.0-alpha - Current](changelog_v3.3.x.md)**
 - [v3.2.0 through v3.2.10](prison_changelogs.md)
 

 

Greetings!  I'm delighted that you are interested in the build logs for the
Prison plugin.  I'm wanting to provide a more formal documentation as to what 
is going on in each build so you have a better idea if it may be something 
that you need.




# V3.2.1 - 2020-09-27

* **New Release!! v3.2.1 published!!**
Bleeding was pulled in to master branch.  And published to spigotmc.org too. :)



## tag v3.2.1-alpha.20 - 2020-09-27



* **Fixed issue with sorting of the mine name in the gui.**
  They are now strictly by alphabetical on the mine name.



* **New Feature: Custom sorting of mines, ladders, and ranks.**
Currently mines and ranks are alphabetical in sort order.
Ladders sorts default first, prestige last, and everything else is alphabetical between them.
The PrisonSortableLaddersRanks will first sort ranks by ladders, then within each ladder, they will preserve the ladder rank order, then all ranks that have no associated ladders will be sorted alphabetically and added to the last of the list.
This new feature has not been hooked up yet to any interfaces, but it's ready.
Need to add mines, but right now there is no way to link mines to ranks.



* **Bug Fix: Prevent mines from being created that have spaces or no names**
A bug was found where the admin was able to create a mine with the name of two spaces.  Changed create mine function to disallow any spaces within the name, or an empty name. Also setup the parms to "require" a name, but not sure if that will work correctly. If it does, then there will be multiple layers of protection to prevent spaces.



* **New Feature: GUI Works with Spigot 1.8 now!**
Prison's GUI now works with spigot 1.8.


* **New Feature: The command /prison now includes all other root commands!**
When the command prison is used, it shows a list of all sub-commands.  Now it also includes all other Prison registered root commands so the uses do not have to guess what other commands are available within prison.  This will provide a listing so they can explore others.
Also disabled the prison troubleshoot command since its not of any benefit.


* **Moved the spiget project in to the prison project's class path**
Move the spiget in to the project's directory with shadowJar since there has been a few issues with the build.


* **New Feature: /mines tp Can Now teleport players within a rank command**
Allow only admins or console commands or rank commands to teleport someone by name.
This allows rank commands to use this to teleport a player when they rank up.



* **Alpha 20 - 2020-09-16**
Yeah.  I know. Alpha 20 and not beta! 
But many releases have happened under Alpha 19 including some bug fixes.  This is incrementing to help ensure when someone grabs the most recent, they know if they have the most recent.
Beta will follow shortly, but there are still a few more additions and adjustments.


* **Tweaks to auto manager**
Found duplication on durability processing.  Eliminated the duplication, but now it will only be applied when auto pickup is enabled.  Not 100% sure if that is the best logic, but figuring if prison does not handle the block break event, then do not apply the durability.
May have to revisit and tweak how smelt and blocking is working too.
Changed the ALL pickup to be the first processed since that is probably going to be the most common situation so bypass the many block checks to save some CPU time.


* **Attempts to force the plugin's config.yml file to be reloaded** 
It "should" work, but it does not. 
Keeping this code since I will expand upon it and get it working, even if I have to go outside the bukkit handling of that file.


* **Bug fix: Duplication of mine blocks. Only impacts more recent Alpha.19 releases.**
Duplicate blocks are now "fixed" when loading a mine.  If a duplication fix is applied, the fixed mine data will be saved and a message will be shown that an inconsistency was detected and fixed.
Overall this did not cause any crashes or corruptions of mines, but it did duplicate internal data for the blocks associated with a given mine.
Symptoms were loss of air blocks (if you had an air percentage) or if enough blocks were duplicated, the GUI would give you an error message indicating there were too many blocks and it could not display them all.
The issue was initially thought to be related to a lambda function, but that was rewritten and confirmed that it wasn't there.  The issue was ultimately tracked back to a typo on the new block handling.  So this would have only impacted the more recent alpha.19 releases (not all).


* **New Feature!! A Reset Threshold Percentage has been added.**
Before the reset threshold was set to zero blocks.  Now, if enabled, it will reset the mine at another predetermined percentage of blocks remaining.
A value of zero disables this feature since that is the same as a zero block reset.


* **Changed mines placeholder for time remaining formatted**
Dropped the decimals since it does not look good when updating every second or less.


* **New Features! PLAYERMINES placeholders!! Added 24 new placeholders including aliases!!**
These placeholders are a hybrid between player and mine placeholders.  These placeholders can only return results when a player is actually within a mine, and these are the mine's stats.  In other words, whichever a mine a player is in, it can show all of that mine's stats.
This will not work in holographic displays since they are not associated with any players.  It will work with chat prefixes, but these are stats you generally don't want as chat prefixes (maybe just the mine's name?).
But where these really can be awesome is used in the player's scoreboard.  So when they hop in a mine, they can pull up in their scoreboard stats on the mine itself.  


* **Bug fix: Placeholder progress bar configs in config.yml not working**
This addresses some issues with being able to load the placeholder progress bar configs from the config.yml file.  
Ran in to issues with symbols not being quoted. Strings normally do not have to be quoted, but yaml was interpreting them incorrectly and was trying to treat them as special values.  So quoting prevents that.
Also had unicode characters in the comments which also may have be causing issues indirectly, so removed them.


* **New Feature!  Placeholder progress bars!**
prison_rankup_cost_bar, prison_rankup_cost_bar_laddername, prison_mines_timeleft_bar_minename, prison_mines_remaining_bar_minename, plus their aliases.
This adds a configurable progress bar to the list of placeholders.  The /plugins/Prison/config.yml file can be reset to load the parameters to customize the progress bars.  For now, if the configs do not exist, they will fall back on to the default values.


* **Minor fix: Found that some placeholder key values were not being generate with all lower case characters.**
This could have resulted in problems if the users were trying to use all lower case.
Also provided a placeholder flags exclusion filter to remove the wrong compound types within mines.


* **Minor improvement to placeholder search when using Players**
Added player information when it's supplied with the placeholder search.  This provides important feedback to the user that the supplied player is valid.


* **Bug fix: For the PlayerManager was not allowing PlaceHolderFlags types of LADDERS to be processed.**
So ladder based placeholders were not being translated correctly (sometimes they were). This was fixed.


* **Eliminate the obsolete message and .meta checks.**
This version of prison cannot perform any updates on the old obsolete versions of data.  That functionality was removed prior to the v3.1.1 release.  To upgrade from v2.x they will first have to upgrade to v3.0.0 and then to the current release.



* **Move the player mine cache to the PrisonMines object**
This will allow it to be accessible from other areas within the prison plugin.  This will need to be used with more advanced placeholders that will be added shortly.


* **Enhancement: Optimize the generation of placeholders**
OPtimizing placeholders by eliminating the generated placeholder that lacks the prison_ prefix.  Instead, adding the prison_ prefix to all identifiers that are lacking it so it can get a hit on the real entry.  Fix the display of the stats... was calling wrong function.


* **New Features! Added 4 new placeholders - Cost Remaining**
These pertain to cost remaining for rankup.
prison_rankup_cost_remaining, prison_rcr, prison_rankup_cost_remaining_laddername, prison_rcr_laddername
Now you can count down the amount of money needed for players to rank up!  Remaining value is never negative, but it shows zero instead.


* **Few Feature!  Reload Placeholders**
Provided a reset function for placeholders.  The new command is registered twice to provide the most flexibility.
**/prison placeholders reload** and **/prison reload placeholders**


* **Realigning source for Spigot Placeholder code.**
Moved a ton of placeholder code to the tech.mcprison.prison.spigot package to get it out of the SpigotPlatform class.  
There was way too much business knowledge being exposed within SpigotPlatform and wanted to group it all under a placeholder's package.
The real meat of placeholders has to come together within spigot because core has no knowledge or access to the mines module, or the rank module.
This required creating a new interface and pulling the majority of the functions out of the Platform interface.


* **Change in SpigotListener to use the singleton that already exists**
Changed to use the singleton instead of the passed object. Cleaner.  


* **Bug Fix: Fixed a mine's block type loading issue.**
The blocks are now checked against a greater range of possible values to improve match rates when reloading mines.
If a block cannot be mapped, then an error message is now printed to the console so it can be addressed.
There was an issue with QUARTZ_PILLAR not being able to be mapped back to the correct block type.  


* **New Feature: Add the internal placeholder counts on startup.**
These counts are the total generated placeholders and some placeholders have more than one key.


* **New Feature: Added reset notification permissions**
Now only players with the mine's permission can be notified if the feature is enabled.
This setting is on a per mine basis, and the permission used is displayed in the /mines info listing.


* **Admins and console are able to view player's past names with /ranks player**
When admins and console use `/ranks player` command it will show past names for all players that have more than one name recorded.  


* **This implements the tracking of the player's name**
This now tracks and detects when player names are changed so it can record the change.
This is passive in that it does not try to actively detect name changes unless the player is active within the prison and using features that require the use of the player ranks and associated details. 
Offline players will never have their names updated if they are changed and they never return to prison.  It is possible that a player could change their names a few times and if they do not log in to prison, then it will not be able to track all the interim name changes, only the current name they just logged in with.


* **Setup the internals for tracking the blocks mined for each player**
Established the core internals on tracking blocks mined per player.  Nothing uses this yet.


* **Add support for recording player names within the RankPlayer.**
This will help admins manage their data especially if they have to work with the raw data.
Multiple names can be tracked, with the last name in the list being the current name. Players can change their name numerous times and always go back to the same name multiple times and this will record them.
This only establishes support for names, but this commit does not get the actual names.  This update is able to save and read the data from the saved file.


* **Enhanced the /ranks info details**
Changed around the /ranks info to show the actual rank name and tag so it's obvious.  Also moved the player count outside of the admin block so all players can see it.


* **Added GUI Compatibility interfaces and support classes**
Restructure the compatibility classes to separate the blocks from non-blocks and added in GUI support. 
It maybe a little odd how the interfaces extend from other interfaces, but the chaining allows separation of functionalities to keep the code cleaner at the interface and implementation layers.


* **Alpha.19 - 2020-09-08**
Final alpha version prior to beta!  Yeah I know I said that before, but I did not expect so much awesome stuff to be added!  Need to have a clean transition to beta, so this has to happen first. 


* **New Features: Show all ranks and player counts upon startup and on demand**
For each ladder, all ranks with the number of players at each rank is now displayed at startup of the plugin.
Also a new command was created to be able to display this information at any time. The command has the option to change the parameters to fine tune the results.  The command is /ranks players.
When the rank of a player is viewed, it is now displayed to the log. An admin reported that all ranks were changed on their server, but no transactions appeared in the logs. It was shown in the logs when players checked their ranks, but it is unknown what thoes ranks were. SO this tries to record that.


* **New Feature: Language files are written to the prison's plugin directory by defaul**
If the language files do not exist at startup, then they will be written to the proper lang directories. This helps admins find the files and edit them without having to try to pull them from the jar file.
The core module, which is not a true Prison module is now correctly being remapped to the /plugins/Prison/module_conf/core/ directory for all language file uses.  This keeps things consistent and clean.
The normal use case is that the jar file language files will be loaded first, then the external files will be loaded and replace any key value pairs that may have been loaded previously.
Warning... the language files ignore the package names and goes strictly by the key value in the files. Therefore if the same key exists in more than one language file, then the last one loaded will replace any other previously loaded values.
This has been tested and it works great.  No new mapping have been added.


* **Added a unique permission for /rankupmax so it can be disabled.**
Its `ranks.rankupmax.[ladderName]` but the player must also have `ranks.user`.


* **Added more details to Ladder save files**
Added Rank names to the ladder's save file. This will help figure out what is what and could help eliminate possible errors.  If a ladder is loaded and a name is added, then it will flag the ladder file to be resaved after being fully loaded to ensure the contents of the file is up to date.
No one should ever make manual changes to these files, but this at least give more information due to the use of the magic numbers with the rankIds.
Added a link to the actual Rank object to simplify the use of the objects.


* **New Feature: Broadcast Rankups and Demotions**
There is a now a new feature that will broadcast to the whole server when someone gets a Rankup Or a Demotion.
This can be disabled within the config.yml file.  If the entry is not there, it is the same as if it has a true value.  Any other value is considered false.


* **Alpha.18 - 2020-09-01**
Final alpha version prior to beta!


* **New Feature: Mine Reset Paging**
Finished hooking up the paged mine reset code.  And it works too!  Tweaked some of the timing settings such as milliseconds and block thresholds which will better tune this to be more responsive to paging.
Had to add comparable to the MineTargetBlockKey for the map to work correctly.
To enable use `/mines set resetpaging help`.


* **Bug fix: Correctly using magic values with XMaterials** 
This fixes the usage of the magic value with bukkit/spigot versions prior to 1.13.  The issue was that the only way to set the data value was through the BlockState and I did not force the update.  Now the block type and rawData are being set only within the BlockState and then the update is forced.  This resolves the issue and should work for all block types that rely on using the magic values.
The way the mapping to the XMaterial was changed to try use the name matching first since they have a high correlation.  Also the the legacy matching is the last resort now.


* **Added v1.16.1 now officially supported and added to the test environments**
Working fine. No issues found.  Now have a working v1.16.1 environment to test from to ensure issues are addressed.


* **Reporting for supported block types**
Added a raw spigot/bukkit test to round out the test to see how many blocks are available to prison.
Running this test on any version should give a fairly accurate idea of how many more blocks are gained with the newer versions of spigot.
Pre 1.13 versions of spigot cannot be fully tested to identify all possible block types due to lack of ensuring individual Material types with different data values within the ItemStack cannot be identified as being invalid since that is left for the client software to decide.
This test adds no new functionality, but provides a reporting on how the newer infrastructure will work.


* **More work on new block type support**
Got a lot of the questionable code working better so 1.8.x and 1.16.x will not have problems with class or method not found exceptions.  Pushed a lot of version specifics to the compatibility classes.
This does not add new blocks to prison, but lays the foundation and support for new block types.


* **Major New Feature: Work In Progress!  New handling of Blocks and Materials!!**
Starting major changes to how the block types and materials are processed.  
This is a Work In Progress! Use with caution until stabilized.
Upgrade the spigot API to 1.13.2!!!
This should make Prison more 1.13+ compatible and support more block types, and transition to supporting all block types that exist for a given version.
Add in the cryptomorin's XSeries to better deal with newer block types.  Trying to get rid of deprecated function usage, except for within spigot 1.8 specific code sets.
Finally got lapis lazuli working!  So this is heading in the right direction. 


* **Alpha.17 - 2020-07-28**


* **New Feature: Sellall has been added**


* **Worked on enhancements for the auto features.**
Enhancements and hooking up fortune, silk (does nothing... yet), durability, and additonal drops.
Enhancements to improve stability and quality
Eliminate the switch statements when dealing with Materials since there are a few that cannot be hard coded and instead must use the Material.matchMaterial() function since the type may not exist in all versions.  It's also preparing to transition to another way of dealing with materials, but cannot use them until after prison is migrated to 1.13.x.


* **Fixing some block types so they are not hard coded.**
Initial setup.  This will help transition to 1.13.x which will better position to supporting 1.15.x and 1.16.x block types.
Not all issues have been addressed yet, like the GUI.  Want to confirm this is working before expanding.


* **Prevent the default ladders from being deleted**
The default ladder was actually deleted by a user. Could prevent a ton of issues by preventing these from being removed in error.


* **Enhanced the handling of unloaded modules.**  
They were not behaving correctly and causing issues.  Also better log the warnings that the module was not loaded.


* **Add documentation for Mine Commands**
Added documents for mine commands.  Document how to setup a Parent Mine with Child Mines.


* **New Enhancement: Mine Commands Enabled and Enhanced!**
Mine commands have been enabled.  They have been enhanced by adding the ability to have some commands ran before the reset, or after the reset.  This gives a great control over customization and integrating external commands to operate most efficiently with actual block resets.  One example is to have a real forest of trees reset and completely rebuilt each time.  The other example of usage is to setup up a parent mine to control the resetting of all child mines.


* **Alpha.16 - 2020-07-04**


* **More Enhancements to AutoManager**
Some refactoring and some enhancements to AutoManager.
Added isCalculateDurabilityEnabled, isCalculateFortuneEnabled, isCalculateSilkEnabled, and lore durability resistance.
Feature enhancement: Permissions added to config file so they can be customized as needed.  Added these custom permissions to the /prison automanager command so they always reflect the correct permissions.  If changed online, everything reflects the changes right away.  The AutoFeatures now are able to record their parents so it makes possible the ability to get all permissions related to a given section.  For example all permissions, or all autoPickup permissions.


* **Enhance Auto Manager Features**
Still a work in progress, but becoming more mature.  
**Added new feature**: Durability able to be turned on/off.  
**Added new feature**: Durability resistance is now able to be used as a lore. With no integer value it will have 100% effect and disable durability calculations on the item that has the lore.  If less than 100, then it's a percent chance durability will be skipped.
**To do:** Hook up a new block drop calculation and use the new fortune and silk functions.
Removed some hard coded Material types to make it more compatible with newer releases of spigot.
Reworked how lapis lazuli auto block is processed to work with v1.13.x and higher.
 

* **Upgrade the Spigot API to v1.12.2 from v1.9.4**
Prison was built on spigot api v1.9.4 for the longest time.  Looking in to trying better support newer block types, had to upgrade the version to something newer.  Tried v1.13.2 but it failed to compile.  V1.12.2 works well.  


* **Fixes to AutoManager and new file IO**
Got the new file IO working better since there were a few issues with getting the actual values from the spigot yaml reader.  Also made changes to the ValueNode objects to make them easier to use and to be consistent between them all.


* **Bug Fix. NEATHER_QUARTZ is not a block**
But its type was set to a MaterialType of BLOCK. It is actually the result of breaking NEATHER_QUARTZ_ORE, of which, that is the proper block type to use.  Use of neather_quartz would result in many exceptions in the console, one for each attempt.


* **New feature: Moved AutoManager to prison core and out of spigot module.**
This will allow access to these settings in all other modules instead of just the spigot module.
Rewrote the interface on how the the properties are saved and loaded to elimiante the dependancies upon spigot.


* **New Feature: Ladder specific placeholders for player ranks**
Add in placeholders for ladders.  These are rank placeholders, but targeted for specific ladders.
Enhanced the `/prison placeholders search` to work with player based placeholders, and added support to specify the actual player too so it can work from the console. The placeholder search should now work with all placeholders and will be able to provide values for players if ran in game, or if the player's name is provided within the console.


* **Ranks names cannot contain formating**
Made it so new rank names cannot contain & formatting.  It causes issues and that's what tags are for.
This change mitigates any preexisting ranks that may have been created with formatting, the /ranks list now escapes the & characters by replacing them with -.  Then the /ranks info command is now  able to use those escaped names.  The results of this change is that new ranks cannot contain & formatting, but preexisting ranks should continue to work and could eliminate issues.
There was an issue with /ranks list and formatted names causing issues with on click events.


* **New Feature: Prestige!  Significant work on Prestige added!**
Gabryca provided a lot of enhancements and features to the new Prestige system that he has been working on.  It was merged with bleeding from it's own branch.  It was pulled in because it has reached a point where it can start being used and undergo testing.


* **New features: AutoManager perms and lore enabled.**
Players can now use auto pickup, auto smelt, and auto block can be used if they either have the perms (all or nothing) or if the tools have the lore (0 to 100.00 percent chance).
The tool lore supports full enablement (no number following the lore) or a percent chance than can range from 0.001 through 100.000.  Allows for tools to gain better chance.
The config setting Options.General.AreEnabledFeatures enables all mines and overrides player perms and lore. Set to false to allow perms and lore to work.
May need more testing, but looks functional so far.
Added new command /prison automanager to just display info on automanager and the perms that will enable it.  The command only shows the help.


* **PlaceholderAPI Troubleshooting and Tips**
Update PlaceholderAPI install docs to reflect a possible way to get it to work with other plugins when the other third-party placeholders are not working.
At this time, it does not appear like this applies to Prison, but it coud be a tip that could be a solution for an edge case issue that someone may experince in the future.


* **Performance improvement in auto manager.**
This change to the auto manager has a significant performance improvement per block break by not having to reload all of the parameters each and every time a block breaks (it no longer loads from the file on each block break event which really reduced the overhead too).
This also sets up the auto manager for the next phase of enhancements.


* **New feature: The /mines tp now can be used by players**
If a player has the new permission: mines.tp.[mineName] they can use this command to tp to the mine.
The mine name must be all lower case.


* **New Feature: altPermissions on commands!**
Now if there is a permission that is internally checked within a command, it can be reported with the altPermissions so it can displayed if the user does the help keyword.
Examples: /rankup, /mines tp, and /ranks

AltPermissions are alternative permissions that are not checked internally, or automatically.
It is up to the programmer to put hooks in to the code to check on these altPermissions.
This field of altPermissions is strictly for displaying helpful information to the end users
and it is only helpful if it is included.
 
For example the command /rankup has an optional parameter ladderName.  If a ladderName is 
provided, then it checks to see if the player has the permission: ranks.rankup.[ladderName].
 
Because these permissions are not ever used to check for actual permissions, it is very
important to provide parameters such as [ladderName] to signify where the server owner, or
admin, must place the real ladder name within the permission.
 
This is such a critically helpful feature because otherwise the only way you would know 
that you need this permission is to look at the source code, of which many cannot do, and 
those who can, may not know where to look.  So this provides very important information that
was not available before.
     

* **New Feature: Command help keyword now includes permissions**
New Feature: Now includes the permissions in the help commands so users do not have to guess or hunt down what permissions are needed for these commands.


* **Fixes ladder commands: delrank and addrank**
Fixes the problem with removal and readding ranks from a ladder.
You can use /ranks ladder delrank <ladderName> <rankName> to disconnect a rank from the ladder.  Then use /ranks ladder addrank <ladderName> <rankName> <position> to put it back in to the ladder in a new position.

These changes makes sure that the rankPrior and rankNext are set to null to remove dead or changed connections.  It also sorts each ladder before hooking up the those links.


* **New Feature: Placeholder tools.**
You can now test all placeholders, including multiple placeholders within one test.
Also you can fully search for any placeholder, either player based or mine based placeholder.  If you run the commands from console, you cannot use the player placeholders.
The commands:  /prison placeholders test  and  /prison placeholders list.


* **Start the ground work for supporting another integration**
Initial setup for WorldGuard. Ran in to issues with lack of v6.x even existing in maven, which is needed for minecraft 1.8 through 1.12.2.  WorldGuard v7.x is for minecraft versions 1.13.0 through 1.15.2.
Also both WG versions appear to use the same package and class names too, so how to include both in the build without conflict? Yeah, not sure.  Will have to figure that out too. 
Anyway, starting to provide the basic integrations hooks, which are disabled right now, but will be enabled as the details are worked out, and as Prison is adjusted to work better with it.


* **Failed Attempt to Enable 1.13 Flattened Materials**
Failed attempt to give 1.15.x the ability to have native block types. This code has been commented out with hopes that it could be used in the future.
The reason why this won't work, is not that the codes entered are incorrect, but it appears like the Material object that is actually being used when running on a Spigot 1.15.2 server is actually using code from Spigot 1.9.2 since none of the 1.13 materials are recognized at runtime.  
So the "wrong" classes could be included in the build artifact, which in turns get's used during runtime.  But should it? 
So for now, its all disabled so it can be revisted later.


* **New Feature: Mine commands.**
Added commands to mines. This may be complete, but not fully tested, so disabling so it cannot be used.  Will enable when it is proven to work well.
The concept with mine commands, is that after every reset, it will run a set of commands associated with the mine, of which could permit new and complex types of mines.


* **New Feature: IGNORE BlockType added.**
Created a new block type that will allow ignoring what is already there. 
Ignore can be used instead of air, and as such it will not replace whatever is there. 
It is possible that all blocks are 100% ignore so that way nothing in the mine changes during a reset.


* **New feature! Two new placeholders: formatted time.**
The placeholders for reset interval and time left now have formatted versions and aliases.
The format is 13d 21h 13m 1.23s.  1h 0m 13.13s.
It does not show leading digits if they are zero, such as days will be suppressed if zero. And days, hours, and minutes will all be suppressed if they are all zero. But if hour is non


* **Bug fix: Failure in logging upon plugin startup**
There was a failure on getting and calculating a paper 1.10.0 version due to a period to the left of the primary version. As such, the old code could not deal with it and was throwing a failure. To add to that failure, since it was during start up of the plugin, the resources were not yet setup for to allow the logging to work so that was causing another failure. 
This fix allows the output of the log message to System.err so it will be at least recorded in the log files.


* **New Feature: Now Supports delayed world loading! Multiverse-core now properly supported!**
New Feature: Major modifications to support delayed world loading. This supports Multiverse-core, and other plugins, that may load worlds.  
You cannot use softdepends or hard depends to ensure the world is loaded, but instead must use WorldLoadEvent events.
These changes enables the mines to load even when their world has not been loaded yet. It also disables any function or changes that could corrupt the mine's data by not having the world enabled and loaded.


* **Bug fix: Reset Radius Notification not working**
The message notification functions were not using the mine's radius value, so all radius notifications were based upon a distance of 150 blocks.
Extend within notifications to include players standing on top of the mines too.


* **New Feature: Start to properly deal with delayed world loading**
Plugins such as Multiverse-core may load most of their worlds after Prison has tried to load the mines.  During the creation of the mines, the worlds must exist, otherwise the mines cannot be loaded under the older way prison was doing things.  

This change allows mines to be loaded, but will delay their hook up to the
world until the worlds are formally done loading.

This is a work in progress and is not complete yet.


* **New Feature: New command /rankupMax will rank up the player until they are unable to afford the next rank.**


* **New feature: Provided a way to disable the resetting of the mine when reaching zero blocks.**


* **New feature: Can now disable the reset when a mine reaches zero blocks"
Provided a way to disable the resetting of the mine when reaching zero blocks.


* **Updates to AutoManager: Full Inventory**
Improve the full inventory handling.  Basically always try to work with inventory items (auto pickup, auto smelt, and auto block) and if they cannot fit back in to inventory then just drop them.  That allows them to fill up inventory slots that may not be full.
Have the dropExtra function use either the specified holographs or the action bar to show the inventory is full message.


* **Few fixes to mine commands, rank loading**
Fixed an issue with Mines commands.  The one was a typo (had to be a stray space) and the other was using block search before using any other mine command (missed testing that before).  Tweak formatting on mines info to make it easier to read.

  Fixed an edge case situation where the next rank was not available.  Not really sure what caused it to be missing, but this prevents issues from happening within prison.


* **Improve how bypass resets works.**
Fine tune the bypass resets so it works better.  Found some issues & fixed them.


* **Added 3d distances to some mine related commands**
Added the distance the player is from the mine to the /mines info command. 
Created a 3d distance function to use for displaying distances to the mine. 
For mine related messaging, its using 2d distances, ignoring the Y axis.


* **New Feature; Slime Block Fun!**
Simple feature to add some bounce to the Prison fun!  
Different items in hand increases boost.  After jumping on a slime block, the player gains immunity from fall damage, but they can still die if they land in lava, the void, etc... 
Not really sure what the life span of this will be? 
To enable, set the configuration property "slim-fun" to true in plugins/Prison/config.yml.


* **New Features: Command /mines list now has paging! Plus useful sorting.**
Major changes to the /mines list command by adding paging and better to read formatting. 
Now you can specify a page and be able to read what's in the list. In game you can click to page to other pages.
This listing is sorted by Blocks Mined (since last server restart) then by Mine Name.  This help to put the more active mines on page 1 and the inactive ones at the bottom of the list
There is an option for pages of ALL so you can get a full dump.  If you want it.


* **Minor refactoring: Remaining Time before the Mine Resets**
Moving the calculations for the remainingTime in to the core mine classes so it can be accessed by other functions.  This also moves the business logic out of the MineManager and puts it in the mine, where it should be.


* **Typo fix: Noticed a field was named "Rest" instead of "Reset".**
The fixing of the name does not alter the behavior of the code, but the wrong name could confuse someone.


* **New Feature:  New Mine Placeholder! Mines Reset Count.**
Added a new mines placeholder: Mines Reset Count.  Identifies how many times the mine was reset since the last server restart.



* **New Feature: Zero Block Mine Reset Delay**
Added a new parameter to mines for a mine reset delay when the mine reaches zero blocks.


* **New Feature: Setup a packaged paging system for the commands**
New feature: Created a new class to encapsulate multiple pages for commands.  Based upon /mines block search and hooked it up to /mines info.
Provisions for pre-pages that are shown before the listings, such as the first page of /mines info. Will expand to other commands later.


* **Bug Fix: If BlockType does not exist use STONE instead**
Bug fix: Found a problem that if you put a Block Type in a mine that does not exist on the server it fails to reset the mine and there really are no errors shown in game.  
Default to STONE if the material does not exist.


* **New Feature: New Placeholder for mines. Blocks Mined!**
Added new placeholder blocks mined.  This reports on the total blocks that have been mined within a mine since the server restart.


* **Improved Performance: AutoManager by extending from OnBlockBreakEventListener**
Extend the AutoManager so the same BlockBreakEvent logic for cacheing the player's last mine used to optimize performance.
By extending the class, and creating the doAction() function allows for simple reuse of the event listener.  
It is also critical that the AutoManager is able to set a LOW event priority.


* **Improved performance: for the onBlockBreak event listener**
This is the code that monitors the number of blocks remaining in a mine.  This logic will also be applied to the auto features since it needs to perform the same basic initial setup and checking to ensure the blocks are within a mine.

Significant improvements to the OnBlockBreakEventListener to try to minimize overhead and to improve individual performance for all players.
The biggest hit, performance wise, will be when mining outside of the prison mines since it would have to check to ensure it's not within a mine, and will go through the whole list.  It's just simple math, but performed for each mine that exists so it will add up.
Overall, the overhead is not much, but efforts were made to reduce it as best as possible, of which, I think is the best that can be done with the current environment.
Also renamed addBlockBreakCount function to incrementBlockBreakCount since that's what it is doing.


* **New Feature: Hooked up auto mine reset when blocks remaining reaches zero**
When a mine reaches zero blocks left, then it will auto reset.
This is hooked in to the onBlockBreak monitor and all it does is cancels the current mine's reset and then resubmits it to reset.
This could allow for the creation of a one block grinder.  Although there may need more work, such as delays to slow down the reset since it is able to reset at blistering speeds.
 

* **New Feature: Enabled the blockBreakCount feature**
Using the new blockBreakCount feature on the placeholders to eliminate the use of the old refreshAirCount() function. 
This should provide a lot of performance improvements and will allow for live updates without much overhead on the server.


* **Internal Mine Optimizations**
Refactored a lot of stuff with mines to eliminate wasteful operations.
Setup a collection organized by name for quicker access when there are larger number of mines within a prison.
Changed remove block to remove ONLY the first block so as to help admins remove duplicates instead of having to go back and readd them. Fixed issue with missing not on if blocktype is in mine.
Cleaned up the save mine function in relationship to the initial loading of mines to simplify the code and to eliminate possible problems.


* **New Feature: Starting to track actual block breaks in mines**
Hooked up an even monitor for BlockBreakEvents to track them within each mine.
This is a work in progress to eliminate the counting of air blocks, of which could be a strain on the server if something like holographic displays is hooked up to each mine, and runs a few times per second.
Upon server startup, had to count air blocks, but changed it to sync since on server load it was throwing too many exceptions due to entities within those chunks.
Hooked up a temp placeholder to test with, which will be removed shortly once moved in to production.


* **Async issue: Mine air counts**
Async failure: Well, found out the hard way there is even less that can be done with async threads. Even just checking the air counts that does not update the world can cause problems and could throw an exception.
The issue is that if a chunk has to be loaded and if there are any entities, then it will throw an exception and could possibly corrupt something.
For now i'm catching the exception and logging it.  Will eliminate the async air counting in the next few days when I hook up live block counts of mined blocks.


* **Clear coordinates after usage**
Made changes to ensure that the coordinates are cleared after they are used.  Also same for the last used mine name when that mine is deleted.


* **Enhancement: Better logging if major failure with rankup**
Enhanced the logging on rankup to better deal with logging even when there is major internal failures.  Emphasis was to ensure transaction logging happens and is recorded no matter what.  Added more details and fixed a few that were not working correctly.
Fixed a failure that was preventing rankup from happening. Had the wrong conditional testing that was preventing a rank from being assigned.


* **Enhancement: Placeholders optimized!**
The placeholders without the prison prefix have been eliminated since the prefix is now prefixed when it is missing prior to matching to a valid placeholder enum, and when generating PlaceholderKeys.  This cuts the number of generated placeholders in half. This is significant since with the addition of the aliases there would be about 744 placeholders generated if the prison had 30 mines setup!  Now a 30 mine prison would have about 372.


* **New Feature: Placeholder aliases!**
Added shorter versions of all placeholder commands.  The aliases still must be prefixed with prison_ and if it's a mine related placeholder, then it must end with _minename.
Aliases are also displayed on the /prison version page in parenthesis following it's original  placeholder name.


* **New Feature: Added a new placeholder! prison_rankup_cost_percent**
This indicates how close a player is to ranking up based upon their balance and expressed in percent closeness. 
If their balance is zero or less, it will show zero percent.  If they have more than enough money then it will show 100%.


* **New Feature: Added an auto rankup for all new players to the server!**
Added a rankup to the default rank on the default ladder for when a new player joins the server.
I REALLY thought this already existed!! oof!!
Modifed the rankupPlayerInternal function to supply the default ladder and default rank if needed.  Also added more logging transactions to cover the new events. 


* **Enhancement: AutoManager's BlockBreakEvent had to be set to a lower event priority**
Had to set to a EventPriorty.LOW so other plugins can work with the blocks. The other plugins were EZBlock & SellAll. This function was canceling the event after it auto picked it up, so the other plugins were not registering the blocks as being broken.  The default EventPriority was originally set to NORMAL and that was not working with the other plugins.  So setting it to LOW allowed them to inspect the block that was being broke, and do what they had to do, before prison's AutoManager performed the auto pickup events with it.


* **New Feature: Auto features added: Auto Pickup, Auto Smelt, and Auto Block!**
Gabryca added auto features to Prison.  They include auto pickup, auto smelt, and auto block. 


* **New Feature: Added detailed reporting on rankup, promote, demote, and set rank**
Added detailed logging on rankups to track all details.  It may appear to be overly detailed, but if something goes wrong, it will allow tracking down exactly what may have went wrong.


* **New Feature: Added support for GemsEconomy and Custom Currency**
Added a direct integration for GemsEconomy so Prison can now use a custom currency with Rankups.  
Added within prison the support for custom currencies.  There is a special mechanism in place for dealing with custom currencies, since it requires special interfaces to specify the currency.  Prison checks to make sure there is not only a registered economy that supports custom currencies, but it also checks to make sure the currency that is specified within the ranks is supported by an economy before allowing the player to add it.  It also checks to make sure all custom currencies have supported economies upon Prison startup and reports errors if they are not found.  When a player attempts a rankup, everything is once again checked to make sure the economies support the custom currency.  Prison provides for admins to bypass the use of defined custom currencies through **/ranks promote**, **/ranks demote**, and **/ranks set rank**.


* **New Feature: List currencies that are used in Ranks**
On startup, gather all currencies that are defined within all of the Ranks, confirm there is a economy that supports it, and then print the list with the ranks module.  If a currency is not found, then print an error message on the Prison start screen. The currencies will then be listed with each of the supporting economies when doing the /prison version command.


* **New Feature: Added temporal references to to the next lower and higher ranks**
To provide a much more simplified way of getting the next higher and lower ranks 
when you already have a rank, I added temporal references internally to the ranks.
This means there is a zero cost associated when trying to get the next higher or lower
rank and its as simple as just another field within ranks. 
These references, because they are temporal, are realigned when the ranks and ladders
are initially loaded, and whenever ladders change. 


* **New Feature: Added base commands to /prison version listing**
Added the base commands to the /prison version, which is tied to the modules. 
If a module is successfully loaded, then it will show the base commands that 
it supports.


* **New Feature: GemsEconomy Direct integration**
Starting to hook up GemsEconomy.  It's not ready and it may take a while to
put everything in place. The major thing that GemsEconomy provides is support
for non-standard currencies.


* **New Feature: AutoManager**
Gabryca is adding this new feature to the spigot interface.


* **Upgraded API: EssentialsX v2.1.7.1.0**
Update to the most recent version of EssentialsX v2.1.7.1.0 was v2.0.1-b354.
This is an internal library used only for compile purposes.


* **Upgraded API: SaneEconomy v0.15.0**
Update to a more recent version of SaneEconomy v0.15.0 was v0.13.1.
Note this is the last version where the signature of getBalance and setBalance is using doubles. Newer versions use BigDecimal so I've put try catches around them to capture potential errors so it can be reported.  Did not upgrade to anything newer since the backend storage has changed which may be a major change for some servers.
This is an internal library used only for compile purposes.


* **New Feature: List all registered plugins**
To better support server owners when they have issues with Prison, the command 
**/prison version** now lists all registered plugins along with their versions.
With this feature, to provide the information we need to help trouble shoot, 
they only need to copy and paste to provide all the information we need.
"Please copy and paste the full **/prison version** command listing."


* **New Feature: prison_rankup_rank_tag added**
This provides the tag of the next available rank.


* **Started to Add Documentation**
To help ensure all users have access to Prison's documentation, and to be able to version all documentation, I've decided to go with github markdown documents.  This will allow the documents to live within the project and they will be accessible online through github.  Simple markdown hyperlinks will tie them together so the user can browse them by just clicking links to navigate.  Markdown is very limited in what can be done with it, but accessibility and versioning is more important than bells and whistles.  


* **Pulled in the Prison GUI menus**
Pulled the prison gui branch in to the bleeding branch. This was the result of the awesome work by Gabryca.
I added the mapping of /prison gui to redirect to /prisonmanager gui so it's integrated
with the standard prison commands.
Added the new permissions to the plugin.yml file.


* **New Feature: Mine Placeholders**
A lot of code was rewritten to support the mine related placeholders. Player placeholders
are simple since there are just a few placeholder keys. Mines are far more complex since
you have the "basic" internal placeholder keys (names), but when exposed outside of 
prison, the mine names must be super imposed on each one. So if 6 mine placeholders
exist, and the server has 40 mines, then it would have to generate 240 placeholders. 
Then it has to map all of those placeholders back to the internal placeholder key, so 
it can identify which action to take.
Hooked the six new mine placeholders to their proper functions.
Created a mines chat handler.
Updated the player chat handler to use the new formats. Also updated MVdWPlaceholderAPI
and PlaceholderAPI to handle the new mines placeholders.


* **New Feature: Add in the target reset time for the mines**
This correctly sets the future targetResetTime when the next workflow job is submitted. This auto-adjusts the target time to compensate for delays in the system.
It also detects if there was a change in the reset time for the mine, and if so, then it will regenerate the jobWorkFlow to reflect those changes.


* **New Features: future targetResetTime, player counts within a mine, and count air blocks**
Start to hook up some mine related features such as future targetResetTime (the project time in the future when the mine will reset).  This allows the creation of a count down timer until the reset happens.
Add a function to count the number of players within a mine.
Added a set of function to count the number of air blocks in a mine asynchronously. Set it up as a submittable task.  The airblock count buffers and will run only every 30 seconds at most. If its a large mine, then it will delay slightly longer before refreshes to conserve computational resources.


* **Bug fix: Mine data was not fully loading prior to submitting workflow**
Found a timing issue where the mine's workflows were being submitted before all of
the mine related data was loaded from the file system.  Over all it did not cause
too many issues, but it was defaulting back on the default values for mine resets
and was ignoring what was set for that mine.  I implemented an inititialization 
"channel" in the mine stacks... basically all layers of the mine's hierarchy will
kick off their constructors from the bottom (MineData) to the top (Mine).  Once all
the layers are instantiated, then it kicks off executing all of the initialization
functions from the bottom (MineData) all the way back up to the top (Mine) again. 
This allows the full loading of the saved mine data at the top layer, Mine, and then 
allows a lower layer to utilize that loaded data, such as submitting the workflow.
This simplifies a lot of complexities pertaining to chicken-or-the-egg timings.


* **Compatibility Fix: Conflict with another chat plugin**
There was an onPlayerChat with the AsyncPlayerChatEvent issue  
with the plugin InteractiveChat that was resulting in intermittent issues where
placeholders and on hover events were not always firing or working correctly.
Issues were resolved when the prison plugin was removed from the server that was
having this problem.
Reviewed the prison code and everything looked good, but what got things to work
correctly was setting the Spigot onPlayerChat event priority to EventPriority.LOW.

* **New feature: /ranks set rank <player> <rank> <ladder>**.
You can now just set a rank on a given ladder, and not have to worry about multiple 
promotes or demotes.
**Caution:** Use carefully, each rank that is configured must be independent of all the other 
ranks. In other words, a given rank cannot expect the lower ranks to have set some 
specific permission, but each rank's commands must ensure it works correctly without 
hitting all the lower ranks.  Same goes for when this command is used for demotions 
and skipping many ranks in the process.

* **New Features: Offline player support.** Added off line player support on some of the
commands. This now allows working with the players even if they are offline, which was not
possible before.

* **New Features:  /ranks promote and /ranks demote.**  This is a way for an 
admin/owner to demote or promote players directly, without incurring a cost to 
the player.
This is good for testing purposes.  But has a major concern.  When setting up 
the rank commands, the commands to remove elevated permissions must also be 
included.  Otherwise if a player is demoted, their perms will not be removed.

* **Tag with v3.2.1-alpha.5 and add Core Documents shells** Started to 
put together the document structures within the project.  NO content yet,
but trying to hook everything up to the table of contents.  More to follow!

* **Removed Feature: Disabled the Sponge build**.  The sponge project
was getting to the point that all the key functions that prison needs, has
no code and were returning nulls.  Did not test the latest sponge build, 
but there is no way some vital aspects would work, such as mine resets. 
So commented the sponge references from gradle config files so it can be
easily reenabled if anyone wants to try to hook it back up in the future.

* **Added command placeholders to display**. When the user is entering the
command **/ranks command add** it will now display the command placeholders
as {player} and {player_uid} so the admins/owners won't have to keep 
asking what they are.  They actually need to be displayed elsewhere too: TBD. 

* **Bug Fix: Glass block was not being removed** when fill mode was 
enabled. Glass block will now be replaced as expected.

* **New Feature: Mine Reset Notification Control** On a per mine
basis, notifications can now be turned off, set to a radius area from
the center of the mine, to player who are properly within the mine.
Added a command to support this new behavior:  **/mines notifications**.
Each mine is independent, and there will be no global values.  
As new mines are created/added they will be set to the default
radius distance which is currently 150 blocks from the center of 
each mine. 

* **Bug Fix: Added PlaceholderAPI to the softdepends** Paper server 
environment identified that it was missing.  So I added it. Not sure
if that could have been cause issues with Multiverse loading issues? 
Doubtful, but this is the first "thing" that has been found related 
to hard depends or softdepends.  

* **New Feature: Command /mines resetTime** Added the command to allow
users to change the reset time for each mine.  The global reset time is
only applied to new mines that are added.  The new times apply once the
mines reset at the next mine reset. 

* **Work in progress: Mine resets** Committed the WIP just to get 
it in git. Currently the code works, for that of which is being used.
The more complex compoennts dealing with async and paging is not yet
enabled so will have no impact yet. Also needed to commit so I can 
add new features that overlap with this work (notifications). 

* **Removed dead links in README.md** A user made a reference to a few
dead links so I removed the ones that I could find. Also removed the
request for donations since that's currently a moot point.

* **Took a quick look at tab completing" but ran in to issues with how
prison uses multi-word commands The space appears to force everything that
follows to be treated as an attribute/parameter to the root command.
Special handling of these conditions will need to be addressed later.
Tabled this attempt with hopes to return to it soon.

* **Added new placeholder tag: prison_rank_tag**  The existing placeholder
**prison_rank** returns the name of the current rank for the player. 
The new placeholder  **prison_rank_tag** returns that rank's tag value, 
which could include formatting characters and would be suitable for
text chat prefixes. 

* **Bug Fix ?? : Cannot manually edit rank and ladder files.**
(to be addressed)
Manually editing the rank and ladder files, and maybe even the player
files, does not work, even when the server is shut down and restarted.
Could not reproduce, but a couple of users reported this as an issue and 
I think I saw it once before too.  Not really sure the cause but did not
see anything obvious in the code either.

* **Bug Fix: Players on server prior to setting up prison have no ranks**
(to be addressed)
When prison is setup initially, if a player is already on the server, they
will not be assigned a player rank. This causes failures when the player
tries to use the /rankup command in that it reports "Error ! You don't have enough
money to rank up! Then next rank costs <amount>!" This happens when the rank 
costs zero. 

* **Bug Fix: Found issue with the Vault Integration for Economy**
The vault economy integration was wrong. It was mixing use of player-centric and 
bank-centric processing. As such balances may not have been working consistently,
and many of the vault's supported economys probably were failing to work at all.
This fix allows it work correctly and will eliminate possible failures and
intermittent issues.t

* **Improve the reporting at startup and also for /prison version**
Added more details to provide the user with more information about
the prison environment and also the integrations.  It's a work in 
progress and some of the current formats will be changing and will be
refactored to be more useful and easier to read.

* **Bug fix in third party tool: Rewrite of the SpiGet version reporting tool**
Bug fix. Third party tool.  Spiget "claims" it deals with semVers when
that is nothing even close to being true.  I wrote a proper version
parser to use for Prison so semVer actually works correctly which will
hopefully eliminate some situations where it fails.  
I may fork the spiget project in the near future and share this
code with that project so others can benefit from proper semVer 
handling, which may address some of their open issues too.

* **Added new bStats parameter - Mines, ranks, and ladder counts**
Added a new custom parameter to bStats to record the number of mines, ranks, and 
ladders that has defined at startup. So this data may now be added as custom
charts, but may not be able to ever see it online since I do not own the 
bstats online account to configure it correctly on that end.  But at least the
data will be there if it's even enabled.

* **Added /ranks player command**
New command /ranks player show what rank a player currently has. The player must
be online. Future could add support for offline players, but quick attempt to 
hook that results in some internal failures within the Prison plugin so deferring
on that feature.

* **Change How Integrations Work**
All directly accessed integrations are now logged and recorded so their status can be
included with the /prison version command.  Integrations that are indirectly used, 
such as through other plugins like Vault, are never listed directly unless Vault 
references them.  

I went though the placeholder integrations and fixed their APIs to use the newer
set of place holders. Fixed some bugs and expanded features.  The list of integrations
now also includes the primary URL where they can find more information on the plugins,
and where to download them from.  Also provides some additional information, such as
available place holders that can be used.

* **Added Minecraft Version**
Added minecraft version to be stored within the plugin so it can be displayed 
in /prison version and also be used
in the future with selecting block types that are appropriate for the server version
that is being ran.

* **Bug fix: Block types not saving correctly if depending upon magic values**
Found an issue where loading blocks were by the BlockType name, but saving was by the 
minecraft id, which does not always match.  As a result, there was the chance a block
type would be lost, or it would revert back to the generic such as Birch Block 
reverting back to Oak Block.

* **Need to update gradle - Was at v4.10.3 - Upgraded to v5.6.4**
Currently this project is using Gradle v4.10.3 and it needs to be updated to v5.6.4 or 
even v6.0.1 which is the current latest release. It was decided to only take the project
to v5.6.4 for now, and wait for the next release on v6.x, which may gain more stability?
But to do that it must be incrementally updated to each minor version and you cannot just 
jump ahead or there will be failures.  At each step you need to evaluate your build scripts and
then make needed adjustments before moving onward.

  * **Versions Upgraded To:**: **v5.0**, **v5.1.1**, **v5.2.1**, v5.3.1, v5.4.1, v5.5.1, **v5.6.4**, 
  * **Versions to be Upgraded To**: v6.0, v6.0.1  
  * <code>gradlew wrapper --gradle-version 5.0</code> :: Sets the new wrapper version  
  * <code>gradlew --version</code> :: Will actually install the new version  
  * <code>gradlew build</code> :: Will build project with the new version to ensure all is good.  If build is good, then you can try to upgrade to the next version.
  * Update to v5.0 was successful. Had to remove <code>enableFeaturePreview('STABLE_PUBLISHING')</code>
   since it has been deprecated. It does nothing in v5.x, but will be removed in v6.0.
  * Update to v5.1.1, v5.2.1, v5.3.1, v5.4.1, v5.5.1, v5.6.4 was successful.


* **Minor clean up to Gradle scripts**
The "compile" directive is actually very old and was deprecated back around version 
v2.x. The replacements for that is "implementation" or "api" instead.  The use of 
api does not make sense since its use is to tag when internal functions are exposed 
to the outside world as if it will be used as an externally facing API.  That 
really does not fit our use case, but what api also does is to force compiling all 
source that is associated with something marked as api, including everyone's children.  So performance will suffer due to that usage, since it shuts down incremental 
building of resources.

I also found that use of compileOnly may not be used correctly, but at this point
I'm just leaving this as a mention and then revisit in the future if time
permits, or issues appear to be related.  Its a very old addition that provided
gradle with "some" maven like behaviors.  It was only intended to be strictly used 
for compile time dependencies, such as for annotations that are only needed for 
compile-time checks, of which the plugins and resources we have marked as 
compileOnly do not fit that use case. 
[discuss.gradle.org: compileOnly](https://discuss.gradle.org/t/is-it-recommended-to-use-compileonly-over-implementation-if-another-module-use-implementation-already/26699/2)


* **Redesigned mine block generation works - Async and paged enabled**
Redesigned how prison blocks are generated to improve performance and to allow the
asynch generation of blocks, but more importantly, allows for paging of actual 
block updates. The paging is a major feature change that will allow for minimal 
impact on server lag, even with stupid-large mines.  The idea is to chop up the
large number of blocks that need to be regenerated in to smaller chunks that can
be performed within one or two ticks, then defer the other updates to the future.
Thus freeing up the main bukkit/spigot execution thread to allow other tasks
to run to help prevent the server from lagging.

* **Support for LuckPerms v5.0.x**
In addition to supporting older versions of LuckPerms, Prison now is able to 
integrate LuckPerms v5.0.x or LuckPerms v.4.x or earlier.  Take your pick. 

* **Minor changes to reduce the number of compiler warnings**
Minor changes, but better code overall.


* **Improved mine regeneration performance by 33.3%**
Figured out how to improve performance on mine regeneration by roughly about 33.3% overall. This
could help drastically improve performance and reduce lag since block updates must run 
synchronously to prevent server corruption (limitation is due to the bukkit and spigot api).  

* **Mine stats and whereami**
Added a new command, **/mines stats**, to toggle the stats on the mine resets.  Viewable with **/mines list** and **/mines info**. Stats are now within the MineManager so it can be accessed from within
the individual mines. 
Added a new command, **/mines whereami**, to tell you what mine you are in, or up to three mines you are nearest to and the distance from their centers.


* **Major restructuring of how Mines work - Self-managing their own workflow**
They now are able to self-manage their own workflow for sending out notifications and for resetting automatically.
Mines is now layered, where each layer (abstract class of its ancestors) contributes a type of behavior and business logic that allows Mines to be more autonomous.
As a result, PrisonMines and MineManager are greatly simplified since they have less to manage.
Because Mines are now self-managing their own workflow, and they have taken on a more expanded role, some of the mine configurations are obsolete and removed.
Mines only notify players that are within a limited range of their center; they no longer blindly broadcast to all players within a given world or the whole server.
Mines extend from MineScheduler, which extend from MineReset, which extend from MineData. Each layer focuses on it's own business rules and reduces the clutter in the others, which results in tighter
code and less moving parts and less possible errors and bugs.


* **Concept of distance added to Bound objects**
Added the concept of distance between two Bound objects so as to support new
functionalities between mines and players.


* **Gradle now ready to upgrade to v5**
Resolved the last few issues that would have caused issues when upgrading to gradle 
v5. Explored how gradle can use java fragments and variables.



