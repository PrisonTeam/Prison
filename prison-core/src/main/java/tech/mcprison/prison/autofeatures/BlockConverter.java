package tech.mcprison.prison.autofeatures;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class BlockConverter {
	
	private String key;
	
	private List<BlockConverterTarget> targets;
	
	public BlockConverter( String blockName ) {
		super();
		
		this.key = blockName;
		
		this.targets = new ArrayList<>();
	}
	
	public BlockConverter( String blockName, Object rawTarget ) {
		this( blockName );
		
		parseConverterTarget( rawTarget );
	}
	
	/**
	 * <p>The rawTarget object is an ArrayList of TreeMaps, where each
	 * TreeMap represents a BlockConverterTarget object.
	 * </p>
	 * 
	 * @param rawTarget
	 */
	private void parseConverterTarget( Object rawTargets ) {
		
		if ( rawTargets != null && rawTargets instanceof ArrayList ) {
			
			@SuppressWarnings("unchecked")
			List<Object> rTargets = (List<Object>) rawTargets;
			
			for ( Object rTarget : rTargets) {
				
				BlockConverterTarget target = new BlockConverterTarget( rTarget );
				
				getTargets().add( target );
			}
		}
	}
	
	/**
	 * <p>This function converts this object in to a saveable yaml object.
	 * It basically undoes what parseConverterTarget does.
	 * The big difference is that where parseConverterTarget has a
	 * blockName and an Object that represents an ArrayList, this
	 * returns a TreeMap where the key is the blockName and the object of
	 * the TreeMap are all of the BlockConverterTarget objects represented
	 * as a yaml maps.
	 * </p>
	 * 
	 * <p>The returned TreeMap should be added to other TreeMaps, where all of 
	 * the keys are combined, and all of the objects are combined in to one 
	 * TreeMap.
	 * </p>
	 * 
	 * @return
	 */
	public TreeMap<String, List<TreeMap<String, Object>>> toYamlMap() {
		TreeMap<String, List<TreeMap<String, Object>>> results = new TreeMap<>();
		
		List<TreeMap<String, Object>> targets = new ArrayList<>();
		
		for ( BlockConverterTarget target : getTargets() ) {
			TreeMap<String, Object> rawTarget = target.toYamlMap();
			
			targets.add(rawTarget);
		}

		results.put( getKey(), targets );
		
		return results;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append( getKey() ).append( ": [");
		
		List<String> strList = new ArrayList<>();
		for (BlockConverterTarget bcTarget : targets) {
			
			strList.add( bcTarget.toString() );
		}
		sb.append( String.join( ", ", strList ) );
		
		sb.append("]");
		
		return sb.toString();
	}

	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}

	public List<BlockConverterTarget> getTargets() {
		return targets;
	}
	public void setTargets(List<BlockConverterTarget> targets) {
		this.targets = targets;
	}

}
