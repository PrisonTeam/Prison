[Prison Documents - Table of Contents](docs/prison_docs_000_toc.md)

## Prison Build Logs for v3.2.x

Greetings!  I'm delighted that you are interested in the build logs for the
Prison plugin.  I'm wanting to provide a more formal documentation as to what 
is going on in each build so you have a better idea if it may be something 
that you need.

## tag 3.2.3-alpha.10 2020-12-18


* **Changed the MineBlockEvent to have an inline mode and sync mode to run the given commands.**
Changed around the save and load for the BlockEvent to support these changes.**


* **Added the ability to remove a ladder from a player** 
using /ranks set rank help using the rank "name" of -remove-.


* **3.2.3-alpha.10 2020-12-14**


* **Bug fix: BlockEvents were not being submitted** unless there was more than one command that was being ran.


* **For the BlockEvent commands, disabled async mode**
since the it will submit an async task to run in the future, then it will try to run a sync dispatch command and will fail.  So impossible to run commands async.


* **Added a new feature to this config: a List of String values.**
Appears to work, but not yet hooked up to anything that would use it yet.  Needs more testing too.
The intended purpose of this is to provide dynamic blocks lists for auto features.


* **Added SellAllAuto per-User toggleable**: the instructions to use this feature are on the SellAll Guidebook,
  the user will be able to enable or disable the sellAll auto on its own by running the command `/sellall auto toggle` if
  this option's enabled in the `SellAllConfig.yml`.


* **3.2.3-alpha.9 2020-12-12**


* **To fix the reported issue of smelt and blocking always being performed when auto pickup is enabled**, changed the hasPermission to isPermissionSet so OPs will not just blindly trigger these features.


* **Some adjustments to the BlockEvent task submission.**


* **Change MineBlockEvents to submitted so they do not run in the same process as the block break event.**
It was causing lag, and now its much better.
Also added async submission.  And the player can add more than one command per task.


* **Issues with ItemMeta not working correctly with auto features.**
Need to do more testing to see if this works, but initial tests are not looking too good.


* **Bug Fix: fixed issue with calculating and applying the durability.**
Turned out there were a few issues, but mostly after the durability was set, the itemMeta was not being resaved to the tool.


* **3.2.3-alpha.9 2020-12-08**  Version increase.


* **Broadcast rankup messages changes:**
Disabled white text by default of prison Broadcast for now, they'll be again available in the future when
  we'll update the lang support.


* **Start to setup the ability to use auto features outside of the mines.**
It's not fully enabled yet, so this won't work if it is tried to be used.


* **Able to dynamically toggle a debug mode to dynamically control logging at the debug level.**


* **Had a few issues with players modifying the contents of the save files for ranks, mines, etc, and the result was invalid json which would fail to load.**
These failure would not normally be encountered if using Prison's commands.  Due to amount of time chasing down bugs that don't exist, I added this catch statement to catch malformed json and then generate an error in the console.


* **Fixed the links to the add and remove blockEvent commands.**
Had a space in there from an earlier version.


* **Bug with the saving of the world name** Parenthesis were not used correctly.



* **Fixed an issue with the lore being "set" when it really wasn't.**
This was forcing the auto block and auto smelt to be used, and also probably auto pickup too.


* **Corrected a typo in an add message on the add BlockEvent**


* **SellAll fix** 
Fixed the sellAll sell bug, it wasn't updating the configs when it should've, now it'll update (even if less efficiently than
before) and it's working.


* **3.2.3-alpha.8 - 2020-12-08**


* **New Feature: Mine BlockEvent Commands!**  
Now define events to run on block break events within a mine.  Each BlockEvent has a chance and an optional permission.  The chance is based upon a range between 0.0000 and 100.0.  One command will be chosen out of many that may be defined for the mine.  If a TokenEnchant explosion event is handled, then for each block broken it will try for a BlockEvent, so an explosion event could produce multiple BlockEvents.  These commands are able to use {player} and {player_uid} within the commands.


* **3.2.3-alpha.7 - 2020-12-07**


* **Bug fix: Fixes a bug that prevents comparison between ItemStack and SpigotItemStack objects, which should be equalable.**
One of the side effects of this bug is the failure of the selection wand to be used since it was not able to  able to identify that it is actually a selection wand.


* **A strange error was found where a virtual mine has its area set, that it fails to load when restarting the server.**
This situation appeared to be related to the world name not being correctly saved and as such, when reloading the mine, it would result in a failure to initialize the mine.  Actions were taken to try to ensure that if the mine is corrupted in memory, then it is still able to be saved correctly so upon loading it is no longer corrupted.


* **Found an issue that when a custom blocks plugin is added, used, then the plugin is removed while the blocks are still defined in the mines.** 
It was resulting in nulls for the integration handling.
This deals better with the loss of the integration so it does not cause problems.


* **Fixed a few issues with blocks returning null PrisonBlocks, or at least deal with the null blocks better.**


* **Attempts to better handle exceptions when running the commands.**
So far, without being able to produce many failures, it does not appear like this is capturing any exceptions? Will have to wait and see if it is able to provide better error messages.


* **Major changes to the Auto Manager to remove as much bukkit/spigot specific code as possible**
since that will be limiting support for custom blocks.  This is the first phase of changes, and the change to support custom blocks will come later.
Due to the major changes that occurred, there could be bugs so an alpha release should not be generated without more testing.


* **Changes to some of the functions in the compatibility classes to prepare for changes with auto manager.**


* **Additional functionality and changes in the way block related things are used and processed.**
This is preparing for changes to auto manager.


* **SellAll Material support**
This should make sellall support all material, from legacy to 1.16 (all XMaterials).
  
* **SellAll signs**
Added the ability to make SellAll signs. 
The **sign** should have the **first line** as `[SellAll]` to make this works, also when clicked it's
  Just a shortcut to the **/sellall sell** command, very simple (also to use the sign you must RIGHT CLICK IT).
    - To create a sign you need to have the permission`prison.sign`.
This feature needs to be enabled from the `config.yml`
by editing or adding (if missing you should add this line manually or delete the config, then
restart the server) this like from:
`sellall-sign: false` to `sellall-sign: true`.
It's possible to edit the tag of the sign when generated from the `config.yml` by editing this:
  - `sellall-sign-visible-tag: "&7[&3SellAll&7]"`
  
* **SellAll AutoSell**
Added the ability to enable the autoSell, autosell's a feature (also needs autoFeatures to be enabled and AutoPickup) with
  the goal of triggering the SellAll command when the player inventory's full.
  You can enable this feature from the SellAllConfig.yml and by default's disabled, to `enable` it
  just turn edit this under the Options config section from `Full_Inv_AutoSell: false` to `Full_Inv_AutoSell: true`.
  It's also possible to enable or disable a notification telling the player that the AutoSell got triggered, by default
  it's under the Options section sellAllConfig.yml section like this: `Full_Inv_AutoSell_Notification: true`, to disable it
  just turn it to `false`, you can also edit this message by editing the Messages en_US.yml at this path `/module_conf/lang/`,
  in other worlds you can find the messages config en_US.yml in the Prison folder, then open the module_cong and the lang
  folders, the line to edit's under the `Message` section with the name of `SellAllAutoSell`.

## tag v3.2.3-alpha.6 - 2020-12-04


* **Setup modules to have a deferredStartup() function.**
This allows code that is dependent upon other integrations to be ran after the integrations are fully setup.
Mines have to load after the integrations are loaded due to the new block model and specifically the new support for custom blocks, which means all custom block integrations must be loaded before anything tries to use the blocks, such as loading of mines.
Placeholders have to be reloaded after the deferredStartup() function is finished.



