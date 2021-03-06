package tech.mcprison.prison.spigot.block;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.TreeMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.vk2gpz.tokenenchant.event.TEBlockExplodeEvent;

import me.badbones69.crazyenchantments.api.events.BlastUseEvent;
import tech.mcprison.prison.Prison;
import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig;
import tech.mcprison.prison.autofeatures.AutoFeaturesFileConfig.AutoFeatures;
import tech.mcprison.prison.autofeatures.AutoFeaturesWrapper;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.mines.PrisonMines;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.mines.features.MineBlockEvent.BlockEventType;
import tech.mcprison.prison.mines.features.MineTargetPrisonBlock;
import tech.mcprison.prison.modules.Module;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.output.Output.DebugTarget;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.api.PrisonMinesBlockBreakEvent;
import tech.mcprison.prison.spigot.autofeatures.AutoManagerFeatures;
import tech.mcprison.prison.spigot.compat.Compatibility;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.spigot.integrations.IntegrationCrazyEnchantmentsPickaxes;
import tech.mcprison.prison.spigot.utils.BlockUtils;
import tech.mcprison.prison.util.Text;

public class OnBlockBreakEventCore
{

	private PrisonMines prisonMineManager;
	private boolean mineModuleDisabled = false;
	
	private int uses = 0;
	private long usesElapsedTimeNano = 0L;
	
	private boolean teExplosionTriggerEnabled;
	
	private AutoFeaturesWrapper autoFeatureWrapper = null;
	
	
	private Boolean crazyEnchantEnabled;
	
	private Random random = new Random();
	
	
	public OnBlockBreakEventCore() {
		super();
		
		this.autoFeatureWrapper = AutoFeaturesWrapper.getInstance();
		
		this.prisonMineManager = null;
		
		this.teExplosionTriggerEnabled = true;
		
		this.crazyEnchantEnabled = null;
	}
	

	public enum EventDetails {
		monitor,
		auto_manager,
		block_events_only
		;
	}
	
	
	/**
	 * <p>The Prison Mines module must be enabled, or these BlockBreakEvents should 
	 * not be enabled since they are geared to work with the prison mines.
	 * </p>
	 * 
	 * <p>At this time, prison's block handling is not supported outside of the mines.
	 * </p>
	 * 
	 * @return
	 */
	public boolean isEnabled() {
		boolean results = false;

		Optional<Module> mmOptional = Prison.get().getModuleManager().getModule( PrisonMines.MODULE_NAME );
		if ( mmOptional.isPresent() && mmOptional.get().isEnabled() ) {
			PrisonMines prisonMines = (PrisonMines) mmOptional.get();
			
			results = prisonMines != null;
		}
		
		return results;
	}

	public AutoFeaturesFileConfig getAutoFeaturesConfig() {
		return autoFeatureWrapper.getAutoFeaturesConfig();
	}

	public boolean isBoolean( AutoFeatures feature ) {
		return autoFeatureWrapper.isBoolean( feature );
	}

	public String getMessage( AutoFeatures feature ) {
		return autoFeatureWrapper.getMessage( feature );
	}

	public int getInteger( AutoFeatures feature ) {
		return autoFeatureWrapper.getInteger( feature );
	}
	
	protected List<String> getListString( AutoFeatures feature ) {
		return autoFeatureWrapper.getListString( feature );
	}
	
	
	
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

	public enum ItemLoreEnablers {
		Pickup,
		Smelt,
		Block
		;
	}
	
	
    
    
    protected void genericBlockEventMonitor( BlockBreakEvent e ) {
    	genericBlockEvent( e, true, false, false );
    }
    
    protected void genericBlockEvent( BlockBreakEvent e ) {
    	genericBlockEvent( e, false, false, false );
    }

    protected void genericBlockEventAutoManager( BlockBreakEvent e, boolean blockEventsOnly ) {
    	genericBlockEvent( e, false, blockEventsOnly, true );
    }
    
	protected void genericBlockExplodeEventMonitor( TEBlockExplodeEvent e ) {
		genericBlockExplodeEvent( e, true, false, false );
	}
	
	protected void genericBlockExplodeEvent( TEBlockExplodeEvent e, boolean blockEventsOnly ) {
		genericBlockExplodeEvent( e, false, blockEventsOnly, false );
	}
	
	protected void genericBlockExplodeEventAutoManager( TEBlockExplodeEvent e, boolean blockEventsOnly ) {
		genericBlockExplodeEvent( e, false, blockEventsOnly, true );
	}
	

	protected void genericBlockExplodeEventMonitor( BlastUseEvent e ) {
		genericBlockExplodeEvent( e, true, false, false );
	}
	
	protected void genericBlockExplodeEvent( BlastUseEvent e, boolean blockEventsOnly ) {
		genericBlockExplodeEvent( e, false, blockEventsOnly, false );
	}

	protected void genericBlockExplodeEventAutoManager( BlastUseEvent e, boolean blockEventsOnly ) {
		genericBlockExplodeEvent( e, true, blockEventsOnly, true );
	}
	
