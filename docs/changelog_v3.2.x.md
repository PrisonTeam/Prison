[Prison Documents - Table of Contents](prison_docs_000_toc.md)

## Prison Build Logs for v3.2.x

## Build logs
 - **[v3.2.5-alpha - Current](changelog_v3.2.x.md)**
 - [v3.2.0 - 2019-12-03](prison_changelog_v3.2.0.md)&nbsp;&nbsp;
[v3.2.1 - 2020-09-27](prison_changelog_v3.2.1.md)&nbsp;&nbsp;
[v3.2.2 - 2020-11-21](prison_changelog_v3.2.2.md)&nbsp;&nbsp;
[v3.2.3 - 2020-12-25](prison_changelog_v3.2.3.md)&nbsp;&nbsp;
[v3.2.4 - 2021-03-01](prison_changelog_v3.2.4.md)**
 

Greetings!  I'm delighted that you are interested in the build logs for the
Prison plugin.  I'm wanting to provide a more formal documentation as to what 
is going on in each build so you have a better idea if it may be something 
that you need.


# v3.2.5-alpha.1 2021-03-04


* **Issue with XMaterial and parsing an item stack.**
Trying to at least catch an exception so it does not flood the console.  The reported item being picked up does not match what's actually going on.  tech.mcprison.prison.spigot.compat.Spigot18Blocks.getBlockType(ItemStack).
Should update XMaterial too.


* **Backpack conflict fix**
Fixed a conflict of backpacks when more peoples are using it at the same time.


* **Update the MineLiner so it can be removed.**
Added a remove command that will remove a specific edge. Added a removeAll that will remove all liners.


* **Prevent a NPE when getPrisonBlock cannot return a value.**
Auto features when getting the block to process.


* **Remove the temporary details on what was causing the issues with TE issues.**


* **Update the documentation for TE explosion events and Crazy Enchant's BlockExplodeEvents.** 
Kept some detailed information in that document that was used to identify the nature of the issue when it wasn't working correctly.  This will be removed soon, but by committing it will preserve a copy in the project's history.


* **Update the spigotmc.org doc to remove spaces from the file names.**
Renamed the files to remove them.  This is to clean up the use of %20 which is used to represent spaces.


* **Moved a few of the doc files in to the doc directory.**
This is important for supporting the github pages.


* **Prison Backpack autopickup support**
Prison backpack now supports autopickup and there're options for it in the backpacksconfig.yml.


* **Prison BackPack setting back default size bug fixed**
Fixed a but on player join that was setting the backpack size back to the default one for some reasons.


* **Prison Backpack SellAll support**
Fixed some issues with Prison Backpacks API and also added support for sellall. There're some editable options in the sellallconfig.yml for it.


* **NEW FEATURE: Now supports mcMMO within the mines!**
Checks to see if mcMMO is able to be enabled, and if it is, then call it's registered function that will do it's processing before prison will process the blocks.
This adds mcMMO support within mines for herbalism, mining, woodcutting, and excavation.


* **Update the spigotmc.org's text for prison's resource page.**  


* **v3.2.5-alpha.1 2021-03-01**



# **v3.2.4 2021-03-01**
  Release v3.2.4.



# V3.2.3 2020-12-25 
**Merry Christmas!!**
Release of next bug update.



# V3.2.2 Release - 2020-11-21


