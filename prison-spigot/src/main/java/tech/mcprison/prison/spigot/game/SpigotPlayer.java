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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.block.Block;
import org.bukkit.entity.ExperienceOrb;

import com.cryptomorin.xseries.XMaterial;

import tech.mcprison.prison.PrisonAPI;
import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
import tech.mcprison.prison.autofeatures.AutoFeaturesWrapper;
import tech.mcprison.prison.autofeatures.PlayerMessaging.MessageType;
import tech.mcprison.prison.cache.PlayerCache;
import tech.mcprison.prison.cache.PlayerCachePlayerData;
import tech.mcprison.prison.file.JsonFileIO;
import tech.mcprison.prison.integration.EconomyCurrencyIntegration;
import tech.mcprison.prison.integration.EconomyIntegration;
import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.Player;
import tech.mcprison.prison.internal.inventory.Inventory;
import tech.mcprison.prison.internal.scoreboard.Scoreboard;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.RankPlayer;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.SpigotUtil;
import tech.mcprison.prison.spigot.block.SpigotBlock;
import tech.mcprison.prison.spigot.compat.SpigotCompatibility;
import tech.mcprison.prison.spigot.game.entity.SpigotEntity;
import tech.mcprison.prison.spigot.inventory.SpigotPlayerInventory;
import tech.mcprison.prison.spigot.scoreboard.SpigotScoreboard;
import tech.mcprison.prison.spigot.sellall.SellAllUtil;
import tech.mcprison.prison.spigot.utils.tasks.PlayerMessagingTask;
import tech.mcprison.prison.util.Gamemode;
import tech.mcprison.prison.util.Location;
import tech.mcprison.prison.util.Vector;

/**
 * @author Faizaan A. Datoo
 */
public class SpigotPlayer 
				extends SpigotEntity 
