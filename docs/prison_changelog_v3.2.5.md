[Prison Documents - Table of Contents](prison_docs_000_toc.md)

## Prison Build Logs for v3.2.5 - 2021-04-01

## Build logs
 - **[v3.3.0-alpha - Current](changelog_v3.3.x.md)**
 - [v3.2.0 through v3.2.10](prison_changelogs.md)
 

 

Greetings!  I'm delighted that you are interested in the build logs for the
Prison plugin.  I'm wanting to provide a more formal documentation as to what 
is going on in each build so you have a better idea if it may be something 
that you need.




# V3.2.5 2021-04-01 
Release of next bug update.

Set the version to v3.2.5.

Publishing this version.  Update documents.


* **Including the new alias for /prison version so it can be used with the next release.**


* **Adding to v3.2.5 release: Fixed an issue if one of the mine or ranks modules is disabled.**
This was encountered with the mines module being disabled when running /ranks autoConfigure.
This is to be included with the v3.2.5 release.



* **v3.2.5-alpha.16 2021-03-30**



* **Fixed issue with applying durability to non tools.**


* **Add in DebugType to logDebug messages.**
They are not yet hooked up, they will allow for controlling which debug messages are displayed.


* **For blocking the source should have been snowball instead of snow_block since the output was snow_block.**


* **Added a few other details that will be displayed when using /prison version all.**


* **Added a few high level features to report on their enabled status.**


* **Setup the hooks for listing active features that will be included with /prison version.**
It's hooked up, but has no content yet.


* **Simplify what is shown under /prison version.**
Also added a new parameter named action with a value of either basic (less detail and default) or all which will include more detailed information, such as the inactive integrations.  In the future, error messages from the start process may also be included if they are captured.
Removed a test comment from the AutoFeaturesFileConfig.
Expand the /ranks players command to add the option of 'full' which will not include all player names for each rank.  This may be a huge list depending upon server.  The player's names that are included will only be for players that prison has captured their names since names are normally not apart of the RankPlayers object.


* **v3.2.5-alpha.15 2021-03-28**



* **For the /ranks autoConfigure it now adds removal of all permissions to rank a's commands.**
This will help when using prestige by removing all higher ranks set by rankup commands.


* **For the command /ranks command add if a command already exists, then don't add a duplicate, just print a message stating its a duplicate.**


* **Add new documents and update a few others**


* **Hooked up new auto feature settings to enable autosell on each block break.** It may not be ideal, and could cause some lag, but it would be better than trying to script it to run outside of prison.  


* **Disabled the upcoming rank permissions since they are not hooked up.**
They have disclaimers stating they are not enabled yet, but people are still trying to use them.  Also the idea of managing groups needs to be figured out, since it cannot be done through vault.


* **When saving mines, it is now saving a nulled field as an empty string, otherwise that field will be omitted from the save file when I always want it to be there, even when empty values exist.**


* **Small tweaks to mine access perms to deal better with null values and to properly handle a situation if there is an empty string that gets set.**


* **Fixed an issue with linking ranks to mines.**  Once unlinked it would not be able to be relinked.


* **For the rank command custom placeholders set them up to default to "none" if the rank does not exist.**
There exists the possibility that some ranks may not exist and result in a null for the selected ranks.


* **v3.2.5-alpha.14 2021-03-24**


* **Suppress the error messages pertaining to the XMaterial not being able to parse some blocks.**
This is normal and caused by enchantment plugins changing the types with the data value not matching the real bukkit values.  So the items cannot be converted, but they are also not ones that can be smelted and blocked too.


* **New feature: If using a prison command task, it will now check to see if there is a placeholder of {inline} and if it finds one, then it will run the command inline instead of submitting it.**
If a TaskMode is defined (such as within BlockEvents) then this will override the supplied TaskMode.  Just place the new placeholder {inline} anywhere within the task, but it's best to include it at the beginning.  If you have multiple commands in one task command, then you only need to include this placeholder once, since it will apply to them all.


* **Old Block Model: Had a situation where block was null.**
This should never happen and was cause by the old block model not being able to properly map and old block type to an existing prison's BlockType. 
This issue has been fixed so it defaults a null value to AIR.  The other issue was resolved too, but more are bound to exist that are not mapped because it is the old block model.


