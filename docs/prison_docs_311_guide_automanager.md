
### Prison Documentation 
[Prison Documents - Table of Contents](prison_docs_000_toc.md)


See below for information on:
- Setting up TokenEnchants
- Setting up CrazyEnchants

- Auto Manager Settings - Block Event Listener priorities and their events


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">

# Auto Manager

AutoManager is Prison's block handler.  It provides support for breaking blocks within mines, auto pickup, normal drops, fortune, mine access, block events, auto-sell, and many other features.  Auto Manager has been one of the most enhanced features of Prison, and what it is capable of doing is probably more that what has been documented in this document due to the complexities involved, and the number of new features added that have not made it to this document.

Extensive work has been done with auto manager to improve the performance, and to extend the compatibility with other plugins.  Many plugins have direct support (Auto Manager has event listeners for their custom events), but prison has been modified to work with many plugins that do not provide custom events too.


*Documented updated: 2023-02-28*

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


# Auto Manager Event Listener Priorities

Prison listens to a number of block break event types, and each one can be enabled or disabled.  By enabling these listeners, any combination of priorities can be applied to any of the listeners.  These priorities cover the standard Bukkit priorities (LOWEST to HIGHEST, plus MONITOR), but also has custom Prison priorities to extend the behaviors.
 
At this time, these event listeners only apply within mines.  In the near future they may be extended to work outside of mines and in other worlds too, but for now, they are only Prison mine specfic.


Valid values for the priorities are:

- `DISABLED` - This turns off all processing for this event type.
- `LOWEST` - Allows Prison to be one of the first plugins to process the event.
- `LOW` - This generally is the suggested setting since it allows other plugins to use LOWEST if they need to.
- `NORMAL`
- `HIGH`
- `HIGHEST`
- `BLOCKEVENTS` - This priority is basically the same as MONITOR, but it will also run the Prison blockEvents within the mines.
- `MONITOR` - This priority only allows Prison to record which blocks were broken in the mines.  It will update the counts for the mines and the players.  This setting will NOT run any Prison blockEvents.


- `ACCESS` - This is a new priority, where prison will check to see if a player has access to a mine, and if not, then it will cancel the block break event.  If they have access, then Prison will ignore the event and will not process anything else.  This `ACCESS` is designed to be used in place of WorldGuard regions if your other plugins do not require the use of WorldGuard regions.  This priority does not count any block breakage.  This `ACCESS` prison priority uses an event priority of `LOWEST` so it is one of the first plugins to check if a player has access to break the blocks.

- `ACCESSMONIOR` - Same as `ACCESS` but adds a second listener to the stack for a `MONITOR` priority too.  So this generates two listeners; see both `ACCESS` and `MONITOR`.

- `ACCESSBLOCKEVENTS` - Same as `ACCESS` but adds a second listener to the stack for a `BLOCKEVENTS` priority too.  So this generates two listeners; see both `ACCESS` and `BLOCKEVENTS`.



Valid event listeners are as follows, including their default values.  Any of the above Prison priorities can be used with the following:

- `blockBreakEventPriority: LOW` - This applies to the org.bukkit.BlockBreakEvent and is the primary way Prison deals with standard block events. There may be some situations where prison would need to DISABLE this listener.  
- `ProcessPrisons_ExplosiveBlockBreakEventsPriority: LOW` - This is the event that must be enabled for Prison Mine Bombs to work.  Other plugins may also use this Prison multi-block explosion event too.
- `TokenEnchantBlockExplodeEventPriority: DISABLED` - For TokenEnchant (premium)
- `CrazyEnchantsBlastUseEventPriority: DISABLED` - For CrazyEnchant (open source)
- `RevEnchantsExplosiveEventPriority: DISABLED` - For RevEnchants ExplosiveEvent (premium)
- `RevEnchantsJackHammerEventPriority: DISABLED` - For RevEnchants JackHammerEvent (premium)

- `ZenchantmentsBlockShredEventPriority: DISABLED` - For ZenChantments (open source)
- `PrisonEnchantsExplosiveEventPriority: DISABLED` - Pulsi's Plugin - (Premium and free... not currently in active development for at this time)


Do not enable any event listener if you are not using that plugin.  Doing so will not contribute to lag, but it will try to setup a useless event listener that could delay startup, and consume a little more memory.




