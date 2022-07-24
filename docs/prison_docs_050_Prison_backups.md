
### Prison Documentation 
[Prison Documents - Table of Contents](prison_docs_000_toc.md)

## Prison - Prison Backups

Prison has the new capability with v3.3.0-beta.12d to make backups of all of its files.  Prison will automatically make a backup when it detects that the server is starting up with a new version of prison.  Manual backups are also available.

All backup files are zip files and can be opened, unzipped, or inflated by almost any zip/compression tool.

Prison will generate temporary files through different conditions, such as if it detects a possible problem when saving data.  During a backup, prison will identify all of these temporary files, and will purge them once they are added to a backup, thus cleaning up the file storage that prison uses.

If anything needs to be recovered from a backup, then it must be done manually.


<hr style="height:3px; border:none; color:#aaf; background-color:#aaf;">


# The Prison Backup Command

The Prison back command is:

`/prison support backup <notes>`

This command generates a manual backup.  The notes are used to append to the file name so as to  help identify the purpose of the backup.


<hr style="height:3px; border:none; color:#aaf; background-color:#aaf;">


# Backup Locations

The backups are stored in the directory:

`plugins/Prison/backups/`

None of the backups are ever removed automatically.  If they are to be removed, they must manually be removed.

<hr style="height:3px; border:none; color:#aaf; background-color:#aaf;">



# The versions.log file

All backups are logged in the file: 
`plugins/Prison/backups/versions.log`

Also all detections of version changes for Prison are logged in this file, which is how prison detects when a new version of prison is being used.

It should be noted that only the last entry in the versions.log file is used to compare to the current version, and if the versions do not match, then it forces a version change backup.  The important point here is that "any" change will be detected and trigger a backup, which will work if downgraded or if it skips many versions.

<hr style="height:3px; border:none; color:#aaf; background-color:#aaf;">



# The "Backup Stats" Report

Within each backup, there will be generated one Backup Stats report that will be placed at the root directory of the backup. It is a plain text file.

This report contains detailed information on what temporary files were backed up and then deleted.

The rest of this report is a dump of all of the `/prison suppor submit` documents, all combined together in to one document.

<hr style="height:3px; border:none; color:#aaf; background-color:#aaf;">



# The Auto Backup on Version Change Detection


The problem with backing up the files within Prison upon startup, is that the Backup Stats file cannot be generated because all of the sub-systems that build that report, have not yet been loaded.  But there is a risk that on a version update, the original configuration files may be automatically backed up.

To deal with this issue of starting up and potential changes to the config files, prison, upon startup, will make a backup of all config files.  That way, if any are updated upon startup, their prior state and format will have been preserved as a backup file.

The list of configuration files that are copied as backup files:

 - config.yml
 - autoFeaturesConfig.yml 
 - blockConvertersConfig.json
 - modules.yml
 - module_conf/mines/mineBombsConfig.json
 - SellAllConfig.yml
 - GuiConfig.yml
 - backpacks/backpacksconfig.yml
 
All of these files are copied with a new name that follows this format:
`<fileName>.newPrisonVersion_<timestamp>.bu`

The format of the `<fileName>` is the original file name, as listed above, with the file suffix.

The format of the `<timestamp>` is:
`yyyy-MM-dd_HH-mm-ss`
 

The location of these backup files will be located in the same directory as the original files.  An example of what the `plugins/Prison/` folder enteries would look like is as follows:


```
autoFeaturesConfig.yml
autoFeaturesConfig.yml.newPrisonVersion_2022-07-06_22-18-23.bu
blockConvertersConfig.json
blockConvertersConfig.json.newPrisonVersion_2022-07-06_22-18-23.bu
config.yml
config.yml.newPrisonVersion_2022-07-06_22-18-23.bu
GuiConfig.yml
GuiConfig.yml.newPrisonVersion_2022-07-06_22-18-23.bu
modules.yml
modules.yml.newPrisonVersion_2022-07-06_22-18-23.bu
SellAllConfig.yml
SellAllConfig.yml.newPrisonVersion_2022-07-06_22-18-23.bu
```

**Please note:** All of these backup files will be removed once the backup is finished.

<hr style="height:3px; border:none; color:#aaf; background-color:#aaf;">



# Temporary Files Will Be Purged after being Backed Up


Prison Backups will identify various temporary files.  These files will be added to the current backup, and then they will be removed from the server's file system.

This is a list of identifiers that are used to detect temporary files, where `*` is a wild-card character.

 - `*.bu`
 - `*.temp`
 - `*.del`
 - `_archived_*`
 - `*.json.ver_*.txt`

**Warning:** When temporary files are backed up, they are removed from the server's file system.  Therefore the **only** copy of those files are within that specific backup file.  Therefore, it is very important that you do not delete these backup files without careful consideration.  Deleting them will mean the permanent loss of those files.





<hr style="height:3px; border:none; color:#aaf; background-color:#aaf;">

