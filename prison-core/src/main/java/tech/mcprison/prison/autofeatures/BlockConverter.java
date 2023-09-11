package tech.mcprison.prison.autofeatures;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * </p>
 *
 */
public class BlockConverter {
	
	private String keyBlockName;
	private int keyQuantity;
	private boolean enabled;
	
	private String mininumSpigotSemanticVersion;
	
	private ArrayList<String> permissions;
	
	private ArrayList<BlockConverterOutput> outputs;

//	private ArrayList<BlockConverterOptions> options;
	
	public BlockConverter( String blockName, int keyQuantity ) {
		super();
		
		this.keyBlockName = blockName;
		this.keyQuantity = keyQuantity;
		this.enabled = true;
		
		this.mininumSpigotSemanticVersion = null;
		
		this.permissions = new ArrayList<>();
		
		this.outputs = new ArrayList<>();

//		this.options = new ArrayList<>();
	}

	public BlockConverter( String blockName, int keyQuantity, String mininumSpigotSemanticVersion ) {
		this( blockName, keyQuantity );
		
		this.mininumSpigotSemanticVersion = mininumSpigotSemanticVersion;
	}
	
	public BlockConverter( String blockName ) {
		this( blockName, 1 );
	}
	
//	public BlockConverter( String blockName, Object rawTarget ) {
//		this( blockName, 1 );
//		
//		parseConverterTarget( rawTarget );
//	}
	
//	/**
//	 * <p>The rawTarget object is an TreeMap of objects.  The 
//	 * class variables are stored in this map, with the "transformers"
//	 * being stored under that key.  The transformer's are an ArrayList 
//	 * of TreeMaps, where each TreeMap represents a BlockConverterTarget object.
//	 * </p>
//	 * 
//	 * @param rawTarget
//	 */
//	private void parseConverterTarget( Object rawTargets ) {
//		
//		if ( rawTargets != null && rawTargets instanceof TreeMap ) {
//			
//			@SuppressWarnings("unchecked")
//			TreeMap<String, Object> rTargets = (TreeMap<String, Object>) rawTargets;
//			
//			if ( rTargets.size() >= 3 ) {
//				if ( rTargets.containsKey("keyBlockName") ) {
//					setKeyBlockName( rTargets.get( "keyBlockName" ).toString() );
//				}
//				if ( rTargets.containsKey("keyQuantity") ) {
//					int keyBlockQty = (Integer) rTargets.get( "keyQuantity" );
//					setKeyQuantity(keyBlockQty);
//				}
//				if ( rTargets.containsKey("mininumSpigotSemanticVersion") ) {
//					setMininumSpigotSemanticVersion( rTargets.get( "mininumSpigotSemanticVersion" ).toString() );
//				}
//				if ( rTargets.containsKey("enabled") ) {
//					setEnabled( (boolean) rTargets.get( "enabled" ) );
//				}
//				
//				if ( rTargets.containsKey( "transformers") ) {
//					
//					@SuppressWarnings("unchecked")
//					List<Object> roTargets = (List<Object>) rTargets.get( "transformers" );
//
//					for ( Object roTarget : roTargets) {
//						
//						BlockConverterTarget target = new BlockConverterTarget( roTarget );
//						
//						getTargets().add( target );
//					}
//				}
//				
//			}
//			
//		}
////		if ( rawTargets != null && rawTargets instanceof ArrayList ) {
////			
////			@SuppressWarnings("unchecked")
////			List<Object> rTargets = (List<Object>) rawTargets;
////			
////			for ( Object rTarget : rTargets) {
////				
////				if ( rTarget instanceof String ) {
////					String temp = (String) rTarget;
////					if ( temp.equals("") ) {
////						setKeyBlockName( temp );
////					}
////				}
////				
////				BlockConverterTarget target = new BlockConverterTarget( rTarget );
////				
////				getTargets().add( target );
////			}
////		}
//	}
	
//	/**
//	 * <p>This function converts this object in to a saveable yaml object.
//	 * It basically undoes what parseConverterTarget does.
//	 * The big difference is that where parseConverterTarget has a
//	 * blockName and an Object that represents an ArrayList, this
//	 * returns a TreeMap where the key is the blockName and the object of
//	 * the TreeMap are all of the BlockConverterTarget objects represented
//	 * as a yaml maps.
//	 * </p>
//	 * 
//	 * <p>The returned TreeMap should be added to other TreeMaps, where all of 
//	 * the keys are combined, and all of the objects are combined in to one 
//	 * TreeMap.
//	 * </p>
//	 * 
//	 * @return
//	 */
//	public TreeMap<String, Object> toYamlMap() {
//		
//		TreeMap<String, Object> results = new TreeMap<>();
//		
//		results.put( "keyBlockName", getKeyBlockName() );
//		results.put( "keyQuantity", getKeyQuantity() );
//		results.put( "enabled", isEnabled() );
//		
//		if ( getMininumSpigotSemanticVersion() != null ) {
//			results.put( "mininumSpigotSemanticVersion", getMininumSpigotSemanticVersion() );
//		}
//		
////		TreeMap<String, List<TreeMap<String, Object>>> results = new TreeMap<>();
//		
//		
//		List<TreeMap<String, Object>> targets = new ArrayList<>();
//		
//		for ( BlockConverterTarget target : getTargets() ) {
//			TreeMap<String, Object> rawTarget = target.toYamlMap();
//			
//			targets.add(rawTarget);
//		}
//
//		results.put( "transformers", targets );
//		
//		return results;
//	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append( getKeyBlockName() );
		
		sb.append( " (keyQuantity: " ).append( getKeyQuantity() );
		
		if ( isEnabled() ) {
			sb.append( " enabled" );
		}
		else {
			sb.append( " !enabled" );
		}
		
		sb.append( ")" );
		
		sb.append( ": [");
		
		List<String> strList = new ArrayList<>();
		for (BlockConverterOutput bcOutput : outputs) {
			
			strList.add( bcOutput.toString() );
		}
		sb.append( String.join( ", ", strList ) );
		
		sb.append("]");
		
		return sb.toString();
	}

	public String getKeyBlockName() {
		return keyBlockName;
	}
	public void setKeyBlockName(String keyBlockName) {
		this.keyBlockName = keyBlockName;
	}

	public int getKeyQuantity() {
		return keyQuantity;
	}
	public void setKeyQuantity(int keyQuantity) {
		this.keyQuantity = keyQuantity;
	}

	public String getMininumSpigotSemanticVersion() {
		return mininumSpigotSemanticVersion;
	}
	public void setMininumSpigotSemanticVersion(String mininumSpigotSemanticVersion) {
		this.mininumSpigotSemanticVersion = mininumSpigotSemanticVersion;
	}

	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public ArrayList<String> getPermissions() {
		return permissions;
	}
	public void setPermissions(ArrayList<String> permissions) {
		this.permissions = permissions;
	}

	public ArrayList<BlockConverterOutput> getOutputs() {
		return outputs;
	}
	public void setOutputs(ArrayList<BlockConverterOutput> outputs) {
		this.outputs = outputs;
	}

//	public ArrayList<BlockConverterOptions> getOptions() {
//		return options;
//	}
//	public void setOptions(ArrayList<BlockConverterOptions> options) {
//		this.options = options;
//	}

}
