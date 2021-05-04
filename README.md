[![License](https://img.shields.io/badge/license-GPL%20License%20v3-blue.svg)](LICENSE.md)
[![Build Status](https://github.com/PrisonTeam/Prison/workflows/Prison%20Build/badge.svg)](https://github.com/PrisonTeam/Prison/actions?query=workflow%3A%22Prison+Build%22)
[![Build Status](https://travis-ci.org/MC-Prison/Prison.svg?branch=master)](https://travis-ci.org/MC-Prison/Prison)
[![Discord](https://discordapp.com/api/guilds/332602419483770890/widget.png)](https://discord.gg/DCJ3j6r)

1. [What is Prison?](#what-is-prison)
2. [Why should I use Prison?](#why-should-i-use-prison)
3. [Get Prison](#get-prison)

## Prison Documentation

[Prison Documents - Table of Contents](https://prisonteam.github.io/Prison/prison_docs_000_toc.html)

The Prison documentation covers the important details on how to setup a usable Prison server.
These documents are in a state of development, so more will be added.

## What is Prison?

Prison is a Minecraft plugin that adds all the features necessary for prison servers. It runs on the popular [Spigot](http://spigotmc.org). Plugins for prison servers exist already, but many of them are poorly maintained, and the solid ones are paid resources. Prison eliminates the paywall and the updating hassle, and puts a one-stop solution in their place.

### History of Prison

Prison began as a plugin with the goal of combining two main prison server features: resetting mines and purchasable ranks. In June 2014, Prison v2.0 was released with the goal of providing a single solution for prison servers. Prison v2.0 has had numerous problems and is not the solution that we had envisioned for an all-in-one package to be. The solution is the current version - Prison v3.0.

Starting with prison v3.2.0 new development efforts were began with a focus on adding new features
and improving the performance and stability.

## Why should I use Prison?

If hassle-free updates and zero payments is not enough for you to consider switching to Prison, it doesn't stop there.

* **Prison is modular.** If you don't want to use a certain feature on your server, you can disable it in just a few keystrokes.
* **Prison is completely, 100% open source.** This means that your developers can make changes to the plugin to customize your server's experience, if you so desire.
* **Prison is and always will be free.** All of our code, resources, and binaries are provided free of charge to the Minecraft community. We do not accept donations.
* **New releases are heavily bug-tested.** We test every single aspect of every new release of Prison before releasing it to the community. If a release doesn't pass our rigorous update checklist, it is iterated upon until it does. What does this mean for you? Solid updates with minimal bugs for your server.
* **Prison is always expanding.** We are always looking for ways to expand Prison with features that make your server run more smoothly. Additionally, we are always looking for contributions from the community, be it code contributions, feature requests, bug reports, translations, or documentation.


## Get Prison


| SpigotMC.org | Polymart.org | Bukkit.org | Experimental |
| ------------ | ------------ | ---------- | ------------ |
|    Stable    |    Stable    |   Stable   |    *Nightly*  |
| [![Stable Download](https://img.shields.io/badge/download-stable-44cc11.svg)](https://www.spigotmc.org/resources/prison.1223/) | [![Stable Download](https://img.shields.io/badge/download-stable-44cc11.svg)](https://polymart.org/resource/prison-1-8-x-1-16-5.678/updates) | [![Stable Download](https://img.shields.io/badge/download-stable-44cc11.svg)](https://dev.bukkit.org/projects/mc-prison-v3) | [![Experimental Download](https://img.shields.io/badge/download-experimental-red.svg)](https://ci.appveyor.com/project/faizaand/prison/build/artifacts) | 




## Gradle Configurations

Prison is now able through the jitpack.io repository:

[![Release](https://jitpack.io/v/PrisonTeam/Prison.svg)](https://jitpack.io/#PrisonTeam/Prison)


Within your `repositories` section, add jitpack.io as the last entry, as suggested by jitpack.io.


```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

Then add the dependency as follows, but replace `Tag` with a valid tag such as `3.2.7`.  See the link to jitpack.io's website for a list of Tags that you can use; select from the tabs Releases, Builds, Branches, and Commits.

```
dependencies {
	implementation 'com.github.PrisonTeam:Prison:Tag'
}
```

Through jitpack.io's webpage you can select a Prison release and it will generate the correct dependency to use: [Prison repo on jitpack.io](https://jitpack.io/#PrisonTeam/Prison)



<hr style="height:3px; border:none; color:#aaf; background-color:#aaf;">

