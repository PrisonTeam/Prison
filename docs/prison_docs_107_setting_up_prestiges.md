
### Prison Documentation 
[Prison Documents - Table of Contents](prison_docs_000_toc.md)

## Prison - Setting Up Prestiges

This document provides information how to setup and use prestiges.


*Documented updated: 2023-06-24*

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">

# Overview

This document should be able to provide the basic information to get the built in prestiges configured and working.


Prestiges are generally used to track how many times a player has gone through all of the default ranks.  There are many variations on how prestiges are implemented, but they area always a status symbol that sets the top players from the rest.  Some features may include resetting a player's monetary balance to zero, setting their rank back to the beginning, or some other change to make the next pass through the ranks slightly different, but usually more difficult.
  

Please use the command `/prestige help` within the console for more information about this command.

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


# Enabling Prestiges

By default, prestiges are now enabled. They may be disabled by changing the configuration setting within the
`config.yml` file.  Even if the prestiges option is disabled, Prison will generate a prestige ladder.  


Edit the following file:

```
plugins/Prison/config.yml
```


Prestiges are enabled with the settings `prestige.enabled: true`.

```
prestige:
  enabled: true
  resetMoney: true
  resetDefaultLadder: true
  confirmation-enabled: true
  prestige-confirm-gui: true
  force-sellall: false
  no-prestige-value: ""
  enable__ranks_rankup_prestiges__permission: false
```



Note: The following setting is obsolete, but still active for now.  If you have this setting in your 
existing `config.yml` file, then you can still use it.  But please consider updating to the newer 
settings.  You can "upgrade" by ranaming `config.yml` to something else, like `config_old.yml`, and 
then restart your server.  A new fresh copy of the `config.yml` file will be added when the 
server starts up.

Then set the following item to `true` and then save the file and restart the server.

```
prestiges: true
```


# Required Permission to use the `/prestige` Command

In order for players to use the `/prestige` command, they must have the follow permission:
`ranks.user`


This permission is not related to just `/prestige`, but enables the players to use the
`/rankup` command too.


This permission is a core perm for the rankup commands, and therefore the Prison command handler will
enforce this perm on all players trying to use `/prestige`.  If players do not have this perm, 
then attempts to use the command will just end silently.


To differentiate `/prestige` from `/rankup`, at least on controlling who can use this command, 
there is a prestige specific permission that needs to be set:
`ranks.rankup.prestiges`


This prestige permission can actually be disabled. See the section below titled "Prestiges: Enable the use of the perm `ranks.rankup.prestiges`" for more information.




<hr style="height:6px; border:none; color:#aaf; background-color:#aaf;">




# Other Prestige Settings


Within the `config.yml` file, there are a few other prestiges settings that can help customize how 
Prestiges works on your server.  Please see the config settings list above.


### Prestiges: Reset Money

`prestige.resetMoney: true`


When enabled, the player's balance will be reset to zero.


 
<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


### Prestiges: Reset Default Ladder

`prestige.resetDefaultLadder: true`


When enabled, the player's default rank will be reset to the default rank.
Generally that is Rank A as configured with the command `/ranks autoConfigure`.

Please use the following command in the console to check for the default rank:
`/ranks list`


 
<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">

### Prestiges: Confirmation Enabled

`prestige.confirmation-enabled: true`


When enabled, the player will be required to reenter the command a second time 
with the keyword `confirm` to provide confirmation that they want to prestige.
This is not a GUI option.

```
/prestige
/prestige confirm

```

Please see the command `/presetige help` to see more information about the command usage.
 
<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">

### Prestiges: Confirmation Enabled for the GUI

`prestige.prestige-confirm-gui: true`


When enabled, the player will need to confirm the prestige through a GUI confirmation 
dialog.

 
<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


### Prestiges: Force SellAll

`prestige.force-sellall: false`


When enabled, the player will be forced to go through a `/sellall` command process 
when the prestige is successful.  This is performed before the player's money is
reset if that option is enabled.

The purpose of this setting is to clear the player's inventory and backpack so they 
cannot sell all of it at the next higher rank which may provide far more money.
As an example, the if the highest default rank's mine has emerald blocks, and if they 
fill up their inventory, and backpack, then after prestiging, they sell everything
at rank A, they may earn enough money to skip over a few of the lower default ranks.

 
<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


### Prestiges: No Prestige Value

`prestige.no-prestige-value: ""`


This is a value that will be used within the placeholders for players that do not
have a prestige rank.  This will be applied to the rank's name and tag value.

Example of a placeholder that would use this setting would be
`prison_rank_laddername`.  So inserting "prestige' for the *laddername* would result in:
`prison_rank_prestige`.


 
<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


### Prestiges: Enable the use of the perm `ranks.rankup.prestiges`

`prestige.enable__ranks_rankup_prestiges__permission: false`


When this setting is enabled, then all players who may use the `/prestige` command
would have to have the permission `ranks.rankup.prestiges`.


When this setting is disabled, all players will be able to use the `/prestige` 
command without having to have a permission set.

 
<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



