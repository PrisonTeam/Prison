[Prison Documents - Table of Contents](prison_docs_000_toc.md)

## Prison Build Logs for v3.3.x

## Build logs
 - **[v3.3.0-alpha - Current](changelog_v3.3.x.md)**
 - [v3.2.0 - 2019-12-03](prison_changelog_v3.2.0.md)&nbsp;&nbsp;
[v3.2.1 - 2020-09-27](prison_changelog_v3.2.1.md)&nbsp;&nbsp;
[v3.2.2 - 2020-11-21](prison_changelog_v3.2.2.md)&nbsp;&nbsp;
[v3.2.3 - 2020-12-25](prison_changelog_v3.2.3.md)&nbsp;&nbsp;
[v3.2.4 - 2021-03-01](prison_changelog_v3.2.4.md)&nbsp;&nbsp;
[v3.2.5 - 2021-04-01](prison_changelog_v3.2.5.md)&nbsp;&nbsp;
[v3.2.6 - 2021-04-11](prison_changelog_v3.2.6.md)&nbsp;&nbsp;
[v3.2.7 - 2021-05-02](prison_changelog_v3.2.7.md)&nbsp;&nbsp;
[v3.2.8 - 2021-06-17](prison_changelog_v3.2.8.md)
 

These build logs represent the work that has been going on within prison. 


*Will continue as v3.3.0-alpha.7 2021-06-?? in the near future.*


# v3.2.10-alpha.4 2021-07-11


* **Clean up Ranks a little more by removing more of the rank position and report failures if a rank is already loaded in another rank to prevent internal failures if a ladder save file is corrupted by manual editing.**


* **Removed the use of rank.getPosition() from all saved information within ranks and ladders.**
Position is no longer used to access any rank. It is only used to compare if one rank is higher or lower than another.  It's lazy loaded only when it is needed the first time. If a rank is removed from a ladder, or inserted in to a ladder other than at the end, then all ranks in the ladder are set to -1 for the position so they recalculate when they are used the next time.


* **This fixes a user error situation, which is not a bug, in the listing of ranks.**
The user copied the contents of one ladder to another ladder, without deleting the source ladder.  Thus there were two instances of a series of ranks, when there should ever be only one instance of a rank.  A rank can be in at most one ladder, or no ladder.  
The result of this issue is that when the second ladder was hookedup at prison's startup, it corrupted the ranks in the first ladder.
This works around such problems by changing /ranks list by using the list of ranks within the ladder, instead of using the rank's prior and next associations.  
These changes also add the rankId to both the '/ranks list' and '/ranks ladder rankList' commands, and also '-' and '+' notations to indicate a rank is linked to a prior and next rank.


* **Simplify the command /mines blockEvent add by removing the permissions and taskMode from the add.**
This was causing too muchh confusion for a lot of people. It should help to keep the add much more basic. There have been alternative commands to modify those settings so it really isn't important to set them on the add.  Also odds are they will not anything but the default values for these two fields.  The defaults are perms = none and taskMode = sync.  TaskMode is not defaulting to inline due to increased risk of lag and prison being falsely blamed for it.  If desired, the admin can always change the taskMode to what they need.


* **Bug fix: Fixed an issue where the "on click" events were using the wrong row number.**
The row number was getting incremented at the wrong time.


* **v3.2.10-alpha.4 2021-07-10 **


* **Few minor tweaks to the mines info command with formatting.**


* **Added player counts to the rank list command.**


* **Hooked up the /ranks list all command to the /prison support submit ranks command.**


* **Add option to /ranks list to include all ranks through the use of the "all" in place of the ladder name.**


* **Fixed a bug where getting a player was returning a null.**
When that null was encountered, it was not processing the placeholders, especially for the bars. 
Not really sure why it was failing, but replaced the use of getting the player balance with the most recent use of RankPlayer's getBalance() works perfectly.


* **More adjustments on the /ranks list to prepare for listing all ladders.**
Also added a couple of more messages.


* **Added an error message if there is a failure when using /prison support submit.**
I assumed that other error messages would exist, but having doubts if it always will have other errors displayed.