    /**
     * <p>This genericBlockEvent handles the basics of a BlockBreakEvent to see if it has happened
     * within a mine or not.  If it is happening within a mine, then we process it with the doAction()
     * function.
     * </p>
     * 
     * <p>For this class only. the doAction() is only counting the block break event, but does
     * nothing with the actual block such that there is no need to have knowledge as to if 
     * it is a custom block.  In other doAction() functions that would exist in other classes 
     * that extend from this one, it may need that information.  The hooks to find the custom
     * blocks is within the Block's getPrisonBlock() function. 
     * </p>
     * 
     * @param e
     * @param montior Identifies that a monitor event called this function.  A monitor should only record
     * 					block break counts.
     */
	protected void genericBlockEvent( BlockBreakEvent e, boolean monitor, boolean blockEventsOnly, 
			boolean autoManager ) {
		
		if ( e instanceof PrisonMinesBlockBreakEvent ) {
			return;
		}
		
		// Register all external events such as mcMMO and EZBlocks:
		OnBlockBreakExternalEvents.getInstance().registerAllExternalEvents();
		
		
		String debugInfo = String.format( "### ** genericBlockEvent ** ### %s%s%s%s ",
				(autoManager ? "autoManager " : ""),
				(e.isCancelled() ? "CANCELED " : ""),
				(monitor ? "MONITOR " : ""), (blockEventsOnly ? "BlockEventsOnly" : "" ));
		
		
		// NOTE that check for auto manager has happened prior to accessing this function.
    	if ( !monitor && !e.isCancelled() || monitor ) 
    	{
    		
//    		boolean isAir = e.getBlock().getType() == null || e.getBlock().getType() == Material.AIR;

    		// Need to wrap in a Prison block so it can be used with the mines:
    		SpigotBlock block = new SpigotBlock(e.getBlock());
    		
    		Long playerUUIDLSB = Long.valueOf( e.getPlayer().getUniqueId().getLeastSignificantBits() );
    		
    		// Get the cached mine, if it exists:
    		Mine mine = getPlayerCache().get( playerUUIDLSB );
    		
    		if ( mine == null || !mine.isInMineExact( block.getLocation() ) ) {
    			// Look for the correct mine to use. 
    			// Set mine to null so if cannot find the right one it will return a null:
    			mine = findMineLocation( block );
    			
    			// Store the mine in the player cache if not null:
    			if ( mine != null ) {
    				getPlayerCache().put( playerUUIDLSB, mine );
    			}
    		}
    		
    		debugInfo += "mine=" + (mine == null ? "none" : mine.getName()) + " ";
    		

    		if ( mine != null && BlockUtils.getInstance().isUnbreakable( block ) ) {
    			// The block is unbreakable because a utility has it locked:
    			
    			e.setCancelled( true );
    			debugInfo += "UNBREAKABLE_BLOCK_UTILS (event canceled) ";
    		}
    		else if ( mine != null && (mine.isMineAccessByRank() || mine.isAccessPermissionEnabled()) && 
    					!mine.hasMiningAccess( new SpigotPlayer( e.getPlayer() ) ) ) {
    			// The player does not have permission to access this mine, so do not process 
    			// 
    			
    			e.setCancelled( true );
    			debugInfo += "ACCESS_DENIED (event canceled) ";
    		}
    		else if ( blockEventsOnly ) {
    			
    			String triggered = null;
    			
    			doActionBlockEventOnly( block, mine, e.getPlayer(), BlockEventType.blockBreak, triggered );

    			debugInfo += "(actionBlockEventOnly) ";
    		}
    		else if ( monitor && mine == null ) {
    			// bypass all processing since the block break is outside any mine:
    			
    			debugInfo += "(bypassed monitor no mine) ";
    		}
    		else if ( monitor && mine != null ) {
    			
    			doActionMonitor( block, mine );
    			
    			debugInfo += "(monitor) ";
    		}
    		
    		// This is where the processing actually happens:
    		else if ( mine != null || mine == null && !isBoolean( AutoFeatures.pickupLimitToMines ) ) {
    			
    			// Set the mine's PrisonBlockTypes for the block. Used to identify custom blocks.
    			// Needed since processing of the block will lose track of which mine it came from.
    			if ( mine != null ) {
    				block.setPrisonBlockTypes( mine.getPrisonBlockTypes() );
    			}
    			
    			// check all external events such as mcMMO and EZBlocks:
    			OnBlockBreakExternalEvents.getInstance().checkAllExternalEvents( e );
    			
    			List<SpigotBlock> explodedBlocks = new ArrayList<>();
    			String triggered = null;
    			
    			PrisonMinesBlockBreakEvent pmbbEvent = new PrisonMinesBlockBreakEvent( e.getBlock(), e.getPlayer(),
    												mine, block, explodedBlocks, BlockEventType.blockBreak, triggered );
                Bukkit.getServer().getPluginManager().callEvent(pmbbEvent);
                if ( pmbbEvent.isCancelled() ) {
                	debugInfo += "(normal processing: PrisonMinesBlockBreakEvent canceld) ";
                }
                else {
                	
                	// doAction returns a boolean that indicates if the event should be canceled or not:
                	if ( doAction( block, mine, e.getPlayer() ) &&
                			!isBoolean( AutoFeatures.isDebugSupressOnBlockBreakEventCancels )) {
                		
                		e.setCancelled( true );
                	}
                }
    			
    			
    			debugInfo += "(normal processing) ";
    		}
    		else {
    			
    			debugInfo += "(logic bypass) ";
    		}
    		
    	}
    	
    	Output.get().logDebug( DebugTarget.blockBreak, debugInfo );
	}


