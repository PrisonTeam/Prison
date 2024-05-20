[Prison Documents - Table of Contents](prison_docs_000_toc.md)

## Prison Build Logs for v3.3.x

## Build logs
 - **[v3.3.0-alpha - Current](changelog_v3.3.x.md)**
 - [v3.2.0 through v3.3.0-alpha.18](prison_changelogs.md)
 

These build logs represent the work that has been going on within prison. 


# 3.3.0-alpha.17 2024-04-21



The following is a highlight of changes for the alpha.17 release since the alpha.16 release.


* Mines: Added more support for the use of '*all*' for mine names so more of the mine commands will be able to apply to all mines.


* **Upgrade XSeries to v9.7.0 from v9.4.0.**
* **Upgrade XSeries from v9.7.0 to v9.8.0.**
* **XSeries: Upgrade from v9.8.0 to v9.9.0**

* **Upgrade nbt-api from v2.12.0 to v2.12.2.**

* **PlaceholderAPI: Upgrade from v2.11.2 to v2.11.5**



* **Breaking change in XSeries: GRASS has been changed to SHORT_GRASS for v20.0.4!** It's disappointing to say the least that after all of these years, XSeries screwed up and pushed a breaking change to their repo.  They should have kept GRASS so they would have remained compatible will all past code and configs that had to refer to GRASS directly, but nope... they opted for causing problems.  Very disappointing.
Setup a converter to automatically convert all GRASS to SHORT_GRASS as the mines are loaded.



* **Bug Fix: Mine resets and block constraints.**
This fixes a few issues with block constrains using min and max, along with exclude from top and bottom too.


* **Bug Fix: GUI ranks, mines, and prestiges were not using the default item name correctly.**  It was using the template correctly, but was not translating the use of placeholders.  Only name and tag are supported.
Mines: `{mineName}` and `{mineTag}`
Ranks and prestiges: `{rankName}` and `{rankTag}`



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



* **Mines: Fixes an issue for when mines are disabled and they are being checked in other processes to see if they are active.**
If the instance of PrisonMines is null, then it will create a temp instance just to prevent an NPE.



* **Prison Block change: Add support for display name, which is optional.**
Setup sellall so it can use the display name now, so renamed items will not be mistaken for vanilla minecraft items.
More work needs to be done to hook up displayName to other features, such as sellall and add prison block to mines.


* **Bug fix: Fixed the command `/mines set accessPermission` where it was apply the given perm to all mines.**
 Likewise, all mines parameter was failing to do anything.


* **NOTE: This alpha version "should" support spigot 20.0.4.**
After a few days, if no other issues surface pertaining to 20.0.4, or other related plugins, this will be released as a public release.


* **Fix issue with BlockEvent's SellAll when isAutoSellIfInventoryIsFullForBLOCKEVENTSPriority feature is enabled.**
This was not using the correct new functions that checks to see if a player can use autsell, or if they have it temporarily toggled off.  This also checks to see if the player has the correct perms, if perms are enabled for the sellall event.




* **alpha.16a - 2023-12-28**
  NOTE: I just noticed that alpha.16a was never committed.  So this is not the correct location of when it was set with the local builds.
  


* **updated the help on the `/mines block constraint` to indicate that the layer count is originating from the top, not the bottom.**


* **Bug fix: Player manager startup: fixed a problem where all players were being updated** even though they did not have a name change.  Only when name changes are detected are the files updated or when a new player is found.


* **Mines set resetTime: fix typo in the description where it shows '*all' instead of '*all*'.**


* **ranks ladder: applyRanksCostMultiplier command was changed to allow the value of 'true' to be used along with the value of 'apply'.**  This helps to eliminate some confusion on how the command works.


* **Bug fix: Prison command handler.  When players are de-op'd, and they do the commands such as `/ranks help` or `/mines help` it was incorrectly showing other sub commands they did not have access to.**
This now shows the correct sub commands that they have access to.


* **Placeholders: topn players - bug fix.  If a player did not have a prestige rank, then it would cause a NPE when using the `prison_top_player_rank_prestiges_nnn_tp` placeholder.**
Just check to ensure its not null... if it is, then return an empty string.



* **Bug fix: prison support submit: if the bukkit system cannot extract a file from the jar**, such as plugin.yml, this will prevent the failure of the command.  This will allow the command to continue being processed, but may just skip the extraction.



