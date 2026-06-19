package tech.mcprison.prison.spigot.game;

public class SpigotPlayerException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public SpigotPlayerException() {
		super( "Cannot load the bukkit org.bukkit.entity.Player." );
	}
	
	public SpigotPlayerException( String msg ) {
		super( msg );
	}
	
}
