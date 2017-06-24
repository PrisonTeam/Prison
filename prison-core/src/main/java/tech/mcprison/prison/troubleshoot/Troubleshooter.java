package tech.mcprison.prison.troubleshoot;

import tech.mcprison.prison.internal.CommandSender;

/**
 * A Troubleshooter performs a maintenance routine for a common problem that a user may face.
 * Before registering a Troubleshooter, ask yourself if the problem can be avoided in your
 * design first. Troubleshooters add an extra task for the user which may hinder the user experience.
 * <p>
 * Troubleshooters are expected to safely perform fixes during runtime. A good example of where a
 * Troubleshooter can be applied is in the old ranking up bug in Prison 2, in which rank IDs would
 * corrupt and cause the same rank to be purchased repeatedly. A Troubleshooter could have set the
 * rank IDs manually at runtime.
 *
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public abstract class Troubleshooter {

    private String name, description;

    public Troubleshooter(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public abstract TroubleshootResult invoke(CommandSender invoker);

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

}