* **Old block model: Added the older name for red_nether_bricks which was without the trailing "s" for v1.10, v1.11, and v1.12.**


* **Custom Command Task Placeholders!!**
Expand what you can do with prison's command tasks!  Just expanded the number of command task placeholders for rankup commands! Your rankup commands will be able to support the following placeholders with the next alpha release.  Plus Rank Commands now supports multiple commands per task, just like the BlockEvents!

  Common placeholders: **{player} {player_uid} {msg} {broadcast}**

  Custom placeholders for Rank Commands: **{balanceInitial} {balanceFinal} {currency} {rankupCost} {ladder} {rank} {rankTag} {targetRank} {targetRankTag}**


* **Hooked up the wrong player object within the rankup commands.**  Using the correct one now.  It works in my testing because my test environment has enabled certain features in the past, but most other users would not have those features enabled so it would not work correctly.


* **Added a new feature of Custom Placeholders for prison's internal command handler.**
This will allow different sources to supply custom placeholders that could expand the usability and flexibility of the commands that can be included. 


* **An unexpected error occurred on trying to match an ItemStack to an XMaterial.**
Put try caches around them.  Also created a way to dump an item stack to expanded text so that way the offending item stack can be fully logged.


* **It was brought to our attention that an informational message dealing with ladder removals was being assigned a "ERROR" status message.**  Removed the error aspect of it.


* **For some reason one of the parameters being passed was null for the player parameter.**
As such, it was resulting in "someone" in the broadcast messages. This should resolve that issue.  


* The use of a lapis function in Compatibility is not correct since XMaterial provides cross version way of getting it that actually works for 1.8 through 1.16.  
These updates are just to maintain compatibility in the code, until they can be updated to use XMaterial's item stacks. 
The interface was changed for where getLapisItemStack is declared.  It's moved to CompatibilityBlocks so that it's use in 1.8 will automatically apply to 1.9 without having to duplicate code.  As such, they have been moved around.
For this function, in Spigot18Blocks, it is using the XMaterial's parseItem() which returns an items stack with the required magic values already set and applied for the version of the server that is running this code.  XMaterial will handle the version conversions.


* **v3.2.5-alpha.13 2021-03-22**


* **Ran in to problems with a token enchanted tool causing internal null pointer exceptions when TokenEnchant was removed from the server.**
Normally this would not be an issue since such a tool would not exist outside of a Token Enchant environment.
These changes are to try to deal with the possible nulls in the item in hand just to be on the safe side, like as if someone broke a block like sand or dirt by hand.  If these NPEs happen, they now are just silently ignored.


* **Fixes an issue with how HashMaps were being used.  The key and value were reversed in a HashMap.**
So now the XMaterial is the key and the item's value is now a Double as the value in the hash.  It also simplifies looking up matches to the player's inventory because now it can be done through a keyed access with the HashMap, which is what maps are designed for.  So overall, this should help improve the performance too.
What it was doing, with the HashMap being reversed, was that if two or more materials had the same cost, then only the last material with that cost would be saved and all prior ones read from the config file would be purged/removed/deleted from memory (the HashMap).  This would appear to be fixed by removing a material and adding it back, but in adding it back it would only result in purging another material from the HashMap.  
This should fix all of the issues since all materials can now be represented.


* **This uses the correct function to convert a String name to an XMaterial.**
It's using matchXMaterial instead of valueOf, which is what is needing to be used for support of materials from spigot 1.8 through 1.16.
The code has been updated for this correction, although there is still a major bug in there.  I'll fix that next.


* **v3.2.5-alpha.12 2021-03-21**



* **In the auto manager, fixed the loss of "extra" inventory that was triggering a sellall event.**  The extras were being lost even with the sellall.
Had to change the code to directly access the sellall sell function so as to allow the player's inventory to be updated directly to make room in the player's inventory.  If there is stil extras, then the extras will be handled as setup in the configurations.


