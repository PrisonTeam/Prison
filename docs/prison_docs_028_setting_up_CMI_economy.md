
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


You need to use and install the normal version of Vault on your server, which is available here: [Vault from SpigotMC.org](https://www.spigotmc.org/resources/34315/).


CMIE needs to customize Vault to add hooks so the two can communicate with eachother.  This is accomplished by using CMIE's Vault Injector.  Upon starting the server, the CMIE injector will dynamically add it's hooks to Vault.


Beware though, CMI offers a prebuilt version of Vault that contains the CMIE's hooks. The prebuild version of Vault should never be used due to past attempts to it has always resulted in failure.  


Therefore, it is recommended to only use the CMIE Vault injector with the normal version of Vault, which has worked every time.


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
  max-attempts: 12
  inspect-vault: true
  triggers:
    vault: true
    vault-economy-name: Economy_CMI
```

Please note that different values for the **vault-economy-name** may be required for different versions of the plugins.  Some possible valus may include: 

 * **Economy_CMI**
 * **Economy_Essentials** (EssentialsX: <= v2.18.x)
 * **VaultEconomyProvider** (EssentialsX: >= v2.19.x)
 


<hr style="height:3px; border:none; color:#aaf; background-color:#aaf;">


# Additional Changes maybe Needed

If Prison is not able to startup with the settings above then there are a few things that need to be checked, and maybe changed.

**Setting the value for: vault-economy-name**

Confirm that Prison is able to successfully inspect the current Vault economy.  If it is not able to find what is set for the `delayedPrisonStartup.triggers.vault-economy-name`, then it needs to be updated.


This value may change with other economy plugins, and with future economy plugin updates.  


If this setting needs to be changed, then Prison will try to display what value to use.  Ensure the config setting `delayedPrisonStartup.inspect-vault: true` is set to true. This setting will allow the following message to be shown in the console. A value of **Failed** generally indicats that no economy plugin was found.

```
Inspect Vault Economy: enabled  
Use 'VaultEconomyProvider' with 'delayedPrisonStartup.triggers-vault-economy-name'
NOTICE: Prison Delayed Enablement: Prison startup has been delayed.  Waiting for a Vault Economy to be enabled.  Attempts: 0 of 12.  Submitting task...
```

```
 Prison Delayed Enablement: Failed to find an active Vault Economy Named 
 'Economy_CMI' after 13 attempts. Cannot start Prison.
```

The above value `VaultEconomyProvider` indictes that Vault is trying to use EssentialsX economy instead of CMIE. This is an example of EssentialsX hooking in to Vault before CMIE.  If this is the situation you're seeing, thn disable EssentialX's economy within their config file to allow CMIE to load.  


NOT the correct value to use for CMIE is: **Economy_CMI**


If Vault is loading the correct economy, but Prison's config setting is incorrect, then you can find the correct value in the first message whre it says "Use.." then that value provided.  Update the config.yml file with the displayed value.


**Setting the cooldown seconds and max attempts:**

If Prison is able to properly lock on the Vault economy, but CMIE is loading after Prison stops waiting, then you can increase either the `delayedPrisonStartup.cooldown-secs` or the `delayedPrisonStartup.max-attempts`.  Increasing either, or both, could help.  It's probably better to increase the max-attempts.


<hr style="height:3px; border:none; color:#aaf; background-color:#aaf;">


# Getting Additional Help

If Prison's startup delay has been enabled, and you tried to make adjustments to the **delayedPrisonStartup** settings, then please contact Blue on Prison's discord server since there could be another conflict going on.  Blue can review your server's startup log to identify the problem and help you fix it.




<hr style="height:3px; border:none; color:#aaf; background-color:#aaf;">
