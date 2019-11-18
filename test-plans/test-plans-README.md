**Test-Plans Plan**

* **Module prison-mine:** [test-plans-prison-mine.md](test-plans-prison-mine.md)

Currently I find myself with a big unknown sitting on my lap.  I want to make a good impact on this project, but
at this time I'm on my own since everyone who used to be involved are off exploring new universes and having 
a great time.  I think that is awesome.  But, the fact remains if I'm going to touch code within this project, 
I also have to be responsible and ensure that I don't royally screw everything up.

Enter the Test-Plans!

They will not be a solve-all solution to help ensure that bugs never escape and make it to your worlds, or
ruin an awesome few hours of online play, but it will help to ensure that critical marks are hit to help 
ensure nothing obvious broke.

The general task of testing is an art and science in it's own right, and I will not dwell upon the delights
that professionals savor within this realm.  My point here, is, I'm not a professional Q/A artist so I may 
miss something obvious or not even consider something critical.  But this is a plan, and like all plans, 
this can be adjusted to better suite the evolution of our knowledge.


Here is the scope of the mother of all intimidations: the number of versions supported in this 
plugin!  The following are the test servers that I built, hitting only the latest release of each 
major minecraft version.  There are too many possible combinations of releases that have been
published to include as an actual test severe.  My 
direction is to test on the latest update of a release.  For example, we support minecraft 
version 1.8, but I'm only going to test it on 1.8.8 since that would help to ensure as 
many bugs within the Spigot api are fixed as possible.  That way this project is less about 
supporting the behavior of known bugs, and more on providing
fun features to help make our servers awesome!  Right? :)  I hope so.
* **Minecraft 1.8.8**
* **Minecraft 1.9.4**
* **Minecraft 1.10.2**
* **Minecraft 1.11**
* **Minecraft 1.12.2**
* **Minecraft 1.13.2**
* **Minecraft 1.14.2** - Failures. Cannot support right now.

It really should be noted that this is a lot of ground to cover.  If you are running a server that is using
a version other than one of these and you hit a problem, I could spin up and test on that.  Honestly,
I'd rather not, but it would depend upon the errors you're encountering.  

One thing I am wanting to do, is to pull in usage stats on the old v3.1.1 plugin and see how many are on each
of these platforms.  I suspect basically no one will be on 1.11, as an example, so I may run through the test
plans once for that, and then consider it good enough until I hear otherwise.  For major rewrite and refactorings,
yes, it will get tested under those conditions, but maybe last and might skip over the basics and simple things 
that probably will not fail.

**These Test-Plans**

These test plans will cover the different modules and how to test them online, as a player or as an administrator.

We will just have to see how these turn out.