	/**
	 * <p>Since there are multiple blocks associated with this event, pull out the player first and
	 * get the mine, then loop through those blocks to make sure they are within the mine.
	 * </p>
	 * 
	 * <p>The logic in this function is slightly different compared to genericBlockEvent() because this
	 * event contains multiple blocks so it's far more efficient to process the player data once. 
	 * So that basically needed a slight refactoring.
	 * </p>
	 * 
	 * @param e
	 */
	private void genericBlockExplodeEvent( TEBlockExplodeEvent e, boolean monitor, boolean blockEventsOnly, 
			boolean autoManager ) {

		// Register all external events such as mcMMO and EZBlocks:
		OnBlockBreakExternalEvents.getInstance().registerAllExternalEvents();
		
		
		String debugInfo = String.format( "### ** genericBlockExplodeEvent(TEBlockExplodeEvent) ** ### %s%s%s%s ",
				(autoManager ? "autoManager " : ""),
				(e.isCancelled() ? "CANCELED " : ""),
				(monitor ? "MONITOR " : ""), (blockEventsOnly ? "BlockEventsOnly" : "" ));
		


		// NOTE that check for auto manager has happened prior to accessing this function.
    	if ( !monitor && !e.isCancelled() || monitor ) {
    		
			List<SpigotBlock> explodedBlocks = new ArrayList<>();

			// Need to wrap in a Prison block so it can be used with the mines:
			SpigotBlock block = new SpigotBlock(e.getBlock());
    		
    		// long startNano = System.nanoTime();
    		Long playerUUIDLSB = Long.valueOf( e.getPlayer().getUniqueId().getLeastSignificantBits() );
    		
    		// Get the cached mine, if it exists:
    		Mine mine = getPlayerCache().get( playerUUIDLSB );
    		
    		if ( mine == null || !mine.isInMineExact( block.getLocation() ) ) {
    			
				
				// Look for the correct mine to use. 
				// Set mine to null so if cannot find the right one it will return a null:
				mine = findMineLocation( block );
				
				// Store the mine in the player cache if not null:
				if ( mine != null ) {
					getPlayerCache().put( playerUUIDLSB, mine );
					
					// we found the mine!
				}
    			
    		}
    		
    		debugInfo += "mine=" + (mine == null ? "none" : mine.getName()) + " ";
    		
    		boolean isTEExplosiveEnabled = isBoolean( AutoFeatures.isProcessTokensEnchantExplosiveEvents );
    		
    		if ( mine != null && BlockUtils.getInstance().isUnbreakable( block ) ) {
    			// The block is unbreakable because a utility has it locked:
    			
    			e.setCancelled( true );
    			debugInfo += "UNBREAKABLE_BLOCK_UTILS (event canceled) ";
    		}
    		else if ( mine != null && (mine.isMineAccessByRank() || mine.isAccessPermissionEnabled()) && 
    					!mine.hasMiningAccess( new SpigotPlayer( e.getPlayer() ) ) ) {
    			// The player does not have permission to access this mine, so do not process 
    			// 
    			
    			e.setCancelled( true );
    			debugInfo += "ACCESS_DENIED (event canceled) ";
    		}
    		else if ( blockEventsOnly ) {
    			
    			if ( mine != null ) {
    				
    				String triggered = null;
    				
    				doActionBlockEventOnly( block, mine, e.getPlayer(), BlockEventType.blockBreak, triggered );
    				
    	   			// All other blocks in the explosion:
        			for ( Block blk : e.blockList() ) {
        				
        				// Need to wrap in a Prison block so it can be used with the mines.
        				// Since this is a monitor, there is no need to check to see if the
        				// block is in a mine since the getTargetPrisonBlock function will 
        				// perform that check indirectly.
        				SpigotBlock sBlock = new SpigotBlock(blk);
        				
        				if ( !BlockUtils.getInstance().isUnbreakable( sBlock ) ) {
        	    			
        					doActionBlockEventOnly( sBlock, mine, e.getPlayer(), BlockEventType.blockBreak, triggered );
        	    		}
        			}

    			}
    			
    			debugInfo += "(actionBlockEventOnly) ";
    		}
    		else if ( monitor && mine == null ) {
    			// bypass all processing since the block break is outside any mine:
    			
    			debugInfo += "(bypassed monitor no mine) ";
    		}
    		else if ( monitor && mine != null ) {

    			// Initial block that was hit:
    			doActionMonitor( block, mine );
    			
    			// All other blocks in the explosion:
    			for ( Block blk : e.blockList() ) {
    				
    				// Need to wrap in a Prison block so it can be used with the mines.
    				// Since this is a monitor, there is no need to check to see if the
    				// block is in a mine since the getTargetPrisonBlock function will 
    				// perform that check indirectly.
    				SpigotBlock sBlock = new SpigotBlock(blk);
    				
    				if ( !BlockUtils.getInstance().isUnbreakable( sBlock ) ) {
    					
    					doActionMonitor( sBlock, mine );
    				}
    			}

    			debugInfo += "(monitor) ";
    		}

    		
    		// now process all blocks (non-monitor):
    		else if ( isTEExplosiveEnabled && 
    				( mine != null || mine == null && !isBoolean( AutoFeatures.pickupLimitToMines )) ) {
    			
    			// have to go through all blocks since some blocks may be outside the mine.
    			// but terminate search upon first find:
   			
    			for ( Block blk : e.blockList() ) {
    				//boolean isAir = blk.getType() != null && blk.getType() == Material.AIR;
    				
    				// Need to wrap in a Prison block so it can be used with the mines:
    				SpigotBlock sBlock = new SpigotBlock(blk);
    				
    				
    				if ( !BlockUtils.getInstance().isUnbreakable( sBlock ) && 
    							mine.isInMineExact( sBlock.getLocation() ) ) {
    					
    					explodedBlocks.add( sBlock );
    					
    					
    					// check all external events such as mcMMO and EZBlocks:
    					OnBlockBreakExternalEvents.getInstance().checkAllExternalEvents( e.getPlayer(), blk );
    				}
    				
    			}
    			
    			if ( explodedBlocks.size() > 0 ) {
    				
					String triggered = checkCEExplosionTriggered( e );
					

					PrisonMinesBlockBreakEvent pmbbEvent = new PrisonMinesBlockBreakEvent( e.getBlock(), e.getPlayer(),
	    												mine, block, explodedBlocks, BlockEventType.TEXplosion, triggered );
	                Bukkit.getServer().getPluginManager().callEvent(pmbbEvent);
	                if ( pmbbEvent.isCancelled() ) {
	                	debugInfo += "(normal processing: PrisonMinesBlockBreakEvent canceld) ";
	                }
	                else {
	                	
	                	// This is where the processing actually happens:
	                	if ( doAction( mine, e.getPlayer(), explodedBlocks, BlockEventType.TEXplosion, triggered ) && 
	                			!isBoolean( AutoFeatures.isDebugSupressOnTEExplodeEventCancels )) {
	                		
	                		e.setCancelled( true );
	                	}
	                }
    				
    			}
    			
    			debugInfo += "(normal processing) ";
   			}
    		else {
    			
    			debugInfo += "(logic bypass) ";
    		}
    			
    	}
    	
    	Output.get().logDebug( DebugTarget.blockBreak, debugInfo );
	}



	private String checkCEExplosionTriggered( TEBlockExplodeEvent e )
	{
		String triggered = null;
		
		// Please be aware:  This function is named the same as the auto features setting, but this is 
		// not related.  This is only trying to get the name of the enchantment that triggered the event.
		if ( isTeExplosionTriggerEnabled() ) {
			
			try {
				triggered = e.getTrigger();
			}
			catch ( Exception | NoSuchMethodError ex ) {
				// Only print the error the first time, then suppress the error:
				String error = ex.getMessage();
				
				Output.get().logError( "Error: Trying to access the TEBlockExplodeEvent.getTrigger() " +
						"function.  Make sure you are using TokenEnchant v18.11.0 or newer. The new " +
						"getTrigger() function returns the TE Plugin that is firing the TEBlockExplodeEvent. " +
						"The Prison BlockEvents can be filtered by this triggered value. " +
						error );
				
				// Disable collecting the trigger.
				setTeExplosionTriggerEnabled( false );
				
			}
		}
		
		return triggered;
	}

	
	
