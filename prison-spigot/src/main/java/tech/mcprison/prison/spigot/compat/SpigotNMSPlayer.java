package tech.mcprison.prison.spigot.compat;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;

import tech.mcprison.prison.output.Output;

/**
 * <p>
 * This player NMS has been used to get the player's requested selected
 * language. This works from Spigot 1.8 (and earlier) through Spigot 1.16.5, 
 * but fails to work with 1.17.
 * </p>
 * 
 * <p>
 * There is no known replacements for Spigot 1.17.
 * </p>
 *
 * <p>This class is currently non-functional for Spigot 1.17.
 * </p>
 * 
 * <p>This class is an adaptation of the NmsHelper class in the Rosetta library
 * by Max Roncace. The library is licensed under the New BSD License. See
 * the {@link tech.mcprison.prison.localization} package for the full
 * license.
 * </p>
 * 
 */
public class SpigotNMSPlayer
{

	private static SpigotNMSPlayer nmsPlayer;
	
	private boolean supported = false;
	
	private String packageVersion;
	
	private Method playerSpigot;
	private Method playerSpigotGetLocale;
	private Method craftPlayerGetHandle;
	
	private Field entityPlayerLocale;
	private Field localLanguageWrappedString;

	private SpigotNMSPlayer() {
		super();
		
		initialize();
	}

	public static SpigotNMSPlayer getInstance() {
		if ( nmsPlayer == null ) {
			synchronized( SpigotNMSPlayer.class ) {
				if ( nmsPlayer == null ) {
					
					nmsPlayer = new SpigotNMSPlayer();
				}
			}
		}
		return nmsPlayer;
	}
	
	private void initialize() {
		
		String[] array = Bukkit.getServer().getClass().getPackage().getName().split( "\\." );
		packageVersion = array.length == 4 ? array[3] + "." : "";
		
		try {
			
			Class<?> craftPlayer = getCraftClass( "entity.CraftPlayer" );
			
			// for reasons not known to me Paper decided to make
			// EntityPlayer#locale null by default and have the
			// fallback defined in CraftPlayer$Spigot#getLocale. Rosetta
			// will use that method if possible and fall
			// back to accessing the field directly.
			try {
				playerSpigot = org.bukkit.entity.Player.class.getMethod( "spigot" );
				Class<?> player$spigot = Class.forName( "org.bukkit.entity.Player$Spigot" );
				playerSpigotGetLocale = player$spigot.getMethod( "getLocale" );
			}
			catch ( NoSuchMethodException ignored ) { 
				// we're non-Spigot or old
			}
			
			if ( playerSpigotGetLocale == null ) {
				
				// fallback for non-Spigot software
				craftPlayerGetHandle = craftPlayer.getMethod( "getHandle" );
				
				entityPlayerLocale = getNmsClass( "EntityPlayer" ).getDeclaredField( "locale" );
				entityPlayerLocale.setAccessible( true );
				
				if ( entityPlayerLocale.getType().getSimpleName().equals( "LocaleLanguage" ) ) {
					
					// On versions prior to 1.6, the locale is stored as a
					// LocaleLanguage object.
					// The actual locale string is wrapped within it.
					// On 1.5, it's stored in field "e".
					// On 1.3 and 1.4, it's stored in field "d".
					try
					{ // try for 1.5
						localLanguageWrappedString = entityPlayerLocale.getType().getDeclaredField( "e" );
					}
					catch ( NoSuchFieldException ex )
					{ // we're pre-1.5
						localLanguageWrappedString = entityPlayerLocale.getType().getDeclaredField( "d" );
					}
				}
			}
		}
		catch ( ClassNotFoundException ex )
		{
			Output.get().logInfo( "Cannot initialize NMS components - ClassNotFoundException - " + "NMS is not functional - " +
					ex.getMessage() );
			
		}
		catch ( NoSuchFieldException | NoSuchMethodException ex )
		{
			Output.get().logInfo( "Cannot initialize NMS components - per-player localization disabled. - " + ex.getMessage() );
		}
		
		supported = craftPlayerGetHandle != null;
	}
	
	public boolean hasSupport() {
		return supported;
	}
	
	public String getLocale( org.bukkit.entity.Player player ) {
		
		String results = null;
		try {
			
			if ( playerSpigotGetLocale != null ) {
				results = (String) playerSpigotGetLocale.invoke( playerSpigot.invoke( player ) );
			}
			else {
				
				Object entityPlayer = craftPlayerGetHandle.invoke( player );
				Object locale = entityPlayerLocale.get( entityPlayer );
				if ( localLanguageWrappedString != null ) {
					results = (String) localLanguageWrappedString.get( locale );
				}
				else {
					results = (String) locale;
				}
			}
		}
        catch (IllegalAccessException | InvocationTargetException ex) {
        	supported = false;
            
        	Output.get().logInfo("Could not get locale of player " + player.getName(), ex);
        }
		catch ( Exception ex ) {
			supported = false;

			Output.get().logInfo(
					"Cannot initialize NMS components -- " +
							"NMS is not functional - " +  ex.getMessage() );
		}
		return results;
	}
	
	private Class<?> getCraftClass( String className )
			throws ClassNotFoundException
	{
		return Class.forName( "org.bukkit.craftbukkit." + packageVersion + className );
	}
	
	private Class<?> getNmsClass( String className )
			throws ClassNotFoundException
	{
		return Class.forName( "net.minecraft.server." + packageVersion + className );
	}

}
