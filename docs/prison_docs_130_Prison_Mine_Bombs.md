
### Prison Documentation 
[Prison Documents - Table of Contents](prison_docs_000_toc.md)

# Prison Mine Bombs


Prison mine bombs can take on a lot of different sizes, shapes, and strengths.


A player can only use a Mine Bomb within a Prison mine, and they cannot activate it outside of a mine.


When a Mine Bomb goes off, it identifies a list of blocks that are included in the explosion event, and then it's passed on to prison to process the actual block breaks and apply any special conditions that goes along with those actions.


*Note: this document is a work in progress.*


*Documented updated: 2022-08-30*

<hr style="height:7px; border:none; color:#aaf; background-color:#aaf;">


# Commands:

```
/prison utils bomb list help
/prison utils bomb reload
/prison utils bomb give help

/prison utils bombs help
```



<hr style="height:7px; border:none; color:#aaf; background-color:#aaf;">


# Prison Mine Bombs Config File

The Prison Mine Bombs use a configuration file to customize the mine bombs.  There are no in-game commands to edit the settings.

The location of the configuration file is:

`plugins/Prison/module_conf/mines/mineBombsConfig.json`


If this file does not exist, you can have prison auto generate it by using any of the above mine bomb commands.  


You can directly edit this file while the server is running, then use `/prison utils bomb reload` to reload it without restarting the server.


