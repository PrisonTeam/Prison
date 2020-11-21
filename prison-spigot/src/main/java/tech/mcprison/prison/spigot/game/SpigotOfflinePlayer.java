package tech.mcprison.prison.spigot.game;

import java.util.Optional;
import java.util.UUID;

import org.bukkit.OfflinePlayer;

import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.OfflineMcPlayer;
import tech.mcprison.prison.internal.inventory.Inventory;
import tech.mcprison.prison.internal.scoreboard.Scoreboard;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.util.Gamemode;
import tech.mcprison.prison.util.Location;

public class SpigotOfflinePlayer
	implements OfflineMcPlayer {
	
	private OfflinePlayer offlinePlayer;
	
	public SpigotOfflinePlayer(OfflinePlayer offlinePlayer) {
		this.offlinePlayer = offlinePlayer;
	}

	@Override
	public String getName() {
		return offlinePlayer.getName();
	}

	@Override
	public void dispatchCommand( String command ) {
		
	}

	@Override
	public UUID getUUID() {
		return offlinePlayer.getUniqueId();
	}

	@Override
	public String getDisplayName() {
		return offlinePlayer.getName();
	}

	@Override
	public boolean isOnline() {
		return false;
	}
	
	@Override
	public boolean hasPermission( String perm ) {
		Output.get().logError( "SpigotOfflinePlayer.hasPermission: Cannot access permissions for offline players." );
		return false;
	}
	
	@Override
	public void setDisplayName( String newDisplayName ) {
		Output.get().logError( "SpigotOfflinePlayer.setDisplayName: Cannot set display names." );
	}
	
	@Override
	public void sendMessage( String message ) {
		Output.get().logError( "SpigotOfflinePlayer.sendMessage: Cannot send messages to offline players." );
	}
	
	@Override
	public void sendMessage( String[] messages ) {
		Output.get().logError( "SpigotOfflinePlayer.sendMessage: Cannot send messages to offline players." );
	}
	
	@Override
	public void sendRaw( String json ) {
		Output.get().logError( "SpigotOfflinePlayer.sendRaw: Cannot send messages to offline players." );
	}

	@Override
	public boolean doesSupportColors() {
		return false;
	}

	@Override
	public void give( ItemStack itemStack ) {
		Output.get().logError( "SpigotOfflinePlayer.give: Cannot give to offline players." );
	}

	@Override
	public Location getLocation() {
		Output.get().logError( "SpigotOfflinePlayer.getLocation: Offline players have no location." );
		return null;
	}

	@Override
	public void teleport( Location location ) {
		Output.get().logError( "SpigotOfflinePlayer.teleport: Offline players cannot be teleported." );
	}

	@Override
	public void setScoreboard( Scoreboard scoreboard ) {
		Output.get().logError( "SpigotOfflinePlayer.setScoreboard: Offline players cannot use scoreboards." );
	}

	@Override
	public Gamemode getGamemode() {
		Output.get().logError( "SpigotOfflinePlayer.getGamemode: Offline is not a valid gamemode." );
		return null;
	}

	@Override
	public void setGamemode( Gamemode gamemode ) {
	}

	@Override
	public Optional<String> getLocale() {
		Output.get().logError( "SpigotOfflinePlayer.getLocale: Offline is not a valid gamemode." );
		return null;
	}

	@Override
	public boolean isOp() {
		return false;
	}

	@Override
	public void updateInventory() {
	}

	@Override
	public Inventory getInventory() {
		return null;
	}

	@Override
	public void printDebugInventoryInformationToConsole() {
		
	}
}