* **Refactored how prison runs the command tasks from rankup commands, mine reset commands, and block event commands.**  
This will allow all of the commands to utilize the name behaviors.  For example when new placeholders are added, they will be available to all instances of command tasks.  One new behavior that is added to rankup commands and mine commands is the ability to have more than one command per task, just use ";" between the commands.  This also allows for the use of `{msg}` and `{broadcast}` in command tasks that have players. 


* **Fixed the ability for either an operator or console to tp a player to any mine that is specified. **


* **v3.2.5-alpha.11 2021-03-20**


* **Had to externalize TokenEnchant's event listener for the TEBlockExplodeEvent**
so it won't cause any errors when TE is not installed.  It was preventing AutoFeatures from loading when it goes wrong.


* **Additional work on mine access permissions.**
This enables the setting, removing, and viewing of the access permission from the mines.  
Mine access permissions can only use permissions, and not group permissions. This feature is also enabled when using /ranks autoConfigure.  
This feature works well when worldguard is used to define a mine's region, or without a mine's region, so it's up to the admin on how they want to configure their server.  The whole world should still be protected with the global passthrough deny.


* **Fixed an NPE issue with loading mines when it encounters an invalid block name and is unable to perform the translation or identification.**


* **Added information about how certain blocks cannot be broken natively within a world guard region in some situations.**
Through the use of prison, they probably will break normally.  Examples are sea_lantern and various forms of prismarine.


* **New Rank cost preview for some GUIs**
Edited how the Rank price/cost's shown in some GUIs.


* **Doubling Ranks amount Admin GUI fixed**
Fixed Ranks items doubling in the Admin GUI (tripwire_hooks doubled).


* **Move the handling of % checking to the top and also check the playerName so it is properly able to report the player error back to the user.**


* **Major bonus fix... If there is a failure with a command, and there is a % in one of the parameters, then this was causing a major failure because it was failing when it failed to begin with.**
So a double failure.
So this double escapes the % to make it a %% so it will not trigger a failure in the String.format() when it's sending the messag to the output.


* **Backpacks GUIs optimzations**
Optimized Backpacks GUIs.

* **Mines GUIs optimizations**
Optimized Mines GUIs.


* **Ranks GUIs optimizations**
Optimized Ranks GUIs.


* **SellAll GUIs optimizations**
Optimized SellAll GUIs.


* **v3.2.5-alpha.10 2021-03-20**


* **Expand the debugging information for auto manager BlockBreakEvent to include logging for auto manager.**


* **With auto features being rather complex and many potential issues with it due to so many possible dependencies, added a debug logging entry to track exactly what happens.**
This debug information is only logged when debug is enabled through the logger.  This will be very useful to help providing support when people are having issues.


* **Change the priority back to LOW.**
It was changed to LOWEST to try something but it was not working on LOWEST either.  Chaning it to LOWEST may cause other issues.


* **v3.2.5-alpha.9 2021-03-19**


* **refactored the OnBlockBreakEventListener to move the majority of the code to a core object** so it can be extended by auto features instead of extending the class with listeners.  This reduces the number of hits to prisons event listeners.


* **Updated the docs on the worldguard settings to remove the "g:" prefix from the luck perm commands.**
Just discovered that the "g:" prefix was incorrect and was failing.


* **Fixed on death backpack lose**
On death if you enable the old backpacksconfig.yml option to lose/reset
  your inventory on death, is now working properly.


* **Backpack delete command fixed**
Fixed backpack delete command only resetting backpacks but not
  deleting them properly


* **Work on the BlockEvent document.**
Not yet finished, but provides a lot of important information.


* **First pass at adding block stats listings for mines.**
It's hard coded to support only the first 10 blocks within a mine, but this will be updated later to include all blocks.


* **Updated the mine sweeper information to contain more details.**


* **Added support for EZBlocks (untested).**
Must be enabled through the auto features config. 
Refactored the way mcMMO and EZBlocks are handled by putting them in another class of their own and having them registered and triggered through a common function, which will allow the addition of many other similar plugins as needed.


* **v3.2.5-alpha.8 2021-03-14**


* **Hook up mcMMO to be registered and used through the TE explode event and the CE Block Blast event.**


* **Enabled the non-auto manager use of the block events.**
These are only called when auto features are turned off, and if the blocks that are actually broken are in the mines.


