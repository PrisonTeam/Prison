
### Prison Documentation 
## Configuring and Using WorldGuard to Protect Mines

[Prison Documents - Table of Contents](prison_docs_000_toc.md)

This document explains how to setup WorldGuard to protect your mines and how to prevent players from accessing it when they don't have the correct permissions.  It also explains how to setup the permissions in the Prison's **/ranks command add <rankName** so they are ran automatically during a **/rankup** and **/ranks promote** event. This document also covers what needs to be configured to ensure that the rank commands will work properly with **/ranks demote**.

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# Dependencies 

* Vault - Interfaces with the Permission Plugins
* EssentialsX
* [Install WorldGuard and WorldEdit](prison_docs_026_setting_up_worldguard_worldedit.md)
* Install a Permissions Plugin that is compatible with Vault

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# Setting up WorldGuard and WorldEdit:

Install both WorldGuard and WorldEdit as required for your version of the server and Minecraft. Follow the general directions in the link above.  If you require additional help, there should be plenty of good resources if you search for them.

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# In Game versus In Console

Please note that for WorldEdit and WorldGuard there is a slightly different way of entering commands **in game** versus **in console**.  This is very important to understand, because most of the commands may be entered in game, but when you add some of these commands to the **/ranks command add** then they will be executed as if they were being entered through the console.

When you are in game, the world you are in will be used as a default value in any command that requires a world parameter.  When you are entering commands from the console, you must specify the world parameter.  Failure to specify the world will prevent the command from running.  This will cause problems during the running of the **/rankup** commands.  

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


As op, protect the whole world with a passthrough flag set to deny. This will prevent building, PVP, and everything else.  Basically, any action that “passthrough” all over defined regions, will be denied.  


    /rg flag __global__ passthrough deny
    /region flag __global__ mob-spawning deny
    
    /gamerule doMobSpawning false

Note that the **/gamerule doMobSpawning false** may also help prevent mobs from spawning, and may help reduce the overhead of WorldGuard from having to deny the spawning events.  Kind of double protection.

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# Create a Mine Template as a Parent for all Mines

**Purpose:** The mine template will enable the players, who have access to the mines, to break the blocks within the mine, and to pickup items, XP, and allows item drops. 

Create a mine template, so all mines will inherit from this template, and thus pick up all of its flags and settings. The `-g` defines it as a global region without dimensions, so no dimensions need to be specified beforehand. Then set the *priority* to a value of 10 to take higher precedence of other lower regions that may overlap.


This template will define the behavior of all mines and what the members can do within the mines. This also adds in the owners as an owner of the regions.  But admins by default are added to the template so they have full access to all mines.


    /region define -g mine_template
    /region setpriority mine_template 10
    /region flag mine_template block-break allow
    
    /region flag mine_template item-pickup allow
    /region flag mine_template exp-drops allow
    /region flag mine_template item-drop allow
    /region addowner mine_template g:owner
    /region addmember mine_template g:admin


The following region setting for access and deny may *appear* to be useful, but don't use them.  Explanations follow.  **Do not use the following:**

    ~~/region flag mine_template entry -g nonmembers deny~~
    
    ~~/region flag mine_template x allow~~
    ~~/region flag mine_template entry-deny-message You must rank-up to access this mine.~~
    

**NOTE:** 

It’s a bad idea to deny access to the mines through these regions.  If the players doesn't have access to the mines, and they try to enter from the top, WorldGuard will continually prevent them from entering, or falling in to the mine, which will basically keep them floating in the air which will trigger a fly event within anti-hacking tools.  It will be far more professional to protect the area that contains the mine, thus you can protect it over the whole y-axis too. Players can actually get caught in a rapid loop where WorldGuard is trying to kick them out of the mine when restricting just the mine; could possibly cause a lot of lag, depending upon how many event’s are being triggered.

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# Defining Regions for Each Mine

Once you have the Mine Template defined, you won't have to repeat any of that for your mines.  Since your mines will inherit from the template, they will take on all of the characteristics of the template.  The result will be less to configure, less chances of getting something configured wrong, and better stability.


Select the same area of the mine with the WorldEdit **wand**, then use the following commands to define a mine.  It will define a region with the mine’s name, and set the parent to mine_template, with the only member ever being the permission **prison.mines.<mine-name>**:


    /region define mine_<mine-name>
    /region setparent mine_<mine-name> mine_template
    /region addmember mine_<mine-name> g:prison.mines.<mine-name>


You would need to repeat these settings for each mine.


Please note that whatever you choose to use for the region name or the permissions is up to you.  One thing to consider is the names you use for the prison regions.  You can use just the mine names, butf your mines are just named a, b, c, d, etc, then it may make more sense, and easier to prevent confusion if you named the regions **mine_<mine-name>**.  

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# Create a Mine Area Template for all Mine Areas

**Purpose:** To keep out all non-members from a mining area.  

In general, it may be tempting to restrict access to the mine itself so non-members cannot mine it.  But there is a serious problem with just protecting the mine, and that’s when non-members walk on top of the mine.  They will fall in to the mine, as expected, but WorldGuard will try to keep them out, so they will be bumped back above the mine, thus triggering a “fly” event, or a “hover” event.  This action may trigger anti-hacking software to auto kick them, or auto ban the players.

