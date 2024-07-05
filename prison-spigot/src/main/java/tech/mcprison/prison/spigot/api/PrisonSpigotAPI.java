package tech.mcprison.prison.spigot.api;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

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
import tech.mcprison.prison.spigot.block.SpigotItemStack;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.game.SpigotWorld;
import tech.mcprison.prison.spigot.sellall.SellAllData;
import tech.mcprison.prison.spigot.sellall.SellAllUtil;
import tech.mcprison.prison.util.ChatColor;
import tech.mcprison.prison.util.Location;

/**
 * <p>These are some api end points to help access some core components within prison.
 * </p>
 * 
 * <p>Use of these are at your own risk.  Misuse can result in corruption of Prison's
 * internal data.
 * </p>
 * 
 * <p>If you need something special, then please ask on our discord server and we 
 * can probably provide it for you.
 * </p>
 */
public class PrisonSpigotAPI {
	
	private PrisonMines prisonMineManager;
	private boolean mineModuleDisabled = false;
	private SellAllUtil sellAll;
	
	
//	private void junk() {
		
//		SpigotPrison.getInstance().isSellAllEnabled();
//		
//		SpigotPlayer sPlayer = new SpigotPlayer( Player player );
//		
//		sPlayer.getSellAllMulitiplier()
//		sPlayer.getSellAllMultiplierListings()
//		sPlayer.checkAutoSellPermsAutoFeatures()
//		sPlayer.checkAutoSellTogglePerms( sbDebug )
//		sPlayer.isAutoSellEnabled( sbDebug )
//		
		
	//	PrisonSpigotAPI.sellPlayerItems(Player player );
//	}

//	private void handleAffectedBlocks(Player p, IWrappedRegion region, List<Block> blocksAffected) {
//        double totalDeposit = 0.0;
//        int fortuneLevel = EnchantUtils.getItemFortuneLevel(p.getItemInHand());
//        boolean autoSellPlayerEnabled = this.plugin.isAutoSellModuleEnabled() && plugin.getCore().getAutoSell().getManager().hasAutoSellEnabled(p);
//
//        TreeMap<String,Double> itemValues = new TreeMap<>();
//        PrisonSpigotAPI pApi = new PrisonSpigotAPI();
//        
//        getItemStackValueTransactions(p, null);
//        
//        for (Block block : blocksAffected) {
//
//        	int amplifier = fortuneLevel;
//            if (FortuneEnchant.isBlockBlacklisted(block)) {
//                amplifier = 1;
//            }
//            
//            String blockNamme = block.getType().name();
//            double value = 0dg;
//            
//            if ( itemValues.containsKey(blockNamme) ) {
//            	value = itemValues.get(blockNamme);
//            }
//            else {
//            	ItemStack iStk = new ItemStack( block.getType() );
//            	if ( iStk != null ) {
//            		
//            		value = getItemStackValue(p, iStk);
//            		if ( value > 0 ) {{
//            			itemValues.put(blockNamme, value);
//            		}
//            	}
//            }
//
//            if (autoSellPlayerEnabled && value > 0) {
//                totalDeposit += value * amplifier;
//            } else {
//                ItemStack itemToGive = CompMaterial.fromBlock(block).toItem(amplifier);
//                p.getInventory().addItem(itemToGive);
//            }
//
//            if (this.removeBlocks ) {
//                this.plugin.getCore().getNmsProvider().setBlockInNativeDataPalette(block.getWorld(), block.getX(), block.getY(), block.getZ(), 0, (byte) 0, true);
//            }
//
//        }
//        this.giveEconomyRewardsToPlayer(p, totalDeposit);
//    }

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
		
		PrisonBlock prisonBlock = Prison.get().getPlatform().getPrisonBlock( blockName );
		if ( prisonBlock != null && prisonBlock.isBlock() ) {
			results = prisonBlock.getBlockName();
		}
		
		
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
			
			
			prisonBlock = Prison.get().getPlatform().getPrisonBlock( prisonBlockName );
			if ( prisonBlock != null && !prisonBlock.isBlock() ) {
				prisonBlock = null;
			}
			
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
			
			Module module = Prison.get().getModuleManager().getModule( PrisonMines.MODULE_NAME );
			
			if (module != null && module.isEnabled() ) {
				PrisonMines prisonMines = (PrisonMines) module;
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
		if (SpigotPrison.getInstance().getConfig().getString("backpacks") != null && 
				SpigotPrison.getInstance().getConfig().getString("backpacks").equalsIgnoreCase("true")){
			return BackpacksUtil.get();
		}
		return null;
	}

