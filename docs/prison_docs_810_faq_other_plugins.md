

### Prison Documentation 
[Prison Documents - Table of Contents](prison_docs_000_toc.md)

## FAQ - Other Plugins

This document covers some common issues that pertain to other plugins, but may not actually be related to Prison.

<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">

# CMI - Used for the Economy within Prison

**Symptoms of Issues**

Symptoms of issues with CMI is when it appears like Vault is using CMI has successfully loaded, but yet the Ranks module fails to load.


**Overview of CMI**

CMI Is a premium plugin and costs about 15.00 Euros.  It is a very extensive plugin that covers many aspects of what you could possibly do with a server.  The developers claim it is a worthy replacement for EssentialsX.  The primary aspects CMI with what Prison needs to use is their economy and permissions.
[CMI on spigotmc.org](https://www.spigotmc.org/resources/cmi-270-commands-insane-kits-portals-essentials-economy-mysql-sqlite-much-more.3742)


**How to Resolve the Issues**

In order to use CMI with prison, it is required that you use Vault.  Prison does not have any direct integrations to CMI. Since it is a premium plugin, the CMI integration for Vault is not publicly available in the original Vault plugin.  Instead CMI offers their own modified version of Vault that has been recompiled with their hooks added, or an injector that will add it to the original Vault plugin. 


On their spigotmc.org resource page, they identify there are two way to enable a working version of Vault:
1. Install and use their modified version of Vault (recommended)
2. Or use the CMIEInjector to modify an original Vault (not recommended)


**Common Problems**

1. Make sure you are using the CMI modified version of Vault
2. The CMIEInjector has problems, or will cause problems.  So don't use it.
3. Do not use both the CMI modified version of Vault and the CMIEInjector. They were never intended to be used together and they will cause problems with prison.


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">


# Prison Won't Load - No Modules Loaded - LuckPerms is the Cause

**Symptoms of Issues**

Symptoms of issues is that Prison fails to start up and load properly. The biggest symptom is that there are no active modules and most of the prison commands fail to work.


**Cause**

This is caused by LuckPerms.  More specifically, trying to use LuckPerms v5.0.x with Prison v3.2.0 or older.  LuckPerms v5.0.x is not supported with older version of Prison.


**How to Resolve the Issues**

There are two major ways to resolve this issue.

1. Down grade to LuckPerms v4.4.1 for use with Prison v3.2.0 and older.

2. Upgrade Prison to v3.2.1 (or the pre-release version of v3.2.1) or newer.  Prison v3.2.1 supports both the newer LuckPerms v5.0.x and the older LuckPerms v4.4.1 and older. 

**Suggested action:** Upgrade Prison to v3.2.1 or newer and use the latest version of LuckPerms.


**Background**

Basically the fault of this issues lies with LuckPerms in using the same registered 
plugin name with the bukkit/Spigot Plugin Manager for v5.0.x.  By using the same registered
plugin name and the same class name, but an altogether different package name, that broke
the standard way of using the plugin manager to get an instance of the plugin. This would 
have caused any plugin to generate a Class Not Found exception. 

They shouldn't have used the same signatures for v5.0.x as they did with v4.x. 
If they would have chosen a new signature, such as LuckPerms5, then there would have been no issues since the plugin manager would not falsely report that the old version of LuckPerms
is available and will not try to cast the new packages in to the old variables.


<hr style="height:1px; border:none; color:#aaf; background-color:#aaf;">
