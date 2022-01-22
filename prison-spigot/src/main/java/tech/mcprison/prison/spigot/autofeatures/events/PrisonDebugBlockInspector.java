package tech.mcprison.prison.spigot.autofeatures.events;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
	OnBlockBreakMines obbMines;
	
	public PrisonDebugBlockInspector() {
		super();
		
		obbMines = new OnBlockBreakMines();
	}

    public void init() {
        Prison.get().getEventBus().register(this);
    }

    @Subscribe
    public void onPlayerInteract( PrisonPlayerInteractEvent e ) {
        ItemStack ourItem = e.getItemInHand();
        ItemStack toolItem = SelectionManager.SELECTION_TOOL;

        if ( !ourItem.equals(toolItem) || !Output.get().isDebug() ) {
            return;
        }
        //e.setCanceled(true);

        SpigotPlayer player = (SpigotPlayer) e.getPlayer();
        
        boolean isSneaking = player.isSneaking();
        
        Location location = e.getClicked();
        SpigotBlock sBlock = (SpigotBlock) location.getBlockAt();
        
        UUID playerUUID = e.getPlayer().getUUID();
        Mine mine = obbMines.findMine( playerUUID, sBlock,  null, null ); 
        
        if ( mine == null ) {
        	
        	player.sendMessage(
        			String.format(
	        			"&dDebugBlockInfo: &7Not in a mine. &5%s &7%s",
	        			sBlock.getBlockName(), location.toWorldCoordinates()) );
        	
        }
        else {
        	player.sendMessage( 
        			String.format(
        					"&dDebugBlockInfo: &3Mine &7%s. " +
			    			"&5%s &7%s",
			    			mine.getName(), 
			    			sBlock.getBlockName(), 
			    			location.toWorldCoordinates()) );
        	
			// Get the mine's targetBlock:
			MineTargetPrisonBlock tBlock = mine.getTargetPrisonBlock( sBlock );

			
			String message = String.format( "&3TargetBlock: &7%s  " +
					"&3Mined: %s%b  &3Broke: &7%b", 
					tBlock.getPrisonBlock().getBlockName(),
					(tBlock.isMined() ? "&d" : "&2"),
					tBlock.isMined(), 
					tBlock.isAirBroke()
					);
        	
			player.sendMessage( message );
			
			String message2 = String.format( "    &3Counted: &7%b  &3Edge: &7%b  " +
					"&3Exploded: %s%b &3IgnorAllEvents: &7%b", 
					tBlock.isCounted(),
					tBlock.isEdge(),
					(tBlock.isExploded() ? "&d" : "&2"),
					tBlock.isExploded(),
					tBlock.isIgnoreAllBlockEvents()
					);
			
			player.sendMessage( message2 );
        }
        
        
        if ( !isSneaking ) {
        	player.sendMessage(
        			String.format(
	        			"&dDebugBlockInfo: &7Sneak to test BlockBreakEvent with block."
	        			) );
        }
        
        else {
//        	player.sendMessage(
//        			String.format(
//        					"&dDebugBlockInfo: &7Sneak enabled! Block testing coming soon..."
//        					) );
        	
        	// Debug the block break events:
        	
        	dumpBlockBreakEvent( player, sBlock );
        		
        	
        	
        	
        	
        	
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
    }
    
    public void dumpBlockBreakEvent( SpigotPlayer player, SpigotBlock sBlock ) {
    	List<String> output = new ArrayList<>();
    	
    	// Save the item held in the player's hand, which should be the prison wand:
    	org.bukkit.inventory.ItemStack heldItem = SpigotCompatibility.getInstance().getItemInMainHand( player.getWrapper() );
    	
    	BlockBreakEvent bbe = new BlockBreakEvent( sBlock.getWrapper(), player.getWrapper() );
    	
    	String blockName = sBlock.getBlockName().toLowerCase();
    	
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
    	
    	output.add(
    			String.format( "&dBlockBreakEvent Dump: &7%s &3%s", 
    					sBlock.getBlockName(), 
    					sBlock.getLocation().toBlockCoordinates()
    					) );
    	output.add(
    			String.format( "  &3Tool Used for drops: &2%s", 
    					tool.getName()
    					) );
    	
    	
    	printEventStatus( bbe, "-initial-", "", sBlock, tool, output );
    	
    	for ( RegisteredListener listener : bbe.getHandlers().getRegisteredListeners() ) {
    		
    		try {
				listener.callEvent( bbe );
			}
			catch ( EventException e ) {
				output.add(
		    			String.format( "  &cError calling event: &3%s  &2[%s]", 
		    					listener.getPlugin().getName(),
		    					e.getMessage()
		    					) );

			}
    		
    		printEventStatus( bbe, 
    				listener.getPlugin().getName(), listener.getPriority().name(), sBlock, tool, output );
    		
    	}
    	
    	
    	// Put the heldItem back in the player's hand, which should be the prison wand:
    	SpigotCompatibility.getInstance().setItemInMainHand( player.getWrapper(), heldItem );
    	
    	for ( String outputLine : output )
		{
			Output.get().logInfo( outputLine );
		}
    }
    
    private void printEventStatus( BlockBreakEvent bbe,
    		String plugin, String priority, SpigotBlock sBlock,
    		SpigotItemStack tool,
    		List<String> output ) {
    	StringBuilder sb = new StringBuilder();
    	sb.append( "  " );
    	
    	boolean isCanceled = bbe.isCancelled();
    	boolean isDropItems = isDropItems( bbe );
    	
    	// Get a fresh copy of the block to ensure we pickup the latest status:
    	SpigotBlock sBlk = (SpigotBlock) sBlock.getLocation().getBlockAt();
    	
    	
    	List<SpigotItemStack> bukkitDrops = new ArrayList<>();
    	obbMines.collectBukkitDrops( bukkitDrops, null, tool, sBlk );
    	bukkitDrops = obbMines.mergeDrops( bukkitDrops );
    	
    	sb.append( " &3Plugin: &7" ).append( plugin ).append( " " )
    		.append( priority == null ? "" : priority ).append( " " );
    	
    	sb.append( "&3Canceled: " ).append( isCanceled ? "&c" : "&a" )
    		.append( isCanceled ? "true " : "false" );

    	if ( !sBlock.getBlockName().equalsIgnoreCase( sBlk.getBlockName() )) {
    		
    		sb.append( "&a" ).append( sBlk.getBlockName() ).append( " " );
    	}
    	

    	
    	if ( !isDropItems ) {
    		
    		sb.append( "&3No Drops" );
    	}
    	
    	output.add( sb.toString() );
    	
    	if ( isDropItems ) {
    		sb.setLength( 0 );
    		
    		sb.append( "    &3Drops:" );
//    		List<ItemStack> drops = sBlk.getDrops( tool );
    		for ( ItemStack itemStack : bukkitDrops )
    		{
    			sb.append( " &a" ).append( itemStack.getName() );
    			if ( itemStack.getAmount() > 0 ) {
    				sb.append( "&3(&2" ).append( itemStack.getAmount() ).append( "&3)" );
    			}
    		}
    		
    		if ( bukkitDrops.size() > 0 ) {
    			output.add( sb.toString() );
    		}
    	}
    }
    
    private boolean isDropItems( BlockBreakEvent bbe ) {
    	boolean results = true;
    	
    	try {
			results = bbe.isDropItems();
		}
    	catch ( NoSuchMethodError e ) {
    		// ignore.... not supported in this version of spigot:
    	}
		catch ( Exception e ) {
			// ignore.... not supported in this version of spigot:
		}
    	
    	return results;
    }
}
