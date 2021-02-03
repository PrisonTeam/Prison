/*
 *  Prison is a Minecraft plugin for the prison game mode.
 *  Copyright (C) 2017 The Prison Team
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package tech.mcprison.prison.spigot.game;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.permissions.PermissionAttachmentInfo;

import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.inventory.Inventory;
import tech.mcprison.prison.internal.scoreboard.Scoreboard;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.spigot.commands.sellall.SellAllPrisonCommands;
import tech.mcprison.prison.spigot.inventory.SpigotPlayerInventory;
import tech.mcprison.prison.spigot.scoreboard.SpigotScoreboard;
import tech.mcprison.prison.util.Gamemode;
import tech.mcprison.prison.util.Location;

/**
 * @author Faizaan A. Datoo
 */
public class SpigotPlayer extends SpigotCommandSender implements Player {

    private org.bukkit.entity.Player bukkitPlayer;

    public SpigotPlayer(org.bukkit.entity.Player bukkitPlayer) {
        super(bukkitPlayer);
        this.bukkitPlayer = bukkitPlayer;
    }

    @Override public UUID getUUID() {
        return bukkitPlayer.getUniqueId();
    }

    @Override public String getDisplayName() {
        return bukkitPlayer.getDisplayName();
    }

    @Override public void setDisplayName(String newDisplayName) {
        bukkitPlayer.setDisplayName(newDisplayName);
    }

    @Override public void give(ItemStack itemStack) {
        bukkitPlayer.getInventory().addItem(SpigotUtil.prisonItemStackToBukkit(itemStack));
    }

    @Override public Location getLocation() {
        return SpigotUtil.bukkitLocationToPrison(bukkitPlayer.getLocation());
    }

