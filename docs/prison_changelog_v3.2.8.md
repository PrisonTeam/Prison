[Prison Documents - Table of Contents](prison_docs_000_toc.md)

## Prison Build Logs for v3.2.8 - 2021-06-17

## Build logs
 - **[v3.3.0-alpha - Current](changelog_v3.3.x.md)**
 - [v3.2.0 through v3.2.10](prison_changelogs.md)
 

 

Greetings!  I'm delighted that you are interested in the build logs for the
Prison plugin.  I'm wanting to provide a more formal documentation as to what 
is going on in each build so you have a better idea if it may be something 
that you need.


# Prison now fully supports Spigot 1.17 and Java 16 


# v3.2.8.1 2021-06-18

* **Apply fixes to the v3.2.8 release**


* **Check for null and an empty string in the parseInt() function for the /prison utils.**


* **Expanded the help information on the commands for /prison utils heal, feed, and breath to explain how amount works.**


* **Pull in the changes to bleeding with the new amount fields for /prison utils heal, feed, and breath.**
Without amount being specified it will restore full healing, feeding, breathing.  If an amount is entered with a "+" then it will add the amount to the current health levels, or if "-" then it subtracts from the current health level.  If a value is supplied without either "+" Or "-" then it will set it to that value.


* **Note: Bug fixes for 3.2.8.**

* **Fixed a failure on startup for new installations of prison.**
Basically it was unable to deploy the language files due to try-with-resources closing the initial zip connection.


# v3.2.8 2021-06-17

Prison V3.2.8 Release!
Prison now fully support Spigot 1.17 and Java 16!


**NOTE:** Since the start of the development on v3.3.0, Prison has had a few other releases under v3.2.7 and v3.2.8.  The reason for these releases is that the major structures (and code) that would make prison v3.4.x, are not complete.  Therefore, to get out new updates sooner than later, v3.2.7 and v3.2.8 have been release.



* **Updates to the stronghold and ruins patterns.**


* **Added a couple more mine liner patterns: darkForest and theColors.**


* **Update XSeries to v8.1.0 and switched back over to mvnrepo instead of using the temporary jitpack.io.**
Update the Prison build text to remove the java version from the build artifact name, since it was already removed from the generated jar.


* **Add 8 new placeholders for rankup cost remaining for percents and bars.**


* **v3.2.8-alpha.3 2021-06-16**


* **Mr. Grumpy Cat's suggestions for 5 new patterns for the liners.**


* **Fixed issue with no fortune for mc 1.8.8 and mc 1.12.2.**
The configs for auto manager now work properly.  


* **Had to double escape the use of %.**
It was working before, so this is a symptom of the messaging now being ran through String.format() twice.


* **Fixed a problem with a rank command's message missing a parameter.**
The text was expecting two parameters, only one was provided through the code.  Fixed.


* **A fortune calculation should have had a ! in an if statement.**


* **v3.2.8-alpha.3 2021-06-16**


* **Removed the java version suffix from the build artifact name.**


* **Clean up some of the LocalManager code.**
Added code to reload/replace language properties files when there are new ones in the jar with no locals, and if the jar version has a greater version.


* **For the /mines block list there was an M missing from the word Remaining for some hover text.


* **For the command prison version, add the java version of what compiled the class files within that plugin's jar.**


* **v3.2.8-alpha.2 2021-06-12**
Bump the version to alpha.2.  This release fixes all the obvious issues relating to the new Spigot 1.17.
This release also has full 1.17 block support too.


* **Prison's New Block Model is now enabled by default.**
With the release of Spigot 1.17 and with 73.5% of all servers using Spigot 1.16.x, it is far past time to make Prison's new block model the default.  All old blocks will be auto converted upon started.  The benefit with this switch over is that all blocks that are valid on the running server will automatically be available.  Such would not be the case with the old block model.
This change was intended for the v3.3.0.  
In the super-rare case where there is an issue with the new block model, there is a new, unpublished setting that will re-enable the old block model.  That setting should never be used; if an issue does exist, then it should be fixed in the new block model.


* **On ranks demotions, added a DEMOTE_SUCCESS RankupStatus to simplify a few things and to get rid of a hard coded boolean that was indicating a rankup vs. demote.**


* **Fixed an issue with players not being able to use /ranks player on themselves.**
It was trying to use an empty player name instead of getting it from the sender object.


* **Bug fix for Spigot 1.17: NMS has been disabled in 1.17, which means cannot get player's locale.**
When running on 1.17 it would produce stack traces every time the multi-language components would be used within Prison.  There was no way to capture the stack traces and it was generating a phantom trace that was lacking formation on where it was being generated from.
To work around this issue, the code for the NMS was moved to the compatibility package and it was rewritten to prevent exceptions.  If it is able get past the initialization without errors, but produce errors upon trying to access the internals, it will now capture the failures and prevent them from being tried again.
The nms code should work from spigot 1.3 through 1.16.5. It does not work with 1.17.  Will have to find an alternative method of getting it.



