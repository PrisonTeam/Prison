package tech.mcprison.prison.placeholders;

public class PlaceholderProgressBarConfig {

	private int segments = 20;
	
	private String positiveColor;
	private String positiveSegment;
	
	private String negativeColor;
	private String negativeSegment;
	
	public PlaceholderProgressBarConfig() {
		super();
		
		
	}
	
	public PlaceholderProgressBarConfig( int segments, 
						String positiveColor, String positiveSegment,
						String negativeColor, String negativeSegment ) {
		super();
		
		this.segments = segments;
		
		this.positiveColor = positiveColor;
		this.positiveSegment = positiveSegment;
		
		this.negativeColor = negativeColor;
		this.negativeSegment = negativeSegment;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append( "bar config: segments: " ).append( getSegments() )
		
			.append(" &7Pos: \\Q" ).append( getPositiveColor() )
			.append( "\\E [" ).append( getPositiveColor() )
			.append( getPositiveSegment()).append( "&7]" )
			
			.append(" Neg: \\Q" ).append( getNegativeColor() )
			.append( "\\E [" ).append( getNegativeColor() )
			.append( getNegativeSegment()).append( "&7]" )
			;
		
		return sb.toString();
	}
	
	public int getSegments(){
		return segments;
	}
	public void setSegments( int segments ) {
		this.segments = segments;
	}

	public String getPositiveColor() {
		return positiveColor;
	}
	public void setPositiveColor( String positiveColor ) {
		this.positiveColor = positiveColor;
	}

	public String getPositiveSegment() {
		return positiveSegment;
	}
	public void setPositiveSegment( String positiveSegment ) {
		this.positiveSegment = positiveSegment;
	}

	public String getNegativeColor() {
		return negativeColor;
	}
	public void setNegativeColor( String negativeColor ) {
		this.negativeColor = negativeColor;
	}

	public String getNegativeSegment() {
		return negativeSegment;
	}
	public void setNegativeSegment( String negativeSegment ) {
		this.negativeSegment = negativeSegment;
	}


	
}
