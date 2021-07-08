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


# v3.2.10-alpha.2 2021-07-08


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
  
