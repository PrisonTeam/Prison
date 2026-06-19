# Phase 0: The "Rehydration" & Purge

## Primary Objective
Prepare the 91k-line legacy codebase for a massive V4.0 refactor. We are in a "Look but don't touch" state for features, focusing purely on stability and cleanup.

Prison has been stuck at v3.0.0.x for the longest time.  I'm wanting to move prison to v4.0.0, but there are some major house keeping tasks that need to be done first.  During these cleanups, prison will be versioned to v3.4.x as these cleanups happen.

These phase document, this being phase_0_goals, will try to organize various tasks with the final task being the version bump.

## Current Tasks - In progress - Preparing for v3.4.0
1. **The Great Purge:** Identify and remove all commented-out code blocks, unused imports, and dead methods.

2. **Dependency Cleanup:** Verify `libs.versions.toml` and check to see if there are any updates available.

3. **Version 3.4.0:** Change version to 3.4.0.


## Optional Tasks that should be addressed
1. **Work on supporting documentation:** Update the road map documentation to provide a more focused number of tasks to include in each of the near future releases.  This will help keep the tasks focused and on track.

2. **Bugs:** Bug fix any issues as they surface.


## AI Constraints (Rules for Gemini)
* **DO NOT** suggest new features or enchantments yet.
* **DO** point out code smells, high cyclomatic complexity, or missing @Override tags.
* **DO** prioritize performance. If a suggestion might cause lag in a 100-player environment, flag it.
* **STRICT:** Always maintain compatibility with the "Yielding" block-processor logic.

