
## Prison Build Logs for v3.5.x

Greetings!  I'm delighted that you are interested in the build logs for the
Prison plugin.  I'm wanting to provide a more formal documentation as to what 
is going on in each build so you have a better idea if it may be something 
that you need.

I'm unofficially taking over the prison project because I need to use it on my
server that I'm starting to build, therefore it's in my best interest to address
open bugs and to add in some new features.  I honestly cannot commit to anything
long term, but I hope to at least bring this project up to date (support 1.13.x)
and address some of the outstanding bugs.  If I can leave this in a slightly 
better state from what I found it, then I will consider this a success.


## tag v3.5.2-beta.1 - 2019-11-27

We are done with alpha tagging!  Time to focus on getting this released!

* **New Feature: Add a text component to RowComponent**
Expanded the ability of RowComponent to accept argumented text now.


* **New Feature: Add volume to the mines info**
Provide a little more information for mines with the mine's volume.


* **Bug fix: Mine blocks updating wrong values and problems deleting blocks**
Fixed a few bugs with adding, setting, and deleting by the wrong value.  By default 
it was trying to delete by id, but multiple blocks have the same id, so it was 
corrupting the block lists for a mine.
Got it where it would use the block name instead of block id.  This eliminated problems.


* **New Feature: Identify all BlockTypes that are blocks as blocks**
Trying to define each BlockType as a BLOCK or not.  This will be used to filter 
the results on /mines block search to ensure only blocks that are placeable can be 
set in the mines. Tried to programatically identify what a block is within all 
supported minecraft versions, but it did not work due to conflicts with the
blocks names within Materials and within the Prison plugin's BlockType enum.

Just went ahead and manually marked each one.  There may be a few that do not
make sense or are incorrect, but can update later when found. Created a new enum
by the name of MaterialType that is used to mark the BlockTypes as 
BLOCKs.  Can also use that enum to label the other items with something more
meaningful such as ITEMs, or even ARMOR or WEAPON. Can have a great deal of 
flexibility moving forward with that.


* **New Feature: Block search new only includes blocks that are actually placable in the mines**
Hooked up the BlockType.getMaterialType() == MaterialType.BLOCK to the block search and the block add.


* **New Feature: Added confirm on Mine Delete**
Added a new feature to provide a confirmation when deleting a mine.  The user can 
click on the notification message to have the command reentered for them, then they
only need to change **cancel** to **confirm**.  
This will help prevent accidental deletion of mines.  Yes mines can be manually 
undeleted, but this can prevent possible errors.


* **New Feature: Using /mines set block will add new blocks**
If you click on a block from under /mines list and you change the mine's name on that
/mines block set command, it will be treated like an add instead of an error.


* **New Feature: Added a center Location to Bounds**
Added a center location for Bounds which will be used for a few things for mines.
Currently it was added as a fall back location for if a spawn point does not exist for teleporting in to a mine.
But it will also be used to identify all players within a given radius from that location to selectively target broadcast messages pertaining to that mine.


* **New Feature: Clone existing locations**
Makes it easier to create clones of locations.


* **New Feature: Track last Mine name used and substitute it for the generic place holder**
Track what the last value for mine was, and put it on a timer.  If it was last referenced within 30 minutes, then use that reference by default in the block search function.
In the future, when auto suggest is enabled, this value will also be used for the targeted mine names instead of just a generic <mine> place holder.


* **New Feature: TP to each mine**
Added a new feature so an admin could TP directly to each mine.  If the mine's spawn location is not set, then it will tp them to the center of the mine, but on top of the surface.
Also, if there is an air block at the player's feet, it will auto spawn in a glass block so they would not take fall damage if there is a significant void below them.


* **Enhanced /mines list now includes more details**
Enhanced the /mines list to show more details about the mines, plus provide a clickable way to TP to each mine.


**Build artifacts:**  
  Be aware that this build artifact is not production grade and still needs more testing.
  * **prison_v3.5.2-beta.1.jar**    



## tag v3.5.2-alpha.6 - 2019-11-24

* **Guava Caching - Google Caching - Redundancy**

