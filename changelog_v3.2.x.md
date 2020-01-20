
## Prison Build Logs for v3.2.x

Greetings!  I'm delighted that you are interested in the build logs for the
Prison plugin.  I'm wanting to provide a more formal documentation as to what 
is going on in each build so you have a better idea if it may be something 
that you need.


## tag v3.2.1-alpha.1 - 2020-01-20

* **Change How Integrations Work**
All directly accessed integrations are now logged and recorded so their status can be
included with the /prison version command.  Integrations that are indirectly used, 
such as through other plugins like Vault, are never listed directly unless Vault 
references them.  

I went though the placeholder integrations and fixed their APIs to use the newer
set of place holders. Fixed some bugs and expanded features.  The list of integrations
now also includes the primary URL where they can find more information on the plugins,
and where to download them from.  Also provides some additional information, such as
available place holders that can be used.

* **Add Minecraft Version**
Added minecraft version so it can be displayed in /prison version and also be used
in the future with selecting block types that are appropriate for the server version
that is being ran.

* **Bug fix: Block types not saving correctly if depending upon magic values**
Found an issue where loading blocks were by the BlockType name, but saving was by the 
minecraft id, which does not always match.  As a result, there was the chance a block
type would be lost, or it would revert back to the generic such as Birch Block 
reverting back to Oak Block.

* **Need to update gradle - Was at v4.10.3 - Upgraded to v5.6.4**
Currently this project is using Gradle v4.10.3 and it needs to be updated to v5.6.4 or 
even v6.0.1 which is the current latest release. It was decided to only take the project
to v5.6.4 for now, and wait for the next release on v6.x, which may gain more stability?
But to do that it must be incrementally updated to each minor version and you cannot just 
jump ahead or there will be failures.  At each step you need to evaluate your build scripts and
then make needed adjustments before moving onward.

  * **Versions Upgraded To:**: **v5.0**, **v5.1.1**, **v5.2.1**, v5.3.1, v5.4.1, v5.5.1, **v5.6.4**, 
  * **Versions to be Upgraded To**: v6.0, v6.0.1  
  * <code>gradlew wrapper --gradle-version 5.0</code> :: Sets the new wrapper version  
  * <code>gradlew --version</code> :: Will actually install the new version  
  * <code>gradlew build</code> :: Will build project with the new version to ensure all is good.  If build is good, then you can try to upgrade to the next version.
  * Update to v5.0 was successful. Had to remove <code>enableFeaturePreview('STABLE_PUBLISHING')</code>
   since it has been deprecated. It does nothing in v5.x, but will be removed in v6.0.
  * Update to v5.1.1, v5.2.1, v5.3.1, v5.4.1, v5.5.1, v5.6.4 was successful.


* **Minor clean up to Gradle scripts**
The "compile" directive is actually very old and was deprecated back around version v2.x I 
think?  So the replacements for that is "implementation" or "api" instead.  The use of 
api does not make sense since its use is to tag when internal functions are exposed to the
outside world as if it will be used as an externally facing API.  That really does not
fit our use case, but what api also does is to force compiling all source that is associated
with something marked as api, including everyone's children.  So performance will suffer due
to that usage, since it shuts down incremental building of resources.

