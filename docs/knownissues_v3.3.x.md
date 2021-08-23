[Prison Documents - Table of Contents](prison_docs_000_toc.md)

# Prison Known Issues and To Do's for v3.3.x



# TODOs for v3.2.10 release:

1. DONE: Final testing of ladder rank multipliers
2. DONE: /ranks ladder moveRank not working
3. DONE: Forced global refresh of rank multipliers when a ladder multiplier is changed.
  - Should be simple
  - Run as async task
  - DONE: Force update when updating a ladder's multiplier - all players
  - DONE: Force update when changing ranks - only targeted player
4. DONE: Add ladder base cost multiplier to /ranks autoConfigure.  Start with a simple 10%.
  - Include message that it's enabled and how to change it or disable it:
  - /ranks ladder rankCostMultiplier prestiges 0
5. Add to /ranks player the detail information on rank cost multipliers
6. DONE: For '/ranks autoConfigure' add an alias to '/prison autoConfigure'

7. DONE: Change /ranks list so if non-op it does not show all the extra details.  A simplified list for plain players.

8. Liners: Some are only for later releases and do not work with 1.8.8.  So need to setup something to restrict the liners that are not functional for the older releases of spigot.

9. Hook up Prison's Explosion event. 


### Others

* DONE: Add a rank cost multiplier to ladders.  Sum all active ranks a player has to get the total multiplier to use for rank costs.
* DONE: Fix the "next rank" value with PlayerRanks.... needs to recalc with the new rank.
* DONE: When a ladder's rate cost multiplier is changed, need to recalculate all player's multipliers.  Setup a task?



# Start on mine bombs

 1. Select size of bomb. Configuration. 
   1a. Identify items to use as bombs
   1b. Bomb size
   1c. Bomb explosion pattern. Currently only sphere, but could include other shapes.
 2. DONE: Select list of blocks that will be effected by bomb explosion
 3. Throw bomb, or place bomb... start processing count down.
 4. Pre-fire effects: sound & particles (low priority)
 5. fire event
 6. Post-fire effects: sound & particles (low priority)
 7. Hook up prison's event handler for prison's explosion event


* DONE: Modified /ranks list to provide this feature
 - Maybe provide a /rankcost command?  Show current player rank costs with rank cost multipliers applied
 - /rankcost <player> <optional-ranks>
 - Optional ranks can be provided as parameters and it will adjust the calculations.
   - Example: /rankcost RoyalBlueRanger A p5
   * will show the rank costs based upon rank A and p5, no matter the players current ranks
 - Will allow players to better understand what rankup costs will be if they prestige twice or 10 times.



# **Mine Valuation Score** - **MVS** 
 - Calculate an estimated value for a mine based upon an inventory full.
 - **Total Blocks** = 2,304 = 9 x 4 x 64
 - For each block, it's percent chance will be multiplied by the **Total Blocks** to get the number used in valuation.
 - **BVS** = **Block Valuation Score** will be calculated by how many blocks are represented, times the block's value in /SELLALL
 - **MVS** = **Rank Cost** divided by sum of all **Block Valuation Scores**
 - Use block chances to determine amounts of each block. Ignore total stack sizes.
 - Calculate how many inventory fulls it will take to reach the next rankup cost.
 - This is a difficult calculation since it is based upon another rank's cost
 - This will give a score value on Mine Valuation.
 - MVS 1.0 or less indicates seriously easy and unbalanced.
 - MVS 20.0 would be normalish range
 - MVS 50.0 or higher would be considered extreme
 - Prestiges Valuations should be calculated on the highest rank of the default ladder.
 -Pickaxe enchantments, such as eff and fortune, will have a huge impact on how fast a player can rankup, but the Mine Valuation cannot take that in to consideration.
  

### others

* placeholder for total block counts and other playercache stats

* Include sellall costs in block lists




# **Possible bugs/issues:**

* DONE: /ranks ladder moveRank did not work to move from one ladder to another

* DONE: When adding a new rank or mine, auto reload all placeholders so they pick up the new entry.

* Test BlockEvent perms... they appear like they don't work.

* If ranks module fails due to no economy, try to make that a little more obvious.

* If no ranks are defined and the placeholders are attempted to be used, it is causing some errors.  No economy plugin so Ranks did not load. {prison_rank_tag} was causing an error with /prison placeholder test.  "Server: Invalid json. Unterminated escape sequence..."
The ranks module did not load due to no economy.

