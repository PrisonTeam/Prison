package tech.mcprison.prison.modules;

public enum ModuleElementType {
	
	MINE,
	RANK,
	LADDER;

	public static ModuleElementType fromString( String value )
	{
		ModuleElementType results = null;

		for ( ModuleElementType meType : values() ) {
			if ( meType.name().equals( value ) ) {
				results = meType;
				break;
			}
		}
		return results;
	}

}
