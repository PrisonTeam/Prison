[Prison Documents - Table of Contents](prison_docs_000_toc.md)

## Prison Build Logs for v3.2.x

## Build logs
 - **[v3.2.5-alpha - Current](changelog_v3.2.x.md)**
 - [v3.2.0 - 2019-12-03](prison_changelog_v3.2.0.md)&nbsp;&nbsp;
[v3.2.1 - 2020-09-27](prison_changelog_v3.2.1.md)&nbsp;&nbsp;
[v3.2.2 - 2020-11-21](prison_changelog_v3.2.2.md)&nbsp;&nbsp;
[v3.2.3 - 2020-12-25](prison_changelog_v3.2.3.md)&nbsp;&nbsp;
[v3.2.4 - 2021-03-01](prison_changelog_v3.2.4.md)**
 

Greetings!  I'm delighted that you are interested in the build logs for the
Prison plugin.  I'm wanting to provide a more formal documentation as to what 
is going on in each build so you have a better idea if it may be something 
that you need.


# v3.2.5-alpha.7 2021-03-12


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