	/**
	 * <p>Get the whole SellAllUtil of Prison Sellall to manage what you
	 * want of Prison SellAll.
	 * </p>
	 * 
	 * <p>WARNING: usage of this object is at your own risk!  Functions will change,
	 * and will be eliminated.  The majority of the functions that were in, and are 
	 * in this object that are `public` were never intended to be used outside
	 * of prison.  Many of the functions really should have been marked as private.
	 * </p>  
	 *
	 * <p>WARNING: The sellall functions will be undergoing major revisions and 
	 * improvements in the near future.  As such, do not rely on internal functions
	 * unless otherwise indicated.  Some of the features that will be changing, is the
	 * support for all of prison's items and blocks, including custom blocks and items.
	 * Also Prison will be adding a very dynamic shop that will allow customization 
	 * for each player rank, if desired. Hence why you should be careful when not using
	 * this API class.
	 * </p>
	 * 
	 * <p>It is highly suggested to use the exposed functions within this API class.
	 * As such, more have been added to get a variety of different data out of prison.
	 * </p>
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
	 * <p>This will convert a List<org.bukkit.block.Block> to Prison's
	 * List<SpigotItemStack> so it can be used with the various SellAll 
	 * functions.
	 * </p>
	 * 
	 * @param blocks
	 * @return
	 */
	public List<SpigotItemStack> getPrisonItemmStacks( List<Block> blocks ) {
		List<SpigotItemStack> results = new ArrayList<>();
		TreeMap<String, SpigotItemStack> map = new TreeMap<>();
		
		if ( blocks != null ) {
			for ( Block b : blocks ) {
				if ( b != null ) {
					SpigotBlock sBlock = SpigotBlock.getSpigotBlock( b );
					
					if ( sBlock != null ) {
						
						String bName = sBlock.getBlockName();
						
						if ( !map.containsKey( bName ) ) {
							String[] lore = null;
							SpigotItemStack sis = new SpigotItemStack( 1, sBlock, lore );
							
							map.put(bName, sis );
						}
						else {
							map.get(bName).addToAmount( 1 );
						}
					}
					
				}
			}
		
		}
		
		if ( map.size() > 0 ) {
			for ( SpigotItemStack sItemStack : map.values() ) {
				results.add( sItemStack );
			}
		}
		
		return results;
	}
	
	public List<ItemStack> getItemStacks( List<Block> blocks ) {
		List<ItemStack> results = new ArrayList<>();
		TreeMap<String, ItemStack> map = new TreeMap<>();
		
		if ( blocks != null ) {
			for ( Block b : blocks ) {
				if ( b != null ) {
					
					String bName = b.getType().name();
							
					if ( bName != null ) {
						
						if ( !map.containsKey( bName ) ) {
							
							ItemStack iStk = new ItemStack( b.getType() );
							map.put(bName, iStk );
						}
						else {
							ItemStack iStk = map.get(bName);

							iStk.setAmount( iStk.getAmount() );
							
							map.put(bName, iStk);
						}
					}
					
				}
			}
			
		}
		
		if ( map.size() > 0 ) {
			for ( ItemStack sItemStack : map.values() ) {
				results.add( sItemStack );
			}
		}
		
		return results;
	}

	/**
	 * <p>Get the money to give to the Player depending on the SellAll multiplier.
	 * This depends by the items in the player's inventory at time of call.
	 * Also if multipliers are disabled, this will return the amount of money you'd
	 * get without multipliers.</p>
	 *
	 * @param player
	 * @return
	 */
	@Deprecated
	public Double getSellAllMoneyWithMultiplier(Player player){
		Double results = null;
		

		if (getPrisonSellAll() != null) {
			
			double value = 0;
			
			List<SellAllData> soldItems = getPrisonSellAll().sellPlayerItems( player );
			for (SellAllData soldItem : soldItems) {
				if ( soldItem != null ) {
					value += soldItem.getTransactionAmount(); 
				}
			}
			
//			return sellAll.getSellMoney(player);
			results = value;
		}

		return results;
	}
	
	
	