These are notes pertaining to a Gauva library that is not making much sense.
I'm trying to rationalize its removal for a much simpler DAO interface which can 
 then better focus on simplicity and reduced failures, along with improved performance.
 There is a cache that is between the file system and the mines data. I'm sure the same 
 technologies are in place for the prison-roles too. This is causing 
 me much trouble since I am trying to figure out why it is there.  These are the reasons
 why it does not make sense to me.
   1. All mines are loaded in to memory when the server starts.  "All mines."
   2. All mines remain in memory while the server is running.
   3. There is no reason to reload from the files system, and even if someone manually 
       modified the file system version, the cache will not reflect those changes.
   4. The cache is a Map. It's not a mirror image of what is on the file system.  It is 
   not a mirror image of the mines that are stored in memory.
   5. I cannot find anything that uses the cache other than initial mines loader.
   6. It is consuming memory with no purpose.
   7. The data is in duplication of the actual Mines data.
   8. Overhead to maintain the cache and evict stale objects (read: all objects 
   after 5 minutes)
   9. Yet another library that really is not needed that is bloating the plugin's 
   jar file.
   10. Added level of complexity, which could possibly hide bugs or make it far more 
   difficult for less experienced programmers to maintain the project, which could lead
   to serious and fatal bugs or loss of customer's data.
   11. When coupled with the project's Document, Collections, and Database abstraction it 
   makes it really difficult to figure out how data is getting pass through the layers, and
   what transactions are operating upon the data.  Hence could be more difficult to debug
   or track down intermittent failures.
   12. When writing a changed Mine to the file system, the cache is updated, but nothing 
   will reuse it.  It has an access count of 1 which is generally hit when the server starts.
   13. Instead of an active cache, the plugin would be better served with a robust DAO.
   14. Focus needs to be redirected to simplicity in the use of JSON objects.  Way too much 
   manual manipulation of the data is happening to coercion it in to either the Java objects,
   or the json structure.  The plugin should allow the JSON tools deal with the ORM since
   they are really good at that, and it would help reduce plugin complexity and improve
   performance and reliability.
   15. There has been a few reports of missing data.  I cannot help think that the 
   possible cause could be hidden somewhere in the layers of abstraction dealing with
   the files on the file system.
   16. Simplification could help extend the plugin's data storage to a SQL database, or
   a no-SQL database.  Hence how the DAO could extend the flexibility.
   17. Side effect of the cache is that there is almost full duplication of the FileCollection 
   code within the source code for testing.  Unit testing should not be performed against 
   file system objects since that defeats the purpose of the unit tests.  Also I'm not really 
   sure why the duplication exists, but it is in duplication and could become a liability. 
   I did not review what's in the test source, so I don't know its true purpose.
   18. Another issue is with updates to the file system for Mines. I'm not 100% sure what's
   going on, but when changes are made in one mine, all mines appear to be saved at the same 
   time.  Mine data files should only be saved when their data changes. Saving all mines is 
   possibly risky since the original is being deleted before the update is written. (I will
   be fixing that at least).

So I think I made a good point for Guava's removal: so be it.

