package tech.mcprison.prison.internal;

import java.util.UUID;

public interface OfflineMcPlayer
	extends Player {
    /**
     * Returns the unique identifier for this player.
     */
    public UUID getUUID();

    /**
     * Returns the player's display name (nickname), which may include colors.
     */
    public String getDisplayName();

    /**
     * Sets the player's display name (nickname).
     *
     * @param newDisplayName The new display name. May include colors, amp-prefixed.
     */
    public void setDisplayName(String newDisplayName);

    /**
     * @return Returns true if the player is online, false otherwise.
     */
    public boolean isOnline();


}
