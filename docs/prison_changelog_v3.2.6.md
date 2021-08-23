[Prison Documents - Table of Contents](prison_docs_000_toc.md)

## Prison Build Logs for v3.2.5 - 2021-04-01

## Build logs
 - **[v3.3.0-alpha - Current](changelog_v3.3.x.md)**
 - [v3.2.0 through v3.2.10](prison_changelogs.md)
 

 

Greetings!  I'm delighted that you are interested in the build logs for the
Prison plugin.  I'm wanting to provide a more formal documentation as to what 
is going on in each build so you have a better idea if it may be something 
that you need.





# v3.2.6 2021-04-10



* **v3.2.6 2021-04-10**


* **Start to add in a PrisonMinesBlockEventEvent class.**
It has been disabled because it cannot be used yet; the BlockEvents need to under go some major changes to support its use.
Currently the BlockEvents receive only references to the sources (block names and not the actual blocks), but in order to hook this up properly, all blocks and details need to be passed.  The current system for controlling the BlockEvents is unable to support that kind of an environment right now. It will be changed in the very near future to get this working.


* **v3.2.6-alpha.4 2021-04-09**


* **Fix to the new PrisonMinesBlockBreakEvent.**
Found out the hard way that since this extended the BlockBreakEvent it was picking up all the registered handlers for the BlockBreakEvent. LOL  So fixed that to prevent a possible stack overflow exception within prison.


* **New Feature: Added PrisonMinesBlockBreakEvent to all auto manager event handlers.**
This allows the canceling of prison's handling of the block break event before they are processed.  This event type also contains the mine in which the blocks exist in, along with the list of any exploded blocks.  This event also identifies the BlockEventType along with the TokenEnchant triggered event if it is set.  Through these prison related objects (Mines and SpigotBlock) the consuming plugin can extract a lot of detailed information about the event and conditions.  The event also includes getOriginalTargetBlock() function that can be used with any of the provided blocks in this event to identify what the original block within the mine was set to, along with the mine's stats that goes along with that block.


* **Fixed a backpack error for backpack set size**
If too many items were in a backpack and it got resized to a smaller
  size than the number of them, it won't give an error anymore.


* **Fixed backpacks issue with dimensions**
Custom backpacks dimensions were resetting when adding an item to the backpack.


* **v3.2.6-alpha.3 2021-04-07**


* **Removed the minepacks maven repo and just included the api jar because their maven repo was failing for over 24 hours.**
Removed jetbrain annotations since they were preventing the project from compliing since the classes could not be found by the compiler.  By changing the build.gradle script to remove the maven repo for minepacks it forced an update and the updated resources were unable to locate jetbrains classes.


* **Player GUI aka /gui ranks have more internal placeholders**
You can now use {rankName} and {rankTag} for the Player Ranks GUI lore
  editor in the guiconfig.yml, they'll be replaced automatically with the
  rank name and rank tag.


* **Option to enable or disable numbers in the /gui ranks or /ranks for players**
It's now possible to show or not the rank number in the GUI by changing in the guiconfig.yml
  this option: 
  Number_of_Rank_Player_GUI: false
  

* **Edited /gui main GUI design and added backpack admin button**
Added Backpack Admin button to the /gui so it's available directly from there.


* **Added SellAllUtil to the PrisonAPI**
You can now access the whole SellAllUtil from the PrisonAPI with
  the getPrisonSellAll method.


* **Added a character to prevent the collapse of the indents under some reporting conditions... generally when copying and pasting in to other tools.**


* **Many SellAll GUI Fixes**
SellAll GUIs will now update dynamically, some double messages errors got fixed,
  also a typo that was breaking the delay GUI.


* **SellAllUtil for SellAll**
Moved many sellall code management to the SellAllUtil class, this can add new
  issues but also fix many, please report them if you found any.


* **SellAll disabled worlds**
It's now possible to add disabled worlds where /sellall sell and /sellall auto toggle won't
  work to sellall from the sellallconfig.yml.


* **Trying to double register Prison with papi, once as normal, and with upper case prefix.**


* **Changed placeholder replacements to be case insensitive when performing the replacements.**
This probably only applies to chat prefixes.
This probably will not be unicode sensitive.


* **Provide a sanity check when creating a mine greater than 20,000 blocks.**
If a mine is a lot larger than expected, it may be due to a stray point set from another mine, which a super huge mine could destroy the surrounding builds.  So if something is horribly wrong with the selection, it may save someone's builds.


* **Backpack Admin Player Backpacks List GUI**
A sub-GUI of the Admin Player List GUI, which will show all the backpacks own by a Player, only a maximum of 54 backpacks of the Player can be shown, pages will be added in the future.


* **Backpack Admin Player List GUI**
A sub-GUI of the backpacks ADMIN GUI that will show a list of players owning at least one backpack, only 54 players can be shown for now, not one more, pages will be added in the future.


* **Backpack admin GUI**
A new admin GUI for backpacks got added, you can open it with /backpack admin.
  NOTE: You need the permission prison.admin to use it.


* **Backpack Internal Error fixed**
Fixed an internal backpack saving error, this didn't compromise the use of backpacks but just the ADMIN management.


* **v3.2.6-alpha.2 2021-04-03**



* **Modified how the configuration of the block break events are processed to give better granular control over them.**
Added a BlockBreakPriority of DISABLED so individual events can be fully controlled, even turned off.
Expanded the block break related priorities so they can be better controlled and even disabled.  This will allow for a fine grained control over each plugin instead of lumping them all together, along with prison.
These changes should provide a much greater degree of flexibility to help get environments working properly.


* **Backpack disabled worlds**
It's now possible to disable backpacks in some worlds, give a look to the backpacksconfig.

* **Enabled /backpack set size**
Enabled /backpack set size command, it must be a multiple of 9.
  The permission to use it is prison.admin.
  The format of the command's /backpack set size <Owner> <Size> <Id>, ID
  is OPTIONAL and required only for multiple backpacks when enabled.


* **Add the block break event priority to the /prison version command.**


* **Added a new auto features option to control the BlockBreakEvent priority.**
It defaults to LOW (what it's been set to for the last 4 versions), but the vaild options are:
LOWEST, LOW, NORMAL, HIGH, and HIGHEST.
The monitor priority is not available since block states must not be changed at that priority.  The existing monitors will remain set to a MONITOR priority irrespective to what priority is set.


* **Prevent prison's BlockBreak event listeners from registering if they are not enabled.**
For the core listeners, this means the mine module must be enabled.


* **Close GUI Message**
A new message got added to the messages FILE that will be shown when closing a Prison GUI with the close GUI button (Message -> GuiClosedWithSuccess:).


* **Player Ranks GUI (/gui ranks or /ranks for players) Lore is now customizable**
It's possible to customize the GUI lore from the guiconfig.yml for Player Ranks.


* **Added /gui reload command**
It's now possible to reload GUIs with the /gui reload command.



* **Set the current bleeding branch to v3.2.6-alpha.1




# v3.2.5 2021-04-01
Release of next bug update: 

**The April Fools release**. LOL I thought it was April 1st, but it really was March 31st.  I was fooled. lol


Set the version to v3.2.5.


# **v3.2.4 2021-03-01**
  Release v3.2.4.



# V3.2.3 2020-12-25 
**Merry Christmas!!**
Release of next bug update.



# V3.2.2 Release - 2020-11-21


