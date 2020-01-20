package tech.mcprison.prison.integration;

import java.lang.reflect.InvocationTargetException;

/**
 * Represents an integration into a third-party plugin.
 * An integration should serve as a template which implementations should easily and safely
 * be able to fill with the desired data.
 *
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public class Integration {
	
	private final String keyName;
	private final String providerName;
	private final IntegrationType type;
	private boolean registered = false;
	
	public Integration( String keyName, String providerName, IntegrationType type ) {
		super();
		
		this.keyName = keyName;
		this.providerName = providerName;
		this.type = type;
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
	
	/**
     * Returns the type of integration that this class provides.
     *
     * @return The {@link IntegrationType}.
     */
    public IntegrationType getType() {
        return type;
    }

    /**
     * Returns true if this class has integrated with the provider successfully, or false otherwise.
     * This should also return false if the third-party plugin which this integration is built for is not present.
     *
     * @return true if this class has integrated successfully, false otherwise.
     */
    public boolean hasIntegrated() {
    	return false;
    }
    
    
    /**
     * The function that is called to perform the integration.  Generally the check is done
     * prior to calling this function, but may vary.  The attempt at integration used to be
     * performed in the constructor, but but that prevents storing the failed integrations
     * which can then be used to inform the admins what's available.
     */
    public void integrate() {
    	
    }
    
    /**
     * Returns the name of the internal representation that should be used for this integrator.
     * It may not always be the same as providerName, but in most circumstances it will be.
     */
    public String getKeyName() {
    	return keyName;
    }

    /**
     * Returns the name of the third-party plugin that this class is integrating with.
     *
     * @return The name of the provider.
     */
    public String getProviderName() {
    	return providerName;
    }
    
    public String getDisplayName() {
    	return getProviderName() + 
    			( getProviderName().equals( getKeyName() ) ? "" : 
    				" (" + getKeyName() + ")" );
    }
    
    /**
     * <p>This is a way for an integration to provide additional information when the
     * integration is displayed through the /prison version command.  This may vary
     * from integration to integration, or by type.  It is perfectly valid for it 
     * to return a null value, which indicates there is nothing available, so ignore
     * this.
     * @return null means nothing is available so ignore
     */
    public String getAlternativeInformation() {
    	return null;
    }
    
    public String getPluginSourceURL() {
    	return null;
    }
}
