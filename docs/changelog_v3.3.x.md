[Prison Documents - Table of Contents](prison_docs_000_toc.md)

## Prison Build Logs for v3.3.x

## Build logs
 - **[v3.3.0-alpha - Current](changelog_v3.3.x.md)**
 - [v3.2.0 through v3.2.10](prison_changelogs.md)
 

These build logs represent the work that has been going on within prison. 


*Will continue as v3.3.0-alpha.7 2021-06-?? in the near future.*


# 3.2.11-alpha.1 2021-09-02


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
  
