[Prison Documents - Table of Contents](prison_docs_000_toc.md)

## Prison Build Logs for v3.3.x

## Build logs
 - **[v3.3.0-alpha - Current](changelog_v3.3.x.md)**
 - [v3.2.0 through v3.3.0-alpha.18](prison_changelogs.md)
 

These build logs represent the work that has been going on within prison. 




# 3.3.0-alpha.18 2024-05-20


**Prison-3.3.0-alpha.18.jar**

This version of prison works with Spigot v1.20.6 and Paper v1.20.6.

The whole prison build environment was rewrote and updated to provide for a cleaner and more modern way to use dependencies within Gradle.

In combination with updating most of the libraries and other dependencies, this resolved many issues that were initially preventing prison from working with Spigot v1.20.5 and v1.20.6.


* Gradle updates: Upgraded from v7.3.3 to v8.7.  Had to upgrade 22 times to reach v8.7.  With gradle, if there is a future breaking change in the configs, it warns you in the before the next release.  So incremental upgrading helps ensure the configs are fixed and functional before moving on to the next release.

* Upgraded Spiget from v1.4.2 to 1.4.6.  This checks prison's version for updates.
Had to also rewrite the code using Spiget because the older build tools were having issues handling anonymous classes when building the final jar file with shadow.

* Upgraded shadow from v6.10 to v8.1.1 2
Shadow v8.x could only be used with gradle v8.x

* Placeholders: There was an odd issue with placeholders when a discord plugin was using papi.  For some reason, that plugin was sending the placeholders with null players.  Now checks for nulls.  Since it was a non-standard way of using papi, there may be other issues?

* PlayerCache: Slight modification in getting the keySet so it will not be tied to the original PlayerCache's TreeMap.  This will eliminate the possibilities of a concurrent modification exception if new players are brought online or unloaded.

* Economies: Added a feature for external functions to be able to check if the player has an account.  It's not being used yet, but it can be used to bypass code (and messages) if a player does not have an account.




# 3.3.0-alpha.18 2024-05-20



**Prison v3.3.0-alpha.18 2024-05-20**

This version has been tested and confirmed to be working with Spigot v1.20.6 and Paper v1.20.6.



 **Player Ranks GUI:  Fixed an issue with the code not using the correct defaults for NoRankAccess when no value is provided in the configs.**



 **Obsolete blocks: Marked an enum as @Deprecated to suppress a compile warning.**  This has not real impact on anything.



 **Gradle updates:**

pgraded XSeries from v9.10.0 to v10.0.0

pgraded nbtApi from v2.12.2 to v2.12.4

pgraded luckperms-v5 from v5.0 to v5.4



 **Economy: Added a feature to check if a player has an economy account.**

urrently this is not being used outside of the economy integrations, but it can be used to help suppress initial startup messages where players do not have an account, which will help prevent flooding a lot of messages to the console for some servers.



 **Player Cache: There was a report of a concurrent modification exception.**

his is very rare and generally should not happen.

he keySet is part of the original TreeMap collection, so the fix here is to take all keys and put them in a new collection so they are then disconnected from the original TreeSet.

his will prevent a concurrent modification exception if there is an action to add or remove users from the user cache, since the user cache remains active and cannot be locked with a synchronization for any amount of time, other than then smallest possible.

he standard solution with dealing with this TreeSet collection would be to synchronize the whole activity of saving the dirty elements of the player cache.  Unfortunately, that will cause blocking transactions when player events try to access the player cache.  Therefore it's a balance game of trying to protect the player cache with the minimal amount of synchronizations, but allow the least amount of I/O blocking for all other processes that are trying to use it.

opefully this is sufficient to allow it all to work without conflict, and to be able to provide enough protection.



 **Gradle: Removed a lot of the older commented out settings.**

ee prior commits to better understand how things were setup before, or for references.



 **Gradle: A few more adjustments to add a few more items to the libs.versions.toml.**




 **Placeholders:  The placeholder api call from PlaceholderAPI is passing a null OfflinePlayer object.**

ot sure why this has never been an issue before, but added support for null OfflnePlayers.




 **Spiget: Updated the way prison handles spiget by now submitting a task with a 5 second delay.**

he messages are more helpful now.

