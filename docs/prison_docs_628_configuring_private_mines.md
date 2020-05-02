
### Prison Documentation 
[Prison Documents - Table of Contents](prison_docs_000_toc.md)

## Setting up Private Mines

This document explains how to setup private mines that are not accessible to 
other players.  This document provides an *idea*, but it's up to your to 
figure out the details to make it all work.


This applies to both personal mines (one player) and also special groups such
as donors or clans or other in-game groups.


This process will be a very manual process with a lot of manual steps that 
must be repeated over and over again.

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# Dependencies 

* Vault - Interfaces with the Permission Plugins
* [Install WorldGuard and WorldEdit](prison_docs_026_setting_up_worldguard_worldedit.md) Understanding this document is critical for getting things to work.
* Install a Permissions Plugin that is compatible with Vault 
* This guide uses LuckPerms.  See [Setting up LuckPerms](prison_docs_020_setting_up_luckperms.md) for more information.

It is very important to understand how to use WorldGuard and LuckPerms with Prison.  The
above document explains one way to do that, and it's something that will be expanded 
upon in this document.

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


# Performance Considerations

As more mines are added to your server, they will have an impact on performance overall. 
Here are some possible concerns if you are adding hundreds of private mines.

* The reset process of replacing blocks is time consuming and processing intensive.  The more mines that exist, the more frequent resets will run. Having hundreds of mines will ensure more are resetting every minute, and you could have multiple that try to reset at the same time.
* Mine resets cannot be done asynchronously.  Asynchronous replacement of blocks could cause corruption of the world.  Even asynchronous reading of block types can corrupt the world IF there are entities in chunks that are loaded to check the blocks.  Entities include mobs and even items thrown to the ground, or drops from mining.  The point is that mine resets can be expensive.
* Prison "tries" to do as much as possible to minimize the load on the server when doing a mine reset.  For example performing precalculations in an asynchornous thread before attempting to replace the blocks.  Also it pages the block placements to allow other processes to run on the server which may increase the reset time, but it should minimize impact on TPS when many events are going on at the same time.
* Many mines will require more memory on your server. The amount will be small, but more mines will require more memory.  Prison even with hundreds of mines, should not consume a ton of memory, but it will consume more than just a dozen mines.
* Having many mines will slow down the searching for the "right" mine.  There are some processes that have to locate which mine is being "used".  Unfortunately, the only way to do so, is to go through each mine one at a time looking for a "hit".  If you have hundreds of mines and the "hit" is on the last one in the list, then the dealy may be slightly longer than if it "hits" on the first item.



<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# Mine Names, Permission Names, and Region Names

You need to figure out how you want to name everything.  This includes the name of
the mines, the permissions, and regions.  Their names are overall
arbitrary, but they need to make sense to you.  There may be a large number that are
needed so it would make sense to use an incrementing number.

For the purpose of this document, and to keep the mine names simple, 
let's name the mines as follows.  This will allow for up to 10,000 mines.
* mine0001
* mine0002
* mine0003
* mine0004


Examples of what will be used in this document:
* prison.mines.private.mine0001
* prison.mines.private.mine0002
* prison.mines.private.mine0003
* prison.mines.private.mine0004


Permissions are critical in that it allows you to associate a player with a mine. 
When the mine is properly configured with WorldGuard, it will prevent other players
from being able to access it. 


Region names should follow the same pattern as provided in the WorldGuard documentation.
* mine_mine0001
* mine_mine0002
* mine_mine0003
* mine_mine0004


In the WorldGuard documentation above, the region names used were based upon 

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# Creating Mines Using Schematic Files

If you will provide personal mines, that implies you will have to have a lot of
mines to sell. You can build them individually, but it may be worth figuring out how
to use WorldEdit to save a mine as a schematic File.  Once saved as a schematic file, you can
then copy and paste it in your world many, many times.  If you need more information on
how to do this, please google: **worldedit using schematics**.

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


# Hooking it up to BuyCraft or Enjin

Whatever your store front you use, to provide the players with access and ownership to their
private mines, all you would have to do is provide the player with the permission that
was used to configure that mine.  It would be as simple as that.  Once the player has 
the permission, everything should work fine.

If you are naming the permission in a sequential order, see above, then the store 
fronts may be able to "sell" a range of permissions.  Otherwise you would have to
configure each one independently.

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


# Another Idea on how to Setup Private Mines

There may be a way to setup a plot world to help with this. 
A plot world plugin could generate the repeating patterns of the mines,
and take care of some of the management of them, but it would have to be a manual
process to hook them up to the prison plugin.  Such an exercise is far beyond 
the scope of this document, or the prison plugin being able to provide an automation
process to manage all of them for you. Good luck, you're on your own
in trying to figure this out.



<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


