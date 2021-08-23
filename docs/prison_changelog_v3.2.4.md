[Prison Documents - Table of Contents](prison_docs_000_toc.md)

## Prison Build Logs for v3.2.4 - 2021-03-01

## Build logs
 - **[v3.3.0-alpha - Current](changelog_v3.3.x.md)**
 - [v3.2.0 through v3.2.10](prison_changelogs.md)
 

 

Greetings!  I'm delighted that you are interested in the build logs for the
Prison plugin.  I'm wanting to provide a more formal documentation as to what 
is going on in each build so you have a better idea if it may be something 
that you need.




# V3.2.4 2021-03-01 
Release of next bug update.



* **Backpacks default size editable**
  It's now possible to edit the backpacks default size from the backpacksconfig.yml that will 
  be set for each new player when joining the server.

* **v3.2.4-alpha.17 2021-03-01**


* **Added support in Mine BlockEvents for the new placeholders `{msg}` and `{broadcast}`**
which runs the commands `/prison utils msg {player}` and `/prison utils broadcast`.


* **Added support for prison utils messages.**
Provides an easy way to send messages to a specific player or to all players. To be used in scripts and within prison commands such as block events or rank commands.


* **More tweaks to get the block counts updated when the server starts up so they will be accurate and won't need to wait until the next reset.**


* **Restructuring of some of the handling of block names prior to processing the block events (block break or explosions) to preserve the original block names.**
This is important too, since if the block name is AIR, it is understood that it could not have been broken with a standard BlockBreakEvent.  The explosion events appear to never eliminate AIR blocks, but prior processing before passing to those points of execution, the AIR blocks were filtered out.  So in general, they should never be AIR, but they could be tied back to the original block names too.  But with explosions, one has to be very careful since erroneous AIR blocks can get counted multiple times and hence throw off the actual counts for the mines.

* **Fixed issue with the logic. Need slight different groupings.**


* **v3.2.4-alpha.16 2021-02-28**


* **Fixed a bug with calculating the fortune above level 5 where the random number was not being multiplied by 100.**
This now works.


* **Added totals to the block list along with the block remaining counts.**
Hooked up the block counts to the mines.  Still having issues with accurate counts on the explosions.


* **Start to hook up block counts for mines.**


* **Hook up auto manager to CrazyEnchant explosions.**
Also reworked how auto manager auto pickup deals with explosions and block counts.  They are more accurate now.  Plus they now support reporting the block names so that can be added as a new filter type in the future.  Monitor has been changed to only support mine resets.


* **Some tweaks to get the CrazyEnchants explosions to work correctly.**


* **Change how blockevents fire for explosions.**  They now fire when the blocks are broken, instead of at the monitor event time.  The reason for this shift for explosions is due to the fact that it is impossible to tell what blocks were just exploded at the monitor event since the block list can contain air to begin with.
Also modified the MineBlockEvent class to provide an isFireEvent function that will perform the check on chance, event type, block name, and triggered (for TE Explosions).


* **Rough start of spatial indexing.**
The purpose of a spatial index is not so much for within a mine, but to help identify if a location is within a mine.  At this time, each mine will need to be checked one at a time, blindly, to see if a location is within a mine, and it may not be. The basic server may have about 30 mines, but a larger, more complex server, may have 40+ mines.  Use of a spatial index will return one or two mines near the point in question, so it will cut down on the amount of blind checks.


* **Fixes crazy enchant's explosion event to work with normal drops.**


* **Moved the PlayerArmorStandManipulateEvent to the correct class.**
Found it in AutoManager when auto manager has nothing to do with armor stands (hopefully).

* **Change how Crazy Enchant explosions work.** 
Using the same new techniques that TokenEnchant is using to ensure that the blocks are within the mines.  I do not have a copy of CrazyEnchant so I cannot test it.


* **Setup a different way to calculate line of sight to find a target location.**
This will be used with /mines whereami so it can look through walls, such as mine liners, to detect if the player is looking through walls.


* **Using the mine tag instead of mine name where it can be used that way.**


* **Backpacks compatibility with 1.8.8 tested**
Tested and fixed an error of backpacks, not it's working with 1.8.8 just fine.


* **On join backpack item**
By default this will be enabled, you can disable it from the backpacksconfig.yml, this feature
  will give you an item to open the backpack on join.


* **/Backpack item command**
It is now possible to get a copy of the that's being used to open the backpack while in game with the command
  /backpack item.
  

* **Prison Backpacks skull support**
Skulls are now supported by Prison backpacks, everything from an ItemStack should be now supported.


