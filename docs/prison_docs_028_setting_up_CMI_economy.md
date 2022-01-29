
### Prison Documentation 
[Prison Documents - Table of Contents](prison_docs_000_toc.md)


## Setting up CMI Economy

CMI Economy, CMIE, is a feature rich economy plugin, but it requires special configuration settings within Prison to get it to work perfectly.

*Documented updated: 2022-01-29*

<hr style="height:3px; border:none; color:#aaf; background-color:#aaf;">


# The Challenge of CMI Economy

CMI "tries" to load last, thus it can ensure all of it's dependencies and hooks are in place before it starts up.  That's understandable, and Prison also has similar requirements and expectations. Unfortunately, this also causes a conflict with Prison, since Prison must perform validation on startup, and if there is no economy, then Prison could fail to start.

To get CMIE to work correctly with prison, there are a couple of things that you must do.


<hr style="height:3px; border:none; color:#aaf; background-color:#aaf;">


# Using the 'Correct' version of Vault

You cannot just use the vanilla version of Vault, since CMI must hook in to Vault.  Most plugins are added to the public release of Vault, but CMI has chosen not to add a public interface directly to Vault. 


Therefore, you must use a customized version of Vault that will work with CMI Economy.


CMI offers a prebuilt version of Vault that includes their hooks, but in all the past attempts to use it, it has never worked, so we do not recommend using that version. 


It is recommended to use their Vault Injector with the normal version of Vault, since that has always worked well.


Download the normal version of [Vault from SpigotMC.org](https://www.spigotmc.org/resources/34315/).


To download the CMI Economy Vault Injector, visit [CMI's page on SpigotMC.org](https://www.spigotmc.org/resources/3742/), then look under Economy's second option for the injector.


Then update the CMI config settings to enable the CMI Economy. 


<hr style="height:3px; border:none; color:#aaf; background-color:#aaf;">


# Enabling Prison's Delayed Startup for CMI Economy

The CMI Economy **has** to be fully loaded and active *before* prison loads the Ranks.  Otherwise, prison will refuse to load the ranks and prison will not work.  


CMI Economy delays it's startup, which causes problems for prison.  Because of this delay, Prison is unable to validate the Ranks and fails.  The solution, is that Prison has to wait until CMI Economy has fully loaded before it can use it.


Prison is able to delay it's startup, to wait for CMI Economy to become active.


To enable this, you need to make a configuration change within Prison's `plugins/Prison/config.yml` file.  Near the bottom of that config, are a few settings that need to be enabled.  The following is an example of what is needed, along with a configuration that will work in most situations.
  
```
delayedPrisonStartup:
  enabled: true
  cooldown-secs: 5
  max-attempts: 6
  inspect-vault: true
  triggers:
    vault: true
    vault-economy-name: Economy_Essentials
```

<hr style="height:3px; border:none; color:#aaf; background-color:#aaf;">


# Additional Changes maybe Needed

If Prison is not able to startup with the settings above then there are a few things that need to be checked, and maybe changed.


Confirm that it's able to successfully inspect the vault economy.  If it is not able to find what is set for the `delayedPrisonStartup.triggers.vault-economy-name`, then it needs to be updated.


This value may change, if it does, Prison will display what this value is within it's first startup message that notifies that it's going in to a delayed startup mode.  If the name shown does not match "Economy_Essentials", then use that new name in these config settings.


If it's able to properly lock on the Vault economy, but CMIE is loading after Prison stops waiting, then you can increase either the `delayedPrisonStartup.cooldown-secs` or the `delayedPrisonStartup.max-attempts`.  Increasing either, or both, could help.  It's probably better to increase the max-attempts.


<hr style="height:3px; border:none; color:#aaf; background-color:#aaf;">


# Getting Additional Help

If Prison's startup delay has been enabled, and you tried to make adjustments to the **delayedPrisonStartup** settings, then please contact Blue on Prison's discord server since there could be another conflict going on.  Blue can review your server's startup log to identify the problem and help you fix it.




<hr style="height:3px; border:none; color:#aaf; background-color:#aaf;">
