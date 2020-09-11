
### Prison Documentation 
[Prison Documents - Table of Contents](prison_docs_000_toc.md)

## Configuring EssentialsX Warps for Mines

*Note that this is a work in progress and does not include all steps yet.**

This document explains how to setup the EssentialsX Warps so players can warp when to the mines that they should have access to.

Please note that within Prison there is the **/mines tp <mineName>** command, but that is intended for use by admins only. There is no way to limit players to have access to specific
mines.  Anyone who is giver access to that command has access to tp to any mine.

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# Dependencies 

* EssentialsX - for the */warps* and */warp* commands.

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# Configuring EssentialsX 

Download and install EssentialsX.


In the `plugins/Essentials/config.yml` is this:

```
// Set this true to enable permission per warp.
per-warp-permission: false
```

Set that to `true`.


Then restart the server.

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# Permissions for Warps


* **essentials.warp** -	Allow access to the /warp command.
* **essentials.warp.list** - Specifies whether you can view warp list with /warp.
* **essentials.warps.[warpname]** - If you have per-warp-permission set to true in the config.yml then you can limit what warps players can use. This also controls what players would see with /warp.

In the EssentialsX config file, set:

```
per-warp-permission: true
```

Then use the following permissions. The first two are required, but the last three are examples based upon the three warp names: a, b, and c.  These three warp names happen to be the same as the mine's names, but they don't have to be.  There is no direct connection between warp names and mine names.  And there are no direct connections between warp permissions and mine permissions.
```
essentials.warp
essentials.warp.list
essentials.warps.a
essentials.warps.b
essentials.warps.c
```


# How to set the warps


First you need to set up each warp. Use the `/setwarp [mineName]` command to accomplish that.


If you want it to be exactly the same as your prison mine warps (ie... `/mines tp [mineName]`), first tp to that location and do not move or change where you are looking.  Next issue the EssentialsX's command `/setwarp [mineName]` to set the warp location.


Then add them to the rank commands. Using LuckPerms from within game will look something like this:

```
/lp user [playerName] permission set essentials.warps.a true`

```

And adding them to the ranks commands but instead of specifying the actual player name, use the `{player}` placeholder and Prison will substitute the player's name at runtime:

```
/ranks command add a lp user {player} permission set essentials.warps.a true
/ranks command add b lp user {player} permission set essentials.warps.b true
/ranks command add c lp user {player} permission set essentials.warps.c true
```

Note that with LuckPerms, the add user permission has an optional Boolean value of true at the end.  If omitted, it defaults to true, but it is included here since most documentation includes it.


# Using PEX permissions with Warps

If you're using PEX, the rank commands are similar, but using the pex notation.

From within game the pex command will look something like this:

```
/pex user [playerName] add essentials.warps.a
```

And adding them to the ranks commands:

```
/ranks command add a pex user {player} add essentials.warps.a
/ranks command add b pex user {player} add essentials.warps.b
/ranks command add c pex user {player} add essentials.warps.c
```



# Links

[http://ess.khhq.net/wiki/Command_Reference](http://ess.khhq.net/wiki/Command_Reference)

[http://wiki.mc-ess.net/doc/](http://wiki.mc-ess.net/doc/)

[https://luckperms.net/wiki/Permission-Commands](https://luckperms.net/wiki/Permission-Commands)


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">

