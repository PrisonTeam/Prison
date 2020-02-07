
## Prison Known Issues and To Do's for v3.2.x

This document is intended to keep track of known issues and also provide for
a short list of To Do's. This list is intended to help work through known
issues, and/or to serve as items that should be added, or fixed.


## To Do Items

* **Tab Completion**
Hook up tab completion on the prison commands.

* **Better logging of major events**
Need to log major events such as rankups, both to the server log, and also
to the community.

* **Block Types for Specific Versions of Minecraft**
Add in support for the loss of magic values, and also provide for newer block
types too.  Basically have a minecraft version selector that can 
tailor the list of available block types that can be used, based upon the
minecraft version that is running.

* **New Feature: Allow reset durations for each mine instead of just globally**

* **New Feature: Upon block break events, log block changes**
This will allow dynamic live tracking of mine stats, such as how many blocks
remain and relating percentages.  The new async processing will enable this
to actually track individual blocks.

* **New Feature: Add Place Holders for Mine related items**
Examples would be place holders for all mines, and their stats such as
size, dimensions, percent remaining, reset duration, time left until reset,
players currently within the mine, ect...

* **Possible new feature: Track how many blocks are mined from the mines**
Stats on how many blocks are mined from each mine. May be bad to track,
but could open the door to interesting stats.

* **Possible new feature: Track how many blocks a player mines, including types**
Stats could be interesting over time, and could also be used for in game
bonuses and rewards and incentives.

* **New Feature: List all registered plugins**
To better support server owners when they have issues with Prison, it would 
be very helpful if /prison version would list all registered plugins in
a concise listing. This could simplify 

* **Redesign the save files to eliminate the magic numbers**
Most of the save files within prison, for players, ranks, and ladders, are
using magic numbers which is highly prone to errors and is considered 
very dangerous.  Also prison would benefit from a redesign in file layout
to improve efficiencies in loading and saving, not to mention greatly reduce
the complexities within the actual prison code, which in turn will help 
eliminate possible bugs too and give tighter code.


* **Eliminate support for Sponge**
It's not being used, so eliminate it and allow prison to possibly eliminate the
extra layers of indirection it currently has to improve performance and to 
possibly reduce the possibilities for errors. 


## tag v3.2.1-alpha.3 - 2020-02-06

* **Some block types may not work for 1.15.x**
Since prison is not currently using the correct block names for 1.15.x, some
block types may not work. Prison is still using magic numbers for the 
block types and those no longer work for 1.15.x.  Symptom would be that
you set a block type such as birch block, but with the loss of the magic 
number, it will revert back to just an oak block.

* **Information: Setting the correct currency for Prison**
The current prison plugin is using java internals to set the currency symbol.
As such, currencies within prison may show the wrong currency symbol.

For example, in RankUpCommand it is using this reference:
	RankUtil.doubleToDollarString(result.rank.cost));
which is using this:
    public static String doubleToDollarString(double val) {
        return NumberFormat.getCurrencyInstance().format(val);
    }
If the currency that is shown is not what is configured on the server, 
then you currently MUST change the java startup variables to set it
the language and location that you are needing to use.  Otherwise
it will be pulling from the file system, which may not match your
in game settings.

	-Duser.language=en -Duser.country=US -Duser.variant=US
In context the server startup may now look like this:
	java -Xms2g -Xmx8g -Duser.language=en -Duser.country=US -Duser.variant=US -jar spigot-1.13.2.jar

In the future, may need to switch this over to use either a language config in 
a config file somewhere, or use what is defined, or set, within the 
currency plugin, such as vault, or EssentialsX.


* **No support for sponge - never had it**
There is a sponge module, but there is so little code that is hooked up, 
there is no way it could ever work as it is right now.  It would be a really
major effort to hook up the missing parts. Don't even think anyone is trying
to run it under sponge.  Maybe best in the long run to eliminate the 
sponge module and just focus on making prison better overall.

* **Reports that other plugins may cause issues with Prison**
It's been mentioned that a plugin or two, named something like 
"nohunger or nofalldmg", may have been causing issues with Prison.  
Not sure if its the loading or running, but it behaved as if the
mines and ranks module was not loaded since those commands were not
functional.  Only /prison was working.
