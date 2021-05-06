
### Prison Documentation 
[Prison Documents - Table of Contents](prison_docs_000_toc.md)

# Prison's APIs and Events


The primary focus of prison is not the Prison APIs or providing Events that can hook in to the prison internals.  

But APIs do exist, and so do a few Events.  More will be added in the future too, but only on a per request basis.  So if you are trying to do something with prison that takes a lot of effort, odds are that you may need to talk to us about adding a new API to make it easier, or at least perhaps we can show you an easier way to accomplish the task.  Please contact us through discord.


Prison does not have any formal maven repository, or similar repos.  The source is only within github so your builds will have to be performed over that, or find some other services such as https://jitpack.io/.  Your choice on how to work with the project will be up to you.


<hr style="height:7px; border:none; color:#aaf; background-color:#aaf;">



# API Classes


There are two primary packages that contains API related classes.  One at the project level, and the other strictly through the spigot plugin.



**Module: prison-core**

`tech.mcprison.prison.PrisonAPI`


**Module: prison-spigot**

`tech.mcprison.prison.spigot.api.PrisonSpigotAPI`


`tech.mcprison.prison.spigot.api.PrisonMinesBlockBreakEvent`



<hr style="height:7px; border:none; color:#aaf; background-color:#aaf;" />



# Prison Events

There are a few prison events that you can include in your project to better control what prison does.  


## Prison Event: tech.mcprison.prison.spigot.api.PrisonMinesBlockBreakEvent

This is a new event that was just added to Prison around the v3.2.6 release.  This event is raised before prison applies changes to the block (actually breaks the block).  If your plugin does not want the block to be broken, the event can be canceled.


This event contains a lot of prison data that could be very useful to your plugin.  It extends the basic BlockBreakEvent so all related fields can be found within this class too.  The exception is that this event creates it's own HandlerList that is disconnected from the BlockBreakEvent so this event will not get mixed in to the normal BlockBreakEvent.


<ul>

  <li><b>tech.mcprison.prison.mines.data.Mine:</b> The actual Prison Mine object.  You have full access to the whole mine's data; be very careful in what you change and modify since changing the wrong thing can corrupt your mine and or server.</li>
  
	
  <li><b>tech.mcprison.prison.spigot.block.SpigotBlock:</b> This is the block that was "hit". If this is part of an explosion event, then the other blocks will be in the explodedBlocks collection.</li>


  <li><b>List &lt;SpigotBlock&gt; explodedBlocks:</b> These are the blocks from explosion events.  If there wasn't an explosion, then this list will be empty.</li>
  
  
  <li><b>tech.mcprison.prison.mines.features.MineBlockEvent.BlockEventType:</b> This identifies what event type triggered Prison to handle the event.  Valid values will be: `blockBreak`, `TEXplosion`, and `CEXplosion`.  As new plugins are added to prison, there may be more possible values.</li>
  
  
  <li><b>String triggered:</b> If the BlockEventType is `TEXplosion` then this field <i>may</i> contain the name of the enchantment that triggered the TE Explosive event.  Older versions of TE does not support this field, so if you are not seeing this field being populated, then check to see if you can upgrade to a newer version.</li>
  
  
  <li><b>Plus</b> all of the standard parameters from the BlockBreakEvent.</li>
  
  
</ul>


<hr style="height:3px; border:none; color:#aaf; background-color:#aaf;" />



## Prison Event: tech.mcprison.prison.spigot.api.PrisonMinesBlockEventEvent

**Coming soon!!**

This event is identical to the PrisonMinesBlockBreakEvent as far as the fields and data that it contains, but this event will be raised before running the block events.

This event will probably be enhanced to contain more of the details related to the BlockEvent.


<hr style="height:3px; border:none; color:#aaf; background-color:#aaf;">


## Prison Event: tech.mcprison.prison.mines.events.MineResetEvent

This event is raised before a mine is reset.  It can be canceled.


<ul>

  <li><b>tech.mcprison.prison.mines.data.Mine:</b> The actual Prison Mine object.  You have full access to the whole mine's data; be very careful in what you change and modify since changing the wrong thing can corrupt your mine and or server.</li>
  
</ul>


<hr style="height:3px; border:none; color:#aaf; background-color:#aaf;" />


## Prison Event: tech.mcprison.prison.ranks.events.RankUpEvent

This event is raised before a Rankup is finalized, and after everything else has been processed.  This event can be canceled to prevent a rankup from happening.

