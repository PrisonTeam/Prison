
### Prison Documentation 
[Prison Documents - Table of Contents](prison_docs_000_toc.md)


See below for information on:
- Setting up TokenEnchants
- Setting up CrazyEnchants

- Auto Manager Settings - Block Event Listener priorities and their events


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">

# Auto Manager

Other content is coming soon...


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


# Auto Manager Event Listener Priorities

Prison listens to a number of block break event types, and each one can be enabled or disabled.  By enabling these listeners, any combination of priorities can be applied to any of the listeners.

Valid values for the priorities are:

- `DISABLED` - This turns off all processing for this event type.
- `LOWEST` - Allows Prison to be one of the first plugins to process the event.
- `LOW` - This generally is the suggested setting since it allows other plugins to use LOWEST if they need to.
- `NORMAL`
- `HIGH`
- `HIGHEST`
- `BLOCKEVENTS` - This priority is basically the same as MONITOR, but it will also run the Prison blockEvents within the mines.
- `MONITOR` - This priority only allows Prison to record which blocks were broken in the mines.  It will update the counts for the mines and the players.  This setting will NOT run any Prison blockEvents.


Valid event listeners are as follows, including their default values:

- `blockBreakEventPriority: LOW` - This applies to the org.bukkit.BlockBreakEvent and is the primary way Prison deals with standard block events. There may be some situations where prison would need to DISABLE this listener.  
- `ProcessPrisons_ExplosiveBlockBreakEventsPriority: LOW` - This is the event that must be enabled for Prison Mine Bombs to work.  Other plugins may also use this Prison multi-block explosion event too.
- `TokenEnchantBlockExplodeEventPriority: DISABLED` - For TokenEnchant (premium)
- `CrazyEnchantsBlastUseEventPriority: DISABLED` - For CrazyEnchant (open source)
- `ZenchantmentsBlockShredEventPriority: DISABLED` - For ZenChantments (open source)
- `PrisonEnchantsExplosiveEventPriority: DISABLED` - Pulsi's Plugin - (Premium and free... not currently in active development for at this time)


Do not enable any event listener if you are not using that plugin.  Doing so will not contribute to lag, but it will try to setup a useless event listener that could delay startup, and consume a little more memory.


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


# Auto Manager's Canceling the Event vs. Canceling the Drops

Prison's primary way of dealing with BlockBreakEvents, and other block events, has always been done by the canceling the event.


Note: Setting this behavior will effect all block break event listeners.  There is no way to customize it for each event type.


