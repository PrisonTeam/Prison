package tech.mcprison.prison.autofeatures;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

public class BlockConvertersNode
//	e-xtends ValueNode 
{
//    private static final long serialVersionUID = -1L;

    TreeMap<String, BlockConverter> value;
    
    public BlockConvertersNode() {
    	super();
    	
    	this.value = new TreeMap<>();
    }
    
//    public BlockConvertersNode( TreeMap<String, BlockConverter> value ) {
//    	this();
//    	
//    	this.value = value;
//    }
    
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		List<String> strList = new ArrayList<>();
		
		Set<String> keys = value.keySet();
		for (String key : keys) {
			BlockConverter bConverter = value.get(key);
			
			String str = bConverter.toString();
			strList.add(str);
		}
		
		sb.append( String.join( ", ", strList ) );
		sb.insert( 0, "[" ).append("]");
		
		return sb.toString();
	}
	
    
	public TreeMap<String, BlockConverter> getValue() {
		return value;
	}
	
//	@Override
//	public boolean isBlockConvertersNode() { return true; }
//	
//	@Override
//	public NodeType getNodeType() {
//		return NodeType.BLOCK_CONVERTER;
//	}
   
    
	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
		if (o == null) return false;
        if (o instanceof BlockConvertersNode) {
        	
        	final BlockConvertersNode otherValue = ((BlockConvertersNode) o);
        	if ( getValue() == null && otherValue.getValue() == null ) return true;
        	if ( getValue() != null ) {
        		return getValue().equals( otherValue.getValue() );
        	}
        }
		return false;
	}
	
	@Override
	public int hashCode() {
        // same as hashCode that List uses
        return getValue().hashCode();
	}

//	/**
//	 * <p>This function takes a map of maps to construct and load the block converters.
//	 * This process takes the keys of this map, and the related object, and then constructs
//	 * the BlockConverter object.  The BlockConverter object deals with processing it's own
//	 * content as passed to it with the Object form this rawBlockConverters map.
//	 * </p>
//	 * 
//	 * @param rawBlockConverters
//	 */
//	public void loadFromYamlFile(TreeMap<String, Object> rawBlockConverters) {
//
//		Set<String> keys = rawBlockConverters.keySet();
//		
//		for (String key : keys) {
//			Object rawBlockConverterData = rawBlockConverters.get(key);
//			
//			BlockConverter blockConverter = new BlockConverter( key, rawBlockConverterData );
//			
//			value.put( blockConverter.getKeyBlockName(), blockConverter );
//		}
//	}
//
//	public TreeMap<String, TreeMap<String, Object>> toYamlMap() {
//		TreeMap<String, TreeMap<String, Object>> results = new TreeMap<>();
//		
//		Set<String> keys = getValue().keySet();
//		for (String key : keys) {
//			BlockConverter blockConverter = getValue().get(key);
//			
//			// NOTE: The TreeMap will always have exactly ONE entry:
//			TreeMap<String, Object> rawBlockConverter = blockConverter.toYamlMap();
//			String yamlKey = rawBlockConverter.get( "keyBlockName" ).toString();
//			
////			List<TreeMap<String, Object>> yamlBlockConverter = rawBlockConverter.firstEntry().getValue();
//			
//			results.put( yamlKey, rawBlockConverter );
//		}
//		
//		return results;
//	}

}