* **Disable unused code dealing with MinesPlayer and the auto pickup and smelt and block within the Mines module.**



* **Setup the file I/O to be specifically UTF-8 enabled.**
The old file I/O would default to the platform defaults, which may not have been UTF-8.  This code has been tested and works with UTF-8 encoded characters, although Prison is unable to display them.  It is unclear if this is a limitation of Prison or Spigot.  This has been tested with Spigot v1.8.8, v1.12.2, and v1.13.3.  The test consisted of directly adding a UTF-8 character to a rank save file with a utf-8 compliant editor (not WordPad), starting the server, making a change to the rank to force a save with the new data, then inspecting the contents of the new rank file to confirm that the utf-8 character is still encoded as utf-8.  All tests were successful.  Future work will be done to see if there are any other ways to enable it, but for now, at least the file I/O has been fixed and proven to be good and working.


* **v3.2.2-alpha.6 - 2020-12-01**


* **Added UUID to the player look up options which hits the bukkit offline players.**


* **Added a logPlain function to the platform that bypasses color code translations** and removal.  Used for debugging situations.


* **Minor changes to address some issues with the new block model.**
For MineReset.resetSynchonouslyInternal() the way the Block is created to a variable now, eliminates a lot overhead since it was being called multiple times, of which it has to go through the construction of that object each time.  So it should be better performance this way.


* **More updates to better support block break events for custom blocks.**
Not yet hooked up, but getting there.


* **Changed the name of a block from pillar_quartz_block to quartz_pillar so it would be supported in both XMaterial and the old prison's BlockTypes.**


* **Fix the way DoubleArgumentHandler and DoubleClassArgumentHandler deals with replacement of the $ and % symbols.**
Needed to use replaceAll() instead of replace().


* **Updates to the /sellall command** 
Hooked /sellall up to the prison command handler.


* **Expanded the command handlers to include more types** to allow a greater variety of parameter types on all prison commands.


* **v3.2.2-alpha.5 - 2020-11-29**

* **Bug fix: Tried to reset a mine with over 400,000 blocks**
and ran in to issues.  Had to set a delay of one tick when resubmitting the next page of blocks.  Works fine now, but the one tick delay does add to the overall time, which is not bad considering.


* **More enhancements to hook up custom blocks and enabling the new block model.**
Have them loading and being added to the PrisonBlockTypes as valid blocks.  Also hooked up to be used with block search and can be assigned to the mines.  These custom blocks are also being saved.


* **Changed the offset time from seconds to milliseconds.**
Enabled the config.yml setting prison-mines-reset-gap. This will allow better tuning of the mine gaps, but could cause server lag problems if the gap is too small.


* **Changed Pgd to Paged so it makes more sense in /mines list.**


* **Clean up placeholders list.**
Very few placeholders are able to be shown on the same line as another.  So eliminate the code that is trying to add to same line.  This also makes it more difficult to find a placeholder.


* **Fixed an issue with the logic statement when using RankPlayers for substitution of offline players**


* **Updated a few documents**
Added a document on setting up Vault.  Added more information on how to reload the placeholders in a few documents.  Added a few screen prints.


* **Prison's placeholders are now fully registered on PlaceholderAPI's wiki**
Prison still dynamically registers its plugins upon startup because everyone has to be generated and expanded.


* **Bug fix: Found a couple of placeholders were mis-configured.  **


* **Upgrade Cryptomorin XSeries to v7.6.0.0.1**


* **v3.2.3-alpha.4 - 2020-11-27**


* **Bug Fix: Fixed a typo on enabling prestiges based upon the config.yml setting.**


* **For prestige this gets rid of the requirement that perms exist for the player to use prestige**
the rankup on the prestige ladder.  All that is needed is to have prestige enabled in the config.yml


* **Simplify some of the rankup code so it's directly using rankNext and rankPrior.** 
Get rid of some optionals to simplify the code too.


* **Significant change: RankPlayer now extends from the Player interface.**
This allows using RankPlayers when cannot get an OfflinePlayer so admins can perform more function on players when they are offline.  For example promote and demote.
This resulted in some major internal changes on how RankPlayers are tracked and managed.


* **Rank up commands were using a misleading generic RANKUP_FAILURE**
which had an associate error message that was misleading and was reporting that there was a failure with reading or writing data and that the files may be corrupt.
Such a situation was not true.  Added other error messages to better explain the situation.


* **Hook up the new Custom Items integrations and tie them in to the prison new block model**
so if the the Custom Items plugin is active, it will automatically add the custom blocks to the available list of block types. 
This is working and the custom blocks show up in the block search.


* **Setup a new Custom Block integration type and added CustomItems integration.**



* **v3.2.3-alpha.3 - 2020-11-23**


* **Fixes to sellall**
Optimization of sellAll and fix of unloaded configs when updated. Fixed configs when edited didn't load with the updated one.


* **v3.2.2-alpha.2 - 2020-11-23**
Bump version because of a lot of improvements to the new block model. It actually appears to be working very well.  Still need to do more testing and will not enable by default for a while.  It will have to be an opt in feature for now.


* **Found a typo in the old block model for weeping_vines** so it was not being recognized.  It's working now.