	/**
	 * <p>This function will use Prison internals to pay a player a given sum of money as specified 
	 * by the amount.  If the currency is non-standard, then you can also specify it's name as a 
	 * String value; otherwise pass a null or an empty String.  Example of non-standard currencies 
	 * would be alternative currencies setup on ranks.
	 * </p>
	 * 
	 * <p>This function will notify the player each time they are paid of the payment amount 
	 * (notifyPlayerEarned).  It will also play a payment sound (playSoundOnSellAll), the sound can
	 * be changed within the SellAll config file.
	 * </p>
	 * 
	 * <p>If you need to delay player notifications, please use the other `payPlayer()` functions.
	 * </p>
	 * 
	 * <p>Prison's internals keep track of the player's balance in the Prison's player cache.  It 
	 * allows for rapid payments to a player, without overwhelming Vault or the Economy plugins. 
	 * Some economy plugins will update their database with each hit, which can result in a 
	 * serious performance drain on the system, since such I/O is blocking, and if they run those
	 * updates in bukkit's main thread.  Since prison is caching the payments, as soon as it 
	 * receives a payment, prison starts a count down to make the payment.  Any additional payment 
	 * made to a player before that count down reaches zero, will be included in the transaction.
	 * Rapid mining can create TONS of transactions per second, so Prison's payment cache is 
	 * an important component to help weaker economy plugins from killing the server's TPS.
	 * </p>
	 * 
	 * @param player
	 * @param amount
	 * @param currency - If standard, pass null or an empty String
	 */
	public void payPlayer( Player player, double amount, String currency ) {
		payPlayer( player, amount, currency, true, false, false, true );
	}
		
	/**
	 * <p>This function will use Prison internals to pay a player a given sum of money as specified 
	 * by the amount.  This function is the same as the other one that is similar, except that it 
	 * provides more options such as delayed notifications.  Please see the help on the other 
	 * `payPlayer()` functions to better understand what they are doing.
	 * </p>
	 * 
	 * <p>The payment amount must be greater than zero.  If the amount is zero, or less,
	 * then this function will end silently without any notification.
	 * </p>
	 * 
	 * <p>This function has additional parameters for controlling player notifications. If they 
	 * are all set to false, then no notifications will be sent to the player (completelySilent).
	 * </p>
	 * 
	 * <p>'notifyPlayerEarned': This function will notify the player each time they are paid 
	 * of the payment amount (notifyPlayerEarned).  It will also play a payment 
	 * sound (playSoundOnSellAll), the sound can be changed within the SellAll config file.
	 * </p>
	 *
	 * <p>'notifyPlayerDelay' & SellAll Delay (cooldown): If the SellAll configuration 
	 * `isSellAllDelayEnabled` is set to `true`, then 
	 * the player will be subjected to a cooldown for using the sellall command/feature.
	 * If the have tried to sell within the allocated amount of time, then they will get a
	 * message indicating that there is a rate limit in effect for them.  They will get
	 * this notification if the parameter 'notifyPlayerDelay' is set to `true`.
	 * </p>
	 * 
	 * <p>Please NOTE: This function, `payPlayer()` only activates the cooldown timer and
	 * the player will be paid.  What is impacted is the ability to use the functions 
	 * `sellAllSell()`... it is then when the rate limit will kick in.  If you use the 
	 * other function `sellPlayerItems()` or `sellPlayerItemStacks()` then the 
	 * cooldown rate limit will be bypassed and will not inhibit any selling.
	 * </p>
	 * 
	 * <p>'notifyPlayerEarningDelay': If in the SellAll config, the option 
	 * `isAutoSellEarningNotificationDelayEnabled` is enabled, then this parameter
	 * `notifyPlayerEarningDelay` will then add the amount the player was paid to
	 * a delayed queue which will gather other payments the player receives during the
	 * delayed period.  At the end of this delayed period of time, then the player
	 * will get a message indicating the total amount of money that they earned.
	 * This helps to reduce the amount of spam the player will get on line for their 
	 * sales.  Please NOTE: This does not delay the player from getting paid.  If the
	 * delay is set to 15 seconds and they mine 100 times, they will get only one 
	 * message with the total amount that they earned; otherwise they would have been
	 * sent 100 messages each time something was sold.
	 * </p>
	 *
	 * <p>`playSoundOnSellAll`: If enabled, then a sound will be played upon a payment
	 * to the player.  Please see the SellAll configuration file for changing 
	 * the settings for the sound.
	 * </p>
	 * 
	 * <p>`completelySilent`: This is a virtual setting that is generated when all four 
	 * of these settings are set to false: `notifyPlayerEarned`, `notifyPlayerDelay`, 
	 * `notifyPlayerEarningDelay`, and `playSoundOnSellAll`.  When enabled, this 
	 * basically shuts off all notifications and the player is paid silently in the 
	 * background.
	 *
	 * @param player
	 * @param amount
	 * @param currency
	 * @param notifyPlayerEarned
	 * @param notifyPlayerDelay
	 * @param notifyPlayerEarningDelay
	 * @param playSoundOnSellAll
	 */
	public void payPlayer( Player player, double amount, String currency,
    		boolean notifyPlayerEarned, 
    		boolean notifyPlayerDelay, 
    		boolean notifyPlayerEarningDelay, 
    		boolean playSoundOnSellAll ) {
		
		if ( amount > 0 ) {
			
			boolean completelySilent = notifyPlayerEarned || notifyPlayerDelay || notifyPlayerEarningDelay || playSoundOnSellAll;
			
			SpigotPlayer sPlayer = new SpigotPlayer( player );
			RankPlayer rankPlayer = PrisonRanks.getInstance().getPlayerManager().getPlayer(sPlayer.getUUID(), sPlayer.getName());
			
			currency = currency != null && currency.equalsIgnoreCase("default") ? null : currency;
			
//        if (!sellInputArrayListOnly) {
//            removeSellableItems(p);
//        }
			rankPlayer.addBalance(currency, amount);
			
			if ( getPrisonSellAll().isSellAllDelayEnabled ){
				getPrisonSellAll().addToDelay(player);
			}
			
			if (!completelySilent) {
				if ( getPrisonSellAll().isSellAllSoundEnabled && playSoundOnSellAll) {
					player.playSound( player.getLocation(), getPrisonSellAll().sellAllSoundSuccess, 3, 1);
				}
				
				if (notifyPlayerEarningDelay && getPrisonSellAll().isAutoSellEarningNotificationDelayEnabled){
					if (!getPrisonSellAll().isPlayerWaitingAutoSellNotification( player )){
						getPrisonSellAll().addToAutoSellNotificationDelay( player);
					} else {
						getPrisonSellAll().addDelayedEarningAutoSellNotification( player, amount );
					}
				} 
				else if (notifyPlayerEarned){
					DecimalFormat fFmt = Prison.get().getDecimalFormat("#,##0.00");
					String amt = fFmt.format( amount );
					
					String message = getPrisonSellAll().sellallAmountEarnedMsg( amt );
					
//            	String message = messages.getString(MessagesConfig.StringID.spigot_message_sellall_money_earned) + amt;
//            	new SpigotPlayer(p).setActionBar( message );
					Output.get().send( sPlayer, message );
					
				}
			}
		}
	}
	
	
	/**
	 * <p>This function will use Prison internals to pay a player a given sum of money as specified 
	 * by the amount.  This function is the same as the other ones with the same name and similar
	 * parameters.  This one differs in that you can pass it a List of `SellAllData` transactions
	 * and it will summarize them all, and pass the total amount to the other
	 * `payPlayer()` function.  Please see the help on the other 
	 * `payPlayer()` functions to better understand what they are doing.
	 * </p>
	 * 
	 * @param player
	 * @param itemsSold
	 * @param currency
	 * @param notifyPlayerEarned
	 * @param notifyPlayerDelay
	 * @param notifyPlayerEarningDelay
	 * @param playSoundOnSellAll
	 */
	public void payPlayer( Player player, List<SellAllData> itemsSold, String currency,
    		boolean notifyPlayerEarned, 
    		boolean notifyPlayerDelay, 
    		boolean notifyPlayerEarningDelay, 
    		boolean playSoundOnSellAll ) {
		double amount = 0;
		
		for (SellAllData item : itemsSold) {
			amount += item.getTransactionAmount();
		}
		
		payPlayer(player, amount, currency, 
				notifyPlayerEarned, 
				notifyPlayerDelay, notifyPlayerEarningDelay, 
				playSoundOnSellAll);
	}
	
	
	/**
	 * <p>This function will calculate the value of all sellable items within the player's inventory 
	 * and provide a total amount that they earned for the sales. 
	 * This includes the calculated player's multipliers.
	 * </p>
	 * 
	 * @param player
	 * @return
	 */
	public double getPlayerInventoryValue(Player player) {
		double results = 0;
		
		if (getPrisonSellAll() != null) {
			
			results = getPrisonSellAll().getPlayerInventoryValue( new SpigotPlayer(player) );
		}
		
		return results;
	}
	