	/**
	 * <p>Since there are multiple blocks associated with this event, pull out the player first and
	 * get the mine, then loop through those blocks to make sure they are within the mine.
	 * </p>
	 * 
	 * <p>The logic in this function is slightly different compared to genericBlockEvent() because this
	 * event contains multiple blocks so it's far more efficient to process the player data once. 
	 * So that basically needed a slight refactoring.
	 * </p>
	 * 
	 * @param e
	 */
	protected void genericBlockExplodeEvent( BlastUseEvent e, boolean monitor, boolean blockEventsOnly, 
			boolean autoManager ) {

		// Register all external events such as mcMMO and EZBlocks:
		OnBlockBreakExternalEvents.getInstance().registerAllExternalEvents();
				
		
		String debugInfo = String.format( "### ** genericBlockExplodeEvent(BlastUseEvent) ** ### %s%s%s%s ",
				(autoManager ? "autoManager " : ""),
				(e.isCancelled() ? "CANCELED " : ""),
				(monitor ? "MONITOR " : ""), (blockEventsOnly ? "BlockEventsOnly" : "" ));
		


		// NOTE that check for auto manager has happened prior to accessing this function.
    	if ( (!monitor && !e.isCancelled() || monitor) && 
				e.getBlockList().size() > 0 ) {
		
			List<SpigotBlock> explodedBlocks = new ArrayList<>();

			
			// long startNano = System.nanoTime();
			Long playerUUIDLSB = Long.valueOf( e.getPlayer().getUniqueId().getLeastSignificantBits() );
			
			// Get the cached mine, if it exists:
			Mine mine = getPlayerCache().get( playerUUIDLSB );
			
			if ( mine == null ) {
				

				// NOTE: The crazy enchantment's blast use event does not identify the block that
				//       was hit, so will have to check them all.
				
				// have to go through all blocks since some blocks may be outside the mine.
				// but terminate search upon first find:
				for ( Block blk : e.getBlockList() ) {
					// Need to wrap in a Prison block so it can be used with the mines:
					SpigotBlock block = new SpigotBlock(blk);
					
					// Look for the correct mine to use. 
					// Set mine to null so if cannot find the right one it will return a null:
					mine = findMineLocation( block );
					
					// Store the mine in the player cache if not null:
					if ( mine != null ) {
						getPlayerCache().put( playerUUIDLSB, mine );
						
						// we found the mine!
						break;
					}
				}
			}
    		else {
    			
    			// NOTE: Just because the mine is not null, does not mean that the block was tested to be
    			//       within the mine. The mine from the player cache could be a different mine
    			//       altogether.  The block must be tested.
    			
				// have to go through all blocks since some blocks may be outside the mine.
				// but terminate search upon first find:
				for ( Block blk : e.getBlockList() ) {
					// Need to wrap in a Prison block so it can be used with the mines:
					SpigotBlock block = new SpigotBlock(blk);
					
					// Look for the correct mine to use. 
					// Set mine to null so if cannot find the right one it will return a null:
					mine = findMineLocation( block );
					
					// Store the mine in the player cache if not null:
					if ( mine != null ) {
						getPlayerCache().put( playerUUIDLSB, mine );
						
						// we found the mine!
						break;
					}
				}

    		}

			debugInfo += "mine=" + (mine == null ? "none" : mine.getName()) + " ";
			
			boolean isCEBlockExplodeEnabled = isBoolean( AutoFeatures.isProcessCrazyEnchantsBlockExplodeEvents );
    		
			if ( mine != null && (mine.isMineAccessByRank() || mine.isAccessPermissionEnabled()) && 
						!mine.hasMiningAccess( new SpigotPlayer( e.getPlayer() ) ) ) {
    			// The player does not have permission to access this mine, so do not process 
    			// 

				e.setCancelled( true );
    			debugInfo += "ACCESS_DENIED (event canceled) ";
    		}
    		else if ( blockEventsOnly ) {
    			
				if ( mine != null ) {
					
					String triggered = null;
		   			
	    			// All other blocks in the explosion:
	    			for ( Block blk : e.getBlockList() ) {
	    				
	    				// Need to wrap in a Prison block so it can be used with the mines.
	    				// Since this is a monitor, there is no need to check to see if the
	    				// block is in a mine since the getTargetPrisonBlock function will 
	    				// perform that check indirectly.
	    				SpigotBlock sBlock = new SpigotBlock(blk);
	    				
	    				if ( !BlockUtils.getInstance().isUnbreakable( sBlock ) ) {
	    					
	    					doActionBlockEventOnly( sBlock, mine, e.getPlayer(), BlockEventType.CEXplosion, triggered );
	    				}
	    			}
				}

				debugInfo += "(actionBlockEventOnly) ";
    		}
    		else if ( monitor && mine == null ) {
    			// bypass all processing since the block break is outside any mine:
    			
    			debugInfo += "(bypassed monitor no mine) ";
    		}
    		else if ( monitor && mine != null ) {

    			// Initial block that was hit:  CrazyE does not have the main block:
    			// doActionMonitor( block, mine );
    			
    			// All other blocks in the explosion:
    			for ( Block blk : e.getBlockList() ) {
    				
    				// Need to wrap in a Prison block so it can be used with the mines.
    				// Since this is a monitor, there is no need to check to see if the
    				// block is in a mine since the getTargetPrisonBlock function will 
    				// perform that check indirectly.
    				SpigotBlock sBlock = new SpigotBlock(blk);
    				
    				if ( !BlockUtils.getInstance().isUnbreakable( sBlock ) ) {
    					
    					doActionMonitor( sBlock, mine );
    				}
    			}

    			debugInfo += "(monitor) ";
    		}

    		// now process all blocks (non-monitor):
    		else if ( isCEBlockExplodeEnabled && 
    				( mine != null || mine == null && !isBoolean( AutoFeatures.pickupLimitToMines )) ) {
    			
    			// have to go through all blocks since some blocks may be outside the mine.
    			// but terminate search upon first find:
   			
    			for ( Block blk : e.getBlockList() ) {
    				
    				// Need to wrap in a Prison block so it can be used with the mines:
    				SpigotBlock sBlock = new SpigotBlock(blk);
    				
    				if ( !BlockUtils.getInstance().isUnbreakable( sBlock ) && 
    						mine.isInMineExact( sBlock.getLocation() ) ) {
    					
    					explodedBlocks.add( sBlock );
    					
    						
    					// check all external events such as mcMMO and EZBlocks:
    					OnBlockBreakExternalEvents.getInstance().checkAllExternalEvents( e.getPlayer(), blk );
    				}
    				
    			}
    			if ( explodedBlocks.size() > 0 ) {
    				
					String triggered = null;
    				
					
					// Warning: BlastUseEvent does not identify the block the player actually hit, so the dummyBlock
					//          is just a random first block from the explodedBlocks list and may not be the block
					//          that initiated the explosion event.
					SpigotBlock dummyBlock = explodedBlocks.get( 0 );
					
	    			PrisonMinesBlockBreakEvent pmbbEvent = new PrisonMinesBlockBreakEvent( dummyBlock.getWrapper(), e.getPlayer(),
	    												mine, dummyBlock, explodedBlocks, BlockEventType.CEXplosion, triggered );
	                Bukkit.getServer().getPluginManager().callEvent(pmbbEvent);
	                if ( pmbbEvent.isCancelled() ) {
	                	debugInfo += "(normal processing: PrisonMinesBlockBreakEvent canceld) ";
	                }
	                else {
	                	
	                	// This is where the processing actually happens:
	                	if ( doAction( mine, e.getPlayer(), explodedBlocks, BlockEventType.CEXplosion, triggered ) && 
	                			!isBoolean( AutoFeatures.isDebugSupressOnCEBlastUseEventCancels )) {
	                		
	                		e.setCancelled( true );
	                	}
	                	
	                }
    			}
    			debugInfo += "(normal processing) ";
    		}
    		else {
    			
    			debugInfo += "(logic bypass) ";
    		}

		}
    	
    	Output.get().logDebug( DebugTarget.blockBreak, debugInfo );

	}
	
	
	public void doActionMonitor( SpigotBlock block, Mine mine ) {
		if ( mine != null ) {
			
			// Good chance the block was already counted, but just in case it wasn't:
			MineTargetPrisonBlock targetBlock = mine.getTargetPrisonBlock( block );
			// Record the block break:
			mine.incrementBlockMiningCount( targetBlock );

			// Never process BlockEvents in a monitor.
			
			// submit a mine sweeper task.  It will only run if it is enabled and another 
			// mine sweeper task has not been submitted.
			mine.submitMineSweeperTask();

			// Checks to see if the mine ran out of blocks, and if it did, then
			// it will reset the mine:
			mine.checkZeroBlockReset();
		}
	}
	
