package tech.mcprison.prison.spigot.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.mines.data.PrisonSortableResults;
import tech.mcprison.prison.mines.managers.MineManager;
import tech.mcprison.prison.mines.managers.MineManager.MineSortOrder;
import tech.mcprison.prison.modules.Module;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.ranks.PrisonRanks;
import tech.mcprison.prison.ranks.data.Rank;
import tech.mcprison.prison.ranks.data.RankPlayer;
import tech.mcprison.prison.ranks.managers.PlayerManager;
import tech.mcprison.prison.ranks.managers.RankManager;
import tech.mcprison.prison.selection.Selection;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.backpacks.BackpacksUtil;
import tech.mcprison.prison.spigot.block.SpigotBlock;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.game.SpigotWorld;
import tech.mcprison.prison.spigot.sellall.SellAllUtil;
import tech.mcprison.prison.util.Location;

/**
 * <p>These are some api end points to help access some core components within prison.
 * </p>
 * 
 * <p>Use of these are at your own risk.  Misuse can result in corruption of Prison's
 * internal data.
 * </p>
 *
 */
public class PrisonSpigotAPI {
	
	private PrisonMines prisonMineManager;
	private boolean mineModuleDisabled = false;
	private SellAllUtil sellAll;
	

	/**
	 * <p>This returns all mines that are within prison.
	 * </p>
	 * 
	 * @return results - List of Mines
	 */
	public List<Mine> getMines() {
		List<Mine> results = new ArrayList<>();
		
    	if ( PrisonMines.getInstance() != null && PrisonMines.getInstance().isEnabled() ) {
    		MineManager mm = PrisonMines.getInstance().getMineManager();

    		results = mm.getMines();
    	}
		
		return results;
	}
	
	/**
	 * <p>Returns all mines within prison, but sorted by the specified sort order.
	 * Because some sort types omit mines, there are two different collections within the
	 * PrisonSortableResults.  There is an include and exclude list.
	 * </p>
	 * 
	 * <p>All sort types that omit mines from the result type has a counter sort type
	 * that will include all mines and will not omit any.  Those begin with an "x".
	 * </p>
	 * 
	 * @param sortOrder - MineSortOrder
	 * @return results - PrisonSortableResults
	 */
	public PrisonSortableResults getMines( MineSortOrder sortOrder ) {
		PrisonSortableResults results = null;
		
    	if ( PrisonMines.getInstance() != null && PrisonMines.getInstance().isEnabled() ) {
    		MineManager mm = PrisonMines.getInstance().getMineManager();

    		results = mm.getMines(sortOrder);
    	}
		
		return results;
	}
	
	
	public RankPlayer getRankPlayer( Player bukkitPlayer ) {
		RankPlayer results = null;
		
		if ( PrisonRanks.getInstance() != null && PrisonRanks.getInstance().isEnabled() ) {
			
			PlayerManager pm = PrisonRanks.getInstance().getPlayerManager();
			
			results = pm.getPlayer( bukkitPlayer.getUniqueId(), bukkitPlayer.getName() );
		}
		
		return results;
	}

	
	/**
	 * <p>This returns a list of all ranks.
	 * </p>
	 * 
	 * @return
	 */
	public List<Rank> getRanks() {
		List<Rank> results = new ArrayList<>();
		
		if ( PrisonRanks.getInstance() != null && PrisonRanks.getInstance().isEnabled()
				) {
			RankManager rm = PrisonRanks.getInstance().getRankManager();
			
			results = rm.getRanks();
		}
		
		return results;
	}
	
	/**
	 * <p>This function verifies that the block name that you are trying to use is
	 * actually a valid block name within prison.  If it is invalid then a null value
	 * will be returned.
	 * </p>
	 * 
	 * <p>There are a lot of cross references that occur that ensures that the best
	 * match occurs to fit the requested block name to an actual prison block.
	 * Since prison supports minecraft 1.8 through 1.16 (and soon to be 1.17), there
	 * are various possible names for some blocks since their names have changed 
	 * between versions.  This also takes in to consideration prison block name 
	 * variations.
	 * </p>
	 * 
	 * @param blockName - The name of a block that is intended to b validated
	 * @return The name of a valid block within prison
	 */
	public String getPrisonBlockName( String blockName ) {
		String results = null;
		
        if ( Prison.get().getPlatform().isUseNewPrisonBlockModel() ) {
        	
        	PrisonBlock prisonBlock = Prison.get().getPlatform().getPrisonBlock( blockName );
        	if ( prisonBlock != null && prisonBlock.isBlock() ) {
        		results = prisonBlock.getBlockName();
        	}
        }
        
        // Obsolete... prison old block model:
//        else {
//        	
//        	BlockType blockType = BlockType.getBlock(blockName);
//        	if (blockType != null && blockType.getMaterialType() == MaterialType.BLOCK ) {
//        		results = blockType.getMaterialType().name();
//        	}
//        }
		
		return results;
	}
	