//				extends SpigotCommandSender 
				implements Player, Comparable<SpigotPlayer> {

	private RankPlayer rankPlayer;
	
    private org.bukkit.entity.Player bukkitPlayer;
    
    
    private transient File filePlayer;
    private transient File fileCache;
    
   

    public SpigotPlayer(org.bukkit.entity.Player bukkitPlayer) {
        super(bukkitPlayer);
        this.bukkitPlayer = bukkitPlayer;
    }

    /**
     * <p>This constructs a player file named based upon the UUID followed 
     * by the player's name.  This format is used so it's easier to identify
     * the correct player.
     * </p>
     * 
     * <p>The format should be UUID-PlayerName.json.  The UUID is a shortened 
     * format, which should still produce a unique id.  The name, when read, 
     * is based upon the UUID and not the player's name, which may change.
     * This format includes the player's name to make it easier to identify
     * who's record is whom's.
     * </p>
     * 
     * @return
     */
    public String getPlayerFileName() {
    	
    	return filenamePlayer();
    }
    
    
	public File getFilePlayer() {
		if ( filePlayer == null ) {
			filePlayer = JsonFileIO.filePlayer( this );;
		}
		return filePlayer;
	}
	public void setFilePlayer(File filePlayer) {
		this.filePlayer = filePlayer;
	}

	public File getFileCache() {
		if ( fileCache == null ) {
			fileCache = JsonFileIO.fileCache( this );
		}
		return fileCache;
	}
	public void setFileCache(File fileCache) {
		this.fileCache = fileCache;
	}

	/**
     * <p>This is a helper function to ensure that the given file name is 
     * always generated correctly and consistently.
     * </p>
     * 
     * @return "player_" plus the least significant bits of the UID
     */
    public String filenamePlayer()
    {
    	return getFilePlayer().getName();
    }
    
    public String filenameCache()
    {
    	return getFileCache().getName();
    }
//    public String getPlayerFileName() {
//    	
//    	return JsonFileIO.filenamePlayer( this );
//    }
    
//    @Override 
//    public UUID getUUID() {
//        return bukkitPlayer.getUniqueId();
//    }
    
//    @Override
//    public String getName() {
//    	return bukkitPlayer.getName();
//    }

    @Override 
    public String getDisplayName() {
        return bukkitPlayer.getDisplayName();
    }

    @Override 
    public void setDisplayName(String newDisplayName) {
        bukkitPlayer.setDisplayName(newDisplayName);
    }

    @Override 
    public void give(ItemStack itemStack) {
        bukkitPlayer.getInventory().addItem(SpigotUtil.prisonItemStackToBukkit(itemStack));
    }

//    @Override 
//    public Location getLocation() {
//        return SpigotUtil.bukkitLocationToPrison(bukkitPlayer.getLocation());
//    }

//    @Override public boolean teleport(Location location) {
//    	
//    	
//        return bukkitPlayer.teleport(SpigotUtil.prisonLocationToBukkit(location),
//            PlayerTeleportEvent.TeleportCause.PLUGIN);
//    }

    @Override 
    public boolean isOnline() {
        return bukkitPlayer.isOnline();
    }

    @Override 
    public void setScoreboard(Scoreboard scoreboard) {
        bukkitPlayer.setScoreboard(((SpigotScoreboard) scoreboard).getWrapper());
    }

    @Override 
    public Gamemode getGamemode() {
        return Gamemode.valueOf(getWrapper().getGameMode().toString());
    }

    @Override 
    public void setGamemode(Gamemode gamemode) {
        getWrapper().setGameMode(GameMode.valueOf(gamemode.toString()));
    }

    @Override 
    public Optional<String> getLocale() {
    	Optional<String> results = Optional.empty();
    	
//    	if ( SpigotNMSPlayer.getInstance().hasSupport() ) {
//            try {
//                results = Optional.ofNullable(
//                		SpigotNMSPlayer.getInstance().getLocale( getWrapper() )
//                		);
//            } 
//            catch ( Exception ex ) {
//            	Output.get().logInfo(
//            			"Failed to initialize NMS components -- " +
//            					"NMS is not functional - " +  ex.getMessage() );
//            }
//    	}
        return results;
    }
    
    @Override
    public SpigotBlock getLineOfSightBlock() {
    	
    	SpigotBlock results = null;
    	
//    	org.bukkit.Location eyeLocation = getWrapper().getEyeLocation();
//    	org.bukkit.util.Vector lineOfSight = eyeLocation.getDirection().normalize();
//    	
//    	double maxDistance = 256;
//    	
//    	for(double i = 0; i < maxDistance; ++i){
//    	    Block block = eyeLocation.add( lineOfSight.clone().multiply(i) ).getBlock();
//    	    if( block.getType() != Material.AIR ) {
////    	    	if( block.getType().isSolid() ) {
//
//    	    	results = new SpigotBlock( block );
//    	    	break;
//    	    }
//    	    
//    	}
//    	
//    	return results;
//    	

        
    	
//    	List<tech.mcprison.prison.internal.block.Block> results = new ArrayList<>();
    	
    	List<Block> blocks = bukkitPlayer.getLineOfSight( null, 256 );
    	for ( Block block : blocks ) {
    		if ( block != null && block.getType() != Material.AIR &&
    				!block.isPassable() ) {

    			
    			// return the first non-null and non-AIR block, which will 
    			// be the one the player is looking at:
    			results = SpigotBlock.getSpigotBlock( block );
    		}
		}
    	
    	return results;
    }

    
    /**
     * <p>This uses the line of sight to get an exact location of where the player
     * is clicking.  Normally, when selecting a block, only the block's location
     * is accessible which is basically integer resolution, although the player maybe 
     * clicking somewhere in the middle of a block.  This gives a way to finely
     * select where they were looking, instead of just the block they were 
     * looking at and clicking on.
     * </p>
     * 
     * <p>Please notice that there may be issues with this code.  Review the
     * other function in this class 'getLineOfSightBlock()' in that it uses 
     * bukkit's block.isPassible().  That function does not exist in the
     * prison block and may need to be added.  Currently it's not possible to 
     * be added at this time since too many other changes are in effect within
     * prison.  This is a note and a placeholder for these possible changes.
     * </p>
     * 
     * @return
     */
    public Location getLineOfSightExactLocation() {
    	
    	SpigotLocation eyeLoc = new SpigotLocation( getWrapper().getEyeLocation() );
    	Vector eyeVec = eyeLoc.getDirection();
    	
    	Location loc = eyeLoc.add(eyeVec);
    	
    	int i = 0;
    	
    	// Is isPassible also true when isEmpty? If so, then this could be simplified...
    	// while ( i++ <= 75 && (loc.getBlockAt().isEmpty() || loc.getBlockAt().isPassible()) ) { 

    	while ( i++ <= 75 && loc.getBlockAt().isEmpty() ) {
    		
    		loc = loc.add(eyeVec);
    	}
    	
    	if ( loc.getBlockAt().isEmpty() ) {
    		loc = null;
    	}
    	
    	return loc;
    }
    
    /**
     * <p>This will return a list of blocks that are in the line of sight of the player.
     * It will initially ignore all air blocks until it hits a non-air block, then it will
     * collect a total of 20 of the next blocks.  
     * </p>
     * @return
     */
    @Override
    public List<tech.mcprison.prison.internal.block.Block> getLineOfSightBlocks() {
    	
    	List<tech.mcprison.prison.internal.block.Block> results = new ArrayList<>();
    	
    	List<Block> blocks = bukkitPlayer.getLineOfSight( null, 256 );
    	for ( Block block : blocks ) {
    		if ( block != null && 
    				(results.size() == 0 && block.getType() != Material.AIR ||
    				results.size() > 0 && results.size() < 20 )) {

    			// return the first non-null and non-AIR block, which will 
    			// be the one the player is looking at:
    			results.add( SpigotBlock.getSpigotBlock( block ) );
    		}
		}
    	
    	return results;
    }

    
    public org.bukkit.entity.Player getWrapper() {
        return bukkitPlayer;
    }

    @Override 
    public Inventory getInventory() {
        return getSpigotPlayerInventory();
    }
    public SpigotPlayerInventory getSpigotPlayerInventory() {
    	return new SpigotPlayerInventory(getWrapper().getInventory());
    }

    @Override public void updateInventory() {
        bukkitPlayer.updateInventory();
    }

	@Override
	public void recalculatePermissions() {
		bukkitPlayer.recalculatePermissions();
	}

	@Override
	public boolean isPlayer() {
		return true;
	}

//    @Override 
//    public boolean isOp() {
//        return bukkitPlayer.isOp();
//    }
    
//	@Override
//	public boolean hasPermission( String perm ) {
//		List<String> perms = getPermissions( perm );
//		return perms.contains( perm );
//	}

    
//    @Override
//    public List<String> getPermissions() {
//    	List<String> results = new ArrayList<>();
//    	
//    	Set<PermissionAttachmentInfo> perms = bukkitPlayer.getEffectivePermissions();
//    	for ( PermissionAttachmentInfo perm : perms )
//		{
//			results.add( perm.getPermission() );
//		}
//    	
//    	return results;
//    }
    
    
//    @Override
//    public List<String> getPermissions( String prefix ) {
//    	List<String> results = new ArrayList<>();
//    	
//    	for ( String perm : getPermissions() ) {
//			if ( perm.startsWith( prefix ) ) {
//				results.add( perm );
//			}
//		}
//    	
//    	return results;
//    }
    
    
//    /**
//     * <p>This uses the sellall configs for the permission name to use to get the list of
//     * multipliers.  It then adds all of the multipliers together to ...
//     * 
//     * </p>
//     * 
//     */
//    @Override
//    public double getSellAllMultiplier() {
//    	double results = 1.0;
//    	
//    	SellAllPrisonCommands sellall = SellAllPrisonCommands.get();
//    	
//    	if ( sellall != null ) {
//    		results = sellall.getMultiplier( this );
//    	}
//    	
//    	return results;
//    }
    
	@Override
	public int compareTo( SpigotPlayer sPlayer) {
		return getUUID().compareTo( sPlayer.getUUID() );
//		return getName().compareTo( sPlayer.getName() );
	}
	
	
    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	
    	sb.append( "SpigotPlayer: " ).append( getName() )
    		.append( "  isOp=" ).append( isOp() )
    		.append( "  isOnline=" ).append( isOnline() )
    		.append( "  isPlayer=" ).append( isPlayer() );
    	
    	return sb.toString();
    }

	
