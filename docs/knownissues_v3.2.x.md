[Prison Documents - Table of Contents](prison_docs_000_toc.md)

# Prison Known Issues and To Do's for v3.2.x

This document is intended to keep track of known issues and also provide for
a short list of To Do's. This list is intended to help work through known
issues, and/or to serve as items that should be added, or fixed.




# To Do Items - During Alpha v3.2.5




* BlockEvents - Action based upon blocks broken, not a percent chance.


NOT an Issue: OfficiallyGuo — Today at 5:41 AM
[19:34:33 INFO]: | Prison |  Spigot18Blocks.getBlockType() : Spigot block cannot be mapped to a prison BlockType : CONCRETE id = 251 data = 11  BlockType = null
Should I worry about this? Prison Version: 3.2.5.  Even tho I get this message, there aren't any actual errors happening in-game.
NOTE: This is not an issue.  OfficiallyGuo is running spigot 1.12.2 and trying to use concrete. More of a reason to use the new block model.


DONE: * Warning if using `/mines set area` and volume is over 20k in size.  Could possibly be an error.


* ladder commands


* global virtual mine:  To apply mine commands & blockEvents to all other mines.

 

DONE * TP warm ups - for /mines tp
  - Take a look at how essentialsX deals with it


DONE * Issue with `/ranks autoConfigure` if Mines module is disabled.  Gets a NPE, which is not surprising.  Note: line number is from v3.2.4 so does not match source anymore.
[21:00:38 WARN]: Caused by: java.lang.NullPointerException
[21:00:38 WARN]:        at tech.mcprison.prison.spigot.SpigotPlatform.getModuleElementCount(SpigotPlatform.java:1077)
[21:00:38 WARN]:        at tech.mcprison.prison.ranks.commands.RanksCommands.autoConfigureRanks(RanksCommands.java:202)
[21:00:38 WARN]:        ... 28 more



* possible change to /prison version all to include errors during startup.  Errors would need to be captchured.


* To `/ranks autoFeatures` add warnings at the completion identifying that the user must create any needed groups.
 - Note that WG global region needs to have the flag `passthrough deny` set.




DONE: Look at sellall and XMAteral's use of parse.  Needs to handle it with an item stack.


Personal mines.  Work in conjunction with a plot world?
- sellable and so would be the features with various upgrades
- Create a new module based upon Mines with new features to support player interactions and upgrades.


- Hook up block filters on the block events.

  
- /prison utils mining
  - Add XP direct and ORBs


Auto features not working outside of the mines.
- Maybe be enabled and working now?

  

- Add new placeholders:
  - Top-n - Blocks mined for mines
  - Top-n - Most active mines (based upon blocks mined)
  - Update papi's wiki
  - Track stats on placeholders?  Could be useful in tracking down expensive stats.
  
  
- Add blocks mined for players
  - 
  
Review the chat hander in the spigot module. It was rewritten a few weeks ago to fix some issues and to optimize how things are handled.  The issue is that the new code (way of handling things) needs to be extended to other areas.  So review the SpigotPlaceholders class and see how it can be updated.  Then end result will be less code and less potential issues.

  
- Update /prison autofeatures to include new settings.


- Could make /prison autofeatures reload happen. Alias: /prison reload autofeatures


Not sure if the following is fixed?
old block model - block constraint - excludeTop - not allowing block counts to be shown
   - air block count fixed and working



- Optimize the handling of chat placeholder.  They will always be the same for the whole server, so cache the PlaceholderKeys that are used.
  - Partially done.  The chat handler was updated, but the change could be pushed in to other existing code to improve the flexibility and resolve some of the weaknesses that may exist.


- /mines set size <mineName> walls 0 is not refreshing.
  - NOTE: Cannot refresh liners if size is 0. The reason for this is that a liner may not always fill the full edge.  Therefore, a value of 0 cannot tell if there should be a liner block in that position. Normally a liner only replaces existing block when applying the liner, or it forces it so it fills the whole edge.  If it's not normally forced, then it cannot be done.
  - NOTE: See the next comment. That may be the best option since anything else will never be perfect anyway and it will also be overly complex.  The following is a simple and clear way of repairing the liners.

- Mine liner data - Is there any way to capture what blocks were set so a "repair" can work?
  - Only real option appears to be to +1 it, then -1 it to force the repair.
  - Or when running the repair state something like:
    "Repair can only update blocks that are in place in the liner. If one is missing, then OP yourself, then fill in the voids with any blocks (cobblestone) then run the repair again."

   
- Future Block Constraints:
 - GradientBottom - The block has a greater chance to spawn near the bottom of the mine. 
 - GradientTop - The block has a greater chance to spawn near the top of the mine.


   
   
   - TODO: Need to have an alt block list for mines were blocks that are not actually 
     defined in that mine included there.  This is so air and other blocks that don't find hits can be included with the counts.  Needed for when blocks are changed so it does not lose change status?
     


