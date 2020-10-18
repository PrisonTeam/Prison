### Prison Documentation - **WORK-IN-PROGRESS**
[Prison Documents - Table of Contents](../prison_docs_000_toc.md)

##Permission:

- `ranks.set`

## Description:

This command will execute a fast setup of the main Prison features, like Ranks from A to Z and virtual mines from A to Z
linked to their ranks, you'll need to setup their blocks and perimeters later with the /mines wand.

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