    @Override public void teleport(Location location) {
        bukkitPlayer.teleport(SpigotUtil.prisonLocationToBukkit(location),
            PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    @Override public boolean isOnline() {
        return bukkitPlayer.isOnline();
    }

    @Override public void setScoreboard(Scoreboard scoreboard) {
        bukkitPlayer.setScoreboard(((SpigotScoreboard) scoreboard).getWrapper());
    }

    @Override public Gamemode getGamemode() {
        return Gamemode.valueOf(getWrapper().getGameMode().toString());
    }

    @Override public void setGamemode(Gamemode gamemode) {
        getWrapper().setGameMode(GameMode.valueOf(gamemode.toString()));
    }

    @Override public Optional<String> getLocale() {
        if (NmsHelper.hasSupport()) {
            try {
                return Optional.ofNullable(NmsHelper.getLocale(getWrapper()));
            } catch (IllegalAccessException | InvocationTargetException ex) {
                Output.get().logWarn("Could not get locale of player " + getName(), ex);
            }
        }
        return Optional.empty();
    }

    @Override public boolean isOp() {
        return bukkitPlayer.isOp();
    }

    public org.bukkit.entity.Player getWrapper() {
        return bukkitPlayer;
    }

    @Override 
    public Inventory getInventory() {
        return new SpigotPlayerInventory(getWrapper().getInventory());
    }

    @Override public void updateInventory() {
        bukkitPlayer.updateInventory();
    }

    @Override
    public List<String> getPermissions() {
    	List<String> results = new ArrayList<>();
    	
    	Set<PermissionAttachmentInfo> perms = bukkitPlayer.getEffectivePermissions();
    	for ( PermissionAttachmentInfo perm : perms )
		{
			results.add( perm.getPermission() );
		}
    	
    	return results;
    }
    
    @Override
    public List<String> getPermissions( String prefix ) {
    	List<String> results = new ArrayList<>();
    	
    	for ( String perm : getPermissions() ) {
			if ( perm.startsWith( prefix ) ) {
				results.add( perm );
			}
		}
    	
    	return results;
    }
    
    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	
    	sb.append( "SpigotPlayer: " ).append( getName() )
    		.append( "  isOp=" ).append( isOp() )
    		.append( "  isOnline=" ).append( isOnline() );
    	
    	return sb.toString();
    }
    
    /**
     * This class is an adaptation of the NmsHelper class in the Rosetta library by Max Roncace. The
     * library is licensed under the New BSD License. See the {@link tech.mcprison.prison.localization}
     * package for the full license.
     *
     * @author Max Roncacé
     */
    private static class NmsHelper {

        private static final boolean SUPPORT;

        private static final String PACKAGE_VERSION;

        private static final Method PLAYER_SPIGOT;
        private static final Method PLAYER$SPIGOT_GETLOCALE;
        private static final Method CRAFTPLAYER_GETHANDLE;

        private static final Field ENTITY_PLAYER_LOCALE;
        private static final Field LOCALE_LANGUAGE_WRAPPED_STRING;

        static {
            String[] array = Bukkit.getServer().getClass().getPackage().getName().split("\\.");
            PACKAGE_VERSION = array.length == 4 ? array[3] + "." : "";

            Method player_spigot = null;
            Method player$spigot_getLocale = null;
            Method craftPlayer_getHandle = null;
            Field entityPlayer_locale = null;
            Field localeLanguage_wrappedString = null;
            try {

                Class<?> craftPlayer = getCraftClass("entity.CraftPlayer");

                // for reasons not known to me Paper decided to make EntityPlayer#locale null by default and have the
                // fallback defined in CraftPlayer$Spigot#getLocale. Rosetta will use that method if possible and fall
                // back to accessing the field directly.
                try {
                    player_spigot = org.bukkit.entity.Player.class.getMethod("spigot");
                    Class<?> player$spigot = Class.forName("org.bukkit.entity.Player$Spigot");
                    player$spigot_getLocale = player$spigot.getMethod("getLocale");
                } catch (NoSuchMethodException ignored) { // we're non-Spigot or old
                }

                if (player$spigot_getLocale == null) { // fallback for non-Spigot software
                    craftPlayer_getHandle = craftPlayer.getMethod("getHandle");

                    entityPlayer_locale = getNmsClass("EntityPlayer").getDeclaredField("locale");
                    entityPlayer_locale.setAccessible(true);
                    if (entityPlayer_locale.getType().getSimpleName().equals("LocaleLanguage")) {
                        // On versions prior to 1.6, the locale is stored as a LocaleLanguage object.
                        // The actual locale string is wrapped within it.
                        // On 1.5, it's stored in field "e".
                        // On 1.3 and 1.4, it's stored in field "d".
                        try { // try for 1.5
                            localeLanguage_wrappedString =
                                entityPlayer_locale.getType().getDeclaredField("e");
                        } catch (NoSuchFieldException ex) { // we're pre-1.5
                            localeLanguage_wrappedString =
                                entityPlayer_locale.getType().getDeclaredField("d");
                        }
                    }
                }
            } catch (ClassNotFoundException | NoSuchFieldException | NoSuchMethodException ex) {
                Output.get().logError(
                    "Cannot initialize NMS components - per-player localization disabled.", ex);
            }
            PLAYER_SPIGOT = player_spigot;
            PLAYER$SPIGOT_GETLOCALE = player$spigot_getLocale;
            CRAFTPLAYER_GETHANDLE = craftPlayer_getHandle;
            ENTITY_PLAYER_LOCALE = entityPlayer_locale;
            LOCALE_LANGUAGE_WRAPPED_STRING = localeLanguage_wrappedString;
            SUPPORT = CRAFTPLAYER_GETHANDLE != null;
        }

        private static boolean hasSupport() {
            return SUPPORT;
        }

        private static String getLocale(org.bukkit.entity.Player player)
            throws IllegalAccessException, InvocationTargetException {
            if (PLAYER$SPIGOT_GETLOCALE != null) {
                return (String) PLAYER$SPIGOT_GETLOCALE.invoke(PLAYER_SPIGOT.invoke(player));
            }

            Object entityPlayer = CRAFTPLAYER_GETHANDLE.invoke(player);
            Object locale = ENTITY_PLAYER_LOCALE.get(entityPlayer);
            if (LOCALE_LANGUAGE_WRAPPED_STRING != null) {
                return (String) LOCALE_LANGUAGE_WRAPPED_STRING.get(locale);
            } else {
                return (String) locale;
            }
        }

        private static Class<?> getCraftClass(String className) throws ClassNotFoundException {
            return Class.forName("org.bukkit.craftbukkit." + PACKAGE_VERSION + className);
        }

        private static Class<?> getNmsClass(String className) throws ClassNotFoundException {
            return Class.forName("net.minecraft.server." + PACKAGE_VERSION + className);
        }

    }

    @SuppressWarnings( "deprecation" )
	public void printDebugInventoryInformationToConsole() {
    	
    	try {
    		printDebugInfo( bukkitPlayer.getInventory().getContents(), "Inventory Contents");
    	}
    	catch ( java.lang.NoSuchMethodError | Exception e ) {
    		// Ignore: Not supported with that version of spigot:
    	}
    	
    	try {
    		printDebugInfo( bukkitPlayer.getInventory().getExtraContents(), "Inventory Extra Contents");
    	}
    	catch ( java.lang.NoSuchMethodError | Exception e ) {
    		// Ignore: Not supported with that version of spigot:
    	}
        
    	try {
    		printDebugInfo( bukkitPlayer.getInventory().getArmorContents(), "Inventory Armor Contents");
    	}
    	catch ( java.lang.NoSuchMethodError | Exception e ) {
    		// Ignore: Not supported with that version of spigot:
    	}
    	try {
    		printDebugInfo( bukkitPlayer.getInventory().getStorageContents(), "Inventory Storage Contents");
    	}
    	catch ( java.lang.NoSuchMethodError | Exception e ) {
    		// Ignore: Not supported with that version of spigot:
    	}
        
    	try {
			printDebugInfo( bukkitPlayer.getInventory().getItemInHand(), "Inventory Item In Hand (pre 1.13)");
		}
		catch ( java.lang.NoSuchMethodError | Exception e ) {
			// Ignore: Not supported with that version of spigot:
		}
    	
    	try {
			printDebugInfo( bukkitPlayer.getInventory().getItemInMainHand(), "Inventory Item in Main Hand");
		}
		catch ( java.lang.NoSuchMethodError | Exception e ) {
			// Ignore: Not supported with that version of spigot:
		}
    	
    	try {
    		printDebugInfo( bukkitPlayer.getInventory().getItemInOffHand(), "Inventory Item in Off Hand");
    	}
    	catch ( java.lang.NoSuchMethodError | Exception e ) {
    		// Ignore: Not supported with that version of spigot:
    	}
    }
    
    private void printDebugInfo( org.bukkit.inventory.ItemStack[] iStacks, String title ) {
    	
    	Output.get().logInfo( "&7%s:", title );
    	for ( int i = 0; i < iStacks.length; i++ ) {
    		org.bukkit.inventory.ItemStack iStack = iStacks[i];
    		
    		if ( iStack != null ) {
    			
    			ItemStack pItemStack = SpigotUtil.bukkitItemStackToPrison(iStack);
    			
    			Output.get().logInfo( "    i=%d  &3%s  &3%d &a[&3%s&a]", 
    					i, iStack.getType().name(), iStack.getAmount(),
    					(pItemStack == null ? "" : 
    						(pItemStack.getDisplayName() == null ? "" : 
    							pItemStack.getDisplayName())) );
    		}
    	}
    }
    
    private void printDebugInfo( org.bukkit.inventory.ItemStack iStack, String title ) {
    	
    	Output.get().logInfo( "&7%s:", title );
    	if ( iStack != null ) {
    		
    		Output.get().logInfo( "    &3%s  &3%d", 
    				iStack.getType().name(), iStack.getAmount() );
    	}
    }
    
    
    /**
     * <p>This uses the sellall configs for the permission name to use to get the list of
     * multipliers.  It then adds all of the multipliers together to ...
     * 
     * </p>
     * 
     */
    @Override
    public double getSellAllMultiplier() {
    	double results = 1.0;
    	
    	SellAllPrisonCommands sellall = SellAllPrisonCommands.get();
    	
    	if ( sellall != null ) {
    		results = sellall.getMultiplier( this );
    	}
    	
    	return results;
    }
    
}
