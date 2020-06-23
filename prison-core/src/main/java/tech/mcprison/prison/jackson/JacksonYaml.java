package tech.mcprison.prison.jackson;

/**
 * <p>This class uses Jackson's YAML databind tools to
 * read and write to yaml configuration files.
 * </p>
 * 
 * <p>What makes this different, is that the yaml is built 
 * upon a hierarchy, but when loaded, it must be flattened 
 * using dot notation to represent the levels within the
 * key.
 * </p>
 * 
 * <p>Since values of the various properties can have
 * different types, internally the Jackson's ValueNode object
 * is used since it is extended to contain various kinds of
 * values.  See list:
 * </p>
 * 
 * <p>Possible ValueNodes values: 
 * </p>
 * <ul>
 *   <li>com.fasterxml.jackson.databind.node.ValueNode</li>
 *   <li>com.fasterxml.jackson.databind.node.BooleanNode</li>
 *   <li>com.fasterxml.jackson.databind.node.TextNode</li>
 *   <li>com.fasterxml.jackson.databind.node.IntNode</li>
 *   <li>com.fasterxml.jackson.databind.node.DoubleNode</li>
 * </ul>
 * 
 * <p>Collections based nodes that extend from ValueNode:
 * </p>
 * <ul>
 *   <li>com.fasterxml.jackson.databind.node.ArrayNode</li>
 *   <li>com.fasterxml.jackson.databind.node.ObjectNode (a Map)</li>
 * </ul>
 * 
 */
public class JacksonYaml {
	
	public JacksonYaml() {
		super();
		
	}

//	/**
//	 * <p>This function will read a yaml file, which loads as a hierarchical
//	 * map.  Then it will flatten the hierarchical map and return it. 
//	 * </p>
//	 * 
//	 * 
//	 * @param file
//	 */
//	public Map<String, ValueNode> loadYamlConfigFile( File file ) {
//		Map<String, ValueNode> map = null;
//		
//		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
//
//		// If using something like a custom date formatter, etc:
//		mapper.findAndRegisterModules();
//		
//
//		try {
//			JsonNode tree = mapper.readTree( file );
//			
//			map = flattenJson( tree );
//			
////			for (Map.Entry<String, ValueNode> kv : map.entrySet()) {
////				System.out.println(kv.getKey() + ": " + 
////						kv.getValue().asText() + " [" + 
////						kv.getValue().getClass() +  "]");
////			}
//		}
//		catch ( IOException e ) {
//			Output.get().logError( String.format( "JacksonYaml.loadYamlConfigFile: " +
//					"Failure: file= %s  :: %s ", file.getAbsoluteFile(), e.getMessage() ));
//		}
//		
//		return map;
//	}
//
//
//
//	private Map<String, ValueNode> flattenJson(JsonNode input) {
//	    Map<String, ValueNode> map = new LinkedHashMap<>();
//	    flattenJson(input, null, map);
//	    return map;
//	}
//	
//	private void flattenJson(JsonNode node, String parent, Map<String, ValueNode> map) {
//	    if (node instanceof ValueNode) {
//	        map.put(parent, (ValueNode)node);
//	    } 
//	    else {
//	        String prefix = parent == null ? "" : parent + ".";
//	        if (node instanceof ArrayNode) {
//	            ArrayNode arrayNode = (ArrayNode)node;
//	            for(int i = 0; i < arrayNode.size(); i++) {
//	                flattenJson(arrayNode.get(i), prefix + i, map);
//	            }
//	        } 
//	        else if (node instanceof ObjectNode) {
//	            ObjectNode objectNode = (ObjectNode) node;
//	            for (Iterator<Map.Entry<String, JsonNode>> it = objectNode.fields(); it.hasNext(); ) {
//	                Map.Entry<String, JsonNode> field = it.next();
//	                flattenJson(field.getValue(), prefix + field.getKey(), map);
//	            }
//	        } 
//	        else {
//				Output.get().logWarn( String.format( "JacksonYaml.flattenJson: " +
//						"Warning: Unknown node type. node= %s ", node.getClass()));
//
//	        }
//	    }
//	}
//	
//	
//	
//	/**
//	 * <p>This function will write a flat map to a yaml file after it
//	 * expands it to an hierarchical map, expanding the key values on
//	 * their periods. 
//	 * </p>
//	 * 
//	 * @param file Target file to save to
//	 * @param map A flat map of all the configs
//	 */
//	public void writeYamlConfigFile( File file, Map<String, ValueNode> map ) {
//		
//		String filenameTemp = file.getName() + ".tmp.yml";
//		File fileTemp = new File( file.getParentFile(), filenameTemp );
//		
//		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
//
//		// If using something like a custom date formatter, etc:
//		mapper.findAndRegisterModules();
//
//		try {
//			Map<String, Object> mapExpanded = expandMap( map );
//			
//			// Write to the temp file:
//			mapper.writeValue( fileTemp, mapExpanded );
//			
//			// Delete the original file if it exists:
//			if ( file.exists() ) {
//				file.delete();
//			}
//			
//			// Rename the temp file to the actual file:
//			fileTemp.renameTo( file );
//		}
//		catch ( IOException e ) {
//			Output.get().logError( String.format( "JacksonYaml.writeYamlConfigFile: " +
//					"Failure: file= %s  :: %s ", file.getAbsoluteFile(), e.getMessage() ));
//		}
//	}
//
//	/**
//	 * <p>This takes a flat map and expands the keys, breaking on the periods, 
//	 * and builds a multi-level hierarchy.
//	 * </p>
//	 * 
//	 * @param map Flat map
//	 * @return Hierarchical map
//	 */
//	private Map<String, Object> expandMap( Map<String, ValueNode> map ) {
//		Map<String, Object> m = new LinkedHashMap<>();
//		
//		for ( Entry<String, ValueNode> node : map.entrySet() ) {
//			
//			String key = node.getKey();
//			
//			if ( key != null ) {
//				
//				ValueNode value = node.getValue();
//				
//				String[] keyz = key.split( "\\." );
//
//				// There are multiple depths and we must place the value at the
//				// leaf nodes.  getChildExpandedMap traverses all the children
//				// to get to the leaf node, making the nodes if needed.
//				Map<String, Object> child = getChildExpandedMap(m, keyz, 0);
//				
//				child.put( keyz[keyz.length-1], value );
//			}
//		}
//		
//		return m;
//	}
//
//	@SuppressWarnings( "unchecked" )
//	private Map<String, Object> getChildExpandedMap( Map<String, Object> map, String[] keyz, int pos ) {
//		if ( pos < keyz.length - 1 ) {
//			String key = keyz[pos];
//			if ( !map.containsKey( key ) ) {
//				map.put( key, new LinkedHashMap<>() );
//			}
//			return getChildExpandedMap( (Map<String, Object>) map.get( key ), keyz, pos + 1);
//		}
//
//		return map;
//	}

	
}