	public void doActionBlockEventOnly( SpigotBlock spigotBlock, Mine mine, Player player, 
													BlockEventType blockEventType, String triggered ) {
		if ( mine != null && spigotBlock != null ) {
			
			MineTargetPrisonBlock targetBlock = mine.getTargetPrisonBlock( spigotBlock );
			
			if ( targetBlock != null && targetBlock.getPrisonBlock() != null ) {
				
//				String targetBlockName =  mine == null ? 
//						spigotBlock.getPrisonBlock().getBlockName()
//						: targetBlock.getPrisonBlock().getBlockName();
						
				// Process mine block break events:
				SpigotPlayer sPlayer = new SpigotPlayer( player );
				
				PrisonBlock prisonBlock = spigotBlock.getPrisonBlock();
				
				mine.processBlockBreakEventCommands( prisonBlock, targetBlock, sPlayer, blockEventType, triggered );
			}
		}
	}
	
	
	public boolean doAction( SpigotBlock spigotBlock, Mine mine, Player player ) {
		boolean cancel = false;
		
		SpigotItemStack itemInHand = SpigotPrison.getInstance().getCompatibility().getPrisonItemInMainHand( player );
		
		AutoManagerFeatures aMan = SpigotPrison.getInstance().getAutoFeatures();
		
		
		// Do not have to check if auto manager is enabled because it isn't if it's calling this function:
//			boolean isAutoManagerEnabled = aMan.isBoolean( AutoFeatures.isAutoManagerEnabled );
		boolean isProcessNormalDropsEnabled = isBoolean( AutoFeatures.handleNormalDropsEvents );
		
		
		if ( isProcessNormalDropsEnabled ) {
			
			// Drop the contents of the individual block breaks
			int drop = aMan.calculateNormalDrop( itemInHand, spigotBlock );
			
			if ( drop > 0 ) {
				
				aMan.processBlockBreakage( spigotBlock, mine, player, drop, BlockEventType.blockBreak,
						null, itemInHand );
				
				cancel = true;
				
				aMan.autosellPerBlockBreak( player );
			}
			
		}
		else {
			
			aMan.processBlockBreakage( spigotBlock, mine, player, 1, BlockEventType.blockBreak, null,
					itemInHand );
			
			cancel = true;
		}
		
		if ( mine != null ) {
			aMan.checkZeroBlockReset( mine );
		}
		
		return cancel;
	}
	
	
	/**
	 * <p>This function is processed when auto manager is disabled and process token enchant explosions
	 * is enabled.  This function is overridden in AutoManager when auto manager is enabled.
	 * </p>
	 * 
	 * 
	 * @param mine
	 * @param e
	 * @param teExplosiveBlocks
	 */
	public boolean doAction( Mine mine, Player player, List<SpigotBlock> explodedBlocks, 
									BlockEventType blockEventType, String triggered ) {
		boolean cancel = false;
	
		int totalCount = 0;
		
		SpigotItemStack itemInHand = SpigotPrison.getInstance().getCompatibility().getPrisonItemInMainHand( player );
		
		AutoManagerFeatures aMan = SpigotPrison.getInstance().getAutoFeatures();
		
		
		// The explodedBlocks list have already been validated as being within the mine:
		for ( SpigotBlock spigotBlock : explodedBlocks ) {
			
			// Drop the contents of the individual block breaks
			int drop = aMan.calculateNormalDrop( itemInHand, spigotBlock );
			totalCount += drop;
			
			if ( drop > 0 ) {
				
				aMan.processBlockBreakage( spigotBlock, mine, player, drop, 
						blockEventType, triggered, itemInHand );
				
				aMan.autosellPerBlockBreak( player );
			}
			
		}
		
		if ( mine != null ) {
			aMan.checkZeroBlockReset( mine );
		}
		
		if ( totalCount > 0 ) {
			cancel = true;
		}
		
		return cancel;
	}
	

	
//	/**
//	 * <p>This function is processed when auto manager is disabled and process crazy enchant explosions
//	 * is enabled.  This function is overridden in AutoManager when auto manager is enabled.
//	 * </p>
//	 * 
//	 * 
//	 * @param mine
//	 * @param e
//	 * @param teExplosiveBlocks
//	 */
//	public void doAction( Mine mine, BlastUseEvent e, List<SpigotBlock> explodedBlocks ) {
//	
//		if ( mine == null || mine != null && !e.isCancelled() ) {
//			
//			int totalCount = 0;
//			
//			
//			SpigotItemStack itemInHand = SpigotPrison.getInstance().getCompatibility().getPrisonItemInMainHand( e.getPlayer() );
//			
//			AutoManagerFeatures aMan = SpigotPrison.getInstance().getAutoFeatures();
//			
//			// Do not have to check if auto manager is enabled because it isn't if it's calling this function:
////			boolean isAutoManagerEnabled = aMan.isBoolean( AutoFeatures.isAutoManagerEnabled );
//			boolean isCEBlockExplodeEnabled = isBoolean( AutoFeatures.isProcessCrazyEnchantsBlockExplodeEvents );
//			
//			
//			if ( isCEBlockExplodeEnabled ) {
//				
////				StringBuilder sb = new StringBuilder();
////				for ( SpigotBlock spigotBlock : explodedBlocks )
////				{
////					sb.append( spigotBlock.toString() ).append( " " );
////				}
//				
////				Output.get().logInfo( "#### OnBlockBreakEventListener.doAction: BlastUseEvent: :: " + mine.getName() + "  e.blocks= " + 
////						e.getBlockList().size() + "  blockSize : " + explodedBlocks.size() + 
////						"  blocks remaining= " + 
////						mine.getRemainingBlockCount() + " [" + sb.toString() + "]"
////						);
//				
//				// The CrazyEnchants block list have already been validated as being within the mine:
//				for ( SpigotBlock spigotBlock : explodedBlocks ) {
//					
//					// Drop the contents of the individual block breaks
//					int drop = aMan.calculateNormalDrop( itemInHand, spigotBlock );
//					totalCount += drop;
//					
//					if ( drop > 0 ) {
//						
//						aMan.processBlockBreakage( spigotBlock, mine, e.getPlayer(), drop, BlockEventType.CEXplosion, null,
//												itemInHand );
//						
//					}
//				}
//				
//				if ( mine != null ) {
//					aMan.checkZeroBlockReset( mine );
//				}
//				
//				if ( totalCount > 0 ) {
//					
//					// Set the broken block to AIR and cancel the event
//					e.setCancelled(true);
//					
//				}
//				
//			}
//			
//		}
//	}
	
//	private void doActionMonitor( Mine mine, BlastUseEvent e, List<SpigotBlock> explodedBlocks ) {
//		if ( mine != null ) {
//			
//			// Checks to see if the mine ran out of blocks, and if it did, then
//			// it will reset the mine:
//			mine.checkZeroBlockReset();
//		}
//	}
	

	
	public void processBlockBreakage( SpigotBlock spigotBlock, 
			Mine mine, Player player, int count,
			BlockEventType blockEventType, String triggered, SpigotItemStack itemInHand )
	{
		MineTargetPrisonBlock targetBlock = null;
		
		if ( mine != null ) {
			targetBlock = mine.getTargetPrisonBlock( spigotBlock );
		}
		
		
		// If this block is not in the mine (if null) and it has not been broke before
		// and wasn't originally air, then process the breakage:
		if ( mine == null || targetBlock != null && !targetBlock.isAirBroke() ) {
		
			String targetBlockName =  mine == null ? 
							spigotBlock.getPrisonBlock().getBlockName()
								: targetBlock.getPrisonBlock().getBlockName();
			
			// Process mine block break events:
			SpigotPlayer sPlayer = new SpigotPlayer( player );
			
			
			int bonusXp = checkCrazyEnchant( player, spigotBlock.getWrapper(), ( itemInHand == null ? null : itemInHand.getBukkitStack()) );
			
			// Calculate XP on block break if enabled:
			calculateAndGivePlayerXP( sPlayer, targetBlockName, count, bonusXp );
			
			// calculate durability impact: Include item durability resistance.
			if ( isBoolean( AutoFeatures.isCalculateDurabilityEnabled ) ) {
				
				// value of 0 = normal durability. Value 100 = never calculate durability.
				int durabilityResistance = 0;
				if ( isBoolean( AutoFeatures.loreDurabiltyResistance ) ) {
					durabilityResistance = getDurabilityResistance( itemInHand,
							getMessage( AutoFeatures.loreDurabiltyResistanceName ) );
				}
				
				calculateAndApplyDurability( player, itemInHand, durabilityResistance );
			}
			
			
			// A block was broke... so record that event on the tool:	
			itemLoreCounter( itemInHand, getMessage( AutoFeatures.loreBlockBreakCountName ), 1 );
			
			
			if ( mine != null ) {
				// Record the block break:
				mine.incrementBlockMiningCount( targetBlock );
				
				PrisonBlock prisonBlock = spigotBlock.getPrisonBlock();
				
				mine.processBlockBreakEventCommands( prisonBlock,
										targetBlock, sPlayer, blockEventType, triggered );
			}
			
		}
	}
	