//    /**
//     * This class is an adaptation of the NmsHelper class in the Rosetta library by Max Roncace. The
//     * library is licensed under the New BSD License. See the {@link tech.mcprison.prison.localization}
//     * package for the full license.
//     *
//     * @author Max Roncacé
//     */
//    private static class NmsHelper {
//
//        private static final boolean SUPPORT;
//
//        private static final String PACKAGE_VERSION;
//
//        private static final Method PLAYER_SPIGOT;
//        private static final Method PLAYER$SPIGOT_GETLOCALE;
//        private static final Method CRAFTPLAYER_GETHANDLE;
//
//        private static final Field ENTITY_PLAYER_LOCALE;
//        private static final Field LOCALE_LANGUAGE_WRAPPED_STRING;
//
//        static {
//            String[] array = Bukkit.getServer().getClass().getPackage().getName().split("\\.");
//            PACKAGE_VERSION = array.length == 4 ? array[3] + "." : "";
//
//            Method player_spigot = null;
//            Method player$spigot_getLocale = null;
//            Method craftPlayer_getHandle = null;
//            Field entityPlayer_locale = null;
//            Field localeLanguage_wrappedString = null;
//            try {
//
//                Class<?> craftPlayer = getCraftClass("entity.CraftPlayer");
//
//                // for reasons not known to me Paper decided to make EntityPlayer#locale null by default and have the
//                // fallback defined in CraftPlayer$Spigot#getLocale. Rosetta will use that method if possible and fall
//                // back to accessing the field directly.
//                try {
//                    player_spigot = org.bukkit.entity.Player.class.getMethod("spigot");
//                    Class<?> player$spigot = Class.forName("org.bukkit.entity.Player$Spigot");
//                    player$spigot_getLocale = player$spigot.getMethod("getLocale");
//                } catch (NoSuchMethodException ignored) { // we're non-Spigot or old
//                }
//
//                if (player$spigot_getLocale == null) { // fallback for non-Spigot software
//                    craftPlayer_getHandle = craftPlayer.getMethod("getHandle");
//
//                    entityPlayer_locale = getNmsClass("EntityPlayer").getDeclaredField("locale");
//                    entityPlayer_locale.setAccessible(true);
//                    if (entityPlayer_locale.getType().getSimpleName().equals("LocaleLanguage")) {
//                        // On versions prior to 1.6, the locale is stored as a LocaleLanguage object.
//                        // The actual locale string is wrapped within it.
//                        // On 1.5, it's stored in field "e".
//                        // On 1.3 and 1.4, it's stored in field "d".
//                        try { // try for 1.5
//                            localeLanguage_wrappedString =
//                                entityPlayer_locale.getType().getDeclaredField("e");
//                        } catch (NoSuchFieldException ex) { // we're pre-1.5
//                            localeLanguage_wrappedString =
//                                entityPlayer_locale.getType().getDeclaredField("d");
//                        }
//                    }
//                }
//            } 
//            catch ( ClassNotFoundException ex ) {
//            	Output.get().logInfo(
//            			"Cannot initialize NMS components - ClassNotFoundException - " +
//            			"NMS is not functional - " +  ex.getMessage() );
//            	
//            }
//            catch (NoSuchFieldException | NoSuchMethodException ex) {
//                Output.get().logInfo(
//                    "Cannot initialize NMS components - per-player localization disabled. - " + ex.getMessage());
//            }
//            PLAYER_SPIGOT = player_spigot;
//            PLAYER$SPIGOT_GETLOCALE = player$spigot_getLocale;
//            CRAFTPLAYER_GETHANDLE = craftPlayer_getHandle;
//            ENTITY_PLAYER_LOCALE = entityPlayer_locale;
//            LOCALE_LANGUAGE_WRAPPED_STRING = localeLanguage_wrappedString;
//            SUPPORT = CRAFTPLAYER_GETHANDLE != null;
//        }
//
//        private static boolean hasSupport() {
//            return SUPPORT;
//        }
//
//        private static String getLocale(org.bukkit.entity.Player player)
//            throws IllegalAccessException, InvocationTargetException, ClassNotFoundException {
//            if (PLAYER$SPIGOT_GETLOCALE != null) {
//                return (String) PLAYER$SPIGOT_GETLOCALE.invoke(PLAYER_SPIGOT.invoke(player));
//            }
//
//            Object entityPlayer = CRAFTPLAYER_GETHANDLE.invoke(player);
//            Object locale = ENTITY_PLAYER_LOCALE.get(entityPlayer);
//            if (LOCALE_LANGUAGE_WRAPPED_STRING != null) {
//                return (String) LOCALE_LANGUAGE_WRAPPED_STRING.get(locale);
//            } else {
//                return (String) locale;
//            }
//        }
//
//        private static Class<?> getCraftClass(String className) throws ClassNotFoundException {
//            return Class.forName("org.bukkit.craftbukkit." + PACKAGE_VERSION + className);
//        }
//
//        private static Class<?> getNmsClass(String className) throws ClassNotFoundException {
//            return Class.forName("net.minecraft.server." + PACKAGE_VERSION + className);
//        }
//
//    }

