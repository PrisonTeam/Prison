package me.faizaand.prison.internal.platform;

import me.faizaand.prison.gui.GUI;
import me.faizaand.prison.internal.GamePlayer;

/**
 * Manages all things to do with titles, action bars, and inventory GUIs.
 *
 * @since 4.0
 */
public interface GuiManager {


    /**
     * Send a title to a player
     *
     * @param player   The player that you want to send the title to
     * @param title    The text of the title
     * @param subtitle The text of the subtitle
     * @param fade     The length of the fade
     */
    void showTitle(GamePlayer player, String title, String subtitle, int fade);

    /**
     * Send an actionbar to a player
     *
     * @param player   The player that you want to send the actionbar to
     * @param text     The text of the actionbar
     * @param duration The amount of time to show the action bar, in seconds. Set to -1 for no duration (i.e. vanilla standard duration of ~3 seconds).
     */
    void showActionBar(GamePlayer player, String text, int duration);

    /**
     * Creates a new {@link GUI} to show to players.
     *
     * @param title   The title of the GUI.
     * @param numRows The number of rows in the GUI; must be divisible by 9.
     * @return The {@link GUI}, ready for use.
     */
    GUI createGUI(String title, int numRows);

}