	/**
	 * <p>This function will calculate the value of the player's inventory that 
	 * are sellable and then will generate 
	 * a simple report listing everything that can be sold with their values.
	 * </p>
	 * 
	 * <p>Nothing is sold. No notifications are sent to the player.
	 * </p>
	 * 
	 * <p>See the function <pre>getPlayerInventoryValueTransactions()</pre> to
	 * gain access to the transaction logs for more information.
	 * </p>
	 * 
	 * @param player
	 * @return
	 */
	public String getPlayerInventoryValueReport(Player player) {
		String results = null;
		
		if (getPrisonSellAll() != null) {
			
			results = getPrisonSellAll().getPlayerInventoryValueReport( new SpigotPlayer(player) );
		}
		
		return results;
	}
	
	/**
	 * <p>This function calculates the value of the player's inventory items that are salable, 
	 * and returns the transaction log within the collection of SellAllData.
	 * </p>
	 * 
	 * <p>Nothing is sold. No notifications are sent to the player.
	 * </p>
	 * 
	 * <p>To get the total transaction value, add all elements together.
	 * To generate a transaction report use the static function:
	 * </p>
	 * <pre>List<SellAllData> itemsSold = prisonApi.getPlayerInventoryValueTransactions(Player player);
	 *SellAllData.itemSoldReport( itemsSold );</pre>
	 * 
	 * @param player
	 * @return transactionLogs
	 */
	public List<SellAllData> getPlayerInventoryValueTransactions(Player player) {
		List<SellAllData> results = new ArrayList<>();
		
		if (getPrisonSellAll() != null) {
			
			results = getPrisonSellAll().getPlayerInventoryValueTransactions( new SpigotPlayer(player) );
		}
		
		return results;
	}
	
	
	/**
	 * <p>This function will calculate the value of ItemStack 
	 * and provide a total amount that would earn for the sales. 
	 * This includes the calculated player's multipliers.
	 * </p>
	 * 
	 * @param player
	 * @param itemStack
	 * @return
	 */
	public double getItemStackValue(Player player, ItemStack itemStack ) {
		double results = 0;
		
		if (getPrisonSellAll() != null) {
			
			results = getPrisonSellAll().getItemStackValue( 
					new SpigotPlayer(player), new SpigotItemStack(itemStack) );
			
		}
		
		return results;
	}
	