* Sometimes Player Ranks lores placeholders from placeholderAPI aren't working, 
it's unknown why it's happening.

* Auto features - disable lore by default - Maybe provide finer grained control on lore features?



# **Document updates needed:**

1. Document how to use Ladder Rank Cost Multipliers

2. Document that to get TE explosions to work, you must also create WG regions in the mine.
  - may be able to bypass with creative use of Access by Ranks (have prison directly fire the BlockBreakEvents within TE's functions?)
  
3. BlockEvent docs need to be updated to reflect recent changes (select by number).

4. Review commands listed in TOC.  Remove obsolete commands (some ladder commands were removed).



* Top Lists - Command based
 - Top ranked players by server
 - Top ranked players by mine
 - Top ranked players by blocks?  



* Add a `*all*` for mine name on remove mines blockEvents.


- hybrid placeholder graphs - hybars
  - super impose text in the graph and have it still be colored correctly.
  - text can be centered, such as the percentage that is feeding the bar
  - For wider bars, the title could be left justified, and the percentage right justified
  


* mine groups
 - Have global sharing of BlockEvents
 - groups cannot be in more than one world
 - all mines must be a part of a group
 

* Eliminate the following message from logging an error, but just keep a note in the code:
 - Cannot initialize NMS components - ClassNotFoundException - NMS is not functional - net.minecraft.server.v1_17_R1.EntityPlayer
 - This does not appear to have much of an impact at this time.
 


* add all commands to the server.yml file.  spigot 1.17.1 is getting fussy.


* Conversion tool to import SuperiorPrison users:
  - rank is based upon perms `permission.mine.A` and they keep former rank perms.
  - No prestiges
  - able to reset ranks so conversion can be ran multiple times.
  - Unable to really do this due to offline players not having access to the perms.  As such, and also due to the need ceasing to exist, this has been put on the back burner.


* extended fortune : Apply fortune to all blocks that bukkit does not apply them to.
 - Maybe do this through a list instead of blindly applying fortune to all blocks with a qty of 1?  Some fortune calculations will result in a qty of one.

* hook up alternative processing to have prison force the running of another plugin if a setting is enabled. This way prison can "control" how it processes the "left-overs".


* prison command handler:
  - Add command cooldowns - time in ticks - And cooldownTypes: server, player


* Player Cache:
 * DONE: Money earned per minute with placeholders
 * Add placeholders for blocks counts
 * Add placeholders for time onlines
 * DONE: Add time mining: per mine
 * Tracking by items such as mines or ranks, where the player's status can be reset, presents some problems.  Such as the block counts need to be reset for those items, but yet, we need to store them for historical purposes too.  So if someone prestiges 10 times, then there will be 10 different times through mine/rank A.  If block counts become a requirement for rankups, which it will, then the block counts for that mine must be reset back to zero.  So there must be a "current" and "historical" tracking.
  - Online stats:
   - track which players are online... may need to get the active player list every minute?  freq could be configurable.
   - track location of player... if the player has not moved in over x-ticks, then mark them as afk.  distance traveled is an important aspect... so afk machines may keep them moving in a 1 block radius.  Might be able to see if a player is being pushed by water or pistons? 
 
 



* When adding block stats.... add player online stats.
  - add events that run, similar to blockEvents, but maybe call them statsEvents?
  - So after so many blocks are mined... or so many minutes a player is online, run a command
  


* Enable mine sweeper when auto features is disabled.  Not sure if this is still needed?




* Add support for BOSS BAR when holding pick axe.  Maybe setup placeholders in the config.yml?
- Example would be showing the bar and percent to next rank.


* Add mine notifications by Rank
 - I forget what this is.... 
 

* Prestiges: Rewrite and enhance so as to make it "automatic" without the need to create ranks
* Externalize the mines module on multi-lang
* Add top-n rankings for mines and maybe other ratings
* Add block break counts to players



* Issue with decay functions or at least it shows the problem exists.  Enable a decay such as obby or rainbow, then test to confirm it works.  Then enchant a tool (increase it's eff).  Then test again and it does not.  This was seen happening while OP'd.  May not be related to decays, but it appears as if enchanting causes the pick to bypass prison?
- Was able to reproduce this at a later time



* Add optional block counts to level up.  So if money and block counts are used, then both have to be satisfied.
If only one, then only one of those would be used.
- DONE: Count only the block mined and not the results of fortune.  It will be easier to control how much mining a player does by ruling out the results of fortune... after all, it's "Blocks Broken" and not "Blocks Received".


- Virtual Inventory Items from mining... With the player object, not only keep track of blocks mined, but have a virtual inventory to track what they have collected.
   - Hooked up to sellall
   - Have max limits and commands to increase and decrease the total inventory size... could have both "slots" and max stack size per slots.  So that way the players can buy, or rank up those components, just like normal backpacks?
   - Will prevent players from trading and cheating... 
   



* Add support for mineTinker
https://www.spigotmc.org/resources/minetinker-50-modifiers-tools-and-armor.58940/






* Plugin exception handling has problems
  - This is a very rare condition that it happens, but when it does, the global trapping of Prison errors produces a stack trace that is missing details.  Off hand it is not known where that code is.  Need to look in to it and fix it.
  


 

* Auto Features
 - DONE: Add a reload for them.
 - Enable alt fortune calculations as a fall through backup calculation if no fortune is provided for the given block  


* /ranks autoConfigure:
  * Show percent breakdown of blocks
  * Option to name all mines: list of mines.  Permit non A, B, C naming.
  * list world guard commands to protect the world (would then have functional world)
  



* Add a message count to the `/prison debug` features where it will only print a specific number of messages before turning off the logging for those targets.



* Support for Personal Mines:
  - World support - main mines & Personal mines


* World support: Only have prison active in the worlds specified?
  - World-main-mines:
  - World-personal-mines:
  
* Or have mine groups where you can specify one world for a given group.

  
* world specific exclusions:
 - Have a world list that will shutdown prison commands when issued in that world:
   - command handler should ignore those worlds
   - chat handler should ignore those worlds



  
* Ranks have been converted to have all messages moved to files:
* UTF-8 support.
 - If UTF-8 characters do not work, then they must be converted with a tool similar to this website:
 - http://www.pinyin.info/tools/converter/chars2uninumbers.html
 



* Fix per rank progress bars.  This includes adding a new placeholder series since you will have to include the rank name in the placeholder.  Current ladder placeholders are based upon the player's current rank only.  



* BlockEvents - Action based upon blocks broken, not a percent chance.
 - Based upon total count of blocks broken.  Example, after a player breaks 1,000 blocks, then fire a BlockEvent.
 



* global virtual mine:  To apply mine commands & blockEvents to all other mines.



* possible change to /prison version all to include errors during startup.  Errors would need to be captchured.



* To `/ranks autoFeatures` add warnings at the completion identifying that the user must create any needed groups.
 - Note that WG global region needs to have the flag `passthrough deny` set.




Personal mines.  Work in conjunction with a plot world?
- sellable and so would be the features with various upgrades
- Create a new module based upon Mines with new features to support player interactions and upgrades.




  
- /prison utils mining
  - Add XP direct and ORBs




Auto features not working outside of the mines.
- Will not support auto features outside of the mines directly.... too many issues.
- Maybe be enabled and working now?
- This caused issues and may have been disabled.



- Add new placeholders:
  - Top-n - Blocks mined for mines
  - Top-n - Most active mines (based upon blocks mined)
  - Update papi's wiki
  - Track stats on placeholders?  Could be useful in tracking down expensive stats.


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



- DONE: Add blocks mined for players



  
- Review the chat hander in the spigot module. It was rewritten a few weeks ago to fix some issues and to optimize how things are handled.  The issue is that the new code (way of handling things) needs to be extended to other areas.  So review the SpigotPlaceholders class and see how it can be updated.  Then end result will be less code and less potential issues.



- Optimize the handling of chat placeholder.  They will always be the same for the whole server, so cache the PlaceholderKeys that are used.
  - Partially done.  The chat handler was updated, but the change could be pushed in to other existing code to improve the flexibility and resolve some of the weaknesses that may exist.




   
Future Block Constraints:
 - GradientBottom - The block has a greater chance to spawn near the bottom of the mine. 
 - GradientTop - The block has a greater chance to spawn near the top of the mine.


   
   
Maybe: Have an alt block list for mines were blocks that are not actually 
     defined in that mine included there.  This is so air and other blocks that don't find hits can be included with the counts.  Needed for when blocks are changed so it does not lose change status?
     



  
  
- auto features
  - custom list of blocks for fortune



- Issue with /ranks demote and refunding player.
  If the current rank has a custom currency and the player is demoted with a refund,
  the refund is credited to the wrong currency
  - This will be easier to fix once currencies are fixed
 
 
 

* **Custom block issues**
- If CustomItems is loaded successfully but yet not using new block model, show error message
- Show a message at startup indicating that the new block model is enabled or not enabled



* **Prestige Options**
 - Auto Prestige - server setting or player setting?
 - prestigemax - keep applying prestiges until run out of funds
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
   


- Prestiges max - if at max show 100% or Max, etc... Maybe set "max" on a placeholder attribute?


- Add a prestiges config option to auto add a zero rank entry for prestige ranks.

 



* **Combine a few commands & Other short Notes:**
 - DONE: Combine `/mines set rank` and `/mines set norank`
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








* **Update config.yml when changes are detected**
Preserving the current settings, replace the out of date config.yml file with the latest that is stored within the jar.  Updating the settings as it goes.






Enable zero block counts for parent mines.
if 100% block type of IGNORE, then after reset do an full mine air count so zero block reset works. :)




* **When creating a new mine, register that mine with the placeholders**
Might be easier to just reregister all mines?  Not sure if that will work?
Right now, if a mine is added, in order for it to show up in the placeholders, you would have to restart the server so all the placeholders are reregistered.
 * * maybe just automatically run reset when vital elements change? * *



* **Redesign the save files to eliminate the magic numbers***
Most of the save files within prison, for players, ranks, and ladders, are
using magic numbers which is highly prone to errors and is considered 
very dangerous.  Also prison would benefit from a redesign in file layout
to improve efficiencies in loading and saving, not to mention greatly reduce
the complexities within the actual prison code, which in turn will help 
eliminate possible bugs too and give tighter code.



Offers for translation:
  Italian : Gabryca
  Greek : NerdTastic
  German: DeadlyKill ?? Did not ask, but a possibility?
  French: LeBonnetRouge
  Portuguese: 1Pedro ? 
  


<hr style="height:13px; border:none; color:#aaf; background-color:#aaf;">




# Features recently added since v3.2.9



* DONE: Enable and disable confirmations for prestiges
 - DONE: Put in config.yml under the existing: prestiges. settings.
 - DONE: Move the GUI confirm to the same group of prestiges. settings.


 Player Cache possibilities?  
 * DONE: money earned per mine?
 

* DONE: Make ranks more expensive on each prestige....


* NOT-AN-ISSUE: If automanager is turned off, and /prison reload automanager is ran, it will reload the settings, but the event listeners are only registered upon server startup.  So if that condition happens... should display a warning indicating the server must be restarted.
- NOT-AN-ISSUE: Add a warning about event priority changes needing a server restart:
-- NOTE: these are no longer an issue since event listeners are reloaded when the auto features are reloaded.



* DONE **Get new block model working**
  *  Start to enable and test various functions
  *  Add in Custom Items Integration
     *  Code Integration for CI - Key to specific version due to api changes
     *  Pull in custom blocks from CI API
     *  Place blocks with CI api
     *  Not sure how block break would work with CI api?
     *  Setup sellall to work with CI api
     


DONE: - Block constraint error when there are no blocks spawned when applying min to add more...
  - if rangeHigh and rangeLow are zero, or the same, then need to set the high and low values.
  - May need to even set them when there are only a few?
  - maybe rangeHigh and rangeLow should be the actual limits, and not the high and low range of where those blocks actually spawned?  Right now it's actual spawned blocks, but if either, or both are 5000 blocks from the true limit, then the range of actual adjustments is artificially limited and narrowed.
  - Maybe add 2 new fields: rangeHighLimit and rangeLowLimit that is the actual boundaries? These could be used to help normal spawning? 


* DONE: On prison support submit mines, include /mines info a all?

* DONE: To all headers that are displayed in prison commands, show the prison's version to the far right.


* CANNOT REPRODUCDE: The command /mines delete does not appear to be working - User error?


* DONE: Add an *all* for mine names under blockEvent add:

 
* DONE: ladder commands



* DONE: On default ladder, at top rank, if prestige is enabled, then show a message that can be configured.


* DONE: Add hunger to auto features calculations


* DONE: /prison placeholders list - group by placeholder type.


* DONE: Placeholders for items in hand: name, 


* DONE: try out the new event priority code. if it works, apply it to all classes

 Player Cache possibilities:
 * DONE: blocks per mine?
 * DONE: time mining per mine?
 
 
* DONE: Issue with placeholders bars and rankup costs...


* DONE: prison should "scan" offline players upon startup and auto add anyone not already hooked up.  This will help reduce a lot of questions and make the first experience with prison smoother.... 


* DONE: Bug: placeholders are not working correctly when player is offline...
  - Example with the bar graph placeholders.


- blockEvent
  - DONE: simplify add - use common defaults - can change features with the other commands
  - DONE: Add a target block name
  - Not an issue: "Use of placeholders is failing %prison_ is failing on %p"  Turned out they were trying to use %player% instead of {player}.
  



Completed for v3.2.9:


* DONE: BlockEvents - add block filters - tested successfully

* DONE: decay:  Add a util function to respawn the block that was mined... use it with blockEvents.

* DONE: Ladder commands:

* DONE: Delete by line number:


* **Commands - Enhancement**
DONE: Be able to select rank and mine commands for edit and deletion, or even moving, with line numbers.


* **Rank Commands - Edit and delete**
DONE: Add line numbers and enable the ability to edit and delete by line number.


* **Ladder commands - global for all ranks in that ladder**
DONE: Add new placeholders for ladder commands to be able to have generic ladder commands that will apply and be ran for all ranks. May be able to eliminate the need for most rank commands.




**For v3.2.8 Release - - Preparing for Java 16**


   * DONE: Update Gradle to most recent release


   * On going: Review other dependencies and update them too, if possible.


   * DONE: Update development environment, and system to support Java 16


   * DONE: Update XSeries to support the new block types. Updated to v8.0.0 and then v8.1.0.
  
  
   * DONE: Enable the new block model - 73.5% are currently using v1.16.x so with the new 1.17 more people will be expecting the new blocks.
  
  
   * DONE: Auto Refresh for language files when version detects new update is available and if auto update is enabled for that language file.


   * DONE: In /prison version, if auto features are disabled, show NO details.


   * DONE: Confirm duplicates on BlockBreakEvents makes sense.  Fixed the BlockBreakEvents by making sure they all had their own classes so they could be identified as to what they really are.  This cleaned up a lot, and also eliminated one set of duplicates too.


   * Works: This was not an issue. Ranks info raw tag is not showing the editing formats...  The rank I saw this on did not have any formatting.


   * DONE: Fortune on all blocks is not working...

  
   * DONE: Hook up java version on /prison version's plugin listings

  
   * DONE: Issue with nms with SpigotPlayer... does not work with 1.17.  
     - Disabled when fails.  No longer produces stack traces.


**Other additions and changes:**


* **Prestige Options**
 - DONE: Reset money on prestige - boolean option
 - DONE: Prevent reset of default ladder on prestiging.
 - DONE: rankmax - keep applying rankups until run out of funds


- DONE: Update /prison autofeatures to include new settings.


- DONE: Could make /prison autofeatures reload happen. Alias: /prison reload autofeatures


- DONE * Mines TP warmup



- DONE: * Issue with removing a BlockEvent by clicking on the remove link.  It converts all of the & to the raw code.
 - `/mines blockEvent remove` now uses line numbers so this is no longer an issue.
 
 
- DONE - Used load delays - Rare condition caused by FAWEs where prison loads before CMI even though it all configured to do so otherwise.  
  - May be able to provide a way to force the ranks module loading so that way it can just "assume" everything is working.
  - Force the loading of Ranks module if there is a failure.  Found that FAWEs is screwing up the loading sequence of prison when dealing with CMI, and as such, ranks are failing to load since CMI loads after prison.  Setting softdepend for all of these do not help.


DONE * Add `/prison debug` targets so specific kinds of debug messages can be turned on and off so it does not flood the console with tons of irrelevant messages.


DONE * replace ranks with mine related commands with ranks.  
  - can eliminate permissions on /mtp and mine accessperms.


DONE * Add system stats to `/prison version`




DONE: If starting up prison and there are 0 ranks and 0 mines, then submit a delayed job that will run in 10 seconds to print out a message to console on how to run `/ranks autoConfigure` for quick setup... include link to the online docs.


DONE: rank commands - remove by line number
  - DONE: Same for mine commands
 




* **Works fine: Bug in /ranks autoConfigure??:  Confirmed everything is OK... No bugs** 
An admin must have messed up mine A since blocks were gone and other settings were not correct. They could have ran `/ranks autoConfigure force` after creating mine a manually.



* **Rework rank permissions to eliminate need to put perms in rank commands**
  - **NOTE: This was a fail!!**
    - This did not work because:
      1. You cannot access group perms through bukkit
      2. You cannot create new group perms through vault
      3. It's not possible to easily check perms through vault
    - This has been made obsolete with the use of Access by Rank !!
- Enhance the PermissionIntegration abstract class to also work with group perms.
- Add to ranks two new fields: permissions and permissionGroups.  Save and load.
- Add a new boolean field to ranks: usePermissions. Save and load.
- Add support for these perms within rank commands
- Rewrite rankups to use these perms when ranking up, promote, demote, and also for prestiges





<hr style="height:13px; border:none; color:#aaf; background-color:#aaf;">



* **NOTICE - About Prison, Java 16, and Minecraft 1.17**

Prison is all about providing the best experience for your server environment.In order to look toward the future, so we can continue to provide the best possible experience, we have to set limits on what we can actually provide support on.  We are currently entering a challenging period of time where we will have to make some difficult decisions to help ensure the best possible future for Prison.

Minecraft 1.17 is scheduled to be released on June 8th, 2021. The release of 1.17 brings with it some major changes that will have significant ripples throughout the minecraft server community, mostly due to the heavy dependency upon Java 1.8 for many years.  This ultimately means there are a lot of unknowns that we will have to work through and there will be situations that were unexpected.

One major change in our support statement is that Prison, as of right now, will only *officially* support Spigot v1.8.8 (10.9% usage), v1.12 (10.9% usage), and v1.16.5 (73.5% usage). These three versions of Spigot represents 95.3% of all servers using the Prison plugin.  This does not include any server that has disabled the bstats, which is one reason why you should keep that enabled so you can be represented in our stats and our plans for the future. 

Note: The follow up versions in descending order of usage are: v1.14.4 (1.6% usage), v1.13.2 (1.2% usage), v1.15.2 (1.2% usage), and v1.11.2 (0.9% usage).  No other versions are recorded by bStats.

But please keep in mind, as with our official stance of only supporting Spigot, we will also *provide* as-needed support for Paper and other Spigot compatible platforms. We want you to have the flexibility to use the platform that best suites your needs.  We just will not pre-test on those platforms, but we will try to address any issues that are brought to our attention. The non-supported versions of Spigot falls under this same limitation of support: we will not pre-test for the non-supported versions, but if you are using them and find issues, we will work with you to resolve them. The actual impact of dropping support for 1.9, 1.10, 1.11, 1.13, 1.14, and 1.15 should have no impact on quality for those releases of Spigot, but it will free up a lot of time spent on testing.


Some initial testing with running a prison server using Java 16:
* Spigot v1.8.8 will startup and run. But there are major failures with missing classes that are critical for the core bukkit and Java components to work properly.  It is doubtful a fix will ever be provided.
* It has been noted that bukkit/spigot 1.8 through 1.11 will not work with Java 11 and later.
* Spigot v1.12.2 compiled with Java 1.8 appears to work perfectly well with Java 16 running on the server.
* Spigot v1.16.5 compiled with Java 1.8 appears to work perfectly well with Java 16 running on the server.


I have not tried to compile anything with Java 16 since I need to first update the Prison build environment to support it.  But these are tests that will be explored in the very near future so as to get a better understanding on what we can expect from the build process.


In the last few days, I've updated gradle to the latest release so it will support Java 16.  Other resources have been updated too.  More work will be done over the next week or two to prepare for MC 1.17 and Java 16.


NOTE: At this time, I think there will have to be two builds.  One with Java 1.8 for Spigot versions 1.8 through 1.16.5.  Then another build with Java 16 that will support Spigot 1.12 through 17.  This will give admins the flexibility to choose the version of Java to run on their servers that they are most comfortable with.  Initially, these builds will be automated, but initially they may be a manual process.



**Prison v3.2.8 will be targeted to support Java 16, and hopefully, Minecraft v1.17 too.**



Java 1.8 and Java 11 have a built-in javascript engine. Java 16 does not.
This plugin provides support, and compatibility, with javascript processing.
Other plugins that use javascript, such as papi, would have to be compiled
to work with this plugin.  
https://polymart.org/resource/jsengine.1095




<hr style="height:13px; border:none; color:#aaf; background-color:#aaf;">