<hr style="height:6px; border:none; color:#aaf; background-color:#aaf;">




# The Prestige Ladder


The prestige ladder is created by default and is named `prestiges`, even if prestiges are not enabled.  Ranks can be added to the prestige ladder, even if it is not enabled.


In order to use prestiges, you need to create new ranks for each prestige level.  Add them as you would for the default ladder and the normal ranks, but you need to specify the prestige ladder name within the commands.


To show all the ladders that are available, use the following command.  This can help confirm that the ladder exists (it should be created as default) and it can confirm the correct spelling too.

```
/ranks ladder list
```

<img src="images/prison_docs_107_setting_up_prestiges_01.png" alt="List of ladders" title="Listing of ladders" width="500" />  


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


# Adding Prestige Ranks

In order to add prestige ranks, you add them just like you add ranks, but you specify the prestige ladder within the command.  


You can name them anything you want, but in our example below we are using p1, p2, etc, with the prestige tag values of `+` for the first prestige rank, then the following would be `+2`, `+3`, etc., wrapped in square brackets and colored.   


Here is an example of adding the first two prestige ranks.  These are the commands:

```
/ranks create p1 5000000 prestiges &d[&5+&d]
/ranks info p1
/ranks create p2 8500000 prestiges &d[&5+2&d]
/ranks info p2
```

This screen print shows the results of the above four commands.


<img src="images/prison_docs_107_setting_up_prestiges_03.png" alt="Creating the first two prestige ranks" title="Creating the first two prestige ranks" width="500" />  



<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# Setting up the Chat Placeholders or Prestige Ranks

You can add placeholders to the chat prefixes.  Please consult your chat documentation on how to do this, but here is an example for Esstentials-X Chat plugin.


Enabling the chat placeholder just requires editing one line within the `config.yml` file.  Search for the keyword **EssentialsChat** in that file, then edit the `format:` tag.  For example:

    format: '<{prison_rank_tag}:{DISPLAYNAME}>{MESSAGE}'


Once setup, restart the server. Or use **/essentials reload**.  Do not force all the plugins to be reloaded with a tool such as plugman since Prison (and other plugins) may fail to re-load properly.


The placeholder `prison_rank_tag` will include all placeholders that are active for the player, including the prestige chat prefixes.  The only issue is that you cannot control the order of how they will display.


It is possible to manually set the order by using the individual placeholders for each rank you want to show.  This allows you to omit certain placeholders too.  You can preview all of the placeholders with the placeholder search command.


```
/prison placeholder search <player> <page> rank_tag

/prison placeholder search RoyalCoffeeBeans rank_tag
/prison placeholder search RoyalCoffeeBeans 2 rank_tag
```

Please notice the first placeholder that is displayed in this screen print.  The `prison_rank_tag` shows prestige as second, and default is third.  It also shows a donor rank first.  This order is not controllable and may vary from server to server.  If you need another order, then you will have to manually set them up.


<img src="images/prison_docs_107_setting_up_prestiges_08.png" alt="Reviewing all placeholder tags" title="Reviewing all placeholder tags" width="650" />  


In our example, to customize the chat prefixes, we are interested in the following prefixes: `prison_rank_tag_pretiges` and `prison_rank_tag_default`.  


So to set them up in the EsstentialsX Chat prefix would be as follows:

```
    format: '<{prison_rank_tag_prestiges}{prison_rank_tag_default}:{DISPLAYNAME}>{MESSAGE}'
```


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# Prestiging

Prestiging is simple to do.  If the player is at the highest default rank, then they can rankup the prestige ladder.  Prestiging will reset the player's balance to zero. Currently there is no warning or confirmation so proceed carefully.

```
/prestige
```

The following screen print is from performing the following commands, where the player does a prestige, then sends a chat message to show the chat prefixes.

```
/ranks player <player>
/prestige

```

Note the following ranks on this server goes from E (the lowest) to A (second highest), then Free (highest).  The first command `/ranks player` shows the player is at Free rank.

<img src="images/prison_docs_107_setting_up_prestiges_04.png" alt="Prestige" title="Prestige" width="500" />  




Prestige is only available through a gui interface.  You have to hover over the blocks to see the lore, which provides additional information.


<img src="images/prison_docs_107_setting_up_prestiges_05.png" alt="Prestige" title="Prestige" width="500" />  


Showing the Confirm Prestige button.

<img src="images/prison_docs_107_setting_up_prestiges_06.png" alt="Prestige" title="Prestige" width="500" />  


Showing the cancel button.

<img src="images/prison_docs_107_setting_up_prestiges_07.png" alt="Prestige" title="Prestige" width="500" />  


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# The prestiges Command

This command shows the prestige ranks.  There are a few ways to get a listing of all of the prestige ranks.

```
/prestiges 
/prisonmanager prestiges

/ranks list prestiges
/ranks ladder listranks prestiges
```

This command is intended for players and can be enabled, or disabled, within the `plugins/Prison/GuiConfig.yml` file.  The command `/prestiges` is less configurable, but not sure why.



<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