//    @SuppressWarnings( "deprecation" )
//	public void printDebugInventoryInformationToConsole() {
//    	
//    	try {
//    		printDebugInfo( bukkitPlayer.getInventory().getContents(), "Inventory Contents");
//    	}
//    	catch ( java.lang.NoSuchMethodError | Exception e ) {
//    		// Ignore: Not supported with that version of spigot:
//    	}
//    	
//    	try {
//    		printDebugInfo( bukkitPlayer.getInventory().getExtraContents(), "Inventory Extra Contents");
//    	}
//    	catch ( java.lang.NoSuchMethodError | Exception e ) {
//    		// Ignore: Not supported with that version of spigot:
//    	}
//        
//    	try {
//    		printDebugInfo( bukkitPlayer.getInventory().getArmorContents(), "Inventory Armor Contents");
//    	}
//    	catch ( java.lang.NoSuchMethodError | Exception e ) {
//    		// Ignore: Not supported with that version of spigot:
//    	}
//    	try {
//    		printDebugInfo( bukkitPlayer.getInventory().getStorageContents(), "Inventory Storage Contents");
//    	}
//    	catch ( java.lang.NoSuchMethodError | Exception e ) {
//    		// Ignore: Not supported with that version of spigot:
//    	}
//        
//    	try {
//			printDebugInfo( bukkitPlayer.getInventory().getItemInHand(), "Inventory Item In Hand (pre 1.13)");
//		}
//		catch ( java.lang.NoSuchMethodError | Exception e ) {
//			// Ignore: Not supported with that version of spigot:
//		}
//    	
//    	try {
//			printDebugInfo( bukkitPlayer.getInventory().getItemInMainHand(), "Inventory Item in Main Hand");
//		}
//		catch ( java.lang.NoSuchMethodError | Exception e ) {
//			// Ignore: Not supported with that version of spigot:
//		}
//    	
//    	try {
//    		printDebugInfo( bukkitPlayer.getInventory().getItemInOffHand(), "Inventory Item in Off Hand");
//    	}
//    	catch ( java.lang.NoSuchMethodError | Exception e ) {
//    		// Ignore: Not supported with that version of spigot:
//    	}
//    }
    
