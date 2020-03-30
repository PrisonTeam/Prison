[Prison Documents - Table of Contents](docs/prison_docs_000_toc.md)

# Prison Known Issues and To Do's for v3.2.x

This document is intended to keep track of known issues and also provide for
a short list of To Do's. This list is intended to help work through known
issues, and/or to serve as items that should be added, or fixed.


# To Do Items

Work to be considered.


<h2> Higher Priority TO DO Items </h2>

* **Complete the new Mines Reset Paging**
  Holding up next release.

* **Integrate GUI in to bleeding**
  Holding up next release.

* **Setup GUI to use /prison gui**

* **Mine Placeholders**

* **Get started on new Multi-Language Support**



<h2>To consider - Lower priority</h2>



* **Add to the Command annotations an option of *async* to run that command asynchronously**

* **Add prison Placeholders to papi's website for downloads**
Should try using papi too to make sure the integration works with it 
as expected?

DeadlyKill: This what he needs ita
Papi
Hook Plugin
They have those expansions which hook other plug-ins

https://github.com/help-chat/PlaceholderAPI/wiki/Placeholders

* **Add support for player use of /mines tp**
Could be done through other perms and then checking to see if they have access to
that mine.  Perm:  prison.playertp and prison.playertp.a

* **Document how to use essentials warps for each mine**

In the plugins/Essentials/config.yml is this:

// Set this true to enable permission per warp.
per-warp-permission: false

Set that to true.
 	
essentials.warp 	Allow access to the /warp command.
essentials.warp.list 	Specifies whether you can view warp list with /warp.
essentials.warps.[warpname] 	If you have per-warp-permission set to true in the config.yml then you can limit what warps players can use. This also controls what players would see with /warp.

set:
per-warp-permission: true

essentials.warp
essentials.warp.list
essentials.warps.a
essentials.warps.b
essentials.warps.c

Then add them to the rank commmands using pex:

/ranks command add a pex user {player} add essentials.warps.a
/ranks command add b pex user {player} add essentials.warps.b
/ranks command add c pex user {player} add essentials.warps.c


http://ess.khhq.net/wiki/Command_Reference
http://wiki.mc-ess.net/doc/


* **Improve some of the display pages for ranks and ladders**
Can add more information to the listings so they have more value.

* **Tab Completion**
Hook up tab completion on the prison commands.

* **Better logging of major events**
Need to log major events such as rankups, both to the server log, and also
to the community.  Server logs for these events, especially when money is
involved, is important.

* **Block Types for Specific Versions of Minecraft**
Add in support for the loss of magic values, and also provide for newer block
types too.  Basically have a minecraft version selector that can 
tailor the list of available block types that can be used, based upon the
minecraft version that is running.

* **New Feature: Upon block break events, log block changes**
This will allow dynamic live tracking of mine stats, such as how many blocks
remain and relating percentages.  The new async processing will enable this
to actually track individual blocks.

* **New Feature: Add Placeholders for Mine related items**
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
a concise listing. In progress.  Included now in bleeding.

* **Redesign the save files to eliminate the magic numbers**
Most of the save files within prison, for players, ranks, and ladders, are
using magic numbers which is highly prone to errors and is considered 
very dangerous.  Also prison would benefit from a redesign in file layout
to improve efficiencies in loading and saving, not to mention greatly reduce
the complexities within the actual prison code, which in turn will help 
eliminate possible bugs too and give tighter code.


* **Improve the prestige laddering system**
A plugin named EZprestige has been attempted to be used with prison. Not sure if successful?


* **Notification that inventory is full**

* **Built in selling system**

* **Custom Mine reset messsages per mine**


* **Enhancement: Multi-Language Support**

Offers for translation:
  Italian : GabryCA
  Greek : NerdTastic
  


# Features recently added:

* **DONE: Offline player support**
Was not possible to get offline users through the prison API. 

* **DONE: New Feature: Admin reset of Player Ranks**
Bypass the costs for the players. The admins can now use
/ranks promote, /ranks demote, and /ranks set rank.

* **DONE: Eliminate support for Sponge**
It's not being used, so eliminate it and allow prison to possibly eliminate the
extra layers of indirection it currently has to improve performance and to 
possibly reduce the possibilities for errors. 



## tag v3.2.1-alpha.3 - 2020-02-18

* **Some block types may not work for 1.15.x**
Since prison is not currently using the correct block names for 1.15.x, some
block types may not work. Prison is still using magic numbers for the 
block types and those no longer work for 1.15.x.  Symptom would be that
you set a block type such as birch block, but with the loss of the magic 
number, it will revert back to just an oak block.  ETA may be with
release v3.2.2?

* **Unable to change language on all Aspects of Prison**
Currently the number of phrases that can be changed to support other
languages is very limiting and requires a decent amount of manipulation
of extracting files from the Prison Jar. Future releases will allow just
about all uses of English to be replaced by external language files.
This will include error messages for players and mods (console errors), 
command descriptions, and even replacement of the English commands as 
aliases.  The implementation of this will help ensure "errors" are caught
at compile time, and not runtime to help improve stability of the game.
Also the way the language files are structured at runtime will make it 
easier to edit them.

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


* **Known issue with LuckPerms v5.0.x Causing Prison Load Failures**
This is a known problem with pre v3.2.1 releases of Prison.  Basically the 
fault was with LuckPerms in using the same registered plugin name with the 
Spigot Pugin Manager and the same class name, but an altogether different 
package name.  This would have caused any plugin to generate a Class Not Founnd 
exception.  They should not have used the same old signatures for v5.0.x and 
there would have been no issues.
Anyway, special handling has been added to prison to work around their
new version, so the solution is to upgrade Prison to v3.2.1 or newer.  Or 
down grade LuckPerms to v4.4.1 until the server admin's are able to upgrade
Prison.


* [ ] **Reports that other plugins may cause issues with Prison**
It's been mentioned that a plugin or two, named something like 
"nohunger or nofalldmg", may have been causing issues with Prison.  
Not sure if its the loading or running, but it behaved as if the
mines and ranks module was not loaded since those commands were not
functional.  Only /prison was working.  This appears as the same general
effect of LuckPerms v5.x failures, where they caused a failure that
prevented prison from performing a normal load.  
I have not looked in to these plugins, but I would suggest that 
WorldGuard should be used instead of these plugins to eliminate possible
conflicts.



* [x] **REMOVED: No support for sponge - appears like it never had it**
Note: Support for sponge was commented out in two gradle files. The source code
remains, but it will not impact the builds anymore.

There is a sponge module, but there is so little code that has been written,
that it does not appear to be hooked up.  There is no way it could have ever worked
correctly since so many core components needed for the functionality of prison 
are dependent upon Spigot internals, of which those same function calls under 
Sponge's API are just empty or returning null values.

For example, getScheduler() and dispatchCommand() both are empty, but they 
are currently heavily used in both the mine reset process and also for
ranking up. 

It would be a really major effort to hook up the missing parts. I don't even 
think anyone is trying to run it under sponge.  Maybe best in the long run to 
eliminate the sponge module and just focus on making prison better overall.
I think if I do get around to disabling it, it will just be commented out of the
gradle build such that the source will still be there, but it will be excluded
from the build.  Otherwise as new features are added, and existing ones under go
major changes, then the Sponge components will have to be revisited and would be 
wasting resources (and time) for no reasonable purpose.


