
### Prison Documentation 
[Prison Documents - Table of Contents](prison_docs_000_toc.md)

# Prison Troubleshooting


There are a lot of thing that Prison is able to do, but there are even more ways to configure your server with many other plugins.  Sometimes there can be conflicts with other plugins, or issues with the way things are configured.


This document provides some directions on how to troubleshoot and possible solutions.  Topics are broken down in to a Question and Suggestion/Answer format.  Follow the directions on how to proceed.


*Note: this document is a work in progress.*


*Documented updated: 2022-08-30*

<hr style="height:7px; border:none; color:#aaf; background-color:#aaf;">

# Troubleshooting Contents

* [Cannot Mine or Break Blocks within a Mine](#cannot-mine-or-break-blocks)


<hr style="height:7px; border:none; color:#aaf; background-color:#aaf;">



<hr style="height:7px; border:none; color:#aaf; background-color:#aaf;">

# Cannot Mine or Break Blocks

A player has access to the mine, but yet they cannot break any blocks in that mine.


**The Need for Information**

To help better understand your server's environment and settings, our support team may need the following information.  This is a list of items and how to find them, or how to submit them. Some of these details may be needed to help address some of the questions and answers below too.


* **Upgrade Prison to the latest alpha release**: Generally, if you found a problem within Prison, so have others and there is a good chance it's been reported and even fixed.  So if you have an older release of prison, or you think your issue is related to a bug within Prison, then upgrading to the latest alpha may solve it.  The latest alpha release are posted on the Prison discord server, under the #alpha-version channel. To identify the current version of Prison, see the next item.


* **Prison Version information**: This information can quickly allow us to evaluate your setup, which can result in quick solutions to your issues.  In the console you can use the command `/prison version` to view the details yourself.  Or if you need to submit them, then run these two commands and copy and paste the provided URL to the support staff on the Prison discord server.
`/prison support setSupportName <yourName>`
`/prison support submit version`


* **Auto Feature Settings**: If you're using auto features settings, then we will need to know if auto features are enabled, and if auto features is setup to cancel the event or to cancel the drops.  You can tell within the above commands, `/prison version`, or by looking at the autoFeaturesConfig.yml file.  For these two settings, one must be true and the other false.  Both cannoot be the same value.


  **Cancel Event settings:**
  
        cancelAllBlockBreakEvents: true
        cancelAllBlockEventBlockDrops: false



  **Cancel Drops settings:**
  
        cancelAllBlockBreakEvents: false
        cancelAllBlockEventBlockDrops: true

<hr style="height:7px; border:none; color:#aaf; background-color:#aaf;">


#### Confirm Player Access to the Mine:

* **Does the Player has access to the mine?**

    * Prison recommends MineAccessByRank: `/mines set mineAccessByRank help`
        * Make sure the mine is linked to a rank: `/mines set rank help`
    * The next alternative is AccessByPerms: `/mines set accessPermisssion help`
        * Ensure the permission is not a group perm and is accessible through bukkit.  If the perm cannot be accessed through bukkit, then it won't work.
        * You can check that the perm is accessible through bukkit with `/ranks player <player> perms` but they must be online, and it must be listed under the "(bukkit)" perms group.
    * If neither MineAccessByRank and AccessByPerms are used, then access to the mines must be managed through a WorldGuard region, of which, WG must have a lower priority than prison and all the perms must be setup correctly with WG's region and also the permission plugin that you're using.


If you are unable to tell if the player has access to the mine, then access may be an issue.  


To be clear, this question "Does the Player have access to the mine?" is related to personal access and not possible issues with items such as WorldGuard regions being omitted or misconfigured; that's a non-player issue.


If you think there is something wrong with player access, then please contact our support team on the Prison discord server.

<hr style="height:7px; border:none; color:#aaf; background-color:#aaf;">


#### Confirm WorldGuard is Setup Correctly:

If you have Auto Features configured for **Cancel Drop Settings** (see the section under **The Need for Information**), or you are using an enchantment type plugin, then review the following points and questions:


* **If using the MineAccessByRank or AccessByPermisssion settings for your mines**: *Then Prison should have a lower priority than the WorldGuard regions that is setup for that mine.  Otherwise, WorldGuard should have a lower priority than prison.

To confirm, please use this command in the console:
`/prison support listeners blockBreak`


* **WorldGuard Region Configured Correctly**:  If using WorldGuard regions, then they could be causing issues if they are misconfigured.  Your world needs to have a global region with passthrough deny.  Then the actual WG region needs to be setup properly.  Here's what we have been suggesting:

```

```






<hr style="height:7px; border:none; color:#aaf; background-color:#aaf;">






<hr style="height:7px; border:none; color:#aaf; background-color:#aaf;">