* **Prison Backpacks are now a thing...**
This's still a very early access feature but it's already working and powerful, it supports custom
  items displaynames, lores (not tested lores), enchantments.
  You can open it with `/backpack` as a player, or `/gui backpack` as an admin.
  It's possible to enable it from the `config.yml` by turning to true the `backpacks: false` option.
  When enabled and restarted your server, you'll see a new folder named `backpacks`, where there're 2 files:
  `backpacksdata.yml` and `backpacksconfig.yml`, all the backpacks options are editable from the backpacksconfig.yml
  and you should **never** touch backpacksdata.yml.
  I repeat, this's early access, might be unstable and have a ton of bugs to fix, until then please report all
  your issues on this new feature and also feel free to make feature requests, it's really appreciated.
  At the time of writing, SELLALL AND AUTOPICKUP AREN'T SUPPORTED!
  

* **v3.2.4-alpha.15 2021-02-25**


* **New feature: If a player logs out while in a mine, and then logs back in to the game after the mine resets they can suffocate to death.**
Now this situation is detected and the player is teleported to the mine's spawn point.


* **Rewrote how the chat placeholders are processed.**
Found issues with the old way of dealing with it.  This should be more flexible and is able to deal with any case placeholder now.
There is room for improvement, such as caching PlaceHolderKey's that are related to chat placeholders since they will always be the same (mostly).  Can also refactor some of the other placeholder code that can take advantage of these new techniques.


* **TE Explosions are not able to produce accurate block counts.**
These changes improves the accuracy, but it's still not perfect.  Will address in the future.


* **Player Mines GUI will now show Mines Tags**
Player Mines gui (aka /mines or /gui mines) will now show Mines Tags as titles instead
  of the Mine Name.
  

* **GUI Prestiges console error fix**
You won't get an error for trying opening the gui prestiges from the console now.
  Some other GUIs are involved and fixed too.


* **Removed some of the logging and update some of the docs too.**


* **Enabled the auto manager's auto pickup to work with TokenEnchant's TEBlockExplosionEvent.**
Needs more testing to ensure everything is working properly with and without the auto manager enabled.


* **Rewrite of TEBlockExplodeEvent to properly handle TE Explosive enchants within the prison mines when auto manager is NOT enabled.**
Most of the rewrite for enable Auto manager is in place but has not been tested or tweaked.
Prison will cancel the explosive event if it is not within a mine.  This may cause issues with other things, such as use out in the wild, but for now it gets it working within the mines.  The targeted block is the one that is tested to see if the explosion occurs within the mine.  If it does, then all other blocks with in the explosion are tested to ensure they are within the mine, and if they are, then they are broke/dropped.  
Warning... OP'd players can break blocks outside of the mine, so only test and use as non-op'd player.


* **Fixed a bug where the block break count could go negative if there are too many blocks counted as being broken, than what are really broken.  **


* **Hook up integration permission listing to the /ranks player command.**
Found an issue with the getSellallMultiplier when the player is offline.


* **Added addGroupPermission and removeGroupPermission to the PermissionIntegration classes.**


* **/gui mines Blocks are now editable while in-game.**
You can now edit the Shown block of Mines of the /gui mines, aka player's GUI,  
while in game from the /gui -> Mines -> Select a mine -> Mine_Show_Item.


* **v3.2.4-alpha.14 2021-02-20**


* **Some adjustments to the /mines list command so its slightly easier to read.**


* **Fixes to finalize the mine constraints. Now it's working with min, max, ExcludeTop, and Exclude Bottom.**


* **New feature: /mines whereami now includes the block that you are looking at** if it is within a mine and it also includes the distance to that block.


* **Clean up a few of the mines constraints to reduce the size of the constraint name.**
It was too long to type and too long to display. 
Still having issues with constraints and mine generation, so it's not ready yet.


* **Fixed an issue with block break events and within mine detection.**
Basically sometime a check to see if something is in a mine needs to be exact, like block breakage, or it needs to include a layer above and below, such as for players.
So made a distinction between the two so block breakage is now exactly within the mine.


* **Starting to add new block constraint for ExcludeTopLayers ExcludeBottomLayers.**
It's not yet functional.


* **SellAll Sell sound play-feedback** 
It's enabled by default now a sound play-feedback when using the /sellall sell command, it's customizable from
  the sellallconfig.yml, you can change the sound on success and fail, disable or enable it.


* **Hooked up the Min block constraint to the mine resets.**
Reworked the mine resets for the synchronous reset so it now uses the target block list just as the paging does. This allows reduction of code and simplification, but more importantly, allows the application of the min constraint before placing any of the blocks.


* **Fixed the /mines block constraint add command to allow a min to be added when max is zero (disabled).**


* **Added a reset for the resetBlock count**
Added toString functions to PrisonBlockStatusData, MineTargetBlockKey, and MineTargetPrisonBlock to help with debugging.


* **Made PrisonBlockStatusData an abstract class and added isAir() abstract function.**
Enabled isAir in both PrisonBlock and BlockOld.



* **Reworked some of the internals on MineResets to prepare for the next phase of work.**
Created a MinTasks class to house some of the tasks that are more task oriented.


