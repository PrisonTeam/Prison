package tech.mcprison.prison.troubleshoot;

import tech.mcprison.prison.internal.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The TroubleshootManager is responsible for containing instances of all registered {@link Troubleshooter}s.
 * You should register your {@link Troubleshooter} here.
 *
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public class TroubleshootManager {

    private List<Troubleshooter> troubleshooters;

    public TroubleshootManager() {
        troubleshooters = new ArrayList<>();
    }

    /**
     * Register a {@link Troubleshooter} with the manager.
     *
     * @param troubleshooter The instantiated {@link Troubleshooter} to register.
     * @throws IllegalArgumentException If a {@link Troubleshooter} with the same name is already registered.
     */
    public void registerTroubleshooter(Troubleshooter troubleshooter) {
        if (getTroubleshooter(troubleshooter.getName()).isPresent()) {
            throw new IllegalArgumentException(
                "troubleshooter with name " + troubleshooter.getName() + " already registered");
        }
        troubleshooters.add(troubleshooter);
    }

    /**
     * Invoke a specific {@link Troubleshooter}.
     *
     * @param name   The name of the {@link Troubleshooter}. This is not case sensitive, but should contain underscores.
     * @param sender The {@link CommandSender} invoking this troubleshooter. Messages will be sent to this sender.
     * @return The {@link TroubleshootResult} if the {@link Troubleshooter} could be run, or null if there is no {@link Troubleshooter} by the provided name.
     */
    public TroubleshootResult invokeTroubleshooter(String name, CommandSender sender) {
        Optional<Troubleshooter> troubleshooter = getTroubleshooter(name);
        return troubleshooter.map(troubleshooter1 -> troubleshooter1.invoke(sender)).orElse(null);

    }

    public Optional<Troubleshooter> getTroubleshooter(String name) {
        return troubleshooters.stream()
            .filter(troubleshooter -> troubleshooter.getName().equalsIgnoreCase(name)).findFirst();
    }

    public List<Troubleshooter> getTroubleshooters() {
        return troubleshooters;
    }

}
