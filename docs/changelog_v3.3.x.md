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


# v3.2.9-alpha.2 2021-06-28


* **Adjustments to the blockEvent block remove.**


* **More work on adding support for block filters on mines blockEvents.**
Got the commands working for adding and removing blocks to and from a blockEvent.


* **Enhancements and fixes to /ranks autoConfigure.**
Fixes an issue when trying to use force where it was not working.  It now works.
Provided new behavior of when forcing it will try to fully use what you already have, both with ranks and mines, but all mine that have blocks will be replaced.  If mine liners already exist, they will not be replaced.
Added a new option to `forceKeepBlocks` that when mines already exist, it will keep their blocks.


* **Setup blockEvents to be able to search for blocks to be added.**
Unfortunately, this turned out to be the wrong approach and is too complicated.  I just thought of a better way to do this.
Committing this code since some of it does improve a few things not directly related to blockEvents.


* **Changed around how the startup of prison works.**
This helps to eliminate a couple of error messages when enabling the compatibility classes.
Moved the display of the system stats to the Prison class so it can be now included with the prison splash screen.  This is important to get displayed here so if there are other failures, at least we have that covered to help with troubleshooting.


* **Update docs and provided a new startup task that runs when there are no mines or ranks on a server.**
The new task will print out a few messages in the console and provide links for additional information.  This is to help new admins of Prison figure out how to get it configured quickly and where to find help.


* **Added a few more rank messages for the ladder commands and fine tune how they work.**
Changes to the list commands for ranks and ladders to suppress the option to remove the commands.  The /ranks info command will now suppress the removals.


* **Hooked up ladder rankup commands to the rankup processes.**


* **Setup the ladder messages for the ladder commands.**
It was using the rank command messages.


* **Hook up the ladder rankup commands to the commands to list, add, and remove commands from the ladder.**
These work, but currently use the rank messages and not ladder specific messages.


* **Added rankup commands to the ladders objects.** 
This sets up the loading and saving of them.


* **In the LadderManager is a function saveLadder that should not be used outside of the ladder manager so it's visibility was set to private.**
As such, many external functions had to be changed to the save() function.


* **Converted mine commands to use row number for removal.**


* **Fixed an issue with rank commands removal with a line number... should have subtracted one from the row number.**


* **Found an issue with lore value of Block hitting upon any lore that begins with Block, such as Blocks Mined:**
To fix this issue, the default lore has been changed to include prefixes of "Auto ".


* **Found bug with lapis_lazuli having a 1 to 1 auto block to lapis_block.**
Should be 1 to 9.


* **GUI utility, started transition:** GUIs are slowly being rewritten to the new GUI
utility, this will improve performance and make GUIs more stable in the future.



* **v3.2.9-alpha.2 2021-06-24**
Bump to alpha.2.


* **Bug fix: Found a rank message that was missing the second parameter.**
This was with the command /ranks command add when the command was a duplicate of a preexisting command.


* **Updated some of the comments on how to manually specify the java location to control what version of java is used (this is like hard coding).**
It is not advisable directly change the actual build scripts since they will cause failures on other platforms.


* **Changed rank commands to use row number for removal**, instead of having to enter the exact command.
Entering the command has problems when formatting is used.


* **Later versions of Java required that the getters and setters for the handlers exist.**


* **Fixed an invalid use of an Intger object.  It was deprecated and removed.**


* **Found a statement that had a parameter on a String.format when it shouldn't have.**
This did not produce any errors; it was just incorrect.


* **Getting ready to add a new debug feature to test the BlockBreakEvent processing by going through all registered event listeners one by one and see if they handle the event.**
The new feature, when done, will list what happens with each listener, and report the state of the block between each one.  
The mine will be a parameter so you can test how it works within a specific mine without having to have an online player look at a block, like how wg does it with their command /wg debug testbreak.


* **Added a new {firstJoin} placeholder for the rank commands.**
This will only be activated upon a first join event, therefore it can only be added to the default rank on the default ladder. If added to any other command on any other ladder or any other rank, that command will always be ignored.
The use of this new placeholder allow special processing for new players only and prevents th need for other plugins just to provide On Join processing.


* **Fixed the placeholder prison_rankup_cost_remaining_bar_laddername to remove a numeric value that was being prepended to the bar graph.**
It was left over from the percent function and should have been removed before.


* **The RankPlayer's addRank() function was changed to eliminate the ladder parameter because the ladder "should" be part of the Rank object anyway.**
Having ladder was redundant.


* **A little more advanced way to delay the startup of prison.**
This change requires that vault be present and active, then also that the VaultEconomy is also active.  It then is able to validate the economy plugin that is actually active to confirm that CMI Economy is loaded and hopefully active. 
It is hoped that this will allow a better control starting up prison.


* **Set the next version to v3.2.9-alpha.1  2021-06-19**


* **Fix a potential NPE. Suspect the new value of Y is too low and is result in no block.**
Cannot check the y value for a lower limit since future versions of minecraft will allow for negative values of y, and values greater than 255.  So having to check when a block cannot be resolved, which is not too specific.


* **Reworked some of the event listeners to eliminate duplicates and to pull some out in to their own classes so it is clear what they are listening for.**
Zenchantments has been fixed for when auto features is off.  It did not have any listeners setup for the normal drop processing.
Moved around some of the config settings for the autoFeaturesConfig.yml to group them better and add a processing control setting for zenchantment.  
Set the default value for enabling auto manager to true so it will be on by default now.


* **Bug fix: Issue with non-plugin jars being scanned, which was resulting in a null value for the plugin names.**
Changed the code to bypass all jars that are not registered (has the signature of) as a bukkit plugin.
This bug prevented prison from starting up, so this is a very serious issue.


* **Rework how some of the BlockBreakEvents are setup to reduce unused and duplicate listeners.**
Changed the defaults for the three enchantment plugins to be DISABLED by default.  This will help reduce listeners when these plugins are not being used.


* **Suppress the auto manager details in `/prison version` if auto manager is disabled.**
Displaying this information would only cause confusion since the setting are ignored, but could imply they are active since they are shown in this command.


# v3.2.8.1 2021-06-18

* **Apply fixes to the v3.2.8 release**


* **Check for null and an empty string in the parseInt() function for the /prison utils.**


* **Expanded the help information on the commands for /prison utils heal, feed, and breath to explain how amount works.**


* **Pull in the changes to bleeding with the new amount fields for /prison utils heal, feed, and breath.**
Without amount being specified it will restore full healing, feeding, breathing.  If an amount is entered with a "+" then it will add the amount to the current health levels, or if "-" then it subtracts from the current health level.  If a value is supplied without either "+" Or "-" then it will set it to that value.


* **Note: Bug fixes for 3.2.8.**

* **Fixed a failure on startup for new installations of prison.**
Basically it was unable to deploy the language files due to try-with-resources closing the initial zip connection.


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
  
