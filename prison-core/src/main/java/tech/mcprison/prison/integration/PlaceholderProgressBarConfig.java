package tech.mcprison.prison.integration;

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
