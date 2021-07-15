
### Prison Documentation 
[Prison Documents - Table of Contents](prison_docs_000_toc.md)

## Configuring and Using WorldGuard with LuckPerms to Protect Mines

This document explains how to setup WorldGuard to protect your mines and how to prevent players from accessing mines they shouldn't have access to.


The preferred way is with using **Access by Ranks**, but other alternative techniques are also included in this document, but some may not be supported (read carefully).  It also explains how to setup the permissions in the Prison's **/ranks command add <rankName** so they are ran automatically during a **/rankup** and **/ranks promote** event. This document also covers what needs to be configured to ensure that the rank commands will work properly with **/ranks demote**.



<hr style="height:8px; border:none; color:#aaf; background-color:#aaf;">



# Please READ This First - Using Access by Ranks to Simplify Many Things


The latest versions of Prison has a new feature called **Access by Ranks** where a player, based upon their rank, is able to access a mine (break blocks within a mine) and also they can use the `/mtp` feature (mines teleport).  By using **Access by Ranks** you do not have to setup any WorldGuard regions to allow players to break blocks within the mines, and you don't have to setup any permissions.


How to use Access by Ranks:

* Setup a WorldGuard __global__ region as defined below.  That global region must enable the flag `passthrough deny`.

* Then setup prison using `/ranks autoConfigure` since it will link mines to ranks, and enable the Access by Rank features.  You are done.



If you need to manually setup Access By Ranks:
* Link mines to the ranks: `/mines set rank help`
* For each mine, enable this feature `/mines set mineAccessByRank help`
* To grant TP access for the players, then also enable `/mines set tpAccessByRank help`.



**Note:** You can setup WorldGuard regions to keep out non-players from the surrounding area around a mine. Access by Rank does not prevent non-members from walking to a mine.


**Note:** As a fallback, you can use **Access by Permissions** which requires more setup and figuring out how to use the perms.  But you do not have to define any WorldGuard regions to allow players to break blocks.  See the command `/mines set accessPermission help` for more information.  NOTE: This is not recommended since it's a more complex configuration process than Access by Ranks.


<hr style="height:8px; border:none; color:#aaf; background-color:#aaf;">


# Prison's Event Priorities and WorldGuard Regions


**NOTE:** You can also grant access through WorldGuard regions, and then prison will allow anyone to break blocks in the mines.  The catch is that you must ensure WorldGuard checks access prior to Prison getting control of the BlockBreakEvents.  This requires setting up WorldGuard regions and permissions. 


**WARNING:** **This is beyond the scope of what prison will provide support** since we highly recommend using **Access by Ranks**, or as a secondary option, Access by Permissions.  Too many users have had problems getting WorldGuard to work properly with their permission plugins and with prison, that this way of granting players access to mine blocks is not supported by Prison any longer.  You can still do it this way, but you are on your own.


If you will be setting up WorldGuard regions to permit block breakage, then keep in mind that as of Prison v3.2.7 you can now change the priority of prison's event listeners for BlockBreakEvents and explosion events.  This can help you fine tune the use of the events.


Please see the `autoFeaturesConfig.yml` configuration file to make changes.  Prison is using the default value of `LOW`, but if you need to make adjustments, you can do so under the group `options.blockBreakEvents` as listed below (which is out of date, please see the configuration file that ships with your version of Prison):  


```
options:
  otherPlugins:
    isProcessTokensEnchantExplosiveEvents: true
    isProcessCrazyEnchantsBlockExplodeEvents: true
    isProcessMcMMOBlockBreakEvents: true
    isProcessEZBlocksBlockBreakEvents: false
  blockBreakEvents:
    blockBreakEventPriority: LOW
    TokenEnchantBlockExplodeEventPriority: LOW
    CrazyEnchantsBlastUseEventPriority: LOW
    ZenchantmentsBlockShredEventPriority: LOW
```

Valid values are `LOWEST`, `LOW`, `NORMAL`, `HIGH`, and `HIGHEST`.  You can also use `DISABLED` to prevent the use of those listeners, but keep in mind that if you have auto features enabled then you set the priority to `DISABLED` then auto features will not work.  If disabled is specified then it will also disable the `MONITOR` even listener so prison will be unable to track which blocks are broken or provide a count of what remains.  