* **Added sellall triggers**
New triggers feature, it's now possible to add items/edit/delete that will trigger the /sellall sell
  while shifting and right click, there're some options in the sellallconfig for customization
  too, it's possible to enable this feature while in-game by commands, there still isn't a GUI
  for this and maybe I'll add one in the future.
  
  Permission: prison.admin
  
  Command: /sellall trigger <args> <args> -> /sellall trigger true will enable this feature
/sellall trigger add-edit-delete <item> is for management. 
  

* **Fixed SellAll Blocks GUI**
Found and fixed a SellAll config bug not updating, this was breaking many things and
  also the new feature incoming, this got now fixed.


* **Changed the mine resets use of target block collections.**
Changed the way the data is stored in that collection so it is able to store both the new and old block models
Every reset now stores their block assignment in this target block collections, of which, paged reset have always used them.  
This is critical for being able to enabling a constraint fo minimal blocks of a specific type. Plus it will be critical to properly logging what the block type was since if prison get's an AIR on a block break event, prison can use that collection to find out what that block was when it was broken so it can properly record it.


* **Auto manager lore: Strip off color codes so the comparisons work if color codes are enabled.**
Noticed that sometimes random color codes are added when enchanting.


* **Rewrote the lore checks on auto manager to use the prison ItemStacks.**
Also simplified some of the code since some of the checks were no longer needed since they are now handled with the prison classes.


* **Fixed bug of not passing the useNewBlockModle variable to the getBlocksList function; was hard coded with true.**
Also showing three dots instead of just one when using the new block model so it's a way to visually tell how the server is configured and what block list is being shown.


* **Player GUIs now editable**
You can edit Player GUIs titles from the GuiConfig.yml now.


* **Hooked up the new block constraints for max.**
Now when enabled it will prevent more than the max amount from being spawned.
The min constraint has not been hooked up yet.


* **Added block constraints of min and max that will try to constrain the spawning of the blocks.**
They are hooked up to be saved and loaded through the mine's save file.  They also are hooked up to the commands to set them, remove them, and to include them in the block lists.
They are not yet hooked up to constrain the block generation yet.


* **Added new command /mines block list to complement the other mines block commands.**
Since the block list was refactored, it was trivial to add this command and only makes sense since new users will see the /mines block listing and may not realize they have to use /mines info with page 2 or ALL to show the block list.


* **Refactor the display of the mine's block list to eliminate duplicate code**
since the new block model and old block model has been able to be merged together for some of the functionalities.


* **v3.2.4-alpha.13 2021-02-14**


* Cleaned up some of the player permissions with SpigotCommandSender, SpigotPlayer, and SpigotOfflinePlayer to ensure they are using the correct bukkit internal functions.**


* **SellAll Permissions edited**
Removed some SellAll permissions options from the SellAll config because kind of useless.


* **Bug fix: SellAll broken**
Working on fixing many SellAll broken permissions, now booleans are working again.


* **Buf fix: GUI error.**
Improved Prison GUIs handler that won't trigger without opening a true Prison GUI, this will
  fix random Prison GUI errors.


* **Bug fix: This audio file does not exist on 1.8 so catch the exception and ignore.**
A sound still plays so not sure the point of this actually.


* **Add the stats on block counts to the /mines info details.**
Includes the number of blocks that are present in the mine (P), the Total (T), the Session (S) since server start, and the Un-Saved (US) counts.


* **Setup blocks mined counts for each mine.**
There are a few stats being tracked: blocks placed during a reset, total blocks mined, total mined since server start (session), and unsaved blocks mined.
This is not fully hooked up yet.


* **Clean up some of the messages related to the /mines stats command.**  
Eliminated the stats on TP events and broadcasting messages since they are trivial in time it takes to run those parts. Fixed a stats bug in the synchronous reset page count should have been always 1, but it was accumulating after each update.


* **Provide some docs on the PlaceholdersUtil.formattedTime() function.**
Also changed the parameter name to timeSec so it reflects the unit that is expected.


* **If the mine file is manually edited**
and a comma is added to the end of one of the block lists, then docBlock could be null and cause a problem when trying to split the percentage from the block name.  This can also be caused if the last block in the block list is deleted and the comma is not removed. The bottom line is the files should not be manually edited. This issue, if it happens again, will be ignored when parsing the files in the future.
			

* **A work in progress: Adding the ability to get the list of player perms from the integrations.**
This will allow for listing perms when the player is offline.  Also will have the ability to include more details with the perms which will be for display purposes only.


* **Change around the /mines block search command to allow the searching for non-block items**
with the new command /mines block searchAll.
It appears like some blocks may not be marked properly as blocks or items, so this at least allows finding items to confirm that they exist.  They player could try to add items to mines, but they will fail to be placed.


* **Initial setup of the /ranks player command to include the players rank if the one who is issuing the command has the perm ranks.admin.**


* **Redesigned how the player permissions are worked to make sure more of the are actually working**
instead of being dead functions.  Also ensure they are trying to use active players to ensure that the permissions have the best chance of being retrieved.  Changed the offline player to actually check for OP and "try" to get the player's perms instead of just return nothing or false.


