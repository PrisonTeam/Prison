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


# v3.2.9-alpha.1 2021-06-19


* **Set the next version to v3.2.9-alpha.1**


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
  