You cannot set any of the above event priorities to MONITOR since that goes against the bukkit/spigot standards to change block states under the MONITOR priorities.



<hr style="height:8px; border:none; color:#aaf; background-color:#aaf;">



# Please READ This Next

This document is a work in progress.  This is a complex topic and depending upon how your environment is setup, the actual configurations may need to vary from what's covered in this document.


The first attempt at this document tried to use region templates, where a template would define the flags set for each region.  That way each mine's specific region would have the template as a parent.  Unfortunately, within LuckPerms there is no such thing as a hierarchical permissions, but instead its granted access to all associations.  So this design failed because once you gave a player access to mine A, then they would have access to all mines.   So if you're thinking about setting up group templates, then you may want to reconsider and do a lot of testing if you use them.


This document used to go in to detail on how to setup WorldGuard regions to grant players the ability to break blocks.  But since that proved numerous times to be confusing, and problematic, Access by Ranks and Access by Perms were added to replace the need to use WorldGuard regions for controlling block breakage.


If you decide not to use **Access by Ranks** or not to use **Access by Permissions** then we cannot support you in your setup and configurations.


The following document provides *some* information if you want to try to use WG regions, but we make no warranty as to how accurate these documents may be, or how successful you will be.
 
<hr style="height:8px; border:none; color:#aaf; background-color:#aaf;">


# Other Dependencies 

* [Install WorldGuard and WorldEdit](prison_docs_026_setting_up_worldguard_worldedit.md)
* Install a Permissions Plugin that is compatible with Vault 
    * This guide uses LuckPerms.  See [Setting up LuckPerms](prison_docs_020_setting_up_luckperms.md) for more information.

<hr style="height:3px; border:none; color:#aaf; background-color:#aaf;">



# Setting up WorldGuard and WorldEdit:

Install both WorldGuard and WorldEdit as required for your version of the server and Minecraft. Follow the general directions in the link above.  If you require additional help, there should be plenty of good resources if you search for them.


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# In Game versus In Console

**Important to understand:** When you add commands to the **/ranks command add** then they will be executed as if they were being entered through the console. Any references to players within a rank command must be replaced with `{player}` or `{player_uid}`.

Please note that for WorldEdit and WorldGuard there is a slightly different way of entering commands **in game** versus **in console**.  This is very important to understand, because most of the commands may be entered in game, but when you add some of these commands to the **/ranks command add** then they will be executed as if they were being entered through the console.

When you are in game, the world you are in will be used as a default value in any command that requires a world parameter.  When you are entering commands from the console, you must specify the world parameter.  Failure to specify the world will prevent the command from running.  This will cause problems during the running of the **/rankup** commands.  

The WorldGuard documentation says the following about the `-w` flag. 

```
-w <world> can be specified to run this command for a different world or from console
```

There are a number of WorldEdit and WorldGuard commands that cannot be ran from the console.  For example, WorldEdit's //pos1  and //pos2 are used to set two points that can be used to define a WorldGuard region.  The problem is that you cannot speicify the world with those commands, so therefore they cannot be used in any rank commands.


Please note **from console**.

**Please Note about other Plugins:** Although this example is using WorldGuard from the console, which requires the use of the **-w** flag, other plugins may also require the use of special considerations when being ran from the console.  If the in-game command does not work, then review that plugin's documentation to see if there is any special requirements to run from console.


<hr style="height:3px; border:none; color:#aaf; background-color:#aaf;">



# Naming Conventions to Consider


Please note that whatever you choose to use for the region names or the permissions is up to you.  It may be easier to understand what is what, if it has some kind of context such as a region named **mine_<mine-name>** when compared with **mine_area_<mine-name>**.  Same with permissions.  If they begin with **prison.mines.<mine-name>** you will easily understand it role versus a **prison.tp.<mine-name>**, or a permission for notifications, or even enchantments.

