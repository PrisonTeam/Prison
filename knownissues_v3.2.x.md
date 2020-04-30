[Prison Documents - Table of Contents](docs/prison_docs_000_toc.md)

# Prison Known Issues and To Do's for v3.2.x

This document is intended to keep track of known issues and also provide for
a short list of To Do's. This list is intended to help work through known
issues, and/or to serve as items that should be added, or fixed.


# To Do Items

Work to be considered.


<h2> Higher Priority TO DO Items </h2>

* **Complete the new Mines Reset Paging**
  Holding up release v3.2.1.


* **Delay Mine Resets based upon blocks remaining**
  Holding up release v3.2.1.

* **Add parameter to charge player for Promote command**


* **Add /ranks remove currency [rankName] [currency]**
Done. Currently no way to remove a currency from a Rank to return it to normal currency.


* **Add onBlockBreak monitor to prison mines to count blocks mined**
Set EventPriority to monitor. Don't change anything. Just confirm block was changed to air and increment the count.
Do not have to precheck if the block was air before, since air cannot trigger a block break event.  Just confirm it is air when monitoring to ensure it was removed. If this works, then this would allow the elimination of the air counts.


* **Add permissions to the AutoManger**
Add permission checking to AutoManager to allow a per-mine selection of which mines to enable it in or to tie it to some rank or donor rank.  Could also put lore checking in place so tools could be enchanted to perform these functions too.  Could have it so there is a percent chance related to the permission or lore.
 

* **Add placeholder aliases so they are not as long**
Done.

* **Eliminate the internal Placeholders**
Done.  Performance improvement.

* **Integrate GUI in to bleeding**
Done.  More GUI features will be added in the next release.

* **Setup GUI to use /prison gui**
Done.

* **Get started on new Multi-Language Support**
This is put on hold for the v3.2.2 release.


* **Exclude specific Prison commands**

Ability to exclude, or ignore, specific commands upon startup.  

NOTE: this may not be needed. Disabling the Prison Ranks module solved the problem, which was trying to use EZRanksPro and prison's /rankup command was conflicting with that plugin's /rankup command.



* **Upon startup validate all Blocks that are defined in the mines**

Upon loading prison, validate that all blocks that are defined within each mine are actually valid for that version of minecraft.  This will be important in that it may help eliminate possible errors when the server owner upgrades the server, or other plugins.  Also it will be very helpful when Prison's block handling is enhanced since it will be a tool used to verify and maybe even fix incorrect block types.


* **Skip Mine Resets - Based upon usage and percent remaining**
* If a mine is not being used, this can greatly reduce the processing needs within the server. If there are about 30 mines, and no players are online for hours, then it can greatly reduce the server loads and reduce the number of chunk updates.
* Placeholder added percent mined so data exists to use for this calculation.
* At reset time, if enabled, check to see how many blocks were mined.
* If more than threshold percentage, such as 10%, then reset the mine.
* Even if one block is mined, and it is below the threshold, may want to reset the mine after X number of *skipped* resets. 
* Do not count as skipped if zero blocks are mined.
* All mines will reset after server reload, after the timer expires the first time, since in-mine block data and stats are not saved to the file system. 


* Fields to add to the Mine data:
    * skipReset - true = enabled, false = disabled
    * skipResetPercent - double - threshold to reset based upon blocks mined. Does not include original air-blocks.
    * skipResetBypassThreshold - int - number of *skips* before a forced reset.
    * skipResetBypassCount - transient - int - counts the number of times a reset is skipped. This is transient value and is never saved.


* **Support QuickSell project for use with Prison *Only* **

*Goal:* Something to consider. See if it can work with 1.15.x. This would provide a solution for prison servers to use with the full range of our supported platform versions.  Intentions of pushing changes back in to the main project and not maintaining a new project.

QuickSell has be abandoned, but could be very useful for prison to provide a simplified integration of features. 