But... (there's always a but) If there is a good reason to use a cache on something that 
can benefit it, then it will be brought back, but only for those resources that can benefit.


* **Update the FileStorage Class**
Provided Java docs and also the logging of warnings.
Altered the behavior so it actually does not delete a FileDatabase but instead it virtually deletes it.  
This provides the user with a way to undelete something if the realize later they should not have 
deleted it  To undelete it, they would have to manually rename the underlying directory and then 
restart the server.
Also cleaned up the nature of some of the code so the functions have one exit point.
Updated Storage so the functions are declared as public and so createDatabase and deleteDatabase returns 
a boolean value to indicate if it was successful so the code using these functions know if it should 
generate warnings.

**Warning:** There is an identical copy of FileStorage, FileCollection, and FileDatabase located within the prison-core/src/test/java directory which must mirror the real ones in the prison-spigot module.


* **Refactored the Virtual Delete**
I created an abstract class that has the virtual delete code in it so now anything that needs to use
that functionality can extend from that class.  I updated FileStorage so it now extends from
FileVirtualDelete.


* **Refactored FileStorage, FileCollection, and FileDatabase**
I deleted these three source files from the prison-spigot module.  Not sure why they were even there?
That never really made sense, nor did I try to figure that out since it appeared like a simple refactoring
would work well.
I then moved the the prison-core source for these three classes from src/**test**/java to
src/**main**/java.  
Had to update the include for prison-spigot module's SpigotPlatform.  Everything compiles well and works. 


* **Found and fixed a fatal bug in PrisonMines.enable()**
Within PrisonMines.enable() the errorManager was being instantiated AFTER initConfig but if there was a 
failure with initConfig, then it would have hit a Null Pointer Exception since that was what the value 
of errorManager was, a null since it was not initialized yet.


* **Created FileIO, JsonFileIO, and FileIOData**
Created a couple of classes and an interface to deal better with saving and loading of the data and to also 
better encapsulate the generation of the JSON files.  The MinesConfig class was the first class to get 
this hooked up with, which simplified a great deal of the code that deals with files.


* **Minor updates to PrisonMines class**
asyncGen is not being used right now, so commented it out to eliminate warnings.  May revisit in the future with actual mine resets instead of just precomputing.
Also clear the precomputed new blocks for the mines.  Frees up memory between resets of the mines.


* **Major changes for the FileCollection and Document related classes**
This was a major change that encompasses the removal of Guava, the caching library.
Everything should work the same as before, but with the exception of saving the individual 
files as change occur; that will be added in next.  This touched basically every module
including the prison-ranks.


* **Clear generated mine blocks between resets**
Will free up some memory. Not convinced there is much of performance gain by precomputing it
or generating it asynchronously.  Will have to revisit and take measurements on timings.
Good chance that just staggering mine resets would do more for overall performance than 
trying to push them all through at the same time.


* **Update FileStorage and FileDatabase**
These changes are fairly similar in nature and the two classes are so very similar. 
Maybe in the future they can be merged so there is the same code base handling both of them.


* **Various clean up items**
Like removal of useless comments that are in the wrong places.  Fixing includes that are including
packages that are not being used.  Removal of warnings.  Etc... just mostly items that will result
is easier to read code without any functional changes (and no program changes in most places).


* **Simple refactorings**
There were a few items that I refactored back in to their controlling class. For example ranks 
and ladders add on an id to generate their file names.  I created a filename function for those
classes so externally all users of those classes do not have to know what the business rules are
for constructing the filenames. This also can help to eliminate errors in the future; only one
location for constructing filename.


* **Need to update gradle - Was at v4.4.1 - Upgraded to v4.10.3**
Currently this project is using Gradle v4.4.1 and it needs to be updated to v5.6.4 or 
even v6.0.1 which is the current latest release.
But to do that it must be incrementally updated to each minor version and you cannot just 
jump ahead or there will be failures.  At each step you need to evaluate your build scripts and
then make needed adjustments before moving onward.

    * **Versions Upgraded To:**: **v4.4.1**, **v4.5.1**, **v4.6**, **v4.7**, **v4.8.1**, **v4.9**, **v4.10.3**, 
    * **Versions to be Upgraded To**: v5.0, v5.1.1, v5.2.1, v5.3.1, v5.4.1, v5.5.1, v5.6.4, v6.0, v6.0.1  
    * <code>gradlew wrapper --gradle-version 4.5.1</code> :: Sets the new wrapper version  
    * <code>gradlew --version</code> :: Will actually install the new version  
    * <code>gradlew build</code> :: Will build project with the new version to ensure all is good.  If build is good, then you can try to upgrade to the next version.


  * Gradle v4.10.3 v5.0 Incompatibility issues:
    * v4.6 hit an incompatibility with v5.0 with command line warnings - Added the gradle.properties to better control the logging of errors.
    * v4.7 hit another incompatibility with v5.0 with the lack of annotationProcessor for resources that are using annotations. - Later versions of Gradle identified the source of this issue and I was able to add an annotationProcessor to the prison-sponge's build.gradle settings.
    * v4.9 hit another incompatibility with v5.0. The 'deferred configurable' behavior of the 'publishing {}' block has been deprecated. From what I can tell, it should be all right? 
    * v4.10.3 hit another incompatibility with the  AbstractFileCollection.getBuildDependencies() method has been deprecated. 

I'm going to call it quits for now with upgrading gradle. v4.10.3 is closer to a current release and I need more time to review the last few issues.


* **Few minor bug fixes**
A few changes that fixed when the mines and ranks were being saved.  There were a few issues 
with them saving when there were no changes made (server start and shutdown).


**Build artifacts:**  
  Be aware that this build artifact is not production grade and still needs more testing.
  * **prison_v3.5.2-alpha.6.jar**    

**NOTE:** I may not release this build artifact until I get a decent amount of testing done 
first. So much core code has changed, that I do not want to risk any unexpected issues. 
Of course no one should ever use an alpha release on a *production* server, but it
happens.


## tag v3.5.2-alpha.5 - 2019-11-21

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
Basically when refactoring the mine generation code, I found a duplicate "rebuild mine" 
function call that was not needed. But based upon the original code, there was a 
check that preventing it from generating new random patterns before the 
mines reset.  I left that check in place, which prevented the mines from 
creating new fill patterns.  The mines now create the proper random fills on each
reset.


* **New feature: Created RowComponent to group multiple FancyMessages on Same Line**
This now allows easy generation of more complex content where there can be
more than one FancyMessage (has hover text, suggests, and run capabilities when mousing)
per row.  This allows putting the two buttons for block search on the same 
chat row to reduce lines consumed.


**Build artifacts:**
  Be aware that this build artifact is not production grade and still needs more testing.
  Note: The reduced size is due to the removal of unused libraries. 
  * **prison_v3.5.2-alpha.5.jar**    


## tag v3.5.2-alpha.4 - 2019-11-18

* **Found another possible cause for suffocation after a mine reset**
I think I identified what could have been causing some of the suffocation events to happen
during a mine reset.  It was not related so much to a bug or code, so it wouldn't
be something that could be detected with debugging.  What it appears to have been
is related to the speed in which the mines reset, or more exactly, the 
server lag or load.  If the player is TP'd and then the mines are slow at being reset,
then the player may fall back in to the mine where they would be suffocated.
Maybe one solution is simple: Add a second TP to catch any players that may have fallen 
back in.
Also found code that generated a second block list after the mine reset and commented
that out (for now).  That could have been contributing to server load and lag if there
were many mines setup, which could result in more players falling back in to the mines.

Of course, the best solution, if this is the case, is to always set a spawn location
for all of your mines! :)

