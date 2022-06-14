package tech.mcprison.prison.placeholders;


/**
 * <p>This class is obsolete and no longer used. Please see PlaceholderIdentifier for its 
 * replacement.  This class will be deleted shortly.
 * </p>
 * 
 * <p>This class is used to store the data of mapping an identifier to the correct
 * PlaceHolderKey.
 * </p>
 *
 */
@Deprecated
public class PlaceholderResults
{
	private String identifier;

	private PlaceHolderKey placeholder;
	private String escapeLeft;
	private String esccapeRight;
	
	private String numericSequencePattern;
	private int numericSequence = -1;
	
	
	
	private String text;
	
//	public PlaceholderResults( PlaceHolderKey placeholder ) {
//		super();
//		
//		this.placeholder = placeholder;
//		this.text = null;
//	}
	public PlaceholderResults( PlaceHolderKey placeholder, String text ) {
		super();
		
		this.placeholder = placeholder;
		
		
		this.text = text;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		if ( getPlaceholder() != null ) {
			sb.append( getPlaceholder().getPlaceholder().name() )
				.append( " " )
				.append( getEscapeLeft() == null ? "" : getEscapeLeft() )
				.append( getEsccapeRight() == null ? "" : getEsccapeRight() )
				.append( " " )
				.append( getIdentifier() == null ? "" : getIdentifier() );
			
		}
		else {
			sb.append( "-- no match --" );
		}
		
		return sb.toString();
	}

	public boolean hasResults() {
		return getIdentifier() != null && getPlaceholder() != null;
	}
	
	public void setIdentifier( String identifier, String escapeLeft, String esccapeRight ) {
		setIdentifier( identifier );
		
		setEscapeLeft( escapeLeft );
		setEsccapeRight( esccapeRight );
	}
	
	
	public String getEscapedIdentifier() {
		return getEscapeLeft() + getIdentifier() + getEsccapeRight();
	}
	
	
	public PlaceHolderKey getPlaceholder() {
		return placeholder;
	}
	public void setPlaceholder( PlaceHolderKey placeholder ) {
		this.placeholder = placeholder;
	}

	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier( String identifier ) {
		this.identifier = identifier;
	}

	public String getNumericSequencePattern() {
		return numericSequencePattern;
	}
	public void setNumericSequencePattern( String numericSequencePattern ) {
		this.numericSequencePattern = numericSequencePattern;
	}

	public int getNumericSequence() {
		return numericSequence;
	}
	public void setNumericSequence( int numericSequence ) {
		this.numericSequence = numericSequence;
	}

	public String getEscapeLeft() {
		return escapeLeft;
	}
	public void setEscapeLeft( String escapeLeft ) {
		this.escapeLeft = escapeLeft;
	}

	public String getEsccapeRight() {
		return esccapeRight;
	} 
	public void setEsccapeRight( String esccapeRight ) {
		this.esccapeRight = esccapeRight;
	}

}
