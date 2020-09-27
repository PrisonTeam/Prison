package tech.mcprison.prison.spigot.util;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import tech.mcprison.prison.file.YamlFileIO;
import tech.mcprison.prison.output.Output;

public class SpigotYamlFileIO 
	extends YamlFileIO {
	
	private YamlConfiguration yamlConfig;
	
	public SpigotYamlFileIO( File yamlFile ) {
		super( yamlFile );
		
		this.yamlConfig = new YamlConfiguration();
		
		loadYaml();
	}
	
	@Override
	protected boolean loadYaml() {
		boolean results = false;
		if (!getYamlFile().exists()) {
			getYamlFile().getParentFile().mkdirs();
			
			try {
				getYamlFile().createNewFile();
			}
			catch ( IOException e ) {
				Output.get().logWarn( 
	            		String.format( "Notice: Failed to create a config file. " +
	            				"&cFile: &7%s   &cError: &7%s", 
	            				getYamlFile().getName(), e.getMessage()) );
			}
         }

        try {
        	yamlConfig.load(getYamlFile());
        	results = true;
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
		return results;
	}
	
	protected boolean saveYaml() {
		boolean results = false;
		
		try {
			yamlConfig.save(getYamlFile());
			results = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return results;
	}

	@Override
	public Set<String> getKeys() {
		return yamlConfig.getKeys( true );
	}
	
	@Override
	public Map<String, Object> getValues() {
		return yamlConfig.getValues( true );
		
	}

	@Override
	protected void clear() {
		Set<String> keys = getKeys();
		for ( String key : keys ) {
			yamlConfig.set( key, null );
		}
	}

	@Override
	protected void set( String key, Object value ) {
		yamlConfig.set( key, value );
	}

	@Override
	protected void createSection( String key ) {
		yamlConfig.createSection( key ) ;
	}
	
	
	@Override
	protected boolean getBoolean( String key ) {
		return yamlConfig.getBoolean( key ) ;
	}
	@Override
	protected boolean getBoolean( String key, boolean defaultValue ) {
		return yamlConfig.getBoolean( key, defaultValue ) ;
	}
	
	@Override
	protected String getString( String key ) {
		return yamlConfig.getString( key ) ;
	}
	@Override
	protected String getString( String key, String defaultValue ) {
		return yamlConfig.getString( key, defaultValue ) ;
	}
	
	@Override
	protected double getDouble( String key ) {
		return yamlConfig.getDouble( key ) ;
	}
	@Override
	protected double getDouble( String key, double defaultValue ) {
		return yamlConfig.getDouble( key, defaultValue ) ;
	}
	
	@Override
	protected int getInteger( String key ) {
		return yamlConfig.getInt( key ) ;
	}
	@Override
	protected int getInteger( String key, int defaultValue ) {
		return yamlConfig.getInt( key, defaultValue ) ;
	}
	
	@Override
	protected long getLong( String key ) {
		return yamlConfig.getLong( key ) ;
	}
	@Override
	protected long getLong( String key, long defaultValue ) {
		return yamlConfig.getLong( key, defaultValue ) ;
	}

	
	public void test( ) {
		
//		yamlConfig. ;
		
		
	}
	
	


}
