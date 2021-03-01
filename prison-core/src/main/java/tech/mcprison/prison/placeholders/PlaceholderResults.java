package tech.mcprison.prison.placeholders;

public class PlaceholderResults
{
	private PlaceHolderKey placeholder;
	private String escapeLeft;
	private String esccapeRight;
	
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
