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

import org.bukkit.Bukkit;
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
 * <p>This is a wrapper class that stores the bukkit player object and enables it
 * to be used with the internal Prison SpigotPlayer object. It also can store the
 * RankPlayer object too.
 * </p>
 * 
 * <p>To generate a SpigotPlayer from a RankPlayer there is a new 
 * static function that will perform the construction of the SpigotPlayer.
 * </p>
 * 
 * <p>This SpigotPlayer object is ONLY for players that are online.  This class 
 * cannot exist for offline players, of which use the SpigotOfflinePlayer object.
 * </p>
 * 
 */
public class SpigotPlayer 
				extends SpigotEntity 
//				extends SpigotCommandSender 
				implements Player, Comparable<SpigotPlayer> {

	private RankPlayer rankPlayer;
	
    private org.bukkit.entity.Player bukkitPlayer;
    
    
    private transient File filePlayer;
    private transient File fileCache;
    
    private transient String miscText;

    public SpigotPlayer(org.bukkit.entity.Player bukkitPlayer) {
        super(bukkitPlayer);
        
        this.bukkitPlayer = bukkitPlayer;
    }
    
    
    /**
     * <p>This function sets up a SpigotPlayer object using a RankPlayer object.
     * It basically joins the bukkit player to the existing RankPlayer.
     * </p>
     * 
     * 
     * <p>This will throw a SpigotPlayerException if the RankPlayer's UUID cannot be 
     * tied to a bukkit player.  This should never happen since UUIDs come from bukkit
     * with the exception of the player being removed from bukkit, but not yet from 
     * prison.
     * </p>
     * 
     * @param rankPlayer
     * @throws SpigotPlayerException 
     */
    public static SpigotPlayer getSpigotPlayer( RankPlayer rankPlayer ) {
    	
	    	SpigotPlayer result = null;
	    	
	    	// We have a RankPlayer object, but we need to connect this SpigotPlayer object 
	    	// to the bukkit player object.
	
	    	// NOTE: We're directly accessing bukkit here because it's more direct instead of 
	    	//       trying to do that through a few different function within the 
	    	//       SpigotPlatform.
	    	
	    	// First try to load a bukkit online player:
	    	org.bukkit.entity.Player bPlayer = Bukkit.getPlayer( rankPlayer.getUUID() );
	    	
	    	if ( bPlayer != null ) {
	    		result = new SpigotPlayer( bPlayer );
	    		
	    		result.setRankPlayer( rankPlayer );
	    	}
	    	
	    	return result;
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

    @Override 
    public String getDisplayName() {
        return bukkitPlayer.getDisplayName();
    }

	@Override
	public String getName() {
		
		return bukkitPlayer != null ?
				bukkitPlayer.getName() : 
					rankPlayer != null ? 
							rankPlayer.getName() : "";
	}

    
    @Override 
    public void setDisplayName(String newDisplayName) {
        bukkitPlayer.setDisplayName(newDisplayName);
    }

    @Override 
    public void give(ItemStack itemStack) {
        bukkitPlayer.getInventory().addItem(SpigotUtil.prisonItemStackToBukkit(itemStack));
    }


    @Override 
    public boolean isOnline() {
        return bukkitPlayer == null ? false : bukkitPlayer.isOnline();
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
    	
        return results;
    }
    
    @Override
    public SpigotBlock getLineOfSightBlock() {
    	
	    	SpigotBlock results = null;
	    	
	    	List<Block> blocks = bukkitPlayer.getLineOfSight( null, 256 );
	    	for ( Block block : blocks ) {
	    		if ( block != null && block.getType() != Material.AIR &&
	    				!SpigotCompatibility.getInstance().isPassable(block) ) {
	
	    			
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

    @Override 
    public void updateInventory() {
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
	

    @Override
    public long getLastSeenDate() {
    		return bukkitPlayer.getLastPlayed();
    }

    
	@Override
	public int compareTo( SpigotPlayer sPlayer) {
		return getUUID().compareTo( sPlayer.getUUID() );
	}
	
	
    @Override
    public String toString() {
	    	StringBuilder sb = new StringBuilder();
	    	
	    	sb.append( "SpigotPlayer: " ).append( getName() )
	    		.append( "  isOp=" ).append( isOp() )
	    		.append( "  isOnline=" ).append( isOnline() )
	    		.append( "  isPlayer=" ).append( isPlayer() )
	    		
	    		.append( "  hasBukkitPlayer=" ).append( bukkitPlayer != null )
	    		.append( "  hasRankPlayer=" ).append( rankPlayer != null )
	    		;
	    	
	    	return sb.toString();
    }
    
	
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

		}
	}
	
	@Override
	public RankPlayer getRankPlayer() {
		if ( rankPlayer == null && PrisonRanks.getInstance() != null &&
				PrisonRanks.getInstance().isEnabled() ) {
			
			rankPlayer = PrisonRanks.getInstance().getPlayerManager().getPlayer( this );
		}
		return rankPlayer;
	}
	
	private void setRankPlayer( RankPlayer rankPlayer ) {
		this.rankPlayer = rankPlayer;
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
		
		XMaterial xMat = XMaterial.matchXMaterial( blockName ).orElse( null );
		
		if ( xMat != null ) {
			Material mat = xMat.parseMaterial();
			
			if ( mat != null ) {
				
				getWrapper().incrementStatistic( Statistic.MINE_BLOCK, xMat.parseMaterial(), quantity );
				
			}
		}
		
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
	
	public boolean isAutoSellEnabled( StringBuilder debugInfo ) {
		boolean results = false;
		
		if ( SpigotPrison.getInstance().isSellAllEnabled() &&
				SellAllUtil.isAutoSellEnabled() ) {
			
			if ( SellAllUtil.get().isAutoSellPerUserToggleable ) {
				debugInfo.append( "(&7sellallEnabled:userToggleable&3)" );
				
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

	
	/**
	 * This miscText is not used for any specific purpose other than to hold a String 
	 * value.  It can be used to return a message from a function, but it should always
	 * be cleared when done using it.
	 * 
	 * @return
	 */
	@Override
	public String getMiscText() {
		return miscText;
	}
	@Override
	public void setMiscText( String text )  {
		miscText = text;
	}
	
}
