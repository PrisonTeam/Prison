package tech.mcprison.prison.internal.events.world;

/**
 * <p>This event monitors the loading of worlds.  During the loading of mines, not
 * all worlds may be loaded yet, so we need to monitor when they come online
 * so we can hook them up to the mines.
 * </p>
 * 
 * <p>At this point in time, all we need to know is the world name when it loads.
 * All other uses of the world can go through the standard procedures since
 * it will then be fully loaded.  The key is knowing when the world is avialble 
 * for use.
 * </p>
 *
 */
public class PrisonWorldLoadEvent {

	private String worldName;
	
	public PrisonWorldLoadEvent(String worldName) {
		super();
		
		this.worldName = worldName;
	}

	public String getWorldName() {
		return worldName;
	}
	public void setWorldName( String worldName ) {
		this.worldName = worldName;
	}
	
}
