package tech.mcprison.prison.spigot.autoFeatures;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.vk2gpz.tokenenchant.event.TEBlockExplodeEvent;

import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;


/**
 * @author GABRYCA
 * @author RoyalBlueRanger
 */
public class AutoManager 
	extends AutoManagerFeatures
	implements Listener {
	
	public enum ItemLoreCounters {
		
		// NOTE: the String value must include a trailing space!
		
		itemLoreBlockBreakCount( ChatColor.LIGHT_PURPLE + "Prison Blocks Mined:" +
							ChatColor.GRAY + " "),
		
		itemLoreBlockExplodeCount( ChatColor.LIGHT_PURPLE + "Prison Blocks Exploded:" +
							ChatColor.GRAY + " ");
		
		
		private final String lore;
		ItemLoreCounters( String lore ) {
			this.lore = lore;
		}
		public String getLore() {
			return lore;
		}
		
	}

	
    public AutoManager() {
        super();
        
        // Save this instance within the SpigotPrison instance so it can be accessed
        // from non-event listeners:
        SpigotPrison.getInstance().setAutoFeatures( this );
    }


//    /**
//     * <p>Had to set to a EventPriorty.LOW so other plugins can work with the blocks.
//     * The other plugins were EZBlock & SellAll. This function was canceling the
//     * event after it auto picked it up, so the other plugins were not registering
//     * the blocks as being broken.
//     * </p>
//     * 
//     * @param e
//     */
//    @EventHandler(priority=EventPriority.LOW) 
//    public void onBlockBreak(BlockBreakEvent e) {
//    	
//    	if ( !e.isCancelled() && e.getBlock().getType() != null) {
//    		
//    		// Get the player objects: Spigot and the Prison player:
//    		Player p = e.getPlayer();
//    		// SpigotPlayer player = new SpigotPlayer( p );
//    		
//    		// Validate that the event is happening within a mine since the
//    		// onBlockBreak events here are only valid within the mines:
//    		Optional<Module> mmOptional = Prison.get().getModuleManager().getModule( PrisonMines.MODULE_NAME );
//    		if ( mmOptional.isPresent() && mmOptional.get().isEnabled() ) {
//    			PrisonMines mineManager = (PrisonMines) mmOptional.get();
//    			
//    			for ( Mine mine : mineManager.getMines() ) {
//    				SpigotBlock block = new SpigotBlock(e.getBlock());
//    				if ( mine.isInMine( block.getLocation() ) ) {
//    					
//    					applyAutoEvents( e, mine, p );
//    					break;
//    				}
//    			}
//    		}
//    	}
//    }

    /**
     * <p>The optimized logic on how an BlockBreakEvent is handled is within the OnBlockBreakEventListener
     * class and optimizes mine reuse.
     * </p>
     *
     * <p>Had to set to a EventPriorty.LOW so other plugins can work with the blocks.
     * The other plugins were EZBlock & SellAll. This function was canceling the
     * event after it auto picked it up, so the other plugins were not registering
     * the blocks as being broken.
     * </p>
     * 
     * 
     */
    @Override
    @EventHandler(priority=EventPriority.LOW) 
    public void onBlockBreak(BlockBreakEvent e) {
    	super.onBlockBreak(e);
    }
    
    @Override
    @EventHandler(priority=EventPriority.LOW) 
    public void onTEBlockExplode(TEBlockExplodeEvent e) {
    	super.onTEBlockExplode(e);
    }
    
    
    
    @Override
	public void doAction( Mine mine, BlockBreakEvent e ) {
    	applyAutoEvents( e, mine );
	}
    
    
    @Override
    public void doAction( Mine mine, TEBlockExplodeEvent e ) {
    	applyAutoEvents( e, mine );
    }
    
    // Prevents players from picking up armorStands (used for holograms), only if they're invisible
	@EventHandler
	public void manipulate(PlayerArmorStandManipulateEvent e) {
		if(!e.getRightClicked().isVisible()) {
			e.setCancelled(true);
		}
	}


	
	private void applyAutoEvents( BlockBreakEvent e, Mine mine ) {
		// Change this to true to enable these features
		
		Player p = e.getPlayer();

		if (isAreEnabledFeatures()) {
			
	

			// Init variables
			Block block = e.getBlock();
			Material brokenBlock = block.getType();
			String blockName = brokenBlock.toString().toLowerCase();
			
			
			// Minecraft's formular for fortune: Should implement it to be fair.
			// https://minecraft.gamepedia.com/Fortune
			
			ItemStack itemInHand = SpigotPrison.getInstance().getCompatibility().getItemInMainHand( p );
			
	
//			int fortuneLevel = itemInHand.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
//			int dropNumber = getDropCount(fortuneLevel);
			
//			// Check if the inventory's full
//			if (p.getInventory().firstEmpty() == -1){
//
//				// Play sound when full
//				if (playSoundIfInventoryIsFull) {
//					p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_PLACE, 10F, 1F);
//				}
//
//				String message;
//
//				// Drop items when full
//				if (dropItemsIfInventoryIsFull) {
//
//					message = SpigotPrison.format(autoConfigs.getString("Messages.InventoryIsFullDroppingItems"));
//					
//					p.sendMessage(message);
//
//					hologram(e, message, hologramIfInventoryIsFull);
//
//				} else {  // Lose items when full
//
//					message = SpigotPrison.format(autoConfigs.getString("Messages.InventoryIsFullLosingItems"));
//					
//					p.sendMessage(message);
//
//					hologram(e, message, hologramIfInventoryIsFull);
//
//					// Set the broken block to AIR and cancel the event
//					e.setCancelled(true);
//					e.getBlock().setType(Material.AIR);
//				}
//				return;
//			}
			
			// AutoPickup
			if (isAutoPickupEnabled()) {
				
				@SuppressWarnings( "unused" )
				int count = 0;
				
				switch (blockName) {
					case "cobblestone":
						count += autoPickup( isAutoPickupCobbleStone(), p, itemInHand, e );
						break;
						
					case "stone":
						count += autoPickup( isAutoPickupStone(), p, itemInHand, e );
						break;
						
					case "gold_ore":
						count += autoPickup( isAutoPickupGoldOre(), p, itemInHand, e );
						break;
						
					case "iron_ore":
						count += autoPickup( isAutoPickupIronOre(), p, itemInHand, e );
						break;
						
					case "coal_ore":
						count += autoPickup( isAutoPickupCoalOre(), p, itemInHand, e );
						break;
						
					case "diamond_ore":
						count += autoPickup( isAutoPickupDiamondOre(), p, itemInHand, e );
						break;
						
					case "redstone_ore":
						count += autoPickup( isAutoPickupRedstoneOre(), p, itemInHand, e );
						break;
						
					case "emerald_ore":
						count += autoPickup( isAutoPickupEmeraldOre(), p, itemInHand, e );
						break;
						
					case "quartz_ore":
						count += autoPickup( isAutoPickupQuartzOre(), p, itemInHand, e );
						break;
						
					case "lapis_ore":
						count += autoPickup( isAutoPickupLapisOre(), p, itemInHand, e );
						break;
						
					case "snow_ball":
						count += autoPickup( isAutoPickupSnowBall(), p, itemInHand, e );
						break;
						
					case "glowstone_dust": // works 1.15.2
						count += autoPickup( isAutoPickupGlowstoneDust(), p, itemInHand, e );
						break;
						
					default:
						count += autoPickup( isAutoPickupAllBlocks(), p, itemInHand, e );
						break;
							
				}
				
//				Output.get().logInfo( "In mine: %s  blockName= [%s] %s  drops= %s  count= %s  dropNumber= %s ", 
//						mine.getName(), blockName, Integer.toString( dropNumber ),
//						(e.getBlock().getDrops(itemInHand) != null ? e.getBlock().getDrops(itemInHand).size() : "-=null=-"), 
//						Integer.toString( count ), Integer.toString( dropNumber )
//						);
//				

			}
			
			// AutoSmelt
			if (isAutoSmeltEnabled()){
				
				autoSmelt( isAutoSmeltGoldOre(), Material.GOLD_ORE, Material.GOLD_INGOT, p, block );

				autoSmelt( isAutoSmeltIronOre(), Material.IRON_ORE, Material.IRON_INGOT, p, block );
			}
			
			// AutoBlock
			if (isAutoBlockEnabled()) {
				// Any autoBlock target could be enabled, and could have multiples of 9, so perform the
				// checks within each block type's function call.  So in one pass, could hit on more 
				// than one of these for multiple times too.
				autoBlock( isAutoBlockGoldBlock(), Material.GOLD_INGOT, Material.GOLD_BLOCK, p, block );
				
				autoBlock( isAutoBlockIronBlock(), Material.IRON_INGOT, Material.IRON_BLOCK, p, block );

				autoBlock( isAutoBlockCoalBlock(), Material.COAL, Material.COAL_BLOCK, p, block );
				
				autoBlock( isAutoBlockDiamondBlock(), Material.DIAMOND, Material.DIAMOND_BLOCK, p, block );

				autoBlock( isAutoBlockRedstoneBlock(), Material.REDSTONE, Material.REDSTONE_BLOCK, p, block );
				
				autoBlock( isAutoBlockEmeraldBlock(), Material.EMERALD, Material.EMERALD_BLOCK, p, block );

				autoBlock( isAutoBlockQuartzBlock(), Material.QUARTZ, Material.QUARTZ_BLOCK, 4, p, block );

				autoBlock( isAutoBlockPrismarineBlock(), Material.PRISMARINE_SHARD, Material.PRISMARINE, 4, p, block );

				autoBlock( isAutoBlockSnowBlock(), Material.SNOW_BALL, Material.SNOW_BLOCK, 4, p, block );

				autoBlock( isAutoBlockGlowstone(), Material.GLOWSTONE_DUST, Material.GLOWSTONE, 4, p, block );
				
				autoBlockLapis( isAutoBlockLapisBlock(), p, block );
					
			}
			
			
			if ( e.isCancelled() ) {
				// The event was canceled, so the block was successfully broke, so increment the name counter:
				
//				itemInHand;
				
				itemLoreCounter( itemInHand, ItemLoreCounters.itemLoreBlockBreakCount, 1 );
				
				
//				ArrayList<String> lore = new ArrayList<String>();
//				lore.add(ChatColor.GRAY + "Click to select server"); //Add the lore
//				meta.setLore(lore); //Set the lore to the item
//				meta.setDisplayName(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "Server Selector" + ChatColor.DARK_GRAY + "]"); //Set the display name (aka the name of the item)
//				itemInHand.setItemMeta(meta); //Set it to the item
////				player.getInventory().setItem(0, selector);
				
			}
			
		}
	}

	
	private void applyAutoEvents( TEBlockExplodeEvent e, Mine mine ) {
		
		Player p = e.getPlayer();

		if (isAreEnabledFeatures()) {
			
			
			ItemStack itemInHand = SpigotPrison.getInstance().getCompatibility().getItemInMainHand( p );
			
			
			if ( e.isCancelled() ) {
				// The event was canceled, so the block was successfully broke, so increment the name counter:
				
				//			itemInHand;
				int blocks = e.blockList().size();
				itemLoreCounter( itemInHand, ItemLoreCounters.itemLoreBlockExplodeCount, blocks );
				
				
//			ArrayList<String> lore = new ArrayList<String>();
//			lore.add(ChatColor.GRAY + "Click to select server"); //Add the lore
//			meta.setLore(lore); //Set the lore to the item
//			meta.setDisplayName(ChatColor.DARK_GRAY + "[" + ChatColor.RED + "Server Selector" + ChatColor.DARK_GRAY + "]"); //Set the display name (aka the name of the item)
//			itemInHand.setItemMeta(meta); //Set it to the item
////			player.getInventory().setItem(0, selector);
				
			}
		}			
	}

	private void itemLoreCounter( ItemStack itemInHand, ItemLoreCounters itemLore, int blocks )
	{
		if ( itemInHand.hasItemMeta() ) {

			List<String> lore = new ArrayList<>();
			
			String prisonBlockBroken = itemLore.getLore();
			
			ItemMeta meta = itemInHand.getItemMeta();
			
			if ( meta.hasLore() ) {
				lore = meta.getLore();
				
				boolean found = false;
				for( int i = 0; i < lore.size(); i++ ) {
					if ( lore.get( i ).startsWith( prisonBlockBroken ) ) {
						String val = lore.get( i ).replace( prisonBlockBroken, "" ).trim();
						
						int count = blocks;
						try {
							count += Integer.parseInt( val );
						}
						catch ( NumberFormatException e1 ) {
							Output.get().logError( "AutoManager: tool counter failure. lore= [" +
									lore.get( i ) + "] val= [" + val + "] error: " + 
									e1.getMessage() );								}
						
						lore.set( i, prisonBlockBroken + count );
						
						found = true;
						break;
					}
				}
				
				if ( !found ) {
					lore.add( prisonBlockBroken + 1 );
				}
				
				
			} else {
				lore.add( prisonBlockBroken + 1 );
			}
			
			meta.setLore( lore );
			
			itemInHand.setItemMeta( meta );
			
			// incrementCounterInName( itemInHand, meta );
			
		}
	}


	private void incrementCounterInName( ItemStack itemInHand, ItemMeta meta )
	{
		String name = meta.getDisplayName();
		if ( !meta.hasDisplayName() || name == null || name.trim().length() == 0 ) {
			name = itemInHand.getType().name().toLowerCase().replace("_", " ").trim();
			name += " [1]";
		} else {
			
			int j = name.lastIndexOf( ']' );
			if ( j == -1 ) {
				name += " [1]";
			} else {
			
				if ( j != -1 ) {
					int i = name.lastIndexOf( '[', j );
					
					if ( i != -1 ) {
						String numStr = name.substring( i + 1, j );
						
//									Output.get().logInfo( String.format( "AutoManager: name:  %s : %s ",  
//											name, numStr) );
						
						
						try {
							int blocksMined = Integer.parseInt( numStr );
							name = name.substring( 0, i ).trim() + " [" + ++blocksMined + "]";
						}
						catch ( NumberFormatException e1 ) {
							Output.get().logError( "AutoManager: tool counter failure. tool name= [" +
									name + "] error: " + 
									e1.getMessage() );
						}
						
					}
				}
			}
			
		
		}
		if ( name != null ) {
			meta.setDisplayName( name );
			itemInHand.setItemMeta( meta );
			
		}
	}



}
