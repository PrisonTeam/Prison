[Prison Documents - Table of Contents](docs/prison_docs_000_toc.md)

## Prison Build Logs for v3.2.x

## Build logs
 - **[v3.2.4-alpha - Current](changelog_v3.2.x.md)**
 - [v3.2.0 - 2019-12-03](docs/prison_changelog_v3.2.0.md)&nbsp;&nbsp;
[v3.2.1 - 2020-09-27](docs/prison_changelog_v3.2.1.md)&nbsp;&nbsp;
[v3.2.2 - 2020-11-21](docs/prison_changelog_v3.2.2.md)&nbsp;&nbsp;
[v3.2.3 - 2020-12-25](docs/prison_changelog_v3.2.3.md)
 

Greetings!  I'm delighted that you are interested in the build logs for the
Prison plugin.  I'm wanting to provide a more formal documentation as to what 
is going on in each build so you have a better idea if it may be something 
that you need.

# V.3.2.4-alpha.6 2021-01-15



* **V.3.2.4-alpha.6 2021-01-15**


* **Add ability to select to TP to either the spawn location for a mine, or the mine's center location even if the spawn point exists.**
This is useful if spawn gets disconnected from the mine and there is no easy way to find the mine.


* **Fixed a NoSuchMethodException on BlockEvents for TE Explosion events.**
Thought that Exception would have caught it.  Must be due to the fact that it is generally assumed that the NoSuchMethodException is mostly a compile time exception and not a runtime exception so they split it out as far as what it extends from?  


* **SellAll delay.**
 Added SellAll sell delay, it's now possible to add a delay to execute sellall sell, global
  permission for editing: `prison.sell.delay`.
  - New /sellall delay <true/false> command, you can now disable sellAll delay while in game.
  - New /sellall delay set <Delay_Seconds> command, you can now edit sellAll delay while in game.


* **Fix a mapping of terracotta to hardend clay only, and not hard clay.**
The two are different. This may "fix" this in the mappings for BlockType, but this still won't work well, since XMaterial is incorrectly mapped with terracotta.  I submitted a pull request to fix it but last check they did not pull it.


* **A few edits to the beginning of the spigotmc.org resource page document.**
Added a couple of screen prints for examples of a couple of commands.


* **Many updates and a lot of great work put in to the spigotmc.org's page design**
by Gabryca and graphics by Madog24 this week.  A lot of greate progress has been made.


* **ChatDisplay changed function text to addText and changed emptyLine to addEmptyLine**
to better align these commands with the other text based message handling functions.


* **Started to add Block names to BlockEvents so blocks can be targeted with the blockEvents.**
What makes this more dificult to deal with, is the fact that you can specify more than one block per BlockEvent.  This makes it more complex for the editor since I want to be more selection based so the end user does not have to type in hugely long commands.  
This is a work in progress and is not yet functional... it's enabled, but does not cause errors or adds the actual blocks as of yet.
For the blockEvents: changed the use of .command to .suggest since suggest will not run the command for the user, but will allow them to make the final edits on it before submitting.  Plus it acts as a "confirmation" to follow through on the command. 


* **New SellAll Admin GUI and Sub-GUIs**
Rework of the admin SellAll GUI, added AutoSell GUI, moved the old Admin GUI to a Blocks GUI.

* **New SellAll AutoSell perusertoggleable commands**
It's now possible to edit while in game the perUserToggleable AutoSell feature with a command:
  - `/sellall autosell perusertoggleable true` `prison.autosell.edit`.
  - `/sellall autosell perusertoggleable false` `prison.autosell.edit`.


* **Rework the BlockEvents to simplify the add command.**
Now to get some features you must make changes after with the editing with the other commands.
Added player task modes and made it an enum.  So a task can now be ran as the player instead of just as console.
Changed the names of the BlockEventTypes to simplify them, but set them up to translate to the new enum names.



* **Added new AutoSell enable and disable commands**
It's now possible to enable and disable autosell while in game for everyone (enable or disable the whole feature).
commands:
  - `/sellall autosell true` `prison.autosell.edit`
  - `/sellall autosell true` `prison.autosell.edit`



