
### Prison Documentation 
[Prison Documents - Table of Contents](prison_docs_000_toc.md)

# BlockEvents

Prison's BlockEvents provides a way to fire commands when a block is broken by a player.  Every BlockEvent must have a chance which can range from 0.00000 percent to 100.0 percent.  Optionally, you can tied the block event to a permission, to an event type, and for Token Enchant explosions you can filter on the enchantment type that triggered that explosion.  Soon, BlockEvents will be able to filter on block types too.


A single BlockEvent can also have more than one command that is ran together. 


BlockEvents have a taskMode that defines how the specified commands are to be ran.  


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">

# BlockEvents Placeholders

Within the BlockEvents commands you can use placeholders and Prison will substitute the correct values for you.  


- **{player}** Provides the player's name.


- **{player_uid}** Provides the player's UUID.


Prison's BlockEvents also supports more advanced placeholders that can help simplify some basic tasks.  Over time this list will be expanded to include more useful features.


- **{msg}** Send a message to the player.  This is a shortcut for the prison command: `/prison utils msg`.  Example: `{msg} Congrats! You found a token!`. 


- **{broadcast}** Sends a broadcast message to all players on the server.  this is a shortcut for the prison command: `/prison utils broadcast`. Example: `{broadcast} {player} found a treasure trove of 100 voter keys!`


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# BlockEvents Commands

The BlockEvent commands must be runnable from the console. It is suggested that you use the console to figure out the correct structure of the command that you are interested in running.  If it works from the console, then it should work as a BlockEvent command.


The structure of the command should be the same as within the console. Do not include a a leading /.  Do not quote the command.  Use the placeholders mentioned above for referring to the players.


Move than one command can be chained together by using a semi-colon between the commands.  The commands do not need to end with a semi-colon.  No space needs to follow a semi-colon before the next command.  There is no limit imposed upon the length of a command, or commands, but the max length may be controlled by outside factors such as ingame text message constraints.  Hence why it may be advisable to enter long commands through the console.


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# BlockEvents 


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# BlockEvents 


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



