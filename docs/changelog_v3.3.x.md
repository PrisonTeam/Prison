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



# v3.3.0-alpha.3 2021-05-12

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
  