* **Update XSeries to v8.0.0**
It is not yet available on the main Maven repo, so using jitpack.io to provide the resource for now.
Note that this gives prison all of the 1.17 blocks.


* **This is a more correct fix for the semantic version value of "1.17".**
In general, 1.17 is invalid as strictly defined, but since that is the convention that Spigot uses, then made this work correctly by detecting this pattern, then appending ".0" to the end of the version to make it correct.
Also updated the unit tests to properly handle this new condition.
Updated the compatibility code to default to Spigot 1.13 compatibility if the Spigot semVer is invalid.



* **Spigot 1.17 release - v3.2.8-alpha.1 - 2021-06-11**
The release v3.2.8-alpha.1 has been release.
Only known issues: 
   * Unable to use nms to get the player's preferred language




* **Spigot 1.17 bug fix: Fixes the identification of the correct version of spigot.**
This is a temp fix for now, but it's functional.
The version of spigot was not able to be resolved so Prison was defaulting to use the spigot 1.8 compatibility mode which resulted in a lot of issues.
With this fix, most issues are resolved and the player can mine.


* **New features of showing the dump of registered listeners for the BlockBreakEvent and the AsyncPlayerChatEvent.**
These are located under the /prison debug command when special targets are specified.
Removed the DebugTarget.blockBreakListeners target type since it now has a directly called component in /prison debug.


* **Added a ranks_message__auto_refresh=true to indicate the language file should be refreshed.**
The lack of this setting will allow it to be  refreshed too.  Only a value of false will prevent a refresh.
A value of false will prevent auto updates so if someone customizes their configs their changes will be preserved.
NOTE: auto update of these language files has not been hooked up yet.


* **Added a new feature to scan plugin jars to identify what java version compiled them.**
This information will be incorporated in to the /prison version shortly.


* **Bug Fix: Player Mine GUI on TP'ing player to mine**
There was a logic error in checking for the player permissions on if they have access to TP to a mine.  The error happened when the mine name is longer than the base permission name such that it produced this index out of bounds exception: "mines.tp.".substr(0, -3).  
The logic was changed to mirror how it was within the SpigotPlayersMineGUI class.
Also took the opportunity to hook up the Access by Rank check too, so that way admins do not have to configure perms just for the GUI.


* **Added a getRankPlayer() to the prison spigot API.**


* **Setup the mine Access by Rank to be applied within the player GUI.**
Had to change the logic on block break handling to only check the Mine.hasMiningAccessByRank() if the access by options have been enabled. Block breakage needs to be more flexible since block breakage may be controlled by WorldGuard which would mean prison would have no way to validate if the player has access or not.  Hence why access by rank is so important.


* **Eliminated a couple of %s entries in this config file.**
The files/directory will need to be cleared so they can be regenerated.
`plugins/Prison/module_conf/ranks/` should be cleared for automated replacement.


* **Added a new debug tool for BlockEvents that dumps all registered BlockEvents that lists the plugin name and priority along with the listener.**
This can help identify potential issues when it appears like its not working.


* **For the prison's compatibility classes, added support for Player getMaxHealth() and setMaxHealth().**


* **Changes to gradle build so the java version can be set in the primary build script.**
Changed the generation of the jar's file name from Prison.jar to Prison-<version>-java1.8.jar to prepare for a java 1.8 and a java 16 build.


* **v3.2.8-alpha.1 2021-06-07**
Internally set the version, but will not release it until a few other things are finished.
The prison version is set to 3.2.8-alpha.1 to prepare for the release of prison that is compatible with Java 16 and Spigot 1.17.
Prison may not support all of the blocks in Spigot 1.17, but will be runnable.
Corrected some of the consistency issues with apache commons not specifying the correct version.


NOTE: v3.2.8-alpha.1 is identical to v3.3.0-alpha.6.  V3.3.0 is far from being ready to be released.  So v3.2.8 will enable Java 16 and also Minecraft 1.17.


# v3.3.0-alpha.6 2021-06-07


* **v3.3.0-alpha.6 2021-06-07**
Setting the version.  The v3.3.0 release will be put on hold since focus will be to get v3.2.8 out which will support Java 16.  It is unknown how many of the spigot 1.17 blocks will be initially supported.


* **Added the reporting of the delayed prison startup to the detail information for the command /prison version.**
It only will be displayed when it's enabled so it's presence will not encourage use by just being there.


* **New feature: Provide a delayed start for the CMI startup.**
This provides a fix for CMI not wanting to start until after prison has loaded without an active economy.
If the vault economy is available then it will start normally.  
If the Vault Economy is not available at startup, then prison will allow normal startup of the server, but delaying prison's startup for 5 seconds and then it will check again.  It will make 6 attempts before failing.  


