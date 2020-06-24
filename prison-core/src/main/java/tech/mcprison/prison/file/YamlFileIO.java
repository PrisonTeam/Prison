package tech.mcprison.prison.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.autofeatures.BooleanNode;
import tech.mcprison.prison.autofeatures.DoubleNode;
import tech.mcprison.prison.autofeatures.IntegerNode;
import tech.mcprison.prison.autofeatures.LongNode;
import tech.mcprison.prison.autofeatures.TextNode;
import tech.mcprison.prison.autofeatures.ValueNode;

public abstract class YamlFileIO {

	private File yamlFile;
	
	public YamlFileIO( File yamlFile ) {
		super();
		
		this.yamlFile = yamlFile;
	}
	
	public boolean saveYamlAutoFeatures( Map<String, ValueNode> config ) {
		
		// clears out the pre-existing data that is stored:
		clear();
		

		// Create the sections first:
		// This controls the order in which the sections will appear in the file:
		for ( AutoFeatures autoFeat : AutoFeatures.values() ) {
			if ( autoFeat.isSection() ) {
				createSection( autoFeat.getKey() );
			}
		}
		
		
		Set<String> keys = config.keySet();
		for ( String key : keys ) {
			ValueNode value = config.get( key );
			
			if ( value.isNullNode() ) {
				// skip nulls
				set( key, null );
			}
			else if ( value.isBooleanNode() ) {
				set( key, ((BooleanNode) value).getValue() );
			}
			else if ( value.isTextNode() ) {
				set( key, ((TextNode) value).getValue() );
			}
			else if ( value.isDoubleNode() ) {
				set( key, ((DoubleNode) value).getValue() );
			}
			else if ( value.isLongNode() ) {
				set( key, ((LongNode) value).getValue() );
			}
			else if ( value.isIntegerNode() ) {
				set( key, ((IntegerNode) value).getValue() );
			}
			else  {
				// invalid type... not supported.
//				set( key, value );
			}
		}
		
		return saveYaml();
	}
	
	/**
	 * This function loads a yaml file based upon the AutoFeatures enumeration.
	 * It loads the proper ValueNode based upon the defined value within the
	 * enum.  If there is a property that does not exist within the yaml file,
	 * it will add it to the config object with the default value, plus it will
	 * return a list of all features that do not exist (dne).
	 * 
	 * This way, if the dne List is not empty, then it needs to be saved to 
	 * update what is saved on the file system.  Only the new items, with the
	 * default values, will be added; the original values will not be altered.
	 * 
	 * @param config
	 * @return
	 */
	public List<AutoFeatures> loadYamlAutoFeatures( Map<String, ValueNode> config ) {
		List<AutoFeatures> dne = new ArrayList<>();
		
		// Load from the actual yaml file:
		loadYaml();
		
		Set<String> keys = getKeys();

		for ( AutoFeatures autoFeat : AutoFeatures.values() ) {
			ValueNode value = null;
			
			if ( autoFeat.isSection() ) {
				createSection( autoFeat.getKey() );
			}
			else if ( autoFeat.isBoolean() ) {
				value = BooleanNode.valueOf( 
					getBoolean( autoFeat.getKey(), autoFeat.getValue().booleanValue() )	);
			}
			else if ( autoFeat.isMessage() ) {
				value = TextNode.valueOf( 
					getString( autoFeat.getKey(), autoFeat.getMessage() ));
			}
			else if ( autoFeat.isInteger() ) {
				value = IntegerNode.valueOf(  
					getInteger( autoFeat.getKey(), autoFeat.getIntValue().intValue() ));
			}
			else if ( autoFeat.isLong() ) {
				value = LongNode.valueOf(  
					getLong( autoFeat.getKey(), autoFeat.getLongValue().longValue() ));
			}
			else if ( autoFeat.isDouble() ) {
				value = DoubleNode.valueOf(  
					getDouble( autoFeat.getKey(), autoFeat.getDoubleValue().doubleValue() ));
			}
			
			
			if ( !keys.contains( autoFeat.getKey() )) {
				// AutoFeature does not exist in save file:
				dne.add( autoFeat );
			}
			
			if ( value != null ) {
				config.put( autoFeat.getKey(), value );
			}
		}
		
		if ( dne.size() > 0 ) {
			// New configs were found. Saving the configs.
			saveYamlAutoFeatures( config );
			Output.get().logWarn( 
					String.format( "&cNotice: &7AutoManager config file was just updated with &c%s&7 new entries. " +
							"May need to be configured. &cFile: &7%s", 
							Integer.toString( dne.size() ),
							getYamlFile().getName()) );
		}
		
		if ( keys.size() == 0 ) {
			Output.get().logWarn( 
            		String.format( "Notice: AutoManager config file was just created. " +
            				"You must configure it to use it. &cFile: &7%s", 
            				getYamlFile().getName()) );
		}
		
		return dne;
	}
	

	public File getYamlFile() {
		return yamlFile;
	}


	abstract protected boolean loadYaml();
	abstract protected boolean saveYaml();
	
	abstract public Set<String> getKeys();
	abstract public Map<String, Object> getValues();
	
	abstract protected void clear();
	abstract protected void set( String key, Object value );
	abstract protected void createSection( String key );
	
	abstract protected boolean getBoolean( String key );
	abstract protected boolean getBoolean( String key, boolean defaultValue );
	
	abstract protected String getString( String key );
	abstract protected String getString( String key, String defaultValue );

	abstract protected double getDouble( String key );
	abstract protected double getDouble( String key, double defaultValue );
	
	abstract protected int getInteger( String key );
	abstract protected int getInteger( String key, int intValue );

	abstract protected long getLong( String key );
	abstract protected long getLong( String key, long defaultValue );
	
}