	/**
	 * <p>This function will calculate the value of the ItemStack that 
	 * are sellable and then will generate 
	 * a simple report listing everything that can be sold with their values.
	 * </p>
	 * 
	 * <p>Nothing is sold. No notifications are sent to the player.
	 * </p>
	 * 
	 * <p>See the function <pre>getPlayerInventoryValueTransactions()</pre> to
	 * gain access to the transaction logs for more information.
	 * </p>
	 * 
	 * @param player
	 * @param itemStack
	 * @return
	 */
	public String getItemStackValueReport(Player player, ItemStack itemStack ) {
		String results = null;
		
		if (getPrisonSellAll() != null) {
			
			results = getPrisonSellAll().getItemStackValueReport( 
					new SpigotPlayer(player), new SpigotItemStack(itemStack) );
			
		}
		
		return results;
	}
	
	/**
	 * <p>This function calculates the value of the ItemStack that are salable, 
	 * and returns the transaction log within the collection of SellAllData.
	 * </p>
	 * 
	 * <p>Nothing is sold. No notifications are sent to the player.
	 * </p>
	 * 
	 * <p>To get the total transaction value, add all elements together.
	 * To generate a transaction report use the static function:
	 * </p>
	 * <pre>List<SellAllData> itemsSold = prisonApi.getPlayerInventoryValueTransactions(Player player);
	 *SellAllData.itemSoldReport( itemsSold );</pre>
	 *
	 * @param player
	 * @param itemStack
	 * @return
	 */
	public List<SellAllData> getItemStackValueTransactions(Player player, ItemStack itemStack ) {
		List<SellAllData> results = new ArrayList<>();
		
		if (getPrisonSellAll() != null) {
			
			results = getPrisonSellAll().getItemStackValueTransactions( 
					new SpigotPlayer(player), new SpigotItemStack(itemStack) );
			
		}
		
		return results;
	}

	/**
	 * <p>This function sells anything within the player's inventory that is 
	 * sellable. This function returns a transaction log of soldItems that can
	 * be summarized to get the total sales amount.</p>
	 * 
	 * <p>WARNING: This function REMOVES items from the player's inventory, but does
	 * 				NOT pay them any money!! If you are using this function, then 
	 * 				you must sum all of the transactions and pay the player.
	 * </p>
	 * 
	 * <p>This is an exposed internal function that provides sellall capabilities without
	 * all of the complexities of the formal `sellAllSell()` functions. 
	 * Use at your own risk.
	 * </p>
	 * 
	 * <p>This function does not notify the players, does not pay the players,
	 * does not provide all of the bells-and-whistles of customization within the
	 * sellall config files, and is basically a bare-bones sell it if it can be sold
	 * type of function. 
	 * </p>
	 * 
	 * @param player
	 * @param itemStack
	 * @return
	 */
	public List<SellAllData> sellPlayerItems(Player player ) {
		List<SellAllData> soldItems = new ArrayList<>();
		
		if (getPrisonSellAll() != null) {
			
			soldItems = getPrisonSellAll().sellPlayerItems( player );
		}
		
		return soldItems;
	}

