
### Prison Documentation 
[Prison Documents - Table of Contents](prison_docs_000_toc.md)

# Backpacks


Prison provides backpacks.  



<hr style="height:3px; border:none; color:#aaf; background-color:#aaf;">


# Enabling Backpacks

Backpacks are disabled by default.  To enable them, you must do so through Prison's config file:

`plugins/Prison/config.yml`

```
# NEW: Enable backpacks integrated within prison.
backpacks: true
```

You will have to restart the server to apply these settings. 



<hr style="height:3px; border:none; color:#aaf; background-color:#aaf;">


# The Configuration Files


The location of the configuration files for backpacks is as follows, along with the global configuration file:

`plugins/Prison/backpacks/backpacksconfig.yml`


The file other configuration file, `backPacksData.yml` is used to store player contents of their backpacks. 





<hr style="height:3px; border:none; color:#aaf; background-color:#aaf;">


# Settings


```
Options:
  BackPack_Use_Permission_Enabled: 'false'
  BackPack_Use_Permission: prison.backpack
  BackPack_Default_Size: '54'
  BackPack_AutoPickup_Usable: 'true'
  Back_Pack_GUI_Opener_Item: 'true'
  BackPack_Item: CHEST
  BackPack_Item_Title: '&3Backpack'
  BackPack_Item_OnJoin: 'true'
  BackPack_Lose_Items_On_Death: 'false'
  BackPack_Open_Sound_Enabled: 'true'
  BackPack_Open_Sound: BLOCK_CHEST_OPEN
  BackPack_Close_Sound_Enabled: 'true'
  BackPack_Close_Sound: BLOCK_CHEST_CLOSE
  Multiple-BackPacks-For-Player-Enabled: 'true'
  Multiple-BackPacks-For-Player: '4'
  DisabledWorlds:
  - exampleWorld
  - anotherExampleWorld

```

# Commands:

Give a look to this guidebook for some extra info about backpacks: 
[Backpacks Commands](docs-commands/prison_docs_command_43_backpack.md)