Quickly reviewed code and it looks fairly good and probably has very low maintenance. Base initial support could be updating dependencies within Maven. Goal to get QuickSell to work with all supported versions of Prison and all supported versions of spigot.

Explicit support going forward would be directly related to Prison. If a support issue has to do with another 3rd party plugin, then support "could" be refused or unsupported 3rd party plugins could be removed. Primary focus would be for the support of Prison and to provide a QuickSell feature to users of the Prison plugin.

https://www.spigotmc.org/resources/quicksell.6107/

https://github.com/TheBusyBiscuit/QuickSell
Currently 15 forks.  Activity unknown.




<h2>To consider - Lower priority</h2>



* **Add to the Command annotations an option of *async* to run that command asynchronously**
Check to see if the commands are being ran sync or async.  Add a parameter support so 
commands that can be ran async.

* **Add prison Placeholders to papi's website for downloads**
Prison is already using papi (PlaceholderAPI).  But see if we can add prison to the supported
plugis for papi's cloud.  Should time this with the v3.2.1 release so there are more 
placeholder to use.

DeadlyKill: This what he needs ita
Papi
Hook Plugin
They have those expansions which hook other plug-ins

https://github.com/help-chat/PlaceholderAPI/wiki/Placeholders

* **Add support for player use of /mines tp**
Could be done through other perms and then checking to see if they have access to
that mine.  Perm:  prison.playertp and prison.playertp.a

* **Document how to use essentials warps for each mine**

In the plugins/Essentials/config.yml is this:

// Set this true to enable permission per warp.
per-warp-permission: false

Set that to true.
 	
essentials.warp 	Allow access to the /warp command.
essentials.warp.list 	Specifies whether you can view warp list with /warp.
essentials.warps.[warpname] 	If you have per-warp-permission set to true in the config.yml then you can limit what warps players can use. This also controls what players would see with /warp.

set:
per-warp-permission: true

essentials.warp
essentials.warp.list
essentials.warps.a
essentials.warps.b
essentials.warps.c

Then add them to the rank commands using pex:

/ranks command add a pex user {player} add essentials.warps.a
/ranks command add b pex user {player} add essentials.warps.b
/ranks command add c pex user {player} add essentials.warps.c


http://ess.khhq.net/wiki/Command_Reference
http://wiki.mc-ess.net/doc/


* **Improve some of the display pages for ranks and ladders**
Can add more information to the listings so they have more value.

* **Tab Completion**
Hook up tab completion on the prison commands.

* **Better logging of major events**
Need to log major events such as rankups, both to the server log, and also
to the community.  Server logs for these events, especially when money is
involved, is important.

* **Block Types for Specific Versions of Minecraft**
Add in support for the loss of magic values, and also provide for newer block
types too.  Basically have a minecraft version selector that can 
tailor the list of available block types that can be used, based upon the
minecraft version that is running.

* **New Feature: Upon block break events, log block changes**
This will allow dynamic live tracking of mine stats, such as how many blocks
remain and relating percentages.  The new async processing will enable this
to actually track individual blocks.

* **Possible new feature: Track how many blocks a player mines, including types**
Stats could be interesting over time, and could also be used for in game
bonuses and rewards and incentives.

* **Redesign the save files to eliminate the magic numbers**
Most of the save files within prison, for players, ranks, and ladders, are
using magic numbers which is highly prone to errors and is considered 
very dangerous.  Also prison would benefit from a redesign in file layout
to improve efficiencies in loading and saving, not to mention greatly reduce
the complexities within the actual prison code, which in turn will help 
eliminate possible bugs too and give tighter code.


* **Improve the prestige laddering system**
A plugin named EZprestige has been attempted to be used with prison. Not sure if successful?


* **Notification that inventory is full**

* **Built in selling system**

* **Custom Mine reset messsages per mine**


* **Enhancement: Multi-Language Support**

