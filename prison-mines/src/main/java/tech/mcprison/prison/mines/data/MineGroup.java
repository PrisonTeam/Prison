package tech.mcprison.prison.mines.data;

import java.util.List;

import tech.mcprison.prison.internal.World;

/**
 * <p>Currently this MineGroup class is not being used.
 * It will be used in the near future.
 * </p>
 *
 */
public class MineGroup
{
	private String name;
	
	private String description;
	
	private World world;
	private String worldName;
	
	List<Mine> mines;
	
	public MineGroup() {
		super();
		
	}

	public String getName() {
		return name;
	}
	public void setName( String name ) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription( String description ) {
		this.description = description;
	}

	public World getWorld() {
		return world;
	}
	public void setWorld( World world ) {
		this.world = world;
	}

	public String getWorldName() {
		return worldName;
	}
	public void setWorldName( String worldName ) {
		this.worldName = worldName;
	}

	public List<Mine> getMines() {
		return mines;
	}
	public void setMines( List<Mine> mines ) {
		this.mines = mines;
	}
}