* **Lockout players from listing prison commands.** 
The subcommands related to prison have been tied to the permission `prison.command` and calls itself with the help keyword.  So the subcommand `/mines set` is locked down with the permission `prison.commmand` and when used submits `/mines set help`.   So the behavior is similar as before, but non-op'd admins need to be granted the permission to list the commands.


* **Fix the CommandHander so it will not print a blank line if the description was not set.**


* **SellAll sell earned money formatted**
SellAll sell money's now formatted properly.


* **New SellAll set Multiplier GUI**
By clicking on a Prestige on the /sellall gui -> Prestige Multiplier -> Click on a prestige
  it's now possible to open an editor to edit the SellAll Multiplier Prestige of that prestige
  directly from the GUI.

 
* **SellAll GUI shortcut**
It's now possible to use /sellall gui even with the command /gui sellall.
  

* **GUIs permissions fixes**
It should be now possible to use GUIs command without permissions issues.


* **v3.2.4-alpha.12 2021-02-08**


* **Hooked up the new /prison utils module.**
I added it as a module so the whole thing can be disabled and won't be loaded, and it can take advantage of the prison module's infrastructure.
Initially this has command line access for /prison utils repair and /prison utils repairAll with a few different options.  These commands, and future util commands, are targeted to be used with blockEvents, or other rank and mine commands. Or they can even be used outside of prison if desired.  These utils will support spigot v1.8 through v1.16.
The next utils command will provide potion effects.  That is not yet enabled or tested.


* **When getting the player's rank number, if it player does not have a rank on that ladder, then it will now return a zero.**


* **Fixed issues with item stacks and support for compatibility issues.**
Reworked some of the compatibility functions to handle different situations where it failing.  Now for spigot 1.8, if it fails to map a block, an item, a tool, a weapon, or armor to a valid BlockType, it will fall back to XMaterial to make that mapping possible.  Found that were some items are defined with a data value of zero, in reality, the value could be anything.  So prison's old block model uses the "book" values, but when those fail, then it defers to XMaterial to apply a more aggressive mapping.  Also fixed some of the SpigotPlayerInventory functions since they were not hooked in to the compatibility functions so they were being problematic.



* **Allow the rankup command be ran from console and through other commands.**  It will not allow it to actually run, since a player name cannot be provided, but it will now allow /rankup help from console.  Added a function isPlayer() to all player objects to identify if being ran by a player, or from the console.


* **Fixes prison's placeholders so that way when papi is reloaded it will no longer forget what prison registered.**


* **v3.2.4-alpha.11b 2021-02-06**
Note: alpha.11 was built, but the changes to the build files were not committed.  11.b now represents an accurately set version. 


* **SellAll new GUI and many changes.** 
Added new GUI and button. Minor changes and some fixes. Remove unused imports.
Fixed some Optional permissions, essentially I was getting the permission path instead of the value from the config with that path.  Fixed a sellall issue when the permission was enabled it was using the permission for the path instead of getting it from the config.


* **Mines GUI pages fixed**
Fixed a simple error of Mines GUI opening the Ladders GUI new page instead of the Mines new page.
  


* **SellAll Prestige Multipliers GUI**
Added a new button to the /sellall gui to open the Prestige Multipliers SellAll GUI.
  For now you can only see Multipliers values and delete them with a right-click.


* **SellAll Optional Permissions review and fix**
Some Optional Permission weren't working, now this got fixed, there may be
  more errors like this, if you find any please report it to me (@GABRYCA).


* **SellAll Sign Permission**
It's now possible to enable a permission to use the SellAll sign, from the SellAll Config you can toggle 
  `SellAll_Sign_Use_Permission_Enabled: false` to `true` to enable it, the permission's also editable 
  from the same config, by default it's like this: `SellAll_Sign_Use_Permission: prison.sign`


* **SellAll Sell only trough sign**
Added a new option, by the name if enabled you'll be able to use `/sellall sell` only trough a sign
  (make a sellall sign and click it), just toggle this option to true to enable it: `SellAll_By_Sign_Only: true`
  from the config, by default it's `false`, **Note:** there's a bypass permission
  so, if you're OP you'll still get access to it but players won't, the permission's editable
  from the SellAllConfig too and by default it's like this: `SellAll_By_Sign_Only_Bypass_Permission: prison.admin`.
  

* **SellAll Sell notify option:**
It's now possible to disable or enable from the SellAll config the message telling 
  you the amount of money earned, to disable the message just turn this option to false
  like this `Sell_Notify_Enabled: false` by default it's on true.


* **Starting to hook up some of the first prison utils** 
That can be used within blockEvents to provide more functionality for the mines.  This is not yet enabled.


* **Add some new functions to Prison's SpigotItemStack and SpigotOffilnePlayer to prepare for the next enhancement pertaining to Utility functions.**


