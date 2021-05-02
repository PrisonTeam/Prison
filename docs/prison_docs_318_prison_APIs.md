
### Prison Documentation 
[Prison Documents - Table of Contents](prison_docs_000_toc.md)

# Prison's APIs and Events


The primary focus of prison is not the Prison APIs or providing Events that can hook in to the prison internals.  

But APIs do exist, and so do a few Events.  More will be added in the future too, but only on a per request basis.  So if you are trying to do something with prison that takes a lot of effort, odds are that you may need to talk to us about adding a new API to make it easier, or at least perhaps we can show you an easier way to accomplish the task.  Please contact us through discord.


Prison does not have any formal maven repository, or similar repos.  The source is only within github so your builds will have to be performed over that, or find some other services such as https://jitpack.io/.  Your choice on how to work with the project will be up to you.


<hr style="height:3px; border:none; color:#aaf; background-color:#aaf;">



# API Classes


There are two primary packages that contains API related classes.  One at the project level, and the other strictly through the spigot plugin.



**Module: prison-core**

`tech.mcprison.prison.PrisonAPI`


**Module: prison-spigot**

`tech.mcprison.prison.spigot.api.PrisonSpigotAPI`


`tech.mcprison.prison.spigot.api.PrisonMinesBlockBreakEvent`



<hr style="height:3px; border:none; color:#aaf; background-color:#aaf;">


# Using jitpack.io as a Prison repo


Since Prison is not setup in any public repos, you can use jitpack.io for your builds.  The following instructions are based upon the details listed at the jitpack.io website.  Since Prison uses gradle, these instructions are for gradle.  Their website has information for maven and a few other repo types.


https://jitpack.io


**Please Note:**  I used their webpage to construct the following directions, along with the correct settings to use, but I could not get the build to work in another project using the `implementation` keyword, but it did compile with the keyword `compileOnly`.  In this test project I do not have code that is using any of Prison's classes, so that could make a difference too.  Use of this site is your choice, but we cannot provide support if the Prison project is unable to be compiled in your project.  - Blue



Look up the repository.  On the main page of jitpack.io enter `com.github.PrisonTeam/Prison` in to the search field and press the button "**Look Up**".  It will return information on prison's commits to the master branch.  


Next you will see a table of versions with tabs at the top (they don't look like tabs). Click on the tab named "Builds".  This will show a list of Versions. For example it will include "v3.2.6" and "3.2.6", but look under the Status column.  Select one that has "Get It" button to populate the examples at the bottom of the page.



If you need to use the current bleeding branch use `com.github.PrisonTeam/Prison/bleeding`



First, you need to add jitpack.io to the your repository listing.

```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```


Then you need to add the jitpack.io dependency as the very last of your dependencies.


This is the generic format:

```
dependencies {
	implementation 'com.github.User:Repo:bleeding-SNAPSHOT'
}
```


This is the formated version for Prison's master branch:

```
dependencies {
	implementation 'com.github.PrisonTeam:Prison:3.2.6'
}
```


The following is for the bleeding branch snapshot, and is using the `compileOnly` keyword instead of `implementation`.

```
dependencies {
	compileOnly 'com.github.PrisonTeam:Prison:bleeding-SNAPSHOT'
}
```

Then all that is left is to build the project.  



<hr style="height:3px; border:none; color:#aaf; background-color:#aaf;">


