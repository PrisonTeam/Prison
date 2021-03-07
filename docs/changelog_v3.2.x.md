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


# v3.2.5-alpha.3 2021-03-07


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


