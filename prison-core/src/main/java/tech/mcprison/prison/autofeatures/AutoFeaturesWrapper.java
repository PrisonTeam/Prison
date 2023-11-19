package tech.mcprison.prison.autofeatures;

import java.util.ArrayList;
import java.util.List;

import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
import tech.mcprison.prison.output.Output;

public class AutoFeaturesWrapper
{
	
	private static AutoFeaturesWrapper instance = null;

	private AutoFeaturesFileConfig autoFeaturesConfig = null;
	
	private static BlockConvertersFileConfig blockConvertersConfig = null;

	private AutoFeaturesWrapper() {
		super();
		
		this.autoFeaturesConfig = new AutoFeaturesFileConfig();
		
//		if ( Output.get().isDebug() ) {
			
//			this.blockConvertersConfig = new BlockConvertersFileConfig();
//		}
	}
	
	public static AutoFeaturesWrapper getInstance() {
		if ( instance == null ) {
			
			synchronized ( AutoFeaturesWrapper.class ) {
				if ( instance == null ) {
					
					instance = new AutoFeaturesWrapper();
				}
			}
		}
		return instance;
	}
	
	
	public static BlockConvertersFileConfig getBlockConvertersInstance() {
		if ( blockConvertersConfig == null ) {
			
			synchronized ( BlockConvertersFileConfig.class ) {
				if ( blockConvertersConfig == null ) {
					
					blockConvertersConfig = new BlockConvertersFileConfig();
					
//					blockConvertersConfig.reloadConfig();
				}
			}
		}
		return blockConvertersConfig;
	}
	
	
	public void reloadConfigs() {
		getAutoFeaturesConfig().reloadConfig();
		
//		if ( Output.get().isDebug() && getBlockConvertersConfig() != null ) {
//			
//			getBlockConvertersConfig().reloadConfig();
//		}
	}

	public AutoFeaturesFileConfig getAutoFeaturesConfig() {
		return autoFeaturesConfig;
	}

//	public BlockConvertersFileConfig getBlockConvertersConfig() {
//		return blockConvertersConfig;
//	}
//
//	public void setBlockConvertersConfig(BlockConvertersFileConfig blockConvertersConfig) {
//		this.blockConvertersConfig = blockConvertersConfig;
//	}

	public boolean isBoolean( AutoFeatures feature ) {
		return autoFeaturesConfig.isFeatureBoolean( feature );
	}

	public String getMessage( AutoFeatures feature ) {
		return autoFeaturesConfig.getFeatureMessage( feature );
	}

	public int getInteger( AutoFeatures feature ) {
		return autoFeaturesConfig.getInteger( feature );
	}
	
	public double getDouble( AutoFeatures feature ) {
		return autoFeaturesConfig.getDouble( feature );
	}
	
	public List<String> getListString( AutoFeatures feature ) {
		List<String> results = null;
		if ( feature.isStringList() ) {
			results = autoFeaturesConfig.getFeatureStringList( feature );
		}
		else {
			results = new ArrayList<>();
		}
		return results;
	}


}
