
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