Offers for translation:
  Italian : Gabryca
  Greek : NerdTastic
  German: DeadlyKill ?? Did not ask, but a possibility?
  French: LeBonnetRouge
  

* **Auto-Config of other Prison Releated Plugins**

GABRYCA: [Idea]

Not sure if it's possible, but an hook with worldguard on mine making, like a region where the mine region's made automatically with basic permissions'd be neat

RoyalBlueRanger:

Gabryca... yes. I think a "clean" integration in to WorldGuard regions and WorldEdit selection tools would be a great improvement.  I've been wanting to do this from day one actually.  But I'm not 100% sure how much "automation" can be done here.

I guess if the first thing you do when you activate prison, is to "confirm" that you want prison that you want to "link" prison to WorldGuard and your Permission plugin (assuming supported ones exist), then auto generate all of the "step" that are outlined in the document that I create on github for WorldGuard.  That would really help everyone setup their basic servers.

A lot of work there.... but it would be VERY beneficial for sure.  It could be the foundation of an automated setup where prison "proactively" works with other plugins to help build and configure the server.

Areas of possibilities in "Auto-Configure Prison Environment":
  * WorldGuard - Regions for mines 
  * WorldEdit - Selection tools
  * LuckPerms - Perms
  * EssentialsX - Warps tied to mine warps
  * QuickSell - Place a [Sell All} sign at the mine warp
  * Citizens - NPC generated to replace the QuckSell Sign
  * CitizensCMD - Links the Citizen NPC to the QuickSell "tiered" pricing (requires Citizens)

I think those few integrations could really provide a huge bootstrap to getting the basics of a prison server up and running.


# Features recently added:

* **DONE: Mine Placeholders**
Added a number of placeholders for mines.

* **DONE: Player Placeholder - prison_rankup_rank_tag**
When adding the new placeholder code, the prison_rankup_rank was set to return the 
rank name and not tag. So added a prison_rankup_rank_tag so there would be access
to both to give the most flexibility.

* **DONE: New Feature: List all registered plugins**
To better support server owners when they have issues with Prison, it would 
be very helpful if **/prison version** would list all registered plugins in
a concise listing. In progress.  Included now in bleeding.
All integrations are now listed with their version.
The spigot command **/pl** (plugin list) shows all active plugins.

* **DONE: New feature: Track how many blocks are mined from the mines**
Stats on how many blocks are mined from each mine. May be bad to track,
but could open the door to interesting stats.
**DONE:** this is a whole mine sweep to count air blocks and is being performed
for the benefit of placeholders.  This could be improved with block break
event tracking.

* **DONE: New Feature: Add Placeholders for Mine related items**
Examples would be place holders for all mines, and their stats such as
size, dimensions, percent remaining, reset duration, time left until reset,
players currently within the mine, ect...

* **DONE: Offline player support**
Was not possible to get offline users through the prison API. 

* **DONE: New Feature: Admin reset of Player Ranks**
Bypass the costs for the players. The admins can now use
/ranks promote, /ranks demote, and /ranks set rank.

* **DONE: Eliminate support for Sponge**
It's not being used, so eliminate it and allow prison to possibly eliminate the
extra layers of indirection it currently has to improve performance and to 
possibly reduce the possibilities for errors. 


* **DONE: List currencies that are used in Ranks**

On startup, gather all currencies that are defined within the Ranks, confirm there is a economy that supports it, and then print the list with the ranks module.



## Known Issues - v3.2.1-alpha.9 - 2020-04-26


* **Prison v3.2.0 (and older) has limited Placeholder Support**

Prison v3.2.0 only has one chat placeholder and it is {PRISON_RANK}, which must be in all uppercase.  

The only Prison integrations supported with this version are PlaceholderAPI and MVdWPlaceholder.  

The placeholders supported by PlaceholderAPI are prison_rank, prison_rankup_cost, and prison_rankup_rank.  Supported case is unknown, so use lowercase.

