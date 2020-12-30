[Prison Documents - Table of Contents](docs/prison_docs_000_toc.md)

## Prison Build Logs for v3.2.x

## Build logs
 - **[v3.2.4-alpha - Current](changelog_v3.2.x.md)**
 - **[v3.2.0 - 2019-12-03](docs/prison_changelog_v3.2.0.md)**
 - **[v3.2.1 - 2020-09-27](docs/prison_changelog_v3.2.1.md)**
 - **[v3.2.2 - 2020-11-21](docs/prison_changelog_v3.2.2.md)**
 - **[v3.2.3 - 2020-12-25](docs/prison_changelog_v3.2.3.md)**
 

Greetings!  I'm delighted that you are interested in the build logs for the
Prison plugin.  I'm wanting to provide a more formal documentation as to what 
is going on in each build so you have a better idea if it may be something 
that you need.


# V3.2.4-alpha.2 2020-12-30


* **Add support for a new Placeholder Attribute called text.**
This is strictly for debug purposes and also to format hex colors if they are not working in the other plugins that are using prison's placeholders.
This actually works very well, and has been tested with v1.16.x.
Update the docs and a few comments on the other attributes.



* **Added an alternative hex formatting for the placeholder attributes.**
This is another way to try to get hex color codes to work with plugins that do not support them directly.  
With testing, I found that hex2 format actually worked very well with other plugins and resulted in the hex colors working well.


* **Placeholder Attributes: Changed how debug works so hex can be added too.**  Instead of using a String array, converted it over to use a list.  The value of hex and debug are extracted prior to extracting any other parameters since those two values are now non-positional and can appear in any parameter location.
Debug statement now includes the original raw string that is non-converted to the spigot color codes so you can see what the original raw codes were for debugging purposes.  The parameter hex now will convert the color codes before sending the resulting placeholder value back to the plugin that is requesting it.  This may allow the successful use of hex colors in plugins that do not yet support them since the hex codes would have already been correctly converted.


* **Full support for hex color codes.**
The use of #abcdef will be converted to the correct color codes.  This applies only to prison messages and will not provide any translation for placeholders that are sent back to the requesting plugins that are using them.  They would have to support hex colors on their own.
This is only a feature that works with minecraft v1.16.x and newer.  Older versions of minecraft and spigot may produce undesirable artifacts.


* **V3.2.4-alpha.2 2020-12-29**
Bump version.


* **Upgrade Cryptomorin's XSeries to v7.8.0 from v7.6.0.0.1.**


* **Upgrade TokenEnchant to v18.11.3 to support the new TokenExplosionEvent's getTrigger().**
This allows BlockEvents to now support filtering on TE's plugins that trigger the explosion event.
Added support for editing and displaying the triggered parameter.


* **Added a new unit type to the number format attribute: binary.**
Binary is based upon the power of two and uses 1024 as a divisor instead of the base 10's 1000.


* **Updates for the placeholder attribute bar to work with the debug mode.**


* **New features added to the PrisonSpigotAPI to locate the given mine a block was broken in**,
or where a player is standing.  If it is being used for block related usage, then it would be best to base it upon the block that was actually broken since the player could be standing outside of the mine while mining (such as on top or to the side).
This code utilizes an internal prison player cache to help reduce overhead in location the mines.  The last mine a player was in when they successfully mined a block from a mine will be use as the first check. If they are not in that mine, then the others will be searched, but odds are that if they are mining blocks, then they will be getting more than just one before going elsewhere.
This code is similar to what's being used within the auto features.


* **Enhancement that provides for a way to prevent the translation of color codes within a given text String.**
Ran in to an issue with for display purposes, had to show the raw codes that were used, but there was no way to do so since they were being translated.  So added support for regular expression style of quotes to skip over a section of the string when translating.  These quotes are \\Q and \\E.  Everything between them will be ignored when translating color codes.


* **Hook up support for placeholder attribute support for bar graph customizations.**
Appears to be working well.


* **Updates to the placeholder attributes for the number formatting... little changes to get it to work better.**
This appears to be working really well. 


* **New feature: Placeholder attributes. Dynamic placeholder customizations.**
Allows for dynamic customization of different placeholders.  
The first placeholder attribute that is supported is the number format: nFormat.  This allows for full customization to be defined within a placeholder itself, so each use of that placeholder could be configured differently.
For example this placeholder provides the number of blocks remaining in a mine: %prison_mr_temp5%'
This can be customized with the following examples: 
  %prison_mr_temp5::nFormat:0.000:1:kmg% 
  %prison_mr_temp5::nFormat:#,##0.##:0:kmg%
  %prison_mr_temp5::nFormat:'&4$&2'#,##0.00'&7':3:kmg:debug%
The last example shows that formatting codes could be enclosed within the placeholder too, but probably shouldn't be use this way, but it can. 
The placeholder attributes also suports the debug parameter so as to provide detailed information in the log for admins to help diagnose possible issues when testing different settings.


* **v3.2.4-alpha1 2020-12-26**
Setup the alpha release.


# V3.2.3 2020-12-25 
**Merry Christmas!!**
Release of next bug update.



# V3.2.2 Release - 2020-11-21


