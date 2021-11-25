[Prison Documents - Table of Contents](prison_docs_000_toc.md)

## Prison Build Logs for v3.3.x

## Build logs
 - **[v3.3.0-alpha - Current](changelog_v3.3.x.md)**
 - [v3.2.0 through v3.2.10](prison_changelogs.md)
 

These build logs represent the work that has been going on within prison. 


*Will continue as v3.3.0-alpha.7 2021-06-?? in the near future.*


# 3.2.11-alpha.6 2021-11-24


* **Ran in to another Java format exception.**
Not sure what caused this problem, but added it to the catch so it can be reported in details so it can be fixed.


* **Add the ability to remove a mine's spawn point.**


* **Reworked how the drops were processed to prevent dropping the wrong contents.**
There was an issue where the drops were retrieved later in the process that allowed it to incorrectly pickup drops that were related to a decay function.  This resolves the incorrect drops.


* **Player cache timing improvements.**  This fixes issues with tracking the timing when mining.  
Not 100% sure it is perfect, but it's actually working much better.  Will need to revisit later.


* **Added a check to detect that a block has been altered and is not the block that was placed there by the mine reset.**
There have been drops of other materials making it in to an explosion event, and mostly due to block changes that happen due to other means.


* **Refactored some of the checks to determin if the event should be processed or not, so this can be used with a new feature that may help to auto manage access when WorldGuard is being used.**


* **Setup a mutex for locking the mine resets, which prevents active block break attempts, including explosions, from trying to break blocks while a mine is actively being reset.**
This helps to reduce the chance of hitting a concurrent modification exception.


* **Shut down auto manager and all auto features if the setting 'autoManager.isAutoManagerEnabled' is set to 'false' in autoFeaturesConfig.yml.**
If anyone wants to use prison's block events, then they must use the auto manager.


* **When using autosell through auto features, if a block cannot be sold, then the block is now placed in the player's inventory, or dropped if their inventory is full.**
If a block is unable to be sold, the amount returned for the item stack will be zero.


* **Bug Fix: There was originally a problem with applying block constraints that resulted in being unable to select a block when trying to randomly choose one.** 
Initially as a first quick fix was to trying to reselect a block, but if the block chances were really low, then it could still fail to select a block.  Then it was attempted to select a default block, but that too failed to work, especially if there were a sizable chance for AIR, and it would fail 100% of the time if the was only one block with a very low chance.  The failure was the whole mine could be filled with that one block with the very small chance.
This fix completely redesigns the block selection, by first selecting only the blocks that are valid for that level of the mine.  That way, when selecting blocks where blocks should be excluded from that level, those excluded blocks are never in the selected blocks to be considered.  Also if AIR is a valid option, then this new process adds an AIR block to the temporary level block list with the percent chance assigned to the air.  
Overall, this is working really well now, and it actually simplifies a lot of code and reduces the amount of processing involved.  This new process always selects a block on the first pass too so it never haves to try to reselect a block.


* **Moved the multi-column formatting of data to the class Text so it can be used in other parts of the project.**
It was originally created for use in /ranks player but is now extended to be usd to list the plugins within the /prison version command.


* **Bug fix: If player is mining outside of a mine, then don't process anything.**
May want to allow prison to manage block breaks outside of mines in the future, but for now, prison is only managing mines.


* **3.2.11-alpha.6 2021-11-21**


* **New feature which lists all of the Player Cache stats in the command `/ranks player`.  This includes stats for block breaks, time spent in mines, and earnings per mine.**


* **Capture an error within prison's Output class when trying to log content that has an UnknownFormatConversionException error.**
This happens when there is a problem with text formatting characters that conflict with Java's formating class.  This tries to log the error with better details so it can be fixed and resolved where the error is happening.  This does not "fix" the problem, but just better reports it so it can be identified and fixed.


* **Update on how prison manages the tracking of block breaks and earnings when auto features has autosell enabled.**


* **Changes to how the event listeners are setup: reduced by 1/3rd.**
Used to be that all three would be set if autopickup was enabled, but now only two will be set... monitor, and then either autopickup or manual drops.
This should improve performance since prison will be processing 1/3 less events.


* **3.2.11-alpha.5 2021-11-19**

Post the alpha.5 release.


* **Remove the now obsolete auto features setting isAutoSellPerBlockBreaknliedEnabled.**
It is no longer needed since the auto features autosell per block break is now optimized and has no impact anymore.
Improve the autosell integration in to the auto features for both the auto pickup and also the normal drops.  Improved the debug logging to include a list of all blocks being sold, or dropped, and their quantity and value.  Also the total counts too.