- Prestiges max - if at max show 100% or Max, etc... Maybe set "max" on a placeholder attribute?


- Add a prestiges config option to auto add a zero rank entry for prestige ranks.




- Top Listings
	- Ranks & Prestiges
	- Individual Ranks
	- Mine base
	- placeholders - dynamic - position in list
	- How to dynamically keep this in memory, without lag or delays? 
	  - Timer to track updates to player's balance so it's not always performing updates
	- Sort order:
	  - Ladder, Rank, percent, name
	  - Penalty for going over 100%?
	    - Encourage ranking up instead of sitting at one rank to dominate.
	    - if > 100% - Take excess and get % of rankup cost and divide by 10, then subtract.
	    - if rankup cost is 1 million and player has 2 M, then they will have a calculated rank score of 90.00.
	      - if cost is 1 M and they have 3 M then it will score them at 80.00
	      
	      
- Provide a generic placeholder that can have the value supplied through the placeholder.
	

- Prepare for sellall integration to the new block model (simple way to put it)
  - PrisonBlock - add "price" - Maybe keyed by "shop".
    - custom currency support like ranks?
    - shop, currency, price, item
  - DONE: RankPlayer now has hooks for getBalance, addBalance, setBalance, both with and without custom currencies.
  - Hook startup for PrisonBlock to sellall to preload the price 
  - PrisonBlock - add quantity
  - Add a utility method for converting a PrisonBlock to ItemStack
  


- blockEvent
  - simplify add - use common defaults - can change features with the other commands
  - Add a target block name
  - Not an issue: "Use of placeholders is failing %prison_ is failing on %p"  Turned out they were trying to use %player% instead of {player}.
  
  
- auto features
  - custom list of blocks for fortune
  - allow to work outside the mines
  

- Issue with /ranks demote and refunding player.
  If the current rank has a custom currency and the player is demoted with a refund,
  the refund is credited to the wrong currency
  - This will be easier to fix once currencies are fixed
  


* **Custom block issues**
- If CustomItems is loaded successfully but yet not using new block model, show error message
- Show a message at startup indicating that the new block model is enabled or not enabled


* **Rework rank permissions to eliminate need to put perms in rank commands**
- Enhance the PermissionIntegration abstract class to also work with group perms.
- Add to ranks two new fields: permissions and permissionGroups.  Save and load.
- Add a new boolean field to ranks: usePermissions. Save and load.
- Add support for these perms within rank commands
- Rewrite rankups to use these perms when ranking up, promote, demote, and also for prestiges


* **Prestige Options**
 - DONE: Reset money on prestige - boolean option
 - DONE: Prevent reset of default ladder on prestiging.
 - Auto Prestige - server setting or player setting?
 - prestigemax - keep applying prestiges until run out of funds
 - DONE: rankmax - keep applying rankups until run out of funds
 - Eliminate prestige ranks - (optional)
   * Would need ladder commands
   * Need to define an upper limit of how many
   * tags may have a placeholder: `&7[&3P&a{p_level}&7]`
   * Have a base cost of prestige: example 100,000,000
   * Have a cost multiplier for ranks: example: 10% more expensive each rank with each prestige
   * Have a cost multiplier for prestiging: example: 20% more expensive each prestige
   * Have a cost multiplier for /sellall: Example: 0.005% (1/2 increase in sale price) or -0.015% (1.5% decrease in sale price to make it even more difficult per prestige)
   * Have a list of permissions and permission groups
   * The above settings are pretty general and would apply to all generated prestige levels, but to allow for customizations, then ladder ranks, perms and perm groups could be setup to accept a level parameter to be applied at a specific level.  Tags set at a given level could also be applied to higher levels until another tag takes it place.
   
 


* **Get new block model working**
  *  Start to enable and test various functions
  *  Add in Custom Items Integration
     *  Code Integration for CI - Key to specific version due to api changes
     *  Pull in custom blocks from CI API
     *  Place blocks with CI api
     *  Not sure how block break would work with CI api?
     *  Setup sellall to work with CI api
     


* **Combine a few commands & Other short Notes:**
 - Combine `/mines set rank` and `/mines set norank`
 - Combine `/mines set notificationPerm` with `/mines set notification`.  Add an option to enable perms.  Allow the perm to be changed? Maybe even use as a default the same permission that is used in `/ranks autoConfigure`.
 - Combine `/mines set zeroBlockResetDelay` with `/mines set resetThreshold`
 
 - Store the permission a mine uses so it can reused elsewhere (know what it is so it can be used). 
 - move `/mines playerinventory` to `/prison player showInventory` 
 - Add alias `/prison player info` on `/ranks player`
 - Add alias `/prison player list` on `/ranks players`



