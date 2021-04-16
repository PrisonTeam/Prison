[Prison Documents - Table of Contents](prison_docs_000_toc.md)

## Prison Build Logs for v3.3.x

## Build logs
 - **[v3.3.0-alpha - Current](changelog_v3.3.x.md)**
 - [v3.2.0 - 2019-12-03](prison_changelog_v3.2.0.md)&nbsp;&nbsp;
[v3.2.1 - 2020-09-27](prison_changelog_v3.2.1.md)&nbsp;&nbsp;
[v3.2.2 - 2020-11-21](prison_changelog_v3.2.2.md)&nbsp;&nbsp;
[v3.2.3 - 2020-12-25](prison_changelog_v3.2.3.md)&nbsp;&nbsp;
[v3.2.4 - 2021-03-01](prison_changelog_v3.2.4.md)&nbsp;&nbsp;
[v3.2.5 - 2021-04-01](prison_changelog_v3.2.5.md)&nbsp;&nbsp;
[v3.2.6 - 2021-04-11](prison_changelog_v3.2.5.md)
 

These represent the work that has been done on prison. 



# v3.3.0-alpha.0 2021-04-15


* **Fixes an issue if the json array for resetWarningTimes has a trailing comma, which will result in a null entry.**
So the fix purges out any nulls, no matter where they may be.
Also if there are no entries, it sets the reset warning times to a single entry that is one year long.  That will ensure no warnings happen, and this assumes that it was an attempt to disable the warnings.


* **Backpack size permission**
Added a permission to set a _custom backpack size of a player_, it works just like permission multipliers,
  you just need to use this permission: **prison.backpack.size.<number>** replacing <number> with
  the custom size, **multiple of 9** that you want to use for your backpack, **example: prison.backpack.size.18** will
  make backpacks of that player with a size of 18 slots/2 rows.


* **Logic error on calculating the units for placeholders.**
Was using <= instead of just < so it would produce a result of "1,000.0 K" instead of "1.0 M".


* **Setup the auto feature's sellall on each block mined to suppress notifications and sounds.**


* **Fixed a thread safety bug with the sign usage variable within the singleton.**  
Basically it would have failed to identify a specific player as to using a sign.  If there were many players online and they all performed a sellall event, but only one using a sign, then any of the other players who were not using the sign would have a race condition and the first one to be processed would be identified as having used the sign.


* **Also provided a way to suppress the notifications so the command can be used in silent mode, which would be beneficial for a per-block use of sellall.**
 When notifications are suppressed, it suppresses the text messages and the audio.


* **v3.3.0-alpha.0 2021-04-11**

  Start on the alpha.1 release.
  
