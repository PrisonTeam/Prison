package tech.mcprison.prison.spigot.worldguard;

import tech.mcprison.prison.file.JsonFileIO;
import tech.mcprison.prison.output.Output;

public class WorldGuardSettings {

	public static final String WORLD_GUARD_CONFIG_PREFIX = "prison-mines.world-guard.";
	public static final String WORLD_GUARD_CONFIG_REGION_MINE = "region-mine";
	public static final String WORLD_GUARD_CONFIG_REGION_MINE_AREA = "region-mine-area";
	
	private String wgPrefixRegionMine;
	private String wgPrefixRegionMineArea;
	
	private WorldGuardData mineRegion;
	private WorldGuardData mineAreaRegion;
	
	public WorldGuardSettings() {
		super();
		
		wgPrefixRegionMine = WORLD_GUARD_CONFIG_PREFIX + WORLD_GUARD_CONFIG_REGION_MINE + ".";
		wgPrefixRegionMineArea = WORLD_GUARD_CONFIG_PREFIX + WORLD_GUARD_CONFIG_REGION_MINE_AREA + ".";
	
		mineRegion = new WorldGuardData( wgPrefixRegionMine );
		mineAreaRegion = new WorldGuardData( wgPrefixRegionMineArea );
	
		String json = new JsonFileIO().toString( this );
		
		Output.get().logInfo( "WorldGuardSettings: \n" + json );
	}

	
	
	
	public String getWGPrefixRegionMine() {
		return wgPrefixRegionMine;
	}
	public void setWGPrefixRegionMine(String wgPrefixRegionMine) {
		this.wgPrefixRegionMine = wgPrefixRegionMine;
	}

	public String getWGPrefixRegionMineArea() {
		return wgPrefixRegionMineArea;
	}
	public void setWGPrefixRegionMineArea(String wgPrefixRegionMineArea) {
		this.wgPrefixRegionMineArea = wgPrefixRegionMineArea;
	}

	public WorldGuardData getMineRegion() {
		return mineRegion;
	}
	public void setMineRegion(WorldGuardData mineRegion) {
		this.mineRegion = mineRegion;
	}

	public WorldGuardData getMineAreaRegion() {
		return mineAreaRegion;
	}
	public void setMineAreaRegion(WorldGuardData mineAreaRegion) {
		this.mineAreaRegion = mineAreaRegion;
	}
	
	
}
