package tech.mcprison.prison.modules;

/**
 * <p>This represents an element that is found within a module.  It may be
 * either a Mine, Rank, or even maybe a Ladder too.
 * </p>
 * 
 * <p>The important factor here is that this can allow one module's elements
 * to be referenced within another module without having to include that
 * module for compiling.  The key is to include the core components required 
 * for the basic references.
 * </p>
 *
 */
public interface ModuleElement {
	
	// private transient final ModuleElementType elementType;
	public ModuleElementType getModuleElementType();
	
	public int getId();
	
	public String getName();
	
	public String getTag();

}