//    private void printDebugInfo( org.bukkit.inventory.ItemStack[] iStacks, String title ) {
//    	
//    	Output.get().logInfo( "&7%s:", title );
//    	for ( int i = 0; i < iStacks.length; i++ ) {
//    		org.bukkit.inventory.ItemStack iStack = iStacks[i];
//    		
//    		if ( iStack != null ) {
//    			
//    			ItemStack pItemStack = SpigotUtil.bukkitItemStackToPrison(iStack);
//    			
//    			Output.get().logInfo( "    i=%d  &3%s  &3%d &a[&3%s&a]", 
//    					i, iStack.getType().name(), iStack.getAmount(),
//    					(pItemStack == null ? "" : 
//    						(pItemStack.getDisplayName() == null ? "" : 
//    							pItemStack.getDisplayName())) );
//    		}
//    	}
//    }
    
//    private void printDebugInfo( org.bukkit.inventory.ItemStack iStack, String title ) {
//    	
//    	Output.get().logInfo( "&7%s:", title );
//    	if ( iStack != null ) {
//    		
//    		Output.get().logInfo( "    &3%s  &3%d", 
//    				iStack.getType().name(), iStack.getAmount() );
//    	}
//    }

	public void giveExp( int xp )
	{
		if ( getWrapper() != null ) {
			getWrapper().giveExp( xp );
		}
	}

	public void dropXPOrbs( int xp ) {

		if ( getWrapper() != null ) {
			
			Location dropPoint = getLocation().add( getLocation().getDirection());
			org.bukkit.Location bukkitLocation = new org.bukkit.Location( getWrapper().getWorld(), 
									dropPoint.getX(), dropPoint.getY(), dropPoint.getZ() );
			
			((ExperienceOrb) getWrapper().getWorld().spawn(bukkitLocation, ExperienceOrb.class)).setExperience(xp);
		}
	}
	
	public double getMaxHealth() {
		double maxHealth = 0;
		if ( getWrapper() != null ) {
			
			maxHealth = SpigotCompatibility.getInstance()
								.getMaxHealth( getWrapper() );
		}
		return maxHealth;
	}
	public void setMaxHealth( double maxHealth ) {
		if ( getWrapper() != null ) {
			SpigotCompatibility.getInstance()
								.setMaxHealth( getWrapper(), maxHealth );
		}
	}
	
	public int getMaximumAir() {
		int results = 0;
		
		if ( getWrapper() != null ) {
			results = getWrapper().getMaximumAir();
		}
		
		return results;
	}
	
	public int getRemainingAir() {
		int results = 0;
		
		if ( getWrapper() != null ) {
			results = getWrapper().getRemainingAir();
		}
		
		return results;
	}
	
	public int getFoodLevel() {
		int results = 0;
		
		if ( getWrapper() != null ) {
			results = getWrapper().getFoodLevel();
		}
		
		return results;
	}
	
	public double getFoodExhaustion() {
		double results = 0;
		
		if ( getWrapper() != null ) {
			results = getWrapper().getExhaustion();
		}
		
		return results;
	}
	
	/**
 	 * <p>This increments the player's exhaustion level each time they
 	 * break a block.  The exhaustion level should increase only by 
 	 * 0.005 per block.</p>
 	 * 
 	 * <p>Since the player is swinging the pickaxe, the hunger should only apply 
 	 * when they break a target block that they actually hit, not all of the
 	 * blocks that are the product of an enchantment, or an explosion.
 	 * </p>
 	 * 
 	 * https://minecraft.fandom.com/wiki/Hunger
	 */
	public void incrementFoodExhaustionBlockBreak() {
		float exhaustion = getWrapper().getExhaustion();
		getWrapper().setExhaustion( exhaustion + 0.005f );
	}
	
	public double getFoodSaturation() {
		double results = 0;
		if ( getWrapper() != null ) {
			results = getWrapper().getSaturation();
		}
		return results;
	}
	
	public double getExp() {
		double results = 0;
		
		if ( getWrapper() != null ) {
			results = getWrapper().getExp();
		}
		
		return results;
	}
	
	public int getLevel() {
		int results = 0;
		
		if ( getWrapper() != null ) {
			results = getWrapper().getLevel();
		}
		
		return results;
	}
	
	
	public double getWalkSpeed() {
		double results = 0;
		
		if ( getWrapper() != null ) {
			results = getWrapper().getWalkSpeed();
		}
		
		return results;
	}
	
	@Override
	public void setTitle( String title, String subtitle, int fadeIn, int stay, int fadeOut ) {
		if ( getWrapper() != null) {
			SpigotCompatibility.getInstance()
					.sendTitle( getWrapper(), title, subtitle, fadeIn, stay, fadeOut );
		}
	}
	
	@Override
	public void setActionBar( String actionBar ) {
		if ( getWrapper() != null) {
			PlayerMessagingTask.submitTask( getWrapper(), MessageType.actionBar, actionBar );

//			SpigotCompatibility.getInstance()
//					.sendActionBar( getWrapper(), actionBar );
		}
	}
	
	@Override
	public RankPlayer getRankPlayer() {
		if ( rankPlayer == null && 
				PrisonRanks.getInstance().isEnabled() ) {
			
			rankPlayer = PrisonRanks.getInstance().getPlayerManager().getPlayer( this );
		}
		return rankPlayer;
	}
	
	@Override
	public PlayerCache getPlayerCache() {
		return PlayerCache.getInstance();
	}
	
	@Override
	public PlayerCachePlayerData getPlayerCachePlayerData() {
		return PlayerCache.getInstance().getOnlinePlayer( this );
	}
	
	
	/**
	 * <p>Based upon the RankPlayer's addBalance, except that this does not cache any
	 * of the transactions, so it could possibly lead to lag due to the economy plugin
	 * not have good performance on many rapid payments.
	 * </p>
	 * 
	 * @param currency
	 * @param amount
	 * @return
	 */
	public boolean addBalance( String currency, double amount ) {
		boolean results = false;
		
		if ( currency == null || currency.trim().isEmpty() || "default".equalsIgnoreCase( currency ) ) {
			// No currency specified, so use the default currency:
			
			EconomyIntegration economy = PrisonAPI.getIntegrationManager().getEconomy();
			
			if ( economy != null ) {
				results = economy.addBalance( this, amount );
			}
			
		}
		else {
			EconomyCurrencyIntegration currencyEcon = PrisonAPI.getIntegrationManager()
					.getEconomyForCurrency(currency );
			
			if ( currencyEcon != null ) {
				results = currencyEcon.addBalance( this, amount, currency );
//				addCachedRankPlayerBalance( currency, amount );
			}
		}
		return results;
	}
	
	public boolean enableFlying( Mine mine, float flightSpeed ) {
		boolean enabled = false;
		
		if ( mine.isInMineExact( getLocation() ) ) {
			// Within mine:
			
			// save the current mine reference in the player's object:
//			this.lastEffectsMine = mine;
			
			if ( getWrapper() != null ) {
				org.bukkit.entity.Player bukkitPlayer = getWrapper();
				
				bukkitPlayer.setAllowFlight( true );
				bukkitPlayer.setFlySpeed( flightSpeed );
				enabled = true;
			}
		}
		
		return enabled;
	}
	
