
### Prison Documentation 
[Prison Documents - Table of Contents](prison_docs_000_toc.md)

## Prison - Getting Help

This document provides some important information on how to find help in setting up your prison server, and ultimately, how and where to ask for help.

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">

# Prison v3.1.1 and Earlier

There is zero support available for these earlier versions of Prison.  It is therefore highly recommended that you upgrade to the latest release of Prison.  Once a version of prison is released, we cannot go back and apply any fixes; fixes will be applied to the next release.


My suggestions are based upon what I have seen within the code for Prison, and best guesses on how the older versions **may** have worked.  These are guesses.  Proceed carefully and make backups at each upgrade to ensure you can retry a step if something should go wrong.  - Blue


To upgrade prison, the process should be rather simple.  But there are general steps that you must follow to help ensure a smoother transition to the latest release.  


It is highly suggested that you should first backup your whole server, especially the data within your plugins folder.  Please be aware that upgrading Prison may require you to upgrade other pugins that you have, and those plugins may require other plugins to be upgraded.  


DO NOT just install the latest plugin(s) you find on the internet!  Most plugins have specific versions of Spigot/Bukkit/Minecraft that they will work with!  Get them only from trusted servers such as spigotmc.org, bukkit.org, or etc...  Those sites should have multiple versions and should identify what server engines they are compatible with.  


If you want to also upgrade to a newer version of the server software that you are running, such as spigot, bukkit, paper, etc..., first upgrade the plugins for your current version of the server.  So if you are running Spigot 1.8.8 and a plugin has a newer version available that works with 1.8.8, install that first and then start the server and make sure everything is working well.  Usually if there is a major change in a plugin from one version to the next, the "last" version may have code to "convert" your data to prepare for the next higher version. If there is an important intermediate step that will convert your data, or require you to make major changes, there should be some notes in documents somewhere.  Spending a few minutes reading the release notes on these websites could save you hours of work trying to recover from a messed up upgrade.  Remember to make backups!
   
   
For prison releases prior to Prison v3.0.0, the data structures they uses to store all the data on the file system was different.  I do not know how it was different, I just know it was.  I saw there was remains of a conversion utility in Prison v3.0.0 that I **think** was able to convert Prison v2.x data to Prison v3.x formats.  If you are upgrading from Prison v2.x it is VERY IMPORTANT that you first upgrade to Prison v3.0.0!!  Once up start up that server, use the command **/prison** and confirm there is a command **/prison convert**, and if there is, run that command to convert your old data to the new Prison v3.0.0 format.  Make sure you take backups BEFORE and AFTER upgrading your data!  Also do a "clean" shutdown on prison v3.0.0 to finalize the changes.


It needs to be understood that changes made to mines, ranks, ladders, or player data, in versions of Prison prior to v3.2.0 did NOT save those changes until the server shutdown.  So if the server should happen to crash, the changes could have been lost.  So this is important to understand, because if you are upgrading from an older version of prison to Prison v3.0.0, a version prior to Prison v3.2.0, then you MUST ensure the server shuts down cleanly or the conversions and/or changes to the prison data may not have been written to the server file system.  After you shut down the server, if performing an upgrade to v3.0.0, please review all the files under the plugins/Prison directory and make sure the last modify date reflects when you shut down the server.  If the files have not been updated, try restarting the server, and maybe make a change to mine or rank to force a save when the server shuts down.  I cannot help with this process other than these suggestions.  Sorry.


Once you are running Prison v3.x.x then you can safely upgrade to Prison v3.2.0, or better yet, to Prison v3.2.1 (or the pre-release edition).



<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


# Prison v3.2.1 and it's Pre-Release Versions


To try to help solve issues with Prison, and to help rule out configuration problems with other plugins, a lot of effort has been put in to the Prison startup screen, and the **/prison version** command.  These contain a lot of useful details that can help get you running Prison, and can save the developers a ton of time too.

![Prison Startup Screen 1](images/prison_docs_013_Prison_Help_startup_1.png "Prison Startup Screen")
![Prison Startup Screen 1](images/prison_docs_013_Prison_Help_startup_2.png "Prison Startup Screen")


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



** incomplete - Work in progress **

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


