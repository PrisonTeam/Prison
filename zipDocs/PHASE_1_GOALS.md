# Phase 1: The Fixings

Prison shold be at v3.4.0 before starting these tasks.

## Primary Objective
Prepare the 91k-line legacy codebase for a massive V4.0 refactor. In phase 1, we are starting to focus on some of the major parts of prison that has been a problem when new versions of spigot roll out.

**Versioning Goals:** Prison's version is currently at 3.3.0-alpha.19j and should be updated to 3.4.0 for all of the work that needs to be done prior to moving to v4.0.0. After the great purge, the project should be versioned to v3.4.x.

## Current Tasks

1. **NBTs:** The use of NBTs appears to be an issue, especially with Mine Bombs. Prison uses an NBT to mark an item as a mine bomb, and also to mark them with who the original owner is. The use of NBTs within Prison has been a problem since the library that prison uses is very brittle and breaks prison when new versions of Spigot are released and you try to run the latest releases.  Prison also has a Compatibility set of of classes, where it would be nice to move this tagging of mine bombs to that package so the most recent versions of spigot can use the native metadata tagging within bukkit and not the NBT library.  

* The NBT primary class:
    * /prison-spigot/src/main/java/tech/mcprison/prison/spigot/nbt/PrisonNBTUtil.java
* The compatibility package and primary class:
    * /prison-spigot/src/main/java/tech/mcprison/prison/spigot/compat/SpigotCompatibility.java.
* Find a cutoff point:
    * What is the spigot version that has built-in NBT support?  What version is stable?
    * All newer instances of Spigot must use the new standard and NOT nbt-lib-api
    * Only use the nbt-lib-api with older versions of spigot

2. **Hotspot Audit:** Identify "God Classes" (classes over 2,000 lines) that need to be broken down in Phase 1.

3. **Testing Prep:** Identify the 5 most "fragile" methods (like the 80k block explosion logic) to be the first targets for Unit Tests.

4. **Version to v3.4.1:** Version to v3.4.1 to signify that these tasks are complete.


## Optional Tasks that should be completed before wrapping up V3.x

1. **Mine Bombs:** These has been some trouble with getting mine bombs to work properly. The priamry cause has been with issues around the use of NBTs since they don't always appear to work, or to be able to read them properly.  Should try to get the usage of tagging working for mine bombs: a mine bomb id, who was the original owner, and being able to transfer these metadata elements to the generated thrown item, and the armor stands.  Armor stands are used for animation sequences and it would be nice to have them tagged with the mine bomb metadata so they can be identified later, if something goes wrong, so they can be removed from the game.

2. **Sellall & Autosell:** These have been problematic and error prone. A lot of work has been done to them and they are much improved with functionality and stability, but there are still some weaknesses with them.  We should review what should be done before v4.0.  Autosell can sometimes be difficult to enable and is usually tied back to a conflicting configuration setting.

3. **Backpacks:** The first attempt at adding backpacks failed with a lot of lag. They have been "disabled" and I would like to fix the backpack model and get them working.  Might be able to utilize the PlayerCache for persisting the backpack data.

4. **Player Objects:** The player objects within Prison is has been refactored with the number of player types being reduced.  There could still be problems that should be ironed out and maybe create some unit tests too.



## AI Constraints (Rules for Gemini)
* **DO NOT** suggest new features or enchantments yet.
* **DO** point out code smells, high cyclomatic complexity, or missing @Override tags.
* **DO** prioritize performance. If a suggestion might cause lag in a 100-player environment, flag it.
* **STRICT:** Always maintain compatibility with the "Yielding" block-processor logic.

