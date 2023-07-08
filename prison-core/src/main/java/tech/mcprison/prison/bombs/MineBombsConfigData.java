package tech.mcprison.prison.bombs;

import java.util.Map;
import java.util.TreeMap;

import tech.mcprison.prison.file.FileIOData;

public class MineBombsConfigData
	implements FileIOData
{
	/**
	 * <p>If the format of this class, or any other variables and their
	 * classes change that would effect the structure of the save file,
	 * then increment this variable by 1.  This will force the saved
	 * files on the servers to be updated to the latest format of the
	 * data.
	 * </p>
	 */
	public static final int MINE_BOMB_DATA_FORMAT_VERSION = 1;
	
	private int dataFormatVersion = 1;
	
	private Map<String, MineBombData> bombs;
	
	public MineBombsConfigData() {
		super();
		
		this.dataFormatVersion = 0;
		
		this.bombs = new TreeMap<>();
	}

	public int getDataFormatVersion() {
		return dataFormatVersion;
	}
	public void setDataFormatVersion( int dataFormatVersion ) {
		this.dataFormatVersion = dataFormatVersion;
	}

	public Map<String, MineBombData> getBombs() {
		return bombs;
	}
	public void setBombs( Map<String, MineBombData> bombs ) {
		this.bombs = bombs;
	}
}
