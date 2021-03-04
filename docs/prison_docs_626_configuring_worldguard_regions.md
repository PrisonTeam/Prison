
### Prison Documentation 
[Prison Documents - Table of Contents](prison_docs_000_toc.md)

## Configuring and Using WorldGuard with LuckPerms to Protect Mines

This document explains how to setup WorldGuard to protect your mines and how to prevent players from accessing it when they don't have the correct permissions.  It also explains how to setup the permissions in the Prison's **/ranks command add <rankName** so they are ran automatically during a **/rankup** and **/ranks promote** event. This document also covers what needs to be configured to ensure that the rank commands will work properly with **/ranks demote**.

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">

# Please READ This

This document is a work in progress.

Sorry for the state of this document. We found a flaw in the original document in which the protections did not work as expected.  The original documents were using region templates to help reduce the amount of typing you would have to do per mine and rank.  But it turns out that once that region template was tied to a permission, then it opened up all mines to be mined by any player that has access to any mine.  In other words, if someone gained access to A they could mine in Z if they could get there.  Therefore I'm in the process of redesigning the permissions to work properly (which they now do).  

But updating this document will take a little more time because I'm needing to rewrite everything a few times.

In the mean time, I've indicated some content that no longer applies with an `x` as the first character on the line.
 

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


# Dependencies 

* Vault - Interfaces with the Permission Plugins
* [Install WorldGuard and WorldEdit](prison_docs_026_setting_up_worldguard_worldedit.md)
* Install a Permissions Plugin that is compatible with Vault 
    * This guide uses LuckPerms.  See [Setting up LuckPerms](prison_docs_020_setting_up_luckperms.md) for more information.

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



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


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# Naming conventions to Consider


Please note that whatever you choose to use for the region names or the permissions is up to you.  It may be easier to understand what is what, if it has some kind of context such as a region named **mine_<mine-name>** when compared with **mine_area_<mine-name>**.  Same with permissions.  If they begin with **prison.mines.<mine-name>** you will easily understand it role versus a **prison.tp.<mine-name>**, or a permission for notifications, or even enchantments.

Putting thought in to the naming of resources, such as regions and permissions, can help make managing your server easier, especially if you have someone joining your staff well after your server has been released.


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# Enable Yourself - Op'ing and Wanding

To simplify things, make sure you are **op**.  You should also know how to **deop** yourself too.  You can **deop** yourself in game too, just prefix the command with a slash.

From console:

    op <yourName>
    deop <yourName>


Then **in game**, give yourself a WorldEdit wand:

    //wand

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# Protecting the World from Players

**Purpose:** This prevents players from breaking any blocks in the world. It also prevents mobs from spawning.


As op, protect the whole world with a passthrough flag set to deny. This will prevent building, PVP, and everything else.  Basically, any action that “passthrough” all over defined regions, will be denied.  The command with the **-w world** parameter has been added to the following list too.  Use that version from console, the other without **-w world** in game.  And where the name **world** is the actual name of your world.

In game commands:

    /rg flag __global__ passthrough deny
    /region flag __global__ mob-spawning deny
    /gamerule doMobSpawning false

Console commands:
   
    /rg flag -w <world> __global__ passthrough deny
    /region flag -w <world> __global__ mob-spawning deny
    /gamerule doMobSpawning false


Note that the **/gamerule doMobSpawning false** may also help prevent mobs from spawning, and may help reduce the overhead of WorldGuard from having to deny the spawning events.  Kind of double protection.

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# Various LuckPerm Commands for Templates and Mines

The WorldGuard region templates are covered below, but first you need to setup the groups within LuckPerm or the permissions on the child groups will not be tied back to the parent groups within LuckPerms.


For prison, we will use the prefix of `prison.mines` so we know what these groups and permissions are related to the mines.


LuckPerms commands to create a group, and how to have a child group inherit permissions from a parent.  The first command should be used twice, first for the parent, then the child group.  The the next command shows how to assign a parent group to a child group.

	/lp creategroup <group>
	/lp group <group> parent add <parent-group>



We **must** create a LuckPerms group for every mine.  Only mine `a` and `b` are shown here, but create one for each mine.
	
	/lp creategroup prison.mines.a
	/lp creategroup prison.mines.b
	
	

