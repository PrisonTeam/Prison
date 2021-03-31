
### Prison Documentation 
[Prison Documents - Table of Contents](prison_docs_000_toc.md)

# BlockEvents

Prison's BlockEvents provides a way to fire commands when a block is broken by a player.  Every BlockEvent must have a chance which can range from 0.0000 percent to 100.0 percent.  Optionally, you can tie the block event to a permission, to an event type, and for Token Enchant explosions you can filter on the enchantment type that triggered that explosion.  You can also filter on a Crazy Enchant blast too.  Soon, BlockEvents will be able to filter on block types too.


A single BlockEvent can also have more than one command that is ran together with each command separated by a semi-colon. ";" 


BlockEvents have a taskMode that defines how the specified commands are to be ran.


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">

# BlockEvents Placeholders

Within the BlockEvents commands you can use placeholders and Prison will substitute the correct values for you.  


- **{player}** Provides the player's name.


- **{player_uid}** Provides the player's UUID.


Prison's BlockEvents also supports more advanced placeholders that can help simplify some basic tasks.  Over time this list will be expanded to include more useful features.


- **{msg}** Send a message to the player.  This is a shortcut for the prison command: `/prison utils msg`.  Example: `{msg} Congrats! You found a token!`. 


- **{broadcast}** Sends a broadcast message to all players on the server.  this is a shortcut for the prison command: `/prison utils broadcast`. Example: `{broadcast} {player} found a treasure trove of 100 voter keys!`. 



Some examples showing how to use these in BlockEvent:


```
/mines blockevent add A 0.075 none inline prison utils repairAll {player};{msg} Congrats! Everything in your inventory has been repaired!

/mines blockevent add A 1 none inline prison utils smelt {player};prison utils block {player}

/mines blockEvent add A 5.0 none inline prison utils potionEffect speed 90 2;prison utils potionEffect night_vision 300 1

```

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# BlockEvents Commands

The BlockEvent command that is used must be be runnable from the console. It is suggested that you use the console to figure out the correct structure of the command that you are interested in running.  If it works from the console, then it should work as a BlockEvent command.


The structure of the command should be the same as within the console. Do not include a a leading /.  Do not quote the command.  Use the placeholders mentioned above for referring to the players. 


Move than one command can be chained together by using a semi-colon between the commands.  The commands do not need to end with a semi-colon.  No space needs to follow a semi-colon before the next command.  There is no limit imposed upon the length of a command, or commands, but the max length may be controlled by outside factors such as ingame text message constraints.  Hence why it may be advisable to enter long commands through the console.


See the above examples on how to enter the commands when setting up a BlockEvent.


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# BlockEvents Chance


The chance that is tied to a block event can range from 100 percent to as low as 0.0001 percent.  


A chance of 100 percent ensures the block event will always run if all the other filters pass.


Please be aware that there are no validations for the lowest setting for chance.  What this means is that you "could" enter a value lower than 0.0001 percent and it could be accepted and it will work, but the displaying of that value will be cut off at four decimals and no rounding will apply so it could appear as if it is the same as zero.  Also a value of 0 could be entered which would imply that the BlockEvent will never be ran.


Block Event chances are all independent from each other.  Each block even setup in a mine will have a chance to be ran on each event.  So if you have three block events, 70%, 20%, and 10%, all three can be ran on the same event since each block event rolls their own chance.  This means you cannot setup block events where you want to apply only one of a number of actions, since all actions will have a chance to be applied.
  

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# BlockEvents Permission


If a permission is added to a BlockEvent then the player must have that permission before the BlockEvent is even considered.  There can only be one permission per block event.


When adding a block event, to bypass the permissions, enter the value of `none`.


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# BlockEvents TaskMode


TaskMode identifies how a block event is ran.  There are two major modes, with two ways to run the command, with the TaskMode combining these two components to define 4 possible TaskModes.


The actual values are: **inline**, **inlinePlayer**, **sync**, and **syncPlayer**.


The two major modes that defines how tasks are ran are **inline** and **sync**.  


**Inline** runs the commands in the same thread as the block break event is processed.  Inline tasks should be simple and fast so they do not produce lag with holding up each block break event.  The **inline** mode is ran as console.


**Sync** submits the task to run right away, but it does so on the bukkit's synchronous thread.  It can produce lag if it takes a long time to run, but it will also allow the block break event to complete right away.  The **Sync** mode is ran as console.


**InlinePlayer** runs the task inline, but as the player.  It will not have any escalated permissions and the task can fail if it does not have access to the commands that are being ran.


**SyncPlayer** submits the task as the player.  It will not have any escalated permissions and the task can fail if it does not have access to the commands that are being ran.


Asynchronously submitted tasks are not an option since commands cannot be submitted asynchronously; only code, or functions, can be submitted to be ran in an asynchronous thread.  There isn't an option to run commands this way, and if a task is submitted to be ran asynchorously to then run a command, then it drops in to synchronous mode to run the command.



<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# BlockEvents EventType


The EventType defines what kind of event is being monitored.


By default, **all** is used.  This applies to all event types.



**BlockBreak** This is the standard event type.  It is used to identify when a single block is broken.


**TEXplosion** is a Token Enchant Explosion event, and generally includes more than one block.  This filters only on the TE Explosion event. With this event type the **Triggered** option can be applied.



**CEXplosion** is a Crazy Enchant explosion event and generally includes more than one block.  Unlike the BlockBreak event or the TE Explosion event, the CEExplosion event does NOT identify which block was actually hit by the player. 




<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# BlockEvents Triggered


Only the Token Enchant Explosion events provides information on which enchantment **triggered** the explosive event.  This allows different BlockEvents to be fired for different enchantments that use explosions.


You must enter the correct value for this triggered command as used by Token Enchant and provided by Token Enchant.  There is no way prison can validate this value when entered.  




<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# BlockEvents Blocks


Coming soon.  Not yet available.


Will be able to filter block events based upon block types.  Will be able to have more than one block defined for each BlockEvent.




<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# BlockEvents 


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



