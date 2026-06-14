
# Prison - TODO Listings

These are general items that are needing to be added to prison. These descriptions are either just a high level thought that is not too well developed, to very detailed ideas.

These ideas run the full range of the spectrum of what defines prison, to what prison should become.

These lists should be kept to simple bullet lists without deep and long paragraphs that ideas could get lost in.


## Mines

* **Mine Areas:**
     * A Mine Area surrounds a mine and is 3-dimensional.  
     * It is a "protected" area that has special effects that can be configured and customized per mine, either by rank associations (prestige ranks, vanity ranks, or donor ranks), or by perms.
     * Effects can be potion effects, like see in the dark, speed, feather fall, breathing under water, fire resistence, etc.
     * Effects can be admin like features like fly, jump, anti-gravity (like fly, but only drift) or even local teleport spots. Could be vanish or invisibility.
     * Effects can bonuses to mining, selling, XP, etc...
     * Effects can be no-pvp or pvp enabled.
     * A mine area provides special behaviors and feature to the area around a mine, and more importantly, with the mine too.


* **Mine Fragments:**
    * A Mine Fragment is just like a normal mine, but it's defined within a mine.
    * The current mine structure is a dimension on size, width, depth, and height.  Plus an anchor point where those dimensions start.  Therefore a mine is defined by the blocks that are included within this cuboid shape.
    * A fragment would be like a segment.
    * Fragmenting the mine, is just a way to create a collection of such cuboid shapes for a single mine.
    * All mine fragments must be contained within a Mine's area.
    * Can allow disconnected fragments, where fragments do not touch another fragment.
    * Fragments can be placed next to each other to form one larger mine.
    * Fragments can be placed within other fragments, or they can overlap other fragments. The last fragment to render "wins" and replaces the prior fragment's blocks that were placed.
    * Fragments could be a better way to implement layering within a mine.
    * You can have one fragment in a mine (a traditional mine), or you can have thousands.
    * Fragments can be combined to form complex shapes, that were not possible with a standard cuboid mine.


* **Mine Bombs:**
    * Mine bombs already exist within prison, but this is to expand their features.
    * Go over the core code to ensure there are no hidden bugs, especially for different versions of prison.
    * Add more bomb explosion shapes.
    * Add more bomb animations for when a bomb is counting down.
    * Add more general features, such as explosion effects that impact other players, including the player who set off the bombs
        * Effects such as damage, payouts to everyone within a range, hunger, low hearts, or even slow.
        * Could have a damaged backpack effect where a player's inventory is lost when mining for x minutes after an explosion, which could be attributed to a hole in their packs.
    * Add effects such as potion effects when deploying the bombs, after the bomb goes off, or during.
    * Example can be no damage, fly, or jump for 30 seconds after a bomb goes off.
    * Other effects can be a "party bomb" mode where the whole acive server can be teleported to that to watch the explosion, then they have 5 minutes afterwards to use that mine before they are returned to where they were.
    * Another type of "party bomb" could be one where the players can be hurt if they are too close to the bomb, so they have to run away from it.  Or they have to mine so many blocks to protect themselves.
    * Another feature of a bomb can be like a temporal displacement where the players within a radius of the explosion can be randomly teleported to other mines and they can mine there for 1, 2, or 5 minutes before being returned to the current mine.  Kind of like a "claim jummper bomb".


## Ladders & Ranks

* **Dyanamic Ranks:**
    * Instead of predefining every rank on a ladder, such as prestiges, you can define a ladder to have dynamic ranking.  This means you setup the ladder characteristics, and all of ranks will be generated for you.
    * The purpose is to prevent the need to manually create hundreds, or thousands of ranks. And to prevent the need to have a config setting for each of these ranks.
    * Those ranks will not have a specific setting, so therefore, you cannot directly adjust each rank.
    * Some rank settings will allow for "milestone" features.  Such as special bonuses paid out every 10 ranks.  Or special perm given to players once the reach a special rank.
    * Basically, the way these will work, is that instead of loading these ranks from a config file, they will be auto generatated at startup, based upon the settings.  So light weight ranks when dealing with settings.


