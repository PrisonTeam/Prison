package tech.mcprison.prison.spigot.spiget;

import org.inventivetalent.update.spiget.comparator.VersionComparator;

/**
 * <p>This class provides a <i>real</i> comparator for semantic versioning 
 * for spiget.  This addresses issues seen with incorrect notifications.
 * Extensive unit tests back the correct functionality of this code base.
 * </p>
 * 
 * <p>Spiget's versions have totally failed on so many levels that they
 * should not imply it has anything to do with semVer at all.
 * The inherent problems with the solutions provided by spiget, is that they may work 
 * some of the time, but then fail once certain incorrect assumptions about semVers 
 * are realized. Intermittent failures are to be expected.  For example, if someone
 * is using version 2.3.11 and 2.4.0 is released, spiget's SEM_VER
 * comparator will think the older version is newer than the current release.
 * Why does that fail? Because they just remove the periods and take the remainder
 * characters and then parse them all as one integer, then they compare integers.  The 
 * context of major, minor, and patch is completely lost, plus it does not factor
 * in prerelease tagging as semVer supports. 
 * </p>
 * 
 * <p>To provide a product that actually works with real semVers, the website
 * <a href="https://semver.org/">https://semver.org/</a> was used as the
 * "standard" to base this functionality upon, and to which our tests are 
 * measured against. 
 * </p>
 * 
 * <p>The result of the new architecture is a solution that ends up being very 
 * simple for this class.
 * </p>
 * 
 * @author RoyalBlueRanger 2020-01-28
 * @return
 */
public class BluesSpigetSemVerComparator
		extends VersionComparator {
	
	@Override
	public boolean isNewer(String currentVersion, String checkVersion) {
		return performComparisons( currentVersion, checkVersion);
	}
	
	/**
	 * <p>This function will take two String values and convert both to a 
	 * SemanticVersioningData objects, that in turn, will parse the string 
	 * and encapsulate the full representation as an object.  Since this object 
	 * implements comparable, its then as simple as comparing the new semVer to the
	 * current semVer to find out if it is actually newer.
	 * </p>
	 * 
	 * <p>The semantic versions must be valid.  At a minimum they have to 
	 * have a format such as 1.0.0.  If one is invalid, then compareTo will
	 * favor the valid semVer.  If both are invalid then compareTo will return 
	 * a negative -1000, which will equate to false result.  
	 * </p>
	 * 
	 * @param currentVersion A String value representing the current semVer
	 * @param checkVersion A String value representing the checked semVer
	 * @return True if the checkVersion is a higher semVer than the current version
	 */
	public boolean performComparisons( String currentVersion, String checkVersion ) {
		
		BluesSemanticVersionData currentSemVer = new BluesSemanticVersionData(currentVersion);
		BluesSemanticVersionData checkSemVer = new BluesSemanticVersionData(checkVersion);

		return (checkSemVer.compareTo( currentSemVer ) > 0);
	}
		
}
