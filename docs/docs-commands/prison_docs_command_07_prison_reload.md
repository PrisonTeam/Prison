### Prison Documentation
[Prison Documents - Table of Contents](../prison_docs_000_toc.md)

## Description:

Prison, on a whole, is unable to be reloaded.  It is always safest to restart the server under most situations, otherwise corruption may occur.

That said, placeholders are currently the only exception to that "rule".  By issuing the command `\prison reload placeholders` it will regenerate the placeholder mappings and reregister with any of the active placeholder plugins that prison is integrated with, such as PlaceholderAPI.  Reloading the placeholders is safe to run at any time.

A couple of examples when you would need to use the reload command for placeholders:
 * Added, changed the name of, or removed any mines or ranks
 * Reloaded a placeholder plugin, which wipes out all of prison's registered plugins.

## Permissions:

- `prison.admin`
- `prison.reload`
- `prison.placeholder`

## SubCommands:

- `/prison reload autofeatures` Reloads all of the auto features settings, including reregistering all of the event listeners that are related to block break events.
- `/prison reload locales` Reloads the language files. 
- `/prison reload placeholders` Regenerates all the placeholder mappings and reregisters them with the supported placeholder plugins.

## How to use the command

Use the command `/prison reload` for the list of subcommands.

### Command Format

`/prison reload`

**END of the command INFO**