<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


# Sync Tasks for Breaking Blocks

Prison is trying its best to optimize performance so your server will have the best experience with no lag.  In order to provide the best performance, some features do not always work the best on all servers due to memory limitations, processor performance, huge number of plugins, or almost any other variable which is outside of the control of Prison.  Therefore, there are some performance features that work really well on some servers, but not on others.  This is not a "problem" with any specific plugin, or server, but it's an expected behavior because all servers are different and they can perform in very different ways based upon configurations.

One way Prison is able to improve performance when breaking blocks, is to process as much as it can within the block break events, except for breaking the actual blocks, and for that, prison submits a task that performs the actual block breakage.  Since the act of breaking the blocks can be considerably slower than performing the calculations that lead up to the block breakage, it has been shown that by submitting the breakage as a task will improve the overall performance when you have very high over powered tools, and when there are many players online mining.

But like everything else, there is a chance that because the block breakage is submitted as a task, that it can lead to some perceptions of lag.  Its not actually causing lag on a server, but since there can be a longer delay between the calculations and the actual breaking of the blocks, it can appear as if it is lag.  It's actually just a longer delay that becomes noticeable.  Sometimes it's been described as it takes a while for the blocks to disappear, or the disappear for a split second, they reappear, then are removed again.  This is not always related to a slow server, but can also be related to a server that has a lot of plugins that are trying to run at the same time (so the time between when the block breaks are calculated and when the submitted task to break the blocks actually runs becomes too great).

This feature can be turned off with a simple setting change.  The following is the default setting which enables the submission of the synchronized tasks.  To turn it off, and to break the blocks when the listeners process the block break calculations, set this to a value of `FALSE`.

`applyBlockBreaksThroughSyncTask: TRUE`


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


# Auto Manager's Canceling the Event vs. Canceling the Drops

Prison's primary way of dealing with BlockBreakEvents, and other block events, has always been done by the canceling the event.


Note: Setting this behavior will effect all block break event listeners.  There is no way to customize it for each event type.