	protected void calculateAndGivePlayerXP(SpigotPlayer player, String blockName, 
					int count, int bonusXp ) {

		if (isBoolean(AutoFeatures.isCalculateXPEnabled) && blockName != null ) {

//			String blockName = block.getPrisonBlock() == null ? null : block.getPrisonBlock().getBlockName();

			if ( blockName != null ) {

				int xp = bonusXp;
				for ( int i = 0; i < count; i++ ) {
					xp += calculateXP( blockName );
				}

				if (xp > 0) {

					if ( isBoolean( AutoFeatures.givePlayerXPAsOrbDrops )) {

						player.dropXPOrbs( xp );
//						tech.mcprison.prison.util.Location dropPoint = player.getLocation().add( player.getLocation().getDirection());
//						((ExperienceOrb) player.getWorld().spawn(dropPoint, ExperienceOrb.class)).setExperience(xp);
					}
					else {
						player.giveExp( xp );
					}
				}
			}
		}
	}
	
	

	
	/**
	 * <p>This calculate xp based upon the block that is broken.
	 * Fortune does not increase XP that a block drops.
	 * </p>
	 *
	 * <ul>
	 *   <li>Coal Ore: 0 - 2</li>
	 *   <li>Nether Gold Ore: 0 - 1</li>
	 *   <li>Diamond Ore, Emerald Ore: 3 - 7</li>
	 *   <li>Lapis Luzuli Ore, Nether Quartz Ore: 2 - 5</li>
	 *   <li>Redstone Ore: 1 - 5</li>
	 *   <li>Monster Spawner: 15 - 43</li>
	 * </ul>
	 *
	 * @param Block
	 * @return
	 */
	private int calculateXP( String blockName ) {
		int xp = 0;

		switch (blockName.toLowerCase()) {
			case "coal_ore":
			case "coal":
				xp = getRandom().nextInt( 2 );
				break;

			case "nether_gold_ore":
				xp = getRandom().nextInt( 1 );
				break;

			case "diamond_ore":
			case "emerald_ore":
				xp = getRandom().nextInt( 4 ) + 3;
				break;

			case "lapis_ore":
			case "nether_quartz_ore":
				xp = getRandom().nextInt( 3 ) + 2;
				break;

			case "redstone_ore":
				xp = getRandom().nextInt( 4 ) + 1;
				break;

			case "spawn":
				xp = getRandom().nextInt( 28 ) + 15;
				break;

			default:
				break;
		}

		return xp;
	}

