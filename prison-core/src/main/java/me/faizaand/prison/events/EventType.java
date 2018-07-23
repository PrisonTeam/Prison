package me.faizaand.prison.events;

import me.faizaand.prison.internal.GameItemStack;
import me.faizaand.prison.internal.GamePlayer;
import me.faizaand.prison.internal.block.Block;
import me.faizaand.prison.util.GameLocation;

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
    PlayerJoinEvent(GamePlayer.class, String.class),

    /**
     * Fires when a player places a block.
     * <ul>
     * <li><b>Block</b> gets the block that was placed</li>
     * <li><b>GamePlayer</b> gets the player who placed the block</li>
     * <li><b>GameItemStack</b> gets the item stack that was used to place the block</li>
     * <li><b>Boolean</b> true if the player can build here, false otherwise.</li>
     * </ul>
     */
    BlockPlaceEvent(Block.class, GamePlayer.class, GameItemStack.class, Boolean.class),

    /**
     * Fires when a player breaks a block. Types allowed:
     * <ul>
     * <li><b>Block</b> the block that was broken</li>
     * <li><b>GamePlayer</b> the player that broke it</li>
     * </ul>
     */
    BlockBreakEvent(Block.class, GamePlayer.class),

    /**
     * Fires when a player sends a chat message. Types allowed:
     * <ul>
     * <li><b>String</b> the message that was sent</li>
     * <li><b>String</b> the format of the message that was sent</li>
     * <li><b>GamePlayer</b> the player who sent it</li>
     * </ul>
     */
    PlayerChatEvent(String.class, String.class, GamePlayer.class),

    /**
     * Fires when a player interacts with a block. Types allowed:
     * <ul>
     * <li><b>GameItemStack</b> the item stack used to interact</li>
     * <li><b>GamePlayer</b> the player that performed the interaction</li>
     * <li><b>boolean</b> true if left-hand click, false if right-hand click</li>
     * <li><b>GameLocation</b> the location that was clicked</li>
     * </ul>
     */
    PlayerInteractBlockEvent(GameItemStack.class, GamePlayer.class, Boolean.class, GameLocation.class),

    /**
     * Fires when a sign is created or updated.
     * <ul>
     * <li><b>String[]</b> the lines of the sign.</li>
     * <li><b>GamePlayer</b> the player who changed the sign.</li>
     * <li><b>Block</b> the sign block</li>
     * </ul>
     */
    SignChangeEvent(String[].class, GamePlayer.class, Block.class);

    Class<?>[] expectedTypes;

    EventType(Class<?>... expectedTypes) {
        this.expectedTypes = expectedTypes;
    }

    public Class<?>[] getExpectedTypes() {
        return expectedTypes;
    }

}