* **Backpack permission option**
It's now possible to enable a permission to use backpacks from the backpacks config.
  Just turn this option to true: `BackPack_Use_Permission_Enabled: false`.
  The permission by default that you need to give to a player
  after enabling this option's visible in the backpacks config
  `BackPack_Use_Permission: prison.backpack`


* **More mine sweeper improvements.**
Added the tracking of blocks changed so as to get a feeling if its even doinig anything.


* **Update more code on the mine sweeper.**
Adding in total counts of the number of times it has been ran since server restart, plus total ms consumed.  That way can display more detailed stats such as total count and average runtimes.  Can be useful to determine how "expensive" this process will be.


* **Hook up the mine sweeper task so it will run upon block breakage.**
Also the command has been added to toggle it on and off.  Still needs some work to be done, such as hooking up to /mines info and some other stats tracking.


* **Setup a mine sweeper task that will refresh the block counts.**
This would be used when an enchantment plugin is not supported, or cannot be supported.  It's not ideal, but it could help minimize the problems of incompatibility.


* **Fine tuned how auto features behaves when it is disabled.**
Only the monitor event is allowed to get through so the block counts could be updated.  When options.general.isAutoManagerEnabled is set to false, auto features will be fully shutdown now.


* **Enable potion effects for the /prison utils potionEffects.**  Works well.


* **Removed color codes from the ladder and rank listings since the numerous use of color codes were resulting in a mess when the log files are unable to decode the color codes.**


* **Updates to the prison util potions.**
Works but not sure if I'm going to release it like this since I am not happy with how the params are setup.  once it is released then it will be difficult to change since people may be using them.


* **For the command /ranks player: Had to double escape the % since there are a few layers of String.format() that it goes through, so one just did not work out well.**


* **v3.2.5-alpha.7 2021-03-12**


* **Add more information to the /prison autoFeatures command.**


* **Fixed issue with xp.  Used the wrong block for the name so it was not getting any hits on the xp lookups.**
Found that the blockNameSearch value was being used on auto pickup... changed it to just blockName for consistency.  When custom items are used by players, then it would be worth reviewing to make sure everything is working correctly.


* **Auto features: More refactoring to simplify a lot of code and now the code for TokenEnchants explosions and Crazy Enchantments blast events are using the same code now!**
This is to eventually allow auto features to work outside of the mine.  But that may not be a great idea to use this outside of the mines due to possible exploits on placing blocks then mining them for the fortune gains.  Prison does not have the database support to manage the tracking of all player based blocks.


* **Fixed an issue where the removal of a rank from a player was not being saved**, 
so the changes would be reverted when the server restarted unless some other rank command caused a save to the rank data.


* **Potential issue with not rejecting canceled events when the listener is not at the MONITOR priority** (monitor should never process block breaks, but is used to log blocks mined as a fall-back stop gap attempt to update counts).


* **Some permissions appear to have % symbols so % is now being escaped so they do not cause issues with Java's String.format() command.**


* **PrisonSpigotAPI now include Prison backpacks.**
You can now use the getPrisonBackpacks method included within PrisonSpigotAPI to get the BackpacksUtil class, which's the core of Prison Backpacks, essentially full API access.


* **/backpack delete for admins command**
New `/backpack delete` command for admins, usage's `/backpack delete <playerName>` or
  `/backpack <playerName> <Id>`, both are valid, the IDs required only if multiple Prison backpacks support
  is enabled.
  The permission's `prison.admin` for now.


* **It's now possible to add a backpack from the backpacksList GUI**
When multiple Prison Backpacks are enabled, it's now possible to add
  a backpack directly from the GUI following the instruction of the "new" 
  button.
  Also added close GUI button.


* **Fixed new design changes for GUIs**
Lores got fixed.


* **try to clean up this sellall stuff in auto manager a bit.**
Realized there is a loss of overflow/extra items that will have to be fixed.


* **Setup auto manager to work outside of the mine.**


* **Added more details to /ranks set currency and now supports removal of a custom currency.**
It is now able to remove a previously set currency too. Mostly importantly, more detailed information has been provided to better explain what its purpose is for.


