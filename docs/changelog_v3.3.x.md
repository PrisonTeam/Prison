[Prison Documents - Table of Contents](prison_docs_000_toc.md)

## Prison Build Logs for v3.3.x

## Build logs
 - **[v3.3.0-alpha - Current](changelog_v3.3.x.md)**
 - [v3.2.0 - 2019-12-03](prison_changelog_v3.2.0.md)&nbsp;&nbsp;
[v3.2.1 - 2020-09-27](prison_changelog_v3.2.1.md)&nbsp;&nbsp;
[v3.2.2 - 2020-11-21](prison_changelog_v3.2.2.md)&nbsp;&nbsp;
[v3.2.3 - 2020-12-25](prison_changelog_v3.2.3.md)&nbsp;&nbsp;
[v3.2.4 - 2021-03-01](prison_changelog_v3.2.4.md)&nbsp;&nbsp;
[v3.2.5 - 2021-04-01](prison_changelog_v3.2.5.md)&nbsp;&nbsp;
[v3.2.6 - 2021-04-11](prison_changelog_v3.2.5.md)
 

These represent the work that has been done on prison. 



# v3.3.0-alpha.2 2021-04-30


* **Bug fix: limit when the MineSweeper task will run.**


* **Prevent negative values in a placeholder when the player has more money than what is needed to rankup.**  
Since the placeholder is identifying how much is needed before ranking up, it only makes sense that once they can afford it, then they won't need more, so the value is zero.


* **Added a new document for prison APIs.**
It's a start, but there is a lot more that needs to be added.


* **Configs support for booleans**
Configs now support booleans values, if you delete your old ones they will work.
  NOTE: There may still be some booleans as strings, please report them and I'll slowly try to remove them.


* **Updated a few repos:**
XSeries, placeholderAPI, be.maximvdw.MVDWPlaceholderAPI, net.milkbowl.vault, me.lucko.luckperms.luckperms-api
NOTE: org.bstats.bstats-bukkit v1.7 has a source code change over v1.5


* **Backpack option for item on join when limit is set to 0.**
It's now possible to give or not the backpack item to open it on join, depending on if
  the limit is 0 or not, by default if the limit is 0 you won't have access to create a backpack and the item.


* **Backpacks list GUI limit 0 message.**
If a player have a limit of 0 backpacks, instead of opening an empty GUI he'll get an info
  message telling him that he can't own backpacks.


* **Backpack list add backpack button FIX.**
The backpack add button won't show if the backpacks limit for a player is set to 0.


* **Backpack admin player list NPE fix.**
Fixed an NPE of the admin backpacks GUI list.


* **Change to the maven URLs to pair them to the implementations.**
Not sure if this will help, but it will help narrow which maven repos are related to the resources.
There's been a problem where repos go off line and then gradle always tries and fails to locate the resources in the apache commons repo.


* **Changes to the teleporting of players.**
This fixes a potential issue with suffocation events locking a player in to an endless loop. 
This disconnects the teleport event from the suffocation event canceling which may help address this.  The teleport event is submitted to run 3 ticks in to the future so it can also allow the suffocation event to be canceled fully.
These changes also places the user one block higher above the mine so their feet are not within it.  This will help prevent the triggering of the teleport since they are no longer within the mine.  When teleporting one block higher, it will spawn a glass block under them, then that glass block will be removed in about 10 ticks.


* **New /backpack limit decrement command.**
This new command is just like /backpack limit add but it does the opposite, instead
  of incrementing the limit it will decrement. Format: 
  - /backpack limit decrement <Player> <DecrementNumber>, for example if you have 3 backpacks
  as a limit, /backpack limit decrement GABRYCA 2, will set my new limit to 3 - 2 = 1.
    Multiple backpacks must be enabled to use this feature.


* **New /backpack limit add command.**
This new command is just like /backpack limit set but instead of setting a new size
  it will increment it of the number specified, format:
  - /backpack limit add <Player> <IncrementNumber>, for example if you have 2 backpacks as a limit
  then execute /backpack limit add GABRYCA 3, your new limit is 2 + 3 = 5.
    Multiple backpacks must be enabled to use this feature.


* **New /backpack limit set command.**
New command to set the amount of backpacks that a player can own, the format is:
  - /backpack limit set <player> <number>, for exaple /backpack limit set GABRYCA 2 will let
  me own only 2 backpacks if multiple backpacks is enabled.


