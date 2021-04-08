
### Prison Documentation 
[Prison Documents - Table of Contents](prison_docs_000_toc.md)

# Prison's Auto Manager - Setting Up and Enabling Other Plugins


Prison's Auto Manger deals with the whole block breaking events.  It's able to provide advanced features such as auto pickup, auto smelt, and auto block, along with providing the player with XP, applying OP fortune, plus many other features.  All with maintaining full compatibility from Spigot v1.8 through Spigot v1.16.


Auto Manager is a very complex "process" and as a result, there are many features that can be configured, and many possible interactions with other plugins.  Needless to say, there are many things that can go wrong, especially when it may not be configured correctly.


This document tries to cover some of those configurations and settings in other plugins to get things working at their best.


This document contains information on how to configure TokenEnchant and Crazy Enchant  


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">

# Auto Manager


(content coming soon)



<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">




# To get TokenEnchant's TEBlockExplosionEvent to work with prison, you must follow these settings:


**Please Note:** that these instructions apply to **Prison v3.2.4** and later only.


**Please Note:** that as of Prison v3.2.5-alpha.11 some of these settings may not be required, especially in relation to WorldGuard and if you are using Mine Access Permissions.


**Please Note:** As of prison v3.2.6-alpha.1 Event priorities have been added to prison. Changing the event priorities may make it easier to get prison to work better with TE instead of having to change the event priorities on TE.  If the follow directions do not work well for you, then try changing a few of the the prison's event priorities.  Currently I do not have any documentation to explain how this may help.



- **WorldGuard:** Enable the WorldGuard's setting for __global__ to prevent block breaks within the world where the mines are.
- **WorldGuard:** Define a **simple region** in the mine the same size as the mine. Name it as suggested at the top of this document, such as prison_mine_c as an example.  The "simple" region only includes defining the region, setting the priority to 10, and add a permission group member. Example: `prison.mines.a`.  You may add the other flags if you want, as suggested at the top of this document.
- **WorldGuard:** Please note: Sometimes you might have to add the WorldGuard flag **block-break** in order to get this to work. `/region flag prison_mine_<mine-name> block-break allow` It may be a WG version issue.  Spigot v1.8.8 requires this flag, while Spigot v1.15.2 does not appear to require it.  Your success may vary.  It may also be a good idea to add it either way.



- **Player Permissions:** You may have to first create the group within your permission plugin before you can assign it to a player.  Example using LuckPerms: `lp creategroup prison.mines.a`
- **Player Permissions:** The player must have the permission that is tied to the afore mentioned region.  For example the group perm: `prison.mines.a`  And assign it with: `/lp user RoyalBlueRanger parent set prison.mines.a true`



- **TokenEnchant:** TokenEnchant must be configured properly to enable Explosive enchantment. Setup TokenEnchant as you would normally. Download and place the **TE-ExplosiveEnchant-8.7.0_4.jar** in the `plugins/TokenEnchant/enchants/` directory.  Start the server to have TE generate the configuration files related to that enhancement.
- **TokenEnchant:** TokenEnchant must not process the TEBlockExplosionEvent and the default settings must be turned off.  TE's auto pickup is defined within TE's config file: `plugins/TokenEnchant/config.yml` at bottom with the two settings: `TEBlockExplodeEvent.process: true` and `TEBlockExplodeEvent.pickup: true` as default values.  Both of these must be set to `false` for this to work with prison.
- **TokenEnchant:** In order for TEBlockExplosiveEvent to be fired, the `plugins/TokenEnchant/enchants/Exposive_config.yml` must be adjusted. First run your server to generate this file if you have not done so yet. Find `Potions.Explosive.occurrence: random` and change it to `always` for testing purposes. Then locate the setting of `Potions.Explosive.event_map.BlockBreakEvent: HIGHEST` which is the default value (HIGHEST), and change it to a value of `LOWEST`.  Failure to change this setting will result in the failure of TE from being able to fire the TEBlockExplodeEvent.
- **TokenEnchant:** To create a pickaxe with an explosion enchant do the following: From within the game, give yourself a diamond pickaxe (`/give <playerName> diamond_pickaxe 1` or `diamondpickaxe`) and enchant it with the command `/te enchant Explosive 1` or up to a level of 10. Run as OP, or if you need tokens to use, give yourself some from the console with `/te add <playerName> 100`.



- **Prison: Auto Manager:** Within the configuration file `plugins/Prison/autoFeaturesConfig.yml` change the setting `options.isProcessTokensEnchantExplosiveEvents: true` to a value of `true`.  The default value is true, but double check to ensure it set correctly.  PLEASE NOTE: This setting is used even if **options.general.isAutoManagerEnabled: false** is disabled (set to false).
- **Prison: Auto Manager:** Enable **Auto Features** and **Auto Pickup** and all blocks from the explosion event will be placed in the player's inventory.
- **Prison: Auto Manager:** If **Auto Features** is disabled (which means auto pickup is also disabled), then the blocks still must be processed and broken by prison to prevent blocks outside of the mine from being broken.  To enable prison to drop blocks normally within the mines, while honoring the TE Explosive event, then you will have to enable the setting `isProcessNormalDropsEvents: true` (set to true).  Prison will calculate the drops and will drop them where the blocks were originally (not at the player's feet). The internal calculations that prison uses for the drops are the same calculations it uses for the auto pickups.



- **Testing:** Deop yourself if you are testing this so you will not break blocks outside the mine.
- **Testing:** Test with a regular pickaxe, a TE Enchanted pickaxe.  Also test with Prison's auto features turned on (auto pickup) and off.  Ensure the setting `isProcessNormalDropsEvents: true` is enabled when auto features is disabled.  This setting can be left on even when auto features is enabled since auto features will override it.

  
  
#####  **WARNINGS:**
  - If anyone is OP'd then they can break blocks outside of the mine through the TE event. This is the result of WorldGuard bypassing the restrictions on the regions and has nothing to do with prison.
  - If TokenEnchant is set to handle the explosion events, with or without their auto pickup enabled, then TE will break blocks outside of the mine and prison will NOT be able to control that.  If you have those settings enabled, then that is outside of the control of prison and you assume all risks of breaking and destroying builds around the mines.


  

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">




# To get Crazy Enchants' BlockExplodeEvent to work with prison, you must follow these settings:


Please note that these instructions apply to **Prison v3.2.4** and later only.


Please follow the directions for Token Enchant explosion that are listed above.  


But ignore everything that is listed for Token Enchant since Crazy Enchants is much simpler to configure. At this time I am not aware of any special changes that you need to make to the Crazy Enchant's configurations.


To enable the processing of the Crazy Enchant BlockExplodeEvent enable this configuration: 
`isProcessCrazyEnchantsBlockExplodeEvents: true` (enable by setting to true)



<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



