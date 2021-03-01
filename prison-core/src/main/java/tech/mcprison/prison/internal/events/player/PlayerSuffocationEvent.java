package tech.mcprison.prison.internal.events.player;

import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.events.Cancelable;

public class PlayerSuffocationEvent
		implements Cancelable {

    private Player player;
    private boolean canceled = false;

    public PlayerSuffocationEvent( Player player ) {
    	this.player = player;
    }

	public Player getPlayer() {
		return player;
	}
	public void setPlayer( Player player ) {
		this.player = player;
	}

	public boolean isCanceled() {
		return canceled;
	}
	public void setCanceled( boolean canceled ) {
		this.canceled = canceled;
	}
    
}
