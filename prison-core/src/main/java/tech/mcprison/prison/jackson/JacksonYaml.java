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
 * @deprecated Not used. Empty.
 * 
 */
public class JacksonYaml {
	
	public JacksonYaml() {
		super();
		
	}

	// NOTE: The commented out source was purged. This appears to be a dead class.
	//       See git history for what was purged.
	
}