	/**
	 * <p>This function sells anything within the ItemStack List that is 
	 * sellable. This function returns a transaction log of soldItems that can
	 * be summarized to get the total sales amount.</p>
	 * 
	 * <p>WARNING: This function REMOVES items from the ItemStack that have been
	 * 				sold, but does NOT pay the player any money!! If you are 
	 * 				using this function, then you must sum all of the 
	 * 				transactions and pay the player.
	 * </p>
	 * 
	 * <p>This is an exposed internal function that provides sellall capabilities without
	 * all of the complexities of the formal `sellAllSell()` functions. 
	 * Use at your own risk.
	 * </p>
	 * 
	 * <p>This function does not notify the players, does not pay the players,
	 * does not provide all of the bells-and-whistles of customization within the
	 * sellall config files, and is basically a bare-bones sell it if it can be sold
	 * type of function. 
	 * </p>
	 * 
	 * @param player
	 * @param itemStacks
	 * @return
	 */
	public List<SellAllData> sellPlayerItemStacks(Player player, List<ItemStack> itemStacks ) {
		List<SellAllData> soldItems = new ArrayList<>();
		
		if (getPrisonSellAll() != null) {
			
			List<SpigotItemStack> iStacks = new ArrayList<>();
			for (ItemStack itemStack : itemStacks) {
				if ( itemStack != null ) {
					iStacks.add( new SpigotItemStack( itemStack ));
				}
			}
			
			soldItems = getPrisonSellAll().sellPlayerItemStacks( player, iStacks );
		}
		
		return soldItems;
	}
	
	
	   /**
     * <p>This function will remove all sellable items from the player's Inventories. It will first ensure that a 
     * Player can sell the items. Some of the conditions that are checked are, along with some of the behaviors:
     * </p>
     * 
     * <ul>
     *   <li>If player has access to use SellAll signs.</li>
     *   <li>Provide the amount the player earned if this is not disabled.</li>
     *   <li>If this actions is silenced, then text and audio notifications are suppressed.<li>
     *   <li>If configured, the reported earnings amount may be delayed and added to other earnings, 
     *   		which will reduce flooding the player with notifications.</li>
     *   <li>If sound notifications are enabled, then they will be played.</li>
     *
     *</ul>
     *
     * <p>Default usage of this method: 
     * </p>
     * <pre>sellAllSell(p, false, false, true, true, false, true);</pre>
     *
     * @param p - Player.
     * @param isUsingSign - boolean.
     * @param completelySilent - boolean.
     * @param notifyPlayerEarned - boolean.
     * @param notifyPlayerDelay - boolean.
     * @param notifyPlayerEarningDelay - boolean.
     * @param playSoundOnSellAll - boolean.
     *
     * @return boolean If successful
     * */
    public boolean sellAllSell(Player p, 
    		boolean isUsingSign, 
    		boolean completelySilent, 
    		boolean notifyPlayerEarned, 
			boolean notifyPlayerDelay, 
			boolean notifyPlayerEarningDelay, 
			boolean playSoundOnSellAll) {
    	boolean results = false;
    	
    	if (getPrisonSellAll() != null) {
    		
    		results = getPrisonSellAll().sellAllSell( p, 
    				isUsingSign, 
    				completelySilent, 
    				notifyPlayerEarned, 
    				notifyPlayerDelay, 
    				notifyPlayerEarningDelay, 
    				playSoundOnSellAll, 
    				null );
    	}
    	
    	return results;
    }
    
    /**
     * <p>Performs a sellall of the player's inventory.
     * </p>
     * 
     * @param p
     * @param isUsingSign
     * @param completelySilent
     * @param notifyPlayerEarned
     * @param notifyPlayerDelay
     * @param notifyPlayerEarningDelay
     * @param playSoundOnSellAll
     * @param amounts
     * @return
     */
    public boolean sellAllSell(Player p, 
    		boolean isUsingSign, 
    		boolean completelySilent, 
    		boolean notifyPlayerEarned, 
    		boolean notifyPlayerDelay, 
    		boolean notifyPlayerEarningDelay, 
    		boolean playSoundOnSellAll, 
    		List<Double> amounts ){
    	boolean results = false;
    	
    	if (getPrisonSellAll() != null) {
    		
    		results = getPrisonSellAll().sellAllSell( p, 
    				isUsingSign, completelySilent, 
    				notifyPlayerEarned, notifyPlayerDelay, 
    				notifyPlayerEarningDelay, playSoundOnSellAll, 
    				amounts );
    	}
    	
    	return results;
    }
    	
    /**
     * <p>This function performs the sellall functions over an ItemStack.
     * </p>
     * 
     * @param p
     * @param itemStack
     * @param completelySilent
     * @param notifyPlayerEarned
     * @param notifyPlayerEarningDelay
     * @return
     */
    public double sellAllSell(Player p, 
    		SpigotItemStack itemStack, 
    		boolean completelySilent, 
    		boolean notifyPlayerEarned, 
    		boolean notifyPlayerEarningDelay)
    {
    	double results = 0;
    	
    	if (getPrisonSellAll() != null) {
    		
    		results = getPrisonSellAll().sellAllSell( p, 
    				itemStack, 
    	    		completelySilent, 
    	    		notifyPlayerEarned, 
    	    		notifyPlayerEarningDelay );
    	}
    	
    	return results;
    }
	
    
    