* **Reworked some of the auto manager functions to move them to the OnBlockBreakEventListener since hooking up Crazy Enchantment injector is needing to have access to some of the core features.**


* **Crazy Enchants has a pickaxe enchantment for bonus xp that is not called when prison handles the block break event.**
This code uses the enchantment to allow CE to apply it if it is enabled.


* **Start to setup a function that will calculate changed blocks so it can count and apply them to the mine's block counts.**
This is the code that performs the checks, but the infrastructure to hook it up and running still needs to be added.


* **Fixed logic error with setting up the backpacks that was resulting in a stack overflow exception as soon as a player would join the server.**


* **Eliminate the use of Collection since not all sources could support addAll()**.
Default to List and ArrayList.


* **SellAll and AutoPickup multiple Prison Backpacks support.**


* **Cleaned up a lot of use of mines and the reference to the new block model config option.**
Mines now has an internal variable isUseNewBlockModel which is set when the mine first loads.  This will reduce a lot of overhead with always looking up the configuration value and will also reduce and clean up code.  This cleanup has only been applied to the mines module.


* **Added some new Prison Utils that will smelt and block a player's inventory.** 


* **Auto Features: Realized that a block parameter was being passed in many functions when it was never used, so got rid of it.**


* **This fixes a few startup issues where it now uses the mine's stats block in the MineTargetPrisonBlock** so when the blocks are broken they have access to the correct stats block to increment.


* **These are a couple of API jars that are not ready yet to be moved in to the project.**
Storing them for later possible use if these plugins fix their issues.


* **Adjustments on how the blocks are tracked and counted.**
  This resolves some of the problems with explosions counting a block more than once. 
The MineTargetPrisonBlock now tracks when it's been counted so it will not be counted twice, plus it can be used to quickly rule out checking the actual block if it's be set to air or counted.


* **Removed a lot of color codes to reduce the server log file mess when viewing.**
There are still some color codes, but they no longer are hiding the data.


* **v3.2.5-alpha.6 2021-03-09**


* **New Backpacks list GUI if multiple backpacks are enabled**
new /gui backpackslist or /backpack list to see a list of own backpacks
  in a GUI.


* **Max number of Prison backpacks each player can own limit**
You can now add a limit of backpacks that each Player can own by editing this option:
  `Options.Multiple-BackPacks-For-Player: 2` by default you can own max 2 backpacks.


* **Prison Backpacks new feature, you can now own more than one backpack**
There's a new option in the backpacks config that you can enable to use more than
  one backpack for each player, this will also enable the command /backpack list and
  /backpack <id> instead of the /backpack only.
  Option name by default disabled: `Multiple-BackPacks-For-Player-Enabled: false`

* **Note: alpha.6 was generated at this point: v3.2.5-alpha.6 2021-03-10**

* **Fixed a problem where /rankup would fail, but would eliminate the player's balance.**
It was hitting the prestige function when it shouldn't have.


* **Finalize fix on calculating fortune that is greater than 3 and when spigot tries to apply fortune to the getDrops() function.**
This prevents wildly huge drops, and is more line with the original fortune randomized calculations.


* **Fix issue with fortune "doubling".**
Some versions of spigot/bukkit are unable to provide a suggested drop with fortune applied.
The issue was for the versions that are able to apply fortune to the drops, then prison's fortune calculations was multiplying them to the point that with high level of fortune enchantment that it would produce an excessive amount of drops.
This controls it by detecting if spigot/bukkit applied a fortune, then it uses that fortune ratio to the number of times the tool's fortune enchantment is above the max vanilla level of 3.  So if the enchantment is 300, then it will multiply the drop quantity from spigot/bukkit by 100.
To try to temper larger drops that will potentially result from these calculations, the adjusted fortune multiplier is multiplied by a random number between 0.7 (decreases) to 1.1 (increases) the drop.


* **v3.2.5-alpha.5 2021-03-08**


* **Setup support for Zenchantments.**
This should possibly support their explosion enchantments, since they fire this custom event for every block.  This has not been tested yet.


* **Fixed an issue with the synch mine resets not updating the reset counter.**


* **Fixed an issue with setting up the initial block counts when using the old block model.**