* **Added new placeholder: prison_player_sellall_multiplier**
with an alias of prison_psm.  This will return the value of the multiplier and if nothing is configured for it (no ranks, no sellall, no perms, ect) then it will return a value of 1.0.  This placeholder works with the nFormat placeholder attribute so the value can be formatted as desired.


* **Add the rank commands for managing rank permissions and rank permission groups.**
This is not functional other than adding these perms to the ranks.



* **Work on Ladder and Rank Perms.**
Got the Ladder perms hooked up to both the loading and saving, and also the command interfaces to add and remove them.
This is a work in progress and is non-functional.  The ladder perms working from the sense that you can add them, list them, and remove them.  But they currently do nothing.
The next step is to finish working on the rank perms commands to get them functional with adding and removing... they should already be functional with saving an loading.
But even at that point, this will not be functional.  The whole core of the perms and the perm integrations need to be rewritten to utilize the expanded features.


* **GUI code cleanup and optimizations.**
The GUI's improving and getting more stable, the code got some refactoring, this won't really change things for the end user but it'll for devs who are wanting
to edit GUIs, I hope to manage in the future to improve it even more.

* **V3.2.4-alpha.5 2021-01-02**


* **Add new feature to reset all mines with one command.**
This works for all mine types, including mines setup for paging.
This will build a list of reset commands for all mines, then it will submit each one to run.  When each mine finishes, then it will submit the next reset command to run until there are no more mines to reset.  It's using synchronous jobs to manage the resets so as not to dominate the processing and to yield to other tasks needing to be processed.
The mine resets can also be canceled.
The following are examples, with additional processing options of `details` will provide some information on the reset progress.  Details is optional.


When running a reset for all mines, it also automatically enables the noCommands option on all resets.  If a mine is setup with a mine command to reset another mine, then this could cause an endless loop.  Therefore no mine commands are ran when all are rest.

```
/mines reset help
/mines reset *all*
/mines reset *all* details
/mines reset *cancel*
```


* **When setting the area of a mine, it now refreshes the liners and shows the tracers.**


* **New feature: Force a reset but be able to not run any of the mine commands.**
This allows for chaining of mine resets to other mines.


* **V3.2.4-alpha.4 2021-01-01**


* **Start to setup the ranks perms listing: disabled.**  It will work, but I turned off the command since it is not ready yet.



* **Changes to the Gems Economy integration wrapper to support the new version of Gems Economy.**
This uses reflection to get around the problems which was introduced with v4.9.0 where the API and its methods remained the same, but one class that is used for the method variable had its package name changed.  Thus breaking support for gems economy v4.9.x when the project is compiled for v4.8.3 and earlier.  This reflection modifications should also work if the project is compiled with GE v4.9.x and the deplyment is using v4.8.3 or earlier.
Tested with being compiled with v4.8.3 and works well with v4.9.1 that is running on v1.16.4 (v1.16.x requires v4.9.1). Tested and works on v1.8.8 with v1.8.8 running either v4.9.1 or v.4.8.3.
It has not yet been tested with compiling prison with v4.9.1, but it should work even when running on 1.8.8 with v4.8.3.


* **Made the selection of ranks case insensitive.**
Many commands in rank commands required the proper case spelling of the rank name.  Changed it so it is now case insensitive so it will be easier for players to select ranks.


* **For a few rank commands: Clean up some formating with currency names.**
Collect all currencies used within the default ladder, then display the player's balance with each of those currencies.


* **Fixed a problem with the addition of the permission and permission groups.**
The loading of of these was failing since the the arrays were not being instantiated. 


* **Updated the RankLadder and RankPlayer so the class variables are not accessible from the outside of those classes.**


* **Update Rank to fix issues with Rank class variables** being directly accessible from outside the class.  Class variables should never be directly accessed by outside classes.


* **V3.2.4-alpha.3 2020-12-30**
Bump the version.


* **Add support for a new Placeholder Attribute called text.**
This is strictly for debug purposes and also to format hex colors if they are not working in the other plugins that are using prison's placeholders.
This actually works very well, and has been tested with v1.16.x.
Update the docs and a few comments on the other attributes.



