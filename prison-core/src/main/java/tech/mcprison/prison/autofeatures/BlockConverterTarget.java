package tech.mcprison.prison.autofeatures;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class BlockConverterTarget {

	private String blockName;
	private Integer quantity;
	private Double chance;
	private List<String> permissions;
	
	public BlockConverterTarget( String blockName, Integer quantity ) {
		super();
		
		this.blockName = blockName;
		this.quantity = quantity;
		this.chance = null;
		
		this.permissions = new ArrayList<>();
	}

	public BlockConverterTarget( String blockName, Integer quantity, Double chance ) {
		this( blockName, quantity );
		
		this.chance = chance;
	}
	
	/**
	 * 
	 * @param rawTarget
	 */
	public BlockConverterTarget( Object rawTarget ) {
		super();

		this.permissions = new ArrayList<>();
	
		parseConverterTarget( rawTarget );
	}
	
	private void parseConverterTarget( Object rawTarget ) {
		
		// This should be a TreeMaps of field settings.   
		if ( rawTarget != null && rawTarget instanceof TreeMap ) {
			
			@SuppressWarnings("unchecked")
			TreeMap<String, Object> rawMaps = (TreeMap<String, Object>) rawTarget;
			
			if ( rawMaps != null ) {
				if ( rawMaps.containsKey("blockName") ) {
					Object raw = rawMaps.get( "blockName" );
					
					if ( raw != null && raw instanceof String ) {
						
						setBlockName( (String) raw );
					}
				}

				if ( rawMaps.containsKey("quantity") ) {
					Object raw = rawMaps.get( "quantity" );
					
					if ( raw != null && raw instanceof Integer ) {
						
						setQuantity( (Integer) raw );
					}
				}
				
				if ( rawMaps.containsKey("chance") ) {
					Object raw = rawMaps.get( "chance" );
					
					if ( raw != null && raw instanceof Double ) {
						
						setChance( (Double) raw );
					}
				}
				
				if ( rawMaps.containsKey("permissions") ) {
					Object raw = rawMaps.get( "permissions" );
					
					if ( raw != null && raw instanceof String ) {
						
						getPermissions().add( (String) raw );
					}
					else if ( raw != null && raw instanceof ArrayList ) {
						
						@SuppressWarnings("unchecked")
						ArrayList<String> perms = (ArrayList<String>) raw;
						
						for (String perm : perms) {
							getPermissions().add(perm);
						}
					}
				}
			}
		}
	}
	
	/**
	 * <p>This function converts this object in to a saveable yaml object.
	 * It basically undoes what parseConverterTarget does.
	 * </p>
	 * @return
	 */
	public TreeMap<String, Object> toYamlMap() {
		TreeMap<String, Object> results = new TreeMap<>();
		
		results.put( "blockName", getBlockName() );
		results.put( "quantity", getQuantity() == null ? null : getQuantity().intValue() );
		
		if ( getChance() != null ) {
			results.put( "chance", getChance().doubleValue() );
		}
		
		if ( getPermissions() != null && getPermissions().size() > 0 ) {
			results.put( "permissions", getPermissions() );
		}
//		
//		results.put( "blockName", new TextNode( getBlockName() ).getValue() );
//		results.put( "quantity", getQuantity() == null ? 
//				new NullNode().getValue() : new IntegerNode( getQuantity() ).getValue() );
//		
//		if ( getChance() != null ) {
//			results.put( "chance", new DoubleNode( getChance() ).getValue() );
//		}
//		
//		if ( getPermissions() != null && getPermissions().size() > 0 ) {
//			results.put( "permissions", new StringListNode( getPermissions() ).getValue() );
//		}
		
		return results;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append( "(" );
		sb.append( getBlockName() ).append( " ");
		sb.append( getQuantity() );
		
		if ( getChance() != null && getChance() != 0d ) {
			sb.append( " " ).append( getChance() );
		}
		
		if ( getPermissions().size() > 0 ) {
			StringBuilder sbp = new StringBuilder();
			sbp.append( " [" );
			
			for (String perm : getPermissions() ) {
				if ( sbp.length() > 2 ) {
					sbp.append( ", " );
				}
				sbp.append( perm );
			}
			sbp.append( "]" );
			sb.append( sbp );
		}
		
		sb.append( ")" );
		
//		sb.append( "{" );
//		sb.append( "blockName:" ).append( getBlockName() ).append( ", ");
//		sb.append( "quantity:" ).append( getQuantity() );
//		sb.append( "}" );
		
		return sb.toString();
	}
	
	public String getBlockName() {
		return blockName;
	}
	public void setBlockName(String blockName) {
		this.blockName = blockName;
	}

	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Double getChance() {
		return chance;
	}
	public void setChance(Double chance) {
		this.chance = chance;
	}

	public List<String> getPermissions() {
		return permissions;
	}
	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}
}
