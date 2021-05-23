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



# v3.3.0-alpha.4 2021-05-23


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
  