	/**
	 * <p>Provides a list of all mines that contains the specified block.
	 * </p>
	 * 
	 * @param prisonBlockName - The prison block name
	 * @return List of all mines that contains the specified block name
	 */
	public List<Mine> getMines( String prisonBlockName ) {
		List<Mine> results = new ArrayList<>();
		
		if ( prisonBlockName != null && prisonBlockName.trim().length() > 0 ) {
			
			PrisonBlock prisonBlock = null;
//			BlockType blockType = null;
			
			if ( Prison.get().getPlatform().isUseNewPrisonBlockModel() ) {
				
				prisonBlock = Prison.get().getPlatform().getPrisonBlock( prisonBlockName );
				if ( prisonBlock != null && !prisonBlock.isBlock() ) {
					prisonBlock = null;
				}
			}
			
			// Obsolete... prison old block model:
//			else {
//				
//				blockType = BlockType.getBlock( prisonBlockName );
//				if (blockType != null && blockType.getMaterialType() != MaterialType.BLOCK ) {
//					blockType = null;
//				}
//			}
			
			if ( prisonBlock != null ) {
				if ( PrisonMines.getInstance() != null && PrisonMines.getInstance().isEnabled() ) {
					MineManager mm = PrisonMines.getInstance().getMineManager();
					
					List<Mine> mines = mm.getMines();
					for ( Mine mine : mines ) {
						if ( prisonBlock != null && mine.isInMine( prisonBlock ) ) {
							results.add( mine );
							break;
						}
					}
				}
			}
			
//			if ( prisonBlock != null || blockType != null ) {
//				if ( PrisonMines.getInstance() != null && PrisonMines.getInstance().isEnabled() ) {
//					MineManager mm = PrisonMines.getInstance().getMineManager();
//					
//					List<Mine> mines = mm.getMines();
//					for ( Mine mine : mines ) {
//						if ( prisonBlock != null && mine.isInMine( prisonBlock ) ||
//								blockType != null && mine.isInMine( blockType ) ) {
//							results.add( mine );
//							break;
//						}
//					}
//				}
//			}
		}
		
		return results;
	}

	/**
	 * <p>This function will take the location that is associated with the player and 
	 * will use it to locate which mine they are standing in.  The player has to be standing
	 * within a mine, and standing on the top will not register as being in a mine. If they 
	 * are not in a mine, then it will return a null value.
	 * </p>
	 * 
	 * @param player - Player
	 * @return The prison mine that the player is standing in or null if they were not in a mine
	 */
	public Mine findMineLocation( Player player ) {
		SpigotPlayer spigotPlayer = new SpigotPlayer( player );
		return getPrisonMineManager().findMineLocation( spigotPlayer );
	}
	
	/**
	 * <p>This function will use the Location to identify if the block came from 
	 * within a mine or not.
	 * </p>
	 * 
	 * @param block A PrisonBlock only if it contains a Location.
	 * @return
	 */
	public Mine findMineLocation( PrisonBlock block ) {
		return getPrisonMineManager().findMineLocation( block.getLocation() );
	}
	
	
	/**
	 * <p>This function will return the mine in which a block break even has occurred.
	 * If the block was not in a mine, then it would return a null.
	 * </p>
	 * 
	 * @param e - BlockBreakEvent
	 * @return The prison mine if the broken block was located in a mine, otherwise it returns a null
	 */
	public Mine getPrisonMine( BlockBreakEvent e ) {
		
		return getPrisonMine( e.getPlayer(), e.getBlock(), e.isCancelled() );
	
	}
	
	/**
	 * <p>This function will return a mine in which the block is located. If the block is not 
	 * in a mine, then it will return a null.  The provided player will be associated with
	 * this transaction to be used to check which mine the last block break event for the player
	 * occurred it. These details are cached so as to provide great performance for when a player
	 * is rapidly mining blocks. The cache will be updated with this current information.
	 * </p>
	 * 
	 * <p>If you always want to check, then always pass false to isCanceledEvent.  If 
	 * isCanceledEvent is true, then the mine lookup will ONLY happen if the current 
	 * block is of type AIR.
	 * </p>
	 * 
	 * @param player - Player
	 * @param block - Block
	 * @param isCanceledEvent - Boolean
	 * @return
	 */
	public Mine getPrisonMine( Player player, Block block, boolean isCanceledEvent) {
		Mine results = null;
		
		// Fast fail: If the prison's mine manager is not loaded, then no point in processing anything.
    	if ( getPrisonMineManager() != null ) 
    	{
    		boolean isAir = block.getType() != null && block.getType() == Material.AIR;

    		// If canceled it must be AIR, otherwise if it is not canceled then 
    		// count it since it will be a normal drop
    		if ( isCanceledEvent && isAir || !isCanceledEvent ) {

    			// Need to wrap in a Prison block so it can be used with the mines:
    			SpigotBlock spigotBlock = SpigotBlock.getSpigotBlock(block);
    			
    			Long playerUUIDLSB = Long.valueOf( player.getUniqueId().getLeastSignificantBits() );

    			// Get the cached mine, if it exists:
    			Mine mine = getPlayerCache().get( playerUUIDLSB );
    			
    			if ( mine == null || !mine.isInMineExact( spigotBlock.getLocation() ) ) {
    				// Look for the correct mine to use. 
    				// Set mine to null so if cannot find the right one it will return a null:
    				mine = findMineLocation( spigotBlock );
    				
    				// Store the mine in the player cache if not null:
    				if ( mine != null ) {
    					getPlayerCache().put( playerUUIDLSB, mine );
    				}
    			}
    			
    			results = mine;
    		}
    	}
    	
    	return results;
	}

