
### Prison Documentation 
[Prison Documents - Table of Contents](prison_docs_000_toc.md)

# BlockConverters


Block Converters are a powerful new feature that is used within the Auto Features.  
Block Converters provides for a very dynamic way to customize the processing of all block
behaviors within the Auto Features, such as selecting which blocks are to be processed 
by the auto features (auto pickup, auto smelt, and auto blocking).  


Block Converters really shine by being using a given block type to figure out what to
**convert** it to.  These conversions are used by smelting and blocking, where is there one
block type as an input, but the output can be none, one, or more other blocks or items.


These conversions are powerful in their own right, but what makes them even more powerful, 
is that both the input and output blocks can be selectively controlled by permissions.
This type of selectivity can provide a very dynamic result that can vary for each player.
This "filtering" can be used to give different results (blocks) to different players.  
For example, the in terms of smelting or blocking, different players, because of their perms,
could get different results and quantities, including multiple items.



*Documented updated: 2022-06-255*

<hr style="height:7px; border:none; color:#aaf; background-color:#aaf;">


# Editing Block Converters

There are no in game commands to configure Block Converters.  You must manually edit the
configuration file to set up the Block Converters.  There is a possibility that a misconfiguration will 
result in unexpected behavior or complete failure. Therefore, follow this documentation carefully.  


If you have questions, please contact our discord server for help. 


The file to edit:

`plugins/Prison/BlockConvertersConfig.json`


This configuration file is in the JSON format, and therefore, if the json format is made invalid, 
then none of the conifgs will be loaded.  You can use an online web site to validate and fix invalid 
json structures.

<hr style="height:7px; border:none; color:#aaf; background-color:#aaf;">



# Block Names


The names used to identify the blocks, and or items, must be something that prison understands, and
what can be converted to valid bukkit item stacks so they can be given to the players.



Block names are mostly tied to the names as found within the XSeries' XMaterial object. This helps
to provide a unified name, of which the bukkit names for many items and blocks have changed a number
of times between Spigot 1.8 through Spigot 1.19.  Some of the bukkit names actually conflicts with
prior version's names.  So this is how prison can provide consistency.  If there is a custom 
block named, then it will need to include the namespace, a colon, followed by the name.  
Example of a custom block name: **CustomItem:compressedCobblestone**.



The best way to ensure you are using the correct names, use the block search to get the names that
prison will understand.  There are two different types of block search that you can use, the primary 
search excludes non-blocks.


```
/mines block search help
/mines block search stone 3
```


The following search includes items.  Do not use items for a **keyBlockName** or the key for a 
BlockConverter since it will never be used.


```
/mines block searchAll help
/mines block searchAll chest
/mines block searchAll gold 2
```




<hr style="height:7px; border:none; color:#aaf; background-color:#aaf;">



# Block Converters - Enabling Auto Features and Normal Drops


Within the `blockConvertersConfig.json` file, are major groups of Block Converters.  The group
name **autoPickupFeatures** identifies which blocks can be processed.  If a block is not included
in this list, then it will be ignored.  NOTE: ignored/excluded blocks may, or may not, be breakable.


<hr style="height:7px; border:none; color:#aaf; background-color:#aaf;">


