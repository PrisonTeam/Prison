package tech.mcprison.prison.internal.block;

public enum MineResetType
{
	normal,
//	paged,
	clear,
	tracer,
	corners,
	outline;
	
	public static MineResetType fromString( String resetType ) {
		MineResetType results = normal;
		
		if ( resetType != null ) {
			
			for (MineResetType mrt : values() ) {
				if ( mrt.name().equalsIgnoreCase( resetType ) ) {
					results = mrt;
					break;
				}
			}
		}
		
		return results;
	}
}
