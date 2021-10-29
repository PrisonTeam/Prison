# Prison Videos

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">

[Prison Documents - Table of Contents](prison_docs_000_toc.md)

[Prison Video Listings](prison_video_000_video_list.md)


# Prison Videos - Basic Setup - Auto Configure

This video covers the very basics on what you need to do to get your Prison server up and running.  Prison has an Auto Configure feature that will setup almost everything you need to run the most basic Prison Server.


[video url goes here](url.to.video.com)


This document is the transcript for this video.  It includes a few screen prints, URLs to resources, and copy and pastable commands.  This document will also contain some updates, more information, and clarifications if they are needed.


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


#Video Transcript

> "Hello, this is RoyalBlueRanger and I'm the lead developer of the Prison Plugin.  Thank you for your interest in Prison, and for taking the time to view this video.  This video will cover the basic setup of a Prison server, which can serve as the foundation of building a uniquely customized server of your own."
> 
> 
> "If you ever need help with Prison, Please visit our Discord server.  You will also find on our Discord server the latest alpha releases, which not only fixes any bugs or issues found, but also contains the new updates that will be included in Prison's next release."
>

Prison's Discord server: 
[![Discord](https://discordapp.com/api/guilds/332602419483770890/widget.png)](https://discord.gg/DCJ3j6r)

>
> "Prison works great on Spigot 1.8.8 through Spigot 1.17.1.  Since it's built on Spigot, it will also work great on other platforms based upon Bukkit and Spigot, such as paper.  For the purpose of this demonstration video, I'm using Spigot 1.8.8."
>
>
> "I should point out that this video will not cover the details on how to build a Spigot server; that would be the subject of a future video.  For this video it is assumed you have your sever built and it's ready to be started.  But before you start your server, let's first go over some of the basic plugins you will need."
>
>
> "All plugins will need to be placed in the **plugins** directory within your Spigot server's directory.  If you do not see that directory, go ahead and create it manually, or just start the server for the first time and spigot will create it for you."
>
>
> "Download the following plugins, which will be in **jar** files. You can find the URLs for these resources within this video's description below, and also within Prison's documentation for this video.  You will need this plugin, which is of course **Prison**. Plus **EssentialsX**, **Vault**, **LuckPerms**, **WorldEdit**, and **WorldGuard**.  Please note for all of these listed plugins, download the latest version that is available, except for Vault, WorldEdit and WorldGuard, of which you must use the version that is designed for your version of Spigot."
>

(URLs to all mentioned plugins)
 * Prison 
     - Download - https://www.spigotmc.org/resources/prison.1223/
 * EssentialsX 
     - https://www.spigotmc.org/resources/essentialsx.9089/
     - Download - https://essentialsx.net/downloads.html?branch=stable - Download only the stable release of the "core" component. 
 * Vault
     - Download - 1.13 to 1.17 - https://www.spigotmc.org/resources/vault.34315/
     - Download - 1.8 to 1.11 use v1.5.6 - https://dev.bukkit.org/projects/vault/files
 * LuckPerms
     - Download - https://www.spigotmc.org/resources/luckperms.28140/
     - Download - https://luckperms.net/
 * WorldEdit
     - Download - https://dev.bukkit.org/projects/worldedit/files
 * WorldGuard
     - Download - https://dev.bukkit.org/projects/worldguard/files

(Screen prints of basic directories prior to starting the server the first time)

> 
> "Let me show you the directories of this test server. As you can see, in the server's directory is the plugins folder, the eula.txt file that has been set to `true` so the server starts up fully for the first time, and the spigot jar file along with the startup script to start the server.  As you can also see in the plugins directory, are the previously listed plugins."
> 
> 
>
> "Once you have placed all of these plugins in the server's **plugin** directory, you can can then start your server.  Let it fully startup.  It may be a good idea to review the startup messages to ensure everything loaded successfully.  Every time you add a new plugin, or make significant changes, you should review the startup messages in the console if something appears to be misbehaving. With these few plugins, the server should startup with no issues, especially if you are using the correct version of Vault, WorldEdit and WorldGuard."
>
>
> "Before we configure Prison, let me first introduce you to some very basic details about Prison.  For the purpose of this video, and when using most Prison commands that generate a lot of text and information, it's best to run them within the server's console, instead of in-game."
>
>
> "First off, it is important to know that all commands within Prison can be reviewed with the command `/prison`.  This command lists all of Prison's root commands that are available, including any commands that had a conflict with another plugin's commands when prison was registering it's commands.  If a listed command has subcommands, it will be indicated in that list.  You can then enter that command to drill down to see the list of subcommands."
>

(Screen print of `/prison`)

>
> "Another important feature with Prison, is that Prison's command handler is able to provide detailed information about the command, including all parameters, permissions, and even links to some online documentation.  To view the **help**, all you need to do is to add the `help` keyword to any command.  For example: `/ranks autoConfigure help` provides detailed information on the autoConfigure command.
>

(Screen print of `/prison autoConfigure help`)

>
> "Finally let's get to the purpose of this video, running Prison's autoConfigure command."
> 
> "From the server's console, enter the command:"
> `/ranks autoConfigure`
> 
> "The command will run for a few seconds, generate a lot of text, which documents all of the configurations that it sets up.  Review the generated text in the console if you like."
> 
> 
> "And that's it.  This is the end of the video, which just covered the basics of running the command `/prison autoConfigure`.  The Prison server is now using the most common features and settings enabled that will allow your server to work properly."
> 
> 
> "In the next video we will cover what you need to do in-game to finalize your server setup, which is also very simple.  The details that will be covered is configuring WorldGuard to protect your Prison world so players cannot break blocks outside of the mines.  It will also explain the various ways you can **set the mines** which will turn the virtual mines in to fully functional physical mines.  At the end of that video, we will have a fully functional Prison server." 


(end of video)

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


