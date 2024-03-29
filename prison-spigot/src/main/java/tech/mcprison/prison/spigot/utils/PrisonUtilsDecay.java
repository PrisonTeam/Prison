package tech.mcprison.prison.spigot.utils;

import java.util.ArrayList;
import java.util.List;

import com.cryptomorin.xseries.XMaterial;

import tech.mcprison.prison.commands.Arg;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.internal.block.PrisonBlock;
import tech.mcprison.prison.mines.data.Mine;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.api.PrisonSpigotAPI;
import tech.mcprison.prison.spigot.utils.tasks.DecayBlocksTask;
import tech.mcprison.prison.spigot.utils.tasks.DecayObbyTask;
import tech.mcprison.prison.spigot.utils.tasks.DecayRainbowTask;

public class PrisonUtilsDecay
	extends PrisonUtils
{
	
	private boolean enableDecayObby = false;
	private boolean enableDecayRainbow = false;
	private boolean enableDecayBlocks = false;
	
	/**
	 * <p>There is no initialization needed for these commands.
	 * <p>
	 * 
	 * <p>This function must return a value of true to indicate that this 
	 * set of commands are enabled.  If it is set to false, then these
	 * commands will not be registered when prison is loaded.
	 * </p>
	 * 
	 * @return
	 */
	@Override
	protected Boolean initialize()
	{
		return true;
	}
	
	
	/**
	 * 
	 * 
	 * // submit decay task... 
			// 1) Set block to unbreakable
			// 2) set sourceBlock to obby
			// 3) submit delayed task
			// 4) task then sets block to targetBlock
			// 5) Remove block from unbreakable
	 * 
	 * @param sender
	 * @param blockCoordinates
	 * @param blockTargetName
	 * @param decayTimeTicks
	 * @param mineName
	 */
	@Command(identifier = "prison utils decay obby", 
			description = "Decays a block from obby to the target blocks",
		onlyPlayers = false, 
		permissions = "prison.utils.decay.obby")
	public void utilDecayObby(CommandSender sender, 
			@Arg(name = "blockCoordinates", 
				description = "Block coordinates represents the source block and location. Format:" +
						"'blockName::(worldName,x,y,z)'. Use placeholder '{blockCoordinates}'") 
						String blockCoordinates,
			@Arg(name = "blockTargetName", description = "Target block type after decaying. " +
						"Use '/mines block search help' to find correct block name.") 
						String blockTargetName,
			@Arg(name = "decayTimeTicks", description = "The length of time the decay lasts, in ticks. " +
					"Must be at least 4 ticks, and no more than 5 minutes (6000 ticks).")
						long decayTimeTicks,
			@Arg(name = "mineName", def = "",
					description = "Optional, but very helpful. The mine " +
					"where the block originated from.  If it was outside of a mine, then " +
					"this should be omitted.") 
						String mineName
		
		 ) {
	
		if ( !isEnableDecayObby() ) {
			
			Output.get().logInfo( "Prison's utils command decayObby is disabled in modules.yml." );
		}
		else {
	
			PrisonBlock sourceBlock = PrisonBlock.fromBlockCoordinates( blockCoordinates );
			
			if ( sourceBlock == null || sourceBlock.getLocation() == null ) {
				Output.get().logInfo( "Prison utils decay obby: blockCoordinates requires a value that " +
						"includes a block name and coordinates: 'blockName::(worldName,x,y,z)' Was: [%s]",
						blockCoordinates );
				return;
			}
			
			PrisonBlock targetBlock = PrisonBlock.fromBlockName( blockTargetName );
			
			if ( targetBlock == null ) {
				Output.get().logInfo( "Prison utils decay obby: blockTargetName is not a valid " +
						"block name. Was: [%s]",
						blockTargetName );
				return;
			}
			
			if ( decayTimeTicks < 4 ) {
				decayTimeTicks = 4;
			}
			else if ( decayTimeTicks > 6000 ) {
				decayTimeTicks = 6000;
			}
			
			// Mine is used to better track these blocks:
			PrisonSpigotAPI spigotApi = new PrisonSpigotAPI();
			
			Mine mine = spigotApi.findMineLocation( sourceBlock );
			
			UnbreakableBlockData data = BlockUtils.getInstance().addUnbreakable( sourceBlock.getLocation(), sourceBlock, mine );
			data.setTargetBlock( targetBlock );
			data.setDecayTimeTicks( decayTimeTicks );
			
			// submit decay task... 
			// 1) Set block to unbreadkable
			// 2) set sourceBlock to obby
			// 3) submit delayed task
			// 4) task then sets block to targetBlock
			// 5) Remove block from unbreakable
			
			// If mine resets, then remove all unbreakable blocks that are within the mine. 
			DecayObbyTask decayTask = new DecayObbyTask( XMaterial.OBSIDIAN, data );
			
			decayTask.submit();
		}
	}

	
	/**
	 * 
	 * 
	 * // submit decay task... 
			// 1) Set block to unbreakable
			// 2) set sourceBlock to the rainbow blocks
			// 3) submit delayed task
			// 4) task then sets block to targetBlock
			// 5) Remove block from unbreakable
 	 *
	 * @param sender
	 * @param blockCoordinates
	 * @param blockTargetName
	 * @param decayTimeTicks
	 * @param mineName
	 */
	@Command(identifier = "prison utils decay rainbow", 
			description = "Decays a block from rainbow to the target blocks.  The rainbow blocks consist of" +
					"wool cycling from block, purple, blue, green, yellow, orange, red, pink, white, " +
					"and then finally diamond block.",
		onlyPlayers = false, 
		permissions = "prison.utils.decay.rainbow" )
	public void utilDecayRainbow(CommandSender sender, 
			@Arg(name = "blockCoordinates", 
				description = "Block coordinates represents the source block and location. Format:" +
						"'blockName::(worldName,x,y,z)'. Use placeholder '{blockCoordinates}'") 
						String blockCoordinates,
			@Arg(name = "blockTargetName", description = "Target block type after decaying. " +
						"Use '/mines block search help' to find correct block name.") 
						String blockTargetName,
			@Arg(name = "decayTimeTicks", description = "The length of time the decay lasts, in ticks. " +
					"Must be at least 10 ticks, and no more than 5 minutes (6000 ticks). Each rainbow " +
					"block will be active for 1/10th the lenght specified with decayTimeTicks so you " +
					"should use multiples of 10.")
						long decayTimeTicks,
			@Arg(name = "mineName", def = "",
					description = "Optional, but very helpful. The mine " +
					"where the block originated from.  If it was outside of a mine, then " +
					"this should be omitted.") 
						String mineName
		
		 ) {
	
		if ( !isEnableDecayRainbow() ) {
			
			Output.get().logInfo( "Prison's utils command decayObby is disabled in modules.yml." );
		}
		else {
	
			PrisonBlock sourceBlock = PrisonBlock.fromBlockCoordinates( blockCoordinates );
			
			if ( sourceBlock == null || sourceBlock.getLocation() == null ) {
				Output.get().logInfo( "Prison utils decay rainbow: blockCoordinates requires a value that " +
						"includes a block name and coordinates: 'blockName::(worldName,x,y,z)' Was: [%s]",
						blockCoordinates );
				return;
			}
			
			PrisonBlock targetBlock = PrisonBlock.fromBlockName( blockTargetName );
			
			if ( targetBlock == null ) {
				Output.get().logInfo( "Prison utils decay rainbow: blockTargetName is not a valid " +
						"block name. Was: [%s]",
						blockTargetName );
				return;
			}
			
			if ( decayTimeTicks < 10 ) {
				decayTimeTicks = 10;
			}
			else if ( decayTimeTicks > 6000 ) {
				decayTimeTicks = 6000;
			}
			
			// Mine is used to better track these blocks:
			PrisonSpigotAPI spigotApi = new PrisonSpigotAPI();
			
			Mine mine = spigotApi.findMineLocation( sourceBlock );
			
			UnbreakableBlockData data = BlockUtils.getInstance().addUnbreakable( sourceBlock.getLocation(), sourceBlock, mine );
			data.setTargetBlock( targetBlock );
			data.setDecayTimeTicks( decayTimeTicks );
			
			DecayRainbowTask decayTask = new DecayRainbowTask( data );
			
			decayTask.submit();
		}
	}

	/**
	 * This command will use the specified list of blocks to perform the decay effect.
	 * 
	 * // submit decay task... 
			// 1) Set block to unbreakable
			// 2) set sourceBlock to the specified list of decayBlocks
			// 3) submit delayed task
			// 4) task then sets block to targetBlock
			// 5) Remove block from unbreakable
	 * 
	 * @param sender
	 * @param blockCoordinates
	 * @param blockTargetName
	 * @param decayBlocks
	 * @param decayTimeTicks
	 * @param mineName
	 */
	@Command(identifier = "prison utils decay blocks", 
			description = "Decays a block using the list of decay blockNames to the target blocks. " +
					"One or more block names can be specified using a pipe | and no spaces.",
		onlyPlayers = false, 
		permissions = "prison.utils.decay.blocks")
	public void utilDecayBlocks(CommandSender sender, 
			@Arg(name = "blockCoordinates", 
				description = "Block coordinates represents the source block and location. Format:" +
						"'blockName::(worldName,x,y,z)'. Use placeholder '{blockCoordinates}'") 
						String blockCoordinates,
			@Arg(name = "blockTargetName", description = "Target block type after decaying. " +
						"Use '/mines block search help' to find correct block name.") 
						String blockTargetName,
			@Arg(name = "decayBlocks", description = "Decay blocks. One or more blockNames to " +
					"use for the decay effects. Use pipe | to add more blocks with no spaces. Use " +
					"block search for the names. Example: '<block1>,<block2>,<block3>' ") 
						String decayBlocks,
			@Arg(name = "decayTimeTicks", description = "The length of time the decay lasts, in ticks. " +
					"Must be at least 4 ticks, and no more than 5 minutes (6000 ticks).")
						long decayTimeTicks,
			@Arg(name = "mineName", def = "",
					description = "Optional, but very helpful. The mine " +
					"where the block originated from.  If it was outside of a mine, then " +
					"this should be omitted.") 
						String mineName
		
		 ) {
	
		if ( !isEnableDecayBlocks() ) {
			
			Output.get().logInfo( "Prison's utils command decay blocks is disabled in modules.yml." );
		}
		else {
	
			PrisonBlock sourceBlock = PrisonBlock.fromBlockCoordinates( blockCoordinates );
			
			if ( sourceBlock == null || sourceBlock.getLocation() == null ) {
				Output.get().logInfo( "Prison utils decay blocks: the parameter 'blockCoordinates' " +
						"requires a value that includes a block name and coordinates: " +
						"'blockName::(worldName,x,y,z)' Was: [%s]",
						blockCoordinates );
				return;
			}
			
			PrisonBlock targetBlock = PrisonBlock.fromBlockName( blockTargetName );
			
			if ( targetBlock == null ) {
				Output.get().logInfo( "Prison utils decay blocks: the parameter 'blockTargetName' " +
						"is not a valid block name(s). Confirm they are correct with " +
						"'/mines block search help`. " +
						"Was: [%s]",
						blockTargetName );
				return;
			}
			
			if ( decayTimeTicks < 4 ) {
				decayTimeTicks = 4;
			}
			else if ( decayTimeTicks > 6000 ) {
				decayTimeTicks = 6000;
			}
			
			
			List<PrisonBlock> decayBlocksList = extractDecayBlocks( decayBlocks );
			
			if ( decayBlocksList.size() == 0 ) {
				Output.get().logInfo( "Prison utils decay blocks: the parameter 'decayBlocks' does not " +
						"contain any valid block names. Example: '<block1>,<block2>,<block3>'.  Was: [%s]",
						decayBlocks );
				return;
			}
			
			// Mine is used to better track these blocks:
			PrisonSpigotAPI spigotApi = new PrisonSpigotAPI();
			
			Mine mine = spigotApi.findMineLocation( sourceBlock );
			
			UnbreakableBlockData data = BlockUtils.getInstance().addUnbreakable( sourceBlock.getLocation(), sourceBlock, mine );
			data.setTargetBlock( targetBlock );
			data.setDecayTimeTicks( decayTimeTicks );
			
			// submit decay task... 
			// 1) Set block to unbreadkable
			// 2) set sourceBlock to obby
			// 3) submit delayed task
			// 4) task then sets block to targetBlock
			// 5) Remove block from unbreakable
			
			// If mine resets, then remove all unbreakable blocks that are within the mine. 
			DecayBlocksTask decayTask = new DecayBlocksTask( data, decayBlocksList );
			
			decayTask.submit();
		}
	}

	private List<PrisonBlock> extractDecayBlocks( String decayBlocks ) {
		List<PrisonBlock> decayBlocksList = new ArrayList<>();
		
		if ( decayBlocksList != null ) {
			String[] dblocks = decayBlocks.trim().split( "," );
			
			if ( dblocks != null && dblocks.length > 0 ) {
				for ( String blockName : dblocks ) {
					
					PrisonBlock block = PrisonBlock.fromBlockName( blockName );
					if ( block != null ) {
						decayBlocksList.add( block );
					}
				}
			}
		}
		return decayBlocksList;
	}


	public boolean isEnableDecayObby() {
		return enableDecayObby;
	}
	public void setEnableDecayObby( boolean enableDecayObby ) {
		this.enableDecayObby = enableDecayObby;
	}
	
	public boolean isEnableDecayRainbow() {
		return enableDecayRainbow;
	}
	public void setEnableDecayRainbow( boolean enableDecayRainbow ) {
		this.enableDecayRainbow = enableDecayRainbow;
	}

	public boolean isEnableDecayBlocks() {
		return enableDecayBlocks;
	}
	public void setEnableDecayBlocks( boolean enableDecayBlocks ) {
		this.enableDecayBlocks = enableDecayBlocks;
	}

}
