[Prison Documents - Table of Contents](prison_docs_000_toc.md)

# Prison Known Issues and To Do's for v3.3.x

These known issues are an informal list of concerns and bugs. Also includes some wish-list items too.

These are notes for personal references.  These are not intended to explain anything useful, but they do track some issues and perspectives that may not be fully expressed in the change logs because initial impressions may not turn out to be the actual problem or the final course of changes and enhancements.


Resolved issues have been relocated to:
* [Known Issues - Resolved](knownissues_v3.2.x_resolved.md)


# TODO Items for v3.3.0-alpha.15


- Reported that a freshly placed mine would not work with an /mtp command - noza3
  (marked as done below since it could not be reproduced, but should test with a new build)
  

- Mine bomb save files - the name used for the data structure (in the hash) is ignored when loading? - FqdedStqr
  - He changed that name and it caused problems.  Maybe change the bombs to just an array?


- Prestige confirmations are not working - Axar
  - Cannot prestige without a confirmation too.
  

- Auto pickup is not working when OP'd in creative mode vs survival mode. - Axar


- New alpha release for alernos - drops and sellall are messed up - notify when published to spigotmc.org - slab


- break one block but blocks counts register 2 - Aimatt

- On the new cmdStats details, add nano run times for commands so we can include avg run times.


- File save to new format - OmarG
  - pre-req for turning off TP
  - pre-req for custom mine shapes
  


- Ability to turn off TP on mine resets.  OmarG
  - Maybe have a special value for spawn to indicate it should TP?
  - Could still have a spawn point, but just not tp during resets.


- Custom mine shapes - Chain / darragh
- custom mine shapes - Chain and Fiba1 and OmarG
 - Have a new command to edit and save the mine's shapes
 - edit would spawn red/yellow wool - remove blocks - save would only save the wool blocks.
 



- when testing the block breakage with the SHIFT-click with the mine wand, I also saw that it processed the same block twice.  The first time looked as if the drop was being canceled, then the second time the event was being canceled.  Or vice-a-versa.  I don't remember setting it up both ways.
 - NOTE: This is an issue with Google Guava. I am not sure why it submits all listeners twice, but it does.


- `/ranks player` is not including all of the info for rank multipliers like it should have.
  - add them
  - List total rank multiplier. Show the multiplier per ladder.
  
  




- admin - prevent you breaking mine liners - me... on other people's servers. lol


- It looks like the following text field is no longer being used, but it should with the placeholders `prison_mines_timeleft_minename` ... looks like it's using the long version.  May want to provide a short and long format?  Long (the current default) may be way too long...
  `core_text__time_units_short=y,m,w,d,h,m,s`

- There is a potential error with the class tech.mcprison.prison.util.TextMessage in that all of the messages are using `.withReplacements( "%s" )` but yet none of the text components are using parameters.  So if a parameter is added to any of those, the `%s` could potentially cause errors!?  Need to test and if errors, then remove all of the `.withReplacements()` in that class.







- For v3.3.0 release:
  - BlockConverters
    - docs
  - Sellall - use prison blocks and not XMaterials - Move more code to the sellall module
  - Backpacks - create a new player cache for backpacks.  Copy the current player cache.
  - ranks and ladders
    - auto prestiges (unlimited) based upon formulas
    - new file formats - json ORM?
  - DONE using topn: archiving old players
  - DONE: more work on top-n rankings
  - /mines wguard - worldguard hints for mines (generate scripts to run?) - Use patterns that can be edited


* BlockConverters 



* placeholder bar debug is not showing anything useful.


- New custom block support: Based upon holding an item or a block. `/mines custom <block/item> <namespace> <hand> <value>`
  - Way to add custom blocks outside of the CustomItems plugin.  Capture NBT values too?  Not 100% what can easily be captured, or how to always id a custom block; has to be fast for block break handling.
  
  

- Transaction logs - Rankup and player joins would be good to take these messages out of the console.




- Print warnings if auto features configs prevent any drops. Include notice when drops don't occur due to autosell.
  - On server startup... not sure how to best check.


- auto pickup off, nothing was dropping.  LurgenAU



- autofeatures BLOCKEVENTS priority - include backpacks on auto sell - Ryankayz


