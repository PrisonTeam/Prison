### Prison Documentation 
[Prison Documents - Table of Contents](prison_docs_000_toc.md)

## Prison - SellAll info

This document provides info about how to use the SellAll feature and set it up

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">

# Overview

SellAll's a feature with the goal of sell and clear the player inventories from ores or blocks for money.
Essentially, while mining you'll fill your inventory and once full you need to sell what you've mined
to get money and rankup or buy stuff, SellAll will do all the job and give you the money. 

There's also a Multiplier feature which will modify the sellall values of all blocks depending on your prestige but before 
using it you should enable ```prestiges: true``` in your ```config.yml```

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">

# Getting started

First of all, if you want to use this feature, you'll need to enable it in your ```config.yml``` by editing ```sellall: false``` to ```sellall: true``` (by default's on false) and restart the server.
Also if you want give a look to the ```sellallconfig.yml``` and customize it.

*THE CONFIG SHOULD LOOKS LIKE THIS:*
_It might even be slightly different but it doesn't matter_

```
Options:
  GUI_Enabled: 'true'
  GUI_Permission_Enabled: 'true'
  GUI_Permission: prison.admin
  Sell_Permission_Enabled: 'false'
  Sell_Permission: prison.admin
  Add_Permission_Enabled: 'true'
  Add_Permission: prison.admin
  Delete_Permission_Enabled: 'true'
  Delete_Permission: prison.admin
  Player_GUI_Enabled: 'true'
  Player_GUI_Permission_Enabled: 'false'
  Player_GUI_Permission: prison.sellall.playergui
  Multiplier_Enabled: 'true'
  Multiplier_Default: '1'
  Multiplier_Command_Permission_Enabled: 'true'
  Multiplier_Command_Permission: prison.admin
```

### Then, you can do two things:
* If you're lazy or just don't want to do a ton of math like me, there's a default setup command which will add all the blocks
you should need for a Prison server with preset values, the command's ```/sellall setdefault```.
Then you could also edit these values if you don't like them with the command ```/sellall edit <ITEM_ID> <VALUE>```, for example ```/sellall edit COAL_ORE 75``` ; or 
remove blocks with the command ```/sellall delete <ITEM_ID>```.

* You can edit values or remove blocks even from the GUI ```/sellall gui```, there's a _similar GUI_ with the _same command_ for 
players **without** special permissions with just the blocks and their values as an info.

**OR**

* Add all the blocks by yourself with your values with the command ```/sellall add <ITEM_ID> <VALUE>```, for example ```/sellall add COAL_ORE 75```.


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


# ADVANCED FEATURES

## Multipliers:

To use this feature, you'll need to enable prestiges in your ```config.yml``` by editing ```prestiges: false``` to ```prestiges: true```, add some Prestiges and enable
multipliers in your sellallconfig.yml by editing ```Multiplier_Enabled: false``` to ```Multiplier_Enabled: true```.

The command ```/sellall multiplier``` will show all the sub-commands of this feature, right now there're only 2 commands:
```
/sellall multiplier add <Prestige> <Multiplier>
/sellall multiplier delete <Prestige>
```


The first one will add a Multiplier to a Prestige and the second will remove it.

How Multipliers work -> they'll just multiply the value of what you sold, for example: Your items worth ```$1000``` and you have a multiplier of ```1```,
so ```1000 * 1 = 1000``` is what you'll get (by default this's the value in the sellallconfig.yml), but if you've a Prestige with a multiplier of maybe ```1.5```, then
you'll get ```1000 * 1.5 = 1500```, this isn't even hard math but could make you some confusion. 

## Permission Multipliers:

It's possible to give a **permission** to a player with an **extra multiplier**, this if **enabled** will sum with
the **default multiplier** and **Prestiges multipliers**, permissions multipliers are **stackable** so this means it's possible
to **add more permissions multipliers to a player**, if you don't want this you can `enable` an option
to only choose and use the **higher one**.

Multiplier Permission format: `prison.sellall.multiplier.<multiplier>`
Example: `prison.sellall.multiplier.1` (this will add a 1x multiplier to a 1x default, so you'll get perm-mult + default-mult = 1 + 1 = 2x total multiplier).

Permission multipliers available options (at time of writing):
```
Multiplier_Permission_Only_Higher: 'false'
Multiplier_Command_Permission_Enabled: 'true'
Multiplier_Command_Permission: prison.admin
```

## SellaAll Signs:

Open your config.yml and turn on true the sellall sign like this: 
```
# NEW: SellAll sign
# New sellall feature which enable a sign with the name of [SellAll] to execute the command /sellall sell of Prison
# To make a sign, give yourself the permission prison.admin and then add as the first line of a sign the tag [SellAll]
sellall-sign: true
sellall-sign-notify: true
sellall-sign-visible-tag: "&7[&3SellAll&7]"
```
You can also turn on or off a notification when clicking the sign, you can edit that in the module-conf/lang/en_US.yml file, and you can edit
the tag from the config.yml which's shown in the sign.

To make a Sign, just place a sign and add as the first line ```[SellAll]```, also be sure to have the permissions ```prison.sign```.
If everything's right, the sign will look like the sellall-sign-visible-tag from the config.yml, and will work on right clicking it.

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">

# SellAll Auto:

You can turn on or off from the SellAllConfig.yml file the SellAll Auto feature, which will sell everything sellable from the player inventory when it's full.
Just edit these config lines like this:
```
  Full_Inv_AutoSell: 'true'
  Full_Inv_AutoSell_Notification: 'false'
```
You can edit the autoSell notification as you want.

## SellAll Auto Per-User-Toggleable

It's also possible to enable or disable sellAll per-User, it'll be them to choose if enable it or not.
They'll need to use the command: `/sellall auto toggle`, it'll let them enable or disable it, when used
for the first time, the user will be stored in the `users` section of the `sellAllConfig.yml`.

The admin can enable this feature from the `sellAllConfig.yml`, by changing this option from `false` to `true`:
```
    Options.Full_Inv_AutoSell_perUserToggleable: false
```

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">

# SellAll Earnings After Delay (custom /sellall sell command):

## What's the command:

The command is: `/sellall delaysell`.

## What is it:
It's a command enabled by default that can let you show, only after a delay, how much money
did the player earn during that delay.
You can disable it from the sellallconfig.yml:
`Full_Inv_AutoSell_EarnedMoneyNotificationDelay_Enabled: true`

For example, you can add it as a blockbreakevent command in a Mine, and the first time the player
breaks a block, a cooldown will start of the number of seconds (by default 10 but it's editable from the sellallconfig.yml, the options's: 
`Full_Inv_AutoSell_EarnedMoneyNotificationDelay_Delay_Seconds: 10`) that you set in the sellallconfig.yml, if you run this
command or /sellall sell while the delay isn't over, it won't tell you how much money did you earn with that /sellall sell,
but at the end of the delay it will tell you the total amount earned during this time.

So, a pratical example:

- A player breaks a block in a mine with a blockbreakevent command: /sellall delaysell.
- The cooldown automatically starts of 10 seconds and the player sellable items get sold, he earned now 100$.
- At 5 seconds, the player breaks another block and another /sellall delaysell command get triggered, the delay isn't over
so there're 5 more seconds to go, he earns 10$.
- At 7 seconds, the player uses /sellall sell because (for example) he dropped a bomb and got instantly full his inventory
so had to clean/sell it and earns 1000$.
- At 10 seconds, a message will show telling "You earned with AutoSell: 1110$".
- End.

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


# LIST OF COMMANDS
```
/sellall
Will show a list of commands to admin and sell items to those who aren't admins or players with special permissions.

/sellall sell
Will sell the items of your inventory.

/sellall add <ITEM_ID> <VALUE>
Add an ITEM/BLOCK to the sellall config.

/sellall delete <ITEM_ID>
Delete an ITEM/BLOCK from the sellall config.

/sellall edit <ITEM_ID> <VALUE>
Edit an ITEM/BLOCK of the sellall config.

/sellall GUI
Open an ADMIN GUI to admins where you can edit blocks values or remove them or open a PLAYERS GUI to player where they can see values of blocks.

/sellall multiplier
Will show a list of multiplier sub-commands.

/sellall multiplier add <Prestige> <Multiplier>
Will setup a multiplier to that prestige.

/sellall multiplier delete <Prestige>
Will remove a multiplier from the prestige.
```

And this's all, I hope this will help you all and also hope I didn't miss something, feel free to report your issues in our discord server or here on github!
