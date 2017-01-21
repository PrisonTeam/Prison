package tech.mcprison.prison.gui;

import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.util.BlockType;

import java.util.List;

public class ClickedButton {
    Button originalButton;
    GUI clickedGUI;
    Player clickingPlayer;

    public ClickedButton(Button btn, GUI gui, Player player) {
        originalButton = btn;
        clickedGUI = gui;
        clickingPlayer = player;
    }

    public BlockType getItem() {
        return originalButton.getItem();
    }

    public Action getAction() {
        return originalButton.getAction();
    }

    public String getName() {
        return originalButton.getName();
    }

    public List<String> getLore() {
        return originalButton.getLore();
    }

    public void addLore(String lore) {
        this.getLore().add(lore);
    }

    public boolean isCloseOnClick() {
        return originalButton.isCloseOnClick();
    }

    public GUI getClickedGUI() {
        return clickedGUI;
    }

    public Player getPlayer() {
        return clickingPlayer;
    }
}