* **Backpacks DATA file structure reworked.**
Backpacks data FILE structure got edited quite a lot for some new features, more customizations
  and hopefully less bugs.
  NOTE: Upgrading from an older version may make the admin commands unusable, to fix this you need
  to open at least one time that backpack and it will convert to the new structure, but stability or bugs
  isn't guaranteed, the best way to avoid issues is to delete the whole backpacksdata.yml config, but
  obviously you'd loose all the backpacks.


* **Elminate all references to the config setting `use-new-prison-block-model` except for the one in the SpigotPlatform object.**
This will allow for easier switch over to the new block model since it will be only one place that needs to be changed.  When switched over, it will support an alternative settings that will allow the forcing of the old block model.


* **Enhanced the conversion to the new block model to preserve the constraints that were added under the old block model.**



* **3.3.0-alpha.2 2021-04-23**


* **added a new prison utils materialsearch that will allow the admin to search to see what materials are available.**
Multiple search words can be used.  such as "gold axe" which will return an axe and pickaxe.


* **More warns and less errors**
GUI won't give you errors when something is wrong, but just warns, so they're
  less allarming.


* **Backpack won't break if size is set to 0**
The Backpack won't break if the size is set to 0, it will open anyway with a minimum size of 9, 
  this's because of an IllegalArgumentException error from Bukkit.


* **Refactored the extraction of the server's version within BlueSpigetSemVerComparator so it could be included in a unit test to help ensure a specific issue with paper v1.15 was not an error within prison.**


* **No need to use the singleton getInstance() since the code is already running in that very same instance: SpigotPrison.**


* **Bug fix: The /mines blockEvent list was generating a "suggest" that was converting the & in the color codes to their raw value.**
The command `/mines blockEvent remove` now uses line number like the other commands so you no longer need to specify the whole command, just the line number.


* **Added FastAsyncWorldEdit to Prison's soft depends.**


* **Fixed a SellAll NPE**
If Ranks are disabled, sellall can't be used, instead of giving an error now it will
  just tell you that Ranks are disabled in a message.


* **Added /sellall delaysell command**
This command's enabled by default and if triggered will work just like sellall sell but
  it will start a countdown that won't tell you how much did you earn immediately but
  will only at the end of it, if the same command or sellall sell is triggered during this,
  the amount of money earned will increase and will tell you at the end the total amount.
  

* **Added a 3d distance on the Bounds to better track player movements when using tp warm ups.**
It was using 2d distance, ignoring the y axis.


* **v3.3.0-alpha.1 2021-04-16**


* **New feature: Add a tp warmup that is enabled within the config.yml file.**
Able to configure the max distance the player can travel before the tp event is canceled.  Also can specify the warmup length of time, in ticks, that is used.


* **Remove the references to mySQL and mongoDB from config.yml file since these never existed and probably won't ever exist any time soon.**


* **Add a function to get a double config value from the config.yml file.**


* **Fixes an issue if the json array for resetWarningTimes has a trailing comma, which will result in a null entry.**
So the fix purges out any nulls, no matter where they may be.
Also if there are no entries, it sets the reset warning times to a single entry that is one year long.  That will ensure no warnings happen, and this assumes that it was an attempt to disable the warnings.


* **Backpack size permission**
Added a permission to set a _custom backpack size of a player_, it works just like permission multipliers,
  you just need to use this permission: **prison.backpack.size.<number>** replacing <number> with
  the custom size, **multiple of 9** that you want to use for your backpack, **example: prison.backpack.size.18** will
  make backpacks of that player with a size of 18 slots/2 rows.


* **Logic error on calculating the units for placeholders.**
Was using <= instead of just < so it would produce a result of "1,000.0 K" instead of "1.0 M".


* **Setup the auto feature's sellall on each block mined to suppress notifications and sounds.**


* **Fixed a thread safety bug with the sign usage variable within the singleton.**  
Basically it would have failed to identify a specific player as to using a sign.  If there were many players online and they all performed a sellall event, but only one using a sign, then any of the other players who were not using the sign would have a race condition and the first one to be processed would be identified as having used the sign.


* **Also provided a way to suppress the notifications so the command can be used in silent mode, which would be beneficial for a per-block use of sellall.**
 When notifications are suppressed, it suppresses the text messages and the audio.


* **v3.3.0-alpha.0 2021-04-11**

  Start on the alpha.1 release.
  