* **Bug fix with SellAll: bug in original logic where the delayed notification when initialized is losing the first amount.**
It now always adds the amount to the delayed queue.


* **Autofeatures ignore certain events to improve performance and reduce logging entries when in debug mode.**
Since there are multiple listeners on block break events, which monitor the same event, these changes are able to mark a specific block within the MineTargetPrisonBlock objects that will be able to pass along an "ignore" event status to the other listeners to short-circuit their processing.  This is highly beneficial when using large mine bombs and the mine has blockEvents setup to perform explosions... which will help reduce a ton of "dead" events. 


* **Auto Features Forced Auto Sell Optimization Improvement: AutoSell within auto features now only uses sellall by item stack and not the player interface that accesses all of the player's inventories.**
Since the auto features items are not placed in the player's inventories at this time in the process of auto features, there is no reason to access the player's inventories.  Selling directly reduces a lot of sellall overhead and as a result sellall is just calculating the prices.
The old autosell code within autofeatures has not be removed yet, but it cannot be called anymore.


* **PrisonDispatchCommandTask: Removed the debug logging since it can be very numerous when used with a large mine bomb and it's pointless since most of the block events being submitted runs in less that one millisecond.**
So this is just cleaning up a messy logging item.


* **SellAll: Setup a sellall function that will allow the selling of one ItemStack, which is not tied to any of the player's inventories.**
This is used in prison's auto features when enabling the option to auto sell on a per block break event basis.  This is highly optimized and a lot faster than using the normal sellall since it does not deal with any of the player's inventories or backpacks.
This forces a delayed sold amount message since an explosion could includ many ItemStacks.


* **Mine bombs: Add cooldown and fuse delay to the mine bomb settings so each bomb can be customized.  
Added gravity to the mine bombs too.**  Gravity, like glow, was added in minecraft 1.9 so older versions won't work.


* **Bug fix: For TokenEnchant's explosive event processing,** need to set PrisonMinesBlockBreakEvent's setForceIfAirBlock( true ) so the explosion event can be processed, even if the initial block has already been processed.
This allows TE explosion events to work correctly now.


* **Bug fix: refined the use of a few internal registers that are being used to control block break behavior, and also block counts and block events.*
A few of the settings were being changed in the wrong places, which was out of synch with when they should have been applied.
A few of the side effects was failure of tracking block counts, block events, and handling some explosion events.


* **Mine Bombs: More features and fixes.**
Added support for radiusInner (for hollow sphere explosion shapes), removalChance (chance for block inclusion), glowing, autoSell, tool material type, tool fortune level. 
 Added a new shape which is "cube" and hooked up sphereHollow.  Hooked up cube, sphereHollow, removalChance, glowing, the specified tool in hand with the custom fortune level.  Did not hook up the forced autosell yet.
Fixed some issues to get things to work a little better too.


* **Prison Bombs: enabled the right clicking of AIR to set the bombs.**
If clicking air blocks, then the block tied to the event will be null (at least for spigot 1.13.x) in that case, will use the block location of the player, and then adding the player's vector to it times three.



* **Fixed a bug with how the regex handles block quotes.**
Not only was it not working correctly for multiple block quotes, but it was incorrectly handling the tail end of the processed text and was basically doubling the text.  It now works correctly, even with multiple block quotes.


* **Fixed an unexpected "bug" with the JumboText font for the letter Q.**
One section was setup to use "\Q" which is an escape character for a regex block quote.  This was causing problems since it was forcing large sections of text to be ignored when translating minecraft color codes.  By changing it to "\q", a lower case Q, this eliminated the translation from making the mistake.


* **Bug fix: The command "/prison support submit ranks" was passing a null sender, which is valid when generated by this support tool.**
The fixes now works well, and treats the null basically the same as an OP'd player, or the command being ran from the console.


* **Bug fix. If a null message is sent to this function, it would cause a NPE.**
This now prevents a few failures from causing potential problems.


* **Fixes a concurrent modification exception when the PlayerCacheCheckTimersTask is running.**  
This happens rarely when a player is logging off while "trying" to process their entries; they have been removed. 
So when this happens, the process retries to start over a total of 2 more times and it skips processing players that have already been processed.  Any update that was skipped would be covered in the next pass with no real loss.


* **The use of a command placeholders for `{actionBar}` and `{title}` were added to the placeholder enumeration so they are included in the placeholders listings.**
The support for these two commands were added a while ago, but because they were not added to the enum, they were not being listed in the help.


**3.2.11-alpha.4 2021-11-01**
 Released alpha.4.