	/**
	 * <p>This function will search for the loreDurabiltyResistanceName within the
	 * item in the hand, if found it will return the number if it exists.  If not
	 * found, then it will return a value of zero, indicating that no special resistance
	 * exists, and that durability should be applied as normal.
	 * </p>
	 *
	 * <p>If there is no value after the lore name, then the default is 100 %.
	 * If a value follows the lore name, then it must be an integer.
	 * If it is less than 0, then 0. If it is greater than 100, then 100.
	 * </p>
	 *
	 * @param itemInHand
	 * @param itemLore
	 * @return
	 */
	protected int getDurabilityResistance(SpigotItemStack itemInHand, String itemLore) {
		int results = 0;

		if ( itemInHand != null && itemInHand.getBukkitStack().hasItemMeta() ) {

			List<String> lore = new ArrayList<>();

			itemLore = itemLore.trim() + " ";

			itemLore = Text.translateAmpColorCodes( itemLore.trim() + " ");

//			String prisonBlockBroken = itemLore.getLore();

			ItemMeta meta = itemInHand.getBukkitStack().getItemMeta();

			if (meta.hasLore()) {
				lore = meta.getLore();

				for (String s : lore) {
					if (s.startsWith(itemLore)) {

						// It has the durability resistance lore, so set the results to 100.
						// If a value is set, then it will be replaced.
						results = 100;

						String val = s.replace(itemLore, "").trim();

						try {
							results += Integer.parseInt(val);
						} catch (NumberFormatException e1) {
							Output.get().logError("AutoManager: tool durability failure. lore= [" + s + "] val= [" + val + "] error: " + e1.getMessage());
						}

						break;
					}
				}
			}

			if ( results > 100d ) {
				results = 100;
			}
			else if ( results < 0 ) {
				results = 0;
			}

		}

		return results;
	}

	

	/**
	 * <p>This should calculate and apply the durability consumption on the tool.
	 * </p>
	 * 
	 * <p>This only applies to to the use of tools. If the player's hand is empty (AIR),
	 * or the item in hand has no max durability, then it's not a tool so it skips 
	 * this processing.
	 * </p>
	 *
	 * <p>The damage is calculated as a value of one durability, but all damage can be
	 * skipped if the tool has a durability enchantment.  If it does, then there is a
	 * percent chance of 1 in (1 + durabilityLevel).  So if a tool has a durability level
	 * of 1, then there is a 50% chance. Level of 2, then a 66.6% chance. Level of 3 has
	 * a 75% chance. And a level of 9 has a 90% chance. There are no upper limits on
	 * durability enchantment levels.
	 * </p>
	 *
	 * <p>Some blocks may have a damage of greater than 1, but for now, this
	 * does not take that in to consideration. If hooking up in the future, just
	 * set the initial damage to the correct value based upon block type that was mined.
	 * </p>
	 *
	 * <p>The parameter durabilityResistance is optional, to disable use a value of ZERO.
	 * This is a percentage and is calculated first.  If random value is equal to the parameter
	 * or less, then it will skip the durability calculations for the current event.
	 * </p>
	 *
	 * <p>Based upon the following URL.  See Tool Durability.
	 * </p>
	 * https://minecraft.gamepedia.com/Item_durability
	 *
	 * @param player
	 * @param itemInHand - Should be the tool they used to mine or dig the block
	 * @param durabilityResistance - Chance to prevent durability wear being applied.
	 * 			Zero always disables this calculation and allows normal durability calculations
	 * 			to be performed. 100 always prevents wear.
	 */
	protected void calculateAndApplyDurability(Player player, SpigotItemStack itemInHand, int durabilityResistance) {

		if ( itemInHand != null && !itemInHand.isAir() ) {
			
			Compatibility compat = SpigotPrison.getInstance().getCompatibility();
			int maxDurability = compat.getDurabilityMax( itemInHand );
			int durability = compat.getDurability( itemInHand );
			
			short damage = 1;  // Generally 1 unless instant break block then zero.
			
			int durabilityLevel = 0;
			boolean toolBreak = false;
			
			// Need to skip processing on empty item stacks and items that have no max durability
			if ( maxDurability > 0 ) {
				
				
				if ( durabilityResistance >= 100 ) {
					damage = 0;
				} else if ( durabilityResistance > 0 ) {
					if ( getRandom().nextInt( 100 ) <= durabilityResistance ) {
						damage = 0;
					}
				}
				
				if ( damage > 0 && itemInHand.getBukkitStack().containsEnchantment( Enchantment.DURABILITY)) {
					durabilityLevel = itemInHand.getBukkitStack().getEnchantmentLevel( Enchantment.DURABILITY );
					
					// the chance of losing durability is 1 in (1+level)
					// So if the random int == 0, then take damage, otherwise none.
					if (getRandom().nextInt( 1 + durabilityLevel ) > 0) {
						damage = 0;
					}
				}
				
				if (damage > 0) {
					
//				Compatibility compat = SpigotPrison.getInstance().getCompatibility();
//				int maxDurability = compat.getDurabilityMax( itemInHand );
//				int durability = compat.getDurability( itemInHand );
					int newDurability = durability + damage;
					
					if (newDurability > maxDurability) {
						// Item breaks! ;(
						compat.breakItemInMainHand( player );
						toolBreak = true;
					} else {
						compat.setDurability( itemInHand, newDurability );
					}
					player.updateInventory();
				}
			}
			
			
			if ( Output.get().isDebug( DebugTarget.durability ) ) {

				String message = String.format( "calculateAndApplyDurability: %s:  maxDurability= %d  " + 
						"durability: %d  damage: %d  durResistance: %d  toolDurabilityLvl: %d  %s", 
						itemInHand.getName(), maxDurability, durability, damage, 
						durabilityResistance, durabilityLevel, 
						(toolBreak ? "[Broke]" : "") );
				
				Output.get().logDebug( DebugTarget.durability, message );
			}
			
		}
	}