To check to see if these groups are setup properly, you can inspect them with the following commands.

    /lp listgroups
    /lp group prison.mines.a info


To grant permission to the players, you need to use the following since we need to add the player to the group.

    /lp user <user-name> parent set <group-name>
    /lp user <user-name> parent set prison.mines.a
    

And to now hook this up to prison, you do same command, dropping the leading slash, but with adding a rank command prefix and use the {player} placeholder (more on this later)


    /ranks command add a lp user {player} parent set prison.mines.a

    
<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">

   


# Unprotecting a Mine for its members - Required for all Mines

**Purpose:** This will actually give members the ability to perform mining related tasks within the mine.  They need to be able to break the blocks within the mine, and to pickup items, XP, and allows item drops. 


This defines a WorldGuard region, and needs to be applied to all mines.


Select the same area of the mine with the WorldEdit **wand**, then use the following commands to define a mine.  It will define a region with the mine’s name, and set the parent to mine_template, with the only member ever being the permission group **prison.mines.<mine-name>**.  Never add a player to a WorldGuard region since it will get messy.  Always use permission based groups and then add the player to that group.


In this example I have included an owner of this mine which is group owner.  And added the group admin as a member so the admins will have full access to this mine, even if they do not personally have the player's rank to access this mine. The actual members you add are up to you, but these are just two examples that you should consider.


    /region define prison_mine_<mine-name>
    /region addmember prison_mine_<mine-name> g:prison.mines.<mine-name>


    /region setpriority prison_mine_<mine-name> 10
    
    /region flag prison_mine_<mine-name> block-break allow
    /region flag prison_mine_<mine-name> item-pickup allow
    /region flag prison_mine_<mine-name> exp-drops allow
    /region flag prison_mine_<mine-name> item-drop allow
    
    /region addowner prison_mine_<mine-name> g:owner
    /region addmember prison_mine_<mine-name> g:admin
    

Set the *priority* to a value of 10 to take higher precedence of other lower regions that may overlap.


The following region setting for access and deny may *appear* to be useful, but don't use them.  Explanations follow.  **Do not use the following:**

    ~~/region flag prison_mine_<mine-name> entry -g nonmembers deny~~
    
    ~~/region flag prison_mine_<mine-name> x allow~~
    ~~/region flag prison_mine_<mine-name> entry-deny-message You must rank-up to access this mine.~~
    
**NOTE:** The use of `~~` above are invalid and are added since markdown documentation *usually* uses them as strike though, but that does not work with github markdown.  Nonetheless, i've kept them there just to add emphasis that it's wrong. 

**NOTE:** 

It’s a bad idea to deny access to the mines through these regions. Such as with **-g nonmembers deny** on the **prison_mine_<mine-name>** regions. If the players doesn't have access to the mines, and they try to enter from the top, WorldGuard will continually prevent them from entering, or more specifically it will prevent them from falling in to the mine.  This will basically keep them floating in the air which will trigger a fly event within anti-hacking tools.  It will be far more professional to protect the area that contains the mine, thus you can protect it over the whole y-axis too. Players can also get caught in a rapid loop where WorldGuard is trying to kick them out of the mine when restricting just the mine; could possibly cause a lot of lag, depending upon how many event’s are being triggered.


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">





# Protecting a Mine's Area - Required for all Mine Areas

**Purpose:** To keep out all non-members from a mining area.  The mining area, as in this context, is the area that immediately surrounds a mine, and generally non-members should not have access to it.
  

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


The correct way to add a player to a mine region. Indirectly by giving them access to the "keys". Or in other words, since we hooked up the LuckPerms group `g:prison.mines.<mine-name>` then all we need to do is add them to the group and they will have access to the proper regions.


Template:

    /lp user <player-name> parent set <group-name> true

    /lp user <player-name> parent set g:prison.mines.<mine-name> true


It is important to know how to remove access from a player so they can be demoted or removed from an area that they should no longer access.

Template:

    /lp user <player-name> parent unset <group-name>

    /lp user <player-name> parent unset g:prison.mines.<mine-name>



The **wrong** way to add a player to a mine region.  Incorrectly by adding them as a direct member.

    /region addmember prison_mine_<mine-name> <player-name>