I also found that use of compileOnly may not be used correctly, but at this point
I'm just leaving this as a mention and then revisit in the future if time
permits, or issues appear to be related.  Its a very old addition that provided
gradle with "some" maven like behaviors.  It was only intended to be strictly used for compile
time dependencies, such as for annotations that are only needed for compile-time checks, of which
the plugins and resources we have marked as compileOnly do not fit that use case.
[discuss.gradle.org: compileOnly](https://discuss.gradle.org/t/is-it-recommended-to-use-compileonly-over-implementation-if-another-module-use-implementation-already/26699/2)


* **Redesigned mine block generation works - Async and paged enabled**
Redesigned how prison blocks are generated to improve performance and to allow the
asynch generation of blocks, but more importantly, allows for paging of actual 
block updates. The paging is a major feature change that will allow for minimal 
impact on server lag, even with stupid-large mines.  The idea is to chop up the
large number of blocks that need to be regenerated in to smaller chunks that can
be performed within one or two ticks, then defer the other updates to the future.
Thus freeing up the main bukkit/spigot execution thread to allow other tasks
to run to help prevent the server from lagging.

* **Support for LuckPerms v5.0.x**
In addition to supporting older versions of LuckPerms, Prison now is able to 
integrate LuckPerms v5.0.x or LuckPerms v.4.x or earlier.  Take your pick. 

* **Minor changes to reduce the number of compiler warnings**
Minor changes, but better code overall.


* **Improved mine regeneration performance by 33.3%**
Figured out how to improve performance on mine regeneration by roughly about 33.3% overall. This
could help drastically improve performance and reduce lag since block updates must run 
synchronously to prevent server corruption (limitation is due to the bukkit and spigot api).  

* **Mine stats and whereami**
Added a new command, **/mines stats**, to toggle the stats on the mine resets.  Viewable with **/mines list** and **/mines info**. Stats are now within the MineManager so it can be accessed from within
the individual mines. 
Added a new command, **/mines whereami**, to tell you what mine you are in, or up to three mines you are nearest to and the distance from their centers.


* **Major restructuring of how Mines work - Self-managing their own workflow**
They now are able to self-manage their own workflow for sending out notifications and for resetting automatically.
Mines is now layered, where each layer (abstract class of its ancestors) contributes a type of behavior and business logic that allows Mines to be more autonomous.
As a result, PrisonMines and MineManager are greatly simplified since they have less to manage.
Because Mines are now self-managing their own workflow, and they have taken on a more expanded role, some of the mine configurations are obsolete and removed.
Mines only notify players that are within a limited range of their center; they no longer blindly broadcast to all players within a given world or the whole server.
Mines extend from MineScheduler, which extend from MineReset, which extend from MineData. Each layer focuses on it's own business rules and reduces the clutter in the others, which results in tighter
code and less moving parts and less possible errors and bugs.


* **Concept of distance added to Bound objects**
Added the concept of distance between two Bound objects so as to support new
functionalities between mines and players.


* **Gradle now ready to upgrade to v5**
Resolved the last few issues that would have caused issues when upgrading to gradle 
v5. Explored how gradle can use java fragments and variables.


## tag v3.2.0 - 2019-12-03


* **Now works in 1.14.4!!**
Added in the missing library to get it to work with 1.14.4.


* **Fixed issue with pull request 132**
Fixes the https://github.com/PrisonTeam/Prison/pull/132 pull request.
It causes prison to fail on a clean server and throws a class not found exception.
The new library needs compile instead of compileOnly and it must also be shadowed.


* **Update to org.inventivetalent.spiget-update** Updated to version 1.4.2-SNAPSHOT since
the old version 1.4.0 was no longer available. 


* **Updates to bStats**  The Maven repository was updated and so was the version of
bStats that is being included on the project.  It is now using the current 1.5 version, 
was using 1.3 which is no longer available in the repository and was causing a build
failure for gradle.


* **New Feature - Decimals in Block chances now supported!** 
Added support to display and use two decimal positions on block chances. 


* **Fix - TP of Players out of the Mines upon Rest** 
This is one part of a multi-step process to help ensure players are TP'd out of mines upon
a reset.  In an effort to help start to address the
known bug [![Mine doesn't teleport to safety #82](https://github.com/PrisonTeam/Prison/issues/82)] 
Rewrote the player teleport function to help address the issue.


* **New Feature - Block search!!**
You can now search for blocks to add to the mines!  It uses the BlockTypes and is able
to search on the ID and the enumeration name.  It displays up to 10 at a times and 
provides Next Page and Previous Page controls to page through the results.
If you click on a BlockType, it will suggest the command for adding blocks:
** /mines block add <mine> <blockTypeSelected> %**  All the user needs to do is 
fill in the percent and change the "<mine>" to a valid mine name. 


* **Create Test plans**
To better ensure all test servers are tested consistently, some basic test plans have been
created to use during online testing. They are important for consistency when testing
all of the supported versions, especially to ensure nothing is skipped.  These will
evolve over time as needed.
Test server versions used in testing:
    * **Minecraft 1.8.8** 
    * **Minecraft 1.9.4** 
    * **Minecraft 1.10.2**
    * **Minecraft 1.11** 
    * **Minecraft 1.12.2** 
    * **Minecraft 1.13.2** 
    * **Minecraft 1.14.4** 


* **Major refactoring of the prison-mines package**
Took some time to refactori parts of the prison-mines package.
Eliminated duplication of source code, and repetitive calculations. Found a few
possible bugs and reworked the code improve performance and stability.


* **Found another possible cause for suffocation after a mine reset**
Identified what could have been yet another cause of some of the suffocation issues
during a mine reset.  It was not related so much to a bug or code, so it wouldn't
be something that could be detected with debugging.  What it appears to have been
is related to the speed in which the mines reset, or more exactly, the 
server lag or load.  If the player is TP'd and then the mines are slow at being reset,
then the player may fall back in to the mine where they would be suffocated.
To address this issue, after the mine is fully reset, it checks to see if anyone has 
fallen back in to the mine and will TP them back out to safety.


* **Update gson library from v1.7 to v1.8.6**
Updated library.


* **Removed the unused library com.fasterxml.jackson**
This library was not used anywhere so it was removed from the gradle configs.


* **Removed the unused library json-simple v1.1.1**
This is also an obsolete and archived project. Removed to reduce compile time
and bloat of the build.


* **TP Player if feet are one block below mine**
Technically they would not be standing within the mine, but since their head is within the
mine they could suffocate when the mine resets.  So include the layer below the mine
to determine if the player needs to be TP'd out.


* **Created a unit test for player being one block below the mine**
This is to ensure the player will be properly identified as being within the mine, although
they are standing one layer below the mine.


* **TP Player up one block higher**
The player needs to be TP'd one block higher if a spawn does not exist.
The former vertical target needed to be one block higher. This got lost
within the prior refactoring.


* **Prevent adding new blocks that have a chance of zero**
The user cannot add blocks to a mine if they provide a zero chance. 


* **Found a bug - Mines would not generate new random pattern**
Refactoring the mine generation code made obsolete a pre existing check for block
regeneration which prevented new fill patterns from being generated for the mines.
Removed the check and it resolved the issue.


* **New feature - Created RowComponent to group multiple FancyMessages on Same Line**
This now allows easy generation of more complex content where there can be
more than one FancyMessage (has hover text, suggests, and run capabilities when mousing)
per row.  This allows putting the two buttons for block search on the same 
chat row to reduce lines consumed.



* **Upgraded Gradle - Was at v4.4.1 - Upgraded to v4.10.3**
This project was using Gradle v4.4.1 and should be updated to v5.6.4 or 
even v6.0.1 which is the current latest release of Gradle.
But to do that it must be incrementally updated to each minor version to identify 
the future changes to gradle, such as deprecated features. As features are deprecated, they 
provide details on how to adjust your build scripts, but those hints are removed in future 
releases.

    * **Versions Upgraded To:**: **v4.4.1**, **v4.5.1**, **v4.6**, **v4.7**, **v4.8.1**, **v4.9**, **v4.10.3**, 
    * **Versions to be Upgraded To**: v5.0, v5.1.1, v5.2.1, v5.3.1, v5.4.1, v5.5.1, v5.6.4, v6.0, v6.0.1  
    * <code>gradlew wrapper --gradle-version 4.5.1</code> :: Sets the new wrapper version  
    * <code>gradlew --version</code> :: Will actually install the new version  
    * <code>gradlew build</code> :: Will build project with the new version to ensure all is good.  If build is good, then you can try to upgrade to the next version.
    * Gradle v4.10.3 version of the project's build scripts has a few v5.0 issues that 
  need to be addressed before upgrading any farther. Future upgrades will be performed
  after these issues are addressed.  ETA unknown.


* **Removed Guava Caching**
There were a number of reasons (about 18) for the removal of Guava caching.  Primarily
it really wasn't being used and the use case of the data does not warrant caching.
Removal of caching also helped to reduce memory consumption and reduce the overhead 
associated with the caching library.


* **Update the FileStorage Class**
Provided Java docs and also the logging of warnings.
Altered the behavior so it actually does not delete a FileDatabase but instead it virtually deletes it.  
This provides the user with a way to undelete something if they realize later they should not have 
deleted it  To undelete it, they would have to manually rename the underlying directory and then 
restart the server (but the data is not lost!).
Also cleaned up the nature of some of the code so the functions have one exit point.
Updated Storage so the functions are declared as public and so createDatabase and deleteDatabase returns 
a boolean value to indicate if it was successful so the code using these functions know if it should 
generate warnings.


* **Refactored the Virtual Delete**
I created an abstract class that has the virtual delete code in it so now anything that needs to use
that functionality can extend from that class.  I updated FileStorage so it now extends from
FileVirtualDelete.


* **Refactored FileStorage, FileCollection, and FileDatabase that were in the src/test packages**
I deleted these three source files from the prison-spigot module.  Not sure why they were there, but
simple refactoring made these obsolete.


* **Found and fixed a fatal bug in PrisonMines.enable()**
Within PrisonMines.enable() the errorManager was being instantiated AFTER initConfig but if there was a 
failure with initConfig, then it would have hit a Null Pointer Exception since that was what the value 
of errorManager was, a null since it was not initialized yet.


* **Created FileIO, JsonFileIO, and FileIOData**
Created a couple of classes and an interface to deal better with saving and loading of the data and 
to also better encapsulate the generation of the JSON files.  The MinesConfig class was the first 
class to get this hooked up with, which simplified a great deal of the code that deals with files.


* **Minor updates to PrisonMines class**
asyncGen is not being used right now, so commented it out to eliminate warnings.  Performance 
improvements may have made this obsolete.  May revisit in the future with actual mine 
resets instead of just precomputing.
Also clear the precomputed new blocks for the mines to free up memory between resets of the mines.


* **Major changes for the FileCollection and Document related classes**
This was a major change that encompasses the removal of Guava, the caching library.
Everything works the same as before, but with the exception of saving the individual 
files as change occur; that will be added in next.  This touched basically every module
including the prison-ranks.


* **Update FileStorage and FileDatabase**
These changes are fairly similar in nature and the two classes are so very similar. 
Maybe in the future they can be merged so there is the same code base handling both of them.


* **Various clean up items**
Like removal of useless comments that are in the wrong places.  Fixing includes that are including
packages that are not being used.  Removal of warnings.  Etc... just mostly items that will result
is easier to read code without any functional changes (and no program changes in most places).


* **Simple refactorings**
There were a few items that were refactored back in to their controlling class. For example ranks 
and ladders add on an id to generate their file names.  Created a filename function for those
classes so externally all users of those classes do not have to know what the business rules are
for constructing the filenames. This also can help to eliminate errors in the future; only one
location for constructing filenames instead of a few different locations.


* **New Feature: Add a text component to RowComponent**
Expanded the ability of RowComponent to accept parameterized text now.


* **New Feature: Add volume to the mines info**
Provide a little more information for mines by now showing the mine's volume.


* **Bug fix - Mine blocks updating wrong values and problems deleting blocks**
Fixed a few bugs with adding, setting, and deleting by the wrong value.  By default 
it was trying to delete by id, but multiple blocks have the same id, so it was 
corrupting the block lists for a mine.
It now uses the block name instead of block id.  This eliminated problems.


* **New Feature - Identify all BlockTypes that are blocks as blocks**
Trying to define each BlockType as a BLOCK or not.  This will be used to filter 
the results on /mines block search to ensure only blocks that are placeable can be 
set in the mines. Tried to programatically identify what a block is within all 
supported minecraft versions, but it did not work due to conflicts with the
blocks names within Materials and within the Prison plugin's BlockType enum.
Manually marked each BlockType since programatic attempts were running in to 
numerous issues.  There may be a few that do not
make sense or are incorrect, but can update later when found. Created a new enum
by the name of MaterialType that is used to mark the BlockTypes as 
BLOCKs.  Can also use that enum to label the other items with something more
meaningful such as ITEMs, or even ARMOR or WEAPON. Can have a great deal of 
flexibility moving forward with that.


* **New Feature - Block search new only includes blocks that are actually placable in the mines**
Hooked up the BlockType.getMaterialType() == MaterialType.BLOCK to the block 
search and the block add.


* **New Feature - Added confirm on Mine Delete**
Added a new feature to provide a confirmation when deleting a mine.  The user can 
click on the notification message to have the command reentered for them, then they
only need to change **cancel** to **confirm**.  
This will help prevent accidental deletion of mines.  Yes mines can be manually 
undeleted, but this can prevent mistakes.


* **New Feature - Using /mines set block will add new blocks**
If you click on a block from under /mines list and you change the mine's name on that
/mines block set command, it will be treated like an add instead of an error. 
This is beneficial if you want to copy a block from one mine to another and you forget
to change "set" to "add".


* **New Feature - Added a center Location to Bounds**
Added a center location for Bounds which will be used for a few things for mines.
Currently it was added as a fall back location for if a spawn point does not exist for 
teleporting in to a mine.  But it will also be used to identify all players within a 
given radius from that location to selectively target broadcast messages pertaining 
to that mine.


* **New Feature - Clone existing Locations**
Makes it easier to create clones of Locations.


* **New Feature - Track last Mine name used and substitute it for the generic place holder**
Track what the last value for mine was, and put it on a timer.  If it was last 
referenced within 30 minutes, then use that reference by default in the block 
search function.
In the future, when auto suggest is enabled, this value will also be used for the 
targeted mine names instead of just a generic <mine> place holder.


* **New Feature: TP to each mine**
Added a new feature so an admin could TP directly to each mine.  If the mine's spawn location 
is not set, then it will tp them to the center of the mine, but on top of the surface.
Also, if there is an air block at the player's feet, it will auto spawn in a glass block so 
they would not take fall damage if there is a significant void below them.


* **Enhanced /mines list now includes more details**
Enhanced the /mines list to show more details about the mines, plus provide a clickable 
way to TP to each mine.


* **Fix - Removed color codes from commands**
Color codes do not work well with 1.14.4.  Removed them.


* **Fixed some issues with deleting a mine**
Changed the way the delete mine command works with the user messages.
Its clear as to what's going on and what the user has to do with the confirmations.
The time to confirm is limited, and reissues the start of the command if the user 
misses the window to confirm.
 


#### v3.2.0 Known Issues: To be addressed

* **Mine TP of Players who logout or set a home in the mines**
If a player logs out of the server when they are in the mines, or if they
set a home within the mines, there currently isn't an event handler that
will TP them to safety when they log back in, or tp to that home location.
This feature is planned for the future, but no ETA has been set.






