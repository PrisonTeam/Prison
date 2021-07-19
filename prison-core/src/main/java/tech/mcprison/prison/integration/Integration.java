package tech.mcprison.prison.integration;

/**
 * Represents an integration into a third-party plugin.
 * An integration should serve as a template which implementations should easily and safely
 * be able to fill with the desired data.
 *
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public interface Integration {


	/**
	 * If there is an initialization process that is dependent upon the modules being loaded, 
	 * then place it within this function.  The integrations are initialized prior to the 
	 * modules, but this is a way to defer initializing something until after they are loaded.
	 */
	public void deferredInitialization();
	
	
	/**
	 * This is used to identify if an integration has been registered with the server engine,
	 * such as with <code>Bukkit.getPluginManager().isPluginEnabled()</code>.  If the 
	 * the given integration has not been registered, then it could be dangerous tying to 
	 * access the class files associated with that plugin since it could result in 
	 * class not found exceptions, or similar.  Some plugins may have the same identifier, 
	 * but yet different internal classes depending upon their versions, such as LuckPerms,
	 * so additional code must be used to verify what is actually loaded.  Of course there
	 * are always exceptions to the rules too.  
	 *  
	 * @return true if the requested plugin has been registered with the server engine
	 */
	public boolean isRegistered();
	
	/**
	 * Used to identify if the integration (plugin) has been registered with the server
	 * engine.
	 * 
	 * @param registered
	 */
	public void setRegistered( boolean registered );

	/**
     * Returns the type of integration that this class provides.
     *
     * @return The {@link IntegrationType}.
     */
    public IntegrationType getType();

    /**
     * Returns true if this class has integrated with the provider successfully, or false 
     * otherwise. This should also return false if the third-party plugin which this 
     * integration is built for is not present.  This is not the same as isRegistered, 
     * since a few integrations may have different class files backing it (such as LuckPerms).
     *
     * @return true if this class has integrated successfully, false otherwise.
     */
    public boolean hasIntegrated();
    
    
    /**
     * The function that is called to perform the integration.  Generally the check is done
     * prior to calling this function, but may vary.  The attempt at integration used to be
     * performed in the constructor, but but that prevents storing the failed integrations
     * which can then be used to inform the admins what's available.  If the integration 
     * was successful, then hasIntegrated() will return a value of true.
     */
    public void integrate();
    
    /**
     * Returns the name of the internal representation that should be used for this integrator.
     * It may not always be the same as providerName, but in most circumstances it will be. 
     * It's important that this keyName should be unique from any other integration keyName. 
     * For example, since there are two major kinds of LuckPerms that are supported, 
     * these should reflect which one is which.  This field is used internally within 
     * collections and for display purposes when listing integrations so it should be
     * more descriptive than the providerName.
     */
    public String getKeyName();

    /**
     * Returns the name of the third-party plugin that this class is integrating with.  This 
     * must be exactly what is provided in the plugin's <code>plugin.yml</code> file that 
     * is within the given jar files for the field called <code>name:</code>.
     *
     * @return The name of the provider.
     */
    public String getProviderName();
    
    /**
     * The displayName is the value that is actually used when listing all the integrations.
     * In general it is the combination of both the providerName and the keyName, but 
     * different integrations could customize it to better suite their unique requirements,
     * such as with Vault.
     * 
     * @return
     */
    public String getDisplayName();
    
    /**
     * <p>This is a way for an integration to provide additional information when the
     * integration is displayed through the /prison version command.  This may vary
     * from integration to integration, or by type.  It is perfectly valid for it 
     * to return a null value, which indicates there is nothing available, so ignore
     * this.
     * </p>
     * 
     * @return null means nothing is available so ignore
     */
    public String getAlternativeInformation();
    
    /**
     * <p>To help admins setup integrations, this is the URL that is generally associated
     * with the integration.  It should usually refer to the spigotmc.org website for
     * that resource. 
     * </p>
     * 
     * @return null means nothing is available so this value should be ignored
     */
    public String getPluginSourceURL();
    
    
    public String getVersion();
    public void setVersion( String version );
    
    
	public String getDebugInfo();
	public void setDebugInfo( String debugInfo );
	public void addDebugInfo( String debugInfo );


	public void disableIntegration();
}
