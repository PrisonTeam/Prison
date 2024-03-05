[Prison Documents - Table of Contents](prison_docs_000_toc.md)

## Prison Build Logs for v3.3.x

## Change logs
 - **[v3.3.0-alpha - Current](changelog_v3.3.x.md)**
 - [v3.2.0 through v3.3.0-alpha.16](prison_changelogs.md)
 
* [Known Issues - Open](knownissues_v3.2.x.md)
* [Known Issues - Resolved](knownissues_v3.2.x_resolved.md)



These change logs represent the work that has been going on within prison. 


# 3.3.0-alpha.16b 2024-03-05


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
Added more comments to make it easier to understand these settings too.


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
  
