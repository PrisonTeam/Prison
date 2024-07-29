# Security Policy

## Supported Versions

All current releases of Prison will always be supported.  If you are having problems, 
or discover an issue, try upgrading to the latest release to see if its been addressed, 
or contact our support team on our Prison Discord Server (see below).  


If an issue is found, old releases will not be fixed.  The issues will be included in 
all future releases.  We are unable to re-release old versions.


| Version       | Supported          |
| ------------- | ------------------ |
| 3.3.0-alpha.x | :white_check_mark: |
| < 3.2.11      | :x:                |
| < 3.2         | :x: Note 1 :x:     |

NOTE 1 : Versions prior to v3.2 predate our working knowledge. All efforts were
         made with v3.2.x to remain compatible with pre v3.2 releases, but upgrading
         to v3.2 first should be tested prior to upgrading to something newer.
         

## Limitations 

Prison, our support staff, and our developers, cannot be held liable for any issues
encounterd during updates or usage of our software. We strongly suggest regular 
backups of your server, data, and plugins.  We strongly suggest that any changes are
first tested on a test server, and not a production enviornment to see how everything
will work on your setup.  

Upgrading from a very old release of prison should go smoothly, but since the 
v3.3.0-alpha releases, there have been some changes that may need to be addressed 
in configs, changes in plugins, or other actions. Always backup your server 
and all plugin data before updating.  It is strongly urged that you
test updates on a test server, and not your primary production server first. 

Support can help address and answer questions about some of thse concerns.


## Reporting a Vulnerability

If you find a security vulnrability, or an exploitable weakness, please 
report this to Prison's Discord server.  Please review the **#alpha-versions** 
channel to see if any recent releases have been published there.

[Prison's Discord Server](https://discord.gg/DCJ3j6r)


If it's a serious issue, and you do not want it to be shared publically on the
discord server, then request permission to DM an admin or a current developer.
Do not randomly DM any of the staff without permission, some may perm ban you 
since it is against the rules.
You have permission to always DM the primary developer: RoyalBlueRanger.


RoyalBlueRanger is usually able to provide fixes within a few hours, IRL
permitting.  If it's not a major issue, then the fix will be released to
the Prison Discord Server's **#alpha-versions** channel.  Critical issues
will result in new releases on the supporting distribution websites, such 
as SpigotMC.org, polymmart.org, and curseforge.com, but they can take a 
few hours of prep work to issue.


Types of vulnerabilities include:
* Internal Prison Sources - Bugs in code, or new features in Spigot that are
  not handled in Prison yet.
* Third Party Libraries - Prison uses a few third-party libraries to provide
  greater flexibility in supporting the wide range of Spigot releases.
* Unsupported Platforms - Prison is developed only to support Spigot, but
  indirectly we support almost all platforms that are built off of Spigot.
  If you find an vulnerability caused by an unsupported platform, we reserve
  the right to not support that platform, or to state that we cannot provide
  a fix, or work on finding a fix for the issue.  We will, within our best
  abilities, try to help address the unsupported platform, but we have
  limiations and will recognize when those limitation may prevent us from
  being successful.
* Conflicts with other plugins - We will strive to provide support for any
  plugin that could be having troubles with Prison, but within limits.
  Any plugin that has already been deemed as unsuportable cannot be
  supported; we cannot force other plugins to change what they do.
* Other plugins may release updates that could break compatibility with
  Prison - If a plugin update breaks something in prison, or another aspect
  of a server's functionality, we will try to support it and make the
  needed changes in Prison, especially if prior version of the other
  plugins worked well with Prison.  But we reserve the right to not
  provide support if we are unable to properly support the newer releases
  of the plugin.  Some examples could include not being able to get the
  newer versions of API support jars for compiling prison (premium
  plugins may prevent our access to api jars).

* Other issues - There are numerous possibilities that could result in
  varous security vulnerabilities, exploits, or other bugs. We reserve
  the right to be able to address each of these as needed, with valid
  solutions being that it's not fixable, or we cannot support another
  plugin or platform.

We will try our best to address all issues that are brought to our attenttion,
but there may be limitations that could prevent an ideal solution to 
address the issues.

