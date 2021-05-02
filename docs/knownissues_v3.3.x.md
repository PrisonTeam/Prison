[Prison Documents - Table of Contents](prison_docs_000_toc.md)

# Prison Known Issues and To Do's for v3.3.x


DONE: * Issue with removing a BlockEvent by clicking on the remove link.  It converts all of the & to the raw code.
 - `/mines blockEvent remove` now uses line numbers so this is no longer an issue.
 

* Rare condition caused by FAWEs where prison loads before CMI even though it all configured to do so otherwise.
  May be able to provide a way to force the ranks module loading so that way it can just "assume" everything is working.
  
  
* UTF-8 support.


* Force the loading of Ranks module if there is a failure.  Found that FAWEs is screwing up the loading sequence of prison when dealing with CMI, and as such, ranks are failing to load since CMI loads after prison.  Setting softdepend for all of these do not help.


* Sometimes Player Ranks lores placeholders from placeholderAPI aren't working, 
it's unknown why it's happening.


* Fix per rank progress bars.  This includes adding a new placeholder series since you will have to include the rank name in the placeholder.  Current ladder placeholders are based upon the player's current rank only.  


DONE * Mines TP warmup


* BlockEvents - Action based upon blocks broken, not a percent chance.
 - Based upon total count of blocks broken.  Example, after a player breaks 1,000 blocks, then fire a BlockEvent.
 

 
* ladder commands


* global virtual mine:  To apply mine commands & blockEvents to all other mines.



* possible change to /prison version all to include errors during startup.  Errors would need to be captchured.



* To `/ranks autoFeatures` add warnings at the completion identifying that the user must create any needed groups.
 - Note that WG global region needs to have the flag `passthrough deny` set.




Personal mines.  Work in conjunction with a plot world?
- sellable and so would be the features with various upgrades
- Create a new module based upon Mines with new features to support player interactions and upgrades.


- Hook up block filters on the block events.

  
- /prison utils mining
  - Add XP direct and ORBs




Auto features not working outside of the mines.
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



- Add blocks mined for players



  
- Review the chat hander in the spigot module. It was rewritten a few weeks ago to fix some issues and to optimize how things are handled.  The issue is that the new code (way of handling things) needs to be extended to other areas.  So review the SpigotPlaceholders class and see how it can be updated.  Then end result will be less code and less potential issues.

- Optimize the handling of chat placeholder.  They will always be the same for the whole server, so cache the PlaceholderKeys that are used.
  - Partially done.  The chat handler was updated, but the change could be pushed in to other existing code to improve the flexibility and resolve some of the weaknesses that may exist.



  
- Update /prison autofeatures to include new settings.



- Could make /prison autofeatures reload happen. Alias: /prison reload autofeatures



   
Future Block Constraints:
 - GradientBottom - The block has a greater chance to spawn near the bottom of the mine. 
 - GradientTop - The block has a greater chance to spawn near the top of the mine.


   
   
Maybe: Have an alt block list for mines were blocks that are not actually 
     defined in that mine included there.  This is so air and other blocks that don't find hits can be included with the counts.  Needed for when blocks are changed so it does not lose change status?
     




- blockEvent
  - simplify add - use common defaults - can change features with the other commands
  - Add a target block name
  - Not an issue: "Use of placeholders is failing %prison_ is failing on %p"  Turned out they were trying to use %player% instead of {player}.
  
  
- auto features
  - custom list of blocks for fortune



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
   


- Prestiges max - if at max show 100% or Max, etc... Maybe set "max" on a placeholder attribute?

- Add a prestiges config option to auto add a zero rank entry for prestige ranks.

 


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

* **Rank Commands - Edit and delete**
Add line numbers and enable the ability to edit and delete by line number.





* **Update config.yml when changes are detected**
Preserving the current settings, replace the out of date config.yml file with the latest that is stored within the jar.  Updating the settings as it goes.


* **Ladder commands - global for all ranks in that ladder**
Add new placeholders for ladder commands to be able to have generic ladder commands that will apply and be ran for all ranks. May be able to eliminate the need for most rank commands.





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
  




# Features recently added:





