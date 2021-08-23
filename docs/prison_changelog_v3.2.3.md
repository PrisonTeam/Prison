[Prison Documents - Table of Contents](prison_docs_000_toc.md)

## Prison Build Logs for v3.2.3 - 2020-12-25

## Build logs
 - **[v3.3.0-alpha - Current](changelog_v3.3.x.md)**
 - [v3.2.0 through v3.2.10](prison_changelogs.md)
 

 

Greetings!  I'm delighted that you are interested in the build logs for the
Prison plugin.  I'm wanting to provide a more formal documentation as to what 
is going on in each build so you have a better idea if it may be something 
that you need.




# V3.2.3 2020-12-25 
**Merry Christmas!!**
Release of next bug update.



* **fix for placeholdermanager's setup**


* **Placeholder refactoring to prepare for the use of dynamic customization by the users**, such as customizing each and every bar graph, or number formatting, where each use of the same placeholder could have different settings.


* **Added 4 new placeholders for rank number**
The new placeholders identifies the ladder position of a rank on their ladder.  The lowest rank has a value of 1.  These prison_rn, prison_rank_name, prison_rn_laddername, and prison_rank_number_laddername.


* **Setup editing options for BlockEvents**
so there are now an add, eventType, list, mode, percent, permission, and remove options to work with the BlockEvents so once created they can be better maintained without having to delete them and readd them.


* **V3.2.3-alpha.13 2020-12-24**


* **Added the ability to tie a BlockEvent to either a normal block break event, or a TE Explosion event, or both.**
This allows a more focused approach and flexibility.
Hooked this up to a few more things and finalized more of it.


* **Found some obscure bugs with the command handler and fixed it. Was generating a NPE.**


* **Changes to ranks autoConfigure to force a generation if ranks and mines already exist.**
If there is a conflict with a preexisting rank, it iwill be skipped and so will the generation of the mine by the same name.


* **Hook up the auto assignment of mine liners to the mines when running the ranks autoConfigure command.**
The selection of the liners is random and are applied to the walls and bottom using the force setting to ensure they generate in a void world.


* **Add support for assigning random liners to the auto generated mines.**
Does not include the last LinerPattern since it is for repair. 


* **More work on the mine liner.  Pretty much working well with /mines set size.**
Still need to hook up to a few other areas.


* **Hooking up the saving of mine liners so they can eventually be used to regenerate themselves when resizing or moving the mine.**
Still needs more work, but getting closer.


* **Update some of the changelog docs.**
It was getting very long, so broke them out in by release version for easier tracking of changes.



* **Refactored some of the Mine related classes** to better organize them since they will be undergoing some changes soon.



* **Initial support for hex colors.**
Works, but its a pain until I can auto parse the hex automatically.  But for now it can be entered and will work well with v1.16.x and will degrade to something reasonable for even v1.8.8.
So instead of using &#abcdef for right now, you need to enter it as &x&a&b&c&d&e&f.  I'll hook up the proper translator later, but the expanded format should always work.


* **3.2.3-alpha.12 2020-12-21**

* **Fixes a potential issue where players are attempted to be added in an async thread which will end with a failure since the commands on rankup cannot be ran in an async thread.**
This now detects if the thread is async (not the primary bukkit thread) and then submits a future sync task to run in zero ticks that will add the new player to Prison, which will result in a rankup.


* **Move the PrisonRunnable out of the Mines module and placed it in core since it needs to be used elsewhere.**


* **RankUpEvent now contains more information and can now cancel a rankup.**
As a result to allow the canceling of a rankup, the rankup code had to be modified to fire the event before anything is applied to the rankup.


* **3.2.3-alpha.11 2020-12-19**


* **Reenabled the global broadcast of rankups**, which this time it's hooked p to the config setting broadcast-rankups in the config.yml file.


* **Enabled new feature prestige.resetMoney** 
that will control if the player's money is reset upon presetiging. The new permission is `presetige.resetMoney`.  If the permission is not set, then it will default to true.  This also changes the configs in config.yml with prestiges being deprecated but will remain for a while.


* **SellAll auto toggle permission**, added the ability to enable or disable a permission in the SellAllConfig.yml for the full inventory SellAll auto per-user toggleable.
  
  
* **SellAll Sell Multiplier Permission**, added the ability to give multipliers to players by adding to the user
the permission prison.sellall.multiplier.valueOfTheMultiplier here, example: prison.sellall.multiplier.2 will add a 2x multiplier to the 1x of the default, so the total multiplier will be 3x, this multiplier system will add to the existing multipliers. If you give player 2 or more permission multipliers, they'll be added to the default multiplier within the sellallconfig.yml. If any multipliers have a total value of zero, then the player will receive no money. Example: GABRYCA has prison.sellall.multiplier.2 and prison.sellall.multiplier.3, along with a default value one of 1, then the total multiplier will be 2+3+1=6x. As a result, if GABRYCA sells one COAL that has a value of 10, then with the multipliers applied it will result in 60 cash.


* **SpigotPlayer: added a function to get a list of all perms the player has, and also a function to filter by prefix.**


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



# V3.2.2 Release - 2020-11-21

