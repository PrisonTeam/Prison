package me.faizaand.prison.gui.signs;

import me.faizaand.prison.util.GameLocation;

/**
 * Represents an individual Prison display sign in the world.
 *
 * @since 4.0
 */
public class DisplaySign {

    private GameLocation location;
    private String identifier;
    private String[] params;

    public DisplaySign(GameLocation location, String identifier, String[] params) {
        this.location = location;
        this.identifier = identifier;
        this.params = params;
    }

    public GameLocation getLocation() {
        return location;
    }

    public void setLocation(GameLocation location) {
        this.location = location;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String[] getParams() {
        return params;
    }

    public void setParams(String[] params) {
        this.params = params;
    }
}
