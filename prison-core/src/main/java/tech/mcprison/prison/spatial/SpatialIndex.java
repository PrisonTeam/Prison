package tech.mcprison.prison.spatial;

import java.util.NavigableMap;

public class SpatialIndex
{
	public static final int SPATIAL_INDEX_GRANULARIT = 25;
	
	private NavigableMap<Integer, String> idxX;
	private NavigableMap<Integer, String> idxY;
	private NavigableMap<Integer, String> idxZ;

}
