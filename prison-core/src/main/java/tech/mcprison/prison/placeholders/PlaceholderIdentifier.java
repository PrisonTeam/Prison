package tech.mcprison.prison.placeholders;

import java.util.ArrayList;
import java.util.UUID;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.Player;


public class PlaceholderIdentifier {
	
	/**
	 * <p>The identifierRaw is the original placeholder text that is sent to prison, 
	 * which needs to be decoded.  It also contains one or more placeholder attributes, 
	 * if they are provided.
	 * </p>
	 */
	private String identifierRaw;
	
	
	/**
	 * <p>This identifier contains the raw identifier text, minus any placeholder attributes.
	 * This also can serve as a unique key for the placeholder.  In most conditions, it should
	 * be able to match the placeholder enum.
	 * </p>
	 * 
	 */
	private String identifier;
	
	
	private ArrayList<PlaceholderAttribute> attributes;
	
	
	private boolean hasSequence;
	private int sequence;
	private String sequencePattern;
	
	private String escapeLeft;
	private String escapeRight;

	private Player player;
	
	private boolean foundAMatch;
	private boolean missingPrisonPrefix;
	
	private PlaceHolderKey placeholderKey;
	private String text;
	
	
	public PlaceholderIdentifier( String identifier ) {
		super();
		
		this.identifierRaw = identifier;
		this.identifier = "";
		
		this.attributes = new ArrayList<>();
		
		this.hasSequence = false;
		this.sequence = -1;
		this.sequencePattern = null;
		
		
		this.escapeLeft = null;
		this.escapeRight = null;
		
		this.player = null;
		
		this.foundAMatch = false;
		this.missingPrisonPrefix = false;
		
		this.placeholderKey = null;
		this.text = null;
		
		intialize();
		
	}
	
	private void intialize() {
		
		if ( identifierRaw == null ) {

			this.identifierRaw = "";
		}
		else {
			
			this.identifierRaw = identifierRaw.trim();
		}
		
		// If the placeholderRaw has escape characters, strip the off and record them:
		PlaceholderManagerUtils.getInstance().convertPlaceholderEscapeCharacters( this );
		
		
		if ( identifierRaw.length() > 0 ) {

			this.identifier = identifierRaw.toLowerCase();
			
			// Extract the placeholder value from the identifierRaw... prefix of "prison_" is not used:
			this.identifier = PlaceholderManagerUtils.getInstance().extractPlaceholder( identifierRaw );
			
			// Some plugins that handle the placeholders omit the prefix.  If that's the case, then
			// add it back.
			if ( !identifier.startsWith( PlaceholderManager.PRISON_PLACEHOLDER_PREFIX_EXTENDED ) ) {
				
				this.missingPrisonPrefix = true;
				
				// Do not modify the identifier... will adjust the identifier when comparing to the
				// PlaceholderKey object:
//				identifier = PlaceholderManager.PRISON_PLACEHOLDER_PREFIX_EXTENDED + identifier;
			}
			
			
			// Extract the placeholder attributes from the identifierRaw:
			this.attributes = PlaceholderManagerUtils.getInstance().extractPlaceholderAttributes( identifierRaw );
			
			
			// Sets the hasSequence, sequence, and sequencePattern:
			PlaceholderManagerUtils.getInstance().convertPlaceholderSequence( this );

			
			// Not sure at this point if the results "text" needs to be set... probably shouldn't since
			// if there isn't a match, then since if it is a valid placeholder, then it should be blank:
//			this.text = this.identifier;
		}
		
	}
	
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		for (PlaceholderAttribute attr : getAttributes() ) {
			sb.append( "[" ).append( attr.toString() ).append( "]" );
		}
		