	/**
     * Sell removing items from Inventories and checking all the possible conditions that a Player must meet to sell
     * items, this includes method parameters like:
     * - Is using SellAll Sign.
     * - If tell the Player how much did he earn (if this's disabled by config, the parameter will be ignored).
     * - If do this action without making the player notice it, disabling sounds and all messages.
     * - If tell the Player to wait the end of SellAll Delay if not ended (if this's disabled by config, the parameter will be ignored).
     * - If tell the Player how much did he earn only after a delay (AutoSell Delay Earnings will use this option for example).
     * - If play sound on SellAll Sell (If sounds are disabled from the config, this parameter will be ignored.
     * - If Sell only stuff from the input arrayList and not sell what is in the many Player inventories and supported backpacks.
     *
     * NOTE: With this method you can add an ArrayList of ItemStacks to sell, remove sold items (this will return the ArrayList without
     * sold items), and give money to the player, also note that this will also trigger the usual sellall sell and sell everything sellable
     * from all inventories and enabled backpacks of the Player.
     *
     * Return True if success, False if error or nothing changed or Player not meeting requirements.
     *
     * Default usage of this method: sellAllSell(p, itemStacks, false, false, true, false, false, true, false);
     *
     * @param p - Player.
     * @param itemStacks - ArrayList of ItemStacks.
     * @param isUsingSign - boolean.
     * @param completelySilent - boolean.
     * @param notifyPlayerEarned - boolean.
     * @param notifyPlayerDelay - boolean.
     * @param notifyPlayerEarningDelay - boolean.
     * @param playSoundOnSellAll - boolean.
     * @param sellInputArrayListOnly - boolean.
     *
     * @return Array of ItemStacks
     * */
    public ArrayList<ItemStack> sellAllSell(Player p, 
    		ArrayList<ItemStack> itemStacks, 
    		boolean isUsingSign, 
    		boolean completelySilent, 
    		boolean notifyPlayerEarned, 
    		boolean notifyPlayerDelay, 
    		boolean notifyPlayerEarningDelay, 
    		boolean playSoundOnSellAll, 
    		boolean sellInputArrayListOnly){
    	
    	ArrayList<ItemStack> results = new ArrayList<>();
    	
    	if (getPrisonSellAll() != null) {
    		
    		results = getPrisonSellAll().sellAllSell( p, 
    				itemStacks,
    				isUsingSign, 
    				completelySilent, 
    				notifyPlayerEarned, 
    				notifyPlayerDelay, 
    				notifyPlayerEarningDelay, 
    				playSoundOnSellAll, 
    				sellInputArrayListOnly);
    	}
    	
    	return results;
    }
    	
    
	
	/**
	 * <p>Gets a player's current token balance.
	 * </p>
	 * 
	 * @param player
	 * @return
	 */
	public long getTokens( Player player ) {
		SpigotPlayer sPlayer = new SpigotPlayer( player );
		
		return sPlayer.getPlayerCachePlayerData().getTokens();
	}
	
	/**
	 * <p>Gets a player's total amount of tokens that they have earned.
	 * </p>
	 * 
	 * @param player
	 * @return
	 */
	public long getTokensTotal( Player player ) {
		SpigotPlayer sPlayer = new SpigotPlayer( player );
		
		return sPlayer.getPlayerCachePlayerData().getTokensTotal();
	}
	
	/**
	 * <p>Gets a player's total amount of tokens given to them by admins.
	 * </p>
	 * 
	 * @param player
	 * @return
	 */
	public long getTokensTotalAdminAdded( Player player ) {
		SpigotPlayer sPlayer = new SpigotPlayer( player );
		
		return sPlayer.getPlayerCachePlayerData().getTokensTotalAdminAdded();
	}
	
	/**
	 * <p>Gets a player's total amount of tokens removed from them by admins.
	 * </p>
	 * 
	 * @param player
	 * @return
	 */
	public long getTokensTotalAdminRemoved( Player player ) {
		SpigotPlayer sPlayer = new SpigotPlayer( player );
		
		return sPlayer.getPlayerCachePlayerData().getTokensTotalAdminRemoved();
	}

	/**
	 * <p>Returns a Map of a player's total tokens earned by mine.
	 * </p>
	 * 
	 * @param player
	 * @return
	 */
	public TreeMap<String, Long> getTokensTotalByMine( Player player ) {
		SpigotPlayer sPlayer = new SpigotPlayer( player );
		
		return sPlayer.getPlayerCachePlayerData().getTokensByMine();
	}
	
