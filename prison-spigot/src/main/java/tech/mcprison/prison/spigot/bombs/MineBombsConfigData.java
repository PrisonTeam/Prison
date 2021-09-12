package tech.mcprison.prison.spigot.bombs;

import java.util.Map;
import java.util.TreeMap;

import tech.mcprison.prison.file.FileIOData;

public class MineBombsConfigData
	implements FileIOData
{
	private Map<String, MineBombData> bombs;
	
	public MineBombsConfigData() {
		super();
		
		this.bombs = new TreeMap<>();
	}

	public Map<String, MineBombData> getBombs() {
		return bombs;
	}
	public void setBombs( Map<String, MineBombData> bombs ) {
		this.bombs = bombs;
	}
}