* **Value estimates for a mine**
We know what blocks are in the mine and the percentages.  If people equally mine all blocks (some only go for the more valuable ones if they can) then we can produce a formula that can tell you how many estimated inventory fulls it would take to reach the rankup cost.  That could be a really awesome "validation" tool to make sure one or two ranks are not messed up with either being too easy or too difficult. Will need hooks in to auto manager tools to calculate fortune and what results from block breaks. Could be complex.
`/mines value info` show breakdown of a mine's defined ores and what it would take to reach /rankup
`/mines value list` show a listing of all mines with the key details: value per inventory full, how many inventory fulls to rankup.



* **Commands - Enhancement**
Be able to select rank and mine commands for edit and deletion, or even moving, with line numbers.


* **New Block Model - Implement in parallel**
Implement and have a fully functional new block handling mechanism that operate in complete parallel to the old method.  This way admins can turn it on when they want to use it, otherwise their server will continue to use the old code.

 1. Done: Create core classes for new block types.  String based so 100% flexible.  Names based upon XMaterial enums so easy mapping to XMaterial then to system version dependency.
 
 2. Done: Available blocks based upon XMateral and spigot version they are running. Create the initial list to be used for block searches.
 
 3. Done: Needs Review: Disable MineData.getBlocks() and use that to write all the main parallel code.  Reenable.
 
 4. Disable BlockType and write parallel code. Reenable.
 
 5. Hookup block search. 
 
 



* **Update config.yml when changes are detected**
Preserving the current settings, replace the out of date config.yml file with the latest that is stored within the jar.  Updating the settings as it goes.


* **Ladder commands - global for all ranks in that ladder**
Add new placeholders for ladder commands to be able to have generic ladder commands that will apply and be ran for all ranks. May be able to eliminate the need for most rank commands.


* **Rank Commands - Edit and delete**
Add line numbers and enable the ability to edit and delete by line number.






Enable zero block counts for parent mines.
if 100% block type of IGNORE, then after reset do an full mine air count so zero block reset works. :)





# To Do Items - Post v3.2.1


<h2> Major tasks</h2>



* **Integration in to WordEdit & WorldGuard**
Largest problem with providing a solution is that prison supports 1.8 through 1.17!!
WE and WG has a strict policy that they only support the current version of spigot/mc. They do not support older versions, but their older versions that worked with older versions still exists to be used.
The major problem is that you cannot run scripts from console since WG and WE require to know what world the actions are in, and they get that from the active player.
There is an API that can be used, but they warn that there are changes to the API from version to version, so it may not be possible to provide the same API calls from 1.8 through 1.17.  Will not know until we try to use them?

Just had this idea... What if for these main commands, for configuration purposes, the player would have to be in game.  When they run these commands we TP the admin to the mines' spawn point, then run them through the console on behalf of that player?  That way WE and WG could get the world from the on line player.  But this will not work for mine resets... :(





```
  Note: May be spread out over multiple releases.
  1. New Block handling if not fully apart of v3.2.1.
  2. New Multi-language support.
  3. New Wizard Configuration support.
     A. Basic Prison functions. 
     B. Basic Mine behaviors
     C. Basic Rank and Ladder behaviors
     D. Basic prestige behaviors
  4.Creation of new scripting language for automation tied to wizards
     A. When creating a mine perform scripted actions to configure each one
     B. When creating a rank perform scripted actions to configure each rank
     C. Create the permission group within the permission plugin selected
     D. Add perms for that permission group, including warp support (prison/warps)
     E. Auto generate rank commands to add and remove group perms from each rank
```

.


<h2> Higher Priority TO DO Items </h2>




* **Refactor GUI?**
Performance updates and validation to ensure computationally expensive processes are not running constantly.



* **Possible Issue with delete Mine**
Delete a mine and try to add a new one right away with the same name.  A user said it would not allow them, that the mine already exists.
* Tested on a few versions and could not reproduce. Keeping this entry here to retest in the near future.


<h2>To consider - Lower priority</h2>



* **When creating a new mine, register that mine with the placeholders**
Might be easier to just reregister all mines?  Not sure if that will work?
Right now, if a mine is added, in order for it to show up in the placeholders, you would have to restart the server so all the placeholders are reregistered.
 * * maybe just automatically run reset when vital elements change? * *



* **Add WorldGuard Support**
This is a work in progress. The biggest problem is that although older versions exist, they only support the latest version, and they have removed the older documentation and maven repositories. So access is highly limited to functional resources for performing builds.  The current documentation identifies that major functional changes exist even between minor versions.  So one API for v6.1 (supports mc 1.7 through 1.8 only) may not work WG v6.2.2 (supports only mc 1.12), and may not even be close to support v7.0.2 (supports 1.14 through 1.15).  

Not knowing what "range" of WorldGuard behaviors are supported through the API, or if they are even the same, the initial range of supported integrations for WorldGuard will be very limited until it can be fully tested to see where those limits are.