* **Placeholders: Top player rank was using the wrong ladder, which was incorrectly the prestiges rank and not the default rank.**
Correcting the rank fixed the problem.  This only was an issue if the player did not have a prestige rank.



* **Bug fix: GUI configuration: Found a problem that when configuring the gui initial settings, that there were problems when trying to access mines and ranks when they don't yet exist.**




* **File saving: alternative technique for saving files. Do not use!**
This is a more dangerous technique that could possibly result in lost configurations.  This is being provided as a degraded service if the fail-safe technique is not working ideally on a degraded server.
This should never be used, unless directed by a prison support admin.



* **Prison startup bug: There was an issue with the prison startup when there was an error and prison tried to log the error**, only to find that resources that were needed for logging were not yet loaded nor were their dependencies.  This fixes some of the entanglements to allow the error messages to be properly logged.



* **Import Mines: Added the ability to import mines from JetPrisonMines config files.**
`/mines import jetprisonmines help`


* **Prison Command Handler: using config.yml you can now change all of prison's root commands with 'prisonCommandHandler.command-roots'.**
Can now map 'prison', 'mines', 'ranks', 'gui', 'sellall' to all new command that you want.




**3.3.0-alpha.16b 2024-02-24** 



* **Mine imports: Fix some minor issues to get this to work even better.**

* **Mines import: prevent the processing of an importing of a mine if there are problems with the mine's name, or locations.**



* **Mine skip reset: If a mine is reset, send a message to players, but only if the message is defined and not empty: 'skip_reset_message='**



* **Player sellall Multipliers: Fixed the ranks multiplier to include all ranks that are defined within the sellall multipliers.**
Added a new function that will gather and list all multipliers that go in to the calculation of the player's total multipliers, which includes the rank multipliers (the sellall multipliers) and also permission based multipliers.
This detailed list of the individual multipliers, is viewable for each player that is online with the command `/ranks player <player>` and reveals the actual details of how it's calculated.



* **AutoSell: Bug fix: When autosell and sell on inventory is full was all turned off, it would still sell.**
This fixes some of the logic to simplify the code, and to fix those issues.


* **Mine skip reset messaging: Did not have it hooked up in the correct location, so the skip messages were not happening.**


* **Mine reset: Under heavy load when performing a mine import, there were seen occasionally errors with concurrent modification errors.**
Code was changed to minimize that possibility.


* **Sellall and GUI message failures: a number of messages that would indicate the player does not have access to that command were changed to remove the permission from the message.**
This was requested by a couple of admins because they did not want the players to see the internal workings that would otherwise control how the software would behave.



* **Block lists: Added the total chance percentage to be displayed with the blocks.**


* **File output technique: Changes to how the "replace" existing files works.**
If the file does not exist, then it opens it to create it, otherwise it truncates it.


* **Mine bombs: Ability to prevent a bomb's blocks from count towards the player's block totals.**


* **Sellall and autosell: Refinements were made to the handling of the sellall settings to better stabilize the use of the commands.**  Setting status for the players were moved to the player objects and is now used in all of the related calculations so there is better stability and consistency.



* **Bug fix: the check for the time the reset has been going on was incorrect and was fixed.**
Also all the code for submitting the reset task was moved in to the mutext.  Now, if the reset got hung up, this will properly terminate it and resubmit it.


* **File format: Eliminate the check for file types since there is only one.**
Currently there isn't a setting to specify what it should be.


* **Mines block layerStats: Added a new command that shows which blocks are in each layer in the mine.**
There are a lot of future enhancements that can be added to this command, such as checking the actual blocks to see if they are still there, or if there was a problem with the spawning of the blocks.


* **Mines block layer: Added colors for same IDs so it's easier to read.**  Added a check that sees what block actually exists.  If counts of what should have spawned match whats in the mine for that layer, then it shows only one number.  It shows two numbers if a block's intended spawn does not match what's in the mine.


* **Mine reset: added a force to the reset so it will ignore an existing mine reset and allow a new one to begin.***
When a mine is being reset, there is no way to actually cancel it.  So this allows a large mine to undergo multiple concurrent resets.  Use at your own risk.