		return getOriginalIdentifier() +
				( getText() != null ? " \"" + getText() + "\"" : "" ) +
				( getPlayer() != null ? " " + getPlayer().getName() : "" ) +
				( hasSequence() ? " Seq: " + getSequence() : "" ) +
				( sb.length() > 0 ? " " + sb.toString() : "");
	}
	
	
	public String getOriginalIdentifier() {
		
		return (getEscapeLeft() == null ? "" : getEscapeLeft()) + 
				getIdentifierRaw() + 
				(getEscapeRight() == null ? "" : getEscapeRight());
	}
	
	public boolean checkPlaceholderKey(PlaceHolderKey placeHolderKey) {
		boolean results = false;
		
		String key = placeHolderKey.getKey().toLowerCase();
		
		// If the placeholder was missing the Prison prefix ( prison_ ) then add it before
		// checking to see if there is a match:
		String adjustedIdentifier = (isMissingPrisonPrefix() ?  
					PlaceholderManager.PRISON_PLACEHOLDER_PREFIX_EXTENDED : "") +
						getIdentifier();
		
		if ( adjustedIdentifier.equalsIgnoreCase( key ) ) {
			
			setPlaceholderKey( placeHolderKey );
			results = true;
		}
		
		return results;
	}
	
	/**
	 * <p>Set the player's object, based upon the player's name, or based upon the 
	 * placeholder attribute if a player attribute is provided.
	 * </p>
	 * 
	 * @param playerUuid
	 * @param playerName
	 */
	public void setPlayer( UUID playerUuid, String playerName ) {
		Player player = null;
		
		if ( playerUuid != null ) {
			
			player = Prison.get().getPlatform().getPlayer( playerUuid ).orElse( null );
		}
		
		if ( player == null && playerName != null ) {
			
			player = Prison.get().getPlatform().getPlayer( playerName ).orElse( null );
		}
		
		if ( player == null && playerUuid != null ) {
			
			player = Prison.get().getPlatform().getOfflinePlayer( playerUuid ).orElse( null );
		}
		
		if ( player == null && playerName != null ) {
			
			player = Prison.get().getPlatform().getOfflinePlayer( playerName ).orElse( null );
		}
		
		
		if ( player != null ) {
			setPlayer(player);
		}
		else {
			// Check placeholder attributes for a defined player's namme:
			String pName = getPlayerNameFromPlaceholderAttributes();
			
			if ( pName != null ) {
				
				setPlayer( null, pName );
			}
		}
	}
	
	public String getPlayerNameFromPlaceholderAttributes() {
		String player = null;
		
		for (PlaceholderAttribute phAttr : getAttributes() ) {
			if ( phAttr.getPlayer() != null ) {
				player = phAttr.getPlayer();
				break;
			}
		}
		
		return player;
	}
	
	public PlaceholderAttributeBar getAttributeBar() {
		PlaceholderAttributeBar phAttribute = null;
		
		for (PlaceholderAttribute placeholderAttribute : getAttributes() ) {
			if ( placeholderAttribute instanceof PlaceholderAttributeBar ) {
				phAttribute = (PlaceholderAttributeBar) placeholderAttribute;
				break;
			}
		}
		
		return phAttribute;
	}

	public PlaceholderAttributeNumberFormat getAttributeNFormat() {
		PlaceholderAttributeNumberFormat phAttribute = null;
		
		for (PlaceholderAttribute placeholderAttribute : getAttributes() ) {
			if ( placeholderAttribute instanceof PlaceholderAttributeNumberFormat ) {
				phAttribute = (PlaceholderAttributeNumberFormat) placeholderAttribute;
				break;
			}
		}
		
		return phAttribute;
	}
	
	public PlaceholderAttributeText getAttributeText() {
		PlaceholderAttributeText phAttribute = null;
		
		for (PlaceholderAttribute placeholderAttribute : getAttributes() ) {
			if ( placeholderAttribute instanceof PlaceholderAttributeText ) {
				phAttribute = (PlaceholderAttributeText) placeholderAttribute;
				break;
			}
		}
		
		return phAttribute;
	}
	
	public String getIdentifierRaw() {
		return identifierRaw;
	}
	public void setIdentifierRaw(String identifierRaw) {
		this.identifierRaw = identifierRaw;
	}

	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public ArrayList<PlaceholderAttribute> getAttributes() {
		return attributes;
	}
	public void setAttributes(ArrayList<PlaceholderAttribute> attributes) {
		this.attributes = attributes;
	}

	public boolean hasSequence() {
		return hasSequence;
	}
	public void setHasSequence(boolean hasSequence) {
		this.hasSequence = hasSequence;
	}

	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public String getSequencePattern() {
		return sequencePattern;
	}
	public void setSequencePattern(String sequencePattern) {
		this.sequencePattern = sequencePattern;
	}

	public String getEscapeLeft() {
		return escapeLeft;
	}
	public void setEscapeLeft(String escapeLeft) {
		this.escapeLeft = escapeLeft;
	}

	public String getEscapeRight() {
		return escapeRight;
	}
	public void setEscapeRight(String escapeRight) {
		this.escapeRight = escapeRight;
	}

	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}

	public boolean isFoundAMatch() {
		return foundAMatch;
	}
	public void setFoundAMatch(boolean foundAMatch) {
		this.foundAMatch = foundAMatch;
	}

	public boolean isMissingPrisonPrefix() {
		return missingPrisonPrefix;
	}
	public void setMissingPrisonPrefix(boolean missingPrisonPrefix) {
		this.missingPrisonPrefix = missingPrisonPrefix;
	}

	public PlaceHolderKey getPlaceholderKey() {
		return placeholderKey;
	}
	public void setPlaceholderKey(PlaceHolderKey placeholderKey) {
		this.placeholderKey = placeholderKey;
	}

	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}


}
