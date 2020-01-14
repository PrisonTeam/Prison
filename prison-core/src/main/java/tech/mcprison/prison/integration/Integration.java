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
     * Returns the type of integration that this class provides.
     *
     * @return The {@link IntegrationType}.
     */
    public IntegrationType getType();

    /**
     * Returns the name of the third-party plugin that this class is integrating with.
     *
     * @return The name of the provider.
     */
    public String getProviderName();

    /**
     * Returns true if this class has integrated with the provider successfully, or false otherwise.
     * This should also return false if the third-party plugin which this integration is built for is not present.
     *
     * @return true if this class has integrated successfully, false otherwise.
     */
    public boolean hasIntegrated();
    
    
    /**
     * The function that is called to perform the integration.  Generally the check is done
     * prior to calling this function, but may vary.  The attempt at integration used to be
     * performed in the constructor, but but that prevents storing the failed integrations
     * which can then be used to inform the admins what's available.
     */
    public void integrate();
    
    /**
     * Returns the name of the internal representation that should be used for this integrator.
     * It may not always be the same as providerName, but in most circumstances it will be.
     */
    public String getKeyName();

}
