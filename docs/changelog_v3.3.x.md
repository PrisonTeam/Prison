[Prison Documents - Table of Contents](prison_docs_000_toc.md)

## Prison Build Logs for v3.3.x

## Build logs
 - **[v3.3.0-alpha - Current](changelog_v3.3.x.md)**
 - [v3.2.0 through v3.2.10](prison_changelogs.md)
 

These build logs represent the work that has been going on within prison. 


*Will continue as v3.3.0-alpha.7 2021-06-?? in the near future.*


# 3.3.0-beta.2 (alpha.7) 2022-02-07


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
  