This also happens really fast, in a very repeated action, so it could lock up the player so they cannot jump back out before they get banned.  I do not know if this could contribute to server lag, but a lot of processing appears to be happening so it is possible.

The suggested action is to create a new region around the mine and protect that from entry by non-members.  This region can then be extended from y=0 to y=255. If anyone does get past it, they still won’t be able to mine.


First, we need to define a **mine_area_template** so we don't have to keep repeating the same setting for all mines.


    /region define -g mine_area_template
    /region setpriority mine_area_template 10
    /region flag mine_area_template entry -g nonmembers deny
    /region flag mine_area_template entry-deny-message You must rank-up to access this mine.
    
    /region addowner mine_area_template g:owner
    /region addmember mine_area_template g:admin

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# Defining Regions for Each Mine Area

Just like for the Mine Template, once you have the Mine Area Template defined, you won't have to repeat any of that for your mines.  Since your mines will inherit from the template, they will take on all of the characteristics of the template.  The result will be less to configure, less chances of getting something configured wrong, and better stability.


Select the an area around the mine with the WorldEdit **wand**.  Only select a rectangle area around the mine, ignoring the **y** axis.  Then use the following commands to define a mine.  It will define a region with the mine’s name, and set the parent to mine_template, with the only member ever being the permission **prison.mines.<mine-name>**:


    //expand vert
    /region define mine_area_<mine-name>
    /region setparent mine_area_<mine-name> mine_area_template
    /region addmember mine_area_<mine-name> g:prison.mines.<mine-name>


The command **//expand vert** will take your selection and extend the **y** to cover the whole vertical range in your region.  This is why you don't have to be concerned with the *y* axis when defining your mine area regions.


You would need to repeat these settings for each mine.


Notice we are using the same permission permission for both the mine and the mine area: **prison.mines.<mine-name>**.  This keeps it simple by reducing the number of permissions we have to give the players.


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# Granting Access to a Mine and Removal of the access

**Purpose:** From either the console, or from within game, manually grant a player access to a mine.

To add a player to the mine regions is as simple as giving the user the permission associated with the mine region.  

It's important to understand that you never add a player as a direct member of a mine region, since if you have thousands of players, it will make your configuration files messy, and could add to lag dealing with such large data files per region.

Instead, you grant players the permission to use the mine regions, and it's the players that have the list of mine permissions that they should access.  One way to look at this is that a permission is like a key, and you're giving players a copy of the key access the mines.  


The correct way to add a player to a mine region. Indirectly by giving them access to the "keys".

    /lp user <player-name> permission set prison.mines.<mine-name> true


The **wrong** way to add a player to a mine region.  Incorrectly by adding them as a direct member.

    /region addmember mine_<mine-name> <player-name>

This will result is potentially hundreds, or thousands, of members being added directly to the mine's region.


Likewise, it is important to know how to remove access from a player so they can be demoted or removed from an area that they should no longer access.


    /lp user <player-name> permission unset prison.mines.<mine-name>


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# Adding Rank Commands to run when /rankup is Performed

**Purpose:** Adds the permission to access the mine area and to mine within a mine, when a player successfully runs /rankup.


Based upon the above documentation, and from within game, we would use the following to *manually* give a player a permission:

    /lp user <player-name> permission set prison.mines.<mine-name> true


For example, if you have a player named *AHappyPrisoner* And you have a mine named "a" you would use the following command:

    /lp user AHappyPrisoner permission set prison.mines.a true


To run the **a** rank commands when the player uses **/rankup**, the following is the command for **/ranks command add <rankName>**:

	/ranks command add a lp user {player} permission set prison.mines.a true


Notice how the manually entered command is used with the **/ranks command add <rankName>**?  Just drop the leading slash and it should be good.


If you want to be able to **demote** a player from rank "b" back down to rank "a" you would need add the following **/ranks command add** to the rank **a** which removes access to the **b** mine.

	/ranks command add a lp user {player} permission unset prison.mines.b


So to recap, for every rank, ideally you should add the new perms for that rank, and remove the perms for the next higher rank so as to enable the proper functioning of **/ranks demote**.

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# Alternatives

There are many ways to accomplish the same goals and that's what makes Minecraft so versatile and interesting to play.  The Prison Plugin does not want to impose a specific way to do most things, since it may not be the ideal way for your sever.

One of the primary focuses for this document has been protecting the area around your mine to prevent players who should not access the mine, from enter that region.  One alternative to needing to protect a mine, would be to limit the access to the mine so it does not have to be protected.  One simple way of accomplishing that, is to have the mines in a void world, and then each mine would be a separate island.  Then all that would need to be protected, or controlled, would be the warping to that location.


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# Other Commands That May Be Important:

    /region redefine mine_<mine-name>
    /region removeowner mine_template <owner-name>
    /region removemember <mine-name> <player-name>

Set’s the WorldEdit selection to the dimensions of the given mine:

    /region select mine_<mine-name>
    /region select mine_area_<mine-name>

    /region info mine_<mine-name>
    /region info mine_area_<mine-name>
    
    /region list
    /region list -p <player-name>


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# WorldGuard And LuckPerms Info

https://worldguard.enginehub.org/en/latest/regions/global-region/

https://worldguard.enginehub.org/en/latest/regions/flags/

https://bukkit.org/threads/how-to-use-the-entry-group-flag-in-worldguard.124066/


https://github.com/lucko/LuckPerms/wiki/Command-Usage


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


