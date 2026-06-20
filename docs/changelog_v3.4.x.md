[Prison Documents - Table of Contents](prison_docs_000_toc.md)

## Prison Build Logs for v3.4.0

## Change logs
 - **[v3.3.1 - Current](changelog_v3.3.x.md)**
 - [v3.2.0 through v3.3.0-alpha.19](prison_changelogs.md)
 
*Note that known issues have not been updated in a long time. They can be found through the main `prison_changelogs.md` link above.*

These change logs represent the work that has been going on within prison. 


# 3.4.0-alpha.1  2026-06-20


** v3.4.0-alpha.1  2026-06-20**

**Build: Establish multi-environment artifact generation for Spigot 26+**

  * Created new prison-spigot26 subproject leveraging shared source code.

  * Successfully bridged local legacy library dependencies (lib/) to compile against modern XSeries v13.8.0.

  * Updated appveyor.yml to track and output the new Prison26 artifact alongside the legacy build.


.

.


# 3.3.1 2026-06-15

** v3.3.1  2026-06-15**

  Prison-3.3.1.jar <-- Support for spigot 1.8 through spigot 1.20.x
  Prison26-3.3.1.jar <-- Support for spigot 1.21.x and spigot 26 (manual build)
  
  NOTE: the latest release of paper will be fully supported in v3.4.0.




-------
------- Upgrading Gradle:
-------

  Use the following for a list of version for upgrading to:
  https://gradle.org/releases/

  * <code>gradlew wrapper --gradle-version=7.4</code> :: Sets the new wrapper version  
  * <code>gradlew --version</code> :: Will actually install the new version  
  * <code>gradlew build</code> :: Will build project with the new version to ensure all is good.  If build is good, then you can try to upgrade to the next version.



