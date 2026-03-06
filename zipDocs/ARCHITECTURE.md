# Prison Project Architecture

## Overview
Prison is a high-performance Minecraft prison management suite consisting of ~91k lines of Java. It is designed for scale, handling massive world edits (80k+ blocks) and high-frequency transactions with zero server lag.  Prison manages the players, their ranks and related leveling, along with the building and management of mines. The players can mine blocks, which earns them money and experience, which allows them to rankup.

## Module Responsibilities
* **prison-core:** The "brain." Contains business logic, player data models, and currency caching. 
    * **STRICT RULE:** No Spigot/Bukkit API dependencies allowed here.
    
* **prison-mines:** Feature-specific modules that build upon the core and API. 
    * **STRICT RULE:** No Spigot/Bukkit API dependencies allowed here.
    
* **prison-misc:** These are source code stubs that are used only for compiling Prison source code, since we do not have access to these various plugin's API jars.  These don't require any functionality, they just need to provide class and method signatures.
    * **STRICT RULE:** The Sigot/Bukkit API dependencies can be used in this module.
    
* **prison-ranks:** Provides player rank logic and leveling. Feature-specific modules that build upon the core and API.
    * **STRICT RULE:** No Spigot/Bukkit API dependencies allowed here.
  
* **prison-sellall:** Provides the ability to sell their inventory based upon shop item lists and prices. This also handles autosell capabilities that allows the mined blocks to be sold instantly without going in to the player's inventory.
    * **STRICT RULE:** No Spigot/Bukkit API dependencies allowed here.
  
* **prison-spigot:** The implementation layer. Handles block-breaking, explosions, and all `org.bukkit` related interactions.
    * **STRICT RULE:** The Sigot/Bukkit API dependencies can be used in this module.

* **prison-spigot-alt:** Ignore. This was an experiment to force a slightly different spigot build.

* **prison-mines-legacy:** Ignore. This is obsolete and outdated code that is kept for no good reason.

* **prison-sponge:** Ignore. This is an obsolete support for the sponge flavor minecraft. It is seriously lacking all of the same classes that prison-spigot currently has, so it won't be able easily be updated.

* **prison-worldguard6:** Not sure if this is actually used yet. This was going to provide hooks to support WorldGuard v6 API calls so prison can perform more complex operations during a mine reset by basically in bedding scripts in the mine reset instructions. 
    * **STRICT RULE:** The Sigot/Bukkit API dependencies can be used in this module.

* **prison-worldguard7:** Not sure if this is actually used yet. This was going to provide hooks to support WorldGuard v6 API calls so prison can perform more complex operations during a mine reset by basically in bedding scripts in the mine reset instructions. 
    * **STRICT RULE:** The Sigot/Bukkit API dependencies can be used in this module.


## Critical Design Patterns
* **Self-Aware Batching:** Our block-state updates (explosions/resets) use a time-sliced execution model. They yield the main thread after X ms to prevent tick lag.

* **Currency Caching:** We use a `HashMap` buffer for all economy transactions. Payments are flushed to the provider (Vault/Economy) in batches, not per block break.

* **Data Flow:** All data moves from `prison-spigot` (events) -> `prison-core` (logic/cache) -> Database.

* **Prison Command Handler:** Prison has a very advanced command handler that is used with every prison command. It provides for command context and help when the user's need help, which is the base command followed by `help`.  The help lists all parameters, details about the command, and even links to documentation.  Commands can be controlled through permissons to restrict players from using and seeing admin commands. The Prison commands are heigherarchial where root command has children, such as `mines`, `ranks`, and even `prison`. Issuing a root command will show all direct children. The command handler also supports auto complete on the commands, but not the parameters.  The Commands are defined within the source code through the use of various annotations.  To find out more about a given command within Prison, inspecting the command parameters will provide a very rich set of information on usage and behaviors.

* **Auto Features:** Auto Features is an enum based set of nested configuration parameters that controls everything related to how Prison behaves with BlockBreakEvents, and other supported plugin's block breaking events such as explosions. There are many settings that controls auto pickup, smelting, fortune, and secondary drops. These settings start off as nested enums, but are persisted in the server's plugins/Prison directory upon startup so all of the settings can be customized.

* **Placeholders:** Placeholders are a dynamic set of tags that can be used in chat, in messages, and other plugins to refer to the player, ranks, mines, and other Prison related features. Prison uses the bukkit chat event to handle chat related placeholders, but also hooks in to PlaceholderAPI plugin to handle placeholders that are embedded within other plugins.  Each module has many placeholders, and some are dynamically generated upon server startup.  On a normal server, when taking in to consideration of all of the ranks, prestige ranks, and mines, there can be thousands of different placeholders that prison will handle and resolve. The core foundation of all placeholders is based upon an enumeration that defines all placeholders.  This ensures that the internal usage of placeholders is strongly typed at compile time. Many placeholders extends the placeholder's name to represent a dynamic set of items. For example, an enumeration `prison_mines_name` is extended by adding a mine name to it: `prison_mines_name_a` or `prison_mines_name_k`. All placeholders have abbreviations that can be much shorter, such as for these mine names: `prison_mn_a` and `prison_mn_k`.  There are about 307 placeholder base types, and when extended to represent mines, ranks, prestiges, etc, there can be thousands of potential placeholders.  Placeholders are classified by category such as: Player, ladders, ranks, playerranks, statsranks, statsplayers, statsmine, mines, mineplayers, playerblocks, statsmines, and custom. Prison also supports custom placeholders where an admin can define a placeholder name, then assign as much text to it as they want, including other prison prison placeholders and placeholder from other plugins.  Therefore a single custom placeholder can replace many individual placeholders which can make it easier to setup other plugins such as holigraphic displays.  Prison has an advanced placeholder caching system that can speed up the response times, and reduce lag, especially if another plugin is spamming prison placeholders.  Prison placeholders also support placeholder attributes, which allows for dynamic control over the formatting of placeholders.  Example would be with numeric placeholders, you can use a placeholder attribute to control the formatting of that number, such as thousands seperators, decimal positions, colors, etc. There are classes of placeholders that are status bars, and through placeholder attributes you can control the colors of the bar, what characters are used for the bars, and the size of the bar.  So there is a ton of control over all of the placeholders within prison.

* **Debug Mode:** Prison has in place a pretty extensive debug mode that can dynamically generate log entries that can track and explain what is happening within Prison most of the time.  There is a global setting to enable it within config.yml, or it can be toggled on or off with `prison debug`.  There are advanced settings to even target specific players and for x-number of messages.  This is very helpful when admins report problems with their servers.

* **Prison support:** Prison has the ability to gather all settings and it's states, and export them to a secure third party website so the Prison support staff can review the server's settings.



## Tech Stack
* **Language:** Java 8 (Current) -> Targeting Java 21/25 for V4.0.
* **Build System:** Gradle (Multi-module).
* **Target Environment:** Spigot/Paper 1.8.8 through 1.21.