* **Added a new field to the MineData class for useNewBlockModel to set it when the mine is loaded so as to reduce all the config checks.**


* **Added a new feature that will prevent prestige from resetting the default ladder.**
In the config file, plugins/Prison/config.yml, set prestige.resetDefaultLadder: true (will have to add it).


* **Design changes to AutoFeatures GUI when disabled.**
Edited design, to apply new lores you should delete the module_conf/lang/EN_US.YML file and restart your server.


* **v3.2.5-alpha.4 2021-03-07**


* **Cleaned up some of the auto manager source code by deleting a lot of commented out stuff.**


* **Hook up the support for normal drop smelting and normal drop blocking.**
Fixed an issue with XP drops for coal.  Plus a few other fixes and improvements.


* **Added similar functions to the inventory add and replace to work on collections of stacks outside of the inventory of players.**  
These would be used for normal drops since the manipulations will have to occur before anything enters their inventory.


* **For auto features, added a new section for normal drops to include the ability to smelt and block.**
These may not alway appear to work since they only apply to the current drop (single block or whole explosion) so if they don't have enough to block (4 or 9 drops) then the cannot.  Inventory is ignored with normal drops for smelting and blocking.


* **Changes to get both backpacks working!  Fixed some issues with smelting and blocking too.**
Appears to be working much better now.


* **Backpack open and close sound**
It's now enabled by default a sound when opening a backpack or closing it.
  You can disable it from the backpacksconfig.yml and also edit the sound.
  This's supported for also 1.8.8.


* **Adding in support for remove all of a specific type from all inventories, including backpacks.**
A work in progress.


* **Per block sellall permission:**
You can now enable a permission in the `sellallconfig.yml` to sell blocks,
  if enabled a player will need the permission `prison.sellall.<blockname>`, for example
  for `COAL_ORE` the permission's going to be `prison.sellall.COAL_ORE`,
  to make sure you're adding the right permission, add again your sellall items and check
  it in the sellallconfig.yml next to the the item you've added.
  
NOTE: You can also edit an already existing item after enabling the per-block permission,
  and you'll see the right permission next to the edited sellall item, remember that if you edit
  this permission, it's going to do nothing because it isn't actually the one in use, so
  this permission isn't fully editable, you can only edit the <`prison.sellall.`>.
  
Option to enable this feature in the sellallconfig.yml:
`Sell_Per_Block_Permission_Enabled: true`

Structure of permission: `prison.sellall.<blockName>`.


* **Changes to prevent a concurrent modification exception when mining gravel and it is trying to add flint to the drops.**


* **Created a singeton wrapper for the AutoFeatures so they can be used with the prison core instead of just being restricted to the spigot module.**  Hooking it up to the command /prison autofeatures.


**v3.2.5-alpha.3 2021-03-06**


* **Using command translation to the registered command for /sellall.**
This helps to eliminate conflicts with other sellall plugins.


* **Hook up the larger set of default values (about 88) to the sellall function to setup the default values.**
This reuses the same data values that are used when building the /ranks autoConfigure.


* **Using command translation to the registered command for /backpack.**
This helps to eliminate conflicts with other backpack plugins.


* **Setup a CommandHandler to translate and lookup prison commands to get their registered command.**
This is important to ensure that internally the commands can be ran and they are running the prison version of the command.


* **Implementing spigot overflow/underflow on addItem or removeItem from inventory for backpacks too.**
Inventory addItem and removeItem features with HashMap returns for overflow/underflow are now supported by Prison backpacks.


* **Move support for automatically adding to the Minepack backpack to the SpigotUtil.addItemToPlayerInventory()**, 
which is where the prison's backpack code is located.  The idea with that is when that function is used (from anywhere) it will try to add to the backpacks automatically if the main inventory is full. 
Also fixed a few issues with addItems on the IntegrationMinepacksPlugin.  Also added a pure bukkit function to use that directly to eliminate a lot of the intermediate classes generation when it's not needed.


* **Backpacks recode**
Backpacks are back and the bug is finally fixed, hopefully.


* **SellAll Sounds fix**
Fixed sellall sounds errors for 1.8.8.


