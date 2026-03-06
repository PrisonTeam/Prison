package tech.mcprison.prison.discord;

import tech.mcprison.prison.discord.PrisonSupportFileLinkage.PrimaryLinkages;
import tech.mcprison.prison.discord.PrisonSupportFileLinkage.SecondaryLinkages;
/**
 * <p>This may be a work in progress to provide hyperlinked help docs
 * that are submitted when admins are needing help with their systems.
 * </p>
 * 
 */
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

	protected String getRawLine() {
		return rawLine;
	}
	protected void setRawLine(String rawLine) {
		this.rawLine = rawLine;
	}

	protected PrimaryLinkages getPrimary() {
		return primary;
	}
	protected void setPrimary(PrimaryLinkages primary) {
		this.primary = primary;
	}

	protected SecondaryLinkages getSecondary() {
		return secondary;
	}
	protected void setSecondary(SecondaryLinkages secondary) {
		this.secondary = secondary;
	}

	protected String getSecondaryText() {
		return secondaryText;
	}
	protected void setSecondaryText(String secondaryText) {
		this.secondaryText = secondaryText;
	}

	protected PrisonSupportLinkageData getOtherLinkage() {
		return otherLinkage;
	}
	protected void setOtherLinkage(PrisonSupportLinkageData otherLinkage) {
		this.otherLinkage = otherLinkage;
	}
	
}
