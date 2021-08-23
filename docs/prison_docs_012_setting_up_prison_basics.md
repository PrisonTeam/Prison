
### Prison Documentation 
[Prison Documents - Table of Contents](prison_docs_000_toc.md)

## Setting up Prison - The Basics

This document provides a quick overview on how to install Prison and get it running.

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# Download Prison


Download Prison from one of the following sites:
* [spigotmc.org's Prison History Page](https://www.spigotmc.org/resources/prison.1223/history).
* [Polymart.org](https://polymart.org/resource/prison-1-8-x-1-17.678)
* [bukkit.org](https://www.curseforge.com/minecraft/bukkit-plugins/mc-prison-v3)



Setting up Prison is simple:

* **Download Prison - Current Releases**
    - Prison's published releases

    	
* **Download Prison's Pre-Release Version**
    - Useful to access newer features and fixes
    - You can always find the latest alpha build on the Discord Server in the #alpha-versions channel:
        - [Prison Discord Server](https://discord.gg/DCJ3j6r)

* Copy the prison jar file to your server's plugin directory.  

* Remove any older prison jar file

* Restart the server. 

* Prison's startup information contains a lot of information.  If you ever have issues, check that information first since it probably will identify what the issues are.


* It is strongly suggested that `/ranks autoConfigure` is ran to initially setup your Prison environment.


* Follow Prison's documentation on customization; at this point it's ready for use. 


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# Prison's Dependencies on Other Plugins

* **None - No hard dependencies** - There are no hard dependencies for Prison.

There may be no hard dependencies that will prevent Prison from running, but there are some core plugins that will make it easier to use.  This short list is just a suggestion, but alternatives do exist and may be outside of our ability to comment or assist in their usage.


* **Vault** - Optional, but STRONGLY Suggested - This is perhaps the most important plugin.  This plugin provides a common way to access other plugins running on your server, but without having to write any code within Prison to support them.  Vault provides the mapping of a plugin's unique APIs to a common Vault API.  Vault helps support Economy, Permissions, and Placeholders.  Because of Vault, Prison can work flawlessly with dozens of other plugins.  Please refer to Vault's documentation for what it supports.


* **EssentialsX** - **STRONGLY SUGGESTED**, but still Optional - Provides many of the basic commands and behaviors that you would expect from a Spigot server such as chat, warps, and even some moderation commands and commands that can be given to premium players.  EssentialsX is not Essentials, since Essentials is an older abandoned project, and EssentialsX is a forked project that is still maintained.  Unfortunately, internally it is identified as simply Essentials, but you can tell it's EssentialsX if the version is greater than 2.15.x.  EssentialsX is released as a zip file and you must extract the jars that you are interested in.  It should also be pointed out that all EssentialsX jars should come from the same zip file so they will be of the same version and the same release.


### Economy Plugins - Required

Prison requires an active economy in order to active the Ranks plugin.  When Prison starts up, it performs many validations on the mines and ranks as they are being loaded.  With Ranks, if Prison cannot find an active economy, then it will refuse to load the Ranks module due to possible server corruption (ie... what failed that there is no economy).


* **EssentialsX Economy** - SUGGESTED - Optional - This is a simple economy plugin that just works well.  If you don't have a specific need to use another economy plugin, then it may be best to use this one since it works so well.
  

* **CMI Economy** - Not Suggested - CMI has a lot of really neat features, so it's totally understandable that you may want to use their economy too.  But the reason why we do not suggest it's use is because it is difficult to get to work with prison for a few reasons.  We will try to support your use of CMIE, but you will have to try to be proactive in trying to get it to work; if you just want "simple", then try EssentialsX Economy first.  
1) You must use the normal Vault and then use the CMI Vault Injector.  We've never seen the CMI provided version of Vault work with prison.  Symptom is that prison reports a 0.00 amount for the player when using `/ranks player <playerName>`.  The Vault inject has always worked well.  
2) The CMI Economy **has** to be fully loaded and active *before* prison loads the Ranks.  Otherwise prison will refuse to load the ranks and prison will not work.  It appears as if CMIE is purposefully delaying it's activation until all other plugins are finished loading; I'm sure there is a good reason for that, but it causes prison to fail.  Setting up proper soft dependencies within Prison does not work. To address this serious issue, because we really want CMIE to work with Prison, there is a new setting that will actually delay prison's startup to give CMIE a chance to active.  This new feature should not be used without a good reason since it alters Prison's startup processes, but it has shown to work very well. The configs are within the config.yml file, but talk to Blue *before* trying to enable it.


### Chat Plugins - Optional

* **EssentialsX Chat** - Optional - Enhanced Chat experience. Provides customizations to the chat prefixes.



* **LuckPerms Chat Formatter** - Optional - Used in place of EssentialsX Chat?



### Permission Plugins - Required


Permission plugins are not *strictly* required for Prison to work, but within a server environment almost *everything* depends upon permissions in order to make things work.


Prison actually uses bukkit's permission interfaces.  This makes it simple for Prison, but it also limits what prison can do.  For example, one limitation is with permission groups; Prison is unable to resolve permission groups since that "concept" does not exist in bukkit, but is a concept that is implemented through plugins like PEX and LuckPerms.


* **LuckPerms** - Required - LuckPerms is a great permission plugin that is actively supported and has many new features to make managing your server easier.


* **LuckPerms v5.x.x** - Use the latest version of LuckPerms whenever you can.  Keep in mind that releases to spigotmc.org is somewhat infrequent; giving them the "counts" for downloads is nice, but you may have to go to their main download page to get the most recent releases: [https://luckperms.net/download](https://luckperms.net/download).  Please note that is normal for issues to surface once in a while, and there were a few that needs to be avoided: The releases v5.3.0 through about v5.3.3 had some issues that caused significant logging and failures.


* **LuckPerms v4.x.x** This older version of LuckPerms is still supported, but it is highly recommended to upgrade to the latest v5.x.x release.  LuckPerms v4.x.x is required for prison v3.2.0 and older (Prison v3.2.1 and newer supports all versions of LuckPerms).  Please consider upgrading both Prison and LuckPerms to the latest releases for the best experience.


* **NOTE: A permissions plugin of your choice** - Required - Prison works with many different permission plugins through Vault.  I strongly suggest LuckPerms since it is free and is under active development so bugs and performance issues will be addressed.  If you choose to use another permission plugin, then please consider ones that work with Vault; Prison uses Vault provide the flexibility of choosing from many different plugins.


* **Other Permission Plugins** There are many out there, and as developer of Prison, and through providing support, we don't hear of any issues with other permission plugins.  The reason for this is because they probably just work well to begin with, and that Prison uses bukkit's permission interfaces.  So it keeps things simple for setting up Prison.  If you are just getting started with building a server, then we suggested keeping it simple with something like LuckPerms, but there are other permission plugins out there that can provide a whole new experience with your server, such as jobs and careers for your players.   


### Placeholder Plugins


Chat related plugins provides access to Prison placeholders, but these placeholder plugins takes it to the next level and allows other plugins to access prison data.  There are not many out there, mostly because papi is so simple and does it so well so there is no real need for other plugins.


It should be noted that Prison's placeholders do not include the escape characters, and they may vary in the other plugins that you use placeholders.  Sometimes they may be { } or % %, and sometimes you may have to mix the two.  The plugin's documentation should help you identify what's the correct usage.


* **PlaceholderAPI** - Strongly Suggested - Also known as papi.  Used through Vault, it is free and provides the core interfaces in to the usage of placeholders.  Prison also has a special integration setup for PlaceholderAPI to register all of the Prison placeholders dynamically upon startup.  You do not need to download anything from the cloud for prison to work with this plugin.  Also if you reload papi, you do not have to reload Prison's placeholders.  Prison registers placeholders that are all lower case and more recently, all upper case too so it's easier to use placholders through papi.


* **MVdWPlaceholder** - Suggested to Avoid - Prison does support this plugin, but since it is used mostly with premium plugins, we have no way to fully test this plugin to ensure it actually works correctly.  We've heard very few people have used this plugin, but we've heard it does work well. Use at your own risk. 
With this plugin, all placeholders are registered with it automatically when prison starts up, and all placeholders should be used as all lower case.  Because prison has so many plugins, with many that are expanded based upon ladders, ranks, and mine names, a modest prison server could generate and register well over 1000 placeholders.  MVdWPlaceholder appears to be very verbose so you will see a lot of logging.
It should also be noted that because of some of the limitations of MVdW, not all features of Prison's placeholder support will be supported.  For example, you may not be able to reload placeholders, or use placeholder attributes to customize how placeholders are used.



### World Protection Plugins


* **WorldEdit** - Recommended - Used with WorldGuard


* **WorldGuard** - Recommended - Used to protect your worlds.  At a minimum setup the `__global__` region to protect your world from players.

```
/rg flag __global__ -w world passthrough deny
```


* **Fast Async World Edit (FAWEs)** - Recommended on the newer Spigot Versions - Read notices on compatibility with WorldEdit and WorldGuard.  Some people report issues with this plugin; read reviews and do your homework if you're having issues.


* **CoreProtect** - Optional - Server protection against griefing and building mistakes, and even server failures that may corrupt blocks/chunks.



### Enchantment Plugins


* **CustomItems** - Recommended - Premium Plugin - Allows for the use of custom blocks within Prison.  This provides for a great deal of customizations, including custom textures for your custom blocks. Prison supports CI at about 95% or more.  If you need additional support added for CI, please contact Blue and he will add it for you.  
[https://polymart.org/resource/custom-items.1](https://polymart.org/resource/custom-items.1)


* **TokenEnchant** - Recommended - Premium Plugin - This is one of the few recommended premium plugins that we would recommend, but it works very well with prison.  It took a lot of effort to get this to work with Prison, but is perhaps the most supported one too.  Keep in mind that it is premium and they also charge for other add on features, so the initial cost may not be your final cost.  
[https://polymart.org/resource/tokenenchant-1-7-10-1-17.155](https://polymart.org/resource/tokenenchant-1-7-10-1-17.155)


* **Prison Enchants** Newest supported enchantment plugin supported.  More information will be added soon.


* **Crazy Enchantments** - Optional - Some support is provided for Crazy Enchantments, but it may not be at 100% in all areas. This is an open source project and supports Spigot 1.8 through 1.16.
[https://www.spigotmc.org/resources/crazy-enchantments.16470/](https://www.spigotmc.org/resources/crazy-enchantments.16470/)


* **Zenchantments** - Optional - Some support is provided for zen, but it may not be 100%.  More work needs to be done to improve the integration in to prison.  This is an open source project.  It identifies that it supports spigot 1.9 through 1.14 (different versions).  [https://www.spigotmc.org/resources/zenchantments.12948/](https://www.spigotmc.org/resources/zenchantments.12948/).


* **Tokens** - **NOT SUPPORTED!!**  
Warning: People have paid for this plugin only to find out after the fact that it is not supported and they mistook it for *TokenEnchant* (see above).  This plugin does not have a block explosion event that prison can hook in to, so it can never be supported.  The developers have been asked a few times to add such an event, but they refused stating they did not see a purpose to add something like that.  Hopefully in the future they will add support, and when they do, then we can add it to Prison.
If you purchase this plugin to use on your server, do so with great caution since it is not supported and it may not integrate with prison.
[ * Not supported * Tokens * Not supported * ](https://www.spigotmc.org/resources/%E2%9A%A1%EF%B8%8F-tokens-%E2%9A%A1%EF%B8%8F-40-enchantments-%E2%AD%95-free-expansions-%E2%AD%95-25-off.79668/)

 
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
* The list of active placeholders
* Startup error messages, if any.  Examples would be if a rank is configured to use a 
custom currency and that currency cannot be found in any economy.


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


# Server Start Up Script


If you are leasing a server from a hosting service you may not be able to customize the startup script. But if you have control over it, then the following information may help.


`java -Xdebug -Xms2g -Xmx4g -jar spigot-1.16.5.jar -nogui`


Example of enabling debug hooks for the server.  This is used with Eclipse, and may work with other IDEs since it's a java directive.

`java -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005 -Xms2g -Xmx8g -jar spigot-1.16.5.jar -nogui --log-strip-color`


Note: The use of `--log-strip-color` may or may not work within your environment.



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

