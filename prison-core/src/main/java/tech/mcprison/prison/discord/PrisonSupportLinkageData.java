package tech.mcprison.prison.discord;

import tech.mcprison.prison.discord.PrisonSupportFileLinkage.PrimaryLinkages;
import tech.mcprison.prison.discord.PrisonSupportFileLinkage.SecondaryLinkages;

public class PrisonSupportLinkageData {

	private String rawLine;
	
	private PrimaryLinkages primary;
	
	private SecondaryLinkages secondary;
	
	private String secondaryText;
	
	
	
	private PrisonSupportLinkageData otherLinkage;
	
	
	public PrisonSupportLinkageData( String line ) {
		super();
		
		
		String rawLine = line.replace(PrisonSupportFileLinkage.LINKAGE_IDENTIFIER, "").trim();
		
		this.rawLine = rawLine;
		
		
		PrimaryLinkages primary = PrimaryLinkages.fromString( rawLine );
		this.primary = primary;
		
		if ( primary != PrimaryLinkages.unknown ) {
			
			String seconds = rawLine.replace( primary.name(), "" ).trim();
			
			SecondaryLinkages secondary = SecondaryLinkages.fromString( seconds );
			this.secondary = secondary;
			
			String secondaryText = secondary == null ? "" : seconds.replace( secondary.name(), "" ).trim();
			this.secondaryText = secondaryText;
			
		}
		
		
	}
	
}
