package tech.mcprison.prison.spigot.spiget;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** 
 * <p>This class provides a <i>real</i> comparator for semantic versioning 
 * for spiget.  This addresses issues seen with incorrect notifications.
 * Extensive unit tests back the correct functionality of this code base.
 * </p>
 * 
 * <p>To provide a product that actually works with real semVers, the web site
 * <a href="https://semver.org/">https://semver.org/</a> was used as the
 * "standard" to base functionality upon, and to which our tests should be 
 * measured against.
 * </p>
 * 
 * <p>The architecture of a solution is based upon leveraging preexisting industry
 * solutions to solve this problem, of which happens to be named regular expressions.
 * Through the use of these standards, as provided by semver.org, they take the 
 * guess work out of re-engineering a solution, and more importantly, they eliminate
 * the possible bugs that may result from incorrect interpretation of those given
 * standards.
 * </p>
 * 
 * <p>The two regular expressions in the code below (one is commented out) come 
 * from the following web site:
 * <a href="https://semver.org/">https://semver.org/</a>.  
 * Another website that is
 * useful in understanding the named pattern regular expression is:  
 * <a href="https://regex101.com/r/Ly7O1x/3/">https://regex101.com/r/Ly7O1x/3/</a>
 * </p>
 * 
 * @author RoyalBlueRanger 2020-01-28
 */
