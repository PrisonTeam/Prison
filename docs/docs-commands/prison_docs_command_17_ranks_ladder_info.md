### Prison Documentation
[Prison Documents - Table of Contents](../prison_docs_000_toc.md)

## Description:

Edit a rank on the ladder side, like moving it from a ladder to another or also **manage ladders.**

## Permissions:

- `ranks.admin`

## SubCommands:

- `/ranks ladder addrank [ladderName] [rankName] [position]`
- `/ranks ladder create [ladderName]`
- `/ranks ladder delete [ladderName]`
- `/ranks ladder delrank [ladderName] [rankName]`
- `/ranks ladder listranks [ladderName]`
- `/ranks ladder list`

## How to use the command

Execute the command or its variables like in the examples:

- `/ranks ladder addranks default A 1` This will move the Ranks A from whatever it's to the default ladder as the first rank. 
- `/ranks ladder create coolLadder` This will create a ladder named "coolLadder".
- `/ranks ladder delete coolLadder` This will delete the ladder named "coolLadder" if found.
- `/ranks ladder delrank default A` Delete a rank from the specified ladder.
- `/ranks ladder listranks default` Shows a list of ranks of the "default" in the example.
- `/ranks ladder list` Shows a list of ladders.

### Command Format

`/ranks ladder <Arguments>`

**END of the command INFO**