## Inventory


* **Prison Backpacks:**
    * Backpacks are dynamic chests, single or double, that can hold additional items.
    * Prison backpacks will take that concept to the next level.
    * A player can potentially have as many backpacks as you want.  But with twists.
    * You can customize how backpacks can be used, such as perms, quantity, size, based upon ladders or ranks, etc... Even allow players to buy backpacks, and have options to expand them through purchases, or through Player Quests.



## Player Features


* **Player Quests:**
    * A Player Quest is an activity that Prison can track and will have a goal for completion.
    * Player Quests will be added to rank ups.  So instead of requiring just so many coins to rank up, you will also be able to add quests, such as mine 1,000,000 blocks in that new mine. Or spend one hour mining in that new mine.  This can prevent friends from just giving the players the money to rankup, or using a bonus mine to earn money faster.  This can prevent cheating, or bypassing the game play.
    * A quest can be any trackable feature within prison.
    * A quest can even consist of other quests, such as a compound quest.
    * Examples would be mining so many blocks, mining specific blocks, mining blocks in a specific mine.
    * Examples can be time spent mining, time spent mining in a specific mine, or even time spent AFK in a certain mine, or time spent online just playing prison or hanging out with friends.  Can even use time spent within certain areas, or mines, or mine area, or regions within the game, such as the shop, or spawn.
    * Examples could be the number of times a player joins prison, number of days a player has been playing prison, number of concecutive days a player has been playing prison.  It can also be use to count the number of rank ups on specific ladders.
    * Most of these trackable features are already active in prison right now.
    * Example when used with ranking: 1,000,000 coins, 20,000 cobble stone, 50 diamonds, 2.5 hours mining in the mine.


* **Player Mines:**
    * Player Mines are personalized mines that a player can buy or earn.
    * A Player Mine will have features that can be expanded, or ranked up.
    * A player mine can have features that can be added to the mine.
        * Example would be the mine size: Can buy one width wider, increased length, depth.
        * Can buy ladders, reset quotas, mine liners.
        * Can add night vision, fly, feather fall, no damage, no hunnger, etc... 
        * Can add new blocks to add to the mine, and control which blocks are actually spawned including their spawn rate.  Basically, a player can buy gold-ore, where level 1 would be 1% spawning, level 2 would be 5%, then 10%, 15%, etc up to 100% spawning.
        * Can add forutune multipliers, and enchantments, and potion effects.
        * Can even add pets, or minions who could help the player mine, even when offline or AFK.
    * Basically there can be a whole ecosystem of things a player can earn, be gifted, or buy to enhance their personal mines.
    * Can invite and allow friends to use their mines too. 
    * Can charge the friends a use tax where they can earn part of what their friends earn.
    * For sizing, could have global setting that can control how large a personal mine can become.
    * Movable to new locations.  Prison has the ability to move mines right now, so this could be a bonus for personal mines, such as moving a mine around within a player's plot, or even changing plots.




## Tools


* **Prison Enchantments:**
    * Enchantments in prison will give tools, weapons, and armor new enchantments
    * The list of enchantments can be extensive
    * Can work outside of mines
    * Can tie enchantments to perms, ranks, ladders, mines, etc...
    * Will have a whole ecosystem for leveling up effects
    * Effects can be potion effects, enchantments, custom behaviors
    * 




## Prison Core


* **Relational Database Support:**
    * Add Database support to prison
    * Initially it will be optional, but will be forced at a future version, such as v4.0.0 or v4.1.0
    * Initially support sqlLite automatically so there would be no config options
    * Add support for other databases such as MySQL, Mariner, etc... 
    * May use hybrid formats on some data types, such as regular fields, then have a json blob for everything else.  Not every feature for a rank, mine, etc, should have it's own field or table within a relational database.  Like block constraints.
    * Provide backup and restore features.
        * Backup to JSON data structures
            * Backup full tables, or all data
            * Backup key data, such as an individual player.
        * Restore from backups
            * Restore full tables, or all data
            * Restore specific mines, ladders, players, etc
            

