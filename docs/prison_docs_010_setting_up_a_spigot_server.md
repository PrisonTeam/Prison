
### Prison Documentation 
[Prison Documents - Table of Contents](prison_docs_000_toc.md)

## Setting up a Spigot Server

The Prison documentation covers how to setup a Spigot server for use as either
a test environment, a production server.  These instructions covers how to
use the spigotmc's buildtools to simplify not only the initial setup, 
but also provides easy updates.  

Buildtools also allows easy setup of many test environments since all you 
would need to do is to just change the version.



<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


# Setting up Java
This is intended strictly as a high-level overview on how to setup Java.
If you need more assistance, please search for online documentation since
there are many good resources out there.

*  First install a copy of Java that is accessible from the command line.  
	- It’s strongly suggested to use only the latest version 1.8.0_x since that is the version spigot and most other plugins are developed under.
    
    
*  You can download it from [Sun SE Development Kit 8]https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html) for product that you need.


*  You can also use Open JDK if Sun's license does not fit your needs. [OpenJDK Install](https://openjdk.java.net/install/)


*  Test the java install with `java -version` from a command line

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# Setting Up and Running BuildTools

*  Download the spigot `BuildTools.jar` file.  Follow the directions from here:
	[https://www.spigotmc.org/wiki/buildtools/](https://www.spigotmc.org/wiki/buildtools/)


*  Once you have downloaded BuildTools.jar, you will run *one* of the following commands (only one) from the command line.  Multiple versions are shown as an example of how by simply specifying different versions, the same BuildTool will generate the different environments.  The Windows and linux command line usage are the same.  See the provided URL in the first step above for the list of valid versions that are supported by the BuildTools.jar file.  

```
  java -jar BuildTools.jar --rev 1.8.8
  java -jar BuildTools.jar --rev 1.12.2
  java -jar BuildTools.jar --rev 1.13.2
  java -jar BuildTools.jar --rev 1.14.4
  java -jar BuildTools.jar --rev 1.15.1
```

*  For example, with BuildTools.jar being in a root directory, create a subdirectory and then start a build within that directory.  The benefit is that you can use the same BuildTools.jar to build multiple different versions of spigot.  This example starts off with building a v1.14.4 instance and then builds a 1.8.8 instance.

```
  mkdir spigot-1.14.4
  cd spigot-1.14.4
  java -jar ../BuildTools.jar –rev 1.14.4
  cd ..
  mkdir spigot-1.8.8
  cd spigot-1.8.8
  java -jar ../BuildTools.jar –rev 1.8.8
```

*  **Updating BuildTools:** Once in a while you will be prompted to update the BuildTools.jar file. To do update it, all you need to do is to just download it, and replace the older one you were using.  


*  **Updating the built servers:** Every once in a while, when you are starting a server, there may be a notification that the server software needs to be update.  Just rerun the BuildTools for the same version within the original build directory.  The build tools will update all of the changed resources and then generate the new server jars that you copy to the actual server (see the next step).  


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# Creating a Runnable Spigot Server

*  Create a runnable server directory by creating a new directory outside of the build directory. Then copy the newly generated jar file, such as spigot-1.14.4.jar, to the new server run time directory. 


*  Windows example, if you’re still in the build directory:

```
  cd ../..
  mkdir spigot-1.14.4_server
  copy /B builds\spigot-1.14.4\spigot-1.14.4.jar spigot-1.14.4_server
```

*  Linux example, if you’re still in the build directory:

```
  cd ../..
  mkdir spigot-1.14.4_server
  cp builds/spigot-1.14.4/spigot-1.14.4.jar spigot-1.14.4_server
```


*  Run the server for the first time (see the next step). It will start to auto-generate the server environment and then will stop.  You will need to manually modify eula.txt and set eula=true.  Save.  Close.  Restart the server.  It will startup successfully now.


*  This is a simple example of what is needed for a windows cmd file. It sets the minimum memory to 2 GB and the max to 8 GB.  A linux script would be similar, but without the pause.

```
    java -Xms2g -Xmx8g -jar spigot-1.14.4.jar
    pause
```

*  Let the server fully start for the first time, then stop it by entering “stop” in the open server console.  The server is now ready to be customized.


This should give you a functional server for which to run the Prison plugin.  


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# Customizing your Server

You can now customize the server, such as by adding plugins, etc...


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">



# Other Server Platforms

The same general instructions apply to paper server, but the big difference is that you download the runnable paper jar file from https://papermc.io/downloads and then place them in to your server runtime directory, and then follow the steps under **Creating a Runnable Spigot Server** but use the resources for the other platform.  Then run the same general startup scripts.


