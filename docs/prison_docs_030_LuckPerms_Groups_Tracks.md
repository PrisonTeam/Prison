
### Prison Documentation 
[Prison Documents - Table of Contents](prison_docs_000_toc.md)

## Setting up LuckPerms' Groups and Tracks

This document provides an overview to help setup LuckPerms groups and tracks.


*Documented updated: 2021-12-19*

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# Setting Up LuckPerms


Setup LuckPerms as needed.  Information on where to download it can be found here:

[Setting Up LuckPerms](prison_docs_020_setting_up_luckperms.md)


Other plugin...

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


# LuckPerms Groups

To help simplify the management of permissions for players, LuckPerms uses permission Groups to form a collection of permissions.  This helps to simplify assigning permission to players since one group can contain many permissions.  For example, if there are 20 permissions in a group, then you would only have to issue one group command instead of 20 permission commands.


# LuckPerms Tracks

A brief overview of Tracks: Tracks are to LuckPerms, as ranks and ladders are to Prison.  They provide an easy way to manage player's perms by associating them with these ranks.  Tracks are also linked together, just like Prison ladders. A player is then moved along these Tracks to provide a way to change their permissions to match the evolving access and roles. 


To advance a player within LuckPerms Tracks, you just promote the player to the next higher Track.  This is very similar to how Prison ranks and ladders are used.


Since Prison needs to use their own ranks, and is not directly tied to LuckPerms Tracks, Prison Ranks can help manage a player's LuckPerms Track.

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


# Using Prison Rank Commands with Groups and Tracks

Since LuckPerm Groups and Tracks must be synchronized with Prison Ranks, the Prison Rank Commands are used to provide this linkage through Prison Rank Commands.  This allows the player to control when they rankup within Prison, which then synchronizes the LuckPerms' Track.


If you are not using LuckPerms Tracks, the following is the command you would use to setup the prison rank command.


```
/ranks command add B lp user {player} parent set [group]
```


As a quick review, to get more help about the prison rank commands.  The `help` key word displays detailed help about the selected Prison command.  And the `placeholders` keyword is important for prison commands since it lists all of the available placeholders that can be used to provide prison related data and values to be used within the commands.

```
/ranks command add help
/ranks command add placeholders
```


The following is how you would setup a Prison rank command to use tracks.

```
/ranks command add B lp user {player} parent settrack [track] [group]
```

Setting up these rank commands, will cause Prison to run these commands when the player ranks up.


So in review, this are two examples of rankup commands for a player who joins the server for the first time, are demoted back to A, or the prestiged and are reset to rank A. The Prison rank is named `A` and so is the LuckPerms group.  The first command is for setups with no tracks, and the second is for tracks; insert the track's name for `[track]`.  If you name your track `PrisonRanks`, with the Track's Groups named the same as the Prison Ranks.


```
/ranks command add B lp user {player} parent set A

/ranks command add B lp user {player} parent settrack PrisonRanks A
```


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


# More Examples of Usage


If you change a player's rank from E to A, the Prison rank commands for A will run, and set the player to group A, or default.

When you prestige, your rank goes from Z to A, so rank's A commands will be ran.
Plus the rank on your first Prestige rank (typically P1) will **ALSO** be ran.  This is highly useful since if you want certain prestige levels to have their own certain benefits/perms, you can setup a separate track and groups for Prestige ranks.

For example, where `P1` is the Prestige 1 rank:

```
/ranks command add P1 lp user {player} parent settrack [track] P1
```




<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


# The Suggested Way to Setup LuckPerms Groups and Tracks

Although there are many ways to configure any plugin, these suggestions tries to keep things simple through using the same names as the counter parts between Prison and LuckPerms.  It may be slightly confusion which is which, as far as Prison or LuckPerms, but it makes it easier by being consistent with the same names for the similar parts.


The best way to setup LuckPerms Groups is to make 1 group for every Prison Rank.  Then use the Rank Commands to assign users to that group automatically when they rankup.  Then to tie things together within LuckPerms, place these created LuckPerms Groups on a LuckPerms Track.


If you have not done so already, the best option to get started with Prison, is to run `/ranks autoConfigure`.  That provides so much of the basic configurations and can save you hours just trying to get the basics setup.  You can always change the default settings later to fine tune your setup.


The command `/ranks autoConfigure` will create Ranks and Mines with names from A through Z.  It will also create the first 10 Prestige ranks with the names P01 through P10.


So to get started with how you would setup a Prison Rank command for Rank B:
`/ranks command add B lp user {player} parent settrack [track] b`


When they become rank B, the command `/lp user {player} parent settrack [track] b` will be ran as console/admin.  The Prison placeholder `{player}` is the player's name, and `[track]` is the name you're calling your track.


To setup all the LuckPerms groups, you can use `/lp editor` to make it easier.  And setup the ranks from A through Z.



Next you should setup the LuckPerms Tracks, which will take a little more effort up front.  You don't have to use Tracks, but it will simplify some things later on by simplifying the synchronization between Prison Ranks and the LuckPerms Groups.  


For our examples, we will name our track **PrisonRank**.

```
/luckperms createtrack [track]

/luckperms createtrack PrisonRanks

```


Now we need to add all of our created groups to this track.  It's important to add them **IN ORDER**.  Yes, the order is critical.  Remember our Prison Ranks are named A through Z, and so are our LuckPerms Groups, so we have to add them all.


```
/luckperms track [track] append <group>

/luckperms track PrisonRank append A
/luckperms track PrisonRank append B
/luckperms track PrisonRank append C
/luckperms track PrisonRank append D

...

/luckperms track PrisonRank append Z

```


The next step that you need to do, is to setup all the perms for each of your groups.  The perms that you need, are based upon the plugins that you have setup on your server, and the perms that they will require.


For brevity, we will only show you the commands to use manually.  You can use the command `/lp editor` too.  Remember that the **group** listed in this command are the LuckPerms groups that you created earlier, that are named A trough Z.  The **permission**s that you use in these commands should be entered exactly as they are needed.

```
/lp group <group> permission set <permission>

```

It may take a while to setup all the permissions, and you will have to revisit these as you're adding more plugins.


Next, we need to setup Prison's Rank Commands to allow the rankup process to keep the LuckPerms Track's Groups in synchronization.  Luckily, this is pretty simple and is accomplished with just one Rank Command per rank.  Please not that for Prison's Rank names they are not case sensitive, but the LuckPerms track names may be, so they are listed here in upper case.


These are based upon the two individual commands; the Prison Ranks Command and the LP command.  The **command** reference in the Prison Rank Command is the whole LP command, as you will see following the templates.

```
/ranks command add <rank> <command>
/lp user {player} parent settrack [track] <group>

/ranks command add a lp user {player} parent settrack PrisonRank A
/ranks command add b lp user {player} parent settrack PrisonRank B
/ranks command add c lp user {player} parent settrack PrisonRank C
/ranks command add d lp user {player} parent settrack PrisonRank D

...

/ranks command add z lp user {player} parent settrack PrisonRank Z
```


The following is optional, but what it does is to apply all rank commands to all players at their current ranks.  So this will apply everything we just entered above to anyone who is already setup in Prison.

`/ranks set rank *all* *same* default`


And that should be it.  




<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# Setting up LuckPerms Chat Prefixes

*coming soon...*


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">