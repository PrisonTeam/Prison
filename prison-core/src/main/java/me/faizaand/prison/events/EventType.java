package me.faizaand.prison.events;

import me.faizaand.prison.internal.GamePlayer;
import me.faizaand.prison.internal.block.Block;

/**
 * Each event type that we can respond to.
 *
 * @since 4.0
 */
public enum EventType {

    /**
     * Fires when a player joins the server. Types allowed:
     * <ul>
     * <li><b>GamePlayer</b> the player that joined</li>
     * <li><b>String</b> the join message</li>
     * </ul>
     */
    PlayerJoinEvent(GamePlayer.class),

    /**
     * Fires when a player breaks a block. Types allowed:
     * <ul>
     * <li><b>Block</b> the block that was broken</li>
     * <li><b>GamePlayer</b> the player that broke it</li>
     * </ul>
     */
    BlockBreakEvent(Block.class, GamePlayer.class);

    Class<?>[] expectedTypes;

    EventType(Class<?>... expectedTypes) {
        this.expectedTypes = expectedTypes;
    }

    public Class<?>[] getExpectedTypes() {
        return expectedTypes;
    }

}
