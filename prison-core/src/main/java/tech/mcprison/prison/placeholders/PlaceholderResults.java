package tech.mcprison.prison.placeholders;

public class PlaceholderResults
{
	private PlaceHolderKey placeholder;
	private String escapeLeft;
	private String esccapeRight;
	
	private String numericSequencePattern;
	private int numericSequence = -1;
	
	private String identifier;
	

	
	public PlaceholderResults( PlaceHolderKey placeholder ) {
		super();
		
		this.placeholder = placeholder;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		if ( getPlaceholder() != null ) {
			sb.append( getPlaceholder().getPlaceholder().name() )
				.append( " " ).append( getEscapeLeft() ).append( getEsccapeRight() )
				.append( " " ).append( getIdentifier() == null ? "" : getIdentifier() );
			
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
