package tech.mcprison.prison.placeholders;

import tech.mcprison.prison.output.Output;

public class PlaceholderAttributeBar
		implements PlaceholderAttribute {
	
	private String[] parts;
	
	private PlaceholderProgressBarConfig barConfig;
	
	private boolean debug = false;
	
	public PlaceholderAttributeBar( String[] parts, PlaceholderProgressBarConfig defaultBarConfig ) {
		super();

		this.parts = parts;

		// ::bar:size:posColor:posSeg:negColor:negSeg:debug
		
		int len = 1;
		
		// format:
		String segStr = parts.length > len ? parts[len++] : null;
		int seg = defaultBarConfig.getSegments();
		if ( segStr != null && !segStr.trim().isEmpty() ) {
			
			try {
				seg = Integer.parseInt( segStr );
			}
			catch (NumberFormatException e ) {
				// invalid supplied segments. 
				// Ignore unless in debug mode:
				if ( isDebug() ) {
					Output.get().logError( 
							String.format( "Error parsing segments to an Integer. String value= [%s] " +
									"ERRROR: %s", 
									
									segStr, e.getMessage()
									));
				}

			}
		}
		

		String pCol = parts.length > len ? parts[len++] : defaultBarConfig.getPositiveColor();
		
		String pSeg = parts.length > len ? parts[len++] : defaultBarConfig.getPositiveSegment();
		
		String nCol = parts.length > len ? parts[len++] : defaultBarConfig.getNegativeColor();

		String nSeg = parts.length > len ? parts[len++] : defaultBarConfig.getNegativeSegment();

		this.barConfig = new PlaceholderProgressBarConfig( seg, pCol, pSeg, nCol, nSeg );
		
		// Debug:
		String debugStr = parts.length > len ? parts[len++] : null;
		boolean debug = debugStr != null && "debug".equalsIgnoreCase( debugStr );
		
		
		this.debug = debug;

		
		if ( isDebug() ) {
			
			// Reconstruct the attribute from the parts:
			String rawBar = PlaceholderManager.PRISON_PLACEHOLDER_ATTRIBUTE_SEPARATOR + 
					String.join( PlaceholderManager.PRISON_PLACEHOLDER_ATTRIBUTE_FIELD_SEPARATOR, 
							parts );

			Output.get().logInfo( 
					String.format( "Placeholder Attribute bar: " +
							"&7%s  &7Raw: [\\Q%s\\E&7] " +
							"(remove :debug from placeholder to disable this message)", 
							
							getBarConfig().toString(), rawBar
							));
		}


	}


	public String[] getParts() {
		return parts;
	}
	public void setParts( String[] parts ) {
		this.parts = parts;
	}

	public boolean isDebug() {
		return debug;
	}
	public void setDebug( boolean debug ) {
		this.debug = debug;
	}

	public PlaceholderProgressBarConfig getBarConfig() {
		return barConfig;
	}
	public void setBarConfig( PlaceholderProgressBarConfig barConfig ) {
		this.barConfig = barConfig;
	}

	@Override
	public String format( String value )
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
