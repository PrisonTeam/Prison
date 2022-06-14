[Prison Documents - Table of Contents](prison_docs_000_toc.md)

## Prison Build Logs for v3.3.x

## Build logs
 - **[v3.3.0-alpha - Current](changelog_v3.3.x.md)**
 - [v3.2.0 through v3.2.10](prison_changelogs.md)
 

These build logs represent the work that has been going on within prison. 



# 3.3.0-alpha.11g 2022-06-14


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
  