* **Changes to improve the way the upcoming mine bombs.**
They are currently non-functional.


* **Adjustments to get block events, such as decays, to work correctly with the new auto feature block event handlers.**
  Block events were moved to be processed after the block is broke.  Also if a block has already been processed, it now will cancel the event to prevent normal block breakage when none should happen.
At this point, the new auto manager appears to be working really well.


* **Changed the location usage with block event placeholders, which now uses the location that is tied to the targetBlock instead of the mined block.**
The mined block may be null so it's not stable.


* **Fixed an issue with /mines block list when an incorrect mine name is used.**
Now displays an error stating the name is invalid.


* **Starting to add some video documents for prison.**


* **Fixed an issue with adding a non-block item to a mine.**
It now validates that the specified item is a block.  Also if a specified block is not in a mine when trying to remove it, it will now display a message indicating that nothing was removed.


* **Major rewrites to how auto features work.**
Using the PrisonMinesBlockBreakEvent object to carry all of the various parameters that are used within the auto features code.  This allowed the elimination of many functions since they have been combined together.  It also allowed for more efficient handling of explosions by combining similar blocks together and processing them as a single unit, so massive explosions are handled far more efficiently.  If the admin chooses to break the blocks in another thread, then handling of many blocks is optimized to reduce the overhead.  The state of the blocks being broken are being tracked through the MineTargetPrisonBlock such that it's flagged as soon as it starts to process the impacted blocks so as to prevent the same block from being processed more than once, even when there are many explosions occurring at the same time. Changes to the block (block break) has been moved out of the depths of the code, to be closer to the event handlers so it's easier to monitor/track.
Due to the many changes and major alterations to the logic, this is a work in progress and needs more testing.


* **Async Mine Reset performance Improvements.** Adjustments were made to improve the performance of the asynch mine resets by providing the ability to fine tune the page sizes, and also provide the ability to reset more than one block in the synchronous thread at a time.  This is called a slice.  Measuring the actual block reset time with nanos for better resolution.
MineTargetBlockKey class was relocated to allow for the use of sub listings on the synchronized block updates.


* **Cloning a bomb was not complete.  Some details were omitted.**


* **Fix for Potion IllegalArgumentException:** Fixed an error with potions in Player inventories when
using sellall sell, potions aren't supported by now and won't be sold, but at least it won't break
sellall anymore.



* **Switched prison block debugging timing to use nanoTime instead of milliseconds since milliseconds is too large of a unit.**


* **Bug Fix: When using block constraints,** there was a common situation where an AIR block was being used in the top layers because all other blocks were being rejected due to chance.  Order of blocks had an impact on this error, when it shouldn't.  Now, if a block cannot be selected, the first block with no constraint issue will be used instead.  Also found a bug in the applying of the chance to each block.  Under some situations, the percent chance was not being reduced for a bypassed block, when it should have.  This now will better select the blocks, and better preserve their intended percentage odds of being spawned.


**Prison v3.2.11-alpha.3 2021-10-18**


* **Enable the ability to choose between setting the block to air inline, or through submitting a synch task to allow the blockBreak event handler to finish quicker, which may reduce lag.**


* **Simplified the configuration of the handling of the block break events.**
Instead of having a separate setting that is a boolean value that indicates it's either enabled or disabled, these are now using the priority value of DISABLED.


* **Add millisecond reporting for the time it takes to handle a block break event.**



**3.2.11-alpha.2 2021-10-14**


* **A few updates to mine bombs.  They have been disabled so they cannot be used.**


* **Add the ability to glow the prison bombs when they are dropped/set.**


* **For a couple of rankup messages, using the rank tag now instead of the rank name.**


* **Fixed a compatibility issue with older versions of spigot.**  Should have possibly use the compatibility classes, but if a method does not exist, then this will fall back on a string matching pattern.


* **Changed the message about worn out tool to use the SpigotPlayer's setActionBar() function to prevent overloading console messages.**


* **Bug fix: Logic was corrected to handle the double negative correctly.**
The was an issue with the /mines set area command when the mine's area was larger than 25,000 blocks.  They have to enter either "confirm" or "yes".  The bug would require them to enter both to create the mine.


* **Added an example of possible backpack object to the PlayerCachePlayerData object.**


* **Adjustments to the new auto features for cancel block break events and block drops.**


* **Removal of some auto feature commented out old code.**


* **New auto features settings: Able to prevent event canceling and also control if the drops are cleared.**
This has not been tested too much, but it may help make prison more compatible with other plugins that are needing to handle the block break events.


* **Fortune on a tool was appearing as a negative value: -1000.**
Not sure how it became negative, but this will better deal with negative values.