This will result is potentially hundreds, or thousands, of members being added directly to the mine's region.

    
    
And also, the wrong way to add a player to a LuckPerms group. This won't work correctly.

    /lp user <player-name> permission set g:prison.mines.<mine-name> true
    /lp user <player-name> permission unset g:prison.mines.<mine-name>




<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# Adding Rank Commands to run when /rankup is Performed

**Purpose:** Adds the permission to access the mine area and to mine within a mine, when a player successfully runs /rankup.


Based upon the above documentation, and from within game, we would use the following to *manually* give a player a permission:

    /lp user <player-name> parent set g:prison.mines.<mine-name> true


For example, if you have a player named *AHappyPrisoner* And you have a mine named "a" you would use the following command:

    /lp user AHappyPrisoner parent set g:prison.mines.a true


To run the **a** rank commands when the player uses **/rankup**, the following is the command for **/ranks command add <rankName>**:

	/ranks command add a lp user {player} parent set g:prison.mines.a true


Notice how the manually entered command is used with the **/ranks command add <rankName>**?  Just drop the leading slash and it should be good.


If you want to be able to **demote** a player from rank "b" back down to rank "a" you would need add the following **/ranks command add** to the rank **a** which removes access to the **b** mine.

	/ranks command add a lp user {player} parent unset g:prison.mines.b


So to recap, for every rank, ideally you should add the new perms for that rank, and remove the perms for the next higher rank so as to enable the proper functioning of **/ranks demote**.

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# Alternatives

There are many ways to accomplish the same goals and that's what makes Minecraft so versatile and interesting to play.  The Prison Plugin does not want to impose a specific way to do most things, since it may not be the ideal way for your sever.

One of the primary focuses for this document has been protecting the area around your mine to prevent players who should not access the mine, from enter that region.  One alternative to needing to protect a mine, would be to limit the access to the mine so it does not have to be protected.  One simple way of accomplishing that, is to have the mines in a void world, and then each mine would be a separate island.  Then all that would need to be protected, or controlled, would be the warping to that location.


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


    


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


# WG LP Commands - Overview

These are entered in a step by step process, intended for you to follow.

These can, and probably will, be translated in to a runnable script within prison that will set everything up for you.  Well, once you take care of the basics. 

You should be in game when you run these commands, otherwise you may have to specify the world name with almost all LuckPerm commands.  When these are converted to scrips, the world parameter will be added.

Some code chunks will have **In Game:** which is intended to run from within minecraft.  The **Console:** is intended to be ran from the console, where there is no player, so you have to provide the "world".  Note that if your world is not named "world" then you will have to change that.  If there are any code chunks that are marked as "script" then those provide an example of what kind of placeholders would have to be used.


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



## WG LP Command - Global for whole world

Run once.


In game:

    /rg flag __global__ passthrough deny
    /region flag __global__ mob-spawning deny
    /gamerule doMobSpawning false
    

Console:

    /rg flag -w world __global__ passthrough deny
    /region flag -w world __global__ mob-spawning deny
    /gamerule doMobSpawning false



<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



## WG LP Command - Setting up LuckPerm Groups

Run once.  You must know what your mines and ranks will be.  Mines are just a simple letter like A through Z.  The ranks generally have the same name.


For the sake of this document we will assume they will range from A to Z, but we will only create permissions and regions for only one mine, a.  Be certain to duplicate this for all of the mines that you have.  There could be donor mines too, but for now let's ignore those.

For each mine, there will be 

	/lp creategroup prison.mines.a
	
	/lp creategroup prison.mines.b

	...
	
	/lp creategroup prison.mines.z
	
	

## LuckPerms Adding Permissions to the Groups

Run once for each mine/rank.

There will be other permissions that players will require in order to use your server.  You can add some of these permissions to the LuckPerm groups so when a player becomes a member of that group, then they will inherit the permissions that are in that group.

A good example of these permissions are of course rank based, such as access to the mine's warp, or even other permissions that everybody should have.

