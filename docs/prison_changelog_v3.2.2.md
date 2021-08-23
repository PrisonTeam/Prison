[Prison Documents - Table of Contents](prison_docs_000_toc.md)

# Prison Build Logs for v3.2.2 - 2020-11-21

## Build logs
 - **[v3.3.0-alpha - Current](changelog_v3.3.x.md)**
 - [v3.2.0 through v3.2.10](prison_changelogs.md)
 

 

Greetings!  I'm delighted that you are interested in the build logs for the
Prison plugin.  I'm wanting to provide a more formal documentation as to what 
is going on in each build so you have a better idea if it may be something 
that you need.




# V3.2.2 Release - 2020-11-21

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