This was the original way of handling block breaks, and is the ONLY way of dealing with block breaks for all spigot versions older than v1.12.x.  Since this is a feature within Spigot (I don't think it is a bukkit feature), there is no way to provide this newer, and better way, of dealing with block breaks in the older versions.  If you try to enable this on older versions of Spigot, then each time the server is started, the setting will be "forced" off and then an error message will be printed to the console too.


You can still cancel the events with newer versions of Spigot, but if you run in to compatibility issues with other plugins, then it may be the easiest solution.


**Canceling the block break Events:**

This is the default settings for Prison's Auto Features.  This is the setting that must be used on Spigot versions prior to 1.12.x (Spigot 1.8.x through 1.11.x).  It can also be used on newer versions of Spigot, but it may cause compatibility issues with some other plugins that need to monitor the event, which canceling the event will cause those plugins to ignore the event.


The idea behind these settings, is that prison will break the block (set it to AIR) and then cancel the event.  Since the event is canceled, all other plugins will ignore the event and will not perform any processing on it.


When the event is canceled, the drops should not be canceled.  If the drops are canceled, then there may not be issues, but to be safe, make sure it's not canceled.


```
options:
  blockBreakEvents:
    cancelAllBlockBreakEvents: true
    cancelAllBlockEventBlockDrops: false
```


**Canceling the block drops:**

This setting is for Prison's Auto Features and **only** works with Spigot versions 1.12.x and higher.  By canceling the drops, it allows other plugins that are listening after Prison, to be able to process the block break event too.  These are generally plugins that gather stats on the block break event, without contributing to the drops. 


The idea behind these settings, is that prison will not break the block (set it to AIR), but instead, it will set the drops to zero.  Since the event is not canceled, other plugins can process it as normal.  Bukkit will then break the block normally when all plugins are done listening to the event, but since the drops have been canceled (set to zero), it will not drop anything.  This allows prison to provide the drops, either normally by dropping them, or through auto pickup. 


Some of the plugins that can benefit from this behavior are: McMMO, Quests, and other plugins that may count block stats.  These plugins do not modify the drops, but monitoring the events will allow them to work normally.


When the drops are canceled, then event must not be canceled.  If the event is canceled, then the drops could be duplicated one or more times.


```
options:
  blockBreakEvents:
    cancelAllBlockBreakEvents: false
    cancelAllBlockEventBlockDrops: true
```



<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


# Prison's Auto Manager - Setting Up and Enabling Other Plugins


Prison's Auto Manger deals with the whole block breaking events.  It's able to provide advanced features such as auto pickup, auto smelt, and auto block, along with providing the player with XP, applying OP fortune, plus many other features.  All with maintaining full compatibility from Spigot v1.8 through Spigot v1.16.


Auto Manager is a very complex "process" and as a result, there are many features that can be configured, and many possible interactions with other plugins.  Needless to say, there are many things that can go wrong, especially when it may not be configured correctly.


This document tries to cover some of those configurations and settings in other plugins to get things working at their best.


This document contains information on how to configure TokenEnchant and Crazy Enchant  


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


# To get TokenEnchant's TEBlockExplosionEvent to work with prison, you must follow these settings:


The information presented here applies only to the latest release of Prison.  There has been many changes related to Prison's auto features that it's next to impossible to cover all past versions of prison.  


Prison's preferred choice of setting up access to the mines and the ability to TP to the mines, is through the use of the linked Ranks.  Prison is able to handle all of the access details for players, without the need to setup permissions and without setting up WorldGuard regions to grant the players access. In the past, with older versions of prison, that was the only way to control mine access, unless the mines were physically isolated from each other through barriers such as void worlds.


Although prison is able to manage it's own access, Prison cannot manage access for other plugins, especially if those plugins utilize API calls directly to WorldGuard to identify what regions a player has access to.  Therefore, when setting up TokenEnchant, you must also setup WorldGuard regions.  


The following instructions will explain how to enable Prison's auto features with auto pickup, and allow Prison to handle TE's multi-block explosion events.  These instructions cover the TE's plugin **TE-ExplosiveEnchant-8.x.x.jar**, but they also apply to other TE Plugins that may utilize the TE's **TEBlockExplodeEvent**.


If you would like have TE handle the auto pickup, or the drops, then you will have to modify these instructions on your own.





**Please Note:** Event priorities have been added to prison. Changing the event priorities within Prison may make it easier to get prison to work better with TE instead of having to change the event priorities on TE.  If the following directions do not work well for you, then try changing prison's event priorities for the events Prison is listening to.  There isn't any documentation to explain how to figure out how priorities on the listeners should work, but in general, the first plugins that lisent to the events has the first chance to do something with the events, but the last plugins have the final say if they want to undo, or override something. This is just letting you know that this may be an option to play with to get things to work.



**Notice when using Spigot Versions 1.12.x and newer:** Prison now supports drop-canceling, which is a new feature in bukkit 1.12.x and newer.  This allows you to turn off Prison's event canceling, and instead, cancel the drops associated with breaking the blocks.  How this will work, is that prison will provide auto-pickup on the drops, but it will set the bukkit drops to zero, and then let bukkit naturally break the block when all other plugins are done "listening" to the break.  Setting it to zero will prevent bukkit from doubling the drops, and prevent other plugins from doubling the drops too.  Older versions of prison did not have this feature, so it was more difficult to get prison to play-well with other plugins.



<h3>General WorldGuard global region settings</h3>

- **WorldGuard:** Enable the WorldGuard's setting for __global__ to prevent block breaks within the world where the mines are.
- **WorldGuard:** Define a **simple region** in the mine the same size as the mine. Name it as suggested at the top of this document, such as prison_mine_c as an example.  The "simple" region only includes defining the region, setting the priority to 10, and add a permission group member. Example: `prison.mines.a`.  You may add the other flags if you want, as suggested at the top of this document.
- **WorldGuard:** Please note: Sometimes you might have to add the WorldGuard flag **block-break** in order to get this to work. `/region flag prison_mine_<mine-name> block-break allow` It may be a WG version issue.  Spigot v1.8.8 requires this flag, while Spigot v1.15.2 does not appear to require it.  Your success may vary.  It may also be a good idea to add it either way.



<h3>General permissions for the WorldGuard regions</h3>

- **Player Permissions:** You may have to first create the group within your permission plugin before you can assign it to a player.  Example using LuckPerms: `lp creategroup prison.mines.a`
- **Player Permissions:** The player must have the permission that is tied to the afore mentioned region.  For example the group perm: `prison.mines.a`  And assign it with: `/lp user RoyalBlueRanger parent set prison.mines.a`



<h3>Configuring TokenEnchant</h3>

- **TokenEnchant:** TokenEnchant must be configured properly to enable Explosive enchantment. Setup TokenEnchant as you would normally. Download and place the **TE-ExplosiveEnchant-8.7.0_4.jar** in the `plugins/TokenEnchant/enchants/` directory.  Start the server to have TE generate the configuration files related to that enhancement.

- **TokenEnchant:** TokenEnchant must not process the TEBlockExplosionEvent and the default settings must be turned off.  TE's auto pickup is defined within TE's config file: `plugins/TokenEnchant/config.yml` at bottom with the two settings: `TEBlockExplodeEvent.process: true` and `TEBlockExplodeEvent.pickup: true` as default values.  Both of these must be set to `false` for this to work with prison.

- **TokenEnchant:** In order for TEBlockExplosiveEvent to be fired, the `plugins/TokenEnchant/enchants/Exposive_config.yml` must be adjusted. First run your server to generate this file if you have not done so yet. Find `Potions.Explosive.occurrence: random` and change it to `always` for testing purposes. Then locate the setting of `Potions.Explosive.event_map.BlockBreakEvent: HIGHEST` which is the default value (HIGHEST), and change it to a value of `LOWEST`.  Failure to change this setting will result in the failure of TE from being able to fire the TEBlockExplodeEvent.

- **TokenEnchant:** To create a pickaxe with an explosion enchant do the following: From within the game, give yourself a diamond pickaxe (`/give <playerName> diamond_pickaxe 1` or `diamondpickaxe`) and enchant it with the command `/te enchant Explosive 1` or up to a level of 10. Run as OP, or if you need tokens to use, give yourself some from the console with `/te add <playerName> 100`.



<h3>Configuring Prison's Auto Manager</h3>

- **Prison: Auto Manager:** Within the configuration file `plugins/Prison/autoFeaturesConfig.yml` change the setting `options.blockBreakEvents.TokenEnchantBlockExplodeEventPriority: LOW` to a value of `LOW`.  The default value is `DISABLED`, but double check to ensure it set correctly.  To disable any event listener, just set the priority to `DISABLED`.

- **Reload Auto Manager's settings:** If you make a change to the auto manager's settings, you can reload them with `/prison reload autoFeatures`.  You don't have to restart the server.

- **Prison: Auto Manager:** Enable **Auto Features** and **Auto Pickup** and all blocks from the explosion event will be placed in the player's inventory.  You can also enable the auto sell on each block break, which may improve the performance when using massive explosions (over 500+ blocks, and even over 30,000+ blocks).

- **Prison: Auto Manager:** If **Auto Features** is disabled (which means auto pickup is also disabled), then the blocks still must be processed and broken by prison to prevent blocks outside of the mine from being broken.  To enable prison to drop blocks normally within the mines, while honoring the TE Explosive event, then you will have to enable the setting `isProcessNormalDropsEvents: true` (set to true).  Prison will calculate the drops and will drop them where the blocks were originally (not at the player's feet). The internal calculations that prison uses for the drops are the same calculations it uses for the auto pickups.



<h3>Testing the configurations</h3>

- **Testing:** Deop yourself if you are testing this so you will not break blocks outside the mine.

- **Testing:** Test with a regular pickaxe, and also a TE Enchanted pickaxe.  Also test with Prison's auto features turned on (auto pickup) and off.  Ensure the setting `isProcessNormalDropsEvents: true` is enabled when auto features is disabled.  This setting can be left on even when auto features is enabled since auto features will override it.

- **Enabling Prison's debug mode:** If the blocks are not breaking as expected, or you're seeing a puff of smoke and hearing an explosion, but no explosion is happening, then you may want to try some of prison's debugging features to see what is going on internally.  Prison has a few that can really be beneficial, but it may help to contact our discord server to get help using them too.

    - **Prison Debug Mode - Enabling and Disabling it:** Using the command `/prison debug` will toggle the primary debug mode on and off.
    
    - The idea is to enable debug mode, break a few blocks, then turn it off.  Prison has a "countdown" feature on debug mode, where you can tell it how many debug statements it should log, and then it will turn itself off automatically.  To enable the limited debug mode, that will turn itself off, add a "count" to the end of the command such as: `/prison debug 10`.  Sometimes, on a busy server, you may have to increase this value to capture the event your looking for, since other players may generate debug statements that are not relevant.
    
    - **Prison BlockBreak Listeners:** Sometimes the order of the blockBreak listeners in other plugins can get in the way and prevent Prison from working, or other plugins.  Use this command to list all listeners in the order in which they will respond to the event. `/prison support listeners blockBreak`
    
    - **Block Inspection:** When prison is in debug mode, you can use the **Prison wand** (give wand with `/mines wand`) to inspect what a block's name is (left or right click the block).  The wand will also set points one and two for creating a new mine too, but ignore that if you want.  If you "sneak-click" a block with the wand, using the shift key, then prison will simulate a block break event, but will monitor the before and after states of the event for each plugin that is listening.  This could tell you which plugin canceled the event, or processed it, or changed what it will drop.  The details will be printed in the console if you do not see them ingame.
    
    
  
#####  **WARNINGS:**
  - If anyone is OP'd then they can break blocks outside of the mine through the TE event. This is the result of WorldGuard bypassing the restrictions on the regions and has nothing to do with prison.
  - If TokenEnchant is set to handle the explosion events, with or without their auto pickup enabled, then TE will break blocks outside of the mine and prison will NOT be able to control that.  If you have those settings enabled, then that is outside of the control of prison and you assume all risks of breaking and destroying builds around the mines.


  

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">




# To get Crazy Enchants' BlockExplodeEvent to work with prison, you must follow these settings:



Please follow the directions for Token Enchant explosion that are listed above.  


But ignore everything that is listed for Token Enchant since Crazy Enchants is much simpler to configure. At this time I am not aware of any special changes that you need to make to the Crazy Enchant's configurations.


To enable the processing of the Crazy Enchant BlockExplodeEvent enable this configuration: 
`CrazyEnchantsBlastUseEventPriority: LOW` 

Enable by setting to any value other than `DISABLED`.



<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# BlockEvents - Fortune Features


Prison provides a few options for controlling fortune.  But before those options are discussed, the process of how fortune is calculated may be a very important topic to understand since it's not a simple calculation.


Initially, prison uses Bukkit's Block.getDrops() function to get a set of ItemStacks of the drops related to the block that is being broken.  These drops are not fully inclusive as you would find within a vanilla game, but are somewhat limited, and they do vary from one version of spigot to another (1.8 vs 1.16 could be drastically different).


So to correct for omissions, such as a percent chance flint would be dropped with mining of gravel, and to adjust for the limited range of fortune within the vanilla server, prison has to make adjustments. 


With Fortune, the standard is for fortune 1 through 3.  But with OP prison setups, Prison needs to support higher fortune levels.  


With a standard block break event, you break one block and you get one drop. That is standard without fortune.  So the way Prison detects if fortune is applied, is if there is more than one block provided for a drop.  With fortune 3 being the highest possible level to influence the drops, Prison checks the tool to see what it's fortune level is, then uses that within the calculations.


So if the tool has a fortune level of three or less, then Prison does nothing with the drops since everything is already taken care of.  But if the fortune level is greater than 3, then Prison must make adjustments to the drops.   


The way prison extends the drops for higher fortune levels for tools with a fortune over three, is to divide the standard drop by three (since it maxes out at three), then multiplies that value by the actual fortune level on the tool.  The reason is that all of the random variables that went in to calculating the standard drop have already been applied, so all that needs to be done is to extend the total drop amount to cover the tool's actual fortune level.  Since a fortune 3 has a higher chance to adding more to the drop compared to a fortune 1, then we can assume those random chances are reflected in the standard drop.  This also means prison should not apply a random chance to the drops since that chance was already applied. 


One issue with extending the fortune on the standard drops is that the amounts can appear to be much higher than what would be expected.  So prison provides a way to apply a multiplier to the results to pull down that generated value.  


**If Auto Pickup is enabled**

* Bukkit's Block.getDrops()
* If drops == 1 then no fortune has been added.
  * If tools fortune enchantment > 3 then apply
* If drops > 1 then fortune has been already applied by bukkit
  * If tool's fortune enchantment <= 3, then do not apply fortune
  * If tool's fortune enchantment > 3, then need to calculate the additional fortune.
    * Formula: drops / 3 * tool_fortune_level = raw_adjusted_drops
    * Formula: raw_adjusted_drops x 


**If Auto Pickup is disabled, and Process Normal Drops is Enabled**



<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# BlockEvents - Silk Touch Features


At this time, Prison does not provide any support for silk touch.  Internally the basics are in place, but there has never been a request to enable anything related to silk touch within the mines.


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


# BlockEvents - Durability (Wear) Calculations

Sometimes the durability on the tools used for mining are applied, and sometimes they are not. 

Prison has the ability to enable durability calculations to add wear to the tools, if needed. If bukkit is applying the durability calculations and wear, and if the prison calculations are also enabled, then too much durability wear could be applied to your tools (a doubling effect).

When it is calculated to apply wear to the tools, it only adds 1 to the total durability on the tool.  When the durability, or wear, exceeds the max durability level for the tool, then the tool will break.

The tool's durability level will impact, and reduce, the chance to add wear to the tool.  Also if the tool has the Durability Resistance enchantment, levels 1 through 100, it too will make it more difficult to add wear to the tool.  If the Durability Resistance enchantment has a level of 100 (or no specified level assumes to be 100), then no wear will ever be applied to the tool.

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# BlockEvents - XP

Prison has the ability to give the player XP for mining certain kinds of blocks.  The XP can be give either directly, or though the generation of XP Orbs.

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# BlockEvents - Tool Lore Counter

Prison provides block break counts on tools through the use of lore, if enabled.  This will track how blocks a tool has broken over it's life span.


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


