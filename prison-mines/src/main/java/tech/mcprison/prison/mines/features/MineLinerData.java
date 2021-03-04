package tech.mcprison.prison.mines.features;

import tech.mcprison.prison.mines.features.MineLinerBuilder.LinerPatterns;
import tech.mcprison.prison.util.Bounds.Edges;

public class MineLinerData {
	
	private String north;
	private String east;
	private String south;
	private String west;
	
	private String walls;
	
	private String top;
	private String bottom;
	
	private boolean forceNorth;
	private boolean forceEast;
	private boolean forceSouth;
	private boolean forceWest;
	
	private boolean forceWalls;
	
	private boolean forceTop;
	private boolean forceBottom;
	
	public MineLinerData() {
		super();
		
		this.north = null;
		this.east = null;
		this.south = null;
		this.west = null;
		
		this.walls = null;

		this.top = null;
		this.bottom = null;
		
		
		this.forceNorth = false;
		this.forceEast = false;
		this.forceSouth = false;
		this.forceWest = false;
		
		this.forceWalls = false;
		
		this.forceTop = false;
		this.forceBottom = false;
	}

	public MineLinerData( String walls, String bottom ) {
		this();
		
		this.walls = walls;
		this.bottom = bottom;
	}
	
	public MineLinerData( String north, String east, String south, String west, String bottom ) {
		this();
		
		this.north = north;
		this.east = east;
		this.south = south;
		this.west = west;
		
		this.bottom = bottom;
	}

	public String toInfoString() {
		String results = generateStringValue(" &2", " &7");
		
		if ( results.trim().isEmpty() ) {
			results = "&dNone";
		}
		
		return results;
	}
	
	public String toSaveString() {
		return generateStringValue("", "");
	}
	
	private String generateStringValue( String color1, String color2 ) {

		StringBuilder sb = new StringBuilder();

		addSaveString( sb, Edges.north, getNorth(), isForceNorth(), color1, color2 );
		addSaveString( sb, Edges.east, getEast(), isForceEast(), color1, color2 );
		addSaveString( sb, Edges.south, getSouth(), isForceSouth(), color1, color2 );
		addSaveString( sb, Edges.west, getWest(), isForceWest(), color1, color2 );

		addSaveString( sb, Edges.walls, getWalls(), isForceWalls(), color1, color2 );

		addSaveString( sb, Edges.top, getTop(), isForceTop(), color1, color2 );
		addSaveString( sb, Edges.bottom, getBottom(), isForceBottom(), color1, color2 );
		
		return sb.toString();
	}
	
	/**
	 * <p>This is an internal function that builds this object as a String so it can be saved and 
	 * restored.  The format it generates is edge-colon-value and if there are more than one, then
	 * it will insert a comma with no spaces. 
	 * </p>
	 * 
	 * @param sb
	 * @param edge
	 * @param value
	 */
	private void addSaveString( StringBuilder sb, Edges edge, String value, 
				boolean forced,
				String color1, String color2 ) {
		if ( value != null && value.trim().length() > 0 ) {
			if ( sb.length() > 0 ) {
				sb.append( "," );
			}
			
			sb.append( color1 ).append( edge.name() ).append( ":" )
					.append( color2 ).append( value );
			if ( forced ) {
				sb.append( ":" ).append( color2 ).append( "forced" );
			}
		}
	}
	
	public static MineLinerData fromSaveString( String savedLiner ) {
		MineLinerData results = new MineLinerData();
		
		if ( savedLiner != null && savedLiner.trim().length() > 0 ) {
			
			String[] surfaces = savedLiner.split( "," );
			
			for ( String surface : surfaces ) {
				if ( surface != null && surface.indexOf( ':' ) != -1 ) {
					String[] edgePatternName = surface.split( ":" );
					Edges edge = Edges.fromString( edgePatternName[0] );
					String pattern = edgePatternName[1];
					boolean forced = edgePatternName.length > 2 && 
										"forced".equalsIgnoreCase( edgePatternName[2] );

					results.setLiner( edge, pattern, forced );
				}
			}
		}
		
		return results;
	}

