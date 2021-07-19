package tech.mcprison.prison.integration;

import java.lang.reflect.InvocationTargetException;

/**
 * <p>This class provides for most of the default implementations for the 
 * Integration interface, and sets up some of the expected core behaviors.
 * </p>
 * 
 */
public class IntegrationCore
	implements Integration
{
	
	private final String keyName;
	private final String providerName;
	private final IntegrationType type;
	private boolean registered = false;
	
	private String version;
	
	private String debugInfo;
	
	public IntegrationCore( String keyName, String providerName, IntegrationType type ) {
		super();
		
		this.keyName = keyName;
		this.providerName = providerName;
		this.type = type;
	}
	
	public void deferredInitialization() {
	}
	
	public boolean isRegistered() {
		return registered;
	}
	public void setRegistered( boolean registered )
	{
		this.registered = registered;
	}

	/**
	 * This may not work since the actual class loader for the plugins 
	 * might not be the System class loader.
	 * 
	 * @param klass
	 * @return
	 */
	public boolean isClassLoaded( Class<?> klass ) {
		Object results = null;
		
		if ( klass != null ) {
			try {
				java.lang.reflect.Method m = 
						ClassLoader.class.getDeclaredMethod("findLoadedClass", new Class[] { String.class});
				m.setAccessible(true);
				ClassLoader cl = ClassLoader.getSystemClassLoader();
				results = m.invoke( cl, klass.getName() );
			}
			catch ( NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException |
					InvocationTargetException e ) {
				// Ignore: the class is not loaded.
				e.getStackTrace();
			}
		}
		
        return results != null;
	}
	
    public IntegrationType getType() {
        return type;
    }
    
    public boolean hasIntegrated() {
    	return false;
    }
    
    
    public void integrate() {
    	
    }
    
    public void disableIntegration() {
    	
    }
    
    public String getKeyName() {
    	return keyName;
    }
    
    public String getProviderName() {
    	return providerName;
    }
    
    public String getDisplayName() {
    	return getProviderName() + 
    			( getProviderName().equals( getKeyName() ) ? "" : 
    				" (" + getKeyName() + ")" );
    }


    public String getAlternativeInformation() {
    	return null;
    }
    
    public String getPluginSourceURL() {
    	return null;
    }

    @Override
    public String getVersion() {
		return version;
	}
    @Override
	public void setVersion( String version ) {
		this.version = version;
	}

	@Override
	public String getDebugInfo() {
		return debugInfo;
	}
    @Override
	public void setDebugInfo( String debugInfo ) {
		this.debugInfo = debugInfo;
	}
    @Override
	public void addDebugInfo( String debugInfo ) {
		this.debugInfo = (this.debugInfo == null ? "" : this.debugInfo) +
				"(" + debugInfo + ")";
	}
    
}
