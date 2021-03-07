package tech.mcprison.prison.autofeatures;

import java.util.ArrayList;
import java.util.List;

import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;

public class AutoFeaturesWrapper
{
	
	private static AutoFeaturesWrapper instance = null;

	private AutoFeaturesFileConfig autoFeaturesConfig = null;

	private AutoFeaturesWrapper() {
		super();
		
		this.autoFeaturesConfig = new AutoFeaturesFileConfig();
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
	

	public AutoFeaturesFileConfig getAutoFeaturesConfig() {
		return autoFeaturesConfig;
	}

	public boolean isBoolean( AutoFeatures feature ) {
		return autoFeaturesConfig.isFeatureBoolean( feature );
	}

	public String getMessage( AutoFeatures feature ) {
		return autoFeaturesConfig.getFeatureMessage( feature );
	}

	public int getInteger( AutoFeatures feature ) {
		return autoFeaturesConfig.getInteger( feature );
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