his also moves it out of the SpigotPrison class.



 **Upgraded John Rengelman's shadow, a gradle plugin, from v6.1.0 to v8.1.1**





 **Upgrade gradle from v7.6.4 to v8.7**

 Upgraded from: v7.6.4 -> v8.0 -> v8.0.2 -> v8.1 -> v8.1.1

 		-> v8.2 -> v8.3 -> v8.4 -> v8.5 -> v8.6 -> v8.7

 v8.3 required a configuration change due to `org.gradle.api.plugins.BasePluginConvention` type has been deprecated and will be removed in gradle v9.x.  This is impacting the use of the `build.gradle`'s `archivesBaseNamme`.  This is being replaced by the new `base{}` configuration block.

 v8.3 also required other config changes.

 



 **Upgrade spiget from v1.4.2 to v1.4.6**

 Was using a jar with v1.4.2 due to their repo going down frequently.

 Switched back to pulling through maven and got rid of jar.

 


 **Upgrade gradle from v7.3.3 to v7.6.4**

 Upgraded from: v7.3.3 -> v7.4 -> v7.4.1 -> v7.4.2 

 		-> v7.5 -> v7.5.1 -> v7.6 -> v7.6.1 -> v7.6.2 -> v7.6.3 -> v7.6.4

 Preparing for Gradle v8.x

 Around v7.5.1 required a change to auto provisioning

 


*v3.3.0-alpha.17a 2024-04-29**



 **GUI settings: Update them to remove unused stuff.**



 **GUI Tools messages: refined the messages and hooked them up.**



 **Initial setup of the GUI tools messages that are at the bottom of a page.**

etup the handling of the messages and added the messages to all of the language files.

upport for prior, current, and next page. Also c

 **Update the plugin.yml and removed the permissions configs since they were generating errors (lack of a schema) and the perms and handled through the prison command handler.**




 **CustomItems: Fixed an issue when CustomItems is a plugin on the server, but the plugin fails to load.**

herefore the problem was fixed to allow a failed CustomItems loading to bypass being setup and loaded for prison.

CustomItems.isEnabled()` must exist and return a value of true before the integration is enabled.



 **XSeries XMaterials: Update to XSeries v9.10.0 from v9.9.0.**

ad issues with case sensitivity when using `valueOf()`, which was changed to `matchXMaterial().orElse(null)` which resolves a few issues.

Materials v9.10.0 sets up support for spigot 1.20.5. There may be more changes as spigot stabilizes.

he issue with using `valueOf("green_wool")` would not find any matches since the enum case must match the string value exactly.  So `valueOf("GREEN_WOOL")` would have worked.  This was fixed to help eliminate possible issues with configuring the server.



 **Auto features: normal drop processing: Added a new feature to check inventory for being full, and if it is, then display the messages.**




 **Auto features: Inventory full chat notification: bug fix.  This fixes using the wrong player object.**

t now use prison's player object so the color codes are properly translated.



 **Placeholders: bug fix: When using a search from the console which included a player name, it was generating an invalid cast to a SpigotPlayer object when it wasn't related to that class due to the player being offline.**



 **GUI: ranks and mines: setup and enable a new default access block type that can be used if that rank or mine  has not been specifically specified.**



 **GUI: tool bar's prior page and next page: Suppress the page buttons if there is only one page worth of gui content. **



 **GUI: Player ranks: Fixed a bug where clicking on a rank in the player's gui was trying to run an empty command, which was generating an invalid command error.**

gnores the command running if the command is either null or blank.




 **Update to plugin.yml since some soft dependencies were missing.**



 **Economies: fixed the display of too many economy related messages, including eliminating logging of messages for offline players.**

he vault economy check for offline players, will now only show one informational message if a player is not setup in the economy.



 **GUI Player ranks: The setting for Options.Ranks.MaterialType.NoRankAccess was not hooked up properly so it was not really working.**

he config creation was wrong.  Also fixed the code that was generating the gui.



 **RankPlayer and topn ranking: This may not have an impact overall, but for both the default and prestiges ladders, they are defaulting to a value of -1 when performing a comparison between players.**



 **Update privatebin-java-api to a newer release that now does a better job with a failure to use the correct protocol.**

t identifies what TLS version is being used, and if TLSv1.3 is missing, then it will indicate that the java version needs to be updated.

s a fallback, if the privatebin cannot be used, it is now using the older paste.helpch.at service.  But if it does, the resulting documents are not purged and not encrypted.



 **Economy: EdPrison's economy. Added support for use of EdPrison's economy and custom currencies.**

his will allow prison to use EdPrison's economy does not also use another established economy that is accessible through vault, or multi-currency.