	private void setLiner( Edges edge, String pattern, boolean forced ) {
		switch ( edge )
		{
			case north:
				setNorth( pattern );
				setForceNorth( forced );
				break;

			case east:
				setEast( pattern );
				setForceEast( forced );
				break;
				
			case south:
				setSouth( pattern );
				setForceSouth( forced );
				break;
				
			case west:
				setWest( pattern );
				setForceWest( forced );
				break;
				
			case walls:
				setWalls( pattern );
				setForceWalls( forced );
				break;
				
			case top:
				setTop( pattern );
				setForceTop( forced );
				break;
				
			case bottom:
				setBottom( pattern );
				setForceBottom( forced );
				break;
				
			default:
				break;
		}
	}

	

	public void setLiner( Edges edge, LinerPatterns linerPattern, boolean isForced ) {
		
		setLiner( edge, linerPattern.name(), isForced );
	}
	
	public boolean hasEdge( Edges edge ) {
		
		return getEdge( edge ) != null;
	}
	
	public String getEdge( Edges edge ) {
		String results = null;
		
		switch ( edge )
		{
			case north:
				results = getNorth() != null ? getNorth() : getWalls();
				break;

			case east:
				results = getEast() != null ? getEast() : getWalls();
				break;
				
			case south:
				results = getSouth() != null ? getSouth() : getWalls();
				break;
				
			case west:
				results = getWest() != null ? getWest() : getWalls();
				break;
				
			case walls:
				results = getWalls();
				break;
				
			case top:
				results = getTop();
				break;
				
			case bottom:
				results = getBottom();
				break;
				
			default:
				break;
		}
		
		return results;
	}
	
	public boolean getForce( Edges edge ) {
		boolean results = false;
		
		switch ( edge )
		{
			case north:
				results = isForceNorth() || isForceWalls();
				break;

			case east:
				results = isForceEast() || isForceWalls();
				break;
				
			case south:
				results = isForceSouth() || isForceWalls();
				break;
				
			case west:
				results = isForceWest() || isForceWalls();
				break;
				
			case walls:
				results = isForceWalls();
				break;
				
			case top:
				results = isForceTop();
				break;
				
			case bottom:
				results = isForceBottom();
				break;
				
			default:
				break;
		}
		return results;
	}
		
	
	public void removeAll() {
		for ( Edges edge : Edges.values() ) {
			remove( edge );
		}
	}

	public void remove( Edges edge ) {
		
		if ( edge != null ) {
			String pattern = null;
			setLiner( edge, pattern, false );
		}
	}
	
	public String getNorth() {
		return north;
	}
	public void setNorth( String north ) {
		this.north = north;
	}

	public String getEast() {
		return east;
	}
	public void setEast( String east ) {
		this.east = east;
	}

	public String getSouth() {
		return south;
	}
	public void setSouth( String south ) {
		this.south = south;
	}

	public String getWest() {
		return west;
	}
	public void setWest( String west ) {
		this.west = west;
	}

	public String getWalls() {
		return walls;
	}
	public void setWalls( String walls ) {
		this.walls = walls;
	}

	public String getTop() {
		return top;
	}
	public void setTop( String top ) {
		this.top = top;
	}

	public String getBottom() {
		return bottom;
	}
	public void setBottom( String bottom ) {
		this.bottom = bottom;
	}

	public boolean isForceNorth() {
		return forceNorth;
	}
	public void setForceNorth( boolean forceNorth ) {
		this.forceNorth = forceNorth;
	}

	public boolean isForceEast() {
		return forceEast;
	}
	public void setForceEast( boolean forceEast ) {
		this.forceEast = forceEast;
	}

	public boolean isForceSouth() {
		return forceSouth;
	}
	public void setForceSouth( boolean forceSouth ) {
		this.forceSouth = forceSouth;
	}

	public boolean isForceWest() {
		return forceWest;
	}
	public void setForceWest( boolean forceWest ) {
		this.forceWest = forceWest;
	}

	public boolean isForceWalls() {
		return forceWalls;
	}
	public void setForceWalls( boolean forceWalls ) {
		this.forceWalls = forceWalls;
	}

	public boolean isForceTop() {
		return forceTop;
	}
	public void setForceTop( boolean forceTop ) {
		this.forceTop = forceTop;
	}

	public boolean isForceBottom() {
		return forceBottom;
	}
	public void setForceBottom( boolean forceBottom ) {
		this.forceBottom = forceBottom;
	}

	
}