List of features that could be nice to have, ordered from easiest to most difficult to implement, with the possibility of never being able to do any of these:
  * Using prison's mine boundaries, set a WorldEdit selection to those same dimensions so the user does not have to reselect the same area. 
  * Using prison's mine boundaries, define, or resize, a given WorldGuard region.  Using a Prison defined naming schema.
  * If a prison is deleted, then remove the Prison's WorldGuard region.
  * Auto define the WorldGuard global templates and permissions, then auto define, update, and remove all mine related regions as the mines are added, changed, or removed.   * Detect if there is an out of sync situation between prison and worldGuard.
  

* **Get started on new Multi-Language Support**
This is put on hold for the v3.2.2 release.



* DONE:  **Upon startup validate all Blocks that are defined in the mines**
\
Upon loading prison, validate that all blocks that are defined within each mine are actually valid for that version of minecraft.  This will be important in that it may help eliminate possible errors when the server owner upgrades the server, or other plugins.  Also it will be very helpful when Prison's block handling is enhanced since it will be a tool used to verify and maybe even fix incorrect block types.



* **Improve some of the display pages for ranks and ladders***
Can add more information to the listings so they have more value.


* **Better logging of major events***
Need to log major events such as rankups, both to the server log, and also
to the community.  Server logs for these events, especially when money is
involved, is important.




* **Possible new feature: Track how many blocks a player mines, including types***
Stats could be interesting over time, and could also be used for in game
bonuses and rewards and incentives.


* **Redesign the save files to eliminate the magic numbers***
Most of the save files within prison, for players, ranks, and ladders, are
using magic numbers which is highly prone to errors and is considered 
very dangerous.  Also prison would benefit from a redesign in file layout
to improve efficiencies in loading and saving, not to mention greatly reduce
the complexities within the actual prison code, which in turn will help 
eliminate possible bugs too and give tighter code.



* **Notification that inventory is full***
In progress!


* **Custom Mine reset messages per mine***


* **Enhancement: Multi-Language Support***

Offers for translation:
  Italian : Gabryca
  Greek : NerdTastic
  German: DeadlyKill ?? Did not ask, but a possibility?
  French: LeBonnetRouge
  Portuguese: 1Pedro ? 
  

* **Auto-Config of other Prison Related Plugins***

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



- DONE - Add support for EZBlocks... need to test?


- DONE - fixed - BlockEvents don't not run unless auto features are enabled


- DONE - Hook mcMMO up to the explosion events