If this event is canceled, please provide a reason for the field `cancelReason` so it can be added to the log.

Please note, this contains a `cost` field, but the currency for that cost is within the `newRank` field.


Note: The following are a list of fields.  They will be expanded in the future in to a list with more information.

```
  private RankPlayer player;
  private Rank oldRank;
  private Rank newRank;
  private double cost;
    
  private RankupCommands command;
  private PromoteForceCharge forceCharge;

  private boolean canceled = false;
  private String cancelReason = null;
```

<hr style="height:7px; border:none; color:#aaf; background-color:#aaf;" />



# Using jitpack.io as a Prison repo


You can use jitpack.io for your builds if you need to include Prison in your project.  The following instructions are based upon the details listed at the jitpack.io web site.  Since Prison uses gradle, these instructions are for gradle.  Their web site has information for maven and a few other repo types.



**Please Note:**  I used their web site to construct the following directions, along with the correct settings to use. I should point out that it can take a few attempts (up to 12) to get a new release from Prison to build successful.  This is related more to gradle's short timeout that does not provide enough time to download and build all dependencies.  After v3.2.7 was released the gradle timeouts were increases so future builds should be more successful.

Use of this site is your choice, but we cannot provide support if the Prison project is unable to be compiled in your project.  - Blue



### How to use jitpack.io's Web site for Prison

Through jitpack.io's web site you can select a Prison release and it will generate the correct dependency to use.  Click on the following button to browse the options available for Prison:

[![Release](https://jitpack.io/v/PrisonTeam/Prison.svg)](https://jitpack.io/#PrisonTeam/Prison)



The link above get's you to Prison's content, but the general instructions to use the site is as simple as looking up the repository.  On the main page of jitpack.io enter `com.github.PrisonTeam/Prison` in to the search field and press the button "**Look Up**".  It will return information on prison's commits to the master branch.  You can enter other github projects this way too.


Next you will see a table of versions with tabs at the top (they don't look like tabs).  The default tab is named Releases and you can use one of the items on this page.  You can also click on the tab named "Builds" which will also list of Versions, but this tab could include different variations for each version. For example it will include "v3.2.7" and "3.2.7", but look under the Status column and select one that has the "Get It" button.  Click on that "Get It" button to populate the examples at the bottom of the page with the selected builds, then you can just copy and paste that information.



### Selecting from the 'bleeding' branch


If you need to use the current bleeding branch, then you can copy and paste the following text in to the Project Lookup field: `com.github.PrisonTeam/Prison/bleeding`



Within your `repositories` section, add jitpack.io as the last entry, as suggested by jitpack.io.

```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

Then add the dependency as follows, but replace `Tag` with a valid tag such as `3.2.7`.  See the link to jitpack.io's web site for a list of Tags that you can use; select from the tabs Releases, Builds, Branches, and Commits.

```
dependencies {
	implementation 'com.github.PrisonTeam:Prison:Tag'
}
```


### Examples using various settings for the 'Tag'


The following uses the most recent commit on the bleeding branch:

```
dependencies {
	implementation 'com.github.User:Repo:bleeding-SNAPSHOT'
}
```


This uses Prison's master branch, release v3.2.7:

```
dependencies {
	implementation 'com.github.PrisonTeam:Prison:3.2.7'
}
```


The following is for the bleeding branch snapshot, and is using the `compileOnly` keyword instead of `implementation`.

```
dependencies {
	compileOnly 'com.github.PrisonTeam:Prison:bleeding-SNAPSHOT'
}
```

Then all that is left is to build the project.  



### If you run in to challenges using jitpack.io


Good luck with your project!  Hopefully jitpack.io can provide the dependencies that you need to build your project using Prison. 


From what I've seen, once their web site has the green "Get It" button it will usually work well.  If the button is not green, but gray, then that means it has not built a local instance of that release yet.  Clicking on "Get It" will initiate a build which can take up to 15 minutes.  If it fails, you can retry the request. If it won't let you, please visit **Prison's Discord Server** and ask Blue to resubmit (Blue has access to resubmit failed builds).  Note builds usually fail because they could not complete in a limited about of time, but upon multiple attempts it will succeed.


Please note that other than these instructions, and requests to resubmit a failed build, we are unable to provide support if attempts to get this to work fails.



<hr style="height:7px; border:none; color:#aaf; background-color:#aaf;">


