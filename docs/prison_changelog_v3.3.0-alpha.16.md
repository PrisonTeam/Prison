[Prison Documents - Table of Contents](prison_docs_000_toc.md)

## Prison Build Logs for v3.3.x

## Build logs
 - **[v3.3.0-alpha - Current](changelog_v3.3.x.md)**
 - [v3.2.0 through v3.3.0-alpha.16](prison_changelogs.md)
 

These build logs represent the work that has been going on within prison. 


# 3.3.0-alpha.16 2023-11-18



The following is a highlight of changes for the alpha.16 release since the alpha.15 release.


* Bug fix: A library that prison uses broke with the release of Spigot 1.20.2 due to new parameter overloading that spigot introduce, that resulted in ambiguous parameter failures.  
 

* Auto Features: Many updates and enhancements.


* Sellall: Many updates and enhancements from API to GUI and autofeatures integration.
  - Streamlined to work with prison's custom blocks instead of just XMaterial items. 
  - New functionality to improve managing what's been sold, or just getting their value. Extended a lot of this new functionality to the prison APIs.
  - Added many new default items
  - Changed all of the commands under sellall to better organize like commands in to sub-commands
  - Rank multipliers now works for all ranks, not just prestiges.  New commands to reset all multipliers for ladders (which is great if you have a few thousand prestige ranks).
  - If blocks could not be sold through auto features autosell, it will generate debug mmessages so it's easier to track why blocks are not being sold.
  - Able to disable the "nothing to sell" message so it stops spamming the player.
  - For different server configs, where there are issues that prevent autosell from being used, such as other plugins placing blocks in the player's inventory, you can now force a sellall after the block break event is finished being processed.
  - Sellall Multipliers - Many new features and commands.  Explore with `/sellall multiplier` and use the keyword `help` on each command for more details.  The multiplier list can use different column widths to better support thousands of prestige ranks.
  


* Prison's command handler: Updates and a few enhancements.
  - Worlds that are disabled in config.yml prevents prison's command handler from working in those worlds, thus shutting down any and all aspects of prison in those worlds.  Use carefully since no warnings will be shown in those worlds that prison is ignoring all commands issued from within those worlds.


* Fixed an issue with the player's inventory not matching prison's internal code vs bukkit's.


* Expanded Prison's APIs so its easier to use prison's internals within other plugins for the most flexibility in customizing your servers.


* Multi-language support files now supports:
 - `*none*` keyword to suppress any text.  Returns an empty String.
 - Now supports line breaks within messages.  Use `{br}` which is similar to the html break: `<br>` but cannot use the `<` or `>`.  The `{}` is typical of placeholders.
 
 
* Improvements to Prison's debug mode and block inspector to make is easier to address issues.
 - Debug mode can now be tied to a single person to prevent the console from being spammed
 

* Prison Mine Bombs - Can now identify which mines specific bombs can be use in, or excluded from. Includes a global setting in config.yml so individual bombs do not need to be configured to be excluded from certain mines.


* Prison support - New features
 - Adding a new HTML bases support file.  More work needs to be done with it before making it the primary tool.
 - Changed servers for submitting support help files... they are not encrypted and password protected.  And they are purged in 7 days.
 
 
* Mines - 
  - Mine tracer now has an option to 'clear' the whole mine, mark jjust the 'corners', or the standard full tracer.
  - Found and fixed a sync task that was a possible cause of jitters. This helps when the server is under a heavy load.
  - Added a `/mtop` command.  `/mines top` which teleports a player to the top of the mine they are in.
  


* Auto Configure - Major changes to add more default prestige ranks, and the ability to go back and add more ranks after your server is up and running without harming anything.
  - Added support to reset all rank cost multiplier and rank costs to apply to all ranks on a ladder.
  

* XPrison Enchantments - Added some basic support for using the XPrison enchantments. There are some limitations, and not all enchantments will work perfectly.


* Placeholders - 
  - Disabled all placeholders in disabled worlds.
  - New placeholder attribute to pass a player name to force a non-player placeholder request to use the specified player.  This is useful for some scripting languages and other plugins.
  


* Auto Manager - 
  - New fortune type: percentGradient.  An alternative fortune calculation that is designed to be more linear distribution, based upon the tool's fortune.
  - Able to use TokenEnchant's fortune level.
  
  
* BlockConverters - Starting to use some minor features.  The first being Event Triggers where a given block can force prison to trigger another plugin and allow that plugin to handle all processing for that block.  Example is using a lucky block.
  


* **Prison compatibility: Added support for block metadata customModelData.**
  - This is only compatible with spigot 1.14.x and higher.
  - Supported in prison mine bombs so you can customize the bomb's texture
  
  
  
* Prison performance improvements:
  - More options to fine tune control of the player cache, mine reset control, economy cache, etc... 
  - Need to produce a document that covers performance optimization for prison
  - Some default settings have been changed to encourage better performance out-of-the-box







# 3.3.0-alpha.16 2023-11-18


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