	/**
	 * <p>This adds a lore counter to the tool if it is enabled.
	 * </p>
	 * 
	 * @param itemInHand
	 * @param itemLore
	 * @param blocks
	 */
	protected void itemLoreCounter( SpigotItemStack itemInHand, String itemLore, int blocks) {

		// A block was broke... so record that event on the tool:	
		if ( itemInHand != null && isBoolean( AutoFeatures.loreTrackBlockBreakCount ) ) {
			
			if (itemInHand.getBukkitStack().hasItemMeta()) {
				
				List<String> lore = new ArrayList<>();
				itemLore = itemLore.trim() + " ";
				itemLore = Text.translateAmpColorCodes( itemLore.trim() + " ");
				ItemMeta meta = itemInHand.getBukkitStack().getItemMeta();
				
//			String prisonBlockBroken = itemLore.getLore();
				
				
				if (meta.hasLore()) {
					lore = meta.getLore();
					boolean found = false;
					
					for( int i = 0; i < lore.size(); i++ ) {
						if ( lore.get( i ).startsWith( itemLore ) ) {
							String val = lore.get( i ).replace( itemLore, "" ).trim();
							int count = blocks;
							
							try {
								count += Integer.parseInt(val);
							} catch (NumberFormatException e1) {
								Output.get().logError("AutoManager: tool counter failure. lore= [" + lore.get(i) + "] val= [" + val + "] error: " + e1.getMessage());								}
							
							lore.set(i, itemLore + count);
							found = true;
							
							break;
						}
					}
					
					if ( !found ) {
						lore.add(itemLore + 1);
					}
					
				} else {
					lore.add(itemLore + 1);
				}
				
				meta.setLore(lore);
				itemInHand.getBukkitStack().setItemMeta(meta);
				
				// incrementCounterInName( itemInHand, meta );
				
			}
		}
		
	}

//
//
//	/**
//	 * <p>Checks to see if mcMMO is able to be enabled, and if it is, then call it's registered
//	 * function that will do it's processing before prison will process the blocks.
//	 * </p>
//	 * 
//	 * <p>This adds mcMMO support within mines for herbalism, mining, woodcutting, and excavation.
//	 * </p>
//	 * 
//	 * @param e
//	 */
//	private void registerMCMMO() {
//		
//		if ( !isMCMMOChecked ) {
//			
//	    	boolean isProcessMcMMOBlockBreakEvents = isBoolean( AutoFeatures.isProcessMcMMOBlockBreakEvents );
//
//			if ( isProcessMcMMOBlockBreakEvents ) {
//				
//				for ( RegisteredListener rListener : BlockBreakEvent.getHandlerList().getRegisteredListeners() ) {
//					
//					if ( rListener.getPlugin().isEnabled() && 
//							rListener.getPlugin().getName().equalsIgnoreCase( "mcMMO" ) ) {
//						
//						registeredListenerMCMMO = rListener;
//					}
//				}
//				
//			}
//			
//			isMCMMOChecked = true;
//		}
//	}
//	
//	private void checkMCMMO( Player player, Block block ) {
//		if ( registeredListenerMCMMO != null ) {
//			BlockBreakEvent bEvent = new BlockBreakEvent( block, player );
//			checkMCMMO( bEvent );
//		}
//	}
//	
//	private void checkMCMMO( BlockBreakEvent e ) {
//		if ( registeredListenerMCMMO != null ) {
//			
//			try {
//				registeredListenerMCMMO.callEvent( e );
//			}
//			catch ( EventException e1 ) {
//				e1.printStackTrace();
//			}
//		}
//	}
//	
//	
//	
//	/**
//	 * <p>Checks to see if mcMMO is able to be enabled, and if it is, then call it's registered
//	 * function that will do it's processing before prison will process the blocks.
//	 * </p>
//	 * 
//	 * <p>This adds mcMMO support within mines for herbalism, mining, woodcutting, and excavation.
//	 * </p>
//	 * 
//	 * @param e
//	 */
//	private void registerEZBlock() {
//		
//		if ( !isEZBlockChecked ) {
//			
//	    	boolean isProcessMcMMOBlockBreakEvents = isBoolean( AutoFeatures.isProcessEZBlocksBlockBreakEvents );
//
//			if ( isProcessMcMMOBlockBreakEvents ) {
//				
//				for ( RegisteredListener rListener : BlockBreakEvent.getHandlerList().getRegisteredListeners() ) {
//					
//					if ( rListener.getPlugin().isEnabled() && 
//							rListener.getPlugin().getName().equalsIgnoreCase( "EZBlocks" ) ) {
//						
//						registeredListenerEZBlock = rListener;
//					}
//				}
//				
//			}
//			
//			isEZBlockChecked = true;
//		}
//	}
//	
//	private void checkEZBlock( Player player, Block block ) {
//		if ( registeredListenerEZBlock != null ) {
//			BlockBreakEvent bEvent = new BlockBreakEvent( block, player );
//			checkEZBlock( bEvent );
//		}
//	}
//	
//	private void checkEZBlock( BlockBreakEvent e ) {
//		if ( registeredListenerEZBlock != null ) {
//			
//			try {
//				registeredListenerEZBlock.callEvent( e );
//			}
//			catch ( EventException e1 ) {
//				e1.printStackTrace();
//			}
//		}
//	}
//	

	
	
	private int checkCrazyEnchant( Player player, Block block, ItemStack item ) {
		int bonusXp = 0;
		
		try {
			if ( isCrazyEnchantEnabled() == null ) {
				Class.forName( 
						"tech.mcprison.prison.spigot.integrations.IntegrationCrazyEnchantmentsPickaxes", false, 
						this.getClass().getClassLoader() );
				setCrazyEnchantEnabled( Boolean.TRUE );
			}
			
			if ( isCrazyEnchantEnabled() != null && isCrazyEnchantEnabled().booleanValue() && 
					item != null && IntegrationCrazyEnchantmentsPickaxes.getInstance().isEnabled() ) {
				
				bonusXp = IntegrationCrazyEnchantmentsPickaxes.getInstance()
						.getPickaxeEnchantmentExperienceBonus( player, block, item );
			}
		}
		catch ( NoClassDefFoundError | Exception e ) {
			setCrazyEnchantEnabled( Boolean.FALSE );
		}
		
		return bonusXp;
	}
	
	
	private Mine findMineLocation( SpigotBlock block ) {
		return getPrisonMineManager() == null ? 
				null : getPrisonMineManager().findMineLocationExact( block.getLocation() );
	}
	

	private TreeMap<Long, Mine> getPlayerCache() {
		return getPrisonMineManager() == null ? 
				new TreeMap<Long, Mine>() :
				getPrisonMineManager().getPlayerCache();
	}

	private PrisonMines getPrisonMineManager() {
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

	
	@SuppressWarnings( "unused" )
	private synchronized String incrementUses(Long elapsedNano) {
		String message = null;
		usesElapsedTimeNano += elapsedNano;
		
		if ( ++uses >= 100 ) {
			double avgNano = usesElapsedTimeNano / uses;
			double avgMs = avgNano / 1000000;
			message = String.format( "OnBlockBreak: count= %s avgNano= %s avgMs= %s ", 
					Integer.toString(uses), Double.toString(avgNano), Double.toString(avgMs) );
			
			uses = 0;
			usesElapsedTimeNano = 0L;
		}
		return message;
	}

	private boolean isTeExplosionTriggerEnabled() {
		return teExplosionTriggerEnabled;
	}

	private void setTeExplosionTriggerEnabled( boolean teExplosionTriggerEnabled ) {
		this.teExplosionTriggerEnabled = teExplosionTriggerEnabled;
	}

	public Random getRandom() {
		return random;
	}

	public Boolean isCrazyEnchantEnabled() {
		return crazyEnchantEnabled;
	}
	public void setCrazyEnchantEnabled( Boolean crazyEnchantEnabled ) {
		this.crazyEnchantEnabled = crazyEnchantEnabled;
	}


}
