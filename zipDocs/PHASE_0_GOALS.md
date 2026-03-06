# Phase 0: The "Rehydration" & Purge

## Primary Objective
Prepare the 91k-line legacy codebase for a massive V4.0 refactor. We are in a "Look but don't touch" state for features, focusing purely on stability and cleanup.

## Current Tasks
1. **The Great Purge:** Identify and remove all commented-out code blocks, unused imports, and dead methods.
2. **Hotspot Audit:** Identify "God Classes" (classes over 2,000 lines) that need to be broken down in Phase 1.
3. **Dependency Cleanup:** Verify `libs.versions.toml` and align all modules to the same library versions.
4. **Testing Prep:** Identify the 5 most "fragile" methods (like the 80k block explosion logic) to be the first targets for Unit Tests.


## Optional Tasks that should be completed before wrapping up V3.x
1. **Sellall & Autosell:** These have been problematic and error prone. A lot of work has been done to them and they are much improved with functionality and stability, but there are still some weaknesses with them.  We should review what should be done before v4.0.  Autosell can sometimes be difficult to enable and is usually tied back to a conflicting configuration setting.
2. **Backpacks:** The first attempt at adding backpacks failed with a lot of lag. They have been "disabled" and I would like to fix the backpack model and get them working.  Might be able to utilize the PlayerCache for persisting the backpack data.
3. **Player Objects:** The player objects within Prison is has been refactored with the number of player types being reduced.  There could still be problems that should be ironed out and maybe create some unit tests too.
4. **MineBombs:** These have been some new features added to mine bombs and they don't always work well and can glitch at times.  Should review MineBombs before v3.x is finalized.


## AI Constraints (Rules for Gemini)
* **DO NOT** suggest new features or enchantments yet.
* **DO** point out code smells, high cyclomatic complexity, or missing @Override tags.
* **DO** prioritize performance. If a suggestion might cause lag in a 100-player environment, flag it.
* **STRICT:** Always maintain compatibility with the "Yielding" block-processor logic.