The format of this config file is JSON, so it very sensitive to typographical errors that breaks the JSON format.  If you do mess it up, or if you need to reformat it so it looks pretty, you can use the online JSON lint tool: [JSON Lint Online](https://jsonlint.com/).


You can edit almost anything in this config file, except for the field `dataFormatVersion` which is used to identify if the underlying format has changed so prison can use the proper converters to ensure the settings are migrated to future formats.  This currently is not being used, but as soon as a major change is made to the data structure, then this will become valuable.

<hr style="height:7px; border:none; color:#aaf; background-color:#aaf;">


# Config File - Bombs Map

In the config file, "bombs" is the root hash (map, object, key-value-pairs) that contains all bombs.  The bombs are keyed on their name, which must be unique.  


There are no limitations on how many bombs you can create, or have active.  You can have 1000's of bombs if you desire.

<hr style="height:7px; border:none; color:#aaf; background-color:#aaf;">


# Config File - Bomb Structure

The config file for a bomb, contains all of the possible settings that can be used, even if they are not enabled or used.  This may make for a verbose bomb listing, but it's part of the process for mapping between JSON and the java data.


## Bomb Name and nameTag

Each bomb must have a name, which can include color codes, but the key name for the bomb is the name but minus color codes.  It's the key name (without the color codes)  becomes the identifier that is used in-game for selecting the bombs.  Since bombs are used with commands, the name cannot contain any spaces.


Bomb names can include color codes, but it is suggested that the color codes are added to the nameTag.  The nameTag can include a limited number of Mine Bomb placeholders to inject various information, such as the bomb name.  Placeholders can be used more than once.


The list of Mine Bomb nameTag placeholders:

* **{name}**  Injects the bomb's name in to the nameTag.

* **{countdown}**  Provides a countdown that is refreshed dynamically.  This can show the players how long they have before a bomb goes off.  The **oofBomb** has an example of this placeholder.'  


<hr style="height:7px; border:none; color:#aaf; background-color:#aaf;">


## Bomb itemType

The itemType identifies what the bomb will look like.  It can be any item or block as long as the name matches the XSeries' XMaterial types.  The itemType must also be valid for your version of Spigot that you're running.


You can find a list of XMaterial names here:

[XSeries' XBlock](https://github.com/CryptoMorin/XSeries/blob/master/src/main/java/com/cryptomorin/xseries/XMaterial.java) 
 
<hr style="height:7px; border:none; color:#aaf; background-color:#aaf;">
 

 ## Bomb Shapes, Dimensions, and Positions
 
 
 Most bombs may be round, but they can take on any shape.  Prison supports a few different shapes, and the plan is to add more in the near future.  To view the current list of shapes use the following command:

`/prison utils bomb list shapes`


Explosive Shapes (field **explosionShape**):

* **cube** a cubic bomb that has equal sized dimensions. The length is calculated from the bomb's radius:  length = 2r + 1.  The **height** may also effect the cube bomb (will have to look it up).

* **sphere** a spherical bomb with a defined **radius**.

* **sphereHollow** a spherical bomb that is defined by it's radius, but the center of the bomb is untouched and its size is defined by the **innerRadius**.  The oofBomb is an example of a sphereHollow bomb, where at ground zero it's the safest place to stand.

* **ring_x ring_y ring_z**  Creates a hollow ring going through the specified plane.  The **radius** defines the size of the ring, and the **innerRadius** defines the size of the hollow inner circle.

* **disk_x disk_y disk_z**  Similar to the **ring_** shapes, but instead of hollow, they are filled in.


The **radius** is not exactly what someone may think it is. Since we're dealing with Minecraft, we are stuck in block mode, therefore everything must be within whole blocks.  So the radius is added to 1, but the total diameter is not 2(r + 1), but instead it is 2r + 1.  So given the starting center point, the radius is added to that block.  So with a radius == 1, the diameter will be 3.  Radius == 5, diameter == 11.


The **placementAdjustmentY** is added to the y-axis to move the center of the bomb up when positive, or down when negative.  Usually since a bomb is symmetrical, if just placed on the top of a mine, then 1/2 of it would go unused and wasted.  So for some bombs it makes sense to adjust the height with a negative value so as to get move coverage.  As with any bomb that has decent hight, it would make the most sense to place it in the very center of the mine to maximize the full volume of the explosion.


<hr style="height:7px; border:none; color:#aaf; background-color:#aaf;">


## Removal Chance


To weaken an explosion, you can use the field **removalChance** which will randomly check each block to see if it's included in the explosion.  Depending upon the percent chance, a few blocks can remain, or all can be removed.

The values range from **100.0** to **0.0001**.  At 100.0 percent, all blocks are removed and no chance calculations are performed.  At 50.00 percent, then about half will be removed.  At very, very low removal chances, it's possible that no blocks will be removed.


**Explosive Strength - Future Idea**

Please note that the removal of a block has nothing to do with the hardness of the blocks.  In the future, I may add explosive strength to Mine Bombs where the total explosion is calculated in terms of a strength value, then each block, starting at the radius and incorporating a proportatant equation, will determine if the block breaks, and how much of the explosive strength was consumed.  This will ensure blocks at ground zero will have more odds of breaking, but blocks on the outer edge will have less of a chance, especially if the explosion runs out of explosive strength.  So two identical bombs set off in sand stone (weak blocks) will have a larger blast area than the same bomb set off on obby.  For the largest bombs, having such high explosive strength at ground zero, could even include breaking bedrock or even stronger materials.



<hr style="height:7px; border:none; color:#aaf; background-color:#aaf;">


## Virtual Tool

When a block is broken, it is taken in to consideration what is hitting the block.  Such as the player's hand, a wooden pickaxe, or a diamond pickaxe.  But with Mine Bombs, the player is holding the bomb before they set it off, so when breaking the block, it is needed to include a "tool".

This virtual tool is defined with four fields and it actually used to break the blocks.  Since some explosions can include tens of thousands, or even millions of blocks (not recommended... lol) the tool never looses durability.  Otherwise if it breaks 1/3 of the way while processing the explosion, then 2/3rds of the explosion will remain unbroken.

**toolInHandName**  Use XSeries' XMaterial names for this field.  See the link above under **Bomb itemType**.

**toolInHandFortuneLevel** sets the fortune level.  Uses unsafe enchantments (unlimited).  This can greatly impact the amount of the drops.

**toolInHandDurabilityLevel** sets the durability level.  Uses unsafe enchantments (unlimited).  Since no wear happens, this really is not needed.

**toolInHandDigSpeedLevel** sets the dig speed level.  Uses unsafe enchantments (unlimited). Since the block break is instant, this really is not needed.


<hr style="height:7px; border:none; color:#aaf; background-color:#aaf;">



## Fuse delay ticks

The fuse delay, in ticks, is the amount of time it takes from placing the bomb to when it goes off.  


Within the **nameTag** field, you can use the placeholder `{countDown}` to display how much time is remaining.  This placeholder can be used multiple times if desired.


Valid values are 0 through no limit.  But if the server restarts before a bomb goes off, it may orphan the bomb, or actually the armor stand that is used to represent the detonation point.  You would have to manually remove these orphaned armor stands with a command. 

(to do: provide command to remove the armor stands)

<hr style="height:7px; border:none; color:#aaf; background-color:#aaf;">


## Cooldown

Once a bomb is used, the field **cooldownTicks** sets how long the player must wait before using that bomb again.  A value of 0 has no cooldown.


Cooldowns apply to a specific bomb and so if a player has three different kinds in their inventory, they can rapidly set off each one without triggering the cooldowns.   So if a player has a stack of 10 smallbombs and it has a cooldown of 10 seconds, then it will take at best, 100 seconds to set them all off.

<hr style="height:7px; border:none; color:#aaf; background-color:#aaf;">


## Item Removal Delay Ticks

Just because the bomb goes off, does not mean the item that represents the bomb is removed.  It must be manually removed from the game or it will just fall and sit at the bottom of the mine, and no one will be able to pick it up.


The field **itemRemovalDelayTicks** is the number of ticks that prison waits after setting off the bomb, before it removes the item from the game.  This is important, since if it is removed as soon as the bomb goes off, the player may see it disappear before the explosion and block removal happens.  But setting to a small value of ticks, the item can become lost in the visual effects so when it's removed, it's not noticeable.

<hr style="height:7px; border:none; color:#aaf; background-color:#aaf;">

## Glowing

Glowing does not work well on all versions of spigot.  Some versions it's horrible and the whole armor stand glows, other versions does not glow at all.  But if you are using a version of spigot where it only glows the itemType, then it's a cool effect.  Glowing will allow you, and other players, to see it through other objects and blocks.  Kind of like a massive warning of doom.

Because it may not always work as expected, please test on your server and confirm if it's what like.  If not, then disable the field **glowing** by setting to a value of false. 

<hr style="height:7px; border:none; color:#aaf; background-color:#aaf;">


## Gravity

Just like glowing, the effects may not be what you want.  The idea was to place a bomb somewhere, even within the air, and have it ignore gravitational effects.  Kind of like hovering, or like Wile e Coyote the moment before he realizes he's ran off the edge of a cliff and is about to fall.

The reality is that in my testing, the mine bomb items just kind of float off in to space and it's totally a dud.  But hey, it's a feature, so maybe you can find a cool way to use them, such as drifting mines that go off in 5 minutes (oh the waiting! lol) so you have no idea where it will wind up within a cavernous mine.


<hr style="height:7px; border:none; color:#aaf; background-color:#aaf;">


## Autosell

With super-large bombs that can produce many times the drops than what a player has the capacity to hold in their inventory, **autosell** will automatically sell every item that was included in that explosive drop.  Even if auto feature's autosell is turned off, this will override that setting and sell it for the players.

As a warning, if you have a super-large explosion, and it drops the blocks, the density of the block pile can overload the player's inventory to the point that it can cause glitching.  It can also potentially cause lag too if the entity counts become too high.  On my test server, with the oof bomb dropping overflows, it can be almost impossible to pickup all of the blocks, or you have to go in to inventory and move a stack of blocks to get them to popuplate within the player's inventory.  So autosell on super huge explosions can eliminate all this mess. 

<hr style="height:7px; border:none; color:#aaf; background-color:#aaf;">


## Sound and Visual Effects - Disclaimer

Prison supports Spigot 1.8.8 through 1.19.x.  Over the years, and with many different versions of minecraft, the names for sound and visual effects have changed.  Because prison "tries" to support a wide range of versions, the demo mine bombs includes a variety of sound effects and visual effects for the wide range of versions just so something works.  Therefore, it is perfectly normal for the demo mine bombs to show warnings that a particular effect does not exist.  Just ignore them, or better yet, remove the ones that are not compatible for your version of spigot.

Newer versions of spigot has a lot more sound and visual effects than older versions.  Way more!

To list what works on your server, run the following commands in the console.  If you're using spigot 1.19.x, then this list will be huge and you will have to do a lot of scrolling.


```
/prison utils bomb list help

/prison utils bomb list sounds
/prison utils bomb list visuals
```

Also, internally, Prison is using the same data object to hold both the sound effects and the visual effects.  Therefore the visual effects in the save file lists **volume** and **pitch**.  You can ignore them and just use the default value of 1.0 for those fields.


<hr style="height:7px; border:none; color:#aaf; background-color:#aaf;">


## Sound and Visual Effects 

The sound and visual effects are played at different points within the explosion event.  These points are identified with **effectState** and the values are:

* **placed** The starting point is when the bomb is place.  An **offsetTicks** of zero runs the effect instantly.  Positive values are ticks after placement with no restrictions on max value, but should be within reason.

* **explode** The starting point for this state is when the bomb explodes.  An **offsetTicks** of zero runs the effects when the bomb "explodes".  A negative value will run the effect before the explosion.  A position value will run the effect after the explosion.


* **finished** The starting point for this state is when everything is done.  I'm not sure about the **offsetTicks** since finished should be done.




**offsetTicks** Depending upon what **effectState** is active, this value could be zero, negative, or positive.  The purpose of the offsetTicks is to string out multiple effects that are played over time which will layer the visuals and audio effects to make them more intense and unique.



**volumne**: Yeah, there's a typo in there... lol

Default volume is 1.0.  Less is quieter and higher is louder.


**pitch** Changes the pitch.  1.0 is the default.  Less is lower, and higher is a much higher pitch.


<hr style="height:7px; border:none; color:#aaf; background-color:#aaf;">

