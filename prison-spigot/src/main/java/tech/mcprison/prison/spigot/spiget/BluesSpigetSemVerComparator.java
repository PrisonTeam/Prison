package tech.mcprison.prison.spigot.spiget;

import org.bukkit.Bukkit;
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
		
	/**
	 * <p>Example how to use:
	 * </p>
	 * 
	 * <pre>
	 * String ver = Bukkit.getVersion().trim();
	 * ver = ver.substring( ver.indexOf("(MC: ") + 5, ver.length() -1 );
	 * if ( new BluesSpigetSemVerComparator().compareTo(ver, "1.9.0") ) {
	 *     // if mc version is less than 1.9.0
	 * }
	 * </pre>
	 * 
	 * @param currentVersion
	 * @param checkVersion
	 * @return
	 */
	public int compareTo( String currentVersion, String checkVersion ) {
		
		BluesSemanticVersionData currentSemVer = new BluesSemanticVersionData(currentVersion);
		BluesSemanticVersionData checkSemVer = new BluesSemanticVersionData(checkVersion);

		return currentSemVer.compareTo( checkSemVer );
	}

	/**
	 * <p>This uses the minecraft version of the server to compare to the provided version.
	 * </p>
	 * 
	 * <p>Samples of what a bukkit, spigot, and paper version would look like. Notice
	 * they all have the version at the end between <b>(MC:</b> and <b>)</b>.
	 * </p>
	 * 
	 * <ul>
	 *     <li>Spigot 1.8.8: git-Spigot-21fe707-e1ebe52 (MC: 1.8.8)</li>
	 *     <li>Spigot 1.10.2: git-Spigot-de459a2-51263e9 (MC: 1.10.2)</li>
	 *     <li>Spigot 1.12.2: git-Spigot-79a30d7-acbc348 (MC: 1.12.2)</li>
	 *     <li>Spigot 1.15.2: git-Spigot-2040c4c-893ad93 (MC: 1.15.2)</li>
	 *     <li>Paper 1.10.2: git-Paper-916.2 (MC: 1.10.2)</li>
	 *     <li>Paper 1.14.2: git-Paper-234 (MC: 1.14.4)</li>
	 * </ul>
	 * 
	 * 	 * <p>Example how to use:
	 * </p>
	 * 
	 * <pre>
	 * if ( new BluesSpigetSemVerComparator().compareMCVersionTo("1.9.0") < 0 ) {
	 *     // if mc version is less than 1.9.0
	 * }
	 * </pre>
	 * 
	 * @param checkVersion
	 * @return
	 */
	public int compareMCVersionTo( String checkVersion ) {
		int results = -1;
		String currentVersion = Bukkit.getVersion().trim().toLowerCase();
		int i = currentVersion.indexOf("(mc:");
		int len = currentVersion.length();
		if ( i >= 0 && (i+4 < len)) {
			currentVersion = currentVersion.substring( i + 4, len -1 ).trim();
			
			results = compareTo( currentVersion, checkVersion );
		}
		return results;
	}

}