* **Backpacks support shutdown**
Removed Prison backpacks until further notice.


* **Setup minepacks as an integration.**
Add to auto pickup and auto smelt.  Not complete yet.  Have to add auto block.  Then do some testing.


* **Removed unused parameters in the AutoManagerFeatures classes**


* **Move Slime Fun to a new package that has slime in the name.**
Boost X and Z a little.


* **v3.2.5-alpha.2 2021-03-05**


* **Modified how Auto Features deals with items such as player XP, durability, item lore, update block break counts, and process block events.**
Now there is only one function that controls all of these and it's called from all locations where blocks are modified.  This applies to auto pickup and block breaks naturally.  And also for block break events, TE Explosion events, and Crazy Enchants blast events.


* **Update the ranks autoConfigure message to explain the force option.**


* **Setup sellall prices for the blocks being used in the auto configure.**
While it's running to generate the mine's blocks allocation it will insert about 87 items for sellall.


* **Added direction (vector) to the prison's Location class, and provide a way to add vectors to the current location.**
SpigotPlayer now has the ability to give the player XP and drop XP orbs at their feet.


* **Fixed issue of not adding a 1 to the durability when calculating the damage to the tools.**
The result is that a tool will have a quicker wear on their tool.


* **Since prison's fortune has no max upper limits, added a maxFortuneLevel** so the admin can put a cap on what's being calculated.  Of course it may be easier to cap the enchantments too.


* **Hooked up the mcMMO enablement in auto manager to a config setting so it could be turned off.**


* **For debugging purposes only, added ability to turn off event canceling on BlockBreakEvents and TEExplosionEvents.**
This may cause a lot of problems and result in many issues.  These should never be used.


* **Update documents to work better with GitHub's sites for the documentation.**
All documents within the directory prison/docs/ will be automatically added to, and published to, the site.  Therefore none of the docs within the docs directory can refer to documents outside of it, or it will result in a 404 when the player is viewing them.


* **Issue with XMaterial and parsing an item stack.**
Trying to at least catch an exception so it does not flood the console.  The reported item being picked up does not match what's actually going on.  tech.mcprison.prison.spigot.compat.Spigot18Blocks.getBlockType(ItemStack).
Should update XMaterial too.


* **Backpack conflict fix**
Fixed a conflict of backpacks when more peoples are using it at the same time.


* **Update the MineLiner so it can be removed.**
Added a remove command that will remove a specific edge. Added a removeAll that will remove all liners.


* **Prevent a NPE when getPrisonBlock cannot return a value.**
Auto features when getting the block to process.


* **Remove the temporary details on what was causing the issues with TE issues.**


* **Update the documentation for TE explosion events and Crazy Enchant's BlockExplodeEvents.** 
Kept some detailed information in that document that was used to identify the nature of the issue when it wasn't working correctly.  This will be removed soon, but by committing it will preserve a copy in the project's history.


* **Update the spigotmc.org doc to remove spaces from the file names.**
Renamed the files to remove them.  This is to clean up the use of %20 which is used to represent spaces.


* **Moved a few of the doc files in to the doc directory.**
This is important for supporting the github pages.


* **Prison Backpack autopickup support**
Prison backpack now supports autopickup and there're options for it in the backpacksconfig.yml.


* **Prison BackPack setting back default size bug fixed**
Fixed a but on player join that was setting the backpack size back to the default one for some reasons.


* **Prison Backpack SellAll support**
Fixed some issues with Prison Backpacks API and also added support for sellall. There're some editable options in the sellallconfig.yml for it.


* **NEW FEATURE: Now supports mcMMO within the mines!**
Checks to see if mcMMO is able to be enabled, and if it is, then call it's registered function that will do it's processing before prison will process the blocks.
This adds mcMMO support within mines for herbalism, mining, woodcutting, and excavation.


* **Update the spigotmc.org's text for prison's resource page.**  


* **v3.2.5-alpha.1 2021-03-01**




# **v3.2.4 2021-03-01**
  Release v3.2.4.



# V3.2.3 2020-12-25 
**Merry Christmas!!**
Release of next bug update.



# V3.2.2 Release - 2020-11-21