* **Improvements to the /ranks list command.**
Enhanced the header so it's clear which ladder is involved.  Also cleaned up the formatting with the tag colors so it does not impact the column alignments. Also doing a little more with injecting the "default" tag so it does not shift the columns.


* **Adds the listing of all mines to the /prison support submit mines command.**
This provides an overview of all mines and is included at the top of the listing.


* **Improve the layout of all of the mines with the command /mines list all.**


* **pasteChat update to keep the color codes.**  It will only keep the color codes that start with &, and all others will be removed.


* **New feature: /prison reload locales.  Its now possible to reload the language files that prison uses.**
Added a new debug item to test to see if utf-8 is working:  /prison debug testLocale.  No, it does not work with utf-8 text yet.
I did confirm that if the properties files are setup with UTF-8 and are a part of the prion jar, that the UTF-8 encoding will properly be saved to the local file system.  
The area that I think is causing failures is with the Properties object itself since that is not utf-8 compatible, so it appears.


* **v3.2.10-alpha.3 2021-07-09**


* **reformat the /mines list command to mak it easier to read**


* **Added support for prison support submit for ranks and ladders, and also for mines.**
These two new commands will submit the raw json files.


* **New Feature!! Major support feature!  Send all of prison's configs to paste.helpch.at so we can know exactly how servers are configured.**
This logs the following config files: config.yml plugin.yml autoFeaturesConfig.yml modules.yml module_conf/mines/config.json 
      SellAllConfig.yml GuiConfig.yml backpacks/backpacksconfig.yml


* **Added a success message when a player is added to prison's default rank.**


* **Fixed issue with a few placeholders that were not working.**
Was using the wrong translator so a number of mine related placeholders stopped working.  
The mineplayers were not working too.  The bug was introduced by hooking up STATSMINES.


* **Hooked up STATSRANKS placeholders and got rid of obsolete ones.  Still more testing needed.**


* **Added a player check on prison startup that will add all offline players that are not hooked up to prison yet.  Plus it will register name changes too.**


* **Added top rank balance placeholders: 6 for now and more will be added soon once these are fully working.**
These are based upon the player's balance within the rank.
Also added a Hesitancy Delay Penalty to help encourage players to rankup instead of trying to dominate a single rank.
These have not yet been tested.


* **Added RankPlayers to Ranks.**
This will allow quicker access to all players at each rank, which will help for top ranking reporting.


* **Clean up some rank and ladder function to eliminate the use of magic numbers: both rank ids and ladder ids.**
These were bad, since the objects were already linked together, so using these functions were just adding unneeded overhead.


* **v3.2.10-alpha.2 2021-07-06**


* **Added the ability to fail silently for the Localizable object to prevent the message ID from flooding certain messages.**
This will be employed in the Output.get() functions so if it cannot load the prefixes and level colors, then the message IDs will not be injected in to the messages.  
This new feature should never be used without careful consideration since it is important to identify when messages fail so they can be fixed properly.


* **Adjustments to LocaleManager for updating the internal checks for if the external files need to be updated.**
Also finalize how Prison object works with the LocaleManager.


* **Removed obsolete placeholders that are not used anymore.**


* **Adjustments to when the LocalManager is started so it can be activated as soon as possible.**
This is because of the need for Output.get() to use externalized messages for it's prefixes and color codes.  This does present some problems, but it actually works fine at runtime and also for junit tests too.


* **Setup a getConfigString in the Platform with a default value.** 


* **New placeholders for mine block stats: Renamed percent to chance. Added prison_top_mine_block_remaining_bar_nnn_minename.**


* **Used the wrong numeric formatter... needed to be an integer.**


* **Added 10 new placeholders to provide stats on mine blocks.**
These use a series value in the placeholder to specify the specific block.  The series number is not limited and can range from 1 through a large reasonable value.  The number can be one or more digits, with or without a left padding zeros.


* **Renamed the field PrisonBlockStatusData.getResetBlockCount() to getBlockPlacedCount() to be more descriptive of what it actually is.**


* **Add the ability of a PlaceholderFlag to self identify as having a sequence.**
This will allow for automatic processing of these kind of placeholders without having to hard code for them.


* **Placeholders test: Fixed issue with player name being incorrect and also added player information if it is provided in the output.**


