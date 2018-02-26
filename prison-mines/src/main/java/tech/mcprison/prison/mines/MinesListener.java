package tech.mcprison.prison.mines;

import com.google.common.eventbus.Subscribe;
import tech.mcprison.prison.selection.SelectionCompletedEvent;
import tech.mcprison.prison.util.Bounds;

/**
 * @author Faizaan A. Datoo
 */
public class MinesListener {

    @Subscribe public void onSelectionComplete(SelectionCompletedEvent e) {
        Bounds bounds = e.getSelection().asBounds();
        String dimensions = bounds.getWidth() + "x" + bounds.getHeight() + "x" + bounds.getLength();
        e.getPlayer().sendMessage("&3Ready. &7Your mine will be &8" + dimensions
            + "&7 blocks. Type /mines create to create it.");
    }

}