In this example we will give the group `prison.mines.a` the standard prison rank related permissions, but also other permissions that everyone should have.  

    
    /lp group prison.mines.a permission set prison.tp.a
    /lp group prison.mines.a permission set prison.gui
    /lp group prison.mines.a permission set prison.user
    

    /lp group prison.mines.a permission set warp
    /lp group prison.mines.a permission set warp.list
    /lp group prison.mines.a permission set warp.a

    
You can also add in a lot of EsentialX's permissions to fine tune what your players can do.  A nice listing of permissions can be found here: https://essinfo.xeya.me/permissions.html

Since rank A will always be a permission group all your players have, you can use this group as a container for those permissions.

Then all other ranks would only need what is required of the new ranks.  Such as:

    /lp group prison.mines.b permission set prison.tp.b
    /lp group prison.mines.b permission set warp.b

    
<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">

    
    
## Creating the Mine's WorldGuard Region


You can either use the worldEdit wand to select what you want to set as a region, or you can use other WorldEdit features to set them.


These are a WorldEdit method that can be used in game if you know the x, y, z coordinate.  This will not work with scripting because you cannot specify the world to apply it to.


    //pos1 x, y, z
    //pos2 x, y, z

    
Once you have a WorldEdit selection then you can create a WorldGuard region.

    /region define prison_mine_a
    /region setpriority prison_mine_a 10
    
    /region flag prison_mine_a block-break -g members allow
    
    /region flag prison_mine_a item-pickup -g members allow
    /region flag prison_mine_a exp-drops -g members allow
    /region flag prison_mine_a item-drop -g members allow
    
    /region addowner prison_mine_a g:owner
    /region addmember prison_mine_a g:admin
    
    /region addmember prison_mine_a g:prison.mines.a


Repeat the same for mine b.



<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


## Creating the Mine Area WorldGuard Region

The mine area is an area that surrounds the mine to protect the area from players who should not have access.  This area should be at least 5 blocks larger in the X and Z axis than the mine, so as to prevent non-member players from being able to attempt mining.

You need to select the area like the prior region and then define it with the following commands.  The following `//pos1` and `//pos2` is just an example of making the selection.  The `//expand vert` is required (strongly suggested) to ensure the region extends from the lowest to the highest blocks.  

    //pos1 x, y, z
    //pos2 x, y, z 
    
    
    //expand vert
    
    /region define prison_mines_area_a
    /region setpriority prison_mines_area_a 10
    /region flag prison_mines_area_a entry -g nonmembers deny
    /region flag prison_mines_area_a entry-deny-message You must rank-up to access this mine.
    
    /region addowner prison_mines_area_a g:owner
    /region addmember prison_mines_area_a g:admin
    
    /region addmember prison_mines_area_a g:prison.mines.a
    

Please notice that we have defined two WorldGuard regions: prison_mines_a and prison_mines_area_a.  But for both of them, we've assigned the LuckPerms group g:prison.mines.a as members.  This means, all we need to do is add the player to that LuckPerms group and they will have access to both the mine_area and also the mine.



<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">




# To get TokenEnchant's TEBlockExplosionEvent to work with prison, you must follow these settings:


Please note that these instructions apply to **Prison v3.2.4** and later only.


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




# Adding the Prison Rank Commands


So finally for our example of setting up mines a and b, we now need to add the Rank Commands to active the permission for both.

For rank a:

    /ranks command add a lp user {player} parent set g:prison.mines.a true
    /ranks command add a lp user {player} parent unset g:prison.mines.b


For rank b:

    /ranks command add b lp user {player} parent set g:prison.mines.b true
    /ranks command add b lp user {player} parent unset g:prison.mines.c


And that's it!  Just repeat for all your other mines.





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
    
    /lp listgroups
    /lp group prison.mines.a info
    
    /lp user <user-name> parent set <group-name>
    
    /lp group <groupname> parent add <parentgroup>
    
    
    
    
# WorldGuard And LuckPerms Info

https://worldguard.enginehub.org/en/latest/regions/global-region/

https://worldguard.enginehub.org/en/latest/regions/flags/

https://worldedit.enginehub.org/en/latest/usage/regions/selections/

https://bukkit.org/threads/how-to-use-the-entry-group-flag-in-worldguard.124066/


https://github.com/lucko/LuckPerms/wiki/Command-Usage


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


