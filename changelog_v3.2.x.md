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


# V3.2.4-alpha.1 2020-12-27


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


