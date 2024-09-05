
### Prison Documentation 
[Prison Documents - Table of Contents](prison_docs_000_toc.md)

## Setting up Prison - The Basics

This document provides a quick overview on how to install Prison and get it running.


*Documented updated: 2024-08-30*

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# Download Prison


Download Prison from one of the following sites:
* [spigotmc.org's Prison History Page](https://www.spigotmc.org/resources/prison.1223/history).
* [Polymart.org](https://polymart.org/resource/prison-1-8-x-1-20-x.678)
* [bukkit.org](https://www.curseforge.com/minecraft/bukkit-plugins/mc-prison-v3)

These sites will have stable alpha releases published to them from time to time.


Setting up Prison is simple:

* **Download Prison - Current Releases**
    - Prison's published releases

    	
* **Download Prison's Pre-Release Version**
    - Useful to access newer features and fixes
    - You can always find the latest alpha build on the Discord Server in the #alpha-versions channel:
        - [Prison Discord Server](https://discord.gg/DCJ3j6r)

* Copy the prison jar file to your server's `/plugin` directory.  

* Remove any older prison jar file

* Restart the server. 

* Prison's startup information contains a lot of information.  If you ever have issues, check that information first since it probably will identify what the issues are.


* It is strongly suggested that `/ranks autoConfigure` is ran to initially setup your Prison environment.  A great deal of configurations are setup that can save a lot of effort.  Even if you are wanting to start from scratch, it may be worth giving it a try to see how some of the more complex settings are configured.  You can always start over by deleting the `/plugins/Prison/` directory then restarting the server.


* Follow Prison's documentation on customization; at this point it's ready for use. 


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# Prison's Dependencies on Other Plugins

* **None - No hard dependencies** - There are no hard dependencies for Prison.

There may be no hard dependencies that will prevent Prison from running, but there are some core plugins that will make it easier to use, and are even required for activation of some features within Prison.  This short list is just a suggestion, but alternatives do exist and may be outside of our ability to comment or assist in their usage.


* **Vault** - Optional, but **STRONGLY Suggested** - This is perhaps the most important plugin.  This plugin provides a common way to access other plugins running on your server, but without having to write any code within Prison to support them.  Vault provides the mapping of a plugin's unique APIs to a common Vault API.  Vault helps support Economy, Permissions, and Placeholders.  Because of Vault, Prison can work flawlessly with dozens of other plugins.  Please refer to Vault's documentation for what it supports.  Valut does have it's limitations, such as it can only support one currency at a time.


* **EssentialsX** - **STRONGLY SUGGESTED**, but still Optional - Provides many of the basic commands and behaviors that you would expect from a Spigot server such as chat, warps, and even some moderation commands and commands that can be given to premium players.  EssentialsX is not Essentials, since Essentials is an older abandoned project, and EssentialsX is a forked project that is still maintained.  Unfortunately, internally it is identified as simply Essentials, but you can tell it's EssentialsX if the version is greater than 2.15.x.  

  EssentialsX is released as a zip file and you must extract the jars that you are interested in.  It should also be pointed out that all EssentialsX jars should come from the same zip file so they will be of the same version and the same release.  UPDATE: The last I checked EssentialsX may not be released in a single zip file anymore.  It looks like you have to download the parts you are interested in using.


### Economy Plugins - Required

Prison requires an active economy in order to active the Ranks plugin.  When Prison starts up, it performs many validations on the mines and ranks as they are being loaded.  With Ranks, if Prison cannot find an active economy, then it will refuse to load the Ranks module due to possible server corruption (ie... what failed that there is no economy).

We say an economy is required, but it's still optional.  Without an economy, you cannot use the ranks module, but we fully understand that some servers choose to use a different ranks plugin.


* **EssentialsX Economy** - SUGGESTED - Optional - This is a simple economy plugin that just works well.  If you don't have a specific need to use another economy plugin, then it may be best to use this one since it works so well.  The reason why we recommend this economy is because it always works.  That said, we acknowledge the reason it works well, is because it is so simple, so if there are features in other economy plugins that you want to use on your sever, then please explore using them.  But overall, if you just want an economy that is rock solid, then EssentialsX's Economy is a great choice.
  

* **CMI Economy** - Optional - If using CMIE then you must enable Prison's delayed startup

  See: [Setting up CMI Economy](prison_docs_028_setting_up_CMI_economy.md) for full information on how to get CMIE working with Prison.

  Background: CMI "tries" to load last, so it can ensure all of it's dependencies and hooks are in place before it starts up.  That's understandable, but Prison also has similar requirements and expectations. Unfortunately, this also causes a conflict with Prison, since Prison must perform validation on startup, and if there is no economy, then Prison could fail to start the Ranks module.
  
  The document, [Setting up CMI Economy](prison_docs_028_setting_up_CMI_economy.md), explains in detail how to get everything working perfectly.  


**Coins Engine** is supported.  A multi-currency economy.
*Need to provide more details*


**Gems Econommy** is supported.  A multi-currency economy.
*Need to provide more details*


**EdPrison Economy** is supported.
*Need to provide more details*


**ESS Economy** is supported.
*Need to provide more details*


**The New Economy** is supported.
*Need to provide more details*


**Sane Economy** is supported.
*Need to provide more details*
  

### Chat Prefix Plugins - Optional

These plugins are used to add rank tags to the player's chat messages that they send.


* **EssentialsX Chat** - Optional - Enhanced Chat experience. Provides customizations to the chat prefixes.



* **LuckPerms Chat Formatter** - Optional - Used in place of EssentialsX Chat?



### Permission Plugins - Required


Permission plugins are not *strictly* required for Prison to work, but within a server environment almost *everything* depends upon permissions in order to make things work.


Prison actually uses bukkit's permission interfaces.  This makes it simple for Prison, but it also limits what prison can do.  For example, one limitation is with permission groups; Prison is unable to resolve permission groups since that "concept" does not exist in bukkit, but is a concept that is implemented through plugins like PEX and LuckPerms.


* **LuckPerms** - Required - Strongly Suggested - LuckPerms is a great permission plugin that is actively supported and has many new features to make managing your server easier.


* **LuckPerms v5.x.x** - Use the latest version of LuckPerms whenever you can.  Keep in mind that releases to spigotmc.org is somewhat infrequent; giving them the "counts" for downloads is nice, but you may have to go to their main download page to get the most recent releases: [https://luckperms.net/download](https://luckperms.net/download).  Please note that it is normal for issues to surface once in a while, and there were a few that needs to be avoided: The releases v5.3.0 through about v5.3.3 had some issues that caused significant logging and failures.


* **LuckPerms v4.x.x** This older version of LuckPerms is still supported, but it is highly recommended to upgrade to the latest v5.x.x release.  LuckPerms v4.x.x is required for prison v3.2.0 and older (Prison v3.2.1 and newer supports all versions of LuckPerms).  Please consider upgrading both Prison and LuckPerms to the latest releases for the best experience.


* **Gems Economy** - Gems Economy supports multiple currencies.  Support for Gems Economy was added a few years ago and worked well at the time.  If you try to use this plugin and have issues, please contact Blue on Prison's discord server so the issues can be addressed.


* **NOTE: A permissions plugin of your choice** - Required - Prison works with many different permission plugins through Vault.  I strongly suggest LuckPerms since it is free and is under active development so bugs and performance issues will be addressed if they ever become an issue.  If you choose to use another permission plugin, then please consider ones that work with Vault; Prison uses Vault to provide the flexibility of choosing from many different plugins.


* **NOTE: A Warning about Vault and Essentials Economy** - There was a problem where essential's economy was refusing to work thought vault.  Since prison is able to directly support essentials economy, there is a new feature that can disable the use of vault for the economy, which will allow prison to directly communicate with Essentials Economy.  Make sure your `config.yml` has the following configuration.  It will be added to new prison installs, otherwise just copy and past it in anywhere.  Also, if you rename the `config.yml` file and restart the server, prison will auto generate a new copy with these settings.

```yaml
integrations:
  prevent-economy-vault-usage: true
```

* **Other Permission Plugins** There are many out there, and as developer of Prison, and through providing support, we don't hear of any issues with other permission plugins.  The reason for this is because they probably just work well to begin with, and that Prison uses bukkit's permission interfaces.  So it keeps things simple for setting up Prison.  If you are just getting started with building a server, then we suggested keeping it simple with something like LuckPerms, but there are other permission plugins out there that can provide a whole new experience with your server, such as jobs and careers for your players.   



### Placeholder Plugins


Chat related plugins are able to provides access to Prison placeholders, which can allow Prison data to be included in their content. PAPI is simple and works so well, that it usually is the default choice.


The way a placeholder works, is that it is a text based **key** that is used to request data.  Generally the key is included with other text, such as a chat message, holographic display, or a scoreboard content, and the key is replaced with the requested data. Usually the plugins using placeholders have no idea what other plugins are supplying the requested data; they basically make a general request for information to all plugins, and the plugins that recognize that **key** will respond.


It should be noted that Prison's placeholders are just text based keys.  They are usually all lower case alpha numeric characters, with underscores, and maybe some colons too.  The important thing to understand is that they do not include the escape characters, and that the escape characters may differ from what is required in other plugins.  Placeholder escape characters are usually `{ }` or `% %`, and sometimes you may have to mix the two.  The plugin's documentation should help you identify what's the correct usage.


All prison placeholders start with `prison_` and are usually all lower case.  Upper case may work too, but lower case is recommended.  Prison tries to ignore the case of its placeholder keys, but its the other plugins that can have issues.  For example, if Prison registers all of the plugins as lower case, then the other plugins may not recognize all upper case, or mixed case, as being related to Prison, so therefore they may not send the request to Prison.


An example of a prison placeholder is `prison_rank`.  But used in another plugin, you will need to add escape characters such that it may be either `{prison_rank}` or `%prison_rank%`.  Prison only controls what the text key is; prison cannot control which escape characters are used in another plugin.


All of Prison's placeholders have an alias.  An alias is a shortened name for a placeholder.  Some placeholder names can become large based upon trying to keep their names informative as to what they represent.  As an example, `prison_rank` has an alias of `prison_r`.  And `prison_player_balance_earnings_per_minute_formatted` has an alias of `prison_pb_epmf` which can be useful if there is limited space in the configurations.  The command `/prison placeholders list` shows all available placeholders, along with their aliases.


* **PlaceholderAPI** - Strongly Suggested - Also known as **papi**.  Used through Vault, it is free and provides the core interfaces in to the usage of placeholders.  Prison also has a special integration setup for PlaceholderAPI to register all of the Prison placeholders dynamically upon startup.  You do not need to download anything from the cloud for prison to work with this plugin.  Also if you reload papi, you do not have to reload Prison's placeholders.  Prison registers placeholders that are all lower case and more recently, all upper case too so it's easier to use placeholders through papi.

  NOTE: You may also need to install the follow plugin for full support: ProtocolLib.


* **MVdWPlaceholder** - Not supported.

**NOTE: Prison no longer supports MVdWPlaceholder** because it could not support all of the advanced features with placeholders that prison uses.  Also, since prison generates so many possible placeholders, MVdW pollutes the console log with thousands of lines of useless information stating each variant of a placeholder has been registered.  We also dropped support for this plugin because there is no way to contact the developer because they hide behind a pay-wall, and I'm not about to buy one of their other plugins to just tell them their so-called-free plugin is not working properly.

But perhaps the biggest reason why I dropped support for MVdW is because it's 100% pointless from Prison's perspective.  **PlaceholderAPI** works flawlessly with MVdW installed too, so there is absolutely no reason why prison needs to support MVdW anymore since everything works perfectly through PlaceholderAPI.  If you need to use MVdW, then please keep using it, it works great with their other plugins.  But you can use PlaceholderAPI along with it too.  So there are zero reasons why you cannot use PlaceholderAPI, and everyone is happy.

~~Suggested to Avoid - Prison does support this plugin, but since it is used mostly with premium plugins, we have no way to fully test this plugin to ensure it actually works correctly.  We've heard very few people have used this plugin, but we've heard it does work well. Use at your own risk.~~

~~With this plugin, all placeholders are registered with it automatically when prison starts up, and all placeholders should be used as all lower case.  Because prison has so many placeholders, with many that are expanded based upon ladders, ranks, and mine names, a modest prison server could generate and register well over 1000 placeholders.  MVdWPlaceholder appears to be very verbose so you will see a lot of logging in the console when it starts up.~~

~~It should also be noted that because of some of the limitations of MVdW, not all features of Prison's placeholder support will be supported.  For example, you may not be able to reload placeholders, or use placeholder attributes to customize how placeholders are used.  Also the numerical sequence placeholders may not work either.~~

~~Like it was said earlier, there is no way to contact the developers.  If we could make just one suggestion, and that would be to allow setting up placeholders by specifying a prefix that's used.  This is how PlaceholderAPI works, so with just registering once, a value of "prison_" that ensures all of prison's placeholders are routed to  us.  Also, make sure the allowable placeholders are not limited by length.  Prison use placeholder attributes that can customize how the results are modified which gives an almost limitless opportunity to customize placeholders as desired to match the server's design standards.  The third suggestion for changes is to allow the reloading of placeholders with a simple command, such as reregistering them.  As admins add ranks, ladders, or mines, or even change their names, then all of the placeholders must be reregistered so the new entries are included.~~



### World Protection Plugins


* **WorldEdit** - Recommended - Used with WorldGuard


* **WorldGuard** - Recommended - Used to protect your worlds.  At a minimum setup the `__global__` region to protect your world from players.

```
/rg flag __global__ -w world passthrough deny
```


* **Fast Async World Edit (FAWEs)** - Recommended on the newer Spigot Versions - Read notices on compatibility with WorldEdit and WorldGuard.  Some people report issues with this plugin; read reviews and do your homework if you're having issues.


* **CoreProtect** - Optional - Server protection against griefing and building mistakes, and even server failures that may corrupt blocks/chunks.



### Enchantment Plugins

*NOTE: this section needs more work.  It's not up to date, and does not list all of the various features.*


* **CustomItems** - Recommended - *A Premium Plugin* - Allows for the use of custom blocks within Prison.  This provides for a great deal of customizations, including custom textures for your custom blocks. Prison supports CI at about 95% or more.  If you need additional support added for CI, please contact Blue and he will add it for you.  
[https://polymart.org/resource/custom-items.1](https://polymart.org/resource/custom-items.1)


* **TokenEnchant** - Recommended - *A Premium Plugin* - This is one of the few recommended premium plugins that we would recommend, but it works very well with prison.  It took a lot of effort to get this to work with Prison, but is perhaps the most supported one too.  Keep in mind that it is premium and they also charge for other add on features, so the initial cost may not be your final cost.  
[https://polymart.org/resource/tokenenchant-1-7-10-1-17.155](https://polymart.org/resource/tokenenchant-1-7-10-1-17.155)


* **Prison Enchants** Newest supported enchantment plugin supported.  


* **Crazy Enchantments** - Optional - Some support is provided for Crazy Enchantments, but it may not be at 100% in all areas. This is an open source project and supports Spigot 1.8 through 1.16.
[https://www.spigotmc.org/resources/crazy-enchantments.16470/](https://www.spigotmc.org/resources/crazy-enchantments.16470/)


* **RevEnchants** - *A Premium Plugin* - Prison now supports RevEnchants through their ExplosiveEvent and JackHammerEvent!  To enable prison to work better with RevEnchants, you can enable it within the `autoFeaturesConfig.yml` file.  Prison supports RevEnchants v11.2 or newer.  It may work with older versions, but if there are issues, we cannot provide support if the older version of RevEnchants is missing either of the events.

To use RevEnchant's block break handling, prison needs to just monitor the events so it can update the block counts.  This is an example of what settings are needed if you are using Prison's Block Events.

```yaml
    blockBreakEventPriority: ACCESSBLOCKEVENTS

    RevEnchantsExplosiveEventPriority: BLOCKSEVENTS
    RevEnchantsJackHammerEventPriority: BLOCKEVENTS
```

Or if you are not using Prison Block Events, then just MONITOR should be used for better performance.

```yaml
    blockBreakEventPriority: ACCESSMONITOR

    RevEnchantsExplosiveEventPriority: MONITOR
    RevEnchantsJackHammerEventPriority: MONITOR
```

**Please note:** These settings may also apply to the other enchantment plugins if you do not want to use Prison's block handling.


* **EntityExplodeEvents** - This is a built in bukkit explosion event that lists multiple blocks.  Some other enchantment plugins may support this event for their enchantments, and if they do, then prison will be able to support those plugins without the need of custom handlers.
*NOTE: This enchantment cannot identify the original block that triggered the explosion.  Therefore there could be possible issues once in a while.*



* **ExcellentEnchants** - Uses EntityExplodeEvents.


* **XPrison** - Prison supports some features form XPrison.  This could allow you to continue to use XPrison for enchantment, but switch over to use prison for managing your ranks and mines.



* **Zenchantments** - Optional - Some support is provided for zen, but it may not be 100%.  More work needs to be done to improve the integration in to prison.  This is an open source project.  It identifies that it supports spigot 1.9 through 1.14 (different versions).  [https://www.spigotmc.org/resources/zenchantments.12948/](https://www.spigotmc.org/resources/zenchantments.12948/).



* **Tokens** - **NOT SUPPORTED!!**  
Warning: People have paid for this plugin only to find out after the fact that it is not supported and they mistook it for *TokenEnchant* (see above).  This plugin does not have a block explosion event that prison can hook in to, so it can never be supported.  The developers have been asked a few times to add such an event, but they refused stating they did not see a purpose to add something like that.  Hopefully in the future they will add support, and when they do, then we can add it to Prison.
If you purchase this plugin to use on your server, do so with great caution since it is not supported and it may not integrate with prison.
[ * Not supported * Tokens * Not supported * ](https://www.spigotmc.org/resources/%E2%9A%A1%EF%B8%8F-tokens-%E2%9A%A1%EF%B8%8F-40-enchantments-%E2%AD%95-free-expansions-%E2%AD%95-25-off.79668/)


**Please Note:** There is another plugin by the same name "Tokens" that strictly deals with tokens and not enchantments, which works just fine with prison.  I have even personally contributed to that plugin to provide caching of the player's data to resolve an issue with ultra fast mining in prison.  Basically it used to be that if you give players tokens too quickly, it would lockup the server trying to update the save files.  Now it easily supports 100's, if not 1000's of transactions per second without any impact to the TPS.



### Enchantment Plugin Features Supported

This table of supported Enchantment Plugins are plugins that have an event that Prison is able to hook in to for the purpose of communicating multiple block breakage.  It should be noted that all of these events are related to block breakage, and originate from the original bukkit's **BlockBreakEvent**, but the other plugins takes the single block, then applies "effects" that expands the one block breakage to multiple blocks.  These events are the mechanism for conveying the list of included blocks to Prison so Prison can do what it needs to do with the blocks.


|     Plugin     | Event | Settings ID | Cancel <br> Events | Cancel <br/> Drops | External <br /> Hooks | ACCESS <br /> Priority <br /> Supported |
|       ---      |  ---  | ----------- |        :---:       |       :----:       |        :----:       |  :----:  |
| Bukkit/Spigot  | **BlockBreakEvent** | `blockBreakEventPriority` | **Yes** | **Yes** | **Yes** | **Yes** |
| Prison         | **ExplosiveBlockBreakEvent** | `ProcessPrisons_ExplosiveBlockBreakEventsPriority` | **Yes** | *No* | *No* | *No* |
| TokenEnchant   | **TEBlockExplodeEvent** | `TokenEnchantBlockExplodeEventPriority` | **Yes** | *No* | *No* | *No* |
| CrazyEnchants  | **BlastUseEvent** | `CrazyEnchantsBlastUseEventPriority` | **Yes** | *No* | *No* | *No* |
| PrisonEnchants | **PEExplosionEvent** | `PrisonEnchantsExplosiveEventPriority` | **Yes** | *No* | *No* | *No* |
| RevEnchants    | **ExplosiveEvent** | `RevEnchantsExplosiveEventPriority` | **Yes** | *No* | *No* | *No* |
| RevEnchants    | **JackHammerEvent** | `RevEnchantsJackHammerEventPriority` | **Yes** | *No* | *No* | *No* |
| Zenchantments  | **BlockShredEvent** | `ZenchantmentsBlockShredEventPriority` | **Yes** | **Yes** | **Yes** | *No* |



**Notes:**
1. A value of *No* for **Cancel Drops** will always use **Cancel Event** instead.
2. **Cancel Drops** was added in Spigot 1.12.x so older versions of Spigot *must* use **Cancel Events**
3. **External Hooks** refers to custom hooks in to mcMMO, EZBlock, and Quests.  See config settings within **AutoFeaturesConfig.yml**. It's strongly suggested to use **Cancel Drops** instead so you won't have to enable these features.  These provides a hacky-fix for the limitations when using **Cancel Events** and may not be perfect.
4. Zenchantments is flexible in how it's **BlockShredEvent** works, mostly because it extends the bukkit **BlockBreakEvent**.  The events can possibly mix with normal **BlockBreakEvent**s. 
5. It may take some effort to find the ideal priorities to use for your environment to ensure everything works as you envision it.
6. **ACCESS Priority Supported*** only the **BlockBreakEvent** should be used with the ACCESS, ACCESSBLOCKEVENTS, or ACCESSMONITOR. All events supports the use of ACCESS, but only the first block in their list is check. Therefore it really won't be ideal.  Plus all of these events originate through the **BlockBreakEvent** so if that is checking ACCESS then it should be sufficient.



### Event Listener Priorities

The above listed supported Events are assigned one of the available Prison Event Priorities.  This table illustrates what features are associated with each priority, which can be somewhat complex and confusing.


* **DISABED** - The event is not enabled within Prison's event monitoring.  The event will not be included in the command `/prison support listeners blockBreak` command.

* **ACCESS** - Access uses the identified block within the event to identify which mine the player is in, and if the player has access to the mine.  This Prison priority will run at the bukkit priority of `LOWEST`.  If the player does not have access, then the event is canceled. If the event has more than one block in the event, and it does not specifically identify which block was broken, it will use the first block in the provided block list.  Therefore, it may make the most sense that this priority, and it's variations, are best suited to be used with the Bukkit/Spigot's **BlockBreakEvent** since all other events originate through this event.

* **ACCESSBLOCKEVENTS** - Same as ACCESS in that it generates a listener that for just mine access, but it also creates another listener for BLOCKEVENTS.  This will be seen as two different listeners.

* **ACCESSMONITOR** - Same as ACCESS in that it generates a listener that for just mine access, but it also creates another  listener for MONITOR.  This will be seen as two different listeners.

* **LOWEST** - **HIGHEST** - These are the normal bukkit event priorities.  The lower the priority, then it will handle the event before other plugins with higher priorities.

* **BLOCKEVENTS** - Same as MONITOR, but also includes the processing of the Prison Block Events and sellall (auto sell) if enabled for full inventory. This Prison priority will run at the bukkit priority of `MONITOR`.

* **MONITOR** - Monitor performs no block handling within prison, but it will try to update the block counts. MONITOR will process the event even if it is canceled. 


| Priority          | Actual <br/> Event <br/> Priority | Access  | In <br/> Mine | In <br /> World | Ignores<br/> Canceled<br/> Events | Block<br/> Break | Block<br/> Count | Reset<br/> Threshold | Block<br/> Events | Mine<br/> Sweeper | AutoSell |
|    ---            |  :---:  |  :---:  |  :---:  |  :---:  |  :---:  |  :---:  |  :---:  |  :---:  |  :---:  |  :---:  |
| DISABLED          | -note1- | *No*    | *No*    | *No*    | *No*    | *No*    | *No*    | *No*    | *No*    | *No*    | *No*    |
| ACCESS            | LOWEST  | **Yes** | **Yes** | *No*    | *No*    | *No*    | *No*    | *No*    | *No*    | *No*    | *No*    |
| ACCESSBLOCKEVENTS | -note1- | **Yes** | **Yes** | *No*    | *NoYes* | *No*    | **Yes** | **Yes** | **Yes** | **Yes** | **Yes** |
| ACCESSMONITOR     | -note1- | **Yes** | **Yes** | *No*    | *NoYes* | *No*    | **Yes** | **Yes** | *No*    | **Yes** | *No*    |
| LOWEST            | LOWEST  | **Yes** | **Yes** | *No*    | *No*    | **Yes** | **Yes** | **Yes** | **Yes** | **Yes** | **Yes** |
| LOW               | LOW     | **Yes** | **Yes** | *No*    | *No*    | **Yes** | **Yes** | **Yes** | **Yes** | **Yes** | **Yes** |
| NORMAL            | NORMAL  | **Yes** | **Yes** | *No*    | *No*    | **Yes** | **Yes** | **Yes** | **Yes** | **Yes** | **Yes** |
| HIGH              | HIGH    | **Yes** | **Yes** | *No*    | *No*    | **Yes** | **Yes** | **Yes** | **Yes** | **Yes** | **Yes** |
| HIGHEST           | HIGHEST | **Yes** | **Yes** | *No*    | *No*    | **Yes** | **Yes** | **Yes** | **Yes** | **Yes** | **Yes** |
| BLOCKEVENTS       | MONITOR | *No*    | **Yes** | *No*    | **Yes** | *No*    | **Yes** | **Yes** | **Yes** | **Yes** | **Yes** |
| MONITOR           | MONITOR | *No*    | **Yes** | *No*    | **Yes** | *No*    | **Yes** | **Yes** | *No*    | **Yes** | *No*    |
| *headers*         | EvntPri | Access  | In Mine | IN Wrld | IgCanEv | Blk Brk | Blk Cnt | ResetTh | BlkEvnt | MineSwp | AutoSel |


**Notes:**
1. **ACCESSBLOCKEVENTS** and **ACCESSMONITOR** will run two events.  One of which will be **ACCESS** which will have an actual bukkit priority of `LOWEST`.  And the other listener will run a **BLOCKEVENTS** or **MONITOR** Prison priority which both will have an actual bukkit priority of `MONITOR`. 
2. **DISABLED** will not have an actual event priority. **ACCESSBLOCKEVENTS** will have two processes; see actual priorities for ACCESS and BLOCKEVENTS.  **ACCESSMONITOR** will have two processes; see actual priorities for ACCESS and MONITOR.
3. **DISABLED** will prevent a listener from starting, and it will prevent any processing of that event.
4. **Access** managed by Prison requires the use of Access by Rank (preferred) or Access by Perms.  It also will vary in effectives based upon the priority level in relation to other plugins, where any plugin that has a lower priority than Prison will bypass Prison's attempts to control access.
5. **ACCESSBLOCKEVENTS** and **ACCESSMONITOR** are able to have a duel behavior because these priorities will create two different listeners, each at a different priority, and each having a different purpose.
6. **Block Break** refers to Prison handling the whole block break processing and drops.
7. **Mine Sweeper** should never be used unless there is no other way to count all the broken blocks.
8. Support for outside of the mine for auto features maybe added soon.  The way it will probably be supported would be through a list of worlds in which it should be active, plus the use of WG regions too.
9. The **MONITOR** priority, including ***BLOCKEVENTS** will ignore all events that have been canceled and will process them anyway.  Therefore **ACCESSBLOCKEVENTS** and **ACCESSMONITOR** will fail on the "access" part if the event is canceled when Prison gets a hold of it the first time, which will deny access to the mines, but it will also still process the event under the priority of MONITOR or BLOCKEVENTS.



 
### Other Plugins
 

* **Holographic Displays** Optional, but Suggested - Will need to use PlaceholderAPI, ProtocolLib, and also HolographicExtension to enable HD to work with other placeholders.


* **AnimatedScoreboard** - Optional - Scoreboard


* **Scoreboard-revision** - Optional - Scoreboard - NOTE: This is buggy if a color code falls in a certain area of the scoreboard.  If you see glitches, you may be able to add a space or character before the placeholder.  Such a fix may not always work though.


* **TAB** - Optional - Tab Menu


* **Crazy Crates** - Optional - Crates Plugin


* **Multiverse-Core** - Optional - Multiple Worlds - Including the ability to create void worlds




<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">




# Important Prison Information

Upon starting up, Prison will display a lot of information in the server's console.
This information is intended to help you configure and confirm that prison started 
correctly with all of the related resources that it is using.  It also provides you 
with valuable information that is needed to help troubleshoot issues, if you should
happen to encounter any.  

Some of the important details that are listed:
* Versions of Prison, Spigot, and other plugins
* Which modules were successfully loaded
* The root commands that are available
* The integration status of all related plugins that are supported
* The list of active placeholders (removed due to size. see `/prison placeholders list`)
* Startup error messages, if any.  Examples would be if a rank is configured to use a 
custom currency and that currency cannot be found in any economy.


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


# Server Start Up Script


If you are leasing a server from a hosting service you may not be able to customize the startup script. But if you have control over it, then the following information may help.


`java -Xms2g -Xmx4g -jar spigot-1.16.5.jar -nogui`


Example of enabling debug hooks for the server.  This is used with Eclipse, and may work with other IDEs since it's a java directive.

`java -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005 -Xms2g -Xmx8g -jar spigot-1.16.5.jar -nogui --log-strip-color`


Note: The use of `--log-strip-color` may or may not work within your environment.



<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">

# Setting up an Eclipse Debugging Session

To go along with the above settings for debugging on port 5005, you need to setup in Eclipse the correct remote debug configuration using a Remote Java Application.

Detailed settings can be found here:


[https://www.spigotmc.org/wiki/eclipse-debug-your-plugin/](https://www.spigotmc.org/wiki/eclipse-debug-your-plugin/)


Basically, under "Debug Configurations...", add a new "Remote Java Application".  Select a name, such as "Spigot Debugger - Local", with port `5005`, and `localhost` for the host.

With the spigot server already running, then set a break point in the code, then start the debugger. 

If the Eclipse debugger breaks on the selected breakpoint, but yet does not show any source, click on the button in the empty source window to select the correct source.  From the popup window, then choose to select an existing Java Project, then select all listed projects associated with your plugin(s), including all sub-projects.  The debugger should then select the correct source.


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# Getting Help

If you should run in to any questions or issues, please first review all online documentation first.


If you cannot find what you need within the documentation, please visit our Discord server to get the quickest responses there.  
[Prison Discord Server](https://discord.gg/DCJ3j6r)


In order to provide the best support, the prison startup screen provides most of the information that is needed to help trouble shoot your issues.  Please take a screen print and provide on discord with a detailed explanation of the issue.



If you do encounter an issue, and the startup information is requested, please include
everything from the first line to the last. Please take a screen print and provide on 
discord with a detailed explanation of the issue.  Include everything from:

```
	[16:21:30 INFO]: [Prison] Enabling Prison v3.2.5
```

through:

```
	[16:21:31 INFO]: | Prison | Prison - Finished loading.
```


# Prison Support Submit Information

Prison now has a built in way to share your configurations and settings with support personnel.

More information will be documented in the future, but for now, here are the basics on how to use it.

When requested by the Prison support team, you would first enter the following command to set your name that will be included on all reports to help identify who the are related to.  It doesn't have to be your full discord name, but enough characters to allow us to identify who you are.


These commands will collect all of the related information from your Prison setup, and send it to the website `https://paste.helpch.at`.  It will provide you with an URL.  All you need to do is to copy and paste that URL in to the discord chat so the Prison support team can help with your issue.


`/prison support setSupportName <yourName>`

Once entered, it will enable the following submit tools:

`/prison support submit`  - Show the available tools.

```
/prison support submit version
/prison support submit configs
/prison support submit latestLogs
/prison support submit mines
/prison support submit ranks
```

Here is an example that I generated from one of my test servers on 2021-12-03.  I have no idea how long the content remains available, but for support purposes, we only need this information for a few hours.  It appears like this information is never deleted?  As such, here are two different versions which shows you how much more information has been addeed. 
  [https://paste.helpch.at/silihuxaja](https://paste.helpch.at/silihuxaja) From Prison v3.3.0-alpha.15a
  [https://paste.helpch.at/itejovejeh](https://paste.helpch.at/itejovejeh) From Prison v3.2.11-alpha.9


# Prison Commands

On the startup screen, prison shows all of the base commands that are active. From these commands, they will provide you with sub-listings of all the other commands.

* **/prison**
* **/mines**
* **/ranks**
* **/rankup** 


If you use the command `/prison` it will not only display all of the sub commands available for `/prison`, but it will also include a list of all the other *root* commands and aliases that have been setup.


<img src="images/prison_docs_012_prison_basics_01.png" alt="Prison Commands" title="Prison Commands" width="600" />  



These commands are intended to run in game, but most can be ran from the system console.  Sometimes the system console is easier to displays longer listings, such as **/mines list**.  Also the console is better with wider text, and with easier to read text since it's not trying to display over a mc world.


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


# Getting Started

There is a lot to do get your server up and running.  But here are some ideas on what to get started on first.  It may even be a good idea to create a couple of small mines in an area that you have not spent much time with your final builds.  Plan on creating a couple of test mines and ranks, then deleting them.  You can quickly get a good understanding of how prison can be setup by playing around with a lot of the setting within a few minutes.


Remember that the command **/prison version** will show all the available root level commands by the modules.  Entering those commands will show all of the related sub commands.  


* Create a couple of small mines.  About 10x10x5 blocks in size would work well.  Above ground is easy too.
* Play around with adding blocks, removing them, searching for blocks to add.
* Try to resize the mines, and even deleting them.

* Go ahead and create a couple of ranks.
* Setup the permissions (see the WorldGuard and LuckPerms document for examples)
* Add some simple permissions to the rank commands
* Test the ranks and the /rankup commands.

* To clean up, you can remove the test ranks (or keep them) 
* Remove the mines.


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">