public class BluesSemanticVersionData 
	implements Comparable<BluesSemanticVersionData> 
{
	private final String patternNamed = 
			"^(?<major>0|[1-9]\\d*)\\." + // major
			"(?<minor>0|[1-9]\\d*)\\." +  // minor
			"(?<patch>0|[1-9]\\d*)" +     // patch
			"(?:-(?<prerelease>(?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*)" +
				"(?:\\.(?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*))*))?" + // prerelease
			"(?:\\+(?<buildmetadata>[0-9a-zA-Z-]+(?:\\.[0-9a-zA-Z-]+)*))?$" // buildmetadata
		;

//	private final String pattern = 
//				"^(0|[1-9]\\d*)\\." +  // major
//				"(0|[1-9]\\d*)\\." +   // minor
//				"(0|[1-9]\\d*)" +      // patch
//				"(?:-((?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*)" +
//					"(?:\\.(?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*))*))" + // prerelease
//				"?(?:\\+([0-9a-zA-Z-]+(?:\\.[0-9a-zA-Z-]+)*))?$";  // buildmetadata
	
	private String original;
	private boolean valid = false;
	
	private Integer major = null;
	private Integer minor = null;
	private Integer patch = null;
	private String prerelease = null;
	private String buildmetadata = null;
	
	private List<String> messages;
	
	public enum GroupNames {
		major,
		minor,
		patch,
		prerelease,
		buildmetadata;
	}
	
	public BluesSemanticVersionData( String semVerStr ) {
		this.original = semVerStr;
		this.messages = new ArrayList<>();
		
		Pattern r = Pattern.compile(patternNamed);
	//	Pattern r = Pattern.compile(pattern);
		Matcher m = ( semVerStr == null ? null : r.matcher(semVerStr) );
		
		this.valid = ( semVerStr == null ? false : m.find() );
		
		if ( isValid() ) {
			this.major = parseInt(m, GroupNames.major);
			this.minor = parseInt(m, GroupNames.minor);
			this.patch = parseInt(m, GroupNames.patch);
			
			this.prerelease = parseString(m, GroupNames.prerelease);
			this.buildmetadata = parseString(m, GroupNames.buildmetadata); 
		}
	}
	
	/**
	 * <p>This toString function generates meaningful representation of the original
	 * semantic version String, but it does not fully try to reproduce it in its 
	 * entirety. There may be important differences, but it should be understood that
	 * the primary purpose of this class is to establish a precedence, not to reproduce
	 * a String value.  That said, this should be able to reproduce it fairly well.
	 * </p>
	 * 
	 * <p>This function also indicates if the parsing of the source was successful or not, 
	 * plus it always includes the original for comparison.  Generally, this function
	 * is seen to be most useful during live debugging.  It's also used in some
	 * unit tests, but that is mostly to help catch slight abnormalities if they
	 * should arise, but should not be depended upon.
	 * </p>
	 * 
	 */
	public String toString() {
		return 
				(getMajor() == null ? "<fail>" : getMajor()) + "." + 
				(getMinor() == null ? "<fail>" : getMinor()) + "." + 
				(getPatch() == null ? "<fail>" : getPatch()) + 
				(getPrerelease() == null ? "" : "-" + getPrerelease() ) +
				(getBuildmetadata() == null ? "" : "+" + getBuildmetadata()) + 
				" (" + (isValid() ? "valid" : "invalid") + ")" +
				" [" + getOriginal() + "]";
	}
	
	/**
	 * <p>Based upon the provided groupName, this function will convert the
	 * extracted text to an Integer value.  If the monitored exception is 
	 * caught, then the error message will be added to the message collection.
	 * <p>
	 * 
	 * @param m
	 * @param groupName
	 * @return an Integer if the value is an integer, otherwise it will be a null
	 */
	private Integer parseInt( Matcher m, GroupNames groupName )
	{
		Integer result = null;
		
		if ( m != null ) {
			String grp = parseString(m, groupName);
			if ( grp != null ) {
				
				try {
					result = Integer.parseInt( grp );
				}
				catch ( NumberFormatException e ) {
					// Should be an integer.... let the null be returned, record in messages:
					getMessages().add( "### BluesSemanticVersionData.parseInt :: group " + groupName.name() + " = " +
							grp + " ### Error Parsing int value ### " );
				}
			}
		}
		
		return result;
	}
	
	/**
	 * <p>Based upon the provided groupName, extract that group from the matcher.
	 * If a monitored exception is caught, then the error message will be added to the 
	 * messages collection. 
	 * </p>
	 * 
	 * @param m
	 * @param groupName
	 * @return The String value of the selected groupName, otherwise a null if there is an error.
	 */
	private String parseString( Matcher m, GroupNames groupName ) {
		String result = null;
		
		if ( m != null ) {
			try {
				result = m.group( groupName.name() ); 
			}
			catch ( IllegalArgumentException e ) {
				getMessages().add( "### BluesSemanticVersionData.parseString :: group " + groupName.name() + " = ?? " +
						" ### Error Capture Group Doesn't Exist in the Regular Expression ### " );
			}
		}
		
		
		return result;
	}
	
	
	/**
	 * <p>The precedence is specified as defined by  
	 * <a href="https://semver.org/#spec-item-11">https://semver.org/#spec-item-11</a>
	 * in their specification
	 * number 11. For sake of Java structures, invalid semantic versions will have the 
	 * lowest order.  It should also be noted that if an object has the same major, minor,
	 * and patch as another object, but yet the object has a prerelease, then it has the
	 * lower order.  If both have prerelease and their major, minor, and patch match, then
	 * the prereleases should be compared as regular string values to determine order.
	 * </p>
	 * 
	 * <p>It should also be noted that buildmetadata values never factor in to the precedence.
	 * </p>
	 * 
	 */
	@Override
	public int compareTo( BluesSemanticVersionData arg0 )
	{
		int result = 0;
		
		if ( !isValid() || !arg0.isValid() ) {
			result = (isValid() ? 1 : arg0.isValid() ? -1 : -1000 );
		} else {
			result = getMajor() - arg0.getMajor();
			
			if ( result == 0 ) {
				result = getMinor() - arg0.getMinor();
				
				if ( result == 0 ) {
					result = getPatch() - arg0.getPatch();
					
					if ( result == 0 ) {
						if ( getPrerelease() == null && arg0.getPrerelease() != null ) {
							result = 1;
						} else if ( getPrerelease() != null && arg0.getPrerelease() == null ) {
							result = -1;
						} else if ( getPrerelease() != null && arg0.getPrerelease() != null ) {
							result = getPrerelease().compareTo( arg0.getPrerelease() );
						}
					}
				}
			}
		}
		
		return result;
	}
	
	
	public String getOriginal() {
		return original;
	}
	public void setOriginal( String original ) {
		this.original = original;
	}
	
	public boolean isValid() {
		return valid;
	}
	public void setValid( boolean valid ) {
		this.valid = valid;
	}
	
	public Integer getMajor() {
		return major;
	}
	public void setMajor( Integer major ) {
		this.major = major;
	}
	
	public Integer getMinor() {
		return minor;
	}
	public void setMinor( Integer minor ) {
		this.minor = minor;
	}
	
	public Integer getPatch() {
		return patch;
	}
	public void setPatch( Integer patch ) {
		this.patch = patch;
	}
	
	public String getPrerelease() {
		return prerelease;
	}
	public void setPrerelease( String prerelease ) {
		this.prerelease = prerelease;
	}
	
	public String getBuildmetadata() {
		return buildmetadata;
	}
	public void setBuildmetadata( String buildmetadata ) {
		this.buildmetadata = buildmetadata;
	}

	public List<String> getMessages() {
		return messages;
	}
	public void setMessages( List<String> messages ) {
		this.messages = messages;
	}
}