This was the original way of handling block breaks, and is the ONLY way of dealing with block breaks for all spigot versions older than v1.12.x.  Since this is a feature within Spigot (I don't think it is a bukkit feature), there is no way to provide this newer, and better way, of dealing with block breaks in the older versions.  If you try to enable this on older versions of Spigot, then each time the server is started, the setting will be "forced" off and then an error message will be printed to the console too.


You can still cancel the events with newer versions of Spigot, but if you run in to compatibility issues with other plugins, then it may be the easiest solution.


**NOTE:** In the future, these two settings will be removed and replaced with just one setting to control what gets canceled, either the event or the drops.  This will eliminate the risk of both settings being set to the same value.


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

*WARNING: Never set them to the same value.*


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

*WARNING: Never set them to the same value.*


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


# AutoFeatures & NormalDrops

AutoFeatures is related to auto-pickup, auto-smelt, and auto-blocking.  These are enabled with the following setting, which is on by default:

`autoFeatures.isAutoFeaturesEnabled: TRUE`

Please note, that these settings are "chained" together.  If you disable auto-pickup, then auto-smelt and auto-block will not apply.  If you have auto-pickup enabled, but disable auto-smelt, then auto-blocking is disabled too.


These auto features ONLY apply on priorities of LOWEST to HIGHEST.  They do not apply to MONITOR, BLOCKEVENTS, ACCESS, ACCESSMONITOR, or ACCESSBLOCKEVENTS.


It is important to note, that this setting is a "global" setting.  If it is set to `FALSE`, then it turns if off for everything, which includes the global settings, the permissions, and the lore that applies to auto-pickup, auto-smelt, and auto-blocking.

**Global Settings**

The following three "global" settings are on by default.  This means that all players in all mines will be able to get auto-pickup, auto-smelting, and auto-blocking by default, in all mines.

`autoPickupEnabled: TRUE`
`autoSmeltEnabled: TRUE`
`autoBlockEnabled: TRUE`

If you do not want to apply these auto features to all players in all mines, then set the above three values to `FALSE` and then use a combination of permissions and/or lore to enable them, if desired.


**Permissions:**

When using permissions for the auto features, the setting `isAutoFeaturesEnabled: TRUE` still must be enabled, but the other three "auto"s should be disabled globally.  

The default values for the perms are as follows, of which you can change the perms to anything you want them to be.

`permissionAutoPickup: prison.automanager.pickup`
`permissionAutoSmelt: prison.automanager.smelt`
`permissionAutoBlock: prison.automanager.block`

So any player who has these perms, whenever they are in any mine, these perms will enable auto-pickup, auto-smelt, and/or auto-blocking for them.


**Lore:**

An alternative to global and perms with the auto features, is to use lore on your tools.  Any tools that have these lore values will enable auto-pickup, auto-smelt, and/or auto-blocking when they are using in any Prison mine.

The following setting must be enabled, which is set to `TRUE` by default:

`lore.isLoreEnabled: TRUE`

Then the following controls the lore for the auto features.  These are the default values, but can be changed to "match" what is used on other enchantment plugins.  The Color codes are ignored when performing the comparisons so if they vary, they should still work.

`lorePickupValue: &dAuto Pickup&7`
`loreSmeltValue: &dAuto Smelt&7`
`loreBlockValue: &dAuto Block&7`


Keep in mind that any combination of Perms, Lore, or global settings will work together (global overriding everything), but `isAutoFeaturesEnabled: TRUE` must be enabled for them to work.



**Normal Drops:**

Since prison is handling the block breakage, the bukkit drops are actually disabled and will not happen.  So these normal drops are enabled by default and will exist just in case the auto-pickup is disabled for the player.  Keep in mind, that auto pickup can be globally disabled, and yet it can be overridden (enabled) by perms and lore.

`handleNormalDropsEvents: TRUE`
`normalDropSmelt: TRUE`
`normalDropBlock: TRUE`

Keep in mind, if auto-pickup is disabled for the player, and if `handleNormalDropsEvents` is also disabled, then no drops will happen even though the player "successfully" breaks the blocks.

If auto-pickup is disabled, the the auto-smelt and auto-blocking is automatically disabled too.  Hence why there is a `normalDropSmelt` and a `normalDropBlock`. 

For the `normalDropBlock` it is important to realize the blocking is based upon only the "drop" and not what is in the players inventory.  So if redstone requires 9 redstone dusts to make one block, and if the player only has 8, then no blocking will happen and it will drop 8 redstone dusts.  If the player has a stack of 64 redstone dust in their inventory (which has no bearing on the blocking) and they drop 14 redstone dusts, then the `normalDropBlockk` will drop ONE redstone_block, and 5 redstone dusts; their inventory has no bearing on the blocking.



# BlockEvents - Fortune Features


Prison provides a few options for controlling fortune.  But before those options are discussed, the process of how fortune is calculated may be a very important topic to understand since it's not a simple calculation.


To enable Fortune calculations within Prison, the following setting must be set to a value of `TRUE`, which is the default value:

`fortuneFeature.isCalculateFortuneEnabled: TRUE`


Once fortune is enabled within Prison, there are two types of fortune calculations that can be used.  Both have their own benefits, but each also has their limitations.  Both cannot be be used together, as only one can be enabled at a time.


Both types of fortune calculations are able to use fortune multipliers, the default values are shown here:

`fortuneMultiplierGlobal: 1.0`
`fortuneMultiplierMax: 0`
`fortuneBukkitDropsMultiplier: 1.0`


The `fortuneMultiplierGlobal` is a multiplier that is applied to all calculations that determine how many blocks are included in the drops. This is applied after calculating the fortune.  A value of **1.0** changes none of the calculations and allows them to be applied as they are calculated.  A value less than one will reduce the amount of the drops.  A value greater than one will increase the value of the drops.  All drops are forced to integer values (you won't get fractional drops).  The lowest value that can be returned from this adjustment is ONE; cannot return a zero value. 



The setting `fortuneMultiplierMax` is a way to prevent OP (over powered) tools with super high fortune enchantment levels from destroying the game play, or the economy.  What it does, is it sets the maximum permitted fortune level on tools, such that if a tool has a fortune level higher than that value, then the max value is used for all fortune calculations.  For example, if you set it to a value of 200 and someone has a pickaxe with a fortune of 379, then all calculations will be performed with a fortune level of **200**.  A max value of ZERO disables the fortune max limit and any value will be valid, even if it's well above 1000.


The new setting `fortuneBukkitDropsMultiplier` has a default value of 1.0 and is applied to the bukkit drops before any fortune is applied. This actually adjusts the bukkit drops so if bukkit is dropping too many items or ores compared to normal blocks, this can help reduce those amounts before the fortune calculations.  This is a good way to control fortune on drops that are too plentiful since it can reduce the amount that spigot is suggesting.  Use a value of 1.0 (the default) to prevent the drops from being modified.  

Use a value less than one to reduce the drops, or a value greater than one to increase the drops. The minimal result value is set to one; cannot be zero.  The drops are expressed in integers and no rounding is performed so a calculated value of 1.9 will have a drop of 1.


The use of `fortuneBukkitDropsMultiplier` is a great way to control how much bukkit drops, to keep the drops closer to a value of one per block.  And then with the use of `fortuneMultiplierGlobal` it controls how generous all the calculated drops are.  A combination of settings can help reduce how many redstone drop, without hurting the normal block drops.



**Extending Bukkit Drops**


Initially, prison uses Bukkit's Block.getDrops() function to get a set of ItemStacks of the drops related to the block that is being broken.  These drops are not fully inclusive as you would find within a vanilla game, but are somewhat limited, and they do vary from one version of spigot to another (1.8 vs 1.19 could be drastically different).


The extended bukkit fortune calculations are enabled with the following, which is enabled by default:

`isExtendBukkitFortuneCalculationsEnabled: TRUE`


So to correct for omissions, such as a percent chance flint would be dropped with mining of gravel, and to adjust for the limited range of fortune within the vanilla server, prison has to make adjustments. 


With Fortune, the bukkit standard is for fortune levels 1 through 3.  But with OP prison setups, Prison needs to support higher fortune levels, including hundreds of levels.


With a standard block break event, you break one block and you get one drop. That is standard without fortune.  So the way Prison detects if fortune is applied, is if there is more than one block provided for a drop.  With fortune 3 being the highest possible level to influence the drops, Prison checks the tool to see what it's fortune level is, then uses that within the calculations.


So if the tool has a fortune level of three or less, then Prison does nothing with the drops since everything is already taken care of.  But if the fortune level is greater than 3, then Prison must make adjustments to the drops.   


The way prison extends the drops for higher fortune levels for tools with a fortune over three, is to divide the standard drop by three (since it maxes out at three), then multiplies that value by the actual fortune level on the tool.  The reason is that all of the random variables that went in to calculating the standard drop have already been applied, so all that needs to be done is to extend the total drop amount to cover the tool's actual fortune level.  Since a fortune 3 has a higher chance to adding more to the drop compared to a fortune 1, then we can assume those random chances are reflected in the standard drop.  This also means prison should not apply a random chance to the drops since that chance was already applied. 


One issue with extending the fortune on the standard drops is that the amounts can appear to be much higher than what would be expected.  So prison provides a way to apply a multiplier to the results to adjust the generated values so the drops have more variability.  There are two settings that allow you to control the calculated range of drops.  With the bukkit drops being at 100 percent, these two allow you to extend the drops to become a percentage range, such as 70 percent to 110 percent (the default values).

`extendBukkitFortuneFactorPercentRangeLow: 70`
`extendBukkitFortuneFactorPercentRangeHigh: 110`


So if a given pickaxe's OP fortune always drops 50 diamonds when mining DIAMOND_ORE, this will allow it to change the range of the drops from 35 (50 * 0.70) to 55 (50 * 1.10) diamonds, which provides a greater variability in what is being dropped.  Keep in mind that bukkit will have a slight variability in the drops, but they will fall in to about 3 to 5 different values, and when extended, will still be only 3 to 5 different values.  So this extendedBukkitFortuneFactor range prevents the same quantity for each drop.



**Prison's Alternative Fortune Calculations**

If you want to use the alternative fortune calculations, then you need to disable the above `isExtendBukkitFortuneCalculationsEnabled: FALSE` and then enable it with the following setting, which `FASLSE` is the default value.

`isCalculateAltFortuneEnabled: FALSE`

The limitations of the bukkit fortunes, is that they will never apply fortune to some blocks, such as diamond_block, emerald_block, or other solid blocks that may not be naturally occurring in the wild.


The alt fortune is initially based upon the bukkit drops.  Therefore there is a risk that ores, such as redstone or lapis, could drop huge amounts when processed by the alt fortune.  This can be controlled by the `fortuneBukkitDropsMultiplier` to lower the range of the bukkit drops, and also use `fortuneMultiplierGlobal` to adjust the final calculations.

The alt fortune calculations are based upon the formulas provided within the Minecraft Wiki's for standard fortune levels.  The big difference is that it applies to all blocks, not just a select few.  But some blocks, such as diamond_ore and redstone_ore, gets higher multipliers as with their vanilla counter parts.  Also Prison's alt fortune provides higher fortune levels and will support an unlimited high fortune.


Remember that the settings for `fortuneBukkitDropsMultiplier`, `fortuneMultiplierGlobal` and `fortuneMultiplierMax` apply too, so you can have some better control over the overall drop amounts.




**Some general notes to better explain the logic of how the extended bukkit fortune is calculated:**

* Bukkit's Block.getDrops()
* If drops == 1 then no fortune has been added.
  * If tools fortune enchantment > 3 then apply
* If drops > 1 then fortune has been already applied by bukkit
  * If tool's fortune enchantment <= 3, then do not apply fortune
  * If tool's fortune enchantment > 3, then need to calculate the additional fortune.
    * Formula: drops / 3 * tool_fortune_level = raw_adjusted_drops
    * Formula: raw_adjusted_drops x 


**Prison's Percent Gradient Fortune Calculation**

New as of v3.3.0-alpha.15e

This is a new fortune calculation type, which defines a max fortune amount, and a max bonus payout that is possible.  Then based upon the fortune level on the player's tool, it will payout a linear maximum amount, adjusted by a random range.

To enable, you need to setup the following settings within the autoFeaturesConfig.yml file.  If you just upgraded prison, restart prison, and the new settings will be injected in to the config file for you.  Then modify those settings, and then use `/prison reload autoFeatures`.

These settings must be set to these values to use this new fortune calculation:

```yml

  fortuneFeature:
    isCalculateFortuneEnabled: true
    isExtendBukkitFortuneCalculationsEnabled: false
    isCalculateAltFortuneEnabled: false
    percentGradientFortune:
      isPercentGradientFortuneEnabled: true

      percentGradientFortuneMaxFortuneLevel: 1000
      percentGradientFortuneMaxBonusBlocks: 200
      percentGradientFortuneMinPercentRandomness: 25.0
```

The last three listed settings can be customized however you need them to be.


Percent Gradient Fortune is an alternative fortune calculation that will only be enabled if extendedBukkitFortune and altFortune is turned off.  

Percent Gradient Fortune will always drop a minimum of 1 block with fortune 0 and higher. The max it will ever drop, will be 1 + MaxBonusBlocks-amount.  The calculation of the MaxBonusBlocks will be a random roll resulting in 0 bonus blocks, to the MaxBonusBlocks amount IF the player has the max fortune on their tool. 

For fortune amounts less than the maxFortuneLevel, it will be treated as a linear percentage gradient of the max amount. For example, MaxFortuneLevel= 1000, and MaxBonusBlocks= 200. Therefore if the player has a fortune 500, the max bonus they could get would be only 100 blocks, but could be as low as zero bonus blocks since it's a random roll on each calculation. If they have a fort 250, then it will be 25% of 200, or 50 blocks as a max bonus. 

For better control of the randomness applied to the bonus block calculations, the MinPercentRandomness sets the lowest range for the randomness. What this means, is for a maxFortuneLevel= 1000 and a maxBonusBlocks of= 200, and a tool with fort 500, the calcs would be for a bonus between '0' and (500 / 1000 * 200 =) 100 bonus blocks.  But with the minPercentRandomness= 25, then the range would be '25%' to 100% of the 100 bonus blocks. The minePercentRandomness would ensure a higher payout of bonus blocks, without effecting the max payout. minPercentRandomness has a valid range of 0.0 (off) to 99.0 percent. 

No other fortune multipliers will apply to these calculations.  

The percentage gradient is a very controlled way of paying out fortune bonuses.


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


# Durability Calculations (Wear)

By default, Prison does not apply any durability (wear) to the tools that are being used within the mines.  The reason Prison does not apply wear by default, is that it depends upon the version of spigot that you are running on your server, or the flavor of spigot (paper).  Sometimes bukkit handles the wear, sometimes it doesn't. If bukkit is applying the wear, and you enable it within Prison too, then a tool can actually wear out twice as fast as it normally should wear out.  So to error on the safe side, Prison will not enable durability calculations by default.

`durability.isCalculateDurabilityEnabled: FALSE`

To enable it, set the value to `TRUE`.

When it is calculated to apply wear to the tools, it only adds 1 to the total durability on the tool.  When the durability, or wear, exceeds the max durability level for the tool, then the tool will break.

The tool's durability level will impact, and reduce, the chance to add wear to the tool.  Also if the tool has the Durability Resistance enchantment, levels 1 through 100, it too will make it more difficult to add wear to the tool.  If the Durability Resistance enchantment has a level of 100 (or no specified level assumes to be 100), then no wear will ever be applied to the tool.
To prevent Prison from breaking the tool when it wears out, set the following configuration settings:

`isPreventToolBreakage: FALSE`
`preventToolBreakageThreshold: 10`

If `isPreventToolBreakage` is enabled, then prison will not allow a tool with a threshold of the specified value, or less, to break any blocks.  If a player tries, nothing will happen.  Also the tool will not be worn down any more, and it will remain at that threshold value.

The reason why there is a threshold value, is to prevent the tool from reaching ZERO and having some other plugin, or the vanilla bukkit, break the tool.  It's an insurance policy to help prevent the tool from breaking.  

Please be aware that Prison will NOT prevent the tool from breaking by another plugin or by bukkit.  So if a tool reaches the threshold and is not able to workk within the mines, if they use the tool out in the wild, outside of a mine, then bukkit can continue to wear the tool down until it breaks.

Prison's preventToolBreakage is a simple process for Prison's purpose.  It is not a global, complex solution that will ensure the tool will never-ever break and be lost.


**Durability Resistance Lore:**

Prison has it's own custom lore that will prevent durability from being applied to the tool that is used for mining, and it is disabled by default.  

`lore.loreDurabiltyResistance: FALSE`
`loreDurabiltyResistanceName: &dDurability Resistance&7`

To use this lore, Prison's lore handling must also be enabled.

`lore.isLoreEnabled: TRUE`


When it's enabled, it is treated as a percentage from 1 percent to 100 percent.  The percentage is a number that follows the durability resistance's name.  If no number is provided, then it is treated as 100 percent.

If a random number is "rolled", from 0 to 99, and that number is greater than the durability resistance value, then prison will allow durability to be calculated normally.

If the random number is the same as the durability resistance value, or less, then the durability calculations will be skipped.

High values for Durability Resistance is not recommended since it will prevent wear to the tools.  Smaller values could help extend the life of the tools, especially when the tools are being used with Prison explosions which may include hundreds of blocks in one hit.



<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# Silk Touch Features


If a tool with Silk Touch enchantment is used, then no fortune is applied, and the blockk that is broken, is the blockk that is provided.  The bukkit drops, or the alt drops, are ignored.

Silk is enabled with the following setting, which defaults to enabled:

`general.isCalculateSilkEnabled: TRUE`


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


# Dropping Extras

Dropping extras is enabled by default, and an example would be dropping flint when mining gravel.  The odds for dropping flint is based upon the published rates as defined by the Minecraft wiki pages.


`general.isCalculateDropAdditionsEnabled: TRUE`


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


# Giving XP

Prison has the ability to give the player XP for mining certain kinds of blocks.  The XP can be give either directly, or though the generation of XP Orbs.


The following are the default settings:

`general.isCalculateXPEnabled: true`
`general.givePlayerXPAsOrbDrops: false`


Please note that drop XP as Orbs is disabled by default.  If dropping by orbs, they behave as normal XP orbs behave.  As such, the player may not be able to collect them all, if any, and other players may collect the XP instead of the player that earned them.  There is no way to change this default orb behavior within Prison, so use with caution.



<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# BlockEvents - Tool Lore Counter

Prison provides block break counts on tools through the use of lore, if enabled.  This will track how blocks a tool has broken over it's life span.



<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# Minecraft Stats Reporting

*since v3.3.0-alpha.15a*

Java based minecraft clients keep track of some basic stats.  Prison, now supports updating these stats by default:

`general.isMinecraftStatsReportingEnabled: TRUE`

Older versions of Prison did not update them, and therefore, if you set the value to `FALSE` then it will not provide any updates to the stats.



<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


# Calculating Food Exhaustion

By default it is enabled:

`general.isCalculateFoodExhustion: TRUE`

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


# Cancel Event if Block is Already Counted (prevent player placed blocks from breaking)

This setting is enabled by default.  This is a performance consideration that prevents prison from processing blocks that it has already counted and processed before. 

`general.ifBlockIsAlreadyCountedThenCancelEvent: TRUE`

If this is set to a value of `FALSE`, Prison will then immediately ignore the event, and won't cancel it.  It's impossible to "force" prison to process the same block twice.

When this is set to a value of `TRUE`, it will prevent any player placed blocks in the mine from being broken.  This includes OP'd players too. 

Setting this value to `FALSE` will allow other plugins, or bukkit, to break the block if world has be configured to allow players to break blocks within the mines.  Prison will not have anything to do with the permissions on player placed blocks.  For example, a world guard region would need to be setup to allow players to break blocks within a mine even if Mine Access by Rank is enabled.  Some servers may allow players to place block in mines, and then break them too.  This is the only way to allow Prison to ignore such blocks so they can be broken by other means.


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">

# MONITOR Priority Only with AIR

The following setting is enabled by default.  It allows MONITOR priorities to be enabled only when the block that is being processed is an AIR block.  This helps to ensure that the player was successful in being able to break the block.

`general.processMonitorEventsOnlyIfPrimaryBlockIsAIR: TRUE`

An example of where this will prevent a block from being processed under a MONITOR or BLOCKEVENTS priority, would be if the player tried to mine a block in a mine they have access to, but their pickaxe is worn out and they were not able to break the block.  Because they did not break the block, the block is not an AIR block, and therefore should not be counted and processed as if it was successfully broken.


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


# ACCESS failure - TP Player to A Mine they have access to

This defaults to enabled:

`general.eventPriorityACCESSFailureTPToCurrentMine: TRUE`

When using one of the ACCESS priorities, and if a player tries to mine in a mine they do not have access to, then this feature will force the player to TP to a mine they have access to.

This runs the command for the player:
`/mines tp`

The behavior of `/mines tp` when no mine is specified, is that it will take the player's current rank, and find the first mine that is linked to that rank, and then it will TP them to that mine.

If the player does not have a rank, or the ranks are not tied to mines, then this command will do nothing.

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


# Prison Tokens

Prison tokens are not enabled by default:

`tokensEnabled: FALSE`
`tokensBlocksPerToken: 100`


When they are enabled, the `tokensBlocksPerToken` defines how many blocks need to be broken before the player earns one token.

Currently there is nothing within prison that can use tokens, but there are a some commands that can be used with other plugins to create custom uses for tokens.

The following command will show all Prison token commands:

`/prison tokens`

When a player earns tokens, prison tracks which mines they earned each token in, and their total tokens earned, and their current token balance.  Since tokens will be used internally in the future to control different features and allow players to "earn" specific rewards, prison also tracks how many tokens admins gave them, or removed from their balance.  So for example, if a mine requires that a player earns 500 tokens within that mine, or just earn 500 tokens in general, then they cannot use tokens that were "given" to them by other players, or by admmins.  Prison will be able to differentiate earned tokens from given tokens.


Prison command options are "add", "balance", "remove", and "set" tokens for a player.

```
/prison tokens add help
/prison tokens balance help
/prison tokens remove help
/prison tokens set help
```

If a player is given any of the tokens by these commands, they are tracked as if they are given, or removed, by an admin.  There is an "option" on these commands called `forcePlayer` which forces the token transaction to be treated as if the player earned them, or spent them under normal conditions.  It is not advisable to use the `forcePlayer` option since it will potentially break quests that require the player to earn tokens on their own.

Another token option is `silent` which will prevent any notifications from being sent to the player.

In the near future, rankup requirements will be expanded to include:
 - A monetary cost (currently how rankups work)
 - Require so many blocks be mined (anywhere)
 - Require so many blocks be mined within the current rank's mines
 - Require so many tokens be earned (anywhere)
 - Require so many tokens be earned within the current rank's mines
 - Require a token cost with tokens earned (or any tokens)
 - And other requirements as considered possible, such as mining a specific number of blocks of a specific type from a specific mine, etc...





<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">