* **Some fixes to get more of the new block model functional.**
Added some support to help fall back to the correct XMaterial for a few block types such as WATER and STATIONARY_WATER which is not mapping for 1.8.8 because of the data value representing the flow height, but yet, XMaterial is trying to map it to an resource using data (it shouldn't though).


* **For the new prison block model, when dealing with PrisonBlocks**, it needs to record invalid block types so they can be logged and hopefully fixed.


* **Setup more of the new prison block model to work with paged mine resets.**
Sharing the MineTargetBlock behavior through the use of MineTargetPrisonBlock to reduce complexity and reuse existing structures. Still a work in progress.


* **Bug fix: Fix an NPE with /sellall**
Remove of many NPE Objects and new messages for sellAll.  New messages added for sellAll in the messageConfig config


* **v3.2.3-alpha.1 2020-11-22**


* **Bug fix: Allows blocks to be added to mines now.**
Thought for sure I removed this code before.  Not sure what happened, but must have missed testing with adding blocks. :(
Will re-release the v3.2.2 version.


* **V3.2.2 Release - 2020-11-21**
This version is being released to fix outstanding bugs and to provide a better end user experience.  Preparing for a few other changes, so want to get this released before possibly changing the stability of any feature.


* **v3.2.2-alpha.14 - 2020-11-21**
Bump version... preparing for v3.2.2 bug fix.


* **Refactored where the commands that are outside of the modules are registered.**
They are now registered within the function where the modules are registered to allow the related commands to either be registered or not, based upon the modules.
Refactored how some of the commands/configs are setup to be more flexible, or to better follow the standards of how prison has been instantiating them (ListenersPrisonManager).


* **Split out the gui ranks and gui mines commands** from the main spigot gui commands. This allows them to be enabled on their own, or more importantly, prevent them from being registered if the modules are not loaded.



* **Changes to the gui commands to prevent console from running a few gui commands.**
Also remove the alias of "prisonmanager" for "gui".  
It was reported that when a non-admin tried to use /mines that it was giving the error message associated with /gui.  Cannot see a direct relationship there, but hard linked /mines to /gui mines by calling the function directly instead of resubmitting the command.  Not sure if this fixes the issue, but I was unable to reproduce it.


* **Minor changes**
Add more to the BaseCommands and start to hook them up. Fix usage of getting the current economy. 


* **To prevent prestige related commands from being registered with bukkit**, 
pulled prestige related commands in to their own command class and then conditionally register them if prestiges are enabled. Cleaned up a few other things such as remove use of deprecated functions and 
moving the implementation of isPrisonConfig to SpigotPrison to simplify a few things and eliminate duplication.


* **Fixed a problem with bukkit on a paper v1.16.4 server where the Bukkit.getOfflinePlayers() was returning either a null player, or a player had a name that was null.**


* **v3.2.2-alpha.13 - 2020-11-15**


* **Updates to the GUI: many.** 
Moving configs to a common package to better manage them, or to prepare to merge them in the future. Many other fixes and enhancements.


* **Updated the SpigotMineBlockPercentageGUI to include a Close button and to show the selected block top and center.**
Also provided links back to the block list gui.  Setup the parameters to return back by setting the font color to black so it is visible that they exist as the players hover.


* **Fixed issue with GUI block list.**
Using a combination of XMaterial and ItemStacks, its able to display the viewable blocks. Added Prior button to go back to prior page.  Got the Next page button working (it was incorrectly just blindly deleting the first two characters of the button name; changed it to strip color so it is not destructive.  Setup this page to be able to return to it from other pages. Confirmed that this works with spigot v1.8.8 and spigot v1.16.3.


* **Fix issue with GUI not being able to display red or lime stained glass panes**
due to use of material instead of ItemStacks.  This applies to mc v1.8 through mc v1.12 and they display as plain glass panes (no colors).


* **Added a SpigotPrison function to strip all colors from text.**
Needed in the GUI to hide extra parameters.


* **GUI direct support for ItemStacks when creating buttons for mc v1.8 through mc v1.12. **
With the use of magic numbers with Minecraft versions less than 1.13 the use of Materail to create ItemStacks fails to get the correct type if magic numbers are involved.  Created a new createButton function to work directly with item stacks so the proper blocks can be used with mc v1.8 through v1.12.


* **Additions to PrisonBlock handlers to provide more utility functions**
and to solve a few complex challenges.  Removed NULL_BLOCK from the valid block lists.


* **Found function names that started with capital letters and changed them to lower case.**
Function names should never be capitalized since that would imply they are classes, or similar objects, and not functions.


* **Compile error with the removal of the prison core gui...**
Removal of this code was forgotten when removing the prison core gui code.  Not sure how that passed the compiler before committing to git?


* **cleaned up unused imports in the gui code; were causing compile warnings.**


* **New feature! Hooked the prison GUI up to the new prison command manager.**
Assigned aliases so as to preserve backwards compatibility with admins who are used to the prisonmanager command.  
The /prisonmanager command has been replaced with just /gui.  Tested and appears to be working well.  Can do /gui mines, /gui ranks, /gui prestige, /gui prestiges.  


* **Added /mtp as an alias to /mines tp.**


* **New GUI config system**
- It's an improvement. Has many code changes
- Deleted the GuiListener.java class, only SpigotPlatform was using it so nothing should break.


* **New Feature!  Tab complete is now functional with prison's command handler.**
When typing in prison related commands, you can now press tab to complete the typing for you if there was only one option available, or it will fill in common letters until you need to make a choice.  Also typing in a command pressing space then tab shows all available options.  In game is slightly different that in console, where in game show a ghosting of the command where you are typing so tabbing will select that option.
Works on spigot 1.8 through 1.16.x. Also works in console.  Functional with aliases too.


* **New Feature! Command Aliases!**
Add the complexities of supporting aliases in the prison command handler.  Each command can have one or more aliases mapped to almost any level of paths.
This also includes a rich support of the sub-command and help listings to better identify which commands are aliases and also what aliases are available. There is room for enhancements that will be added soon.


* **v3.2.2-alpha.12 - 2020-11-10**


* **removed the trailing &f from the rank tag**
This was within the new feature /ranks autoConfigure.  It was reported that there were issues within the plugin Scoreboard-r by RienBijl that data was being truncated and lost.  Looking in to the issue it was found that there was a stray &f at the end of a tag.  It had no impact, but it was removed anyway since it does nothing.  It was determined that the scoreboard-r plugin is buggy and was causing errors.


* **New Feature: Now provides the capture of the actual label that a command is registered with Bukkit when there is a conflict.**  The prison Command Handler now uses the registered label when displaying any of the sub commands or list of all registered root commands.  This will allow the users to know what commands they actually have to enter to get them to work, instead of guessing when there is a conflict.


* **Improve block matching for pre mc v1.13.0** 
For the 1.8.x material types in prison, there exists different states with the data value that could result in block types that are unknown.  Some of it may be orientation or degree of flowing water, or even wetness of soil. I've seen it with leaves of different shades, or even with logs.
The idea here to fix this issue is not so much that we don't know the block type as much as it shouldn't matter the slight variations in the data field.  Therefore if we fail to match on the id and data, then go off of the material name.  That's a good fallback.


* **Bug fix: Fix incorrect display of no other mines near for /mines whereami**
If the player was standing in a mine and there are no other mines around, it used to show the mine they are in, plus say there are no other mines within 150 blocks.
Now it will not show the "no other mines in 150 blocks" message.


* **Mines Blocks GUI Fix**


* **Bug Fix: Found a bug in the command registration code** that could result in failing to properly register commands. This would have been an issue if there were upper case letters in a command, since all commands are converted to lowercase when added, but when checking to see if a subcommand was already processed (ie... the "set" in the following two commands:  /mines set tag, /mines set resetTime).  The symptoms would be missing commands at runtime.  I actually have seen this failure in the past, and realized that all commands should be entered as lowercase due to this error.  Now it should work correctly.


* **Clarify the role of a CommandHandler field that is used in a situation of when there is a command collision.**


* **Fix typo: In the /mines command add function, a & was placed one character to the right of where it should have been.**


* **Added an unregister all for the commands and hooked it up on the plugin's onDisable.**


* **Fixed issue with dropping of inventory.**
Had a ! where it shouldn't have been and forgot to hook up the new messageId variable so the warning can change.
I'm not so sure about messaging this way, using the action bar, but don't want to flood chat with a ton of messages either.  Would have to put a limiter on the chat messages?


* **Fixed an index out of range issue in the gui.**  
Was 45 when should have been 44.


* **New Feature: Added XP calculations to the block break (auto pickup) function**
which can be disabled.  Give the option to drop the xp as orbs (default) or give it directly to the player with no orbs.


* **It was realized that dropItemsIfInventoryIsFull was not hooked up.**
Hooked it up.


* **Update some docs and added a few screen prints.**
Updates to a few documents to reflect some of the more recent updates to prison.


* **Updates to the IntegrationManager** so the variable is more consistent and especially the message for WorldGuard integration is clear that it is not an error that it is not yet active.


* **Change the command /prison alerts so they can be ran from the console** since it made no sense why the console was locked out from using them.
Slight changed the information for /prison gui that shows that it could be preferred to configure the autofeatures.


* **Changed the perms to lower case, specifically the mine/rank name.  Should have been lower case.**


* **Had the wrong block name for dark_oak_planks (thought I fixed that already).**


* **v3.2.2-alpha.11 - 2020-10-29**


* **For /ranks autoConfigure: Almost forgot to add the removal of the mines.tp.<mineName> when demoting a player.**


* **Added a new feature: can now set the area of a mine based upon location of your feet.**
This allows you to create a virtual mine, then set its location where you're standing or flying, then you can resize it.  This bypasses the need of having to use a wand to create a mine or define it's size and location and allows it to be defined in mid air or in a void world where you cannot click on any blocks.


* **Fix the report on how many blocks are in a new mine.** It was reporting on surface area and not block count.
Fixed an issue with world being saved correctly. When a virtual mine was converted to a real mine, the world and world name were not always being updated.  Fixed it by not only being more aggressive when setting the Bounds, but also when saving the mine.


* **Reenable the compatibility cache on block mapping.**
This eliminates related failures to map blocks, which will prevent wasted time continuously looking them up.


* **A few more tweaks to the block types to fix missing block from a pattern.**


* **Added the ability to add the "force" option to the /mines set liner command.**
Normally the command only adds the liner if there is not air so that the mine's liner does not extend above the ground level, and it ends at ground level.  
Force is intended to cause the liner to work in a void area where all there is only air.  Otherwise you would have to place blocks on the outside to "trick" the liner to work.


* **Fixed the block names for the new liner patterns.**
I accidentally added them based upon actual block names and not what is mapped to the prison block names or the new block types which are keyed to XMaterials.  This fixed them.


* **Added a compatibility reference to help map it to XMaterials.** 
Jungle_planks


* **Enhance the ranks auto configure by adding support for GroupManager permission plugin.**
Also added the remove permission of the next higher rank so as to support demotions.


* **Added three new patterns to the MineLinerBuilder.**


* **Minor code improvements for the player GUI and this should also fix the null value.**


* **The gui was trying to pass null strings to this format function.**
If the parameter value is null, then just return an empty String, otherwise try to format it.


* **Simplify the error message if a player does not have access to tp to a mine**
they should not be seeing anything pertaining to perms.


* **Hooking up more of the prison's mines commands to properly, and fully, use the new prison block model.**
The new PrisonBlocks are now being validated against the dynamic list of valid blocks that are available on the server that is running prison.  So within the block search, as one example, it will only show valid blocks that can be used; the old block model would show all possible blocks within prison, some of which may not have been blocks.


* **Enhancements for Prison's new block model**
Enhance the PrisonBlock to use block instead of mineable for better consistency with bukkit and spigot use of blocks.  Also set block name to be always lower case for easier searches.
PrisonBlockTypes has been enhanced to be able to search for blocks by name.  This is using a b-tree for quicker retrieval instead of loop though all available blocks.
When valid blocks are added to PrisonBlockTypes (the valid blocks that exist on that server) they are have their isValid and isBlock values set to true.  Also now using XMaterial names for better long term consistency that will not change if the admins upgrade or down grade their servers.


* **Hook PrisonBlockTypes up to the Prison object.**
This will load a list of new block types at server startup that have been validated against the version of minecraft that is running.  This will ensure that the only blocks that the player will see listed in block searches, as one example, will be blocks they can actually use.  This is highly dynamic based upon the server and not the complier.
This is preparing for the use of the new block model by giving access to valid blocks within many of the /mine related commands.


* **Remove more of the Items related code** which has not been used for awhile.  ItemManager and troubleshooters related to that.


* **Reenabled the caching of null values when translating from one material type to another.**
This prevents looking up a failed code many times and only allows one error message to be logged to the console.


* **v3.2.2-alpha.10 - 2020-10-26**
Version bump due to the significance of the last bug fix.


* **Major bug fix: Eliminated the in prison caching of players.**
This collection was not able to deal with players reconnecting to the server, which would give them new player objects.  The issue was that prison would have an obsolete (zombie) copy of the player and their inventory. So if any operations would be performed on the inventory, such as giving a selection wand, it would be placed in the orphaned object and the player would never get it.  The only way to "fix" this issue would be to restart the server and then it would fail once they would log out and reconnect.
This was confirmed a problem with 1.9.2, 1.13.4, 1.15.2, and 1.16.3.  It was not an issue with spigot v1.8.8.  This did not impact players retrieved from events.
This fixes the issue by removing prison caching of the players.  Now all instances of the players being used within prison are now live bukkit objects.
I suspect this issue has been within prison for a long time.  Not sure why no one reported it before, or if they did, it was not clear how to reproduce this issue and the ones reporting it may not have been able to provide enough information to reproduce it.  The way it is reproducable is to login, get a /mines wand or have prison interact with the player's inventory, then log out and then back in. Then /mines wand will fail to place it in the real player's inventory.  It will only go in to the prison cache.


* **v3.2.2-alpha.9e - 2020-10-26**

* **Bug fix: When a world is not available upon startup** it will try to set the boundaries for the mine.  In doing so, it will try to extract the correct world to enable it and to remove the virtual status if it was set.  If the world has not yet been loaded (ie... if you're using multiverse) then this was causing an error for that mine.  This now will only mark the mine as disabled and allow the multiverse plugin to trigger the completion of the mine loading event for that mine (as it has been doing before virtual mines were added).
If a mine has the boundaries set and if it was disabled or virtual, then make it a real mine.  Have checks to ensure the world is available, if not, then disable the mine.


* **v3.2.2-alpha.9d - 2020-10-26**

* **New feature: Dump player's inventory to console**
This is useful to check status of various prison related functions.
One area that this maybe useful with some players reporting that they are getting a Prison Selection Wand, but it's not showing up in their inventory, although inspection of their inventory is showing they are getting it.


* **v3.2.2-alpha.9c - 2020-10-26**


* **Fix to GUIs and Close button conflict**
Fix to Mines GUI lore issues for Virtual Mines


* **v3.2.2-alpha.9b - 2020-10-26**


* **Fixes issue with not enabling a virtual mines**
when the area is set with the command: /mines set area help


* **Fixes to some blocks and use of XMaterial for STAINED_GLASS** 
This allows for multi-version support since material names have changed at 1.13. This supports 1.8 through 1.16 now. This fixes an exception for versions less than 1.13.


* **Fixes to config, now you'll not see any "§" but only "&"**
There might be some strings that won't translate that, If you spot them (like lores with an & instead of color), please report it.  This fixes an issue for some user's environment not being able to properly translate the `§` when generating the GUI config files and language files.  Basically the file's UTF-8 encoding was being treated as ASCII and that character was being converted to a hex value that could not be translated back to something usable.


* **v3.2.2-alpha.9a - 2020-10-26**


* **Enable OPs to tp players even from console.**
Also if the player who is running the command is the same as the name being passed as a parameter, then allow it.


* **v3.2.2-alpha.9 - 2020-10-26**


* **Added the ability to move a mine but am not enabling it.**
It is not behaving as well as it should.  The tracer is being left behind when it shouldn't.
Other adjustments to the sizing and liner functions have been made too.


* **Adjustments to getting the MineLinerBuilder working.**
These here are minor changes that makes a few aspects work slightly better.


* **Change the way the caching is working on block mappings.**
Removed the caching of no-hits on block conversions.  Normally recording the no-hit conditions will result in significant performance improvements, but removed them for now to make sure they are not causing issues.  Will have to reenable them (rewrite the code) in the future.


* **Provided more detailed reports on missing block types**
from spigot mapping to prison's old block types.  Some of these will be needed until we can get the new block model fully functional.


* **Added a few new block types for v1.13.**
The major one that fixed some significant issues was GRASS_BLOCK since it used to be named GRASS in older versions of minecraft, but now GRASS is just the plant.
Also added 18 new log types for v1.13 and newer.

* **Removed traces of the enableMineTracer within the Platform object.**


* **More improvements to the /mines set size and the /mines set liner commands.**
Added ladders to the liner, and also added repair to undo the liner based upon the surrounding blocks.  Appears to be working well for spigot 1.8.8.


* **Rank Tags modified to accept spaces**
To match the capabilities of the Mine Tag, the rank tag was adjusted to allow spaces.  The command /ranks set tag was also changed to remove the tag if desired.  Also changed the /ranks create command to accept spaces in the tag name too.


* **v3.2.2-alpha.8 - 2020-10-22**


* **New Feature: Now able to line a mine based upon the selection of edges and patterns.**
This feature helps players to get up and running their prison a lot faster.
Many additional patterns can be added in the future.  Supports 2d patterns that are from 1x1 to any larger size.


* **Bug fix? Needed to add TokenEnchant to the softdepend**
to prevent a java.lang.NoClassDefFoundError with the class com/vk2gpz/vklib/logging/ColorConsoleLogger.
Not really sure if I can call this a Prison bug since it appears to originate from within the TE API code base.  But this works around their potential short comings.


* **Bug fix!  Trying to format an already formatted item... oof!!**
This had everything to do with formatted currency amounts in placeholders.


* **Disabled the loading of the /items.csv file**
since that is obsolete and not working anymore.  The items.csv file has been removed from the project since people were thinking they can just modify that file to add custom blocks.  Nope...


* **Add logging to count how many blaze rods the player has before and after issuing the /mines wand command.**
there have been a few reports that it does not work, but I cannot reproduce the error.  So this is step one in confirming if they actually get the blaze rod or not.
One possibility could be that another plugin is canceling the event so the player never gets the blaze rod.


* **v3.2.2-alpha.7b - 2020-10-21**


* **Temp pulled alpha.7** Someone said the were getting a lot of errors with this release. Hence the past two fixes.


* **Fix potential issue where the user tries to use a % % as escape characters when they should be using { } instead.**
It was causing a failure when trying to redisplay the text as for the % was trying to be used as a placeholder when fed through the String.format command.


* **New feature: /mines set size**
Can now adjust the mine size by specifying the edge and adjustment amount. Edges are top, bottom, north, south, east, west, and walls.
When adjusting the size, it automatically goes in to tracer mode so the mine's dimensions are easily seen.


* **v3.2.2-alpha.7 - 2020-10-21**


* **Created the code to add blocks to virtual mines when they are being generated with the /ranks autoConfigure command.**
Blocks are setup in a List with the least valuable to the most valuable.  Then it's a sliding window of selecting blocks from the lowest mine to the highest ranked mine.  The percentage per blocks are 5, 10, 20, 20, 20, 25 where the most valuable are least represented.


* **Added a few new blocks to prison's old block model.**
These are actually duplicates of what already exists, but these are instead named to mirror XMaterial names.  The reason for this is to ensure consistency between the two block models used for testing.
Removed the items.csv document since it is not being used anymore and will only cause confusion if admins think they can add new blocks through that file.



* **Added some internal reporting of the data contents.**
Most of this will be used with logging in the /ranks autoConfigure and also jUnit testing.


* **Error logging needs to throw these stack traces** since thats really the only way to get the details we need to fix the problem.
A throw was eliminated, but needs to be added back.


* **New Feature: Added 12 new placeholders including aliases.**
Added formatted placeholders for player's costs.  Added a new placeholder for player balance.
Created a PlaceholdersUtil class to perform some common functions, such as formatting an amount to include a metric prefix.


* **tag v3.2.2-alpha.6a - 2020-10-19**

* **Fix issue with mine name** 
that is related to virtual mines if not creating a virtual mine.  An extra space was added to the end of the mine name which was triggering an error message about spaces in mine names.


* **Add a second perm to allow mines.tp.<mineName>**
to be added to each rank that is auto generated.
will provide support soon for EssentialsX warp...


* **New Feature: Allow the user to specify the material name in the GUI config files to use for a mine's block type.**
The material names are based upon XMaterial for consistancy throughout all versions of spigot/minecraft.
The format is: Options.Mines.MaterialType.<MineName>



* **New feature: produce a warning when the first parameter of any prison command is not a CommandSender.**
Parameters using Player has been causing stack traces under different circumstances because the prison command handler ALWAYS passes a CommandSender object as the first parameter, therefore that is the type it needs to be to prevent a method type mismatch exception.  The new changes will provide a warning when starting prison; it should ideally be caught at compile time but it can't.



* **tag v3.2.2-alpha.6 - 2020-10-19** Bump the version due to significant changes.
It needs more testing prior to being released to the alpha channel.


* **Bug fix: prevent the config files for gui and it's messages from loading from the file every single time it is accessed.**
And they were accessed everywhere, and for one message, even three to four times just to generate one message.
This should improve performance significantly for the gui overall.


* **Provide a way to get the counts of a given ModuleElementType.**
Allows for access in modules that don't have direct access to other modules.


* **Wrapping up changes on virtual mines**
Fixes issues with virtual mines. Allows full configurations of virtual mines except for setting the area and spawn point.


* **Bug Fix: Found the wrong parameter was being used on a few commands**
Fixes some hard to find problems where the wrong parameter was being supplied to the commands.  It would work most of the time, but under some conditions it would fail.  Was using a parameter of Player instead of CommandSender. 


* **Added a logCore feature that does not try to translate colors...**
this helps to bypass exceptions if an exception is trapped.
Added a dump when parameters on messages are not properly paired.  This will be critical when all messages will be externalized and subject to users messing up the formatting placeholders.


* **New feature!  Auto generate Ranks and Mines!!**
This is based upon the work of Gabryaca, but I reproduced it to be a part of the RanksCommand and gave it the capability of generating virtual mines too!
This is actually a starting point of what it can become.  I not only added the creation of mines, but I also hooked it up to link the mines and ranks together as it generates them.


* **Added a new command to the Platform... This allows the creation of a Rank or Mine based upon a ModuleElement.**
That has major impact on added flexibility, and could lead to an automation of generating mines and ranks.


* **Storing the rank commands within the PrisonRanks class.**
The allows internal access to the commands so they can be used internally too.  Will be used for automation purposes.


* **Linked the various mine and rank commands to their respective managers**
so they can be easily accessed programmatically now.



* **New Feature! Virtual Mines!**
Now able to create virtual mines.  A virtual mine does not exist yet since it has no location, but you can configure all of the options first before setting the area with /prison set area.
This is phase 1 of this new feature.  Not fully tested yet. 
The intention is that when you auto configure all your ranks, it will also auto configure all your mines to go with those ranks.  Then you can go back and set the mine's area as you build them.


* **Links the rank command commands to the rank commands so they can be used together.**
Setup createRank to return a boolean to indicate if the rank was successfully created. This will allow programmatic internal use of createRank to automate more features such as rank configurations.


* **Add mine commands to add ranks to mines, and to remove ranks from mines.**



* **Added mines and ranks to the /mines info and /ranks info commands.**



* **Added to the platform the ability to link mines with ranks.**
Individual ranks or mines cannot perform this action, but going through the platform can.



* **New Feature! Added _/ranks setup_ and _/prisonmanager setup ranks_ commands.**
Now you can setup your default ranks in the default ladder with a command without
adding them manually for the first time, this command will add all Ranks from A to Z
and their rankupCommands executing the command that your permissions manager uses to add
a permission to the player to access to the `/mines tp <mineName>` command, the mineName
given by the permission have this format `mines.tp.<rankName>` so you should make your Mines
with the same name of the rank, you can edit the `mines.tp.` for to another you want, like
`essentials.warps.` in the _guiconfig.yml_.
The supported permissions managers for now are: 
- Ultra Permissions
- LuckPerms
- PermissionsEX
- zPermissions
- PowerfulPerms


## tag v3.2.2-alpha.5 - 2020-10-13


* **New Feature!! Added new 20 new blocks to the old block model.**
  v1.10.x:  structure_block, magma_block, bone_block
  v1.11.x: shulker_box - plus the 16 other colors that are available.
  

* **Bug fix: Prevent a NPE when no rank is assigned to the mine.**


* **v3.2.2-alpha.5c - 2020-10-13** Important bug fix.  Need to bump alpha version soon.


* **New Features! Added 48 v1.12 blocks and 44 v1.13 blocks!**
V1.12 blocks: 16 colored glazed terracotta, 16 colored concrete, 16 colored concrete powder.
v1.13 blocks: 10 coral types, 10 coral block types, 10 coral fan types, and 10 coral wall fan types, cave_air, void_air, blue_ice, and bubble_column.



* **Upgrade XSeries from v7.2.1 to v7.5.4**


* **New Feature!! Added the first Prison API components**
Started to add some api end points to make it easier to access some basic internals
without having to figure out how to conform to prison's restrictions.


* **Continuing work on linking mines and ranks...**
Mines and ranks are now being linked together upon server startup. 
Not finished.  There are still items to be added, like the mine and rank commands to work with adding and removing ranks and mines.



* **v3.2.2-alpha.5 - 2020-10-11**


* **Cleaned up and standardized log levels.**
Added PLAIN for use with mine resets, and DEBUG too.  Eliminated redundancies.


* **tag v3.2.2-alpha.4 - 2020-10-08**
Had to bump this to alpha.4 due to the new mine sortOrder being set to -1 if 
the value is not found in the save files! This may cause users to panic.


* **New Feature: Initial work in linking Mines and Ranks.**
This is just the initial framework for the final product.  Ranks have not been modified yet.


* **New Feature: New Feature: Add a warning if PlugMan is detected.**
It notifies the user that prison will not behave well and can be corrupted if PlugMan tries to reload it.
Also states we are not responsible for any corruption, nor are we obligated to help recover from said corruption.


* **New Feature: Added ability to send the ChatDisplay object directly to console log.**


* **New Feature: Added /ranks ladder moveRank**
since most people don't realize they can remove a rank from a ladder (it is not deleted) and then add it back in to another place within the same ladder, or another ladder.
This new feature just calls /ranks ladder remove and then /ranks ladder addRank.  Simplifies the process and makes it clear to the user that the option is there.


* **Eliminated the listing of placeholders from /prison version**
since it was getting to be a really long list.
Provided a reminder on where to find the placeholders.


* **BUG FIX!! Found that the default value on mine sortOrder was being set to -1 instead of zero.**
This will suppress all the mines, but nothing will be lost. This value will be set upon initial 
loading of the mines if they did not have that value set previously.


* **v3.2.2-alpha.3 - 2020-10-07**
Bump to v3.2.2-alpha.3 due to significant update to the Vault integration.  
Have not heard anyone else has had issues, so may hold off on releasing v3.2.2 for a few days to add more updates.



* **Bug fix: Had to make changes to which functions Vault is using based upon Vault's version.**
It appears like formerly deprecated functions have been disabled and does nothing now.
Made changes to inspect the version of vault that is being used, then properly target the correct function so it works properly with all versions of vault, including pre v1.4.0.
This bug fix is potentially a critical bug fix and may warrant publishing Prison release v3.2.2 to take care of this issue.


* **New feature: Added the player's current balance on the command /ranks player.**



* **Bug fix: Found that the wrong amount was being refunded to the player when doing a /ranks demote with a player_refund.**
Works now, and the log entries are also correct.


* **Space missing in the display of the default rank for the command /ranks list.**


* **Bug Fix: A config file was being loaded many times to build one gui page.**
Moved the config to a class variable so it would only be loaded once.
This reduced the opening of a 39 mine GUI from 5.5 seconds down to 6 milliseconds for the offending function call.


* **v3.2.2-alpha.2 - 2020-10-06**


* **Updated the sorting of mines to simplify the sorting.**
Now the sort types either include or exclude the mines.  There are no sort types that include all mines.  But the function that performs the sorting based upon the sort type returns a collection that contains the included mines (sortOrder >= 0) and also a second collection that contains the excluded mines (sortOrder == -1).
The returned object, PrisonSortableResults, has helper functions to simplify integration in to the /mines list command.
The /mines list command now shows how many mines are included and excluded in that listing, and identifies what the other sort types will display the suppressed mines.



* **New Feature: Setup the complex sorting on mines.**
Mines can now be assigned a sort order, with even suppressing mines from being included in the output.
Mines list can now be sorted in six different orders: alpha, active, and now sortOrder (user defined order), all of which suppresses mines with a -1 for the sort orders. Plus those but with including the suppressed mines.
The default being sortOrder, but if no mines have been configured, then they all will have a sortOrder == 0, and then all will be sorted alphabetically within that grouping.
Changed the prison's GUI to display mines in the sortOrder, with suppression of the -1 sortOrder values.
Added a junit test to test the generated sorting orders since they can be rather complex and should be tested at compile time that they are correct.
Note: In adding this new sorting, found where the bug was where the actual internal sort order was being altered.  This is no longer the case and is fixed.


* **New Feature: Added Mine Tag Names and 8 new placeholders to support them.**
This is required for the future changes to support linking mines and ranks.


* **New Feature: Rename Mines.  Bug Fix: Delete Mines.**
You can now rename mines.
This also fixes an issue with not being able to delete a mine: It deletes successfully, but is still active in memory.


* **Prison_v3.2.2-alpha.1d.jar - 2020-10-01**


* **Bug fix: change how /ranks list works with perms.**
Should allow all players to use this command since no-perm players can use /ranks and that just redirects to this command.
Removed admin features unless player has the ranks.list perm or they are op. The admin features are links to other internal commands.



* **Potential bug fix if a config option does not exist**
Reformatted so the code will have a better chance of fitting on the screen without a bunch of horizontal scrolling... 
plus got rid of a few instances of Objects.requireNonNull() which throws exceptions, which are not being caught.  Which is also the wrong behavior to what we need here... if those configs are null, then instead of throwing exceptions, just move on to the next conditional in the if chain.
Should probably never use Objects.requireNonNull since it will crash prison and prevent intended functionality.  This should probably be removed from elsewhere.



* **New feature: Gui Languages Support**
New languages folder containing the GUI and future languages files so players can edit them or contribute to make a new
one with translations to download and put there, then you can select it by editing in the `config.yml` the `default-language:`
to the language of your area, this's also related to the file language name,
for example the name of the GUI messages file should be `GUI-en_US.yml` by default, so if you translate to another language
(for example italian) you should rename a new file to `GUI-it_IT.yml` and edit in the config.yml the string 
`default-language: en_US` to `default-language: it_IT`, if you select a language file missing in the folder or invalid, 
it'll be generated anyway using the default config as the model so it won't break the plugin, then you can translate it 
later or edit the config to the correct one. 


* **Prison_v3.2.2-alpha.1c - 2020-10-10**


* **Found an inconsistency in how the ranks are dealing with the document engine.**
Mines do not throw exceptions, but they were within ranks, which is not needed.


* **New feature: added some new 1.14 and 1.15 blocks**
Added some more new blocks since some of the 1.16 blocks appear to work in most circumstances.
Use at your own risk.
These blocks may not properly be spawned and may not be all blocks available for these versions.
v1.14 : BAMBOO, BAMBOO_SAPLING, BARREL, BELL, CAMPFIRE, CARTOGRAPHY_TABLE, COMOSTER, 
FLETCHING_TABLE, GRINDSTONE, JIGSAW, LANTERN, LECTERN, LOOM, SCAFFOLDING,
SMITHING_TABLE, SMOKER, STONECUTTER, SWEET_BERRY_BUSH
v1.15 : BEE_NEST, BEEHIVE, HONEY_BLOCK, HONEYCOMB_BLOCK


* **Added a few v1.16 block types.**
Not sure if they will actually work.  Obviously will never work with mc versions < 1.16.  Use at own risk.
ANCIENT_DEBRIS CRYING_OBSIDIAN NETHER_GOLD_ORE BASALT POLISHED_BASALT
NETHERITE_BLOCK BLACKSTONE POLISHED_BLACKSTONE CHISELED_POLISHED_BLACKSTONE
NETHER_BRICKS RED_NETHER_BRICKS CRACKED_NETHER_BRICKS CHISELED_NETHER_BRICKS
CRIMSON_PLANKS WARPED_PLANKS STRIPPED_CRIMSON_HYPHAE
STRIPPED_WARPED_HYPHAE NETHER_WART_BLOCK WARPED_WART_BLOCK
LODESTONE QUARTZ_BRICKS RESPAWN_ANCHOR SHROOMLIGHT CAMPFIRE SOUL_CAMPFIRE
SOUL_LANTERN SOUL_TORCH SOUL_SOIL TARGET TWISTING_VINES WEAPING_VINES


* **Fix: Changed nether_brick to an item**
 since this is an item and not a block. Added support so if it is found in a mine upon server startup, it will change it to a double_nether_brick_slab.


* **Prison_v3.2.2-alpha.1b - 2020-09-29**


* **Bug fix: Prison was allowing the material REDSTONE to be added to mines.**
This caused a failure during mine resets since that is not a valid block. That is redstone dust!
Fixed it so upon server startup, it will auto detect the use of this item in mines and convert it to REDSTONE_ORE.  Thus the mine will work and won't disable the mine.
Also I changed the BlockType to Item so it's still there, but it cannot be presented as a block in the /mines block search tool.




* **Set new version to v3.2.2-alpha.1**  2020-09-27


## tag v3.2.1 - 2020-09-27

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


## tag v3.2.0 - 2019-12-03


* **Now works in 1.14.4!!**
Added in the missing library to get it to work with 1.14.4.


* **Fixed issue with pull request 132**
Fixes the https://github.com/PrisonTeam/Prison/pull/132 pull request.
It causes prison to fail on a clean server and throws a class not found exception.
The new library needs compile instead of compileOnly and it must also be shadowed.


* **Update to org.inventivetalent.spiget-update** Updated to version 1.4.2-SNAPSHOT since
the old version 1.4.0 was no longer available. 


* **Updates to bStats**  The Maven repository was updated and so was the version of
bStats that is being included on the project.  It is now using the current 1.5 version, 
was using 1.3 which is no longer available in the repository and was causing a build
failure for gradle.


* **New Feature - Decimals in Block chances now supported!** 
Added support to display and use two decimal positions on block chances. 


* **Fix - TP of Players out of the Mines upon Rest** 
This is one part of a multi-step process to help ensure players are TP'd out of mines upon
a reset.  In an effort to help start to address the
known bug [![Mine doesn't teleport to safety #82](https://github.com/PrisonTeam/Prison/issues/82)] 
Rewrote the player teleport function to help address the issue.


* **New Feature - Block search!!**
You can now search for blocks to add to the mines!  It uses the BlockTypes and is able
to search on the ID and the enumeration name.  It displays up to 10 at a times and 
provides Next Page and Previous Page controls to page through the results.
If you click on a BlockType, it will suggest the command for adding blocks:
** /mines block add <mine> <blockTypeSelected> %**  All the user needs to do is 
fill in the percent and change the "<mine>" to a valid mine name. 


* **Create Test plans**
To better ensure all test servers are tested consistently, some basic test plans have been
created to use during online testing. They are important for consistency when testing
all of the supported versions, especially to ensure nothing is skipped.  These will
evolve over time as needed.
Test server versions used in testing:
    * **Minecraft 1.8.8** 
    * **Minecraft 1.9.4** 
    * **Minecraft 1.10.2**
    * **Minecraft 1.11** 
    * **Minecraft 1.12.2** 
    * **Minecraft 1.13.2** 
    * **Minecraft 1.14.4** 


* **Major refactoring of the prison-mines package**
Took some time to refactori parts of the prison-mines package.
Eliminated duplication of source code, and repetitive calculations. Found a few
possible bugs and reworked the code improve performance and stability.


* **Found another possible cause for suffocation after a mine reset**
Identified what could have been yet another cause of some of the suffocation issues
during a mine reset.  It was not related so much to a bug or code, so it wouldn't
be something that could be detected with debugging.  What it appears to have been
is related to the speed in which the mines reset, or more exactly, the 
server lag or load.  If the player is TP'd and then the mines are slow at being reset,
then the player may fall back in to the mine where they would be suffocated.
To address this issue, after the mine is fully reset, it checks to see if anyone has 
fallen back in to the mine and will TP them back out to safety.


* **Update gson library from v1.7 to v1.8.6**
Updated library.


* **Removed the unused library com.fasterxml.jackson**
This library was not used anywhere so it was removed from the gradle configs.


* **Removed the unused library json-simple v1.1.1**
This is also an obsolete and archived project. Removed to reduce compile time
and bloat of the build.


* **TP Player if feet are one block below mine**
Technically they would not be standing within the mine, but since their head is within the
mine they could suffocate when the mine resets.  So include the layer below the mine
to determine if the player needs to be TP'd out.


* **Created a unit test for player being one block below the mine**
This is to ensure the player will be properly identified as being within the mine, although
they are standing one layer below the mine.


* **TP Player up one block higher**
The player needs to be TP'd one block higher if a spawn does not exist.
The former vertical target needed to be one block higher. This got lost
within the prior refactoring.


* **Prevent adding new blocks that have a chance of zero**
The user cannot add blocks to a mine if they provide a zero chance. 


* **Found a bug - Mines would not generate new random pattern**
Refactoring the mine generation code made obsolete a pre existing check for block
regeneration which prevented new fill patterns from being generated for the mines.
Removed the check and it resolved the issue.


* **New feature - Created RowComponent to group multiple FancyMessages on Same Line**
This now allows easy generation of more complex content where there can be
more than one FancyMessage (has hover text, suggests, and run capabilities when mousing)
per row.  This allows putting the two buttons for block search on the same 
chat row to reduce lines consumed.



* **Upgraded Gradle - Was at v4.4.1 - Upgraded to v4.10.3**
This project was using Gradle v4.4.1 and should be updated to v5.6.4 or 
even v6.0.1 which is the current latest release of Gradle.
But to do that it must be incrementally updated to each minor version to identify 
the future changes to gradle, such as deprecated features. As features are deprecated, they 
provide details on how to adjust your build scripts, but those hints are removed in future 
releases.

    * **Versions Upgraded To:**: **v4.4.1**, **v4.5.1**, **v4.6**, **v4.7**, **v4.8.1**, **v4.9**, **v4.10.3**, 
    * **Versions to be Upgraded To**: v5.0, v5.1.1, v5.2.1, v5.3.1, v5.4.1, v5.5.1, v5.6.4, v6.0, v6.0.1  
    * <code>gradlew wrapper --gradle-version 4.5.1</code> :: Sets the new wrapper version  
    * <code>gradlew --version</code> :: Will actually install the new version  
    * <code>gradlew build</code> :: Will build project with the new version to ensure all is good.  If build is good, then you can try to upgrade to the next version.
    * Gradle v4.10.3 version of the project's build scripts has a few v5.0 issues that 
  need to be addressed before upgrading any farther. Future upgrades will be performed
  after these issues are addressed.  ETA unknown.


* **Removed Guava Caching**
There were a number of reasons (about 18) for the removal of Guava caching.  Primarily
it really wasn't being used and the use case of the data does not warrant caching.
Removal of caching also helped to reduce memory consumption and reduce the overhead 
associated with the caching library.


* **Update the FileStorage Class**
Provided Java docs and also the logging of warnings.
Altered the behavior so it actually does not delete a FileDatabase but instead it virtually deletes it.  
This provides the user with a way to undelete something if they realize later they should not have 
deleted it  To undelete it, they would have to manually rename the underlying directory and then 
restart the server (but the data is not lost!).
Also cleaned up the nature of some of the code so the functions have one exit point.
Updated Storage so the functions are declared as public and so createDatabase and deleteDatabase returns 
a boolean value to indicate if it was successful so the code using these functions know if it should 
generate warnings.


* **Refactored the Virtual Delete**
I created an abstract class that has the virtual delete code in it so now anything that needs to use
that functionality can extend from that class.  I updated FileStorage so it now extends from
FileVirtualDelete.


* **Refactored FileStorage, FileCollection, and FileDatabase that were in the src/test packages**
I deleted these three source files from the prison-spigot module.  Not sure why they were there, but
simple refactoring made these obsolete.


* **Found and fixed a fatal bug in PrisonMines.enable()**
Within PrisonMines.enable() the errorManager was being instantiated AFTER initConfig but if there was a 
failure with initConfig, then it would have hit a Null Pointer Exception since that was what the value 
of errorManager was, a null since it was not initialized yet.


* **Created FileIO, JsonFileIO, and FileIOData**
Created a couple of classes and an interface to deal better with saving and loading of the data and 
to also better encapsulate the generation of the JSON files.  The MinesConfig class was the first 
class to get this hooked up with, which simplified a great deal of the code that deals with files.


* **Minor updates to PrisonMines class**
asyncGen is not being used right now, so commented it out to eliminate warnings.  Performance 
improvements may have made this obsolete.  May revisit in the future with actual mine 
resets instead of just precomputing.
Also clear the precomputed new blocks for the mines to free up memory between resets of the mines.


* **Major changes for the FileCollection and Document related classes**
This was a major change that encompasses the removal of Guava, the caching library.
Everything works the same as before, but with the exception of saving the individual 
files as change occur; that will be added in next.  This touched basically every module
including the prison-ranks.


* **Update FileStorage and FileDatabase**
These changes are fairly similar in nature and the two classes are so very similar. 
Maybe in the future they can be merged so there is the same code base handling both of them.


* **Various clean up items**
Like removal of useless comments that are in the wrong places.  Fixing includes that are including
packages that are not being used.  Removal of warnings.  Etc... just mostly items that will result
is easier to read code without any functional changes (and no program changes in most places).


* **Simple refactorings**
There were a few items that were refactored back in to their controlling class. For example ranks 
and ladders add on an id to generate their file names.  Created a filename function for those
classes so externally all users of those classes do not have to know what the business rules are
for constructing the filenames. This also can help to eliminate errors in the future; only one
location for constructing filenames instead of a few different locations.


* **New Feature: Add a text component to RowComponent**
Expanded the ability of RowComponent to accept parameterized text now.


* **New Feature: Add volume to the mines info**
Provide a little more information for mines by now showing the mine's volume.


* **Bug fix - Mine blocks updating wrong values and problems deleting blocks**
Fixed a few bugs with adding, setting, and deleting by the wrong value.  By default 
it was trying to delete by id, but multiple blocks have the same id, so it was 
corrupting the block lists for a mine.
It now uses the block name instead of block id.  This eliminated problems.


* **New Feature - Identify all BlockTypes that are blocks as blocks**
Trying to define each BlockType as a BLOCK or not.  This will be used to filter 
the results on /mines block search to ensure only blocks that are placeable can be 
set in the mines. Tried to programatically identify what a block is within all 
supported minecraft versions, but it did not work due to conflicts with the
blocks names within Materials and within the Prison plugin's BlockType enum.
Manually marked each BlockType since programatic attempts were running in to 
numerous issues.  There may be a few that do not
make sense or are incorrect, but can update later when found. Created a new enum
by the name of MaterialType that is used to mark the BlockTypes as 
BLOCKs.  Can also use that enum to label the other items with something more
meaningful such as ITEMs, or even ARMOR or WEAPON. Can have a great deal of 
flexibility moving forward with that.


* **New Feature - Block search new only includes blocks that are actually placable in the mines**
Hooked up the BlockType.getMaterialType() == MaterialType.BLOCK to the block 
search and the block add.


* **New Feature - Added confirm on Mine Delete**
Added a new feature to provide a confirmation when deleting a mine.  The user can 
click on the notification message to have the command reentered for them, then they
only need to change **cancel** to **confirm**.  
This will help prevent accidental deletion of mines.  Yes mines can be manually 
undeleted, but this can prevent mistakes.


* **New Feature - Using /mines set block will add new blocks**
If you click on a block from under /mines list and you change the mine's name on that
/mines block set command, it will be treated like an add instead of an error. 
This is beneficial if you want to copy a block from one mine to another and you forget
to change "set" to "add".


* **New Feature - Added a center Location to Bounds**
Added a center location for Bounds which will be used for a few things for mines.
Currently it was added as a fall back location for if a spawn point does not exist for 
teleporting in to a mine.  But it will also be used to identify all players within a 
given radius from that location to selectively target broadcast messages pertaining 
to that mine.


* **New Feature - Clone existing Locations**
Makes it easier to create clones of Locations.


* **New Feature - Track last Mine name used and substitute it for the generic place holder**
Track what the last value for mine was, and put it on a timer.  If it was last 
referenced within 30 minutes, then use that reference by default in the block 
search function.
In the future, when auto suggest is enabled, this value will also be used for the 
targeted mine names instead of just a generic <mine> place holder.


* **New Feature: TP to each mine**
Added a new feature so an admin could TP directly to each mine.  If the mine's spawn location 
is not set, then it will tp them to the center of the mine, but on top of the surface.
Also, if there is an air block at the player's feet, it will auto spawn in a glass block so 
they would not take fall damage if there is a significant void below them.


* **Enhanced /mines list now includes more details**
Enhanced the /mines list to show more details about the mines, plus provide a clickable 
way to TP to each mine.


* **Fix - Removed color codes from commands**
Color codes do not work well with 1.14.4.  Removed them.


* **Fixed some issues with deleting a mine**
Changed the way the delete mine command works with the user messages.
Its clear as to what's going on and what the user has to do with the confirmations.
The time to confirm is limited, and reissues the start of the command if the user 
misses the window to confirm.
 


#### v3.2.0 Known Issues: To be addressed

* **Mine TP of Players who logout or set a home in the mines**
If a player logs out of the server when they are in the mines, or if they
set a home within the mines, there currently isn't an event handler that
will TP them to safety when they log back in, or tp to that home location.
This feature is planned for the future, but no ETA has been set.