* **Added a listener for PlayerInteractEvent.**


* **Add a new feature to the PrisonSpigotAPI to allow for the creation of a new mine through the API.**
This could be used to generate player mines in a plot world.



* **Able to give players bombs, based upon the item type as defined for the bomb.**


* **Significant progress on Prison's Mine Bombs:**
Moved the mine bombs primary classes to the prison core so it's accessible from all packages.
Setup 4 default mine bombs if the commands are used and there are none defined.
Setup a new /prison utils bomb commands to list all bombs and to give players bombs.  These are not complete and fully tested yet.


* **Some initial work to setup the mine bombs configs.**


* **Add placeholders {actionBar} and {title} to the blockEvent listing of placeholders that can be used.**
They are shortcuts for the new prison utils commands.


* **For the actionBar and title, translate the color codes so they work properly.**


* **Hook up the auto features notification to use the new actionBar interface.**
This "should" prevent duplicate messages from being sent to the player while the same message is displayed in the actionbar.


* **Fixed an error about backpacks and lore transition:** A single lore was being used for the backpacks utility, if a server
was new and fresh, this would've been missing and an error could occur, this now got fixed with the full transition.


* **Full transition of all messages to the .properties lang:** All messages are now on the .properties file and the old 
.yml one is unused from now on, you can delete it and start translating the new one. Please note that some messages may
be wrong, as it's still in a young stage and a lot of messages got transitioned.


* **The player cache, when being shut down ran in to a problem if the players were removed when they logged off.**
This function was making a new collection based upon the original player cache copy of players, of which, when a few of the players are removed, then they were resulting in nulls. 


* **Prevented a problem when unloading players, and when a player is null.**
The condition that was causing a null player was that the player was unloaded when the player left the server at the same time when the server was shut down.  Basically a race condition with two parallel async tasks trying to shut down the player cache object, each focusing on a different aspect (player vs. server).


* **Hooked up XSeries' Titles to the compatibility class instead of using version specific code.**
XSeries says they support 1.8.8 through 1.17.1.
Deleted the support for the Spigot110 classes since it was only to support the use of the ActionBar and also the Title, which are no longer needed for 1.10+.


* **Adding a player messaging component to the PlayerCache.**
When used, this will prevent more than one of the same messages from being displayed at the same time.


* **For the command /mines set area the confirmation of "yes" was setup incorrectly with being negated.**


* **Switch over to using XSeries for the actionBar.**
XSeries claims it works for 1.8.8 through 1.17.1.


* **Moved all Lores to the new .properties Language file:** Changes to the old .yml language file about Lore messages
won't take effect, only if you edit the .properties file they will.


* **Added the trigger "minebombs" for the utils command bombs.**


* **Adjustments to the BlockEvents and how it handles some of the event types.**
Expanded and fixed some of the settings for prison's explosions, and PE's too. 
Added the ability to exclude specfic triggers.


* **Updates to the Prison's explosion event handling to correct a few problems. **


* **Fixed SellAll Hand not removing item:** SellAll Hand didn't work properly and got now fixed.


* **Initial setup of Prison's mine bombs.**
Initially it will be controllable as a utils command, so random chances can be assigned to explosions.


* **Cleaned up some of the unused variables in the Utils titles command.**
There were plans for more commands, but they were eliminated.  This will soon be rewritten to utilize XSeries's classes for these display items.


* **Ran in to a situation where results was actually null.  So this prevents a NPE.**


* **Fixed issue with tool's durability being cutoff right before reaching the threshold.**
Had to change a > to a >=.


* **3.2.11-alpha.1 2021-08-31**
- Release the first alpha.1 


* **Replace the block with air through a task to get it out of the auto features thread.**


* **If the settings isPreventToolBreage is enabled, then don't allow the tool to break.**


* **Update some messages to be clearer as to what they are.**
Removed the MONITOR from auto features since they should not have the monitor setting enabled.  The blockBreakEvent has the monitoring event.


* **Trying to fix an error related to SpigotRankManager GUI:** I can't reproduce the issue but the NPE shouldn't
give a stacktrace in the console anymore.


* **If the primary block was null, which it never should be, then this prevents a failure in this section of code in the OnBlockBreakEventCore.**



* **For the initial startup air count task, which is used to "reset" the block counts on a mine.**
This does not change any blocks, but just finds out where the mine was when the server was last shut down.  This is needed to ensure we have valid counts for the mines before the first time they are reset.  The other way to update these values is to do a full mine reset which is more costly.
There was an inconclusive error that just listed "null" as the error messags, without identifying the actual line number.  This error catching was changed to now generate a stack trace so it can be properly fixed if it occurs in the future.


