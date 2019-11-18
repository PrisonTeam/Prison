
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
