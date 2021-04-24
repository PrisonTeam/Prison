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



# v3.3.0-alpha.2 2021-04-23


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
  