* **Fixed issue with blockEvents by using the newer versions of the BlockEventType.**
There was a change last week to simplify them, and not all references to the old types were changed. 
It's setup that when loaded from the files it will auto convert to the correct new version.


* **SellAll set currency from GUI.**
  It's now possible to edit the SellAll Currency from the /sellall GUI using the SellAll-Currency button.


* **Fixed the issue with the chat handling of the placeholders.**
The issue I saw was where one placeholder was being processed instead of the correct one.  So what i saw was trying to use the placeholder prison_mbm_pm but it was targeting prison_mbm_p which is a prison_mbm_minename instead.  This was fixed by anchoring the end of the placeholder so the whole placeholder is tested from { to }, or from { to :: if an attribute is being used.


* **Fixed a few more things pertaining to the playermines placeholders.**
Works now, but still issues when used in chat messages (will fix next).


* **Ranks,Ladders, Mines GUI now support more than 54 Ranks/Ladders/Mines:** It's possible to see now
more than 54 Ranks, Ladders or Mines, a new button got added to go to the next page if more than 45 Ranks/Ladders
  are added, there's also a Prior button, this works like the "add blocks GUI" for Mines, just with Mines, Ranks and ladders.


* alpha.10d

* **New feature: This update hooks up the support for `options.autopickup.autoPickupBlockNameList`.**
To enable this list, you must set `options.autopickup.autoPickupBlockNameListEnabled` to true.
The block list names must be from this list of values and must be valid for your server's version of spigot: 
https://github.com/CryptoMorin/XSeries/blob/master/src/main/java/com/cryptomorin/xseries/XMaterial.java
NOTE: This new feature has not been tested yet.


* **This is a work in progress.  Working on getting playermines placeholders working.  Not yet functional.**


* **Include top of mines within the check for a location being within a mine.**  Also allows a virtual mine to perform a check if a block is within a mine's config.


* alpha.10c

* **Bug fix: Fixed a bug where it was not able to properly change ranks or remove ranks.**
This was related to an internal change where the main processes had to constantly look up ranks and ladders because they were using magic numbers for all the references.  It was changed over to use an object oriented design so nothing has to be cross referenced and looked up by magic numbers.  This should result in a performance boost under heavy loads.


* **Map currency name "default" to the default currency.**


* **Add new option: isCalculateFortuneOnAllBlocksEnabled which defaults to false.**
When set to true it will apply fortune to every block that is mined, ignoring the type of block.


* **Sellall fix for custom currency.**
The bug was basically within the RankPlayer object. 
Hooked up sellall to the RankPlayer for the currency usage which will update the player's currency cache.


* **v3.2.4-alpha.10 2021-01-28**


* **Some adjustments to the RankPlayer's econ functions.**
Reworked the rankup code to use the RankPlayer functions for economy.


* **Setup new mine related parameters to fine-tune control reset-paging.**
This can be disastrous if the admins do not know what they are doing.


* **Added support to parse Long parameter values through the Platform.**
This is needed so as to prevent the need of casting a long to an int.  The actual parameters used may not require actual long values, but the targeted fields and data types are of type long so these need to be properly reflected in the configs.


* **Sellall sell improvements:** 
It was possible to use `/sellall sell` even without the permission when enabled, and sometimes it didn't  worked for everyone.


* **`SellAll set currency <currency>` with currency names with spaces fixed.**
- Because Spigot recognizes spaces as multiple arguments, a custom currency name with spaces would lead to a missing economy (for example "Euro Broken" would be recognized as "Euro" and not with the full name, which's an error).

**NOTE: Custom currencies with spaces in their name may not work with ranks.  Use spaces at your own risk.**



* **PLEASE DELETE YOUR OLD MESSAGES CONFIG TO APPLY CHANGES!**
  To do it please open the Prison folder on your server, then module_conf and lang, there delete your old config en_US.yml
  (for example) to apply all changes and avoid double messages, **Prison will try to automatically update already existing messages**
  with an integrated converter, it'll try to remove tags from messages, but sometimes they don't work properly,
  so some tags might still be there, if you want please *move on and delete them manually*.


* **Edited Spigot Messages config.**
Edited the Spigot messages config, removed Prison tags and now using default Prison standards methods for all messages.
  

* **Fixed some SellAll bugs.**
Spotted some weird things about some command of SellAll not updating correctly (like /sellall delete) and
  fixed them.



* **Fixed critical SellAll value!**
Literally, one logic character was missing for sellall and kind of breaking it, fixed.


* **v3.2.4-alpha.9 2021-01-27**


* **Add support for Crazy Enchant's BlastUseEvent so prison will pickup explosion events within the mines.**


* **Fixed the currency issue with RANKUP_CANT_AFFORD.**
It was using the local currency, which was injecting symbols that cannot be displayed within minecraft, like non-breaking spaces.  
It now just formats the number naturally with commas and will display the rank's custom currency if it is provided.


* **Hooked up a new way of storing ranks in ladders.**
Eliminate a lot of the old ways that used the PositionRank object (which was eliminated).  Also eliminating references by rankId and by position.  Position is now fabricated and will be phased out, but in the mean time it has been moved to Rank.


* **Edited many messages.**
Edited many messages at the messages file (path /module_conf/lang/), they'll apply
  only if you delete your old one, be careful because if you do it all your custom
  or edited/translated messages inside this config will get resetted.


* **SellAll delay set moved to SellAll set delay**
Moved the command /Sellall delay set to /sellall set delay.


* **SellAll set currency**
Added a new command, like the /ranks set currency <currency>, this will allow SellAll to work with custom currencies.


* **SellAll BackPacks support**
  Added support for MinesBackPacks.


* **Start work on the prison's top player enhancement**
Moving economy functions to the RankPlayer object to minimize having to have external hooks to get those details.  Provide for a player's balance cache to help reduce hits on the server to full fill requests for placeholders that will be added soon.


* **v3.2.4-alpha.8 2021-01-19**


* **SellAll Delay GUI**
Added a new Button to the SellAll GUI and also a new sub-GUI for the SellAll Delay
  feature.


* **AutoFeatures GUI redesign**
New design for AutoFeatures GUI.


* **Added Ranks GUI Progress Bar.**
Just added a progress bar to the Ranks GUI to see the next Rank how far is to get available for unlock/rankup.


* **Added spiget as a jar instead of using the maven repo since it's been going down frequently and the builds were failing remotely.**


* **Setup** `/mines tp` **or just** `/mtp` **to teleport to them mine associated with the current player's rank**
on the default ladder.  Only will work when mines are associated to ranks.  If there is more than one mine tied to a rank, it will first try to select the mine that has the same name as the rank, otherwise it uses the first mine (arbitrary mine).


* **Added an alias for removing ranks from a player: /ranks remove rank.**
This is a shortcut for /ranks set rank -remove-.
Updated the docs on rankup to reflect these changes and to cover how to remove ranks from a player.  Also added information on /ranks players.


* **Added a formula to calculate the fortune multiplier on all fortune levels greater than 5.**
This allows for an highly OP tool with no limit on fortune levels.
 

* **Bug fix with enabling fortune being applied.**
Needed to check to ensure the bukkitStack object was not null prior to updating.  The bukkitStack may not always exist especially if the stack is created.


* **V3.2.4-alpha.7 2021-01-16**


* **Added many more ores and blocks to the fortune.**
Some of which are not normally found in "nature" (man made) so they would never be mined (most of the blocks).


* **Bug fix: This fixes an issue with the fortune enchantment not providing more drops.**
The code that generates the additional drops was found to be good.  But what was wrong, was Prison's SpigotItemStack's internal value was not being reflected back in to the spigot's ItemStack that backed the object.  The fix for the setAmount function is to set the amount value for both the prison's SpigotItemStack and also bukkit's ItemStack.
This probably was causing potential issues elsewhere too.


* **Text.translateHexColorCodes: Fix a potential NPE when the text is null.**
  It should not happen, but suspect it could be caused if there isn't a value, such as if it is trying to get the rank of a player on a ladder of which the player is not a part of.


* **Added playerName as a parameter to the /prison placeholder test command**
so it can use stats from that player.  If the playerName cannot be resolved, then the value used within the playerName variable is added to the search text field so nothing is lost and playerName is strictly optional.


* **Added a new unit type for placeholder formatting.  kmbt**
which uses KMBTqQsS as units.
Example of use: /prison placeholders test {prison_mines_size_temp5::nFormat:#,##0.00:0:kmbt}
Where temp5 is a mine name.


* **V3.2.4-alpha.6 2021-01-15**


* **Add ability to select to TP to either the spawn location for a mine, or the mine's center location even if the spawn point exists.**
This is useful if spawn gets disconnected from the mine and there is no easy way to find the mine.


* **Fixed a NoSuchMethodException on BlockEvents for TE Explosion events.**
Thought that Exception would have caught it.  Must be due to the fact that it is generally assumed that the NoSuchMethodException is mostly a compile time exception and not a runtime exception so they split it out as far as what it extends from?  


* **SellAll delay.**
 Added SellAll sell delay, it's now possible to add a delay to execute sellall sell, global
  permission for editing: `prison.sell.delay`.
  - New /sellall delay <true/false> command, you can now disable sellAll delay while in game.
  - New /sellall delay set <Delay_Seconds> command, you can now edit sellAll delay while in game.


* **Fix a mapping of terracotta to hardend clay only, and not hard clay.**
The two are different. This may "fix" this in the mappings for BlockType, but this still won't work well, since XMaterial is incorrectly mapped with terracotta.  I submitted a pull request to fix it but last check they did not pull it.


* **A few edits to the beginning of the spigotmc.org resource page document.**
Added a couple of screen prints for examples of a couple of commands.


* **Many updates and a lot of great work put in to the spigotmc.org's page design**
by Gabryca and graphics by Madog24 this week.  A lot of greate progress has been made.


* **ChatDisplay changed function text to addText and changed emptyLine to addEmptyLine**
to better align these commands with the other text based message handling functions.


* **Started to add Block names to BlockEvents so blocks can be targeted with the blockEvents.**
What makes this more dificult to deal with, is the fact that you can specify more than one block per BlockEvent.  This makes it more complex for the editor since I want to be more selection based so the end user does not have to type in hugely long commands.  
This is a work in progress and is not yet functional... it's enabled, but does not cause errors or adds the actual blocks as of yet.
For the blockEvents: changed the use of .command to .suggest since suggest will not run the command for the user, but will allow them to make the final edits on it before submitting.  Plus it acts as a "confirmation" to follow through on the command. 


* **New SellAll Admin GUI and Sub-GUIs**
Rework of the admin SellAll GUI, added AutoSell GUI, moved the old Admin GUI to a Blocks GUI.

* **New SellAll AutoSell perusertoggleable commands**
It's now possible to edit while in game the perUserToggleable AutoSell feature with a command:
  - `/sellall autosell perusertoggleable true` `prison.autosell.edit`.
  - `/sellall autosell perusertoggleable false` `prison.autosell.edit`.


* **Rework the BlockEvents to simplify the add command.**
Now to get some features you must make changes after with the editing with the other commands.
Added player task modes and made it an enum.  So a task can now be ran as the player instead of just as console.
Changed the names of the BlockEventTypes to simplify them, but set them up to translate to the new enum names.



* **Added new AutoSell enable and disable commands**
It's now possible to enable and disable autosell while in game for everyone (enable or disable the whole feature).
commands:
  - `/sellall autosell true` `prison.autosell.edit`
  - `/sellall autosell true` `prison.autosell.edit`



* **Added new placeholder: prison_player_sellall_multiplier**
with an alias of prison_psm.  This will return the value of the multiplier and if nothing is configured for it (no ranks, no sellall, no perms, ect) then it will return a value of 1.0.  This placeholder works with the nFormat placeholder attribute so the value can be formatted as desired.


* **Add the rank commands for managing rank permissions and rank permission groups.**
This is not functional other than adding these perms to the ranks.



* **Work on Ladder and Rank Perms.**
Got the Ladder perms hooked up to both the loading and saving, and also the command interfaces to add and remove them.
This is a work in progress and is non-functional.  The ladder perms working from the sense that you can add them, list them, and remove them.  But they currently do nothing.
The next step is to finish working on the rank perms commands to get them functional with adding and removing... they should already be functional with saving an loading.
But even at that point, this will not be functional.  The whole core of the perms and the perm integrations need to be rewritten to utilize the expanded features.


* **GUI code cleanup and optimizations.**
The GUI's improving and getting more stable, the code got some refactoring, this won't really change things for the end user but it'll for devs who are wanting
to edit GUIs, I hope to manage in the future to improve it even more.

* **V3.2.4-alpha.5 2021-01-02**


* **Add new feature to reset all mines with one command.**
This works for all mine types, including mines setup for paging.
This will build a list of reset commands for all mines, then it will submit each one to run.  When each mine finishes, then it will submit the next reset command to run until there are no more mines to reset.  It's using synchronous jobs to manage the resets so as not to dominate the processing and to yield to other tasks needing to be processed.
The mine resets can also be canceled.
The following are examples, with additional processing options of `details` will provide some information on the reset progress.  Details is optional.


When running a reset for all mines, it also automatically enables the noCommands option on all resets.  If a mine is setup with a mine command to reset another mine, then this could cause an endless loop.  Therefore no mine commands are ran when all are rest.

```
/mines reset help
/mines reset *all*
/mines reset *all* details
/mines reset *cancel*
```


* **When setting the area of a mine, it now refreshes the liners and shows the tracers.**


* **New feature: Force a reset but be able to not run any of the mine commands.**
This allows for chaining of mine resets to other mines.


* **V3.2.4-alpha.4 2021-01-01**


* **Start to setup the ranks perms listing: disabled.**  It will work, but I turned off the command since it is not ready yet.



* **Changes to the Gems Economy integration wrapper to support the new version of Gems Economy.**
This uses reflection to get around the problems which was introduced with v4.9.0 where the API and its methods remained the same, but one class that is used for the method variable had its package name changed.  Thus breaking support for gems economy v4.9.x when the project is compiled for v4.8.3 and earlier.  This reflection modifications should also work if the project is compiled with GE v4.9.x and the deplyment is using v4.8.3 or earlier.
Tested with being compiled with v4.8.3 and works well with v4.9.1 that is running on v1.16.4 (v1.16.x requires v4.9.1). Tested and works on v1.8.8 with v1.8.8 running either v4.9.1 or v.4.8.3.
It has not yet been tested with compiling prison with v4.9.1, but it should work even when running on 1.8.8 with v4.8.3.


* **Made the selection of ranks case insensitive.**
Many commands in rank commands required the proper case spelling of the rank name.  Changed it so it is now case insensitive so it will be easier for players to select ranks.


* **For a few rank commands: Clean up some formating with currency names.**
Collect all currencies used within the default ladder, then display the player's balance with each of those currencies.


* **Fixed a problem with the addition of the permission and permission groups.**
The loading of of these was failing since the the arrays were not being instantiated. 


* **Updated the RankLadder and RankPlayer so the class variables are not accessible from the outside of those classes.**


* **Update Rank to fix issues with Rank class variables** being directly accessible from outside the class.  Class variables should never be directly accessed by outside classes.


* **V3.2.4-alpha.3 2020-12-30**
Bump the version.


* **Add support for a new Placeholder Attribute called text.**
This is strictly for debug purposes and also to format hex colors if they are not working in the other plugins that are using prison's placeholders.
This actually works very well, and has been tested with v1.16.x.
Update the docs and a few comments on the other attributes.



* **Added an alternative hex formatting for the placeholder attributes.**
This is another way to try to get hex color codes to work with plugins that do not support them directly.  
With testing, I found that hex2 format actually worked very well with other plugins and resulted in the hex colors working well.


* **Placeholder Attributes: Changed how debug works so hex can be added too.**  Instead of using a String array, converted it over to use a list.  The value of hex and debug are extracted prior to extracting any other parameters since those two values are now non-positional and can appear in any parameter location.
Debug statement now includes the original raw string that is non-converted to the spigot color codes so you can see what the original raw codes were for debugging purposes.  The parameter hex now will convert the color codes before sending the resulting placeholder value back to the plugin that is requesting it.  This may allow the successful use of hex colors in plugins that do not yet support them since the hex codes would have already been correctly converted.


* **Full support for hex color codes.**
The use of #abcdef will be converted to the correct color codes.  This applies only to prison messages and will not provide any translation for placeholders that are sent back to the requesting plugins that are using them.  They would have to support hex colors on their own.
This is only a feature that works with minecraft v1.16.x and newer.  Older versions of minecraft and spigot may produce undesirable artifacts.


* **V3.2.4-alpha.2 2020-12-29**
Bump version.


* **Upgrade Cryptomorin's XSeries to v7.8.0 from v7.6.0.0.1.**


* **Upgrade TokenEnchant to v18.11.3 to support the new TokenExplosionEvent's getTrigger().**
This allows BlockEvents to now support filtering on TE's plugins that trigger the explosion event.
Added support for editing and displaying the triggered parameter.


* **Added a new unit type to the number format attribute: binary.**
Binary is based upon the power of two and uses 1024 as a divisor instead of the base 10's 1000.


* **Updates for the placeholder attribute bar to work with the debug mode.**


* **New features added to the PrisonSpigotAPI to locate the given mine a block was broken in**,
or where a player is standing.  If it is being used for block related usage, then it would be best to base it upon the block that was actually broken since the player could be standing outside of the mine while mining (such as on top or to the side).
This code utilizes an internal prison player cache to help reduce overhead in location the mines.  The last mine a player was in when they successfully mined a block from a mine will be use as the first check. If they are not in that mine, then the others will be searched, but odds are that if they are mining blocks, then they will be getting more than just one before going elsewhere.
This code is similar to what's being used within the auto features.


* **Enhancement that provides for a way to prevent the translation of color codes within a given text String.**
Ran in to an issue with for display purposes, had to show the raw codes that were used, but there was no way to do so since they were being translated.  So added support for regular expression style of quotes to skip over a section of the string when translating.  These quotes are \\Q and \\E.  Everything between them will be ignored when translating color codes.


* **Hook up support for placeholder attribute support for bar graph customizations.**
Appears to be working well.


* **Updates to the placeholder attributes for the number formatting... little changes to get it to work better.**
This appears to be working really well. 


* **New feature: Placeholder attributes. Dynamic placeholder customizations.**
Allows for dynamic customization of different placeholders.  
The first placeholder attribute that is supported is the number format: nFormat.  This allows for full customization to be defined within a placeholder itself, so each use of that placeholder could be configured differently.
For example this placeholder provides the number of blocks remaining in a mine: %prison_mr_temp5%'
This can be customized with the following examples: 
  %prison_mr_temp5::nFormat:0.000:1:kmg% 
  %prison_mr_temp5::nFormat:#,##0.##:0:kmg%
  %prison_mr_temp5::nFormat:'&4$&2'#,##0.00'&7':3:kmg:debug%
The last example shows that formatting codes could be enclosed within the placeholder too, but probably shouldn't be use this way, but it can. 
The placeholder attributes also suports the debug parameter so as to provide detailed information in the log for admins to help diagnose possible issues when testing different settings.


* **v3.2.4-alpha1 2020-12-26**
Setup the alpha release.


# V3.2.3 2020-12-25 
**Merry Christmas!!**
Release of next bug update.



# V3.2.2 Release - 2020-11-21