* **Added an alternative hex formatting for the placeholder attributes.**
This is another way to try to get hex color codes to work with plugins that do not support them directly.  
With testing, I found that hex2 format actually worked very well with other plugins and resulted in the hex colors working well.


* **Placeholder Attributes: Changed how debug works so hex can be added too.**  Instead of using a String array, converted it over to use a list.  The value of hex and debug are extracted prior to extracting any other parameters since those two values are now non-positional and can appear in any parameter location.
Debug statement now includes the original raw string that is non-converted to the spigot color codes so you can see what the original raw codes were for debugging purposes.  The parameter hex now will convert the color codes before sending the resulting placeholder value back to the plugin that is requesting it.  This may allow the successful use of hex colors in plugins that do not yet support them since the hex codes would have already been correctly converted.


* **Full support for hex color codes.**
The use of #abcdef will be converted to the correct color codes.  This applies only to prison messages and will not provide any translation for placeholders that are sent back to the requesting plugins that are using them.  They would have to support hex colors on their own.
This is only a feature that works with minecraft v1.16.x and newer.  Older versions of minecraft and spigot may produce undesirable artifacts.


* **V3.2.4-alpha.2 2020-12-29**
Bump version.


* **Upgrade Cryptomorin's XSeries to v7.8.0 from v7.6.0.0.1.**


* **Upgrade TokenEnchant to v18.11.3 to support the new TokenExplosionEvent's getTrigger().**
This allows BlockEvents to now support filtering on TE's plugins that trigger the explosion event.
Added support for editing and displaying the triggered parameter.


* **Added a new unit type to the number format attribute: binary.**
Binary is based upon the power of two and uses 1024 as a divisor instead of the base 10's 1000.


* **Updates for the placeholder attribute bar to work with the debug mode.**


* **New features added to the PrisonSpigotAPI to locate the given mine a block was broken in**,
or where a player is standing.  If it is being used for block related usage, then it would be best to base it upon the block that was actually broken since the player could be standing outside of the mine while mining (such as on top or to the side).
This code utilizes an internal prison player cache to help reduce overhead in location the mines.  The last mine a player was in when they successfully mined a block from a mine will be use as the first check. If they are not in that mine, then the others will be searched, but odds are that if they are mining blocks, then they will be getting more than just one before going elsewhere.
This code is similar to what's being used within the auto features.


* **Enhancement that provides for a way to prevent the translation of color codes within a given text String.**
Ran in to an issue with for display purposes, had to show the raw codes that were used, but there was no way to do so since they were being translated.  So added support for regular expression style of quotes to skip over a section of the string when translating.  These quotes are \\Q and \\E.  Everything between them will be ignored when translating color codes.


* **Hook up support for placeholder attribute support for bar graph customizations.**
Appears to be working well.


* **Updates to the placeholder attributes for the number formatting... little changes to get it to work better.**
This appears to be working really well. 


* **New feature: Placeholder attributes. Dynamic placeholder customizations.**
Allows for dynamic customization of different placeholders.  
The first placeholder attribute that is supported is the number format: nFormat.  This allows for full customization to be defined within a placeholder itself, so each use of that placeholder could be configured differently.
For example this placeholder provides the number of blocks remaining in a mine: %prison_mr_temp5%'
This can be customized with the following examples: 
  %prison_mr_temp5::nFormat:0.000:1:kmg% 
  %prison_mr_temp5::nFormat:#,##0.##:0:kmg%
  %prison_mr_temp5::nFormat:'&4$&2'#,##0.00'&7':3:kmg:debug%
The last example shows that formatting codes could be enclosed within the placeholder too, but probably shouldn't be use this way, but it can. 
The placeholder attributes also suports the debug parameter so as to provide detailed information in the log for admins to help diagnose possible issues when testing different settings.


* **v3.2.4-alpha1 2020-12-26**
Setup the alpha release.


# V3.2.3 2020-12-25 
**Merry Christmas!!**
Release of next bug update.



# V3.2.2 Release - 2020-11-21


