This pertains to the bbcode that is within this directory:


The targeted editor is the bbcode editor on the spigotmc.org site.  It is recognized that most people will not have access to the editor.  In order to use it, or have access to the editor, a resource needs to be edited.  If such an editor cannot be used, then the following may be a substitution but strictly follow the guidelines.




If you are unable to have access to a spigotmc.org editor, then the following could be used, but with caution.  Use at your own risk, but it can be a reasonable fall-back.

The following editor works and will show how the page will render (mostly), but the images are not properly sized, when compared to spigotmc.org.  This is good for editing text, but do not use this editor to format any images; keep the image sizes the way they are.  It probably is also a good idea not to use this editor for tweaking sizes or spaces either, since minor things like that may render differently on spigotmc.org too.  
https://www.sceditor.com/



The following is an example of an online bb editor that does not work since it is unable parse all of the codes:
https://www.systutorials.com/tools/bbeditor/


<hr style="height:5px; border:none; color:#aaf; background-color:#aaf;">


## Sites that need to be updated


### spigotmc.org
  https://www.spigotmc.org/resources/prison.1223/
  
The bbcode is designed and targeted for spigotmc.org so this should work the best.  I find that some things are best modified by hand instead of using their editor so you have better control over the items being changed since nesting can get somewhat deep.


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


  
### Polymart.org
https://polymart.org/resource/prison-1-8-x-1-16-5.678

Polymart.org also uses bbcode, but it's not exactly the same.  There were two major problems.

First the bbedit tag [list] is displayed on spigotmc.org as an unordered list.  But on Polymart it is shown as an ordered list.  For some reason, by going in and out of the editor, and perhaps adding a line feed here or there, Polymart converted it back to an unordered list.  Polymart is unable to decode [list=1] so I just converted it back to a plain [list].


Secondly, the most severe issue was with the [img] tags.  They are structured properly, but they would not render correctly.  They would show the full bbedit [img ...] statement, minus the first four characters of "[img]".  Going back in to the source thy appeared correct.  Not really sure what fixed it, but by putting [img] on a new line and removing the alt attribute, they were able to render correctly, although the code was identical in all other ways.

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



### bukkit.org, curseforge.com, twitch

Twitch owns bukkit.org and curseforge.com and those two sites are linked together: publish to one site, and it appears on the other.  I publish to bukkit.com.

https://dev.bukkit.org/projects/mc-prison-v3
https://www.curseforge.com/minecraft/bukkit-plugins/mc-prison-v3


This site does not use bbedit codes.  What it does use is html.  


** NOTE : The following is important to understand** since I went back and tried to update the site and it failed because I tried to do it a different way.  lol


To get the "translated" html from the bbedit codes, I opened spigotmc.org within firefox's developer's tools to inspect the container that help the html code.  I copied all of it and pasted it in to the editor in bukkit after enabling the code view of the editor.  I kept the initial content.  It worked fine without having to tweak anything after that. It may not be a perfect rendition since it does not carry over the css that is used within spigotmc.org, but it's not bad overall. It is not worth trying to get the css working better (you can't change it anyway). 


Note: bukkit.org has an option for a "markdown" editor, but it does not work.  It's bad.  It's trash.


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">