//	public Mine getEffectsMine() {
//		Mine effectsMine = null;
//		
//		if ( lastEffectsMine != null ) {
//			
//			if ( !lastEffectsMine.isInMineExact( getLocation() ) ) {
//				lastEffectsMine = null;
//				
//				// cancel all effects for player
//			}
//			effectsMine = lastEffectsMine;
//		}
//		return effectsMine;
//	}
	
	public boolean isFlying() {
		boolean flying = false;
		
		if ( getWrapper() != null ) {
			org.bukkit.entity.Player bukkitPlayer = getWrapper();
			
			flying = bukkitPlayer.isFlying();
		}
		return flying;
	}
	
	@Override
	public boolean isSneaking() {
		boolean sneaking = false;
		
		if ( getWrapper() != null ) {
			org.bukkit.entity.Player bukkitPlayer = getWrapper();
			
			sneaking = bukkitPlayer.isSneaking();
		}
		return sneaking;
	}
	
	
	@Override
	public boolean isMinecraftStatisticsEnabled() {
		return AutoFeaturesWrapper.getInstance().isBoolean( AutoFeatures.isMinecraftStatsReportingEnabled );
	}

	@Override
	public void incrementMinecraftStatsMineBlock( Player player, String blockName, int quantity) {
		
//		Statistic.BREAK_ITEM;
//		Statistic.DROP_COUNT;
//		Statistic.MINE_BLOCK;
//		Statistic.PICKUP;
		
		XMaterial xMat = XMaterial.matchXMaterial( blockName ).orElse( null );
//		XMaterial xMat = SpigotCompatibility.getInstance().getXMaterial( block );
		
		if ( xMat != null ) {
			Material mat = xMat.parseMaterial();
			
			if ( mat != null ) {
				
				getWrapper().incrementStatistic( Statistic.MINE_BLOCK, xMat.parseMaterial(), quantity );
				
			}
		}
		
//		Statistic.MINE_BLOCK;
//		player.setStatistic(null, count);
//		player.incrementStatistic(null, null);
//		player.incrementStatistic(null, null, count);
//		player.statistic
		
	}
	
	@Override
	public void incrementMinecraftStatsDropCount( Player player, String blockName, int quantity) {
		
		XMaterial xMat = XMaterial.matchXMaterial( blockName ).orElse( null );
		
		if ( xMat != null ) {
			Material mat = xMat.parseMaterial();
			
			if ( mat != null ) {
				
				getWrapper().incrementStatistic( Statistic.DROP_COUNT, xMat.parseMaterial(), quantity );
				
			}
		}
		
	}
	
	
	public boolean isInventoryFull() {
		boolean results = false;
		
		if ( getWrapper() != null ) {
			org.bukkit.entity.Player bukkitPlayer = getWrapper();
			
			int firstEmpty = bukkitPlayer.getInventory().firstEmpty();
			
			results = (firstEmpty == -1);
		}
		
		return results;
	}

	/**
	 * <p>This function will identify if the player is able to have 
	 * autosell enabled for them.  This will take in to consideration 
	 * global settings, and also if they have toggled off their 
	 * autosell capabilities ('/sellall autoSellToggle`).
	 * </p>
	 * 
	 * <p>Please note that for sellall, autosell happens upon a 
	 * full inventory event, and only when sellall detects it, which
	 * it may not alway detect it as soon as it happens.
	 * </p>y
	 * 
	 * <p>Autosell in auto features behaves differently, it sells all 
	 * blocks that are mined, before they even enter the player's 
	 * inventory.  This is also a performance boost since the player's
	 * inventory does not have to be accessed or manipulated.  
	 * As such, auto feature's autsell only is applied within the 
	 * block handling of auto feature's code, it should never be applied
	 * outside of auto features, and as such, should not be included in 
	 * the processing of this function.
	 * </p>
	 * 
	 * @return
	 */