Putting thought in to the naming of resources, such as regions and permissions, can help make managing your server easier, especially if you have someone joining your staff well after your server has been released.


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# Enable Yourself - Op'ing and Wanding

To simplify things, make sure you are **op**.  You should also know how to **deop** yourself too.  You can **deop** yourself in game too, just prefix the command with a slash.

From console:

    op <yourName>
    deop <yourName>
    
From in game:

    /deop <yourName>


Then **in game**, give yourself a WorldEdit wand:

    //wand

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# Protecting the World from Players

**Purpose:** This prevents players from breaking any blocks in the world. It also prevents mobs from spawning.


As op, protect the whole world with a passthrough flag set to deny. This will prevent building, PVP, and everything else.  Basically, any action that “passthrough” all over defined regions, will be denied.  The command with the **-w world** parameter has been added to the following list too.  Use that version from console, the other without **-w world** in game.  And where the name **world** is the actual name of your world.


Note: the minimum you will need is the first line.  The other two shuts down mob spawning, which is optional.


In game commands:

    /rg flag __global__ passthrough deny
    
    /region flag __global__ mob-spawning deny
    /gamerule doMobSpawning false


Console commands.  Notice the addition of the world parameter:
   
    /rg flag -w <world> __global__ passthrough deny
    
    /region flag -w <world> __global__ mob-spawning deny
    /gamerule doMobSpawning false


Note that the **/gamerule doMobSpawning false** may also help prevent mobs from spawning.  It's like double protection.

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# Various LuckPerm Commands for Templates and Mines

*Not supported - For informational purposes only*

The WorldGuard regions are covered below, but first you need to setup the groups within LuckPerm. Failure to create the groups prior to using them with the regions and prison rank commands may result in failures to work properly.


These examples use the prefix of `prison.mines` so we know what these groups and permissions are related to the mines.


LuckPerms commands to create a group is as follows. 

	/lp creategroup <group>


We **must** create a LuckPerms group for every mine.  Only mine `a` and `b` are shown here, but create one for each mine.
	
	/lp creategroup prison.mines.a
	/lp creategroup prison.mines.b
	
	
To check to see if these groups are setup properly, you can inspect them with the following commands.

    /lp listgroups
    /lp group prison.mines.a info


To grant permission to the players, you need to use the following commands since we need to add the player to the group. 

Please be aware of the difference between **set** and **add**.  Most of the time you probably want to use **add** otherwise **set** will mess up your LuckPerms prefixes if you have them configured since you can only have one primary group set at a time (set being the key word that assigns a group to be the primary).

Setting the primary Luckperms group:

    /lp user <user-name> parent set <group-name>
    /lp user <user-name> parent set prison.mines.a
    
Adding a LuckPerms group to a player:

    /lp user <user-name> parent add <group-name>
    /lp user <user-name> parent add prison.mines.a
    

And to now hook this up to prison, you do same command, dropping the leading slash, but with adding a rank command prefix and use the {player} placeholder (more on this later)


    /ranks command add a lp user {player} parent set prison.mines.a
    
    /ranks command add a lp user {player} parent add prison.mines.a

    
<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">

   


# Unprotecting a Mine for its members - Required for all Mines

*Not supported - For informational purposes only*


**Purpose:** This will actually give members the ability to perform mining related tasks within the mine.  They need to be able to break the blocks within the mine, and to pickup items, XP, and allows item drops. 



This defines a WorldGuard region, and needs to be applied to all mines, unless the Mine Access Permissions are used.


Select the same area of the mine with the WorldEdit **wand**, then use the following commands to define a mine.  It will define a region with the mine’s name, and set the parent to mine_template, with the only member ever being the permission group **prison.mines.<mine-name>**.  Never add a player to a WorldGuard region since it will get messy.  Always use permission based groups and then add the player to that group.


