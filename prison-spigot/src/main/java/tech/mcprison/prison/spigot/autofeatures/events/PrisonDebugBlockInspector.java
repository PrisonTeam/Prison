package tech.mcprison.prison.spigot.autofeatures.events;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.EventException;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.RegisteredListener;

import com.cryptomorin.xseries.XMaterial;
import com.google.common.eventbus.Subscribe;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.internal.ItemStack;
import tech.mcprison.prison.internal.block.MineTargetPrisonBlock;
import tech.mcprison.prison.internal.events.player.PrisonPlayerInteractEvent;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.selection.SelectionManager;
import tech.mcprison.prison.spigot.block.OnBlockBreakMines;
import tech.mcprison.prison.spigot.block.SpigotBlock;
import tech.mcprison.prison.spigot.block.SpigotItemStack;
import tech.mcprison.prison.spigot.compat.SpigotCompatibility;
import tech.mcprison.prison.spigot.game.SpigotPlayer;
import tech.mcprison.prison.util.Location;

public class PrisonDebugBlockInspector
//	extends OnBlockBreakMines
{
	private static PrisonDebugBlockInspector instance;
	
	private OnBlockBreakMines obbMines;
	
	private long lastAccess = 0;
//	private boolean active = false;
	
	public enum EventDropsStatus {
		normal,
		canceled,
		notSupported;
	}
	
	private PrisonDebugBlockInspector() {
		super();
		
		obbMines = new OnBlockBreakMines();
		
		init();
	}
	
	public static PrisonDebugBlockInspector getInstance() {
		if ( instance == null ) {
			synchronized ( PrisonDebugBlockInspector.class ) {
				if ( instance == null ) {

						instance = new PrisonDebugBlockInspector();
				
				}
			}
		}
		return instance;
	}

    private void init() {
    	
    	
        Prison.get().getEventBus().register(this);
        
        
    	
//    	// Check to see if the class BlockBreakEvent even exists:
//    	try {
//    		
//    		Output.get().logInfo( "AutoManager: Trying to register PrisonDebugBlockInspector" );
//    		
//
//
//    		
//    		
//    		if ( getBbPriority() != BlockBreakPriority.DISABLED ) {
//    			if ( bbPriority.isComponentCompound() ) {
//    				
//    				for (BlockBreakPriority subBBPriority : bbPriority.getComponentPriorities()) {
//						
//    					createListener( subBBPriority );
//					}
//    			}
//    			else {
//    				
//    				createListener(bbPriority);
//    			}
//    			
//    		}
//    		
//    	}
//    	catch ( Exception e ) {
//    		Output.get().logInfo( "AutoManager: BlockBreakEvent failed to load. [%s]", e.getMessage() );
//    	}
    }

    @Subscribe
    public void onPlayerInteract( PrisonPlayerInteractEvent e ) {
    	
    	// Cool down: run no sooner than every 2 seconds... prevents duplicate runs:
    	if ( lastAccess != 0 && (System.currentTimeMillis() - lastAccess) < 2000 ) {
    		return;
    	}
    	
    	this.lastAccess = System.currentTimeMillis();
    	
        ItemStack ourItem = e.getItemInHand();
        ItemStack toolItem = SelectionManager.SELECTION_TOOL;

        if ( ourItem == null || !ourItem.equals(toolItem) || !Output.get().isDebug() ) {
            return;
        }
        //e.setCanceled(true);

        SpigotPlayer player = (SpigotPlayer) e.getPlayer();
        
        boolean isSneaking = player.isSneaking();
        
        Location location = e.getClicked();
        SpigotBlock sBlock = (SpigotBlock) location.getBlockAt();
        
//        UUID playerUUID = e.getPlayer().getUUID();
//        Mine mine = obbMines.findMine( playerUUID, sBlock,  null, null ); 
        
        // Get the mine, and if in a mine, then get the target block:
    	Mine mine = obbMines.findMine( player.getWrapper(), sBlock, null, null );
    	
    	MineTargetPrisonBlock targetBlock = null;
    	
        if ( mine == null ) {
        	
        	player.sendMessage(
        			String.format(
	        			"&dDebugBlockInfo: &7Not in a mine. &5%s &7%s",
	        			sBlock.getBlockName(), location.toWorldCoordinates()) );
        	
        }
        else {

        	targetBlock = mine.getTargetPrisonBlock( sBlock );
        	
        	if ( targetBlock != null && obbMines.isBlockAMatch( targetBlock, sBlock ) ) {
        		// Match ... PrisonBlockType and blockName was updated in isBlockAMatch():
        	}
        	
//        	// Check if it's a custom block, if it is, then change PrisonBlockType and blockName:
//        	checkForCustomBlock( sBlock, targetBlock );
        	
        	String m1 = String.format(
        					"&dDebugBlockInfo:  &3Mine &7%s  &3Rank: &7%s  " +
			    			"&5%s  &7%s",
			    			mine.getName(),
			    			(mine.getRank() == null ? "---" : mine.getRank().getName()),
			    			sBlock.getBlockName(), 
			    			location.toWorldCoordinates());
        	
        	player.sendMessage( m1 );
        	Output.get().logInfo( m1 );
        	
			// Get the mine's targetBlock:
//			MineTargetPrisonBlock tBlock = mine.getTargetPrisonBlock( sBlock );

        	if ( targetBlock == null ) {
        		player.sendMessage( "Notice: Unable to get a mine's targetBlock. This could imply " +
        				"that the mine was not reset since the server started up, or that the air-block " +
        				"check was not ran yet.  Use `/mine reset " + mine.getName() + "' to reset the " +
        						"target blocks." );
        	}
        	else {
        		
        		
        		String message = String.format( "    &3TargetBlock: &7%s  " +
        				"&3Mined: %s%b  &3Broke: &7%b  &3Counted: &7%b", 
        				targetBlock.getPrisonBlock().getBlockName(),
        				(targetBlock.isMined() ? "&d" : "&2"),
        				targetBlock.isMined(), 
        				targetBlock.isAirBroke(),
        				targetBlock.isCounted()
        				);
        		
        		player.sendMessage( message );
        		Output.get().logInfo( message );
        		
        		String message2 = String.format( "    &3isEdge: &7%b  " +
        				"&3Exploded: %s%b  &3IgnoreAllEvents: &7%b", 
        				
        				targetBlock.isEdge(),
        				(targetBlock.isExploded() ? "&d" : "&2"),
        				targetBlock.isExploded(),
        				targetBlock.isIgnoreAllBlockEvents()
        				);
        		
        		player.sendMessage( message2 );
        		Output.get().logInfo( message2 );
        		
        	}
        }
        
        
        if ( !isSneaking ) {
        	player.sendMessage(
        			String.format(
	        			"  &d(&7Sneak to test BlockBreakEvent with block.&d)"
	        			) );
        }
        
        else {
//        	player.sendMessage(
//        			String.format(
//        					"&dDebugBlockInfo: &7Sneak enabled! Block testing coming soon..."
//        					) );
        	
        	// Debug the block break events:

        	dumpBlockBreakEvent( player, sBlock, targetBlock );
        	
        }
        	
        	
        	
        	
        
//        if (e.getAction() == PrisonPlayerInteractEvent.Action.LEFT_CLICK_BLOCK) {
//            // Set first position
//            Selection sel = Prison.get().getSelectionManager().getSelection(e.getPlayer());
//            sel.setMin(e.getClicked());
//            Prison.get().getSelectionManager().setSelection(e.getPlayer(), sel);
//            e.getPlayer()
//                .sendMessage("&7First position set to &8" + e.getClicked().toBlockCoordinates());
//
//            checkForEvent(e.getPlayer(), sel);
//        } else if (e.getAction() == PrisonPlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
//            // Set second position
//            Selection sel = Prison.get().getSelectionManager().getSelection(e.getPlayer());
//            sel.setMax(e.getClicked());
//            Prison.get().getSelectionManager().setSelection(e.getPlayer(), sel);
//            e.getPlayer()
//                .sendMessage("&7Second position set to &8" + e.getClicked().toBlockCoordinates());
//
//            checkForEvent(e.getPlayer(), sel);
//        }

        // disable active prior to exiting function:
    	//this.active = false;

    }
    
    public void dumpBlockBreakEvent( SpigotPlayer player, SpigotBlock sBlock, MineTargetPrisonBlock targetBlock ) {
    	List<String> output = new ArrayList<>();
    	
    	SpigotBlock checkBlock = sBlock;
    	
//    	
//    	// Get the mine, and if in a mine, then get the target block:
//    	Mine mine = obbMines.findMine( player.getWrapper(), sBlock, null, null );
//    	
//    	MineTargetPrisonBlock targetBlock = null;
//    	if ( mine != null ) {
//    		
//    		targetBlock = mine.getTargetPrisonBlock( sBlock );
//    	}
//    	
//    	
//    	// Check if it's a custom block:
//    	checkForCustomBlock( checkBlock, targetBlock );
//    	
    	
    	// Save the item held in the player's hand, which should be the prison wand:
    	org.bukkit.inventory.ItemStack heldItem = SpigotCompatibility.getInstance().getItemInMainHand( player.getWrapper() );
    	
    	BlockBreakEvent bbe = new BlockBreakEvent( sBlock.getWrapper(), player.getWrapper() );
    	
    	String blockName = checkBlock.getBlockName().toLowerCase();
    	
    	boolean useShovel = blockName.matches( 
    					"^clay$|farmland|grass_block|dirt|gravel|mycelium|" +
    					"podzol|^sand$|^red_sand$|soul_sand|soul_soil|" +
    					"concrete_powder|^snow$|snow_block|powder_snow" );
    	
//    	XMaterial.
    	
    	boolean useAxe = blockName.matches( 
    					"wood$|acacia|birch|jungle|spruce|leaves|crimson|" +
    					"sapling|bamboo|ladder|vine|bed$|fence|chest$|" +
    					"table|bookshelf|jack_o_lantern|^melon$|^pumpkn$|" +
    					"sign|^cocoa$|mushroom_block|note_block|campfire|" +
    					"banner|beehive|loom|barrel|jukebox|composter|" +
    					"daylight_detector"
    					);
    	
//    	boolean isLeaves = blockName.contains( "leaves" );
//    	boolean isWood = blockName.matches( "wood|log|planks|sapling" );
    	
    	
    	SpigotItemStack tool = useShovel ?
    			new SpigotItemStack( XMaterial.DIAMOND_SHOVEL.parseItem() ) :
    			( useAxe ? 
    					new SpigotItemStack( XMaterial.DIAMOND_AXE.parseItem() ) :
    						new SpigotItemStack( XMaterial.DIAMOND_PICKAXE.parseItem()
    					) );
    	
    	
    	// Temporaily put the tool in the player's hand:
    	SpigotCompatibility.getInstance().setItemInMainHand( player.getWrapper(), tool.getBukkitStack() );
    	
    	
    	//String blockName = sBlk.getBlockName();
    	

    	
    	output.add(
    			String.format( "&dBlockBreakEvent Dump: &7%s &3%s", 
    					checkBlock.getBlockName(), 
    					checkBlock.getLocation().toBlockCoordinates()
    					) );

    	output.add(
    			String.format( "  &3Tool Used for drops: &2%s", 
    					tool.getName()
    					) );
    	
    	output.add( "   &3Legend: &7EP&3: Event Priority  &7EC&3: Event Canceled  "
    			+ "&7DC&3: Drops Canceled  &7EB&3: Event Block  &7Ds&3: Drops  "
    			+ "&7ms&3: duration in ms");
    	
    	
    	printEventStatus( bbe, "-initial-", "", checkBlock, targetBlock, tool, output, player, -1 );
    	
    	
    	for ( RegisteredListener listener : bbe.getHandlers().getRegisteredListeners() ) {
    		
    		long start = 0;
    		long stop = 0;
    		
    		try {
//    			boolean isPrison = listener.getPlugin().getName().equalsIgnoreCase( "Prison" );
//    			boolean isSpigotListener = isPrison && listener.getListener() instanceof SpigotListener;
    			
//    			if ( !isSpigotListener ) {
    				
	    			start = System.nanoTime();
    				listener.callEvent( bbe );
    				stop = System.nanoTime();
//    			}
    			
			}
			catch ( EventException e ) {
				output.add(
		    			String.format( "  &cError calling event: &3%s  &2[%s]", 
		    					listener.getPlugin().getName(),
		    					e.getMessage()
		    					) );

			}
    		
    		double durationNano = (stop - start);
    		
    		if ( durationNano > 0 ) {
    			durationNano = durationNano / 1_000_000;
    		}
    		
    		printEventStatus( bbe, 
    				listener.getPlugin().getName(), listener.getPriority().name(), checkBlock, targetBlock, 
    				tool, output, player, durationNano );
    		
    	}
    	
    	
    	// Put the heldItem back in the player's hand, which should be the prison wand:
    	SpigotCompatibility.getInstance().setItemInMainHand( player.getWrapper(), heldItem );
    	
    	for ( String outputLine : output )
		{
			Output.get().logInfo( outputLine );
		}
    }
    
    
//    private void checkForCustomBlock( SpigotBlock checkBlock, MineTargetPrisonBlock targetBlock ) {
//
//    	// USE OnBlockBreakMines.isBlockAMatch() instead of this one...
//    	
//		if ( targetBlock != null && targetBlock.getPrisonBlock().getBlockType() == PrisonBlockType.CustomItems ) {
//			
//			List<CustomBlockIntegration> cbIntegrations = 
//					PrisonAPI.getIntegrationManager().getCustomBlockIntegrations();
//			
//			for ( CustomBlockIntegration customBlock : cbIntegrations )
//			{
//				PrisonBlock ciPBlock = customBlock.getCustomBlock( checkBlock );
//				
//				if ( ciPBlock != null ) {
//					
//					checkBlock.setBlockType( ciPBlock.getBlockType() );
//					checkBlock.setBlockName( ciPBlock.getBlockName() );
//					
//					break;
//				}
//			}
//		}
//    }
    
    private void printEventStatus( BlockBreakEvent bbe,
    		String plugin, String priority, 
    		SpigotBlock sBlock, 
    		MineTargetPrisonBlock targetBlock,
    		SpigotItemStack tool,
    		List<String> output, 
    		SpigotPlayer player, 
    		double durationNano ) {
    	
    	StringBuilder sb = new StringBuilder();
    	StringBuilder sb2 = new StringBuilder();
    	sb.append( "  " );
    	
    	boolean isCanceled = bbe.isCancelled();
    	EventDropsStatus isDropCanceled = isDropCanceled( bbe );
    	String dropStats = "&7" + isDropCanceled.name();
    	if ( isDropCanceled == EventDropsStatus.canceled ) {
    		dropStats = "&4" + isDropCanceled.name();
    	}
    	else if ( isDropCanceled == EventDropsStatus.notSupported ) {
    		dropStats = "&d" + isDropCanceled.name();
    	}
    	
    	// Get a fresh copy of the block to ensure we pickup the latest status:
    	SpigotBlock sBlk = (SpigotBlock) sBlock.getLocation().getBlockAt();

    	List<SpigotItemStack> bukkitDrops = new ArrayList<>();
    	obbMines.collectBukkitDrops( bukkitDrops, targetBlock, tool, sBlk, player );
    	bukkitDrops = obbMines.mergeDrops( bukkitDrops );
    	
    	
    	SpigotBlock eventBlock = SpigotBlock.getSpigotBlock( bbe.getBlock() );
    	String eventBlockName = eventBlock == null ? 
						"&4none" : 
						eventBlock.getBlockNameFormal();
		    	
    	
    	// Build the drops listing:
    	if ( bukkitDrops.size() > 0 ) {
    		
//    		List<ItemStack> drops = sBlk.getDrops( tool );
    		for ( ItemStack itemStack : bukkitDrops )
    		{
//    			SpigotItemStack sis = (SpigotItemStack) itemStack;
    			
    			sb2.append( " &b" ).append( itemStack.getName() );
    			if ( itemStack.getAmount() > 0 ) {
    				sb2.append( "&a(&b" ).append( itemStack.getAmount() ).append( "&a)" );
    			}
    		}
    	}
    	else { 
    		sb2.append( "&4none" );
    	}


    	DecimalFormat dFmt = new DecimalFormat("#,##0.000000");
    	String durationNanoStr = durationNano == -1 ? "---" : dFmt.format( durationNano );
    	
    	
    	String msg = String.format( " &3Plugin: &7%-15s &2EP: &7%-9s  "
    			+ "&2EC: &7%5s  &2DC: &7%s  &aEB: &b%s  &aDs: %s  &ams: &7%s",
    			plugin,
    			( priority == null ? "$dnone" : priority ),
    			( isCanceled ? "&4true " : "false" ),
    			dropStats,
    			eventBlockName,
    			sb2,
    			durationNanoStr
    			);
    	sb.append( msg );
    	
    	
    	output.add( sb.toString() );

    	
//    	sb.setLength( 0 );
    	

//    	String msg2 = String.format( "                           &aEventBlock: &b%s  ",
//    			eventBlock == null ? 
//    					"&4none" : 
//    					eventBlock.getBlockNameFormal() );
//    	sb.append( msg2 );
    	
//    	sb.append( "    &aDrops:" );
//    	if ( bukkitDrops.size() > 0 ) {
//    		
////    		List<ItemStack> drops = sBlk.getDrops( tool );
//    		for ( ItemStack itemStack : bukkitDrops )
//    		{
////    			SpigotItemStack sis = (SpigotItemStack) itemStack;
//    			
//    			sb.append( " &b" ).append( itemStack.getName() );
//    			if ( itemStack.getAmount() > 0 ) {
//    				sb.append( "&a(&b" ).append( itemStack.getAmount() ).append( "&a)" );
//    			}
//    		}
//    	}
//    	else { 
//    		sb.append( "&4none" );
//    	}

//    	if ( sb.length() > 0 ) {
//    		output.add( sb.toString() );
//    		sb.setLength( 0 );
//    	}
    	
    }
    
    private EventDropsStatus isDropCanceled( BlockBreakEvent bbe ) {
    	EventDropsStatus results = EventDropsStatus.normal;
    	
    	try {
			if ( bbe.isDropItems() ) {
				results = EventDropsStatus.canceled;
			}
			else {
				results = EventDropsStatus.normal;
			}
		}
    	catch ( NoSuchMethodError e ) {
    		// ignore.... not supported in this version of spigot:
    		results = EventDropsStatus.notSupported;
    	}
		catch ( Exception e ) {
			// ignore.... not supported in this version of spigot:
			results = EventDropsStatus.notSupported;
		}
    	
    	return results;
    }
}
