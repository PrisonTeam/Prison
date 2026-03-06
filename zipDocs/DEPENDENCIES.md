Project: Prison

Current Target: Java 8 / Legacy Spigot

Migration Target: Java 25 / Bukkit API v1.21.10 (v4.0)
1. Root Build (build.gradle)

These are the orchestrating plugins governing the entire build process.

    Shadow Plugin

        Current: com.github.johnrengelman.shadow:8.1.1

        Migration Flag: Version 8.1.1 supports Java 17 well, but you may need to bump to the latest 8.3.x branch to ensure smooth compilation and shading when moving your Eclipse project facets to Java 25.

2. prison-core

The pure logic and data management layer.

    Google GSON

        Current: com.google.code.gson:gson (Version dictated by Spigot or explicit declaration)

        Migration Flag: Ensure this is explicitly bumped to 2.10.1 or higher in v4.0 to properly handle Java record serialization if we refactor your data models.

    JUnit (Testing)

        Current: junit:junit:4.12

        Migration Flag: MAJOR BUMP REQUIRED. JUnit 4 is entirely obsolete for modern testing. Phase 0 must include migrating the test suite to JUnit 5 (Jupiter) 5.10+.

3. prison-spigot & prison-spigot-alt

The Bukkit/Spigot implementation layers handling the block processing and economy hooks.

    Spigot / Bukkit API

        Current: Legacy 1.8.x - 1.12.x APIs.

        Migration Flag: MAJOR BUMP REQUIRED. Must be updated to target the Bukkit v1.21.10 release. This will break your block-state manipulation logic due to the removal of legacy block IDs (e.g., Material.WOOL with byte data) in favor of the modern BlockData API. The yielding 80k block processor will need its block modification calls completely rewritten.

    Vault API

        Current: net.milkbowl.vault:VaultAPI (Likely 1.7.x)

        Migration Flag: Safe to keep, but ensure it is compiled against the modern API standard.

4. prison-worldguard6

Legacy region protection hooks.

    WorldGuard API (v6)

        Current: com.sk89q:worldguard:6.1.1-SNAPSHOT (or similar 6.x)

        Migration Flag: DEPRECATION WARNING. WorldGuard 6 is completely incompatible with modern Minecraft. For the v4.0 update, this entire sub-project should be archived or deleted.

5. prison-worldguard7

Modern region protection hooks.

    WorldGuard Core & Bukkit

        Current: com.sk89q.worldguard.worldguard-libs:core:7.0.4

        Migration Flag: BUMP REQUIRED. Version 7.0.4 will not support the Bukkit v1.21.10 API. You must bump this to WorldGuard 7.0.9 or higher, which introduces new adapters for modern Spigot versions.

6. prison-mines, prison-ranks, prison-sellall

Feature modules relying on the core and API.

    Dependencies: Primarily internal project dependencies (project(':prison-core'), project(':prison-api')).

    Migration Flag: No external library bumps needed, but these will inherit all the breaking changes from the Spigot v1.21.10 and Java 25 upgrades applied in the core and implementation modules.


    