* **Fixed a typo that was made a long time ago.  Placeholder has a capitol H in the name.**


* **Increased the number of rows displayed when using the placeholder search.  It was showing six placeholders and I increased it to 10.**


* **Renamed the PlaceHolderFlags from PLAYERMINES to MINEPLAYERS which is more consistent in how it is being used.**


* **v3.2.10-alpha.1 2021-07-05**


* **Bug fix... when selling on each block break event, it needs to suppress the per-sale messages.**


* **Fix a few other issues with the new placeholders.**


* **Enhance the /ranks autoConfigure to work much better with existing mines and ranks when doing a force.**


* **New 18 new placeholders added to prison.  Hooked up the following placeholders, but have not yet ran through testing yet.**
prison_rank_ladder_position (prison_rlp), prison_rank_ladder_position_laddername (prison_rlp_laddername), 
prison_rank__ladder_position_rankname (prison_r_lp_rankname), prison_rank__player_cost_rankname (prison_r_pcst_rankname),
prison_rank__player_cost_formatted_rankname (prison_r_pcf_rankname),   prison_rank__player_cost_remaining_rankname (prison_r_pcf_rankname),
prison_rank__player_cost_remaining_formatted_rankname (prison_r_pcf_rankname),  prison_rank__player_cost_percent_rankname (prison_r_pcp_rankname),
prison_rank__player_cost_bar_rankname (prison_r_pcb_rankname)


* **Refactored ranks and RankLadders to eliminate old and inefficient ways to get the ranks and next ranks.**
These change will help improve the performance of processing the placeholders.
This also allows the elimination of a few functions that are now obsolete.


# v3.2.9 2021-07-03

- release v3.2.9


# v3.2.8.1 2021-06-18


* **Note: Bug fixes for 3.2.8.**

* **Fixed a failure on startup for new installations of prison.**
Basically it was unable to deploy the language files due to try-with-resources closing the initial zip connection.


# v3.2.8 2021-06-17

Prison V3.2.8 Release!
Prison now fully support Spigot 1.17 and Java 16!


**NOTE:** Since the start of the development on v3.3.0, Prison has had a few other releases under v3.2.7 and v3.2.8.  The reason for these releases is that the major structures (and code) that would make prison v3.4.x, are not complete.  Therefore, to get out new updates sooner than later, v3.2.7 and v3.2.8 have been release.


* **Released v3.2.8!**


* **v3.2.8-alpha.3 2021-06-16**


* **v3.2.8-alpha.2 2021-06-12**

* **Spigot 1.17 release - v3.2.8-alpha.1 - 2021-06-11**
Only known issues: 
   * Unable to use nms to get the player's preferred language

* **v3.2.8-alpha.1 2021-06-07**
Internally set the version, but will not release it until a few other things are finished.
The prison version is set to 3.2.8-alpha.1 to prepare for the release of prison that is compatible with Java 16 and Spigot 1.17.


NOTE: v3.2.8-alpha.1 is identical to v3.3.0-alpha.6.  V3.3.0 is far from being ready to be released.  So v3.2.8 will enable Java 16 and also Minecraft 1.17.


# v3.3.0-alpha.6 2021-06-07


* **v3.3.0-alpha.6 2021-06-07**
Setting the version.  The v3.3.0 release will be put on hold since focus will be to get v3.2.8 out which will support Java 16.  It is unknown how many of the spigot 1.17 blocks will be initially supported.

* **v3.3.0-alpha.5c - 2021-06-06**

* **v3.3.0-alpha.5 2021-06-01**

* **v3.3.0-alpha.4 2021-05-15**


* Next release will be v3.3.0-alpha.3
Please note that the correct order of releases have been: 
v3.2.6, v3.3.0-alpha.1, v3.3.0-alpha.2, v3.2.7, v3.3.0-alpha.3


# v3.2.7 2021-05-02


* **Set version to v3.2.7**
  - Note that all changes that were made under v3.3.0-alpha.1 and v3.3.0-alpha.2 have been publicly released under v3.2.7


* **3.3.0-alpha.2 2021-04-23**


* **v3.3.0-alpha.1 2021-04-16**


* **v3.3.0-alpha.0 2021-04-11**

  Start on the alpha.1 release.
  