	/**
	 * <p>This creates a prison Location. It must include a valid world, and
	 * then the x, y, z coordinates.
	 * </p>
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public Location createLocation( World world, int x, int y, int z ) {
		Location results = null;
		
		if ( world != null ) {
			
			results = new Location( new SpigotWorld(world), x, y, z );
		}
		return results;
	}
	
	/**
	 * <p>This creates a mine at the given two coordinates.  The provided tag
	 * value is optional.  Minimal checks are performed.  This checks to ensure the
	 * mines module is active, and a mine by the same name does not exist.
	 * </p>
	 * 
	 * @param mineName
	 * @param tag
	 * @param location1
	 * @param location2
	 * @return
	 */
	public boolean createMine( String mineName, String tag, 
					Location location1, Location location2 ) {
		boolean results = false;
		
		if ( mineName != null && tag != null && location1 != null && location2 != null ) {
			PrisonMines mm = getPrisonMineManager();
			if ( mm !=  null ) {
				
				if ( mm.getMine(mineName) != null) {

					String message = String.format( "Cannot create requested mine. " +
							"Mine already exists by the name of '%s'.",
							mineName);
					Output.get().logError( message );
					
		    	}
				else {
					Selection selection = new Selection( location1, location2 );
					
					Mine mine = new Mine(mineName, selection);
					
					if ( tag != null ) {
						mine.setTag( tag );
					}
					
			        mm.getMineManager().add(mine);
				}
			}
		}
		
		return results;
	}

	private TreeMap<Long, Mine> getPlayerCache() {
		return getPrisonMineManager().getPlayerCache();
	}

	public PrisonMines getPrisonMineManager() {
		if ( prisonMineManager == null && !isMineModuleDisabled() ) {
			Optional<Module> mmOptional = Prison.get().getModuleManager().getModule( PrisonMines.MODULE_NAME );
			if ( mmOptional.isPresent() && mmOptional.get().isEnabled() ) {
				PrisonMines prisonMines = (PrisonMines) mmOptional.get();
				this.prisonMineManager = prisonMines;
			} else {
				setMineModuleDisabled( true );
			}
		}
		return prisonMineManager;
	}

	private boolean isMineModuleDisabled() {
		return mineModuleDisabled;
	}
	private void setMineModuleDisabled( boolean mineModuleDisabled ) {
		this.mineModuleDisabled = mineModuleDisabled;
	}
	
	private Mine findMineLocation( SpigotBlock block ) {
		return getPrisonMineManager().findMineLocationExact( block.getLocation() );
	}


	/**
	 * <p>This return the total Prison sellall boost/multiplier of a player. If 
	 * the player has no multipliers, or if ranks is not enabled, or if sellall 
	 * is not enabled, then it will return a value of 1.0 so it will not change
	 * any values multiplied by this value.</p>
	 * 
	 *
	 * @param player
	 * @return
	 * */
	public double getSellAllMultiplier(Player player){

		SpigotPlayer spigotPlayer = new SpigotPlayer( player );
		
		return spigotPlayer.getSellAllMultiplier();
	}

	/**
	 * Get the Prison backpacksUtil, which's essentially the core
	 * of Prison backpacks, to edit or use them by yourself.
	 *
	 * @return BackpaacksUtil - Null if Prison Backpacks are disabled.
	 * */
	public BackpacksUtil getPrisonBackpacks(){
		if (SpigotPrison.getInstance().getConfig().getString("backpacks") != null && SpigotPrison.getInstance().getConfig().getString("backpacks").equalsIgnoreCase("true")){
			return BackpacksUtil.get();
		}
		return null;
	}

	/**
	 * Get the whole SellAllUtil of Prison Sellall to manage what you
	 * want of Prison SellAll.
	 *
	 * @return SellAllUtil - Null if Prison Sellall's disabled.
	 * */
	public SellAllUtil getPrisonSellAll(){

		if (sellAll == null){
			sellAll = SpigotPrison.getInstance().getSellAllUtil();
		}

		return sellAll;
	}

	/**
	 * <p>Get the money to give to the Player depending on the SellAll multiplier.
	 * This depends by the items in the player's inventory at time of call.
	 * Also if multipliers are disabled, this will return the amount of money you'd
	 * get without multipliers.</p>
	 *
	 * @param player
	 * @return
	 * */
	public Double getSellAllMoneyWithMultiplier(Player player){

	    if (sellAll == null){
	        sellAll = SpigotPrison.getInstance().getSellAllUtil();
        }

		if (sellAll != null){
			return sellAll.getSellMoney(player);
		}

		return null;
	}
}