- DONE: CMI currency is not working correctly with vault and prison (Monzter)
- Unable to test since it is a premium plugin. :(
- Turned out the CMI version of vault he downloaded didn't work. Had to use the injector.



- DONE: Add potion effects to prison mines.  
 - The potion effects should only last a short duration with auto renewal
 -   


- DONE: /ranks set currency may not be able to remove custom currencies
  - able to remove custom currencies if they are set


- DONE - **Block Counts Refresh - (ExtraSean)**
- His enchantment plugin does not have an explosion event
- Provide a way to update block counts using TargetBlockList
- Delay 4 to 10 seconds based upon percent remaining
- Submitted after a blockBreakEvent
- This is a new feature that has to be enabled for each mine


- DONE - Prison Utils
  - potions - need to work on that!!



- DONE: Add /prison utils smelt
- DONE: Add /prison utils block


- DONE: Block breaking in auto manager
  - Just thought of this... if using the target block list to record block breakage,
    then can still double up the counts if more than one explosion event includes the
    same block.  Need to add to the target block class a boolean to indicate that the
    block has been broken, or at least already counted.  That way duplicate use of that
    block won't result in over counting the breaks.
    

- DONE - do nothing - auto manager - add support for mending enchantment on tools.
- mending assigns XP received to repair tools and armor. This is more complicated than needed and there may be other solutions, such as dropping orbs so it can be processed naturally.


- DONE: Convert AutoFeaturesConfig to a Singleton.  GUI is having issues with consistency?
  - The AutoFeaturesConfig is not a singleton, but there is now a wrapper that is.


- DONE: Provide util functions that can run other commands for the players.  Could be useful to for BlockEvents.
  - DONE: repair & repairAll
  - DONE: potions and potion effects - in progress




- DONE:  List more enabled features within /prison version such as if GUI, sellall, auto features, etc are enabled.


- DONE: Change `/prison version` to minimize info.  Add an "all" feature to show full details.  Capture errors and reshow them in the /prsion version?  Not sure about this last one.


- DONE - Under SpigotUtils.ItemStackRemovalAll - remove the error messages.  Basically it is normal for non-standard material types to be used by other plugins, and they trigger XMaterial's exception that an item cannot be mapped.  Basically the datavalue is non-standard.




- DONE - Look at rank commands that may not be running as console? 
- The commands are being ran as console and not user.
- The issue was it was using the wrong user object and therefore the commands were not being resolved correctly.


- DONE - Was a new config that was not allowing the auto pickup to be enabled. Backpacks not working with auto pickup anymore?  Noticed at one point it wasn't working, but need to see if its still the case.  The issues could have been resolved.



- DONE:  /rankup states the player does not have enough money, then takes what they do have.


- DONE:  Auto manager drops - fortune - if spigot applies fortune to the drops (item != 1) then use a different calculations for generating the extra drops.


- DONE: minepacks plugin - NoClassDefFoundError - pcgamingfreaks/Minepacks/bukkit/API/MinepacksPlugin
  - Not an issue: I think this was a non-related issue that someone else was happening. They later reported it was woring well.


- DONE: ranks autoConfigure 
  - DONE: add blocks that are assigned to the mines to sellall
     - add a block price to the blocks - base upon essentialsX?
  - DONE: enable sellall

- DONE: auto features - lore - need to be hooked up to TE explosions, CE blasts, normal block breaks... etc... basically review and enable where needed.


- DONE: /prison utils message - add ability to send message to player or broadcast



- DONE: COUNT blocks upon startup when doing the air counts:


- DONE: build targetBlockList when counting air blocks upon server startup.


- DONE: Use the mine tag name over the mine's name if it exists.


- DONE: TE Explosions does not produce exact counts.  Have done extensive work to get this to work better, but can only get closer and has not been exact. Not sure why at this time.


- DONE: Setup an on suffocation event listener so if a player gets stuck in a mine, tp them out to the spawn point. 
 - This is done, but the player has to take damage before the event is fired.  Therefore it is not instant when they log in.  Also if they cannot take damage, such they are in god mode, then it won't kick in either.  I've also seen this where it won't tp myself to the mine's spawn because I don't have the perms to go there, but not really sure if that was the cause.


- DONE: For `/ranks player <playerName>` add a optional perm to list all permissions that match a pattern.  This will allow checking to see if a player has a specfic perm.


- DONE: player.getPermission() is not returning anything??


- DONE: Possible issue with auto features preventing WorldGuard from protecting a mine.  In the auto features GUI, when the bottom three features are turned off then WG won't protect the region.


- DONE: ? auto features - issue with lore. world guard - not working properly





- DONE: Try to for a limit on a certain block type?  M_Malmstedt
   - a specific number may not work well... but maybe a max limit?  Or a range?
   - max limit prevent more generation of that block type.
   - if a min limit and it's not reached, then reset the mine or randomly add that many more to the mine?


- DONE: Block generation constraints:
   Let me "make up" some future constraints so we can better understand how powerful this can become:
  - Min - Not yet implemented but a work in progress - Ensures a minimal number of blocks to be generated in a mine
  - Max - Caps the max number of blocks generated in a mine
  - NoTop - Prevent a block from appearing in the top layer of a mine.  Percent from the top where it cannot spawn.  10% would be no spawn in top 10% layers.


DONE:
[Plugin Prison - To be able to manage at which layer such or such block appears]
- For example if I create a mine of 10 high, with iron_ore, gold_ore, emerald_ore,... I would like choose between which layers a particular bloc appears.

  "blocks": [
    "STONE-20.0-0",         0 when not configured = all layers 
    "IRON_ORE-25.0-0,5",    0,5 for all layers up to the layer 5
    "GOLD_ORE-25.0-4,7",    4,7 for layers between layer 4 and 7
    "EMERALD_ORE-25.0-8,0"  8,0 for all layers after layer 8 (in example : so up to layer 10)
  ],




- DONE: restrict access to the root commands in prison so players cannot run them even though they won't have access to the actual commands.


- Fixed: player-mine placeholders do not work


- Fixed: rankup does not work.  This may have looked like some placeholders were not working after a /rankup.


- DONE: Hook the mine reset paging variables up to config parameters.  OfficiallyGuo needs them.
  - 

* **DONE: Need to figure out how to better handle the Spigot commands that bypass the prison command interface**
They are inconsistent and non-standard to prison. They do not support the `help` keyword, nor do they show what perms they need.  


- DONE: Remove obsolete objects:
  - prison-spigot/out
  

- DONE: use the spiget jar and remove the maven hook since maven has been going down frequently.
   - There were strange issues when "trying" to remove the maven hooks. I don't think they were caused by that, but more so could have been triggered because the core of gradle has changed.  Most of the issues manifested themselves in unit test packages where core classes were missing at the running of the junit tests.  Such failures should not happen since the environments for unit tests should not exclude core classes that are available within the main source.
   

- DONE: /mines tp - mineName optional - default to the highest ranking Rank's mine.


- DONE: auto pickup fortune - formula for levels greater than 5?
   - Allows unlimited fortune levels.


- DONE: Remove a player from a ladder. (This already existed)
  - Cannot remove from the default ladder

-
- DONE: new placeholder - user - prison placeholder for total multiplier
  - see the new API... 
  


- DONE: Placeholder Attributes:
  A way for placeholders to be customized dynamically other plugin configs.
  Example would be a scoreboard that uses bar graphs but customizes each one to different colors, characters, and size. 
  Use :: to identify the start of an attribute followed by the type of attribute.
    Examples:  ::nFormat:    and   ::bar:  
  Use of : to separate each parameter.
  


- DONE: Hex colors:
  https://www.spigotmc.org/threads/hex-color-code-translate.449748/#post-3867804
  https://regex101.com/
  (?i)&#[A-Fa-f0-9]{6}|&[0-9A-FK-OR]
  




* **DONE: Save the liner settings for each mine**
Currently is not saved and have to manually reissue each time.
Save all six faces and include pattern and if forced
Add command to regenerate, or reapply, the liners.
Add liner details to mines info



* **DONE: Add numeric abbreviations on rank costs**
 K, M, B, T, Q, etc... 
 New placeholder? Formatted?
 https://en.wikipedia.org/wiki/Metric_prefix Use Prefix Symbol column.


* **ranks autoConfigure - Feature Ideas**
- option for using EssentialsX warps instead: essentials.warps.<mineName>
- DONE: Generate default blocks for all mines.
- DONE: Add perms for /mines tp
- DONE: Perm names: mines.tp.<mineName>, mines.<mineName>



- DONE auto manager - durability not working even when feature is enabled
- DONE autoConfigure - fixed?


- DONE mines - add storage for liner so it can be regenerated
- DONE mine liner - add bedrock
- DONE BlockEvents - submit to run synch or async. Eliminated asynch and added inline.
- DONE BlockEvents - multiple on same command.  Use ; as separators
- DONE auto features - fixed - durabilty not working
- DONE auto features - cannot turn off smelt or blocking


* **DONE: Sellall - Hook up to prison command handler** 
Currently sellall is not hooked up to the prison command handler and it needs to be.


* **DONE: added to their papi's wiki...**
Add prison Placeholders to papi's website for downloads
Prison is already using papi (PlaceholderAPI).  But see if we can add prison to the supported
plugis for papi's cloud.  Should time this with the v3.2.1 release so there are more 
placeholder to use.

DeadlyKill: This what he needs ita
Papi
Hook Plugin
They have those expansions which hook other plug-ins

https://github.com/help-chat/PlaceholderAPI/wiki/Placeholders


* **DONE: Tab Completion***
Hook up tab completion on the prison commands.



* **DONE: EXP with auto pickup**
For certain blocks such as coal, diamonds, other... provide xp...


* **DONE! Rework commands within the spigot module so all user facing commands are routed through Prison's Command Interface**
 Blue should work on this.



* **CANNOT DO: Add to the Command annotations an option of *async* to run that command asynchronously**
Check to see if the commands are being ran sync or async.  Add a parameter support so 
commands that can be ran async.
Cannot run commands in async mode... they fail and cannot be ran. Can only run tasks in async.


 
* **DONE: Built in selling system***
 

* **DONE: Rename Mines**
Been a few requests to be able to rename mines.  Since so much can go wrong with manually changing the files, this should be a reasonable new feature added before beta.


 
 * **Fixed: Problem with rank removal from Ladders**
 This was fixed a few weeks ago. Parts of the code was rewritten when implementing the new mine sortOrder code. The bugs were found and fixed at that time.  There is not a new ladder, but the concept of "none" as a ladder exists virtually.  During prison startup all ladders and ranks are displayed within the prison startup details and none is always listed there.
- Create a new rank to the default ladder.  Add a player to it. Then remove the rank from the ladder.
- The ladder no longer contains the rank. But the player is still associated with the rank, but yet ranks cannot contain players if they are not on a ladder.  The commands expect a valid ladder name.  Also there does not appear to be any checks and balances when ranks are moved from one ladder to the other since ranks have no idea what ladder they are in.
- This could cause major corruption if moving ranks between ladders, removing ranks from a ladder, and players being associated with those ranks. 
- Create a "void" ladder and prevent ladders from being named: default, void, and prestige.  When a rank is removed from a ladder, place it in to none and update all players that use that rank so the rank is still valid.  Do not include void ladders in placeholders.




* **Done: Prestige references**
Add the prestige command to the /prison version page; rework the commands layout.
Add the documentation for prestige.  Gabryca provided the base docs.


* **Done: Prestige Fixes**
The existing prestige command is GUI only.  That may be an issue with 1.8.x. When testing, it appears to work, but may need an non-gui way to rank up.

The /prestige command does not show the cost.  The cost must be shown, along with a warning that the player's balance will be set to zero.

Since the player loses their balance, which may be far more than what the prestige may cost, there really must be a confirmation added confirming the cost, and the player's balance.  It should also identify that the excess amount of money the player will lose.

Must change the cancel button lore.  It's grammatically incorrect with a double negative.  Should only be: "Cancel Prestige".  Likewise the confirmation button should just be "Confirm Prestige" since the colon is grammatically ambiguous.



**Done! Parameterize Sort order for /mines list.**  
Default sort order should probably be alphabetical.  
Currently it is alphabetical with most active mines since restart at the top of the list, based upon blocks mined.


 
* **Done!! Add GUI support for v1.8.x**
 Might be able to add GUI support for 1.8.x with a few simple lines of code?



* **Done: Autosell feature**
Be able to allow the players to toggle it on or off. Will not sell what is in inventory, but only the blocks that they are mining.  Use /autosell .
What would be nice is if you could look up all permissions that start with prison.sellall.1.05 where the number at the end is the multiplier.  So you setup the multiplier based upon permissions and no other configs.  Then take all the multipliers and multiply them together.  So if they have a 1.05 (donor), 1.03 (special limited time), 0.97 (prestige penalty... more difficult the higher you go) would have a total of 1.049.  So basically different ranks, or even different tools could increase or decrease payout.




* **FIXED: BUG? Able to add more than 100% in one mine?**
User reported having three copies of everything which totaled more than 300%.
Used the /mines block add function?  

 
 
* **DONE: Add player names to the player file**
 Have no idea who is who in the player files.
 Make it an array of a new object, player name that has name and timestamp.
 When the file is loaded and the player is online, check name, and if not recorded, then add it. 
 

 
* **DONE: Add permissions on notifications for mine resets **
Only send a message to the player if they have the permission set, and follow the other notification settings too.
Add permission `mines.notification.[mineName]` and have each mine have a boolean field to indicate that it should check for permissions on the players prior to sending the message.

 
* **DONE: Broadcast all rankups**
Option to disable it in config settings.



* **DONE: Placeholders - Reregister**
Add a new command `/prison placeholders reload` that will reregister all placeholders.  I don't think there is a clean way to unload the registrations, but just going through registration may be good enough. Deleted, or obsolete, placeholders may still be registered, but at least the new ones will get registered too.


* **DONE: Placeholders - Add placeholder counts to startup**
Counts on how many placeholders are generate and registered.


* **DONE: Placeholders - Add new placeholders**
prison_rankup_cost_remaining 
Such that `prison_mines_*_minename` will also be mapped to `prison_mines_*_player` and will only report the values when a player is in a mine.  
I think that will be super cool, since you could put some current mine stats in your placeholder such as `prison_mines_timeleft_formatted_player`  `prison_mines_remaining_player` and even `prison_mines_player_count_player`.

NOTE this was added: the new ones are playermines and there are a total of 24 new ones.  Will work great with scoreboards.



 
 * **Not an issue: Prison Wand breaks blocks**
 Should probably monitor block break events and cancel breakage with wand.
 This was caused by a conflict with a scoreboard plugin that advertised as supporting 1.16.2, but broke a few different plugins.
 
 
 
* **DONE! Complete the new Mines Reset Paging**
  Holding up release v3.2.1.


* **DONE: Enhance AutoManager**
Make auto manager more like vanilla on the drops and support silking.  Try to make more consistent between versions. 
This is a lot closer to the vanilla. May need to revisit in the future to enhance even more.


* **DONE: Add permissions to the AutoManger***
Add permission checking to AutoManager to allow a per-mine selection of which mines to enable it in or to tie it to some rank or donor rank.  Could also put lore checking in place so tools could be enchanted to perform these functions too.  Could have it so there is a percent chance related to the permission or lore.


* **DONE: Prestige and Rebirth**



* **Already Exists: Update the Prison command handlers to support help context**
-= Note I was unaware if you use the command `help` as the only parameter it shows the help =-
This would show the parameter details for the commands. Right now the annotations that defines the command parameters has descriptions for the command and the parameters, but they are not displayed anywhere useful.



* **DONE: Add support for player use of /mines tp**

Could be done through other perms and then checking to see if they have access t

that mine.  Perm:  mines.tp (all mines) or mines.tp.<mineName>.



* **DONE: Add a placeholder test command**
Create a command, like under **/prison** that can be used to test placeholders.
Have one where the user can enter any free text and then translate it.
Also have a page(s) that goes through all of them printing the place holder name and the current values.



* **Improve the prestige laddering system***
A plugin named EZprestige has been attempted to be used with prison. Not sure if successful?
-= Prison now has its own prestige system =-



* **DONE: Remove world check before loading mine***
Now supports deferred world loading, where the world loads after prison initializes.
This is a problem with Multiverse-core plugin since a softdepend loads way before a hard depend. As such, the worlds that were created with Multiverse-core have not yet been added to the bukkit list of worlds.

If the world is checked after the server is running, they will be available.  Put in a class variable that identifies if the world was verified, and if not, then check. 

Problem is that at startup time, we won't know if there is a problem with missing worlds.


* **DONE: Add /ranks remove currency [rankName] [currency]**
Done. Currently no way to remove a currency from a Rank to return it to normal currency.


* **DONE: Add onBlockBreak monitor to prison mines to count blocks mined**
Set EventPriority to monitor. Don't change anything. Just confirm block was changed to air and increment the count.
Do not have to precheck if the block was air before, since air cannot trigger a block break event.  Just confirm it is air when monitoring to ensure it was removed. If this works, then this would allow the elimination of the air counts.


* **DONE: Delay Mine Resets based upon blocks remaining**
 Done: This is the Skip Mine Reset processing.  It will not do a reset until enough blocks are mined.


* **DONE: Skip Mine Resets - Based upon usage and percent remaining**
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






* **Not needed: Exclude specific Prison commands**
NOTE: The player asking for this was able to disable the ranks module and that was able to solve their problem. There are no other reasons to cause this much trouble and redesign of the command handling internally, so this is being removed from consideration.
Ability to exclude, or ignore, specific commands upon startup. 
NOTE: this may not be needed. Disabling the Prison Ranks module solved the problem, which was trying to use EZRanksPro and prison's /rankup command was conflicting with that plugin's /rankup command




* **OBSOLETE: Sellall is now internal. Support QuickSell project for use with Prison *Only* ***

*Goal:* Something to consider. See if it can work with 1.15.x. This would provide a solution for prison servers to use with the full range of our supported platform versions.  Intentions of pushing changes back in to the main project and not maintaining a new project.

QuickSell has be abandoned, but could be very useful for prison to provide a simplified integration of features. 

Quickly reviewed code and it looks fairly good and probably has very low maintenance. Base initial support could be updating dependencies within Maven. Goal to get QuickSell to work with all supported versions of Prison and all supported versions of spigot.

Explicit support going forward would be directly related to Prison. If a support issue has to do with another 3rd party plugin, then support "could" be refused or unsupported 3rd party plugins could be removed. Primary focus would be for the support of Prison and to provide a QuickSell feature to users of the Prison plugin.

https://www.spigotmc.org/resources/quicksell.6107/

https://github.com/TheBusyBiscuit/QuickSell
Currently 15 forks.  Activity unknown.




* **Obsolete: New block handling system - put on hold**
-= Using XMaterial so this is covered, except for possibility of custom blocks =-

Current system is based upon enumerations which are static and may not reflect the actual run time environment.  Prison is compiled with 1.9.4, but yet the list may not include all blocks for all versions of bukkit/spigot/minecraft


If the new block handling system gets all blocks from org.bukkit.Material.values(), then it should reflect what's available on the server version that is running.  If the server owner decides to upgrade, or down grade, their server version, then they will be responsible for "correcting" any block name that is no longer supported.  This would be the negative for such a system


The benefits would be less to manage within prison; attitude of do what you want to do, instead of micro managing the list of blocks.  Dynamic to support newest blocks available on minecraft/bukkit/spigot, or another platform.  Ability to pickup custom blocks if they have been injected in to the Material enumeration


Currently there is a HUGE problem.  Upon testing, I have determined that although a block exists within the server's org.bukkit.Material enum, Prison cannot select it.  I do not know why. It could be related to the fact that prison is built with Gradle using spigot v1.9.4 and that imposes restrictions upon what enumerations can be accessed at runtime?  That makes no sense since no artifacts of org.bukkit.Material should be carried over outside of the compile time instance.  Until this issue can be addressed, there will be no work around or implmentation.



* **Obsolete: Block Types for Specific Versions of Minecraft**
-= Using XMaterial going forward =-
Add in support for the loss of magic values, and also provide for newer block types too.  Basically have a minecraft version selector that can tailor the list of available block types that can be used, based upon the minecraft version that is running



* **DONE: Add parameter to charge player for Promote command**
On /ranks promote you can now charage a player for that rank.  Also on /ranks demote you can issue a refund to the player too.  See the actual commands for usage.


* **DONE: New Feature: Upon block break events, log block changes**
This will allow dynamic live tracking of mine stats, such as how many blocks
remain and relating percentages.  The new async processing will enable this
to actually track individual blocks.


* **DONE: Document how to use essentials warps for each mine**
See documentation within github for these details.


* **DONE: Add placeholder aliases so they are not as long**
Done.


* **DONE: Eliminate the internal Placeholders**
Done.  Performance improvement.


* **DONE: Integrate GUI in to bleeding**
Done.  More GUI features will be added in the next release.


* **DONE: Setup GUI to use /prison gui**
Done.


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


* **Multiverse-core prevents worlds from loading**
Multiverse-core when it is a softdepend prevents prison from being able to access all of the worlds that it will normally load.  When it is a softdepend Prison loads prior to multiverse-core so bukkit has no knowledge of those worlds.

To fix this problem, you must manually add Multiverse-core as a hard dependency. 

**NOTICE** the line that starts with **depend:** since that line does not exist in the normal config.yml file. 

```
website: https://mc-prison.tech
softdepend: [Essentials, Vault, LuckPerms, Multiverse-Core, Multiworld, MVdWPlaceholderAPI, PlaceholderAPI, GemsEconomy, WorldEdit, WorldGuard, ProtocolLib, PerWorldInventory, Multiverse-SignPortals, Multiverse-NetherPortals ]
depend: [Multiverse-Core]
commands:
```


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


