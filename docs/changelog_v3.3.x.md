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
[v3.2.7 - 2021-05-02](prison_changelog_v3.2.7.md)
 

These build logs represent the work that has been going on within prison. 



# v3.3.0-alpha.5 2021-06-01

* **More adjustments to the rank messages.**
Added a few more messages so incremented ranks_messages__version=2.
Moved all code dealing with messages to a Messages class by the same name.  This will keep the actual code much cleaner.


* **Refactoring ranks module commands to put the external messages within messages classes since it cleans up a lot of code since the messages are messy (multi-lined).**


* **v3.3.0-alpha.5 2021-06-01**


* **Added a ranks_rankup__version number to the en_US.properties file so as to help track future changes.**
At this time, nothing is automated, but this is a manual verification version id.


* **Reposition the location of the durability debug message.**
This allows only constructing it when the debugging messages are enabled for this target type which will reduce unneeded overhead when debugging is turned off.
Also repositioning the message allows more information to be included so it's actually more useful too.


* **Externalized messages for the RanksCommmands.**
All of the details with the messages has been put in to the new class RanksCommandMessages.
Moved some unused RanksCommands to the RanksCommandsPerms class.  May eliminate them, or hook them up later.  Probably eliminate them.


* **Updates to a few different documents.**


* **Changes to the /prison debug options by enabling the use of DebugTargets so as to only turn on specific debug logging entries within prison, instead of all of them. ** 
This will give a lot more control over helping players figure out what's going on.
When using the `/prison debug` command, it will show the current status and all available debug targets. Can use the key words of 'on' and 'off', or without them, everything is just toggled on and off.  If some targets are enabled and then the global commands are used, the targets will be cleared.  So if `/prison debug blockBreak` is enabled, then no other debugging messages are shown.  But then if `/prison debug` is issued, then that blockBreak target is removed, but the global debug is enabled.  Then a second use of `/prison debug` will turn off all logging.  Of course instead of cycling through the gobals, the player can also turn off **blockBreak** logging with any of the following: `/prison debug blockBreak` (toggled), `/prison debug off blockBreak`, or `/prison debug blockBreak off` since position of **off** does not matter.


* **Had to redo how crazy enchants were being initialized.**
Need to get everything in the try catch for overall safety, but had to add NoClassDefFoundError too.
This prevents this from failing when CE is not installed, but when it is, it allows the player to pickup the pickaxe enchantment experience bonus.


* **Adjustments to the calculate bukkit extended fortune.**
Got rid of the int values and are now using doubles in the calculations so the results are finer grained and actually have a better chance of varying.
Also added a debug statement to print out details when debugging is enabled through /prison debug toggling.


* **More adjustments to auto features, but this time focusing mostly on fortune.**
Reordered the fortune settings to now be within a group.  If it is disabled, then none of the related fortune calculations will be performed.
Under /prison version all, added information to better explain what's controlling other settings.  For example if one setting if enabled causes another setting to be disabled, it will show that the setting is disabled.
This should help inform the users how fortune works and what is controlled by the settings.


* **Some significant changes have been made to the names and ordering (nesting) of the autoFeaturesConfig.yml file.**
The auto features is being changed around to add more granular control over how auto features work, and to fix some odd behaviors.  As such, many of the configuration names have been changed and old configurations will be reset/lost.  
So if auto features were turned off, the will have to be turned off again within the config file after upgrading.


* **A few changes to automanager in how and when it processes the block breaking events.**
Auto smelt and auto block now only is processed right after auto pickup happens, and before the items are placed within the inventory.
If auto pickup is turned off, then auto smelt and auto block is off by default too.  There is a auto manager normal drop processing that has smelt and blocking before the items drop.  They can enable those features if they need them.
Fine tuned how fortune is applied to both auto pickup and normal drops.  Moved around a few things so they may behave more naturally, and hopefully won't get messed up by having drawn the wrong conclusions.
Starting to work on documents to better describe the chain of events and processing so it make more sense on how the settings work in conjunction with each other.


* **Fix issue with crazyEnchants generation of bonus xp.**
Had to check if the ce class exists, if not, then it disables the processing.


* **Changes to /ranks autoConfigure to configure tp and mine access by ranks.**
These are the new preferred ways to configure TP access and also grant access to the mines.  Permissions are still an option, but are more complex for the users to setup.  This will require no effort, but will just work without needing any external plugin support.
Since access by rank is now standard, auto configure no long will generate ANY permissions rank commands.  If a player needs them, then they can manually add them.  Soon, ladder commands will be added so at that point they can setup one ladder command to apply to all ranks on that ladder.
Eliminated the /ranks playerInventory command.


* **Externalized the LadderCommands.**


* **Fixed an issue with mine block commands not working since the functions were moved to another class.**


* **CrazyEnchant was having similar issues as papi was in that the prison instance of CrazyEnchant (what was used to build prison) was resulting in nulls when trying to use that plugin within prison.**
So changed the way CrazyEnchant was used in gradle as compileOnly, but this resulted in numerous error message about the BlastUseEvent could not be registered since it did not exist.  The odd thing was, that even when preventing registration, somehow it was still getting to be registered.  As such, the only way to prevent registration was to "hide" the event in a plain Object in the parameters of the core classes, then cast them to the correct class once inside the functions.  This solved the problems but appears to be a strange way to deal with it.