The actual placeholders for MVdWPlaceholder is actually unknown, but may be the same as listed for PlaceholderAPI, but I cannot confirm it.


* **Prison v3.2.1 Placeholder Support**

The best way to find the available placeholders is to use the command **/prison version**.  Keep in mind that any placeholder that ends with **minename** will be expanded for each mine, substituting the mine name for the suffix **minename**.  For example, if there are 30 mines, then Prison will register 30 placeholders for each listed placeholder under **/prison version**.  

The placeholders will be registered as shown, in lowercase.  They actually are case insensitive, but since they are registered in lowercase, the various placeholder APIs may only recognize lowercase entries.  Also Prison's placeholders are registered without any brackets.  Most placeholder tools use {} and a few others use %%; refer to the placeholder's documentation on correct usage. 

Prison's Ranks has a chat handler that now supports all placeholders.

Support for PlaceholderAPI is through the prefix of "prison", of which it will route all placeholders with prefixes of "prison" through prison.  When PlaceholderAPI makes calls to Prison, it strips the prefix.  Therefore, Prison's placeholders will respond to the full placeholder, or the the placeholder minus the prefix.  The list of all existing placeholders within Prison are not pre-registered.

The MVdWPlaceholder api requires all placeholders be registered.


* **Some block types may not work for 1.15.x**
Since prison is not currently using the correct block names for 1.15.x, some
block types may not work. Prison is still using magic numbers for the 
block types and those no longer work for 1.15.x.  Symptom would be that
you set a block type such as birch block, but with the loss of the magic 
number, it will revert back to just an oak block.  ETA may be with
release v3.2.2?


* **Unable to change language on all Aspects of Prison**
Currently the number of phrases that can be changed to support other
languages is very limiting and requires a decent amount of manipulation
of extracting files from the Prison Jar. Future releases will allow just
about all uses of English to be replaced by external language files.
This will include error messages for players and mods (console errors), 
command descriptions, and even replacement of the English commands as 
aliases.  The implementation of this will help ensure "errors" are caught
at compile time, and not runtime to help improve stability of the game.
Also the way the language files are structured at runtime will make it 
easier to edit them.



* **Information: Setting the correct currency for Prison**

	Note: Moved to FAQ docs.



* **Known issue with LuckPerms v5.0.x Causing Prison Load Failures**

	Note: Moved to FAQ docs.



* [ ] **Reports that other plugins may cause issues with Prison**
It's been mentioned that a plugin or two, named something like 
"nohunger or nofalldmg", may have been causing issues with Prison.  
Not sure if its the loading or running, but it behaved as if the
mines and ranks module was not loaded since those commands were not
functional.  Only /prison was working.  This appears as the same general
effect of LuckPerms v5.x failures, where they caused a failure that
prevented prison from performing a normal load.  
I have not looked in to these plugins, but I would suggest that 
WorldGuard should be used instead of these plugins to eliminate possible
conflicts.



* [x] **REMOVED: No support for sponge - appears like it never had it**
Note: Support for sponge was commented out in two gradle files. The source code
remains, but it will not impact the builds anymore.

There is a sponge module, but there is so little code that has been written,
that it does not appear to be hooked up.  There is no way it could have ever worked
correctly since so many core components needed for the functionality of prison 
are dependent upon Spigot internals, of which those same function calls under 
Sponge's API are just empty or returning null values.

For example, getScheduler() and dispatchCommand() both are empty, but they 
are currently heavily used in both the mine reset process and also for
ranking up. 

It would be a really major effort to hook up the missing parts. I don't even 
think anyone is trying to run it under sponge.  Maybe best in the long run to 
eliminate the sponge module and just focus on making prison better overall.
I think if I do get around to disabling it, it will just be commented out of the
gradle build such that the source will still be there, but it will be excluded
from the build.  Otherwise as new features are added, and existing ones under go
major changes, then the Sponge components will have to be revisited and would be 
wasting resources (and time) for no reasonable purpose.


