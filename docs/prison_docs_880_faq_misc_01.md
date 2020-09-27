

### Prison Documentation 
[Prison Documents - Table of Contents](prison_docs_000_toc.md)

## FAQ - Miscellaneous Questions

This document covers some miscellaneous questions pertain to Prison in general.

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">

# Paper - Is it supported?

**Quick Q & A:** Does it work?  Yes it should work, but Paper is not supported. 

Although Paper servers are not officially supported, there is a strong interest in making sure Prison will work with Paper.  Theoretically since Paper is a direct fork from Spigot, it should always be 100% compatible.  So if issues are found with Paper, efforts will be made to fix those issues.  

The primary reason why paper servers are not directly supported has to do with the number of minecraft versions that are supported, and the number of test servers that requires to perform testing upon.  By adding in paper as an officially supported platform, that would increase the complexities of testing.  Therefore, buy not supporting paper directly, it simplifies the testing phase of development, but at the risk that there may be an ever slight chance of something not working correctly with paper.  

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


# Configuring the Correct Currency Symbol for Prison


**Symptoms of Issues**

The wrong currency symbol is used when showing monetary values.


**How to Resolve the Issues**

Prison can use a standard national currency symbol.  It cannot use a custom symbol
for a custom currency. 
This FAQ explains how to change the national currency symbol.


The current prison plugin is using java internals to set the currency symbol.
As such, currencies within prison may show the wrong currency symbol.

For example, in RankUpCommand it is using this reference:

	RankUtil.doubleToDollarString(result.rank.cost));

which is using this:

    public static String doubleToDollarString(double val) {
        return NumberFormat.getCurrencyInstance().format(val);
    }


If the currency that is shown is not what is configured on the server, 
then you currently MUST change the java startup variables to set it
the language and location that you are needing to use.  Otherwise
it will be pulling from the file system, which may not match your
in game settings.

	-Duser.language=en -Duser.country=US -Duser.variant=US

In context the server startup may now look like this:

	java -Xms2g -Xmx8g -Duser.language=en -Duser.country=US -Duser.variant=US -jar spigot-1.13.2.jar

In the future, Prison may need to switch this over to use either a language config in 
a config file somewhere, or use what is defined, or set, within the 
currency plugin, such as vault, or EssentialsX.


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


# Information on Groupmanager and using it with Rank Commands


**Information provided by ChrisGames**

**groupmanager**

So what i was trying to do was do this command:

	manuaddp {player} essentials.warps.a

manuaddp is for 1 specific player, then just the node for warping to a mine
when you give a rank or a specific player a permission you need to do

	/manselect [Worldname]
	
To get it to work with the prison ranks you need to have it do manselect (world) before it does the other command.

if the manuaddp command is higher in the config than the manselect (world) it will not work.

.

The commands, as ran from the console, for a player named *TestPlayer* in the world named *PrisonWorld*:

    manselect PrisonWorld
    manuaddp TestPlayer essentials.warps.a
    

These are the same commands, but are in the format that is needed to be added to the Prison Rank commands for a rank named "a".  Please note that the order in which these commands are entered are the way they will be ran upon rankup.

	ranks command add a manselect PrisonWorld
	ranks command add a manuaddp {player} essentials.warps.a



<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">
