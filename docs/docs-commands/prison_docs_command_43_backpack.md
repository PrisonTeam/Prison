### Prison Documentation
[Prison Documents - Table of Contents](../prison_docs_000_toc.md)

## Description:

Open a list of backpacks commands.

## Permission:

- none

## SubCommands:

- `/backpack admin` `prison.admin` 
- `/backpack delete [Backpack Owner] [id]` `prison.admin`
- `/backpack item` 
- `/backpack list`  
- `/backpack set [Owner] [Size] [Optional-ID]` `prison.admin`

## How to use the command

Execute the command himself to use it.

### Command Format

`/backpack [args] [args]`

## Something about backpacks.

### Custom size:

You can set a custom size for backpacks by commands: 
- `/backpack set [Owner] [Size] [Optional-ID]` 
  
or with a size permission:
- `prison.backpack.size.<size>` replace `<size>` with the size that you want.

Always use as the size a **multiple of 9**, the size can't exceed 54.

**END of the command INFO**