This example includes an owner of this mine which is the group owner.  And added the group admin as a member so the admins will have full access to this mine, even if they do not personally have the player's rank to access this mine. The actual members you add are up to you, but these are just two examples that you should consider.


    /region define prison_mine_<mine-name>
    /region addmember prison_mine_<mine-name> g:prison.mines.<mine-name>


    /region setpriority prison_mine_<mine-name> 10
    
    /region flag prison_mine_<mine-name> block-break allow
    /region flag prison_mine_<mine-name> item-pickup allow
    /region flag prison_mine_<mine-name> exp-drops allow
    /region flag prison_mine_<mine-name> item-drop allow
    
    
*Optional:*

    /region addowner prison_mine_<mine-name> g:owner
    /region addmember prison_mine_<mine-name> g:admin
    

Set the *priority* to a value of 10 to take higher precedence of other lower regions that may overlap.


Please note that with some versions of WorldGuard, such as 1.8.8, there are some blocks that cannot be broken within regions with the use of the **flag block-break allow**.  The reasons of why this was setup this way is unknown to myself.  Examples of some blocks are **sea_lantern**, **prismarine**, **dark_prismarine**, and other variations of prismarine.  In order to break these blocks the **flag build allow** must be used, but then the players are able to place blocks within the mine, which is not usually acceptable.  It should also be noted that depending upon how your server is configured prison may also be able to break these blocks within these regions, but if there are issues with these kinds of blocks, then realize the cause is how WorldGuard treats the blocks.


The following region setting for access and deny may *appear* to be useful, but don't use them.  Explanations follow.  **Do not use the following:**

    /region flag prison_mine_<mine-name> entry -g nonmembers deny
    /region flag prison_mine_<mine-name> x allow
    /region flag prison_mine_<mine-name> entry-deny-message You must rank-up to access this mine.
    

**NOTE:** 

It’s a bad idea to deny access to the mines through these regions. Such as with **-g nonmembers deny** on the **prison_mine_<mine-name>** regions. If the players doesn't have access to the mines, and they try to enter from the top, WorldGuard will continually prevent them from entering, or more specifically it will prevent them from falling in to the mine.  This will basically keep them floating in the air which will trigger a fly event within anti-hacking tools.  It will be far more professional to protect the area that contains the mine, thus you can protect it over the whole y-axis too. Players can also get caught in a rapid loop where WorldGuard is trying to kick them out of the mine when restricting just the mine; could possibly cause a lot of lag, depending upon how many event’s are being triggered.


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">





# Protecting a Mine's Area - Required for all Mine Areas

*This kind of a region is partially supported.  It is used to physically prevent a player from entering an a mine's area.**


**Purpose:** To keep out all non-members from a mining area.  The mining area, as in this context, is the area that immediately surrounds a mine, and generally non-members should not have access to it.


**Important:** You don't need to define mine-area regions if your mines are geographically isolated, such as islands in a void world.
  

In general, it may be tempting to restrict access to the mine itself so non-members cannot mine it.  But there is a serious problem with just protecting the mine, and that’s when non-members walk on top of the mine.  They will fall in to the mine, as expected, but WorldGuard will try to keep them out, so they will be bumped back above the mine, thus triggering a “fly” event, or a “hover” event.  This action may trigger anti-hacking software to auto kick them, or auto ban the players, or the players could get stuck, and it may even cause a lot of lag on the server too.

This also happens really fast, in a very repeated action, so it could lock up the player so they cannot jump back out before they get banned.  I do not know if this could contribute to server lag, but a lot of processing appears to be happening so it is possible.

