package xyz.faizaan.prison.integration;

import java.util.*;

/**
 * The IntegrationManager stores instances of each {@link Integration} and allows
 * them to be registered and retrieved.
 *
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public class IntegrationManager {

    private Map<IntegrationType, List<Integration>> integrations;

    public IntegrationManager() {
        this.integrations = new HashMap<>();
    }

    /**
     * Returns a list of all of the {@link Integration}s that are registered under a certain {@link IntegrationType}, if any.
     * This includes integrations that have not successfully integrated.
     * If there are none, an empty list will be returned.
     *
     * @param type The desired {@link IntegrationType}.
     * @return A list.
     */
    public List<Integration> getAllForType(IntegrationType type) {
        return integrations.getOrDefault(type, Collections.emptyList());
    }

    /**
     * Returns an optional containing the first working {@link Integration} for the specified {@link IntegrationType}.
     * If there are no working integrations, the optional will be empty.
     *
     * @param type The desired {@link IntegrationType}.
     * @return An optional containing the first working integration, or empty if none are found.
     */
    public Optional<Integration> getForType(IntegrationType type) {
        if(!integrations.containsKey(type)) {
            return Optional.empty();
        }
        return integrations.get(type).stream().filter(Integration::hasIntegrated).findFirst();
    }

    /**
     * Returns true if there are any working {@link Integration}s registered for a specific {@link IntegrationType}.
     *
     * @param type The desired {@link IntegrationType}.
     * @return true if there is any working {@link Integration} registered, false otherwise.
     */
    public boolean hasForType(IntegrationType type) {
        return getForType(type).isPresent();
    }

    /**
     * Registers an {@link Integration}.
     * @param i The {@link Integration}.
     */
    public void register(Integration i) {
        List<Integration> integrationList = integrations.getOrDefault(i.getType(), new ArrayList<>());
        integrationList.add(i);
        integrations.put(i.getType(), integrationList);
    }

}
