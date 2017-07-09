package tech.mcprison.prison.selection;

import tech.mcprison.prison.internal.Player;

/**
 * Fired when both selections in a {@link Selection} are made.
 * @author Faizaan A. Datoo
 * @since API 1.0
 */
public class SelectionCompletedEvent {

    private Player player;
    private Selection selection;

    public SelectionCompletedEvent(Player player, Selection selection) {
        this.player = player;
        this.selection = selection;
    }

    public Player getPlayer() {
        return player;
    }

    public Selection getSelection() {
        return selection;
    }

}