* **Mine resets: Reworked how prison is selecting random blocks per layer, to properly include constraints.**
The addition of various new features in the past made a mess of the logic, so it's been cleaned up greatly so it now makes sense and should work properly now.
There is a slight risk, that as blocks are removed from a layer due to reaching it's max constraint value, that future random selections were missing blocks selections and was then inserting AIR. This fixed code now will insert a filler block which has been selected with no constraints, and the largest chance value.


* **mines block layerStats: rewrote to improve and get rid of the collection manipulations.**
Found potential problem with air being inserted in to mines.
Renamed a lot of uses of Location objects to include the name "location" in their variable names instead of "block".



* **New feature: TopN customization now possible.  The messages and placeholders that you can use are located in the core multi-language files.**
See the bottom of the files for instructions on usage.
TopN data is set to delay load so it does not lengthen the startup process.  As such, it now reports that the data is being loaded so it is now clear why there are no entries in the list initially.



* **Prison support listeners: added support for listening to and providing dumps for PlayerDropItemEvent, PlayerPickupItemEvent, and BlockPlaceEvent.**


* **Promote & Demote: Improved upon reporting issues with the command.**
There were few situations where the command would exit without reporting why, which was leading to difficulties with using the command effectively. 


* **Mine bombs: add support for BlockPlacementEvent so if someone is using a Block they can use it as the mine bomb's item.**


* **Prison's NBT: Add support for using NBTs with bukkit's Block.**



* **PlaceholderAPI: Upgrade from v2.11.2 to v2.11.5**


* **XSeries: Upgrade from v9.8.0 to v9.9.0**



**3.3.0-alpha.16c 2024-03-11** 



* **Mine Bombs: wrapped up the changes to enable the placement of a mine bomb when using the BlockPlaceEvent which is used when using a block for the bomb's item.**


* **Prison ItemStack: remove enchantments from the core ItemStack since prison cannot properly represent it in versions lower than 1.13.x**, plus it was wrong for all spigot versions greater than 1.12.x.
Added the proper enchantment functions to the SpigotItemStack object.




* **Sellall: New command:  '/sellall items inspect'**
This new command will inspect what the player is holding, and dump the details so the admin can see exactly how an item/block is created, including lore and enchantments.
Eventually this information can be used to enhance the ability to sell and buy non-standard items by allowing the admins to filter on lore, enchantments, and/or NBTs.



* **SpigotPlayer: Fixed a potential issue if trying to use getRankPlayer() if the ranks module is not enabled.**
Added a check to ensure it's active.
We have not seen any reports of issues related to this.





* **Prison API: Added a few new functions to work with ItemStacks.**




* **Localization: If admin adds extra parameters, or other parsing failures, happens on a message, the error will now be trapped and logged to the console without formatting.**



* **Economy: For economies that prison supports that has a method to check if the player has an account, prison now tries to check if there is an account for the player before trying to use the economy.**
This could potentially prevent issues or run time failures.


* **Player Manager: Secondary Placeholders:  Setup the secondary placeholder support on the PlayerManager, but it has not been enabled yet** since secondary placeholders on placeholders do not make a lot of sense because each placeholder is only one value and they cannot contain alternative text.  At least not yet.


* **Localizable: Secondary placeholders:  Rewrote the whole support of secondary placeholders related to players.***
Expanded the support by making them generic so other data sources can also have their own custom set of placeholders too.  Such as mines.
This now supports a new interface that will provide the generic support.
Player's commands have been modified to pass a RankPlayer object, which supports the new interface.  Non-player commands have not been converted since players will never see those messages (such as admin commands).



* **Placeholder bug: The placeholder 'prison_rankup_cost_percent' uses the calculated value of a percentage, but when used with the placeholder attribute, it was found to use the price instead.**  As such, the actual value returned for the placeholder was incorrect.



* **Mines messages: Secondary placeholders.  Added support for mines' messages to be able to support secondary placeholders within the language files. NOTE: Not usable at this time.**
But... this is basically useless.  Within the mines language files, the vast majority of all messages are related to admin messages and are not viewable by the players.
Therefore the admin-only messages will not support the secondary placeholders since the players will never see them.
At this time, there are no messages that supports the use of these secondary placeholders, although the feature has been enabled for mines.
If a need is required, then please reach out and request such features should be enabled.  At this time, effort and work will not be performed blindly upon items that will never be used, so if you see a need for this, I'd be happy to add them since you would have a need.






* * * * * * * * * * *



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
More work needs to be done to hoo up displayName to other features, such as sellall and add prison block to mines.


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