Note: I did hear the suffocation issue was happening with a spawn defined, so there may be yet another situation out there.  But at least we could be eliminating possible issues.

* **Added back the spiget updater.**  
Figured out how it worked so was able to 
hook it back up.  Basically when you add a resource to spigotmc.org your 
resource (project) gets assigned a number.  Let's call it a spigotMC resource ID
for now.  So that is the value that needs to be passed to the SpigetUpdater 
constructor.  No where in the documentation, or anywhere else, does it explain
what that is... oof!!

* **Created a SpigotMC.org resource for this fork**  
[SpigotMC.org - Prison - The Updated Edition](https://www.spigotmc.org/resources/prison-the-updated-edition.72740/)
Created a new resource on spigotmc.org and added some of the build artifacts.
Will manually maintain this and update the stats as I can.


* **Major refactoring with prison-mines package!!**
I took some time to start a first pass on refactoring of the prison-mines package.
I did not go too deep since I was wanting to ensure I did not break anything, and
also I was wanting to get a better feel of what is going on and how things work.
I think overall I am happy with what I achieved since I did eliminate 
duplication in source code, eliminated repetitive calculations such that many 
are performed when the mine first loads to reduce computational loads when the 
mines are reset.  I also found a few possible bugs (did not mark them, just fixed them).
Removed old comments and obsolete code.  


* **The need for major testing!  Only testing prison-mines package.**
Due to the refactoring, I'm forced (forcing myself) to go through all the supported
Spigot supported Minecraft versions and test the user interface to ensure all is 
still working!  This will also give me a chance to confirm all versions do work for 
these basic user interface commands.

Unfortunately this is taking a while so it will result in a delay in pushing out any changes for the v3.5.2-alpha.3 build artifacts.


* **Built test servers**
I've build test servers for the following version of spigot and minecraft. These will
be used going forward to ensure the code works, and also to help reproduce issues 
so I can fix problems as they are reported.  The following versions were only tested
with the prison-mine modules; nothing else.

    * **Minecraft 1.8.8** - Passed!
    * **Minecraft 1.9.4** - Passed!
    * **Minecraft 1.10.2** - Passed!
    * **Minecraft 1.11** - Passed!
    * **Minecraft 1.12.2** - Passed!
    * **Minecraft 1.13.2** - Passed!
    * **Minecraft 1.14.2** - Failure in /mines block add. Testing stopped.

  Note: Minecraft 1.11.2 was released but does not work with the spigot's 1.11 support.
    
* **Minecraft 1.14.2 Failures** 
It should be noted that there are a significant failures within this plugin for minecraft 1.14.2.  
Therefore 1.14.2 cannot be supported at this time.  The failures that I saw and reviewed does not
rule out support in the near future since the failures appear to be caused by a dependency that 
does not exist anymore in the 1.14.x minecraft or bukit builds.  Therefore, with perhaps careful 
shadowing of the jars for Prison, it may be functional.  Of course once those dependencies are
working, there is the possibility that something else may be shown to fail.  Therefore it can be 
a rather complex task to get working.


* **Create Test plans**
To better ensure all test servers are tested consistently, I've started to create test plans to use during online testing.  These will evolve.  But they are important for
consistency.

- <a href="test-plans/test-plans-README.md">test-plans-README.md</a>
- <a href="test-plans/test-plans-prison-mine.md">test-plans-prison-mine.md</a>


* **Issues found during these tests**
It should be noted that right now only the prison-mines plugin was tested against the above minecraft spigot versions.  Not prison-ranks nor prison-core (outside of prison-mines).
Issues found does not mean they will all be fixed or addressed, nor do they imply any order of urgency to be addressed if they ever will.  They may not be actual issues either, but just items that caught my attention.

    - Mines set area : Should add message indicating how large the mine is, or do a check to ensure if the size did change or not. It looks like it will use the pre existing size if you just created the mine.
    - Delete mines : No confirmation.  Add confirmation before deleting.
    - Delete mines : Does not clear the old blocks... maybe should set to 100% air and reset before deleting.  Have a parameter of "clear" to perform this task?
    - Delete mines : Settings are deleted from the file system. Probably should move in to a sub folder named "deleted" so there could be an option to undelete a mine?  If accidentally done, could be difficult to tweak it again.
    - Add block : Should not add a zero percent! Currently it does, which is in error.
    - Add block : Allow block edit to add a block if it does not already exist.
    - Remove block : If a non-block type is added to a mine, it cannot be removed. Allow it to be removed if it is in there.
    - Block search : Fix buttons so they are on the same line.
    - Block search : Add page counts, like page 3 out of 5.
    - Block search : Try to figure out how to validate if it a block and not an object.  Object are selectable, but never spawnable within the mines.  So if we can eliminate them from the listing, then the search will be tighter.
    - Mine reset - If the player is standing one block below the mine and it resets, they will not be TP'd out and will suffocate.  May need to expand the is-in-the-mine check to include one lower block.
    - Mine reset
    - Mine reset - Set a home and TP back, or use /back may fail. May work with an event monitor to get the players out.
    - Mine reset - Not sure if suffocation on mine reset has been fixed yet? Still may exist.



**Build artifacts:**
  Be aware that this build artifact is not production grade and still needs more testing.
  * **prison_v3.5.2-alpha.4.jar**    



## tag v3.5.2-alpha.2 - 2019-11-14

* **New functionality!! Block search!!**
You can now search for blocks to add to the mines!  It uses the BlockTypes and is able
to search on the ID and the enumeration name.  It displays up to 10 at a times and 
provides Next Page and Previous Page controls to page through the results.
If you click on a BlockType, it will suggest the command for adding blocks:
** /mines block add <mine> <blockTypeSelected> %**  All the user needs to do is 
fill in the percent and change the "<mine>" to a valid mine name. 

* **Disabled the checking for updates (temporary)**
Until I can figure out how to get it to work over this fork, it will always 
report that the obsolete version 3.1.1 is a newer version. This also generates in game
messages that will annoy the players.  

* **Tested the manual mine reset under Spigot 1.8.8 and could not reproduce the failure**
I spent some time testing the manual and automatic mine reseting conditions under a 
Spigot 1.8.8 build using the older Prison Plugin v3.1.1 and could not get it to fail. 
I also tested fairly extensively the Prison Plugin v3.5.2-alpha.1 under Spigot 1.8.8 and
could not get it to fail either.  So still not sure what was causing those failures
(could be a conflicting plugin too), but the changes I made appear to be solid too.
I hear a few accounts that others were having issues with Spigot 1.8.8.

**Build artifacts:**
  Be aware that this build artifact is not production grade and still needs more testing.
  * **prison_v3.5.2-alpha.2.jar** 


## tag v3.5.2-alpha.1 - 2019-11-13

First set of changes I'm able to contribute to the project. 

Please note that I'm having issues getting the Gradle unit tests to run.  Once I get them
working I will enable them in the commit below dealing with mine resets.  

* **TP of Players out of the Mines upon Rest** In an effort to help start to address the
known bug [![Mine doesn't teleport to safety #82](https://github.com/PrisonTeam/Prison/issues/82)] 
I rewrote the player teleport function to clean it up and to hopefully address the issue.
I personally was unable to get it to fail under minecraft 1.13.2, but I have reports
that it is failing under mc 1.8.8.  This issue needs to be tested farther to see if it has
any effect on systems that are failing.  Please see 
[![Commit 99baa99](https://github.com/rbluer/Prison/commit/96baa99545cdf61f4de6131d665803ea52aac847)]
and related source code form more information. 

* **New functionality!! Decimals in Block chances now supported!** 
I added support to display and use two decimal positions on block chances. This mostly 
involved using doubles instead of integers in most of the calculations.  Otherwise 
not really much had to change.
See [![Commit 0942811](https://github.com/rbluer/Prison/commit/09428114b8cf434f88b6e9bf6ab1bfba2c48c3a8)] for
more information.


**Build artifacts:**
  I think I spoke too soon on the sponge build artifact; I have no way to test it so 
  probably won't include any future build artifacts unless requested.
  Be aware that this build artifact is not production grade and still needs more testing.
  * **prison_v3.5.2-alpha.1.jar** 

## tag v3.5.1 - 2019-11-11

I updated minimal resources to get a clean gradle build.  I think I have the core
basics figured out on how to use gradle so I can hopefully provide quicker responses
in the future when I need to.

* **Updates to bStats**  The Maven repository was updated and so was the version of
bStats that is being included on the project.  It is now using the current 1.5 version, 
was using 1.3 which is no longer available in the repository and was causing a build
failure for gradle.

* **Update to org.inventivetalent.spiget-update** Updated to version 1.4.2-SNAPSHOT since
the old version 1.4.0 was no longer available. 

**Build artifacts:**
  I think going forward I will include the tagged version in the jar file name. Also, 
  unless someone provides a good reason not to, I will also include the sponge version
  since it is being generated, although at this time I have no means to test that artifact.
  * **prison_v3.5.1.jar** 
  * **prison-sponge_v3.5.1.jar**
  
**Notes for Future Reference:** The build artifacts are generated by running the Gradle command
**gradlew build --info** (--info optional) and can be found in the two files:
 * **Prison/prison-spigot/build/libs/Prison.jar**
 * **Prison/prison-sponge/build/libs/Prison-sponge.jar**

## tag v3.5.0 - 2019-11-08

As soon as I forked this plugin, I tagged it to version 3.5.0 to set a steak in the
ground and to establish the versioning.  I cannot provide build artifacts because
the project will not compile as is.  It's important that for this first tag/version
that I have zero changes and commits.

This is my first GitHub project and also my first exposure to Gradle so I want to 
take my time and do this right and provide good documentation so it is easier
to follow my reasoning for why I'm doing what I am doing.

I decided to go with version 3.5.0 since I do not want to conflict with the 
main project's upcoming v4.0.0. I've jumped to 3.5.x to signify that there is a
new developer taking over and there may be slightly new directions in which 
this project may head.  I'm also thinking about Apache's projects that have used
the jump to the x.5.x version such as what Tomcat had with v5.5.x and v8.5.x; 
different enough to jump ahead on the 
**api version**, but not radically different enough to warrant a new **generation version**.  