* **Added a few more reporting entries on the block break handling.**
Reporting how many blocks are being processed and if it passes the validation phase.


* **Some fixes for teleporting and the removal of the teleport glass block.**


* **Updates to the PrisonEnchant's API.**
Minor adjustments to work with the new API from PrisonEnchants.


* **Updates to async block updates.**
Included changes to hook up the CustomItems to work with the async updates.


* **Clarify some of the messages related to listing of the block events.**


* **Added the ability to identify if a block is able to be affected by gravity.  Also the mine has a global setting to identify quickly if any block is gravity affected.**
This will be used to alter the mine reset strategy to improve performance so as to hopefully eliminate long resets due to extensive lag from falling blocks.  The idea is to get all the other blocks in to place before placing the falling blocks to ensure they are less likely to fall.


* **If the Mine's saved file data is corrupted (manually edited with incorrect data), this will prevent the mine from being loaded and will now generate an error message indicating which mine has a problem loading.  It will print out the invalid data, and it will default to a value of 0.00001.  The function has been updated to "properly" use the localized format, so if it saves in a non US way, then it should now be able to read it back in and parse it correctly.


* **If the Mine's saved file data is corrupted (manually edited with incorrect data),**
this will prevent the mine from being loaded and will now generate an error message indicating which mine has a problem loading.  It will print out the invalid data, and it will default to a value of 0.00001.


* **Checking to ensure the locations are not null when loading.**
There was a failure with bad data on the files system that was resulting in trying to resolve nulls to a location, which obviously cannot happen.


* **There was an odd situation where the player was null, when usually they never can be, so this helps prevent possible errors.**
The null was caused by an issue with a placeholder?  Don't really remember.


* **Adjustments to Prison's TPS calculations.**
They were only taking the average of just two readings which was resulting in very unstable TPS values. Now 10 are being used.
Enabled a new feature where the resolution can be changed from normal (a reading every tick) to high resolution (one reading every 2 ticks). When the resolution changes, the task will auto terminate and resubmit with the new settings.


* **For the command /ranks autoConfigure made some adjustments to the block lists being used so the top mines have more valuable ores and blocks.  There was a shortage and the wrong blocks were being used.


* **Fixed an auto features config setting for prison's ExplosiveBockBreakvents priority; it was missing the word priority.**
Reworked some of the details on autofeatures as displayed through /prison version to update them to better reflect the correct settings and dependencies.


* **Fixed a problem with placeholders when using the search feature, but not supplying a player's name.**


* **some internal changes to improve the resets**


* **eliminate the block access in this class since it handles everything in the submitted task.**
This was causing an error when it was being ran in an async thread.  When the task is submitted, it is ran synchoronously so it works correctly.


* **Changed prison's TPS calculation to be able to enable a high-res setting when the `/mines stats` is enabled.**
The one problem with enabling high resolution mode is that it could show an unrealistic low TPS during a reset.  The /lag command shows a much higher TPS value.  


* **Setup up the basics for async updates.**
In the code that does the update, it also now reads the block to ensure that the read and update is all done in the synch thread.  Otherwise the old code would be risking chunk loading in an async thread.
Using the Location and World to perform the async updates outside of needing access to the spigot module.
At this time, only `/mines set tracer` is using the new async reset.


* **Tweaks to the event listener dumps for block breaks.**
Updated the notes about prison's listeners.
PEExplosionEvent was setup with the wrong forClass name.


* **Setting up a new way to handle block updates in prison.  Adding functions that are intended to be use while running in an async threads.**


* **Transitioning over to the correct way to get the compatibility object.**
Just a few classes are using the old way, but they will be switched over when they are done with the edits.


* **Fixed the way some of the language files were being generated**
so it can include the spigot module, which makes it self-contained for actual Modules, since it's always based upon the module name. 
Core and spigot are not technically modules, so they have special setups.
Changed the Module folder from dataFolder to moduleDataFolder so it would not conflict with the SpigotPrison object.


* **Fixed  a problem with Prison's ExplosiveBlockBreakHandler**
 in that it has a typo in the getHandlerList() and was not included with the registration processes. It also needed to be included with the generation of the listener dumps.


* **Added /sellall hand command.**


* **Minor changes to SellAll Util.**


* **Much better performance for SellAll generally.**


* **SellAll Commands internal changes.**


* **Minor changes to GUIs:** Some fixes and visual changes.


* **SellAllUtil Rewrite:** New internals for SellAll and SellAll API.




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
  