//	public boolean isAutoSellEnabled() {
//		return isAutoSellEnabled( null );
//	}
	
	public boolean isAutoSellEnabled( StringBuilder debugInfo ) {
		boolean results = false;
		
		if ( SpigotPrison.getInstance().isSellAllEnabled() &&
				SellAllUtil.get().isAutoSellEnabled ) {
			
			if ( SellAllUtil.get().isAutoSellPerUserToggleable ) {
				debugInfo.append( "(&7sellallEnabled:userToggleable&3)" );
				
//			boolean isAutoSellPerUserToggleable = SellAllUtil.get().isAutoSellPerUserToggleable;
				
				boolean isPlayerAutoSellTurnedOn = 
						SellAllUtil.get().isSellallPlayerUserToggleEnabled( getWrapper() );
				
				if ( debugInfo != null ) {
					debugInfo.append( "(&7autosellPlayerToggled&3: " )
							 .append( Output.get().getColorCodeWarning() )
							 .append( isPlayerAutoSellTurnedOn ? "enabled" : 
									Output.get().getColorCodeError() + "disabled:" + Output.get().getColorCodeDebug() )
							 .append( Output.get().getColorCodeDebug() )
							 .append( ")");
				}
				
				// This will return true (allow autosell) unless players can toggle autosell and they turned it off:
				// This is to be used with other auto sell setting, but never on it's own:
				results = isPlayerAutoSellTurnedOn;
				
				
				// if autosell is enabled, then need to check to see if perms permit
				// it to remain enabled... if not, then set results to false:
				if ( results ) {
					results = checkAutoSellTogglePerms( debugInfo );
				}
				
			}
			else {
				debugInfo.append( "(autosell " )
	  					 .append( Output.get().getColorCodeWarning() )
						 .append( "Enabled" )
						 .append( Output.get().getColorCodeDebug() )
						 .append( ")" );
				results = true;
			}
			
		}
		else {
			debugInfo.append( "(autosell " )
						.append( Output.get().getColorCodeWarning() )
						.append( "Disabled" )
						.append( Output.get().getColorCodeDebug() )
						.append( ")" );
		}
		
		return results;
	}
	
	