	/**
	 * <p>Adds tokens to a player's current balance.  The tokens
	 * will be counted as being earned under normal conditions.
	 * </p>
	 * 
	 * @param player
	 * @param amount
	 */
	public void addTokens( Player player, long amount ) {
		SpigotPlayer sPlayer = new SpigotPlayer( player );
		
		sPlayer.getPlayerCachePlayerData().addTokens( amount );
	}
	
	/**
	 * <p>Adds tokens to a player's balance, but will be recorded as
	 * an adjustment made by an admin, which means the player did not
	 * "earn" the tokens under normal conditions.
	 * </p>
	 * 
	 * @param player
	 * @param amount
	 */
	public void addTokensAdmin( Player player, long amount ) {
		SpigotPlayer sPlayer = new SpigotPlayer( player );
		
		sPlayer.getPlayerCachePlayerData().addTokensAdmin( amount );
	}
	
	/**
	 * <p>Removes tokens from a player's current balance.  The tokens
	 * will be counted as being spent under normal conditions.
	 * </p>
	 * 
	 * @param player
	 * @param amount
	 */
	public void removeTokens( Player player, long amount ) {
		SpigotPlayer sPlayer = new SpigotPlayer( player );
		
		sPlayer.getPlayerCachePlayerData().removeTokens( amount );
	}
	
	/**
	 * <p>Removes tokens from a player's balance, but will be recorded as
	 * an adjustment made by an admin, which means the player did not
	 * "spend" the tokens under normal conditions.
	 * </p>
	 * 
	 * @param player
	 * @param amount
	 */
	public void removeTokensAdmin( Player player, long amount ) {
		SpigotPlayer sPlayer = new SpigotPlayer( player );
		
		sPlayer.getPlayerCachePlayerData().removeTokensAdmin( amount );
	}
	
	/**
	 * <p>Sets a player's current token balance.  The tokens
	 * will be counted as being earned, or spent, under normal conditions, 
	 * based upon the change in the original amounts.
	 * </p>
	 * 
	 * @param player
	 * @param amount
	 */
	public void setTokens( Player player, long amount ) {
		SpigotPlayer sPlayer = new SpigotPlayer( player );
		
		sPlayer.getPlayerCachePlayerData().setTokens( amount );
	}
	
	/**
	 * <p>Sets a player's current balance, but will be recorded as
	 * an adjustment made by an admin, which means the player did not
	 * "earn" or "spend" the tokens under normal conditions.
	 * </p>
	 * 
	 * @param player
	 * @param amount
	 */
	public void setTokensAdmin( Player player, long amount ) {
		SpigotPlayer sPlayer = new SpigotPlayer( player );
		
		sPlayer.getPlayerCachePlayerData().setTokensAdmin( amount );
	}
	
	
	/**
	 * <p>This function will translate placeholders, but only the specified placeholder.
	 * This cannot contain any placeholder escape characters. 
	 * </p>
	 * 
	 * @param player
	 * @param identifier
	 * @return
	 */
	public String getPrisonPlaceholder( OfflinePlayer player, String identifier) {
		
//		if ( !identifier.toLowerCase().startsWith( PlaceholderManager.PRISON_PLACEHOLDER_PREFIX_EXTENDED ) ) {
//			identifier = PlaceholderManager.PRISON_PLACEHOLDER_PREFIX_EXTENDED + identifier;
//		}

		UUID playerUuid = player.getUniqueId();
		String results = Prison.get().getPlatform().getPlaceholders()
									.placeholderTranslate( playerUuid, player.getName(), identifier );

		return ChatColor.translateAlternateColorCodes( '&', results);
	}
	
	/**
	 * <p>This function will translate placeholders, and text with placeholders within the text.
	 * You can use any placeholder escape character as long as they are: `% %` or `{ }`.
	 * This can contain multiple placeholders too.  This can also include placeholder attributes.
	 * </p>
	 * 
	 * @param player
	 * @param identifier
	 * @return
	 */
	public String getPrisonPlaceholderFullText( OfflinePlayer player, String identifier) {
		
//		if ( !identifier.toLowerCase().startsWith( PlaceholderManager.PRISON_PLACEHOLDER_PREFIX_EXTENDED ) ) {
//			identifier = PlaceholderManager.PRISON_PLACEHOLDER_PREFIX_EXTENDED + identifier;
//		}
		
		UUID playerUuid = player.getUniqueId();
		String results = Prison.get().getPlatform().getPlaceholders()
				.placeholderTranslateText( playerUuid, player.getName(), identifier );
		
		return ChatColor.translateAlternateColorCodes( '&', results);
	}
	
}
