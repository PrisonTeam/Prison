package me.faizaand.prison.integration;

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
    IntegrationType getType();

    /**
     * Returns the name of the third-party plugin that this class is integrating with.
     *
     * @return The name of the provider.
     */
    String getProviderName();

    /**
     * Returns true if this class has integrated with the provider successfully, or false otherwise.
     * This should also return false if the third-party plugin which this integration is built for is not present.
     *
     * @return true if this class has integrated successfully, false otherwise.
     */
    boolean hasIntegrated();

}
