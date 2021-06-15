package tech.mcprison.prison.localization;

import java.io.File;

public class LocalManagerPropertyFileData
{
	
	private File localPropFile;
	
	private String localVersion;
	private boolean localHasAutoReplace;
	private boolean localAutoReplace;
	
	private String jarVersion;
	private boolean jarHasAutoReplace;
	private boolean jarAutoReplace;
	
	public LocalManagerPropertyFileData( File localPropFile ) {
		super();
		
		this.localPropFile = localPropFile;
	}
	
	
	public boolean replaceLocalWithJar() {
		boolean results = false;
		
		boolean localReplaceable = !isLocalHasAutoReplace() || 
						isLocalHasAutoReplace() && isLocalAutoReplace();
		
		boolean jarReplaceable = isJarHasAutoReplace() && isJarAutoReplace();
		
		if ( localReplaceable && jarReplaceable &&
				getLocalVersion() != null && getJarVersion() != null ) {
			
			int localVersionNum = Integer.parseInt( getLocalVersion() );
			int jarVersionNum = Integer.parseInt( getJarVersion() );
			
			if ( localVersionNum < jarVersionNum ) {
				results = true;
			}
		}

		return results;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		String path = getLocalPropFile().getAbsolutePath();
		int idx = path.indexOf( "module_conf" );
		path = path.substring( idx == -1 ? 0 : idx );
		
		sb.append( "localPropFile: " )
				.append( path )
			.append( "   Local Ver: " ).append( getLocalVersion() )
			.append( "  hasAutoReplace: " ).append( isLocalHasAutoReplace() )
			.append( "  autoReplace: " ).append( isLocalAutoReplace() )
			.append( "   Jar Ver: " ).append( getJarVersion() )
			.append( "  hasAutoReplace: " ).append( isJarHasAutoReplace() )
			.append( "  autoReplace: " ).append( isJarAutoReplace() );
		
		return sb.toString();
	}
	
	public String getLocalPropertiesName() {
		return localPropFile == null ? null : localPropFile.getName();
	}

	public File getLocalPropFile() {
		return localPropFile;
	}
	public void setLocalPropFile( File localPropFile ) {
		this.localPropFile = localPropFile;
	}

	public String getLocalVersion() {
		return localVersion;
	}
	public void setLocalVersion( String localVersion ) {
		this.localVersion = localVersion;
	}

	public boolean isLocalHasAutoReplace() {
		return localHasAutoReplace;
	}
	public void setLocalHasAutoReplace( boolean localHasAutoReplace ) {
		this.localHasAutoReplace = localHasAutoReplace;
	}

	public boolean isLocalAutoReplace() {
		return localAutoReplace;
	}
	public void setLocalAutoReplace( boolean localAutoReplace ) {
		this.localAutoReplace = localAutoReplace;
	}

	public String getJarVersion() {
		return jarVersion;
	}
	public void setJarVersion( String jarVersion ) {
		this.jarVersion = jarVersion;
	}

	public boolean isJarHasAutoReplace() {
		return jarHasAutoReplace;
	}
	public void setJarHasAutoReplace( boolean jarHasAutoReplace ) {
		this.jarHasAutoReplace = jarHasAutoReplace;
	}

	public boolean isJarAutoReplace() {
		return jarAutoReplace;
	}
	public void setJarAutoReplace( boolean jarAutoReplace ) {
		this.jarAutoReplace = jarAutoReplace;
	}

}