//	/**
//	 * <p>This will check to see if the player has the perms enabled
//	 * for autosell.  
//	 * </p
//	 * 
//	 * <p>If the function 'isAutoSellEnabled()' has already
//	 * been called, you can also pass that in as a parameter so it does
//	 * not have to be recalculated.
//	 * </p>
//	 * 
//	 * @return
//	 */
//	public boolean isAutoSellByPermEnabled( StringBuilder debugInfo ) {
//		return isAutoSellByPermEnabled( isAutoSellEnabled( debugInfo ), debugInfo );
//	}

	/**
	 * <p>This will check to see if the player has the perms enabled
	 * for autosell.  
	 * </p>
	 * 
	 * <p>If the function 'isAutoSellEnabled()' has already
	 * been called, you can also pass that in as a parameter so it does
	 * not have to be recalculated.
	 * </p>
	 * 
	 * @param isPlayerAutosellEnabled
	 * @return
	 */
	@SuppressWarnings("unused")
	private boolean isAutoSellByPermEnabledAutoFeatures( StringBuilder debugInfo ) {
		
		boolean autoSellByPerm = true;
		
		AutoFeaturesWrapper afw = AutoFeaturesWrapper.getInstance();
		
		if ( afw.isBoolean(AutoFeatures.isAutoSellPerBlockBreakEnabled) ) {
			
			String perm = afw.getMessage( AutoFeatures.permissionAutoSellPerBlockBreakEnabled );

			if ( !"disable".equalsIgnoreCase( perm ) &&
					!"false".equalsIgnoreCase( perm ) ) {
				
				debugInfo.append( "(&7autosellAutoFeaturesByPerm&3: " )
				.append( Output.get().getColorCodeWarning() )
				;
				
				if ( isOp() ) {
					debugInfo.append( 
							Output.get().getColorCodeError() + "Op-Disabled" + Output.get().getColorCodeDebug()
							);
					autoSellByPerm = false;
				}
				else {
					
					autoSellByPerm = hasPermission( perm );
					
					debugInfo.append( autoSellByPerm ? "hasPerm" : "noPerm" );
				}
				
				debugInfo.append( Output.get().getColorCodeDebug() )
						.append( ")" );
			}
		}

		return autoSellByPerm;
	}
	
	public boolean checkAutoSellPermsAutoFeatures() {
		boolean results = false;
		
		AutoFeaturesWrapper afw = AutoFeaturesWrapper.getInstance();
		if ( afw.isBoolean(AutoFeatures.isAutoSellPerBlockBreakEnabled) ) {
			
			String perm = afw.getMessage( AutoFeatures.permissionAutoSellPerBlockBreakEnabled );

			if ( !"disable".equalsIgnoreCase( perm ) &&
					!"false".equalsIgnoreCase( perm ) ) {
				if ( isOp() ) {
					results = false;
				}
				else {
					
					results = hasPermission( perm );
					
				}
			}
		}
		
		return results;
	}
	
	/**
	 * <p>This is using the sellall perms check to see if the player has 
	 * the perms to use the player toggle.
	 * </p>
	 * 
	 * @return
	 */
	public boolean checkAutoSellTogglePerms( StringBuilder debugInfo ) {
		boolean results = true;
		
		if ( SellAllUtil.get().isAutoSellPerUserToggleablePermEnabled ) {
			
			debugInfo.append( "(&7autosellToggleByPerm&3: " )
				.append( Output.get().getColorCodeWarning() )
				;
			
			String perm = SellAllUtil.get().permissionAutoSellPerUserToggleable;
			
			if ( !"disable".equalsIgnoreCase( perm ) &&
					!"false".equalsIgnoreCase( perm ) ) {
				if ( isOp() ) {
					
					debugInfo.append( 
							Output.get().getColorCodeError() + "Op-Disabled:" + Output.get().getColorCodeDebug()
							 );
					
					results = false;
				}
				else {
					
					results = hasPermission( perm );
					
					debugInfo.append( results ? "hasPerm" : 
						Output.get().getColorCodeError() + "noPerm" + Output.get().getColorCodeDebug() );
				}
			}
			
			debugInfo.append( Output.get().getColorCodeDebug() )
					 .append( ")" );
		}
		
		return results;
	}


	
}