- TopN for tokens - Phoung Nguyen


- (done ??) auto features autosell - tie to sellall's player toggle - Ryankayz

- SQL support - Six




- Mine Resets - Glass block not being removed - harold




- Placeholders - dynamic content - 
  - custom placeholders based upon other primary placeholders?


* Add a preformatted sellall multiplier
* {prison_player_sellall_mutiplier::nFormat:#,##0.0000}


- GUI forces admins to run auto configure.  Review and remove?


* messages - split on \n character to multiple lines
  - player messages
  - console messages
  

* Mine bombs:
  - glowing effect (enchantment)
    - give madog24 stone{Enchantments:[{}]}
    - ItemMeta.addFlags(ItemFlag.HIDE_ENCHANTMENTS)
  - throwing... as in egg or ender pearl
  throwableEnable: true,
  throwVectorVelocity: 1,
  thrownInstantExplode: true,



* HiPriority: calculated infinite prestiges - Fluffy_ak47

* HiPriority: sellall for custom items



- In mines block list, new feature to prevent drops for that item... which then can use blockEvents, but that would bypass auto pickup and autosell. 


- update LP docs with more info...
 -  https://discord.com/channels/332602419483770890/943874819101982790/943905937641574420



- %prison_rrt% (prison_rankup_rank_tag) Does not show the next prestige rank if you are not on the prestige ladder yet


- Prison GUI modifications to allow customizations
  - Notify Nick1 when changes are made
  
  


- custom block support: Items Adder - No one is using it currently.

- add `/mines set rank *all*`
- add `/mines set mineAccessByRank *all*`
- add `/mines set tpAccessByRank *all*`
- add `/mines set resetTime *all*`


- custom blocks not working with sellall.   Sellall is not honoring the custom block's names.

- Add ItemAddr for another custom block integration

- Smelt and blocking: rewrite to provide a list of conversions to eliminate current hard coding.

- Auto smelt is missing some blocks?  Symadude23

- placeholder attributes: Add overrides for "units".  kstance requested it for time, such as h,m,s...



- Need to get CustomItems working with sellall

- sellall get player multiplier needs to be rewritten - cached?  Currently goes through all perms

- automatic prestiges

- Docs:
  - finish luckperms doc
  - Placeholders details - Explain each placeholder

- Rankup commands: placeholders for {promote}{demote}

- Mine reset notifications logging to console - options? 



* Problems with blocks:
 - Sand or any other block that falls is no longer in original location so cannot break it. 
   - Might have to tag the blocks with NMS?
   - Just fixed an issue with a block that was in a mine with gravel, sand, and dirt.  MIght have been sand causing the error.  NOTE: prevent error, not fixed the actual problem.

 
- Problem with actionBar - messages are not going through.
 - sellall updates every second - different messages - but never show first one - PlayerMessages



 
 - block stats based upon drops instead of breakage?? (not sure if this has merit?)
 


-> Support for eco enchants:
  - Need to add an event listener and then have a new function in EnchantmentUtils handle the event, with passing continuing to call the normal rehandleBreaking.  Maybe name it rehandleBreakingEvent?
https://github.com/Auxilor/EcoEnchants/blob/master/eco-core/core-plugin/src/main/java/com/willfp/ecoenchants/enchantments/ecoenchants/normal/BlastMining.java
https://github.com/Auxilor/EcoEnchants/blob/master/eco-core/core-plugin/src/main/java/com/willfp/ecoenchants/enchantments/util/EnchantmentUtils.java



* calculate mine worth?


* ShiftAndRightClickSellAll is not working

* Found a problem with mcMMO, Quest, and EZBlock support... only works on BlockBreakEvents.  I added logging to identify when they are called, but if an explosion has 20,000 blocks, then it will log 20,000 times!  😂  So I need to figure out something before hooking it up to multi-block breaks.


* sellall - ladder based sellall rank multipliers 
  - so a ladder value of 0.05 would apply p1 = 1.05, p2 = 1.10, p3 = 1.15, etc...
  

* auto run autoConfigure upon startup.  Use autoStart as the base configuration for prison instead of nothing.
  


# Completed tasks



<hr style="height:13px; border:none; color:#aaf; background-color:#aaf;">