The suggested action is to create a new region around the mine and protect that from entry from non-members.  This region can then be extended from y=0 to y=255 with the WorldEdit command `//expand vert``. If anyone does get past it, they still won’t be able to mine.


The primary purpose is to keep non-members out of the region.  It will also prevent non-members from TP'ing in to the area too.  It will also supply the player with an error message to inform them they don't have the rn


Select the an area around the mine with the WorldEdit **wand**.  Only select a rectangle area around the mine, ignoring the **y** axis.  Then use the following commands to define a mine.  It will define a region with the mine’s name, and set the parent to mine_template, with the only member ever being the permission **g:prison.mines.<mine-name>**:


The command **//expand vert** will take your selection and extend the **y** to cover the whole vertical range in your region.  This is why you don't have to be concerned with the *y* axis when defining your mine area regions.


Just like **prison_mine_<mine-name>** WorldGuard region, we need to add the **g:prison.mines.<mine-name>**.

    //expand vert
    /region define prison_mine_area_<mine-name>
    /region addmember prison_mine_area_<mine-name> g:prison.mines.<mine-name>


    /region setpriority prison_mine_area_<mine-name> 10
    /region flag prison_mine_area_<mine-name> entry -g nonmembers deny
    /region flag prison_mine_area_<mine-name> entry-deny-message You must rank-up to access this mine.

    
*Optional:*

    /region addowner prison_mine_area_<mine-name> g:owner
    /region addmember prison_mine_area_<mine-name> g:admin
    

You would need to repeat these settings for each mine.


Notice we are using the same permission permission for both the mine and the mine area: **g:prison.mines.<mine-name>**.  This keeps it simple by reducing the number of permissions we have to give the players.


Of course, just like **prison_mine_<mine-name>** region, we also give `owner` and `admin` access too.

    

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">





# Granting Access to a Mine and Removal of the access

**Purpose:** From either the console, or from within game, manually grant a player access to a mine.

To add a player to the mine regions is as simple as giving the user the permission associated with the mine region.  

It's important to understand that you never add a player as a direct member of a mine region, since if you have thousands of players, it will make your configuration files messy, and could add to lag dealing with such large data files per region.

Instead, you add the players to a permission group that has access to use the mine regions.  One way to look at this is that a permission is like a key, and you're giving players a copy of the key access the mines.  With a slight twist on that analogy, since we're dealing with groups, everyone within that group shares the same key for the mine regions.  


The correct way to add a player to a mine region. Indirectly by giving them access to the "keys". Or in other words, since we hooked up the LuckPerms group `prison.mines.<mine-name>` then all we need to do is add them to the group and they will have access to the proper regions.


NOTE: With world guard we had to use the prefix of `g:` to indicate the permission was a group.  But with luckperms since we are using the `parent` option it implies its a group.  Therefore if `g:` is used in luckperms it would be an error.


Template and examples as used in rank commands using **parent set**, you may want to actually use **parent add** instead:

    /lp user <player-name> parent set <group-name>

    /lp user <player-name> parent set prison.mines.<mine-name>

    /lp user {player} parent set prison.mines.a
    /lp user {player} parent set prison.mines.b


Examples using **parent add** for the groups:

    /lp user <player-name> parent add <group-name>

    /lp user <player-name> parent add prison.mines.<mine-name>

    /lp user {player} parent add prison.mines.a
    /lp user {player} parent add prison.mines.b



It is important to know how to remove access from a player so they can be demoted or removed from an area that they should no longer access.  Please beware that with luckperms to add a group to a player it is "parent set" and then to remove the group from the player you use "parent remove".  For permissions it it's "permission set" and to remove them it's "permission unset".


The examples shown here are removing groups for ranks a and b.  For the rank A command you need to remove rank b permission.  For rank b you would remove rank C permissions.  More information is provided below about why you would want to remove the permissions for the next higher rank.


Template and examples as used in rank commands:

    /lp user <player-name> parent remove <group-name>

    /lp user <player-name> parent remove prison.mines.<mine-name>

    /lp user {player) parent remove prison.mines.b
    /lp user {player) parent remove prison.mines.c



The **wrong** way to add a player to a mine region.  Incorrectly by adding them as a direct member.

    /region addmember prison_mine_<mine-name> <player-name>

This will result is potentially hundreds, or thousands, of members being added directly to the mine's region.

    
    
The following is an example of adding and removing a permission to a player.  These are plain permissions and not a permission group. 

    /lp user <player-name> permission set prison.mines.<mine-name> true
    /lp user <player-name> permission unset prison.mines.<mine-name>




<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# Adding Rank Commands to run when /rankup is Performed

**Purpose:** Adds the permission to access the mine area and to mine within a mine, when a player successfully runs /rankup.


Based upon the above documentation, and from within game, we would use the following to *manually* give a player a permission:

    /lp user <player-name> parent add prison.mines.<mine-name>


For example, if you have a player named *AHappyPrisoner* And you have a mine named "a" you would use the following command:

    /lp user AHappyPrisoner parent add prison.mines.a


To run the **a** rank commands when the player uses **/rankup**, the following is the command for **/ranks command add <rankName>**:

	/ranks command add a lp user {player} parent add prison.mines.a


Notice how the manually entered command is used with the **/ranks command add <rankName>**?  Just drop the leading slash and it should be good.


If you want to be able to **demote** a player from rank "b" back down to rank "a" you would need add the following **/ranks command add** to the rank **a** which removes access to the **b** mine.

	/ranks command add a lp user {player} parent remove prison.mines.b


So to recap, for every rank, ideally you should add the new perms for that rank, and remove the perms for the next higher rank so as to enable the proper functioning of **/ranks demote**.

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">




# Adding the Prison Rank Commands - Summary of Rank Commands


This is an example of setting up Rank Commands for mines a and b, we now need to add the Rank Commands to active the permission for both.  Also included in these commands are the permissions for the mines.tp command, where mines.tp.<MineName> is a permission and not a group.


For rank a:

    /ranks command add a lp user {player} parent add prison.mines.a
    /ranks command add a lp user {player} parent remove prison.mines.b
    /ranks command add a lp user {player} permission set mines.tp.a true
    /ranks command add a lp user {player} permission unset mines.tp.b


For rank b:

    /ranks command add b lp user {player} parent add prison.mines.b
    /ranks command add b lp user {player} parent remove prison.mines.c
    /ranks command add b lp user {player} permission set mines.tp.b true
    /ranks command add b lp user {player} permission unset mines.tp.c


And that's it!  Just repeat for all your other mines.




<hr style="height:8px; border:none; color:#aaf; background-color:#aaf;">





# Alternatives

There are many ways to accomplish the same goals and that's what makes Minecraft so versatile and interesting to play.  The Prison Plugin does not want to impose a specific way to do most things, since it may not be the ideal way for your sever.

One of the primary focuses for this document has been protecting the area around your mine to prevent players who should not access the mine, from enter that region.  One alternative to needing to protect a mine, would be to limit the access to the mine so it does not have to be protected.  One simple way of accomplishing that, is to have the mines in a void world, and then each mine would be a separate island.  Then all that would need to be protected, or controlled, would be the warping to that location.


<hr style="height:8px; border:none; color:#aaf; background-color:#aaf;">


    


<hr style="height:8px; border:none; color:#aaf; background-color:#aaf;">




# Other Commands That May Be Important:


    /region redefine mine_<mine-name>
    
    
    /region removeowner prison_mine_<mine-name> <owner-name>
    /region removemember prison_mine_<mine-name> <player-name>
    
    

Set’s the WorldEdit selection to the dimensions of the given mine:

    /region select prison_mine_<mine-name>
    /region select prison_mine_area_<mine-name>


    /region info prison_mine_<mine-name>
    /region info prison_mine_area_<mine-name>

    
    /region list 
    /region list -w world
    /region list -p <player-name>
    
    
Some LuckPerm commands that may be useful.

    /lp group prison.mines.<mine-name> listmembers
    /lp user <user-name> group add <group-name>

    /lp user <user-name> info
    
    
    /lp listgroups
    /lp group prison.mines.a info
    
    
    /lp user <user-name> parent set <group-name>
    /lp user <user-name> parent add <group-name>
    /lp user <user-name> parent remove <group-name>
    
    /lp user <user-name> permission set <permission-name> true
    /lp user <user-name> permission unset <permission-name>
    
    
    /lp group <groupname> parent add <parentgroup>
    
    
    
    
# WorldGuard And LuckPerms Info

https://worldguard.enginehub.org/en/latest/regions/global-region/

https://worldguard.enginehub.org/en/latest/regions/flags/

https://worldedit.enginehub.org/en/latest/usage/regions/selections/

https://bukkit.org/threads/how-to-use-the-entry-group-flag-in-worldguard.124066/


https://github.com/lucko/LuckPerms/wiki/Command-Usage


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


