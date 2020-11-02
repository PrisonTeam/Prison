### Prison Documentation - **WORK-IN-PROGRESS**
[Prison Documents - Table of Contents](../prison_docs_000_toc.md)

## Permission:

- `ranks.set`

## Description:

This command is only available when you first run Prison on your sever.  It cannot be used if you already setup any ranks or mines because this could potentially cause conflicts.

This new feature will provide a fast way to configure most of the core features of Prison, and it may be the preferred way to setup a new server.


Features provided:
 - Setup all basic Ranks in the **default** ladder ranging from Rank **A** to Rank **Z**.
 - Setup basic Rank Commands.  Provides granting of permissions to that Rank (`mines.<mineName>` and `mines.tp.<mineName>`) and removing the ranks of the next higher rank so `/ranks demote` will work properly.
 - Setup all mines, as virtual mines, from **A** to **Z**.
 - Links all mines to the rank of the same name.
 - Setup all blocks for all mines, using a list of blocks of increasing values. Mine A would have the lowest values of blocks, and mine Z would have the greatest value of blocks.  All of these can be customized.
 

Once this command is ran, these setting can be used as is, or they can be farther customized to suite any need.  

The only requirement to creating a functional prison is to convert all of the virtual mines to physical mines.  This is accomplished through the command `/mines set area <mineName>`.

Other simple tools to help customize the mines, are as follows:

 - `/mines set size <mineName>` This allows you to resize a mine without having to reselect it's area.
 - `/mines set liner <mineName>` Wraps your mine with a patterned liner and inserts ladders along the edges.  There are a number of different patterns to choose from, with more being added in the near future.
 
  

## SubCommands:

- `/ranks autoConfigure full` Full setup
- `/ranks autoConfigure mines` Mines setup
- `/ranks autoConfigrue ranks` Ranks setup

## How to use the command

Execute the command itself with the argument you want, like in the examples below:

- `/ranks autoConfigure full` Full setup
- `/ranks autoConfigure mines` Mines setup
- `/ranks autoConfigrue ranks` Ranks setup

In these cases, for ranks the default starting Price will be 50000 and the Multiplier for each Rank (how much a Rank 
will be more expensive than the previous one)'s of 1.5.

An example with custom values's this one, remember to add the whole `price=x` and `mult=x` tag:

- `/ranks autoConfigure full price=1000 mult=1.5`

You can execute the command without args, like in this example:

- `/ranks autoConfigure`

It'll use default values like if you're using:

- `/ranks autoConfigure full price=50000 mult=1.5`

### Command Format

`/ranks autoConfigure <full/mines/ranks> <price=x> <multi=x>`

**END of the command INFO**