* **Fixed an issue with using just /mtp with no mine name.**


* **More changes for access by rank.**
Moved the logic for identifying if a player has access to a given rank to the RankPlayer object.  Also set it up so it is checking the correct ladder based upon the supplied targetRank. 
Hooked up miningAccessByRank for block events.  Tested to confirm accessPermissions and tp perms still work for mines that are not tied to ranks. 


* **Fix a bug in searching for rank dependency.**
This is currently being used for /mines tp if tpAccessByRank is enabled.


* **Hook up the commands /mines set mineAccessByRank and /mines set tpAccessByRank.**
Tied it in to a few other commands such as /mines tp and /mines set accessPermission to indicate that the preferred way to access these commands are by rank.
If mineAccessByRank is enabled, then it will disable the /mines set acessPermission and will not even show it's value if it was set previously.
May not be fully functional and needs some testing.


* **Refactored some of the Mine commands to better organize them and to reduce the number of lines of code in one source member.**


* **Bug Fix in Gradle config: Issue with using the gradle implementation directive instead of compileOnly.**
The issue was presenting itself with PlaceholderAPI not working when registering prison's placeholders.  This was caused by using implementation which was causing the classes to be compiled with prison, which caused issues when spigot/paper was recently released.  I suspect it had to do with different class loaders trying to load the same classes.  But by changing over to compileOnly the classes were not included with prison and therefore were not causing conflicts when running with multiple class loaders.
There is a strong chance that luckperms could possibly have the same issues, so changed that too since luckperms was not being shadowed to prevent the conflicts.


* **Save and load Mine's MineTypes, tpAccessByRank, and mineAccessByRank.**
These still are not hooked up to the user options and commands so they cannot be setup yet, but they are functional.


* **Slight change to the way the info is printed for /prison version to add periods in the leading spaces that defined indentations.**
This is done to prevent the gaps from being eliminated in the log files on some environments.


* **Bug fix: gradle option of compileOnly corrected a new issue with placeholderapi where two different placeholders were trying to load the same class, but yet the results were producing unexpected results.**


* **Rewrite of the /mines tp to hook up the new functionality of rank access.**
Also simplified how the tp commands are handled to reduce some of the complexities.


* **Setup new fields within the Mines to provide access by rank control, and to start to setup the use of mine groups and even mine types.**
None of these have been hooked up yet to be saved or loaded.  None have been hooked up to configs through commands yet.  But basic behavior with these new settings will be functional once they are enabled.


* **Setup the platform to be able to check if a player has a rank, or if that rank is a lower rank on that ladder.**
The assumption is that if a player has a rank that is higher than the one being tested for, then they would still have access to that rank.
This allows for the elimination of using permissions for items like TP or mining access.



* **Added prison file stats to the /prison version command.**
Now lists how many files, folders, and total file size that prison is using within the plugins/Prison/ directory.


* **Added the Java Version to the /prison version to work with the other system stats**


* **Add some system stats on cores and memory.**
Could be helpful on troubleshooting performance issues.


* **v3.3.0-alpha.4 2021-05-15**


* **Finalized moving all messages within the ranks module to a file.**
If rank related messages are not being displayed, but instead a code is being shown instead, then you will need to delete the following folder on the server and then restart prison to get it to regenerate with the latest messages.  If you made customizations to the files within this directory, then please back it up before deleting it, then copy them back in to the new files.
`plugins/Prison/module_conf/ranks` 


* **Added more rank related messages to the external file.**


* **Fixed a bug with getting the position of the rank.**
For example if trying to get the last rank, it would cause an index out of range exception.  For example if there are no ranks but yet trying to select the first rank, it will not work.


* **Slight changes to the GUI in accessing the auto features settings in a more direct way.**


* **Fix access to auto features if auto features is disabled.**
If auto features is disabled, then it will not be instantiated.  This may cause issues with the GUI where it expects it exists.  This change will create an instance so the GUI will have access to it.


* **Attempting fix of NPE for AutoFeatures GUI.**
Potentially fixed NPE for autofeatures.


* **Setup about 22 new messages for ranks so they can be externalized.**
They are related to rankup commands.
Since ranks did not have any messages before, the language file will be generated when starting the server.
Changed the version to v3.3.0-alpha.3b just to mark it.


* **Mine Sweeper was not hooked up to the blockBreakCounts so it was not triggering a mine reset on zero blocks remaining like it should have been doing.**
Also increased the length between sweeps from 10 seconds to 15 seconds on a full mine.  The lowest value is still 2 seconds when it becomes close to empty.  Will put these values in a config setting soon so the admins can tweak them (hopefully increase the timings to be longer).


* **Update some docs to provide more details on where to find Prison releases, and how to include Prison within builds utilizing the repo jitpack.io.** 
Made a few config changes to the gradle build to increase the timeouts so gradle will not be so quick to terminate if it's taking a while to pull all of the resources.  This was strongly suggested by jitpack.io and it could also help resolve some of the recent issues we have been seeing with continual integration builds setup on the project.


* **Fixed possible issues with NPE but could not reproduce.**


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
  
