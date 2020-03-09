package tech.mcprison.prison.internal;

import java.util.UUID;

public interface OfflineMcPlayer
	extends Player {
    /**
     * Returns the unique identifier for this player.
     */
    UUID getUUID();

    /**
     * Returns the player's display name (nickname), which may include colors.
     */
    String getDisplayName();

    /**
     * Sets the player's display name (nickname).
     *
     * @param newDisplayName The new display name. May include colors, amp-prefixed.
     */
    void setDisplayName(String newDisplayName);

    /**
     * @return Returns true if the player is online, false otherwise.
     */
    boolean isOnline();
    
}
