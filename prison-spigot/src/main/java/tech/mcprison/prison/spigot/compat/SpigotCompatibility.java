package tech.mcprison.prison.spigot.compat;

import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.spiget.BluesSemanticVersionData;
import tech.mcprison.prison.spigot.spiget.BluesSpigetSemVerComparator;

public class SpigotCompatibility {

	private static Compatibility instance;

	public static Compatibility getInstance() {
		
		if ( instance == null ) {
			setup();
		}
		
		return instance;
	}
	
	private static synchronized void setup() {
		
		if ( instance == null ) {
			
			Compatibility results = null;
			
			String bukkitVersion =  new BluesSpigetSemVerComparator().getBukkitVersion();
			
			if ( bukkitVersion == null ) {
				
				results = new Spigot_1_13();
			}
			else {
				
				BluesSemanticVersionData svData = new BluesSemanticVersionData( bukkitVersion );
				
				if ( svData.compareTo( new BluesSemanticVersionData( "1.9.0" ) ) < 0 ) {
					
					results = new Spigot_1_8();
				}
				else if ( svData.compareTo( new BluesSemanticVersionData( "1.13.0" ) ) < 0 ) {
					
					results = new Spigot_1_9();
				}
				else if ( svData.compareTo( new BluesSemanticVersionData( "1.14.0" ) ) < 0 ) {
					
					results = new Spigot_1_13();
				}
				else if ( svData.compareTo( new BluesSemanticVersionData( "1.18.0" ) ) < 0 ) {
					
					results = new Spigot_1_14();
				}
				else {
					
					results = new Spigot_1_18();
				}
			}
			
			Output.get().logInfo( "Using version adapter " + results.getClass().getName());
			
			instance = results;
		}
	}
}