* **Update a few gradle build includes: bStats and apache commons lang4. Fixed INCLUDE dup strategy.**
Update bStats from v1.5 to v2.2.1.  Not 100% sure that the plugin's ID is 657, but that's what is loaded on their website for Prison.
Update apache commons lang3 from v3.9 to v3.12.0.
Had to change the duplication strategy to INCLUDE so the project's version would be added to the generate jar.  Using EXCLUDE was preventing the version from being injected in to plugin.yml.


* **Added CMIEInjector to the soft depends to see if this will help resolve an issue with CMI loading after prison (again).**


* **Upgrade Gradle to version v7.0.2, from v5.6.4**
In order to better support the Java 16 environment, Gradle needs to be upgrade to the latest v7.0.2 release. During upgrading, which must be performed one version at a time, each version will both identify problems that will impact the future versions of the build, but also they will suggest how to resolve those problems.  



  * **Versions Upgraded To:** : **v6.0**, **v6.1**, **v6.2**, **v6.3**, **v6.4**, **v6.5**, **v6.6**, **v6.7**, **v6.8**, **v6.9**,  **v7.0**,  **v7.0.1**,  **v7.0.2**
  * **Versions to be Upgraded To**: v6.0, v6.0.1, v6.1, v6.1.1, v6.2, v6.2.1, v6.2.2, v6.3, v6.4, v6.4.1, v6.5, v6.5.1, v6.6, v6.6.1, v6.7, v6.7.1, v6.8, v6.8.1, v6.8.2, v6.8.3, v6.9, v7.0, v7.0.1, v7.0.2 (note: I think I can skip bug releases).
  * <code>gradlew wrapper --gradle-version 6.0</code> :: Sets the new wrapper version  
  * <code>gradlew --version</code> :: Will actually install the new version  
  * <code>gradlew build</code> :: Will build project with the new version to ensure all is good.  If build is good, then you can try to upgrade to the next version.
  

* **Upgrade to Gradle v7.0, v7.0.1, v7.0.2**
Gradle v7.0 had a bug, but the build completed normally.  
Gradle v7.0.1 had issues with maven repo **jcenter** which is shutting down and will be removed in Gradle v8.0.  Suggested to use mavenCentral instead. Just removed `jcenter()` from two `build.gradle` configs and all is working again.
Gradle v7.0.2 had no issues when upgrading.


* **Upgrade to Gradle v6.2, v6.3, v6.4, v6.5, v6.6 v6.7, v6.8, v6.9**
Upgraded gradle to these versions without any new warnings or errors.  So no change to any of the build configs.


* **Upgrade to Gradle v6.1**  
Added duplicateStrategy of DuplicatesStrategy.EXCLUDE to prevent multiple copies of resources from being added to the generated jar file. 
Duplicates could cause problems and are redundant objects that just inflate the jar.


* **Upgrade to Gradle v6.0** - Had to fix a few dozen issues.  

- Maven plugin is being deprecated and is replaced with maven-publish plugin.  testCompile is being replaced with testImplementation.
- Updates to enable support for Gradle v6.0.  These changes are mandatory for support with Gradle v7.0.
- Removed support for building a local maven repos (commented it out)... it never worked locally (since it's the wrong computer) and it never worked on remote servers for similar reasons.
- Change all URL protocols from http to https.
- Change the directive testCompile to testImplmentation
- Change the directive compile to implementation
The command gradlew was updated when gradle wrapper was updated to version 6.0.
Bump prison version to v3.3.0-alpha5c.


* **v3.3.0-alpha.5c - 2021-06-06**
.

* **Setting up SpigotPrison to support a reload on ranks and mines.**
It's not hooked up yet, and it may not work, but the structure of performing the reload and calling the correct functions are now in place.


* **Possible bug fix: Custom prison command placeholders were not be applied to the commands prior to submitting them.** Cause was missing code?  
This was tested and working a few months ago, so not sure what happened to the source.  But added it in to the command translation function to fix this issue.


* **Potential bug fix where access to the mine needs to be granted by default if both access by perms and access by ranks are disabled.**
This may have been preventing players from mining when relying on WorldGuard regions for player access.


* **Adjustments and improvements to the prison TPS calculation, and expansion on some of the documentation to go along with it.**
This should result in a more accurate TPS calculation that is more responsive to the current conditions.


* `BY GABRYCA on 04/06/2021:`
**FIX for NPEs from AutoFeatures GUI.**
  Potentially fixed NPE error from AutoFeatures GUI, now instead of "_BREAKING_" it should
  open the "this feature is disabled" GUI as expected.

* **Prison now does TPS calculations.**
These are based upon EssentialsX, but improves a few issues that I found with their calculations.  Prison's TPS calcuations have a much lower overhead with the calculations too.
The display of TPS is shown in the command /prison version.


* **Added a new command to allow admins to reload the auto features configs so they do not have to restart the server if they made manual changes to the config file.**
`/prison reload autoFeatures help`


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
  


